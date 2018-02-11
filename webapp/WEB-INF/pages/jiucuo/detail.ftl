<#assign webTitle="纠错管理" in model>
<#assign webHead in model>
</#assign>
<@model.webhead />
<!-- 头部 -->
<@model.webMenu current="纠错管理" child="纠错管理" />

<!-- 特色内容 -->
<#include "./detailContent.ftl"/>
<!-- 特色内容 -->

<@model.webend />