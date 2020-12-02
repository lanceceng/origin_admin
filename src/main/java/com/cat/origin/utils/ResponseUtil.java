package com.cat.origin.utils;

import com.alibaba.fastjson.JSONObject;
import com.cat.origin.common.result.MsgResult;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResponseUtil {

	public static void responseJson(HttpServletResponse response, int status, Object data) {
		try {
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.setHeader("Access-Control-Allow-Methods", "*");
			response.setContentType("application/json;charset=UTF-8");
			response.setStatus(status);

//			response.getWriter().write(JSONObject.toJSONString(MsgResult.ok()));
			response.getWriter().write(JSONObject.toJSONString(data));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
