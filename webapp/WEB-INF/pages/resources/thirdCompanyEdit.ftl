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
					<td class="a-title" style="padding:20px 10px;">登录账户：</td>
					<td style="padding:20px 10px;">
					<#if (obj.id)?exists>
					   ${(obj.username)?if_exists}
					    <input type="hidden"  id="username" name="username" value="${(obj.username)?if_exists}"/>
					<#else>
					    <input type="text"  id="username" name="username" value="${(obj.username)?if_exists}" autocomplete="off" class="form-control"> <br><span id="usernameTip"></span>
					</td>
					</#if>
				</tr>
				<tr>
                     <td class="a-title" style="padding:20px 10px;">公司名称：</td>
                     <td style="padding:20px 5px;">
                     <input type="text" id="realname" name="realname" value="${(obj.realname)?if_exists}" autocomplete="off" class="form-control"> <br><span id="realnameTip"></span></td>
                 </tr>
				<tr>
					<td class="a-title" style="padding:20px 10px;">登录密码：</td>
					<td style="padding:20px 10px;">
						<div style="display: none;">
						  <input type="password" name="password" value="">
						</div><input type="password" id="password" name="password" value="<#if (obj.id)?exists>******</#if>" autocomplete="off" class="form-control"> <br><span id="passwordTip"></span></td>
				</tr>
				
				<tr>
					<td class="a-title">联系方式：</td>
					<td><input type="text" id="telephone" name="telephone" value="${(obj.telephone)?if_exists}" autocomplete="off" class="form-control"> <br><span id="telephoneTip"></span></td>
				</tr>
				<tr>
					<td class="a-title">&nbsp;</td>
					<td>
						<br></br>
						<button type="button" class="btn btn-red" autocomplete="off" id="accountSubmit">提　交</button>
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
<script>
$(function() {
	<#if !((obj.id)?exists)>
	   $("")
	</#if>

    var id = $("#id").val();
    // 新建账户处理
			$.formValidator.initConfig({
				validatorGroup:"2",
		        submitButtonID: "accountSubmit",
		        debug: false,
		        submitOnce: false,
		        errorFocus: false,
		        onSuccess: function(){
			        var username = $("#username").val();
	                var password = $("#password").val();
	                var realname = $("#realname").val();
					var telephone = $("#telephone").val();
		            $.ajax({
		                url: "/thirdCompany/save",
		                type: "post",
		                data: {
		                	"id": id ,
		                    "username": username,
		                    "password": password,
		                    "realname": realname,
		                    "telephone": telephone
		                },
		                cache: false,
		                dataType: "json",
		                success: function(datas) {
		                    var resultRet = datas.ret;
		                    console.log(resultRet)
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
			    // 登录账户check
			    $("#username").formValidator({
					validatorGroup:"2",
					tipID:"usernameTip",
			        onShow: "　",
			        onFocus: "请输入登录账户",
			        onCorrect: "　"
			    }).inputValidator({
			        min:1,
	        		max:100,
	        		onError:"登录账户为邮箱格式，请重新输入"
			    }).regexValidator({
	        		regExp:"^(\\w-*\\.*)+@(\\w-?)+(\\.\\w{2,})$",
	        		onError:"登录账户为邮箱格式，请重新输入"
	        	}).ajaxValidator({
	        		type: "post",
			        dataType: "json",
			        async: true,
			        url: "/media/isExistsAccountName",
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
			        onError: "已存在该登录账户，请修改",
			        onWait: "正在对登录账户进行校验，请稍候..."
			    });
		    }
		    
		    // 密码check
	    	$("#password").formValidator({
				validatorGroup:"2",
	    		onShow:"　",
	    		onFocus:"请输入6-16位密码,字母和数字组合",
	    		onCorrect:"　"
	    	}).inputValidator({
	    		min:6,
	    		max:16,
	    		empty:{leftEmpty:false,rightEmpty:false,emptyError:"密码两边不能有空符号"},
	    		onError:"密码输入不正确，请重新输入"
	    	}).regexValidator({
	    		regExp:"^\\*{6}|(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$",
				onError:"密码格式不对，请重新输入"
			});
			
	    	// 第三方监测公司姓名
    		$("#realname").formValidator({
				validatorGroup:"2",
    			onShow:"　",
    			onFocus:"请输入联系人姓名",
    			onCorrect:"　"
    		}).regexValidator({
    			regExp:"^\\S{0,20}$",
    			onError:"姓名不能太长，请输入"
    		});
	    	
    		// 联系电话
        	$("#telephone").formValidator({
				validatorGroup:"2",
        		onShow:"　",
        		onFocus:"请输入手机或固定电话，如：0571-88888888",
        		onCorrect:"　"
        	}).regexValidator({
        		regExp:["^(([0\\+]\\d{2,3}-)?(0\\d{2,3})-)?(\\d{7,8})(-(\\d{3,}))?$","^(13|15|18|17|19)[0-9]{9}$"],
        		onError:"手机或电话格式不正确，请重新输入"
        	});
});
            
</script>


