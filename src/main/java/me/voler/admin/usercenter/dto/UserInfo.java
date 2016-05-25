package me.voler.admin.usercenter.dto;

import java.io.Serializable;

public class UserInfo implements Serializable {

	private static final long serialVersionUID = -626674416356084334L;

	private String username;
	private String telephone;
	private transient String password;
	private int level;
	private int status;
	private String nickname;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getLevel() {
		return level;
	}

	/**
	 * 
	 * @param level
	 * 
	 * @see me.voler.admin.enumeration.UserLevel
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	public int getStatus() {
		return status;
	}

	/**
	 * 
	 * @param status
	 * 
	 * @see me.voler.admin.enumeration.UserStatus
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

}
