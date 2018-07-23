<link rel="stylesheet" type="text/css" href="${model.static_domain}/css/new_main.css">
<link rel="stylesheet" type="text/css" href="${model.static_domain}/css/icon_fonts.css">
	<style type="text/css">
		html, body{ min-width: auto; }
		.basic-info .bd .a-title{ width: 100px; }
        .basic-info .bd td{ padding: 0 10px;}
		.role-nav-authority li{ float: left; overflow: hidden; margin-bottom: 5px; height: 25px;}
		.role-nav-authority li label input[type=checkbox]{ width: 15px; height: 15px; margin: 2px 3px 0 0; vertical-align: text-top;}
		.bd tr .a-title{padding: 10px;}
	</style>

<div class="basic-info">
	<div class="bd">
		<table width="100%" cellpadding="0" cellspacing="0" border="0" class="tablesorter">
			<tbody>
			    <input type="hidden" name="id" id="id" value="${(obj.id)?if_exists}"/>
				<tr>
					<td class="a-title">媒体类型名称：</td>
					<td>
					<#if (obj.id)?exists>
					   ${(obj.name)?if_exists}
					    <input type="hidden"  id="mediaName" name="mediaName" value="${(obj.name)?if_exists}"/>
					<#else>
					    <input type="text"  id="mediaName" name="mediaName" value="${(obj.name)?if_exists}" autocomplete="off" class="form-control"> <br><span id="mediaNameTip"></span>
					</td>
					</#if>
				</tr>
				<tr>
					<td class="a-title">类型：</td>
					<td>
						<#if (obj.id)?exists>
						   <#if (obj?exists&&obj.mediaType?exists&&obj.mediaType == 1)>媒体大类</#if>
						   <#if (obj?exists&&obj.mediaType?exists&&obj.mediaType == 2)>媒体小类</#if>
						<#else>
							<div class="select-box select-box-100 un-inp-select ll">
								<select name="addMediaType" class="select form-control" id="addMediaType">
			            			<option value="1" <#if (obj?exists&&obj.mediaType?exists&&obj.mediaType == 1)>selected</#if>>媒体大类</option>
			                        <option value="2" <#if (obj?exists&&obj.mediaType?exists&&obj.mediaType == 2)>selected</#if>>媒体小类</option>
			                    </select>
			                    <br>
								<span id="addMediaTypeTip"></span>
								<br>
							</div>
						</#if>
					</td>
				</tr>
				
				<tr id="selectParentMedia" style="display: none">
					<td class="a-title">所属媒体大类：</td>
					<td>
						<div class="select-box select-box-100 un-inp-select ll">
		                    <select name="parentId" class="select form-control" id="parentId">
		                    	<@model.showAllAdMediaTypeAvailableOps value="<#if (obj?exists&&obj.parentId?exists)>obj.parentId</#if>"/>
		                    </select>
		                    <br>
		                    <span id="parentIdTip"></span>
		                    <br>
		                </div>
					</td>
				</tr>
				
				<tr id="multiNumPanel" style="display: none">
                     <td class="a-title">允许的活动数量：</td>
                     <td>
                     	<input type="text" id="multiNum" name="allowMulti" <#if (obj?exists&&obj.multiNum?exists)>value="${(obj.multiNum)?if_exists}"<#else>value="1"</#if> autocomplete="off" class="form-control"> 
	                     <br>
	                     <span id="multiNumTip"></span>
                     </td>
                 </tr>
				
				<#-- 
				<tr id="selectUniqueKeyNeed" style="display: none">
					<td class="a-title">需要唯一标识：</td>
					<td>
						<div class="select-box select-box-100 un-inp-select ll">
		                    <select name="uniqueKeyNeed" class="select form-control" id="uniqueKeyNeed">
		                    	<option value="2" <#if (obj?exists&&obj.uniqueKeyNeed?exists&&obj.uniqueKeyNeed == 2)>selected</#if>>不需要</option>
		                    	<option value="1" <#if (obj?exists&&obj.uniqueKeyNeed?exists&&obj.uniqueKeyNeed == 1)>selected</#if>>需要</option>
		                    </select>
		                    <br>
		                    <span id="uniqueKeyNeedTip"></span>
		                    <br>
		                </div>
					</td>
				</tr>
				 -->
				
				<tr>
					<td class="a-title">&nbsp;</td>
					<td>
						<button type="button" class="btn btn-red" autocomplete="off" id="mediaTypeSubmit">提　交</button>
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

	var type = $('#addMediaType').val();
	if(type == 1) {
		$("#selectParentMedia").hide();
		$("#multiNumPanel").hide();
		//$("#selectUniqueKeyNeed").hide();
	} else {
		$("#selectParentMedia").show();
		$("#multiNumPanel").show();
		//$("#selectUniqueKeyNeed").show();
	}

	$("#addMediaType").siblings().find(".searchable-select-item").click(function(){
        var type = $('#addMediaType').val();
		if(type == 1) {
			$("#selectParentMedia").hide();
			$("#multiNumPanel").hide();
			//$("#selectUniqueKeyNeed").hide();
		} else {
			$("#selectParentMedia").show();
			$("#multiNumPanel").show();
			//$("#selectUniqueKeyNeed").show();
		}
    });

	<#if !((obj.id)?exists)>
	   $("")
	</#if>

    var id = $("#id").val();
    // 新建账户处理
	$.formValidator.initConfig({
		validatorGroup:"2",
        submitButtonID: "mediaTypeSubmit",
        debug: false,
        submitOnce: false,
        errorFocus: false,
        onSuccess: function(){
	        var mediaName = $("#mediaName").val();
            var addMediaType = $("#addMediaType").val();
            var parentId = $("#parentId").val();
            //var uniqueKeyNeed = $("#uniqueKeyNeed").val();
            var multiNum = $("#multiNum").val(); //正整数且大于等于1
            
            if(!/^[1-9]\d*$/.test(multiNum)){
            	layer.confirm("请填写正确的允许活动数量", {
					icon: 5,
					btn: ['确定'] //按钮
				});
				return false;
            }
            
            $.ajax({
                url: "/mediaType/save",
                type: "post",
                data: {
                	"id": id,
                    "name": mediaName,
                    "parentId": parentId,
                    "mediaType": addMediaType,
                    //"uniqueKeyNeed": uniqueKeyNeed,
                    "multiNum": multiNum
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
	    // 媒体类型名称check
	    $("#mediaName").formValidator({
			validatorGroup:"2",
	        onShow: "　",
	        onFocus: "请输入媒体类型名称",
	        onCorrect: "　"
	    }).inputValidator({
	        min:1,
    		max:100,
    		onError:"媒体类型名称不能为空，请输入"
	    }).regexValidator({
			regExp:"^\\S+$",
			onError:"媒体类型名称不能为空，请输入"
		});
    } else {
    	// 媒体类型名称check
	    $("#mediaName").inputValidator({
	        min:1,
    		max:100,
    		onError:"媒体类型名称不能为空，请输入"
	    }).regexValidator({
			regExp:"^\\S+$",
			onError:"媒体类型名称不能为空，请输入"
		});
    }
});
            
</script>