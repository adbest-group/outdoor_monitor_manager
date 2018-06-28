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
			    <#if (usertype?exists && usertype==4)>
			    <tr>
			    	<td class="a-title">部门名称：</td>
			    	<td  style="padding-bottom:20px;" id="allDepartment">
				    	<div class="select-box select-box-140 un-inp-select ll">
	                        <select name="parentid" class="select" id="parentid">
	                            <option value="">所有部门</option>
	                        	<@model.showAllDepartmentOps value="${obj.parentid?if_exists}"/>
	                        </select>
	                    </div>
	                    <span id="allDepartmentTip" class="onError" style="display:none">请选择部门名称</span>
                    </td>
			    </tr>
			    </#if>
				<tr>
					<td class="a-title">组名称：</td>
					<td>
					    <input type="text" id="groupName" name="groupName" value="${(obj.name)?if_exists}" autocomplete="off" class="form-control">
					    <br><span id="groupNameTip"></span>
					</td>
				</tr>
				
				<tr>
					<td class="a-title">&nbsp;</td>
					<td>
						<button type="button" class="btn btn-red" autocomplete="off" id="groupSubmit">提　交</button>
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
	$('.select').searchableSelect({
		afterSelectItem: function(){
			if(this.holder.data("value")){
				$('#allDepartmentTip').css('display', 'none')
			}else{
				$('#allDepartmentTip').css('display', 'inline-block')
			}
		}
	});

	<#if ((obj.id)?exists)>
	 	$("#allDepartment").html("<div id='allDepartmentSelect' data-id='"+$('#parentid option:selected').val()+"'>"+ $('#parentid option:selected').text()+" </div>")
	</#if>

	var id = $("#id").val();

	if(id != null && id != ""){
		$("#groupLeaderId").siblings().find(".searchable-select-dropdown").hide();
	}

    // 新建账户处理
	$.formValidator.initConfig({
		validatorGroup:"2",
        submitButtonID: "groupSubmit",
        debug: false,
        submitOnce: false,
        errorFocus: false,
        onSuccess: function(){
	        var groupName = $("#groupName").val();
            var groupLeaderId = $("#groupLeaderId").val();
            var parentid = $("#parentid").val();
            <#if ((obj.id)?exists)>
            	parentid = $('#allDepartmentSelect').data('id')
            </#if>
			if(!parentid){
				$('#allDepartmentTip').css('display', 'inline-block');
				return false
			}else{
				$.ajax({
	                url: "/sysResources/saveGroup",
	                type: "post",
	                data: {
	                	"id": id,
	                    "name": groupName,
	                    "parentid" : parentid
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
			}
            
        },
        submitAfterAjaxPrompt: '有数据正在异步验证，请稍等...'
    });
    
    // 组名称check
    $("#groupName").formValidator({
		validatorGroup:"2",
        onShow: "　",
        onFocus: "请输入组名称",
        onCorrect: "　"
    }).inputValidator({
        min:1,
		max:100,
		onError:"组名称不能为空，请输入"
    }).regexValidator({
		regExp:"^\\S+$",
		onError:"组名称不能为空，请输入"
	});
	
});
            
</script>