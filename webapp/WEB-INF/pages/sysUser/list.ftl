<#assign webTitle="组与员工管理" in model>
<#assign webHead in model>
</#assign>
<@model.webhead />
<!-- 头部 -->
<@model.webMenu current="组与员工管理" child="admin账号管理"/>
<div class="main-container" style="height: auto;">
    <div class="main-box ott-market">
        <div class="title clearfix">
            <a href="javascript:;" class="add-new-btn ll" id="add_user"><i></i> 新建员工账号</a>
            <div class="search-box search-ll" style="margin: 0 0 0 20px">
                <div class="inp">
                    <input type="text" placeholder="请输入员工名称" value="${searchName?if_exists}" id="searchName" name="searchName">
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
                        <th>用户名</th>
                        <th>真实名字</th>
                  		<th>电话号码</th>
                  		<th>状态</th>
                  		
                  		<th>创建时间</th>
                        <th>操作</th>
                        
                    </tr>
                    </thead>
                    <tbody>
                    <#if (bizObj.list?exists && bizObj.list?size>0)>
                        <#list bizObj.list as type>
                        <tr>
                            <td width="30">${(bizObj.page.currentPage-1)*20+type_index+1}</td>
                            <td>${type.username?if_exists}</td>
                            <td>${type.realname?if_exists}</td>
                            <td>${type.telephone?if_exists}</td>
                            <td>
                            	<#if type.status?exists && type.status == 1>可用</#if>
                            	<#if type.status?exists && type.status == 2>不可用</#if>
                            </td>
                         	
                            <td>${type.createTime?string('yyyy-MM-dd HH:mm:ss')}</td>
                            <td>
                            	<#if type.isOwn?exists && type.isOwn == "1">
                                	<a href="javascript:void(0);" onclick="edit('${type.id}');">编辑</a>
                                	
                                	<#if type.status?exists && type.status == 1>
	                                	<a href="javascript:void(0);" onclick="updateStatus('${type.id}', 2);">不可用</a>
	                                </#if>
	                                <#if type.status?exists && type.status == 2>
	                            		<a href="javascript:void(0);" onclick="updateStatus('${type.id}', 1);">可用</a>
	                                </#if>
                                </#if>
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

        window.location.href = "/sysUser/list" + strParam;
    });

    $("#add_user").on("click", function () {
        //iframe层
        layer.open({
            type: 2,
            title: '添加账号',
            shadeClose: true,
            shade: 0.8,
            area: ['600px', '480px'],
            content: '/sysUser/adminAdd' //iframe的url
        });
    });

    function edit(id) {
        layer.open({
            type: 2,
            title: '修改账号',
            shadeClose: true,
            shade: 0.8,
            area: ['600px', '480px'],
            content: '/sysUser/adminEdit?id=' + id //iframe的url
        });
    }
        
    // 更新账户状态
    function updateStatus(id, status) {
        if (status == "2") {
            layer.confirm("确定要停用该账户", {
                icon: 3,
                btn: ['确定', '取消'] //按钮
            }, function () {
                doUpdate(id, status);
            });
        } else {
            doUpdate(id, status);
        }
    }

    function doUpdate(id, status) {
        $.ajax({
            url: "/sysUser/adminStatus",
            type: "post",
            data: {
                "id": id,
                "status": status
            },
            cache: false,
            dataType: "json",
            success: function (result) {
                var resultRet = result.ret;
                if (resultRet.code == 101) {
                    layer.confirm(resultRet.resultDes, {
                        icon: 2,
                        btn: ['确定'] //按钮
                    });
                } else {
                    var msg = "";
                    if (status == "1") {
                        msg = "启用成功";
                    } else {
                        msg = "停用成功";
                    }
                    layer.confirm(msg, {
                        icon: 1,
                        btn: ['确定'] //按钮
                    }, function () {
                        window.location.reload();
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

    }
</script>
<@model.webend />