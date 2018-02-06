<#assign webTitle="资源管理" in model> <#assign webHead in model> </#assign>
<@model.webhead />
<!-- 头部 -->
<@model.webMenu current="资源管理" child="资源管理" />
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
					onclick="javascrtpt:window.location.href='/resource/add'">新增广告位</button>
				<button style="margin: 10px" type="button" class="btn"
					autocomplete="off" onclick="">批量导入</button>
				<div style="border-bottom: 1px solid black; margin-bottom: 10px"></div>
				<form id="form" method="get" action="/resource/list">
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

					<div style="float: left; margin-left: 40px; font-size: 12px">
						媒体: <select style="height: 30px" name="mediaId">
							<option value="">所有媒体</option> <@model.showAllMediaOps
							value="${bizObj.queryMap.mediaName?if_exists}" />
						</select>
					</div>

					<div style="float: left; margin-left: 40px; font-size: 12px">
						人群选择: <select style="height: 30px" name="sex">
							<option value="">性别</option>
							<option value="1">男</option>
							<option value="2">女</option>
						</select> <select style="height: 30px" name="agePart">
							<option value="">年龄段</option>
							<option value="1"><18</option>
							<option value="2">19-24</option>
							<option value="3">25-29</option>
							<option value="4">30-39</option>
							<option value="5">40-49</option>
							<option value="6">50-59</option>
							<option value="7">>60</option>
						</select>
					</div>
					<button type="button" class="btn btn-red"
						style="margin-left: 10px;" autocomplete="off" id="searchBtn">查询</button>
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
							<th>广告位位置</th>
							<th>广告位尺寸</th>
							<th>广告位类型</th>
							<th>日均PV</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<#if (bizObj.list?exists && bizObj.list?size>0) > <#list
						bizObj.list as resource>
						<tr>
							<td>${resource.id}</td>
							<td>${resource.street!""}</td>
							<td>${resource.mediaName!""}</td>
							<td>${resource.location!""}</td>
							<td>${resource.adSize!""}</td>
							<td>${resource.name!""}</td>
							<td>${resource.pv!""}</td>
							<td style="width: 80px"><a href="#"
								style="margin-right: 5px">数据上传</a> <a
								href="javascript:deleteAdSeat('${resource.id}');"
								style="margin-right: 5px">删除</a> <a
								href="/resource/showDetails?id=${resource.id}">详情</a></td>
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

<script type="text/javascript" src="../static/js/jquery.citys.js"></script>


<script type="text/javascript">
	deleteAdSeat = function(id){
	    layer.confirm("确认删除？", {
	        icon: 3,
	        btn: ['确定', '取消'] //按钮
	    }, function(){
	        verify(id);
	    });
	}
	
	verify = function (id) {
        $.ajax({
            url: "/resource/verify",
            type: "post",
            data: {
                "id": id,
            },
            cache: false,
            dataType: "json",
            success: function(datas) {
                if(datas.success){
                	 layer.confirm("删除成功", {
                         icon: 2,
                         btn: ['确定'] //按钮
                     }, function() {
  						window.location.reload();
  					});
                }else{
                	layer.confirm("删除失败，请稍后再试", {
                        icon: 5,
                        btn: ['确定'] //按钮
                    });
                }
            }
        });
    }

	$("#searchBtn").on("click", function() {
		$("#form").submit();
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
					for (i in town) {
						$town.append('<option value="'+i+'">' + town[i]
								+ '</option>');
					}
				}
			});
		}
	};
	$('#demo3').citys({
		province : '所有城市',
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
