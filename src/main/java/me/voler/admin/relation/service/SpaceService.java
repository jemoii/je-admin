package me.voler.admin.relation.service;

import me.voler.admin.usercenter.dto.UserInfo;
import me.voler.admin.util.DataBaseUtil;

public class SpaceService {
	private static DataBaseUtil dbUtil = new DataBaseUtil();

	public static UserInfo getUserInfo(String email) {
		UserInfo userInfo = dbUtil.selectUserInfo(email);
		if (userInfo == null) {
			return new UserInfo();
		}
		return userInfo;
	}

	public static boolean refreshUserInfo(UserInfo info) {
		if (dbUtil.updateUserInfo(info) >= 0) {
			return true;
		}
		return false;
	}

}
