<#assign webTitle="任务管理-报错任务" in model> <#assign webHead in model>
</#assign> <@model.webhead />
<!-- 头部 -->
<@model.webMenu current="任务管理" child="报错任务" />
<#include "../../task/detailsContent.ftl" />
<@model.webend />
