<#assign webTitle="首页" in model>
<#assign webHead in model>
</#assign>
<@model.webhead />
<!-- 头部 -->
<@model.webMenu current="index" child="index" />

<@shiro.hasRole name="admin">
    <#include "./index_admin.ftl" />
</@shiro.hasRole>
<@shiro.hasRole name="customer">
    <#include "./index_customer.ftl" />
</@shiro.hasRole>
<@shiro.hasRole name="media">
    <#include "./index_admin.ftl" />
</@shiro.hasRole>

<@model.webend/>
		