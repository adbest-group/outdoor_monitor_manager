<#assign webTitle="监测管理-活动编辑" in model>
<#assign webHead in model>
</#assign>
<@model.webhead />
<!-- 头部 -->
<@model.webMenu current="监测管理" child="活动管理" />

<#include "./editMain.ftl"/>

<@model.webend />