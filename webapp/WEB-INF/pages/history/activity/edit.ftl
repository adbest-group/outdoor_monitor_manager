<#assign webTitle="历史活动-活动查看" in model>
<#assign webHead in model>
</#assign>
<@model.webhead />
    <!-- 头部 -->
    <@model.webMenu current="活动查看" child="活动查看" />

<#include "../../adcustomer/activity/editMain.ftl"/>

<@model.webend />