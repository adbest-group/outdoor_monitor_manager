<#assign webTitle="积分管理" in model> <#assign webHead in model> </#assign>
<@model.webhead />
<!-- 头部 -->
<@model.webMenu current="积分管理" child="用户积分管理" />
<link rel="stylesheet" type="text/css"
	href="${model.static_domain}/css/new_main.css">
<link rel="stylesheet" type="text/css"
	href="${model.static_domain}/css/icon_fonts.css">
<!-- 特色内容 -->
<div class="main-container" style="height: auto;">
	<div class="main-box">
		<div class="title clearfix" style="display: block;">
		
			<div class="search-box search-ll" style="margin: 0 0 0 20px">
			
                <div class="inp">
                    <input type="text" placeholder="请输入用户名称" value="${searchName?if_exists}" id="searchName" name="username">
                </div>
                <button type="button" class="btn btn-red" autocomplete="off" id="searchBtn">查询</button>
            </div>
		</div>

		<!-- 数据报表 -->
		<div class="data-report">
			<div class="bd">
				<table width="100%" cellpadding="0" cellspacing="0" border="0"
					class="tablesorter" id="plan">
					<thead>
						<tr>
							<th width="30">序号</th>
							<th>用户名</th>
                        	<th>积分值</th>
                        	<th>积分明细</th>
                        	<th>创建时间</th>
                        	<th>更新时间</th>
						</tr>
					</thead>
					<tbody>
						<#if (bizObj.list?exists && bizObj.list?size>0) > <#list
						bizObj.list as type>
						<tr>
							<td>${(bizObj.page.currentPage-1)*20+type_index+1}</td>
							<td>${type.username!""}</td>
							<td>${type.point!""}</td>
							<td>${type.result!""}</td>
							<td>${type.createTime?string('yyyy-MM-dd HH:mm:ss')}</td>
							<td>${type.updateTime?string('yyyy-MM-dd HH:mm:ss')}</td> 
						</tr>
						</#list> <#else>
						<tr>
							<td colspan="20">没有相应结果。</td>
						</tr>
						</#if>
					</tbody>
					<!-- 翻页 -->
					<@model.showPage url=vm.getUrlByRemoveKey(thisUrl, ["start",
					"size"]) p=bizObj.page parEnd="" colsnum=9 />
				</table>
			</div>
		</div>
	</div>
</div>
</div>
</div>
</div>
<script type="text/javascript"
	src="${model.static_domain}/js/jquery-2.1.4.min.js"></script>
<!-- 下拉 -->
<link
	href="${model.static_domain}/js/select/jquery.searchableSelect.css"
	rel="stylesheet">
<script
	src="${model.static_domain}/js/select/jquery.searchableSelect.js"></script>

<script type="text/javascript" src="/static/js/jquery.citys.js"></script>
<script type="text/javascript">
 	// 查询
    $("#searchBtn").on("click", function () {
        var strParam = "";
        var username = $("#searchName").val();
        
        if (username != null && $.trim(username).length) {
            strParam = strParam + "?username=" + username;
        }

        window.location.href = "/sysResources/user/pointList" + strParam;
    });
</script>
<!-- 特色内容 -->
<@model.webend />
