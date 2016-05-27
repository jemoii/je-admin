package me.voler.admin.usercenter;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import me.voler.admin.enumeration.ResetError;
import me.voler.admin.usercenter.service.LoginService;
import me.voler.admin.util.JsonResponseUtil;

public class ResetServlet extends HttpServlet {

	private static final long serialVersionUID = 2482430318737694900L;

	/**
	 * 用户请求重置密码
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");

		String username = request.getParameter("username");
		if (StringUtils.isEmpty(username)) {
			response.getWriter().print(JsonResponseUtil.errorResponse(ResetError.SYSTEM_ERROR));
			return;
		}
		// 检查邮箱是否已注册
		if (!LoginService.existUser(username)) {
			response.getWriter().print(JsonResponseUtil.errorResponse(ResetError.EMAIL_ERROR));
			return;
		}
		if (!LoginService.verifyEmail(username)) {
			response.getWriter().print(JsonResponseUtil.errorResponse(ResetError.SYSTEM_ERROR));
			return;
		}
		response.getWriter().print(JsonResponseUtil.okResponse(ResetError.NONE_ERROR));
	}

	/**
	 * （重置密码）验证邮箱成功，回调
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		String referer = request.getHeader("Referer");

		String username = request.getParameter("id");
		String result = request.getParameter("res");
		// 检查请求参数是否合法
		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(result) || StringUtils.isEmpty(referer)
				|| !referer.contains("jeveri")) {
			request.setAttribute("canReset", false);
		} else {
			request.setAttribute("canReset", Boolean.parseBoolean(result));
		}
		request.setAttribute("loginname", username);

		RequestDispatcher view = request.getRequestDispatcher("/views/usercenter/resetting.jsp");
		view.forward(request, response);
	}

}
