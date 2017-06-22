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
       data-options="fit:true, striped:true, rownumbers:true, fitColumns:true, singleSelect:false, checkOnSelect:true, selectOnCheck:true, pagination:true, toolbar:'#whgdg-tb', url:'${basePath}/admin/cultureunit/srchList4p'">
    <thead>
    <tr>
        <th data-options="field:'unitname',width:80">单位名</th>
        <th data-options="field:'unitdesc',width:80">单位简介</th>
        <th data-options="field:'unitcreatetime', width:60,formatter:WhgComm.FMTDateTime">创建时间</th>
        <th data-options="field:'unitstate', width:50,formatter:myState" >状态</th>
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
                data-options="width:120, value:'', valueField:'id', textField:'text', data:myStateAll()">
        </select>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="WhgComm.search('#whgdg', '#whgdg-tb');">查 询</a>
    </div>
</div>

<!-- 操作按钮 -->
<div id="whgdg-opt" style="display: none;">
    <shiro:hasPermission name="${resourceid}:view"><a href="javascript:void(0)" class="easyui-linkbutton" validKey="unitstate" validVal="1,2" method="view">查看</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:edit"><a href="javascript:void(0)" class="easyui-linkbutton" validKey="unitstate" validVal="2" method="edit">编辑</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:on"><a href="javascript:void(0)" class="easyui-linkbutton" validKey="unitstate" validVal="2" method="doOn">启用</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:off"><a href="javascript:void(0)" class="easyui-linkbutton" validKey="unitstate" validVal="1" method="doOff">停用</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:del"><a href="javascript:void(0)" class="easyui-linkbutton" validKey="unitstate" validVal="2" method="del">删除</a></shiro:hasPermission>
</div>
<!-- 操作按钮-END -->

</body>
<script type="text/javascript">
    function myState(state) {
        if(1 == state){
            return "已启用";
        }else if(2 == state){
            return "已停用";
        }else {
            return "未知";
        }
    }
    
    function myStateAll() {
        var _myState = [];
        _myState.push({id:0,text:"全部"});
        _myState.push({id:1,text:"已启用"});
        _myState.push({id:2,text:"已停用"});
        return _myState;
    }
    
    function add() {
        WhgComm.editDialog("${basePath}/admin/cultureunit/edit/add");
    }

    function view(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        WhgComm.editDialog('${basePath}/admin/cultureunit/edit/view?id='+curRow.id);
    }

    function edit(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        WhgComm.editDialog('${basePath}/admin/cultureunit/edit/edit?id='+curRow.id);
    }

    function doOn(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        $.messager.confirm("确认信息", "确定要启用选中的项吗？", function(r){
            $.getJSON("${basePath}/admin/cultureunit/updateState?id=" + curRow.id + "&state=1",function (data) {
                if("1" != data.success){
                    $.messager.alert('提示', '操作失败:'+data.errormsg+'!', 'error');
                    return;
                }
                $('#whgdg').datagrid('reload');
            });
        });

    }

    function doOff(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        $.messager.confirm("确认信息", "确定要停用选中的项吗？", function(r){
            $.getJSON("${basePath}/admin/cultureunit/updateState?id=" + curRow.id + "&state=2",function (data) {
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
            $.getJSON("${basePath}/admin/cultureunit/delUnit?id=" + curRow.id,function (data) {
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
