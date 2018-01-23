<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>第三方平台投放链接生成界面</title>
	<link rel="stylesheet" type="text/css" href="${model.static_domain}/css/style.css?v=3.0">
	<style type="text/css">
		.link-gen{ pointer-events:auto;  background-color: #fff;}
		.link-gen .data-report .bd tr td{ text-align: left; font-size: 14px;}
		.link-gen .black-hd{ padding:0 80px 0 20px;height:42px;line-height:42px;border-bottom:1px solid #eee;font-size:14px;color:#333;overflow:hidden;text-overflow:ellipsis;white-space:nowrap;background-color:#F8F8F8; position: relative; margin-bottom: 13px;}
		.link-gen .black-hd .black-closed:hover{ color: #333;}
		.link-gen .data-report .bd .form-control{ height: 17px; width: 200px;}
	</style>
</head>
<body>
	<!-- 第三方平台投放链接生成界面 -->
	<div class="link-gen">
		<div class="black-hd"><label id="layerTitle">第三方平台投放链接生成界面</label></div>
		<div class="data-report">
			<div class="bd">
				<table width="100%" cellpadding="0" cellspacing="0" border="0" class="tablesorter">
					<tbody>
						<tr>
							<td>出　价：默认以创意ID对应的单元出价进行计费</td>
						</tr>
						<tr>
							<td>点击率：<input type="text" id="click_rate_down" name="click_rate_down" autocomplete="off" class="form-control" placeholder="点击率下限" style="width: 80px;">
							 - 
							<input type="text" id="click_rate_up" name="click_rate_up" autocomplete="off" class="form-control" placeholder="点击率上限" style="width: 80px;">						
							</td>
						</tr>
						<tr>
							<td>投标价（分）：<input type="text" id="tender_price" name="tender_price" autocomplete="off" class="form-control"> <span id=""></span></td>
						</tr>
						<tr>
							<td>中标价（分）：<input type="text" id="Middle_price" name="Middle_price" autocomplete="off" class="form-control"> <span id=""></span></td>
						</tr>
						<tr class="blacklist">
							<td>创意ID：<input type="text" id="createId" name="createId" autocomplete="off" class="form-control" style="width: 60px;" maxLength="9"> 
							<input type="text" id="" name="" autocomplete="off" placeholder="http://" class="form-control or-url"> <span id=""></span>
							<button type="button" class="btn btn-primary" autocomplete="off" id="">生成投放链接</button> 
							<button type="button" class="btn btn-success" autocomplete="off">复制</button><p class="copy-info"></p></td>
						</tr>
						<tr class="or-add">
							<td>创意ID：<button type="button" class="btn btn-success" autocomplete="off" id="or_add_btn">添　加</button></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<script type="text/javascript" src="${model.static_domain}/js/jquery-1.11.3.min.js"></script>
	<link type="text/css" rel="stylesheet" href="${model.static_domain}/js/formValidator/style/validator.css"></link>
    <script type="text/javascript" src="${model.static_domain}/js/formValidator/formValidator-4.0.1.js"></script>
    	<!-- common.js -->
	<script type="text/javascript" src="${model.static_domain}/js/common.js"></script>
		<!-- 弹出框 -->
	<script type="text/javascript" src="${model.static_domain}/js/layer/layer.js"></script>
	

	<script type="text/javascript">
		$(function () {
            var _this = $('.blacklist');
            //添加
            var _html = '<tr>'+ _this.html() + '</tr>';
            $('#or_add_btn').click(function () {
                $('.or-add').before(_html);
            });      
           $(document).on('click','.btn-success',function(){
				var _this=$(this);
				var coppy=(_this.siblings(".or-url").val());
				if(coppy==""){
				return
				}
				var _copy=(_this.siblings(".or-url"));
	           	_copy.select();
				document.execCommand("Copy");
				_this.siblings(".copy-info").text("已经复制到粘贴板!你可以使用Ctrl+V 贴到需要的地方去了哦!");

           }); 
           
            $(document).on('click','.btn-primary',function(){
				var _this=$(this);
	           var createId=(_this.siblings(".form-control").first().val());
	           var  rate1=$("#click_rate_down").val();
	           var  rate2=$("#click_rate_up").val();
	           var  bidPrice=$("#tender_price").val();
	           var  middlePrice =$("#Middle_price").val();
	           $.ajax({
						url: "/throwThree/getIp",
		                type: "post",
						data: {
							"createId":createId,
							"rate1":rate1,
							"rate2":rate2,
							"bidPrice":bidPrice,
							"middlePrice":middlePrice,
						},
		                cache: false,
		                dataType: "json",
		                success: function(result) {
		                	if (result == "error") {
							alert("请正确填写");								
		                	}else if(result =="createNull"){
		                	alert("该创意Id不存在或初审未通过");
		                	} else {
							_this.siblings(".or-url").val(result);	
		                	}
		                },
		            });
           })           
        });
		$("#createId,#click_rate_down,#click_rate_up,#tender_price,#Middle_price").keyup(function(){    
			$(this).val($(this).val().replace(/[^0-9]/g,''));
		}).bind("paste",function(){  //CTR+V事件处理    
			$(this).val($(this).val().replace(/[^0-9]/g,''));
		}).css("ime-mode", "disabled"); //CSS设置输入法不可用
        
        
              
	</script>
</body>
</html>