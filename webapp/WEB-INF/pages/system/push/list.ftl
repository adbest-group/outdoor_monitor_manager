<#assign webTitle="系统管理" in model>
<#assign webHead in model>
</#assign>
<@model.webhead />
<!-- 头部 -->
<@model.webMenu current="系统管理" child="系统消息推送" />
<div class="main-container" style="height: auto;">
    <div class="main-box ott-market">
        <div class="title clearfix">
            <#-- <a href="javascript:;" class="add-new-btn ll" id="add_push"><i></i> 新建系统消息推送</a> -->
            <div class="search-box search-ll" style="margin: 0 0 0 20px">
                <#-- <div class="select-box select-box-100 un-inp-select ll">
                    <select name="searchType" class="select" id="searchType">
                    	<option value="" selected>请选择</option>
            			<option value="1" <#if (searchType?exists&&searchType == '1')>selected</#if>>版本更新</option>
                        <option value="2" <#if (searchType?exists&&searchType == '2')>selected</#if>>免责声明更新</option>
                    </select>
                </div> -->
                <div class="inp">
                    <input type="text" placeholder="请输入用户名称" value="${name?if_exists}" id="searchName" name="name">
                </div>
                <div class="inp">
                    <input type="text" placeholder="请输入推送内容" value="${push?if_exists}" id="searchPush" name="push">
                </div>
                <button type="button" class="btn btn-red" autocomplete="off" id="searchBtn">查询</button>
            </div>
        </div>

        <!-- 数据报表 -->
        <div class="data-report">
            <div class="bd">
                <table width="100%" cellpadding="0" cellspacing="0" border="0" class="tablesorter" id="plan">
                    <thead>
                    <tr>
                        <th>序号</th>
                        <th>推送用户</th>
                        <th>推送标题</th>
                        <th>推送内容</th>
                        <th>创建日期</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#if (bizObj.list?exists && bizObj.list?size>0)>
                        <#list bizObj.list as push>
                        <tr>
                            <td width="30">${(bizObj.page.currentPage-1)*20+1}</td>
                           <#--  <td>
                            	<#if push.type?exists && push.type == '1'>版本更新</#if>
                            	<#if push.type?exists && push.type == '2'>免责声明更新</#if>
                            </td> -->
                            <td>${push.username?if_exists}</td>
                            <td>${push.title?if_exists}</td>
                            <td>${push.content?if_exists}</td>
                            <td>${push.createTime?string('yyyy-MM-dd HH:mm:ss')}</td>
                        </tr>
                        </#list>
                    </#if>
                    </tbody>
                    <!-- 翻页 -->
                <@model.showPage url=vm.getUrlByRemoveKey(thisUrl, ["start", "size"]) p=bizObj.page parEnd="" colsnum=7 />
                </table>
            </div>
        </div>
    </div>
</div>
</div>
</div>
</div>
<!-- 下拉 -->
<link href="${model.static_domain}/js/select/jquery.searchableSelect.css" rel="stylesheet">
<script src="${model.static_domain}/js/select/jquery.searchableSelect.js"></script>

<script type="text/javascript">

	$(function() {
        $(window).resize(function() {
            var h = $(document.body).height() - 115;
            $('.main-container').css('height', h);
        });
        $(window).resize();
    });

	$('.select').searchableSelect();
    
    <#-- // 查询
    $("#searchBtn").on("click", function () {
        var strParam = "";
        var searchType = $("#searchType").val();
        var searchContent = $("#searchContent").val();
        
        if (searchType != null && $.trim(searchType).length) {
            strParam = strParam + "?type=" + searchType;
        }
        if (searchContent != null && $.trim(searchContent).length) {
        	if(strParam.indexOf('?') != -1) {
        		strParam = strParam + "&content=" + searchContent;
        	}else {
        		strParam = strParam + "?content=" + searchContent;
        	}
        }

        window.location.href = "/systempush/list" + strParam;
    }); -->
	// 查询
    $("#searchBtn").on("click", function () {
        var strParam = "";
        var push = $("#searchPush").val();
        var name = $("#searchName").val();
        
        if (push != null && $.trim(push).length) {
            strParam = strParam + "?push=" + push;
        }
		
        if (name != null && $.trim(name).length) {
        	if(strParam.indexOf('?') != -1) {
        		strParam = strParam + "&name=" + name;
        	}else {
        		strParam = strParam + "?name=" + name;
        	}
        }
        window.location.href = "/systempush/list" + strParam;
    });
    
    <#--//跳转到添加页面
     $("#add_push").on("click", function () {
        //iframe层
        layer.open({
            type: 2,
            title: '添加系统消息推送',
            shadeClose: true,
            shade: 0.8,
            area: ['600px', '480px'],
            content: '/systempush/add' //iframe的url
        });
    });
 -->
</script>
<@model.webend />