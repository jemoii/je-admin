package me.voler.admin.usercenter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import me.voler.admin.usercenter.dto.LoginInfoIDTO;
import me.voler.admin.usercenter.dto.UserInfo;
import me.voler.admin.usercenter.service.LoginService;
import me.voler.admin.util.HttpResponseUtil;
import me.voler.admin.util.TicketGeneratorUtil;

public class QRLoginServlet extends HttpServlet {

	private static final long serialVersionUID = 1760542279912669131L;

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");

		String status = request.getParameter("status");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String token = request.getParameter("token");
		// 检查请求参数是否合法
		if (StringUtils.isEmpty(status) || StringUtils.isEmpty(email) || StringUtils.isEmpty(password)
				|| StringUtils.isEmpty(token)) {
			response.getWriter().print(HttpResponseUtil.errorResponse());
			return;
		}
		//
		LoginInfoIDTO info = new LoginInfoIDTO();
		info.setStatus(status);
		info.setEmail(email);
		info.setPassword(password);
		// 检查邮箱/密码是否一致
		if (!LoginService.login(info)) {
			response.getWriter().print(HttpResponseUtil.errorResponse());
			return;
		}
		// 检查邮箱是否验证
		if (!LoginService.isAuth(email)) {
			response.getWriter().print(HttpResponseUtil.okResponse("邮箱未验证"));
			return;
		}
		LoginService.qrlogin(email, token);

		response.getWriter().print(HttpResponseUtil.okResponse("登录成功"));
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");

		String status = request.getParameter("status");
		String email = request.getParameter("email");
		String token = request.getParameter("token");
		// 检查请求参数是否合法
		if (StringUtils.isEmpty(status) || StringUtils.isEmpty(email) || StringUtils.isEmpty(token)) {
			response.getWriter().print(HttpResponseUtil.errorResponse());
			return;
		}
		// 检查扫码登录是否成功
		if (!LoginService.checkQRLogin(email, token.substring(2, 10))) {
			response.getWriter().print(HttpResponseUtil.errorResponse());
			return;
		}

		LoginInfoIDTO info = new LoginInfoIDTO();
		info.setEmail(email);
		UserInfo userInfo = LoginService.getUserInfo(info);
		HttpSession session = request.getSession();
		session.setAttribute("email", userInfo.getEmail());
		session.setAttribute("username", LoginService.encryptUsername(userInfo.getEmail()));
		// 管理员身份登录时存在会话属性sentk、midtk
		// 教师身份登录时存在会话属性midtk
		TicketGeneratorUtil generator = new TicketGeneratorUtil(8);
		session.setAttribute("pritk", generator.getNewTicket("pritk"));
		if (status.equals("teacher") || status.equals("admin")) {
			session.setAttribute("midtk", generator.getNewTicket("midtk"));
		}
		if (status.equals("admin")) {
			session.setAttribute("sentk", generator.getNewTicket("sentk"));
		}
		response.addCookie(new Cookie("jeadmin_user", userInfo.getEmail()));

		response.getWriter().print(HttpResponseUtil.okResponse("登录成功"));
	}

}
