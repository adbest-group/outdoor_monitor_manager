
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
                        <div style="float: left; font-size: 12px;">
                            媒体: <select style="height: 30px" name="mediaId" id="mediaId">
                            <option value="">所有媒体</option> <@model.showAllMediaOps value="" />
                        </select>
                        </div>

                        <button type="button" class="btn btn-red"
                                style="margin-left: 10px;" autocomplete="off" id="searchBtn">筛选
                        </button>
                        <button type="button" class="btn btn-primary"
                                style="margin-left: 10px;" autocomplete="off" id="clear">清除条件
                        </button>
                    </div>
                    <div style="clear:both;padding-top:10px;">
                        <div id="demo3" class="citys" style="float: left; font-size: 12px">
                            <p>
                                投放地区： <select style="height: 30px" id="province"
                                              name="province">
                                <option value=""></option>
                            </select> <select style="height: 30px" id="city" name="city"></select>
                                <select style="height: 30px" id="region" name="region"></select>
                                <select style="height: 30px" id="street" name="street"></select>
                            </p>
                        </div>
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
            $("#activityId,#mediaId,#province,#city,#region,#street").val("");
            $("#city,#region,#street").empty().hide();
        });

        /*获取城市  */
        var $town = $('#demo3 select[name="street"]');
        var townFormat = function (info) {
            $town.hide().empty();
            if (info['code'] % 1e4 && info['code'] < 7e5) { //是否为“区”且不是港澳台地区
                $.ajax({
                    url: '/api/city?provinceId=' + info['code'],
                    dataType: 'json',
                    success: function (town) {
                        $town.show();
                        $town.append('<option value> - 请选择 - </option>');
                        for (i in town) {
                            $town.append('<option value="' + i + '" <#if (street?exists&&street?length>0)>'+ (i ==${street!0} ? "selected" : "")+'</#if>>' + town[i]
                                    + '</option>');
                        }
                    }
                });
            }
        };
        $('#demo3').citys({
            required: false,
            province: '${province!"所有城市"}',
            city: '${city!""}',
            region: '${region!""}',
            onChange: function (info) {
                townFormat(info);
            }
        }, function (api) {
            var info = api.getInfo();
            townFormat(info);
        });

        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('bar'));

        // 指定图表的配置项和数据
        var option = {
            //color:["#5d83c6","#f80e1c","#fdbd2c","#1aaf55"],
            title: {
                text: '工作量汇总',
                left: 'center'
            },
            tooltip: {},
            legend: {
                data: ['监测中', '报错', '已完成'],
                bottom: "10px"
            },
            xAxis: {
                data: ["昨天", "今天", "明天", "本周剩余"]
            },
            yAxis: {},
            series: [{
                name: '监测中',
                type: 'bar',
                data: [5, 20, 18, 20]
            }, {
                name: '报错',
                type: 'bar',
                data: [5, 20, 2, 18]
            }, {
                name: '已完成',
                type: 'bar',
                data: [5, 20, 1, 1]
            }]
        };

        // 使用刚指定的配置项和数据显示图表。
//        myChart.setOption(option);


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

        loadBarData();
    })
</script>
		