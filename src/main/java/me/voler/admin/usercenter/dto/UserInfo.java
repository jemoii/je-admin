package me.voler.admin.usercenter.dto;

import java.io.Serializable;

import me.voler.admin.util.db.Column;
import me.voler.admin.util.db.Table;
import me.voler.admin.util.db.WhereClause;

@Table("user_info_v3")
public class UserInfo implements Serializable {

	private static final long serialVersionUID = -626674416356084334L;

	@WhereClause
	private String username;
	@WhereClause(priority = 1)
	private String telephone;
	private transient String password;
	@Column("user_level")
	private Integer level;
	@Column("user_status")
	private Integer status;
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

	public Integer getLevel() {
		return level;
	}

	/**
	 * 
	 * @param level
	 * 
	 * @see me.voler.admin.enumeration.UserLevel
	 */
	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getStatus() {
		return status;
	}

	/**
	 * 
	 * @param status
	 * 
	 * @see me.voler.admin.enumeration.UserStatus
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

}
