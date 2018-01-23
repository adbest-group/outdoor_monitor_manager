package test.java.com.bt.om;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.bt.om.entity.OttvOrder;
import com.bt.om.mapper.OttvOrderMapper;
import com.bt.om.util.GsonUtil;
import com.bt.om.vo.web.SearchDataVo;
import com.bt.om.web.util.SearchUtil;

@ContextConfiguration(locations = { "classpath*:/applicationContext-test.xml" })
public class BaseTest extends AbstractJUnit4SpringContextTests {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private OttvOrderMapper ottvOrderMapper;

	@Autowired
	// private IOttvOrderPlanService ottvOrderPlanService;

	public static void main(String[] args) {
		String md5Pwd = new Md5Hash("123456", "yuhao@adbest.com").toString();
		System.out.println(md5Pwd);
		// Map<String, Object> map =
		// GsonUtil.GsonToMaps("{\"1012\":{\"value\":\"1012\",\"name\":\"
		// 浙江\",\"child\":{\"1125\":{\"value\":\"1125\",\"name\":\"
		// 杭州\"}}},\"1996\":{\"value\":\"1996\",\"name\":\"
		// 北京\",\"child\":{\"1002\":{\"value\":\"1002\",\"name\":\"
		// 北京\"}}}}\n");
		String proName = "北京";
		System.out.println("北京".equals(proName));
	}

	public List<String> getAreaNeedAssigned(Integer orderId) {
		List<String> areaList = new ArrayList<String>();

		OttvOrder ottvOrder = ottvOrderMapper.selectByPrimaryKey(orderId);
		String area = ottvOrder.getArea();
		SearchDataVo vo = SearchUtil.getVo();

		if (orderId != null) {
			vo.putSearchParam("id", orderId + "", orderId);
		}
		// List<OttvOrderPlanVo> ottvOrderPlanVoList =
		// ottvOrderPlanService.getOrderPlanListByOrderId(vo);

		if (area != null && area != "") {
			Map<String, Object> map = GsonUtil.GsonToMaps(area);
			for (Map.Entry<String, Object> en : map.entrySet()) {
				try {
					Map<String, Object> map2 = GsonUtil.GsonToMaps(GsonUtil.GsonString(en.getValue()));
					Map<String, String> map3 = GsonUtil.GsonToMaps(GsonUtil.GsonString(map2.get("child")));
					String proName = (String) map.get("name");
					for (Map.Entry<String, String> en2 : map3.entrySet()) {
						areaList.add(
								proName + "-" + (GsonUtil.GsonToMaps(GsonUtil.GsonString(en2.getValue()))).get("name"));
					}
				} catch (Exception e) {

				}
			}
		}
		System.out.println(areaList.toString());
		return areaList;
	}
}
