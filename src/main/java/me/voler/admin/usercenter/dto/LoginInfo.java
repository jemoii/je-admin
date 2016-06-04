package me.voler.admin.usercenter.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import me.voler.admin.util.db.Column;
import me.voler.admin.util.db.Table;

@Table("login_info_v3")
public class LoginInfo implements Serializable {

	private static final long serialVersionUID = 3169270106389430461L;

	private String username;
	@Column(value = "last_logined")
	private Timestamp lastLogined;

	public LoginInfo() {

	}

	public LoginInfo(String username) {
		this.username = username;
		this.lastLogined = new Timestamp(System.currentTimeMillis());
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getLastLoginedDate() {
		return new Date(lastLogined.getTime());
	}

	public Timestamp getLastLogined() {
		return lastLogined;
	}

	public void setLastLogined(Timestamp lastLogined) {
		this.lastLogined = lastLogined;
	}

}
