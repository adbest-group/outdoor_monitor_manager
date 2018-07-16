<!-- 特色内容 -->
<style type="text/css">
    .basic-info .bd .a-title {
        width: 120px;
    }

    img.demo {
        width: 50px;
    }
</style>
<#assign editMode=false/>
<#-- <@shiro.hasRole name="customer"> -->
    <#if !activity?exists||activity.status = 1>
        <#assign editMode=true />
    </#if>
<#-- </@shiro.hasRole> -->

<div class="main-container" style="height: auto;">
    <div class="clearfix">
        <div class="main-box basic-info">
            <div class="bd">
                <form id="subForm" method="post">
                	<input type="hidden" id="monitorTime" value="${monitorTime?if_exists}"/>
                	<input type="hidden" id="auditTime" value="${auditTime?if_exists}"/>
                
                    <input type="hidden" id="id" value=""/>
                    <table width="100%" cellpadding="0" cellspacing="0" border="0" type="">
                        <tbody>
						
						<#if usertype?exists && usertype != 2 && !activity?exists>
							<tr>
								<td class="a-title"><font class="s-red">*</font>广告主：</td>
								<td>
									<div class="select-box select-box-140 un-inp-select ll">
									<select style="width: 250px;" name="customerId" id="customerId" class="form-control select">
										<option value="">请选择广告主</option>
										<@model.showAllCustomerAvailableOps value="<#if (activity?exists&&activity.userId?exists)>activity.userId</#if>"/>
				                    </select>
									</div>
                                    <span id="customerIdTip"></span>
								</td>
							</tr>
						</#if>

                        <tr>
                            <td class="a-title"><font class="s-red">*</font>广告活动名称：</td>
                            <td>
                                <input type="text" id="activityName" ${editMode?string("","disabled")} name="activityName" value="" autocomplete="off" class="form-control">
                                <span id="activityNameTip"></span>
                                <#-- <input type="button" id="btnDemo" class="btn btn-green" value="演示专用"/> -->
                            </td>
                        </tr>

						<#-- 
						<tr>
							<td class="a-title"><font class="s-red">*</font>客户类型：</td>
							<td>
                                <select name="customerTypeId" ${editMode?string("","disabled")} class="searchable-select-holder" id="customerTypeId">
		                            <option value="">请选择</option>
		                            <@model.showAllCustomerTypeOps value="<#if (activity?exists&&activity.customerTypeId?exists)>activity.customerTypeId</#if>"/>
		                        </select>
                                <span id="customerTypeIdTip"></span>
                            </td>
		                </tr>
		                 -->

						<!-- 活动开始时间是今天的至少m+n+1天以后 -->
                        <tr>
                            <td class="a-title"><font class="s-red">*</font>活动时间：</td>
                            <td>
                                <div class="ll inputs-date" id="activityTime">
                                    <div class="date">
                                        <input id="dts" ${editMode?string("","disabled")} class="activityTime-Wdate Wdate" type="text"> -
                                        <input id="dt" ${editMode?string("","disabled")} class="activityTime-Wdate Wdate" type="text">
                                    </div>
                                </div>
                                <span style="margin-left:10px;" id="dateTip"></span>
                            </td>
                        </tr>
                        
                        <!-- 与活动开始时间同步, 不能改 -->
                        <tr>
                            <td class="a-title"><font class="s-red">*</font>上刊报告时间：</td>
                            <td>
                                <div class="ll inputs-date">
                                    <div class="date">
                                        <input id="upTaskTime" disabled class="Wdate" type="text">
                                    </div>
                                </div>
                                <span style="margin-left:10px;" id="upTaskTimeTip"></span>
                            </td>
                        </tr>
                        
                        <!-- 1、是活动开始时间的至少m+n天以后  2、在活动结束时间之前 -->
                        <tr>
                            <td class="a-title"><font class="s-red">*</font>上刊监测报告时间：</td>
                            <td>
                                <div class="ll inputs-date" id="upMonitorTaskTime" >
                                    <div class="date">
                                        <input id="upMonitor-Wdate" ${editMode?string("","disabled")} class="upMonitor-Wdate Wdate" type="text">
                                    </div>
                                </div>
                                <span style="margin-left:10px;" id="upMonitorTaskTimeTip"></span>
                            </td>
                        </tr>
                        
                        <!-- 1、是活动开始时间的至少m+n天以后  1、在活动结束时间之前  2、扩展 -->
                        <tr class='last'>
                            <td class="a-title"><font class="s-red">*</font>投放期间监测报告时间：</td>
                            <td>
                                <div class="ll inputs-date durationMonitorTaskTime" id="durationTime0">
                                    <div class="date">
                                        <input id="durationMonitorTaskTime0" ${editMode?string("","disabled")} class="durationMonitor-Wdate Wdate" type="text">
                                    </div>
                                </div>
                                <span style="margin-left:10px;" id="durationMonitorTaskTimeTip0"></span> &nbsp;&nbsp;<#if editMode><input class="btn btn-primary" type='button' id="addDurationMonitor" value='添加'></#if>
                            </td>
                        </tr>
                        
                        <!-- 与活动结束时间同步 -->
                        <tr class='down'>
                            <td class="a-title"><font class="s-red">*</font>下刊监测报告时间：</td>
                            <td>
                                <div class="ll inputs-date">
                                    <div class="date">
                                        <input id="downMonitorTaskTime" disabled class="Wdate" type="text">
                                    </div>
                                </div>
                                <span style="margin-left:10px;" id="downMonitorTaskTimeTip"></span>
                            </td>
                        </tr>

				<#-- 
                <tr id="upMonitorLastDaysTr">
                    <td class="a-title"><font class="s-red">*</font>上刊监测任务<br/>可持续天数：</td>
                    <td>
                        <input type="text" style="width:50px;text-align:right;" id="upMonitorLastDays" name="upMonitorLastDays" value="2" autocomplete="off" class="form-control">
                        <span id="upMonitorLastDaysTip"></span>
                    </td>
                </tr>

                <tr id="durationMonitorLastDaysTr">
                    <td class="a-title"><font class="s-red">*</font>投放期间监测任务<br/>可持续天数：</td>
                    <td>
                        <input type="text" style="width:50px;text-align:right;" id="durationMonitorLastDays" name="durationMonitorLastDays" value="2" autocomplete="off" class="form-control">
                        <span id="durationMonitorLastDaysTip"></span>
                    </td>
                </tr>

                <tr id="downMonitorLastDaysTr">
                    <td class="a-title"><font class="s-red">*</font>下刊监测任务<br/>可持续天数：</td>
                    <td>
                        <input type="text" style="width:50px;text-align:right;" id="downMonitorLastDays" name="downMonitorLastDays" value="3" autocomplete="off" class="form-control">
                        <span id="downMonitorLastDaysTip"></span>
                    </td>
                </tr>
                 -->

						<#--<#if editMode>
						<tr>
                            <td class="a-title"><font class="s-red">*</font>投放地区(筛选广告位)：</td>
                            <td>  -->
                        
                            <#--<input type="text" style="width:100px;"  id="province" name="province" value="浙江省" autocomplete="off" class="form-control">-->
                            <#--<a class="addBtn" href="javascript:;" id="resource_sel">选择</a>-->
                              
						<#--   <div id="demo3" class="citys">
                                        <select name="province" ${editMode?string("","disabled")} class="searchable-select-holder" id="province">
                                        </select>
                                        <select name="city" ${editMode?string("","disabled")} class="searchable-select-holder" id="city">
                                        </select>
                                        <select name="region" ${editMode?string("","disabled")} class="searchable-select-holder" id="region">
                                        </select>
                                        <select name="street" ${editMode?string("","disabled")} class="searchable-select-holder" id="street">
                                        </select>
                                </div>
                                <span style="margin-left:10px;" id="areaTip"></span>
                            </td>
                        </tr>
						</#if> -->
						<#-- <tr>
                            <td class="a-title"><font class="s-red">*</font>媒体主：</td>
                            <td id="mediaTd"> -->
                        
                                <#--<label>-->
                                    <#--<input type="checkbox" name="media" id="media0" value="1" checked> 媒体1-->
                                <#--</label>-->
                                <#--<label>-->
                                    <#--<input type="checkbox" name="media" id="media0" value="2" checked> 媒体2-->
                                <#--</label>-->
                                <#--<label>-->
                                    <#--<input type="checkbox" name="media" id="media0" value="3" checked> 媒体3-->
                                <#--</label>-->
                                <#--<label>-->
                                    <#--<input type="checkbox" name="media" id="media0" value="4" checked> 媒体4-->
                                <#--</label>-->
                          
						<#--   </td>
                        </tr> -->
						<tr>
							<td class="a-title"><font class="s-red">*</font>广告投放画面：</td>
							<td>
								<#if (editMode && activity.status==1)!true>
									<input type="hidden" id="img-demo-bak"/>
                       					<div class="btn-file addBtn" id="resource_sel" style="width:74px;height:28px;top:0px;cursor:pointer;line-height:28px;padding:0px;color:#fff">
										上传
										<input type="file" id="img-demo" name="file" unselectable="on" onchange="uploadPic('img-demo')">
									</div> <span id="img-demoTip"></span>
								</#if>
                   			</td>
						</tr>
						<tr>
                    		<td class="a-title">&nbsp;</td>
                    		<td>
                    			<img src="" id="img-demo-img" width="280" alt="请上传图片"/>
                    		</td>
                		</tr>

                        <tr>
                            <td class="a-title"><font class="s-red">*</font>广告位监测：</td>
                            <td>
                                <#if editMode>
                                    <a class="addBtn" href="javascript:;" id="add-adseat">选择广告位</a>
                                </#if>
                            </td>
                        </tr>

                        <tr>
                            <td colspan="2" >
                                <div class="data-report" style="margin: 0px;">
                                    <div id="as-container" class="bd" style="padding:0px;">
                                    <#--<table width="100%" cellpadding="0" cellspacing="0" border="0" class="tablesorter" id="plan">-->
                                            <#--<thead>-->
                                            <#--<tr>-->
                                                <#--<th>序号</th>-->
                                                <#--<th>广告位</th>-->
                                                <#--<th>媒体</th>-->
                                                <#--<th>投放品牌</th>-->
                                                <#--<th>监测时间段</th>-->
                                                <#--<th>监测次数</th>-->
                                                <#--<th>监测时间</th>-->
                                                <#--<th>广告投放画面</th>-->
                                                <#--<th>操作</th>-->
                                            <#--</tr>-->
                                            <#--</thead>-->
                                            <#--<tbody>-->
                                                <#--<tr>-->
                                                    <#--<td width="30">1</td>-->
                                                    <#--<td>新联路1号灯箱</td>-->
                                                    <#--<td>媒体1</td>-->
                                                    <#--<td>可口可乐</td>-->
                                                    <#--<td>2018-01-01至2018-02-01</td>-->
                                                    <#--<td>3</td>-->
                                                    <#--<td>上刊、投放期间、下刊</td>-->
                                                    <#--<td><img src="${model.static_domain}/images/300x250.gif" class="demo"/></td>-->
                                                    <#--<td>-->
                                                        <#--<a href="javascript:;">详情</a>-->
                                                        <#--<a href="javascript:;">删除</a>-->
                                                    <#--</td>-->
                                                <#--</tr>-->
                                            <#--</tbody>-->
                                        <#--</table>-->
                                    </div>
                                </div>

                            </td>
                        </tr>

                        <tr>
                            <td class="a-title">&nbsp;</td>
                            <td>
                                <#if editMode>
                                    <input type="button" id="btnSave" class="btn btn-red" value="　保 存　"/>
                                </#if>
                                    <input type="button" id="btnBack" class="btn btn-primary" value="　返 回　"/>

                            </td>
                        </tr>
                        </tbody>
                    </table>
                </form>
            </div>
        </div>
    </div>
</div>
</div>
</div>
</div>

<script type="text/javascript" src="${model.static_domain}/js/jquery-2.1.4.min.js"></script>
<!-- 下拉 -->
<link href="${model.static_domain}/js/select/jquery.searchableSelect.css" rel="stylesheet">
<script src="${model.static_domain}/js/select/jquery.searchableSelect.js"></script>
<script src="${model.static_domain}/js/ajaxfileupload.js"></script>
<!-- 时期 -->
<link href="${model.static_domain}/js/date/daterangepicker.css" rel="stylesheet">
<script type="text/javascript" src="${model.static_domain}/js/date/moment.min.js"></script>
<script type="text/javascript" src="${model.static_domain}/js/date/jquery.daterangepicker.js"></script>
<script type="text/javascript" src="${model.static_domain}/js/date.js"></script>
<!-- 图片缩放 -->
<script type="text/javascript" src="${model.static_domain}/js/jquery.resize.js"></script>
<!-- formValidator -->
<link type="text/css" rel="stylesheet" href="${model.static_domain}/js/formValidator/style/validator.css"></link>
<script type="text/javascript" src="${model.static_domain}/js/formValidator/formValidator-4.0.1.js"></script>
<script type="text/javascript" src="/static/js/jquery.citys.js"></script>

<script type="text/javascript">
    var editMode = ${editMode?string("true","false")}
    var mod_activity_seat = null;
    var del_activity_seats = [];
    <#if activity?exists>
	    var activity = {
	        "id":${activity.id},
	        "activityName": '${activity.activityName}',
	        <#-- "customerTypeId": '${activity.customerTypeId}', -->
	        "dts": "${activity.startTime?string('yyyy-MM-dd')}",
	        "dt": "${activity.endTime?string('yyyy-MM-dd')}",
	        "samplePicUrl": '${activity.samplePicUrl!""}',
	        "upTaskTime": '${activity.upTaskTime!""}',
	        "upMonitorTaskTime": '${activity.upMonitorTaskTime!""}',
	        "durationMonitorTaskTime": '${activity.durationMonitorTaskTime!""}',
	        "downMonitorTaskTime": '${activity.downMonitorTaskTime!""}',
	        "zhuijiaMonitorTaskTime": '${activity.zhuijiaMonitorTaskTime!""}'
	    }
	    $("#img-demo-img").attr("src",activity.samplePicUrl);//广告投放画面图片地址
	    $("#img-demo-bak").attr("src",activity.samplePicUrl);//广告投放画面图片地址
	    
	    $("#upTaskTime").val(activity.upTaskTime); //上刊任务时间
	    $("#upMonitor-Wdate").val(activity.upMonitorTaskTime); //上刊监测任务时间
	    $("#downMonitorTaskTime").val(activity.downMonitorTaskTime); //下刊监测任务时间
	    
	    //拼接显示 投放期间监测任务
	    var result = activity.durationMonitorTaskTime.split(",");
	    //回显时添加新的 投放期间监测任务
		for (var i = 1; i < result.length; i++) {
		  	var index = $('.last').length;
	    	var str = '<tr class="last"><td class="a-title"><font class="s-red">*</font>投放期间监测报告时间：</td><td><div class="ll inputs-date durationMonitorTaskTime" id="durationTime'+index+'"><div class="date"><input id="durationMonitorTaskTime'+index+'" ${editMode?string("","disabled")} class="durationMonitor-Wdate Wdate" type="text"></div></div><span style="margin-left:10px;" id="durationMonitorTaskTimeTip'+index+'"></span></td></tr>'
	    	$('.last:last').after(str);
		}
		for (var i = 0; i < result.length; i++) {
			$("#durationMonitorTaskTime" + i).val(result[i]); //投放期间监测任务时间
		}
		
		//拼接显示 追加监测任务
		if(activity.zhuijiaMonitorTaskTime != null && activity.zhuijiaMonitorTaskTime != "") {
			var resultZhuijia = activity.zhuijiaMonitorTaskTime.split(",");
			//回显时添加追加监测任务
			for (var i = 0; i < resultZhuijia.length; i++) {
			  	var index = $('.zhuijia').length;
		    	var str = '<tr class="zhuijia"><td class="a-title"><font class="s-red">*</font>追加监测任务出报告时间：</td><td><div class="ll inputs-date zhuijiaMonitorTaskTime" id="zhuijiaTime'+index+'"><div class="date"><input id="zhuijiaMonitorTaskTime'+index+'" ${editMode?string("","disabled")} class="zhuijiaMonitor-Wdate Wdate" type="text"></div></div><span style="margin-left:10px;" id="zhuijiaMonitorTaskTimeTip'+index+'"></span></td></tr>'
		    	$('.down').after(str);
			}
			for (var i = 0; i < resultZhuijia.length; i++) {
				$("#zhuijiaMonitorTaskTime" + i).val(resultZhuijia[i]); //追加监测任务时间
			}
		}
	    
	    var activity_seats = [
	        <#if (activity.adActivityAdseatVos?exists && activity.adActivityAdseatVos?size>0) >
	            <#list activity.adActivityAdseatVos as seat>
	                {
	                    id: ${seat.id},
	                    mediaId: "${seat.mediaId!""}",
	                    mediaName: "${seat.mediaName!""}",
	                    parentName: "${seat.parentName!""}",
	                    secondName: "${seat.secondName!""}",
	                    road: "${seat.road!""}",
	                    location: "${seat.location!""}",
	                    area: "${vm.getCityName(seat.province)!""} ${vm.getCityName(seat.city!"")}",
	                    seatId: "${seat.adSeatId!""}",
	                    seatName: "${seat.adSeatName!""}",
	                    startDate: "${seat.monitorStart?string("yyyy-MM-dd")!""}",
	                    endDate: "${seat.monitorEnd?string("yyyy-MM-dd")!""}",
	                    <#-- brand: "${seat.brand!""}", -->
	                    upMonitor: "${seat.upMonitor!""}",
	                    downMonitor: "${seat.downMonitor!""}",
	                    durationMonitor: "${seat.durationMonitor!""}",
	                    upMonitorLastDays: "${seat.upMonitorLastDays!"3"}",
	                    downMonitorLastDays: "${seat.downMonitorLastDays!"3"}",
	                    durationMonitorLastDays: "${seat.durationMonitorLastDays!"3"}",
	                    <#-- monitorCount: "${seat.monitorCount!""}", -->
	                    samplePicUrl: "${seat.samplePicUrl!""}"
	                }<#if seat_has_next>,</#if>
	            </#list>
	        </#if>];
	    var activity_meias = [
	        <#if (activity.activityMedias?exists && activity.activityMedias?size>0) >
	            <#list activity.activityMedias as media>
	            ${media.mediaId}<#if media_has_next>,</#if>
	            </#list>
	        </#if>]
    <#else>
	    var activity = null;
	    var activity_seats = [];
	    var activity_meias = [];
    </#if>
    $(function () {
        $(".nav-sidebar>ul>li").on("click", function () {
            $(".nav-sidebar>ul>li").removeClass("on");
            $(this).addClass("on");
        });
    });

    $(function () {
        $(window).resize();
    });

    $(window).resize(function () {
        var h = $(document.body).height() - 115;
        $('.main-container').css('height', h);
    });

    $(function () {
        window.$province = $("#province");
        window.$city = $("#city");
        window.$region = $("#region");
        window.$street = $("#street");
        window.$dts = $("#dts");
        window.$dt = $("#dt");

        // 下拉
        $('.select').searchableSelect();
        $('#customerId').next().find('.searchable-select-input').css('display', 'block');

        if(editMode) {
            //日期
            $('#activityTime').dateRangePicker({
                separator: ' 至 ',
                showShortcuts: false,
                getValue: function () {
                    if ($('#dts').val() && $('#dt').val())
                        return $('#dts').val() + ' 至 ' + $('#dt').val();
                    else
                        return '';
                },
                setValue: function (s, s1, s2) {
                    $('#dts').val(s1);
                    $('#dt').val(s2);
					$('.activityTime-Wdate').blur()
					$('#upTaskTime').val(s1);
                    $('#downMonitorTaskTime').val(s2);
                }
            });
            
            $('#upMonitorTaskTime').dateRangePicker({
            	   singleDate: true,
            	   showShortcuts: false,
                   getValue: function () {
                       return $(this).find('.upMonitor-Wdate').val()
                   },
                   setValue: function (s) {
                      $(this).find('.upMonitor-Wdate').val(s)
   					  $(this).find('.upMonitor-Wdate').blur()
                   }
              });
            
            <#-- 
            $('#durationTime0').dateRangePicker({
           	   singleDate: true,
           	   showShortcuts: false,
                  getValue: function () {
                      return $(this).find('#durationMonitorTaskTime0').val()
                  },
                  setValue: function (s) {
                     $(this).find('#durationMonitorTaskTime0').val(s)
  					  $(this).find('#durationMonitorTaskTime0').blur()
                  }
             });
            -->
            
            $('.durationMonitor-Wdate').each(function(index){
	    		var topId = "durationTime" + index;
	    		var lastId = "durationMonitorTaskTime" + index;
	    		$('#' + topId).dateRangePicker({
	            	   singleDate: true,
	            	   showShortcuts: false,
	                   getValue: function () {
	                       return $(this).find('#' + lastId).val()
	                   },
	                   setValue: function (s) {
	                      $(this).find('#' + lastId).val(s)
	   					  $(this).find('#' + lastId).blur()
	                   }
	             });
	    	})
        }

        $("#add-adseat").click(function () {
            <#-- if($("#dts").val().length<1||$("#province").val().length<1||$("input:checkbox:checked").length<1){ -->
            if($("#dts").val().length<1){
                layer.alert("请先确认活动时间");
                return;
            }
            mod_activity_seat = null;

            delDataArr = []
            modDataArr = []
            
            layer.open({
                type: 2,
                title: '新增广告位监测',
                shade: 0.8,
                area: ['1020px', '600px'],
                content: '/customer/activity/adseat/select?startDate=' + $('#dts').val() + '&endDate=' + $('#dt').val() + '&seatIds=' + getSeatIds() //iframe的url
            });
        });

        /*获取城市  */
        var $town = $('#demo3 select[name="street"]');
        var townFormat = function (info) {
            $town.hide().empty();
            if (info['code'] % 1e4 && info['code'] < 7e5) { //是否为“区”且不是港澳台地区
                $.ajax({
                    url: 'http://passer-by.com/data_location/town/' + info['code']
                    + '.json',
                    dataType: 'json',
                    success: function (town) {
                        $town.show();
                        for (i in town) {
                            $town.append('<option value="' + i + '" <#if adSeatInfo?exists&&adSeatInfo.street?exists>'+ (i ==${adSeatInfo.street} ? "selected" : "")+'</#if>>' + town[i]
                                    + '</option>');
                        }
                    }
                });
            }
        };
        $('#demo3').citys({
            "province": '330000',
            "city": '330100',
            "region": '330108',
            "required":false,
            onChange: function (info) {
                townFormat(info);
            }
        }, function (api) {
            var info = api.getInfo();
            townFormat(info);
        });
        
        if(!localStorage.getItem('fromUrl')){
        	localStorage.setItem('fromUrl', document.referrer)
        }
        $("#btnBack").click(function(){
        	location = localStorage.getItem('fromUrl')
        	localStorage.removeItem('fromUrl')
        });
    });

</script>

<script type="text/javascript">
    $("#date_val").click(function () {
        if ($(".dsp-select").hasClass("hover")) {
            $(".dsp-select").removeClass("hover")
            $(".time-conditions").hide();
        } else {
            $(".dsp-select").addClass("hover")
            $(".time-conditions").show();
        }
    });


    //以下演示数据
    $(function () {
        //加载所有媒体
        $.each(media_seats,function(i,n){
            $("#mediaTd").append("<label><input type=\"checkbox\" "+(editMode?"":"disabled")+" id=\"media_"+n.id+"\" name=\"media\" value=\""+n.id+"\"> "+n.name+"</label>");
        });
        $("#mediaTd").append("<br/><span id=\"mediaTip\"></span>");
        $("input:checkbox").change(function () {
            checked_media = [];
            $("input:checkbox:checked").each(function (i, n) {
                var _id = n.value * 1;
                $.each(media_seats, function (i, m) {
                    //console.log(_id+":"+m.id)
                    if (_id == m.id) checked_media.push(m)
                });
            });
        });

        $("#btnDemo").click(function () {
            var demo_data = {
                "activityName": "可口可乐2018新年投放" + Math.ceil(Math.random() * 100),
                "dts": "2018-03-01",
                "dt": "2018-05-01"
            }
            $.each(demo_data, function (key, value) {
                $("#" + key).val(value);
            })
        });
        if (activity) {
            $("#btnDemo").hide();
            $.each(activity, function (key, value) {
                $("#" + key).val(value);
            })
            checked_media = [];
            $.each(media_seats, function (i, n) {
                //if (activity_meias.includes(n.id)) {
                if (activity_meias.toString().indexOf(n.id) > -1) {
                    checked_media.push(n);
                    $("input:checkbox[name='media'][value='" + n.id + "']").prop("checked",true);
                } else {
                    //$("input:checkbox[name='media'][value='" + n.id + "']").removeAttr("checked");
                    $("input:checkbox[name='media'][value='" + n.id + "']").prop("checked",false);
                }
            });
            // renderASTable();
        }

        //表单处理
        $.formValidator.initConfig({
            validatorGroup: "2",
            submitButtonID: "btnSave",
            debug: true,
            submitOnce: false,
            errorFocus: false,
            onSuccess: function () {
            	var html = $("#as-container").html();
            	if(html.length <= 37) {
            		layer.confirm("请选择广告位", {
                        icon: 2,
                        btn: ['确定'] //按钮
                    });
                	return ;
            	}
            	
                var activityName = $("#activityName").val(); //活动名
                var startDate = $("#dts").val(); //投放开始时间
                var endDate = $("#dt").val(); //投放结束时间
				var samplePicUrl= $("#img-demo-img").attr('src')//广告投放画面图片地址
                
                var startTime = new Date(startDate);
                var time1 = startTime.getTime();
                var endTime = new Date(endDate);
                var time2 = endTime.getTime();
                
                if((time2 - time1) < 2*24*60*60*1000) {
                	layer.confirm("活动时间间隔至少3天", {
                        icon: 2,
                        btn: ['确定'] //按钮
                    });
                	return ;
                }
                
                var province = $("#province").val();//省
                var city = $("#city").val();
                var region = $("#region").val();
                var street = $("#street").val();
                console.log(samplePicUrl)
				if(samplePicUrl.length <= 0) {
            		layer.confirm("请上传广告投放画面", {
                        icon: 2,
                        btn: ['确定'] //按钮
                    });
                	return ;
            	}
				
				var customerId = $("#customerId").val();
                <#-- var customerTypeId = $("#customerTypeId").val(); -->
                var media = [];
                $("input[name='media']:checked").each(function (i, n) {
                    media.push($(n).val());
                });
//                var dels = [];
//                $.each(del_activity_seats,function(i,n){
//                   dels.push(n.id);
//                });

				var upTaskTime = $("#upTaskTime").val(); //上刊任务时间
				var upMonitorTaskTime = $("#upMonitor-Wdate").val(); //上刊监测任务时间
				var durationMonitorTaskTime = $("#durationMonitorTaskTime0").val(); //投放期间监测任务时间
				var downMonitorTaskTime = $("#downMonitorTaskTime").val(); //下刊监测任务时间
				
				var durationTimeArray = [];
                $(".durationMonitor-Wdate").each(function(i,ck){
                    if(ck.value) durationTimeArray.push(ck.value);
                });
                var durationTimes = durationTimeArray.join(",");
				
                $.ajax({
                    url: "/customer/activity/save",
                    type: "post",
                    data: {
                        "id": $("#id").val(),
                        "activityName": activityName,
                        "startDate": startDate,
                        "endDate": endDate,
                        "area": JSON.stringify([{
                            "province": province,
                            "city": city,
                            "region": region,
                            "street": street
                        }]),
                        "city": city,
                        "region": region,
                        "street": street,
                        <#-- "customerTypeId": customerTypeId, -->
                        "media": media.join(","),
//                      "dels" : dels.join(","),
						"samplePicUrl" : samplePicUrl,
						"customerId" : customerId,
                        "activeSeat": getSeatIds(),
                        "upMonitorLastDays": $('#upMonitorLastDays').val(),
                        "durationMonitorLastDays": $('#durationMonitorLastDays').val(),
                        "downMonitorLastDays": $('#downMonitorLastDays').val(),
                        "upTaskTime": upTaskTime,
                        "upMonitorTaskTime": upMonitorTaskTime,
                        "durationMonitorTaskTime": durationTimes,
                        "downMonitorTaskTime": downMonitorTaskTime
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
                            var msg = "新增成功";
                            if (id != null && id != "") {
                                msg = "编辑成功";
                            }
                            layer.confirm(msg, {
                                icon: 1,
                                btn: ['确定'] //按钮
                            }, function () {
                            	<#-- 4：超级管理员 -->
                            	<#if usertype?exists && usertype == 4>
                            		window.location.href = "/sysResources/activity";
                            	</#if>
                            	
                            	<#-- 5：部门领导 -->
                            	<#if usertype?exists && usertype == 5>
                            		window.location.href = "/sysResources/activity";
                            	</#if>
                            	
                            	<#-- 2：客户账户-->
                            	<#if usertype?exists && usertype == 2>
	                                window.location.href = "/customer/activity/list";
                                </#if>
                                
                                <#-- 1：运营平台账户 -->
                                <#if usertype?exists && usertype == 1>
	                                window.location.href = "/activity/list";
                                </#if>
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
            },
            submitAfterAjaxPrompt: '有数据正在异步验证，请稍等...'
        });
        
        <#if usertype?exists && usertype != 2>
			// 广告主校验
	        $("#customerId").formValidator({
	            validatorGroup:"2",
	            onShow:"",
	            onFocus:"请选择广告主",
	            onCorrect:""
	        }).regexValidator({
	            regExp:"^\\S+$",
	            onError:"广告主不能为空，请选择"
	        }).inputValidator({
	            min: 1,
	            max: 60,
	            onError: "广告主不能为空，请选择"
	        });
        </#if>

        // 活动名称的校验
        $("#activityName").formValidator({
            validatorGroup:"2",
            onShow:"",
            onFocus:"请输入活动名称",
            onCorrect:""
        }).regexValidator({
            regExp:"^\\S+$",
            onError:"活动名称不能为空，请输入"
        }).inputValidator({
            min: 1,
            max: 60,
            onError: "请输入活动名称，30字以内"
        });
        
        // 活动投放时间的校验
        $(".activityTime-Wdate").formValidator({
            validatorGroup:"2",
            tipID:"dateTip",
            onShow:"",
            onFocus:"请选择投放时间",
            onCorrect:""
        }).regexValidator({
            regExp:"^\\S+$",
            onError:"请输入投放时间"
        }).functionValidator({
        	fun: function(val, ele){
        		var now = new Date();
        		var date = getDate(now)
        		var m = parseInt($('#monitorTime').val())
        		var n = parseInt($('#auditTime').val())
        		var after = m + n + 1
        		var afterDay = new Date(now.getTime() + 24 * 60 * 60 * 1000 * after)
        		var afterDate = getDate(afterDay)
				console.log($('#dts').val() < afterDate, $('#dts').val(), afterDate)
        		if($('#dts').val() < date) {
        			return '不能选择过去的时间'
        		}else if ($('#dts').val() < afterDate){
        			return '活动开始时间是今天的至少' + after + '天以后'
        		}else{
        			return true
        		}
        	}
        })
        
        // 上刊监测报告时间的校验
        $('.upMonitor-Wdate').formValidator({
               validatorGroup: '2',
               tipID:"upMonitorTaskTimeTip",
               onShow:"",
               onFocus:"请选择上刊监测报告时间",
               onCorrect:""
           }).regexValidator({
               regExp:"^\\S+$",
               onError:"请输入上刊监测报告时间"
           }).functionValidator({
	           	fun: function(val, ele){
	           		// 1、是活动开始时间的至少m+n天以后  2、在活动结束时间之前
	           		var startDate = new Date($('#dts').val())
	           		var m = parseInt($('#monitorTime').val())
	           		var n = parseInt($('#auditTime').val())
	           		var after = m + n
	           		var afterDay = new Date(startDate.getTime() + 24 * 60 * 60 * 1000 * after)
	           		var afterDate = getDate(afterDay)
	           		console.log(afterDate < $('.upMonitor-Wdate').val(), afterDate, $('.upMonitor-Wdate').val())
	           		if(!$('#dts').val() || !$('#dt').val()){
	           			return '请先选择活动时间'
	           		}else if($('#dt').val() < val) {
	           			return '出报告时间是在活动结束时间之前'
	           		}else if (val < afterDate){
	           			return '出报告时间是活动开始时间的至少' + after + '天以后'
	           		}else{
	           			return true
	           		}
	           	}
          })
          
          // 投放期间监测报告时间的校验
          $('.durationMonitor-Wdate').each(function(index){
        	  $(this).formValidator({
        	  	   empty:true,
                   validatorGroup: '2',
                   tipID:"durationMonitorTaskTimeTip" + index,
                   onShow:"",
                   onFocus:"请选择投放期间监测报告时间",
                   onCorrect:""
               }).regexValidator({
                   regExp:"^\\S+$",
                   onError:"请输入投放期间监测报告时间"
               }).functionValidator({
    	           	fun: function(val, ele){
    	           		// 1、是活动开始时间的至少m+n天以后  2、在活动结束时间之前
    	           		var startDate = new Date($('#dts').val())
    	           		var m = parseInt($('#monitorTime').val())
    	           		var n = parseInt($('#auditTime').val())
    	           		var after = m + n
    	           		var afterDay = new Date(startDate.getTime() + 24 * 60 * 60 * 1000 * after)
    	           		var afterDate = getDate(afterDay)
    	           		console.log($('#dt').val() < afterDate, $('#dt').val(), afterDate)
    	           		if(!$('#dts').val() || !$('#dt').val()){
    	           			return '请先选择活动时间'
    	           		}else if($('#dt').val() < val) {
    	           			return '出报告时间是在活动结束时间之前'
    	           		}else if (val < afterDate){
    	           			return '出报告时间是活动开始时间的至少' + after + '天以后'
    	           		}else{
    	           			return true
    	           		}
    	           	}
              })
       	})
       	
       	//添加投放期间监测报告时间
        $('#addDurationMonitor').click(function(){
			var flag = false;
			var durationVar = $('.durationMonitor-Wdate');
        	$(".durationMonitor-Wdate").each(function(index){
        		var wdateValue = $(this).val();
        		if(wdateValue == ""){
					if(flag == false){
						flag = true;
					}
        		}
        	})
        	
        	if(flag == true){
        		layer.confirm("请先选择投放期间监测报告时间再添加", {
                    icon: 2,
                    btn: ['确定'] //按钮
                });	
        		return false;
        	}
        	
        	var index = $('.last').length;
        	var str = '<tr class="last"><td class="a-title"><font class="s-red">*</font>投放期间监测报告时间：</td><td><div class="ll inputs-date durationMonitorTaskTime" id="durationTime'+index+'"><div class="date"><input id="durationMonitorTaskTime'+index+'" ${editMode?string("","disabled")} class="durationMonitor-Wdate Wdate" type="text"></div></div><span style="margin-left:10px;" id="durationMonitorTaskTimeTip'+index+'"></span>&nbsp;&nbsp;<input class="btn btn-red delDurationMonitor" type="button" value="删除"></td></tr>'
        	$('.last:last').after(str)
        	// 先解绑删除按钮事件  避免每次点击添加时重复绑定之前已经绑定事件的删除按钮
        	$('.delDurationMonitor').unbind()
        	$('.delDurationMonitor').click(function(){
        		var index = $(this).parents('.last').index();
        		$('#subForm tr').eq(index).remove()
        	})
        	
       		var topId = "durationTime" + index;
       		var lastId = "durationMonitorTaskTime" + index;
       		$('#' + topId).dateRangePicker({
           	   singleDate: true,
           	   showShortcuts: false,
                  getValue: function () {
                      return $(this).find('#' + lastId).val()
                  },
                  setValue: function (s) {
                     $(this).find('#' + lastId).val(s)
  					  $(this).find('#' + lastId).blur()
                  }
            });
        	
        	<#--
        	$('.durationMonitorTaskTime').dateRangePicker({
           	   singleDate: true,
           	   showShortcuts: false,
                  getValue: function () {
                      return $(this).find('.durationMonitor-Wdate').val()
                  },
                  setValue: function (s) {
                     $(this).find('.durationMonitor-Wdate').val(s)
  					  $(this).find('.durationMonitor-Wdate').blur()
                  }
             });
            -->
        	
       		$('.durationMonitor-Wdate').each(function(index){
        	  $(this).formValidator({
        	  	   empty:true,
                   validatorGroup: '2',
                   tipID:"durationMonitorTaskTimeTip" + index,
                   onShow:"",
                   onFocus:"请选择投放期间监测报告时间",
                   onCorrect:""
               }).regexValidator({
                   regExp:"^\\S+$",
                   onError:"请输入投放期间监测报告时间"
               }).functionValidator({
    	           	fun: function(val, ele){
    	           		// 1、是活动开始时间的至少m+n天以后  2、在活动结束时间之前
    	           		var startDate = new Date($('#dts').val())
    	           		var m = parseInt($('#monitorTime').val())
    	           		var n = parseInt($('#auditTime').val())
    	           		var after = m + n
    	           		var afterDay = new Date(startDate.getTime() + 24 * 60 * 60 * 1000 * after)
    	           		var afterDate = getDate(afterDay)
    	           		console.log($('#dt').val() < afterDate, $('#dt').val(), afterDate)
    	           		if(!$('#dts').val() || !$('#dt').val()){
    	           			return '请先选择活动时间'
    	           		}else if($('#dt').val() < val) {
    	           			return '出报告时间是在活动结束时间之前'
    	           		}else if (val < afterDate){
    	           			return '出报告时间是活动开始时间的至少' + after + '天以后'
    	           		}else{
    	           			return true
    	           		}
    	           	}
              })
       		})
        })
        
        function getDate(date) {
        	var month = (date.getMonth() + 1).toString().length < 2 ? '0' + (date.getMonth() + 1).toString() : (date.getMonth() + 1).toString()
       		var day = date.getDate().toString().length < 2 ? '0' + date.getDate().toString() : date.getDate().toString()
       		return date.getFullYear()+ '-' + month + '-' + day
        }

        //投放媒体
        $("input:checkbox[name='media']").formValidator({
            validatorGroup: "2",
            tipID:"mediaTip",
            onFocus:" ",
            onShow: "　",
            onCorrect: " "
        }).inputValidator({
            min: 1,
            onError: "请选择投放媒体"
        });
        
        <#-- 
        // 客户类型校验
        $("#customerTypeId").formValidator({
            validatorGroup:"2",
            onShow:"",
            onFocus:"请选择客户类型",
            onCorrect:""
        }).regexValidator({
            regExp:"^\\S+$",
            onError:"客户类型不能为空，请选择"
        }).inputValidator({
            min: 1,
            onError:"客户类型不能为空，请选择"
        });
         -->
    });

    //媒体广告位
    var checked_media = media_seats = [
        <#list vm.getAllAvailableMedia() as media>
            {
                id:${media.id},
                name:'${media.mediaName}'
            },
        </#list>
        /*{
            id: 1,
            name: '媒体1',
            seats: [
                {
                    id: 1,
                    name: '新联路1号灯箱'
                },
                {
                    id: 2,
                    name: '新联路2号灯箱'
                },
                {
                    id: 3,
                    name: '新联路3号灯箱'
                }
            ]
        },
        {
            id: 2,
            name: '媒体2',
            seats: [
                {
                    id: 4,
                    name: '西兴路1号灯箱'
                },
                {
                    id: 5,
                    name: '西兴路2号灯箱'
                },
                {
                    id: 6,
                    name: '西兴路3号灯箱'
                }
            ]
        },
        {
            id: 3,
            name: '媒体3',
            seats: [
                {
                    id: 7,
                    name: '月明路1号灯箱'
                },
                {
                    id: 8,
                    name: '月明路2号灯箱'
                },
                {
                    id: 9,
                    name: '月明路3号灯箱'
                }
            ]
        },
        {
            id: 4,
            name: '媒体4',
            seats: [
                {
                    id: 10,
                    name: '阡陌路1号灯箱'
                },
                {
                    id: 11,
                    name: '阡陌路2号灯箱'
                },
                {
                    id: 12,
                    name: '阡陌路3号灯箱'
                }
            ]
        }*/
    ];

    /*renderASTable = function () {
        $("#as-container").html("");
        if (activity_seats.length > 0) {
            var tab = $('<table width="100%" cellpadding="0" cellspacing="0" border="0" class="tablesorter" id="plan"> <thead> <tr> <th>序号</th> <th>广告位</th> <th>媒体</th> <th>投放品牌</th> <th>监测时间段</th> <th>监测时间</th><#--  <th>广告投放画面</th>  --><th>操作</th> </tr> </thead> <tbody></tbody></table>');
            $.each(activity_seats, function (i, as) {
                tab.find("tbody").append("<tr> <td width='30'>" + (i + 1) + "</td> <td>" + as.seatName + "</td> <td>" + as.mediaName + "</td> <td>" + as.brand + "</td> <td>" + as.startDate + "至" + as.endDate + "</td><td>" + (as.upMonitor == 1 ? "上刊" : "") + "&nbsp;" + (as.durationMonitor == 1 ? "投放期间" : "") + "&nbsp;" + (as.downMonitor == 1 ? "下刊" : "") + "&nbsp;" + "</td> <#-- <td><img src='" + as.samplePicUrl + "' class='demo'/></td> --> <td> <a href='javascript:modAS(" + i + ");'>详情</a> "+(editMode?"<a href='javascript:dealAS(" + i + ");'>删除</a>":"")+" </td> </tr>");
                $("#img-demo-img").attr("src",as.samplePicUrl);//广告投放画面图片地址
            });
            
            $("#as-container").append(tab);
        }
    }

    dealAS = function (i) {
        if(!!activity_seats[i].id) {
            del_activity_seats.push(activity_seats[i]);
        }
        activity_seats.splice(i, 1);
        renderASTable();
    }

    modAS = function (i) {
        mod_activity_seat = activity_seats[i];
        layer.open({
            type: 2,
            title: '详细信息',
            shade: 0.8,
            area: ['820px', '600px'],
            content: '/customer/activity/adseat/edit' //iframe的url
        });
    }*/

    showQR = function (id) {
        layer.open({
            type: 1,
            title: false,
            shade: [0.9, '#000'],
            closeBtn: 0,
            shadeClose: true,
            // skin: 'layui-layer-rim', //加上边框
            area: ['400px', '400px'], //宽高
            content: '<img src="/activity/getQrcode?id=' + id + '" style="display:block;width:100%;height:auto;"/>'
        });
    }
	
	function uploadPic(id){
		var picName = $("#"+id).val();
	
		/*if(!/\.(jpg|JPG)$/.test(picName)) {
			layer.confirm("图片类型必须是jpg格式", {
				icon: 0,
				btn: ['确定'] //按钮
			});
			return false;
		}*/
		$.ajaxFileUpload({
			url:'/upload',
			secureuri:false,
			fileElementId:id,
			dataType: 'json',
			success: function (data, status) {
				var ret = data.ret;
				if (data == '"error"') {
					layer.confirm("上传图片失败", {
						icon: 2,
						btn: ['确定'] //按钮
					});
					return;
				} else if(data=='"overPic"') {
					layer.confirm("上传图片太大！请小于1MB", {
						icon: 0,
						btn: ['确定'] //按钮
					});
				} else if (data == '"notPic"') {
					layer.confirm("上传的不是图片", {
						icon: 0,
						btn: ['确定'] //按钮
					});
					return;
				} else {
					var arr=data.split('"');
					var dataNew=arr[1];
					$("#"+id+"-bak").val(dataNew);
					$("#"+id+"-img").attr('src',dataNew);
				}
			},
			error: function (data, status, e) {
				layer.confirm("上传图片失败", {
					icon: 5,
					btn: ['确定'] //按钮
				});
			}
		});
	}
	
	var checkArr = [];
	var delDataArr = []
	var modDataArr = []
	
	function pushCheckArr(arr, val) {
		var hasVal = true
		var len = arr.length
		for(var i = 0; i < len; i++){
			if(parseInt(arr[i].id) === parseInt(val.id)){
				hasVal = false
				break;
			}
		}
		// 如果原先中不存在则添加
		if(hasVal){
			arr.push(val)
		}
	}
	
	function addDelData(val) {
		pushCheckArr(delDataArr, val)
		removeModData(val.id)
	}
	
	function setDelData(val) {
		delDataArr = val
	}
	
	function addModData(val) {
		pushCheckArr(modDataArr, val)
	}
	
	function setModData(val) {
		modDataArr = val
	}
	
	function removeModData(id) {
		for(var i = 0; i < modDataArr.length; i++) {
			if(parseInt(id) === parseInt(modDataArr[i].id)){
				modDataArr.splice(i, 1)
				break;
			}
		}
	}
	
	// 添加子页面中选中的checkbox
	function addCheck(val){
		pushCheckArr(checkArr, val)
	}
	
	// 获取所有选中的checkbox
	function getCheck(){
		return checkArr
	}
	
	// 根据下方表格显示的数据获取checkArr
	function getAllCheckArr(){
		$('#plan tbody tr').each(function() {
			checkArr.push({
				id: $(this).find('td').eq(5).find('a').data('id'),
				html: '<tr>' + $(this).html() + '</tr>'
			})
		})
		return checkArr
	}
	
	// 设置checkbox
	function setCheck(val){
		checkArr = val
	}
	
	// 移除取消选中的checkbox
	function removeCheck(id) {
		for(var i = 0; i < checkArr.length; i++) {
			if(parseInt(id) === parseInt(checkArr[i].id)){
				checkArr.splice(i, 1)
				break;
			}
		}
	}
	
	function getCheckboxData(isEdit) {
		var html = '<table width="100%" cellpadding="0" cellspacing="0" border="0" class="tablesorter" id="plan"> <thead><tr><th>广告位名称</th><th>区域</th><th>主要路段</th><th>详细位置</th><th>媒体主</th> <th>媒体大类</th><th>媒体小类</th><th>操作</th> </tr></thead><tbody>'
		console.log('save', checkArr)
		if(!isEdit){ // 如果是创建的时候
			activity_seats = []
				
			var len = checkArr.length
			for(var i = 0;i < len; i++){
				html += '<tr>' + checkArr[i].html
				// 如果有a标签就不添加
				html += checkArr[i].html.indexOf('</a>') != -1 ? '</tr>' : '<td><a style="cursor:pointer" class="deleteCheckBtn" data-id='+ checkArr[i].id + '>删除</a></td></tr>'
				
				activity_seats.push(checkArr[i].id)
			}
			html += '</thbody>'
			$('#as-container').html(html)
			$('.deleteCheckBtn').click(function(){
				
				removeCheck($(this).data('id'))
				getCheckboxData()
			})
		} else {
			var len = checkArr.length
			for(var i = 0;i < len; i++){
				html += checkArr[i].html
			}
			html += '</thbody>'
			$('#as-container').html(html)
			$('.deleteCheckBtn').click(function(){
				removeCheck($(this).data('id'))
				getCheckboxData(true)
			})
		}
		
	}
	
	ModCheckboxData()
	
	function ModCheckboxData() {
		var len = activity_seats.length
		if(len > 0){
			var html = '<table width="100%" cellpadding="0" cellspacing="0" border="0" class="tablesorter" id="plan"> <thead><tr><th>广告位名称</th><th>区域</th><th>主要路段</th><th>详细位置</th><th>媒体主</th> <th>媒体大类</th><th>媒体小类</th><th>操作</th> </tr></thead><tbody>'
			for(var i = 0; i < len; i++){
				var str = '<tr><td>' + activity_seats[i].seatName + '</td><td>' + activity_seats[i].area + '</td><td>' + activity_seats[i].road + '</td><td>' + activity_seats[i].location + '</td><td>' + activity_seats[i].mediaName + '</td> <td>' + activity_seats[i].parentName + '</td><td>' + activity_seats[i].secondName + '</td><td><#if (activity?exists&&activity.status?exists&&activity.status==1)><a style="cursor:pointer" class="deleteCheckBtn" data-id='+ activity_seats[i].seatId + '>删除</a></#if> <#if usertype?exists && usertype != 2><#if (activity?exists&&activity.status?exists&&activity.status==2)><a style="cursor:pointer" class="addMonitorTask" data-id='+ activity_seats[i].seatId + '>追加监测</a></#if></#if></td></td> </tr>'
				checkArr.push({
					id: activity_seats[i].seatId,
					html: str
				})
				html += str
			}
			html += '</thbody>'
			$('#as-container').html(html)
			$('.deleteCheckBtn').click(function(){
				removeCheck($(this).data('id'))
				getCheckboxData(true)
			})
			
			$('.addMonitorTask').click(function(){
				var seatId = $(this).data('id');
				var startTime = $("#dts").val();
				var endTime = $("#dt").val();
				var activityId = $("#id").val();
				//iframe层
		        layer.open({
		            type: 2,
		            title: '追加监测任务',
		            shadeClose: true,
		            shade: 0.8,
		            area: ['500px', '380px'],
		            content: '/activity/addTask?seatId=' + seatId + '&startTime=' + startTime + '&endTime=' + endTime + '&activityId=' + activityId //iframe的url
		        });
			})
		}
	}
	
	function getSeatIds() {
		
		var seatIds = []
		var len = checkArr.length
		for(var i = 0;i < len; i++){
			seatIds.push(checkArr[i].id)
		}
		return seatIds.join(',')
	}
	
	
        //上刊监测持续天数
        $("#upMonitorLastDays").formValidator({
            validatorGroup: "2",
            onShow: "　",
            onCorrect: "",
            onFocus:"请填写1-2的数字"
        }).functionValidator({
            fun:function(val){
                return ($("#durationMonitor:checked").length<1) || /^[1-2]$/.test(val);
            },
            onError: "只允许填写1-2的数字"
        });

        //投放期间监测持续天数
        $("#durationMonitorLastDays").formValidator({
            validatorGroup: "2",
            onShow: "　",
            onCorrect: "",
            onFocus:"请填写1-2的数字"
        }).functionValidator({
            fun:function(val){
                return ($("#durationMonitor:checked").length<1) || /^[1-2]$/.test(val);
            },
            onError: "只允许填写1-2的数字"
        });

        //下刊监测持续天数
        $("#downMonitorLastDays").formValidator({
            validatorGroup: "2",
            onShow: "　",
            onCorrect: "",
            onFocus:"请填写1-3的数字"
        }).functionValidator({
            fun:function(val){
                return ($("#downMonitor:checked").length<1) || /^[1-3]$/.test(val);
            },
            onError: "只允许填写1-3的数字"
        });
        

</script>