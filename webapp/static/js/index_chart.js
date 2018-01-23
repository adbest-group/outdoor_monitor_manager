	// 路径配置
	require.config({
		paths: {
			echarts: '/static/js'
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
			var myChart = ec.init(document.getElementById('line_charts'), 'macarons');
			
			var option = {
				tooltip : {
                    show: true,
                    trigger: 'axis',
                    axisPointer: {
						lineStyle: {
							color: '#ccc',
							width: '1'
						},
					},
                },
				legend: {
					y: "20px" ,
					selectedMode: 'single',
					selected: {
						'点击次数': false,
						'点击率': false,
						'平均点击单价': false,
						'消费': false
					},
					data:['展示次数','点击次数','点击率','平均点击单价','消费']
				},
                dataZoom : {
                    show : false,
                    start : 0,
                    end : 100
                },
				xAxis : [
					{
						type : 'category',
						data : belongDateArr,
						name: dayOrHour,
                        boundaryGap : false,
						axisLine:{
							lineStyle: {
								color: '#ccc',
								width: '1'
							},
						}
					}
				],
				yAxis : [
					{
						type : 'value',
						name:"数值",
						axisLine:{
							lineStyle: {
								color: '#ccc',
								width: '1'
							},
						}
					}
				],
				series: [
				    {
				    	name:"展示次数",
				    	type:"line",
				    	smooth:true,
				    	itemStyle: {
				    		normal: {
				    			areaStyle: {type: 'macarons'}
							}
						},
						data: viewNumArr
					},{
						name:"点击次数",
						type:"line",
						smooth	:true,
						itemStyle: {
							normal: {
								areaStyle: {type: 'macarons'}
							}
						},
						data: clickNumArr
					},{
						name:"点击率",
						type:"line",
						smooth:true,
						itemStyle: {
							normal: {
								areaStyle: {type: 'macarons'}
							}
						},
						data:clickRateArr
					},{
						name:"平均点击单价",
						type:"line",
						smooth:true,
						itemStyle: {
							normal: {
								areaStyle: {type: 'macarons'}
							}
						},
						data:clickPriceArr
					},{
						name:"消费",
						type:"line",
						smooth	:true,
						itemStyle: {
							normal: {
								areaStyle: {type: 'macarons'}
							}
						},
						data:consumeArr
					}
				]
			};
			myChart.setOption(option);
			window.onresize = myChart.resize;
		}
	);