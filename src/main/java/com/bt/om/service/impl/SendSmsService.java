package com.bt.om.service.impl;

import java.text.MessageFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.bt.om.entity.AdSms;
import com.bt.om.exception.web.SmsException;
import com.bt.om.mapper.AdSmsMapper;
import com.bt.om.service.ISendSmsService;
import com.bt.om.util.ConfigUtil;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;

/**
 * 发送短信 相关业务层
 */
@Service
public class SendSmsService implements ISendSmsService {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	private Integer appId; //腾讯云短信appId
	private String appKey; //腾讯云短信appKey
	
	@Autowired
	private AdSmsMapper	adSmsMapper;
	
	/**
	 * 按内容发送短信
	 * cell：手机号
	 * smsContent：内容
	 * 有异常直接处理, 不抛给上层
	 */
	@Override
	public String sendSms(String cell, String smsContent) {
		SmsSingleSenderResult result = new SmsSingleSenderResult();
		try {
			appId = ConfigUtil.getInt("sms_appid");
			appKey = ConfigUtil.getString("sms_appKey");
			SmsSingleSender sender = new SmsSingleSender(appId, appKey);
			if(!StringUtils.isEmpty(cell) && !StringUtils.isEmpty(smsContent)) {
				//短信发送
				result = sender.send(0, "86", cell, smsContent, "", "123");
				logger.info(MessageFormat.format("短信发送成功, result: ", new Object[] {result}));
				//录入数据库
				AdSms record = new AdSms();
				record.setCell(cell);
				record.setCreateTime(new Date());
				record.setErrmsg(result.errMsg);
				record.setExt(result.ext);
				record.setFee(result.fee);
				record.setMsg(smsContent);
				record.setResult(result.result + "");
				record.setSid(result.sid);
				adSmsMapper.insert(record);
			}
		} catch (Exception e) {
			logger.error(MessageFormat.format("短信发送异常, result: ", new Object[] {result}));
			//throw new SmsException("短信发送异常, result: " + result.toString());
		}
		return result.toString();
	}
}
