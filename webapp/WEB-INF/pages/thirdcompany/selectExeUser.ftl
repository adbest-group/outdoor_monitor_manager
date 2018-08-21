<link rel="stylesheet" type="text/css" href="${model.static_domain}/css/new_main.css">
<link rel="stylesheet" type="text/css" href="${model.static_domain}/css/icon_fonts.css">
<style type="text/css">
		html, body{ min-width: 100%;overflow:auto; }
		.basic-info .bd .a-title{ width: 120px;font-size:14px; }
		
		.basic-info .bd td label{ margin-right:20px; display: inline-block; font-size:14px;}
		.basic-info .bd td span{font-size:14px;}
		.basic-info .bd .formZone li.city-item label{ margin-right: 0; }
		.file-upload{position: relative;overflow: hidden; float: left;}
		.file-upload a,.interaction-bac-big a{color:#ee5f63;text-decoration:underline;}
		.file-upload input{position: absolute;left: 0; top: 0; opacity: 0;font-size: 50px; width: 60px;}
		.interaction-bac-big img{width: 296px;height: 194px;float: left}
		.interaction-bac-big label{height: 194px;line-height: 194px;display: block;float: left;padding-left: 15px}
</style>
	<div class="basic-info">
		<div class="bd">
            <form id="subForm" method="post">
                <input type="hidden" id="id"/>
                <input type="hidden" id="companyId" value="${companyId?if_exists}"/>
			<table width="100%" cellpadding="0" cellspacing="0" border="0" class="tablesorter">
				<tbody>
				
				<tr>
					<span class="a-title" style="padding:10px">监测人员：</span>
						<div class="select-box select-box-140 un-inp-select" id="companyUserSelect">
                            <select name="user" class="select" id="user">
								<#if (userList?exists&&userList?size >0)>
									<#list userList as user>
                                        <option value="${user.id}">${user.realname}</option>
									</#list>
								</#if>
                            </select>
                        </div>
				</tr>
				<tr>
					<td class="a-title">&nbsp;</td>
					<td>
						<button type="button" class="btn btn-red" autocomplete="off" id="btnSave" style="margin-top:10px">确定</button>
					</td>
				</tr>
			</tbody>
		</table>
            </form>
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
	
	<script>
		$(function(){
			// 下拉
			$('.select').searchableSelect();
			$('#user').next().find('.searchable-select-input').css('display', 'block')
            $("form").submit(function(){return false;});
		}) 
		//演示用
        $(function(){
            $("#btnSave").click(function () {
                parent.window.selectUserExecuteHandle($("#user").val(),$("#companyId").val());
            });
        });
	</script>
