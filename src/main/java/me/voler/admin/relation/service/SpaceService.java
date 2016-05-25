package me.voler.admin.relation.service;

import me.voler.admin.enumeration.UserStatus;
import me.voler.admin.usercenter.dto.UserInfo;
import me.voler.admin.util.DataBaseUtil;

public class SpaceService {
	private static DataBaseUtil dbUtil = new DataBaseUtil();

	public static UserInfo getUserInfo(String username) {
		UserInfo spaceInput = new UserInfo();
		spaceInput.setUsername(username);
		UserInfo spaceOutput = dbUtil.selectUserInfo(spaceInput);
		if (spaceOutput == null) {
			return new UserInfo();
		}
		return spaceOutput;
	}

	public static boolean refreshUserInfo(UserInfo info) {
		if (dbUtil.updateUserInfo(info, "nickname", "status") >= 0) {
			return true;
		}
		return false;
	}

	public static boolean deleteUserInfo(UserInfo info) {
		info.setStatus(UserStatus.DISABLED.getStatus());
		if (dbUtil.updateUserInfo(info, "status") >= 0) {
			return true;
		}
		return false;
	}

}
