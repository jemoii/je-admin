package me.voler.admin.usercenter;

import java.io.IOException;

import javax.servlet.ServletException;
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

public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 4885831796852470125L;

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");

		String status = request.getParameter("status");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		// 检查请求参数是否合法
		if (StringUtils.isEmpty(status) || StringUtils.isEmpty(email) || StringUtils.isEmpty(password)) {
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
		HttpSession session = request.getSession();
		// 检查邮箱是否验证
		if (!LoginService.isAuth(email)) {
			response.getWriter().print(HttpResponseUtil.okResponse("邮箱未验证"));
			session.setAttribute("email", email);
			return;
		}

		UserInfo userInfo = LoginService.getUserInfo(info);
		session.setAttribute("email", userInfo.getEmail());
		session.setAttribute("username", encryptUsername(userInfo.getEmail()));
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

		response.getWriter().print(HttpResponseUtil.okResponse("登录成功"));
	}

	private String encryptUsername(String username) {
		char[] chs = username.toCharArray();
		int atIndex = username.indexOf('@');
		for (int i = atIndex / 3; i < atIndex / 3 * 2; i++) {
			chs[i] = '*';
		}
		return new String(chs);
	}

}
