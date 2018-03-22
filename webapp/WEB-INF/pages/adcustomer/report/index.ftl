<#assign webTitle="首页" in model>
<#assign webHead in model>
</#assign>
<@model.webhead />
<!-- 头部 -->
<@model.webMenu current="数据报表" child="数据报表" />


<div class="main-container">
    <div class="main-box">
        <div class="title clearfix" style="display: block;">
            <div class="search-box search-ll" style="margin: 0 0 0 20px">
                <form id="form" method="get" action="/adseat/list">
                    <!--所有活动下拉框-->
                    <div>
                        <div style="float:left;height:30px;line-height:30px;" class="mr-10">
                            活动名称:
                        </div>
                        <div class="select-box select-box-140 un-inp-select ll">
                            <select name="activityId" class="select" id="activityId">
                                <option value="">所有活动</option>
                            <@model.showAllActivityOps value=""/>
                            </select>
                        </div>

                        <button type="button" class="btn btn-red"
                                style="margin-left: 10px;" autocomplete="off" id="searchBtn">筛选
                        </button>
                        <button type="button" class="btn btn-primary"
                                style="margin-left: 10px;" autocomplete="off" id="clear">清除条件
                        </button>
                    </div>

                </form>
            </div>
        </div>

        <div id="bar" style="width:100%;height:400px;margin-top:10px;">
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
<!--图表地图-->
<script type="text/javascript" src="${model.static_domain}/js/echarts.js"></script>
<script type="text/javascript" src="${model.static_domain}/js/walden.js"></script>

<script type="text/javascript" src="/static/js/jquery.citys.js"></script>

<script type="text/javascript">

    $(function () {

        $('.select').searchableSelect();
        $("#searchBtn").click(function () {
            loadBarData();
        });
        $("#clear").click(function(){
            $("#activityId").val("");});

        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('bar'));

        // 指定图表的配置项和数据
        var option = {
            //color:["#5d83c6","#f80e1c","#fdbd2c","#1aaf55"],
            title: {
                show: false,
                text: '工作量汇总',
                left: 'center'
            },
            grid:{
                top:30,bottom:60
            },
            tooltip: {},
            legend: {
                data: ['监测', '报错', 'PV'],
                bottom: "10px",
                axisLabel: {
                    interval: 0
                }
            },
            xAxis: {
                type:'category',
                max:'dataMax',
                data: ["2018/1/1","2018/1/2","2018/1/3","2018/1/4","2018/1/5","2018/1/6","2018/1/7","2018/1/8","2018/1/9","2018/1/10","2018/1/11","2018/1/12","2018/1/13","2018/1/14","2018/1/15","2018/1/16","2018/1/17","2018/1/18","2018/1/19","2018/1/20","2018/1/21","2018/1/22","2018/1/23","2018/1/24","2018/1/25","2018/1/26","2018/1/27","2018/1/28","2018/1/29","2018/1/30"]
            },
            yAxis: {},
            series: [{
                name: '监测',
                type: 'line',
                data: [500 ,300 ,200 ,350 ,400 ,400 ,330 ,500 ,300 ,200 ,350 ,400 ,400 ,330 ,500 ,300 ,200 ,350 ,400 ,400 ,330 ,500 ,300 ,200 ,350 ,400 ,400 ,330 ,450 ,220]
            }, {
                name: '报错',
                type: 'line',
                data: [60 ,40 ,70 ,80 ,80 ,66 ,90 ,44 ,100 ,60 ,40 ,70 ,80 ,80 ,66 ,100 ,60 ,70 ,80 ,80 ,66 ,100 ,40 ,70 ,80 ,80 ,66 ,100 ,60 ,40]
            }, {
                name: 'PV',
                type: 'line',
                data: [7500 ,4500 ,3000 ,5250 ,6000 ,6000 ,4950 ,7500 ,4500 ,3000 ,5250 ,6000 ,6000 ,4950 ,7500 ,4500 ,3000 ,5250 ,6000 ,6000 ,4950 ,7500 ,4500 ,3000 ,5250 ,6000 ,6000 ,4950 ,6750 ,3300]
            }]
        };

        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);


        var loadBarData = function () {
            $.ajax({
                url: "/index/report",
                type: "post",
                data: {
                    activityId:$("#activityId").val(),
                    mediaId:$("#mediaId").val(),
                    province:$("#province").val(),
                    city:$("#city").val(),
                    region:$("#region").val(),
                    street:$("#street").val()
                },
                cache: false,
                dataType: "json",
                success: function (datas) {
                    var resultRet = datas.ret;
                    if (resultRet.code == 100) {
                        console.log(resultRet.result);
                        option.series[0].data = [resultRet.result.yesterday.taskCount, resultRet.result.today.taskCount, resultRet.result.tomorrow.taskCount, resultRet.result.leftWeek.taskCount]
                        option.series[1].data = [resultRet.result.yesterday.problemCount, resultRet.result.today.problemCount, resultRet.result.tomorrow.problemCount, resultRet.result.leftWeek.problemCount]
                        option.series[2].data = [resultRet.result.yesterday.completeCount, resultRet.result.today.completeCount, resultRet.result.tomorrow.completeCount, resultRet.result.leftWeek.completeCount]
                    }
                    myChart.setOption(option);
                },
                error: function (e) {
                    layer.confirm("服务忙，请稍后再试", {
                        icon: 5,
                        btn: ['确定'] //按钮
                    });
                }
            });
        }

        //loadBarData();
    })
</script>

<@model.webend/>
		