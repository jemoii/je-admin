package me.voler.admin.enumeration;

public enum LoginError {

	SYSTEM_ERROR(-200, "系统错误，请稍后重试"),

	LEVEL_ERROR(-300, "当前权限下，账号不存在"),

	EMAIL_ERROR(-301, "登录邮箱未验证，请验证邮箱"),

	NOT_EQUAL_ERROR(-400, "邮箱或密码错误"),

	TIMEOUT_ERROR(-400, "扫码登录失败，请稍后重试"),

	NONE_ERROR(0, "登录成功");

	private int errCode;
	private String errMsg;

	public final int getErrCode() {
		return errCode;
	}

	public final String getErrMsg() {
		return errMsg;
	}

	LoginError(int errCode, String errMsg) {
		this.errCode = errCode;
		this.errMsg = errMsg;
	}

}
