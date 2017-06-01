<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<% request.setAttribute("basePath", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath());%>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <title>数字展馆列表</title>
    <%@include file="/pages/comm/admin/header.jsp"%>
    <c:choose>
        <c:when test="${type eq 'edit'}">
            <c:set var="pageTitle" value="展馆编辑列表"></c:set>
        </c:when>
        <c:when test="${type eq 'check'}">
            <c:set var="pageTitle" value="展馆审核列表"></c:set>
        </c:when>
        <c:when test="${type eq 'publish'}">
            <c:set var="pageTitle" value="展馆发布列表"></c:set>
        </c:when>
        <c:when test="${type eq 'recycle'}">
            <c:set var="pageTitle" value="回收站"></c:set>
        </c:when>
        <c:otherwise>
            <c:set var="pageTitle" value="无标题"></c:set>
        </c:otherwise>
    </c:choose>
    <script type="text/javascript" charset="utf-8" src="${basePath}/static/ueditor/ueditor.config.js"></script>
    <script type="text/javascript" charset="utf-8" src="${basePath}/static/ueditor/ueditor.all.min.js"></script>
    <script type="text/javascript" charset="utf-8" src="${basePath}/static/ueditor/lang/zh-cn/zh-cn.js"></script>
</head>
<body>
<!-- 表格 -->
<table id="whgdg" title="${pageTitle}" class="easyui-datagrid" style="display: none"
       data-options="fit:true, striped:true, rownumbers:true, fitColumns:true, singleSelect:true, checkOnSelect:true, selectOnCheck:true, pagination:true, toolbar:'#tb',
       url:'${basePath}/admin/pavilion/getList?type=${type}'">
    <thead>
    <tr>
        <th data-options="field:'hallname', width:120">展馆名称</th>
        <th data-options="field:'hallsummary', width:200, ">展馆简介</th>
        <th data-options="field:'hallcover', width:80, formatter:showImg">展馆封面</th>
        <th data-options="field:'hallstate', width:60, formatter:setMyState">状态</th>
        <th data-options="field:'_opt', width:300, formatter:WhgComm.FMTOpt, optDivId:'whgdg-opt'">操作</th>
    </tr>
    </thead>

    <c:choose>
        <c:when test="${type eq 'edit'}">
            <div id="tb">
                <shiro:hasPermission name="${resourceid}:add"><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" onclick="addPavilion()">添加</a></shiro:hasPermission>
            </div>
        </c:when>
    </c:choose>


    <div id="whgdg-opt" class="none" style="display:none">
        <shiro:hasPermission name="${resourceid}:view"><a href="javascript:void(0)" validKey="hallstate" validVal="0,1,2,3" method="doView">查看</a></shiro:hasPermission>
        <c:choose>
            <c:when test="${type eq 'edit'}">
                <shiro:hasPermission name="${resourceid}:edit"><a href="javascript:void(0)" validKey="hallstate" validVal="0" method="editzx">编辑</a></shiro:hasPermission>
                <shiro:hasPermission name="${resourceid}:checkgo"><a href="javascript:void(0)" validKey="hallstate" validVal="0" method="doCheckgo">送审</a></shiro:hasPermission>
                <shiro:hasPermission name="${resourceid}:del"><a href="javascript:void(0)" validKey="hallstate" validVal="0,1,2" method="doDel">回收</a></shiro:hasPermission>
            </c:when>
            <c:when test="${type eq 'check'}">
                <shiro:hasPermission name="${resourceid}:checkon"> <a href="javascript:void(0)"  validKey="hallstate" validVal="1" method="doCheckOn">审核通过</a></shiro:hasPermission>
                <shiro:hasPermission name="${resourceid}:checkoff"><a href="javascript:void(0)" validKey="hallstate" validVal="1" method="doCheckOff">审核不通过</a></shiro:hasPermission>
                <shiro:hasPermission name="${resourceid}:del"><a href="javascript:void(0)" validKey="hallstate" validVal="0,1,2" method="doDel">回收</a></shiro:hasPermission>
            </c:when>
            <c:when test="${type eq 'publish'}">
                <shiro:hasPermission name="${resourceid}:publish"><a href="javascript:void(0)" validKey="hallstate" validVal="2" method="doPublish">发布</a></shiro:hasPermission>
                <shiro:hasPermission name="${resourceid}:publishoff"><a href="javascript:void(0)" validKey="hallstate" validVal="3" method="publishoff">取消发布</a></shiro:hasPermission>
                <shiro:hasPermission name="${resourceid}:del"><a href="javascript:void(0)" validKey="hallstate" validVal="0,1,2" method="doDel">回收</a></shiro:hasPermission>
            </c:when>
            <c:when test="${type eq 'recycle'}">
                <shiro:hasPermission name="${resourceid}:del"><a href="javascript:void(0)" validKey="hallstate" validVal="0,1,2,3" method="doDelForver">删除</a></shiro:hasPermission>
                <shiro:hasPermission name="${resourceid}:undel"><a href="javascript:void(0)" validKey="hallstate" validVal="0,1,2,3" method="doUnDel">还原</a></shiro:hasPermission>
            </c:when>
            <c:otherwise>
            </c:otherwise>
        </c:choose>



    </div>
</table>
<!-- 表格 END -->
<script type="text/javascript">

    function setMyState(hallstate) {
        if(0 == hallstate){
            return "可编辑";
        }
        if(1 == hallstate){
            return "待审核";
        }
        if(2 == hallstate){
            return "待发布";
        }
        if(3 == hallstate){
            return "已发布";
        }
    }

    //begin 格式化转换
    function showImg(v, r, i){
        var img = r.hallcover || "";
        if (!img) return "";
        return imgFMT(img, r, i);
    }
    //新增
    function addPavilion() {
        WhgComm.editDialog('${basePath}/admin/pavilion/edit/add');
    }
    //修改
    function editzx(idx){
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        WhgComm.editDialog('${basePath}/admin/pavilion/edit/edit?id='+curRow.id);
    }
    //查看
    function doView(idx){
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        WhgComm.editDialog('${basePath}/admin/pavilion/edit/view?id='+curRow.id);
    }

    /**
     * 送审 [1]->9
     * @param idx
     */
    function doCheckgo(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        $.messager.confirm("确认信息", "确定要送审选中的项吗？", function(r){
            if (r){
                __updStateSend(row.id, 0, 1);
            }
        })
    }

    /**
     * 审通过 [9]->2
     * @param idx
     */
    function doCheckOn(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        $.messager.confirm("确认信息", "确定要审核选中的项吗？", function(r){
            if (r){
                __updStateSend(row.id, 1, 2);
            }
        })
    }

    /**
     * 审不通过 [2]->9
     * @param idx
     */
    function doCheckOff(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        $.messager.confirm("确认信息", "确定要取消审核选中的项吗？", function(r){
            if (r){
                __updStateSend(row.id, 1, 0);
            }
        })
    }

    /**
     * 发布 [2,4]->6
     * @param idx
     */
    function doPublish(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        $.messager.confirm("确认信息", "确定要发布选中的项吗？", function(r){
            if(r){
                __updStateSend(row.id, 2, 3);
            }
        })
    }

    /**
     * 删除
     * @param idx
     */
    function doDel(idx) {
        var row = $("#whgdg").datagrid("getRows")[idx];
        $.messager.confirm("确认信息", "确定要删除选中的项吗？", function(r){
            if (r){
                updateDelState(row.id, 1);
            }
        });
    }

    /**
     * 还原删除
     * @param idx
     */
    function doUnDel(idx) {
        var row = $("#whgdg").datagrid("getRows")[idx];
        $.messager.confirm("确认信息", "确定要还原选中的项吗？", function(r){
            if (r){
                updateDelState(row.id, 2);
            }
        });
    }

    /**
     * 永久删除
     * @param idx
     */
    function doDelForver(idx) {
        var row = $("#whgdg").datagrid("getRows")[idx];
        $.messager.confirm("确认信息", "确定要删除选中的项吗？", function(r){
            if (r){
                updateDelState(row.id, 3);
            }
        });
    }

    function updateDelState(ids,isdel) {
        $.messager.progress();
        var params = {id: ids, isdel: isdel};
        $.post('${basePath}/admin/pavilion/do/updisdel', params, function(data){
            $("#whgdg").datagrid('reload');
            if (!data.success || data.success != "1"){
                $.messager.alert("错误", data.errormsg||'操作失败', 'error');
            }
            $.messager.progress('close');
        }, 'json');
    }

    /**
     * 取消发布 [6]->4
     * @param idx
     */
    function publishoff(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        $.messager.confirm("确认信息", "确定要取消发布选中的项吗？", function(r){
            if (r){
                __updStateSend(row.id, 3, 0);
            }
        })
    }

    /**
     * 状态变更（送审、审核、发布....操作状态变更）
     * @param idx
     */
    function __updStateSend(ids, fromState, toState){
        $.messager.progress();
        var params = {id: ids, fromState: fromState, toState: toState};
        $.post('${basePath}/admin/pavilion/do/updstate', params, function(data){
            $("#whgdg").datagrid('reload');
            if (!data.success || data.success != "1"){
                $.messager.alert("错误", data.errormsg||'操作失败', 'error');
            }
            $.messager.progress('close');
        }, 'json');
    }

</script>
</body>
</html>