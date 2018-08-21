<#assign webTitle="客户类型管理" in model>
<#assign webHead in model>
</#assign>
<@model.webhead />
<!-- 头部 -->
<@model.webMenu current="资源管理" child="customerType" />
<div class="main-container" style="height: auto;">
    <div class="main-box ott-market">
        <div class="title clearfix">
        <#if user.usertype !=6>
            <a href="javascript:;" class="add-new-btn ll" id="add_type"><i></i> 新建客户类型</a>
        </#if>
            <div class="search-box search-ll" style="margin: 0 0 0 20px">
                <div class="inp">
                    <input type="text" placeholder="请输入客户行业类型名称" value="${searchName?if_exists}" id="searchName" name="searchName">
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
                        <th>行业类型</th>
                        <th>用户</th>
                        <#if user.usertype !=6>
                        <th>操作</th>
                        </#if>
                    </tr>
                    </thead>
                    <tbody>
                    <#if (bizObj.list?exists && bizObj.list?size>0)>
                        <#list bizObj.list as obj>
                        <tr>
                            <td width="30">${(bizObj.page.currentPage-1)*20+obj_index+1}</td>
                            <td>${obj.name?if_exists}</td>
                            <td>
                            	<a href="javascript:void(0);" onclick="users('${obj.id}');">查看</a>
                            </td>
                            <#if user.usertype !=6>
                            <td>
                                <a href="javascript:void(0);" onclick="edit('${obj.id}');">编辑</a>
                            </td>
                            </#if>
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
        var searchName = $("#searchName").val();
        
        if (searchName != null && $.trim(searchName).length) {
            strParam = strParam + "?name=" + searchName;
        }

        window.location.href = "/customerType/list" + strParam;
    });

    $("#add_type").on("click", function () {
        //iframe层
        layer.open({
            type: 2,
            title: '添加客户类型',
            shadeClose: true,
            shade: 0.8,
            area: ['600px', '480px'],
            content: '/customerType/add' //iframe的url
        });
    });

    function edit(id) {
    	//iframe层
        layer.open({
            type: 2,
            title: '修改客户类型',
            shadeClose: true,
            shade: 0.8,
            area: ['600px', '480px'],
            content: '/customerType/edit?id=' + id //iframe的url
        });
    }

	// 修改是否需要唯一标识
	function updateNeed(id, uniqueKeyNeed) {
		$.ajax({
            url: "/mediaType/updateUniqueTypeNeed",
            type: "post",
            data: {
                "id": id,
                "uniqueKeyNeed": uniqueKeyNeed
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
                    var msg = "";
                    if (status == "2") {
                        msg = "操作成功";
                    } else {
                        msg = "操作成功";
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
	
    // 修改媒体类型状态
    function updateStatus(id, status, mediaType) {
        layer.confirm("确认修改媒体类型状态", {
            icon: 3,
            btn: ['确定', '取消'] //按钮
        }, function () {
            doUpdate(id, status, mediaType);
        });
    }

    function doUpdate(id, status, mediaType) {
        $.ajax({
            url: "/mediaType/updateMediaTypeStatus",
            type: "post",
            data: {
                "id": id,
                "status": status,
                "mediaType": mediaType
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
                    var msg = "";
                    if (status == "2") {
                        msg = "停用成功";
                    } else {
                        msg = "启用成功";
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
    function users(id) {
        layer.open({
            type: 2,
            title: '用户',
            shadeClose: true,
            shade: 0.8,
            area: ['890px', '480px'],
            content: '/customerType/users?customerTypeId=' + id //iframe的url
        });
    }
</script>
<@model.webend />