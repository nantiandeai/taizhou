<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<% request.setAttribute("basePath", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath());%>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<% String path = request.getContextPath();%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>评论管理-查看评论</title>
    <%@include file="/pages/comm/admin/header.jsp"%>
    <link rel="stylesheet" href="${basePath}/static/admin/Font-Awesome/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${basePath}/static/admin/css/build.css"/>
</head>
<body>

<form id="whgff" class="whgff" method="post">
    <h2>${commentInfo.rmtitle }</h2>
    <div class="whgff-row">
        <div class="whgff-row-label">评论人：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="host" value="${comentUser.nickname }"
                   style="width: 500px; height: 32px" data-options="required:false,readonly:true" />
        </div>
    </div>
    <%--<div class="whgff-row">--%>
        <%--<div class="whgff-row-label">评论内容：</div>--%>
        <%--<div class="whgff-row-input">--%>
            <%--<input class="easyui-textbox" name="host" value="${commentInfo.rmcontent }"--%>
                   <%--style="width: 500px; height: 32px" data-options="required:false, readonly:true" />--%>
        <%--</div>--%>
    <%--</div>--%>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>评论内容：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="host" value="${commentInfo.rmcontent}" style="width:500px; height:100px"
                   data-options="required:true,readonly:true, multiline:true, validType:['length[0,400]', 'isText']">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">评论时间：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="host" value="<fmt:formatDate value="${commentInfo.rmdate }" pattern="yyyy-MM-dd HH:mm:ss"/>"
                   style="width: 500px; height: 32px" data-options="required:false,readonly:true" />
        </div>
    </div>
</form>

<div id="whgwin-edit-btn" class="whgff-but" style="width: 400px; margin:20px 0px 50px 350px">
    <a href="javascript:void(0)" class="easyui-linkbutton whgff-but-clear" iconCls="icon-undo" onclick="WhgComm.editDialogClose()">返 回</a>
</div>
</body>
</html>