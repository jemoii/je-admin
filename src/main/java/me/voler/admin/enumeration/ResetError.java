package me.voler.admin.enumeration;

public enum ResetError {

	SYSTEM_ERROR(-200, "系统错误，请稍后重试"),

	EMAIL_ERROR(-300, "邮箱未注册，请前往注册"),

	// NOT_EQUAL_ERROR(-400, "邮箱验证失败，请稍后重试"),

	NONE_ERROR(0, "请登录注册邮箱，按提示重置密码");

	private int errCode;
	private String errMsg;

	public final int getErrCode() {
		return errCode;
	}

	public final String getErrMsg() {
		return errMsg;
	}

	ResetError(int errCode, String errMsg) {
		this.errCode = errCode;
		this.errMsg = errMsg;
	}

}
