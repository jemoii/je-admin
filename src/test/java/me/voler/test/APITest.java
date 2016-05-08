package me.voler.test;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class APITest extends HttpServlet {

	private static final long serialVersionUID = -3131283097466112058L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		// response.getWriter().print(HttpResponseUtil.okResponse(System.getProperties()));
	}

}
