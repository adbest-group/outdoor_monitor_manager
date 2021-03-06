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
					<td class="a-title" style="padding:20px 10px;">广告主账户：</td>
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
					<td class="a-title">登录密码：</td>
					<td>
						<div style="display: none;">
						  <input type="password" name="password" value="">
						</div><input type="password" id="password" name="password" value="<#if (obj.id)?exists>******</#if>" autocomplete="off" class="form-control"> <br><span id="passwordTip"></span></td>
				</tr>
				<tr>
					<td class="a-title">客户名称：</td>
					<td><input type="text" id="name" name="name" value="${(obj.realname)?if_exists}" autocomplete="off" class="form-control"> <br><span id="nameTip"></span></td>
				</tr>
				<tr>
					<td class="a-title">联系方式：</td>
					<td><input type="text" id="telephone" name="telephone" value="${(obj.telephone)?if_exists}" autocomplete="off" class="form-control"> <br><span id="telephoneTip"></span></td>
				</tr>
				<tr>
					<td style="padding-bottom:20px;" class="a-title">客户行业：</td>
					<td style="padding-bottom:20px;">
                        <select style="width: 156px;" name="customerTypeId" class="searchable-select-holder" id="customerTypeId" >
                            <option value="">请选择</option>
                            <@model.showAllCustomerTypeOps value="${(obj.customerTypeId)?if_exists}"/>
                        </select>
                        <span id="customerTypeIdTip"></span>
                    </td>
                </tr>
                <tr>
					<td style="padding-bottom:20px;" class="a-title">所属App：</td>
					<td style="padding-bottom:20px;">
                        <select name="appTypeId" class="searchable-select-holder" id="appTypeId">
                            <option value="">请选择</option>
                            <@model.showAllAppTypeOps value="${(obj.appTypeId)?if_exists}"/> 
                        </select>
                        <span id="appTypeIdTip"></span>
                    </td>
                </tr>
				<tr>
					<td class="a-title">&nbsp;</td>
					<td>
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
<!-- 下拉 -->
<link
	href="${model.static_domain}/js/select/jquery.searchableSelect.css"
	rel="stylesheet">

<!-- 下拉 -->
<link
	href="${model.static_domain}/js/select/jquery.searchableSelect.css"
	rel="stylesheet">
<script
	src="${model.static_domain}/js/select/jquery.searchableSelect.js"></script>
<script src="${model.static_domain}/js/ajaxfileupload.js"></script>
<style>
	.searchable-select{
		width: 150px;
	}
</style>
<script>
$(function() {
	$("#customerTypeId,#appTypeId").searchableSelect();
	
	<#if !((obj.id)?exists)>
	   $("")
	</#if>
             
    var id = $("#id").val();
    // 新建账户处理
			$.formValidator.initConfig({
				validatorGroup:"2",
		        submitButtonID: "accountSubmit",
		        debug: false,
		        submitOnce: true,
		        errorFocus: false,
		        onSuccess: function(){
			        var username = $("#username").val();
	                var name = $("#name").val();
	                var telephone = $("#telephone").val();
	                var password = $("#password").val();
	                var customerTypeId = $("#customerTypeId").val();
             		var appTypeId = $("#appTypeId").val();
//		             var selRoles = "";
//				            $("input[name='role']:checked").each(function(i) {
//				            	if (0==i) {
//							 		selRoles = $(this).val();
//								} else {
//								    selRoles = selRoles + "," + $(this).val();
//								}
//				            });
		            $.ajax({
		                url: "/customer/save",
		                type: "post",
		                data: {
		                	"id": id ,
		                    "username": username,
		                    "password": password,
		                    "name": name,
		                    "telephone": telephone,
		                    "customerTypeId": customerTypeId,
		                    "appTypeId": appTypeId
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
			    // 登录账户check
			    $("#username").formValidator({
					validatorGroup:"2",
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
	    	
	    	// 联系人
    		$("#name").formValidator({
				validatorGroup:"2",
    			onShow:"　",
    			onFocus:"请输入联系人",
    			onCorrect:"　"
    		}).regexValidator({
    			regExp:"^\\S+$",
    			onError:"联系人不能为空，请输入"
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
        	
        	// 客户行业类型校验
	        $("#customerTypeId").formValidator({
	            validatorGroup:"2",
	            onShow:"",
	            onFocus:"请选择客户行业类型",
	            onCorrect:""
	        }).regexValidator({
	            regExp:"^\\S+$",
	            onError:"客户行业类型不能为空，请选择"
	        }).inputValidator({
	            min: 1,
	            onError:"客户行业类型不能为空，请选择"
	        });
        	
        	// app类型校验
	        $("#appTypeId").formValidator({
	            validatorGroup:"2",
	            onShow:"",
	            onFocus:"请选择app类型",
	            onCorrect:""
	        }).regexValidator({
	            regExp:"^\\S+$",
	            onError:"app类型不能为空，请选择"
	        }).inputValidator({
	            min: 1,
	            onError:"app类型不能为空，请选择"
	        });
	        
//        	// 权限配置
//        	$("#checkBoxUl").formValidator({
//        		validatorGroup:"2",
//        		onShow:"　",
//        		onFocus:"　",
//        		onCorrect:"　"
//        	}).functionValidator({
//        		fun:function(){
//        			if ($("input[name='role']:checked").size()>0) {
//        				return true;
//        			} else {
//        				return "请选择权限";
//        			}
//        		}
//        	});
});
            
</script>
