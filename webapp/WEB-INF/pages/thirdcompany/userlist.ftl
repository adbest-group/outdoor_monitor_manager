<#assign webTitle="监测人员管理" in model>
<#assign webHead in model>
</#assign>
<@model.webhead />
<!-- 头部 -->
<@model.webMenu current="监测人员管理" child="监测人员管理" />
<div class="main-container" style="height: auto;">
    <div class="main-box ott-market">
        <div class="title clearfix">
            <button href="javascript:;" class="add-new-btn ll" id="add_media"><i></i> 新建监测人员账号</button>
            <div class="search-box search-ll" style="margin: 0 0 0 20px">
            	<form id="form" method="get" action="/thirdCompany/userlist">
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
                        <th>监测人员账户</th>
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
                            <td>${user.realname?if_exists}</td>
                            <td>${user.mobile?if_exists}</td>
                            <td><span onclick="updStatus('${user.id}', '${user.status}');"
                                   class="switch<#if user.status?exists && user.status == 1> current</#if>"><s></s><b></b></span>
                            </td>
                            <td>
                                <a href="javascript:void(0);" onclick="edit('${user.id}');">编辑</a>
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
	$('#companyId').next().find('.searchable-select-input').css('display', 'block');
	
	 $("#usertype").siblings().find(".searchable-select-item").click(function(){
        if($("#usertype").val()==3){
            $("#mediaTr").show();
            $("#companyTr").hide();
		} else if($("#usertype").val()==5) {
			$("#mediaTr").hide();
            $("#companyTr").show();
		} else {
            $("#mediaTr").hide();
            $("#companyTr").hide();
		}
    });

    $("#add_media").on("click", function () {
        //iframe层
        layer.open({
            type: 2,
            title: '编辑账户',
            shadeClose: true,
            shade: 0.8,
            area: ['600px', '480px'],
            content: '/thirdCompany/userEdit' //iframe的url
        });
    });
    
    //批量导入打开媒体界面
    $('#batchInsert').on('click',function(){
    	 layer.open({
    		 type: 1,
    		 title:"批量导入",
             shift: 2,
             shade: 0.8,
             area: ['500px', '380px'], 
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
            content: '/thirdCompany/userEdit?id=' + id //iframe的url
        });
    }
    
    $("#searchBtn").on("click", function () {
        $("#form").submit();
    });


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
            url: "/thirdCompany/updateStatus",
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
  	    var password = $('#password').val();
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
		  password: function() {
		  	return $('#password').val();
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
    	$.get('/excel/downloadThirdCompanyUserBatch', function(data){
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