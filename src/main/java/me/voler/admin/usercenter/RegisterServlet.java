package me.voler.admin.usercenter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import me.voler.admin.enumeration.RegisterError;
import me.voler.admin.usercenter.dto.UserInfo;
import me.voler.admin.usercenter.service.RegisterService;
import me.voler.admin.util.JsonResponseUtil;

public class RegisterServlet extends HttpServlet {

	private static final long serialVersionUID = -4047227759527252617L;

	/**
	 * 用户注册
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");

		String level = request.getParameter("level");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		// 检查请求参数是否合法
		if (StringUtils.isEmpty(level) || StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
			response.getWriter().print(JsonResponseUtil.errorResponse(RegisterError.SYSTEM_ERROR));
			return;
		}
		UserInfo registerInput = new UserInfo();
		registerInput.setLevel(Integer.parseInt(level));
		registerInput.setUsername(username);
		registerInput.setPassword(password);

		RegisterError registerError = RegisterService.register(registerInput);
		if (registerError.getErrCode() < 0) {
			response.getWriter().print(JsonResponseUtil.errorResponse(registerError));
			return;
		}
		request.getSession().setAttribute("loginname", registerInput.getUsername());
		response.getWriter().print(JsonResponseUtil.okResponse(registerError));
	}

}
