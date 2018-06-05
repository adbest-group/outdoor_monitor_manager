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
<@shiro.hasRole name="customer">
    <#if !activity?exists||activity.status = 1>
        <#assign editMode=true />
    </#if>
</@shiro.hasRole>

<div class="main-container" style="height: auto;">
    <div class="clearfix">
        <div class="main-box basic-info">
            <div class="bd">
                <form id="subForm" method="post">
                    <input type="hidden" id="id" value=""/>
                    <table width="100%" cellpadding="0" cellspacing="0" border="0" type="">
                        <tbody>

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

                        <tr>
                            <td class="a-title"><font class="s-red">*</font>投放时间：</td>
                            <td>
                                <div class="ll inputs-date">
                                    <div class="date">
                                        <input id="dts" ${editMode?string("","disabled")} class="Wdate" type="text"> -
                                        <input id="dt" ${editMode?string("","disabled")} class="Wdate" type="text">
                                    </div>
                                </div>
                                <span style="margin-left:10px;" id="dateTip"></span>
                            </td>
                        </tr>
						<#if editMode>
                        <tr>
                            <td class="a-title"><font class="s-red">*</font>投放地区(筛选广告位)：</td>
                            <td>
                            <#--<input type="text" style="width:100px;"  id="province" name="province" value="浙江省" autocomplete="off" class="form-control">-->
                            <#--<a class="addBtn" href="javascript:;" id="resource_sel">选择</a>-->
                                <div id="demo3" class="citys">
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
						</#if>
                        <tr>
                            <td class="a-title"><font class="s-red">*</font>媒体主：</td>
                            <td id="mediaTd">
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
                            </td>
                        </tr>
						
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
                            <td class="a-title">&nbsp;</td>
                            <td>
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
                                    <input type="submit" id="btnSave" class="btn btn-red" value="　保 存　"/>
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
        "samplePicUrl": '${activity.samplePicUrl!""}'
    }
    var activity_seats = [
        <#if (activity.activitySeats?exists && activity.activitySeats?size>0) >
            <#list activity.activitySeats as seat>
                {
                    id: ${seat.id},
                    mediaId: "${seat.mediaId}",
                    mediaName: "${seat.mediaName}",
                    seatId: "${seat.adSeatId}",
                    seatName: "${seat.adSeatName}",
                    startDate: "${seat.monitorStart?string("yyyy-MM-dd")}",
                    endDate: "${seat.monitorEnd?string("yyyy-MM-dd")}",
                    brand: "${seat.brand}",
                    upMonitor: "${seat.upMonitor}",
                    downMonitor: "${seat.downMonitor}",
                    durationMonitor: "${seat.durationMonitor}",
                    upMonitorLastDays: "${seat.upMonitorLastDays!"3"}",
                    downMonitorLastDays: "${seat.downMonitorLastDays!"3"}",
                    durationMonitorLastDays: "${seat.durationMonitorLastDays!"3"}",
                    monitorCount: "${seat.monitorCount}",
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

        if(editMode) {
            //日期
            $('.inputs-date').dateRangePicker({
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
					$('.Wdate').blur()
                }
            });
        }

        $("#add-adseat").click(function () {
            if($("#dts").val().length<1||$("#province").val().length<1||$("input:checkbox:checked").length<1){
                layer.alert("请先确认投放时间，地区和媒体");
                return;
            }
            mod_activity_seat = null;
            layer.open({
                type: 2,
                title: '新增广告位监测',
                shade: 0.8,
                area: ['820px', '600px'],
                content: '/customer/activity/adseat/edit' //iframe的url
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
        $("#btnBack").click(function(){history.back();});

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
            renderASTable();
        }

        //表单处理
        $.formValidator.initConfig({
            validatorGroup: "2",
            submitButtonID: "btnSave",
            debug: true,
            submitOnce: false,
            errorFocus: false,
            onSuccess: function () {
                var activityName = $("#activityName").val(); //活动名
                var startDate = $("#dts").val(); //投放开始时间
                var endDate = $("#dt").val(); //投放结束时间
				samplePicUrl: $("#img-demo-bak").val()//广告投放画面图片地址
                
                var startTime = new Date(startDate);
                var time1 = startTime.getTime();
                var endTime = new Date(endDate);
                var time2 = endTime.getTime();
                
                if((time2 - time1) < 2*24*60*60*1000) {
                	layer.confirm("投放时间间隔至少3天", {
                        icon: 2,
                        btn: ['确定'] //按钮
                    });
                	return ;
                }
                
                var province = $("#province").val();//省
                var city = $("#city").val();
                var region = $("#region").val();
                var street = $("#street").val();
				var samplePicUrl = $("#img-demo-bak").val();
                <#-- var customerTypeId = $("#customerTypeId").val(); -->
                var media = [];
                $("input[name='media']:checked").each(function (i, n) {
                    media.push($(n).val());
                });
//                var dels = [];
//                $.each(del_activity_seats,function(i,n){
//                   dels.push(n.id);
//                });
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
//                        "dels" : dels.join(","),
						"samplePicUrl" : samplePicUrl,
                        "activeSeat": JSON.stringify(activity_seats)
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
                                window.location.href = "/customer/activity/list";
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

        // 活动名称
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

        //投放时间
        $(".Wdate").formValidator({
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
        		var date = now.getFullYear()+ '-' + (now.getMonth() + 1).toString().padStart(2, 0) + '-' + now.getDate().toString().padStart(2, 0)

        		if($('#dts').val() < date) {
        			return false
        		}else {
        			return true
        		}
        	},
        	onError: '不能选择过去的时间'
        })

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

    renderASTable = function () {
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
    }

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

</script>