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
		List<AdMonitorTaskVo> vo = adMonitorTaskMapper.getSubmitDetails(7);
		for (AdMonitorTaskVo list : vo) {
			System.out.println(list.getStatus());
			System.out.println(list.getActivityName());
			System.out.println(list.getCreateTime());
			System.out.println(list.getProvince());
			System.out.println(list.getCity());
			System.out.println(list.getRegion());
			System.out.println(list.getStreet());
			System.out.println(list.getMonitorsStart());
			System.out.println(list.getProblem());
			System.out.println(list.getPic_url1());
			System.out.println(list.getPic_url2());
			System.out.println(list.getPic_url3());
			System.out.println(list.getPic_url4());
		}
	}
}
