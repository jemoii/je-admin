package me.voler.admin.usercenter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import me.voler.admin.enumeration.LoginError;
import me.voler.admin.enumeration.UserLevel;
import me.voler.admin.usercenter.dto.UserInfo;
import me.voler.admin.usercenter.service.LoginService;
import me.voler.admin.util.HashUtil;
import me.voler.admin.util.JsonResponseUtil;
import me.voler.admin.util.TicketGeneratorUtil;

public class QRLoginServlet extends HttpServlet {

	private static final long serialVersionUID = 1760542279912669131L;

	/**
	 * 移动端扫描二维码后，提交密码。完成合法性验证后，标记扫码登录成功。
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");

		// String level = request.getParameter("level");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String token = request.getParameter("token");
		// 检查请求参数是否合法
		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password) || StringUtils.isEmpty(token)) {
			response.getWriter().print(JsonResponseUtil.errorResponse(LoginError.SYSTEM_ERROR));
			return;
		}
		UserInfo loginInput = new UserInfo();
		loginInput.setUsername(username);
		loginInput.setPassword(password);

		LoginError loginError = LoginService.login(loginInput);
		if (loginError.getErrCode() < 0) {
			response.getWriter().print(JsonResponseUtil.errorResponse(loginError));
			return;
		}
		if (!LoginService.qrlogin(username, token)) {
			response.getWriter().print(JsonResponseUtil.errorResponse(LoginError.SYSTEM_ERROR));
			return;
		}

		response.getWriter().print(JsonResponseUtil.okResponse(loginError));
	}

	/**
	 * 桌面端轮询，检查扫码登录标记。
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");

		// String level = request.getParameter("level");
		String username = request.getParameter("username");
		String token = request.getParameter("token");
		// 检查请求参数是否合法
		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(token)) {
			response.getWriter().print(JsonResponseUtil.errorResponse(LoginError.SYSTEM_ERROR));
			return;
		}
		UserInfo loginInput = new UserInfo();
		loginInput.setUsername(username);
		// 检查扫码登录是否成功
		if (!LoginService.checkQRLogin(username, token.substring(2, 10))) {
			response.getWriter().print(JsonResponseUtil.errorResponse(LoginError.TIMEOUT_ERROR));
			return;
		}

		HttpSession session = request.getSession();

		UserInfo userInfo = LoginService.getUserInfo(loginInput);
		session.setAttribute("loginname", userInfo.getUsername());
		session.setAttribute("username", LoginService.encryptUsername(userInfo.getUsername()));
		// 管理员身份登录时存在会话属性sentk、midtk
		// 教师身份登录时存在会话属性midtk
		TicketGeneratorUtil generator = new TicketGeneratorUtil(8);
		session.setAttribute("pritk", generator.getNewTicket("pritk"));
		if (userInfo.getLevel() >= UserLevel.TEACHER.getLevel()) {
			session.setAttribute("midtk", generator.getNewTicket("midtk"));
		}
		if (userInfo.getLevel() >= UserLevel.ADMINISTRATOR.getLevel()) {
			session.setAttribute("sentk", generator.getNewTicket("sentk"));
		}
		response.addCookie(new Cookie("jeadmin_user", userInfo.getUsername()));
		response.addCookie(new Cookie("jeadmin_token", HashUtil.randomKey()));

		response.getWriter().print(JsonResponseUtil.okResponse(LoginError.NONE_ERROR));
	}

}
