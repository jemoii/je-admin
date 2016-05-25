package me.voler.test;

import org.junit.Test;

import com.alibaba.fastjson.JSON;

import me.voler.admin.usercenter.dto.ParamMap;

public class SignTest {

	@Test
	public void testSign() {
		ParamMap paramMap = new ParamMap();
		paramMap.put("key", "jemoii@163.com");
		paramMap.put("value", "123456789");
		paramMap.put("expierTime", null);
		System.err.println(paramMap.toString());
		System.err.println(JSON.toJSONString(paramMap.toMap(), true));
	}

}
