<#assign webTitle="积分管理" in model> <#assign webHead in model> </#assign>
<@model.webhead />
<!-- 头部 -->
<@model.webMenu current="积分管理" child="积分设置管理" />
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
                    <input type="text" placeholder="请输入积分名称" value="${searchName?if_exists}" id="searchName" name="name">
                </div>
                <button type="button" class="btn btn-red" autocomplete="off" id="searchBtn">查询</button>
                <button type="button" class="btn btn-red" autocomplete="off"
					onclick="window.location.href='/sysResources/pointEdit'">新增</button>
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
							<th>积分设置名称</th>
                        	<th>积分值</th>
                        	<th>创建时间</th>
                        	<th>更新时间</th>
                        	<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<#if (bizObj.list?exists && bizObj.list?size>0) > <#list
						bizObj.list as type>
						<tr>
							<td>${(bizObj.page.currentPage-1)*20+type_index+1}</td>
							<td>${type.name!""}</td>
							<td>${type.point!""}</td>
							<td>${type.createTime?string('yyyy-MM-dd HH:mm:ss')}</td>
							<td>${type.updateTime?string('yyyy-MM-dd HH:mm:ss')}</td>
						
							<td style="width: 80px">
								<a href="/sysResources/pointEdit?id=${type.id}" style="margin-right: 5px">编辑</a>
	                        </td>
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
        var name = $("#searchName").val();
        
        if (name != null && $.trim(name).length) {
            strParam = strParam + "?name=" + name;
        }

        window.location.href = "/sysResources/pointList" + strParam;
    });
</script>
<!-- 特色内容 -->
<@model.webend />
