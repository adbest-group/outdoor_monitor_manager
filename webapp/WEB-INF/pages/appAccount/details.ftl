<link rel="stylesheet" type="text/css" href="${model.static_domain}/css/new_main.css">
<link rel="stylesheet" type="text/css" href="${model.static_domain}/css/icon_fonts.css">
	<style type="text/css">
		html, body{ min-width: auto; }
		.basic-info .bd .a-title{ width: 100px; }
        .basic-info .bd td{ padding: 0 10px;}
		.role-nav-authority li{ float: left; overflow: hidden; margin-bottom: 5px; height: 25px;}
		.role-nav-authority li label input[type=checkbox]{ width: 15px; height: 15px; margin: 2px 3px 0 0; vertical-align: text-top;}
	</style>

<div class="data-report" style="height:100%;overflow-y:auto;margin:0px;">
	<div class="bd">
		<table width="100%" cellpadding="0" cellspacing="0" border="0" class="tablesorter" id="plan">
            <thead>
            <tr>
            	<th>序号</th>
                <th>更新时间</th>
                <th>过去用户类型</th>
                <th>过去所属媒体主</th>
                <th>现在用户类型</th>
                <th>现在所属媒体主</th>
                <th>操作人</th>
            </tr>
            </thead>
            <tbody>
            <#if (bizObj.list?exists && bizObj.list?size>0)>
                <#list bizObj.list as user>
                <tr>
                	<td width="30">${(bizObj.page.currentPage-1)*20+user_index+1}</td>
                    <td>${user.updateTime?string('yyyy-MM-dd HH:mm:ss')}</td>
                    <td><#if user.usertypeOld?exists&&user.usertypeOld==3>媒体监测人员</#if>
                    	<#if user.usertypeOld?exists&&user.usertypeOld==4>社会人员</#if>
                    </td>
                    <td>${user.mediaNameOld?if_exists}</td>
                    <td><#if user.usertypeNew?exists&&user.usertypeNew==3>媒体监测人员</#if>
                    	<#if user.usertypeNew?exists&&user.usertypeNew==4>社会人员</#if></td>
                    <td>${user.mediaNameNew?if_exists}</td>
                	<td>${user.loginName?if_exists}</td>
                </tr>
                </#list>
            </#if>
            </tbody>
            <@model.showPage url=vm.getUrlByRemoveKey(thisUrl, ["start", "size"]) p=bizObj.page parEnd="" colsnum=7 />
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
      
</script>


