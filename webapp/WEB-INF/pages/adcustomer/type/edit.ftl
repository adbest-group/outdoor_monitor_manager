<link rel="stylesheet" type="text/css" href="${model.static_domain}/css/new_main.css">
<link rel="stylesheet" type="text/css" href="${model.static_domain}/css/icon_fonts.css">
	<style type="text/css">
		html, body{ min-width: auto; }
		.basic-info .bd .a-title{ width: 100px; }
        .basic-info .bd td{ padding: 0 10px;}
		.role-nav-authority li{ float: left; overflow: hidden; margin-bottom: 5px; height: 25px;}
		.role-nav-authority li label input[type=checkbox]{ width: 15px; height: 15px; margin: 2px 3px 0 0; vertical-align: text-top;}
	</style>

<div class="basic-info">
	<div class="bd">
		<table width="100%" cellpadding="0" cellspacing="0" border="0" class="tablesorter">
			<tbody>
			    <input type="hidden" name="id" id="id" value="${(obj.id)?if_exists}"/>
				<tr>
					<td class="a-title">客户行业类型名称：</td>
					<td>
					    <input type="text" id="custormerTypeName" name="custormerTypeName" value="${(obj.name)?if_exists}" autocomplete="off" class="form-control">
					    <br><span id="custormerTypeNameTip"></span>
					</td>
				</tr>
				<tr>
					<td class="a-title">&nbsp;</td>
					<td>
						<button type="button" class="btn btn-red" autocomplete="off" id="customerTypeSubmit">提　交</button>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
</div>
<script type="text/javascript" src="${model.static_domain}/js/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="${model.static_domain}/js/layer/layer.js"></script>
<script type="text/javascript" src="${model.static_domain}/js/common.js"></script>
<!-- formValidator -->
<link type="text/css" rel="stylesheet" href="${model.static_domain}/js/formValidator/style/validator.css"></link>
<script type="text/javascript" src="${model.static_domain}/js/formValidator/formValidator-4.0.1.js"></script>
<!-- 下拉 -->
<link href="${model.static_domain}/js/select/jquery.searchableSelect.css" rel="stylesheet">
<script src="${model.static_domain}/js/select/jquery.searchableSelect.js"></script>
<script>
$(function() {
	<#if !((obj.id)?exists)>
	   $("")
	</#if>

    var id = $("#id").val();
    // 新建账户处理
	$.formValidator.initConfig({
		validatorGroup:"2",
        submitButtonID: "customerTypeSubmit",
        debug: false,
        submitOnce: true,
        errorFocus: false,
        onSuccess: function(){
	        var name = $("#custormerTypeName").val();
            $.ajax({
                url: "/customerType/save",
                type: "post",
                data: {
                	"id": id,
                    "name": name
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
    
    // 客户类型名称check
    $("#custormerTypeName").formValidator({
		validatorGroup:"2",
        onShow: "　",
        onFocus: "请输入客户行业类型名称",
        onCorrect: "　"
    }).inputValidator({
        min:1,
		max:100,
		onError:"客户行业类型名称不能为空，请输入"
    }).regexValidator({
		regExp:"^\\S+$",
		onError:"客户行业类型名称不能为空，请输入"
	});
});
            
</script>