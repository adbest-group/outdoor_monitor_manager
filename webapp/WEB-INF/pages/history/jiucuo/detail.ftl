<#assign webTitle="历史活动查看" in model>
<#assign webHead in model>
</#assign>
<@model.webhead />
<!-- 头部 -->
<@model.webMenu current="历史活动查看" child="纠错任务查看" />

<!-- 特色内容 -->
<#include "./detailContent.ftl"/>
<!-- 特色内容 -->

<@model.webend />