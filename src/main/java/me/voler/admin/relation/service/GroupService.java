package me.voler.admin.relation.service;

import java.util.ArrayList;

import me.voler.admin.usercenter.dto.UserInfo;
import me.voler.admin.util.db.DataBaseUtil;

public class GroupService {
	private static DataBaseUtil dbUtil = new DataBaseUtil();

	public static ArrayList<UserInfo> getUserInfo(int limitLevel) {
		ArrayList<UserInfo> infoList = dbUtil.selectUserInfoList(limitLevel);
		if (infoList == null) {
			return new ArrayList<UserInfo>();
		}
		return infoList;
	}

	/**
	 * 重构后将待删除的用户标记为封禁状态，不再执行数据库删除操作
	 * 
	 * @param info
	 * @return
	 */
	@Deprecated
	public static boolean deleteUserInfo(UserInfo info) {/*
		// 先删除用户信息，再删除登录信息
		if (dbUtil.deleteUserInfo(info) >= 0) {
			if (dbUtil.deleteLoginInfo(info) >= 0) {
				return true;
			} else {
				dbUtil.insert(info);
				Log.error(String.format("删除用户信息时未能删除登录信息，已回滚，用户信息：%s", JSON.toJSONString(info)));
				return false;
			}
		}*/
		return false;

	}

}
