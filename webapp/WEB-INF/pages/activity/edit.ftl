<#assign webTitle="监测管理-活动编辑" in model>
<#assign webHead in model>
</#assign>
<@model.webhead />
    <!-- 头部 -->
    <@model.webMenu current="活动管理" child="活动管理" />

<#include "../adcustomer/activity/editMain.ftl"/>

<@model.webend />