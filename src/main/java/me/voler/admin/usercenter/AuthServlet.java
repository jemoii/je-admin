package me.voler.admin.usercenter;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import me.voler.admin.enumeration.VerifyError;
import me.voler.admin.usercenter.service.RegisterService;
import me.voler.admin.util.JsonResponseUtil;

public class AuthServlet extends HttpServlet {

	private static final long serialVersionUID = -7765091139234234946L;

	/**
	 * 用户请求验证邮箱
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");

		HttpSession session = request.getSession();
		String username = (String) session.getAttribute("loginname");
		if (StringUtils.isEmpty(username)) {
			response.getWriter().print(JsonResponseUtil.errorResponse(VerifyError.SYSTEM_ERROR));
			return;
		}
		// 检查是否重复验证邮箱
		if (!RegisterService.needVerify(username)) {
			response.getWriter().print(JsonResponseUtil.errorResponse(VerifyError.EMAIL_ERROR));
			return;
		}
		// 发送验证邮件
		if (!RegisterService.verifyEmail(username)) {
			response.getWriter().print(JsonResponseUtil.errorResponse(VerifyError.SYSTEM_ERROR));
			return;
		}
		response.getWriter().print(JsonResponseUtil.okResponse(VerifyError.NONE_ERROR));
	}

	/**
	 * 验证邮箱成功，回调
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
			request.setAttribute("isAuth", false);
		} else {
			if (Boolean.parseBoolean(result) && RegisterService.refreshStatus(username)) {
				request.setAttribute("isAuth", true);
			} else {
				request.setAttribute("isAuth", false);
			}
		}

		RequestDispatcher view = request.getRequestDispatcher("/views/usercenter/authentication.jsp");
		view.forward(request, response);
	}
}
