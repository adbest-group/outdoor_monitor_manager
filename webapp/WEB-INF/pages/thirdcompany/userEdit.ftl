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
			     <input type="hidden" name="status" id="status" value="${(obj.status)?if_exists}"/>
			     <input type="hidden" name="usertype" id="usertype" value="${(obj.usertype)?if_exists}"/>
				<tr>
					<td class="a-title">监测人员账户：</td>
					<td style="padding-bottom:20px;">
					    <input type="text" <#if (obj.id)?exists>readonly</#if> id="username" name="username" value="${(obj.username)?if_exists}" autocomplete="off" class="form-control"> <br><span id="usernameTip"></span>
                    </td>
				</tr>
				<tr>
					<td class="a-title">登录密码：</td>
					<td>
						<div style="display: none;">
						  <input type="password" name="password" value="">
						</div><input type="password" id="password" name="password" value="<#if (obj.id)?exists>******</#if>" autocomplete="off" class="form-control"> <br><span id="passwordTip"></span></td>
				</tr>
				 
				<#-- <tr style="margin-bottom:20px">
					<td class="a-title">用户类型：</td>
					<td style="padding-bottom:20px;">
					<#if (obj?exists&&obj.id?exists)>
					   <#if (obj.usertype?exists&&obj.usertype==5)>
					   <div class="select-box select-box-100 un-inp-select ll">
                            <select class="select" name="usertype" id="usertype">
                            	<option value="5" disabled>第三方监测人员</option>
                            </select>
                        </div>
                        </#if>
					   <input type="hidden" id="usertype" name="usertype" value="${(obj.usertype)?if_exists}"/>
					<#else>
					   <div class="select-box select-box-110 un-inp-select ll">
                            <select class="select" name="usertype" id="usertype">
                            	<option value="5" >第三方监测人员</option>
                            </select>
                        </div>
						<br/>
						<span id="usertypeTip">&nbsp;</span>
					</td>
					</#if>
				</tr>
				 -->
				
                <tr>
                     <td class="a-title">姓名：</td>
                     <td><input type="text" id="name" name="name" value="${(obj.realname)?if_exists}" autocomplete="off" class="form-control"> <br><span id="nameTip"></span></td>
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
<!-- 下拉 -->
<link href="${model.static_domain}/js/select/jquery.searchableSelect.css" rel="stylesheet">
<script src="${model.static_domain}/js/select/jquery.searchableSelect.js"></script>
<!-- formValidator -->
<link type="text/css" rel="stylesheet" href="${model.static_domain}/js/formValidator/style/validator.css"></link>
<script type="text/javascript" src="${model.static_domain}/js/formValidator/formValidator-4.0.1.js"></script>
<script>
$(function() {
    $('.select').searchableSelect();
	$('#mediaId').next().find('.searchable-select-input').css('display', 'block');
	$('#companyId').next().find('.searchable-select-input').css('display', 'block');
	//$("#mediaTr").show();

    $("#usertype").siblings().find(".searchable-select-item").click(function(){
        if($("#usertype").val()==3){
            $("#mediaTr").show();
            $("#companyTr").hide();
		} else if($("#usertype").val()==5) {
			$("#mediaTr").hide();
            $("#companyTr").show();
		} else {
            $("#mediaTr").hide();
            $("#companyTr").hide();
		}
    });
    
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
                var usertype = $("#usertype").val();
                var password = $("#password").val();
                var companyId = $("#companyId").val();
				var status = $("#status").val();
				var usertype = $("#usertype").val();
	            $.ajax({
	                url: "/thirdCompany/saveUser",
	                type: "post",
	                data: {
	                	"id": id ,
	                    "username": username,
	                    "password": password,
	                    "name": name,
	                    "companyId": companyId,
	                    "status" : status,
	                    "usertype" :usertype
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
		    
	    var userType = $("#usertype").val();
	    
		if(id != null && id != "" <#if (obj?exists&&obj.usertype?exists)> && userType != 2 </#if>){
			// 登录账户check
		    $("#username").formValidator({
				validatorGroup:"2",
		        onShow: "　",
		        onFocus: "请输入登录账户，使用手机号",
		        onCorrect: "　"
				<#--<#if (obj?exists&&obj.username?exists)>,defaultValue:"${obj.username}"</#if>-->
		    }).inputValidator({
		        min:1,
        		max:11,
        		onError:"登录账户为手机号码，请重新输入"
		    }).regexValidator({
        		regExp:"^1([358][0-9]|4[579]|66|7[0135678]|9[89])[0-9]{8}$",
        		onError:"登录账户为手机号码，请重新输入"
        	}); /**.ajaxValidator({
        		type: "post",
		        dataType: "json",
		        async: false,
				data:{id:$("#id").val()},
		        url: "/appAccount/isExistsAccountName",
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
		    }).defaultPassed(); **/
		} else if(id != null && id != "" <#if (obj?exists&&obj.usertype?exists)> && userType == 2 </#if>) {
			
		} else {
			// 登录账户check
		    $("#username").formValidator({
				validatorGroup:"2",
		        onShow: "　",
		        onFocus: "请输入登录账户，使用手机号",
		        onCorrect: "　"
		    }).inputValidator({
		        min:1,
        		max:11,
        		onError:"登录账户为手机号码，请重新输入"
		    }).regexValidator({
        		regExp:"^1([358][0-9]|4[579]|66|7[0135678]|9[89])[0-9]{8}$",
        		onError:"登录账户为手机号码，请重新输入"
        	}).ajaxValidator({
        		type: "post",
		        dataType: "json",
		        async: false,
				data:{id:$("#id").val()},
		        url: "/appAccount/isExistsAccountName",
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
		    }).defaultPassed();
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
			onFocus:"请输入姓名",
			onCorrect:"　"
		}).regexValidator({
			regExp:"^\\S{0,20}$",
			onError:"姓名不能太长，请输入"
		});
		
});
            
</script>


