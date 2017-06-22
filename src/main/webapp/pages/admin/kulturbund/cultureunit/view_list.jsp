<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<% request.setAttribute("basePath", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath());%>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <c:set var="pageTitle" value="文化联盟单位列表"></c:set>
    <title>${pageTitle}</title>
    <%@include file="/pages/comm/admin/header.jsp"%>
</head>
<body>
<table id="whgdg" title="${pageTitle}" class="easyui-datagrid" style="display: none"
       data-options="fit:true, striped:true, rownumbers:true, fitColumns:true, singleSelect:false, checkOnSelect:true, selectOnCheck:true, pagination:true, toolbar:'#whgdg-tb', url:'${basePath}/admin/cultheritage/srchList4p?type=${type}'">
    <thead>
    <tr>
        <th data-options="field:'name',width:80">名称</th>
        <th data-options="field:'summary',width:80">简介</th>
        <th data-options="field:'crtdate', width:60,formatter:WhgComm.FMTDateTime">创建时间</th>
        <th data-options="field:'state', width:50,formatter:WhgComm.FMTBizState" >状态</th>
        <th data-options="field:'_opt', formatter:WhgComm.FMTOpt,fixed:true, optDivId:'whgdg-opt'">操作</th>
    </tr>
    </thead>

</table>
</body>
</html>
