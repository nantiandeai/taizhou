<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<% request.setAttribute("basePath", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath());%>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <title>分馆管理</title>
    <%@include file="/pages/comm/admin/header.jsp"%>
    <script type="text/javascript" charset="utf-8" src="${basePath}/static/ueditor/ueditor.config.js"></script>
    <script type="text/javascript" charset="utf-8" src="${basePath}/static/ueditor/ueditor.all.min.js"></script>
    <script type="text/javascript" charset="utf-8" src="${basePath}/static/ueditor/lang/zh-cn/zh-cn.js"></script>
    <script type="text/javascript">
        function doAdd() {
            preForAdd();
            $("#dd").dialog(
                {
                    title:'添加分馆',
                    width:450,
                    height:350,
                    close:false,
                    modal:true,
                    buttons:[
                        {
                            text:'保存',
                            handler:function () {
                                $("form[id='whgff']").form('submit',{
                                    url : getFullUrl('/admin/branch/saveBranch?saveType=add'),
                                    onSubmit : function(param) {
                                        var _valid = $(this).form('enableValidation').form('validate');
                                        if(_valid){
                                            $.messager.progress();
                                        }
                                        return _valid;
                                    },
                                    success : function(data) {
                                        $.messager.progress('close');
                                        var Json = eval('('+data+')');
                                        if(Json && Json.success == '1'){
                                            $("#dd").dialog('close');
                                            $('#whgdg').datagrid('reload');
                                        } else {
                                            $.messager.alert('提示', '操作失败:'+Json.errormsg+'!', 'error');
                                        }
                                    }
                                });

                            }
                        },
                        {
                            text:'关闭',
                            handler:function () {
                                $("#dd").dialog('close');
                            }
                        }
                    ]
                }
            );
        }

        function doEdit(idx) {
            var row = $("#whgdg").datagrid("getRows")[idx];
            preForEdit(row);
            $("#dd").dialog(
                {
                    title:'编辑分馆',
                    width:450,
                    height:350,
                    close:false,
                    modal:true,
                    buttons:[
                        {
                            text:'保存',
                            handler:function () {
                                $("form[id='whgff']").form('submit',{
                                    url : getFullUrl('/admin/branch/saveBranch?saveType=edit&id=' + row.id),
                                    onSubmit : function(param) {
                                        var _valid = $(this).form('enableValidation').form('validate');
                                        if(_valid){
                                            $.messager.progress();
                                        }
                                        return _valid;
                                    },
                                    success : function(data) {
                                        $.messager.progress('close');
                                        var Json = eval('('+data+')');
                                        if(Json && Json.success == '1'){
                                            $("#dd").dialog('close');
                                            $('#whgdg').datagrid('reload');
                                        } else {
                                            $.messager.alert('提示', '操作失败:'+Json.errormsg+'!', 'error');
                                        }
                                    }
                                });

                            }
                        },
                        {
                            text:'关闭',
                            handler:function () {
                                $("#dd").dialog('close');
                            }
                        }
                    ]
                }
            );
        }
        
        function doSee(idx) {
            var row = $("#whgdg").datagrid("getRows")[idx];
            preForEdit(row);
            $("#dd").dialog(
                {
                    title:'编辑分馆',
                    width:450,
                    height:350,
                    close:false,
                    modal:true,
                    buttons:[
                        {
                            text:'关闭',
                            handler:function () {
                                $("#dd").dialog('close');
                            }
                        }
                    ]
                }
            );
        }

        function doBatchUpd(state) {
            var list = $('#whgdg').datagrid('getChecked');
            $.each(list,function (index,value) {
                if(0 == state){
                    doOff(index);
                }else {
                    doOn(index);
                }
            });
        }

        function doOn(idx) {
            var row = $("#whgdg").datagrid("getRows")[idx];
            var url = getFullUrl('/admin/branch/saveBranch?saveType=edit&id='+row.id + "&state=1");
            $.getJSON(url,function (data) {
                if("1" != data.success){
                    $.messager.alert('提示', '操作失败:'+data.errormsg+'!', 'error');
                }else {
                    $('#whgdg').datagrid('reload');
                }
            });
        }
        
        function doOff(idx) {
            var row = $("#whgdg").datagrid("getRows")[idx];
            var url = getFullUrl('/admin/branch/saveBranch?saveType=edit&id='+row.id + "&state=0");
            $.getJSON(url,function (data) {
                if("1" != data.success){
                    $.messager.alert('提示', '操作失败:'+data.errormsg+'!', 'error');
                }else {
                    $('#whgdg').datagrid('reload');
                }
            });
        }

        function preForEdit(row) {
            getAreaList();
            $("#name").textbox("setValue",row.name);
            $("#contacts").textbox("setValue",row.contacts);
            $("#contactway").textbox("setValue",row.contactway);
            $("#areaid").combobox("setValue",row.areaid);
        }
        
        function preForAdd() {
            getAreaList();
            $("#name").textbox("setValue","");
            $("#contacts").textbox("setValue","");
            $("#contactway").textbox("setValue","");
        }

        function getAreaList() {
            var str = '${areaList}';
            if("" == str){
                return "";
            }
            var areaList = JSON.parse(str);
            var data = [];
            for(var i = 0;i < areaList.length;i++){
                var item = areaList[i];
                var option = {};
                option.typid = item.typid;
                option.typname = item.typname;
                data.push(option);
            }
            $("#areaid").combobox('loadData',data);
        }

        function getAreaName(areaId) {
            var str = '${areaList}';
            if("" == str){
                return "";
            }
            var areaList = JSON.parse(str);
            for(var i = 0;i < areaList.length;i++){
                var item = areaList[i];
                if(areaId == item.typid){
                    return item.typname;
                }
            }
            return "";
        }
        
        function getStateName(state) {
            if('0' == state){
                return '未启用';
            }else if('1' == state){
                return '已启用';
            }else {
                return '未知状态';
            }
        }
    </script>
</head>
<body>

    <!-- 表格 -->
    <table id="whgdg" title="分馆管理" class="easyui-datagrid" style="display: none"
           data-options="fit:true, striped:true, rownumbers:true, fitColumns:false, singleSelect:false, checkOnSelect:true, selectOnCheck:true, pagination:true, toolbar:'#whgdg-tb', url:'${basePath}/admin/branch/branchList'">
        <thead>
        <tr>
            <th data-options="field:'ck', checkbox:true"></th>
            <th data-options="field:'name', sortable: true,width:120">名称</th>
            <th data-options="field:'areaid', width:150,formatter:getAreaName">区域</th>
            <th data-options="field:'contacts', width:150">联系人</th>
            <th data-options="field:'contactway', width:100">联系手机</th>
            <th data-options="field:'state', width:80,formatter:getStateName">状态</th>
            <th data-options="field:'_opt', width:540, formatter:WhgComm.FMTOpt, optDivId:'whgdg-opt'">操作</th>
        </tr>
        </thead>
    </table>
    <!-- 表格 END -->
    <!-- 表格操作工具栏 -->
    <div id="whgdg-tb" style="display: none;">
        <shiro:hasPermission name="${resourceid}:add"><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" onclick="doAdd();">添加分馆</a></shiro:hasPermission>
        <shiro:hasPermission name="${resourceid}:off"><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-no" onclick="doBatchUpd(0);">批量停用</a></shiro:hasPermission>
        <shiro:hasPermission name="${resourceid}:on"> <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="doBatchUpd(1);">批量启用</a></shiro:hasPermission>
    </div>
    <!-- 表格操作工具栏-END -->
    <!-- 操作按钮 -->
    <div id="whgdg-opt" style="display: none;">
        <shiro:hasPermission name="${resourceid}:view"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" method="doSee">查看</a></shiro:hasPermission>
        <shiro:hasPermission name="${resourceid}:edit"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validKey="state" validVal="1" method="doEdit">编辑</a></shiro:hasPermission>
        <shiro:hasPermission name="${resourceid}:off"> <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validKey="state" validVal="1" method="doOff">停用</a></shiro:hasPermission>
        <shiro:hasPermission name="${resourceid}:on">  <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validKey="state" validVal="0" method="doOn">启用</a></shiro:hasPermission>
        <shiro:hasPermission name="${resourceid}:del"> <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validKey="state" validVal="0" method="doDel">删除</a></shiro:hasPermission>
    </div>

    <div id="dd" style="display: none">
        <form id="whgff" method="post" class="whgff">
            <div class="whgff-row">
                <div class="whgff-row-label" style="width: 15%"><i>*</i>名称：</div>
                <div class="whgff-row-input" style="width: 80%">
                    <input class="easyui-textbox" name="name" id="name"
                           style="width: 250px; height: 32px" data-options="required:true,validType:['length[1,60]']" value=""/>
                </div>
            </div>
            <div class="whgff-row">
                <div class="whgff-row-label" style="width: 15%"><i>*</i>区域：</div>
                <div class="whgff-row-input" style="width: 80%">
                    <input class="easyui-combobox" style="width: 250px; height: 32px" name="areaid" id="areaid" panelHeight="auto" limitToList="true"
                           data-options="required:true, editable:false,multiple:false, mode:'remote',
                        valueField:'typid',
                        textField:'typname',
                        onLoadSuccess: function(){$(this).combobox('setValue','${act.venueid}')},
                        prompt:'请选择区域'"/>
                </div>
            </div>
            <div class="whgff-row">
                <div class="whgff-row-label" style="width: 15%">联系人：</div>
                <div class="whgff-row-input" style="width: 80%">
                    <input class="easyui-textbox" name="contacts" id="contacts"
                           style="width: 250px; height: 32px" data-options="required:false,validType:['length[1,60]']" value=""/>
                </div>
            </div>
            <div class="whgff-row">
                <div class="whgff-row-label" style="width: 15%">联系方式：</div>
                <div class="whgff-row-input" style="width: 80%">
                    <input class="easyui-textbox" name="contactway" id="contactway"
                           style="width: 250px; height: 32px" data-options="required:false,validType:['length[1,60]','isPhone']" value=""/>
                </div>
            </div>
        </form>
    </div>

</body>
</html>
