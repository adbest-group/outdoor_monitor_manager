<link rel="stylesheet" type="text/css" href="${model.static_domain}/css/new_main.css">
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
					<td class="a-title">消息推送类型：</td>
					<td>
						<div class="select-box select-box-100 un-inp-select ll">
							<select name="addType" class="select form-control" id="addType">
		            			<option value="1" <#if (obj?exists&&obj.type?exists&&obj.type == '1')>selected</#if>>版本更新</option>
		                        <option value="2" <#if (obj?exists&&obj.type?exists&&obj.type == '2')>selected</#if>>免责声明更新</option>
		                    </select>
		                    <br>
							<span id="addTypeTip"></span>
							<br>
						</div>
					</td>
				</tr>
				<tr>
					<td class="a-title">推送内容：</td>
					<td>
					<#if (obj.id)?exists>
					   ${(obj.content)?if_exists}
					    <input type="hidden"  id="content" name="content" value="${(obj.content)?if_exists}"/>
					<#else>
					    <input type="text"  id="content" name="content" value="${(obj.content)?if_exists}" autocomplete="off" class="form-control"> <br><span id="contentTip"></span>
					</td>
					</#if>
				</tr>
				<tr>
					<td class="a-title">&nbsp;</td>
					<td>
						<button type="button" class="btn btn-red" autocomplete="off" id="typeSubmit">提　交</button>
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
	$('.select').searchableSelect();

	var type = $('#addType').val();
	if(type == 1) {
		$("#selectParentMedia").hide();
		$("#selectUniqueKeyNeed").hide();
	} else {
		$("#selectParentMedia").show();
		$("#selectUniqueKeyNeed").show();
	}

	$("#addType").siblings().find(".searchable-select-item").click(function(){
        var type = $('#addType').val();
		if(type == 1) {
			$("#selectParentMedia").hide();
			$("#selectUniqueKeyNeed").hide();
		} else {
			$("#selectParentMedia").show();
			$("#selectUniqueKeyNeed").show();
		}
    });

	<#if !((obj.id)?exists)>
	   $("")
	</#if>

    var id = $("#id").val();
    // 新建账户处理
	$.formValidator.initConfig({
		validatorGroup:"2",
        submitButtonID: "typeSubmit",
        debug: false,
        submitOnce: true,
        errorFocus: false,
        onSuccess: function(){
	        var content = $("#content").val();
            var addType = $("#addType").val();
            $.ajax({
                url: "/systempush/addSystemPush",
                type: "post",
                data: {
                	"id": id,
                    "content": content,
                    "type": addType
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
    
    if(id == null || id.length==0){
	    // 推送内容check
	    $("#content").formValidator({
			validatorGroup:"2",
	        onShow: "　",
	        onFocus: "请输入推送内容",
	        onCorrect: "　"
	    }).inputValidator({
	        min:1,
    		max:100,
    		onError:"推送内容不能为空，请输入"
	    }).regexValidator({
			regExp:"^\\S+$",
			onError:"推送内容不能为空，请输入"
		});
    } else {
    	// 推送内容check
	    $("#content").inputValidator({
	        min:1,
    		max:100,
    		onError:"推送内容不能为空，请输入"
	    }).regexValidator({
			regExp:"^\\S+$",
			onError:"推送内容不能为空，请输入"
		});
    }
});
            
</script>