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

import me.voler.admin.enumeration.LoginError;
import me.voler.admin.enumeration.UserLevel;
import me.voler.admin.usercenter.dto.UserInfo;
import me.voler.admin.usercenter.service.LoginService;
import me.voler.admin.util.HashUtil;
import me.voler.admin.util.JsonResponseUtil;
import me.voler.admin.util.TicketGeneratorUtil;

public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 4885831796852470125L;

	private static final String QRCODE_CONTENT = "http://duapp.voler.me/jeadmin/qrlogin?u=%s&tk=%s";

	/**
	 * 用户登录
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");

		// String level = request.getParameter("level");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		// 检查请求参数是否合法
		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
			response.getWriter().print(JsonResponseUtil.errorResponse(LoginError.SYSTEM_ERROR));
			return;
		}
		UserInfo loginInput = new UserInfo();
		loginInput.setUsername(username);
		loginInput.setPassword(password);

		LoginError loginError = LoginService.login(loginInput);
		if (loginError.getErrCode() < 0) {
			if (loginError.getErrCode() == LoginError.EMAIL_ERROR.getErrCode()) {
				request.getSession().setAttribute("loginname", username);
			}
			response.getWriter().print(JsonResponseUtil.errorResponse(loginError));
			return;
		}
		HttpSession session = request.getSession();

		UserInfo userInfo = LoginService.getUserInfo(loginInput);
		session.setAttribute("loginname", userInfo.getUsername());
		session.setAttribute("username", LoginService.encryptUsername(userInfo.getUsername()));
		// 管理员身份登录时存在会话属性sentk、midtk
		// 教师身份登录时存在会话属性midtk
		TicketGeneratorUtil generator = new TicketGeneratorUtil(8);
		session.setAttribute("pritk", generator.getNewTicket("pritk"));
		if (userInfo.getLevel() >= UserLevel.TEACHER.getLevel()) {
			session.setAttribute("midtk", generator.getNewTicket("midtk"));
		}
		if (userInfo.getLevel() >= UserLevel.ADMINISTRATOR.getLevel()) {
			session.setAttribute("sentk", generator.getNewTicket("sentk"));
		}
		response.addCookie(new Cookie("jeadmin_user", userInfo.getUsername()));
		response.addCookie(new Cookie("jeadmin_token", HashUtil.randomKey()));

		response.getWriter().print(JsonResponseUtil.okResponse(loginError));
	}

	/**
	 * 用户请求扫码登录
	 * 
	 * @see com.google.code.kaptcha.servlet.KaptchaServlet
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		// String level = request.getParameter("level");
		String username = request.getParameter("username");
		String token = request.getParameter("j");
		// 检查请求参数是否合法
		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(token)) {
			response.getWriter().print(JsonResponseUtil.errorResponse(LoginError.SYSTEM_ERROR));
			return;
		}

		// Default contents, width and height
		String contents = String.format(QRCODE_CONTENT, username, token.substring(2, 10));
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
			e.printStackTrace();
			response.getWriter().print(JsonResponseUtil.errorResponse(LoginError.SYSTEM_ERROR));
			return;
		}

		BufferedImage bi = MatrixToImageWriter.toBufferedImage(matrix);
		ServletOutputStream out = response.getOutputStream();

		// write the data out
		ImageIO.write(bi, "jpg", out);
	}

}
