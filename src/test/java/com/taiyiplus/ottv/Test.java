package com.taiyiplus.ottv;

import java.util.Date;
import java.util.List;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bt.om.entity.AdMedia;
import com.bt.om.entity.vo.AdCrowdVo;
import com.bt.om.entity.vo.AdSeatInfoVo;
import com.bt.om.mapper.AdCrowdMapper;
import com.bt.om.mapper.AdMediaMapper;
import com.bt.om.mapper.AdSeatInfoMapper;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/applicationContext.xml")
public class Test {
	@Autowired
	private AdCrowdMapper adCrowdMapper;
	@Autowired
	private AdActivityAdseatMapper adActivityAdseatMapper;
	
	@org.junit.Test
	public void test() {
		//Integer age[] = new Integer[] { 1, 2, 3, 4, 5, 6, 7 };
		Integer num[] = new Integer[] { 30, 20, 20, 20, 0, 0, 20 };

		AdCrowdVo vo = new AdCrowdVo();
		vo.setSexs(1);
		//vo.setAge(age);
		vo.setNum(num);
		vo.setAdSeatId(1);

		adCrowdMapper.insertAdCrowdVoMale(vo);
	}
	
	@org.junit.Test
	public void testAdMapper() {
		System.out.println(adActivityAdseatMapper.selectByActivityId("167"));
	}
}
