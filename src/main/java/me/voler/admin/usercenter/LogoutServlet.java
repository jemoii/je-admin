package me.voler.admin.usercenter;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LogoutServlet extends HttpServlet {

	private static final long serialVersionUID = 6911325983306371516L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		HttpSession session = request.getSession();
		session.removeAttribute("email");
		session.removeAttribute("username");
		session.removeAttribute("pritk");
		session.removeAttribute("midtk");
		session.removeAttribute("sentk");

		RequestDispatcher view = request.getRequestDispatcher("views/usercenter/login.jsp");
		view.forward(request, response);
	}

}