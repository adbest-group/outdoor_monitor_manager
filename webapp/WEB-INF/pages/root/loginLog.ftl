<#assign webTitle="登录日志管理" in model> <#assign webHead in model> </#assign>
<@model.webhead />
<!-- 头部 -->
<@model.webMenu current="登录日志管理" child="登录日志管理" />
<!-- 特色内容 -->
<div class="main-container" style="height: auto;">
	<div class="main-box">
		<div class="title clearfix" style="display: block;">
		
			<div class="search-box search-ll" style="margin: 0 0 0 20px">
			
                <div class="inp">
                    <input type="text" placeholder="请输入用户名" value="${username?if_exists}" id="username" name="username">
                </div>
                <div class="inp">
                    <input type="text" placeholder="请输入真实姓名" value="${realname?if_exists}" id="realname" name="realname">
                </div>
 				<div class="select-box select-box-140 un-inp-select ll">
                        <select id="type" name="type" class="select">
                         	<option value="">人员类型</option>
                            <option value="0" <#if (type?exists&&type == '0')>selected</#if>>后台</option>
                        	<option value="1" <#if (type?exists&&type == '1')>selected</#if>>APP</option>	     					 
                        </select>
                 </div>
                 <div class="ll inputs-date" id="createTimeLog">
                      <div class="date">
                          <input id="createTimeBegin" value="${beginTime?if_exists}" class="Wdate" type="text" >
                          <input id="createTimeEnd" value="${endTime?if_exists}" class="Wdate" type="text" >
                      </div>
                  </div>
                  <span style="margin-left:10px;" id="createTimeTip"></span>
                <button type="button" class="btn btn-red" autocomplete="off" id="searchBtn">查询</button>
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
							<th>用户名</th>
							<th>真实名字</th>
                        	<th>IP</th>
                        	<!-- <th>位置</th> -->
                        	<th>创建时间</th>
                        	
						</tr>
					</thead>
					<tbody>
						<#if (bizObj.list?exists && bizObj.list?size>0) > <#list
						bizObj.list as type>
						<tr>
							<td>${(bizObj.page.currentPage-1)*20+type_index+1}</td>
							<td>${type.username!""}</td>
							<td>${type.realname!""}</td> 
							<td>${type.ip!""}</td>
							<!-- <td>${type.location!""}</td> -->
							<td>${type.createTime?string('yyyy-MM-dd HH:mm:ss')}</td>
							
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
<!-- 时期 -->
<link href="${model.static_domain}/js/date/daterangepicker.css" rel="stylesheet">
<script type="text/javascript" src="${model.static_domain}/js/date/moment.min.js"></script>
<script type="text/javascript" src="${model.static_domain}/js/date/jquery.daterangepicker.js"></script>
<script type="text/javascript" src="${model.static_domain}/js/date.js"></script>
<script type="text/javascript">
$(function(){
    $(window).resize();
    $('#type').searchableSelect();
    $('#createTimeLog').dateRangePicker({
        separator: '-',
        showShortcuts: false,
        getValue: function () {
        	return '';
        },
        setValue: function (s1, s2, s3) {
        	console.log(s1, s2, s3)
            $('#createTimeBegin').val(s2);
            $('#createTimeEnd').val(s3);	
        }
    });
});

$(window).resize(function() {
    var h = $(document.body).height() - 115;
    $('.main-container').css('height', h);
});

 	// 查询
    $("#searchBtn").on("click", function () {
        var strParam = {};
        var username = $("#username").val();
        var realname = $("#realname").val();
        var type = $("#type").val();
         var begin = $('#createTimeBegin').val();
         var end = $('#createTimeEnd').val()
        if (username != null && $.trim(username).length) {
            strParam.username = username
        }
        
        if (realname != null && $.trim(realname).length) {
            strParam.realname = realname
        }
        
        if (type != null && $.trim(type).length) {
            strParam.type = type
        }
        if (begin != null && $.trim(begin).length) {
            strParam.begin = begin
        }
        if (end != null && $.trim(end).length) {
            strParam.end = end
        }
        var param = '?';
        for(var key in strParam){
        	param = param + key + '=' + strParam[key] + '&'
        }
        param = param.substr(0, param.length - 1)
        window.location.href = "/root/loginLog" + param;
    });
</script>
<!-- 特色内容 -->
<@model.webend />
