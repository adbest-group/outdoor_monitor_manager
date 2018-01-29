<#assign webTitle="监测管理-任务详情" in model> 
<#assign webHead in model>
</#assign> 
<@model.webhead />
<!-- 头部 -->
<@model.webMenu current="监测管理" child="监测管理" />
<link rel="stylesheet" type="text/css" href="http://ottstatic2.taiyiplus.com/css/new_main.css">
<link rel="stylesheet" type="text/css" href="http://ottstatic2.taiyiplus.com/css/icon_fonts.css">

<!-- 特色内容 -->
<div class="main-container">
    <div class="clearfix">
        <div class="main-box">
            <div class="crumb-nav">
                <a href="/task/list">监测管理</a>　>　任务指派
            </div>
            <div class="bd new-active">
                <div class="hd mt-10"><h3>任务详情</h3></div>
                <div class="bd">
                    <div class="details-wrap" style="font-size: 15px"></div>
                </div>
            </div>
        </div>
    </div>
    
    <div class="clearfix">
        <div class="main-box">
            <div class="bd new-active">
           	 	<div class="hd mt-10"><h3>提交详情</h3></div>
                <div class="bd">
                    <div class="submitdetails-wrap" style="font-size: 15px"></div>
                </div>
            </div>
        </div>
    </div>
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
		$(window).resize(function() {
	        var h = $(document.body).height() - 115;
	        $('.main-container').css('height', h);
	    });
		
		$(window).resize();
		var html='';
		var submithtml='';
		$.ajax({
			url : "/task/details?task_Id=" + ${Request.taskId},
			type : "get",
			dataType : "json",
			success : function(data) {
				if (data.success) {
					var vo = data.vo;
					var list=data.list;
					html += '<p>广告活动时间：'+ vo.activityName + '</p>'
							+'<p>投放时间：'+new Date(vo.startTime).Format("yyyy-MM-dd")+'至'+new Date(vo.endTime).Format("yyyy-MM-dd")+'</p>'
							+'<p>投放地区：'+vo.province+vo.city+vo.region+vo.street+'</p>'
							+'<p>投放广告位：'+vo.name+'</p>'
							+'<p>监测时间段：'+new Date(vo.monitorsStart).Format("yyyy-MM-dd")+'-'+new Date(vo.monitorsEnd).Format("yyyy-MM-dd")+'</p>'
							+'<p>媒体名称：'+vo.mediaName+'</p>'
							+'<p>监测次数：'+vo.monitorsCount+'(上刊('+new Date(vo.monitorsStart).Format("yyyy-MM-dd")+')，下刊('+new Date(vo.monitorsEnd).Format("yyyy-MM-dd")+')'+')'+'</p>'
							+'<p>投放品牌：'+vo.brand+'</p>'
							+'<p>创建时间：'+new Date(vo.createTime).Format("yyyy-MM-dd")+'</p>'
							+'<p>样例：<img style="vertical-align: top" src="'+vo.samplePicUrl+'"></img></p>';
					
					list.map(function(item, index) {
							submithtml+='<p>广告活动名称：'+item.activityName+'</p>'	
								+'<p>提交时间：'+new Date(item.createTime).Format("yyyy-MM-dd HH:mm")+'</p>'
								+'<p>提交地区：'+item.province+item.city+item.region+item.street+'</p>'
								+'<p>监测时间：上刊('+new Date(item.monitorsStart).Format("yyyy/MM/dd")+')</p>'
								+'<p>问题反馈：'+item.problem+'</p>'
								+'<p>执行状态：'+item.status+'</p>'
								+'<p>提交照片：</br><img style="vertical-align: top;width:350px;" src="'+item.pic_url1+'"></img><img style="vertical-align: top;width:350px" src="'+item.pic_url2+'"></img><img style="vertical-align: top;width:350px" src="'+item.pic_url3+'"></img><img style="vertical-align: top;width:350px" src="'+item.pic_url4+'"></img></p>'
					});
				}
				$('.details-wrap').html(html);
				$('.submitdetails-wrap').html(submithtml);
			}
		});
	});
	
	
</script>
<@model.webend />
