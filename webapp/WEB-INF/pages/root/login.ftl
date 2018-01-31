<#assign webTitle="登录" in model>
<#assign webHead in model>
<link rel="stylesheet" type="text/css" href="${model.static_domain}/css/login.css">
<style>
	header .head h1 { margin-left: 0;position: relative;left:0px}
</style>
</#assign>
<@model.webhead />
<div class="wrapper clearfix">
		<header class="clearfix">
			<div class="head">
				<h1><img src="/static/images/logo2.png" style="width:210px;"></h1>
				<span class="login-line"></span>
			</div>
		</header>
		
		<div class="main" id="main">
			<div class="main-container" style="height: auto;">
				<div class="area">
					<div class="ban_anim_bg ban1_anim_bg"></div>
					<div class="ban_anim_bg ban2_anim_bg"></div>
					<div class="ban_anim_bg ban3_anim_bg"></div>
				</div>
				<div class="login-box-line"></div>
				<div class="login" id="login">
					<div class="hd"><img src="/static/images/logo3.png" style="height:60px;"></div>
					<div class="login-form">
						<form id="loginform" name="loginform" action="/doLogin" method="post"  style="margin:0px">
							<div class="login-main">
								<div class="inp">
								   <div class="u-icon userbg "></div>
									<input type="text" autocomplete="off" name="username" class="username" value="testkh@adbest.com" placeholder="请输入用户名" value="${userName!}">
								</div>
								<div class="inp">
									<div class="u-icon passwordbg"></div>
									<input type="password" autocomplete="off" name="password" class="password" value="123456" placeholder="请输入密码">
								</div>
								
								<input type="text" class="captcha" name="code" maxlength="5" placeholder="请输入验证码">
								<img id="num" src="/getCode?%27+(new%20Date()).getTime()" class="captcha-img" onclick="document.getElementById('num').src='/getCode?'+(new Date()).getTime()" alt="换一换" title="换一换">
								<div class="tip"><div class="s-info">${ret?if_exists}</div></div>
								<button type="submit" class="login-but">登 录</button>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>

		<footer class="clearfix">
			© 2018 浙江百泰信息技术有限公司 All Rights Reserved
		</footer>
	</div>
	<script type="text/javascript" src="${model.static_domain}/js/jquery-2.1.4.min.js"></script>
	
	<script type="text/javascript">
		$(function(){
			$(window).resize();
		});
		$(window).resize(function() {
			var h = $(window).outerHeight() - 0;
			$('.main-container').css('min-height', h);
		});
		
		$(function(){
			var c_info = $(".s-info").html();
        	if (c_info == '') {
        		$(".s-info").hide();
        	} else {
        		$(".s-info").show();
        	}
		});
	</script>
<@model.webend />
