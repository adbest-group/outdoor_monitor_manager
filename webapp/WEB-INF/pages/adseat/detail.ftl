<#assign webTitle="资源管理任务详情" in model> <#assign webHead in model>
</#assign> <@model.webhead />
<!-- 头部 -->
<@model.webMenu current="监测管理" child="监测管理" />

<!-- 特色内容 -->
<div class="main-container">
	<div class="clearfix">
		<div class="main-box">
			<div class="bd new-active">
				<div class="hd mt-10">
					<h3>广告位详情</h3>
				</div>
				<div class="bd" style="font-size: 15px">
					<#if (vo?exists)>
						<div>广告位名称：${vo.name!""}</div>
						<div>媒体名称：${vo.mediaName!""}</div>
						<div>广告位类型名称：${vo.typeName!""}</div>
						<div>地区：${vo.province!""}${vo.city!""}${vo.region!""}${vo.street!""}</div>
						<div>广告位编号：${vo.adCode!""}</div>
						<div>广告位位置：${vo.location!""}</div>
						<div>广告位尺寸：${vo.adSize!""}</div>
						<div>日均PV：${vo.pv!""}</div>
						<div>私家车日均PV：${vo.privateCarPv!""}</div>
						<div>公交车日均PV：${vo.busPv!""}</div>
						<div>经度：${vo.lon!""}</div>
						<div>纬度：${vo.lat!""}</div>
					<#else>
						<div>没有相应结果。</div>
					</#if>
				</div>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript"
	src="${model.static_domain}/js/jquery-2.1.4.min.js"></script>
<script type="text/javascript">
	$(function() {
		$(window).resize(function() {
			var h = $(document.body).height() - 115;
			$('.main-container').css('height', h);
		});
		$(window).resize();
	});
</script>
<@model.webend />
