<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<% request.setAttribute("basePath", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath());%>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <c:choose>
        <c:when test="${listType eq 'edit'}">
            <c:set var="pageTitle" value="云直播编辑列表"></c:set>
            <c:set var="getUrl" value="/admin/live/search4p?livestate=edit"></c:set>
        </c:when>
        <c:when test="${listType eq 'check'}">
            <c:set var="pageTitle" value="云直播审核列表"></c:set>
            <c:set var="getUrl" value="/admin/live/search4p?livestate=check"></c:set>
        </c:when>
        <c:when test="${listType eq 'publish'}">
            <c:set var="pageTitle" value="云直播发布列表"></c:set>
            <c:set var="getUrl" value="/admin/live/search4p?livestate=publish"></c:set>
        </c:when>
        <c:when test="${listType eq 'cycle'}">
            <c:set var="pageTitle" value="回收站"></c:set>
            <c:set var="getUrl" value="/admin/live/search4p?livestate=cycle"></c:set>
        </c:when>
        <c:otherwise>
            <c:set var="pageTitle" value="无标题"></c:set>
        </c:otherwise>
    </c:choose>
    <title>${pageTitle}</title>
    <%@include file="/pages/comm/admin/header.jsp"%>
</head>
<body>
    <h2>${pageTitle}</h2>
</body>
</html>
