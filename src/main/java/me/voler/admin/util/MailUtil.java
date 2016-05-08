package me.voler.admin.util;

import java.io.Serializable;
import java.util.Properties;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;

public class MailUtil {
	private static String hostName;
	private static String from;
	private static String userName;
	private static String password;

	static {
		Properties prop = CustomDeployUtil.getResources("mail.properties");
		hostName = prop.getProperty("me.mail.hostname");
		userName = prop.getProperty("me.mail.auth.username");
		password = prop.getProperty("me.mail.auth.password");
		from = prop.getProperty("me.mail.from");
	}

	private static final Logger Log = Logger.getLogger(MailUtil.class);

	public static boolean sendEmail(Email mail) {
		SimpleEmail email = new SimpleEmail();
		email.setHostName(hostName);
		email.setSmtpPort(465);
		email.setAuthenticator(new DefaultAuthenticator(userName, password));
		email.setSSLOnConnect(true);
		email.setCharset("UTF-8");
		email.setSubject(mail.getSubject());
		try {
			email.setFrom(from);

			email.setMsg(mail.getContent());
			email.addTo(mail.getToAddress());
			email.send();
		} catch (EmailException e) {
			Log.error(String.format("发送邮件失败，%s", JSON.toJSONString(mail)));
			return false;
		}
		return true;
	}

	public static class Email implements Serializable {

		private static final long serialVersionUID = 3675547042180894863L;

		private String toAddress;
		private String subject;
		private String content;

		public String getToAddress() {
			return toAddress;
		}

		public void setToAddress(String toAddress) {
			this.toAddress = toAddress;
		}

		public String getSubject() {
			return subject;
		}

		public void setSubject(String subject) {
			this.subject = subject;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}
	}
}
