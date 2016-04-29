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

import me.voler.admin.relation.dto.UserInfoODTO;
import me.voler.admin.relation.service.GroupService;
import me.voler.admin.relation.service.SpaceService;
import me.voler.admin.usercenter.dto.RegisterInfoIDTO;
import me.voler.admin.usercenter.dto.UserInfo;
import me.voler.admin.usercenter.service.RegisterService;
import me.voler.admin.util.HttpResponseUtil;

public class GroupServlet extends HttpServlet {

	private static final long serialVersionUID = -4482851717527038364L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");

		ArrayList<UserInfo> infoList = GroupService.getUserInfo();
		// 将用户列表分为学生、教师两部分
		ArrayList<UserInfo> students = new ArrayList<UserInfo>();
		ArrayList<UserInfo> teachers = new ArrayList<UserInfo>();
		for (int i = 0; i < infoList.size(); i++) {
			UserInfo info = infoList.get(i);
			if (info.getStatus().equals("student")) {
				students.add(info);
			} else {
				teachers.add(info);
			}
		}
		HttpSession session = request.getSession();
		if (!StringUtils.isEmpty((String) session.getAttribute("sentk"))) {
			students.addAll(teachers);
		}
		response.getWriter().print(JSON.toJSONString(new UserInfoODTO(students)));
	}

	/**
	 * 由管理员直接添加用户，与用户注册过程一致
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 * @see me.voler.admin.usercenter.RegisterServlet#doPost
	 */
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
		response.getWriter().print(HttpResponseUtil.okResponse("添加成功"));
	}

	/**
	 * 由管理员直接修改用户信息，与用户修改信息一致
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 * @see me.voler.admin.relation.SpaceServlet#doPut
	 */
	@Override
	public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");

		String body = request.getReader().readLine();
		if (StringUtils.isEmpty(body)) {
			response.getWriter().print(HttpResponseUtil.errorResponse());
			return;
		}

		UserInfo requestInfo = JSON.parseObject(body, UserInfo.class);
		// 更新用户信息失败
		if (!SpaceService.refreshUserInfo(requestInfo)) {
			response.getWriter().print(HttpResponseUtil.errorResponse());
			return;
		}
		UserInfo responseInfo = SpaceService.getUserInfo(requestInfo.getEmail());
		response.getWriter().print(HttpResponseUtil.okResponse(responseInfo));
	}

	@Override
	public void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");

		String body = request.getReader().readLine();
		if (StringUtils.isEmpty(body)) {
			response.getWriter().print(HttpResponseUtil.errorResponse());
			return;
		}

		UserInfo requestInfo = JSON.parseObject(body, UserInfo.class);
		// 删除用户信息失败
		if (!GroupService.deleteUserInfo(requestInfo)) {
			response.getWriter().print(HttpResponseUtil.errorResponse());
			return;
		}
		response.getWriter().print(HttpResponseUtil.okResponse(""));

	}
}
