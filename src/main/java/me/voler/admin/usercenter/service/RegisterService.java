package me.voler.admin.usercenter.service;

import org.apache.commons.lang.StringUtils;

import me.voler.admin.usercenter.dto.LoginInfo;
import me.voler.admin.usercenter.dto.MailAuthentication;
import me.voler.admin.usercenter.dto.RegisterInfoIDTO;
import me.voler.admin.usercenter.dto.UserInfo;
import me.voler.admin.util.DataBaseUtil;
import me.voler.admin.util.MailUtil;
import me.voler.admin.util.PasswordEncoderUtil;
import me.voler.admin.util.TicketGeneratorUtil;

public class RegisterService {
	private static DataBaseUtil dbUtil = new DataBaseUtil();

	private static final String AUTH_CONTENT = "在浏览器中访问如下链接：http://localhost:8080/jeadmin/auth.json?mail=%s&code=%s";

	/**
	 * 检查是否为重复注册
	 * 
	 * @param info
	 * @return
	 */
	public static boolean isRepeated(RegisterInfoIDTO info) {
		LoginInfo loginInfo = dbUtil.selectLoginInfo(info.getEmail());
		// 系统错误，无法注册
		if (loginInfo == null) {
			return true;
			// 邮箱已验证，已注册
		} else if (!StringUtils.isEmpty(loginInfo.getPassword())) {
			return true;
		} else {
			return false;
		}
	}

	public static void register(RegisterInfoIDTO info) {
		PasswordEncoderUtil encoder = new PasswordEncoderUtil("MD5");
		// 使用MD5加密密码
		String encryptedPassword = encoder.encode(info.getPassword());
		dbUtil.insertLoginInfo(info, encryptedPassword);
		UserInfo userInfo = new UserInfo();
		userInfo.setUserId(buildID(encoder.encode(info.getEmail())));
		userInfo.setUsername(info.getUsername());
		userInfo.setStatus(info.getStatus());
		userInfo.setEmail(info.getEmail());
		dbUtil.insertUserInfo(userInfo);
	}

	/**
	 * 由注册邮箱的MD5值得到9位十进制数字表示的用户ID
	 * 
	 * @param seed
	 * @return
	 */
	private static String buildID(String seed) {
		StringBuilder builder = new StringBuilder();

		for (int i = 9; builder.length() < 9 && i < seed.length(); i++) {
			builder.append(Integer.valueOf(seed.substring(i, i + 1), 16));
		}

		return builder.substring(0, 9);
	}

	public static boolean sendAuthCode(String email) {
		TicketGeneratorUtil generator = new TicketGeneratorUtil(6);
		String authCode = generator.getNewTicket("auth");
		dbUtil.insertMailAuth(email, authCode);
		MailUtil.Email mail = new MailUtil.Email();
		mail.setToAddress(email);
		mail.setSubject("请验证注册使用的邮箱...");
		mail.setContent(String.format(AUTH_CONTENT, email, authCode));
		if (!MailUtil.sendEmail(mail)) {
			return false;
		}
		return true;
	}

	public static boolean auth(MailAuthentication auth) {
		MailAuthentication authentication = dbUtil.selectMailAuth(auth.getEmail());
		// 系统错误，邮箱验证失败
		if (authentication == null) {
			return false;
		} else if (StringUtils.isEmpty(authentication.getAuthCode())) {
			return false;
		} else {
			/*
			 * // 超时验证，邮箱验证失败 if (auth.getSentTime() -
			 * authentication.getSentTime() > 30 * 60 * 60) { return false; }
			 */
			final String authCode = auth.getAuthCode();
			// 验证码错误
			if (!authCode.equals(authentication.getAuthCode())) {
				return false;
			}
			return true;
		}
	}

	public static boolean refreshAuth(MailAuthentication auth) {
		if (dbUtil.updateLoginInfo(auth) >= 0) {
			return true;
		}
		return false;
	}

}
