<#assign webTitle="监测管理" in model>
<#assign webHead in model>
</#assign>
<@model.webhead />
<!-- 头部 -->
<@model.webMenu current="监测管理" child="监测管理" />

<!-- 特色内容 -->
<div class="main-container" style="height: auto;">
    <div class="main-box">
        <div class="title clearfix" style="display:block;">
            <div class="search-box search-ll" style="margin: 0 0 0 20px">
                <form id="form" method="get" action="/task/list">
                    <!--任务下拉框-->
                    <div class="select-box select-box-140 un-inp-select ll">
                        <select name="activityId" class="select" id="activityId">
                            <option value="">所有活动</option>
                        <@model.showAllActivityOps value="${bizObj.queryMap.activityId?if_exists}"/>
                        </select>
                    </div>
                    <div class="select-box select-box-100 un-inp-select ll">
                        <select class="select" name="taskType">
                            <option value="">任务类型</option>
                            <#-- 
                            <option value="1">上刊监测</option>
                            <option value="2">投放期间监测</option>
                            <option value="3">下刊监测</option>
                             -->
                        	<@model.showMonitorTaskTypeOps value="${bizObj.queryMap.taskType?if_exists}" />
                        </select>
                    </div>
                    <div class="select-box select-box-100 un-inp-select ll">
                        <select class="select" name="status">
                        	<option value="3" <#if (status?exists&&status == '3')>selected</#if>>待审核</option>
                        	<option value="4" <#if (status?exists&&status == '4')>selected</#if>>审核通过</option>
                        	<option value="5" <#if (status?exists&&status == '5')>selected</#if>>审核未通过</option>
                        	<#-- <option value="7" <#if (status?exists&&status == '7')>selected</#if>>待激活</option> -->
                        	<option value="8" <#if (status?exists&&status == '8')>selected</#if>>可抢单</option>
                        	<option value="1" <#if (status?exists&&status == '1')>selected</#if>>待指派</option>
                        	<option value="2" <#if (status?exists&&status == '2')>selected</#if>>待执行</option>
                        	<option value="6" <#if (status?exists&&status == '6')>selected</#if>>未完成</option>
                        	<#-- <@model.showMonitorTaskStatusOps value="${bizObj.queryMap.status?if_exists}"/> -->
                        </select>
                    </div>
                    <div class="select-box select-box-100 un-inp-select ll">
                        <select class="select" name="problemStatus">
                            <option value="">所有问题状态</option>
                        <@model.showProblemStatusList value="${bizObj.queryMap.problemStatus?if_exists}" />
                        </select>
                    </div>
                    <div class="ll inputs-date">
                    <#--<input class="ui-date-button" type="button" value="昨天" alt="-1" name="">-->
                    <#--<input class="ui-date-button" type="button" value="近7天" alt="-6" name="">-->
                    <#--<input class="ui-date-button on" type="button" value="近30天" alt="-29" name="">-->
                        <div class="date">
                            <input id="dts" class="Wdate" type="text" name="startDate"
                                   value="${bizObj.queryMap.startDate?if_exists}"> -
                            <input id="dt" class="Wdate" type="text" name="endDate"
                                   value="${bizObj.queryMap.endDate?if_exists}">
                        </div>
                    </div>
                    <button type="button" class="btn btn-red" style="margin-left:10px;" autocomplete="off"
                            id="searchBtn">查询
                    </button>
                    <#if (status?exists&&status == '3')>
                    <a disable="disable" style="display: inline;						
						padding: 5px 7px;
						margin: 0 2px;
						color: #6b6b6b;
						text-decoration: none;
						background-color: #f9f9f9;
						border: 1px solid #c2c2c2;
					    border-top-color: rgb(194, 194, 194);
					    border-right-color: rgb(194, 194, 194);
					    border-bottom-color: rgb(194, 194, 194);
					    border-left-color: rgb(194, 194, 194);
						outline: none;
						cursor: pointer;
						border-radius: 3px;
						overflow: hidden;"> 剩余待审核总数${shenheCount?if_exists}条</a>
					</#if>
                </form>
            </div>
        </div>

        <!-- 数据报表 -->
        <div class="data-report">
            <div class="bd">
                <table width="100%" cellpadding="0" cellspacing="0" border="0" class="tablesorter" id="plan">
                    <thead>
                    <tr>
                        <th width="30">序号</th>
                        <th>活动名称</th>
                        <th>上刊示例</th>
                        <th>投放周期</th>
                        <th>地区</th>
                        <th>媒体</th>
                        <th>广告位</th>
                        <th>执行人员</th>
                        <th>监测时间点</th>
                        <th>状态</th>
                        <th>问题状态</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#if (bizObj.list?exists && bizObj.list?size>0) >
                        <#list bizObj.list as task>
                        <tr>
                            <td width="30">${(bizObj.page.currentPage-1)*20+task_index+1}</td>
                            <td>
                                <div class="data-title w200" data-title="${task.activityName}"
                                     data-id="${task.id}">${task.activityName?if_exists}</div>
                            </td>
                            <td><img width="50" src="${task.samplePicUrl}"/></td>
                            <td>${task.startTime?string('yyyy-MM-dd')}<br/>${task.endTime?string('yyyy-MM-dd')}</td>
                            <td>${vm.getCityNameFull(task.street!task.region,"-")!""}</td>
                            <td>${task.mediaName}</td>
                            <td>${task.adSeatName!""}</td>
                            <td>${task.realname!""}</td>
                            <td>${vm.getMonitorTaskTypeText(task.taskType)}</td>
                            <td>${vm.getMonitorTaskStatusText(task.status)}</td>
                            <td>${vm.getProblemStatusText(task.problemStatus!0)}</td>
                            <td>
                            <#--<#if task.status==1><a href="javascript:assign('${task.id}')">指派</a></#if>-->
                            <#--<#if task.status==2><a href="javascript:assign('${task.id}')">重新指派</a></#if>-->
                                <#if (task.status==4&&task.problemStatus?exists&&task.problemStatus==4&&(!task.subCreated?exists||task.subCreated==2))>
                                    <a href="javascript:createTask('${task.id}');">创建复查</a></#if>
                                <#if (task.parentId?exists&&task.parentType=1)>
                                    <a href="/task/list?pid=${task.parentId}&ptype=1">复查配对</a></#if>
                                <#if (task.parentId?exists&&task.parentType=2)>
                                    <a href="/jiucuo/list?id=${task.parentId}">查看纠错</a></#if>
                                <#if (task.status==4&&task.problemStatus?exists&&task.problemStatus==4&&task.subCreated?exists&&task.subCreated==1)>
                                    <a href="/task/list?pid=${task.id}&ptype=1">复查配对</a></#if>
                                <#if (task.status==4 && task.problemStatus?exists&&task.problemStatus==4)><a
                                        href="javascript:close('${task.id}')">关闭</a></#if>
                                <#if task.status==3><a href="javascript:pass('${task.id}')">通过</a></#if>
                                <#if task.status==3><a href="javascript:reject('${task.id}')">拒绝</a></#if>
                                <a href="/task/details?task_Id=${task.id}">详情</a>
                                <#if task.status==3><a href="javascript:cancelSh('${task.id}')">撤消</a></#if>
                            <#--<#if task.status==1><a href="javascript:del('${task.id}')">删除</a></#if>-->
                            </td>
                        </tr>
                        </#list>
                    <#else>
                    <tr>
                        <td colspan="20">没有相应结果。</td>
                    </tr>
                    </#if>
                    </tbody>
                    <!-- 翻页 -->
                <@model.showPage url=vm.getUrlByRemoveKey(thisUrl, ["start", "size"]) p=bizObj.page parEnd="" colsnum=9 />
                </table>
            </div>
        </div>
    </div>
</div>
</div>
</div>
</div>
<script type="text/javascript" src="${model.static_domain}/js/jquery-2.1.4.min.js"></script>
<!-- 下拉 -->
<link href="${model.static_domain}/js/select/jquery.searchableSelect.css" rel="stylesheet">
<script src="${model.static_domain}/js/select/jquery.searchableSelect.js"></script>
<!-- 时期 -->
<link href="${model.static_domain}/js/date/daterangepicker.css" rel="stylesheet">
<script type="text/javascript" src="${model.static_domain}/js/date/moment.min.js"></script>
<script type="text/javascript" src="${model.static_domain}/js/date/jquery.daterangepicker.js"></script>
<script type="text/javascript" src="${model.static_domain}/js/date.js"></script>

<script type="text/javascript">
    $(function () {
        $(".nav-sidebar>ul>li").on("click", function () {
            $(".nav-sidebar>ul>li").removeClass("on");
            $(this).addClass("on");
        });
    });

    $(function () {
        $(window).resize();
    });

    $(window).resize(function () {
        var h = $(document.body).height() - 115;
        $('.main-container').css('height', h);
    });

    function createDateStr(alt) {
        var today = new Date();
        var t = today.getTime() + 1000 * 60 * 60 * 24 * alt;
        var newDate = new Date(t).Format("yyyy-MM-dd");
        if (alt == -6 || alt == -29)
            return newDate + " 至 " + today.Format("yyyy-MM-dd");
        return newDate + " 至 " + newDate;
    }
    var assign_ids;
    $(function () {
        $('.select').searchableSelect();

        $('.inputs-date').dateRangePicker({
            separator: ' 至 ',
            showShortcuts: false,
            getValue: function () {
                if ($('#dts').val() && $('#dt').val())
                    return $('#dts').val() + ' 至 ' + $('#dt').val();
                else
                    return '';
            },
            setValue: function (s, s1, s2) {
                $('#dts').val(s1);
                $('#dt').val(s2);

                var dateArr = [-1, -6, -29]
                $(".ui-date-button").removeClass("on");
                dateArr.forEach(function (v, i) {
                    if (s == createDateStr(v)) {
                        $(".ui-date-button").eq(i).addClass("on");
                    }
                })

            }
        });

//        var newDate=new Date().Format("yyyy-MM-dd");
//        $('.inputs-date').data('dateRangePicker').setDateRange(newDate,newDate, true);

    });

    $("#searchBtn").on("click", function () {
        $("#form").submit();
    });

    //指派
    assign = function (id) {
        assign_ids = id;
        openSelect();
    }

    //打开选择执行者
    openSelect = function () {
        layer.open({
            type: 2,
            title: '选择监测人员',
            shade: 0.8,
            area: ['400px', '220px'],
            content: '/task/selectUserExecute' //iframe的url
        });
    }
    //选择执行人后的回调
    selectUserExecuteHandle = function (userId) {
        layer.closeAll();
        if (!userId) {
            layer.alert("并没有指定执行人员");
            return;
        }

        $.ajax({
            url: "/task/assign",
            type: "post",
            data: {
                "ids": assign_ids,
                "userId": userId
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
                    layer.confirm("指派成功", {
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

    //审核通过
    pass = function (id) {
        layer.confirm("确认审核通过？", {
            icon: 3,
            btn: ['确定', '取消'] //按钮
        }, function () {
            verify(id, 4);
        });
    }

    //审核不通过
    reject = function (id) {
        layer.confirm("确认审核不通过？", {
            icon: 3,
            btn: ['确定', '取消'] //按钮
        }, function (index) {
            layer.close(index);
            layer.prompt({title: '请填写审核意见', formType: 2}, function (val, index) {
                if (val.trim().length < 1 || val.trim().length > 33) {
                    layer.alert("请填写30字以内！", {icon: 2});
                    return;
                }
                layer.close(index);
                verify(id, 5, val);
            });

        });
    }
    //发起审核请求
    verify = function (id, status, reason) {
        $.ajax({
            url: "/task/verify",
            type: "post",
            data: {
                "id": id,
                "status": status,
                "reason": reason
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
                    layer.confirm("审核成功", {
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
     //撤消审核任务
     cancelSh = function(id){
        layer.confirm("确定撤销该审核？", {
            icon: 3,
            btn: ['确定', '取消'] //按钮
        }, function(){
         	cancel(id);
         	});
      }
    //发起撤消请求
    cancel = function (id,reason) {
        $.ajax({
            url: "/task/cancel",
            type: "post",
            data: {
                "id": id,
                "reason": reason
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
                    layer.confirm("撤消成功", {
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
    //处理问题
    close = function (id) {
        layer.confirm("确认关闭问题任务？", {
            icon: 3,
            btn: ['确定', '取消'] //按钮
        }, function () {
            $.ajax({
                url: "/task/close",
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
                        layer.confirm("关闭成功", {
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
        });
    }

    //创建子任务
    createTask = function (id) {
//                layer.alert("暂未开放！谢谢！");
//                return;
        layer.confirm("确认创建监测任务？", {
            icon: 3,
            btn: ['确定', '取消'] //按钮
        }, function () {
            $.ajax({
                url: "/task/createTask",
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
                        layer.confirm("创建成功，请及时指派检测人员执行！", {
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
        });
    }

</script>
<!-- 特色内容 -->

<@model.webend />