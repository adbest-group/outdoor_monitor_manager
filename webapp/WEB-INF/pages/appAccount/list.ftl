<#assign webTitle="账户管理" in model>
<#assign webHead in model>
</#assign>
<@model.webhead />
<!-- 头部 -->
<@model.webMenu current="账户管理" child="appAccount" />
<div class="main-container" style="height: auto;">
    <div class="main-box ott-market">
        <div class="title clearfix">
            <button href="javascript:;" class="add-new-btn ll" id="add_media"><i></i> 新建App账号</button>
            <div class="search-box search-ll" style="margin: 0 0 0 20px">
            	<form id="form" method="get" action="/appAccount/list">
	            	<div class="select-box select-box-100 un-inp-select ll">
	                    <select name="searchUserType" class="select" id="searchUserType">
	            			<option value="">所有类型</option>
	                        <option value="2" <#if (bizObj.queryMap.usertype?exists&&bizObj.queryMap.usertype == '2')>selected</#if>>客户人员</option>
	                    	<option value="3" <#if (bizObj.queryMap.usertype?exists&&bizObj.queryMap.usertype == '3')>selected</#if>>媒体人员</option>
	                    	<option value="4" <#if (bizObj.queryMap.usertype?exists&&bizObj.queryMap.usertype == '4')>selected</#if>>社会人员</option>
	                    	<option value="5" <#if (bizObj.queryMap.usertype?exists&&bizObj.queryMap.usertype == '5')>selected</#if>>第三方监测人员</option>
	                    </select>
	                </div>
	                <div class="inp">
	                    <input type="text" value="${nameOrUsername?if_exists}" placeholder="登录账户" id="nameOrUsername" name="nameOrUsername">
	                </div>
	                
	                <button type="button" class="btn btn-red" autocomplete="off" id="searchBtn">查询</button>
	                
	                <button style="margin-left: 10px" type="button" class="btn" id="batchInsert" autocomplete="off">批量导入</button>
					<button style="margin-left: 10px" type="button" class="btn" id="downloadBatch" autocomplete="off" onclick="">模板下载</button>
	            </form>
            </div>
        </div>

        <!-- 数据报表 -->
        <div class="data-report">
            <div class="bd">
                <table width="100%" cellpadding="0" cellspacing="0" border="0" class="tablesorter" id="plan">
                    <thead>
                    <tr>
                        <th>序号</th>
                        <th>App账户</th>
                        <th>账户类型</th>
                        <th>所属公司</th>
                        <th>姓名</th>
                        <th>联系电话</th>
                        <th>状态</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#if (bizObj.list?exists && bizObj.list?size>0)>
                        <#list bizObj.list as user>
                        <tr>
                            <td width="30">${(bizObj.page.currentPage-1)*20+user_index+1}</td>
                            <td>${user.username?if_exists}</td>
                            <td>${vm.getUserExecuteTypeText(user.usertype)?if_exists}</td>
                            <td>
                            	<#if user.usertype?exists&&user.usertype==3>${user.mediaName?if_exists}</#if>
                            	<#if user.usertype?exists&&user.usertype==5>${user.companyName?if_exists}</#if>
                            </td>
                            <td>${user.realname?if_exists}</td>
                            <td>${user.mobile?if_exists}</td>
                            <td><span onclick="updStatus('${user.id}', '${user.status}');"
                                   class="switch<#if user.status?exists && user.status == 1> current</#if>"><s></s><b></b></span>
                            </td>
                            <td>
                                <a href="javascript:void(0);" onclick="edit('${user.id}');">编辑</a>
                                <#if user.usertype==3 || user.usertype==4 || user.usertype==5>
                                	<a href="javascript:void(0);" onclick="details('${user.id}');">详情</a>
                                </#if>
                                <#--<a href="javascript:void(0);" onclick="deleteAccount('${partnerUser.id}');">删除</a>-->
                            </td>
                        </tr>
                        </#list>
                    </#if>
                    </tbody>
                    <!-- 翻页 -->
                <@model.showPage url=vm.getUrlByRemoveKey(thisUrl, ["start", "size"]) p=bizObj.page parEnd="" colsnum=7 />
                </table>
            </div>
        </div>
        
        <div id="mediaSelCV" style="display:none">
        	<table width="100%" cellpadding="0" cellspacing="0" border="0" class="tablesorter">
				<tbody>
					<tr>
						<td class="a-title">媒体：</td>
						<td>
						    <div class="select-box select-box-100 un-inp-select ll">
			                    <select class="select" name="mediaId" id="importMediaId">
			                    	<@model.showAllMediaOps value="${bizObj.queryMap.mediaId?if_exists}" />
			                    </select>
			                </div>
							<br/>
							<span id="importMediaIdTip">&nbsp;</span>
	                    </td>
					</tr>
					<tr>
						<td class="a-title">登录密码：</td>
						<td>
							<div>
							  <input type="password" id="password" name="password" autocomplete="off" class="form-control"> 
							</div>
							<br/>
							<span id="passwordTip"></span>
						</td>
					</tr>
					<tr>
						<td class="a-title">&nbsp;</td>
						<td>
							<button type="button" class="btn btn-red" autocomplete="off" onclick="checkVal(this)">导  入</button>
							<button type="button" class="btn btn-red" autocomplete="off" style="visibility: hidden" id="insertBatchSubmit">导  入</button>
						</td>
					</tr>
				</tbody>
			</table>
        </div>
        
    </div>
</div>
</div>
</div>
</div>
<style>
	#mediaSelCV .tablesorter{
		width: auto;
    	margin: 100px auto 0;
	}
	#mediaSelCV .tablesorter .a-title{
		text-align: right;
		padding-right: 20px;
	}
</style>
<link href="${model.static_domain}/js/select/jquery.searchableSelect.css" rel="stylesheet">
<script src="${model.static_domain}/js/select/jquery.searchableSelect.js"></script>

<script type="text/javascript">
$(function(){
    $(window).resize();
});

$(window).resize(function() {
    var h = $(document.body).height() - 115;
    $('.main-container').css('height', h);
});

	$('.select').searchableSelect();
	$('#importMediaId').next().find('.searchable-select-input').css('display', 'block');

    $("#add_media").on("click", function () {
        //iframe层
        layer.open({
            type: 2,
            title: '编辑账户',
            shadeClose: true,
            shade: 0.8,
            area: ['600px', '480px'],
            content: '/appAccount/edit' //iframe的url
        });
    });
    
    //批量导入打开媒体界面
    $('#batchInsert').on('click',function(){
    	 layer.open({
    		 type: 1,
    		 title:"批量导入",
             shift: 2,
             shade: 0.8,
             area: ['600px', '480px'], 
             shadeClose: false,
             content: $("#mediaSelCV")
    	 });
    });
    
    function edit(id) {
        layer.open({
            type: 2,
            title: '编辑账户',
            shadeClose: true,
            shade: 0.8,
            area: ['600px', '480px'],
            content: '/appAccount/edit?id=' + id //iframe的url
        });
    }

	function details(id) {
        layer.open({
            type: 2,
            title: '账户详情',
            shadeClose: true,
            shade: 0.8,
            area: ['890px', '480px'],
            content: '/appAccount/details?id=' + id //iframe的url
        });
    }
    
    $("#searchBtn").on("click", function () {
        $("#form").submit();
    });

    // 删除账户
    function deleteAccount(id) {
        layer.confirm("确定要删除该账户", {
            icon: 3,
            btn: ['确定', '取消'] //按钮
        }, function () {
            $.ajax({
                url: "/deleteAccount",
                type: "post",
                data: {
                    "id": id
                },
                cache: false,
                dataType: "json",
                success: function (datas) {
                    var resultRet = datas.ret;
                    if (resultRet.code == 101) {
                        layer.confirm(resultRet.resultDes, {
                            icon: 2,
                            btn: ['确定'] //按钮
                        });
                    } else {
                        layer.confirm("删除成功", {
                            icon: 1,
                            btn: ['确定'] //按钮
                        }, function () {
                            window.location.href = "/system/account/list?nameOrUsername=" + $("#nameOrUsername").val();
                        });
                    }
                },
                error: function (e) {
                    layer.confirm("服务忙，请稍后再试", {
                        icon: 5,
                        btn: ['确定'] //按钮
                    });
                }
            });
        });
    }

    // 更新账户状态
    function updStatus(id, status) {
        if (status == "1") {
            layer.confirm("确定要停用该账户", {
                icon: 3,
                btn: ['确定', '取消'] //按钮
            }, function () {
                doUpdate(id, status);
            });
        } else {
            doUpdate(id, status);
        }
    }

    function doUpdate(id, status) {
    	var toStatus;
    	if(status == 1){
    		toStatus = 2;
    	} else {
    		toStatus = 1;
    	}
    
        $.ajax({
            url: "/appAccount/updateAccountStatus",
            type: "post",
            data: {
                "id": id,
                "status": toStatus
            },
            cache: false,
            dataType: "json",
            success: function (datas) {
                var resultRet = datas.ret;
                if (resultRet.code == 101) {
                    layer.confirm(resultRet.resultDes, {
                        icon: 2,
                        btn: ['确定'] //按钮
                    });
                } else {
                    var msg = "";
                    if (toStatus == "1") {
                        msg = "启用成功";
                    } else {
                        msg = "停用成功";
                    }
                    layer.confirm(msg, {
                        icon: 1,
                        btn: ['确定'] //按钮
                    }, function () {
                        window.location.reload();
                    });
                }
            },
            error: function (e) {
                layer.confirm("服务忙，请稍后再试", {
                    icon: 5,
                    btn: ['确定'] //按钮
                });
            }
        });
    }
    
    function checkVal(that){
	   var mediaId = $('#importMediaId').val();
  	   var password = $('#password').val();
  	  
  	   if(mediaId == null || mediaId == "" || mediaId.length <= 0){
  	  	layer.confirm("请选择媒体", {
  			icon: 2,
  			btn: ['确定'] //按钮
  		});
  	  	return false;
  	   }
  	  
  	  if(password == null || password == "" || password.length <= 0){
  	  	layer.confirm("请填写密码", {
  			icon: 2,
  			btn: ['确定'] //按钮
  		});
  	  	return false;
  	  }
  	  
  		if(/^\*{6}|(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$/.test(password)) {
		   
	    } else {
		  layer.confirm("输入的密码格式不规范", {
			 icon: 2,
			 btn: ['确定'] //按钮
		  });
	  	  return false;
	   }
  	  
  		$(that).next().click()
	}
    
    //批量导入
	layui.use('upload', function(){
	  var upload = layui.upload;
	  
	  //执行实例
	  var uploadInst = upload.render({
	    elem: '#insertBatchSubmit' //绑定元素 
	    ,data: {
		  mediaId: function() {
		  	return $('#importMediaId').val()
		  },
		  password: function() {
		  	return $('#password').val()
		  }
		}
	    ,accept: 'file' //指定只允许上次文件
	    ,exts: 'xlsx|xls' //指定只允许上次xlsx和xls格式的excel文件
	    ,field: 'excelFile' //设置字段名
	    ,url: '/excel/insertMediaAppUserByExcel' //上传接口
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
	
	// 下载导入模板
    $('#downloadBatch').click(function(){
    	$.get('/excel/downloadMediaAppUserBatch', function(data){
    		if(data.ret.code === 100) {
    			window.open(data.ret.result)
    		}else{
    			layer.confirm("下载失败", {
                    icon: 5,
                    btn: ['确定'] //按钮
                });
    		}
    	})
    })
	
</script>
<@model.webend />