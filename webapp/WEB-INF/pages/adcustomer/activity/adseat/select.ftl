<link rel="stylesheet" type="text/css" href="${model.static_domain}/css/new_main.css">
<link rel="stylesheet" type="text/css" href="${model.static_domain}/css/icon_fonts.css">
<style type="text/css">
		html, body{ min-width: 100%;overflow:auto; }
		.basic-info .bd .a-title{ width: 120px;font-size:14px; }
		
		.basic-info .bd td label{ margin-right:20px; display: inline-block; font-size:14px;}
		.basic-info .bd td span{font-size:14px;}
		.basic-info .bd .formZone li.city-item label{ margin-right: 0; }
		.file-upload{position: relative;overflow: hidden; float: left;}
		.file-upload a,.interaction-bac-big a{color:#ee5f63;text-decoration:underline;}
		.file-upload input{position: absolute;left: 0; top: 0; opacity: 0;font-size: 50px; width: 60px;}
		.interaction-bac-big img{width: 296px;height: 194px;float: left}
		.interaction-bac-big label{height: 194px;line-height: 194px;display: block;float: left;padding-left: 15px}
</style>
<div class="main-container" style="height: auto; overflow-x:auto">
    <div class="main-box">
        <div class="title clearfix" style="display: block;">
            <div class="search-box search-ll" style="margin: 0 0 0 20px;display: inline-block;">
                <form id="form" style="display: inline-block;">			
                	<input type="hidden" id="startDate" value="" name="startDate">
                	<input type="hidden" id="endDate" value="" name="endDate">
                	<input type="hidden" id="seatIds" value="" name="seatIds">
                	<input type="hidden" id="start" value="" name="start">
                	<input type="hidden" id="size" value="" name="size">
                	<input type="hidden" id="mediaTypeIdHidden" <#-- value="${bizObj.queryMap.mediaTypeId?if_exists}" -->>
                
                    <div id="demo3" class="citys" style="float: left; font-size: 12px">
                        <p>
                            投放地区： <select style="height: 30px" id="adSeatInfo-province"
                                          name="province">
                            <option value=""></option>
                        </select> <select style="height: 30px" id="adSeatInfo-city" name="city"></select>      
                        </p>
                    </div>

                    <div style="float: left; margin-left: 40px; font-size: 12px">
                        媒体主: <select style="height: 30px" name="mediaId" id="selectMediaId">
                        <option value="">所有媒体</option>
                        <@model.showAllMediaOps <#-- value="${bizObj.queryMap.mediaId?if_exists}" --> />
                    </select>
                    </div>
                    
                    <div style="float: left; margin-left: 40px; font-size: 12px">
                        媒体大类: <select style="height: 30px" name="mediaTypeParentId" id="mediaTypeParentId" onchange="changeMediaTypeId();">
                        <option value="">请选择媒体大类</option>
						<@model.showAllAdMediaTypeAvailableOps <#-- value="${bizObj.queryMap.mediaTypeParentId?if_exists}" -->/>
                    </select>
                    </div>
                    
                    <div style="float: left; margin-left: 40px; font-size: 12px">
                        媒体小类: <select style="height: 30px" name="mediaTypeId" id="mediaTypeId">
                        <option value="">请选择媒体小类</option>
                    </select>
                    </div>
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
                   	     <th width="30"><input type="checkbox" id='thead-checkbox' name="ck-alltask" value=""/></th>                    
                        <th width="30">序号</th>
                        <th>广告位名称</th>
                        <th>区域</th>
                        <th>主要路段</th>
                        <th>详细位置</th>
                        <th>媒体主</th>
                        <th>媒体大类</th>
						<th>媒体小类</th>
                    </tr>
                    </thead>
                    <tbody id="tbody">
                    <#if bizObj?exists>
					<#if (bizObj.list?exists && bizObj.list?size>0) > 
						<#list bizObj.list as adseat>
	                    <tr>
	                       <td width="30"><input  type="checkbox"  name="ck-task" value="${adseat.id}"/></td>  
	                        <td>${(bizObj.page.currentPage-1)*20+adseat_index+1}</td>
	                        <td>${adseat.name!""}</td>
	                        <td>${vm.getCityName(adseat.province)!""} ${vm.getCityName(adseat.city!"")}</td>
	                        <td>${adseat.road!""}</td>
							<td>${adseat.location!""}</td>
	                        <td>${adseat.mediaName!""}</td>
	                        <td>${adseat.parentName!""}</td>
	                        <td>${adseat.secondName!""}</td>
	                    </tr>
					</#list> <#else>
                    <tr>
                        <td colspan="20">没有相应结果。</td>
                    </tr>
					</#if>
                    </tbody>
                    
                    <!-- 翻页 
					<@model.showPage url=vm.getUrlByRemoveKey(thisUrl+'', ["start",
					"size"]) p=bizObj.page parEnd="" colsnum=9 />-->
					</#if>
                </table>
                
					<tr><div id="selectPage" style='text-align: right;'></div></tr>
                <tr>
                    <td class="a-title">&nbsp;</td>
                    <td style='text-align: center;'>
                        <button type="button" class="btn btn-red" autocomplete="off" id="btnSave">保　存</button>
                        <button type="button" class="btn btn-primary" autocomplete="off" id="btnCancel">关  闭</button>
                    </td>
                </tr>
            </div>
        </div>
    </div>
	<script type="text/javascript" src="${model.static_domain}/js/jquery-2.1.4.min.js"></script>
	<script type="text/javascript" src="${model.static_domain}/js/layer/layer.js"></script>
	<script type="text/javascript" src="${model.static_domain}/js/common.js"></script>
	<!-- 下拉 -->
	<link href="${model.static_domain}/js/select/jquery.searchableSelect.css" rel="stylesheet">
	<script src="${model.static_domain}/js/select/jquery.searchableSelect.js"></script>
	<!-- 时期 -->
	<link href="${model.static_domain}/js/date/daterangepicker.css" rel="stylesheet">
	<script type="text/javascript" src="${model.static_domain}/js/date/moment.min.js"></script>
	<script type="text/javascript" src="${model.static_domain}/js/date/jquery.daterangepicker.js"></script>
	<script type="text/javascript" src="${model.static_domain}/js/date.js"></script>
	<!-- formValidator -->
	<link type="text/css" rel="stylesheet" href="${model.static_domain}/js/formValidator/style/validator.css"></link>
	<!--文件上传-->
	<script type="text/javascript" src="${model.static_domain}/js/formValidator/formValidator-4.0.1.js"></script>
	<script src="${model.static_domain}/js/ajaxfileupload.js"></script>
	<script type="text/javascript" src="/static/js/jquery.citys.js"></script>
	<link type="text/css" rel="stylesheet" href="/static/js/layer/layui.css"></link>
	<script type="text/javascript" src="/static/js/layer/layui.js"></script>
	<script type="text/javascript">
	var dts = parent.$('#dts').val();
	var dt = parent.$('#dt').val();
	var seatIds = parent.getSeatIds();
	var params = {
		startDate: dts,
		endDate: dt	
	};
	getData()
	function getData(){
		params.seatIds = parent.getSeatIds();

		$.post('/customer/activity/adseat/select', params, function(res){
			console.log(res);
			var curr = res.bizObj.page.currentPage
			renderTableData(res.bizObj.list, curr)
			layui.use('laypage', function(){
				var laypage = layui.laypage;
				laypage.render({
				    elem: 'selectPage'
				    ,count: res.bizObj.count
				    ,limit: 20
				    ,curr: curr
				    ,layout: ['page', 'count']
				    ,jump: function(obj, first){
				    	if(!first){
				    		
					        //obj包含了当前分页的所有参数，比如：
					        // $('#start').val((obj.curr - 1) * obj.limit);
					        // $('#size').val(obj.limit);
					        params.start = (obj.curr - 1) * obj.limit;
					        params.size = obj.limit;
				    		getData();
					        // $("#form").submit();
			    	    }
				        
				      }
				  });
			})
		})
	}
	
	
	// 渲染表格数据
	function renderTableData(data, currentPage){
		var len = data.length;
		var html = '';
		if(len === 0){
			html += '<tr><td colspan="20">没有相应结果。</td></tr>'
		}else{
			for(var i = 0; i < len; i++){
				html += '<tr><td width="30"><input  type="checkbox"  name="ck-task" value="'+ data[i].id +'"/></td><td>'+((currentPage-1)*20 + i + 1)+'</td><td>'+(data[i].name?data[i].name:"")+'</td><td>'+(data[i].provinceName?data[i].provinceName:"") + (data[i].cityName?data[i].cityName:"")+'</td><td>'+(data[i].road?data[i].road:"")+'</td><td>'+(data[i].location?data[i].location:"")+'</td><td>'+(data[i].mediaName?data[i].mediaName:"")+'</td><td>'+(data[i].parentName?data[i].parentName:"")+'</td><td>'+(data[i].secondName?data[i].secondName:"")+'</td></tr>';
			}
		}
		$('#tbody').html(html);

		$("input[name='ck-alltask']").removeAttr("checked");
		$("input[name='ck-task']").change(function(){
	        if($(this).is(":checked")){
	        	if($("input[name='ck-task']:checked").length === $("input[name='ck-task']").length && $("input[name='ck-task']").length != 0){
	        		$("input[name='ck-alltask']").prop("checked",true)
	        	}
	        	parent.addModData({
	        		id: $(this).val(),
	        		html: $(this).parents('tr').html().split('</td>').slice(2).join('</td>')
	        	})
	        }else{
	        	parent.addDelData({
	        		id: $(this).val(),
	        		html: $(this).parents('tr').html().split('</td>').slice(2).join('</td>')
	        	})
	        	$("input[name='ck-alltask']").removeAttr("checked");
	        }
	        console.log(parent.getCheck())
		});
		setCheckData();
	}
	
	function changeMediaTypeId() {
		params.mediaTypeId = ''
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
					$("#mediaTypeId").val($("#mediaTypeIdHidden").val());
				} else {
					alert('修改失败!');
				}
			}
		});
	}
    $("#clear").click(function () {
        $("#demo3 select").val("");
        $("#selectMediaId").val("");
         $("#mediaTypeParentId").val("");
          $("#mediaTypeId").val("");
        $('#insertBatchId').attr("disabled","disabled");
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
    
    $("input[name='ck-alltask']").change(function(){
	    if(!this.value){
	        if($(this).is(":checked")){
	            $("input[name='ck-task']").each(function(){
	            	if(!$(this).is(":checked")){
		            	$(this).prop("checked",true)
		            	parent.addModData({
		            		id: $(this).val(),
	        				html: $(this).parents('tr').html().split('</td>').slice(2).join('</td>')
		            	})
	            	}
	            })
	        }else{
	            $("input[name='ck-task']").each(function(){
	            	if($(this).is(":checked")){

		            	$(this).removeAttr("checked");
		            	parent.addDelData({
		            		id: $(this).val(),
	        				html: $(this).parents('tr').html().split('</td>').slice(2).join('</td>')
		            	})
	            	}
	            })
	        }
	    }
	});
    
    
    $("#btnCancel").click(function(){
        parent.window.layer.closeAll();
    });
    
    // 选择媒体时查询
    $('#selectMediaId').change(function(){
		// $("#form").submit();
		params.start = 0;
		params.mediaId = $('#selectMediaId').val();
		getData();
		parent.setModData([])
		parent.setDelData([])
    })
    
    // 选择直辖市时查询
    $('#adSeatInfo-province').change(function(){
    	//北京市：110000
    	//天津市：120000
    	//上海市：310000
    	//重庆市：500000
    	// 香港: 810000
    	// 澳门: 820000
    	if('110000,120000,310000,500000,810000,820000'.indexOf($(this).val()) >= 0){
    		// $("#form").submit();
			params.start = 0;
    		params.province = $('#adSeatInfo-province').val();
			getData();
    		parent.setModData([])
    		parent.setDelData([])
    	}
    })
    
    // 选择城市时查询
    $('#adSeatInfo-city').change(function(){
		// $("#form").submit();
		params.start = 0;
		params.city = $('#adSeatInfo-city').val();
		getData();
		parent.setModData([])
		parent.setDelData([])
    })
    
    // 选择媒体大类时查询
    $('#mediaTypeParentId').change(function(){
		// $("#form").submit();
		params.start = 0;
		console.log(params)
		params.mediaTypeParentId = $('#mediaTypeParentId').val();
		getData();
		parent.setModData([])
		parent.setDelData([])
    })
    
    // 选择媒体小类时查询
    $('#mediaTypeId').change(function(){
		// $("#form").submit();
		params.start = 0;
		params.mediaTypeId = $('#mediaTypeId').val();
		getData();
		parent.setModData([])
		parent.setDelData([])
    })

    
	
	
    setCheckData()
	
	// 分页跳转后初始化已选的数据
	function setCheckData(){
		var arr = parent.getCheck()
	    for(var i = 0; i < arr.length; i++){
	    	var $el = $("input[name='ck-task'][value='"+ arr[i].id +"']")
	    	if($el[0]){
		    	$el.prop("checked", true)
	    	}
	    }
		
		var modArr = parent.modDataArr
		for(var i = 0; i < modArr.length; i++){
	    	var $el = $("input[name='ck-task'][value='"+ modArr[i].id +"']")
	    	if($el[0]){
		    	$el.prop("checked", true)
	    	}
	    }
		var delArr = parent.delDataArr

		for(var i = 0; i < delArr.length; i++){
	    	var $el = $("input[name='ck-task'][value='"+ delArr[i].id +"']")
	    	if($el[0]){
		    	$el.removeAttr("checked")
	    	}
	    }
		
		if($("input[name='ck-task']:checked").length === $("input[name='ck-task']").length && $("input[name='ck-task']").length != 0){
    		$("input[name='ck-alltask']").prop("checked",true)
    	}
	}
	
	$('#btnSave').click(function(){
		var len = parent.modDataArr.length
		var len2 = parent.delDataArr.length

		for(var i = 0; i < len; i++) {
			parent.addCheck(parent.modDataArr[i])
		}
		
		for(var i = 0; i < len2; i++) {
			parent.removeCheck(parent.delDataArr[i].id)
		}
		
		parent.getCheckboxData()
		//关闭当前窗口
	    var index = parent.layer.getFrameIndex(window.name);
	    parent.layer.close(index);
	})
	
	$('#btnCancel').click(function(){
		//关闭当前窗口
	   var index = parent.layer.getFrameIndex(window.name);
	   parent.layer.close(index);
	})

	</script>
