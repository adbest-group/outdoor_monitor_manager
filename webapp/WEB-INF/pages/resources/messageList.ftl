<#assign webTitle="站内信" in model>
<#assign webHead in model>
</#assign>
<@model.webhead />
    <!-- 头部 -->
    <@model.webMenu current="站内信" child="站内信" />
	<!-- 特色内容 -->
<div class="main-container" style="height: auto;">
    <div class="main-box">
        <div class="title clearfix">
            <div class="search-box search-ll" style="margin: 0 0 0 20px">
                <form id="form" method="get" action="/sysResources/messageList">
                	<div class="select-box select-box-100 un-inp-select ll">
                        <select class="select" name="isFinish">
                        	<option value="">是否已处理</option>
                        	<option value="0" <#if (isFinish?exists&&isFinish == '0')>selected</#if>>否</option>
                        	<option value="1" <#if (isFinish?exists&&isFinish == '1')>selected</#if>>是</option>
                        </select>
                    </div>
                    <div class="select-box select-box-100 un-inp-select ll">
                        <select class="select" name="type">
                        	<option value="">类型</option>
                        	<option value="1" <#if (type?exists&&type == '1')>selected</#if>>活动确认</option>
                        	<option value="2" <#if (type?exists&&type == '2')>selected</#if>>任务审核</option>
                        	<option value="3" <#if (type?exists&&type == '3')>selected</#if>>任务指派</option>
                        	<option value="4" <#if (type?exists&&type == '4')>selected</#if>>纠错审核</option>
                        </select>
                    </div>
                     <button type="button" class="btn btn-red" style="margin-left:10px;" id="searchBtn">查询</button>
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
                        <th>类型</th>
                        <th>站内信内容</th>                        
                        <th>是否已处理</th>
                        <th>创建时间</th>
                        <th>更新时间</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#if (bizObj.list?exists && bizObj.list?size>0) >
                        <#list bizObj.list as list>
                        <tr>
                            <td width="30">${(bizObj.page.currentPage-1)*20+list_index+1}</td>
                            <td>
                                <div class="data-title w200" data-title="${list.type}" data-id="${list.id}">${vm.getUserMessageTypeText(list.type)!""}</div>
                            </td>
                            <td>${list.content?if_exists}</td>
                            <td>${vm.getUserMessageText(list.isFinish)!""}</td>
                            <td>${list.createTime?string('yyyy-MM-dd HH:mm:ss')}</td>
                            <td>${list.updateTime?string('yyyy-MM-dd HH:mm:ss')}</td>
                        </tr>
                        </#list>
                    <#else>
                    <tr><td colspan="20">没有相应结果。</td> </tr>
                    </#if>
                    </tbody>
                    <!-- 翻页 -->
                <@model.showPage url=vm.getUrlByRemoveKey(thisUrl, ["start", "size"]) p=bizObj.page parEnd="" colsnum=9 />
                </table>
            </div>
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
<script type="text/javascript" src="/static/js/jquery.citys.js"></script>
<!-- 时期 -->
<link href="${model.static_domain}/js/date/daterangepicker.css" rel="stylesheet">
<script type="text/javascript" src="${model.static_domain}/js/date/moment.min.js"></script>
<script type="text/javascript" src="${model.static_domain}/js/date/jquery.daterangepicker.js"></script>
<script type="text/javascript" src="${model.static_domain}/js/date.js"></script>

<script type="text/javascript">
	
    $(function(){
          $(".nav-sidebar>ul>li").on("click",function(){
               $(".nav-sidebar>ul>li").removeClass("on");
               $(this).addClass("on");
          });
    });

    $(function(){
        $(window).resize();
    });

    $(window).resize(function() {
        var h = $(document.body).height() - 115;
        $('.main-container').css('height', h);
    });

        $("input[name='ck-alltask']").change(function(){
            if($(this).is(":checked")){
                $("input[name='ck-task']").prop("checked",true)
            }else{
                $("input[name='ck-task']").removeAttr("checked");
            }
        });
    
     $(window).resize(function () {
        var h = $(document.body).height() - 115;
        $('.main-container').css('height', h);
    });


    $("#searchBtn").on("click", function () {
        $("#form").submit();
    });
    



</script>
<!-- 特色内容 -->

<@model.webend />