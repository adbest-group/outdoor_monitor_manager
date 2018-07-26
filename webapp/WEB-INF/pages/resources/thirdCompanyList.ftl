<#assign webTitle="账户管理" in model>
<#assign webHead in model>
</#assign>
<@model.webhead />
<!-- 头部 -->
<@model.webMenu current="账户管理" child="第三方监测公司管理" />
<div class="main-container" style="height: auto;">
    <div class="main-box ott-market">
        <div class="title clearfix">
            <a href="javascript:;" class="add-new-btn ll" id="add_phoneoperator"><i></i> 新建第三方监测公司账号</a>
            <div class="search-box search-ll" style="margin: 0 0 0 20px">
                <div class="inp">
                    <input type="text" value="${nameOrUsername?if_exists}" placeholder="请输入公司名/登录账户" id="nameOrUsername"
                           name="nameOrUsername">
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
                        <th>第三方监测公司名称</th>
                        <th>登录账户</th>
                        <th>联系电话</th>
                        <th>状态</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#if (bizObj.list?exists && bizObj.list?size>0)>
                        <#list bizObj.list as partnerUser>
                        <tr>
                            <td width="30">${(bizObj.page.currentPage-1)*20+partnerUser_index+1}</td>
                            <td>${partnerUser.realname?if_exists}</td>
                            <td>${partnerUser.username?if_exists}</td>
                            <td>${partnerUser.telephone?if_exists}</td>
                            <td><span onclick="updStatus('${partnerUser.id}', '${partnerUser.status}');"
                                      class="switch<#if partnerUser.status?exists && partnerUser.status == 1> current</#if>"><s></s><b></b></span>
                            </td>
                            <td>
                                <a href="javascript:void(0);" onclick="edit('${partnerUser.id}');">编辑</a>
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
<script type="text/javascript">
$(function(){
    $(window).resize();
});

$(window).resize(function() {
    var h = $(document.body).height() - 115;
    $('.main-container').css('height', h);
});

    $("#add_phoneoperator").on("click", function () {
        //iframe层
        layer.open({
            type: 2,
            title: '新建账户',
            shadeClose: true,
            shade: 0.8,
            area: ['600px', '480px'],
            content: '/thirdCompany/toEdit' //iframe的url
        });
    });

    function edit(id) {
        layer.open({
            type: 2,
            title: '编辑账户',
            shadeClose: true,
            shade: 0.8,
            area: ['600px', '480px'],
            content: '/thirdCompany/toEdit?id=' + id //iframe的url
        });
    }

    // 查询
    $("#searchBtn").on("click", function () {
        var strParam = "";
        var nameOrUsername = $("#nameOrUsername").val();
        if (nameOrUsername != null && $.trim(nameOrUsername).length) {
            strParam = strParam + "?nameOrUsername=" + nameOrUsername;
        }

        window.location.href = "/thirdCompany/list" + strParam;
    });

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
        var toStatus = "";
        if (status == "2") {
            toStatus = "1";
        } else {
            toStatus = "2";
        }

        $.ajax({
            url: "/media/updateAccountStatus",
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
                    if (status == "2") {
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