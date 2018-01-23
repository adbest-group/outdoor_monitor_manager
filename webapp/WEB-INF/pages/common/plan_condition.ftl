<link rel="stylesheet" type="text/css" href="${model.static_domain}/css/new_main.css">
<meta charset="utf-8">
<style type="text/css">
    html, body{ min-width: 100%; }
    .activity{margin: 20px 20px 30px 20px; padding: 18px 0;}

    .activity-aggregate{ border: 1px solid #ddd; border-radius: 5px;}
    .activity-aggregate li{width: auto;margin-right: 20px; width: 160px; overflow: hidden;}
    .activity .basic-info,.activity .basic-info .bd{padding: 0}
    .basic-info .bd .a-title{ width: 100px;vertical-align:middle;text-align: center}
    .basic-info .bd td{padding: 5px; font-size: 14px;}
    .role-nav-authority li{ float: left; overflow: hidden; margin-bottom: 5px; height: 24px; line-height: 24px; font-size: 14px;}
    .role-nav-authority li label input[type=checkbox]{ width: 15px; height: 15px; margin: 2px 3px 0 0; vertical-align: text-top;}
    .activity-but {text-align: center}
    .activity-but button{padding:5px  30px;}
</style>

<div class="activity-aggregate activity">
    <div class="basic-info">
        <div class="bd">
            <table width="100%" cellpadding="0" cellspacing="0" border="0" class="tablesorter">
                <tbody>
               <#-- <#if (plan?? && plan?size > 0) >
                      <#list plan as ll>
		                <tr>
		                    <td class="a-title">${(ll.type)!}类型活动：</td>
		                    <td>
		                        <ul class="role-nav-authority" id="checkBoxOTT">
		                            <#if ((ll.result)?? && (ll.result)?size > 0) >
		                                <#list (ll.result) as l>
				                            <li><label><input type="checkbox" name="role" value="${(l.id)!}">${(l.name)!}</label></li>
		                                </#list>
		                            </#if>
		                        </ul>
		                    </td>
		                </tr>
                      </#list>
                </#if>
                
                <#if (thirdPlan?? && thirdPlan?size > 0) >
                      <#list thirdPlan as ll>
		                <tr>
		                    <td class="a-title">第三方类型活动：</td>
		                    <td>
		                        <ul class="role-nav-authority" id="checkBoxOTT">
		                            <#if ((ll.result)?? && (ll.result)?size > 0) >
		                                <#list (ll.result) as l>
				                            <li><label><input type="checkbox" name="role" value="${(l.id)!}">${(l.name)!}</label></li>
		                                </#list>
		                            </#if>
		                        </ul>
		                    </td>
		                </tr>
                      </#list>
                </#if>
                <#if (!plan?? || plan?? && plan?size < 1) && (!media?? || media?? && media?size < 1) && (!thirdPlan?? || thirdPlan?? && thirdPlan?size < 1) >
                	<span style="margin-left:10px;">暂无活动</span>
                </#if>
                </tbody>
            </table> V1.1版本修改界面展示特此注释先前版本代码 -->
            <table width="100%" cellpadding="0" cellspacing="0" border="0" class="tablesorter">
                <tbody>
                <tr>
                    <td class="a-title">实时投放</td>
                    <td>
                    	<table width="100%" cellpadding="0" cellspacing="0" border="0" class="tablesorter">
                    		<#assign n1=0 />
                    		<#if (plan?exists && plan?size > 0) >
			                      <#list plan as ll>
			                      	<#if ll.isRtb=="1">
			                      		<#assign n1=n1+1 />
						                <tr>
						                    <td><#if ll.type=="live_show">TV互动<#else>${(ll.type)?if_exists}类型活动</#if>：</td>
						                </tr>
						                <tr>
						                    <td>
						                        <ul class="role-nav-authority" id="checkBoxOTT">
						                            <#if ((ll.result)?exists && (ll.result)?size > 0) >
						                                <#list (ll.result) as l>
								                            <li><label><input type="checkbox" name="role" value="${(l.id)?if_exists}">${(l.name)?if_exists}</label></li>
						                                </#list>
						                            </#if>
						                        </ul>
						                    </td>
						                </tr>
			                      	</#if>
			                      </#list>
			                </#if>
			                <#if n1==0>
			                	<tr>
				                    <td>暂无此类活动</td>
				                </tr>
			                </#if>
                    	</table>
                    </td>
                </tr>
                
                <tr style="border-top: 1px solid #ccc">
                    <td class="a-title">定时投放</td>
                    <td >
						<table width="100%" cellpadding="0" cellspacing="0" border="0" class="tablesorter">
							<#assign n0=0 />
			                <#if (plan?exists && plan?size > 0) >
			                      <#list plan as ll>
			                      	<#if ll.isRtb=="0">
			                      	<#assign n0=n0+1 />
						                <tr>
						                    <td>${(ll.type)?if_exists}类型活动：</td>
						                </tr>
						                <tr>
						                    <td>
						                        <ul class="role-nav-authority" id="checkBoxOTT">
						                            <#if ((ll.result)?exists && (ll.result)?size > 0) >
						                                <#list (ll.result) as l>
								                            <li><label><input type="checkbox" name="role" value="${(l.id)?if_exists}">${(l.name)?if_exists}</label></li>
						                                </#list>
						                            </#if>
						                        </ul>
						                    </td>
						                </tr>
			                      	</#if>
			                      </#list>
			                </#if>
			                <#if n0==0>
			                	<tr>
				                    <td>暂无此类活动</td>
				                </tr>
			                </#if>
                    	</table>
					</td>
                </tr>
                <tr style="border-top: 1px solid #ccc">
                    <td class="a-title">第三方活动</td>
                    <td>
						<table width="100%" cellpadding="0" cellspacing="0" border="0" class="tablesorter">
			                <#if (thirdPlan?exists && thirdPlan?size > 0) >
			                      <#list thirdPlan as ll>
					                <tr>
					                    <td>
					                        <ul class="role-nav-authority" id="checkBoxOTT">
					                            <#if ((ll.result)?exists && (ll.result)?size > 0) >
					                                <#list (ll.result) as l>
							                            <li><label><input type="checkbox" name="role" value="${(l.id)?if_exists}">${(l.name)?if_exists}</label></li>
					                                </#list>
					                            </#if>
					                        </ul>
					                    </td>
					                </tr>
			                      </#list>
			                <#else>
			                	<tr>
				                    <td>暂无此类活动</td>
				                </tr>
			                </#if>
                    	</table>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>

<div class="activity-but">
    <button type="button" class="btn btn-red" id="activitySubmit">确&nbsp;定</button>
</div>



<script type="text/javascript" src="${model.static_domain}/js/jquery-2.1.4.min.js"></script>
<script>

    $(function(){
    
   		 var codeArr =parent.$('#activity_code').val().split(',');
       	 $('input:checkbox').each(function(){
            if(codeArr.indexOf( $(this).val())!=-1){
                $(this).attr('checked','checked');
            }
        });
        
        var index = parent.layer.getFrameIndex(window.name); //获取窗口索引

        $('#activitySubmit').on('click', function(){
            var val=[],checkedCode=[];
            $('input:checkbox:checked').each(function(){
                checkedCode.push($(this).val());
                val.push($(this).parent().text());
            })
            var valStr='';
            if(val.join(',').length<=10)valStr=val.join(',');
            else valStr=val.join(',').slice(0,30)+"..."
            parent.$('#activity_val').val(valStr);
            parent.$('#activity_code').val(checkedCode.join(','));
            parent.layer.close(index);
        });
    });

</script>
