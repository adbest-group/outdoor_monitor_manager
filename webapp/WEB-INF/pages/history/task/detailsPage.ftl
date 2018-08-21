<#assign webTitle="监测管理-任务详情" in model> <#assign webHead in model>
</#assign> <@model.webhead />
<!-- 头部 -->
<@model.webMenu current="监测管理" child="监测管理" />
<#include "./detailsContent.ftl" />
<@model.webend />
