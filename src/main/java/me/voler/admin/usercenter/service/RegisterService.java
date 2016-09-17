package me.voler.admin.usercenter.service;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;

import com.alibaba.fastjson.JSONObject;

import me.voler.admin.enumeration.RegisterError;
import me.voler.admin.enumeration.UserStatus;
import me.voler.admin.usercenter.dto.ParamMap;
import me.voler.admin.usercenter.dto.UserInfo;
import me.voler.admin.util.MailUtil;
import me.voler.admin.util.PasswordEncoderUtil;
import me.voler.admin.util.TicketGeneratorUtil;
import me.voler.admin.util.db.DataBaseUtil;

public class RegisterService {
	private static DataBaseUtil dbUtil = new DataBaseUtil();
	@Deprecated
	private static final String AUTH_CONTENT = "你使用的注册邮箱为%s，请在浏览器中打开如下链接："
			+ "http://duapp.voler.me/jeadmin/auth.json?mail=%s&code=%s";
	private static final Logger Log = Logger.getLogger(RegisterService.class);

	/**
	 * 重构后不再单独检查是否为重复注册，合并到{@link #register register}
	 * </p>
	 * 
	 * 检查是否为重复注册
	 * 
	 * @param info
	 * @return
	 */
	@Deprecated
	public static boolean isRepeated(UserInfo info) {
		UserInfo registerOutput = dbUtil.selectUserInfo(info);
		// 系统错误，无法注册
		if (registerOutput == null) {
			return true;
			// 邮箱已验证，已注册
		} else {
			return StringUtils.isNotEmpty(registerOutput.getPassword());
		}
	}

	public static RegisterError register(UserInfo registerInput) {
		UserInfo registerOutput = dbUtil.selectUserInfo(registerInput);

		// 系统错误，无法注册
		if (registerOutput == null) {
			return RegisterError.SYSTEM_ERROR;
			// 邮箱已注册
		} else if (StringUtils.isNotEmpty(registerOutput.getUsername())) {
			return RegisterError.EMAIL_ERROR;
		} else {
			PasswordEncoderUtil encoder = new PasswordEncoderUtil("MD5");
			// 使用MD5加密密码
			final String encryptedPassword = encoder.encode(registerInput.getPassword());
			registerInput.setPassword(encryptedPassword);
			if (dbUtil.insert(registerInput) > 0) {
				return RegisterError.NONE_ERROR;
				// 系统错误，无法注册
			} else {
				return RegisterError.SYSTEM_ERROR;
			}
		}
	}

	public static boolean needVerify(String username) {
		UserInfo input = new UserInfo();
		input.setUsername(username);
		UserInfo output = dbUtil.selectUserInfo(input);
		// 系统错误，不需要验证
		return output != null && output.getStatus() == UserStatus.INACTIVE.getStatus();
	}

	public static boolean verifyEmail(String username) {
		ParamMap requestBody = new ParamMap();
		requestBody.put("businessType", "1");
		requestBody.put("identity", username);
		requestBody.put("callback", "http://duapp.voler.me/jeadmin/auth.json");
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

	public static boolean refreshStatus(String username) {
		UserInfo info = new UserInfo();
		info.setUsername(username);
		info.setStatus(UserStatus.NORMAL.getStatus());
		return dbUtil.update(info) >= 0;
	}

	/**
	 * 重构后不再设置userId字段
	 * </p>
	 * 
	 * 由注册邮箱的MD5值得到9位十进制数字表示的用户ID
	 * 
	 * @param seed
	 * @return
	 */
	@Deprecated
	public static String buildID(String seed) {
		StringBuilder builder = new StringBuilder();

		for (int i = 9; builder.length() < 9 && i < seed.length(); i++) {
			builder.append(Integer.valueOf(seed.substring(i, i + 1), 16));
		}

		return builder.substring(0, 9);
	}

	/**
	 * 重构后验证相关逻辑独立到jeveri项目
	 * 
	 * @param email
	 * @return
	 */
	@Deprecated
	public static boolean sendAuthCode(String email) {
		TicketGeneratorUtil generator = new TicketGeneratorUtil(6);
		String authCode = generator.getNewTicket("auth");
		// dbUtil.insertMailAuth(email, authCode);
		MailUtil.Email mail = new MailUtil.Email();
		mail.setToAddress(email);
		mail.setSubject("请验证注册使用的邮箱...");
		mail.setContent(String.format(AUTH_CONTENT, email, email, authCode));
		return MailUtil.sendEmail(mail);
	}

}
