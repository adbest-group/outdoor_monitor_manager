<#assign webTitle="系统管理" in model>
<#assign webHead in model>
<link rel="stylesheet" type="text/css" href="${model.static_domain}/css/style.css?v=3.0">
<link rel="stylesheet" type="text/css" href="${model.static_domain}/css/fonts.css?v=3.0">
<link rel="stylesheet" type="text/css" href="${model.static_domain}/css/daterangepicker.css" />
</#assign>
<@model.webhead />
	<div class="wrapper clearfix">
		<!-- 头部 -->
		<@model.menutop />
		<!-- 左侧 -->
		<@model.menuleft current="system" />
		<!-- 主业务区域 -->
		<div class="main" id="main">
			<div class="side clearfix" style="width:85px;height:60px;float:left;"></div>
			<div class="main-container" style="height: auto;">
				<!-- 系统管理 -->
                <div class="ty-effect analysis">
	                <ul class="nav-tabs clearfix">
	                    <li>
	                        <a href="/system/account/list">账户管理</a>
	                    </li>
	                    <li>
	                        <a href="/system/role/list">角色管理</a>
	                    </li>
	                    <li class="on">
	                        <a href="/system/log/list">操作记录</a>
	                    </li>
	                </ul>
					<!-- 筛选 -->
	                <div class="hd bor-left bor-right">
	                	<div class="select-box plan-select-box">
							<select class="plan-select" name="module_name">
								<option value="">全部模块</option>
								<option <#if bizObj.queryMap.moduleName?if_exists=='系统首页'>selected</#if> value="系统首页">系统首页</option>
								<option <#if bizObj.queryMap.moduleName?if_exists=='客户管理-直客管理'>selected</#if> value="客户管理-直客管理">客户管理-直客管理</option>
								<option <#if bizObj.queryMap.moduleName?if_exists=='客户管理-合作方管理'>selected</#if> value="客户管理-合作方管理">客户管理-合作方管理</option>
								<option <#if bizObj.queryMap.moduleName?if_exists=='审核管理-资质审核'>selected</#if> value="审核管理-资质审核">审核管理-资质审核</option>
								<option <#if bizObj.queryMap.moduleName?if_exists=='审核管理-创意审核'>selected</#if> value="审核管理-创意审核">审核管理-创意审核</option>
								<option <#if bizObj.queryMap.moduleName?if_exists=='渠道控制'>selected</#if> value="渠道控制">渠道控制</option>
								<option <#if bizObj.queryMap.moduleName?if_exists=='投放监测'>selected</#if> value="投放监测">投放监测</option>
								<option <#if bizObj.queryMap.moduleName?if_exists=='优化策略'>selected</#if> value="优化策略">优化策略</option>
								<option <#if bizObj.queryMap.moduleName?if_exists=='报表分析'>selected</#if> value="报表分析">报表分析</option>
								<option <#if bizObj.queryMap.moduleName?if_exists=='财务充值'>selected</#if> value="财务充值">财务充值</option>
								<option <#if bizObj.queryMap.moduleName?if_exists=='系统管理-账户管理'>selected</#if> value="系统管理-账户管理">系统管理-账户管理</option>
								<option <#if bizObj.queryMap.moduleName?if_exists=='系统管理-网站管理'>selected</#if> value="系统管理-网站管理">系统管理-网站管理</option>
								<option <#if bizObj.queryMap.moduleName?if_exists=='系统管理-角色管理'>selected</#if> value="系统管理-角色管理">系统管理-角色管理</option>
							</select>
						</div>
						<div class="select-box state-select-box"  style="margin: 0 20px 0 0">
							<select class="plan-select" name="opt_type">
							    <option value="">全部操作</option>
								<option <#if bizObj.queryMap.optType?if_exists=='充值'>selected</#if> value="充值">充值</option>
								<option <#if bizObj.queryMap.optType?if_exists=='登录'>selected</#if> value="登录">登录</option>
								<option <#if bizObj.queryMap.optType?if_exists=='删除'>selected</#if> value="删除">删除</option>
								<option <#if bizObj.queryMap.optType?if_exists=='修改'>selected</#if> value="修改">修改</option>
								<option <#if bizObj.queryMap.optType?if_exists=='新增'>selected</#if> value="新增">新增</option>
								<option <#if bizObj.queryMap.optType?if_exists=='提交'>selected</#if> value="提交">提交</option>
								<option <#if bizObj.queryMap.optType?if_exists=='审核通过'>selected</#if> value="审核通过">审核通过</option>
								<option <#if bizObj.queryMap.optType?if_exists=='审核拒绝'>selected</#if> value="审核拒绝">审核拒绝</option>
							</select>
						</div>
	                    <div class="search-box search-ll">
                            <div class="inp">
                                <input type="text" value="${bizObj.queryMap.userName?if_exists}" placeholder="请输入操作者" id="name"></input>
                            </div>
                        </div>
                        <a href="javascript:;" class="btn btn-success" id="findBtn">查询</a> 
	                    <div class="datepicker" style="float: right;">
	                        <div class="ui-datepicker-quick">
	                            <input class="ui-date-quick-button <#if tag?exists && tag=='0'>on</#if>" type="button" value="今天" alt="0"  name=""/>
								<input class="ui-date-quick-button <#if tag?exists && tag=='-1'>on</#if>" type="button" value="昨天" alt="-1" name=""/>
								<input class="ui-date-quick-button <#if tag?exists && tag=='-2'>on</#if>" type="button" value="前天" alt="-2" name=""/>
								<input class="ui-date-quick-button <#if tag?exists && tag=='-6'>on</#if>" type="button" value="最近7天" alt="-6" name=""/>
								<input class="ui-date-quick-button <#if tag?exists && tag=='-29'>on</#if>" type="button" value="近30天" alt="-29" name=""/>
	                        </div>
	                        <input type="text" class="ui-datepicker-time" readonly value="${bizObj.queryMap.startDate?if_exists}一${bizObj.queryMap.endDate?if_exists}" />
	                        <div class="ui-datepicker-css">
	                            <div class="ui-datepicker-choose">
	                                <p>日期选择<a class="ui-close-date">X</a></p>
	                                <div class="ui-datepicker-date">
	                                    <input name="startDate" id="startDate" class="startDate" readonly value="${bizObj.queryMap.startDate?if_exists}" type="text">
	                                    -
	                                    <input name="endDate" id="endDate" class="endDate" readonly  value="${bizObj.queryMap.endDate?if_exists}" type="text" disabled onChange="datePickers()">
	                                </div>
	                            </div>
	                        </div>
	                    </div>
	                </div>
	                <div class="bd">
						<!-- 数据报表 -->
		                <div class="data-report">
		                    <div class="bd">
		                        <table width="100%" cellpadding="0" cellspacing="0" border="0" class="tablesorter" id="plan">
		                            <thead>
		                            <tr>
                                        <th>序号</th>
		                                <th>时间</th>
		                                <th>操作描述</th>
		                                <th>操作类型</th>
		                                <th>模块名称</th>
		                                <th>操作者</th>
		                                <th>访问IP</th>
		                            </tr>
		                            </thead>
		                            <tbody>
		                                <#list bizObj.list as l>
											<tr>
											    <td>${(bizObj.page.currentPage-1)*20+l_index+1}</td>
												<td>${l.createTime?string('yyyy-MM-dd HH:mm:ss')}</td>
												<td><#if l.optType=='修改'><span class="modified">${l.operateDesc}</span>
												    <#else>${l.operateDesc}
												    </#if>
												</td>
												<input type="hidden" class="before" value="${l.beforeDesc}">
												<input type="hidden" class="after" value="${l.afterDesc}">
												<td><#if l.optType=='充值'><span class="s-orange">充值</span>
													    <#elseif l.optType=='登录'><span class="s-green">登录</span>
													    <#elseif l.optType=='删除'>删除
		 											    <#elseif l.optType=='修改'><span class="modified s-red">修改</span>
													    <#elseif l.optType=='新增'><span class="s-blue">新增</span>
													    <#elseif l.optType=='提交'>提交 
													    <#elseif l.optType=='审核通过'>审核通过
													    <#elseif l.optType=='审核拒绝'>审核拒绝
													 <#else>未知     
												    </#if>
												</td>
												<td>${l.moduleName}</td>
												<td>${l.userName}</td>
												<td>${l.ip}</td>
											</tr>
										</#list>
		                            </tbody>
		                            <!-- 翻页 -->
								    <@model.showPage url=vm.getUrlByRemoveKey(thisUrl, ["start", "size"]) p=bizObj.page parEnd="" colsnum=7 />
		                        </table>
		                    </div>
		                </div>
		            </div>
	            </div>
	            <div class="hold-bottom" style="height:30px"></div>
			</div>
		</div>
	</div>
	
	<!--弹框 修改前和修改后 -->
	<div class="black-overlay">
		<div class="black-box" style="width:800px; margin-left:-400px;">
			<div class="black-hd">修改情况 <a class="black-closed" href="javascript:;">×</a></div>
			<div class="data-report">
				<div class="bd">
					<table width="100%" cellpadding="0" cellspacing="0" border="0" class="tablesorter"> 
						<thead>
						  <tr>
						   		<th>修改前：</th>
						   		<th>修改后：</th>
						  </tr>
					 	</thead>
						<tbody id="tbody">
							<tr>
						   		<td>人群设置：123 123 12 3 12</td>
						   		<td>人群设置：223 223 22 3 22</td>
							</tr>
							<tr>
						   		<td>出价：1.0</td>
						   		<td>出价：1.2</td>
							</tr>
						</tbody>
					</table>
			  	</div>
			  </div>
		</div>
	</div>
	
	<!-- jquery -->
	<script type="text/javascript" src="${model.static_domain}/js/jquery-1.11.3.min.js"></script>
	<!-- 弹出框 -->
	<script type="text/javascript" src="${model.static_domain}/js/layer/layer.js"></script>
	<!-- 下拉 -->
	<link href="${model.static_domain}/js/select/jquery.searchableSelect.css" rel="stylesheet">
	<script src="${model.static_domain}/js/select/jquery.searchableSelect.js"></script>
	<!-- 日期 -->
	<link rel="stylesheet" href="${model.static_domain}/js/date/jquery-ui-1.9.2.custom.css" type="text/css">
	<script type="text/javascript" src="${model.static_domain}/js/date/jquery-ui-1.9.2.custom.js"></script>
	<script type="text/javascript" src="${model.static_domain}/js/date/share.js"></script>
	<!-- common.js -->
	<script type="text/javascript" src="${model.static_domain}/js/common.js"></script>
    <!-- 图片缩放 -->
	<script type="text/javascript" src="${model.static_domain}/js/jquery.resize.js"></script>
	<script type="text/javascript">
		$(function(){
			// 下拉
			$('.plan-select').searchableSelect();
			
			// 时间tag点击事件
			$(".ui-datepicker-quick input").on("click", function(){
				var tag = $(this).attr("alt");
				var moduleName = $("select[name='module_name']").siblings().find(".searchable-select-item.selected").data("value");
		        var optType = $("select[name='opt_type']").siblings().find(".searchable-select-item.selected").data("value");
		        var userName=$("#name").val();
				window.location.href = "/system/log/list?tag=" + tag+"&moduleName="+moduleName+"&optType="+optType+"&userName="+userName;
			});
         
            //多条件查询
	        $("#findBtn").on("click",function(){
			   var moduleName = $("select[name='module_name']").siblings().find(".searchable-select-item.selected").data("value");
			   var optType = $("select[name='opt_type']").siblings().find(".searchable-select-item.selected").data("value");
			   var userName=$("#name").val();
			   var tag = "";
				if ($(".ui-date-quick-button.on")) {
					tag = $(".ui-date-quick-button.on").attr("alt");
				}
				var startDate = "";
				var endDate = "";
				if (typeof(tag) == "undefined") {
					tag = "";
					startDate = $("#startDate").val();
					endDate = $("#endDate").val()
				}
	           
	           var strParam = "?&startDate=" + startDate + "&endDate=" + endDate+"&tag="+tag+"&userName="+userName;
		    	if (moduleName != "") {
					strParam = strParam + "&moduleName=" + moduleName;
				}
				if ("optType" !="" ) {
					strParam = strParam + "&optType=" + optType;
				}
	           window.location.href="/system/log/list"+ strParam;
	        });
	        
	        $('.modified').on("click", function(){
	            $("#tbody").html("");
				$(".black-overlay").show();
				var _tr = $(this).closest('tr');
				var before=_tr.find(".before").val();
				var after=_tr.find(".after").val();
				var beforeArray=before.split(",");
				var afterArray=after.split(",");
				for(i=0;i<beforeArray.length;i++){
				    if(beforeArray[i].indexOf("jpg") != -1 || afterArray[i].indexOf("jpg") != -1){
				        var before_describe=beforeArray[i].substring(0,beforeArray[i].indexOf(":")+1);
				        var after_describe=afterArray[i].substring(0,afterArray[i].indexOf(":")+1);
				        var before_jpg=beforeArray[i].substring(beforeArray[i].indexOf(":")+1,beforeArray[i].length);
				        var after_jpg=afterArray[i].substring(afterArray[i].indexOf(":")+1,afterArray[i].length);
				        $("#tbody").append("<tr><td><div class='pic' style='display:inline;'>"+before_describe+"<div class='subpic'><img src="+before_jpg+"></div></div></td><td><div class='pic' style='display:inline;'>"
				           +after_describe+"<div class='subpic'><img src="+after_jpg+"></div></div></td></tr>");
						//图片等比例缩放
			    		$("#tbody .pic img").resizeimg(80, 80);
				    }else{
				        $("#tbody").append("<tr><td><div style='overflow: hidden;width: 340px;word-wrap: break-word;white-space: normal;'>"+beforeArray[i]+"</div></td><td><div style='overflow: hidden;width: 340px;word-wrap: break-word;white-space: normal;'>"+afterArray[i]+"</div></td></tr>");
				    }
				}
			});
			
			//关闭
			$(".black-closed,.btn-default").on('click', function(){
				$(".black-overlay").hide();
			});
		});
     
        function datePickers() {
		   $(".ui-datepicker-css").css("display","none");
		   var moduleName = $("select[name='module_name']").siblings().find(".searchable-select-item.selected").data("value");
		   var optType = $("select[name='opt_type']").siblings().find(".searchable-select-item.selected").data("value");
           var startDate=$("#startDate").val();
           var endDate=$("#endDate").val();
           var userName=$("#name").val();
           var strParam = "?&startDate=" + startDate + "&endDate=" + endDate+"&userName="+userName;
	    	if (moduleName != "") {
				strParam = strParam + "&moduleName=" + moduleName;
			}
			if (optType != "") {
				strParam = strParam + "&optType=" + optType;
			}
           window.location.href="/system/log/list"+ strParam;
		}
	</script>
<@model.webend />