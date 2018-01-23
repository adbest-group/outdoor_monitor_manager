var timeTicket;
// 路径配置
require.config({
	paths : {
		echarts : '/static/js'
	}
});

// 使用
require(
    [
        'echarts',
        'echarts/chart/line'
    ],
    function (ec) {
        // 基于准备好的dom，初始化echarts图表
        var myChart = ec.init(document.getElementById('chart_line'), 'macarons');
        $.post(
        	'/monitor/getLineData',
        	function(data){
				var displayNum = [];
				var clickNum = [];
				
				if (data.ret.code == 100) {
					var list = data.ret.result;
					if (typeof(list) == "undefiedn" || list == null) {
						for(var i=0; i<30; i++){
							displayNum.push(0);
							clickNum.push(0);
						}
					} else {
						var size = list.length;
						for(var i =0; i<size; i++){
							displayNum.push(list[i].displayNum);
							clickNum.push(list[i].clickNum);
						}
					}
				} else {
					for(var i=0; i<30; i++){
						displayNum.push(0);
						clickNum.push(0);
					}
				}
				
                myChart.setOption({
                	tooltip : {
                        show: true,
                        trigger: 'axis',
                        axisPointer : {
    						lineStyle: {
    							color: '#ccc',
    							width: '1'
    						},
    					},
                    },
                    legend: {
                    	selectedMode: 'single',
                    	selected: {
							'点击次数' : false
                    	},
                    	data:['展示次数', '点击次数']
                    },
                    dataZoom : {
                        show : false,
                        start : 0,
                        end : 100
                    },
                    xAxis : [
                        {
                        	type : 'category',
    						name: '时间',
                            axisLine:{
    							lineStyle: {
    								color: '#ccc',
    								width: '1'
    							},
    						},
                            boundaryGap : false,
                            data : (function (){
                                var now = new Date();
                                var res = [];
                                var len = 30;
                                while (len--) {
                                	var hours = now.getHours();
                                	var minutes = now.getMinutes();
                                	var seconds = now.getSeconds();
                                	var time = (hours < 10 ? "0" + hours : "" + hours);
                                	time = time + ":" + (minutes < 10 ? "0" + minutes : "" + minutes);
                                	time = time + ":" + (seconds < 10 ? "0" + seconds : "" + seconds);
                                    res.unshift(time);
                                    now = new Date(now - 30000);
                                }
                                return res; 
                            })()
                        }
                    ],
                    yAxis : [
                         {
                             name : '次',
                             type : 'value',
                             splitArea: { show: true },
                             axisLine:{
    							lineStyle: {
    								color: '#ccc',
    								width: '1'
    							},
                             }
                         }
                     ],
                     series : [
                           {
                        	   "name":"展示次数",
	                          	 "type":"line",
	                          	 "smooth"	:true,
	                          	 "itemStyle"	: {
	       							"normal": {
	       								"areaStyle": {"type": 'macarons'}
	       							}
	       						},
                               data:(function (){
                                   return displayNum;
                               })()
                           },
                           {
                        	   "name":"点击次数",
        						"type":"line",
        						"smooth"	:true,
        						"itemStyle"	: {
        							"normal": {
        								"areaStyle": {"type": 'macarons'}
        							}
        						},
                               data:(function (){
                                   return clickNum;
                               })()
                           }
                       ]
                   });
                
                   var lastData = 60;
                   var axisData;
                   clearInterval(timeTicket);
                   timeTicket = setInterval(function (){
                   	$.post(
                    	'/monitor/getLineDataInterval',
                		function(data){
	                        lastData += Math.random() * ((Math.round(Math.random() * 60) % 2) == 0 ? 1 : -1);
	                        lastData = lastData.toFixed(1) - 0;
	                        var now = new Date();
                        	var hours = now.getHours();
                        	var minutes = now.getMinutes();
                        	var seconds = now.getSeconds();
                        	var time = (hours < 10 ? "0" + hours : "" + hours);
                        	time = time + ":" + (minutes < 10 ? "0" + minutes : "" + minutes);
                        	time = time + ":" + (seconds < 10 ? "0" + seconds : "" + seconds);
                        	
                        	var displayNum = 0;
                        	var clickNum = 0;
                        	if (data.ret.code == 100) {
                        		displayNum = data.ret.result.displayNum;
                        		clickNum = data.ret.result.clickNum;
                        	}
                        	
	                        axisData = time;
	                        
	                        // 动态数据接口 addData
	                        myChart.addData([
	                            [
	                                0,        // 系列索引
	                                displayNum, // 新增数据
	                                false,     // 新增数据是否从队列头部插入
	                                false     // 是否增加队列长度，false则自定删除原有数据，队头插入删队尾，队尾插入删队头
	                            ],
	                            [
	                                1,        // 系列索引
	                                clickNum, // 新增数据
	                                false,    // 新增数据是否从队列头部插入
	                                false,    // 是否增加队列长度，false则自定删除原有数据，队头插入删队尾，队尾插入删队头
	                                axisData  // 坐标轴标签
	                            ]
	                        ]);
                		}
                	);
                }, 30000);
        	}
        );
        window.onresize = myChart.resize;
    }
);