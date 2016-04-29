package me.voler.admin.usercenter.dto;

import java.io.Serializable;

/**
 * 与数据库表login_info相对应
 *
 */
public class LoginInfo implements Serializable {

	private static final long serialVersionUID = -8650406785888669023L;

	private int id;
	private String status; // 用户身份，student：学生、teacher：教师、admin：管理员
	private String email;
	private String password;
	private boolean auth; // 邮箱是否已验证

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isAuth() {
		return auth;
	}

	public void setAuth(boolean auth) {
		this.auth = auth;
	}

}
