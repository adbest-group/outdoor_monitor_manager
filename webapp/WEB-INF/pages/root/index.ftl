<#assign webTitle="首页" in model>
<#assign webHead in model>
</#assign>
<@model.webhead />
    <!-- 头部 -->
    <@model.webMenu current="index" child="" />
                <div class="main-container">
                    <div class="main-box">
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
	<script type="text/javascript" src="${model.static_domain}/js/walden.js"></script>
<@model.webend/>
		