<#assign webTitle="App管理" in model> <#assign webHead in model> </#assign>
<@model.webhead />
<!-- 头部 -->
<@model.webMenu current="资源管理" child="App管理" />
<!-- 特色内容 -->
<div class="main-container" style="height: auto;">
	<div class="main-box">
		<div class="title clearfix" style="display: block;">
			<div class="search-box search-ll" style="margin: 0 0 0 20px">
				<button type="button" class="btn btn-red" autocomplete="off"
					onclick="window.location.href='/app/edit'">新增</button>
				<div style="border-bottom: 1px solid black; margin:10px auto"></div>
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
							<th>App名称</th>
							<th>App序列号</th>
							<th>App logo</th>
							<th>App标题</th>
							<th>创建时间</th>
							<th>更新时间</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<#if (bizObj.list?exists && bizObj.list?size>0) > <#list
						bizObj.list as adapp>
						<tr>
							<td>${(bizObj.page.currentPage-1)*20+adapp_index+1}</td>
							<td>${adapp.appName!""}</td>
							<td>${adapp.appSid!""}</td>
							<td><img width="50" src="${adapp.appPictureUrl!""}"/></td>
							<td>${adapp.appTitle!""}</td>
							<td>${adapp.createTime?string('yyyy-MM-dd HH:mm:ss')}</td>
							<td>${adapp.updateTime?string('yyyy-MM-dd HH:mm:ss')}</td>
							<td style="width: 80px">
								<#--<a href="#" style="margin-right: 5px">数据上传</a> -->
								<a href="/app/edit?id=${adapp.id}" style="margin-right: 5px">编辑</a>
                                <a href="javascript:deleteAppSid('${adapp.id}');" style="margin-right: 5px">删除</a>
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
var deleteAppSid = function(id){
        layer.confirm("确认删除？", {
            icon: 3,
            btn: ['确定', '取消'] //按钮
        }, function(){
            $.ajax({
                url: "/app/delete",
                type: "post",
                data: {
                    "id": id,
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
                        layer.confirm("删除成功", {
                            icon: 1,
                            btn: ['确定'] //按钮
                        }, function () {
                            window.location.reload();
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
        });
    };

</script>
<!-- 特色内容 -->
<@model.webend />
