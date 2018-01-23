<#assign webTitle="账户信息" in model>
<#assign webHead in model>
</#assign>
<@model.webhead />
	<div class="wrapper clearfix">
        <!-- 左侧菜单 -->
        <@model.menuleft current="account_info" child="account_info"/>
        <!-- 右侧主业务区 -->
        <div class="main">
            <div class="main-wrap">
                <!-- 头部 -->
                <@model.menutop />
                <div class="main-container">
					<div class="main-box basic-info">
						<div class="bd">
					<form id="subForm" method="post">
                    <table width="100%" cellpadding="0" cellspacing="0" border="0" type="">
                        <tbody>
                        	<tr>
                                <td class="a-title"><font class="s-red">*</font>客户账户：</td>
                                <td>
	                                <input type="hidden" name="id" id="id" value="${(obj.id)?if_exists}"/>
	                                <input type="hidden" name="ottvMaterial.id" id="ottvMaterial.id" value="${(obj.ottvMaterial.id)?if_exists}"/>
                                <#if (obj.id)?exists>
					               <span class="ott-user-name">${(obj.username)?if_exists}</span><a href="javascript:;" class="s-red user-name" id="change_pwd">修改密码</a>
					               <input type="hidden"  id="username" name="username" value="${(obj.username)?if_exists}"/>
					            <#else>
					               <input type="text"  id="username" name="username" value="${(obj.username)?if_exists}" autocomplete="off" class="form-control"> <span id="usernameTip"></span>
					            </#if>
					            </td>
                            </tr>
                            <#--
                           	<tr>
                                <td class="a-title"><font class="s-red">*</font>密　　码：</td>
                                <td><input type="password" id="password" name="password" value="<#if (obj.id)??>******</#if>" autocomplete="off" class="form-control"> <span id="passwordTip"></span></td>
                            </tr> -->
                              <tr>
                                <td class="a-title"><font class="s-red">*</font>公司名称：</td>
                                    
                                <td>
                                	<#if (obj.id)?exists>
						               <input type="text" id="companyName" name="companyName" value="${(obj.companyName)?if_exists}" autocomplete="off" class="form-control" disabled="disabled">
						               <input type="hidden"  id="companyName" name="companyName" value="${(obj.companyName)?if_exists}"/>
						            <#else>
						               <input type="text" id="companyName" name="companyName" value="${(obj.companyName)?if_exists}" autocomplete="off" class="form-control"> <span id="companyNameTip"></span>
						            </#if>
                                </td>
                            </tr>
                            <tr>
                                <td class="a-title"><font class="s-red">*</font>所属行业：</td>
                                <td>
                                    <div id="cm_add">
                                        <@model.showCategoryOneSelect name="categoryOne" value="" id="categoryOne" className="form-control" />
                                        <select class="form-control" id="categoryTwo" name="categoryTwo" style="width:160px;display:none;float:left; margin-left:10px;"><option value="">请选择</option></select>　<span id="categoryTwoTip" style="display: none;"></span>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="a-title" style=" vertical-align: top;"><font class="s-red">*</font>联  系  人：</td>
                                <td>
                                    <input type="text" id="name" name="name" value="${(obj.name)?if_exists}" autocomplete="off" class="form-control" disabled="disabled"> <span id="nameTip"></span>
                                </td>
                            </tr>
                            <tr>
                                <td class="a-title"><font class="s-red">*</font>联系电话：</td>
                                <td><input type="text" id="telephone" name="telephone" value="${(obj.telephone)?if_exists}" autocomplete="off" class="form-control" disabled="disabled"> <span id="telephoneTip"></span></td>
                            </tr>
                            <tr>
                                <td class="a-title" style=" vertical-align: top;"><font class="s-red">*</font>公司网址：</td>
                                <td><input type="text" class="form-control" value="${(obj.ottvMaterial.url)?if_exists}" placeholder="网站地址格式如：http://www.adtime.com" name="ottvMaterial.url" id="ottvMaterial_url" disabled="disabled"><span id="ottvMaterial_urlTip"></span>
                                </td>
                            </tr>
                            <tr>
                                <td class="a-title"><font class="s-red">*</font>详情地址：</td>
                                <td><input type="text" class="form-control" name="ottvMaterial.address" id="ottvMaterial_address" value="${(obj.ottvMaterial.address)?if_exists}" placeholder="请输入详情地址"  disabled="disabled"><span id="ottvMaterial_addressTip"></span></td>
                            </tr>
                            <tr>
                                <td class="a-title">资质情况：</td>
                                <td >
                                	<div class="data-report" style="margin-top:0;">
												<div class="bd" style="padding: 0">
													<table width="70%" cellpadding="0" cellspacing="0" border="0" class="tablesorter">
														<thead>
															<tr>
																<th>资质名称</th>
																<th>状态</th>
																<th width="60">信息</th>
															</tr>
														</thead>
														<tbody>
															<tr>
																<td>ICPC备案</td>
																<td><#if (obj.ottvMaterial.icp)?exists>已上传<#else>未上传</#if></td>
																<td>
																	<div class="pic">
																		<div class="subpic">
																			<a href="<#if (obj.ottvMaterial.icp)?exists>${(obj.ottvMaterial.icp)?if_exists}<#else>${model.static_domain}/images/300x250.gif</#if>" class="thickbox">
																				<img src="<#if (obj.ottvMaterial.icp)?exists>${(obj.ottvMaterial.icp)?if_exists}<#else>${model.static_domain}/images/300x250.gif</#if>">
																			</a>
																		</div>
																	</div>
																</td>
															</tr>
															<tr>
																<td>营业执照</td>
																<td><#if (obj.ottvMaterial.lic)?exists>已上传<#else>未上传</#if></td>
																<td>
																	<div class="pic">
																		<div class="subpic">
																			<a href="<#if (obj.ottvMaterial.lic)?exists>${(obj.ottvMaterial.lic)?if_exists}<#else>${model.static_domain}/images/300x250.gif</#if>" class="thickbox">
																				<img src="<#if (obj.ottvMaterial.lic)?exists>${(obj.ottvMaterial.lic)?if_exists}<#else>${model.static_domain}/images/300x250.gif</#if>">
																			</a>
																		</div>
																	</div>
																</td>
															</tr>
															<tr>
																<td>组织机构代码证</td>
																<td><#if (obj.ottvMaterial.org)?exists>已上传<#else>未上传</#if></td>
																<td>
																	<div class="pic">
																		<div class="subpic">
																			<a href="<#if (obj.ottvMaterial.org)?exists>${(obj.ottvMaterial.org)?if_exists}<#else>${model.static_domain}/images/300x250.gif</#if>" class="thickbox">
																				<img src="<#if (obj.ottvMaterial.org)?exists>${(obj.ottvMaterial.org)?if_exists}<#else>${model.static_domain}/images/300x250.gif</#if>">
																			</a>
																		</div>
																	</div>
																</td>
															</tr>
															
															<tr>
																<td>法人身份证</td>
																<td><#if (obj.ottvMaterial.card)?exists>已上传<#else>未上传</#if></td>
																<td>
																	<div class="pic">
																		<div class="subpic">
																			<a href="<#if (obj.ottvMaterial.card)?exists>${(obj.ottvMaterial.card)?if_exists}<#else>${model.static_domain}/images/300x250.gif</#if>" class="thickbox">
																				<img src="<#if (obj.ottvMaterial.card)?exists>${(obj.ottvMaterial.card)?if_exists}<#else>${model.static_domain}/images/300x250.gif</#if>">
																			</a>
																		</div>
																	</div>
																</td>
															</tr>
															
															
															<tr>
																<td>税务登记证</td>
																<td><#if (obj.ottvMaterial.reg)?exists>已上传<#else>未上传</#if></td>
																<td>
																	<div class="pic">
																		<div class="subpic">
																			<a href="<#if (obj.ottvMaterial.reg)?exists>${(obj.ottvMaterial.reg)?if_exists}<#else>${model.static_domain}/images/300x250.gif</#if>" class="thickbox">
																				<img src="<#if (obj.ottvMaterial.reg)?exists>${(obj.ottvMaterial.reg)?if_exists}<#else>${model.static_domain}/images/300x250.gif</#if>">
																			</a>
																		</div>
																	</div>
																</td>
															</tr>
															
															
															<tr>
																<td>其他</td>
																<td><#if (obj.ottvMaterial.etc)?exists>已上传<#else>未上传</#if></td>
																<td>
																	<div class="pic">
																		<div class="subpic">
																			<a href="<#if (obj.ottvMaterial.etc)?exists>${(obj.ottvMaterial.etc)?if_exists}<#else>${model.static_domain}/images/300x250.gif</#if>" class="thickbox">
																				<img src="<#if (obj.ottvMaterial.etc)?exists>${(obj.ottvMaterial.etc)?if_exists}<#else>${model.static_domain}/images/300x250.gif</#if>">
																			</a>
																		</div>
																	</div>
																</td>
															</tr>
														</tbody>
													</table>
												</div>
											</div>
                                    
                                </td>
                            </tr>
                            <#-- <tr>
										<td class="a-title">&nbsp;</td>
										<td>
										<input type="submit" id="commit" class="btn btn-red" value="保存"/>
										</td>
							</tr> -->
                        </tbody>
                    </table>
                    </form>
                      </div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<script type="text/javascript" src="${model.static_domain}/js/jquery-2.1.4.min.js"></script>
	<!-- 下拉 -->
	<link href="${model.static_domain}/js/select/jquery.searchableSelect.css" rel="stylesheet">
	<script src="${model.static_domain}/js/select/jquery.searchableSelect.js"></script>
	<script src="${model.static_domain}/js/ajaxfileupload.js"></script>
	<!-- 图片缩放 -->
	<script type="text/javascript" src="${model.static_domain}/js/jquery.resize.js"></script>
	<script type="text/javascript" src="${model.static_domain}/js/jquery.thickbox.js"></script>
	
	<!-- formValidator -->
    <link type="text/css" rel="stylesheet" href="${model.static_domain}/js/formValidator/style/validator.css"></link>
    <script type="text/javascript" src="${model.static_domain}/js/formValidator/formValidator-4.0.1.js"></script>

	<script type="text/javascript">
		$(window).resize(function() {
			var h = $(document.body).height() - 115;
			$('.main-container').css('height', h);
		});
		
	    $(function(){
	        $(".nav-sidebar>ul>li").on("click",function(){
	            $(".nav-sidebar>ul>li").removeClass("on");
	            $(this).addClass("on");
	        });
			$(window).resize();
	    });
		$(function(){
			$("#categoryOne").on("change", function(){
	        	var categoryOne = $("#categoryOne").val();
	        	selectCategory(categoryOne);
			});
            
            <#--禁用右键-->
			$(document).ready(function() { 
				$(document).bind("contextmenu",function(e) {
					return false; 
				}); 
			}); 

			
			<#--禁用浏览器Backspace-->
			$(document).keydown(function(e){   
				var keyEvent;   
				if(e.keyCode==8){   
					var d=e.srcElement||e.target;
				    if(d.tagName.toUpperCase()=='INPUT'||d.tagName.toUpperCase()=='TEXTAREA'){
				        keyEvent=d.readOnly||d.disabled;   
				    }else{
				        keyEvent=true;   
				    }
				}else{
				    keyEvent=false;   
				}
				if(keyEvent){   
				    e.preventDefault();   
				}   
			});
			
			<#--禁用submit提交-->
			$(document).ready(function(){
				$("form").submit(function(e){
				    e.preventDefault();
				});
			});
			var id = $("#id").val();
			<#--校验：绑定了submit,使用ajax提交，所以要禁用submit的form提交-->	
			$(document).ready(function() {
            
				$.formValidator.initConfig({
					validatorGroup:"2",
					submitButtonID: "commit",
			        formID: "subForm",
			        debug: false,
			        submitOnce: true,
			        errorFocus: false,
			        onError: function(msg, obj, errorlist) {
			            $("#errorlist").empty();
			            $.map(errorlist,
			            function(msg) {
			                $("#errorlist").append("<li>" + msg + "</li>")
			            });
			        },
					
			        onSuccess: function() {
			   			$.ajax({
			       			url: "/customer/save",
			       			type: "post",
			       			data: $('#subForm').serialize(),
			                cache: false,
			                dataType: "json",
			       			success: function(data){
			       				var code=data.ret.code;
			       				if(code =100) {
			       					layer.confirm("保存成功", {
										icon: 1,
										btn: ['确定'] //按钮
									}, function(){
										window.location.href="/customer/list";
									});
			       				} else {
			       					layer.confirm(data.ret.resultDes, {
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
			    if(id == null || id.length==0){
			    // 客户帐户check
	        	$("#username").formValidator({
	        		validatorGroup:"2",
	        		onShow:"",
	        		onFocus:"请输入您的邮箱",
	        		onCorrect:"　"
	        	}).inputValidator({
	        		min:1,
	        		max:100,
	        		onError:"邮箱格式不正确，请重新输入！"
	        	}).regexValidator({
	        		regExp:"^([\\w-.]+)@(([[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.)|(([\\w-]+.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(]?)$",
	        		onError:"邮箱格式不正确，请重新输入！"
	        	}).ajaxValidator({
	        		type: "post",
	        		dataType: "json",
			        async: true,
			        url: "/customer/isExistsName",
			        success: function(result) {
			            if (result.ret.code == 100) {
			                return false;
			            }
			            return true;
			        },
			        buttons: $("#button"),
			        error: function(jqXHR, textStatus, errorThrown) {
                 		layer.confirm("服务忙，请稍后再试", {
							icon: 5,
							btn: ['确定'] //按钮
						});
			        },
			        onError: "该客户帐户已存在，请修改",
			        onWait: "正在对客户帐户进行校验，请稍候..."
	        	  });
	        	}
	        	// 密码check
	        	$("#password").formValidator({
	        		validatorGroup:"2",
	        		onShow:"",
	        		onFocus:"请输入6-16位密码",
	        		onCorrect:""
	        	}).inputValidator({
	        		min:6,
	        		max:16,
	        		empty:{leftEmpty:false,rightEmpty:false,emptyError:"密码两边不能有空符号"},
	        		onError:"密码输入不正确，请重新输入！"
	        	});
	        	// 公司名称check
	        	$("#companyName").formValidator({
	        	validatorGroup:"2",
	        		onShow:"",
	        		onFocus:"请输入公司名称名称",
	        		onCorrect:""
	        	}).inputValidator({
	        		min:1,
	        		max:40,
	        		empty:{leftEmpty:false,rightEmpty:false},
	        		onError:"公司名称不能为空，请输入！"
	        	});
	        	
	        	$("#categoryTwo").formValidator({
	        		validatorGroup:"2",
	        		onShow:"",
	        		onFocus:"请选择所属行业",
	        		onCorrect:""
	        	}).inputValidator({
	        		min:1,
	        		onError:"请选择有效的行业！"
	        	});
	    		// 联系人
	    		$("#name").formValidator({
	    			validatorGroup:"2",
	    			onShow:"",
	    			onFocus:"请输入联系人",
	    			onCorrect:""
	    		}).regexValidator({
	    			regExp:"^\\S+$",
	    			onError:"联系人不能为空，请输入！"
	    		});
	    		// 联系电话
	        	$("#telephone").formValidator({
	       		 	validatorGroup:"2",
	        		onShow:"",onFocus:"请输入你的手机或固定电话，固定电话格式如：0571-88888888",onCorrect:""
	        	}).regexValidator({
	        		regExp:["^(([0\\+]\\d{2,3}-)?(0\\d{2,3})-)?(\\d{7,8})(-(\\d{3,}))?$","^(13|15|17|18)[0-9]{9}$"],
	        		onError:"手机或电话格式不正确，请重新输入！"
	        	});
	        	// 公司网址
	    		$("#ottvMaterial_url").formValidator({
	    			validatorGroup:"2",
	    			onShow:"",
	    			onFocus:"请输入公司网站",
	    			onCorrect:""
	    		}).regexValidator({
	    			regExp:"^\\S+$",
	    			onError:"公司网站不能为空，请输入！"
	    		});
	        	// 详情地址
	        	$("#ottvMaterial_address").formValidator({
	        		validatorGroup:"2",
	        		onShow:"",
	        		onFocus:"请输入详情地址",
	        		onCorrect:""
	        	}).inputValidator({
	        		min:1,empty:{leftEmpty:true,rightEmpty:true,emptyError:""},onError:"详情地址不能为空，请输入！"
	        	});
			});
		});
		
		function selectCategory(categoryOne) {
			
        	if (categoryOne == "") {
        		$("#categoryTwo").css("display", "none");
        		$("#categoryTwo").empty();
              	return;
            } else {
        		$("#categoryTwo").css("display", "");
        		$("#categoryTwo").empty();
            }
            
        	$.post(
   				"/dict/getCategoryTwoAjax",
       			{
       				"categoryOne":categoryOne
       			},
   				function(data){
       				var code=data.ret.code;
			      	var desc=data.ret.resultDes;
			      	var result=data.ret.result;
			      	if(code=100){
			      		$("#categoryTwo").append('<option value="">请选择</option>');
			      		for(var i=0 ;i<result.length;i++){
			        		var ca = "${(obj.ottvMaterial.category)?if_exists}";
			      		     if(ca != null && ca.length>0){
			      		        if(ca == result[i].key ){
				        		   	$("#categoryTwo").append('<option selected value="'+result[i].key+'">'+result[i].value+'</option>');
			      		        }else{
				        			$("#categoryTwo").append('<option value="'+result[i].key+'">'+result[i].value+'</option>');
			      		           
			      		        }
			      		     }else{
				        		$("#categoryTwo").append('<option value="'+result[i].key+'">'+result[i].value+'</option>');
			      		     }
			         	}
	               	}
       			}
   			);
		}
		
		$(function(){
		  init();
		});
		function init(){
		  <#if (obj.ottvMaterial.category)?exists>
	        <#assign dict=cacheHelper.getDict(obj.ottvMaterial.category) />
	        <#if dict.parentKey?exists>
					$("#categoryOne").val("${(dict.parentKey)?if_exists}");
					var categoryOne = $("#categoryOne").val();
					if(categoryOne == ''){
				    	$("#categoryOneTip").html("请选择广告行业!");
				    	$("#categoryOneTip").css("color","red");
				    }else{
				    	$("#categoryOneTip").html("&nbsp;&nbsp;");
				    }
					selectCategory(categoryOne);
	        </#if>
	        </#if>
		}
	</script>
	
	<script type="text/javascript">
        $("#date_val").click(function(){
            if($(".dsp-select").hasClass("hover")){
                $(".dsp-select").removeClass("hover")
                $(".time-conditions").hide();
            }else{
                $(".dsp-select").addClass("hover")
                $(".time-conditions").show();
            }
        });
	
		function uploadPic(id){
			var picName = $("#"+id).val();
			if(!/\.(jpg|JPG)$/.test(picName)) {
				layer.confirm("图片类型必须是jpg格式", {
					icon: 0,
					btn: ['确定'] //按钮
				});
	          return false;
	        }
		 	$.ajaxFileUpload({  
                 url:'/upload',
                 secureuri:false,
                 fileElementId:id,
                 dataType: 'json',
                 success: function (data, status) {
                 			var ret = data.ret;
                 	if(data == '"error"'){
                 		layer.confirm("上传图片失败", {
							icon: 2,
							btn: ['确定'] //按钮
						});
                 		return;
                 	}else if(data=='"overPic"'){
                 		layer.confirm("上传图片太大！请小于1MB", {
							icon: 0,
							btn: ['确定'] //按钮
						});
                 	
                 	}else if(data == '"notPic"'){
                 		layer.confirm("上传的不是图片", {
							icon: 0,
							btn: ['确定'] //按钮
						});
                 		return;
                 	}else{
	                 	var arr=data.split('"'); 
		                var dataNew=arr[1];
		                $("#"+id+"-bak").val(dataNew);
		                $("#"+id+"-img").attr('src',dataNew);
                 	}
                 },
                 error: function (data, status, e) {
             			layer.confirm("上传图片失败", {
							icon: 5,
							btn: ['确定'] //按钮
						});
                 }
             });
		}
    </script>
<@model.webend />