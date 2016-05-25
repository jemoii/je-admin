package me.voler.admin.usercenter.dto;

import java.io.Serializable;
import java.util.Date;

public class LoginInfo implements Serializable {

	private static final long serialVersionUID = 3169270106389430461L;

	private String username;
	private int loginCount;
	private Date lastLogined;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getLoginCount() {
		return loginCount;
	}

	public void setLoginCount(int loginCount) {
		this.loginCount = loginCount;
	}

	public Date getLastLogined() {
		return lastLogined;
	}

	public void setLastLogined(Date lastLogined) {
		this.lastLogined = lastLogined;
	}

}
