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
                        <input type="button" id="btnDemo" class="btn btn-green" value="演示专用" style="display: none;"/>
                    </td>
                </tr>

                <tr>
                    <td class="a-title"><font class="s-red">*</font>广告位：</td>
                    <td>
                        <div class="select-box select-box-140 un-inp-select">
                            <select name="seat" class="select" id="seat">
                            </select>
                        </div>
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
                        <span id="usernameTip"></span>
                    </td>
                </tr>

                <tr>
                    <td class="a-title"><font class="s-red">*</font>监测时间：</td>
                    <td>
                        <label>
                            <input type="checkbox" checked id="upMonitor" name="monitor_time" value="0"  > 上刊
                        </label>
                        <label>
                            <input type="checkbox" checked id="downMonitor" name="monitor_time" value="0"  > 投放期间
                        </label>
                        <label>
                            <input type="checkbox" checked id="durationMonitor" name="monitor_time" value="0"  > 下刊
                        </label>
                    </td>
                </tr>

                <tr>
                    <td class="a-title"><font class="s-red">*</font>监测次数：</td>
                    <td>
                        <input type="text" style="width:50px;text-align:right;" disabled id="monitorCount" name="monitorCount" value="3" autocomplete="off" class="form-control">
                        <span id="usernameTip"></span>
                    </td>
                </tr>

                <tr>
                    <td class="a-title"><font class="s-red">*</font>样例：</td>
                    <td>
						<input type="hidden" id="img-demo-bak"/>
                        <div class="btn-file" style="width:74px;height:28px;top:0px;">
							<a class="addBtn" href="javascript:;" id="resource_sel">上传</a>
							<input type="file" id="img-demo" name="file" onchange="uploadPic('img-demo')">
						</div>
                    </td>
                </tr>

                <tr>
                    <td class="a-title">&nbsp;</td>
                    <td>
                        <img src="" id="img-demo-img" width="280" alt="请上传图片"/>
                    </td>
                </tr>

					<tr>
						<td class="a-title">&nbsp;</td>
						<td>
							<button type="button" class="btn btn-red" autocomplete="off" id="btnSave">保　存</button>
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
	     
		$(function(){
		
			// 下拉
//			$('.select').searchableSelect();
            $("form").submit(function(){return false;});
            //日期
            $('.inputs-date').dateRangePicker({
                separator : ' 至 ',
                showShortcuts:false,
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
            });
		})
		
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
            $.each(media_seat,function(i,media){
                $("#media").append("<option value='"+media.id+"'>"+media.name+"</option>");
            })
            $.each(media_seat[0].seats,function(j,seat){
                $("#seat").append("<option value='"+seat.id+"'>"+seat.name+"</option>");
            })
            $('.select').searchableSelect();

            $("#media").siblings().find(".searchable-select-item").click(function(){
                $("#seat").empty();
                $("#seat").siblings(".searchable-select").remove();
                $.each(media_seat,function(i,media){
                    if(media.id==$("#media").val()*1){
                        $.each(media.seats,function(j,seat){
                            $("#seat").append("<option value='"+seat.id+"'>"+seat.name+"</option>");
                        })
                        $('#seat').searchableSelect();
                    }
                })
            });

            if(!!as){
                $("#id").val(as.id);
                        mediaId : $("#media").val(as.mediaId); //媒体id
                        //mediaName:$("#media").siblings().find(".searchable-select-holder").text(),
                        $("#seat").val(as.seatId); //媒体id
                        //seatName : $("#seat").siblings().find(".searchable-select-holder").text(),
                        $("#dts").val(as.startDate); //监测开始时间
                        $("#dt").val(as.endDate); //监测结束时间
                        $("#brand").val(as.brand); //品牌
                        if(as.upMonitor>1)$("#upMonitor").removeAttr("checked");//上刊监测
                        if(as.downMonitor>1)$("#downMonitor").removeAttr("checked");//下刊监测
                        if(as.durationMonitor>1)$("#durationMonitor").removeAttr("checked");//投放期间监测
                        $("#monitorCount").val(as.monitorCount);//监测次数
                        $("#img-demo-bak").val(as.samplePicUrl);//样例图片地址
                        $("#img-demo-img").attr("src",as.samplePicUrl);//样例图片地址
            }else{
                $("#btnDemo").click(function(){
                    var demo_data = {
                        "brand" : "可口可乐",
                        "dts":"2018-02-01",
                        "dt":"2018-03-01",
                        "img-demo-bak":"/static/upload/demo.png"
                    }
                    $.each(demo_data,function(key,value){
                        $("#"+key).val(value);
                    })
                    $("#img-demo-img").attr("src",demo_data["img-demo-bak"]);
                }).show();
            }

            $("input[name='monitor_time']").change(function(){
                $("#monitorCount").val($("input[name='monitor_time']:checked").length);
            });


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
                        downMonitor: $("#downMonitor:checked").length > 0 ? 1 : 2,//下刊监测
                        durationMonitor: $("#durationMonitor:checked").length > 0 ? 1 : 2,//投放期间监测
                        monitorCount: $("#monitorCount").val(),//监测次数
                        samplePicUrl: $("#img-demo-bak").val()//样例图片地址
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

        });
	</script>
