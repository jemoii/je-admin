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

public class LoginFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String fromURI = ((HttpServletRequest) request).getRequestURI();
		HttpSession session = ((HttpServletRequest) request).getSession();
		String stk = (String) session.getAttribute("pritk");
		// 登录成功存在回话属性pritk
		if (StringUtils.isEmpty(stk)) {
			((HttpServletResponse) response).sendRedirect("/duobei/login?from=" + fromURI);
			return;
		}
		if (!Pattern.compile("^pritk-\\d+-\\w{8}$").matcher(stk).matches()) {
			((HttpServletResponse) response).sendRedirect("/duobei/404.jsp");
			return;
		}

		chain.doFilter(request, response);

	}

	@Override
	public void destroy() {

	}

}
