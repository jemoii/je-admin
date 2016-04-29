package me.voler.admin.util;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;

/**
 * 以Json字符串响应接口请求
 *
 */
public class HttpResponseUtil {
	public static String okResponse(Object obj) {
		return response(true, obj);
	}

	public static String errorResponse() {
		return response(false, "");
	}

	public static String response(boolean status, Object obj) {
		return JSON.toJSONString(new Response(status, obj));
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
