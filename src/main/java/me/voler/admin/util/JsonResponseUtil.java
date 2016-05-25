package me.voler.admin.util;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * 以Json字符串响应接口请求
 *
 */
public class JsonResponseUtil {

	public static String emptyResponse() {
		return response(true, StringUtils.EMPTY);
	}

	public static String okResponse(Object obj) {
		return response(true, obj);
	}

	public static String errorResponse(Object obj) {
		return response(false, obj);
	}

	public static String response(boolean status, Object obj) {
		return JSON.toJSONString(new Response(status, obj), SerializerFeature.WriteMapNullValue,
				SerializerFeature.WriteNullStringAsEmpty);
	}

	static class Response implements Serializable {

		private static final long serialVersionUID = -6821087022538048320L;

		private boolean status; // “业务”请求是否成功
		private Object obj;

		public Response(boolean status, Object obj) {
			this.status = status;
			this.obj = obj;
		}

		public boolean isStatus() {
			return status;
		}

		public void setStatus(boolean status) {
			this.status = status;
		}

		public Object getObj() {
			return obj;
		}

		public void setObj(Object obj) {
			this.obj = obj;
		}

	}

}
