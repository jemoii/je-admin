package me.voler.admin.relation;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;

import me.voler.admin.enumeration.LoginError;
import me.voler.admin.relation.service.SpaceService;
import me.voler.admin.usercenter.dto.UserInfo;
import me.voler.admin.util.JsonResponseUtil;

public class SpaceServlet extends HttpServlet {

	private static final long serialVersionUID = -1300471707178169606L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		HttpSession session = request.getSession();
		String username = (String) session.getAttribute("loginname");

		request.setAttribute("info", SpaceService.getUserInfo(username));
		RequestDispatcher view = request.getRequestDispatcher("/views/relation/space.jsp");
		view.forward(request, response);
	}

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

}
