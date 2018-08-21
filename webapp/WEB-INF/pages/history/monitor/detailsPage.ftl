<#assign webTitle="监测管理-任务详情" in model> <#assign webHead in model>
</#assign> <@model.webhead />
<!-- 头部 -->
<@model.webMenu current="历史活动查看" child="监测任务查看" />
<#include "./detailsContent.ftl" />
<@model.webend />
