<link rel="stylesheet" type="text/css" href="${model.static_domain}/css/new_main.css">
<link rel="stylesheet" type="text/css" href="${model.static_domain}/css/icon_fonts.css">
<style type="text/css">
		html, body{ min-width: 100%;overflow:auto; }
		.basic-info .bd .a-title{ width: 120px;font-size:14px; }
		
		.basic-info .bd td label{ margin-right:20px; display: inline-block; font-size:14px;}
		.basic-info .bd td span{font-size:14px;}
		.basic-info .bd .formZone li.city-item label{ margin-right: 0; }
		.file-upload{position: relative;overflow: hidden; float: left;}
		.file-upload a,.interaction-bac-big a{color:#ee5f63;text-decoration:underline;}
		.file-upload input{position: absolute;left: 0; top: 0; opacity: 0;font-size: 50px; width: 60px;}
		.interaction-bac-big img{width: 296px;height: 194px;float: left}
		.interaction-bac-big label{height: 194px;line-height: 194px;display: block;float: left;padding-left: 15px}
</style>
	<div class="basic-info">
		<div class="bd">
            <form id="subForm" method="post">
                <input type="hidden" id="id"/>
			<table width="100%" cellpadding="0" cellspacing="0" border="0" class="tablesorter">
				<tbody>
					<#if (reportList?exists && reportList?size>0)>
						<#list reportList as report>
							<#if report?exists><a href="javascript:zhuijiaMonitorShow(${activityId},'${report}')">${report?if_exists}</a><br/></#if>
						</#list>
					<#else>当前时间暂无报告</#if>
				</tbody>
			</table>
            </form>
		</div>
	</div>
	<script type="text/javascript" src="${model.static_domain}/js/jquery-2.1.4.min.js"></script>
	<script type="text/javascript" src="${model.static_domain}/js/layer/layer.js"></script>
	<script type="text/javascript" src="${model.static_domain}/js/common.js"></script>
	<!-- formValidator -->
	<link type="text/css" rel="stylesheet" href="${model.static_domain}/js/formValidator/style/validator.css"></link>
	
<script type="text/javascript">
	function upTaskShow(activityId,taskreport) { //上刊任务
		layer.open({
    		type : 2,
    		title: '',
    		shade: 0.8,
            area: ['600px', '420px'],
            content: '/activity/writeBrand?activityId=' + activityId +'&taskreport=' +taskreport
    	});
    }
	
	function upmonitorShow(activityId,taskreport) { //上刊监测
    	layer.open({
    		type : 2,
    		title: '',
    		shade: 0.8,
            area: ['600px', '420px'],
            content: '/activity/writeBrand?activityId=' + activityId +'&taskreport=' +taskreport
    	});
    }
    function durationMonitorShow(activityId,taskreport) { //投放期间监测
    	layer.open({
    		type : 2,
    		title: '',
    		shade: 0.8,
            area: ['600px', '420px'],
            content: '/activity/writeBrand?activityId=' + activityId +'&taskreport=' +taskreport
    	});
    }
    function zhuijiaMonitorShow(activityId,taskreport) { //追加监测
    	layer.open({
    		type : 2,
    		title: '',
    		shade: 0.8,
            area: ['600px', '420px'],
            content: '/activity/writeBrand?activityId=' + activityId +'&taskreport=' +taskreport
    	});
    }
    function downMonitorShow(activityId,taskreport) { //下刊监测
    	layer.open({
    		type : 2,
    		title: '',
    		shade: 0.8,
            area: ['600px', '420px'],
            content: '/activity/writeBrand?activityId=' + activityId +'&taskreport=' +taskreport
    	});
    }
</script>
