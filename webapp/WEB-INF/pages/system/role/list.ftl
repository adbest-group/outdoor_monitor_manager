<#assign webTitle="角色管理" in model>
<#assign webHead in model>
    <link rel="stylesheet" type="text/css" href="${model.static_domain}/css/style.css?v=3.0">
    <link rel="stylesheet" type="text/css" href="${model.static_domain}/css/fonts.css?v=3.0">
</#assign>
<@model.webhead />
	<div class="wrapper clearfix">
		<@model.menutop />
		<@model.menuleft current="system" />
		<div class="main" id="main">
			<div class="side clearfix" style="width:85px;height:60px;float:left;"></div>
			<div class="main-container" style="height: auto;">
				<!-- 投放数据 -->
				<div class="ty-effect analysis">
	                <ul class="nav-tabs clearfix">
	                    <!--
	                    <li>
	                        <a href="/system/message/list">消息管理</a>
	                    </li>
	                    <li>
	                        <a href="/system/site/list">网站管理</a>
	                    </li>
	                    <li>
	                        <a href="/system/link/list">外部链接管理</a>
	                    </li>
	                    -->
	                    <li>
	                        <a href="/system/account/list">账户管理</a>
	                    </li>
	                    <li class="on">
	                        <a href="/system/role/list">角色管理</a>
	                    </li>
	                   <!-- <li>
	                        <a href="/system/log/list">操作记录</a>
	                    </li>-->
	                </ul>
             		<!-- 筛选 -->
                    <div class="bd">
                    	<a href="javascript:void(0);" class="btn btn-primary" id="add" style="margin:0 0 13px 13px;">新增角色</a>
						<!-- 数据报表 -->
	                    <div class="data-report">
	                        <div class="bd">
	                            <table width="100%" cellpadding="0" cellspacing="0" border="0" class="tablesorter">
									<thead>
										<tr>
											<th>序号</th>
											<th>角色名称</th>
											<th>角色描述</th>
											<th>操作</th>
										</tr>
									</thead>
								<#list bizObj.list  as l>
									<tbody>
										<tr>
											<td width="30">${(bizObj.page.currentPage-1)*20 + l_index+1}</td>
											<td>${l.name}</td>
											<td>${l.desc}</td>
											<td>
											<#if l.userId??>
											<a href="javascript:" title="编辑"  onclick="editRole('${l.id}')">编辑</a>　
												<!--<a href="javascript:" title="删除"  onclick="deleteRole('${l.id}')">删除</a>-->
											</td>
											<#else>
												不允许删除										
											</#if>
										</tr>
								</#list>
									</tbody>
									<!-- 翻页 -->
								    <@model.showPage url=vm.getUrlByRemoveKey(thisUrl, ["start", "size"]) p=bizObj.page parEnd="" colsnum=4 />
								</table>
	                        </div>
	                    </div>
	                </div>
                </div>
                <div class="hold-bottom" style="height:30px"></div>
            </div>
        </div>
		<!-- 新增角色 -->
		<div class="black-overlay">
		<div class="black-box" style="width:520px;">
		<form id="accountForm" action="" method="post">
			<div class="black-hd"><span  id="showTable">新增角色</span> <a class="black-closed" href="javascript:;">×</a></div>
			<div class="data-report">
				<div class="bd">
					<table width="100%" cellpadding="0" cellspacing="0" border="0" class="tablesorter">
						<tbody>
							<tr>
								<td>角色名称：<input type="text" name="name"  class="form-control"  id="name" maxLength="10"> <span id="nameTip"></span></td>
								<input type="hidden" id="roleId">
							</tr>
							<tr>
								<td>角色描述：<textarea class="form-control" style="resize: none; width:200px;" id="desc"></textarea> <span id="descTip"></span></td>
							</tr>
							<tr>
								<td><span>权限配置：</span>
									<ul class="nav-authority" id="checkBoxUl">
									</ul><br /><span id="checkBoxUlTip">
								</td>
							</tr>
							<tr>
								<td><button type="button" class="btn btn-primary" autocomplete="off" id="submit_ok1">提　交</button>　<button type="button" class="btn btn-default" autocomplete="off">取　消</button></td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
			</form>
		</div>
	</div>
	<script type="text/javascript" src="${model.static_domain}/js/jquery-1.11.3.min.js"></script>
	<!-- 弹出框 -->
	<script type="text/javascript" src="${model.static_domain}/js/layer/layer.js"></script>
	<!-- common.js -->
	<script type="text/javascript" src="${model.static_domain}/js/common.js"></script>
	<!-- formValidator -->
	<link type="text/css" rel="stylesheet" href="${model.static_domain}/js/formValidator/style/validator.css"></link>
	<script type="text/javascript" src="${model.static_domain}/js/formValidator/formValidator-4.0.1.js"></script>	

	<script type="text/javascript">
		$(function(){
			// 新增角色
			$('#add').on("click", function(){
				$("#showTable").html("新增角色");
				$("#roleId").val("");
				$('#name').removeAttr("readonly");
				$.ajax({
					url : "/system/role/list/showMenu",
					type : "post",					
					cache: false,
					dataType : "json",
					success : function(result) {
						var ret = result.ret;
						if (ret.code == 100) {
							var checkBox= "";
							for(var i=0; i<ret.result.length; i++){
								var sysMenu = ret.result[i];
								checkBox = checkBox + '<li><label><input type="checkbox" name="menu" value="' + sysMenu.id + '" value=""> ' + sysMenu.text + '</label></li>';
							}
							$(".nav-authority").html(checkBox);
							$(".black-overlay").show();
							$(".black-overlay .form-control").val('');
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
				
			$.formValidator.initConfig({
				validatorGroup:"2",
		        submitButtonID: "submit_ok1",
		        debug: false,
		        submitOnce: true,
		        errorFocus: false,
		        onError: function(msg, obj, errorlist) {
		            $("#errorlist").empty();
		            $.map(errorlist,
		            function(msg) {
		                $("#errorlist").append("<li>" + msg + "</li>")
		            });
		            //layer.alert(msg);
		        },
		        onSuccess: function(){
		        var  name=$("#name").val();
				var  desc=$("#desc").val();
				var roleId=$("#roleId").val();
				var menu="";
				 $("input[name='menu']:checked").each(function(i) {
					 	if (0==i) {
					 		menu = $(this).val();
						} else {
						    menu = menu + "," + $(this).val();
						}
						});
		            $.ajax({
			           	url : "/system/role/list/addRole",
						type : "post",
						data : {
							"name" : name,
			    			"desc" : desc,   
			    			"menu" : menu,
			    			"roleId" : roleId,     
								},
			                cache: false,
			                dataType: "json",
						success : function(result) {
						var ret = result.ret;
						if (ret.code == 100) {
							layer.confirm(ret.resultDes, {
								icon: 1,
								btn: ['确定'] //按钮
							}, function(){
								window.location.href = "/system/role/list";
							});
						} else {
							layer.confirm(ret.resultDes, {
								icon: 2,
								btn: ['确定'] //按钮
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
		        },
		        submitAfterAjaxPrompt: '有数据正在异步验证，请稍等...'
		    });
		    // 权限配置
        	$("#checkBoxUl").formValidator({
        		validatorGroup:"2",
        		onShow:"",
        		onFocus:"",
        		onCorrect:""
        	}).functionValidator({
        		fun:function(){
        			if ($("input[name='menu']:checked").size()>0) {
        				return true;
        			} else {
        				return "请选择权限";
        			}
        		}
        	});
		    // 角色名称check
		    $("#name").formValidator({
				validatorGroup:"2",
		        onShow: "",
		        onFocus: "请输入角色名称",
		        onCorrect: "　"
		    }).inputValidator({
		        min:1,
        		max:10,
        		onError:"请输入1-10位文字的角色名称"
		    }).regexValidator({
        		regExp:"^\\S+$",
        		onError:"角色名称不能为空"
        	}).ajaxValidator({
        		type: "post",
		        dataType: "json",
		        async: true,
		         url: "/system/isExistsRoleName",
		        buttons: $("#button"),
		        success: function(result) {
		            if (result.ret.code == 100) {
		                return true;
		            }
		            return false;
		        },
		        error: function(jqXHR, textStatus, errorThrown) {
             		layer.confirm("服务忙，请稍后再试", {
						icon: 5,
						btn: ['确定'] //按钮
					});
		        },
		        onError: "已存在该角色名称，请修改",
		        onWait: "正在对角色名称进行校验，请稍候..."
		    });
				    $("#desc").formValidator({
				validatorGroup:"2",
		        onShow: "",
		        onFocus: "请输入角色描述",
		        onCorrect: "　"
		    }).inputValidator({
		        min:1,
        		max:100,
        		onError:"请输入1-100位文字角色描述"
		    }).regexValidator({
        		regExp:"^\\S+$",
        		onError:"角色描述不能为空"
        	})		
			});
			//关闭
			$(".black-closed,.btn-default").on('click', function(){
				$(".black-overlay").hide();
				$(".black-overlay .form-control").val('');
				$('#price_money').show()
				$('#price_ok').hide();
			});
			});
			
		//编辑角色
		function editRole(id){
					$.ajax({
					url : "/system/role/list/editRole",
					type : "post",					
					cache: false,
					dataType : "json",
					data : {
						"roleId" : id,
					},
					success : function(result) {
						var ret = result.ret;
						if (ret.code == 100) {
							var checkBox= "";
							for(var i=0; i<ret.result.length; i++){
								var sysMenu = ret.result[i];
								var flag=1;
							for(var j=0;j<result.roleMenu.length;j++){
							var roleMenu=result.roleMenu[j];
							if(sysMenu.id==roleMenu.menuId){
							flag=2;
							}
							}	
							if(flag==1){
							checkBox = checkBox + '<li><label><input type="checkbox" name="menu" value="' + sysMenu.id + '" > ' + sysMenu.text + '</label></li>';
							}else{						
							checkBox = checkBox + '<li><label><input type="checkbox" name="menu" value="' + sysMenu.id + '" checked> ' + sysMenu.text + '</label></li>';
							}
							
							}
							$(".nav-authority").html(checkBox);						
							$("#name").val(result.sysRole.name).attr("readonly","readonly");									
							$("#desc").val(result.sysRole.desc);
							$("#showTable").html("编辑角色");
							$("#roleId").val(id);
							$(".black-overlay").show();
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
				
				
				$.formValidator.initConfig({
				validatorGroup:"2",
		        submitButtonID: "submit_ok1",
		        debug: false,
		        submitOnce: true,
		        errorFocus: false,
		        onError: function(msg, obj, errorlist) {
		            $("#errorlist").empty();
		            $.map(errorlist,
		            function(msg) {
		                $("#errorlist").append("<li>" + msg + "</li>")
		            });
		            //layer.alert(msg);
		        },
		        onSuccess: function(){
		        var  name=$("#name").val();
				var  desc=$("#desc").val();
				var roleId=$("#roleId").val();
				var menu="";
				 $("input[name='menu']:checked").each(function(i) {
					 	if (0==i) {
					 		menu = $(this).val();
						} else {
						    menu = menu + "," + $(this).val();
						}
						});
		            $.ajax({
			           	url : "/system/role/list/addRole",
						type : "post",
						data : {
							"name" : name,
			    			"desc" : desc,   
			    			"menu" : menu,
			    			"roleId" : roleId,     
								},
			                cache: false,
			                dataType: "json",
						success : function(result) {
						var ret = result.ret;
						if (ret.code == 100) {
							layer.confirm(ret.resultDes, {
								icon: 1,
								btn: ['确定'] //按钮
							}, function(){
								window.location.href = "/system/role/list";
							});
						} else {
							layer.confirm(ret.resultDes, {
								icon: 2,
								btn: ['确定'] //按钮
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
		        },
		        submitAfterAjaxPrompt: '有数据正在异步验证，请稍等...'
		    });
		  	// 权限配置
        	$("#checkBoxUl").formValidator({
        		validatorGroup:"2",
        		onShow:"",
        		onFocus:"",
        		onCorrect:""
        	}).functionValidator({
        		fun:function(){
        			if ($("input[name='menu']:checked").size()>0) {
        				return true;
        			} else {
        				return "请选择权限";
        			}
        		}
        	});
		    // 登录账户check
		    $("#name").formValidator({
				validatorGroup:"2",
		        onShow: "",
		        onFocus: "请输入角色名称",
		        onCorrect: "　"
		    }).inputValidator({
		        min:1,
        		max:10,
        		onError:"请输入1-10位文字的角色名称"
		    }).regexValidator({
        		regExp:"^\\S+$",
        		onError:"角色名称不能为空"
        	})
		    
		    $("#desc").formValidator({
				validatorGroup:"2",
		        onShow: "",
		        onFocus: "请输入角色描述",
		        onCorrect: "　"
		    }).inputValidator({
		        min:1,
        		max:100,
        		onError:"请输入1-100位文字角色描述"
		    }).regexValidator({
        		regExp:"^\\S+$",
        		onError:"角色描述不能为空"
        	})		
		}
		//删除角色
		function  deleteRole(roleId){
		 	layer.confirm("确定要删除该账户", {
				icon: 3,
				btn: ['确定', '取消'] //按钮
			}, function(){
			$.ajax({
					url : "/system/role/list/deleteRole",
					type : "post",					
					cache: false,
					dataType : "json",
					data : {
						"roleId" : roleId,
					},
					success : function(result) {
						var ret = result.ret;
						if (ret.code == 100) {
							layer.confirm(ret.resultDes, {
								icon: 1,
								btn: ['确定'] //按钮
							}, function(){
								window.location.href = "/system/role/list";
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
		})
		}
	</script>
<@model.webend />