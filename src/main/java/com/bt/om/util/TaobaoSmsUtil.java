package com.bt.om.util;

import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;

public class TaobaoSmsUtil {
	private static final String Url = "http://gw.api.taobao.com/router/rest";
	private static final String AppKey = "23597119";
	private static final String AppSecret = "7607483df891e2e7f49db33b960a09fd";

	public static void sendSms(String signName, String smsTemplateCode, String name, String mobile) {
		TaobaoClient client = new DefaultTaobaoClient(Url, AppKey, AppSecret);
		AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
		req.setExtend("");
		req.setSmsType("normal");
		req.setSmsFreeSignName(signName);
		req.setSmsParamString("{vcode:'" + name + "'}");
		req.setRecNum(mobile);
		req.setSmsTemplateCode(smsTemplateCode);
		try {
			AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);
			System.out.println(rsp.getBody());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		sendSms("逛鱼返利", "SMS_125955002", "12345", "13732203065");
	}
}
