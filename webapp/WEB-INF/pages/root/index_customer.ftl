
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

        <div id="chart" style="width:100%;height:600px;margin-top:10px;border:1px solid #ccc;">
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

        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('chart'));

        var data = [
            {name: '海门', value: 9},
            {name: '鄂尔多斯', value: 12},
            {name: '招远', value: 12},
            {name: '舟山', value: 12},
            {name: '齐齐哈尔', value: 14},
            {name: '盐城', value: 15},
            {name: '赤峰', value: 16},
            {name: '青岛', value: 18},
            {name: '乳山', value: 18},
            {name: '金昌', value: 19},
            {name: '泉州', value: 21},
            {name: '莱西', value: 21},
            {name: '日照', value: 21},
            {name: '胶南', value: 22},
            {name: '南通', value: 23},
            {name: '拉萨', value: 24},
            {name: '云浮', value: 24},
            {name: '梅州', value: 25},
            {name: '文登', value: 25},
            {name: '上海', value: 25},
            {name: '攀枝花', value: 25},
            {name: '威海', value: 25},
            {name: '承德', value: 25},
            {name: '厦门', value: 26},
            {name: '汕尾', value: 26},
            {name: '潮州', value: 26},
            {name: '丹东', value: 27},
            {name: '太仓', value: 27},
            {name: '曲靖', value: 27},
            {name: '烟台', value: 28},
            {name: '福州', value: 29},
            {name: '瓦房店', value: 30},
            {name: '即墨', value: 30},
            {name: '抚顺', value: 31},
            {name: '玉溪', value: 31},
            {name: '张家口', value: 31},
            {name: '阳泉', value: 31},
            {name: '莱州', value: 32},
            {name: '湖州', value: 32},
            {name: '汕头', value: 32},
            {name: '昆山', value: 33},
            {name: '宁波', value: 33},
            {name: '湛江', value: 33},
            {name: '揭阳', value: 34},
            {name: '荣成', value: 34},
            {name: '连云港', value: 35},
            {name: '葫芦岛', value: 35},
            {name: '常熟', value: 36},
            {name: '东莞', value: 36},
            {name: '河源', value: 36},
            {name: '淮安', value: 36},
            {name: '泰州', value: 36},
            {name: '南宁', value: 37},
            {name: '营口', value: 37},
            {name: '惠州', value: 37},
            {name: '江阴', value: 37},
            {name: '蓬莱', value: 37},
            {name: '韶关', value: 38},
            {name: '嘉峪关', value: 38},
            {name: '广州', value: 38},
            {name: '延安', value: 38},
            {name: '太原', value: 39},
            {name: '清远', value: 39},
            {name: '中山', value: 39},
            {name: '昆明', value: 39},
            {name: '寿光', value: 40},
            {name: '盘锦', value: 40},
            {name: '长治', value: 41},
            {name: '深圳', value: 41},
            {name: '珠海', value: 42},
            {name: '宿迁', value: 43},
            {name: '咸阳', value: 43},
            {name: '铜川', value: 44},
            {name: '平度', value: 44},
            {name: '佛山', value: 44},
            {name: '海口', value: 44},
            {name: '江门', value: 45},
            {name: '章丘', value: 45},
            {name: '肇庆', value: 46},
            {name: '大连', value: 47},
            {name: '临汾', value: 47},
            {name: '吴江', value: 47},
            {name: '石嘴山', value: 49},
            {name: '沈阳', value: 50},
            {name: '苏州', value: 50},
            {name: '茂名', value: 50},
            {name: '嘉兴', value: 51},
            {name: '长春', value: 51},
            {name: '胶州', value: 52},
            {name: '银川', value: 52},
            {name: '张家港', value: 52},
            {name: '三门峡', value: 53},
            {name: '锦州', value: 54},
            {name: '南昌', value: 54},
            {name: '柳州', value: 54},
            {name: '三亚', value: 54},
            {name: '自贡', value: 56},
            {name: '吉林', value: 56},
            {name: '阳江', value: 57},
            {name: '泸州', value: 57},
            {name: '西宁', value: 57},
            {name: '宜宾', value: 58},
            {name: '呼和浩特', value: 58},
            {name: '成都', value: 58},
            {name: '大同', value: 58},
            {name: '镇江', value: 59},
            {name: '桂林', value: 59},
            {name: '张家界', value: 59},
            {name: '宜兴', value: 59},
            {name: '北海', value: 60},
            {name: '西安', value: 61},
            {name: '金坛', value: 62},
            {name: '东营', value: 62},
            {name: '牡丹江', value: 63},
            {name: '遵义', value: 63},
            {name: '绍兴', value: 63},
            {name: '扬州', value: 64},
            {name: '常州', value: 64},
            {name: '潍坊', value: 65},
            {name: '重庆', value: 66},
            {name: '台州', value: 67},
            {name: '南京', value: 67},
            {name: '滨州', value: 70},
            {name: '贵阳', value: 71},
            {name: '无锡', value: 71},
            {name: '本溪', value: 71},
            {name: '克拉玛依', value: 72},
            {name: '渭南', value: 72},
            {name: '马鞍山', value: 72},
            {name: '宝鸡', value: 72},
            {name: '焦作', value: 75},
            {name: '句容', value: 75},
            {name: '北京', value: 79},
            {name: '徐州', value: 79},
            {name: '衡水', value: 80},
            {name: '包头', value: 80},
            {name: '绵阳', value: 80},
            {name: '乌鲁木齐', value: 84},
            {name: '枣庄', value: 84},
            {name: '杭州', value: 84},
            {name: '淄博', value: 85},
            {name: '鞍山', value: 86},
            {name: '溧阳', value: 86},
            {name: '库尔勒', value: 86},
            {name: '安阳', value: 90},
            {name: '开封', value: 90},
            {name: '济南', value: 92},
            {name: '德阳', value: 93},
            {name: '温州', value: 95},
            {name: '九江', value: 96},
            {name: '邯郸', value: 98},
            {name: '临安', value: 99},
            {name: '兰州', value: 99},
            {name: '沧州', value: 100},
            {name: '临沂', value: 103},
            {name: '南充', value: 104},
            {name: '天津', value: 105},
            {name: '富阳', value: 106},
            {name: '泰安', value: 112},
            {name: '诸暨', value: 112},
            {name: '郑州', value: 113},
            {name: '哈尔滨', value: 114},
            {name: '聊城', value: 116},
            {name: '芜湖', value: 117},
            {name: '唐山', value: 119},
            {name: '平顶山', value: 119},
            {name: '邢台', value: 119},
            {name: '德州', value: 120},
            {name: '济宁', value: 120},
            {name: '荆州', value: 127},
            {name: '宜昌', value: 130},
            {name: '义乌', value: 132},
            {name: '丽水', value: 133},
            {name: '洛阳', value: 134},
            {name: '秦皇岛', value: 136},
            {name: '株洲', value: 143},
            {name: '石家庄', value: 147},
            {name: '莱芜', value: 148},
            {name: '常德', value: 152},
            {name: '保定', value: 153},
            {name: '湘潭', value: 154},
            {name: '金华', value: 157},
            {name: '岳阳', value: 169},
            {name: '长沙', value: 175},
            {name: '衢州', value: 177},
            {name: '廊坊', value: 193},
            {name: '菏泽', value: 194},
            {name: '合肥', value: 229},
            {name: '武汉', value: 273},
            {name: '大庆', value: 279}
        ];
        var geoCoordMap = {
        	'北京市': [116.40, 39.90],
            '天津市': [117.20, 39.12],
            '石家庄市': [114.52, 38.05],
            '唐山市': [118.20, 39.63],
            '秦皇岛市': [119.60, 39.93],
            '邯郸市': [114.48, 36.62],
            '邢台市': [114.48, 37.07],
            '保定市': [115.47, 38.87],
            '张家口市': [114.88, 40.82],
            '承德市': [117.93, 40.97],
            '沧州市': [116.83, 38.30],
            '廊坊市': [116.70, 39.52],
            '衡水市': [115.68, 37.73],
            '太原市': [112.55, 37.87],
            '大同市': [113.30, 40.08],
            '阳泉市': [113.57, 37.85],
            '长治市': [113.12, 36.20],
            '晋城市': [112.83, 35.50],
            '朔州市': [112.43, 39.33],
            '晋中市': [112.75, 37.68],
            '运城市': [110.98, 35.02],
            '忻州市': [112.73, 38.42],
            '临汾市': [111.52, 36.08],
            '吕梁市': [111.13, 37.52],
            '呼和浩特市': [111.73, 40.83],
            '包头市': [109.83, 40.65],
            '乌海市': [106.82, 39.67],
            '赤峰市': [118.92, 42.27],
            '通辽市': [122.27, 43.62],
            '鄂尔多斯市': [109.80, 39.62],
            '呼伦贝尔市': [119.77, 49.22],
            '巴彦淖尔市': [107.42, 40.75],
            '乌兰察布市': [113.12, 40.98],
            '兴安盟': [122.05, 46.08],
            '锡林郭勒盟': [116.07, 43.95],
            '阿拉善盟': [105.67, 38.83],
            '沈阳市': [123.43, 41.80],
            '大连市': [121.62, 38.92],
            '鞍山市': [122.98, 41.10],
            '抚顺市': [123.98, 41.88],
            '本溪市': [123.77, 41.30],
            '丹东市': [124.38, 40.13],
            '锦州市': [121.13, 41.10],
            '营口市': [122.23, 40.67],
            '阜新市': [121.67, 42.02],
            '辽阳市': [123.17, 41.27],
            '盘锦市': [122.07, 41.12],
            '铁岭市': [123.83, 42.28],
            '朝阳市': [120.45, 41.57],
            '葫芦岛市': [120.83, 40.72],
            '长春市': [125.32, 43.90],
            '吉林市': [126.55, 43.83],
            '四平市': [124.35, 43.17],
            '辽源市': [125.13, 42.88],
            '通化市': [125.93, 41.73],
            '白山市': [126.42, 41.93],
            '松原市': [124.82, 45.13],
            '白城市': [122.83, 45.62],
            '延边朝鲜族自治州': [129.50, 42.88],
            '哈尔滨市': [126.53, 45.80],
            '齐齐哈尔市': [123.95, 47.33],
            '鸡西市': [130.97, 45.30],
            '鹤岗市': [130.27, 47.33],
            '双鸭山市': [131.15, 46.63],
            '大庆市': [125.03, 46.58],
            '伊春市': [128.90, 47.73],
            '佳木斯市': [130.37, 46.82],
            '七台河市': [130.95, 45.78],
            '牡丹江市': [129.60, 44.58],
            '黑河市': [127.48, 50.25],
            '绥化市': [126.98, 46.63],
            '大兴安岭地区': [124.12, 50.42],
            '上海市': [121.47, 31.23],
            '南京市': [118.78, 32.07],
            '无锡市': [120.30, 31.57],
            '徐州市': [117.18, 34.27],
            '常州市': [119.95, 31.78],
            '苏州市': [120.58, 31.30],
            '南通市': [120.88, 31.98],
            '连云港市': [119.22, 34.60],
            '淮安市': [119.02, 33.62],
            '盐城市': [120.15, 33.35],
            '扬州市': [119.40, 32.40],
            '镇江市': [119.45, 32.20],
            '泰州市': [119.92, 32.45],
            '宿迁市': [118.28, 33.97],
            '杭州市': [120.15, 30.28],
            '宁波市': [121.55, 29.88],
            '温州市': [120.70, 28.00],
            '嘉兴市': [120.75, 30.75],
            '湖州市': [120.08, 30.90],
            '绍兴市': [120.57, 30.00],
            '金华市': [119.65, 29.08],
            '衢州市': [118.87, 28.93],
            '舟山市': [122.20, 30.00],
            '台州市': [121.43, 28.68],
            '丽水市': [119.92, 28.45],
            '合肥市': [117.25, 31.83],
            '芜湖市': [118.38, 31.33],
            '蚌埠市': [117.38, 32.92],
            '淮南市': [117.00, 32.63],
            '马鞍山市': [118.50, 31.70],
            '淮北市': [116.80, 33.95],
            '铜陵市': [117.82, 30.93],
            '安庆市': [117.05, 30.53],
            '黄山市': [118.33, 29.72],
            '滁州市': [118.32, 32.30],
            '阜阳市': [115.82, 32.90],
            '宿州市': [116.98, 33.63],
            '巢湖市': [117.87, 31.60],
            '六安市': [116.50, 31.77],
            '亳州市': [115.78, 33.85],
            '池州市': [117.48, 30.67],
            '宣城市': [118.75, 30.95],
            '福州市': [119.30, 26.08],
            '厦门市': [118.08, 24.48],
            '莆田市': [119.00, 25.43],
            '三明市': [117.62, 26.27],
            '泉州市': [118.67, 24.88],
            '漳州市': [117.65, 24.52],
            '南平市': [118.17, 26.65],
            '龙岩市': [117.03, 25.10],
            '宁德市': [119.52, 26.67],
            '南昌市': [115.85, 28.68],
            '景德镇市': [117.17, 29.27],
            '萍乡市': [113.85, 27.63],
            '九江市': [116.00, 29.70],
            '新余市': [114.92, 27.82],
            '鹰潭市': [117.07, 28.27],
            '赣州市': [114.93, 25.83],
            '吉安市': [114.98, 27.12],
            '宜春市': [114.38, 27.80],
            '抚州市': [116.35, 28.00],
            '上饶市': [117.97, 28.45],
            '济南市': [116.98, 36.67],
            '青岛市': [120.38, 36.07],
            '淄博市': [118.05, 36.82],
            '枣庄市': [117.32, 34.82],
            '东营市': [118.67, 37.43],
            '烟台市': [121.43, 37.45],
            '潍坊市': [119.15, 36.70],
            '济宁市': [116.58, 35.42],
            '泰安市': [117.08, 36.20],
            '威海市': [122.12, 37.52],
            '日照市': [119.52, 35.42],
            '莱芜市': [117.67, 36.22],
            '临沂市': [118.35, 35.05],
            '德州市': [116.30, 37.45],
            '聊城市': [115.98, 36.45],
            '滨州市': [117.97, 37.38],
            '牡丹区': [115.43, 35.25],
            '郑州市': [113.62, 34.75],
            '开封市': [114.30, 34.80],
            '洛阳市': [112.45, 34.62],
            '平顶山市': [113.18, 33.77],
            '安阳市': [114.38, 36.10],
            '鹤壁市': [114.28, 35.75],
            '新乡市': [113.90, 35.30],
            '焦作市': [113.25, 35.22],
            '濮阳市': [115.03, 35.77],
            '许昌市': [113.85, 34.03],
            '漯河市': [114.02, 33.58],
            '三门峡市': [111.20, 34.78],
            '南阳市': [112.52, 33.00],
            '商丘市': [115.65, 34.45],
            '信阳市': [114.07, 32.13],
            '周口市': [114.65, 33.62],
            '驻马店市': [114.02, 32.98],
            '武汉市': [114.30, 30.60],
            '黄石市': [115.03, 30.20],
            '十堰市': [110.78, 32.65],
            '宜昌市': [111.28, 30.70],
            '襄樊市': [112.15, 32.02],
            '鄂州市': [114.88, 30.40],
            '荆门市': [112.20, 31.03],
            '孝感市': [113.92, 30.93],
            '荆州市': [112.23, 30.33],
            '黄冈市': [114.87, 30.45],
            '咸宁市': [114.32, 29.85],
            '随州市': [113.37, 31.72],
            '恩施土家族苗族自治州': [109.47, 30.30],
            '仙桃市': [113.45, 30.37],
            '长沙市': [112.93, 28.23],
            '株洲市': [113.13, 27.83],
            '湘潭市': [112.93, 27.83],
            '衡阳市': [112.57, 26.90],
            '邵阳市': [111.47, 27.25],
            '岳阳市': [113.12, 29.37],
            '常德市': [111.68, 29.05],
            '张家界市': [110.47, 29.13],
            '益阳市': [112.32, 28.60],
            '郴州市': [113.02, 25.78],
            '永州市': [111.62, 26.43],
            '怀化市': [110.00, 27.57],
            '娄底市': [112.00, 27.73],
            '湘西土家族苗族自治州': [109.73, 28.32],
            '广州市': [113.27, 23.13],
            '韶关市': [113.60, 24.82],
            '深圳市': [114.05, 22.55],
            '珠海市': [113.57, 22.27],
            '汕头市': [116.68, 23.35],
            '佛山市': [113.12, 23.02],
            '江门市': [113.08, 22.58],
            '湛江市': [110.35, 21.27],
            '茂名市': [110.92, 21.67],
            '肇庆市': [112.47, 23.05],
            '惠州市': [114.42, 23.12],
            '梅州市': [116.12, 24.28],
            '汕尾市': [115.37, 22.78],
            '河源市': [114.70, 23.73],
            '阳江市': [111.98, 21.87],
            '清远市': [113.03, 23.70],
            '东莞市': [113.75, 23.05],
            '中山市': [113.38, 22.52],
            '潮州市': [116.62, 23.67],
            '揭阳市': [116.37, 23.55],
            '云浮市': [112.03, 22.92],
            '南宁市': [108.37, 22.82],
            '柳州市': [109.42, 24.33],
            '桂林市': [110.28, 25.28],
            '梧州市': [111.27, 23.48],
            '北海市': [109.12, 21.48],
            '防城港市': [108.35, 21.70],
            '钦州市': [108.62, 21.95],
            '贵港市': [109.60, 23.10],
            '玉林市': [110.17, 22.63],
            '百色市': [106.62, 23.90],
            '贺州市': [111.55, 24.42],
            '河池市': [108.07, 24.70],
            '来宾市': [109.23, 23.73],
            '崇左市': [107.37, 22.40],
            '海口市': [110.32, 20.03],
            '三亚市': [109.50, 18.25],
            '五指山市': [109.52, 18.78],
            '重庆市': [106.55, 29.57],
            '成都市': [104.07, 30.67],
            '自贡市': [104.78, 29.35],
            '攀枝花市': [101.72, 26.58],
            '泸州市': [105.43, 28.87],
            '德阳市': [104.38, 31.13],
            '绵阳市': [104.73, 31.47],
            '广元市': [105.83, 32.43],
            '遂宁市': [105.57, 30.52],
            '内江市': [105.05, 29.58],
            '乐山市': [103.77, 29.57],
            '南充市': [106.08, 30.78],
            '眉山市': [103.83, 30.05],
            '宜宾市': [104.62, 28.77],
            '广安市': [106.63, 30.47],
            '达州市': [107.50, 31.22],
            '雅安市': [103.00, 29.98],
            '巴中市': [106.77, 31.85],
            '资阳市': [104.65, 30.12],
            '阿坝藏族羌族自治州': [102.22, 31.90],
            '甘孜藏族自治州': [101.97, 30.05],
            '凉山彝族自治州': [102.27, 27.90],
            '贵阳市': [106.63, 26.65],
            '六盘水市': [104.83, 26.60],
            '遵义市': [106.92, 27.73],
            '安顺市': [105.95, 26.25],
            '铜仁市': [109.18, 27.72],
            '兴义市': [104.90, 25.08],
            '毕节市': [105.28, 27.30],
            '黔东南苗族侗族自治州': [107.97, 26.58],
            '黔南布依族苗族自治州': [107.52, 26.27],
            '昆明市': [102.72, 25.05],
            '曲靖市': [103.80, 25.50],
            '玉溪市': [102.55, 24.35],
            '保山市': [99.17, 25.12],
            '昭通市': [103.72, 27.33],
            '丽江市': [100.23, 26.88],
            '墨江哈尼族自治县': [101.68, 23.43],
            '临沧市': [100.08, 23.88],
            '楚雄彝族自治州': [101.55, 25.03],
            '红河哈尼族彝族自治州': [103.40, 23.37],
            '文山壮族苗族自治州': [104.25, 23.37],
            '西双版纳傣族自治州': [100.80, 22.02],
            '大理白族自治州': [100.23, 25.60],
            '德宏傣族景颇族自治州': [98.58, 24.43],
            '怒江傈僳族自治州': [98.85, 25.85],
            '迪庆藏族自治州': [99.70, 27.83],
            '拉萨市': [91.13, 29.65],
            '昌都市': [97.18, 31.13],
            '山南市': [91.77, 29.23],
            '日喀则市': [88.88, 29.27],
            '那曲市': [92.07, 31.48],
            '阿里地区': [80.10, 32.50],
            '林芝市': [94.37, 29.68],
            '西安市': [108.93, 34.27],
            '铜川市': [108.93, 34.90],
            '宝鸡市': [107.13, 34.37],
            '咸阳市': [108.70, 34.33],
            '渭南市': [109.50, 34.50],
            '延安市': [109.48, 36.60],
            '汉中市': [107.02, 33.07],
            '榆林市': [109.73, 38.28],
            '安康市': [109.02, 32.68],
            '商洛市': [109.93, 33.87],
            '兰州市': [103.82, 36.07],
            '嘉峪关市': [98.27, 39.80],
            '金昌市': [102.18, 38.50],
            '白银市': [104.18, 36.55],
            '天水市': [105.72, 34.58],
            '武威市': [102.63, 37.93],
            '张掖市': [100.45, 38.93],
            '平凉市': [106.67, 35.55],
            '酒泉市': [98.52, 39.75],
            '庆阳市': [107.63, 35.73],
            '定西市': [104.62, 35.58],
            '陇南市': [104.92, 33.40],
            '临夏回族自治州': [103.22, 35.60],
            '甘南藏族自治州': [102.92, 34.98],
            '西宁市': [101.78, 36.62],
            '海东市': [102.12, 36.50],
            '海北藏族自治州': [100.90, 36.97],
            '黄南藏族自治州': [102.02, 35.52],
            '海南藏族自治州': [100.62, 36.28],
            '果洛藏族自治州': [100.23, 34.48],
            '玉树藏族自治州': [97.02, 33.00],
            '海西蒙古族藏族自治州': [97.37, 37.37],
            '银川市': [106.28, 38.47],
            '石嘴山市': [106.38, 39.02],
            '吴忠市': [106.20, 37.98],
            '固原市': [106.28, 36.00],
            '中卫市': [105.18, 37.52],
            '乌鲁木齐市': [87.62, 43.82],
            '克拉玛依市': [84.87, 45.60],
            '吐鲁番市': [89.17, 42.95],
            '哈密市': [93.52, 42.83],
            '昌吉回族自治州': [87.30, 44.02],
            '博尔塔拉蒙古自治州': [82.07, 44.90],
            '巴音郭楞蒙古自治州': [86.15, 41.77],
            '阿克苏地区': [80.27, 41.17],
            '阿图什市': [76.17, 39.72],
            '喀什地区': [75.98, 39.47],
            '和田地区': [79.92, 37.12],
            '伊犁哈萨克自治州': [81.32, 43.92],
            '塔城地区': [82.98, 46.75],
            '阿勒泰地区': [88.13, 47.85],
            '石河子市': [86.03, 44.30],
            '香港特别行政区': [114.08, 22.20],
            '澳门特别行政区': [113.33, 22.13],
            '台北市': [121.50, 25.03],
            '高雄市': [120.28, 22.62],
            '基隆市': [121.73, 25.13],
            '台中市': [120.67, 24.15],
            '台南市': [120.20, 23.00],
            '新竹市': [120.95, 24.82],
            '嘉义市': [120.43, 23.48]
        };

        var convertData = function (data) {
            var res = [];
            for (var i = 0; i < data.length; i++) {
                var geoCoord = geoCoordMap[data[i].name];
                if (geoCoord) {
                    res.push({
                        name: data[i].name,
                        value: geoCoord.concat(data[i].value)
                    });
                }
            }
            return res;
        };

        var option = {
            tooltip: {
                trigger: 'item'
            },
            grid:{
                left:20
            },
            legend: {
                orient: 'vertical',
                y: 'bottom',
                x: 'right',
                data: ['广告位'],
                textStyle: {
                    color: '#333'
                }
            },
            geo: {
                map: 'china',
                label: {
                    emphasis: {
                        show: false
                    }
                },
                roam: true,
                itemStyle: {
                    normal: {
                        areaColor: '#eee',
                        borderColor: '#111'
                    },
                    emphasis: {
                        areaColor: '#ccc'
                    }
                }
            },
            series: [
                {
                    name: '广告位',
                    type: 'scatter',
                    coordinateSystem: 'geo',
                    data: convertData(data),
                    symbolSize: function (val) {
                        return val[2] / 10;
                    },
                    label: {
                        normal: {
                            formatter: '{b}',
                            position: 'right',
                            show: false
                        },
                        emphasis: {
                            show: true
                        }
                    },
                    itemStyle: {
                        normal: {
                            color: '#1b87dd'
                        }
                    }
                },
                {
                    name: 'Top 5',
                    type: 'effectScatter',
                    coordinateSystem: 'geo',
                    data: convertData(data.sort(function (a, b) {
                        return b.value - a.value;
                    }).slice(0, 6)),
                    symbolSize: function (val) {
                        return val[2] / 10;
                    },
                    showEffectOn: 'render',
                    rippleEffect: {
                        brushType: 'stroke'
                    },
                    hoverAnimation: true,
                    label: {
                        normal: {
                            formatter: '{b}',
                            position: 'right',
                            show: true
                        }
                    },
                    itemStyle: {
                        normal: {
                            color: '#1b87dd',
                            shadowBlur: 10,
                            shadowColor: '#333'
                        }
                    },
                    zlevel: 1
                }
            ]
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

//        loadBarData();
        myChart.setOption(option);
    })
</script>
		