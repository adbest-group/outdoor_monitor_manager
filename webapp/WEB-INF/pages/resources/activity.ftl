<#assign webTitle="活动管理" in model>
<#assign webHead in model>
<link rel="stylesheet" href="/static/upload/webuploader.css" />
</#assign>
<@model.webhead />
    <!-- 头部 -->
    <@model.webMenu current="活动管理" child="活动管理" />
	<!-- 特色内容 -->
<div class="main-container" style="height: auto;">
    <div class="main-box">
        <div class="title clearfix">
        	<#if user.usertype !=6>
        		<a href="/customer/activity/edit" class="btn btn-red mr-10 ll">创建活动</a>
                <a id="test_upload">测试上传文件</a>
        	</#if>
            <div class="search-box search-ll" style="margin: 0 0 0 20px">
                <form id="form" method="get" action="/sysResources/activity">
                    <!--活动搜索框-->
                     <div class="inp">
                    	<input type="text" placeholder="请输入活动名称" value="${name?if_exists}" id="searchName" name="name">
                	</div>
                    <#-- <div class="select-box select-box-140 un-inp-select ll">
                        <select name="activityId" class="select" id="activityId">
                            <option value="">所有活动</option>
                        <@model.showAllActivityOps value="${bizObj.queryMap.activityId?if_exists}"/>
                        </select>
                    </div> -->
                    <div class="select-box select-box-140 un-inp-select ll">
                        <select name="userId" class="select" id="userId">
                            <option value="">所有广告主</option>
                        	<@model.showAllCustomerOps value="${bizObj.queryMap.userId?if_exists}"/>
                        </select>
                    </div>
                    <div class="select-box select-box-100 un-inp-select ll">
                        <select class="select" name="status">
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
	                </div>
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
                    <#if user.usertype !=6>
                     <button type="button" class="btn btn-red" style="margin-left:10px;" id="assignBtn">批量确认</button> 
                     <button style="margin-left: 10px" type="button" class="btn" id="downloadBatch" autocomplete="off" onclick="">监测任务模板下载</button>
                    </#if>
                </form>
            </div>
        </div>

        <!-- 数据报表 -->
        <div class="data-report">
            <div class="bd">
                <table width="100%" cellpadding="0" cellspacing="0" border="0" class="tablesorter" id="plan">
                    <thead>
                    <tr>
                     <th width="30"><#if user.usertype !=6><input type="checkbox" style="visibility: hidden" id='thead-checkbox' name="ck-alltask" value=""/></#if></th>
                        <th>序号</th>
                        <th>活动名称</th>
                        <th>广告主</th>
                        <th>投放周期</th>
                        <th>活动状态</th>
                        <th>审核人</th>
                        <th>审核时间</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#if (bizObj.list?exists && bizObj.list?size>0) >
                        <#list bizObj.list as activity>
                        <tr>
                        	<td width="30"><#if (activity.status?exists&&activity.status == 1)>
                        		<#if user.usertype !=6><input type="checkbox"  name="ck-task" data-status='${activity.status}' value="${activity.id}"/></#if>
                        	</#if></td>
                            <td width="30">${(bizObj.page.currentPage-1)*20+activity_index+1}</td>
                            <td>
                                <div class="data-title w200" data-title="${activity.activityName}" data-id="${activity.id}">${activity.activityName?if_exists}</div>
                            </td>
                            <td>${activity.customerName?if_exists}</td>
                            <td>${activity.startTime?string('yyyy-MM-dd')} 至 ${activity.endTime?string('yyyy-MM-dd')}</td>
                            <td>${vm.getActivityStatusTextWithColor(activity.status)}</td>
                            <td>${activity.realName?if_exists}</td>
                            <td>${activity.updateTime?string('yyyy-MM-dd HH:mm:ss')}</td>
                            <td>
                                <#if user.usertype !=6>
                            		<#if activity.status==1><a href="javascript:queren('${activity.id}','${activity.userId}')">确认</a></#if>
                            	</#if>
                                <#if activity.status gt 0 ><a href="/activity/edit?id=${activity.id}">详情</a></#if>
                                <#if user.usertype !=6>
                                	<#if activity.status==1><a href="javascript:del('${activity.id}')">删除</a></#if>
                                </#if>
                                <#if activity.status!=1&&activity.status!=4><a id="openActivityExcel" href="javascript:openActivityExcel('${activity.id}')">导出excel</a></#if>
                                <#if activity.status!=1&&activity.status!=4><a id="openActivityPdf" href="javascript:openActivityPdf('${activity.id}')">导出pdf</a></#if>
                                <#if user.usertype !=6>
                                	<#if activity.status==2>
                                		<button style="margin-left: 10px" type="button" class="btn batchInsert" autocomplete="off" ai=${activity.id}>导入监测任务</button>
                                	</#if>
                                </#if>
                                 <#-- <#if activity.status!=1&&activity.status!=4><a id="exportExcel" href="javascript:exportExcel('${activity.id}')">导出excel</a></#if> -->
                                <#-- <#if activity.status!=1&&activity.status!=4><a id="exportPdf" href="javascript:exportPdf('${activity.id}')">导出pdf</a></#if> -->
                            </td>
                        </tr>
                        </#list>
                    <#else>
                    <tr><td colspan="20">没有相应结果。</td> </tr>
                    </#if>
                    </tbody>
                    <input id="upload_file" type="hidden"/>
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
<script type="text/javascript" src="/static/upload/webuploader.js"></script>
<script type="text/javascript">

	$('.select').searchableSelect();
	$('#userId').next().find('.searchable-select-input').css('display', 'block');
	<#--
	changeMediaTypeId();
            $(function(){
                $(".nav-sidebar>ul>li").on("click",function(){
                    $(".nav-sidebar>ul>li").removeClass("on");
                    $(this).addClass("on");
                });
            }); -->

    $(function(){
        $(window).resize();
    });

    $(window).resize(function() {
        var h = $(document.body).height() - 115;
        $('.main-container').css('height', h);
    });

    <#-- 
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

	 <#--
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
     -->
     <#--
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
     -->
 	// 查询
    $("#searchBtn").on("click", function () {
        var strParam = "";
        var name = $("#searchName").val();

        if (name != null && $.trim(name).length) {
            strParam = strParam + "?name=" + name;
        }

        window.location.href = "/sysResources/activity" + strParam;
    });

            function createDateStr(alt){
                var today =  new Date();
                var t=today.getTime()+1000*60*60*24*alt;
                var newDate=new Date(t).Format("yyyy-MM-dd");
                if(alt==-6||alt==-29)
                    return newDate+" 至 "+today.Format("yyyy-MM-dd");
                return newDate+" 至 "+newDate;
            }

    $(function(){
/*
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
        }); */

//        var newDate=new Date().Format("yyyy-MM-dd");
//        $('.inputs-date').data('dateRangePicker').setDateRange(newDate,newDate, true);

       // 如果列表中有未确认的状态就显示表头的多选框
        $("input[name='ck-task']").each(function() {
        	if($(this).data('status') === 1){
        		$('#thead-checkbox').css('visibility', 'visible')
        		return false;
        	}
        })

		var isLoading = true;
	   //批量审核
       $("#assignBtn").click(function(){
        if($("input[name='ck-task']:checked").length<1){
            layer.confirm('请选择需要审核的活动', {
                icon: 0,
                btn: ['确定'] //按钮
            });
        }else{
        	isLoading = true;
       		var ids = [];
            $("input[name='ck-task']:checked").each(function(i,ck){
                if(ck.value) ids.push(ck.value);
            });
            id_sel = ids.join(",");

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
		            url: "/activity/confirm",
		            type: "post",
		            data: {
		                "ids": id_sel
		            },
		            cache: false,
		            dataType: "json",
		            success: function(datas) {
		            	isLoading = false;
                		layer.closeAll('msg');
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
		            	isLoading = false;
                		layer.closeAll('msg');
		                layer.confirm("服务忙，请稍后再试", {
		                    icon: 5,
		                    btn: ['确定'] //按钮
		                });
		            }
		        });
        	}
       	});

        $("input[name='ck-alltask']").change(function(){
            if($(this).is(":checked")){
                $("input[name='ck-task']").prop("checked",true)
            }else{
                $("input[name='ck-task']").removeAttr("checked");
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

    //活动确认
    queren = function(activityId,userId){
    	isLoading = true;
        layer.confirm("确认该活动将生成对应的的监测任务", {
            icon: 3,
            btn: ['确定', '取消'] //按钮
        }, function(){
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
                url: "/activity/confirm",
                type: "post",
                data: {
                    "ids": activityId,
                    "userId" : userId
                },
                cache: false,
                dataType: "json",
                success: function(datas) {
                	isLoading = false;
                	layer.closeAll('msg');
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
                	isLoading = false;
                	layer.closeAll('msg');
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
	// 下载模板
    $('#downloadBatch').click(function(){
    	$.get('/excel/downMonitorBatch', function(data){
    		if(data.ret.code === 100) {
    			window.open(data.ret.result)
    		}else{
    			layer.confirm("下载失败", {
                    icon: 5,
                    btn: ['确定'] //按钮
                });
    		}
    	})
    })

    // 批量导入监听
    $(".batchInsert").on("click", function (e) {
        $("#upload_file").val($(this).attr("ai"));
        $("#upload_file").click();
    });

    // 批量导入执行
	layui.use('upload', function(){
	  var upload = layui.upload;
	  //执行实例
	  var uploadInst = upload.render({
	    elem: '#upload_file' //绑定元素
	    ,data: {
	    	activityId: function () {
	    	    return $("#upload_file").val()
            }
		}
	    ,accept: 'file' //指定只允许上次文件
	    ,exts: 'xlsx|xls' //指定只允许上次xlsx和xls格式的excel文件
	    ,field: 'excelFile' //设置字段名
	    ,url: '/excel/insertTaskBatchByExcel' //上传接口
	    ,before: function() {
	    	isLoading = true;
	    	layer.msg('正在努力上传中...', {
	    		icon: 16,
	    		shade: [0.5, '#f5f5f5'],
	    		scrollbar: false,
	    		time: 300000
	    	}, function(){
	    		if(isLoading){
	    			layer.alert('上传超时', {icon: 2, closeBtn: 0, btn: [], title: false, time: 3000, anim: 6});
	    		}
    		})
	    }
	    ,done: function(res){
	    	isLoading = false;
	    	layer.closeAll('msg')
	    	if(res.ret.code == 100){
	    	layer.alert('导入成功', {icon: 1, closeBtn: 0, btn: [], title: false, time: 3000});
	    	window.open(res.ret.result);
	    	window.location.reload();
	    	} else if (res.ret.code == 101){
	    	layer.alert(res.ret.resultDes, {icon: 2, closeBtn: 0, btn: [], title: false, time: 3000, anim: 6});
	    	} else if (res.ret.code == 105){
	    	layer.alert('没有导入权限', {icon: 2, closeBtn: 0, btn: [], title: false, time: 3000, anim: 6});
	    	}
	    }
	    ,error: function(res){
	    	isLoading = false
	       layer.closeAll('msg')
	       layer.alert('导入失败', {icon: 2, closeBtn: 0, btn: [], title: false, time: 3000, anim: 6});
	    }
	  });
	});

    // 测试上传文件
    var fileMd5 = "";
    WebUploader.Uploader.register({
        "add-file": "addFile",
        "before-send-file": "beforeSendFile",
        "before-send": "beforeSend",
        "after-send-file": "afterSendFile"
    }, {
        addFile: function(files){
            console.log("addFile", arguments);
            files.forEach(function (file, index) {
                if(file.size == 0){
                    files.splice(index, 1);
                }
            })
        },
        beforeSendFile:function (file) {
            // var deferred = WebUploader.Deferred();
            // console.log("beforeSendFile", arguments);
        },
        beforeSend:function (block) {
            var deferred = WebUploader.Deferred();
            console.log("beforeSend", arguments);
            var data = {
                md5: fileMd5,
                chunk: block.chunk,
                chunkSize: block.blob.size
            };
            $.post("/file/checkChunk", data, function (res) {
                console.log("res", res);
                // console.log("deferred", deferred);
                if(res){
                    console.log("reject");
                    deferred.reject();
                } else {
                    console.log("resolve");
                    deferred.resolve();
                }
            })


        },
        afterSendFile:function (file) {
            // var deferred = WebUploader.Deferred();
            console.log("afterSendFile", arguments);
            $.post("/file/mergeChunks", {md5: fileMd5}, function (res) {
                console.log("res", res);


            })
        }
    });

    var uploader = WebUploader.create({
        pick: '#test_upload',  // 文件按钮选择器或者dom节点
        swf: "static/upload/Uploader.swf", // swf文件路径
        server: "/file/upload",  // 上传地址
        auto: false, // 是否自动上传
        disableGlobalDnd: true, // 是否禁止页面其他地方拖拽功能
        chunked: true, // 是否分片上传
        prepareNextFile: true, // 是否允许文件传输时准备下一个文件
        chunkRetry: 3, // 分片上传失败重试次数
        threads: 5, // 上传并发数
        formData: {}, // 请求时参数表(此处留空，上传时uploader会附加参数)
        fileVal: "file", // 文件上传域的name
        method: "POST", // 请求方式
        fileSingleSizeLimit: 2147483648 // 单个文件最大允许大小（2G）
    });

    // 文件加入队列
    uploader.on("fileQueued",function(file){
        console.log("queued file", file);
        uploader.md5File(file, Math.round(file.size*0.2), Math.ceil(file.size*0.2)).progress(function (percentage) {
            console.log("per", percentage);
        }).then(function (value) {
            console.log("md5", value);
            fileMd5 = value;
            uploader.options.formData.md5 = value;
            uploader.upload();
        });
    });

    // 文件的分块在发送前触发
    uploader.on("uploadBeforeSend",function(object, data, headers){
        // console.log("beforeSend", arguments);
    });

    // 上传进度
    uploader.on("uploadProgress",function(file, percentage){
        // console.log("progress", arguments)
    });

    // 上传成功
    uploader.on("uploadSuccess",function(file, response){
        console.log("success", arguments)

    });
    // 上传失败
    uploader.on("uploadError", function(file, reason){
        console.log("error", arguments)

    });
    // 上传结束
    uploader.on("uploadFinished", function(){
        // console.log("finished", arguments)

    });

</script>
<!-- 特色内容 -->

<@model.webend />