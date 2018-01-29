<#assign webTitle="监测管理-任务详情" in model> <#assign webHead in model>
</#assign> <@model.webhead />
<!-- 头部 -->
<@model.webMenu current="监测管理" child="任务指派" />

<!-- 特色内容 -->
<div class="main-container" style="height: auto;">
	<div>
		<a href="/task/list">监测管理</a> <a href="/task/unassign">-->任务指派</a>
	</div>
	<div
		style="margin-top: 20px; margin-bottom: 10px; color: gray; font-size: 20px">
		任务详情:</div>
	<div class="details-wrap"></div>

	<div class="submitdetails-wrap"></div>
</div>
<script type="text/javascript"
	src="http://ottstatic2.taiyiplus.com/js/jquery-2.1.4.min.js"></script>
<script type="text/javascript">
	Date.prototype.Format = function (fmt) { //author: meizz   
	    var o = {  
	        "M+": this.getMonth() + 1, //月份   
	        "d+": this.getDate(), //日   
	        "H+": this.getHours(), //小时   
	        "m+": this.getMinutes(), //分   
	        "s+": this.getSeconds(), //秒   
	        "q+": Math.floor((this.getMonth() + 3) / 3), //季度   
	        "S": this.getMilliseconds() //毫秒   
	    };  
	    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));  
	    for (var k in o)  
	    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));  
	    return fmt;  
	}  
	
	$(function() {
		var html='';
		var submithtml='';
		$.ajax({
			url : "/task/details?task_Id=" + ${Request.taskId} + "&media_Name=" + '${Request.mediaName}',
			type : "get",
			dataType : "json",
			success : function(data) {
				if (data.success) {
					var vo = data.vo;
					var list=data.list;
					html += '<div>广告活动时间:'+ vo.activityName + '</div>'
							+'<div>投放时间:'+new Date(vo.startTime).Format("yyyy-MM-dd")+'至'+new Date(vo.endTime).Format("yyyy-MM-dd")+'</div>'
							+'<div>投放地区:'+vo.province+vo.city+'</div>'
							+'<div>投放广告位:'+vo.region+vo.street+'</div>'
							+'<div>监测时间段:'+new Date(vo.monitorsStart).Format("yyyy-MM-dd")+'-'+new Date(vo.monitorsEnd).Format("yyyy-MM-dd")+'</div>'
							+'<div>媒体名称:'+vo.mediaName+'</div>'
							+'<div>监测次数:'+vo.monitorsCount+'(上刊('+new Date(vo.monitorsStart).Format("yyyy-MM-dd")+'),下刊('+new Date(vo.monitorsEnd).Format("yyyy-MM-dd")+')'+')'+'</div>'
							+'<div>投放品牌:'+vo.brand+'</div>'
							+'<div>创建时间:'+new Date(vo.createTime).Format("yyyy-MM-dd")+'</div>'
							+'<div>样例:<img src="'+vo.samplePicUrl+'"></img></div>';
					
					list.map(function(item, index) {
							submithtml+='<div style="margin-top: 20px; margin-bottom: 10px; color: gray; font-size: 20px">提交详情:</div>'
								+'<div>广告活动名称:'+item.activityName+'</div>'	
								+'<div>提交时间:'+new Date(item.createTime).Format("yyyy-MM-dd HH:mm")+'</div>'
								+'<div>提交地区:'+item.province+item.city+item.region+item.street+'</div>'
								+'<div>监测时间:上刊('+new Date(item.monitorsStart).Format("yyyy/MM/dd")+')</div>'
								+'<div>问题反馈:'+item.problem+'</div>'
								+'<div>执行状态:'+item.status+'</div>'
								+'<div>提交照片:<img src="'+item.pic_url1+'"></img><img src="'+item.pic_url2+'"></img><img src="'+item.pic_url3+'"></img><img src="'+item.pic_url4+'"></img></div>'
					});
				}
				$('.details-wrap').html(html);
				$('.submitdetails-wrap').html(submithtml);
			}
		});
	});
	
	
</script>
<@model.webend />
