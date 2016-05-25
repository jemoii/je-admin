package me.voler.admin.enumeration;

public enum VerifyError {

	SYSTEM_ERROR(-200, "系统错误，请稍后重试"),

	EMAIL_ERROR(-300, "登录邮箱已验证"),

	// NOT_EQUAL_ERROR(-400, "邮箱验证失败，请稍后重试"),

	NONE_ERROR(0, "请登录注册邮箱，按提示验证邮箱");

	private int errCode;
	private String errMsg;

	public final int getErrCode() {
		return errCode;
	}

	public final String getErrMsg() {
		return errMsg;
	}

	VerifyError(int errCode, String errMsg) {
		this.errCode = errCode;
		this.errMsg = errMsg;
	}

}
