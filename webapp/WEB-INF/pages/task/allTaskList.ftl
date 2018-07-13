<#assign webTitle="任务管理" in model>
<#assign webHead in model>
</#assign>
<@model.webhead />
<!-- 头部 -->
<@model.webMenu current="资源管理" child="任务管理" />

<div class="main-container" style="height: auto;">
    <div class="main-box">
        <div class="title clearfix" style="display:block;">
            <div class="search-box search-ll" style="margin: 0 0 0 20px">
                <form id="form" method="get" action="/task/allList">
                    <!--下拉框-->
                    <div class="select-box select-box-140 un-inp-select ll">
                        <select name="activityId" class="select" id="activityId">
                            <option value="">所有活动</option>
                        <@model.showAllActivityOps value="${bizObj.queryMap.activityId?if_exists}"/>
                        </select>
                    </div>
                    <div class="select-box select-box-100 un-inp-select ll">
                        <select class="select" name="taskType">
                            <option value="">任务类型</option>
                        <@model.showMonitorTaskTypeOps value="${bizObj.queryMap.taskType?if_exists}" />
                        </select>
                    </div>
                    <div class="select-box select-box-100 un-inp-select ll">
                        <select class="select" name="status">
                            <option value="">所有状态</option>
                        <@model.showMonitorTaskStatusOps value="${bizObj.queryMap.status?if_exists}" />
                        </select>
                    </div>
                    <div class="select-box select-box-100 un-inp-select ll">
                        <select class="select" name="problemStatus">
                            <option value="">所有问题状态</option>
                        <@model.showProblemStatusList value="${bizObj.queryMap.problemStatus?if_exists}" />
                        </select>
                    </div>
                   <#--   <div class="ll inputs-date">
                  <input class="ui-date-button" type="button" value="昨天" alt="-1" name="">
                    <input class="ui-date-button" type="button" value="近7天" alt="-6" name="">
                    <input class="ui-date-button on" type="button" value="近30天" alt="-29" name=""> 
                        <div class="date">
                            <input id="dts" class="Wdate" type="text" name="startDate"
                                   value="${bizObj.queryMap.startDate?if_exists}"> 
                            <input id="dt" class="Wdate" type="text" name="endDate"
                                   value="${bizObj.queryMap.endDate?if_exists}">
                        </div>
                    </div>-->
                    <button type="button" class="btn btn-red" style="margin-left:10px;" autocomplete="off"
                            id="searchBtn">查询
                    </button>
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
                        <th>广告位名称</th>
                        <th>任务类型</th>
                        <th>任务日期</th>
                        <th>任务持续天数</th>
                        <th>任务执行人员</th>
                        <th>任务状态</th>
                        <th>问题状态</th>
                        <th>审核人员</th>
                        <th>指派人员</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#if (bizObj.list?exists && bizObj.list?size>0) >
                        <#list bizObj.list as task>
                        <tr>
                            <td width="30">${(bizObj.page.currentPage-1)*20+task_index+1}</td>
                            <td>${task.activityName!""}</td>
                            <td>${task.seatInfoName!""}</td>
                            <td>${vm.getMonitorTaskTypeText(task.taskType)}</td>
                            <td>${task.monitorDate?string('yyyy-MM-dd')}</td>
    						<td>${task.monitorLastDays!""}</td>
                            <td>${task.userRealName!""}</td>
                            <td>${vm.getMonitorTaskStatusText(task.status)}</td>
                            <td>${vm.getProblemStatusText(task.problemStatus!0)}</td>
                            <td>${task.assessorName!""}</td>
                			<td>${task.assignorName!""}</td>
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



</script>


<@model.webend />