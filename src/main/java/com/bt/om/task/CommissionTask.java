package com.bt.om.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.bt.om.entity.ProductInfo;
import com.bt.om.service.IProductInfoService;
import com.bt.om.util.DateUtil;
import com.bt.om.util.GsonUtil;
import com.bt.om.util.HttpClientHelper;
import com.bt.om.util.HttpcomponentsUtil;

@Component
public class CommissionTask {
	@Autowired
	private IProductInfoService productInfoService;

	@Value("${commission.get.url}")
	private String commissionUrl;

	// 每隔一段时间获取一次佣金信息
	@Scheduled(cron = "0/10 * * * * ?")
	public void getCommission() {
		//获取前一日的商品数据
		ProductInfo searchProductInfo=new ProductInfo();
		searchProductInfo.setSize(48);
		searchProductInfo.setUpdateTime(DateUtil.getPrevDate(new Date()));
		List<ProductInfo> productInfoList = productInfoService.getList(searchProductInfo);		
		if(productInfoList==null || productInfoList.size()<=0){
			return;
		}

		List<String> plist = new ArrayList<>();
		for (ProductInfo productInfo : productInfoList) {
			plist.add(productInfo.getProductId());
		}

		String productIds = StringUtils.join(plist.toArray(), ",");
		System.out.println(productIds);	

		List<NameValuePair> nvpList = new ArrayList<>();
		nvpList.add(new BasicNameValuePair("num_iids", productIds));
		try {
			String ret = HttpcomponentsUtil.postReq(nvpList, commissionUrl);
			System.out.println(ret);
			ret=ret.replaceAll("null", "\"0.0\"");
			List<String> list = GsonUtil.GsonToList(ret, String.class);
			System.out.println(StringUtils.join(list.toArray(), ","));

			for (int i = 0; i < productInfoList.size(); i++) {
				ProductInfo pi = new ProductInfo();
				pi.setCommission(Float.valueOf(list.get(i)));
				pi.setProductId(productInfoList.get(i).getProductId());
				pi.setUpdateTime(new Date());
				productInfoService.updateCommission(pi);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) throws Exception {
		String[] cfgs = new String[] { "classpath:spring/applicationContext.xml" };
		ApplicationContext ctx = new ClassPathXmlApplicationContext(cfgs);
		((CommissionTask) ctx.getBean("commissionTask")).getCommission();
	}
}
