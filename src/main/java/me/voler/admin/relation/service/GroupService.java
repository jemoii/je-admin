package me.voler.admin.relation.service;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;

import me.voler.admin.usercenter.dto.UserInfo;
import me.voler.admin.util.DataBaseUtil;

public class GroupService {
	private static DataBaseUtil dbUtil = new DataBaseUtil();

	private static final Logger Log = Logger.getLogger(GroupService.class);

	public static ArrayList<UserInfo> getUserInfo() {
		ArrayList<UserInfo> infoList = dbUtil.selectUserInfoList();
		if (infoList == null) {
			return new ArrayList<UserInfo>();
		}
		return infoList;
	}

	public static boolean deleteUserInfo(UserInfo info) {
		// 先删除用户信息，再删除登录信息
		if (dbUtil.deleteUserInfo(info) >= 0) {
			if (dbUtil.deleteLoginInfo(info) >= 0) {
				return true;
			} else {
				dbUtil.insertUserInfo(info);
				Log.error(String.format("删除用户信息时未能删除登录信息，已回滚，用户信息：%s", JSON.toJSONString(info)));
				return false;
			}
		}
		return false;

	}
}
