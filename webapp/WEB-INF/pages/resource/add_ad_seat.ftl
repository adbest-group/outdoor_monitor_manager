<#assign webTitle="资源管理" in model> <#assign webHead in model>
</#assign> <@model.webhead />
<!-- 头部 -->
<@model.webMenu current="资源管理" child="资源管理" />
<link rel="stylesheet" type="text/css"
	href="${model.static_domain}/css/new_main.css">
<link rel="stylesheet" type="text/css"
	href="${model.static_domain}/css/icon_fonts.css">
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

				<form action="#" id="form">
					广告位名称：<input name="adSeatInfo.name" id="adSeatInfo-name" type="text"></br> 
					媒体名称：<input name="adMedia.mediaName" id="adSeatInfo-mediaName" type="text"></br>
					<div id="demo3" class="citys">
						<p>
							投放地区： <select id="adSeatInfo-province" name="province" style="width: 147px"></select> 
							<select id="adSeatInfo-city" name="city"></select> 
							<select id="adSeatInfo-region" name="region"></select> 
							<select id="adSeatInfo-street" name="street"></select>
						</p>
					</div>
					广告位位置：<input id="adSeatInfo.location" name="adSeatInfo.location" type="text"></br> 
					广告位编号：<input id="adSeatInfo.adCode" name="adSeatInfo.adCode" type="text"></br> 
					广告位尺寸：<input id="adSeatInfo.adSize" name="adSeatInfo.adSize" type="text"></br>
					日均PV：<input id="adSeatInfo.pv" name="adSeatInfo.pv" type="text"></br>
					私家车日均PV：<input id="adSeatInfo.privateCarPv" name="adSeatInfo.privateCarPv" type="text"></br> 
					公交车日均PV：<input id="adSeatInfo.busPv" name="adSeatInfo.busPv" type="text"></br>
					广告位经度：<input id="adSeatInfo.lon" name="adSeatInfo.lon" type="text"></br>
					广告位纬度：<input id="adSeatInfo.lat" name="adSeatInfo.lat" type="text"></br>
					广告位类型：<input name="adSeatType.name" type="text"></br>
					<table>
						<tbody>
							<tr>
								<td>人群选择：</td>
								<td><input type="checkbox" name="adCrowdVo.male" value="1" id="male" onclick="checkboxOnclick(this)">男</td>
							</tr>
							<tr>
								<td><18：</td>
								<td><input type="range" name="adCrowdVo.num" class="malerange" value="0" disabled="disabled" oninput="myFunction(this)"></td>
								<td><p class="rangevalue">0%</p></td>
							</tr>
							<tr>
								<td>18-24：</td>
								<td><input class="malerange" disabled="disabled" value="0" type="range" name="adCrowdVo.num" oninput="myFunction(this)"></td>
								<td><p class="rangevalue">0%</p></td>
							</tr>
							<tr>
								<td>25-29：</td>
								<td><input class="malerange" disabled="disabled" value="0" name="adCrowdVo.num" type="range" oninput="myFunction(this)"></td>
								<td><p>0%</p></td>
							</tr>
							<tr>
								<td>30-39：</td>
								<td><input class="malerange" disabled="disabled" value="0" name="adCrowdVo.num" type="range" oninput="myFunction(this)"></td>
								<td><p>0%</p></td>
							</tr>
							<tr>
								<td>40-49：</td>
								<td><input class="malerange" disabled="disabled" value="0" name="adCrowdVo.num" type="range" oninput="myFunction(this)"></td>
								<td><p>0%</p></td>
							</tr>
							<tr>
								<td>50-59：</td>
								<td><input class="malerange" disabled="disabled" value="0" name="adCrowdVo.num" type="range" oninput="myFunction(this)"></td>
								<td><p>0%</p></td>
							</tr>
							<tr>
								<td>>60：</td>
								<td><input class="malerange" disabled="disabled" value="0" name="adCrowdVo.num" type="range" oninput="myFunction(this)"></td>
								<td><p>0%</p></td>
							</tr>

							<tr>
								<td>人群选择：</td>
								<td><input type="checkbox" name="adCrowdVo.female" id="female" value="2" onclick="checkboxOnclick(this)">女</td>
							</tr>
							<tr>
								<td><18：</td>
								<td><input type="range" class="femalerange" value="0" name="adCrowdVo.num" disabled="disabled" oninput="myFunction(this)"></td>
								<td><p>0%</p></td>
							</tr>
							<tr>
								<td>18-24：</td>
								<td><input class="femalerange" disabled="disabled" name="adCrowdVo.num" value="0" type="range" oninput="myFunction(this)"></td>
								<td><p>0%</p></td>
							</tr>
							<tr>
								<td>25-29：</td>
								<td><input class="femalerange" disabled="disabled" name="adCrowdVo.num" value="0" type="range" oninput="myFunction(this)"></td>
								<td><p>0%</p></td>
							</tr>
							<tr>
								<td>30-39：</td>
								<td><input class="femalerange" disabled="disabled" name="adCrowdVo.num" value="0" type="range" oninput="myFunction(this)"></td>
								<td><p>0%</p></td>
							</tr>
							<tr>
								<td>40-49：</td>
								<td><input class="femalerange" disabled="disabled" name="adCrowdVo.num" value="0" type="range" oninput="myFunction(this)"></td>
								<td><p>0%</p></td>
							</tr>
							<tr>
								<td>50-59：</td>
								<td><input class="femalerange" disabled="disabled" name="adCrowdVo.num" value="0" type="range" oninput="myFunction(this)"></td>
								<td><p>0%</p></td>
							</tr>
							<tr>
								<td>>60：</td>
								<td><input class="femalerange" disabled="disabled" name="adCrowdVo.num" value="0" type="range" oninput="myFunction(this)"></td>
								<td><p>0%</p></td>
							</tr>
							<tr>
								<td>
									<div class="col-50">
										<button class="btn btn-red" id="submit">提交</button>
									</div>
								</td>

								<td>
									<div class="col-50">
										<button class="btn btn-red">取消</button>
									</div>
								</td>
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
	src="${model.static_domain}/js/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="../static/js/jquery.citys.js"></script>
<script type="text/javascript">
	$.fn.serializeObject = function() {
		var o = {};
		var a = this.serializeArray();
		$.each(a, function() {
			if (o[this.name] !== undefined) {
				if (!o[this.name].push) {
					o[this.name] = [ o[this.name] ];
				}
				o[this.name].push(this.value || '');
			} else {
				o[this.name] = this.value || '';
			}
		});
		return o;
	};

	$(function() {
		$('#form').submit(function() {
			return false;
		})
	});
	$('#submit').click(function() {
		$.ajax({
			url : '/resource/addinfo',
			type : 'POST',
			data : $('#form').serializeObject(),
			dataType : "json",
			traditional:true,
			success : function(data) {
				if (data.success) {
					alert('修改成功!');
				} else {
					alert('修改失败!' + data.errMsg);
				}
			}
		});
	});

	/*复选框选中时拖动条可以拖动  */
	function checkboxOnclick(checkbox) {
		if ($(checkbox).attr('id') == "male") {
			if ($(checkbox).is(':checked')) {
				$(".malerange").removeAttr("disabled");
			} else {
				$(".malerange").attr("disabled", "disabled");
			}
		} else if ($(checkbox).attr('id') == "female") {
			if ($(checkbox).is(':checked')) {
				$(".femalerange").removeAttr("disabled");
			} else {
				$(".femalerange").attr("disabled", "disabled");
			}
		}
	}

	function myFunction(range) {
		var x = $(range).val();
		$(range).parent().next().find("p").html(x + "%");
	}

	/* 	var x = 0;
	 var newX = 0;
	 function myFunction(range) {
	 x = Number($(range).val())+x;
	 $(range).parent().next().find("p").html(x + "%");
	 ($("tr input[type='range']").not(range)).each(function(index, element) {
	 newX=x;
	 if (Number($(this).val()) == 100 - newX) {
	 $(this).attr("disable","disable");
	 } else if(Number($(this).val())<100-newX){
	 $(this).attr("max",newX);
	 newX=100-x;
	 }
	 });
	 } */

	/*获取城市  */
	var $town = $('#demo3 select[name="street"]');
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
		region : '思明',
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
