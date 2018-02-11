package com.bt.om.web.component;


import com.bt.om.entity.vo.*;
import com.bt.om.enums.*;
import com.bt.om.security.ShiroUtils;
import com.bt.om.util.ConfigUtil;
import com.bt.om.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.Map.Entry;

/**
 * 
 * 
 * @author hl-tanyong
 * @version $Id: VMComponent.java, v 0.1 2015年9月29日 上午11:33:12 hl-tanyong Exp $
 */
@Component
public class VMComponent {

	protected final Logger logger = LoggerFactory.getLogger(VMComponent.class);


	public String getEvnVariable() {
		return ConfigUtil.getString("is_test_evn", "off");
	}

	@SuppressWarnings("unchecked")
	public List<SysMenuVo> getMenuList() {
		Object menuList = ShiroUtils.getSessionAttribute(SessionKey.SESSION_USER_MENU.toString());
		if (menuList == null) {
			return new ArrayList<SysMenuVo>();
		} else {
			return (List<SysMenuVo>) menuList;
		}
	}

	public String getLoginUserName() {
		Object userObj = ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
		if (userObj != null) {
			SysUserVo userInfo = (SysUserVo) userObj;
			return userInfo.getRealname();
		}
		return "";
	}

	/**
	 * 得到一个删除其中几个参数的URL
	 * 
	 * @param url
	 * @param keys
	 * @return
	 */
	public String getUrlByRemoveKey(String url, String[] keys) {
		String qs = StringUtil.findStr(url, "?", null);
		HashMap<String, String> map = getParams(qs);
		if (map.size() > 0) {
			for (String key : keys) {
				map.remove(key);
			}
			StringBuilder sb = new StringBuilder();
			sb.append(StringUtil.findStr(url, null, "?"));
			if (map.size() > 0) {
				sb.append("?");
				int i = 0;
				for (Entry<String, String> kv : map.entrySet()) {
					if (i > 0) {
						sb.append("&");
					}
					sb.append(kv.getKey());
					sb.append("=");
					sb.append(StringUtil.urlencoder(kv.getValue()));
					i++;
				}
			}
			return sb.toString();
		}
		return url;
	}

	/**
	 * 获取广告活动状态文字（带颜色）
	 * */
	public String getActivityStatusTextWithColor(int id){
		String color = "";
		if(id==ActivityStatus.UNCONFIRM.getId()){
			color = "style='color:red;'";
		}else if(id==ActivityStatus.COMPLETE.getId()){
			color = "style='color:green;'";
		}
		return "<span "+color+" >"+ActivityStatus.getText(id)+"</span>";
	}
	/**
	 * 获取广告活动状态文字
	 * */
	public String getActivityStatusText(int id){
		return ActivityStatus.getText(id);
	}
	/**
	 * 获取广告活动状态列表
	 * */
	public ActivityStatus[] getActivityStatusList(){
		return ActivityStatus.values();
	}
	/**
	 * 获取广告活动监测任务状态文字
	 * */
	public String getMonitorTaskStatusText(int id){
		return MonitorTaskStatus.getText(id);
	}
	/**
	 * 获取广告活动监测任务状态列表
	 * */
	public MonitorTaskStatus[] getMonitorTaskStatusList(){
		return MonitorTaskStatus.values();
	}
	/**
	 * 获取广告活动监测任务类型文字
	 * */
	public String getMonitorTaskTypeText(int id){
		return MonitorTaskType.getText(id);
	}
	/**
	 * 获取广告活动纠错任务状态文字
	 * */
	public String getJiucuoTaskStatusText(int id){
		return JiucuoTaskStatus.getText(id);
	}
	/**
	 * 获取广告活动纠错任务状态列表
	 * */
	public JiucuoTaskStatus[] getJiucuoTaskStatusList(){
		return JiucuoTaskStatus.values();
	}

	/***************************** 下面是工具类 ****************************************/
	private HashMap<String, String> getParams(String qs) {
		HashMap<String, String> params = new HashMap<String, String>();
		if (qs != null) {
			String[] keyValue = qs.split("&");
			for (String kv : keyValue) {
				int offset = kv.indexOf("=");
				if ((offset > 0) && (offset < (kv.length() - 1))) {
					params.put(kv.substring(0, offset), kv.substring(offset + 1));
				}
			}
		}
		return params;
	}

//	public List<OttvUserinfoAdvertiserVo> getAdvUserList() {
//
//		OttvUserVo user = (OttvUserVo) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
//		List<OttvUserinfoAdvertiserVo> ottvUserinfoAdvertiserList = null;
//		Map<String, Object> searchMap = new HashMap<String, Object>();
//
//		if (user.isSale()) {
//			searchMap.put("saleId", user.getId());
//			ottvUserinfoAdvertiserList = ottvUserInfoService.getAdvUserList(searchMap);
//		} else {
//			ottvUserinfoAdvertiserList = ottvUserInfoService.getAdvUserList(searchMap);
//		}
//
//		return ottvUserinfoAdvertiserList;
//	}
//
//	public List<OttvOrder> getOttvOrderNameList() {
//		OttvUserVo user = (OttvUserVo) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
//		List<OttvOrder> ottvOrderList = null;
//		if (user.isSale()) {
//			ottvOrderList = ottvOrderService.getOrderNameList(user.getId());
//		} else {
//			ottvOrderList = ottvOrderService.getOrderNameList(null);
//		}
//		return ottvOrderList;
//	}
//
//	public List<String> getAreaNeedAssigned(Integer orderId) {
//		List<String> areaList = new ArrayList<String>();
//
//		OttvOrder ottvOrder = ottvOrderService.selectByPrimaryKey(orderId);
//		String area = ottvOrder.getArea();
//		SearchDataVo vo = SearchUtil.getVo();
//
//		if (orderId != null) {
//			vo.putSearchParam("id", orderId + "", orderId);
//		}
//
//		if (area != null && area != "") {
//			Map<String, Object> map = GsonUtil.GsonToMaps(area);
//			for (Entry<String, Object> en : map.entrySet()) {
//				try {
//					Map<String, Object> map2 = GsonUtil.GsonToMaps(GsonUtil.GsonString(en.getValue()));
//					Map<String, String> map3 = GsonUtil.GsonToMaps(GsonUtil.GsonString(map2.get("child")));
//					String proName = (String) map2.get("name");
//					for (Entry<String, String> en2 : map3.entrySet()) {
//						if ("北京".equals(proName.trim()) || "上海".equals(proName.trim()) || "天津".equals(proName.trim()) || "重庆".equals(proName.trim())
//								|| "澳门".equals(proName.trim()) || "澳门".equals(proName.trim())) {
//
//							areaList.add("" + (GsonUtil.GsonToMaps(GsonUtil.GsonString(en2.getValue()))).get("name"));
//						} else {
//							areaList.add(proName + "-"
//									+ (GsonUtil.GsonToMaps(GsonUtil.GsonString(en2.getValue()))).get("name"));
//						}
//					}
//				} catch (Exception e) {
//
//				}
//			}
//		}
//
//        List<OttvOrderPlanVo> ottvOrderPlanVoList = ottvOrderPlanService.getOrderPlanListByOrderId(vo);
//        List<Integer> planIds = new ArrayList<Integer>();
//        for (OttvOrderPlan ottvOrderPlan : ottvOrderPlanVoList) {
//            planIds.add(ottvOrderPlan.getId());
//        }
//
//        Set<String> set = new HashSet<String>();
//		if(CollectionUtils.isNotEmpty(planIds)){
//            List<OttvOrderPlanResource> ottvOrderPlanResourceList = ottvOrderPlanResourceService.getOrderPlanListByOrderPlanId(planIds);
//            for (OttvOrderPlanResource ottvOrderPlanResource : ottvOrderPlanResourceList) {
//                set.add(ottvOrderPlanResource.getAreaName());
//            }
//
//            Iterator<String> iterator = areaList.iterator();
//            while(iterator.hasNext()) {
//                String areaName = iterator.next().trim();
//                for (String areaStr : set) {
//                    if (areaName.contains(areaStr.trim()) || areaName.equals(areaStr.trim())) {
//                        iterator.remove();
//                    }
//                }
//            }
//        }
//
//		return areaList;
//	}

}