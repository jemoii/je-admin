package me.voler.admin.enumeration;

public enum RegisterError {

	SYSTEM_ERROR(-200, "系统错误，请稍后重试"),

	EMAIL_ERROR(-300, "邮箱已注册，请直接登录"),

	NONE_ERROR(0, "登录成功");

	private int errCode;
	private String errMsg;

	public final int getErrCode() {
		return errCode;
	}

	public final String getErrMsg() {
		return errMsg;
	}

	RegisterError(int errCode, String errMsg) {
		this.errCode = errCode;
		this.errMsg = errMsg;
	}

}
