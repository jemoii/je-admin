package me.voler.admin.usercenter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import me.voler.admin.enumeration.ResetError;
import me.voler.admin.usercenter.service.LoginService;
import me.voler.admin.util.JsonResponseUtil;

public class ResettingServlet extends HttpServlet {

	private static final long serialVersionUID = -5969721426204578923L;

	/**
	 * 用户重置密码
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");

		String username = request.getParameter("username");
		String password = request.getParameter("password");
		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
			response.getWriter().print(JsonResponseUtil.errorResponse(ResetError.SYSTEM_ERROR));
			return;
		}
		if (!LoginService.refreshPassword(username, password)) {
			response.getWriter().print(JsonResponseUtil.errorResponse(ResetError.SYSTEM_ERROR));
			return;
		}

		response.getWriter().print(JsonResponseUtil.emptyResponse());
	}

}
