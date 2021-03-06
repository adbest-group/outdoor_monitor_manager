<#assign webTitle="任务管理" in model>
<#assign webHead in model>
</#assign>
<@model.webhead />
<!-- 头部 -->
<@model.webMenu current="任务管理" child="监测任务审核" />

<!-- 特色内容 -->
<div class="main-container" style="height: auto;">
    <div class="main-box">
        <div class="title clearfix" style="display:block;">
            <div class="search-box search-ll" style="margin: 0 0 0 20px">
                <form id="form" method="get" action="/task/list">
                 	 <!--活动搜索框-->
                     <div class="inp">
                    	<input type="text" placeholder="请输入活动名称" value="${name?if_exists}" id="searchName" name="name">
                	</div>
                    <!--任务下拉框-->
                   <#--  <div class="select-box select-box-140 un-inp-select ll">
                    
                        <select name="activityId" class="select" id="activityId">
                            <option value="">所有活动</option>
                        <@model.showAllActivityOps value="${bizObj.queryMap.activityId?if_exists}"/>
                        </select>
                    </div> -->
                    <div class="select-box select-box-100 un-inp-select ll">
                        <select class="select" name="taskType">
                            <option value="">任务类型</option>
                            <#-- 
                            <option value="1">上刊监测</option>
                            <option value="2">投放期间监测</option>
                            <option value="3">下刊监测</option>
                             -->
                        	<@model.showMonitorTaskTypeOps value="${bizObj.queryMap.taskType?if_exists}" />
                        </select>
                    </div>
                    <div class="select-box select-box-100 un-inp-select ll">
                        <select class="select" name="status">
                        	<#-- <option value="">所有任务状态</option> -->
                        	<#-- <@model.showMonitorTaskStatusOps value="${bizObj.queryMap.status?if_exists}"/> -->
                        	<option value="3" <#if (status?exists&&status == 3)>selected</#if>>待审核</option>
                        	<option value="4" <#if (status?exists&&status == 4)>selected</#if>>审核通过</option>
                        	<option value="5" <#if (status?exists&&status == 5)>selected</#if>>审核未通过</option>
                        	<option value="1" <#if (status?exists&&status == 1)>selected</#if>>待指派</option>
                        	<option value="8" <#if (status?exists&&status == 8)>selected</#if>>可抢单</option>
                        	<option value="2" <#if (status?exists&&status == 2)>selected</#if>>待执行</option>
                        	<option value="6" <#if (status?exists&&status == 6)>selected</#if>>未完成</option>
                        	<option value="7" <#if (status?exists&&status == 7)>selected</#if>>待激活</option>
                        </select>
                    </div>
                    <div class="select-box select-box-100 un-inp-select ll">
                        <select class="select" name="problemStatus">
                            <option value="">所有问题状态</option>
                        <@model.showProblemStatusList value="${bizObj.queryMap.problemStatus?if_exists}" />
                        </select>
                    </div><br/><br/><br/>
                    <div class="select-box select-box-100 un-inp-select ll">
	                    <select style="width: 120px;height:31px;" name="mediaTypeParentId" id="mediaTypeParentId" onchange="changeMediaTypeId();">
	                    <option value="">所有媒体大类</option>
	                    <@model.showAllAdMediaTypeAvailableOps value="${bizObj.queryMap.mediaTypeParentId?if_exists}"/>
	                     </select>
                	</div>
                    <div class="select-box select-box-100 un-inp-select ll" id="mediaTypeSelect">
	                    <select style="width: 120px;height:31px;display: none" name="mediaTypeId" id="mediaTypeId">
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
                    
                    <#-- 
                    <div class="ll inputs-date"> -->
                    <#--<input class="ui-date-button" type="button" value="昨天" alt="-1" name="">-->
                    <#--<input class="ui-date-button" type="button" value="近7天" alt="-6" name="">-->
                    <#--<input class="ui-date-button on" type="button" value="近30天" alt="-29" name="">-->
                    <#-- 
                        <div class="date">
                            <input id="dts" class="Wdate" type="text" name="startDate"
                                   value="${bizObj.queryMap.startDate?if_exists}"> -
                            <input id="dt" class="Wdate" type="text" name="endDate"
                                   value="${bizObj.queryMap.endDate?if_exists}">
                        </div>
                    </div>
                     -->
                    <button type="button" class="btn btn-red" style="margin-left:10px;" autocomplete="off"
                            id="searchBtn">查询
                    </button>
                    <button type="button" class="btn btn-red" style="margin-left:10px;" id="assignBtn">批量通过</button>
                    <button type="button" class="btn btn-red" style="margin-left:10px;" id="batchRefuse">批量拒绝</button>
                    <#-- 
                    <#if (status?exists&&status == '3')>
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
						overflow: hidden;"> 剩余待审核总数${shenheCount?if_exists}条</a>
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
                        <th width="30">序号</th>
                        <th>活动名称</th>
                        <th>上刊示例</th>
                        <th>投放周期</th>
                        <th>地区</th>
                        <th>媒体</th>
                        <th>媒体大类</th>
					    <th>媒体小类</th>
                        <th>广告位</th>
                        <th>执行人员</th>
                        <th>任务类型</th>
                        <th>状态</th>
                        <th>问题状态</th>
                        <th>审核人</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#if (bizObj.list?exists && bizObj.list?size>0) >
                        <#list bizObj.list as task>
                        <tr>
                         	<td width="30"><#if (task.status?exists&&task.status == 3)><input data-status='${task.status}' type="checkbox"  name="ck-task" value="${task.id}"/></#if></td>  
                            <td width="30">${(bizObj.page.currentPage-1)*20+task_index+1}</td>
                            <td>
                                <div class="data-title w200" data-title="${task.activityName}"
                                     data-id="${task.id}">${task.activityName?if_exists}</div>
                            </td>
                            <td><img width="50" src="${task.samplePicUrl!""}"/></td>
                            <td>${task.startTime?string('yyyy-MM-dd')}<br/>${task.endTime?string('yyyy-MM-dd')}</td>
                            <td>${vm.getCityName(task.province)!""} ${vm.getCityName(task.city!"")}</td>
                            <td>${task.mediaName!""}</td>
                            <td>${task.parentName!""}</td>
                            <td>${task.secondName!""}</td>
                            <td>${task.adSeatName!""}</td>
                            <td>${task.realname!""}</td>
                            <td>${vm.getMonitorTaskTypeText(task.taskType)!""}</td>
                            <td>${vm.getMonitorTaskStatusText(task.status)!""}</td>
                            <td>${vm.getProblemStatusText(task.problemStatus!0)}</td>
                            <td>${task.assessorName!""}</td>
                            <td>
                            <#--<#if task.status==1><a href="javascript:assign('${task.id}')">指派</a></#if>-->
                            <#--<#if task.status==2><a href="javascript:assign('${task.id}')">重新指派</a></#if>-->
                                <#if (task.status==4&&task.problemStatus?exists&&task.problemStatus==4&&(!task.subCreated?exists||task.subCreated==2))>
                                    <a href="javascript:createTask('${task.id}');">创建复查</a></#if>
                                <#-- 
                                <#if (task.parentId?exists&&task.parentType=1)>
                                    <a href="/task/list?pid=${task.parentId}&ptype=1">复查配对</a></#if> -->
                                <#if (task.parentId?exists&&task.parentType=2)>
                                    <a href="/jiucuo/list?id=${task.parentId}">查看纠错</a></#if>
                                <#-- 
                                <#if (task.status==4&&task.problemStatus?exists&&task.problemStatus==4&&task.subCreated?exists&&task.subCreated==1)>
                                    <a href="/task/list?pid=${task.id}&ptype=1">复查配对</a></#if> -->
                                <#-- 
                                <#if (task.status==4 && task.problemStatus?exists&&task.problemStatus==4)><a
                                        href="javascript:close('${task.id}')">关闭</a></#if> -->
                                <#if task.status==3><a href="javascript:pass('${task.id}')">通过</a></#if>
                                <#if task.status==3><a href="javascript:reject('${task.id}')">拒绝</a></#if>
                                <a href="/task/details?task_Id=${task.id}">详情</a>
                                <#-- <#if task.status==3><a href="javascript:cancelSh('${task.id}')">撤消</a></#if> -->
                            <#--<#if task.status==1><a href="javascript:del('${task.id}')">删除</a></#if>-->
                            </td>
                        </tr>
                        </#list>
                    <#else>
                    <tr>
                        <td colspan="20">没有相应结果。</td>
                    </tr>
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
	$('#mediaTypeParentId').searchableSelect({
		afterSelectItem: function(){
			console.log(this.holder.data("value"), this.holder.text())
			if(this.holder.data("value")){
				
				changeMediaTypeId(this.holder.data("value"))
				$('#mediaTypeId').css('display', 'inline-block')
			}else{
				$('#mediaTypeId').parent().html('<select style="width: 120px;height:31px;display:none" name="mediaTypeId" id="mediaTypeId"><option value="">请选择媒体小类</option></select>')
			}
		}
	})
	
	$('#mediaTypeParentId').next().find('.searchable-select-input').css('display', 'block')

    var assign_ids;
    $(function () {
    
    	$(window).resize();
        $(".nav-sidebar>ul>li").on("click", function () {
            $(".nav-sidebar>ul>li").removeClass("on");
            $(this).addClass("on");
        });
        $('.select').searchableSelect();

		<#-- 
        $('.inputs-date').dateRangePicker({
            separator: ' 至 ',
            showShortcuts: false,
            getValue: function () {
                if ($('#dts').val() && $('#dt').val())
                    return $('#dts').val() + ' 至 ' + $('#dt').val();
                else
                    return '';
            },
            setValue: function (s, s1, s2) {
                $('#dts').val(s1);
                $('#dt').val(s2);

                var dateArr = [-1, -6, -29]
                $(".ui-date-button").removeClass("on");
                dateArr.forEach(function (v, i) {
                    if (s == createDateStr(v)) {
                        $(".ui-date-button").eq(i).addClass("on");
                    }
                })

            }
        });
         -->
       
      // 如果列表中有未确认的状态就显示表头的多选框
        $("input[name='ck-task']").each(function() {
        	if($(this).data('status') === 3){
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
                url : '/api/city?provinceId=' + info['code'],
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
   var currentCity = ""
	<#if city?exists && city != ""> currentCity = ${city!""} </#if>
	var currentProvince = ""
	<#if province?exists && province != ""> currentProvince = ${province!""} </#if>
    $('#demo3').citys({
        required:false,
        province : '${province!"所有城市"}',
        city : '${city!""}',
        onChange : function(info) {
            townFormat(info);
            var str = '110000,120000,310000,500000,810000,820000'
            if(str.indexOf(info.code) === -1){
            	$('#adSeatInfo-city').val(currentCity)
	            $('#adSeatInfo-city').searchableSelect()
	            $('#adSeatInfo-city').next().css('width', '130px')
            }
        }
    }, function(api) {
        var info = api.getInfo();
        townFormat(info);
        $('#adSeatInfo-province').val(currentProvince)
        $('#adSeatInfo-province').searchableSelect({
			afterSelectItem: function(){
				
				$('#adSeatInfo-city').next().remove()
				if(this.holder.data("value")){
					$('#adSeatInfo-province').val(this.holder.data("value")).trigger("change");
					currentCity = ""
				}
			}
		})
        $('#adSeatInfo-province').next().css('width', '130px')
    });
    	<#-- 
        // 查询
    	$("#searchBtn").on("click", function () {
        	var strParam = "";
        	var name = $("#searchName").val();
        
        	if (name != null && $.trim(name).length) {
         	   strParam = strParam + "?name=" + name;
        	}

        	window.location.href = "/task/list" + strParam;
    	});
    	 -->
        //批量审核任务
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
			            url: "/task/verify",
			            type: "post",
			            data: {
			                "ids": id_sel,
			                "status": 4
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
        
        //批量拒绝通过任务
        $("#batchRefuse").click(function(){
        	var id_sel;
            if($("input[name='ck-task']:checked").length<1){
                layer.confirm('请选择需要拒绝的活动', {
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
			            url: "/task/verify",
			            type: "post",
			            data: {
			                "ids": id_sel,
			                "status": 5
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
			                    layer.confirm("拒绝成功", {
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


    $(window).resize(function () {
        var h = $(document.body).height() - 115;
        $('.main-container').css('height', h);
    });

    function createDateStr(alt) {
        var today = new Date();
        var t = today.getTime() + 1000 * 60 * 60 * 24 * alt;
        var newDate = new Date(t).Format("yyyy-MM-dd");
        if (alt == -6 || alt == -29)
            return newDate + " 至 " + today.Format("yyyy-MM-dd");
        return newDate + " 至 " + newDate;
    }

        
//        var newDate=new Date().Format("yyyy-MM-dd");
//        $('.inputs-date').data('dateRangePicker').setDateRange(newDate,newDate, true);

    $("#searchBtn").on("click", function () {
        $("#form").submit();
    });

    //指派
    assign = function (id) {
        assign_ids = id;
        openSelect();
    }

    //打开选择执行者
    openSelect = function () {
        layer.open({
            type: 2,
            title: '选择监测人员',
            shade: 0.8,
            area: ['400px', '220px'],
            content: '/task/selectUserExecute' //iframe的url
        });
    }
    //选择执行人后的回调
    selectUserExecuteHandle = function (userId) {
        layer.closeAll();
        if (!userId) {
            layer.alert("并没有指定执行人员");
            return;
        }

        $.ajax({
            url: "/task/assign",
            type: "post",
            data: {
                "ids": assign_ids,
                "userId": userId
            },
            cache: false,
            dataType: "json",
            success: function (datas) {
                var resultRet = datas.ret;
                if (resultRet.code == 101) {
                    layer.confirm(resultRet.resultDes, {
                        icon: 2,
                        btn: ['确定'] //按钮
                    });
                } else {
                    layer.confirm("指派成功", {
                        icon: 1,
                        btn: ['确定'] //按钮
                    }, function () {
                        window.location.reload();
                    });
                }
            },
            error: function (e) {
                layer.confirm("服务忙，请稍后再试", {
                    icon: 5,
                    btn: ['确定'] //按钮
                });
            }
        });
    }

    //审核通过
    pass = function (id) {
        layer.confirm("确认审核通过？", {
            icon: 3,
            btn: ['确定', '取消'] //按钮
        }, function () {
            verify(id, 4);
        });
    }

    //审核不通过
    reject = function (id) {
        layer.confirm("确认审核不通过？", {
            icon: 3,
            btn: ['确定', '取消'] //按钮
        }, function (index) {
            layer.close(index);
            layer.prompt({title: '请填写审核意见', formType: 2}, function (val, index) {
                if (val.trim().length < 1 || val.trim().length > 33) {
                    layer.alert("请填写30字以内！", {icon: 2});
                    return;
                }
                layer.close(index);
                verify(id, 5, val);
            });

        });
    }
    //发起审核请求
    verify = function (id, status, reason) {
        $.ajax({
            url: "/task/verify",
            type: "post",
            data: {
                "id": id,
                "status": status,
                "reason": reason
            },
            cache: false,
            dataType: "json",
            success: function (datas) {
                var resultRet = datas.ret;
                if (resultRet.code == 101) {
                    layer.confirm(resultRet.resultDes, {
                        icon: 2,
                        btn: ['确定'] //按钮
                    }, function(){
                        window.location.reload();
                    });
                } else {
                    layer.confirm("审核成功", {
                        icon: 1,
                        btn: ['确定'] //按钮
                    }, function () {
                        window.location.reload();
                    });
                }
            },
            error: function (e) {
                layer.confirm("服务忙，请稍后再试", {
                    icon: 5,
                    btn: ['确定'] //按钮
                });
            }
        });
    }
     //撤消审核任务
     cancelSh = function(id){
        layer.confirm("确定撤销该审核？", {
            icon: 3,
            btn: ['确定', '取消'] //按钮
        }, function(){
         	cancel(id);
         	});
      }
    //发起撤消请求
    cancel = function (id,reason) {
        $.ajax({
            url: "/task/cancel",
            type: "post",
            data: {
                "id": id,
                "reason": reason
            },
            cache: false,
            dataType: "json",
            success: function (datas) {
                var resultRet = datas.ret;
                if (resultRet.code == 101) {
                    layer.confirm(resultRet.resultDes, {
                        icon: 2,
                        btn: ['确定'] //按钮
                    });
                } else {
                    layer.confirm("撤消成功", {
                        icon: 1,
                        btn: ['确定'] //按钮
                    }, function () {
                        window.location.reload();
                    });
                }
            },
            error: function (e) {
                layer.confirm("服务忙，请稍后再试", {
                    icon: 5,
                    btn: ['确定'] //按钮
                });
            }
        });
    }
    //处理问题
    close = function (id) {
        layer.confirm("确认关闭问题任务？", {
            icon: 3,
            btn: ['确定', '取消'] //按钮
        }, function () {
            $.ajax({
                url: "/task/close",
                type: "post",
                data: {
                    "id": id
                },
                cache: false,
                dataType: "json",
                success: function (datas) {
                    var resultRet = datas.ret;
                    if (resultRet.code == 101) {
                        layer.confirm(resultRet.resultDes, {
                            icon: 2,
                            btn: ['确定'] //按钮
                        });
                    } else {
                        layer.confirm("关闭成功", {
                            icon: 1,
                            btn: ['确定'] //按钮
                        }, function () {
                            window.location.reload();
                        });
                    }
                },
                error: function (e) {
                    layer.confirm("服务忙，请稍后再试", {
                        icon: 5,
                        btn: ['确定'] //按钮
                    });
                }
            });
        });
    }

	function changeMediaTypeId(mediaTypeParentId) {	
		// var mediaTypeParentId = $("#mediaTypeParentId").val();
		if(!mediaTypeParentId) {
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
				var mediaTypeIdSelect = ""
				<#if mediaTypeId?exists && mediaTypeId != ""> mediaTypeIdSelect = ${mediaTypeId!""} </#if>
				var isSelect = false;
				var result = data.ret;
				if (result.code == 100) {
					var adMediaTypes = result.result;
					var htmlOption = '<select style="width: 120px;height:31px;" name="mediaTypeId" id="mediaTypeId"><option value="">请选择媒体小类</option>';
					for (var i=0; i < adMediaTypes.length;i++) { 
						var type = adMediaTypes[i];
						htmlOption = htmlOption + '<option value="' + type.id + '">' + type.name + '</option>';
						if(mediaTypeIdSelect === type.id){
							isSelect = true
						}
					}
					htmlOption += '</select>'
					$("#mediaTypeSelect").html(htmlOption);
					$("#mediaTypeId").val(isSelect ? mediaTypeIdSelect : "");
					$("#mediaTypeId").searchableSelect()
					$('#mediaTypeId').next().find('.searchable-select-input').css('display', 'block')
				} else {
					alert('修改失败!');
				}
			}
		});
	}

    //创建子任务
    createTask = function (id) {
//                layer.alert("暂未开放！谢谢！");
//                return;
        layer.confirm("确认创建监测任务？", {
            icon: 3,
            btn: ['确定', '取消'] //按钮
        }, function () {
            $.ajax({
                url: "/task/createTask",
                type: "post",
                data: {
                    "id": id
                },
                cache: false,
                dataType: "json",
                success: function (datas) {
                    var resultRet = datas.ret;
                    if (resultRet.code == 101) {
                        layer.confirm(resultRet.resultDes, {
                            icon: 2,
                            btn: ['确定'] //按钮
                        });
                    } else {
                        layer.confirm("创建成功，请及时指派检测人员执行！", {
                            icon: 1,
                            btn: ['确定'] //按钮
                        }, function () {
                            window.location.reload();
                        });
                    }
                },
                error: function (e) {
                    layer.confirm("服务忙，请稍后再试", {
                        icon: 5,
                        btn: ['确定'] //按钮
                    });
                }
            });
        });
    }

</script>
<!-- 特色内容 -->

<@model.webend />