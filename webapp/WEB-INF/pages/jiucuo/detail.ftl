<#assign webTitle="纠错管理" in model>
<#assign webHead in model>
</#assign>
<@model.webhead />
    <!-- 头部 -->
    <@model.webMenu current="纠错管理" child="纠错管理" />

	<!-- 特色内容 -->
<style>

</style>
<div class="main-container">
    <div class="clearfix">
        <div class="main-box">
            <div class="crumb-nav">
                <a href="/jiucuo/list">纠错管理</a>　>　纠错详情
            </div>
            <div class="bd new-active">
                <div class="hd mt-10"><h3>广告位相关信息</h3></div>
                <div class="bd">
                    <div class="bor-box detail-demand">
                        <div class="con">
                            <p>广告活动名称：${activity.activityName}</p>
                            <p>投放时间：${activity.startTime?string('yyyy-MM-dd')} 至 ${activity.endTime?string('yyyy-MM-dd')}</p>
                            <p>投放地区：${task.province} ${task.city} ${task.region} ${task.street}</p>
                            <p>媒体名称：${task.mediaName}</p>
                            <p>投放广告位：${task.adSeatName}</p>
                            <p>投放品牌：${seat.brand}</p>
                            <p>监测时间段：${seat.monitorStart?string('yyyy-MM-dd')} 至 ${seat.monitorEnd?string('yyyy-MM-dd')}</p>
                            <p>监测次数：${seat.monitorCount}</p>
                            <p>创建时间：${seat.createTime?string('yyyy-MM-dd')}</p>
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
                            <p>问题反馈：<span style="color:orangered;">${feedback.problem!""} ${feedback.problemOther!""}</p>
                            <p>执行状态：${vm.getJiucuoTaskStatusText(task.status)}</p>
                            <p>提交照片：<img style="vertical-align: top" src="${feedback.picUrl1}" width="300"/></p>
                            <#if task.status ==1>
                            <br/>
                            <p style="text-align:center;">
                                <button type="button" class="btn btn-red" style="margin-left:10px;" id="btnPass">通过</button>
                                <button type="button" class="btn btn-red" style="margin-left:10px;" id="btnReject">拒绝</button>
                            </p>
                            </#if>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</div>
</div>
</div>

<script type="text/javascript" src="${model.static_domain}/js/jquery-2.1.4.min.js"></script>

<script type="text/javascript">
    $(function(){
        $(".nav-sidebar>ul>li").on("click",function(){
            $(".nav-sidebar>ul>li").removeClass("on");
            $(this).addClass("on");
        });
    });

    $(function(){
        $(window).resize();

        $("#btnPass").click(function(){
            pass(${task.id});
        });
        $("#btnReject").click(function(){
            reject(${task.id});
        });
    });

    $(window).resize(function() {
        var h = $(document.body).height() - 115;
        $('.main-container').css('height', h);
    });


</script>

<script type="text/javascript">
    $("#date_val").click(function(){
        if($(".dsp-select").hasClass("hover")){
            $(".dsp-select").removeClass("hover")
            $(".time-conditions").hide();
        }else{
            $(".dsp-select").addClass("hover")
            $(".time-conditions").show();
        }
    });


    //审核通过
    pass = function(id){
        layer.confirm("确认审核通过？", {
            icon: 3,
            btn: ['确定', '取消'] //按钮
        }, function(){
            verify(id,2);
        });
    }

    //审核不通过
    reject = function(id){
        layer.confirm("确认审核不通过？", {
            icon: 3,
            btn: ['确定', '取消'] //按钮
        }, function(){
            layer.prompt({title:'请填写审核意见',formType:2},function (val,index) {
//                        if(val.length>33){
//                            layer.
//                        }
                verify(id,3，val);
            });
        });
    }

    //发起审核请求
    verify = function (id,status) {
        $.ajax({
            url: "/jiucuo/verify",
            type: "post",
            data: {
                "id": id,
                "status":status
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
                    layer.confirm("审核成功", {
                        icon: 1,
                        btn: ['确定'] //按钮
                    }, function(){
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