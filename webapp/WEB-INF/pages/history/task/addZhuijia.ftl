<link rel="stylesheet" type="text/css" href="${model.static_domain}/css/new_main.css">
<link rel="stylesheet" type="text/css" href="${model.static_domain}/css/icon_fonts.css">
	<style type="text/css">
		html, body{ min-width: auto; }
		.basic-info .bd .a-title{ width: 100px; }
        .basic-info .bd td{ padding: 0 10px;}
		.role-nav-authority li{ float: left; overflow: hidden; margin-bottom: 5px; height: 25px;}
		.role-nav-authority li label input[type=checkbox]{ width: 15px; height: 15px; margin: 2px 3px 0 0; vertical-align: text-top;}
		.basic-info tr .a-title{padding:10px;}
	</style>

<div class="basic-info">
	<div class="bd">
		<table width="100%" cellpadding="0" cellspacing="0" border="0" class="tablesorter">
			<tbody>
			    <input type="hidden" name="seatId" id="seatId" value="${(seatId)?if_exists}"/>
			    <input type="hidden" name="startTime" id="startTime" value="${(startTime)?if_exists}"/>
			    <input type="hidden" name="endTime" id="endTime" value="${(endTime)?if_exists}"/>
			    <input type="hidden" name="monitorTime" id="monitorTime" value="${(monitorTime)?if_exists}"/>
			    <input type="hidden" name="auditTime" id="auditTime" value="${(auditTime)?if_exists}"/>
			    <input type="hidden" name="activityId" id="activityId" value="${(activityId)?if_exists}"/>
				<tr>
					<td class="a-title">追加监测报告时间：</td>
					<td>
					    <div class="ll inputs-date" id="zhuiJiaTaskTime" >
	                        <div class="date">
	                            <input id="zhuiJia-Wdate" class="zhuiJia-Wdate Wdate" type="text" name="zhuiJia-Wdate" autocomplete="off">
	                        </div>
	                    </div>
						<br><br><span id="zhuiJiaTaskTimeTip"></span>
					</td>
				</tr>
				<tr>
					<td class="a-title">积分：</td>
					<td>
					    <div>
                        	<input type="text" id="zhuijiaMonitorTaskPoint"  name="zhuijiaMonitorTaskPoint" style="width: 100px;" value="0" autocomplete="off" class="form-control point"> 
            				<span id="zhuijiaMonitorTaskPointTip"></span>
                    	</div>
					</td>
				</tr>
				<tr>
					<td  class="a-title">
                    	金额：
                    </td>
					<td>
                    	<div>
                    		<input type="text" id="zhuijiaMonitorTaskMoney" name="zhuijiaMonitorTaskMoney" value="0.00" style="width: 100px;" autocomplete="off" class="form-control money"> 
                    		<span id="zhuijiaMonitorTaskMoneyTip"></span>
                    	</div>
                    </td>
				</tr>
				<tr>
					<td class="a-title">&nbsp;</td>
					<td>
						<button type="button" class="btn btn-red" autocomplete="off" id="zhuiJiaSubmit">提　交</button>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
</div>
<script type="text/javascript" src="${model.static_domain}/js/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="${model.static_domain}/js/layer/layer.js"></script>
<script type="text/javascript" src="${model.static_domain}/js/common.js"></script>
<!-- 下拉 -->
<link href="${model.static_domain}/js/select/jquery.searchableSelect.css" rel="stylesheet">
<script src="${model.static_domain}/js/select/jquery.searchableSelect.js"></script>
<!-- 时期 -->
<link href="${model.static_domain}/js/date/daterangepicker.css" rel="stylesheet">
<script type="text/javascript" src="${model.static_domain}/js/date/moment.min.js"></script>
<script type="text/javascript" src="${model.static_domain}/js/date/jquery.daterangepicker.js"></script>
<script type="text/javascript" src="${model.static_domain}/js/date.js"></script>
<!-- formValidator -->
<link type="text/css" rel="stylesheet" href="${model.static_domain}/js/formValidator/style/validator.css"></link>
<script type="text/javascript" src="${model.static_domain}/js/formValidator/formValidator-4.0.1.js"></script>
<script>
$(function() {
	function getDate(date) {
    	var month = (date.getMonth() + 1).toString().length < 2 ? '0' + (date.getMonth() + 1).toString() : (date.getMonth() + 1).toString()
   		var day = date.getDate().toString().length < 2 ? '0' + date.getDate().toString() : date.getDate().toString()
   		return date.getFullYear()+ '-' + month + '-' + day
    }
	
	

	// 新建账户处理
	$.formValidator.initConfig({
		validatorGroup:"2",
        submitButtonID: "zhuiJiaSubmit",
        debug: false,
        submitOnce: true,
        errorFocus: false,
        onSuccess: function(){
	        var reportTime = $("#zhuiJia-Wdate").val();
	        var seatId = $("#seatId").val();
	        var activityId = $("#activityId").val();
	        var zhuijiaMonitorTaskPoint = $("#zhuijiaMonitorTaskPoint").val();
	        var zhuijiaMonitorTaskMoney = $("#zhuijiaMonitorTaskMoney").val();
	        console.log(222, reportTime, seatId)
            $.ajax({
                url: "/activity/zhuijiaTask",
                type: "post",
                data: {
                	"reportTime": reportTime,
                    "seatIds": seatId,
                    "activityId": activityId,
                    "zhuijiaMonitorTaskPoint" :zhuijiaMonitorTaskPoint,
                    "zhuijiaMonitorTaskMoney" :zhuijiaMonitorTaskMoney
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
                    	var msg = "保存成功";
                    	layer.confirm(msg, {
							icon: 1,
							closeBtn: 0,
							btn: ['确定'] //按钮
						}, function(){
							window.parent.location.reload();
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
        },
        submitAfterAjaxPrompt: '有数据正在异步验证，请稍等...'
    });
	// 追加监测任务出报告时间的校验
    $('#zhuiJia-Wdate').formValidator({
           validatorGroup: '2',
           tipID:"zhuiJiaTaskTimeTip",
           onShow:"",
           onFocus:"请选择追加监测出报告时间",
           onCorrect:""
       }).regexValidator({
           regExp:"^\\S+$",
           onError:"请输入追加监测出报告时间"
       }).functionValidator({
           	fun: function(val, ele){
           		// 1、是活动开始时间的至少m+n天以后  2、在活动结束时间之前
           		var startDate = new Date($('#startTime').val())
           		var m = parseInt($('#monitorTime').val())
           		var n = parseInt($('#auditTime').val())
           		var after = m + n
           		var afterDay = new Date(startDate.getTime() + 24 * 60 * 60 * 1000 * after)
           		var afterDate = getDate(afterDay)
           		console.log(1111, $('#endTime').val(), val, afterDate)
           		if($('#endTime').val() < val) {
           			return '出报告时间是在活动结束时间之前'
           		}else if (val < afterDate){
           			return '出报告时间是活动开始时间的至少' + after + '天以后' ;
           		}else{
           			return true;
           		}
           	}
      });
      
    $('#zhuiJiaTaskTime').dateRangePicker({
 	   singleDate: true,
 	   showShortcuts: false,
        getValue: function () {
            return $(this).find('.zhuiJia-Wdate').val()
        },
        setValue: function (s) {
           $(this).find('.zhuiJia-Wdate').val(s)
		   $(this).find('.zhuiJia-Wdate').blur()
        }
    });
    
	//追加监测任务积分值
    $("#zhuijiaMonitorTaskPoint").formValidator({
        validatorGroup:"2",
        tipID:"zhuijiaMonitorTaskPointTip",
        onShow: "　",
        onFocus: "请输入追加监测任务积分",
        onCorrect: ""
    }).regexValidator({
    	regExp:"^[0-9]*$",
    	onError:"积分数值输入有误"
    }).inputValidator({
        min: 1,
        max: 60,
        onError: "积分数值不能为空，请输入"
    });
    
    
    //追加监测任务金额值
    $("#zhuijiaMonitorTaskMoney").formValidator({
        validatorGroup:"2",
        tipID:"zhuijiaMonitorTaskMoneyTip",
        onShow: "　",
        onFocus: "请输入追加监测任务金额",
        onCorrect: ""
    }).regexValidator({
    	regExp:"^[0-9]{1}\\d\*(\\.\\d{1,2})?$",
    	onError:"金额数值输入有误"
    }).inputValidator({
        min: 1,
        max: 60,
        onError: "金额数值不能为空，请输入"
    });
});
            
</script>