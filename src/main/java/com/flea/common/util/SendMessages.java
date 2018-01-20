/**  
* @Package com.flea.common.util
* @Description: TODO
* @author bruce
* @date 2016年8月12日 下午6:20:28
* @version V1.0  
*/ 
package com.flea.common.util;

import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;

/**
 * @author bruce
 * @2016年8月12日 下午6:20:28
 */
public class SendMessages {
	
	private final static String url ="http://gw.api.taobao.com/router/rest";
	private final static String appkey ="23439251";
	private final static String secret ="95ea158b20c27e61d55912ca3bb79ad4";
	
	
	public static void sendMessages(String phone,String content,String template) throws ApiException {
		TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
		AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
		req.setExtend("");
		req.setSmsType("normal");
		req.setSmsFreeSignName("云图书馆");
		req.setSmsParamString(content); 
		req.setRecNum(phone);
		req.setSmsTemplateCode(template);
		AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);
		System.out.println(rsp.getBody());
		
	}

}
