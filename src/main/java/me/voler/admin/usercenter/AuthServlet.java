package me.voler.admin.usercenter;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import me.voler.admin.usercenter.dto.MailAuthentication;
import me.voler.admin.usercenter.service.LoginService;
import me.voler.admin.usercenter.service.RegisterService;
import me.voler.admin.util.HttpResponseUtil;

public class AuthServlet extends HttpServlet {

	private static final long serialVersionUID = -7765091139234234946L;

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");

		HttpSession session = request.getSession();
		String email = (String) session.getAttribute("email");
		if (StringUtils.isEmpty(email)) {
			response.getWriter().print(HttpResponseUtil.errorResponse());
			return;
		}
		// 检查是否重复验证邮箱
		if (LoginService.isAuth(email)) {
			response.getWriter().print(HttpResponseUtil.errorResponse());
			return;
		}
		if (!RegisterService.sendAuthCode(email)) {
			response.getWriter().print(HttpResponseUtil.errorResponse());
			return;
		}
		response.getWriter().print(HttpResponseUtil.okResponse("发送验证邮件成功"));
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		String email = request.getParameter("mail");
		String authCode = request.getParameter("code");
		// 检查请求参数是否合法
		if (StringUtils.isEmpty(email) || StringUtils.isEmpty(authCode)) {
			request.setAttribute("isAuth", false);
		} else {
			MailAuthentication auth = new MailAuthentication();
			auth.setEmail(email);
			auth.setAuthCode(authCode);
			auth.setSentTime(System.currentTimeMillis());
			// 检查邮箱/验证码是否一致
			if (!RegisterService.auth(auth)) {
				request.setAttribute("isAuth", false);
			} else {
				// 更新login_info表中auth属性
				if (!RegisterService.refreshAuth(auth)) {
					request.setAttribute("isAuth", false);
				} else {
					request.setAttribute("isAuth", true);
				}
			}
		}

		RequestDispatcher view = request.getRequestDispatcher("/views/usercenter/authentication.jsp");
		view.forward(request, response);
	}
}
