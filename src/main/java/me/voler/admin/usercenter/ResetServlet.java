package me.voler.admin.usercenter;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import me.voler.admin.usercenter.dto.MailAuthentication;
import me.voler.admin.usercenter.service.LoginService;
import me.voler.admin.usercenter.service.RegisterService;
import me.voler.admin.util.HttpResponseUtil;

public class ResetServlet extends HttpServlet {

	private static final long serialVersionUID = 2482430318737694900L;

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");

		String email = request.getParameter("email");
		if (StringUtils.isEmpty(email)) {
			response.getWriter().print(HttpResponseUtil.errorResponse());
			return;
		}
		// 检查邮箱是否已注册
		if (!LoginService.existEmail(email)) {
			response.getWriter().print(HttpResponseUtil.errorResponse());
			return;
		}
		if (!LoginService.sendResetCode(email)) {
			response.getWriter().print(HttpResponseUtil.errorResponse());
			return;
		}
		response.getWriter().print(HttpResponseUtil.okResponse("发送邮件成功"));
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		String email = request.getParameter("mail");
		String resetCode = request.getParameter("code");
		// 检查请求参数是否合法
		if (StringUtils.isEmpty(email) || StringUtils.isEmpty(resetCode)) {
			request.setAttribute("canReset", false);
		} else {
			MailAuthentication auth = new MailAuthentication();
			auth.setEmail(email);
			auth.setAuthCode(resetCode);
			auth.setSentTime(System.currentTimeMillis());
			// 检查邮箱/验证码是否一致
			if (!RegisterService.auth(auth)) {
				request.setAttribute("canReset", false);
			} else {
				request.setAttribute("canReset", true);
			}
		}
		request.setAttribute("email", email);

		RequestDispatcher view = request.getRequestDispatcher("/views/usercenter/resetting.jsp");
		view.forward(request, response);
	}

}
