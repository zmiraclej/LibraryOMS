package com.flea.common.util;

import com.alibaba.fastjson.JSONObject;

public class JsonUtil {
	public static JSONObject createSuccessJson(boolean success){
		JSONObject json = new JSONObject();
		json.put("success",success);
		return json;
	}
	public static JSONObject createSuccessJson(boolean success,Object msg){
		JSONObject json = new JSONObject();
		json.put("success",success);
		json.put("msg", msg);
		return json;
	}
}
