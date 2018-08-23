package com.bt.om.web.controller;

import com.bt.om.cache.CityCache;
import com.bt.om.common.DateUtil;
import com.bt.om.common.SysConst;
import com.bt.om.entity.*;
import com.bt.om.entity.vo.AdActivityAdseatTaskVo;
import com.bt.om.entity.vo.AdMonitorTaskVo;
import com.bt.om.enums.*;
import com.bt.om.security.ShiroUtils;
import com.bt.om.service.*;
import com.bt.om.vo.web.ResultVo;
import com.bt.om.web.BasicController;
import com.bt.om.web.util.PDFHelper;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author guomw
 */
@Controller
@RequestMapping("/pdf")
public class ReportPdfController extends BasicController {
    @Autowired
    private IAdMediaTypeService adMediaTypeService;
    @Autowired
    private IAdActivityService adActivityService;
    @Autowired
    private IAdMonitorTaskService adMonitorTaskService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private IAppService adappService;

    @Autowired
    private CityCache cityCache;
    /**
     * 具体活动的pdf导出
     *
     * @throws ParseException
     */
    @RequiresRoles(value = {"superadmin", "activityadmin", "depactivityadmin", "admin", "customer", "phoneoperator"}, logical = Logical.OR)
    @RequestMapping(value = "/exportAdMediaPdf")
    @ResponseBody
    public Model exportPdf(Model model, HttpServletRequest request, HttpServletResponse response,
                           @RequestParam(value = "activityId", required = false) Integer activityId,
                           @RequestParam(value = "taskreport", required = false) String taskreport,
                           @RequestParam(value = "brandName", required = false) String brandName,
                           @RequestParam(value = "titleName", required = false) String titleName) throws ParseException {
        System.setProperty("sun.jnu.encoding", "utf-8");
        SysUser userObj = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());

        //相关返回结果
        ResultVo result = new ResultVo();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("查询成功");
        model = new ExtendedModelMap();
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, Object> searchMap = new HashMap<>();
        String type = null;
        //查询媒体类型
        List<AdMediaType> allAdMediaType = adMediaTypeService.getAll();
        Map<Integer, String> mediaTypeMap = new HashMap<>();
        for (AdMediaType adMediaType : allAdMediaType) {
            mediaTypeMap.put(adMediaType.getId(), adMediaType.getName());
        }
        Date reportTime = null;
        Integer taskType = null;
        if(taskreport!=null) {
            //报告时间
            String reportTimeStr = taskreport.substring(0, 10);
            reportTime = sdf.parse(reportTimeStr);
            type = taskreport.substring(10,taskreport.length()-2);
            if(type.contains("上刊")) {
                taskType = MonitorTaskType.UP_TASK.getId();
            } else if(type.contains("上刊监测")) {
                taskType = MonitorTaskType.UP_MONITOR.getId();
            } else if(type.contains("投放期间监测")) {
                taskType = MonitorTaskType.DURATION_MONITOR.getId();
            } else if(type.contains("下刊监测")) {
                taskType = MonitorTaskType.DOWNMONITOR.getId();
            } else if(type.contains("追加监测")) {
                taskType = MonitorTaskType.ZHUIJIA_MONITOR.getId();
            }
            searchMap.put("activityId", activityId);
            searchMap.put("reportTime", reportTime);
            searchMap.put("taskType", taskType);
        }
        //导出文件相关
        AdActivity adActivity = adActivityService.getById(activityId);
        //导出文件名
        final String fileName = adActivity.getId() + "-" + System.currentTimeMillis() + ".pdf";
        List<List<String>> listString = new ArrayList<>();
        Map<Integer, List<String>> map = new HashMap<>();
        List<AdMonitorTaskVo> taskVos = adMonitorTaskService.selectMonitorTaskIdsByActicityId(adActivity.getId());
        Map<Integer, Integer> taskIds = new HashMap<>();
        for (AdMonitorTaskVo adMonitorTaskVo : taskVos) {
            taskIds.put(adMonitorTaskVo.getId(), adMonitorTaskVo.getActivityAdseatId());
        }
        //广告主id
        Integer userId = adActivity.getUserId();
        SysUser sysUser = sysUserService.getUserAppType(userId);
        Integer appId = sysUser.getAppTypeId();
        AdApp adapp = adappService.selectById(appId);

        try{
            //指定文件保存位置
            String path = request.getSession().getServletContext().getRealPath("/");
            path = path + (path.endsWith(File.separator)?"":File.separatorChar)+"static"+File.separatorChar+"pdf"+File.separatorChar+fileName;
            result.setCode(ResultCode.RESULT_SUCCESS.getCode());
            result.setResult("/static/pdf/" + fileName);
            //拼接title
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(taskreport.substring(10));

            List<AdActivityAdseatTaskVo> vos = adActivityService.newSelectAdActivityAdseatTaskReport(searchMap);
            for (AdActivityAdseatTaskVo vo : vos) {
                List<String> list = new ArrayList<>();
                list.add(adActivity.getActivityName()); //活动名称 0
                list.add(vo.getInfo_name()); //广告位名称 1
                list.add(cityCache.getCityName(vo.getInfo_province())); //省 2
                list.add(cityCache.getCityName(vo.getInfo_city())); //市 3
                list.add(vo.getInfo_road());//主要路段 4
                list.add(vo.getInfo_location()); //详细位置 5
                list.add(vo.getInfo_memo()); //媒体方广告位编号 6
                list.add(DateUtil.dateFormate(vo.getMonitorStart(), "yyyy-MM-dd")); //开始监测时间 7
                list.add(DateUtil.dateFormate(vo.getMonitorEnd(), "yyyy-MM-dd")); //结束监测时间 8
                Integer prostatus = vo.getProblemStatus();
                String problemStatus = null;
                if(prostatus==TaskProblemStatus.CLOSED.getId()) {
                    problemStatus = TaskProblemStatus.CLOSED.getText();
                }else if(prostatus==TaskProblemStatus.FIXED.getId()) {
                    problemStatus = TaskProblemStatus.FIXED.getText();
                }else if(prostatus==TaskProblemStatus.NO_PROBLEM.getId()) {
                    problemStatus = TaskProblemStatus.NO_PROBLEM.getText();
                }
                else if(prostatus==TaskProblemStatus.UNMONITOR.getId()) {
                    problemStatus = TaskProblemStatus.UNMONITOR.getText();
                }
                else if(prostatus==TaskProblemStatus.PROBLEM.getId()) {
                    problemStatus = TaskProblemStatus.PROBLEM.getText();
                }
                list.add(problemStatus);
                list.add(vo.getInfo_adSize()); //尺寸 10
                list.add(vo.getInfo_adArea()); //面积 11
                list.add(vo.getInfo_adNum()+"");//面数12
                list.add(vo.getInfo_lon() + ""); //经度 13
                list.add(vo.getInfo_lat() + ""); //纬度 14
                if(vo.getInfo_mapStandard() != null) {
                    list.add(MapStandardEnum.getText(vo.getInfo_mapStandard())); //地图标准 15
                } else {
                    list.add(null);
                }
                list.add(vo.getInfo_contactName()); //联系人姓名 16
                list.add(vo.getInfo_contactCell()); //联系人电话 17
                list.add(mediaTypeMap.get(vo.getInfo_mediaTypeParentId())); //媒体大类 18
                list.add(mediaTypeMap.get(vo.getInfo_mediaTypeId())); //媒体小类 19
                list.add(vo.getMediaName()); //媒体主20
                list.add(type);//任务类型21
                list.add(vo.getRealname());	//22 审核人员
                Integer taskStatus = vo.getStatus();
                String  status= null;
                if(taskStatus== MonitorTaskStatus.UNASSIGN.getId()) {
                    status = MonitorTaskStatus.UNASSIGN.getText();
                }else if(taskStatus == MonitorTaskStatus.TO_CARRY_OUT.getId()) {
                    status = MonitorTaskStatus.TO_CARRY_OUT.getText();
                }else if(taskStatus == MonitorTaskStatus.UNVERIFY.getId()) {
                    status = MonitorTaskStatus.UNVERIFY.getText();
                }else if(taskStatus == MonitorTaskStatus.VERIFIED.getId()) {
                    status = MonitorTaskStatus.VERIFIED.getText();
                }else if(taskStatus == MonitorTaskStatus.VERIFY_FAILURE.getId()) {
                    status = MonitorTaskStatus.VERIFY_FAILURE.getText();
                }else if(taskStatus == MonitorTaskStatus.UN_FINISHED.getId()) {
                    status = MonitorTaskStatus.UN_FINISHED.getText();
                }else if(taskStatus == MonitorTaskStatus.UN_ACTIVE.getId()) {
                    status = MonitorTaskStatus.UN_ACTIVE.getText();
                }else if(taskStatus == MonitorTaskStatus.CAN_GRAB.getId()) {
                    status = MonitorTaskStatus.CAN_GRAB.getText();
                }else if(taskStatus == MonitorTaskStatus.VERIFY.getId()) {
                    status = MonitorTaskStatus.VERIFY.getText();
                }
                list.add(status);//23 任务状态
                list.add(vo.getExe_realname());//24 任务执行人
                list.add(vo.getSamplePicUrl());//25 活动示例图
                if(taskreport != null) {
                    String reportTimeStr = taskreport.substring(0, 10);
                    reportTime = sdf.parse(reportTimeStr);
                    list.add(reportTimeStr);//26 报告时间
                }
                list.add(brandName);//27 品牌名
                list.add(vo.getMapPic());//28 广告位点位图
                map.put(vo.getId(), list); //ad_activity_adseat的id
                listString.add(list);
            }

            //【3】生成pdf图片页
            List<Integer> ids = new ArrayList<>();
            List<Integer> activityAdseatIds = new ArrayList<>();
            //查询每个广告位最新的一条监测任务
            List<AdMonitorTask> tasks = adMonitorTaskService.newSelectLatestMonitorTaskIds(searchMap);
            for (AdMonitorTask task : tasks) {
                if((userObj.getUsertype()==UserTypeEnum.CUSTOMER.getId() && task.getStatus()==MonitorTaskStatus.VERIFIED.getId()) || (userObj.getUsertype()!=UserTypeEnum.CUSTOMER.getId())) {
                    //登录方是广告主且任务当前状态是已审核  或者是群邑
                    //ad_monitor_task的id
                    ids.add(task.getId());
                    //ad_activity_adseat的id
                    activityAdseatIds.add(task.getActivityAdseatId());
                }
            }
            if(ids.size()>0) {
                List<AdMonitorTaskFeedback> taskFeedbacks = adMonitorTaskService.selectByActivity(ids);
                PDFHelper pdfHelper = new PDFHelper();
                if (!pdfHelper.buildReport(request,taskFeedbacks, path, ids, activityAdseatIds, taskIds, map, adapp, titleName + stringBuffer.toString())) {
                    result.setCode(ResultCode.RESULT_FAILURE.getCode());
                    result.setResultDes("批量导出pdf失败");
                } else {
                    result.setCode(ResultCode.RESULT_SUCCESS.getCode());
                }
            }
        }
        catch (Exception e) {
            logger.error(MessageFormat.format("批量导出pdf失败", new Object[] {}));
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes(e.getMessage());
            e.printStackTrace();
        }

        model.addAttribute(SysConst.RESULT_KEY, result);
        return model;
    }
}
