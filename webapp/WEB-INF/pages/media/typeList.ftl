<#assign webTitle="资源管理" in model>
<#assign webHead in model>
</#assign>
<@model.webhead />
<!-- 头部 -->
<@model.webMenu current="资源管理" child="adMediaType" />
<div class="main-container" style="height: auto;">
    <div class="main-box ott-market">
        <div class="title clearfix">
            <a href="javascript:;" class="add-new-btn ll" id="add_media"><i></i> 新建媒体类型</a>
            <div class="search-box search-ll" style="margin: 0 0 0 20px">
            	<form id="form" method="get" action="/mediaType/list">
	                
	                <!--<div class="select-box select-box-100 un-inp-select ll">
	                    <select name="mediaType" class="select" id="mediaType">
	                    	<option value="">全部媒体类型</option>
	                        <@model.showMediaTypeOps value="${bizObj.queryMap.mediaType?if_exists}"/>
	                    </select>
	                </div>  -->
	                <div class="select-box select-box-100 un-inp-select ll">
	                    <select name="parentId" class="select" id="parentId">
	                    	<option value="">全部媒体大类</option>
	                        <@model.showAllAdMediaTypeOps value="${bizObj.queryMap.parentId?if_exists}"/>
	                    </select>
	                </div>
	                <div class="inp">
	                    <input type="text" placeholder="请输入媒体类型名称" value="${searchMediaName?if_exists}" id="name" name="name">
	                </div>
	                <button type="button" class="btn btn-red" autocomplete="off" id="searchBtn">查询</button>
	                
	                <button style="margin-left: 10px" type="button" class="btn" id="insertBatchId" autocomplete="off">批量导入</button>
	                
	                <button style="margin-left: 10px" type="button" class="btn" id="downloadBatch" autocomplete="off" onclick="">模板下载</button>
                </form>
            </div>
        </div>

        <!-- 数据报表 -->
        <div class="data-report">
            <div class="bd">
                <table width="100%" cellpadding="0" cellspacing="0" border="0" class="tablesorter" id="plan">
                    <thead>
                    <tr>
                        <th>序号</th>
                        <th>媒体类型名称</th>
                        <th>类型</th>
                        <th>是否允许同时有多个活动</th>
                        <th>允许的活动数量</th>
                        <#-- <th>需要唯一标识</th> -->
                        <th>当前状态</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#if (bizObj.list?exists && bizObj.list?size>0)>
                        <#list bizObj.list as type>
                        <tr>
                            <td width="30">${(bizObj.page.currentPage-1)*20+type_index+1}</td>
                            <td>${type.name?if_exists}</td>
                            <td>
                            	<#if type.mediaType?exists && type.mediaType == 1>媒体大类</#if>
                            	<#if type.mediaType?exists && type.mediaType == 2>媒体小类</#if>
                            </td>
                            <#-- 
                            <td>
                            	<#if type.uniqueKeyNeed?exists && type.mediaType == 2 && type.uniqueKeyNeed == 1>需要</#if>
                            	<#if type.uniqueKeyNeed?exists && type.mediaType == 2 && type.uniqueKeyNeed == 2>不需要</#if>
                            </td>
                             -->
                            <td>
                            	<#if type.mediaType?exists && type.mediaType == 2>
	                            	<#if type.allowMulti?exists && type.allowMulti == 1>是</#if>
	                            	<#if type.allowMulti?exists && type.allowMulti == 0>否</#if>
                            	</#if>
                            </td>
                            <td>
                            	<#if type.mediaType?exists && type.mediaType == 2>${type.multiNum?if_exists}</#if>
                            </td>
                            <td>
                            	<#if type.status?exists && type.status == 1>可用</#if>
                            	<#if type.status?exists && type.status == 2>不可用</#if>
                            </td>
                            <td>
                            	<#if type.mediaType?exists && type.mediaType == 2>
                                <a href="javascript:void(0);" onclick="edit('${type.id}');">编辑</a>&nbsp&nbsp&nbsp&nbsp
                               	</#if>
                               
                                <!--只显示可用或者不可用 <#if type.status?exists && type.status == 1>
                                	<a href="javascript:void(0);" onclick="updateStatus('${type.id}', 2, '${type.mediaType}');">不可用</a>
                                </#if>
                                <#if type.status?exists && type.status == 2>
                            		<a href="javascript:void(0);" onclick="updateStatus('${type.id}', 1, '${type.mediaType}');">可用</a>
                                </#if>  -->
                               
                            	<a href="javascript:void(0);" onclick="updateStatus('${type.id}', 1, '${type.mediaType}');">可用</a>&nbsp&nbsp&nbsp&nbsp
                                <a href="javascript:void(0);" onclick="updateStatus('${type.id}', 2, '${type.mediaType}');">不可用</a>
                                <#-- 
                                <#if type.mediaType?exists && type.mediaType == 2 && type.uniqueKeyNeed == 2>
                                	&nbsp;&nbsp;
                                	<a href="javascript:void(0);" onclick="updateNeed('${type.id}', 1);">需要唯一标识</a>
                                </#if>
                                <#if type.mediaType?exists && type.mediaType == 2 && type.uniqueKeyNeed == 1>
                                	&nbsp;&nbsp;
                            		<a href="javascript:void(0);" onclick="updateNeed('${type.id}', 2);">不需要唯一标识</a>
                                </#if>
                                 -->
                            </td>
                        </tr>
                        </#list>
                    </#if>
                    </tbody>
                    <!-- 翻页 -->
                <@model.showPage url=vm.getUrlByRemoveKey(thisUrl, ["start", "size"]) p=bizObj.page parEnd="" colsnum=7 />
                </table>
            </div>
        </div>
    </div>
</div>
</div>
</div>
</div>
<!-- 下拉 -->
<link href="${model.static_domain}/js/select/jquery.searchableSelect.css" rel="stylesheet">
<script src="${model.static_domain}/js/select/jquery.searchableSelect.js"></script>

<script type="text/javascript">

	$(function() {
        $(window).resize(function() {
            var h = $(document.body).height() - 115;
            $('.main-container').css('height', h);
        });
        $(window).resize();
    });

	$('.select').searchableSelect();
	$('#parentId').next().find('.searchable-select-input').css('display', 'block');
	$("#parentId").siblings().hide();
	var type = $('#mediaType').val();
	if(type == "") {
		$("#parentId").siblings().hide();
	} else if(type == 1) {
		$("#parentId").siblings().hide();
	} else {
		$("#parentId").siblings().show();
	}

	$("#mediaType").siblings().find(".searchable-select-item").click(function(){
        var type = $('#mediaType').val();
		if(type == "") {
			$("#parentId").siblings().hide();
		} else if(type == 1) {
			$("#parentId").siblings().hide();
		} else {
			$("#parentId").siblings().show();
		}
    });
    
    // 查询
    $("#searchBtn").on("click",function() {
        $("#form").submit();
    });
    
    // 下载模板
    $('#downloadBatch').click(function(){
    	$.get('/excel/downloadMediaTypeBatch', function(data){
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
    
    <#-- 
    // 查询
    $("#searchBtn").on("click", function () {
        var strParam = "";
        var mediaType = $("#mediaType").val();
        var parentId = $("#parentId").val();
        var name = $("#name").val();
        
        if (mediaType != null && $.trim(mediaType).length) {
            strParam = strParam + "?mediaType=" + mediaType;
        }
        if (mediaType == 2) {
        	if (parentId != null && $.trim(parentId).length) {
	            strParam = strParam + "&parentId=" + parentId;
	        }
        }
        if (name != null && $.trim(name).length) {
            strParam = strParam + "&name=" + name;
        }

        window.location.href = "/mediaType/list" + strParam;
    });
     -->

    $("#add_media").on("click", function () {
        //iframe层
        layer.open({
            type: 2,
            title: '添加媒体类型',
            shadeClose: true,
            shade: 0.8,
            area: ['600px', '480px'],
            content: '/mediaType/add' //iframe的url
        });
    });

    function edit(id) {
        layer.open({
            type: 2,
            title: '修改媒体类型',
            shadeClose: true,
            shade: 0.8,
            area: ['600px', '480px'],
            content: '/mediaType/edit?id=' + id //iframe的url
        });
    }

	// 修改是否需要唯一标识
	function updateNeed(id, uniqueKeyNeed) {
		$.ajax({
            url: "/mediaType/updateUniqueTypeNeed",
            type: "post",
            data: {
                "id": id,
                "uniqueKeyNeed": uniqueKeyNeed
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
                    var msg = "";
                    if (status == "2") {
                        msg = "操作成功";
                    } else {
                        msg = "操作成功";
                    }
                    layer.confirm(msg, {
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
	
    // 修改媒体类型状态
    function updateStatus(id, status, mediaType) {
        layer.confirm("确认修改媒体类型状态", {
            icon: 3,
            btn: ['确定', '取消'] //按钮
        }, function () {
            doUpdate(id, status, mediaType);
        });
    }

    function doUpdate(id, status, mediaType) {
        $.ajax({
            url: "/mediaType/updateMediaTypeStatus",
            type: "post",
            data: {
                "id": id,
                "status": status,
                "mediaType": mediaType
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
                    var msg = "";
                    if (status == "2") {
                        msg = "停用成功";
                    } else {
                        msg = "启用成功";
                    }
                    layer.confirm(msg, {
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
    
    //批量导入
	layui.use('upload', function(){
	  var upload = layui.upload;
	  
	  //执行实例
	  var uploadInst = upload.render({
	    elem: '#insertBatchId' //绑定元素
	    ,data: {
		}
	    ,accept: 'file' //指定只允许上次文件
	    ,exts: 'xlsx|xls' //指定只允许上次xlsx和xls格式的excel文件
	    ,field: 'excelFile' //设置字段名
	    ,url: '/excel/insertMediaTypeByExcel' //上传接口
	    ,done: function(res){
	    	if(res.ret.code == 100){
	    		layer.alert('导入成功', {icon: 1, closeBtn: 0, btn: [], title: false, time: 3000});
	    		//window.open(res.ret.result);
	    		window.location.reload();
	    	} else if (res.ret.code == 101){
	    		layer.alert(res.ret.resultDes, {icon: 2, closeBtn: 0, btn: [], title: false, time: 3000, anim: 6});
	    	} else if (res.ret.code == 105){
	    		layer.alert('没有导入权限', {icon: 2, closeBtn: 0, btn: [], title: false, time: 3000, anim: 6});
	    	}
	    }
	    ,error: function(res){
	       layer.alert('导入失败', {icon: 2, closeBtn: 0, btn: [], title: false, time: 3000, anim: 6});
	    }
	  });
	});
	
</script>
<@model.webend />