package me.voler.test;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;

import me.voler.admin.usercenter.dto.UserInfo;

public class JsonTest {
	public static void main(String[] args) {
		UserInfo info = new UserInfo();
		System.err.println(toJSONString(info, new SerializerFeature[] { SerializerFeature.WriteMapNullValue,
				SerializerFeature.WriteNullStringAsEmpty }, new SerializerFeature[0]));
	}

	public static String toJSONString(Object obj, SerializerFeature[] enableFeatures,
			SerializerFeature[] disableFeatures) {
		SerializeWriter out = new SerializeWriter();

		try {
			JSONSerializer serializer = new JSONSerializer(out);
			for (SerializerFeature feature : enableFeatures) {
				serializer.config(feature, true);
			}
			for (SerializerFeature feature : disableFeatures) {
				serializer.config(feature, false);
			}

			serializer.write(obj);

			return out.toString();
		} finally {
			out.close();
		}
	}

}
