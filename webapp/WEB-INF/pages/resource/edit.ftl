<#assign webTitle="资源管理-编辑广告位" in model> <#assign webHead in model> </#assign>
<@model.webhead />
<!-- 头部 -->
<@model.webMenu current="资源管理" child="资源管理" />
<link rel="stylesheet" type="text/css"
	href="${model.static_domain}/css/new_main.css">
<link rel="stylesheet" type="text/css"
	href="${model.static_domain}/css/icon_fonts.css">

<style type="text/css">
.basic-info .bd .a-title {
	width: 120px;
}

img.demo {
	size: 50px;
}


</style>
<div class="main-container" style="height: auto;">
	<div class="clearfix">
		<div class="main-box basic-info">
			<div class="crumb-nav">
				<a href="/resource/list">资源管理</a>>编辑广告位
			</div>
			<div class="bd new-active">
				<div class="hd mt-10">
					<h3>编辑广告位</h3>
				</div>
				<form id="form" action="#">
					<table width="100%" cellpadding="0" cellspacing="0" border="0">
						<tbody>
							<tr>
								<td class="a-title"><font class="s-red">*</font>广告位名称：</td>
								<td>
									<input type="text" <#if (vo?exists)>value="${vo.name!""}"</#if> style="width: 130px;" id="adSeatInfo-name" name="adSeatInfo.name" autocomplete="off" class="form-control">
								</td>
							</tr>

							<tr>
								<td class="a-title"><font class="s-red">*</font>广告位编号：</td>
								<td>
									<input type="text" style="width: 130px;" id="adSeatInfo.adCode" <#if (vo?exists)>value="${vo.adCode!""}"</#if> name="adSeatInfo.adCode" autocomplete="off" class="form-control">
								</td>
							</tr>

							<tr>
								<td class="a-title"><font class="s-red">*</font>广告位置：</td>
								<td><input type="text" style="width: 130px;" id="adSeatInfo.location" name="adSeatInfo.location" <#if (vo?exists)>value="${vo.location!""}"</#if>
									autocomplete="off" class="form-control"></td>
							</tr>
 
							<tr>
								<td class="a-title"><font class="s-red">*</font>媒体名称：</td>
								<td>
									<select class="searchable-select-holder" name="adMedia.id"> 
										<#if (mediaList?exists && mediaList?size>0)> 
											<#list mediaList as item>
												<option value="${item.id}" <#if (vo.mediaId?exists && item.id==vo.mediaId)>selected="selected"</#if>>${item.mediaName!""}</option>
											</#list> 
										</#if>
									</select>
								</td>
							</tr>
							<tr>
								<td class="a-title"><font class="s-red">*</font>广告位类型：</td>
								<td>
									<select class="searchable-select-holder" name="adSeatType.id"> 
										<#if (adSeatTypesList?exists && adSeatTypesList?size>0)> 
											<#list adSeatTypesList as item>
												<option value="${item.id}" <#if (vo.typeId?exists && item.id==vo.typeId)>selected="selected"</#if>>${item.name!""}</option> 
											</#list>
										</#if>
									</select>
								</td>
							</tr>
							<tr>
								<td class="a-title"><font class="s-red">*</font>投放地区：</td>
								<td>
									<div id="demo3" class="citys">
										<p>
											<select class="searchable-select-holder" id="adSeatInfo-province" name="province" style="width: 147px"></select> 
											<select class="searchable-select-holder" id="adSeatInfo-city" name="city"></select> 
											<select class="searchable-select-holder" id="adSeatInfo-region" name="region"></select> 
											<select class="searchable-select-holder" id="adSeatInfo-street" name="street"></select>
										</p>
									</div>
								</td>
							</tr>
							<tr>
								<td class="a-title"><font class="s-red">*</font>广告位尺寸：</td>
								<td>
									<input type="text" style="width: 130px;" <#if (vo?exists)>value="${vo.adSize!""}"</#if> id="adSeatInfo.adSize" name="adSeatInfo.adSize" autocomplete="off" class="form-control">
								</td>
							</tr>
							<tr>
								<td class="a-title"><font class="s-red">*</font>日均PV：</td>
								<td>
									<input type="text" style="width: 130px;" <#if (vo?exists)>value="${vo.pv!""}"</#if> id="adSeatInfo.pv" name="adSeatInfo.pv" autocomplete="off" class="form-control">
								</td>
							</tr>
							<tr>
								<td class="a-title"><font class="s-red">*</font>私家车日均PV：</td>
								<td>
									<input type="text" style="width: 130px;" id="adSeatInfo.privateCarPv" <#if (vo?exists)>value="${vo.privateCarPv!""}"</#if> name="adSeatInfo.privateCarPv" autocomplete="off" class="form-control">
								</td>
							</tr>
							<tr>
								<td class="a-title"><font class="s-red">*</font>公交车日均PV：</td>
								<td>
									<input type="text" style="width: 130px;" id="adSeatInfo.busPv" <#if (vo?exists)>value="${vo.busPv!""}"</#if> name="adSeatInfo.busPv" autocomplete="off" class="form-control">
								</td>
							</tr>
							<tr>
								<td class="a-title"><font class="s-red">*</font>广告位经度：</td>
								<td>
									<input type="text" style="width: 130px;" id="adSeatInfo.lon" <#if (vo?exists)>value="${vo.lon!""}"</#if> name="adSeatInfo.lon" autocomplete="off" class="form-control">
								</td>
							</tr>
							<tr>
								<td class="a-title"><font class="s-red">*</font>广告位纬度：</td>
								<td>
									<input type="text" style="width: 130px;" id="adSeatInfo.lat" <#if (vo?exists)>value="${vo.lat!""}"</#if> name="adSeatInfo.lat" autocomplete="off" class="form-control">
								</td>
							</tr>
							<tr>
								<td class="a-title"><font class="s-red">*</font>人群选择：</td>
								<td>男</td>
								<td></td>
								<td class="a-title"><font class="s-red">*</font>人群选择：</td>
								<td>女</td>
								<td></td>
							</tr>
							</tr>
							<tr>
								<td class="a-title"><18：</td>
								<td style="float: left">
									<input type="range" name="adCrowdVo.maleNum" 
									<#if (adCrowdVoList?exists && adCrowdVoList?size>0)>
										<#list adCrowdVoList as list>
											<#if list_index==0>
												value="${list.nums!""}"
											</#if>
										</#list>
									</#if> class="malerange" value="0" oninput="myFunction(this)">
								</td>
								<td style="float: left">
									<p class="rangevalue">
										<#if (adCrowdVoList?exists && adCrowdVoList?size>0)>
											<#list adCrowdVoList as list>
												<#if list_index==0>
													${list.nums!""}%
												</#if>
											</#list>
										</#if>
									</p>
								</td>

								<td class="a-title"><18：</td>
								<td>
									<input type="range" class="femalerange" 
									<#if (adCrowdVoList?exists && adCrowdVoList?size>0)>
										<#list adCrowdVoList as list>
											<#if list_index==7>
												value="${list.nums!""}"
											</#if>
										</#list>
									</#if> name="adCrowdVo.femaleNum" oninput="myFunction(this)">
								</td>
								<td style="float: left">
									<p>
										<#if (adCrowdVoList?exists && adCrowdVoList?size>0)>
											<#list adCrowdVoList as list>
												<#if list_index==7>
													${list.nums!""}%
												</#if>
											</#list>
										</#if>
									</p>
								</td>
							</tr>
							
							<tr>
								<td class="a-title">18-24：</td>
								<td style="float: left">
									<input class="malerange" 
									<#if (adCrowdVoList?exists && adCrowdVoList?size>0)>
										<#list adCrowdVoList as list>
											<#if list_index==1>
												value="${list.nums!""}"
											</#if>
										</#list>
									</#if> value="0" type="range" name="adCrowdVo.maleNum" oninput="myFunction(this)">
								</td>
								<td style="float: left">
									<p class="rangevalue">
										<#if (adCrowdVoList?exists && adCrowdVoList?size>0)>
											<#list adCrowdVoList as list>
												<#if list_index==1>
													${list.nums!""}%
												</#if>
											</#list>
										</#if>
									</p>
								</td>

								<td class="a-title">18-24：</td>
								<td>
									<input class="femalerange" name="adCrowdVo.femaleNum" 
									<#if (adCrowdVoList?exists && adCrowdVoList?size>0)>
										<#list adCrowdVoList as list>
											<#if list_index==8>
												value="${list.nums!""}"
											</#if>
										</#list>
									</#if> type="range" oninput="myFunction(this)">
								</td>
								<td style="float: left"><p>
										<#if (adCrowdVoList?exists && adCrowdVoList?size>0)>
											<#list adCrowdVoList as list>
												<#if list_index==8>
													${list.nums!""}%
												</#if>
											</#list>
										</#if></p></td>
							</tr>
							
							<tr>
								<td class="a-title">25-29：</td>
								<td style="float: left">
									<input class="malerange" 
										<#if (adCrowdVoList?exists && adCrowdVoList?size>0)>
											<#list adCrowdVoList as list>
												<#if list_index==2>
													value="${list.nums!""}"
												</#if>
											</#list>
										</#if> name="adCrowdVo.maleNum" type="range" oninput="myFunction(this)">
								</td>
								<td style="float: left">
									<p>
										<#if (adCrowdVoList?exists && adCrowdVoList?size>0)>
											<#list adCrowdVoList as list>
												<#if list_index==2>
													${list.nums!""}%
												</#if>
											</#list>
										</#if>
									</p>
								</td>

								<td class="a-title">25-29：</td>
								<td>
									<input class="femalerange" name="adCrowdVo.femaleNum" 
									<#if (adCrowdVoList?exists && adCrowdVoList?size>0)>
										<#list adCrowdVoList as list>
											<#if list_index==9>
												value="${list.nums!""}"
											</#if>
										</#list>
									</#if> type="range" oninput="myFunction(this)">
								</td>
								<td><p><#if (adCrowdVoList?exists && adCrowdVoList?size>0)>
											<#list adCrowdVoList as list>
												<#if list_index==9>
													${list.nums!""}%
												</#if>
											</#list>
										</#if></p></td>
							</tr>
							
							<tr>
								<td class="a-title">30-39：</td>
								<td style="float: left">
									<input class="malerange" 
										<#if (adCrowdVoList?exists && adCrowdVoList?size>0)>
											<#list adCrowdVoList as list>
												<#if list_index==3>
													value="${list.nums!""}"
												</#if>
											</#list>
										</#if> name="adCrowdVo.maleNum" type="range" oninput="myFunction(this)">
								</td>
								<td style="float: left">
									<p>
										<#if (adCrowdVoList?exists && adCrowdVoList?size>0)>
											<#list adCrowdVoList as list>
												<#if list_index==3>
													${list.nums!""}%
												</#if>
											</#list>
										</#if>
									</p>
								</td>

								<td class="a-title">30-39：</td>
								<td>
									<input class="femalerange" name="adCrowdVo.femaleNum" 
									<#if (adCrowdVoList?exists && adCrowdVoList?size>0)>
										<#list adCrowdVoList as list>
											<#if list_index==10>
												value="${list.nums!""}"
											</#if>
										</#list>
									</#if> type="range" oninput="myFunction(this)">
								</td>
								<td><p><#if (adCrowdVoList?exists && adCrowdVoList?size>0)>
											<#list adCrowdVoList as list>
												<#if list_index==10>
													${list.nums!""}%
												</#if>
											</#list>
										</#if></p></td>
							</tr>
							
							<tr>
								<td class="a-title">40-49：</td>
								<td style="float: left">
									<input class="malerange" 
									<#if (adCrowdVoList?exists && adCrowdVoList?size>0)>
										<#list adCrowdVoList as list>
											<#if list_index==4>
												value="${list.nums!""}"
											</#if>
										</#list>
									</#if> name="adCrowdVo.maleNum" type="range" oninput="myFunction(this)">
								</td>
								<td style="float: left">
									<p>
										<#if (adCrowdVoList?exists && adCrowdVoList?size>0)>
											<#list adCrowdVoList as list>
												<#if list_index==4>
													${list.nums!""}%
												</#if>
											</#list>
										</#if>
									</p>
								</td>

								<td class="a-title">40-49：</td>
								<td>
									<input class="femalerange" name="adCrowdVo.femaleNum" 
									<#if (adCrowdVoList?exists && adCrowdVoList?size>0)>
										<#list adCrowdVoList as list>
											<#if list_index==11>
												value="${list.nums!""}"
											</#if>
										</#list>
									</#if> type="range" oninput="myFunction(this)">
								</td>
								<td><p><#if (adCrowdVoList?exists && adCrowdVoList?size>0)>
											<#list adCrowdVoList as list>
												<#if list_index==11>
													${list.nums!""}%
												</#if>
											</#list>
										</#if></p></td>
							</tr>
							
							<tr>
								<td class="a-title">50-59：</td>
								<td style="float: left">
									<input class="malerange" 
									<#if (adCrowdVoList?exists && adCrowdVoList?size>0)>
										<#list adCrowdVoList as list>
											<#if list_index==5>
												value="${list.nums!""}"
											</#if>
										</#list>
									</#if> name="adCrowdVo.maleNum" type="range" oninput="myFunction(this)">
								</td>
								<td style="float: left">
									<p>
										<#if (adCrowdVoList?exists && adCrowdVoList?size>0)>
											<#list adCrowdVoList as list>
												<#if list_index==5>
													${list.nums!""}%
												</#if>
											</#list>
										</#if>
									</p>
								</td>

								<td class="a-title">50-59：</td>
								<td>
									<input class="femalerange" name="adCrowdVo.femaleNum" 
									<#if (adCrowdVoList?exists && adCrowdVoList?size>0)>
										<#list adCrowdVoList as list>
											<#if list_index==12>
												value="${list.nums!""}"
											</#if>
										</#list>
									</#if> type="range" oninput="myFunction(this)">
								</td>
								<td><p><#if (adCrowdVoList?exists && adCrowdVoList?size>0)>
											<#list adCrowdVoList as list>
												<#if list_index==12>
													${list.nums!""}%
												</#if>
											</#list>
										</#if></p></td>
							</tr>
							
							<tr>
								<td class="a-title">>60：</td>
								<td style="float: left">
									<input class="malerange" 
									<#if (adCrowdVoList?exists && adCrowdVoList?size>0)>
										<#list adCrowdVoList as list>
											<#if list_index==6>
												value="${list.nums!""}"
											</#if>
										</#list>
									</#if> name="adCrowdVo.maleNum" type="range" oninput="myFunction(this)">
								</td>
								<td style="float: left">
									<p>
										<#if (adCrowdVoList?exists && adCrowdVoList?size>0)>
											<#list adCrowdVoList as list>
												<#if list_index==6>
													${list.nums!""}%
												</#if>
											</#list>
										</#if>
									</p>
								</td>

								<td class="a-title">>60：</td>
								<td>
									<input class="femalerange" name="adCrowdVo.femaleNum" 
									<#if (adCrowdVoList?exists && adCrowdVoList?size>0)>
										<#list adCrowdVoList as list>
											<#if list_index==13>
												value="${list.nums!""}"
											</#if>
										</#list>
									</#if> type="range" oninput="myFunction(this)">
								</td>
								<td><p><#if (adCrowdVoList?exists && adCrowdVoList?size>0)>
											<#list adCrowdVoList as list>
												<#if list_index==13>
													${list.nums!""}%
												</#if>
											</#list>
										</#if></p></td>
							</tr>


							<tr>
								<td></td>

								<td rowspan="6" colspan="6">
									<div class="col-50">
										<button class="btn btn-red" id="submit">修改</button>
									</div>
								</td>
								<td></td>
							</tr>
						</tbody>
					</table>
				</form>
			</div>
		</div>
	</div>
</div>
</div>
</div>
</div>

<script type="text/javascript"
	src="${model.static_domain}/js/jquery-2.1.4.min.js"></script>
<!-- 下拉 -->
<link
	href="${model.static_domain}/js/select/jquery.searchableSelect.css"
	rel="stylesheet">
<script
	src="${model.static_domain}/js/select/jquery.searchableSelect.js"></script>
<script src="${model.static_domain}/js/ajaxfileupload.js"></script>
<!-- 时期 -->
<link href="${model.static_domain}/js/date/daterangepicker.css"
	rel="stylesheet">
<script type="text/javascript"
	src="${model.static_domain}/js/date/moment.min.js"></script>
<script type="text/javascript"
	src="${model.static_domain}/js/date/jquery.daterangepicker.js"></script>
<script type="text/javascript" src="${model.static_domain}/js/date.js"></script>
<!-- 图片缩放 -->
<script type="text/javascript"
	src="${model.static_domain}/js/jquery.resize.js"></script>
<!-- formValidator -->
<link type="text/css" rel="stylesheet"
	href="${model.static_domain}/js/formValidator/style/validator.css"></link>
<script type="text/javascript"
	src="${model.static_domain}/js/formValidator/formValidator-4.0.1.js"></script>

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
			url : '',
			type : 'POST',
			data : $('#form').serializeObject(),
			dataType : "json",
			traditional : true,
			success : function(data) {
				if (data.success) {
					alert('修改成功!');
				} else {
					alert('修改失败!' + data.errMsg);
				}
			}
		});
	});

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
