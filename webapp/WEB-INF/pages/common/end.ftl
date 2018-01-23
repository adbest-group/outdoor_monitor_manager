	${webEnd?if_exists}
	
	<script>
	$(function(){
		$(".longTitle .data-title").hover( function(){
			$(this).addClass("hover");
		},function(){
			$(this).removeClass("hover");
		});
		
		// 修改密码
		$(".user-name").on("click", function(){
			//iframe层
			layer.open({
				type: 2,
				title: '修改密码',
				shadeClose: true,
				shade: 0.8,
				area: ['600px', '350px'],
				content: '/index/topwd' //iframe的url
			});
		});
	
		// 退出
		$('.sign-out').on('click', function(){
			layer.confirm('您确定要退出当前账号吗？', {
				icon: 3,
				btn: ['确定','取消'] //按钮
			}, function() {
				window.location.href = "/logout";
			});
		});
	});
	</script>
	</body>
</html>