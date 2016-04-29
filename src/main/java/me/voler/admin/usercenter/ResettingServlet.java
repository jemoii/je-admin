package me.voler.admin.usercenter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import me.voler.admin.usercenter.service.LoginService;
import me.voler.admin.util.HttpResponseUtil;

public class ResettingServlet extends HttpServlet {

	private static final long serialVersionUID = -5969721426204578923L;

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");

		String email = request.getParameter("email");
		String password = request.getParameter("password");
		if (StringUtils.isEmpty(email) || StringUtils.isEmpty(password)) {
			response.getWriter().print(HttpResponseUtil.errorResponse());
			return;
		}
		if (!LoginService.refreshPassword(email, password)) {
			response.getWriter().print(HttpResponseUtil.errorResponse());
			return;
		}

		response.getWriter().print(HttpResponseUtil.okResponse("重置密码成功"));
	}

}
