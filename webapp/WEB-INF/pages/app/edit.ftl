<#assign webTitle="资源管理-编辑App信息" in model> <#assign webHead in model> </#assign>
<@model.webhead />
<!-- 头部 -->
<@model.webMenu current="资源管理" child="App管理" />
<link rel="stylesheet" type="text/css"
      href="${model.static_domain}/css/new_main.css">
<link rel="stylesheet" type="text/css"
      href="${model.static_domain}/css/icon_fonts.css">

<style type="text/css">
    .basic-info .bd .a-title {
        width: 120px;
    }

    img.demo {
        size: 50px;
    }


</style>
<div class="main-container" style="height: auto;">
    <div class="clearfix">
        <div class="main-box basic-info">
            <div class="bd new-active">
                <div class="hd mt-10">
                    <h3>编辑App信息</h3>
                </div>
                <form id="form" action="#">
                    <input type="hidden" id="id" name="id"
                           value="<#if (adapp?exists)&&(adapp.id?exists)>${adapp.id}</#if>"/>
                    <input type="hidden" id="pic" name="pic"
                           value="<#if (adapp?exists)&&(adapp.appPictureUrl?exists)>${adapp.appPictureUrl}</#if>"/>
                    <table width="100%" cellpadding="0" cellspacing="0" border="0">
                        <tbody>
                        <tr>
                            <td class="a-title"><font class="s-red">*</font>App名称：</td>
                            <td>
                                <input type="text" <#-- disabled --> value="<#if (adapp?exists)>${adapp.appName!""}</#if>"
                                       style="width: 130px;" id="appName" name="appName" autocomplete="off"
                                       class="form-control">
                                <span id="appNameTip"></span>
                            </td>
                        </tr>
                        <tr>
                            <td class="a-title"><font class="s-red">*</font>App logo：</td>
                    		<td>
									<input type="hidden" id="img-demo-bak"/>
                       					<div class="btn-file" style="width:74px;height:28px;top:0px;">
											<a class="addBtn" href="javascript:;" id="resource_sel">上传</a>
											<input type="file" id="img-demo" name="file" onchange="uploadPic('img-demo')">
										</div><span id="pictureTip"></span>
                   			</td>
                        </tr>
                        <tr>
                    		<td class="a-title">&nbsp;</td>
                    		<td>
                    			<img src="" id="img-demo-img" width="280" alt="请上传App logo"/>
                    		</td>
                		</tr>
                        <tr>
                            <td class="a-title"><font class="s-red">*</font>App标题：</td>
                            <td>
                                <input type="text" <#-- disabled --> value="<#if (adapp?exists)>${adapp.appTitle!""}</#if>"
                                       style="width: 130px;" id="appTitle" name="appTitle" autocomplete="off"
                                       class="form-control">
                                <span id="appTitleTip"></span>
                            </td>
                        </tr>
                        <#-- <tr>
                            <td class="a-title"><font class="s-red">*</font>App序列号：</td>
                            <td><input type="text"  style="width: 130px;" id="appSid" name="appSid"
                                       value="<#if (adapp?exists)>${adapp.appSid!""}</#if>"
                                       autocomplete="off" class="form-control">
                                <span id="locationTip"></span>
                            </td>
                        </tr>
                         -->
                        <tr>
                            <td></td>

                            <td rowspan="6" colspan="6">
                                <div class="col-50"> 
                                    <button class="btn btn-red" id="submit">保存</button>
                                    <button class="btn btn-primary ml-20" id="back">返回</button>
                                </div>
                            </td>
                            <td></td>
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

<script type="text/javascript"
        src="${model.static_domain}/js/jquery-2.1.4.min.js"></script>
<!-- 下拉 -->
<link
        href="${model.static_domain}/js/select/jquery.searchableSelect.css"
        rel="stylesheet">
<script
        src="${model.static_domain}/js/select/jquery.searchableSelect.js"></script>
<script src="${model.static_domain}/js/ajaxfileupload.js"></script>
<!-- 时期 -->
<link href="${model.static_domain}/js/date/daterangepicker.css"
      rel="stylesheet">
<script type="text/javascript"
        src="${model.static_domain}/js/date/moment.min.js"></script>
<script type="text/javascript"
        src="${model.static_domain}/js/date/jquery.daterangepicker.js"></script>
<script type="text/javascript" src="${model.static_domain}/js/date.js"></script>
<!-- 图片缩放 -->
<script type="text/javascript" src="${model.static_domain}/js/jquery.resize.js"></script>
<!-- formValidator -->
<link type="text/css" rel="stylesheet" href="${model.static_domain}/js/formValidator/style/validator.css"></link>
<script type="text/javascript" src="${model.static_domain}/js/formValidator/formValidator-4.0.1.js"></script>
<script type="text/javascript">
    $.fn.serializeObject = function () {
        var o = {};
        var a = this.serializeArray();
        $.each(a, function () {
            if (o[this.name] !== undefined) {
                if (!o[this.name].push) {
                    o[this.name] = [o[this.name]];
                }
                o[this.name].push(this.value || '');
            } else {
                o[this.name] = this.value || '';
            }
        });
        return o;
    };

	
    $(function () {
    
        $('#form').submit(function () {
            return false;
        })
		$("#back").click(function () {
        	history.back();
    	});
    	
    	var pic = $('#pic').val();
       	$("#img-demo-img").attr("src",pic);//样例图片地址
      
        $.formValidator.initConfig({
            validatorGroup:"2",
            submitButtonID: "submit",
            debug: false,
            submitOnce: true,
            errorFocus: false,
            onSuccess: function(){
            	var id = $('#id').val();
            	var appName = $("#appName").val();
                var appTitle = $("#appTitle").val();
				var samplePicUrl = $("#img-demo-bak").val();
            
				$.ajax({
					url : '/app/save',
					type : 'POST',
					data : {
						"id": id,
						"appName": appName,
                        "appTitle": appTitle,
                        "appPictureUrl": samplePicUrl
					},
					dataType : "json",
					traditional : true,
					success : function (datas) {
                        var resultRet = datas.ret;
                        if (resultRet.code == 101) {
                            layer.confirm(resultRet.resultDes, {
                                icon: 2,
                                btn: ['确定'] //按钮
                            });
                        } else {
                            layer.confirm("保存成功", {
                                icon: 1,
                                btn: ['确定'] //按钮
                            }, function () {
                                window.location = "/app/list";
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
        
        $("#appName").formValidator({
			validatorGroup:"2",
			onShow:"　",
			onFocus:"请输入app名称",
			onCorrect:"　"
		}).regexValidator({
			regExp:"^\\S+$",
			onError:"app名称不能为空，请输入"
		});
		$("#appTitle").formValidator({
			validatorGroup:"2",
			onShow:"　",
			onFocus:"请输入app标题",
			onCorrect:"　"
		}).regexValidator({
			regExp:"^\\S+$",
			onError:"app标题不能为空，请输入"
		});
        
    });
    
    	function uploadPic(id){
		var appName = $("#"+id).val();
	
		<#-- if(!/\.(jpg|JPG)$/.test(appName)) {
			layer.confirm("图片类型必须是jpg格式", {
				icon: 0,
				btn: ['确定'] //按钮
			});
			return false;
		} -->
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
<@model.webend />
