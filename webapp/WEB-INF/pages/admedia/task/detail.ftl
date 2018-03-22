<#assign webTitle="监测管理-任务详情" in model> <#assign webHead in model>
</#assign> <@model.webhead />
<!-- 头部 -->
<@model.webMenu current="任务管理" child="任务管理" />
<#include "../../task/detailsContent.ftl" />
<@model.webend />
