package me.voler.admin.usercenter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import me.voler.admin.usercenter.dto.RegisterInfoIDTO;
import me.voler.admin.usercenter.service.RegisterService;
import me.voler.admin.util.HttpResponseUtil;

public class RegisterServlet extends HttpServlet {

	private static final long serialVersionUID = -4047227759527252617L;

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");

		String status = request.getParameter("status");
		String username = request.getParameter("username");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		// 检查请求参数是否合法
		if (StringUtils.isEmpty(status) || StringUtils.isEmpty(username) || StringUtils.isEmpty(email)
				|| StringUtils.isEmpty(password)) {
			response.getWriter().print(HttpResponseUtil.errorResponse());
			return;
		}
		RegisterInfoIDTO info = new RegisterInfoIDTO();
		info.setStatus(status);
		info.setUsername(username);
		info.setEmail(email);
		info.setPassword(password);
		// 检查是否为重复注册
		if (RegisterService.isRepeated(info)) {
			response.getWriter().print(HttpResponseUtil.errorResponse());
			return;
		}

		RegisterService.register(info);
		request.getSession().setAttribute("email", email);
		response.getWriter().print(HttpResponseUtil.okResponse(email));
	}

}
