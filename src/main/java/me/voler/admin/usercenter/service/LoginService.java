package me.voler.admin.usercenter.service;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;

import com.alibaba.fastjson.JSONObject;

import me.voler.admin.enumeration.LoginError;
import me.voler.admin.enumeration.UserStatus;
import me.voler.admin.usercenter.dto.ParamMap;
import me.voler.admin.usercenter.dto.UserInfo;
import me.voler.admin.util.MailUtil;
import me.voler.admin.util.PasswordEncoderUtil;
import me.voler.admin.util.TicketGeneratorUtil;
import me.voler.admin.util.db.DataBaseUtil;

public class LoginService {
	private static DataBaseUtil dbUtil = new DataBaseUtil();
	@Deprecated
	private static final String RESET_CONTENT = "你使用的登录邮箱为%s，请在浏览器中打开如下链接："
			+ "http://duapp.voler.me/jeadmin/reset.json?mail=%s&code=%s";
	private static final Logger Log = Logger.getLogger(LoginService.class);

	public static LoginError login(UserInfo loginInput) {
		UserInfo loginOutput = dbUtil.selectUserInfo(loginInput);

		// 系统错误，无法登录
		if (loginOutput == null) {
			return LoginError.SYSTEM_ERROR;
			// 邮箱未注册
		} else if (StringUtils.isEmpty(loginOutput.getUsername())) {
			return LoginError.NOT_EQUAL_ERROR;
			// 账号被封禁
		} else if (loginOutput.getStatus() == UserStatus.DISABLED.getStatus()) {
			return LoginError.NOT_EQUAL_ERROR;
			// 注册与登录时的身份不一致
			// } else if (loginOutput.getLevel().intValue() !=
			// loginInput.getLevel().intValue()) {
			// return LoginError.LEVEL_ERROR;
			// 邮箱未验证
		} else if (loginOutput.getStatus() == UserStatus.INACTIVE.getStatus()) {
			return LoginError.EMAIL_ERROR;
		} else {
			PasswordEncoderUtil encoder = new PasswordEncoderUtil("MD5");
			final String encryptedPassword = encoder.encode(loginInput.getPassword());
			// 密码错误
			if (!encryptedPassword.equals(loginOutput.getPassword())) {
				return LoginError.NOT_EQUAL_ERROR;
			}
			return LoginError.NONE_ERROR;
		}
	}

	public static boolean qrlogin(String username, String token) {
		ParamMap requestBody = new ParamMap();
		requestBody.put("key", username);
		requestBody.put("value", token);
		try {
			String json = Jsoup.connect("http://duapp.voler.me/jeveri/cache/post.json").data(requestBody.toMap())
					.ignoreContentType(true).method(Method.POST).timeout(5000).execute().body();
			JSONObject result = JSONObject.parseObject(json);
			if (!result.getBooleanValue("status")) {
				Log.error(String.format("jeveri cache post error, error: %s", result.getString("obj")));
			}
			return result.getBooleanValue("status");
		} catch (IOException e) {
			Log.error(String.format("jeveri cache post error, error: %s", e.getMessage()));
			return false;
		}
	}

	public static boolean checkQRLogin(String username, String token) {
		try {
			String json = Jsoup.connect("http://duapp.voler.me/jeveri/cache/get.json").data("key", username)
					.ignoreContentType(true).timeout(5000).execute().body();
			JSONObject result = JSONObject.parseObject(json);
			if (!result.getBooleanValue("status")) {
				return false;
			} else {
				return result.getString("obj").equals(token);
			}
		} catch (IOException e) {
			Log.error(String.format("jeveri cache get error, error: %s", e.getMessage()));
			return false;
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

	public static UserInfo getUserInfo(UserInfo loginInput) {
		UserInfo userInfo = dbUtil.selectUserInfo(loginInput);
		if (userInfo == null) {
			return new UserInfo();
		}
		return userInfo;
	}

	public static boolean existUser(String username) {
		UserInfo input = new UserInfo();
		input.setUsername(username);
		UserInfo output = dbUtil.selectUserInfo(input);
		// 系统错误，无法重置密码
		if (output == null) {
			return false;
		}
		//
		if (StringUtils.isEmpty(output.getUsername())) {
			return false;
		}
		// 账号被禁用，无法重置密码
		if (output.getStatus() == UserStatus.DISABLED.getStatus()) {
			return false;
		}
		return true;
	}

	public static boolean verifyEmail(String username) {
		ParamMap requestBody = new ParamMap();
		requestBody.put("businessType", "3");
		requestBody.put("identity", username);
		requestBody.put("callback", "http://duapp.voler.me/jeadmin/reset.json");
		try {
			String json = Jsoup.connect("http://duapp.voler.me/jeveri/delivery.json").data(requestBody.toMap())
					.ignoreContentType(true).method(Method.POST).timeout(5000).execute().body();
			JSONObject result = JSONObject.parseObject(json);
			if (!result.getBooleanValue("status")) {
				Log.error(String.format("jeveri email delivery error, error: %s", result.getString("obj")));
			}
			return result.getBooleanValue("status");
		} catch (IOException e) {
			Log.error(String.format("jeveri email delivery error, error: %s", e.getMessage()));
			return false;
		}
	}

	/**
	 * 重构后验证相关逻辑独立到jeveri项目
	 * 
	 * @param email
	 * @return
	 */
	@Deprecated
	public static boolean sendResetCode(String email) {
		TicketGeneratorUtil generator = new TicketGeneratorUtil(6);
		String resetCode = generator.getNewTicket("reset");
		// dbUtil.insertMailAuth(email, resetCode);
		MailUtil.Email mail = new MailUtil.Email();
		mail.setToAddress(email);
		mail.setSubject("找回登录密码...");
		mail.setContent(String.format(RESET_CONTENT, email, email, resetCode));
		if (!MailUtil.sendEmail(mail)) {
			return false;
		}
		return true;
	}

	public static boolean refreshPassword(String username, String password) {
		UserInfo info = new UserInfo();
		info.setUsername(username);
		PasswordEncoderUtil encoder = new PasswordEncoderUtil("MD5");
		// 使用MD5加密密码
		String encryptedPassword = encoder.encode(password);
		info.setPassword(encryptedPassword);
		if (dbUtil.update(info) >= 0) {
			return true;
		}
		return false;
	}
}
