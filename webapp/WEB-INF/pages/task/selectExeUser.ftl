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
		
		.a-title{
			float: left;
			margin-right: 20px;
			line-height: 30px;
		}
</style>
	<div class="basic-info">
		<div class="bd">
            <form id="subForm" method="post">
                <input type="hidden" id="id"/>
			<table width="100%" cellpadding="0" cellspacing="0" border="0" class="tablesorter">
				<tbody>
				
				<tr>
					<td>
						<span class="a-title">类型：</span>
						<div class="select-box select-box-140 un-inp-select ll">
	                        <select name="change" class="select" id="change" >
	                        	<option value="">请选择指派对象</option>
	                        	<option value="1">指派给媒体公司</option>
	                            <option value="2">指派给第三方监测公司</option>
	                            <option value="3">指派给媒体公司人员</option>
	                            <option value="4">指派给第三方监测公司人员</option>
	                        </select>
	                    </div>
	                </td>
				</tr>
				<#-- 选择添加媒体人员, 所属媒体 -->
				<tr>
					<td>
						<div id="mediaTr" style="display:none;">
							<span class='a-title'>媒体主：</span>
							<div class="select-box select-box-110 un-inp-select ll" >
		                        <select class="select" name="mediaId" id="mediaId">
		                        	<option value="">请选择媒体主</option>
									<@model.showAllMediaOps value="${mediaId?if_exists}" />
		                        </select>
		                    </div>
							<br/>
							<span id="mediaIdTip">&nbsp;</span>
						</div>
						<div id="mediaUserSelect" style="display:none;">
							<span class="a-title">监测人员：</span>
							<div class="select-box select-box-140 un-inp-select" style="width: 100px">
	                            <select name="mediaUser" class="select" id="mediaUser">
	                            </select>
	                        </div>
						</div>
					</td>
				</tr>
                <#-- 选择添加第三方监测人员, 所属公司 -->
				<tr >
					<td>
						<div id="companyTr" style="display:none;">
							<span class="a-title">第三方监测公司：</span>
							<div class="select-box select-box-110 un-inp-select ll" >
		                        <select class="select" name="companyId" id="companyId">
		                        	<option value="">请选择第三方监测公司</option>
									<@model.showAllThirdCompanyOps value="${companyId?if_exists}" />
		                        </select>
		                    </div>
							<br/>
							<span id="companyIdTip">&nbsp;</span>
						</div>
						<div id="companyUserSelect" style="display:none;">
							<span class="a-title">监测人员：</span>
							<div class="select-box select-box-140 un-inp-select" style="width: 100px">
	                            <select name="user" class="select" id="user">
									<#-- <#if (userList?exists&&userList?size >0)>
										<#list userList as user>
	                                        <option value="${user.id}">${user.realname}</option>
										</#list>
									</#if> -->
	                            </select>
	                        </div>
						</div>
					</td>
				</tr>
				<tr>
					<td style="text-align: center;">
						<button type="button" class="btn btn-red" autocomplete="off" id="btnSave">确定</button>
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
	
	<script>
		$(function(){
			// 下拉
			$('#user').next().find('.searchable-select-input').css('display', 'block')
            $("form").submit(function(){return false;});
		})
		
		function changeOption(){
			//显示媒体公司/第三方监测公司
			var change = $("#change").val();
			$('#mediaUserSelect').hide()
			$('#companyUserSelect').hide()
			
			console.log(change);
			if(change==1 || change==3){
				$("#mediaTr").show();
			}else{
				$("#mediaTr").hide();
			}
			if(change==2 || change==4){
				$("#companyTr").show();
			}else{
				$("#companyTr").hide();
			}
		}
		
		$('#change').searchableSelect({
			afterSelectItem: function(){
				changeOption()
			}
		})
		
		$('#mediaId').searchableSelect({
			afterSelectItem: function(){
				/*if(this.holder.data("value")){
					changeMediaId(this.holder.data("value"))
					$('#mediaUser').css('display', 'inline-block')
				}else{
					$('#mediaUser').parent().html('<select style="width: 120px;height:31px;display:none" name="mediaUser" id="mediaUser"><option value="">请选择媒体监测人员</option></select>')
				}*/
				
				var change = $("#change").val();
				if(change === '3') {
					var mediaId = $('#mediaId').val();
					
				console.log(mediaId, 'changemediaId')
					$('#mediaUserSelect').show()
					changeMediaId(mediaId)
				}
				
			}
		})
		function changeMediaId(mediaId) {	
			if(!mediaId) {
				var option = '<option value="">请选择媒体监测人员</option>';
				$("#mediaUser").html(option);
				return ;
			}
			$.ajax({
				url : '/task/selectUserExecute',
				type : 'POST',
				data : {"mediaId":mediaId},
				dataType : "json",
				traditional : true,
				success : function(data) {
					var result = data.ret;
					if (result.code == 100) {
						var adMediaTypes = result.result;
						var htmlOption = '<span class="a-title">监测人员：</span><select style="width: 120px;height:31px;" name="mediaUser" id="mediaUser"><option value="">请选择监测人员</option>';
						for (var i=0; i < adMediaTypes.length;i++) { 
							var type = adMediaTypes[i];
							htmlOption = htmlOption + '<option value="' + type.id + '">' + type.realname + '</option>';
						}
						htmlOption += '</select>'
						$("#mediaUserSelect").html(htmlOption);
						$("#mediaUser").searchableSelect()
						$('#mediaUser').next().find('.searchable-select-input').css('display', 'block')
					} else {
						alert('修改失败!');
					}
				}
			});
		}
		
		$('#companyId').searchableSelect({
			afterSelectItem: function(){
				/*if(this.holder.data("value")){
					changeCompanyId(this.holder.data("value"))
					$('#companyUser').css('display', 'inline-block')
				}else{
					$('#companyUser').parent().html('<select style="width: 120px;height:31px;display:none" name="companyUser" id="companyUser"><option value="">请选择第三方监测人员</option></select>')
				}*/
				
				var change = $("#change").val();
				if(change === '4') {
					var companyId = $('#companyId').val();
					$('#companyUserSelect').show()
					changeCompanyId(companyId)
				}
			}
		})
		function changeCompanyId(companyId) {	
			if(!companyId) {
				var option = '<option value="">请选择第三方监测人员</option>';
				$("#companyUser").html(option);
				return ;
			}
			$.ajax({
				url : '/task/selectUserExecute',
				type : 'POST',
				data : {"companyId":companyId},
				dataType : "json",
				traditional : true,
				success : function(data) {
					var result = data.ret;
					if (result.code == 100) {
						var adMediaTypes = result.result;
						var htmlOption = '<span class="a-title">监测人员：</span><select style="width: 120px;height:31px;" name="companyUser" id="companyUser"><option value="">请选择第三方监测人员</option>';
						for (var i=0; i < adMediaTypes.length;i++) { 
							var type = adMediaTypes[i];
							htmlOption = htmlOption + '<option value="' + type.id + '">' + type.realname + '</option>';
						}
						htmlOption += '</select>'
						$("#companyUserSelect").html(htmlOption);
						$("#companyUser").searchableSelect()
						$('#companyUser').next().find('.searchable-select-input').css('display', 'block')
					} else {
						alert('修改失败!');
					}
				}
			});
		}
		    
		//演示用
        $(function(){
            $("#btnSave").click(function () {
            	var change = $("#change").val();
            	console.log(change,'change');
				if(change === '1') {
					if($("#mediaId").val()){
						parent.window.selectUserExecuteHandle($("#mediaId").val(),null,null,null);
					}else{
						 layer.alert('请选择媒体公司', {icon: 5, closeBtn: 0, btn: [], title: false, time: 3000});
					}
				}else if(change === '2'){
					if($("#companyId").val()){
						parent.window.selectUserExecuteHandle(null,null,$("#companyId").val(),null);
					}else{
						 layer.alert('请选择第三方监测公司', {icon: 5, closeBtn: 0, btn: [], title: false, time: 3000});
					}
				}else if(change === '3'){
					if($("#mediaId").val() && $("#mediaUser").val()){
						parent.window.selectUserExecuteHandle($("#mediaId").val(),$("#mediaUser").val(),null,null);
					}else{
						 layer.alert('请选择媒体公司员工', {icon: 5, closeBtn: 0, btn: [], title: false, time: 3000});
					}
				}else if(change === '4'){
					if($("#companyId").val() && $("#companyUser").val()){
						parent.window.selectUserExecuteHandle(null,null,$("#companyId").val(),$("#companyUser").val());
					}else{
						 layer.alert('请选择第三方监测公司员工', {icon: 5, closeBtn: 0, btn: [], title: false, time: 3000});
					}
                	
                }
            });
        });
	</script>
