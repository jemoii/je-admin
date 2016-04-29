package me.voler.admin.usercenter.dto;

import java.io.Serializable;

/**
 * 与数据库表user_info表相对应
 *
 */
public class UserInfo implements Serializable {

	private static final long serialVersionUID = 2485686287382286570L;

	private String userId;
	private String username;
	private String status;
	private String email;
	private String telephone;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

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

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

}
