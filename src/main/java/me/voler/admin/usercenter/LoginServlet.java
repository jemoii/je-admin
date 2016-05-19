package me.voler.admin.usercenter;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import me.voler.admin.usercenter.dto.LoginInfoIDTO;
import me.voler.admin.usercenter.dto.UserInfo;
import me.voler.admin.usercenter.service.LoginService;
import me.voler.admin.util.HttpResponseUtil;
import me.voler.admin.util.TicketGeneratorUtil;

public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 4885831796852470125L;

	private static final String QRCODE_CONTENT = "http://duapp.voler.me/jeadmin/qrlogin?status=%s&email=%s&token=%s";

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");

		String status = request.getParameter("status");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		// 检查请求参数是否合法
		if (StringUtils.isEmpty(status) || StringUtils.isEmpty(email) || StringUtils.isEmpty(password)) {
			response.getWriter().print(HttpResponseUtil.errorResponse());
			return;
		}
		//
		LoginInfoIDTO info = new LoginInfoIDTO();
		info.setStatus(status);
		info.setEmail(email);
		info.setPassword(password);
		// 检查邮箱/密码是否一致
		if (!LoginService.login(info)) {
			response.getWriter().print(HttpResponseUtil.errorResponse());
			return;
		}
		HttpSession session = request.getSession();
		// 检查邮箱是否验证
		if (!LoginService.isAuth(email)) {
			response.getWriter().print(HttpResponseUtil.okResponse("邮箱未验证"));
			session.setAttribute("email", email);
			return;
		}

		UserInfo userInfo = LoginService.getUserInfo(info);
		session.setAttribute("email", userInfo.getEmail());
		session.setAttribute("username", LoginService.encryptUsername(userInfo.getEmail()));
		// 管理员身份登录时存在会话属性sentk、midtk
		// 教师身份登录时存在会话属性midtk
		TicketGeneratorUtil generator = new TicketGeneratorUtil(8);
		session.setAttribute("pritk", generator.getNewTicket("pritk"));
		if (status.equals("teacher") || status.equals("admin")) {
			session.setAttribute("midtk", generator.getNewTicket("midtk"));
		}
		if (status.equals("admin")) {
			session.setAttribute("sentk", generator.getNewTicket("sentk"));
		}
		response.addCookie(new Cookie("jeadmin_user", userInfo.getEmail()));

		response.getWriter().print(HttpResponseUtil.okResponse("登录成功"));
	}

	/**
	 * @see com.google.code.kaptcha.servlet.KaptchaServlet
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		String status = request.getParameter("status");
		String email = request.getParameter("email");
		String token = request.getParameter("j");
		// 检查请求参数是否合法
		if (StringUtils.isEmpty(status) || StringUtils.isEmpty(email) || StringUtils.isEmpty(token)) {
			response.getWriter().print(HttpResponseUtil.errorResponse());
			return;
		}

		// Default contents, width and height
		String contents = String.format(QRCODE_CONTENT, status, email, token.substring(2, 10));
		int width = 150;
		int height = 150;

		// Set standard HTTP/1.1 no-cache headers.
		response.setHeader("Cache-Control", "no-store, no-cache");

		// return a jpeg
		response.setContentType("image/jpeg");

		Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

		BitMatrix matrix = null;
		try {
			matrix = new MultiFormatWriter().encode(contents, BarcodeFormat.QR_CODE, width, height, hints);
		} catch (WriterException e) {
			response.getWriter().print(HttpResponseUtil.errorResponse());
			return;
		}

		BufferedImage bi = MatrixToImageWriter.toBufferedImage(matrix);
		ServletOutputStream out = response.getOutputStream();

		// write the data out
		ImageIO.write(bi, "jpg", out);
	}

}
