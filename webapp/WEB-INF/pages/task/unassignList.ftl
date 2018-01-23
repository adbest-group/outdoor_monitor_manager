<#assign webTitle="监测管理-任务指派" in model>
<#assign webHead in model>
</#assign>
<@model.webhead />
    <!-- 头部 -->
    <@model.webMenu current="监测管理" child="任务指派" />

	<!-- 特色内容 -->
<div class="main-container" style="height: auto;">
    <div class="main-box">
        <div class="title clearfix" style="display:block;">
            <div class="search-box search-ll" style="margin: 0 0 0 20px">
                <form id="form" method="get" action="/task/unassign">
                    <!--销售下拉框-->
                    <div class="select-box select-box-140 un-inp-select ll">
                        <select name="activityId" class="select" id="activityId">
                            <option value="">所有活动</option>
                        <@model.showAllActivityOps value="${bizObj.queryMap.activityId?if_exists}"/>
                        </select>
                    </div>
                    <div class="ll inputs-date">
                        <#--<input class="ui-date-button" type="button" value="昨天" alt="-1" name="">-->
                        <#--<input class="ui-date-button" type="button" value="近7天" alt="-6" name="">-->
                        <#--<input class="ui-date-button on" type="button" value="近30天" alt="-29" name="">-->
                        <div class="date">
                            <input id="dts" class="Wdate" type="text" name="startDate" value="${bizObj.queryMap.startDate?if_exists}"> -
                            <input id="dt" class="Wdate" type="text" name="endDate" value="${bizObj.queryMap.endDate?if_exists}">
                        </div>
                    </div>
                    <button type="button" class="btn btn-red" style="margin-left:10px;" id="searchBtn">查询</button>
                    <button type="button" class="btn btn-red" style="margin-left:10px;" id="assignBtn">指派</button>
                </form>
            </div>
        </div>

        <!-- 数据报表 -->
        <div class="data-report">
            <div class="bd">
                <table width="100%" cellpadding="0" cellspacing="0" border="0" class="tablesorter" id="plan">
                    <thead>
                    <tr>
                        <th width="30"><input type="checkbox" name="ck-task" value=""/></th>
                        <th width="30">序号</th>
                        <th>活动名称</th>
                        <th>上刊示例</th>
                        <th>投放周期</th>
                        <th>地区</th>
                        <th>媒体</th>
                        <th>广告位</th>
                        <#--<th>执行人员</th>-->
                        <th>监测时间点</th>
                        <th>状态</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#if (bizObj.list?exists && bizObj.list?size>0) >
                        <#list bizObj.list as task>
                        <tr>
                            <td width="30"><input type="checkbox" name="ck-task" value="${task.id}"/></td>
                            <td width="30">${(bizObj.page.currentPage-1)*20+task_index+1}</td>
                            <td>
                                <div class="data-title w200" data-title="${task.activityName}" data-id="${task.id}">${task.activityName?if_exists}</div>
                            </td>
                            <td><img width="50" src="${task.samplePicUrl}"/> </td>
                            <td>${task.startTime?string('yyyy-MM-dd')} 至 ${task.endTime?string('yyyy-MM-dd')}</td>
                            <td>${task.city!""}</td>
                            <td>${task.mediaName}</td>
                            <td>${task.adSeatName}</td>
                            <#--<td>${task.userId!""}</td>-->
                            <td>${vm.getMonitorTaskTypeText(task.taskType)}</td>
                            <td>${vm.getMonitorTaskStatusText(task.status)}</td>
                            <td>
                                <#if task.status==1><a href="javascript:assign('${task.id}')">指派</a></#if>
                            </td>
                        </tr>
                        </#list>
                    <#else>
                        <tr><td colspan="20">没有相应结果。</td> </tr>
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
<script type="text/javascript" src="http://ottstatic2.taiyiplus.com/js/jquery-2.1.4.min.js"></script>
<!-- 下拉 -->
<link href="http://ottstatic2.taiyiplus.com/js/select/jquery.searchableSelect.css" rel="stylesheet">
<script src="http://ottstatic2.taiyiplus.com/js/select/jquery.searchableSelect.js"></script>
<!-- 时期 -->
<link href="${model.static_domain}/js/date/daterangepicker.css" rel="stylesheet">
<script type="text/javascript" src="http://ottstatic2.taiyiplus.com/js/date/moment.min.js"></script>
<script type="text/javascript" src="http://ottstatic2.taiyiplus.com/js/date/jquery.daterangepicker.js"></script>
<script type="text/javascript" src="http://ottstatic2.taiyiplus.com/js/date.js"></script>

<script type="text/javascript">
            $(function(){
                $(".nav-sidebar>ul>li").on("click",function(){
                    $(".nav-sidebar>ul>li").removeClass("on");
                    $(this).addClass("on");
                });
            });

    $(function(){
        $(window).resize();
    });

    $(window).resize(function() {
        var h = $(document.body).height() - 115;
        $('.main-container').css('height', h);
    });

            function createDateStr(alt){
                var today =  new Date();
                var t=today.getTime()+1000*60*60*24*alt;
                var newDate=new Date(t).Format("yyyy-MM-dd");
                if(alt==-6||alt==-29)
                    return newDate+" 至 "+today.Format("yyyy-MM-dd");
                return newDate+" 至 "+newDate;
            }
    var assign_ids;
    $(function(){
        $('.select').searchableSelect();

        $('.inputs-date').dateRangePicker({
            separator : ' 至 ',
            showShortcuts:false,
            getValue: function()
            {
                if ($('#dts').val() && $('#dt').val() )
                    return $('#dts').val() + ' 至 ' + $('#dt').val();
                else
                    return '';
            },
            setValue: function(s,s1,s2)
            {
                $('#dts').val(s1);
                $('#dt').val(s2);

                var dateArr=[-1,-6,-29]
                $(".ui-date-button").removeClass("on");
                dateArr.forEach(function(v,i){
                    if(s==createDateStr(v)){
                        $(".ui-date-button").eq(i).addClass("on");
                    }
                })

            }
        });

        $("#assignBtn").click(function(){
            if($("input[name='ck-task']:checked").length<1){
                layer.confirm('请选择需要指派的任务', {
                    icon: 0,
                    btn: ['确定'] //按钮
                });
            }else{
                var ids = [];
                $("input[name='ck-task']:checked").each(function(i,ck){
                    if(ck.value) ids.push(ck.value);
                });
                assign_ids = ids.join(",");
                openSelect();
            }
        });
        $("input[name='ck-task']").change(function(){
            if(this.value){

            }else{
                if($(this).is(":checked")){
                    $("input[name='ck-task']").prop("checked",true)
                }else{
                    $("input[name='ck-task']").removeAttr("checked");
                }
            }
        });
    });

    $("#searchBtn").on("click",function() {
        $("#form").submit();
    });

    //指派
    assign = function (id) {
        assign_ids = id;
        openSelect();
    }

    //打开选择执行者
    openSelect = function() {
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
        $.ajax({
            url: "/task/assign",
            type: "post",
            data: {
                "ids": assign_ids,
                "userId":userId
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
                    layer.confirm("指派成功", {
                        icon: 1,
                        btn: ['确定'] //按钮
                    },function () {
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
    }


</script>
<!-- 特色内容 -->

<@model.webend />