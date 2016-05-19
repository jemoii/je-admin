package me.voler.admin.usercenter.service;

import org.apache.commons.lang.StringUtils;

import me.voler.admin.usercenter.dto.LoginInfo;
import me.voler.admin.usercenter.dto.LoginInfoIDTO;
import me.voler.admin.usercenter.dto.MailAuthentication;
import me.voler.admin.usercenter.dto.UserInfo;
import me.voler.admin.util.DataBaseUtil;
import me.voler.admin.util.MailUtil;
import me.voler.admin.util.PasswordEncoderUtil;
import me.voler.admin.util.TicketGeneratorUtil;

public class LoginService {
	private static DataBaseUtil dbUtil = new DataBaseUtil();

	private static final String RESET_CONTENT = "你使用的登录邮箱为%s，请在浏览器中打开如下链接：http://duapp.voler.me/jeadmin/reset.json?mail=%s&code=%s";

	public static boolean login(LoginInfoIDTO info) {
		LoginInfo loginInfo = dbUtil.selectLoginInfo(info.getEmail());

		// 系统错误，无法登录
		if (loginInfo == null) {
			return false;
			// 邮箱未注册
		} else if (StringUtils.isEmpty(loginInfo.getPassword())) {
			return false;
			// 注册与登录时的身份不一致
		} else if (!loginInfo.getStatus().equals(info.getStatus())) {
			return false;
		} else {
			PasswordEncoderUtil encoder = new PasswordEncoderUtil("MD5");
			final String encryptedPassword = encoder.encode(info.getPassword());
			// 密码错误
			if (!encryptedPassword.equals(loginInfo.getPassword())) {
				return false;
			}
			return true;
		}
	}

	public static void qrlogin(String email, String token) {
		dbUtil.insertMailAuth(email, token);
	}

	public static boolean checkQRLogin(String email, String token) {
		MailAuthentication authentication = dbUtil.selectMailAuth(email);
		// 系统错误，扫码登录失败
		if (authentication == null) {
			return false;
		} else if (StringUtils.isEmpty(authentication.getAuthCode())) {
			return false;
		} else {
			/*
			 * // 超时验证，扫码登录失败 if (auth.getSentTime() -
			 * authentication.getSentTime() > 30 * 60 * 60) { return false; }
			 */
			final String authCode = token;
			// token错误
			if (!authCode.equals(authentication.getAuthCode())) {
				return false;
			}
			return true;
		}
	}

	public static String encryptUsername(String username) {
		char[] chs = username.toCharArray();
		int atIndex = username.indexOf('@');
		for (int i = atIndex / 3; i < atIndex / 3 * 2; i++) {
			chs[i] = '*';
		}
		return new String(chs);
	}

	public static UserInfo getUserInfo(LoginInfoIDTO info) {
		UserInfo userInfo = dbUtil.selectUserInfo(info.getEmail());
		if (userInfo == null) {
			return new UserInfo();
		}
		return userInfo;
	}

	public static boolean isAuth(String email) {
		LoginInfo info = dbUtil.selectLoginInfo(email);
		// 系统错误，无法验证
		if (info == null) {
			return true;
		} else if (StringUtils.isEmpty(info.getPassword())) {
			return true;
		} else {
			return info.isAuth();
		}
	}

	public static boolean existEmail(String email) {
		LoginInfo info = dbUtil.selectLoginInfo(email);
		// 系统错误，无法重置密码
		if (info == null) {
			return false;
		}
		return !StringUtils.isEmpty(info.getPassword());
	}

	public static boolean sendResetCode(String email) {
		TicketGeneratorUtil generator = new TicketGeneratorUtil(6);
		String resetCode = generator.getNewTicket("reset");
		dbUtil.insertMailAuth(email, resetCode);
		MailUtil.Email mail = new MailUtil.Email();
		mail.setToAddress(email);
		mail.setSubject("找回登录密码...");
		mail.setContent(String.format(RESET_CONTENT, email, email, resetCode));
		if (!MailUtil.sendEmail(mail)) {
			return false;
		}
		return true;
	}

	public static boolean refreshPassword(String email, String password) {
		PasswordEncoderUtil encoder = new PasswordEncoderUtil("MD5");
		// 使用MD5加密密码
		String encryptedPassword = encoder.encode(password);
		if (dbUtil.updateLoginInfo(email, encryptedPassword) >= 0) {
			return true;
		}
		return false;
	}
}
