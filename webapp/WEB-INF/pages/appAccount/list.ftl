<#assign webTitle="账户管理" in model>
<#assign webHead in model>
</#assign>
<@model.webhead />
<!-- 头部 -->
<@model.webMenu current="账户管理" child="appAccount" />
<div class="main-container" style="height: auto;">
    <div class="main-box ott-market">
        <div class="title clearfix">
            <button href="javascript:;" class="add-new-btn ll" id="add_media"><i></i> 新建账号</button>
            <div class="search-box search-ll" style="margin: 0 0 0 20px">
            	<form id="form" method="get" action="/appAccount/list">
	            	<div class="select-box select-box-100 un-inp-select ll">
	                    <select name="searchUserType" class="select" id="searchUserType">
	            			<option value="">所有类型</option>
	                        <option value="2" <#if (bizObj.queryMap.usertype?exists&&bizObj.queryMap.usertype == '2')>selected</#if>>客户人员</option>
	                    	<option value="3" <#if (bizObj.queryMap.usertype?exists&&bizObj.queryMap.usertype == '3')>selected</#if>>媒体人员</option>
	                    	<option value="4" <#if (bizObj.queryMap.usertype?exists&&bizObj.queryMap.usertype == '4')>selected</#if>>社会人员</option>
	                    </select>
	                </div>
	                <div class="inp">
	                    <input type="text" value="${nameOrUsername?if_exists}" placeholder="登录账户" id="nameOrUsername" name="nameOrUsername">
	                </div>
	                
	                <button type="button" class="btn btn-red" autocomplete="off" id="searchBtn">查询</button>
	            </form>
            </div>
        </div>

        <!-- 数据报表 -->
        <div class="data-report">
            <div class="bd">
                <table width="100%" cellpadding="0" cellspacing="0" border="0" class="tablesorter" id="plan">
                    <thead>
                    <tr>
                        <th>序号</th>
                        <th>登录账户</th>
                        <th>账户类型</th>
                        <th>所属媒体主</th>
                        <th>姓名</th>
                        <th>联系电话</th>
                        <th>状态</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#if (bizObj.list?exists && bizObj.list?size>0)>
                        <#list bizObj.list as user>
                        <tr>
                            <td width="30">${(bizObj.page.currentPage-1)*20+user_index+1}</td>
                            <td>${user.username?if_exists}</td>
                            <td>${vm.getUserExecuteTypeText(user.usertype)?if_exists}</td>
                            <td>${user.mediaName?if_exists}</td>
                            <td>${user.realname?if_exists}</td>
                            <td>${user.mobile?if_exists}</td>
                            <td><span onclick="updStatus('${user.id}', '${user.status}');"
                                      class="switch<#if user.status?exists && user.status == 1> current</#if>"><s></s><b></b></span>
                            </td>
                            <td>
                                <a href="javascript:void(0);" onclick="edit('${user.id}');">编辑</a>
                                <#--<a href="javascript:void(0);" onclick="deleteAccount('${partnerUser.id}');">删除</a>-->
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

<link href="${model.static_domain}/js/select/jquery.searchableSelect.css" rel="stylesheet">
<script src="${model.static_domain}/js/select/jquery.searchableSelect.js"></script>

<script type="text/javascript">
	$('.select').searchableSelect();

    $("#add_media").on("click", function () {
        //iframe层
        layer.open({
            type: 2,
            title: '编辑账户',
            shadeClose: true,
            shade: 0.8,
            area: ['600px', '480px'],
            content: '/appAccount/edit' //iframe的url
        });
    });

    function edit(id) {
        layer.open({
            type: 2,
            title: '编辑账户',
            shadeClose: true,
            shade: 0.8,
            area: ['600px', '480px'],
            content: '/appAccount/edit?id=' + id //iframe的url
        });
    }

    $("#searchBtn").on("click", function () {
        $("#form").submit();
    });

    // 删除账户
    function deleteAccount(id) {
        layer.confirm("确定要删除该账户", {
            icon: 3,
            btn: ['确定', '取消'] //按钮
        }, function () {
            $.ajax({
                url: "/deleteAccount",
                type: "post",
                data: {
                    "id": id
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
                        layer.confirm("删除成功", {
                            icon: 1,
                            btn: ['确定'] //按钮
                        }, function () {
                            window.location.href = "/system/account/list?nameOrUsername=" + $("#nameOrUsername").val();
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
        });
    }

    // 更新账户状态
    function updStatus(id, status) {
        if (status == "1") {
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
    	var toStatus;
    	if(status == 1){
    		toStatus = 2;
    	} else {
    		toStatus = 1;
    	}
    
        $.ajax({
            url: "/appAccount/updateAccountStatus",
            type: "post",
            data: {
                "id": id,
                "status": toStatus
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
                    if (toStatus == "1") {
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