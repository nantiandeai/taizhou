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
            <c:set var="pageTitle" value="文化联盟资讯编辑列表"></c:set>
        </c:when>
        <c:when test="${listType eq 'check'}">
            <c:set var="pageTitle" value="文化联盟资讯审核列表"></c:set>
        </c:when>
        <c:when test="${listType eq 'publish'}">
            <c:set var="pageTitle" value="文化联盟资讯发布列表"></c:set>
        </c:when>
    </c:choose>

    <title>${pageTitle}</title>
    <%@include file="/pages/comm/admin/header.jsp"%>
</head>
<body>
<table id="whgdg" title="${pageTitle}" class="easyui-datagrid" style="display: none"
       data-options="fit:true, striped:true, rownumbers:true, fitColumns:true, singleSelect:false, checkOnSelect:true, selectOnCheck:true, pagination:true, toolbar:'#whgdg-tb', url:'${basePath}/admin/cultureinfo/srchList4p?listType=${listType}'">
    <thead>
    <tr>
        <th data-options="field:'culzxtitle',width:80">资讯标题</th>
        <th data-options="field:'culzxcreator',width:80,formatter:getUserName">创建人</th>
        <th data-options="field:'culzxcreattime', width:60,formatter:WhgComm.FMTDateTime">创建时间</th>
        <th data-options="field:'culzxstate', width:50,formatter:myState" >状态</th>
        <th data-options="field:'_opt', width:450, formatter:WhgComm.FMTOpt,fixed:true, optDivId:'whgdg-opt'">操作</th>
    </tr>
    </thead>

</table>

<!-- 表格操作工具栏 -->
<div id="whgdg-tb" style="display: none;">
    <div id="tb">
        <shiro:hasPermission name="${resourceid}:add"><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" onclick="doAdd()">添加</a></shiro:hasPermission>
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
    <shiro:hasPermission name="${resourceid}:view"><a href="javascript:void(0)" class="easyui-linkbutton" validKey="culzxstate" validVal="0,1,2,3" method="doSee">查看</a></shiro:hasPermission>
    <c:choose>
        <c:when test="${listType eq 'edit'}">
            <shiro:hasPermission name="${resourceid}:edit"><a href="javascript:void(0)" class="easyui-linkbutton" validKey="culzxstate" validVal="0" method="doEdit">编辑</a></shiro:hasPermission>
            <shiro:hasPermission name="${resourceid}:checkgo"><a href="javascript:void(0)" class="easyui-linkbutton" validKey="culzxstate" validVal="0" method="checkgo">送审</a></shiro:hasPermission>
            <shiro:hasPermission name="${resourceid}:del"><a href="javascript:void(0)" class="easyui-linkbutton" validKey="culzxstate" validVal="0,1,2" method="del">删除</a></shiro:hasPermission>
        </c:when>
        <c:when test="${listType eq 'check'}">
            <shiro:hasPermission name="${resourceid}:checkon"><a href="javascript:void(0)" class="easyui-linkbutton" validKey="culzxstate" validVal="1" method="checkon">审核通过</a></shiro:hasPermission>
            <shiro:hasPermission name="${resourceid}:checkoff"><a href="javascript:void(0)" class="easyui-linkbutton" validKey="culzxstate" validVal="1" method="checkoff">审核不通过</a></shiro:hasPermission>
            <shiro:hasPermission name="${resourceid}:del"><a href="javascript:void(0)" class="easyui-linkbutton" validKey="culzxstate" validVal="0,1,2" method="del">删除</a></shiro:hasPermission>
        </c:when>
        <c:when test="${listType eq 'publish'}">
            <shiro:hasPermission name="${resourceid}:publish"><a href="javascript:void(0)" class="easyui-linkbutton" validKey="culzxstate" validVal="2" method="publish">发布</a></shiro:hasPermission>
            <shiro:hasPermission name="${resourceid}:publishoff"><a href="javascript:void(0)" class="easyui-linkbutton" validKey="culzxstate" validVal="3" method="publishoff">取消发布</a></shiro:hasPermission>
            <shiro:hasPermission name="${resourceid}:del"><a href="javascript:void(0)" class="easyui-linkbutton" validKey="culzxstate" validVal="0,1,2" method="del">删除</a></shiro:hasPermission>
        </c:when>
        <c:when test="${listType eq 'cycle'}">
            <shiro:hasPermission name="${resourceid}:del"><a href="javascript:void(0)" class="easyui-linkbutton" validKey="culzxstate" validVal="0,1,2,3" method="doDelForver">删除</a></shiro:hasPermission>
            <shiro:hasPermission name="${resourceid}:undel"><a href="javascript:void(0)" class="easyui-linkbutton" validKey="culzxstate" validVal="0,1,2,3" method="undel">还原</a></shiro:hasPermission>
        </c:when>
    </c:choose>
</div>
<!-- 操作按钮-END -->

<div id="dd" style="display: none">
    <form id="whgff" method="post" class="whgff">
        <div class="whgff-row">
            <div class="whgff-row-label" style="width: 15%"><i>*</i>标题：</div>
            <div class="whgff-row-input" style="width: 80%">
                <input class="easyui-textbox" name="culzxtitle" id="culzxtitle"
                       style="width: 300px; height: 32px" data-options="required:true,validType:['length[1,60]']" value=""/>
            </div>
        </div>
        <div class="whgff-row">
            <div class="whgff-row-label" style="width: 15%">简介：</div>
            <div class="whgff-row-input" style="width: 80%">
                <input class="easyui-textbox" style="width: 300px; height: 50px" name="culzxdesc" id="culzxdesc" panelHeight="auto" limitToList="true"
                       data-options="required:false, multiline:true,validType:['length[1,256]'],prompt:'请输入简介'"/>
            </div>
        </div>
        <div class="whgff-row">
            <div class="whgff-row-label" style="width: 15%">链接：</div>
            <div class="whgff-row-input" style="width: 80%">
                <input class="easyui-textbox" name="culzxlink" id="culzxlink"
                       style="width: 300px; height: 32px" data-options="required:true,validType:['length[1,1000]','url'],prompt:'请输入连接地址,如：http://hn.creatoo.cn'" value=""/>
            </div>
        </div>
    </form>
</div>

</body>
<script type="text/javascript">
    function preForAdd() {
        $("#culzxtitle").textbox("setValue", "");
        $("#culzxdesc").textbox("setValue", "");
        $("#culzxlink").textbox("setValue", "");
    }
    
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
                                url : getFullUrl('/admin/cultureinfo/edit/add'),
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

    function preForEdit(row) {
        $("#culzxtitle").textbox("setValue", row.culzxtitle);
        $("#culzxdesc").textbox("setValue", row.culzxdesc);
        $("#culzxlink").textbox("setValue", row.culzxlink);
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
                                url : getFullUrl('/admin/cultureinfo/edit/edit?id=' + row.id),
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

    function checkgo(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        $.messager.confirm("确认信息", "确定要送审吗？", function(r){
            if(!r){
                return;
            }
            $.getJSON("${basePath}/admin/cultureinfo/doUpdateState?id=" + curRow.id +"&state=checkpending",function (data) {
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
            if(!r){
                return;
            }
            $.getJSON("${basePath}/admin/cultureinfo/doUpdateState?id=" + curRow.id +"&state=checked",function (data) {
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
            if(!r){
                return;
            }
            $.getJSON("${basePath}/admin/cultureinfo/doUpdateState?id=" + curRow.id +"&state=initial",function (data) {
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
            if(!r){
                return;
            }
            $.getJSON("${basePath}/admin/cultureinfo/doUpdateState?id=" + curRow.id +"&state=published",function (data) {
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
            if(!r){
                return;
            }
            $.getJSON("${basePath}/admin/cultureinfo/doUpdateState?id=" + curRow.id +"&state=initial",function (data) {
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
            if(!r){
                return;
            }
            $.getJSON("${basePath}/admin/cultureinfo/doDel?id=" + curRow.id + "&state=del",function (data) {
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
            if(!r){
                return;
            }
            $.getJSON("${basePath}/admin/cultureinfo/doDel?id=" + curRow.id + "&state=undel",function (data) {
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
            if(!r){
                return;
            }
            $.getJSON("${basePath}/admin/cultureinfo/doDel?id=" + curRow.id + "&state=delforever",function (data) {
                if("1" != data.success){
                    $.messager.alert('提示', '操作失败:'+data.errormsg+'!', 'error');
                    return;
                }
                $('#whgdg').datagrid('reload');
            });
        });
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
</script>
</html>
