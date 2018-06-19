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
                <form id="form" method="get" action="/customer/activity/adseat/select" style="display: inline-block;">			
                	<input type="hidden" id="startDate" value="" name="startDate">
                	<input type="hidden" id="endDate" value="" name="endDate">
                	<input type="hidden" id="seatIds" value="" name="seatIds">
                	<input type="hidden" id="start" value="" name="start">
                	<input type="hidden" id="size" value="" name="size">
                	<input type="hidden" id="mediaTypeIdHidden" value="${bizObj.queryMap.mediaTypeId?if_exists}">
                
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
                        <@model.showAllMediaOps value="${bizObj.queryMap.mediaId?if_exists}" />
                    </select>
                    </div>
                    
                    <div style="float: left; margin-left: 40px; font-size: 12px">
                        媒体大类: <select style="height: 30px" name="mediaTypeParentId" id="mediaTypeParentId" onchange="changeMediaTypeId();">
                        <option value="">请选择媒体大类</option>
						<@model.showAllAdMediaTypeAvailableOps value="${bizObj.queryMap.mediaTypeParentId?if_exists}"/>
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
                    <tbody>
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
	
	$('#startDate').val(parent.$('#dts').val());
	$('#endDate').val(parent.$('#dt').val());
	$('#seatIds').val(parent.getSeatIds());
	var count = parseInt(${count?if_exists})
	var curr = parseInt(${start?if_exists})
	
	layui.use('laypage', function(){
		var laypage = layui.laypage;
		laypage.render({
		    elem: 'selectPage'
		    ,count: count
		    ,limit: 20
		    ,curr: curr + 1
		    ,layout: ['page', 'count']
		    ,jump: function(obj, first){
		    	if(!first){

			        //obj包含了当前分页的所有参数，比如：
			        $('#start').val((obj.curr - 1) * obj.limit)
			        $('#size').val(obj.limit)
			        
			        $("#form").submit();
	    	    }
		        
		      }
		  });
	})
	
	
	
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
                url : 'http://passer-by.com/data_location/town/' + info['code']
                + '.json',
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
    
    // 存储当前筛选状态下选中的广告位
    var modDataArr = []
    
    $("input[name='ck-alltask']").change(function(){
	    if(!this.value){
	        if($(this).is(":checked")){
	            $("input[name='ck-task']").each(function(){
	            	if(!$(this).is(":checked")){
		            	$(this).prop("checked",true)
		            	modDataArr.push({
		            		id: $(this).val(),
	        				html: $(this).parents('tr').html().split('</td>').slice(2).join('</td>')
		            	})
	            	}
	            })
	        }else{
	            $("input[name='ck-task']").each(function(){
	            	if($(this).is(":checked")){

		            	$(this).removeAttr("checked");
		            	parent.addDelData($(this).val())
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
		$("#form").submit();
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
    		$("#form").submit();
    	}
    })
    
    // 选择城市时查询
    $('#adSeatInfo-city').change(function(){
		$("#form").submit();
    })
    
    // 选择媒体大类时查询
    $('#mediaTypeParentId').change(function(){
		$("#form").submit();
    })
    
    // 选择媒体小类时查询
    $('#mediaTypeId').change(function(){
		$("#form").submit();
    })

    $("input[name='ck-task']").change(function(){
        if($(this).is(":checked")){
        	if($("input[name='ck-task']:checked").length === $("input[name='ck-task']").length && $("input[name='ck-task']").length != 0){
        		$("input[name='ck-alltask']").prop("checked",true)
        	}
        	modDataArr.push({
        		id: $(this).val(),
        		html: $(this).parents('tr').html().split('</td>').slice(2).join('</td>')
        	})
        }else{
        	parent.addDelData($(this).val())
        	$("input[name='ck-alltask']").removeAttr("checked");
        }
        console.log(parent.getCheck())
	});
	
	
    setCheckData()
	
	// 分页跳转后初始化已选的数据
	function setCheckData(){
		var arr = parent.getCheck()
	    for(var i = 0; i < arr.length; i++){
	    	var $el = $("input[name='ck-task'][value='"+ arr[i].id +"']")
	    	if($el[0]){
		    	$el.prop("checked",true)
	    	}
	    }
		
		if($("input[name='ck-task']:checked").length === $("input[name='ck-task']").length && $("input[name='ck-task']").length != 0){
    		$("input[name='ck-alltask']").prop("checked",true)
    	}
	}
	
	$('#btnSave').click(function(){
		var len = modDataArr.length
		var len2 = parent.delDataArr.length

		for(var i = 0; i < len2; i++) {
			parent.removeCheck(parent.delDataArr[i])
		}
		for(var i = 0; i < len; i++) {
			parent.addCheck(modDataArr[i])
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
