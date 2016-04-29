package me.voler.admin.usercenter.dto;

import java.io.Serializable;

/**
 * 与数据库表mail_auth相对应
 *
 */
public class MailAuthentication implements Serializable {

	private static final long serialVersionUID = 5628043825831813715L;

	private String email;
	private String authCode;
	private long sentTime;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public long getSentTime() {
		return sentTime;
	}

	public void setSentTime(long sentTime) {
		this.sentTime = sentTime;
	}

}
