<#assign webTitle="监测管理-任务详情" in model> <#assign webHead in model>
</#assign> <@model.webhead />
<!-- 头部 -->
<@model.webMenu current="监测管理" child="监测管理" />
<link rel="stylesheet" type="text/css" href="${model.static_domain}/css/new_main.css">
<link rel="stylesheet" type="text/css" href="${model.static_domain}/css/icon_fonts.css">

<!-- 特色内容 -->
<div class="main-container">
	<div class="clearfix">
		<div class="main-box">
			<div class="crumb-nav">
				<a href="/task/list">监测管理</a> > 任务指派
			</div>
			<div class="bd new-active">
				<div class="hd mt-10">
					<h3>任务详情</h3>
				</div>
				<div class="bd" style="font-size:15px">
					<#if (vo?exists) >
						<div>广告活动名称：${vo.activityName}</div>
						<div>投放时间：${vo.startTime?string('yyyy-MM-dd')} 至 ${vo.endTime?string('yyyy-MM-dd')}</div>
						<div>投放地区：${vo.province}-${vo.city}-${vo.region}-${vo.street}</div>
						<div>投放广告位：${vo.name}</div>
						<div>监测时间段：${vo.monitorsStart?string('yyyy-MM-dd')} 至 ${vo.monitorsEnd?string('yyyy-MM-dd')}</div>
						<div>媒体名称：${vo.mediaName}</div>
						<div>投放品牌：${vo.brand}</div>
						<div>创建时间：${vo.createTime?string('yyyy-MM-dd HH:mm')}</div>
						<div>
							样例:</br><img style="vertical-align: top" src="${vo.samplePicUrl}"></img>
						</div>
					<#else>
					   	<div>没有相应结果。</div>
					</#if>
				</div>
			</div>
		</div>
	</div>

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
				                    <div style="padding-top:20px">广告活动名称：${item.activityName}</div>
				                    <div>提交时间：${(item.feedbackCreateTime?string('yyyy-MM-dd HH:mm'))!""}</div>
				                    <div>提交地区：${item.province}-${item.city}-${item.region}-${item.street}</div>
				                    <div>检测时间：
				                    	${vm.getMonitorTaskTypeText(item.taskType)}
				                    	${(item.feedbackCreateTime?if_exists)?string('yyyy-MM-dd HH:mm')}</div>
				                    <div>问题反馈：${item.problem!""} ${item.problemOther!""}</div>
				                    <div>
				                    	执行状态：
				                    	<#if item.feedbackStatus!=2>
					                    	${vm.getMonitorTaskStatusText(item.status)}
					                    <#elseif item.feedbackStatus==2>
					                    	审核未通过
				                    	</#if>
				                    </div>
				                    <div>
				                    	提交照片：</br>
				                    	<div style="width: 360px;height: 300px;vertical-align: middle;display: table-cell;text-align: center;"><img style="vertical-align: top;width:350px" src="${item.picUrl1!""}"></img></div>
				                    	<div style="width: 360px;height: 300px;vertical-align: middle;display: table-cell;text-align: center;"><img style="vertical-align: top;width:350px"" src="${item.picUrl2!""}"></img></div></br>
				                    	<div style="width: 360px;height: 300px;vertical-align: middle;display: table-cell;text-align: center;"><img style="vertical-align: top;width:350px"" src="${item.picUrl3!""}"></img></div>
				                    	<div style="width: 360px;margin-bottom: 10px;height: 300px;vertical-align: middle;display: table-cell;text-align: center;"><img style="vertical-align: top;width:350px"" src="${item.picUrl4!""}"></img></div>
				                    </div>
				                    <#if (item.status==3 && item.feedbackStatus==1)>
				                    	<button type="button" onclick="javascript:pass('${Request.taskId}')" class="btn btn-red" style="margin-left:10px;" autocomplete="off" id="searchBtn" onclick="">
				                    		通过
				                    	</button>
				                    </#if>
				                    
				                    <#if (item.status==3 && item.feedbackStatus==1)>
				                    	<button type="button" onclick="javascript:reject('${Request.taskId}')" class="btn btn-red" style="margin-left:10px;" autocomplete="off" id="searchBtn">
				                    		拒绝
				                    	</button>
				                    </#if>
				                    <div style="border-bottom: 1px solid #ddd;padding-top: 20px"></div>
				            	</#if>   
			                </#list>  
			            </div>
					</div>
				</div>
			</div>
		</div>
	</#if> 			
</div>

<script type="text/javascript"
	src="${model.static_domain}/js/jquery-2.1.4.min.js"></script>
<script type="text/javascript">
	pass = function(id){
	    layer.confirm("确认审核通过？", {
	        icon: 3,
	        btn: ['确定', '取消'] //按钮
	    }, function(){
	        verify(id,4);
	    });
	}
	
	reject = function(id){
        layer.confirm("确认审核不通过？", {
            icon: 3,
            btn: ['确定', '取消'] //按钮
        }, function(index){
            layer.close(index);
            layer.prompt({title:'请填写审核意见',formType:2},function (val,index) {
                if(val.trim().length<1||val.trim().length>33){
                    layer.alert("请填写30字以内！",{icon:2});
                    return;
                }
                layer.close(index);
                verify(id,5,val);
            });

        });
    }
	
	verify = function (id,status,reason) {
        $.ajax({
            url: "/task/verify",
            type: "post",
            data: {
                "id": id,
                "status":status,
                "reason":reason
            },
            cache: false,
            dataType: "json",
            success: function(datas) {
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
                    }, function(){
                        window.location.reload();
                    });
                }
            },
            error: function(e) {
                layer.confirm("服务忙，请稍后再试", {
                    icon: 5,
                    btn: ['确定'] //按钮
                });
            }
        });
    } 
	
	$(function() {
		$(window).resize(function() {
	        var h = $(document.body).height() - 115;
	        $('.main-container').css('height', h);
	    });	
		$(window).resize();
	});
</script>
<@model.webend />
