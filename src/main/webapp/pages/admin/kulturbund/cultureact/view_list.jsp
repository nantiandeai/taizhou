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
            <c:set var="pageTitle" value="文化联盟大型活动编辑列表"></c:set>
        </c:when>
        <c:when test="${listType eq 'check'}">
            <c:set var="pageTitle" value="文化联盟大型活动审核列表"></c:set>
        </c:when>
        <c:when test="${listType eq 'publish'}">
            <c:set var="pageTitle" value="文化联盟大型活动发布列表"></c:set>
        </c:when>
    </c:choose>

    <title>${pageTitle}</title>
    <%@include file="/pages/comm/admin/header.jsp"%>
</head>
<body>
<table id="whgdg" title="${pageTitle}" class="easyui-datagrid" style="display: none"
       data-options="fit:true, striped:true, rownumbers:true, fitColumns:true, singleSelect:false, checkOnSelect:true, selectOnCheck:true, pagination:true, toolbar:'#whgdg-tb', url:'${basePath}/admin/cultureact/srchList4p?listType=${listType}'">
    <thead>
        <tr>
            <th data-options="field:'culactname',width:80">活动标题</th>
            <th data-options="field:'culactuser',width:80,formatter:getUserName">创建人</th>
            <th data-options="field:'culactcreattime', width:60,formatter:WhgComm.FMTDateTime">创建时间</th>
            <th data-options="field:'culactstate', width:50,formatter:myState" >状态</th>
            <th data-options="field:'_opt', width:450, formatter:WhgComm.FMTOpt,fixed:true, optDivId:'whgdg-opt'">操作</th>
        </tr>
    </thead>

</table>

<!-- 表格操作工具栏 -->
<div id="whgdg-tb" style="display: none;">
    <div id="tb">
        <shiro:hasPermission name="${resourceid}:add"><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" onclick="add()">添加</a></shiro:hasPermission>
    </div>
    <div class="whgdg-tb-srch" style="padding-top: 8px">
        <input class="easyui-textbox" style="width: 200px;" name="name" data-options="prompt:'请输入名称', validType:'length[1,32]'" />
        <select class="easyui-combobox" name="state" prompt="请选择状态" panelHeight="auto" limitToList="true"
                data-options="width:120, value:'', valueField:'id', textField:'text', data:WhgComm.getBizState()">
        </select>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="WhgComm.search('#whgdg', '#whgdg-tb');">查 询</a>
    </div>
</div>

<!-- 操作按钮 -->
<div id="whgdg-opt" style="display: none;">
    <shiro:hasPermission name="${resourceid}:view"><a href="javascript:void(0)" class="easyui-linkbutton" validKey="culactstate" validVal="0,1,2,3" method="view">查看</a></shiro:hasPermission>
    <c:choose>
        <c:when test="${listType eq 'edit'}">
            <shiro:hasPermission name="${resourceid}:edit"><a href="javascript:void(0)" class="easyui-linkbutton" validKey="culactstate" validVal="0" method="edit">编辑</a></shiro:hasPermission>
            <shiro:hasPermission name="${resourceid}:checkgo"><a href="javascript:void(0)" class="easyui-linkbutton" validKey="culactstate" validVal="0" method="checkgo">送审</a></shiro:hasPermission>
            <shiro:hasPermission name="${resourceid}:del"><a href="javascript:void(0)" class="easyui-linkbutton" validKey="culactstate" validVal="0,1,2" method="del">删除</a></shiro:hasPermission>
        </c:when>
        <c:when test="${listType eq 'check'}">
            <shiro:hasPermission name="${resourceid}:checkon"><a href="javascript:void(0)" class="easyui-linkbutton" validKey="culactstate" validVal="1" method="checkon">审核通过</a></shiro:hasPermission>
            <shiro:hasPermission name="${resourceid}:checkoff"><a href="javascript:void(0)" class="easyui-linkbutton" validKey="culactstate" validVal="1" method="checkoff">审核不通过</a></shiro:hasPermission>
            <shiro:hasPermission name="${resourceid}:del"><a href="javascript:void(0)" class="easyui-linkbutton" validKey="culactstate" validVal="0,1,2" method="del">删除</a></shiro:hasPermission>
        </c:when>
        <c:when test="${listType eq 'publish'}">
            <shiro:hasPermission name="${resourceid}:publish"><a href="javascript:void(0)" class="easyui-linkbutton" validKey="culactstate" validVal="2" method="publish">发布</a></shiro:hasPermission>
            <shiro:hasPermission name="${resourceid}:publishoff"><a href="javascript:void(0)" class="easyui-linkbutton" validKey="culactstate" validVal="3" method="publishoff">取消发布</a></shiro:hasPermission>
            <shiro:hasPermission name="${resourceid}:del"><a href="javascript:void(0)" class="easyui-linkbutton" validKey="culactstate" validVal="0,1,2" method="del">删除</a></shiro:hasPermission>
        </c:when>
        <c:when test="${listType eq 'cycle'}">
            <shiro:hasPermission name="${resourceid}:del"><a href="javascript:void(0)" class="easyui-linkbutton" validKey="culactstate" validVal="0,1,2,3" method="doDelForver">删除</a></shiro:hasPermission>
            <shiro:hasPermission name="${resourceid}:undel"><a href="javascript:void(0)" class="easyui-linkbutton" validKey="culactstate" validVal="0,1,2,3" method="undel">还原</a></shiro:hasPermission>
        </c:when>
    </c:choose>
</div>
<!-- 操作按钮-END -->

</body>
<script type="text/javascript">

    function getUserName(userId) {
        $.ajaxSetup({
            async: false
        });
        var account = "";
        $.getJSON("${basePath}/admin/getUser?id="+userId,function (data) {
            if("1" != data.success){
                $.messager.alert('提示', '操作失败:'+data.errormsg+'!', 'error');
                return "未知"
            }
            account = data.data.account;
        });
        $.ajaxSetup({
            async: true
        });
        return account;
    }

    function myState(state) {
        if(0 == state){
            return "可编辑";
        }
        if(1 == state){
            return "待审核";
        }
        if(2 == state){
            return "待发布";
        }
        if(3 == state){
            return "已发布";
        }
        return "未知状态";
    }


    
    function add() {
        WhgComm.editDialog("${basePath}/admin/cultureact/edit/add");
    }

    function view(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        WhgComm.editDialog('${basePath}/admin/cultureact/edit/view?id='+curRow.id);
    }

    function edit(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        WhgComm.editDialog('${basePath}/admin/cultureact/edit/edit?id='+curRow.id);
    }

    function checkgo(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        $.messager.confirm("确认信息", "确定要送审吗？", function(r){
            $.getJSON("${basePath}/admin/cultureact/doUpdateState?id=" + curRow.id +"&state=checkpending",function (data) {
                if("1" != data.success){
                    $.messager.alert('提示', '操作失败:'+data.errormsg+'!', 'error');
                    return;
                }
                $('#whgdg').datagrid('reload');
            });
        });
    }

    function checkon(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        $.messager.confirm("确认信息", "确定送审通过吗？", function(r){
            $.getJSON("${basePath}/admin/cultureact/doUpdateState?id=" + curRow.id +"&state=checked",function (data) {
                if("1" != data.success){
                    $.messager.alert('提示', '操作失败:'+data.errormsg+'!', 'error');
                    return;
                }
                $('#whgdg').datagrid('reload');
            });
        });
    }

    function checkoff(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        $.messager.confirm("确认信息", "确定审核不通过吗？", function(r){
            $.getJSON("${basePath}/admin/cultureact/doUpdateState?id=" + curRow.id +"&state=initial",function (data) {
                if("1" != data.success){
                    $.messager.alert('提示', '操作失败:'+data.errormsg+'!', 'error');
                    return;
                }
                $('#whgdg').datagrid('reload');
            });
        });
    }

    function publish(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        $.messager.confirm("确认信息", "确定发布吗？", function(r){
            $.getJSON("${basePath}/admin/cultureact/doUpdateState?id=" + curRow.id +"&state=published",function (data) {
                if("1" != data.success){
                    $.messager.alert('提示', '操作失败:'+data.errormsg+'!', 'error');
                    return;
                }
                $('#whgdg').datagrid('reload');
            });
        });
    }

    function publishoff(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        $.messager.confirm("确认信息", "确定取消发布吗？", function(r){
            $.getJSON("${basePath}/admin/cultureact/doUpdateState?id=" + curRow.id +"&state=initial",function (data) {
                if("1" != data.success){
                    $.messager.alert('提示', '操作失败:'+data.errormsg+'!', 'error');
                    return;
                }
                $('#whgdg').datagrid('reload');
            });
        });
    }

    function del(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        $.messager.confirm("确认信息", "确定要删除选中的项吗？", function(r){
            $.getJSON("${basePath}/admin/cultureact/doDel?id=" + curRow.id + "&state=del",function (data) {
                if("1" != data.success){
                    $.messager.alert('提示', '操作失败:'+data.errormsg+'!', 'error');
                    return;
                }
                $('#whgdg').datagrid('reload');
            });
        });
    }

    function undel(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        $.messager.confirm("确认信息", "确定要还原选中的项吗？", function(r){
            $.getJSON("${basePath}/admin/cultureact/doDel?id=" + curRow.id + "&state=undel",function (data) {
                if("1" != data.success){
                    $.messager.alert('提示', '操作失败:'+data.errormsg+'!', 'error');
                    return;
                }
                $('#whgdg').datagrid('reload');
            });
        });
    }

    function doDelForver(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        $.messager.confirm("确认信息", "确定要永久删除选中的项吗？", function(r){
            $.getJSON("${basePath}/admin/cultureact/doDel?id=" + curRow.id + "&state=delforever",function (data) {
                if("1" != data.success){
                    $.messager.alert('提示', '操作失败:'+data.errormsg+'!', 'error');
                    return;
                }
                $('#whgdg').datagrid('reload');
            });
        });
    }
</script>
</html>
