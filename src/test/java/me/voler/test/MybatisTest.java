package me.voler.test;

import java.io.IOException;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import com.alibaba.fastjson.JSON;

import me.voler.admin.mybatis.mapper.UserInfoMapper;

public class MybatisTest {
	@Test
	public void testSelectUser() throws IOException {
		SqlSession session = new SqlSessionFactoryBuilder()
				.build(Resources.getResourceAsStream("mybatis-configuration.xml")).openSession();

		UserInfoMapper mappper = session.getMapper(UserInfoMapper.class);
		System.out.println(JSON.toJSONString(mappper.findUserInfoByEmail("jemoii@163.com")));

		session.commit();
		session.close();
	}
}
