package me.voler.admin.relation.dto;

import java.io.Serializable;
import java.util.List;

import me.voler.admin.usercenter.dto.UserInfo;

/**
 * 获取用户列表时封装响应参数
 *
 */
public class UserInfoODTO implements Serializable {

	private static final long serialVersionUID = -1563335463734471423L;

	private int total;
	private List<UserInfo> rows;

	public UserInfoODTO() {

	}

	public UserInfoODTO(List<UserInfo> rows) {
		this.total = rows.size();
		this.rows = rows;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<UserInfo> getRows() {
		return rows;
	}

	public void setRows(List<UserInfo> rows) {
		this.rows = rows;
	}

}
