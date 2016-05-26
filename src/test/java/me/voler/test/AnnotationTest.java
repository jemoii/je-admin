package me.voler.test;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import me.voler.admin.usercenter.dto.UserInfo;
import me.voler.admin.util.db.DataBaseUtil;

public class AnnotationTest {
	@Test
	public void testAnnotation() {
		UserInfo info = new UserInfo();
		info.setUsername("1393381170@qq.com");
		System.err.println(JSON.toJSONString(new DataBaseUtil().selectUserInfo(info),
				SerializerFeature.WriteMapNullValue, SerializerFeature.PrettyFormat));

	}

}
