<#assign webTitle="纠错管理" in model> <#assign webHead in model> </#assign>
<@model.webhead />
<!-- 头部 -->
<@model.webMenu current="纠错管理" child="纠错管理" />

<!-- 特色内容 -->
<div class="main-container" style="height: auto;">
    <div class="main-box">
        <div class="title clearfix">
            <div class="search-box search-ll" style="margin: 0 0 0 20px">
                <form id="form" method="get" action="/sysResources/jiucuoList">
                    <!--活动下拉框-->
                    <div class="select-box select-box-140 un-inp-select ll">
                        <select name="activityId" class="select" id="activityId">
                            <option value="">所有活动</option>
                        <@model.showAllActivityOps value="${bizObj.queryMap.activityId?if_exists}"/>
                        </select>
                    </div>
                    <div class="select-box select-box-100 un-inp-select ll">
                        <select class="select" name="status">
                        	<option value="">所有状态</option>
                        	<@model.showJiucuoTaskStatusOps value="${bizObj.queryMap.status?if_exists}" />
                        </select>
                    </div>
                    <div class="select-box select-box-100 un-inp-select ll">
                        <select class="select" name="problemStatus">
                            <option value="">所有问题状态</option>
                        <@model.showMediaProblemStatusList value="${bizObj.queryMap.problemStatus?if_exists}"/>
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
                    <button type="button" class="btn btn-red" style="margin-left:10px;" id="searchBtn">查询</button>
                    <button type="button" class="btn btn-red" style="margin-left:10px;" id="assignBtn">批量审核</button> 
                     <button type="button" class="btn btn-red" style="margin-left:10px;" id="batchRefuse">批量拒绝</button>

                </form>
            </div>
        </div>

        <!-- 数据报表 -->
        <div class="data-report">
            <div class="bd">
                <table width="100%" cellpadding="0" cellspacing="0" border="0" class="tablesorter" id="plan">
                    <thead>
                    <tr>
                     <th width="30"><input type="checkbox" style="visibility: hidden" id='thead-checkbox' name="ck-alltask" value=""/></th>
                        <th>序号</th>
                        <th>活动名称</th>
                        <th>纠错照片</th>
                        <th>提交时间</th>
                        <th>地区</th>
                        <th>媒体</th>
                        <th>广告位</th>
                        <th>状态</th>
                        <th>问题状态</th>
                        <th>审核人</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#if (bizObj.list?exists && bizObj.list?size>0) >
                        <#list bizObj.list as task>
                        <tr id="task_${task.id}">
                        	<td width="30"><#if (task.status?exists&&task.status == 1)><input type="checkbox" data-status='${task.status}'  name="ck-task" value="${task.id}"/></#if></td> 
                            <td width="30">${(bizObj.page.currentPage-1)*20+task_index+1}</td>
                            <td>
                                <div class="data-title w200" data-title="${task.activityName!""}"
                                     data-id="${task.id}">${task.activityName?if_exists}</div>
                            </td>
                            <td><img width="50" src="${task.picUrl1!""}"/></td>
                            <td>${task.submitTime?string('yyyy-MM-dd HH:mm:ss')}</td>
                            <td>${vm.getCityNameFull(task.street!task.region,"-")!""}</td>
                            <td>${task.mediaName!""}</td>
                            <td>${task.adSeatName!""}</td>
                            <td>${vm.getJiucuoTaskStatusText(task.status)}</td>
                            <td>${vm.getProblemStatusText(task.problemStatus!0)}</td>
                            <td>${task.assessorName!""}</td>
                            <td>
                            	<#if task.status==1><a href="javascript:pass('${task.id}');">通过</a></#if>
                                <#if task.status==1><a href="javascript:reject('${task.id}');">拒绝</a></#if>
                                <#if (task.status==2&&task.problemStatus?exists&&task.problemStatus==4&&(!task.subCreated?exists||task.subCreated==2))>
                                    <a href="javascript:createTask('${task.id}');">创建监测</a></#if>
                                <#if (task.status==2&&task.problemStatus?exists&&task.problemStatus==4&&task.subCreated?exists&&task.subCreated==1)>
                                    <a href="/task/list?pid=${task.id}&ptype=2">查看监测</a></#if>
                                <#if (task.status==2&&task.problemStatus?exists&&task.problemStatus==4)><a
                                        href="javascript:close('${task.id}');">关闭</a></#if>
                                <a href="/jiucuo/detail?id=${task.id}">详情</a>
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
<script type="text/javascript"
	src="${model.static_domain}/js/jquery-2.1.4.min.js"></script>
<!-- 下拉 -->
<link
	href="${model.static_domain}/js/select/jquery.searchableSelect.css"
	rel="stylesheet">
<script
	src="${model.static_domain}/js/select/jquery.searchableSelect.js"></script>
<!-- 时期 -->
<link href="${model.static_domain}/js/date/daterangepicker.css"
	rel="stylesheet">
<script type="text/javascript"
	src="${model.static_domain}/js/date/moment.min.js"></script>
<script type="text/javascript"
	src="${model.static_domain}/js/date/jquery.daterangepicker.js"></script>
<script type="text/javascript" src="${model.static_domain}/js/date.js"></script>

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
        
       // 如果列表中有未确认的状态就显示表头的多选框
        $("input[name='ck-task']").each(function() {
        	if($(this).data('status') === 1){
        		$('#thead-checkbox').css('visibility', 'visible')
        		return false;
        	}
        })
        
    //批量审核纠错
       $("#assignBtn").click(function(){
            if($("input[name='ck-task']:checked").length<1){
                layer.confirm('请选择需要审核的任务', {
                    icon: 0,
                    btn: ['确定'] //按钮
                });
            }else{
           		var ids = [];
                $("input[name='ck-task']:checked").each(function(i,ck){
                    if(ck.value) ids.push(ck.value);
                });
                id_sel = ids.join(",");
	             $.ajax({
			            url: "/jiucuo/verify",
			            type: "post",
			            data: {
			                "ids": id_sel,
			                "status": 2
			            },
			            cache: false,
			            dataType: "json",
			            success: function(datas) {
			                var resultRet = datas.ret;
			                if (resultRet.code == 101) {
			                    layer.confirm(resultRet.resultDes, {
			                        icon: 2,
			                        btn: ['确定'] //按钮
			                    }, function(){
			                        window.location.reload();
			                    });
			                } else {
			                    layer.confirm("确认成功", {
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
           });
           
          //批量拒绝通过任务
        $("#batchRefuse").click(function(){
        	var id_sel;
            if($("input[name='ck-task']:checked").length<1){
                layer.confirm('请选择需要拒绝的活动', {
                    icon: 0,
                    btn: ['确定'] //按钮
                });
            }else{
                var ids = [];
                $("input[name='ck-task']:checked").each(function(i,ck){
                    if(ck.value) ids.push(ck.value);
                });
                id_sel = ids.join(",");
	             $.ajax({
			            url: "/jiucuo/verify",
			            type: "post",
			            data: {
			                "ids": id_sel,
			                "status": 3
			            },
			            cache: false,
			            dataType: "json",
			            success: function(datas) {
			                var resultRet = datas.ret;
			                if (resultRet.code == 101) {
			                    layer.confirm(resultRet.resultDes, {
			                        icon: 2,
			                        btn: ['确定'] //按钮
			                    }, function(){
			                        window.location.reload();
			                    });
			                } else {
			                    layer.confirm("拒绝成功", {
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
        });

        $("input[name='ck-alltask']").change(function(){
            if($(this).is(":checked")){
                $("input[name='ck-task']").prop("checked",true)
            }else{
                $("input[name='ck-task']").removeAttr("checked");
            }
        });
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

        
//        var newDate=new Date().Format("yyyy-MM-dd");
//        $('.inputs-date').data('dateRangePicker').setDateRange(newDate,newDate, true);

    $("#searchBtn").on("click", function () {
        $("#form").submit();
    });
    //审核通过
    pass = function (id) {
        layer.confirm("确认审核通过？", {
            icon: 3,
            btn: ['确定', '取消'] //按钮
        }, function () {
            verify(id, 2);
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
                verify(id, 3, val);
            });
        });
    }

    //发起审核请求
    verify = function (id, status, reason) {
        $.ajax({
            url: "/jiucuo/verify",
            type: "post",
            data: {
                "ids": id,
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
                    }, function(){
                        window.location.reload();
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

    //处理问题
    close = function (id) {
        layer.confirm("确认关闭问题任务？", {
            icon: 3,
            btn: ['确定', '取消'] //按钮
        }, function () {
            $.ajax({
                url: "/jiucuo/close",
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

    //创建监测任务
    createTask = function (id) {
//        layer.alert("暂未开放！谢谢！");
//        return;
        layer.confirm("确认创建监测任务？", {
            icon: 3,
            btn: ['确定', '取消'] //按钮
        }, function () {
            $.ajax({
                url: "/jiucuo/createTask",
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

    showTask = function(id){
        layer.open({
            type: 1,
            title: false,
            closeBtn: 1,
            shadeClose: true,
            skin: 'data-report',
            area:['50%','50%'],
            content: ''
        });
    }
</script>
<!-- 特色内容 -->

<@model.webend />
