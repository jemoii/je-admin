package me.voler.admin.util;

import java.io.Serializable;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;

public class MailUtil {
	private static final String hostName = "smtp.qq.com";
	private static final int smtpPort = 465;
	private static final String from = "";
	private static final boolean sslOnConnect = true;
	private static final String userName = "";
	private static final String password = "";

	private static final Logger Log = Logger.getLogger(MailUtil.class);

	public static boolean sendEmail(Email mail) {
		SimpleEmail email = new SimpleEmail();
		email.setHostName(hostName);
		email.setSmtpPort(smtpPort);
		email.setAuthenticator(new DefaultAuthenticator(userName, password));
		email.setSSLOnConnect(sslOnConnect);
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
