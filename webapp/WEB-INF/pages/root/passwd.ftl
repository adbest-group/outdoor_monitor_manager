<link rel="stylesheet" type="text/css" href="${model.static_domain}/css/new_main.css">
	<style type="text/css">
		html, body{ min-width: auto; }
		.basic-info .bd .a-title{ width: 100px; }
		.basic-info .bd td .form-control{ width: 220px; }
		.role-nav-authority li{ float: left; overflow: hidden; margin-bottom: 5px; height: 25px;}
		.role-nav-authority li label input[type=checkbox]{ width: 15px; height: 15px; margin: 2px 3px 0 0; vertical-align: text-top;}
	</style>

<div class="basic-info">
	<div class="bd">
		<table width="100%" cellpadding="0" cellspacing="0" border="0" class="tablesorter">
			<tbody>
				<tr>
					<td class="a-title">　原密码：</td>
					<td><input type="password" name="platUser_passWord" autocomplete="off" id="platUser_passWord" class="form-control"> <span id="platUser_passWordTip"></span></td>
				</tr>
				<tr>
					<td class="a-title">　新密码：</td>
					<td><input type="password" name="platUser_passWord1" autocomplete="off" id="platUser_passWord1" class="form-control"> <span id="platUser_passWord1Tip"></span></td>
				</tr>
				<tr>
					<td class="a-title">确认密码：</td>
					<td><input type="password" name="platUser_passWord2" autocomplete="off" id="platUser_passWord2" class="form-control"> <span id="platUser_passWord2Tip"></span></td>
				</tr>
				<tr>
					<td class="a-title">&nbsp;</td>
					<td><button type="button" class="btn btn-red" autocomplete="off" id="platUser_submit_ok">提　交</button></td>
				</tr>
			</tbody>
		</table>
	</div>
</div>

<script type="text/javascript" src="${model.static_domain}/js/jquery-2.1.4.min.js"></script>
<link type="text/css" rel="stylesheet" href="${model.static_domain}/js/formValidator/style/validator.css" />
<script type="text/javascript" src="${model.static_domain}/js/formValidator/formValidator-4.0.1.js"></script>
<script type="text/javascript" src="${model.static_domain}/js/layer/layer.js"></script>
<script>
		$(function(){
			$.formValidator.initConfig({errorFocus: false,submitButtonID:"platUser_submit_ok"});
	        $("#platUser_passWord").formValidator({
	        	onShow:"",onFocus:"密码不能小于6位",onCorrect:""}).inputValidator({
	        	min:6,empty:{leftEmpty:false,rightEmpty:false,emptyError:"密码两边不能有空符号"},onError:"输入有误,请重新输入！"});
			$("#platUser_passWord1").formValidator({
				onShow:"",onFocus:"密码不能小于6位",onCorrect:""}).inputValidator({
				min:6,empty:{leftEmpty:false,rightEmpty:false,emptyError:"密码两边不能有空符号"},onError:"输入有误,请重新输入！"});
			$("#platUser_passWord2").formValidator({
				onShow:"",onFocus:"密码不能小于6位",onCorrect:""}).inputValidator({
				min:6,empty:{leftEmpty:false,rightEmpty:false,emptyError:"重复密码两边不能有空符号"},onError:"输入有误,请重新输入！"}).compareValidator({
				desID:"platUser_passWord1",operateor:"=",onError:"两次密码不一致"});
				
			$("#platUser_submit_ok").on("click",function(){
				var passWordTip = $("#platUser_passWordTip").css("display");
				var passWord1Tip = $("#platUser_passWord1Tip").css("display");
				var passWord2Tip = $("#platUser_passWord2Tip").css("display");
				if (passWordTip == "none" && passWord1Tip == "none" && passWord2Tip == "none") {
					var password = $("#platUser_passWord").val();
					var password1 = $("#platUser_passWord1").val();
					var password2 = $("#platUser_passWord2").val();
					$.ajax({
						url : "/index/updatepwd",
						type : "post",
						data : {
							"password" : password,
							"password1" : password1,
							"password2" : password2
						},
						cache: false,
						dataType : "json",
						success : function(result) {
							var ret = result.ret;
							if (ret.code == 100) {
								layer.confirm(ret.resultDes, {
									icon: 1,
									btn: ['确定'] //按钮
								}, function(){
									$("#pwd_box").hide();
									//layer.closeAll();
									parent.location.reload();
								});
							} else {
								layer.confirm(ret.resultDes, {
									icon: 2,
									btn: ['确定'] //按钮
								});
							}
						},
						error : function(e) {
							layer.confirm("服务忙，请稍后再试", {
								icon: 5,
								btn: ['确定'] //按钮
							});
						}
					});
				}
			});
		});
	</script>
