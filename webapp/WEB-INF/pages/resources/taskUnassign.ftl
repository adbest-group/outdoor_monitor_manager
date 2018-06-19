<#assign webTitle="任务管理-任务指派" in model>
<#assign webHead in model>
</#assign>
<@model.webhead />
    <!-- 头部 -->
    <@model.webMenu current="任务管理" child="任务指派" />

	<!-- 特色内容 -->
<div class="main-container" style="height: auto;">
    <div class="main-box">
        <div class="title clearfix" style="display:block;">
            <div class="search-box search-ll" style="margin: 0 0 0 20px">
                <form id="form" method="get" action="/sysResources/taskUnassign">
                	<div class="inp">
                    	<input type="text" placeholder="请输入活动名称" value="${name?if_exists}" id="searchName" name="name">
                	</div>
                    <#-- <!--活动下拉框
                    <div class="select-box select-box-140 un-inp-select ll">
                        <select name="activityId" class="select" id="activityId">
                            <option value="">所有活动</option>
                        <@model.showAllActivityOps value="${bizObj.queryMap.activityId?if_exists}"/>
                        </select>
                    </div>
                     -->
                    <!--任务状态下拉框-->
                    <div class="select-box select-box-140 un-inp-select ll">
                        <select name="status" class="select" id="status">
                        	<option value="">所有任务状态</option>
                            <option value="1" <#if (bizObj.queryMap.status?exists&&bizObj.queryMap.status=="1")>selected</#if> >待指派</option>
                            <option value="8" <#if (bizObj.queryMap.status?exists&&bizObj.queryMap.status=="8")>selected</#if> >可抢单</option>
                        </select>
                    </div>
                    <div class="select-box select-box-100 un-inp-select ll">
                       	 <select class="select" name="mediaId" onchange="importEnabled()" id="selectMediaId">
                        <option value="">所有媒体</option> <@model.showAllMediaOps
                    value="${bizObj.queryMap.mediaId?if_exists}" />
                    </select>
                    </div>
	               
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
	                </div>
	                <#-- 城市 -->
					<div id="demo3" class="citys" style="float: left; font-size: 12px">
                        <p>
                                               城市： <select style="height: 30px" id="adSeatInfo-province" name="province">
                            <option value=""></option>
                        </select> <select style="height: 30px" id="adSeatInfo-city" name="city"></select>
    
                        </p>
                    </div>
                    <#-- <div class="ll inputs-date"> -->
                        <#--<input class="ui-date-button" type="button" value="昨天" alt="-1" name="">-->
                        <#--<input class="ui-date-button" type="button" value="近7天" alt="-6" name="">-->
                        <#--<input class="ui-date-button on" type="button" value="近30天" alt="-29" name="">-->
                        <#-- <div class="date">
                            <input id="dts" class="Wdate" type="text" name="startDate" value="${bizObj.queryMap.startDate?if_exists}"> -
                            <input id="dt" class="Wdate" type="text" name="endDate" value="${bizObj.queryMap.endDate?if_exists}">
                        </div>
                    </div> -->
                    <button type="button" class="btn btn-red" style="margin-left:10px;" id="searchBtn">查询</button>
                     <button type="button" class="btn btn-red" style="margin-left:10px;" id="assignBtn">批量指派</button> 
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
                        <th width="30">序号</th>
                        <th>活动名称</th>
                        <th>上刊示例</th>
                        <th>投放周期</th>
                        <th>地区</th>
                        <th>媒体</th>
                        <th>广告位</th>
                        <#--<th>执行人员</th>-->
                        <th>监测时间点</th>
                        <th>状态</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#if (bizObj.list?exists && bizObj.list?size>0) >
                        <#list bizObj.list as task>
                        <tr>
                        	<td width="30"><#if (task.status?exists&&task.status == 1)><input type="checkbox" data-status='${task.status}'  name="ck-task" value="${task.id}"/><div style='display:none;' data-id='${task.mediaId}'></div></#if></td>
                            <td width="30">${(bizObj.page.currentPage-1)*20+task_index+1}</td>
                            <td>
                                <div class="data-title w200" data-title="${task.activityName!""}" data-id="${task.id}">${task.activityName?if_exists}</div>
                            </td>
                            <td><img width="50" src="${task.samplePicUrl!""}"/> </td>
                            <td>${task.startTime?string('yyyy-MM-dd')}<br/>${task.endTime?string('yyyy-MM-dd')}</td>
                           <td>${vm.getCityName(task.province)!""} ${vm.getCityName(task.city!"")}</td>
                            <td  id="media_${task.id}">${task.mediaName!""}</td>
                            <td>${task.adSeatName!""}</td>
                            <#--<td>${task.userId!""}</td>-->
                            <td>${vm.getMonitorTaskTypeText(task.taskType)!""}</td>
                            <td>${vm.getMonitorTaskStatusText(task.status)!""}</td>
                            <td>
                            	<#if vm.getUnassignTask(task.endTime)&lt;0><#if (task.status==1 || task.status==8)><a href="javascript:assign('${task.id}',${task.mediaId})">指派</a></#if></#if>
                                <a href="/task/details?task_Id=${task.id}">详情</a>
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
	changeMediaTypeId();
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
    $("#searchBtn").on("click", function () {
        var strParam = "";
        var name = $("#searchName").val();
        
        if (name != null && $.trim(name).length) {
            strParam = strParam + "?name=" + name;
        }

        window.location.href = "/sysResources/taskUnassign" + strParam;
    });
    function importEnabled(){
    	var mediaId = $('#selectMediaId').val();
    	if(mediaId){
    		$('#insertBatchId').removeAttr("disabled");
    	} else {
    		$('#insertBatchId').attr("disabled","disabled");
    	}
    }
            function createDateStr(alt){
                var today =  new Date();
                var t=today.getTime()+1000*60*60*24*alt;
                var newDate=new Date(t).Format("yyyy-MM-dd");
                if(alt==-6||alt==-29)
                    return newDate+" 至 "+today.Format("yyyy-MM-dd");
                return newDate+" 至 "+newDate;
            }
    var assign_ids;
    $(function(){
        $('.select').searchableSelect();
	<#-- 
        $('.inputs-date').dateRangePicker({
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
        }); -->
        
       // 如果列表中有未确认的状态就显示表头的多选框
        $("input[name='ck-task']").each(function() {
        	if($(this).data('status') === 1){
        		$('#thead-checkbox').css('visibility', 'visible')
        		return false;
        	}
        })
		
		//批量指派
        $("#assignBtn").click(function(){
            if($("input[name='ck-task']:checked").length<1){
                layer.confirm('请选择需要指派的任务', {
                    icon: 0,
                    btn: ['确定'] //按钮
                });
            }else{
                var ids = [];
                var preMediaTxt;
                var mediaTxt;
                var flag=true;
                var mediaId;
                
                //添加校验，如果批量选择中媒体类型不一致，提醒用户选择相同的媒体类型
                $("input[name='ck-task']:checked").each(function(i,ck){
                	if(ck.value){
                		if(!preMediaTxt){
                			preMediaTxt=$(ck).parent().siblings("#media_"+ck.value).text();
	                	}
	            		mediaTxt=$(ck).parent().siblings("#media_"+ck.value).text();
	            		if(preMediaTxt!=mediaTxt){
	            			flag=false;
	            			layer.confirm("请选择同一家媒体的指派任务", {
		                        icon: 2,
		                        btn: ['确定'] //按钮
		                    }, function(){
		                        window.location.reload();
		                    });
	            		}
	                    ids.push(ck.value);
	                    //传到前台媒体id
	                    mediaId = $(ck).next().data('id');
                	}
                });
                if(flag){
                	assign_ids = ids.join(",");
                	openSelect(mediaId);
                }
            }
        });
		
        $("input[name='ck-alltask']").change(function(){
            if(this.value){

            }else{
                if($(this).is(":checked")){
                    $("input[name='ck-task']").prop("checked",true)
                }else{
                    $("input[name='ck-task']").removeAttr("checked");
                }
            }
        });
    });

    $("#searchBtn").on("click",function() {
        $("#form").submit();
    });

    //指派
    assign = function (id,mediaId) {
        assign_ids = id;
        openSelect(mediaId);
    }

    //打开选择执行者
    openSelect = function(mediaId) {
        layer.open({
            type: 2,
            title: '选择监测人员',
            shade: 0.8,
            area: ['600px', '420px'],
            content: '/task/selectUserExecute?mediaId=' + mediaId //iframe的url
        });
    }
    //选择执行人后的回调
    selectUserExecuteHandle = function (userId) {
        $.ajax({
            url: "/task/assign",
            type: "post",
            data: {
                "ids": assign_ids,
                "userId":userId
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
                    layer.confirm("指派成功", {
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


</script>
<!-- 特色内容 -->

<@model.webend />