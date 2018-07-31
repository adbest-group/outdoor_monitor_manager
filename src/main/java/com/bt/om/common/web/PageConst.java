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
	
	/** 登录日志 ***/
	public static final String LOGIN_LOG_PAGE = "root/loginLog";

	/** 首页 ***/
	public static final String INDEX_PAGE = "root/index";

	/** 修改密码 ***/
	public static final String INDEX_PASSWD = "root/passwd";

	/*** 广告主活动管理 ***/
	// 活动列表
	public static final String CUSTOMER_ACTIVITY_LIST = "adcustomer/activity/list";
	public static final String CUSTOMER_ACTIVITY_EDIT = "adcustomer/activity/edit";
	public static final String CUSTOMER_ACTIVITY_ADSEAT_EDIT = "adcustomer/activity/adseat/edit";
	public static final String CUSTOMER_ACTIVITY_ADSEAT_SELE = "adcustomer/activity/adseat/select";
	public static final String CUSTOMER_JIUCUO_LIST = "adcustomer/jiucuo/list";
	public static final String CUSTOMER_JIUCUO_DETAIL = "adcustomer/jiucuo/detail";
	public static final String CUSTOMER_RESOURCE = "adcustomer/resource/index";
	public static final String CUSTOMER_REPORT = "adcustomer/report/index";

	/*** 媒体端任务列表 ***/
	public static final String MEDIA_TASK_LIST = "admedia/task/list";
	public static final String MEDIA_PROBLEM_TASK_LIST = "admedia/task/problemList";
	public static final String MEDIA_TASK_DETAIL = "admedia/task/detail";
	public static final String MEDIA_JIUCUO_LIST = "admedia/jiucuo/list";
	public static final String MEDIA_ADSEAT_LIST = "admedia/adseat/list";
	public static final String MEDIA_ADSEAT_EDIT = "admedia/adseat/edit";
	public static final String MEDIA_WORKER_LIST = "admedia/worker/list";
	public static final String MEDIA_WORKER_EDIT = "admedia/worker/edit";

	/*** 活动管理 ***/
	public static final String ACTIVITY_LIST = "activity/list";
	public static final String ACTIVITY_EDIT = "activity/edit";

	/*** 任务管理 ***/
	public static final String TASK_LIST = "task/list";
	public static final String UNASSIGN_TASK_LIST = "task/unassignList";
	public static final String UP_TASK_LIST = "task/upTaskList";
	public static final String SELECT_USER_EXECUTE = "task/selectExeUser";
	public static final String DETAILS_PAGE = "task/detailsPage";
	public static final String ALLTASK_LIST = "task/allTaskList";
	public static final String ADD_ZHUIJIA = "task/addZhuijia";
	public static final String SELECT_ALL_TASKS = "task/selectTaskToExcel";
	public static final String SELECT_TASKPDF = "task/selectTaskToPdf";
	public static final String VERIFYPIC_PAGE = "task/verifyPic";
	/*** 纠错管理 ***/
	public static final String JIUCUO_LIST = "jiucuo/list";
	public static final String JIUCUO_DETAIL = "jiucuo/detail";

	/*** 资源管理 ***/
	public static final String RESOURCE_ADD = "resource/add_ad_seat";
	public static final String ADSEAT_LIST = "adseat/list";
	public static final String RESOURCE_DETAILS = "resource/detail";
	public static final String ADSEAT_EDIT = "adseat/edit";

	/*** 媒体管理 ***/
	public static final String MEDIA_LIST = "media/list";
	public static final String MEDIA_EDIT = "media/edit";
	public static final String MEDIA_TYPE_LIST = "media/typeList";
	public static final String MEDIA_TYPE_EDIT = "media/typeEdit";

	/*** 客户账号管理 ***/
	public static final String CUSTOMER_LIST = "customer/list";
	public static final String CUSTOMER_EDIT = "customer/edit";

	/*** APP账号管理 ***/
	public static final String APP_ACCOUNT_LIST = "appAccount/list";
	public static final String APP_ACCOUNT_EDIT = "appAccount/edit";
	public static final String APP_ACCOUNT_DETAILS = "appAccount/details";
	
	/*** 客户类型管理  ***/
	public static final String CUSTOMER_TYPE_LIST = "adcustomer/type/list";
	public static final String CUSTOMER_TYPE_EDIT = "adcustomer/type/edit";
	public static final String CUSTOMER_TYPE_DETAILS = "adcustomer/type/details";

	/*** 超级管理员下的部门管理 ***/
	public static final String SUPER_ADMIN_DEPT_LIST = "resources/departmentList";
	public static final String SUPER_ADMIN_DEPT_EDIT = "resources/departmentEdit";

	/*** 超级管理员下的领导管理 ***/
	public static final String SUPER_ADMIN_LEAD_LIST = "sysUser/leaderList";
	public static final String SUPER_ADMIN_LEAD_EDIT = "sysUser/leaderEdit";

	/*** 部门管理员下的小组管理 ***/
	public static final String DEPARMENT_ADMIN_GROUP_LIST = "resources/groupList";
	public static final String DEPARMENT_ADMIN_GROUP_EDIT = "resources/groupEdit";
	
	/*** 部门管理员下的员工管理 ***/
	public static final String DEPARMENT_ADMIN_USER_LIST = "sysUser/list";
	public static final String DEPARMENT_ADMIN_USER_EDIT = "sysUser/edit";
	public static final String SUPERADMIN_ADMIN_USER_LIST = "sysUser/adminList";
	
	/*** 部门管理员下的组与员工、客户管理 ***/
	public static final String DEPARMENT_ADMIN_GROUP_USER = "resources/resUserList";
	public static final String DEPARMENT_ADMIN_GROUP_CUSTOMER = "resources/resCustomerList";
	
	/*** 超级管理员下的APP管理 ***/
	public static final String SUPER_ADMIN_APP_LIST = "app/list";
	public static final String SUPER_ADMIN_APP_EDIT = "app/edit";
	
	/*** 超级管理员下的积分设置管理 ***/
	public static final String SUPER_ADMIN_POINT_LIST = "resources/pointList";
	public static final String SUPER_ADMIN_POINT_EDIT = "resources/pointEdit";
	public static final String SUPER_ADMIN_USERPOINT_LIST = "resources/userpointList";
	
	/*** 超级管理员下的金额设置管理***/
	public static final String SUPER_ADMIN_USERMONEY_LIST = "resources/moneyList";
	/*** 超级管理员下的站内信管理 ***/
	public static final String RESOURCES_MESSAGE_LIST = "resources/messageList";
	
	/*** 各部门领导查看自己部门的审核任务 ***/
	public static final String RESOURCES_ACTIVITY = "resources/activity";
	public static final String RESOURCES_TASK_LIST = "resources/taskList";
	public static final String RESOURCES_TASK_UNASSIGN = "resources/taskUnassign";
	public static final String UP_TASK_UNASSIGN = "resources/upTaskList";
	public static final String RESOURCES_JIUCUO_LIST = "resources/jiucuoList";
	
	/*** 后台人员操作日志管理  ***/
	public static final String OPERATE_lOG_LIST = "resources/operateLog";
	
	/*** 系统管理下的系统消息推送管理  ***/
	public static final String SYSTRM_PUSH_LIST = "system/push/list";
	public static final String SYSTRM_PUSH_EDIT = "system/push/edit";
	
	/*** 超级管理员下的呼叫中心人员管理  ***/
	public static final String RESOURCES_PHONEOPERATOR_LIST = "resources/phoneoperatorList";
	public static final String PHONEOPERATOR_EDIT = "resources/phoneoperatorEdit";
	
	/*** 超级管理员下的第三方监测公司管理  ***/
	public static final String THIRD_COMPANY_LIST = "resources/thirdCompanyList";
	public static final String THIRD_COMPANY_EDIT = "resources/thirdCompanyEdit";

}