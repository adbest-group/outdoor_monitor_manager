package com.taiyiplus.ottv;

import java.util.List;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.bt.om.entity.vo.AdMonitorTaskVo;
import com.bt.om.mapper.AdMonitorTaskMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/applicationContext.xml")
public class Test {
	@Autowired
	private AdMonitorTaskMapper adMonitorTaskMapper;

	@org.junit.Test
	public void test() {
		List<AdMonitorTaskVo> list = adMonitorTaskMapper.getSubmitDetails(7);
		for (AdMonitorTaskVo vo : list) {
			System.out.println(vo.getFeedbackStatus());
			System.out.println(vo.getFeedbackCreateTime());
		}
	}
}
