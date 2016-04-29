package me.voler.admin.usercenter.dto;

import java.io.Serializable;

/**
 * 注册时封装请求参数
 *
 */
public class RegisterInfoIDTO implements Serializable {

	private static final long serialVersionUID = -3053126432512620768L;

	private String status;
	private String username;
	private String email;
	private String password;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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
