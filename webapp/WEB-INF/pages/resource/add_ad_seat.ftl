<#assign webTitle="监测管理-任务详情" in model> <#assign webHead in model>
</#assign> <@model.webhead />
<!-- 头部 -->
<@model.webMenu current="监测管理" child="监测管理" />
<link rel="stylesheet" type="text/css"
	href="http://ottstatic2.taiyiplus.com/css/new_main.css">
<link rel="stylesheet" type="text/css"
	href="http://ottstatic2.taiyiplus.com/css/icon_fonts.css">
<style type="text/css">
input {
	margin: 5px;
}

select {
	margin: 5px;
}

tr {
	text-align: right;
}
</style>
<!-- 特色内容 -->
<div class="main-container">
	<div class="clearfix">
		<div class="main-box">
			<div class="crumb-nav">
				<a href="/task/list">监测管理</a> > 任务指派
			</div>
			<div class="bd new-active">
				<div class="hd mt-10">
					<h3>新增投放广告位</h3>
				</div>

				<form action="">
					媒体名称：<input type="text"></br>
					<div id="demo3" class="citys">
						<p>
							投放地区： <select name="province" style="width: 147px"></select> <select
								name="city"></select> <select name="area"></select> <select
								name="town"></select>
						</p>
					</div>
					广告位位置：<input type="text"></br> 广告位类型：<input type="text"></br>
					广告位编号：<input type="text"></br> 广告位尺寸：<input type="text"></br>
					日均PV：<input type="text"></br> 私家车日均PV：<input type="text"></br>
					公交车日均PV：<input type="text"></br>
					
					<table>
						<tbody>
							<tr>
								<td> 人群选择：</td>
								<td><input type="checkbox" id="male" onclick="checkboxOnclick(this)">男</td>
							</tr>
							<tr> 
								<td><18：</td>
								<td><input type="range" id="male18" class="malerange" value="0" disabled="disabled" oninput="myFunction(this)"></td>
								<td><p class="rangevalue">0%</p></td>
							</tr>
							<tr> 
								<td>18-24：</td>
								<td><input class="malerange" disabled="disabled" value="0" type="range"></td>
								<td><p class="rangevalue">0%</p></td>
							</tr>
							<tr> 
								<td>25-29：</td>
								<td><input class="malerange" disabled="disabled" value="0" type="range"></td>
								<td><p>0%</p></td>
							</tr>
							<tr> 
								<td>30-39：</td>
								<td><input class="malerange" disabled="disabled" value="0" type="range"></td>
								<td><p>0%</p></td>
							</tr>
							<tr> 
								<td>40-49：</td>
								<td><input class="malerange" disabled="disabled" value="0" type="range"></td>
								<td><p>0%</p></td>
							</tr>
							<tr> 
								<td>50-59：</td>
								<td><input class="malerange" disabled="disabled" value="0" type="range"></td>
								<td><p>0%</p></td>
							</tr>
							<tr> 
								<td>>60：</td>
								<td><input class="malerange" disabled="disabled" value="0" type="range"></td>
								<td><p>0%</p></td>
							</tr>
							
							<tr>
								<td> 人群选择：</td>
								<td><input type="checkbox" id="female" onclick="checkboxOnclick(this)">女</td>
							</tr>
							<tr> 
								<td><18：</td>
								<td><input type="range" class="femalerange" value="0" disabled="disabled"></td>
								<td><p>0%</p></td>
							</tr>
							<tr> 
								<td>18-24：</td>
								<td><input class="femalerange" disabled="disabled" value="0" type="range"></td>
								<td><p>0%</p></td>
							</tr>
							<tr> 
								<td>25-29：</td>
								<td><input class="femalerange" disabled="disabled" value="0" type="range"></td>
								<td><p>0%</p></td>
							</tr>
							<tr> 
								<td>30-39：</td>
								<td><input class="femalerange" disabled="disabled" value="0" type="range"></td>
								<td><p>0%</p></td>
							</tr>
							<tr> 
								<td>40-49：</td>
								<td><input class="femalerange" disabled="disabled" value="0" type="range"></td>
								<td><p>0%</p></td>
							</tr>
							<tr> 
								<td>50-59：</td>
								<td><input class="femalerange" disabled="disabled" value="0" type="range"></td>
								<td><p>0%</p></td>
							</tr>
							<tr> 
								<td>>60：</td>
								<td><input class="femalerange" disabled="disabled" value="0" type="range"></td>
								<td><p>0%</p></td>
							</tr>
							<tr>
								<td><input type="submit" value="保存"></td>
								<td><input type="button" value="取消"></td>
							</tr>
						</tbody>
					</table>
				</form>
			</div>
		</div>
	</div>
</div>
</div>

<script type="text/javascript"
	src="http://ottstatic2.taiyiplus.com/js/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="../static/js/jquery.citys.js"></script>
<script type="text/javascript">
	/*复选框选中时拖动条可以拖动  */
	function checkboxOnclick(checkbox) {
		if($(checkbox).attr('id')=="male"){
			if ($(checkbox).is(':checked')) {
				$(".malerange").removeAttr("disabled");
			} else {
				$(".malerange").attr("disabled", "disabled");
			}
		}else if($(checkbox).attr('id')=="female"){
			if ($(checkbox).is(':checked')) {
				$(".femalerange").removeAttr("disabled");
			} else {
				$(".femalerange").attr("disabled", "disabled");
			}
		}
	}

	function myFunction(range) {
		var x = $(range).val();
		$(range+"+td+p").html(x + "%");
	}

	/*获取城市  */
	var $town = $('#demo3 select[name="town"]');
	var townFormat = function(info) {
		$town.hide().empty();
		if (info['code'] % 1e4 && info['code'] < 7e5) { //是否为“区”且不是港澳台地区
			$.ajax({
				url : 'http://passer-by.com/data_location/town/' + info['code']
						+ '.json',
				dataType : 'json',
				success : function(town) {
					$town.show();
					for (i in town) {
						$town.append('<option value="'+i+'">' + town[i]
								+ '</option>');
					}
				}
			});
		}
	};
	$('#demo3').citys({
		province : '福建',
		city : '厦门',
		area : '思明',
		onChange : function(info) {
			townFormat(info);
		}
	}, function(api) {
		var info = api.getInfo();
		townFormat(info);
	});

	$(function() {
		$(window).resize(function() {
			var h = $(document.body).height() - 115;
			$('.main-container').css('height', h);
		});
		$(window).resize();

	});
</script>
<@model.webend />
