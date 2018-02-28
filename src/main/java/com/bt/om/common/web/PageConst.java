package com.bt.om.common.web;

/**
 * 
 * 
 * @author hl-tanyong
 * @version $Id: PageConst.java, v 0.1 2015年9月30日 上午9:27:00 hl-tanyong Exp $
 */
public class PageConst {

	// ***************************common**********************************
	public static final String ERROR = "error";
	public static final String NO_AUTHORITY = "noAuthority";

	// ***************************root**********************************
	/** 登录页 ***/
	public static final String LOGIN_PAGE = "root/login";

	/** 首页 ***/
	public static final String INDEX_PAGE = "root/index";

	/** 修改密码 ***/
	public static final String INDEX_PASSWD = "root/passwd";

	/*** 广告主活动管理 ***/
	// 活动列表
	public static final String CUSTOMER_ACTIVITY_LIST = "adcustomer/activity/list";
	public static final String CUSTOMER_ACTIVITY_EDIT = "adcustomer/activity/edit";
	public static final String CUSTOMER_ACTIVITY_ADSEAT_EDIT = "adcustomer/activity/adseat/edit";
	public static final String CUSTOMER_JIUCUO_LIST = "adcustomer/jiucuo/list";
	public static final String CUSTOMER_JIUCUO_DETAIL = "adcustomer/jiucuo/detail";

	/*** 媒体端任务列表 ***/
	public static final String MEDIA_TASK_LIST = "admedia/task/list";
	public static final String MEDIA_PROBLEM_TASK_LIST = "admedia/task/problemList";
	public static final String MEDIA_JIUCUO_LIST = "admedia/jiucuo/list";

	/*** 活动管理 ***/
	public static final String ACTIVITY_LIST = "activity/list";
	public static final String ACTIVITY_EDIT = "activity/edit";

	/*** 任务管理 ***/
	public static final String TASK_LIST = "task/list";
	public static final String UNASSIGN_TASK_LIST = "task/unassignList";
	public static final String SELECT_USER_EXECUTE = "task/selectExeUser";
	public static final String DETAILS_PAGE = "task/detailsPage";

	/*** 纠错管理 ***/
	public static final String JIUCUO_LIST = "jiucuo/list";
	public static final String JIUCUO_DETAIL = "jiucuo/detail";

	/*** 资源管理 ***/
	public static final String RESOURCE_ADD = "resource/add_ad_seat";
	public static final String RESOURCE_LIST = "resource/list";
	public static final String RESOURCE_DETAILS = "resource/detail";
	public static final String RESOURCE_EDIT = "resource/edit";

	/*** 媒体管理 ***/
	public static final String MEDIA_LIST = "media/list";
	public static final String MEDIA_EDIT = "media/edit";

}
