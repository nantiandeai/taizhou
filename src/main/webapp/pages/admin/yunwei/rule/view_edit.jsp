<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<% request.setAttribute("basePath", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath());%>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>黑名单规则配置</title>
    <%@include file="/pages/comm/admin/header.jsp"%>
    <link rel="stylesheet" href="${basePath}/static/admin/css/bootstrap.css"/>
    <link rel="stylesheet" href="${basePath}/static/admin/Font-Awesome/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${basePath}/static/admin/css/build.css"/>
    <script type="text/javascript" src="${basePath}/static/plupload/lib/plupload-2.1.2/js/plupload.full.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/plupload/upload-img.js"></script>
</head>
<body>

<form id="whgff" class="whgff" method="post">

    <h2>配置黑名单规则</h2>

    <input type="hidden" name="id" value="${rule.id}">
    <div class="whgff-row">
        <div class="whgff-row-input">
            <input class="easyui-numberbox"  name="cancelnum" value="${rule.cancelnum}" style="width:60px; height:32px" data-options="required:true,min:1,max:100">
            次取消预约，则加入黑名单，
            <input class="easyui-numberbox" name="cancelday" value="${rule.cancelday}" style="width:60px; height:32px" data-options="required:true,min:1,max:100">
            天内，不得进行活动的预约<br><br>
            <input class="easyui-numberbox" name="missnum" value="${rule.missnum}" style="width:60px; height:32px" data-options="required:true,min:1,max:100">
            次未参加活动，则加入黑名单，
            <input class="easyui-numberbox" name="missday" value="${rule.missday}" style="width:60px; height:32px" data-options="required:true,min:1,max:100">
            天内，不得进行活动的报名
        </div>
    </div>
</form>

<div id="whgwin-add-btn" class="whgff-but" style="width: 400px; margin:20px 0px 50px 350px">
    <a href="javascript:void(0)" class="easyui-linkbutton whgff-but-submit" iconCls="icon-ok" id="whgwin-add-btn-save">保 存</a>
</div>

<!-- script -->
<script type="text/javascript">
    $(function () {
        $('#whgff').form({
            novalidate: true,
            url : getFullUrl('/admin/yunwei/rule/add'),
            onSubmit : function(param) {
                //alert('onSubmit');
                var _valid = $(this).form('enableValidation').form('validate')
                if(_valid){
                    $.messager.progress();
                }else{
                    //失败时再注册提交事件
                    $('#whgwin-add-btn-save').off('click').one('click', function () { $('#whgff').submit(); });
                }
                return _valid;
            },
            success : function(data) {
                $.messager.progress('close');
                var Json = eval('('+data+')');
                if(Json && Json.success == '1'){
                    $.messager.alert('提示', '操作成功！', 'success');
                } else {
                    $.messager.alert('提示', '操作失败！', 'error');
                    $('#whgwin-add-btn-save').off('click').one('click', function () { $('#whgff').submit(); });
                }
            }
        });
        //注册提交事件
        $('#whgwin-add-btn-save').off('click').one('click', function () { $('#whgff').submit(); });
    });
</script>
<!-- script END -->
</body>
</html>