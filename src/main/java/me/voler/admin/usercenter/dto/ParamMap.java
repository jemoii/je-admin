package me.voler.admin.usercenter.dto;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import me.voler.admin.util.PasswordEncoderUtil;

public final class ParamMap extends HashMap<String, String> {

	private static final long serialVersionUID = -1683712461291256679L;

	private static final String SECRET_KEY = "jemoii.duapp.com";

	private Date timestamp;
	private String sign;

	@Override
	public String put(String key, String value) {
		if (StringUtils.isNotEmpty(value)) {
			return super.put(key, value);
		}
		return null;
	}

	@Override
	public String toString() {
		timestamp = new Date();
		this.put("timestamp", getTimestamp());

		String[] keyArray = this.keySet().toArray(new String[0]);
		Arrays.sort(keyArray);
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < keyArray.length; i++) {
			String value = this.get(keyArray[i]);
			builder.append(keyArray[i]).append("=").append(value);
			if (i != keyArray.length - 1) {
				builder.append("&");
			}
		}
		return builder.toString();
	}

	public HashMap<String, String> toMap() {
		sign = new PasswordEncoderUtil("MD5").encode(this.toString() + SECRET_KEY);
		this.put("sign", sign);
		return this;
	}

	private String getTimestamp() {
		return DateFormatUtils.format(timestamp, "yyyy-MM-dd HH:mm:ss");
	}

}
