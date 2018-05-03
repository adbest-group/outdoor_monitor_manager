<#assign webTitle="资源管理" in model>
<#assign webHead in model>
</#assign>
<@model.webhead />
<!-- 头部 -->
<@model.webMenu current="资源管理" child="departmentList" />
<div class="main-container" style="height: auto;">
    <div class="main-box ott-market">
        <div class="title clearfix">
            <a href="javascript:;" class="add-new-btn ll" id="add_department"><i></i> 新建部门</a>
            <div class="search-box search-ll" style="margin: 0 0 0 20px">
                <div class="inp">
                    <input type="text" placeholder="请输入部门名称" value="${searchName?if_exists}" id="searchName" name="searchName">
                </div>
                <button type="button" class="btn btn-red" autocomplete="off" id="searchBtn">查询</button>
            </div>
        </div>

        <!-- 数据报表 -->
        <div class="data-report">
            <div class="bd">
                <table width="100%" cellpadding="0" cellspacing="0" border="0" class="tablesorter" id="plan">
                    <thead>
                    <tr>
                        <th>序号</th>
                        <th>部门名称</th>
                        <th>部门领导</th>
                        <th>创建时间</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#if (bizObj.list?exists && bizObj.list?size>0)>
                        <#list bizObj.list as type>
                        <tr>
                            <td width="30">${(bizObj.page.currentPage-1)*20+type_index+1}</td>
                            <td>${type.name?if_exists}</td>
                            <td>${type.leaderName?if_exists}</td>
                            <td>${type.createTime?string('yyyy-MM-dd HH:mm')}</td>
                            <td>
                                <a href="javascript:void(0);" onclick="edit('${type.id}');">编辑</a>
                            </td>
                        </tr>
                        </#list>
                    </#if>
                    </tbody>
                    <!-- 翻页 -->
                <@model.showPage url=vm.getUrlByRemoveKey(thisUrl, ["start", "size"]) p=bizObj.page parEnd="" colsnum=7 />
                </table>
            </div>
        </div>
    </div>
</div>
</div>
</div>
</div>
<!-- 下拉 -->
<link href="${model.static_domain}/js/select/jquery.searchableSelect.css" rel="stylesheet">
<script src="${model.static_domain}/js/select/jquery.searchableSelect.js"></script>

<script type="text/javascript">

	$(function() {
        $(window).resize(function() {
            var h = $(document.body).height() - 115;
            $('.main-container').css('height', h);
        });
        $(window).resize();
    });

    // 查询
    $("#searchBtn").on("click", function () {
        var strParam = "";
        var name = $("#searchName").val();
        
        if (name != null && $.trim(name).length) {
            strParam = strParam + "?name=" + name;
        }

        window.location.href = "/sysResources/departmentList" + strParam;
    });

    $("#add_department").on("click", function () {
        //iframe层
        layer.open({
            type: 2,
            title: '添加部门',
            shadeClose: true,
            shade: 0.8,
            area: ['600px', '480px'],
            content: '/sysResources/addDepartment' //iframe的url
        });
    });

    function edit(id) {
        layer.open({
            type: 2,
            title: '修改部门',
            shadeClose: true,
            shade: 0.8,
            area: ['600px', '480px'],
            content: '/sysResources/editDepartment?id=' + id //iframe的url
        });
    }
</script>
<@model.webend />