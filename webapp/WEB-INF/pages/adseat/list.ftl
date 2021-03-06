<#assign webTitle="资源管理" in model> <#assign webHead in model> </#assign>
<@model.webhead />
<!-- 头部 -->
<@model.webMenu current="资源管理" child="广告位管理" />
<!-- 特色内容 -->
<div class="main-container" style="height: auto;">
    <div class="main-box">
        <div class="title clearfix" style="display: block;">
            <div class="search-box search-ll" style="margin: 0 0 0 20px;display: inline-block;">
            	  <div style="width:120px;display:block;float:left">
            	  	
            	  </div>
	                <#--<button type="button" class="btn btn-red" autocomplete="off"-->
	                <#--onclick="window.location.href='/adseat/edit'">新增广告位</button>-->
					<#--<button style="margin-left: 10px" type="button" class="btn"-->
					<#--autocomplete="off" onclick="">批量导入</button>-->
	                <#--<div style="border-bottom: 1px solid black; margin:10px auto"></div>-->
                <form id="form" method="get" action="/adseat/list" style="display: inline-block;">
                	<#if user.usertype !=6>
              		<button type="button" style="float:left;margin-right:30px" class="btn btn-red" autocomplete="off" onclick="window.location.href='/platmedia/adseat/edit'">新增广告位</button>
              		</#if>
                	<!--活动搜索框-->
                     <div class="inp">
                    	<input type="text" placeholder="请输入广告位名称" value="${name?if_exists}" id="searchName" name="name">
                	</div>
               		 <input type="hidden" id="startDate" value="" name="startDate">
                	<input type="hidden" id="endDate" value="" name="endDate">
                	<input type="hidden" id="seatIds" value="" name="seatIds">
                	<input type="hidden" id="start" value="" name="start">
                	<input type="hidden" id="size" value="" name="size">
                	<input type="hidden" id="mediaTypeIdHidden" value="${bizObj.queryMap.mediaTypeId?if_exists}">
                    <!--销售下拉框-->
                    <div id="demo3" class="citys" style="float: left; font-size: 12px">
                        <p>
                           	 投放地区： <select style="height: 30px" id="adSeatInfo-province" name="province">
                        			</select> 
                        	<select style="height: 30px" id="adSeatInfo-city" name="city"></select>
                            <!-- <select style="height: 30px" id="adSeatInfo-region" name="region"></select>
                            <select style="height: 30px" id="adSeatInfo-street" name="street"></select> -->
                        </p>
                    </div>
                    <#if user.usertype !=6>
                       	<button style="margin-left: 10px" type="button" class="btn" id="batchInsert" autocomplete="off">批量导入</button>
						<button style="margin-left: 10px" type="button" class="btn" id="downloadBatch" autocomplete="off" onclick="">模板下载</button>
					</#if>
                    <br/><br/>

                    <div style="float: left;  font-size: 12px" class="select-box select-box-100 un-inp-select ll">
                        <select style="height: 30px;" name="mediaId" onchange="importEnabled()" id="selectMediaId">
                        			<option value="">所有媒体</option> 
                        			<@model.showAllMediaOps value="${bizObj.queryMap.mediaId?if_exists}" />
                    			</select>
                    </div>
                    
                    <div style="float:left;height:30px;line-height:30px;" class="mr-10">
                       	 媒体类型: 
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
	                </div>
                    
					<button type="button" class="btn btn-red" style="margin-left: 10px;" autocomplete="off" id="searchBtn">查询</button>
					<#-- 
                	<button type="button" class="btn btn-primary" style="margin-left: 10px;" autocomplete="off" id="clear">清除条件</button>
                	 -->
                </form>
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
					   <th>广告位名称</th>
					   <th>媒体大类</th>
					   <th>媒体小类</th>
					   <th>媒体主</th>
					   <th>广告位编号</th>
					   <th>区域</th>
					   <th>主要路段</th>
					   <th>广告位具体位置</th>
					   <th>面数</th>
					   <!--<th>广告位尺寸</th>  -->
					   <th>是否已贴二维码</th>
					   <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
					<#if (bizObj.list?exists && bizObj.list?size>0) > 
					<#list bizObj.list as adseat>
	                    <tr>
	                        <td>${(bizObj.page.currentPage-1)*20+adseat_index+1}</td>
							<td>${adseat.name!""}</td>
							<td>${adseat.parentName!""}</td>
							<td>${adseat.secondName!""}</td>
							<td>${adseat.mediaName!""}</td>
							<td>${adseat.memo!""}</td>
							<td>${vm.getCityName(adseat.province)!""} ${vm.getCityName(adseat.city!"")}</td>
							<td>${adseat.road!""}</td>
							<td>${adseat.location!""}</td>
							<td>${adseat.adNum!""}</td>
							<!--<td>${adseat.adSize!""}</td>  -->
							<td>
	                           	<#if adseat.codeFlag?exists && adseat.codeFlag == 1>已贴</#if>
	                           	<#if adseat.codeFlag?exists && adseat.codeFlag == 0>未贴</#if>
	                        </td>
	                        <td style="width: 80px">
							<#--<a href="#" style="margin-right: 5px">数据上传</a> -->
	                            <a href="/adseat/edit?id=${adseat.id}" style="margin-right: 5px">编辑</a>
	                            <#if user.usertype !=6>
	                            <a href="javascript:deleteSeat('${adseat.id}');" style="margin-right: 5px">删除</a>
	                            <#if adseat.adCodeUrl?exists && adseat.adCode?exists>
	                            	<#if adseat.codeFlag?exists && adseat.codeFlag == 1>
			                             <a href="javascript:void(0);" onclick="updateStatus('${adseat.id}', 0);">未贴</a>
			                        </#if>
			                        <#if adseat.codeFlag?exists && adseat.codeFlag == 0>
			                            <a href="javascript:void(0);" onclick="updateStatus('${adseat.id}', 1);">已贴</a>
			                        </#if>
		                        <#else>
		                        	<a href="javascript:void(0);" onclick="generateAdCode('${adseat.id}');">生成二维码</a>
		                        </#if>
		                        </#if>
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
        
        <#-- 
        <div id="mediaSelCV" style="display:none">
        	<div class="layui-row">
           		<div class="layui-col-md2">
           			&nbsp;
           		</div>
           		<div class="layui-col-md7">
           			<span class="layui-col-md3" style="margin-top: 20px;height:30px;line-height:30px;">媒体:</span><select class="layui-col-md7" style="margin-top: 20px;height: 30px" name="mediaId" onchange="importEnabled()" id="importMediaId">
           			<@model.showAllMediaOps value="${bizObj.queryMap.mediaId?if_exists}" />
       			</select>
           		</div>
           	</div>
           	<div class="layui-row" style="margin-top:20px">
           		<div class="layui-col-md7">&nbsp;</div>
           		<div class="layui-col-md4">
           			<button style="float:left" type="button" class="layui-btn layui-btn-primary layui-btn-xs" id="insertBatchId" autocomplete="off">批量导入</button>
           		<div>
           		<div class="layui-col-md4">
				<button style="margin-left: 8px;width: 60px;" type="button" class="layui-btn layui-btn-primary layui-btn-xs" id="batchCancel" autocomplete="off">&nbsp;&nbsp;&nbsp;取消&nbsp;&nbsp;&nbsp;</button>
				</div>
           	</div>     	
        </div>
         -->
         
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

    var deleteSeat = function(id){
        layer.confirm("确认删除？", {
            icon: 3,
            btn: ['确定', '取消'] //按钮
        }, function(){
            $.ajax({
                url: "/adseat/delete",
                type: "post",
                data: {
                    "id": id,
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
                        }, function () {
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
    };
	// 查询
    $("#searchBtn").on("click", function () {
        var strParam = "";
        var name = $("#searchName").val();
        
        if (name != null && $.trim(name).length) {
            strParam = strParam + "?name=" + name;
        }

        window.location.href = "/adseat/list" + strParam;
    });

    $("#searchBtn").on("click", function() {
        $("#form").submit();
    });
    $("#clear").click(function () {
        $("#demo3 select").val("");
        $("#selectMediaId").val("");
        $("#mediaTypeParentId").val("");
        $("#mediaTypeId").val("");
        /* $('#insertBatchId').attr("disabled","disabled"); */
    });

	// 下载模板
    $('#downloadBatch').click(function(){
    	$.get('/excel/downloadBatch', function(data){
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

    var assign_ids;
    $(function() {
    	//$('#selectMediaId,#mediaTypeId,#mediaTypeParentId').searchableSelect();
        $(window).resize(function() {
            var h = $(document.body).height() - 115;
            $('.main-container').css('height', h);
        });
        $(window).resize();
        $('#selectMediaId').searchableSelect();
        $('.select').searchableSelect();
        
        <#-- 
        //批量导入打开媒体界面
        $('#batchInsert').on('click',function(){
        	 var layMediaSel=layer.open({
        		 type: 1,
        		 title:"批量导入",
                 closeBtn: false,
                 shift: 2,
                 shade: 0.8,
                 area: ['320px', '180px'], 
                 shadeClose: false,
                 content: $("#mediaSelCV")
        	 });
        });
         -->
        
        $('#batchCancel').on('click',function(){
        	layer.closeAll();
        })
    });

    $(window).resize(function() {
        var h = $(document.body).height() - 115;
        $('.main-container').css('height', h);
    });

    
    function importEnabled(){
    	var mediaId = $('#selectMediaId').val();
    	/* if(mediaId){
    		$('#insertBatchId').removeAttr("disabled");
    	} else {
    		$('#insertBatchId').attr("disabled","disabled");
    	} */
    }
    
   var isLoading = true;
    
    //批量导入
	layui.use('upload', function(){
	  var upload = layui.upload;
	  
	  //执行实例
	  var uploadInst = upload.render({
	    elem: '#batchInsert' //绑定元素 
	    ,data: {
		}
	    ,accept: 'file' //指定只允许上次文件
	    ,exts: 'xlsx|xls' //指定只允许上次xlsx和xls格式的excel文件
	    ,field: 'excelFile' //设置字段名
	    ,url: '/excel/insertBatch' //上传接口
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
	
	// 生成二维码图片
	function generateAdCode(adSeatId){
		layer.confirm("确认生成二维码？", {
            icon: 3,
            btn: ['确定', '取消'] //按钮
        }, function(){
            $.ajax({
                url: "/adseat/generateAdCode",
                type: "post",
                data: {
                    "adSeatId": adSeatId,
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
                        layer.confirm("操作成功", {
                            icon: 1,
                            btn: ['确定'] //按钮
                        }, function () {
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
    
	// 更新二维码状态
    function updateStatus(id, codeFlag) {
        if (codeFlag == 0) {
            layer.confirm("确定要更换二维码状态", {
                icon: 3,
                btn: ['确定', '取消'] //按钮
            }, function () {
                doUpdate(id, codeFlag);
            });
        } else {
            doUpdate(id, codeFlag);
        }
    }

	function doUpdate(id, codeFlag) {
        $.ajax({
            url: "/platmedia/codeFlag",
            type: "post",
            data: {
                "id": id,
                "codeFlag": codeFlag
            },
            cache: false,
            dataType: "json",
            success: function (result) {
                var resultRet = result.ret;
                if (resultRet.code == 101) {
                    layer.confirm(resultRet.resultDes, {
                        icon: 2,
                        btn: ['确定'] //按钮
                    });
                } else {
                    var msg = "";
                    if (codeFlag == "0") {
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
    
</script>
<!-- 特色内容 -->
<@model.webend />