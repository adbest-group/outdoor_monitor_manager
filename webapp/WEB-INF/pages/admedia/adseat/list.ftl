<#assign webTitle="资源管理" in model> <#assign webHead in model> </#assign>
<@model.webhead />
<!-- 头部 -->
<@model.webMenu current="广告位管理" child="" />
<link rel="stylesheet" type="text/css"
	href="${model.static_domain}/css/new_main.css">
<link rel="stylesheet" type="text/css"
	href="${model.static_domain}/css/icon_fonts.css">
<!-- 特色内容 -->
<div class="main-container" style="height: auto;">
	<div class="main-box">
		<div class="title clearfix" style="display: block;">
			<div class="search-box search-ll" style="margin: 0 0 0 20px">
				<button type="button" class="btn btn-red" autocomplete="off"
					onclick="window.location.href='/platmedia/adseat/edit'">新增广告位</button>
				<#--<button style="margin-left: 10px" type="button" class="btn"-->
					<#--autocomplete="off" onclick="">批量导入</button>-->
				<div style="border-bottom: 1px solid black; margin:10px auto"></div>
				<form id="form" method="get" action="/platmedia/adseat/list">
					<!--销售下拉框-->
					<div id="demo3" class="citys" style="float: left; font-size: 12px">
						<p>
							投放地区： <select style="height: 30px" id="adSeatInfo-province"
								name="province">
								<option value=""></option>
							</select> <select style="height: 30px" id="adSeatInfo-city" name="city"></select>
							<select style="height: 30px" id="adSeatInfo-region" name="region"></select>
							<select style="height: 30px" id="adSeatInfo-street" name="street"></select>
						</p>
					</div>

					<button type="button" class="btn btn-red"
						style="margin-left: 10px;" autocomplete="off" id="searchBtn">查询</button>
                    <button type="button" class="btn btn-primary"
                            style="margin-left: 10px;" autocomplete="off" id="clear">清除条件</button>
				</form>
			</div>
		</div>

		<!-- 数据报表 -->
		<div class="data-report">
			<div class="bd">
				<table width="100%" cellpadding="0" cellspacing="0" border="0"
					class="tablesorter" id="plan">
					<thead>
						<tr>
							<th width="30">序号</th>
							<th>区域</th>
							<th>所属媒体</th>
							<#--<th>媒体广告位编号</th>-->
							<th>广告位位置</th>
							<th>广告位尺寸</th>
							<th>广告位类型</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<#if (bizObj.list?exists && bizObj.list?size>0) > <#list
						bizObj.list as adseat>
						<tr>
							<td>${(bizObj.page.currentPage-1)*20+adseat_index+1}</td>
							<td>${vm.getCityNameFull(adseat.street!adseat.region,"-")!""}</td>
							<td>${adseat.mediaName!""}</td>
							<#--<td>${adseat.adCode!""}</td>-->
							<td>${adseat.location!""}</td>
							<td>${adseat.adSize!""}</td>
							<td>${adseat.typeName!""}</td>
							<td style="width: 80px">
								<#--<a href="#" style="margin-right: 5px">数据上传</a> -->
								<a href="/platmedia/adseat/edit?id=${adseat.id}" style="margin-right: 5px">编辑</a>
                                    <a href="javascript:deleteSeat('${adseat.id}');" style="margin-right: 5px">删除</a>
						</tr>
						</#list> <#else>
						<tr>
							<td colspan="20">没有相应结果。</td>
						</tr>
						</#if>
					</tbody>
					<!-- 翻页 -->
					<@model.showPage url=vm.getUrlByRemoveKey(thisUrl, ["start",
					"size"]) p=bizObj.page parEnd="" colsnum=9 />
				</table>
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

<script type="text/javascript" src="/static/js/jquery.citys.js"></script>


<script type="text/javascript">
	var deleteSeat = function(id){
	    layer.confirm("确认删除？", {
	        icon: 3,
	        btn: ['确定', '取消'] //按钮
	    }, function(){
            $.ajax({
                url: "/platmedia/adseat/delete",
                type: "post",
                data: {
                    "id": id,
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
						layer.confirm("删除成功", {
							icon: 1,
							btn: ['确定'] //按钮
						}, function () {
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
	    });
	};


	$("#searchBtn").on("click", function() {
		$("#form").submit();
	});
	$("#clear").click(function () {
        $("#demo3 select").val("");
    });

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
                    $town.append('<option value> - 请选择 - </option>');
					for (i in town) {
						$town.append('<option value="'+i+'" <#if (street?exists&&street?length>0)>'+(i==${street!0}?"selected":"")+'</#if>>' + town[i]
								+ '</option>');
					}
				}
			});
		}
	};
	$('#demo3').citys({
		required:false,
		province : '${province!"所有城市"}',
		city : '${city!""}',
		region : '${region!""}',
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

	$(function() {
		$(window).resize();
	});

	$(window).resize(function() {
		var h = $(document.body).height() - 115;
		$('.main-container').css('height', h);
	});

	var assign_ids;
	$(function() {
		$('.select').searchableSelect();
	});
</script>
<!-- 特色内容 -->
<@model.webend />
