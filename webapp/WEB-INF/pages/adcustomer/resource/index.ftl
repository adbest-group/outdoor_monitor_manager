<#assign webTitle="首页" in model>
<#assign webHead in model>
</#assign>
<@model.webhead />
<!-- 头部 -->
<@model.webMenu current="资源管理" child="资源管理" />
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
                            <@model.showOwnActivityOps value=""/>
                            </select>
                        </div>
                        <div style="float: left; font-size: 12px;">
                            媒体: <select style="height: 30px" name="mediaId" id="mediaId">
                            <option value="">所有媒体</option> <@model.showAllAvailableMediaOps value="" />
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

        <div style="width:100%;height:100%;margin-top:10px; background:'#555'">
            

            <div id="map" style="float:left;width:90%;height:700px;margin-top:10px;margin-left: 1%;-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;border:1px solid #aaa;">

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
<!--图表地图-->
<script type="text/javascript" src="${model.static_domain}/js/echarts.js"></script>
<script src="/static/js/china.js"></script>
<script type="text/javascript" src="${model.static_domain}/js/walden.js"></script>

<script type="text/javascript" src="/static/js/jquery.citys.js"></script>
<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=T8nSZc6XXTiu1vm5pCwdYu1D5AIb2F1w"></script>

<script type="text/javascript">
    $(function () {
        $(window).resize(function () {
            var h = $(document.body).height() - 115;
            $('.main-container').css('height', h);
        });
        $(window).resize();

        $('.select').searchableSelect();
        $("#searchBtn").click(function () {
            loadBarData();
        });
        $("#clear").click(function () {
            $("#activityId,#mediaId,#province,#city,#region,#street").val("");
            $("#city,#region,#street").empty().hide();
        });

        /*获取城市  */
        var $town = $('#demo3 select[name="street"]');
        var townFormat = function (info) {
            $town.hide().empty();
            if (info['code'] % 1e4 && info['code'] < 7e5) { //是否为“区”且不是港澳台地区
                $.ajax({
                    url: 'http://passer-by.com/data_location/town/' + info['code']
                    + '.json',
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

		// 百度地图API功能
        var map = new BMap.Map("map");    // 创建Map实例
        map.centerAndZoom(new BMap.Point(116.413624, 39.910837), 5);  // 初始化地图,设置中心点坐标和地图级别
        //添加地图类型控件
        map.addControl(new BMap.MapTypeControl({
            mapTypes:[
                BMAP_NORMAL_MAP
            ]}));
        map.setCurrentCity("杭州市");        //设置地图显示的城市 此项是必须设置的
        map.enableScrollWheelZoom(true);     //开启鼠标滚轮缩放

		//添加广告位坐标点
        map.addOverlay(new BMap.Marker(new BMap.Point(116.413624, 39.910837),{}));

        var loadBarData = function () {
            $.ajax({
                url: "/adseat/getAllLonLat",
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

    })
</script>
<@model.webend/>
		