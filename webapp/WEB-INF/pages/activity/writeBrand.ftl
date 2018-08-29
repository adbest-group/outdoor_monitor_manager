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
		#exportPdf,#search{font-size: 16px;}
		#exportPdf a{margin-top: 5px;margin-left: 10px}
		#searchForm{padding-left: 10px;padding-top: 25px;}
		#search a{margin-top: 5px;margin-left: 10px}
		#searchRes li{margin-left: 20px;}
</style>
	<div id="searchForm"></div>
	<div class="basic-info">
		<div class="bd">
            <form id="subForm" method="post">
                <input type="hidden" id="id"/>
                <input type="hidden" id="activityId" value="${activityId?if_exists}"/>
                <input type="hidden" id="taskreport" value="${taskreport?if_exists}"/>
			<table width="100%" cellpadding="0" cellspacing="0" border="0" class="tablesorter">
				<tbody>
				
				<tr>
					<span class="a-title">品牌：</span>
						<div class="select-box select-box-140 un-inp-select" id="brand">
                            <input type="text" name="brandName" id="brandName" style="width:180px;height:30px"autocomplete="off" class="form-control"/>
                        </div>
				</tr>
				<tr>
					<span class="a-title">pdf标题：</span>
						<div class="select-box select-box-140 un-inp-select" id="title">
                            <input type="text" name="titleName" id="titleName" style="width:180px;height:30px"autocomplete="off" class="form-control"/>
                        </div>
				</tr>
				<tr>
					<td class="a-title">&nbsp;</td>
					<td>
						<button type="button" class="btn btn-red" autocomplete="off" id="submit">确定</button>
					</td>
				</tr>
			</tbody>
		</table>
            </form>
		</div>
	</div>
	<div style="display: none;" id="search">
		<ul id="searchRes"></ul>
	</div>
	<script type="text/javascript" src="${model.static_domain}/js/jquery-2.1.4.min.js"></script>
	<script type="text/javascript" src="${model.static_domain}/js/layer/layer.js"></script>
	<script type="text/javascript" src="${model.static_domain}/js/common.js"></script>
	<!-- 下拉 -->
	<link href="${model.static_domain}/js/select/jquery.searchableSelect.css" rel="stylesheet">
	<script src="${model.static_domain}/js/select/jquery.searchableSelect.js"></script>
	<!-- formValidator -->
	<link type="text/css" rel="stylesheet" href="${model.static_domain}/js/formValidator/style/validator.css"></link>
	<script type="text/javascript" src="${model.static_domain}/js/formValidator/formValidator-4.0.1.js"></script>
	<script src="${model.static_domain}/js/ajaxfileupload.js"></script>
	<script>
	var id = $("#id").val();
	$("#submit").click(function () {
		var isLoading =false;
		 var brandName = $("#brandName").val();
		 var titleName = $("#titleName").val();
		 var activityId = $("#activityId").val();
		 var taskreport = $("#taskreport").val();
		 $.ajax({
            url: "/pdf/exportAdMediaPdf",
            type: "post",
            data: {
                "activityId" : activityId,
                "taskreport": taskreport,
                "brandName" : brandName,
	            "titleName" : titleName
            },
            cache: false,
            dataType: "json",
            beforeSend: function () {
			    isLoading = true;
			    layer.msg('正在生成PDF文件...', {
		    		icon: 16,
		    		shade: [0.5, '#f5f5f5'],
		    		scrollbar: false,
		    		time: 300000
		    	}, function(){
		    		if(isLoading){
		    			layer.alert('生成超时', {icon: 2, closeBtn: 0, btn: [], title: false, time: 3000, anim: 6});
		    		}
	    		})
			},
            success: function(datas) {
		  		isLoading = false;
	    		layer.closeAll('msg')
                var resultRet = datas.ret;
                if (resultRet.code == 101) {
                    layer.confirm(resultRet.resultDes, {
                        icon: 2,
                        btn: ['确定'] //按钮
                    });
                } else {
                    layer.alert('导出成功', {icon: 1, closeBtn: 0, btn: [], title: false, time: 800});
		    		//window.open(resultRet.result);
		    		if(resultRet.result.files.length <= 1){
			    		var newA = document.createElement("a");
				        newA.id = 'gg'
				        newA.target = '_blank';
				        newA.href = resultRet.result.domainPath + resultRet.result.files[0];
				        document.body.appendChild(newA);
				        newA.click();
		    		}else{
		    			var searchForm = "<div class='search-box search-ll' style='margin: 0 0 10px 20px'>" + 
		    						"<form class='navbar-form navbar-right'>" +
										"<div class='form-group'>" +
											"<div class='inp'>" +
						                    	"<input type='text' placeholder='请输入搜索内容' id='searchText' name='name'>" +
						                	"</div>" +
										"</div>" +
										"<button type='button' class='btn btn-red' style='margin-left:10px;' autocomplete='off' id='searchBth' onclick='search()'>查询</button>" +
									"</form></div>";
		    			$('#searchForm').html(searchForm);
		    		    var newdiv = "<div id = 'exportPdf'><ul>";
		    			for (var i=0;i<resultRet.result.files.length;i++){
		    				newdiv += "<li><a target='_blank' href='" + resultRet.result.domainPath + resultRet.result.files[i] + "'>" + resultRet.result.files[i] + "</a></li>"
						}
						newdiv += '</ul></div>';
						$(".basic-info").html(newdiv);
		    		}
		    		
                }
            },
            error: function(e) {
		  		isLoading = false;
	    		layer.closeAll('msg')
                layer.confirm("服务忙，请稍后再试", {
                    icon: 5,
                    btn: ['确定'] //按钮
                });
            }
        });
	 });
	 
	//pdf结果搜索
  	var searchTextChange = function() {
		var searchText = $('#searchText').val();
		var $searchTr = "";
		$('#searchRes').html("");
		if(searchText == "") {
		 	$('.basic-info').show();
			$('#search').hide();
			return;
		}
		  //筛选搜索
		  $searchTr = $('#exportPdf').find('li:contains(' + searchText + ')');
		  $searchTr.each(function(i, e) {
		  console.log(e);
		  	$('#searchRes').append($(e).clone(true));
		  });
		  //如果没有搜索结果 显示一个报错div
		  if($searchTr.length <= 0) {
		  	$('#searchRes').html('<div>没有报告</div>')
		  }
		  
		  $('.basic-info').hide();
		  $('#search').show();
	};
	// 查询
    var search = function () {
		searchTextChange();
    };
</script>