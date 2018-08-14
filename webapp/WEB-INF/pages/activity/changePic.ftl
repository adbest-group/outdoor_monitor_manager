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
	<div class="basic-info">
		<div class="bd">
            <form id="subForm" method="post">
                <input type="hidden" id="id"/>
                <input type="hidden" id="companyId" value="${companyId?if_exists}"/>
			<table width="100%" cellpadding="0" cellspacing="0" border="0" class="tablesorter">
				<tbody>
				
				<tr>
					<span class="a-title">替换路径地址：</span>
						<div class="select-box select-box-140 un-inp-select" id="companyUserSelect">
                            <input type="text" name="filepath" id="filepath" style="width:180px;height:30px"autocomplete="off" class="form-control"/>
                        </div>
				</tr>
				<tr>
					<td class="a-title">&nbsp;</td>
					<td>
						<button type="button" class="btn btn-red" autocomplete="off" id="insertBatchSubmit">批量导入</button>
					</td>
				</tr>
			</tbody>
		</table>
            </form>
		</div>
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
	    //批量导入
		layui.use('upload', function(){
		  var upload = layui.upload;
		  var filepath = $("#filepath").val();
		  //执行实例
		  var uploadInst = upload.render({
		    elem: '#insertBatchSubmit' //绑定元素 
		    ,data: {
		    	"filepath": filepath 
			}
		    ,accept: 'file' //指定只允许上次文件
		    ,exts: 'xlsx|xls' //指定只允许上次xlsx和xls格式的excel文件
		    ,field: 'excelFile' //设置字段名
		    ,url: '/excel/insertMemoByExcel' //上传接口
		    ,before: function() {
		    	layer.msg('正在努力上传中...', {
		    		icon: 16,
		    		shade: [0.5, '#f5f5f5'],
		    		scrollbar: false,
		    		time: 300000,
		    		end: function(){
		    			layer.alert('上传超时', {icon: 2, closeBtn: 0, btn: [], title: false, time: 3000, anim: 6});
		    		}
		    	})
		    }
		    ,done: function(res){
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
		       layer.closeAll('msg')
		       layer.alert('导入失败', {icon: 2, closeBtn: 0, btn: [], title: false, time: 3000, anim: 6});
		    }
		  });
		}); 
</script>