<#assign webTitle="监测管理" in model>
<#assign webHead in model>
</#assign>
<@model.webhead />
    <!-- 头部 -->
    <@model.webMenu current="任务管理" child="任务管理" />

	<!-- 特色内容 -->
<div class="main-container" style="height: auto;">
    <div class="main-box">
        <div class="title clearfix" style="display:block;">
            <div class="search-box search-ll" style="margin: 0 0 0 20px">
                <form id="form" method="get" action="/platmedia/task/list">
                	<!--活动搜索框-->
                     <div class="inp">
                    	<input type="text" placeholder="请输入活动名称" value="${name?if_exists}" id="searchName" name="name">
                	</div>
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
	                </div><br/><br/>
	                <#-- 城市 -->
					<div id="demo3" class="citys" style="float: left; font-size: 12px">
                        <p>
                                               城市： <select style="height: 30px" id="adSeatInfo-province" name="province">
                            <option value=""></option>
                        </select> <select style="height: 30px" id="adSeatInfo-city" name="city"></select>
    
                        </p>
                    </div>
                    <#-- <!--销售下拉框
                    <div class="select-box select-box-140 un-inp-select ll">
                        <select name="activityId" class="select" id="activityId">
                            <option value="">所有活动</option>
                        <@model.showAllActivityOps value="${bizObj.queryMap.activityId?if_exists}"/>
                        </select>
                    </div> -->
                    <#--<div class="select-box select-box-100 un-inp-select ll">-->
                        <#--<select class="select" name="status">-->
                            <#--<option value="">所有状态</option>-->
                            <#--<@model.showMonitorTaskStatusOps value="${bizObj.queryMap.status?if_exists}" />-->
                        <#--</select>-->
                    <#--</div>-->
                    <div class="ll inputs-date">
                        <#--<input class="ui-date-button" type="button" value="昨天" alt="-1" name="">-->
                        <#--<input class="ui-date-button" type="button" value="近7天" alt="-6" name="">-->
                        <#--<input class="ui-date-button on" type="button" value="近30天" alt="-29" name="">-->
                        <div class="date">
                            <input id="dts" class="Wdate" type="text" name="startDate" value="${bizObj.queryMap.startDate?if_exists}"> -
                            <input id="dt" class="Wdate" type="text" name="endDate" value="${bizObj.queryMap.endDate?if_exists}">
                        </div>
                    </div>
                    <button type="button" class="btn btn-red" style="margin-left:10px;" autocomplete="off" id="searchBtn">查询</button>
                </form>
            </div>
        </div>

        <!-- 数据报表 -->
        <div class="data-report">
            <div class="bd">
                <table width="100%" cellpadding="0" cellspacing="0" border="0" class="tablesorter" id="plan">
                    <thead>
                    <tr>
                        <th width="30">序号</th>
                        <th>活动名称</th>
                        <th>上刊示例</th>
                        <th>投放周期</th>
                        <th>地区</th>
                        <th>媒体</th>
                        <th>媒体大类</th>
					    <th>媒体小类</th>
                        <th>广告位</th>
                        <th>执行公司</th>
                        <th>执行人员</th>
                        <th>监测类型</th>
                        <th>状态</th>
                        <th>指派人</th>
                        <th>指派时间</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#if (bizObj.list?exists && bizObj.list?size>0) >
                        <#list bizObj.list as task>
                        <tr>
                            <td width="30">${(bizObj.page.currentPage-1)*20+task_index+1}</td>
                            <td>
                                <div class="data-title w200" data-title="${task.activityName!""}" data-id="${task.id}">${task.activityName?if_exists}</div>
                            </td>
                            <td><img width="50" src="${task.samplePicUrl!""}"/> </td>
                            <td>${task.startTime?string('yyyy-MM-dd')}<br/>${task.endTime?string('yyyy-MM-dd')}</td>
                            <td>${vm.getCityName(task.province)!""} ${vm.getCityName(task.city!"")}</td>
                            <td>${task.mediaName!""}</td>
                            <td>${task.parentName!""}</td>
                            <td>${task.secondName!""}</td>
                            <td>${task.adSeatName!""}</td>
                            <td>${task.companyName!""}</td>
                            <td>${task.realname!""}</td>
                            <td>${vm.getMonitorTaskTypeText(task.taskType)!""}</td>
                            <td>${vm.getMonitorTaskStatusText(task.status)!""}</td>
                            <td>${task.assignorName!""}</td>
                            <td>${(task.assignorTime?string('yyyy-MM-dd HH:mm:ss'))!""}</td>
                            <td>
                            	<#if vm.getUnassignTask(task.endTime)&lt;0><#if (task.status==1 || task.status==8 || task.status==2)><#if task.companyId?exists><#else><a href="javascript:assign('${task.id}',${task.mediaId})">指派</a></#if></#if></#if>
                               <#--   <#if task.status==1><a href="javascript:assign('${task.id}')">指派</a></#if>
                                <#--<#if task.status==2><a href="javascript:assign('${task.id}')">重新指派</a></#if>-->
                               <#--   <#if task.status==3><a href="javascript:pass('${task.id}')">通过</a></#if>
                                <#if task.status==3><a href="javascript:reject('${task.id}')">拒绝</a></#if>-->
                                <a href="/platmedia/task/details?task_Id=${task.id}">详情</a>
                                <#--<#if task.status==1><a href="javascript:del('${task.id}')">删除</a></#if>-->
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
<script type="text/javascript" src="/static/js/jquery.citys.js"></script>
<!-- 下拉 -->
<link href="${model.static_domain}/js/select/jquery.searchableSelect.css" rel="stylesheet">
<script src="${model.static_domain}/js/select/jquery.searchableSelect.js"></script>
<!-- 时期 -->
<link href="${model.static_domain}/js/date/daterangepicker.css" rel="stylesheet">
<script type="text/javascript" src="${model.static_domain}/js/date/moment.min.js"></script>
<script type="text/javascript" src="${model.static_domain}/js/date/jquery.daterangepicker.js"></script>
<script type="text/javascript" src="${model.static_domain}/js/date.js"></script>

<script type="text/javascript">
	$('#mediaTypeParentId').searchableSelect({
		afterSelectItem: function(){
			if(this.holder.data("value")){
				
				changeMediaTypeId(this.holder.data("value"))
				$('#mediaTypeId').css('display', 'inline-block')
			}else{
				$('#mediaTypeId').parent().html('<select style="width: 120px;height:31px;display:none" name="mediaTypeId" id="mediaTypeId"><option value="">请选择媒体小类</option></select>')
			}
		}
	})
	$('#mediaTypeParentId').next().find('.searchable-select-input').css('display', 'block')

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
 	// 查询
    $("#searchBtn").on("click", function () {
        var strParam = "";
        var name = $("#searchName").val();
        
        if (name != null && $.trim(name).length) {
            strParam = strParam + "?name=" + name;
        }

        window.location.href = "/platmedia/task/list" + strParam;
    });
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
        });

//        var newDate=new Date().Format("yyyy-MM-dd");
//        $('.inputs-date').data('dateRangePicker').setDateRange(newDate,newDate, true);

    });

    $("#searchBtn").on("click",function() {
        $("#form").submit();
    });

            //指派
            assign = function (id) {
                assign_ids = id;
                openSelect();
            }

            //打开选择执行者
            openSelect = function() {
                layer.open({
                    type: 2,
                    title: '选择监测人员',
                    shade: 0.8,
                    area: ['400px', '320px'],
                    content: '/platmedia/selectUserExecute' //iframe的url
                });
            }
            //选择执行人后的回调
            selectUserExecuteHandle = function (userId,mediaId) {
           		isLoading = true;
                layer.closeAll();
                if(!userId){
                    layer.alert("并没有指定执行人员");
                    return;
                }
				layer.msg('正在操作中...', {
		    		icon: 16,
		    		shade: [0.5, '#f5f5f5'],
		    		scrollbar: false,
		    		time: 150000
		    	}, function(){
		    		if(isLoading){
		    			layer.alert('操作超时', {icon: 2, closeBtn: 0, btn: [], title: false, time: 3000, anim: 6});
		    		}
		    	})
                $.ajax({
                    url: "/task/assign",
                    type: "post",
                    data: {
                        "ids": assign_ids,
                        "userId":userId,
                        "mediaId":mediaId
                    },
                    cache: false,
                    dataType: "json",
                    success: function(datas) {
                    	isLoading = false;
                		layer.closeAll('msg');
                        var resultRet = datas.ret;
                        if (resultRet.code == 100) {
                            layer.confirm("指派成功", {
                                icon: 1,
                                btn: ['确定'] //按钮
                            },function () {
                                window.location.reload();
                            });
                        } else {
                            layer.confirm(resultRet.resultDes, {
                                icon: 2,
                                btn: ['确定'] //按钮
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

            //审核通过
            pass = function(id){
                layer.confirm("确认审核通过？", {
                    icon: 3,
                    btn: ['确定', '取消'] //按钮
                }, function(){
                    verify(id,4);
                });
            }

            //审核不通过
            reject = function(id){
                layer.confirm("确认审核不通过？", {
                    icon: 3,
                    btn: ['确定', '取消'] //按钮
                }, function(index){
                    layer.close(index);
                    layer.prompt({title:'请填写审核意见',formType:2},function (val,index) {
                        if(val.trim().length<1||val.trim().length>33){
                            layer.alert("请填写30字以内！",{icon:2});
                            return;
                        }
                        layer.close(index);
                        verify(id,5,val);
                    });

                });
            }

            //发起审核请求
            verify = function (id,status,reason) {
                $.ajax({
                    url: "/task/verify",
                    type: "post",
                    data: {
                        "id": id,
                        "status":status,
                        "reason":reason
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
                            layer.confirm("审核成功", {
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
            }

</script>
<!-- 特色内容 -->

<@model.webend />