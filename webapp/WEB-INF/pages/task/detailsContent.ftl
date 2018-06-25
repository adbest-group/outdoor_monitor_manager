
<!-- 特色内容 -->
<link rel="stylesheet" type="text/css" href="${model.static_domain}/css/new_main.css">
<link rel="stylesheet" type="text/css" href="${model.static_domain}/css/icon_fonts.css">
<div class="main-container">
    <div class="clearfix">
        <div class="main-box">
        <#--<div class="crumb-nav">-->
        <#--<a href="/task/list">监测管理</a> > 任务指派-->
        <#--</div>-->
            <div class="bd new-active">
                <div class="hd mt-10">
                    <h3>任务详情</h3>
                </div>
                <div class="bd" style="font-size:15px">
                <#if (vo?exists) >
                    <p>广告活动名称：${vo.activityName!""}</p>
                    <p>活动投放时间：${vo.startTime?string('yyyy-MM-dd')!""} 至 ${vo.endTime?string('yyyy-MM-dd')!""}</p>
                    <p>投放地区：${vm.getCityName(vo.province)!""} - ${vm.getCityName(vo.city!"")} - ${vo.road!""} - ${vo.location!""}</p>
                    <p>投放广告位：${vo.name!""}</p>
                    <p>监测时间段：${vo.monitorsStart?string('yyyy-MM-dd')!""} 至 ${vo.monitorsEnd?string('yyyy-MM-dd')!""}</p>
                    <p>任务类型：${vm.getMonitorTaskTypeText(vo.taskType)!""}</p>
                    <p>媒体名称：${vo.mediaName!""}</p>
                    <#-- <p>投放品牌：${vo.brand!""}</p> -->
                    <p>创建时间：${vo.createTime?string('yyyy-MM-dd HH:mm')!""}</p>
                    <p>
                        样例:</br><img style="vertical-align: top" src="${vo.samplePicUrl!""}"></img>
                    </p>
                <#else>
                    <div>没有相应结果。</div>
                </#if>
                <input type="button" id="btnBack" class="btn btn-primary" value="　返 回　"/>
                </div>
            </div>
        </div>
    </div>

<#if pmTask?exists><#-- 如果父监测任务存在 -->
    <div class="clearfix">
        <div class="main-box">
            <div class="bd new-active">
                <div class="hd mt-10">
                    <h3>前置问题任务提交详情</h3>
                </div>
                <div class="bd">
                    <div class="submitdetails-wrap" style="font-size: 15px">
                        <p>提交时间：${(pmTask.feedbackCreateTime?string('yyyy-MM-dd HH:mm'))!""}</p>
                        <p>提交人：${pmTask.realname!""}</p>
                    <#--<p>提交地区：${item.province}-${item.city}-${item.region}-${item.street}</p>-->
                    <#--<p>检测时间：-->
                    <#--${vm.getMonitorTaskTypeText(item.taskType)}-->
                    <#--${(item.feedbackCreateTime?if_exists)?string('yyyy-MM-dd HH:mm')}</p>-->
                        <p>问题反馈：<#if (!pmTask.problem?exists&&!pmTask.problemOther?exists)>
                            无</#if><span
                                style="color:orangered;">${pmTask.problem!""} <#if (pmTask.problem?exists && pmTask.problemOther?exists)>
                            ，</#if> ${pmTask.problemOther!""}</span></p>
                        <p>
                            执行状态：
                        ${vm.getMonitorTaskStatusText(pmTask.status)!""}
                            <#if (pmTask.reason?exists) >
                                <span style="color:orangered;">(${pmTask.reason!""})</span>
                            </#if>
                        </p>
                        <p>
                            提交照片：</br>
                        <div style="width: 360px;height: 300px;vertical-align: middle;display: table-cell;text-align: center;">
                            <img style="vertical-align: top;width:350px" src="${pmTask.picUrl1!""}"></img></div>
                        <div style="width: 360px;height: 300px;vertical-align: middle;display: table-cell;text-align: center;">
                            <img style="vertical-align: top;width:350px"" src="${pmTask.picUrl2!""}"></img></div>
                        </br>
                        <div style="width: 360px;height: 300px;vertical-align: middle;display: table-cell;text-align: center;">
                            <img style="vertical-align: top;width:350px"" src="${pmTask.picUrl3!""}"></img></div>
                        <div style="width: 360px;margin-bottom: 10px;height: 300px;vertical-align: middle;display: table-cell;text-align: center;">
                            <img style="vertical-align: top;width:350px"" src="${pmTask.picUrl4!""}"></img></div>
                        </p>
                        <div style="border-bottom: 1px solid #ddd;padding-top: 20px"></div>
                        
                    </div>
                </div>
            </div>
        </div>
    </div>
</#if>

<#if pjTask?exists><#-- 如果父纠错存在 -->
    <div class="clearfix">
        <div class="main-box">
            <div class="bd new-active">
                <div class="hd mt-10">
                    <h3>前置纠错提交详情</h3>
                </div>
                <div class="bd">
                    <div class="submitdetails-wrap" style="font-size: 15px">
                        <p>提交时间：${(pjTask.createTime?string('yyyy-MM-dd HH:mm'))!""}</p>
                        <p>提交人：${pjTask.realname!""}</p>
                    <#--<p>提交地区：${item.province}-${item.city}-${item.region}-${item.street}</p>-->
                    <#--<p>检测时间：-->
                    <#--${vm.getMonitorTaskTypeText(item.taskType)}-->
                    <#--${(item.feedbackCreateTime?if_exists)?string('yyyy-MM-dd HH:mm')}</p>-->
                        <p>问题反馈：<#if (!pjTask.problem?exists&&!pjTask.problemOther?exists)>
                            无</#if><span
                                style="color:orangered;">${pjTask.problem!""} <#if (pjTask.problem?exists && pjTask.problemOther?exists)>
                            ，</#if> ${pjTask.problemOther!""}</span></p>
                        <p>
                            执行状态：
                        ${vm.getJiucuoTaskStatusText(pjTask.status)!""}
                            <#if (pjTask.reason?exists) >
                                <span style="color:orangered;">(${pjTask.reason!""})</span>
                            </#if>
                        </p>
                        <p>
                            提交照片：</br>
                        <div style="width: 360px;height: 300px;vertical-align: middle;display: table-cell;text-align: center;">
                            <img style="vertical-align: top;width:350px" src="${pjTask.picUrl1!""}"></img></div>
                        </p>
                        <div style="border-bottom: 1px solid #ddd;padding-top: 20px"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</#if>

<#if (list?exists && list?size>0) >
    <div class="clearfix">
        <div class="main-box">
            <div class="bd new-active">
                <div class="hd mt-10">
                    <h3>提交详情</h3>
                </div>
                <div class="bd">
                    <div class="submitdetails-wrap" style="font-size: 15px">
                        <#list list as item>
                          <#if item.status!=1>
                            	<input type="hidden" id="selectTaskFeedBackId" value="">
                            
                                <p>广告活动名称：${item.activityName!""}</p>
                                <p>提交时间：${(item.feedbackCreateTime?string('yyyy-MM-dd HH:mm'))!""}</p>
                            <#--<p>提交地区：${item.province}-${item.city}-${item.region}-${item.street}</p>-->
                            <#--<p>检测时间：-->
                            <#--${vm.getMonitorTaskTypeText(item.taskType)}-->
                            <#--${(item.feedbackCreateTime?if_exists)?string('yyyy-MM-dd HH:mm')}</p>-->
                                <p>问题反馈：<#if (!item.problem?exists&&!item.problemOther?exists)>
                                    无</#if><span
                                        style="color:orangered;">${item.problem!""} <#if (item.problem?exists && item.problemOther?exists)>
                                    ，</#if> ${item.problemOther!""}</span></p>
                                <p>
                                    执行状态：
                                    <#if item.feedbackStatus!=2>
                                    ${vm.getMonitorTaskStatusText(item.status)!""} <#if item.status==5><span style="color:orangered;">(审核意见：${item.reason!""})</span></#if>
                                    <#elseif item.feedbackStatus==2>
                                        审核未通过
                                        <#if (item.reason?exists && item.reason!="" && item.reason!="null") >
                                            <span style="color:orangered;">(审核意见：${item.reason!""})</span>
                                        </#if>
                                    </#if>
                                </p>
                                <p>
                                    提交照片：</br>
                                <div style="width: 360px;height: 300px;vertical-align: middle;display: table-cell;text-align: center;">
                                     <img style="vertical-align: top;width:350px" src="${item.picUrl1!""}"></img>
                                     <#if usertype?exists&&usertype==4><#if (vo.status?exists&&vo.status!=7&&vo.status!=8)> <input type="button" id="changePic1" class="changePic btn btn-primary" value="　更换　" onclick="setFeedbackId(${item.feedbackId!""})"/></#if></#if>
                                </div>
                                <div style="width: 360px;height: 300px;vertical-align: middle;display: table-cell;text-align: center;">
                                    <img style="vertical-align: top;width:350px"" src="${item.picUrl2!""}"></img>
                                    <#if usertype?exists&&usertype==4><#if (vo.status?exists&&vo.status!=7&&vo.status!=8)><input type="button" id="changePic2" class="changePic btn btn-primary" value="　更换　" onclick="setFeedbackId(${item.feedbackId!""})"/></#if></#if>
                                </div> 
                                </br>
                                <div style="width: 360px;height: 300px;vertical-align: middle;display: table-cell;text-align: center;">
                                    <img style="vertical-align: top;width:350px"" src="${item.picUrl3!""}"></img>
                                    <#if usertype?exists&&usertype==4><#if (vo.status?exists&&vo.status!=7&&vo.status!=8)><input type="button" id="changePic3" class="changePic btn btn-primary" value="　更换　" onclick="setFeedbackId(${item.feedbackId!""})"/></#if></#if>
                                </div> 
                                <div style="width: 360px;margin-bottom: 10px;height: 300px;vertical-align: middle;display: table-cell;text-align: center;">
                                    <img style="vertical-align: top;width:350px"" src="${item.picUrl4!""}"></img>
                                    <#if usertype?exists&&usertype==4><#if (vo.status?exists&&vo.status!=7&&vo.status!=8)><input type="button" id="changePic4" class="changePic btn btn-primary" value="　更换　" onclick="setFeedbackId(${item.feedbackId!""})"/></#if></#if>
                                </div> 
                                </p>
                                <p><br/>
                                    <#setting number_format="#0.######" />
                                    <div class="feedbackMap" id="feedback-item.id" data-location="{lon:${item.lon!"null"},lat:${item.lat!"null"},feedbackLon:${item.feedbackLon!"null"},feedbackLat:${item.feedbackLat!"null"}}" style="width:400px;height:275px;"></div>
                                </p>
                                <@shiro.hasRole name="admin">
                                    <p style="text-align: center;">
                                        <#if (item.status==3 && item.feedbackStatus==1)>
                                            <button style="margin-top: 10px" type="button"
                                                    onclick="javascript:pass('${Request.taskId!""}')" class="btn btn-red"
                                                    style="margin-left:10px;" autocomplete="off" id="searchBtn"
                                                    onclick="">
                                                通过
                                            </button>
                                        </#if>


                                        <#if (item.status==3 && item.feedbackStatus==1)>
                                            <button style="margin-top: 10px" type="button"
                                                    onclick="javascript:reject('${Request.taskId!""}')" class="btn btn-red"
                                                    style="margin-left:10px;" autocomplete="off" id="searchBtn">
                                                拒绝
                                            </button>
                                        </#if>
                                    </p>
                                </@shiro.hasRole>
                                <div style="border-bottom: 1px solid #ddd;padding-top: 20px"></div>
                            </#if>
                        </#list>
                    </div>
                </div>
            </div>
        </div>
    </div>
</#if>

<#if vo.status?exists && vo.status==1 || vo.status==2>
    <div class="clearfix">
        <div class="main-box">
            <div class="bd new-active">
                <div class="hd mt-10">
                    <h3>提交详情</h3>
                </div>
                <div class="bd">
                    <div class="submitdetails-wrap" style="font-size: 15px">
                    	<input type="hidden" id="selectMonitorTaskId" value="${vo.id?if_exists}">
                    		<p>替换的执行人员：</p> <br>
							<div class="select-box select-box-100 un-inp-select ll">
		                       	 <select class="select" name="selectMediaId" id="selectMediaId" onchange="changeMediaTypeId();">
				                     <option value="">所有媒体</option>
				                     <@model.showAllMediaOps value="" />
				                 </select>
		                    </div>
		                    <div class="select-box select-box-100 un-inp-select ll">
		                       	 <select class="select" name="selectMediaName" id="selectMediaName">
				                     <option value="">媒体成员</option>
				                 </select>
		                    </div> 
		                    <br><br>
		                    
		                    <p>执行人员做任务的经纬度：</p> <br>
                            <div style="vertical-align: middle;display: table-cell;text-align: center;">
	                            <table id="formValid">
	                            	<tr>
	                            		<td>经度：<input type="text" id="lontitude" name="lontitude"/></td>
	                            		<td><span id="lonTip"></span></td>
	                            	<tr>
	                            	<tr>
	                            		<td>&nbsp;</td>
	                            		<td>&nbsp;</td>
                            		</tr>
	                            	<tr>
	                            		<td>纬度：<input type="text" id="latitude" name="latitude"/></td>
	                            		<td><span id="latTip"></span></td>
	                            	</tr>
	                            </table>
                            </div>
                            
                            </br></br>
                            <p>提交照片：</p>
                            <div style="width: 360px;height: 300px;vertical-align: middle;display: table-cell;text-align: center;">
                                 <img style="vertical-align: top;width:350px" src=""></img>
                                 <input type="button" class="changePic btn btn-primary" value="　更换　" onclick="checkVal(this);"/>
                                 <input type="button" id="changePic11" style="visibility: hidden" class="changePic btn btn-primary" value="　更换　"/>
                            </div>
                            <div style="width: 360px;height: 300px;vertical-align: middle;display: table-cell;text-align: center;">
                                <img style="vertical-align: top;width:350px"" src=""></img>
                                <input type="button" class="changePic btn btn-primary" value="　更换　" onclick="checkVal(this);"/>
                                <input type="button" id="changePic22" style="visibility: hidden" class="changePic btn btn-primary" value="　更换　"/>
                            </div> 
                            </br>
                            <div style="width: 360px;height: 300px;vertical-align: middle;display: table-cell;text-align: center;">
                                <img style="vertical-align: top;width:350px"" src=""></img>
                                <input type="button" class="changePic btn btn-primary" value="　更换　" onclick="checkVal(this);"/>
                                <input type="button" id="changePic33" style="visibility: hidden" class="changePic btn btn-primary" value="　更换　"/>
                            </div> 
                            <div style="width: 360px;margin-bottom: 10px;height: 300px;vertical-align: middle;display: table-cell;text-align: center;">
                                <img style="vertical-align: top;width:350px"" src=""></img>
                                <input type="button" class="changePic btn btn-primary" value="　更换　" onclick="checkVal(this);"/>
                                <input type="button" id="changePic44" style="visibility: hidden" class="changePic btn btn-primary" value="　更换　"/>
                            </div> 
                        
                            <div style="border-bottom: 1px solid #ddd;padding-top: 20px"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>

</#if>

</div>

<script type="text/javascript"
        src="${model.static_domain}/js/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=T8nSZc6XXTiu1vm5pCwdYu1D5AIb2F1w"></script>
<!-- formValidator -->
<link type="text/css" rel="stylesheet" href="${model.static_domain}/js/formValidator/style/validator.css"></link>
<script type="text/javascript" src="${model.static_domain}/js/formValidator/formValidator-4.0.1.js"></script>
<script type="text/javascript">

	function setFeedbackId(id){
		$("#selectTaskFeedBackId").val(id);
	}
	
	function checkVal(that){
	   var lon = $('#lontitude').val();
  	   var lat = $('#latitude').val();
  	   var userId = $('#selectMediaName').val();
  	  
  	   if(lon == null || lon == "" || lon.length <= 0){
  	  	layer.confirm("请填写经度", {
  			icon: 2,
  			btn: ['确定'] //按钮
  		});
  	  	return false;
  	   }
  	  
  	  if(lat == null || lat == "" || lat.length <= 0){
  	  	layer.confirm("请填写纬度", {
  			icon: 2,
  			btn: ['确定'] //按钮
  		});
  	  	return false;
  	  }
  	  
  	  if(userId == null || userId == "" || userId.length <= 0){
  	  	layer.confirm("请选择媒体执行人员", {
  			icon: 2,
  			btn: ['确定'] //按钮
  		});
  	  	return false;
  	  }
  	  $(that).next().click()
	}
	
    pass = function (id) {
        layer.confirm("确认审核通过？", {
            icon: 3,
            btn: ['确定', '取消'] //按钮
        }, function () {
            verify(id, 4);
        });
    }

    reject = function (id) {
        layer.confirm("确认审核不通过？", {
            icon: 3,
            btn: ['确定', '取消'] //按钮
        }, function (index) {
            layer.close(index);
            layer.prompt({title: '请填写审核意见', formType: 2}, function (val, index) {
                if (val.trim().length < 1 || val.trim().length > 33) {
                    layer.alert("请填写30字以内！", {icon: 2});
                    return;
                }
                layer.close(index);
                verify(id, 5, val);
            });

        });
    }

    verify = function (id, status, reason) {
        $.ajax({
            url: "/task/verify",
            type: "post",
            data: {
                "id": id,
                "status": status,
                "reason": reason
            },
            cache: false,
            dataType: "json",
            success: function (datas) {
                var resultRet = datas.ret;
                if (resultRet.code == 101) {
                    layer.confirm(resultRet.resultDes, {
                        icon: 2,
                        btn: ['确定'] //按钮
                    });
                } else {
                    layer.confirm("审核成功", {
                        icon: 1,
                        btn: ['确定'] //按钮
                    }, function () {
                        window.location.reload();
                    });
                }
            },
            error: function (e) {
                layer.confirm("服务忙，请稍后再试", {
                    icon: 5,
                    btn: ['确定'] //按钮
                });
            }
        });
    }


    //更换详情图片1
	layui.use('upload', function(){
	  var upload = layui.upload;
	  var elem = '#changePic1'
	  if(document.getElementById('changePic1')){
	  	elem = '#changePic11'
	  }
	  //执行实例
	  var uploadInst = upload.render({
	    elem: elem //绑定元素
	    ,data: {

	    	  taskFeedBackId: function() {
		  	  return $('#selectTaskFeedBackId').val()
		 	 },

		  index : 1
		}
	    ,accept: 'images' //指定只允许上次文件
	    ,exts: 'jpg|jpeg|png' //指定只允许上次jpg和png格式的图片
	    ,field: 'picFile' //设置字段名
	    ,url: '/task/changePic' //上传接口
	    ,done: function(res){
	   		if(res.ret.code == 100){
	    		layer.alert('替换成功', {icon: 1, closeBtn: 0, btn: [], title: false, time: 3000});
	    		window.location.reload();
	    	} else if (res.ret.code == 101){
	    		layer.alert(res.ret.resultDes, {icon: 2, closeBtn: 0, btn: [], title: false, time: 3000, anim: 6});
	    	} else if (res.ret.code == 105){
	    		layer.alert('没有替换权限', {icon: 2, closeBtn: 0, btn: [], title: false, time: 3000, anim: 6});
	    	}
	    }
	    ,error: function(res){
	       layer.alert('替换失败', {icon: 2, closeBtn: 0, btn: [], title: false, time: 3000, anim: 6});
	    }
	  });
	}); 

	//更换详情图片2
	layui.use('upload', function(){
	  var upload = layui.upload;
	  
	  //执行实例
	  var uploadInst = upload.render({
	    elem: '#changePic2' //绑定元素
	    ,data: {
		  taskFeedBackId: function() {
		  	return $('#selectTaskFeedBackId').val()
		  },
		  index : 2
		}
	    ,accept: 'images' //指定只允许上次文件
	    ,exts: 'jpg|jpeg|png' //指定只允许上次jpg和png格式的图片
	    ,field: 'picFile' //设置字段名
	    ,url: '/task/changePic' //上传接口
	    ,done: function(res){
	    		if(res.ret.code == 100){
	    		layer.alert('替换成功', {icon: 1, closeBtn: 0, btn: [], title: false, time: 3000});
	    		window.location.reload();
	    	} else if (res.ret.code == 101){
	    		layer.alert(res.ret.resultDes, {icon: 2, closeBtn: 0, btn: [], title: false, time: 3000, anim: 6});
	    	} else if (res.ret.code == 105){
	    		layer.alert('没有替换权限', {icon: 2, closeBtn: 0, btn: [], title: false, time: 3000, anim: 6});
	    	}
	    }
	    ,error: function(res){
	       layer.alert('替换失败', {icon: 2, closeBtn: 0, btn: [], title: false, time: 3000, anim: 6});
	    }
	  });
	}); 
	
	//更换详情图片3
	layui.use('upload', function(){
	  var upload = layui.upload;
	  
	  //执行实例
	  var uploadInst = upload.render({
	    elem: '#changePic3' //绑定元素
	    ,data: {
		  taskFeedBackId: function() {
		  	return $('#selectTaskFeedBackId').val()
		  },
		  index : 3
		}
	    ,accept: 'images' //指定只允许上次文件
	    ,exts: 'jpg|jpeg|png' //指定只允许上次jpg和png格式的图片
	    ,field: 'picFile' //设置字段名
	    ,url: '/task/changePic' //上传接口
	    ,done: function(res){
	    		if(res.ret.code == 100){
	    		layer.alert('替换成功', {icon: 1, closeBtn: 0, btn: [], title: false, time: 3000});
	    		window.location.reload();
	    	} else if (res.ret.code == 101){
	    		layer.alert(res.ret.resultDes, {icon: 2, closeBtn: 0, btn: [], title: false, time: 3000, anim: 6});
	    	} else if (res.ret.code == 105){
	    		layer.alert('没有替换权限', {icon: 2, closeBtn: 0, btn: [], title: false, time: 3000, anim: 6});
	    	}
	    }
	    ,error: function(res){
	       layer.alert('替换失败', {icon: 2, closeBtn: 0, btn: [], title: false, time: 3000, anim: 6});
	    }
	  });
	}); 
	
	//更换详情图片4
	layui.use('upload', function(){
	  var upload = layui.upload;
	  
	  //执行实例
	  var uploadInst = upload.render({
	    elem: '#changePic4' //绑定元素
	    ,data: {
		  taskFeedBackId: function() {
		  	return $('#selectTaskFeedBackId').val()
		  },
		  index : 4
		}
	    ,accept: 'images' //指定只允许上次文件
	    ,exts: 'jpg|jpeg|png' //指定只允许上次jpg和png格式的图片
	    ,field: 'picFile' //设置字段名
	    ,url: '/task/changePic' //上传接口
	    ,done: function(res){
	    		if(res.ret.code == 100){
	    		layer.alert('替换成功', {icon: 1, closeBtn: 0, btn: [], title: false, time: 3000});
	    		window.location.reload();
	    		//window.location.href = document.referrer;	    		
	    	} else if (res.ret.code == 101){
	    		layer.alert(res.ret.resultDes, {icon: 2, closeBtn: 0, btn: [], title: false, time: 3000, anim: 6});
	    	} else if (res.ret.code == 105){
	    		layer.alert('没有替换权限', {icon: 2, closeBtn: 0, btn: [], title: false, time: 3000, anim: 6});
	    	}
	    }
	    ,error: function(res){
	       layer.alert('替换失败', {icon: 2, closeBtn: 0, btn: [], title: false, time: 3000, anim: 6});
	    }
	  });
	});
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//更换详情图片1
	layui.use('upload', function(){
	  var upload = layui.upload;
	  
	  //执行实例
	  var uploadInst = upload.render({
	    elem: '#changePic11' //绑定元素
	    ,data: {
		  monitorTaskId: function() {
		  	return $('#selectMonitorTaskId').val()
		  },
		  index : 1,
		  lon: function() {
		  	return $('#lontitude').val()
		  },
		  lat: function() {
		  	return $('#latitude').val()
		  },
		  userId: function() {
		  	return $('#selectMediaName').val()
		  }
		}
	    ,accept: 'images' //指定只允许上次文件
	    ,exts: 'jpg|jpeg|png' //指定只允许上次jpg和png格式的图片
	    ,field: 'picFile' //设置字段名
	    ,url: '/task/changePic' //上传接口
	    ,done: function(res){
	    		if(res.ret.code == 100){
	    		layer.alert('替换成功', {icon: 1, closeBtn: 0, btn: [], title: false, time: 3000});
	    		window.location.reload();
	    		//window.location.href = document.referrer;	    		
	    	} else if (res.ret.code == 101){
	    		layer.alert(res.ret.resultDes, {icon: 2, closeBtn: 0, btn: [], title: false, time: 3000, anim: 6});
	    	} else if (res.ret.code == 105){
	    		layer.alert('没有替换权限', {icon: 2, closeBtn: 0, btn: [], title: false, time: 3000, anim: 6});
	    	}
	    }
	    ,error: function(res){
	       layer.alert('替换失败', {icon: 2, closeBtn: 0, btn: [], title: false, time: 3000, anim: 6});
	    }
	  });
	});
	
	//更换详情图片2
	layui.use('upload', function(){
	  var upload = layui.upload;
	  
	  //执行实例
	  var uploadInst = upload.render({
	    elem: '#changePic22' //绑定元素
	    ,data: {
		  monitorTaskId: function() {
		  	return $('#selectMonitorTaskId').val()
		  },
		  index : 2,
		  lon: function() {
		  	return $('#lontitude').val()
		  },
		  lat: function() {
		  	return $('#latitude').val()
		  },
		  userId: function() {
		  	return $('#selectMediaName').val()
		  }
		}
	    ,accept: 'images' //指定只允许上次文件
	    ,exts: 'jpg|jpeg|png' //指定只允许上次jpg和png格式的图片
	    ,field: 'picFile' //设置字段名
	    ,url: '/task/changePic' //上传接口
	    ,done: function(res){
	    		if(res.ret.code == 100){
	    		layer.alert('替换成功', {icon: 1, closeBtn: 0, btn: [], title: false, time: 3000});
	    		window.location.reload();
	    		//window.location.href = document.referrer;	    		
	    	} else if (res.ret.code == 101){
	    		layer.alert(res.ret.resultDes, {icon: 2, closeBtn: 0, btn: [], title: false, time: 3000, anim: 6});
	    	} else if (res.ret.code == 105){
	    		layer.alert('没有替换权限', {icon: 2, closeBtn: 0, btn: [], title: false, time: 3000, anim: 6});
	    	}
	    }
	    ,error: function(res){
	       layer.alert('替换失败', {icon: 2, closeBtn: 0, btn: [], title: false, time: 3000, anim: 6});
	    }
	  });
	});
	
	//更换详情图片3
	layui.use('upload', function(){
	  var upload = layui.upload;
	  
	  //执行实例
	  var uploadInst = upload.render({
	    elem: '#changePic33' //绑定元素
	    ,data: {
		  monitorTaskId: function() {
		  	return $('#selectMonitorTaskId').val()
		  },
		  index : 3,
		  lon: function() {
		  	return $('#lontitude').val()
		  },
		  lat: function() {
		  	return $('#latitude').val()
		  },
		  userId: function() {
		  	return $('#selectMediaName').val()
		  }
		}
	    ,accept: 'images' //指定只允许上次文件
	    ,exts: 'jpg|jpeg|png' //指定只允许上次jpg和png格式的图片
	    ,field: 'picFile' //设置字段名
	    ,url: '/task/changePic' //上传接口
	    ,done: function(res){
	    		if(res.ret.code == 100){
	    		layer.alert('替换成功', {icon: 1, closeBtn: 0, btn: [], title: false, time: 3000});
	    		window.location.reload();
	    		//window.location.href = document.referrer;	    		
	    	} else if (res.ret.code == 101){
	    		layer.alert(res.ret.resultDes, {icon: 2, closeBtn: 0, btn: [], title: false, time: 3000, anim: 6});
	    	} else if (res.ret.code == 105){
	    		layer.alert('没有替换权限', {icon: 2, closeBtn: 0, btn: [], title: false, time: 3000, anim: 6});
	    	}
	    }
	    ,error: function(res){
	       layer.alert('替换失败', {icon: 2, closeBtn: 0, btn: [], title: false, time: 3000, anim: 6});
	    }
	  });
	});
	
	//更换详情图片4
	layui.use('upload', function(){
	  var upload = layui.upload;
	  
	  //执行实例
	  var uploadInst = upload.render({
	    elem: '#changePic44' //绑定元素
	    ,data: {
		  monitorTaskId: function() {
		  	return $('#selectMonitorTaskId').val()
		  },
		  index : 4,
		  lon: function() {
		  	return $('#lontitude').val()
		  },
		  lat: function() {
		  	return $('#latitude').val()
		  },
		  userId: function() {
		  	return $('#selectMediaName').val()
		  }
		}
	    ,accept: 'images' //指定只允许上次文件
	    ,exts: 'jpg|jpeg|png' //指定只允许上次jpg和png格式的图片
	    ,field: 'picFile' //设置字段名
	    ,url: '/task/changePic' //上传接口
	    ,done: function(res){
	    		if(res.ret.code == 100){
	    		layer.alert('替换成功', {icon: 1, closeBtn: 0, btn: [], title: false, time: 3000});
	    		window.location.reload();
	    		//window.location.href = document.referrer;	    		
	    	} else if (res.ret.code == 101){
	    		layer.alert(res.ret.resultDes, {icon: 2, closeBtn: 0, btn: [], title: false, time: 3000, anim: 6});
	    	} else if (res.ret.code == 105){
	    		layer.alert('没有替换权限', {icon: 2, closeBtn: 0, btn: [], title: false, time: 3000, anim: 6});
	    	}
	    }
	    ,error: function(res){
	       layer.alert('替换失败', {icon: 2, closeBtn: 0, btn: [], title: false, time: 3000, anim: 6});
	    }
	  });
	});
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////

    $(function () {
        $(window).resize(function () {
            var h = $(document.body).height() - 115;
            $('.main-container').css('height', h);
        });
        $(window).resize();

        //任务提交地图坐标相关
        $(".feedbackMap").each(function(i,n){
            // 百度地图API功能
            var map = new BMap.Map(n.id);    // 创建Map实例
            var location = eval("("+$(n).data("location")+")");
            var seatMarker;
            var feedbackMarker;
            console.log(location)
            var feedbackPoint = new BMap.Point(location.feedbackLon, location.feedbackLat);
            feedbackMarker = new BMap.Marker(feedbackPoint)
            if(location.lon&&location.lat) {
                var seatPoint = new BMap.Point(location.lon, location.lat);
                map.centerAndZoom(seatPoint, 16);  // 初始化地图,设置中心点坐标和地图级别
                seatMarker = new BMap.Marker(seatPoint);
                seatMarker.setAnimation(BMAP_ANIMATION_BOUNCE);
            }else{
                map.centerAndZoom(feedbackPoint, 16);
            }

            //添加地图类型控件
            map.addControl(new BMap.MapTypeControl({
                mapTypes:[
                    BMAP_NORMAL_MAP,
//                BMAP_HYBRID_MAP
                ]}));
//            map.enableScrollWheelZoom(true);     //开启鼠标滚轮缩放

            map.addOverlay(feedbackMarker);
            map.addOverlay(seatMarker);
        });
        $("#btnBack").click(function(){history.back();});
        changeMediaTypeId();
      <#--   $.formValidator.initConfig({ formID: "formValid", onError: function () { alert("校验没有通过，具体错误请看错误提示") } });
        inputValid();--> 
        
    });
    
    function inputValid(){
    		//广告位经度
        $("#lon").formValidator({
            validatorGroup:"2",
            onShow: "　",
            onFocus: "请输入经度",
            onCorrect: ""
        }).functionValidator({
			fun:function(val){
				if($.trim(val).length<0)
				    return false;
				return true;
			},
			onError:"请输入经度"
		}).inputValidator({
            type:"number",
            min:-180,
            max:180,
            onError:"经度支持 -180 ~ 180"
        });
        
    	//广告位纬度
        $("#lat").formValidator({
            validatorGroup:"2",
            onShow: "　",
            onFocus: "请输入纬度",
            onCorrect: ""
        }).functionValidator({
            fun:function(val){
                if($.trim(val).length<0)
                    return false;
                return true;
            },
            onError:"请输入纬度"
        }).inputValidator({
            type:"number",
            min:-90,
            max:90,
            onError:"纬度支持 -90 ~ 90"
        });
        
    }
    
	function changeMediaTypeId() {
		var selectMediaId = $("#selectMediaId").val();
		if(selectMediaId == "" || selectMediaId.length <= 0) {
			var option = '<option value="">媒体成员</option>';
			$("#selectMediaName").html(option);
			return;
		}
		$.ajax({
			url : '/task/selectUserExecuteTask',
			type : 'POST',
			data : {"mediaId":selectMediaId},
			dataType : "json",
			traditional : true,
			success : function(data) {
				var result = data.ret;
				if (result.code == 100) {
					var adMediaTypes = result.result;
					var htmlOption = '<option value="">媒体成员</option>';
					for (var i=0; i < adMediaTypes.length;i++) { 
						var type = adMediaTypes[i];
						htmlOption = htmlOption + '<option value="' + type.id + '">' + type.realname + '</option>';
					}
						$("#selectMediaName").html(htmlOption);
				} else {
					alert('修改失败!');
				}
			}
		});
	}
</script>
