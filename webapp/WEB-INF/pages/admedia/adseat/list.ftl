<#assign webTitle="资源管理" in model> <#assign webHead in model> </#assign>
<@model.webhead />
<!-- 头部 -->
<@model.webMenu current="广告位管理" child="" />
<!-- 特色内容 -->
<div class="main-container" style="height: auto;">
	<div class="main-box">
		<div class="title clearfix" style="display: block;">
			<div class="search-box search-ll" style="margin: 0 0 0 20px">
				<button type="button" class="btn btn-red" autocomplete="off"
					onclick="window.location.href='/platmedia/adseat/edit'">新增广告位</button>
					
				<button style="margin-left: 10px" type="button" class="btn" id="insertBatchId"
					autocomplete="off" onclick="">批量导入</button>
					
				<button style="margin-left: 10px" type="button" class="btn" id="downloadBatch"
					autocomplete="off" onclick="">模板下载</button>
					
				<div style="border-bottom: 1px solid black; margin:10px auto"></div>
				<form id="form" method="get" action="/platmedia/adseat/list">
					<!--销售下拉框-->
					<div id="demo3" class="citys" style="float: left; font-size: 12px">
						<p>
							投放地区： <select style="height: 30px" id="adSeatInfo-province"
								name="province">
								<option value=""></option>
							</select> <select style="height: 30px" id="adSeatInfo-city" name="city"></select>
							<select style="height: 30px" id="adSeatInfo-region" name="region"></select>
							<select style="height: 30px" id="adSeatInfo-street" name="street"></select>
						</p>
					</div>

					<button type="button" class="btn btn-red"
						style="margin-left: 10px;" autocomplete="off" id="searchBtn">查询</button>
                    <button type="button" class="btn btn-primary"
                            style="margin-left: 10px;" autocomplete="off" id="clear">清除条件</button>
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
					    	<th>媒体方广告位编号</th>
					   		<th>区域</th>
					   		<th>主要路段</th>
					   		<th>广告位具体位置</th>
					   		<th>面数</th>
					   		<th>广告位尺寸</th> 
					   		<th>是否已贴上二维码</th>
					   		<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<#if (bizObj.list?exists && bizObj.list?size>0) > <#list
						bizObj.list as adseat>
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
							<td>${adseat.adSize!""}</td>
							<td>
                           	<#if adseat.codeFlag?exists && adseat.codeFlag == 1>已贴</#if>
                           	<#if adseat.codeFlag?exists && adseat.codeFlag == 0>未贴</#if>
                        </td>
							<td style="width: 80px">
								<#--<a href="#" style="margin-right: 5px">数据上传</a> -->
								<a href="/platmedia/adseat/edit?id=${adseat.id}" style="margin-right: 5px">编辑</a>
                                <a href="javascript:deleteSeat('${adseat.id}');" style="margin-right: 5px">删除</a>
                                <#if adseat.codeFlag?exists && adseat.codeFlag == 1>
	                                	<a href="javascript:void(0);" onclick="updateStatus('${adseat.id}', 0);">未贴</a>
	                        	</#if>
	                        	<#if adseat.codeFlag?exists && adseat.codeFlag == 0>
	                            	<a href="javascript:void(0);" onclick="updateStatus('${adseat.id}', 1);">已贴</a>
	                        	</#if>
	                        </td>
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


<script type="text/javascript">
	var deleteSeat = function(id){
	    layer.confirm("确认删除？", {
	        icon: 3,
	        btn: ['确定', '取消'] //按钮
	    }, function(){
            $.ajax({
                url: "/platmedia/adseat/delete",
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


	$("#searchBtn").on("click", function() {
		$("#form").submit();
	});
	$("#clear").click(function () {
        $("#demo3 select").val("");
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
                    $town.append('<option value> - 所有地区 - </option>');
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
		region : '${region!""}',
		onChange : function(info) {
			townFormat(info);
		}
	}, function(api) {
		var info = api.getInfo();
		townFormat(info);
	});

	$(function() {
		$(window).resize(function() {
			var h = $(document.body).height() - 115;
			$('.main-container').css('height', h);
		});
		$(window).resize();
	});

	$(function() {
		$(window).resize();
	});

	$(window).resize(function() {
		var h = $(document.body).height() - 115;
		$('.main-container').css('height', h);
	});

	var assign_ids;
	$(function() {
		$('.select').searchableSelect();
	});

	//批量导入
	layui.use('upload', function(){
	  var upload = layui.upload;
	  
	  //执行实例
	  var uploadInst = upload.render({
	    elem: '#insertBatchId' //绑定元素
	    ,data: {
		  city: function() {
		  	return $('#selectMedia').val()
		  }
		}
	    ,accept: 'file' //指定只允许上次文件
	    ,exts: 'xlsx|xls' //指定只允许上次xlsx和xls格式的excel文件
	    ,field: 'excelFile' //设置字段名
	    ,url: '/excel/insertBatch' //上传接口
	    ,done: function(res){
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
	       layer.alert('导入失败', {icon: 2, closeBtn: 0, btn: [], title: false, time: 3000, anim: 6});
	    }
	  });
	});
	
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
                    if (codeFlag == "1") {
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
</script>
<!-- 特色内容 -->
<@model.webend />
