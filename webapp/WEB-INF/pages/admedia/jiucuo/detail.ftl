<#assign webTitle="监测管理 - 纠错管理 - 纠错详情" in model>
<#assign webHead in model>
</#assign>
<@model.webhead />
    <!-- 头部 -->
    <@model.webMenu current="纠错处理" child="纠错处理" />

	<!-- 特色内容 -->
<#include "../../jiucuo/detailContent.ftl">
    <!-- 特色内容 -->

<@model.webend />