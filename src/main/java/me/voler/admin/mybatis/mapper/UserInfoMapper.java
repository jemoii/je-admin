package me.voler.admin.mybatis.mapper;

import me.voler.admin.usercenter.dto.UserInfo;

public interface UserInfoMapper {

	UserInfo findUserInfoByEmail(String email);

}
