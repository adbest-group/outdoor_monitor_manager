<#assign webTitle="首页" in model>
<#assign webHead in model>
</#assign>
<@model.webhead />
    <!-- 头部 -->
    <@model.webMenu current="index" child="" />
				<div class="main-container">
					<div class="main-box">
						<div class="unit-info-list unit-info-list2 clearfix">
							<ul>
								<li>
									<div>
										<em class="clearfix">客户</em>
										<span></span>
										<i class="icon-12"></i>
										<a href="javascript:;"><strong>${obj.advUserNum?if_exists}</strong></a>
									</div>
								</li>
								<li>
									<div>
										<em class="clearfix">代理商</em>
										<span></span>
										<i class="icon-7"></i>
										<a href="javascript:;"><strong>${obj.agentNum?if_exists}</strong></a>
									</div>
								</li>
								<li>
									<div>
										<em class="clearfix">总预算</em>
										<span></span>
										<i class="icon-11"></i>
										<a href="javascript:;"><strong>
										<#if (obj.putAmount?default(0) > 1000000)>${numberUtils.getPutAmount(obj.putAmount/10000?default(0))} 万
										<#else>
										  ${numberUtils.getPutAmount(obj.putAmount?default(0))}
										</#if>
										</strong></a>
									</div>
								</li>
								<li>
									<div>
										<em class="clearfix">已完成</em>
										<span></span>
										<i class="icon-9"></i>
										<a href="javascript:;"><strong>${obj.playedNum?if_exists}</strong></a>
									</div>
								</li>
							</ul>
						</div>
					</div>
                    
                    <#if (noticeMap?size>0) >
                    <div class="notice" style="overflow: hidden">                       
                        <ul>
						  <#list noticeMap as map>
						      <li><p>通知：</b>${map['name']}活动 距离活动开始日期${map['put_startdate']}还有<span class="s-red">${map['last_days']}</span>天，当前状态${map['plan_status']}，请尽快和客户联络。</p></li>
						  </#list>
						</ul>
                    </div>
                    </#if>
                    
					<div class="main-box">						
						<div class="bd">
							<div class="clearfix">
								<div class="select-box ll select-box-150" id="mediaAgent">
									<@model.showPartnerSelect name="agentName" value="${agentName?if_exists}" />
								</div>
								<div class="select-box ll select-box-150" id="mediaCustomer">
									<select class="select" name="userName">
										<option value="">客户名称</option>
									</select>
								</div>
								<div class="select-box ll select-box-150" id="mediaType">
									<@model.showMediaFlagSelect name="mediaFlag" value="${mediaFlag?if_exists}" />
								</div>
								<button type="button" class="btn btn-red" autocomplete="off" id="search">查询</button>

								<div class="rr inputs-date">
									<input class="ui-date-button on" type="button" value="今天" alt="0" name="">
									<input class="ui-date-button" type="button" value="昨天" alt="-1" name="">
									<input class="ui-date-button" type="button" value="近7天" alt="-6" name="">
									<input class="ui-date-button" type="button" value="近30天" alt="-29" name="">
									<div class="date">
										<input id="dts" class="Wdate" type="text" > - 
										<input id="dt" class="Wdate" type="text" >
									</div>
								</div>
							</div>
							<div class="charts-hd">
								投放曝光汇总： ${displaySum?if_exists}
							</div>

							<div class="default_charts">
		                        <div id="ott_line_charts"></div>
		                    </div>
						</div>

						<div class="bd mt-20">
							<div class="hd"><h3>媒体覆盖</h3></div>
							<div class="data-report">
								<div class="bd" style="max-height: 250px;overflow: auto">
									<table width="100%" cellpadding="0" cellspacing="0" border="0" class="tablesorter" >
										<thead>
											<tr>
												<th>代理商</th>
												<th>客户</th>
												<th>量级/CPM</th>
											</tr>
										</thead>
										<tbody id="areaMedia">
										    <#list mediaCoverList as coverMap>
										      <tr>
										   	     <td>${coverMap["company_name"]?if_exists}</td>
										   	     <td>${coverMap["username"]?if_exists}</td>
										   	     <td>${coverMap["display_num"]?if_exists}</td>
										       </tr>
										    </#list>
										</tbody>
									</table>
								</div>
							</div>


							<div class="hd mt-20"><h3>区域覆盖</h3></div>
							<div class="clearfix mt-20">
								<div class="select-box ll" id="provinceId">
									<select class="select">
										<option value="">地区名称</option>
										<#list vm.getProvice() as sysZone>
										    <option value="${sysZone.id?if_exists}">${sysZone.dicvalue?if_exists}</option>
										</#list>
									</select>
								</div>
								<a class="btn btn-red" href="javascript:;" id="search2">查 询</a>
							</div>
							<div class="data-report">
								<div class="bd" style="max-height: 250px;overflow: auto">
									<table width="50%" cellpadding="0" cellspacing="0" border="0" class="tablesorter">
										<thead>
											<tr>
												<th>覆盖</th>
												<th>量级/CPM</th>
											</tr>
										</thead>
										<tbody id="areaTbody">
										    <#list regionCoverList as regionMap>
										      <tr>
  										          <td>${regionMap["province_name"]?if_exists}</td>
												  <td>${regionMap["display_num"]?if_exists}</td>
											  </tr>
										    </#list>
											
										</tbody>
									</table>
								</div>
							</div>
						</div>
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

	<script>
	    <#if hourDisplayNum??>
		    var line11Data=[
		    	{name:'展示次数', childName:['0-2点','2-4点','4-6点','6-8点','8-10点','10-12点','12-14点','14-16点','16-18点','18-20点','20-22点','22-24'],
		        childValue:${hourDisplayNum?default('')}}];
		<#else>
		    var line11Data=[${obj.displayReportStr?if_exists}];
	    </#if>
	</script>
	<script type="text/javascript" src="${model.static_domain}/js/charts/index_chart.js"></script>

	<script type="text/javascript">
        $(function(){
            $(".nav-sidebar>ul>li").on("click",function(){
                $(".nav-sidebar>ul>li").removeClass("on");
                $(this).addClass("on");
            });
        });
        
		$(function(){
			$(window).resize();
		});

        $(window).resize(function() {
            var h = $(document.body).height() - 115;
            $('.main-container').css('height', h);
        });
		
		$(function(){
            function createDateStr(alt){
                var today =  new Date();
                var t=today.getTime()+1000*60*60*24*alt;
                var newDate=new Date(t).Format("yyyy-MM-dd");
                if(alt==-6||alt==-29)
                return newDate+" 至 "+today.Format("yyyy-MM-dd");
                return newDate+" 至 "+newDate;
            }
			
	        $('.inputs-date').dateRangePicker({
		        separator : ' 至 ',
		        showShortcuts:false,
		        getValue: function() {
		        	$(document).click();
		            if ($('#dts').val() && $('#dt').val()) {
		                return $('#dts').val() + ' 至 ' + $('#dt').val();
		            } else {
		                return '';
		            }
		        },
		        setValue: function(s,s1,s2) {
		            $('#dts').val(s1);
		            $('#dt').val(s2);
		        }
		    });

			// 下拉
			$('.select').searchableSelect();
			
			function clickEvent(){
				var startDate =	$('#dts').val();
		        var endDate = $('#dt').val();
		        var mediaFlag = $("#mediaType").find(".searchable-select-item.selected").data("value");
		        var mediaAgent = $("#mediaAgent").find(".searchable-select-item.selected").data("value");
				var mediaCustomer = $("#mediaCustomer").find(".searchable-select-item.selected").data("value");
		        $.ajax({
		            url: "/index/mediaCover",
	                type: "get",
	                data: {
	                    "agentName": mediaAgent,
	                    "customerId": mediaCustomer,
	                    "mediaFlag":mediaFlag,
	                    "startDate":startDate,
	                    "endDate":endDate
	                },
	                cache: false,
	                dataType: "json",
	                success: function(data) {
	                    var mediaJson = JSON.parse(data.mediaCover);
	                    var displayJson = JSON.parse(data.disNum);
                        //总曝光数
                        var displaySum = data.displaySum;
                        $(".charts-hd").text('投放曝光汇总：'+displaySum);
	                    if(data.ret.code==100) {
	                        var seriseLine11Arr=[];
							for(var i = 0,len =displayJson.length;i<len;i++ ){
								seriseLine11Arr.push({
									name:displayJson[i].name,
									type:'line',
									data:displayJson[i].childValue,
									
								});
							}

							ottLine11Chart.setOption({
								series: seriseLine11Arr,
								xAxis: {
									data:displayJson[0].childName,
								},
							});

							$('#areaMedia').empty()
							var html='';
							for(var i= 0;i<mediaJson.length;i++){
								html+='<tr>';
								html+='	<td>'+mediaJson[i].company_name+'</td>';
								html+='	<td>'+mediaJson[i].username+'</td>';
								html+='	<td>'+mediaJson[i].display_num+'</td>';
								html+='</tr>';
							}
							$('#areaMedia').append(html);
				         }
				  }
		        });
			}
			// clickEvent();

			
            $('#search').click(clickEvent)
			$(".inputs-date input.ui-date-button").on("click", clickEvent);
		    
			if($('#dts').val()==''){
				var newDate=new Date().Format("yyyy-MM-dd");
				$('.inputs-date').data('dateRangePicker').setDateRange(newDate,newDate, true);
			}else{
				var startDate =	$('#dts').val();
		        var endDate = $('#dt').val();
		        var s = startDate+' 至 '+endDate;
		         var dateArr=[0,-1,-6,-29]
				$(".ui-date-button").removeClass("on");
				dateArr.forEach(function(v,i){
					if(s==createDateStr(v)){
						$(".ui-date-button").eq(i).addClass("on");
					}
				})
			}
			
			$('.apply-btn').click(clickEvent);
			
	      $('#search2').click(function(){
			var provinceId = $("#provinceId").find(".searchable-select-item.selected").data("value");
            $.ajax({
              url:"/index/regioncover",
              type:"get",
              data:{"provinceId":provinceId},
              cache:false,
              dataType:"json",
              success: function(data){
                var regionCoverJson = JSON.parse(data.regionCoverJson);
                if (data.ret.code === 100) {
                  $('#areaTbody').empty()
				  var html='';
				  for(var i= 0;i<regionCoverJson.length;i++){
					html+='<tr>';
					html+='	<td>'+regionCoverJson[i].province_name+'</td>';
					html+='	<td>'+regionCoverJson[i].display_num+'</td>';
					html+='</tr>';
				  }
				  $('#areaTbody').append(html);
                }
              }
            });
	      });
		});
		
		$(function(){
			
			<#if agentName?exists>
			 //#先请原有的
             $("select[name='userName']").empty();
             $("select[name='userName']").append("<option value=''>客户名称</option>");
             $("select[name='userName']").siblings(".searchable-select").remove();
             
             var agentId = ${agentName};
             var userId = "";
             <#if customerId?exists>
             userId = ${customerId};
             </#if>
             if (selAgentId != null) {
                selAgentId(agentId, userId);
             }
             
             // 下拉
             $("select[name='userName']").searchableSelect();
			</#if>
        
            //代理选择联动
            $("select[name='agentName']").siblings(".searchable-select").find(".searchable-select-item").on("click",function(){
                 //#先请原有的
                 $("select[name='userName']").empty();
                 $("select[name='userName']").append("<option value=''>客户名称</option>");
                 $("select[name='userName']").siblings(".searchable-select").remove();
                 
                 var agentId = $(this).data("value");
                 
                 if (selAgentId != null) {
                    selAgentId(agentId, "");
                 }
                 // 下拉
                 $("select[name='userName']").searchableSelect();
            });
			
			$("#creativeSearch").on("click", function(){
              $("#searchForm").submit();
            });
            
            var html= $(".notice ul li").eq(0).html();
			var l = '<li>'+html+'</li>'
			$(".notice ul").append(l);
			var length = $(".notice ul li").length;
			var mt=0;
			if(length!=2)
			setInterval(function(){
				mt+=50;
				if(mt==50*length){
					mt=50;
					$(".notice ul").animate({ 'margin-top': "0px" }, 0)
				}
				$(".notice ul").animate({ 'margin-top': "-"+mt+"px" }, 1000)
			},4000);

		});

		function selAgentId(agentId, defValue) {
    		 $.ajax({
                url: "/plan/selAgentId",
                type: "post",
                data:{
                    "agentId":agentId
                },
                cache: false,
                async: false,
                dataType: "json",
                success: function(datas) {
                    var resultRet = datas.ret;
                    if (resultRet.code == 100) {
                        var result=datas.ret.result;
                        for(var i=0 ;i<result.length;i++){
                            if (result[i].id+"" == defValue) {
                            $("select[name='userName']").append('<option value="' + result[i].id + '" selected>' +result[i].companyName+'</option>');
                            } else {
                            $("select[name='userName']").append('<option value="' + result[i].id + '">' +result[i].companyName+'</option>');
                            }
                        }
                    }
                },
                error: function(e) {
                    layer.confirm("获取广告主信息失败", {
                        icon: 5,
                        btn: ['确定']
                    });
                }
            });
		}
	</script>
<@model.webend/>