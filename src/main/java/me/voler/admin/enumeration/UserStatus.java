package me.voler.admin.enumeration;

public enum UserStatus {

	/**
	 * 账号被封禁
	 */
	DISABLED(-1),

	/**
	 * 账号待激活，需要验证
	 */
	INACTIVE(0),

	NORMAL(1);

	private int status;

	public final int getStatus() {
		return status;
	}

	UserStatus(int status) {
		this.status = status;
	}

}
