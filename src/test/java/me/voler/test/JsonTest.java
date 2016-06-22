package me.voler.test;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;

import me.voler.admin.usercenter.dto.UserInfo;

public class JsonTest {
	@Test
	public void testJson() throws IOException {
		UserInfo info = new UserInfo();
		System.err.println(toJSONString(info, new SerializerFeature[] { SerializerFeature.WriteMapNullValue,
				SerializerFeature.WriteNullStringAsEmpty }, new SerializerFeature[0]));

		info = new UserInfo();
		info.setNickname("139xx");
		info.setUsername("1393381170@qq.com");
		info.setLevel(3);
		info.setStatus(1);
		info.setTelephone("");
		// System.err.println(JSON.toJSONString(editUserInfo(info)));
		System.err.println(System.getenv("JE_PROP_HOME"));
	}

	public static UserInfo editUserInfo(UserInfo info) {
		String jsonResponse = "";
		try {
			HttpURLConnection conn = (HttpURLConnection) new URL("http://172.16.163.1:8182" + "/jeadmin/space.json")
					.openConnection();
			conn.setRequestMethod("PUT");
			conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			conn.setConnectTimeout(15000);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			DataOutputStream outputStream = new DataOutputStream(conn.getOutputStream());
			outputStream.writeBytes(JSON.toJSONString(info));
			outputStream.flush();
			outputStream.close();

			if (conn.getResponseCode() == 200) {
				InputStreamReader inputStream = new InputStreamReader(conn.getInputStream());
				BufferedReader reader = new BufferedReader(inputStream);
				String line = "";
				while ((line = reader.readLine()) != null) {
					jsonResponse += line;
				}
				inputStream.close();
				conn.disconnect();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return new UserInfo();
		}

		JSONObject response = JSONObject.parseObject(jsonResponse);
		if (response.getBoolean("status")) {
			return response.getObject("obj", UserInfo.class);
		} else {
			return new UserInfo();
		}
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
