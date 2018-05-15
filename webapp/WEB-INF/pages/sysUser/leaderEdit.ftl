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
			    <input type="hidden" name="oldPassword" id="oldPassword" value="${(obj.password)?if_exists}"/> 
				<tr>
					<td class="a-title">用户名：</td>
					<td>
					    <input type="text" id="username" name="username" value="${(obj.username)?if_exists}" autocomplete="off" class="form-control" <#if (obj?exists&&obj.id?exists)>readonly="readonly"</#if>>
					    <br><span id="usernameTip"></span>&nbsp;
					</td>
				</tr>
				<tr>
					<td class="a-title">真实名字：</td>
					<td>
					    <input type="text" id="realname" name="realname" value="${(obj.realname)?if_exists}" autocomplete="off" class="form-control">
					    <br><span id="realnameTip"></span>&nbsp;
					</td>
				</tr>
				<tr>
					<td class="a-title">密码：</td>
					<td>
					    <input type="text" id="password" name="password" value="<#if (obj?exists&&obj.id?exists)>********</#if>" autocomplete="off" class="form-control">
					    <br><span id="passwordTip"></span>&nbsp;
					</td>
				</tr>
				<tr>
					<td class="a-title">手机号：</td>
					<td>
					    <input type="text" id="telephone" name="telephone" value="${(obj.telephone)?if_exists}" autocomplete="off" class="form-control">
					    <br><span id="telephoneTip"></span>&nbsp;
					</td>
				</tr>
				<tr>
					<td class="a-title">&nbsp;</td>
					<td>
						<button type="button" class="btn btn-red" autocomplete="off" id="userSubmit">提　交</button>
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

	<#if !((obj.id)?exists)>
	   $("")
	</#if>

	var id = $("#id").val();

	if(id != null && id != ""){
		$("#userLeaderId").siblings().find(".searchable-select-dropdown").hide();
	}

    // 新建账户处理
	$.formValidator.initConfig({
		validatorGroup:"2",
        submitButtonID: "userSubmit",
        debug: false,
        submitOnce: true,
        errorFocus: false,
        onSuccess: function(){
	        var username = $("#username").val();
            var realname = $("#realname").val();
            var password = $("#password").val();
            var telephone = $("#telephone").val();
          	var oldPassword = $("#oldPassword").val();
          
            $.ajax({
                url: "/sysUser/saveLeader",
                type: "post",
                data: {
                	"id": id,
                    "username": username,
                    "password": password,
		            "realname": realname,
		            "mobile": telephone,
		            "oldPassword": oldPassword
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
    
 
	

	// 密码check
	$("#password").formValidator({
		validatorGroup:"2",
		onShow:"　",
		onFocus:"请输入6-16位密码",
		onCorrect:"　"
	}).inputValidator({
		min:6,
		max:16,
		empty:{leftEmpty:false,rightEmpty:false,emptyError:"密码两边不能有空符号"},
		onError:"密码输入不正确，请重新输入"
	});
	
	// 用户名
	$("#username").formValidator({
		validatorGroup:"2",
		onShow:"　",
		onFocus:"请输入用户名",
		onCorrect:"　"
	}).inputValidator({
        min:1,
		max:100,
		onError:"用户名不能为空，请输入"
	}).regexValidator({
		regExp:"^(\\w-*\\.*)+@(\\w-?)+(\\.\\w{2,})$",
		onError:"登录用户名为邮箱格式，请重新输入"
    }).ajaxValidator({
		type: "post",
        dataType: "json",
        async: true,
        url: "/customer/isExistsAccountName",
        buttons: $("#button"),
        success: function(result) {
            if (result.ret.code == 100) {
                return true;
            }
            return false;
        },
        error: function(jqXHR, textStatus, errorThrown) {
     		layer.confirm("服务忙，请稍后再试", {
				icon: 5,
				btn: ['确定'] //按钮
			});
        },
        onError: "已存在该登录用户名，请修改",
        onWait: "正在对登录账户进行校验，请稍候..."
    });
    
	// 真实名字
	$("#realname").formValidator({
		validatorGroup:"2",
		onShow:"　",
		onFocus:"请输入真实姓名",
		onCorrect:"　"
	}).regexValidator({
		regExp:"^\\S+$",
		onError:"真实姓名不能为空，请输入"
	});
	
	// 联系电话
	$("#telephone").formValidator({
		validatorGroup:"2",
		onShow:"　",
		onFocus:"请输入手机号",
		onCorrect:"　"
	}).regexValidator({
		regExp:["^(([0\\+]\\d{2,3}-)?(0\\d{2,3})-)?(\\d{7,8})(-(\\d{3,}))?$","^(13|15|18|17|19)[0-9]{9}$"],
		onError:"手机或电话格式不正确，请重新输入"
	});
	
});
            
</script>