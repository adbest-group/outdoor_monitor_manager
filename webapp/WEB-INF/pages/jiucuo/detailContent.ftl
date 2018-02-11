<div class="main-container">
    <div class="clearfix">
        <div class="main-box">
        <#--<div class="crumb-nav">-->
        <#--<a href="/jiucuo/list">纠错管理</a>　>　纠错详情-->
        <#--</div>-->
            <div class="bd new-active">
                <div class="hd mt-10"><h3>广告位相关信息</h3></div>
                <div class="bd">
                    <div class="bor-box detail-demand">
                        <div class="con">
                            <p>广告活动名称：${activity.activityName}</p>
                            <p>投放时间：${activity.startTime?string('yyyy-MM-dd')}
                                至 ${activity.endTime?string('yyyy-MM-dd')}</p>
                            <p>投放地区：${task.province} ${task.city} ${task.region} ${task.street}</p>
                            <p>媒体名称：${task.mediaName}</p>
                            <p>投放广告位：${task.adSeatName}</p>
                            <p>投放品牌：${seat.brand}</p>
                            <p>监测时间段：${seat.monitorStart?string('yyyy-MM-dd')}
                                至 ${seat.monitorEnd?string('yyyy-MM-dd')}</p>
                            <p>监测次数：${seat.monitorCount}</p>
                            <p>创建时间：${seat.createTime?string('yyyy-MM-dd HH:mm:ss')}</p>
                            <p>广告样例：<img style="vertical-align: top" src="${seat.samplePicUrl}" width="300"/></p>
                        </div>
                    </div>
                </div>

                <div class="hd mt-10"><h3>纠错信息</h3></div>
                <div class="bd">
                    <div class="bor-box detail-demand">
                        <div class="con">
                            <p>广告活动名称：${activity.activityName}</p>
                            <p>提交时间：${task.submitTime?string('yyyy-MM-dd HH:mm:dd')}</p>
                            <p>提交人：${task.realname}</p>
                            <p>问题反馈：<span style="color:orangered;">${feedback.problem!""} ${feedback.problemOther!""}
                            </p>
                            <p>执行状态：${vm.getJiucuoTaskStatusText(task.status)} <#if task.reason?exists>（${task.reason}
                                ）</#if></p>
                            <p>提交照片：<img style="vertical-align: top" src="${feedback.picUrl1}" width="300"/></p>
                        <@shiro.hasRole name="admin">
                            <#if task.status ==1>
                                <br/>
                                <p style="text-align:center;">
                                    <button type="button" class="btn btn-red" style="margin-left:10px;" id="btnPass">通过
                                    </button>
                                    <button type="button" class="btn btn-red" style="margin-left:10px;" id="btnReject">
                                        拒绝
                                    </button>
                                </p>
                            </#if>
                        </@shiro.hasRole>
                        </div>
                    </div>
                </div>

            <#-- 子任务提交的监测信息 -->
            <#if (subs?exists && subs?size>0) >
                <#list subs as sub >
                    <div class="hd mt-10"><h3>复查任务提交信息</h3></div>
                    <div class="bd">
                        <div class="bor-box detail-demand">
                            <div class="con">
                                <p>提交时间：${sub.feedbackCreateTime?string('yyyy-MM-dd HH:mm:dd')}</p>
                                <p>提交人：${sub.realname!""}</p>
                                <#if (sub.problem?exists || sub.problemOther?exists) >
                                    <p>问题反馈：<span
                                            style="color:orangered;">${sub.problem!""} ${sub.problemOther!""}
                                    </p>
                                </#if>
                                <p>执行状态：${vm.getMonitorTaskStatusText(sub.status)} <#if sub.reason?exists>
                                    （${sub.reason}
                                    ）</#if>
                                    <@shiro.hasRole name="admin">（<a href="/task/list?pid=${task.id}&ptype=2">管理监测任务</a>）</@shiro.hasRole>
                                </p>

                                <p>提交照片：
                                    <br/>
                                    <#if sub.picUrl1?exists><img style="vertical-align: top" src="${sub.picUrl1}"
                                                                 width="300"/></#if>
                                    &nbsp;&nbsp;&nbsp;
                                    <#if sub.picUrl2?exists><img style="vertical-align: top" src="${sub.picUrl2}"
                                                                 width="300"/></#if>
                                    <br/><br/>
                                    <#if sub.picUrl3?exists><img style="vertical-align: top" src="${sub.picUrl3}"
                                                                 width="300"/></#if>
                                    &nbsp;&nbsp;&nbsp;
                                    <#if sub.picUrl4?exists><img style="vertical-align: top" src="${sub.picUrl4}"
                                                                 width="300"/></#if>
                                </p>
                            </div>
                        </div>
                    </div>
                </#list>
            <#else>
                <#if task.subCreated == 1>
                    <div class="hd mt-10"><h3>已创建复查监测子任务，目前还没有监测反馈 <@shiro.hasRole name="admin">&nbsp;&nbsp;&nbsp;<a
                            href="/task/list?pid=${task.id}&ptype=2">去查看任务</a></@shiro.hasRole></h3></div>

                </#if>
            </#if>
            </div>
        </div>
    </div>
</div>
</div>
</div>
</div>

<script type="text/javascript" src="${model.static_domain}/js/jquery-2.1.4.min.js"></script>

<script type="text/javascript">
    $(function () {
        $(".nav-sidebar>ul>li").on("click", function () {
            $(".nav-sidebar>ul>li").removeClass("on");
            $(this).addClass("on");
        });
    });

    $(function () {
        $(window).resize();

        $("#btnPass").click(function () {
            pass(${task.id});
        });
        $("#btnReject").click(function () {
            reject(${task.id});
        });
    });

    $(window).resize(function () {
        var h = $(document.body).height() - 115;
        $('.main-container').css('height', h);
    });


</script>

<script type="text/javascript">
    $("#date_val").click(function () {
        if ($(".dsp-select").hasClass("hover")) {
            $(".dsp-select").removeClass("hover")
            $(".time-conditions").hide();
        } else {
            $(".dsp-select").addClass("hover")
            $(".time-conditions").show();
        }
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

</script>
