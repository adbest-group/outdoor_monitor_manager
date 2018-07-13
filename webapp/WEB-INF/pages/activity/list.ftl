<#assign webTitle="活动管理" in model>
<#assign webHead in model>
</#assign>
<@model.webhead />
    <!-- 头部 -->
    <@model.webMenu current="活动管理" child="活动管理" />

	<!-- 特色内容 -->
<div class="main-container" style="height: auto;">
    <div class="main-box">
        <div class="title clearfix">
        	<a href="/customer/activity/edit" class="btn btn-red mr-10 ll">创建活动</a>
            <div class="search-box search-ll" style="margin: 0 0 0 20px">
                <form id="form" method="get" action="/activity/list">
                 <!--活动搜索框-->
                     <div class="inp">
                    	<input type="text" placeholder="请输入活动名称" value="${name?if_exists}" id="searchName" name="name">
                	</div>
                    <!--活动下拉框-->
                    <#-- <div class="select-box select-box-140 un-inp-select ll">
                        <select name="activityId" class="select" id="activityId">
                            <option value="">所有活动</option>
                        <@model.showAllActivityOps value="${bizObj.queryMap.activityId?if_exists}"/>
                        </select>
                    </div> -->
                    <#-- 
                    <div class="select-box select-box-140 un-inp-select ll">
                        <select name="userId" class="select" id="userId">
                            <option value="">所有广告主</option>
                        	<@model.showAllCustomerOps value="${bizObj.queryMap.userId?if_exists}"/>
                        </select>
                    </div>
                     -->
                    <div class="select-box select-box-100 un-inp-select ll">
                        <select class="select" name="status">
                        	 <#-- <option value="1" <#if (status?exists&&status == '1')>selected</#if>>未确认</option>
                        	<option value="2" <#if (status?exists&&status == '2')>selected</#if>>已确认</option>
                        	<option value="3" <#if (status?exists&&status == '3')>selected</#if>>已结束</option>-->
                        	 <option value="">所有状态</option>
                           <@model.showActivityStatusOps value="${bizObj.queryMap.status?if_exists}" /> 
                        </select>
                    </div>
                    <#-- 
                     <div class="select-box select-box-100 un-inp-select ll">
	                    <select style="width: 120px;height:31px;" name="mediaTypeParentId" id="mediaTypeParentId" onchange="changeMediaTypeId();">
	                    <option value="">所有媒体大类</option>
	                    <@model.showAllAdMediaTypeAvailableOps value="${bizObj.queryMap.mediaTypeParentId?if_exists}"/>
	                     </select>
                	</div>
                    <div class="select-box select-box-100 un-inp-select ll">
	                    <select style="width: 120px;height:31px;" name="mediaTypeId" id="mediaTypeId">
	                    	<option value="">所有媒体小类</option>
	                    </select>

	                </div><br/><br/>
                   -->
	                <#-- 城市 -->
	                <#-- 
					<div id="demo3" class="citys" style="float: left; font-size: 12px">
                        <p>
                                               城市： <select style="height: 30px" id="adSeatInfo-province" name="province">
                            <option value=""></option>
                        </select> <select style="height: 30px" id="adSeatInfo-city" name="city"></select>
    
                        </p>
                    </div>
                     -->
                     <#-- 
                    <div class="ll inputs-date"> -->
                        <#--<input class="ui-date-button" type="button" value="昨天" alt="-1" name="">-->
                        <#--<input class="ui-date-button" type="button" value="近7天" alt="-6" name="">-->
                        <#--<input class="ui-date-button on" type="button" value="近30天" alt="-29" name="">-->
                    <#-- 
                        <div class="date">
                            <input id="dts" class="Wdate" type="text" name="startDate" value="${bizObj.queryMap.startDate?if_exists}"> -
                            <input id="dt" class="Wdate" type="text" name="endDate" value="${bizObj.queryMap.endDate?if_exists}">
                        </div>
                    </div>
                     -->
                    <button type="button" class="btn btn-red" style="margin-left:10px;" autocomplete="off" id="searchBtn">查询</button>
                    <button type="button" class="btn btn-red" style="margin-left:10px;" id="assignBtn">批量确认</button>

                    <#-- 
                    <#if (status?exists&&status == '1')>
                    <a disable="disable" style="display: inline;						
						padding: 5px 7px;
						margin: 0 2px;
						color: #6b6b6b;
						text-decoration: none;
						background-color: #f9f9f9;
						border: 1px solid #c2c2c2;
					    border-top-color: rgb(194, 194, 194);
					    border-right-color: rgb(194, 194, 194);
					    border-bottom-color: rgb(194, 194, 194);
					    border-left-color: rgb(194, 194, 194);
						outline: none;
						cursor: pointer;
						border-radius: 3px;
						overflow: hidden;"> 剩余待审核活动总数${shenheCount?if_exists}条</a>
					</#if>
					 -->
                </form>
            </div>
        </div>

        <!-- 数据报表 -->
        <div class="data-report">
            <div class="bd">
                <table width="100%" cellpadding="0" cellspacing="0" border="0" class="tablesorter" id="plan">
                    <thead>
                    <tr>
                    	<th width="30"><input type="checkbox" style="visibility: hidden" id='thead-checkbox' name="ck-alltask" value=""/></th>  
                        <th>序号</th>
                        <th>活动名称</th>                       
                        <th>广告商</th> 
                        <th>投放周期</th>
                        <th>活动状态</th>                         
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#if (bizObj.list?exists && bizObj.list?size>0) >
                        <#list bizObj.list as activity>
                        <tr>
                        	<td width="30"><#if (activity.status?exists&&activity.status == 1)><input type="checkbox" data-status='${activity.status}'  name="ck-task" value="${activity.id}"/></#if></td> 
                            <td width="30">${(bizObj.page.currentPage-1)*20+activity_index+1}</td>
                            <td>
                                <div class="data-title w200" data-title="${activity.activityName!""}" data-id="${activity.id!""}">${activity.activityName?if_exists}</div>
                            </td>
                            <td>${activity.customerName?if_exists}</td>
                            <td>${activity.startTime?string('yyyy-MM-dd')} 至 ${activity.endTime?string('yyyy-MM-dd')}</td>
                            <td>${vm.getActivityStatusTextWithColor(activity.status)}</td>
                     
                            <td>
                                <#if activity.status==1><a href="javascript:queren('${activity.id}')">确认</a></#if>
                                <#-- <#if activity.status==1><a href="javascript:cancel('${activity.id}')">撤销</a></#if> -->
                                <#if activity.status gt 0 ><a href="/activity/edit?id=${activity.id}">详情</a></#if>  
                              	<#if activity.status==1><a href="javascript:del('${activity.id}')">删除</a></#if>
                                <#-- <#if activity.status!=1&&activity.status!=4><a id="exportExcel" href="javascript:exportExcel('${activity.id}')">导出excel</a></#if>
                                <#if activity.status!=1&&activity.status!=4><a id="exportPdf" href="javascript:exportPdf('${activity.id}')">导出pdf</a></#if> -->
                                <#if activity.status!=1&&activity.status!=4><a id="openActivityExcel" href="javascript:openActivityExcel('${activity.id}')">导出excel</a></#if>
                                <#if activity.status!=1&&activity.status!=4><a id="openActivityPdf" href="javascript:openActivityPdf('${activity.id}')">导出pdf</a></#if>
                            </td>
                        </tr>
                        </#list>
                    <#else>
                    <tr><td colspan="20">没有相应结果。</td> </tr>
                    </#if>
                    </tbody>
                    <!-- 翻页 -->
                <@model.showPage url=vm.getUrlByRemoveKey(thisUrl, ["start", "size"]) p=bizObj.page parEnd="" colsnum=9 />
                </table>
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
<script type="text/javascript" src="/static/js/jquery.citys.js"></script>
<!-- 时期 -->
<link href="${model.static_domain}/js/date/daterangepicker.css" rel="stylesheet">
<script type="text/javascript" src="${model.static_domain}/js/date/moment.min.js"></script>
<script type="text/javascript" src="${model.static_domain}/js/date/jquery.daterangepicker.js"></script>
<script type="text/javascript" src="${model.static_domain}/js/date.js"></script>

<script type="text/javascript">
<#-- 
changeMediaTypeId();
function changeMediaTypeId() {	
		var mediaTypeParentId = $("#mediaTypeParentId").val();
		if(mediaTypeParentId == "" || mediaTypeParentId.length <= 0) {
			var option = '<option value="">请选择媒体小类</option>';
			$("#mediaTypeId").html(option);
			return ;
		}
		$.ajax({
			url : '/platmedia/adseat/searchMediaType',
			type : 'POST',
			data : {"parentId":mediaTypeParentId},
			dataType : "json",
			traditional : true,
			success : function(data) {
				var result = data.ret;
				if (result.code == 100) {
					var adMediaTypes = result.result;
					var htmlOption = '<option value="">请选择媒体小类</option>';
					for (var i=0; i < adMediaTypes.length;i++) { 
						var type = adMediaTypes[i];
						htmlOption = htmlOption + '<option value="' + type.id + '">' + type.name + '</option>';
					}
					$("#mediaTypeId").html(htmlOption);
					$("#mediaTypeId").val(${mediaTypeId?if_exists});
				} else {
					alert('修改失败!');
				}
			}
		});
	}
	 -->
    $(function(){
     	$(".nav-sidebar>ul>li").on("click",function(){
                    $(".nav-sidebar>ul>li").removeClass("on");
                    $(this).addClass("on");
                });
        $(window).resize();
        $('.select').searchableSelect();

        /* $('.inputs-date').dateRangePicker({
            separator : ' 至 ',
            showShortcuts:false,
            getValue: function()
            {
                if ($('#dts').val() && $('#dt').val() )
                    return $('#dts').val() + ' 至 ' + $('#dt').val();
                else
                    return '';
            },
            setValue: function(s,s1,s2)
            {
                $('#dts').val(s1);
                $('#dt').val(s2);

                var dateArr=[-1,-6,-29]
                $(".ui-date-button").removeClass("on");
                dateArr.forEach(function(v,i){
                    if(s==createDateStr(v)){
                        $(".ui-date-button").eq(i).addClass("on");
                    }
                })

            }
        }); */
        
        // 如果列表中有未确认的状态就显示表头的多选框
        $("input[name='ck-task']").each(function() {
        	if($(this).data('status') === 1){
        		$('#thead-checkbox').css('visibility', 'visible')
        		return false;
        	}
        })
     
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
        onChange : function(info) {
            townFormat(info);
        }
    }, function(api) {
        var info = api.getInfo();
        townFormat(info);
    });
        // 查询
    	<#-- $("#searchBtn").on("click", function () {
      	  var strParam = "";
      	  var name = $("#searchName").val();
        
      	  if (name != null && $.trim(name).length) {
      	      strParam = strParam + "?name=" + name;
      	  }
	
      	  window.location.href = "/activity/list" + strParam;
   		 }); -->
        //批量确认活动
        $("#assignBtn").click(function(){
        	var id_sel;
            if($("input[name='ck-task']:checked").length<1){
                layer.confirm('请选择需要确认的活动', {
                    icon: 0,
                    btn: ['确定'] //按钮
                });
            }else{
                var ids = [];
                $("input[name='ck-task']:checked").each(function(i,ck){
                    if(ck.value) ids.push(ck.value);
                });
                id_sel = ids.join(",");
	             $.ajax({
			            url: "/activity/confirm",
			            type: "post",
			            data: {
			                "ids": id_sel
			            },
			            cache: false,
			            dataType: "json",
			            success: function(datas) {
			                var resultRet = datas.ret;
			                if (resultRet.code == 101) {
			                    layer.confirm(resultRet.resultDes, {
			                        icon: 2,
			                        btn: ['确定'] //按钮
			                    }, function(){
			                        window.location.reload();
			                    });
			                } else {
			                    layer.confirm("确认成功", {
			                        icon: 1,
			                        btn: ['确定'] //按钮
			                    },function () {
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
            }
        });
        $("input[name='ck-alltask']").change(function(){
            if(!this.value){
                if($(this).is(":checked")){
                    $("input[name='ck-task']").prop("checked",true)
                }else{
                    $("input[name='ck-task']").removeAttr("checked");
                }
            }
        });
    });

    $(window).resize(function() {
        var h = $(document.body).height() - 115;
        $('.main-container').css('height', h);
    });

	function createDateStr(alt){
	    var today =  new Date();
	    var t=today.getTime()+1000*60*60*24*alt;
	    var newDate=new Date(t).Format("yyyy-MM-dd");
	    if(alt==-6||alt==-29)
	        return newDate+" 至 "+today.Format("yyyy-MM-dd");
	    return newDate+" 至 "+newDate;
	}
	

//        var newDate=new Date().Format("yyyy-MM-dd");
//        $('.inputs-date').data('dateRangePicker').setDateRange(newDate,newDate, true);

   

    $("#searchBtn").on("click",function() {
        $("#form").submit();
    });

    //活动确认
    queren = function(activityId){
	    layer.confirm("确认该活动将生成对应的的监测任务", {
	        icon: 3,
	        btn: ['确定', '取消'] //按钮
	    }, function(){
	        $.ajax({
	            url: "/activity/confirm",
	            type: "post",
	            data: {
	                "ids": activityId
	            },
	            cache: false,
	            dataType: "json",
	            success: function(datas) {
	                var resultRet = datas.ret;
	                if (resultRet.code == 101) {
	                    layer.confirm(resultRet.resultDes, {
	                        icon: 2,
	                        btn: ['确定'] //按钮
	                    }, function(){
	                        window.location.reload();
	                    });
	                } else {
	                    layer.confirm("确认成功", {
	                        icon: 1,
	                        btn: ['确定'] //按钮
	                    }, function(){
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
    }
    
    //活动撤销
    cancel = function(id){
        layer.confirm("确定撤销该活动？", {
            icon: 3,
            btn: ['确定', '取消'] //按钮
        }, function(){
            $.ajax({
                url: "/activity/cancel",
                type: "post",
                data: {
                    "id": id
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
                        layer.confirm("撤销成功", {
                            icon: 1,
                            btn: ['确定'] //按钮
                        }, function(){
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
    }
    
  	//活动删除
    del = function(activityId){
        layer.confirm("确定删除该活动？", {
            icon: 3,
            btn: ['确定', '取消'] //按钮
        }, function(){
            $.ajax({
                url: "/activity/delete",
                type: "post",
                data: {
                    "id": activityId
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
                        }, function(){
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
    }

    // 删除
    function deleteAccount(id){
        layer.confirm("确定要删除该账户", {
            icon: 3,
            btn: ['确定', '取消'] //按钮
        }, function(){
            $.ajax({
                url: "/system/deleteAccount",
                type: "post",
                data: {
                    "id": id
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
                        }, function(){
                            window.location.href = "/system/account/list?nameOrUsername=" + $("#nameOrUsername").val();
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
    }

    // 更新账户状态
    function updStatus(id, status) {
        if (status == "0") {
            layer.confirm("确定要停用该账户", {
                icon: 3,
                btn: ['确定', '取消'] //按钮
            }, function(){
                doUpdate(id, status);
            });
        } else {
            doUpdate(id, status);
        }
    }
    
      function openActivityExcel(activityId){
    	layer.open({
            type: 2,
            title: '导出excel报表',
            shade: 0.8,
            area: ['600px', '420px'],
            content: '/activity/selectTaskToExcel?activityId=' + activityId //iframe的url
        });
    }
    
    function openActivityPdf(activityId){
    	layer.open({
            type: 2,
            title: '导出pdf报表',
            shade: 0.8,
            area: ['600px', '420px'],
            content: '/activity/selectTasksToPdf?activityId=' + activityId //iframe的url
        });
    }
    
    function exportPdf(activityId) {
    	$.ajax({
            url: "/excel/exportAdMediaPdf",
            type: "post",
            data: {
                "activityId": activityId
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
                    layer.alert('导出成功', {icon: 1, closeBtn: 0, btn: [], title: false, time: 3000});
		    		//window.open(resultRet.result);
		    		var newA = document.createElement("a");
			        newA.id = 'gg'
			        newA.target = '_blank';
			        newA.href = resultRet.result;
			        document.body.appendChild(newA);
			        newA.click();
			        document.body.removeChild(newA);
                }
            },
            error: function(e) {
                layer.confirm("服务忙，请稍后再试", {
                    icon: 5,
                    btn: ['确定'] //按钮
                });
            }
        });
    }
    
    function exportExcel(activityId) {
		$.ajax({
            url: "/excel/exportAdMediaInfo",
            type: "post",
            data: {
                "activityId": activityId
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
                    layer.alert('导出成功', {icon: 1, closeBtn: 0, btn: [], title: false, time: 3000});
		    		//window.open(resultRet.result);
		    		var newA = document.createElement("a");
			        newA.id = 'gg'
			        newA.target = '_blank';
			        newA.href = resultRet.result;
			        document.body.appendChild(newA);
			        newA.click();
			        document.body.removeChild(newA);
                }
            },
            error: function(e) {
                layer.confirm("服务忙，请稍后再试", {
                    icon: 5,
                    btn: ['确定'] //按钮
                });
            }
        });
	}

</script>
<!-- 特色内容 -->

<@model.webend />