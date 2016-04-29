package me.voler.admin.usercenter.dto;

import java.io.Serializable;

/**
 * 登录时封装请求参数
 *
 */
public class LoginInfoIDTO implements Serializable {

	private static final long serialVersionUID = -6119516100731706636L;

	private String status;
	private String email;
	private String password;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
