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
                <tr>
                    <td class="a-title"><font class="s-red">*</font>媒体：</td>
                    <td>
                        <div class="select-box select-box-140 un-inp-select">
                            <select name="media" class="select" id="media">
                            </select>
                        </div>
                        <#-- <input type="button" id="btnDemo" class="btn btn-green" value="演示专用" style="display: none;"/> -->
                    </td>
                </tr>

                <tr>
                    <td class="a-title"><font class="s-red">*</font>广告位：</td>
                    <td>
                        <div class="select-box select-box-140 un-inp-select">
                            <select name="seat" class="select" id="seat">
                            </select>
                        </div> <span id="seatTip"></span>
                    </td>
                </tr>

                <tr>
                    <td class="a-title"><font class="s-red">*</font>品牌：</td>
                    <td>
                        <input type="text"  id="brand" name="brand" value="" autocomplete="off" class="form-control">
                        <span id="brandTip"></span>
                    </td>
                </tr>

                <tr>
                    <td class="a-title"><font class="s-red">*</font>监测时间段：</td>
                    <td>
                        <div class="ll inputs-date">
                            <div class="date">
                                <input id="dts" class="Wdate" type="text"> -
                                <input id="dt" class="Wdate" type="text">
                            </div>
                        </div>
                        <span id="dateTip"></span>
                    </td>
                </tr>

                <tr>
                    <td class="a-title"><font class="s-red">*</font>监测类型：</td>
                    <td>
                        <label>
                            <input type="checkbox" checked id="upMonitor" name="monitor_time" value="0"  > 上刊
                        </label>
                        <label>
                            <input type="checkbox" checked id="durationMonitor" name="monitor_time" value="0"  > 投放期间
                        </label>
                        <label>
                            <input type="checkbox" checked id="downMonitor" name="monitor_time" value="0"  > 下刊
                        </label>

                        <span id="monitorTimeTip"></span>
                    </td>
                </tr>

                <tr id="upMonitorLastDaysTr" style="display:none;">
                    <td class="a-title"><font class="s-red">*</font>上刊监测任务<br/>可持续天数：</td>
                    <td>
                        <input type="text" style="width:50px;text-align:right;" id="upMonitorLastDays" name="upMonitorLastDays" value="2" autocomplete="off" class="form-control">
                        <span id="upMonitorLastDaysTip"></span>
                    </td>
                </tr>

                <tr id="durationMonitorLastDaysTr" style="display:none;">
                    <td class="a-title"><font class="s-red">*</font>投放期间监测任务<br/>可持续天数：</td>
                    <td>
                        <input type="text" style="width:50px;text-align:right;" id="durationMonitorLastDays" name="durationMonitorLastDays" value="2" autocomplete="off" class="form-control">
                        <span id="durationMonitorLastDaysTip"></span>
                    </td>
                </tr>

                <tr id="downMonitorLastDaysTr" style="display:none;">
                    <td class="a-title"><font class="s-red">*</font>下刊监测任务<br/>可持续天数：</td>
                    <td>
                        <input type="text" style="width:50px;text-align:right;" id="downMonitorLastDays" name="downMonitorLastDays" value="3" autocomplete="off" class="form-control">
                        <span id="downMonitorLastDaysTip"></span>
                    </td>
                </tr>

                <tr style="display:none;">
                    <td class="a-title"><font class="s-red">*</font>监测次数：</td>
                    <td>
                        <input type="text" style="width:50px;text-align:right;" disabled id="monitorCount" name="monitorCount" value="3" autocomplete="off" class="form-control">
                        <span id="monitorCountTip"></span>
                    </td>
                </tr>
                
                <#--  <tr>
                    <td class="a-title"><font class="s-red">*</font>样例：</td>
                    <td>
						<input type="hidden" id="img-demo-bak"/>
                        <div class="btn-file" style="width:74px;height:28px;top:0px;">
							<a class="addBtn" href="javascript:;" id="resource_sel">上传</a>
							<input type="file" id="img-demo" name="file" onchange="uploadPic('img-demo')">
						</div> <span id="img-demoTip"></span>
                    </td>
                </tr>

                <tr>
                    <td class="a-title">&nbsp;</td>
                    <td>
                    	<img src="" id="img-demo-img" width="280" alt="请上传图片"/>
                    </td>
                </tr>
			 	-->
					<tr>
						<td class="a-title">&nbsp;</td>
						<td>
							<button type="button" class="btn btn-red" autocomplete="off" id="btnSave">保　存</button>
							<button type="button" class="btn btn-primary" autocomplete="off" id="btnCancel">关  闭</button>
						</td>
					</tr>
				</tbody>
			</table>
            </form>
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
	<!--文件上传-->
	<script type="text/javascript" src="${model.static_domain}/js/formValidator/formValidator-4.0.1.js"></script>
	<script src="${model.static_domain}/js/ajaxfileupload.js"></script>
	
	<script>

		$(document).ready(function() {
             var id = $("#id").val();
             

		});
		
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


		//演示用
        $(function(){
            var media_seat = parent.window.checked_media;
            var as = parent.window.mod_activity_seat;
            var activity_seats = parent.window.activity_seats;
            var editMode = parent.window.editMode;


            var $province = parent.window.$province;
            var $city = parent.window.$city;
            var $region = parent.window.$region;
            var $street = parent.window.$street;
            var $dts = parent.window.$dts;
            var $dt = parent.window.$dt;

            $("form").submit(function(){return false;});
            //日期
            var dateRangePickerOption = {
                separator : ' 至 ',
                showShortcuts:false,
                startDate:moment($dts.val()),
                endDate:moment($dt.val()),
                minDate:moment($dts.val()),
                maxDate:moment($dt.val()),
                getValue: function()
                {
                    if ($('#dts').val() && $('#dt').val() )
                        return $('#dts').val() + ' 至 ' + $('#dt').val();
                    else
                        return '';
                },
                setValue: function(s,s1,s2)
                {
                    $('#dts').val(s1);
                    $('#dt').val(s2);

                }
            }
//            if($dts.val().length>0){
//                dateRangePickerOption["minDate"] = "2018-03-02";
//            }
//            if($dt.val().length>0){
//                dateRangePickerOption["maxDate"] = "2018-03-18";
//            }
            //console.log(dateRangePickerOption);
            if(editMode) {
                $('.inputs-date').dateRangePicker(dateRangePickerOption);
            }

            $.each(media_seat,function(i,media){
                $("#media").append("<option value='"+media.id+"' "+(i==0?"selected":"")+">"+media.name+"</option>");
            })

            selectSeatOps = function(mediaId) {
                $("#seat").empty();
                $("#seat").siblings(".searchable-select").remove();
                $.ajax({
                    url: "/adseat/selectSeat",
                    type: "post",
                    data: {
                        "mediaId": mediaId,
                        "province": $province.val(),
                        "city": $city.val(),
                        "region": $region.val(),
                        "street": $street.val(),
                        "startDate": $("#dts").val(), //监测开始时间
                        "endDate": $("#dt").val() //监测结束时间
                    },
                    cache: false,
                    dataType: "json",
                    success: function (datas) {
                        var resultRet = datas.ret;
                        if (resultRet.code == 100&&resultRet.result&&resultRet.result.length>0) {
                            $.each(resultRet.result, function (i, seat) {
                                var name = seat.name;
                                $.each(activity_seats,function(j,n){
                                    if( (!as || as.seatId != n.seatId) && n.seatId == seat.id){
                                        name += "(已添加)";
                                        return false;
                                    }
                                });
                                $("#seat").append("<option value='" + seat.id + "' "+((!!as&&as.seatId==seat.id||i==0)?"selected":"")+">" + name + "</option>");
                            });
                            $('#seat.select').searchableSelect();
                        }else{
                            $("#seat").append("<option value=''>- 请选择 -</option>");
                            $('#seat.select').searchableSelect();

                        }
                        if(!editMode){
                            $('#seat.select').siblings(".searchable-select").find(".searchable-select-dropdown").hide();
                        }
                    }
                });
            }


            if(!!as){
                $("#id").val(as.id);
                        $("#media").val(as.mediaId); //媒体id
                        //mediaName:$("#media").siblings().find(".searchable-select-holder").text(),
                        $("#seat").val(as.seatId); //媒体id
                        //seatName : $("#seat").siblings().find(".searchable-select-holder").text(),
                        $("#dts").val(as.startDate); //监测开始时间
                        $("#dt").val(as.endDate); //监测结束时间
                        $("#brand").val(as.brand); //品牌
                        //上刊监测
                        if(as.upMonitor>1) {
                            $("#upMonitor").removeAttr("checked");
                        }else{
                            $("#upMonitorLastDays").val(as.upMonitorLastDays);
                            $("#upMonitorLastDaysTr").show();
                        }
                        //下刊监测
                        if(as.downMonitor>1){
                            $("#downMonitor").removeAttr("checked");
                        }else{
                            $("#downMonitorLastDays").val(as.downMonitorLastDays);
                            $("#downMonitorLastDaysTr").show();
                        }
                        //投放期间监测
                        if(as.durationMonitor>1){
                            $("#durationMonitor").removeAttr("checked");
                        }else{
                            $("#durationMonitorLastDays").val(as.durationMonitorLastDays);
                            $("#durationMonitorLastDaysTr").show();
                        }
                        $("#monitorCount").val(as.monitorCount);//监测次数
                        $("#img-demo-bak").val(as.samplePicUrl);//样例图片地址
                        $("#img-demo-img").attr("src",as.samplePicUrl);//样例图片地址
                        <#-- $("#img-demo-bak").val(as.samplePicUrl);//样例图片地址
                        $("#img-demo-img").attr("src",as.samplePicUrl);//样例图片地址 -->
            }else{
                $("#btnDemo").click(function(){
                    var demo_data = {
                        "brand" : "可口可乐",
                        "dts":$dts.val(),
                        "dt":$dt.val(),
                        "img-demo-bak":"/static/upload/demo.png"
                    }
                    $.each(demo_data,function(key,value){
                        $("#"+key).val(value);
                    })
                    $("#img-demo-img").attr("src",demo_data["img-demo-bak"]);
                }).show();
                $("#dts").val($dts.val())
                $("#dt").val($dt.val())

                $("#upMonitorLastDaysTr").show();
                $("#downMonitorLastDaysTr").show();
                $("#durationMonitorLastDaysTr").show();
            }

            selectSeatOps(as&&as.mediaId || media_seat[0].id);
            $('#media.select').searchableSelect();
            $("#media").siblings().find(".searchable-select-item").click(function(){
                selectSeatOps($("#media").val());
            });

            $("input[name='monitor_time']").change(function(){
                $("#monitorCount").val($("input[name='monitor_time']:checked").length);
                if($("#upMonitor:checked").length > 0){$("#upMonitorLastDaysTr").show();}else{$("#upMonitorLastDaysTr").hide();}
                if($("#durationMonitor:checked").length > 0){$("#durationMonitorLastDaysTr").show();}else{$("#durationMonitorLastDaysTr").hide();}
                if($("#downMonitor:checked").length > 0){$("#downMonitorLastDaysTr").show();}else{$("#downMonitorLastDaysTr").hide();}
            });
            $("#btnCancel").click(function(){
                parent.window.layer.closeAll();
            });

            //判断是否可编辑
            if(!editMode){
                $(".select").siblings(".searchable-select").find(".searchable-select-dropdown").hide();
                $("#brand,#dts,#dt,input:checkbox[name='monitor_time'],#upMonitorLastDays,#durationMonitorLastDays,#downMonitorLastDays").attr("disabled",true);
                $("#resource_sel").parent().hide();
                $("#btnSave").hide();
            }

            //表单处理
            $.formValidator.initConfig({
                validatorGroup:"2",
                submitButtonID: "btnSave",
                debug:true ,
                submitOnce: false,
                errorFocus: false,
                onSuccess: function() {
                    var activity_seat = {
                        id: $("#id").val(),
                        mediaId: $("#media").val(), //媒体id
                        mediaName: $("#media").siblings().find(".searchable-select-holder").text(),
                        seatId: $("#seat").val(), //媒体id
                        seatName: $("#seat").siblings().find(".searchable-select-holder").text(),
                        startDate: $("#dts").val(), //监测开始时间
                        endDate: $("#dt").val(), //监测结束时间
                        brand: $("#brand").val(), //品牌
                        upMonitor: $("#upMonitor:checked").length > 0 ? 1 : 2,//上刊监测
                        upMonitorLastDays: $("#upMonitorLastDays").val(),
                        downMonitor: $("#downMonitor:checked").length > 0 ? 1 : 2,//下刊监测
                        downMonitorLastDays: $("#downMonitorLastDays").val(),
                        durationMonitor: $("#durationMonitor:checked").length > 0 ? 1 : 2,//投放期间监测
                        durationMonitorLastDays: $("#durationMonitorLastDays").val(),
                        monitorCount: $("#monitorCount").val()//监测次数
                        <#-- samplePicUrl: $("#img-demo-bak").val()//样例图片地址  -->
                    }
                    //console.log(activity_seat)
                    if (!!as) {
                        $.extend(parent.window.mod_activity_seat,activity_seat);
                    } else{
                        parent.window.activity_seats.push(activity_seat);
                    }
                    parent.window.renderASTable();
                    parent.window.layer.closeAll();
                },
                submitAfterAjaxPrompt: '有数据正在异步验证，请稍等...'
            });

            //广告位
            $("#seat").formValidator({
                validatorGroup: "2",
                onShow: "　",
                onFocus: "",
                onCorrect: ""
            }).functionValidator({
                fun: function(val){
                    if(val&&val.length>0){
                        return true
                    }
                    return false;
                },
                onError: "请选择广告位"
            }).functionValidator({
                fun: function(val){
                    var ret = true;
                    if(activity_seats&&activity_seats.length>0){
                        $.each(activity_seats,function(i,n){
                            if((!as || as.seatId != n.seatId) && n.seatId == val ){
                                ret = false;return false;
                            }
                        });
                    }
                    return ret;
                },
                onError: "该广告位已添加"
            });

            //品牌
            $("#brand").formValidator({
                validatorGroup: "2",
                onShow: "　",
                onFocus: "请输入品牌，30字以内",
                onCorrect: ""
            }).inputValidator({
                min: 1,
                max: 60,
                onError: "请输入品牌，30字以内"
            });

            //监测时间
            $("input:checkbox[name='monitor_time']").formValidator({
                validatorGroup: "2",
                tipID:"monitorTimeTip",
                onShow: "　",
                onCorrect: "",
                onFocus:""
            }).inputValidator({
                min: 1,
                onError: "请选择监测时间"
            });

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

            //样例
            <#-- $("#img-demo-bak").formValidator({
                validatorGroup: "2",
                tipID:"img-demoTip",
                onShow: "　",
                onCorrect: ""
            }).inputValidator({
                min: 1,
                onError: "请上传样例"
            });  -->
        });
	</script>
