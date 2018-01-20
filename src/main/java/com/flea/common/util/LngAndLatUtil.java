package com.flea.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.alibaba.fastjson.JSONObject;
/**
 * 
 * @author aa
 * 通过地址获取经纬度
 * dateTime 2016-12-28
 *
 */
public class LngAndLatUtil {
	public static String getLngAndLat(String address1) {
		//去除传入的地址所有空格
		String address = address1.replace(" ", "");
		String str = "";
		String url = "http://api.map.baidu.com/geocoder/v2/?address=" + address.trim()
				+ "&output=json&ak=EmWvece0GHuKBjnfjSm2DwOO";
		String json = loadJSON(url);
//		System.out.println(json);
		JSONObject obj = JSONObject.parseObject(json);
		if (obj.get("status").toString().equals("0")) {
			//经纬度保留6位小数
//			String lng = obj.getJSONObject("result").getJSONObject("location").getDouble("lng") + "";
			String lng = new BigDecimal(obj.getJSONObject("result").getJSONObject("location").getDouble("lng")).setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue() + "";
//			String lat = obj.getJSONObject("result").getJSONObject("location").getDouble("lat") + "";
			String lat = new BigDecimal(obj.getJSONObject("result").getJSONObject("location").getDouble("lat")).setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue() + "";
			str = lng +","+ lat;
			// System.out.println("经度："+lng+"---纬度："+lat);
		} else {
			// System.out.println("未找到相匹配的经纬度！");
		}
		return str;
	}

	public static String loadJSON(String url) {
		StringBuilder json = new StringBuilder();
		try {
			URL oracle = new URL(url);
			URLConnection yc = oracle.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					yc.getInputStream()));
			String inputLine = null;
			while ((inputLine = in.readLine()) != null) {
				json.append(inputLine);
			}
			in.close();
		} catch (MalformedURLException e) {
		} catch (IOException e) {
		}
		return json.toString();
	}

}
