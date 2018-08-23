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
import com.bt.om.util.ConfigUtil;
import com.bt.om.util.StringUtil;
import com.bt.om.vo.web.ResultVo;
import com.bt.om.web.BasicController;
import com.bt.om.web.util.PDFHelper;
import com.sun.javafx.binding.StringFormatter;
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
    private String file_upload_path = ConfigUtil.getString("file.upload.path");
   private  String file_upload_ip=ConfigUtil.getString("file.upload.ip");
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
        if (taskreport != null) {
            //报告时间
            String reportTimeStr = taskreport.substring(0, 10);
            reportTime = sdf.parse(reportTimeStr);
            type = taskreport.substring(10, taskreport.length() - 2);
            if (type.contains("上刊")) {
                taskType = MonitorTaskType.UP_TASK.getId();
            } else if (type.contains("上刊监测")) {
                taskType = MonitorTaskType.UP_MONITOR.getId();
            } else if (type.contains("投放期间监测")) {
                taskType = MonitorTaskType.DURATION_MONITOR.getId();
            } else if (type.contains("下刊监测")) {
                taskType = MonitorTaskType.DOWNMONITOR.getId();
            } else if (type.contains("追加监测")) {
                taskType = MonitorTaskType.ZHUIJIA_MONITOR.getId();
            }
            searchMap.put("activityId", activityId);
            searchMap.put("reportTime", reportTime);
            searchMap.put("taskType", taskType);
        }
        //导出文件相关
        AdActivity adActivity = adActivityService.getById(activityId);
        //导出文件名
        //final String fileName = adActivity.getId() + "-" + System.currentTimeMillis() + ".pdf";

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

        try {
            //指定文件保存位置
            String path = file_upload_path;//request.getSession().getServletContext().getRealPath("/");
            //pdf存在路径，已活动ID为文件夹
            String target=(path.endsWith(File.separator) ? "" : File.separatorChar) + activityId.toString() + File.separatorChar + "pdf" + File.separatorChar;
            path = path + target;
            result.setCode(ResultCode.RESULT_SUCCESS.getCode());
            //result.setResult("/static/pdf/" + fileName);
            //返回pdf存储路径
            String  _path = path.substring(path.indexOf(":")+1, path.length()).replaceAll("\\\\", "/");
            _path = _path.replaceFirst("/opt/", "/");
            result.setResult(file_upload_ip+_path);
            //拼接title
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(taskreport.substring(10));

            List<AdActivityAdseatTaskVo> vos = adActivityService.newSelectAdActivityAdseatTaskReport(searchMap);
            Map<String, Map<Integer, List<String>>> cityMap = new HashMap<>();
            Map<String, String> cityFileNameMap = new HashMap<>();
            for (AdActivityAdseatTaskVo vo : vos) {
                Map<Integer, List<String>> _tempMap = new HashMap<>();
                Long provinceCode = vo.getInfo_province();
                Long cityCode = vo.getInfo_city();
                String provinceName = cityCache.getCityName(provinceCode);
                String cityName = cityCache.getCityName(cityCode);
                List<String> list = new ArrayList<>();
                //活动名称 0
                list.add(adActivity.getActivityName());
                //广告位名称 1
                list.add(vo.getInfo_name());
                //省 2
                list.add(provinceName);
                //市 3
                list.add(cityName);
                //主要路段 4
                list.add(vo.getInfo_road());
                //详细位置 5
                list.add(vo.getInfo_location());
                //媒体方广告位编号 6
                list.add(vo.getInfo_memo());
                /**开始监测时间 7*/
                list.add(DateUtil.dateFormate(vo.getMonitorStart(), "yyyy-MM-dd"));
                /**结束监测时间 8*/
                list.add(DateUtil.dateFormate(vo.getMonitorEnd(), "yyyy-MM-dd"));
                Integer proStatus = vo.getProblemStatus();
                String problemStatus = null;
                if (proStatus.equals(TaskProblemStatus.CLOSED.getId())) {
                    problemStatus = TaskProblemStatus.CLOSED.getText();
                } else if (proStatus.equals(TaskProblemStatus.FIXED.getId())) {
                    problemStatus = TaskProblemStatus.FIXED.getText();
                } else if (proStatus.equals(TaskProblemStatus.NO_PROBLEM.getId())) {
                    problemStatus = TaskProblemStatus.NO_PROBLEM.getText();
                } else if (proStatus.equals(TaskProblemStatus.UNMONITOR.getId())) {
                    problemStatus = TaskProblemStatus.UNMONITOR.getText();
                } else if (proStatus.equals(TaskProblemStatus.PROBLEM.getId())) {
                    problemStatus = TaskProblemStatus.PROBLEM.getText();
                }
                list.add(problemStatus);
                //尺寸 10
                list.add(vo.getInfo_adSize());
                //面积 11
                list.add(vo.getInfo_adArea());
                //面数12
                list.add(vo.getInfo_adNum() + "");
                //经度 13
                list.add(vo.getInfo_lon() + "");
                //纬度 14
                list.add(vo.getInfo_lat() + "");
                if (vo.getInfo_mapStandard() != null) {
                    //地图标准 15
                    list.add(MapStandardEnum.getText(vo.getInfo_mapStandard()));
                } else {
                    list.add(null);
                }
                //联系人姓名 16
                list.add(vo.getInfo_contactName());
                //联系人电话 17
                list.add(vo.getInfo_contactCell());
                //媒体大类 18
                list.add(mediaTypeMap.get(vo.getInfo_mediaTypeParentId()));
                //媒体小类 19
                list.add(mediaTypeMap.get(vo.getInfo_mediaTypeId()));
                //媒体主20
                list.add(vo.getMediaName());
                //任务类型21
                list.add(type);
                //22 审核人员
                list.add(vo.getRealname());
                Integer taskStatus = vo.getStatus();
                String status = null;
                if (taskStatus.equals(MonitorTaskStatus.UNASSIGN.getId())) {
                    status = MonitorTaskStatus.UNASSIGN.getText();
                } else if (taskStatus.equals(MonitorTaskStatus.TO_CARRY_OUT.getId())) {
                    status = MonitorTaskStatus.TO_CARRY_OUT.getText();
                } else if (taskStatus.equals(MonitorTaskStatus.UNVERIFY.getId())) {
                    status = MonitorTaskStatus.UNVERIFY.getText();
                } else if (taskStatus.equals(MonitorTaskStatus.VERIFIED.getId())) {
                    status = MonitorTaskStatus.VERIFIED.getText();
                } else if (taskStatus.equals(MonitorTaskStatus.VERIFY_FAILURE.getId())) {
                    status = MonitorTaskStatus.VERIFY_FAILURE.getText();
                } else if (taskStatus.equals(MonitorTaskStatus.UN_FINISHED.getId())) {
                    status = MonitorTaskStatus.UN_FINISHED.getText();
                } else if (taskStatus.equals(MonitorTaskStatus.UN_ACTIVE.getId())) {
                    status = MonitorTaskStatus.UN_ACTIVE.getText();
                } else if (taskStatus.equals(MonitorTaskStatus.CAN_GRAB.getId())) {
                    status = MonitorTaskStatus.CAN_GRAB.getText();
                } else if (taskStatus.equals(MonitorTaskStatus.VERIFY.getId())) {
                    status = MonitorTaskStatus.VERIFY.getText();
                }
                //23 任务状态
                list.add(status);
                //24 任务执行人
                list.add(vo.getExe_realname());
                //25 活动示例图
                list.add(vo.getSamplePicUrl());
                if (taskreport != null) {
                    String reportTimeStr = taskreport.substring(0, 10);
                    reportTime = sdf.parse(reportTimeStr);
                    //26 报告时间
                    list.add(reportTimeStr);
                }
                //27 品牌名
                list.add(brandName);
                //28 广告位点位图
                list.add(vo.getMapPic());
                //ad_activity_adseat的id
                map.put(vo.getId(), list);

                //将数据根据城市进行区分
                String key = provinceCode.toString() + (cityCode == null ? "" : cityCode.toString());
                if (cityMap.containsKey(key)) {
                    cityMap.get(key).put(vo.getId(), list);
                } else {
                    _tempMap.put(vo.getId(), list);
                    cityMap.put(key, _tempMap);
                }
                //如果当前城市key不存在，则添加当前城市的pdf文件名记录
                if (!cityFileNameMap.containsKey(key)) {
                    String reportTimeStr = taskreport.substring(0, 10);
                    reportTimeStr = reportTimeStr.replaceAll("-", "");
                    cityFileNameMap.put(key, MessageFormat.format("/{0}{1}{2}{3}.pdf", provinceName, cityName == null ? "" : cityName, reportTimeStr, activityId));
                }
            }

            //【3】生成pdf图片页
            List<Integer> ids = new ArrayList<>();
            List<Integer> activityAdseatIds = new ArrayList<>();
            //查询每个广告位最新的一条监测任务
            List<AdMonitorTask> tasks = adMonitorTaskService.newSelectLatestMonitorTaskIds(searchMap);
            for (AdMonitorTask task : tasks) {
                //登录方是广告主且任务当前状态是已审核  或者是群邑
                if ((userObj.getUsertype().equals(UserTypeEnum.CUSTOMER.getId()) && task.getStatus().equals(MonitorTaskStatus.VERIFIED.getId())) || (userObj.getUsertype() != UserTypeEnum.CUSTOMER.getId())) {
                    //ad_monitor_task的id
                    ids.add(task.getId());
                    //ad_activity_adseat的id
                    activityAdseatIds.add(task.getActivityAdseatId());
                }
            }
            if (ids.size() > 0) {
                List<AdMonitorTaskFeedback> taskFeedbacks = adMonitorTaskService.selectByActivity(ids);
                PDFHelper pdfHelper = new PDFHelper();

                if (!pdfHelper.buildCityReport(request, taskFeedbacks, path, ids, activityAdseatIds, taskIds, cityFileNameMap, cityMap, adapp, titleName + stringBuffer.toString())) {
                    result.setCode(ResultCode.RESULT_FAILURE.getCode());
                    result.setResultDes("批量导出pdf失败");
                } else {
                    result.setCode(ResultCode.RESULT_SUCCESS.getCode());
                }
//                if (!pdfHelper.buildReport(request, taskFeedbacks, path, ids, activityAdseatIds, taskIds, map, adapp, titleName + stringBuffer.toString())) {
//                    result.setCode(ResultCode.RESULT_FAILURE.getCode());
//                    result.setResultDes("批量导出pdf失败");
//                } else {
//                    result.setCode(ResultCode.RESULT_SUCCESS.getCode());
//                }
            }
        } catch (Exception e) {
            logger.error(MessageFormat.format("批量导出pdf失败", new Object[]{}));
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes(e.getMessage());
            e.printStackTrace();
        }

        model.addAttribute(SysConst.RESULT_KEY, result);
        return model;
    }
}
