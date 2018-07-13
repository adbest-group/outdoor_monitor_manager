<link rel="stylesheet" type="text/css" href="${model.static_domain}/css/new_main.css">
<link rel="stylesheet" type="text/css" href="${model.static_domain}/css/icon_fonts.css">
	<style type="text/css">
		html, body{ min-width: auto; }
		.basic-info .bd .a-title{ width: 100px; }
        .basic-info .bd td{ padding: 0 10px;}
		.role-nav-authority li{ float: left; overflow: hidden; margin-bottom: 5px; height: 25px;}
		.trr{display:flex;flex-direction:row;align-items:center;}
		.item{}
	</style>

<div class="basic-info">
	<div class="bd">
		<input type="hidden" id="groupId" value="${groupId?if_exists}">
		<table width="100%" cellpadding="0" cellspacing="0" border="0" class="tablesorter">
			<tbody>
				<#if (sysUsers?exists && sysUsers?size>0)>					 
                    <#list 1..((sysUsers?size)/4)?ceiling as k>
                    <tr class='trr'>
                        <#list (4*k-3)..4*k as innerLoop>
	                    	<td class='itemCheckBox item' style='padding:0px'>
	                    		<#if (innerLoop <= sysUsers?size)>
	                    			<input type='checkbox' checked="checked" id='${sysUsers[innerLoop - 1].id?if_exists}' name='userIds' value='${sysUsers[innerLoop - 1].id?if_exists}'/>
	                    		</#if>
	                    	</td>
	                        <td class='itemName item' style='padding:0px'>
	                        	<#if (innerLoop <= sysUsers?size)>
	                        		${sysUsers[innerLoop - 1].realname?if_exists}
	                        	</#if>
	                        </td>
                   	    </#list>               		                                               
                    </tr>
                    </#list>	                
				</#if>
			
				<#if (sysUserss?exists && sysUserss?size>0)>					 
                    <#list 1..((sysUserss?size)/4)?ceiling as k>
                    <tr class='trr'>
                        <#list (4*k-3)..4*k as innerLoop>
	                    	<td class='itemCheckBox item' style='padding:0px'>
	                    		<#if (innerLoop <= sysUserss?size)>
	                    			<input type='checkbox' id='${sysUserss[innerLoop - 1].id?if_exists}' name='userIds' value='${sysUserss[innerLoop - 1].id?if_exists}'/>
	                    		</#if>
	                    	</td>
	                        <td class='itemName item' style='padding:0px'>
	                        	<#if (innerLoop <= sysUserss?size)>
	                        		${sysUserss[innerLoop - 1].realname?if_exists}
	                        	</#if>
	                        </td>
                   	    </#list>               		                                               
                    </tr>
                    </#list>	                
				</#if>
			
				<tr>
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
<script type="text/javascript">
$(function(){
	var bodyWidth =	$('.trr').width()
	$('.itemCheckBox').css({'width':bodyWidth/24,'padding':0})
	$('.itemName').css({'width':bodyWidth/6,'padding':0})
	
	//表单处理
    $.formValidator.initConfig({
        validatorGroup: "2",
        submitButtonID: "userSubmit",
        debug: true,
        submitOnce: false,
        errorFocus: false,
        onSuccess: function () {
            var groupId = $("#groupId").val();
            var userIds = [];
		    $("input[name='userIds']:checked").each(function (i, n) {
		        userIds.push($(n).val());
		    });
            $.ajax({
                url: "/sysResources/saveCustomer",
                type: "post",
                data: {
                    "groupId": $("#groupId").val(),
                    "userIds": userIds.join(",")
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
	
})

	
</script>


