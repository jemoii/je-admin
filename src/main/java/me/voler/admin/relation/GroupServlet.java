package me.voler.admin.relation;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;

import me.voler.admin.enumeration.LoginError;
import me.voler.admin.enumeration.RegisterError;
import me.voler.admin.enumeration.UserLevel;
import me.voler.admin.relation.dto.UserInfoODTO;
import me.voler.admin.relation.service.GroupService;
import me.voler.admin.relation.service.SpaceService;
import me.voler.admin.usercenter.dto.UserInfo;
import me.voler.admin.usercenter.service.RegisterService;
import me.voler.admin.util.JsonResponseUtil;

public class GroupServlet extends HttpServlet {

	private static final long serialVersionUID = -4482851717527038364L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");

		HttpSession session = request.getSession();
		String sentk = (String) session.getAttribute("sentk");
		int level = UserLevel.TEACHER.getLevel();
		if (StringUtils.isNotEmpty(sentk)) {
			level = UserLevel.ADMINISTRATOR.getLevel();
		}

		ArrayList<UserInfo> infoList = GroupService.getUserInfo(level);

		response.getWriter().print(JSON.toJSONString(new UserInfoODTO(infoList)));
	}

	/**
	 * 由管理员直接添加用户，与用户注册过程一致
	 * 
	 * @see me.voler.admin.usercenter.RegisterServlet#doPost
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

		response.getWriter().print(JsonResponseUtil.okResponse(registerError));
	}

	/**
	 * 由管理员直接修改用户信息，与用户修改信息一致
	 * 
	 * @see me.voler.admin.relation.SpaceServlet#doPut
	 */
	@Override
	public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");

		String body = request.getReader().readLine();
		if (StringUtils.isEmpty(body)) {
			response.getWriter().print(JsonResponseUtil.errorResponse(LoginError.SYSTEM_ERROR));
			return;
		}

		UserInfo requestInfo = JSON.parseObject(body, UserInfo.class);
		// 更新用户信息失败
		if (!SpaceService.refreshUserInfo(requestInfo)) {
			response.getWriter().print(JsonResponseUtil.errorResponse(LoginError.SYSTEM_ERROR));
			return;
		}
		UserInfo responseInfo = SpaceService.getUserInfo(requestInfo.getUsername());
		response.getWriter().print(JsonResponseUtil.okResponse(responseInfo));
	}

	@Override
	public void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");

		String body = request.getReader().readLine();
		if (StringUtils.isEmpty(body)) {
			response.getWriter().print(JsonResponseUtil.errorResponse(LoginError.SYSTEM_ERROR));
			return;
		}

		UserInfo requestInfo = JSON.parseObject(body, UserInfo.class);
		// 删除用户信息失败
		if (!SpaceService.deleteUserInfo(requestInfo)) {
			response.getWriter().print(JsonResponseUtil.errorResponse(LoginError.SYSTEM_ERROR));
			return;
		}
		response.getWriter().print(JsonResponseUtil.emptyResponse());

	}
}
