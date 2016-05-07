package me.voler.admin.filter;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

public class AdministratorFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpSession session = ((HttpServletRequest) request).getSession();
		String stk = (String) session.getAttribute("midtk");
		// 无权限访问页面时重定向至404
		if (StringUtils.isEmpty(stk)) {
			((HttpServletResponse) response).sendRedirect("/jeadmin/404.jsp");
			return;
		}
		if (!Pattern.compile("^midtk-\\d+-\\w{8}$").matcher(stk).matches()) {
			((HttpServletResponse) response).sendRedirect("/jeadmin/404.jsp");
			return;
		}

		chain.doFilter(request, response);

	}

	@Override
	public void destroy() {

	}

}
