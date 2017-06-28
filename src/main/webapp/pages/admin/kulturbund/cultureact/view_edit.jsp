<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<% request.setAttribute("basePath", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath());%>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<% String path = request.getContextPath();%>
<!DOCTYPE html>
<html>
<head>
    <%@include file="/pages/comm/admin/header.jsp"%>
    <c:choose>
        <c:when test="${editType eq 'edit'}">
            <c:set var="pageTitle" value="文化联盟大型活动编辑"></c:set>
        </c:when>
        <c:when test="${editType eq 'add'}">
            <c:set var="pageTitle" value="文化联盟大型活动添加"></c:set>
        </c:when>
        <c:when test="${editType eq 'view'}">
            <c:set var="pageTitle" value="文化联盟大型活动查看"></c:set>
        </c:when>
        <c:otherwise>
            <c:set var="pageTitle" value="无标题"></c:set>
        </c:otherwise>
    </c:choose>
    <title>${pageTitle}</title>
    <link rel="stylesheet" href="${basePath}/static/admin/Font-Awesome/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${basePath}/static/admin/css/build.css"/>
    <script type="text/javascript" charset="utf-8"	src="${basePath}/static/ueditor/ueditor.config.js"></script>
    <script type="text/javascript" charset="utf-8"	src="${basePath}/static/ueditor/ueditor.all.min.js"></script>
    <script type="text/javascript" charset="utf-8"	src="${basePath}/static/ueditor/lang/zh-cn/zh-cn.js"></script>
    <script type="text/javascript" src="${basePath}/static/plupload/lib/plupload-2.1.2/js/plupload.full.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/plupload/upload-img.js"></script>
    <script type="text/javascript" src="${basePath}/static/admin/js/whgtoolmodule.js"></script>
    <script type="text/javascript" src="${basePath}/static/common/js/whg.maps.js"></script>
    <script type="text/javascript" src="${basePath}/static/admin/js/whgmodule-venseatmaps.js"></script>
</head>
<body>
<form id="whgff" method="post" class="whgff">
    <input id="type" type="hidden" name="editType" value="${editType}">
    <h2>${pageTitle}</h2>
    <input id="id" name="id" type="hidden" value="${whgCultureAct.id}">
    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>活动标题：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="culactname"
                   style="width: 500px; height: 32px" data-options="required:true,validType:['length[1,60]']" value="${whgCultureAct.culactname}"/>
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label">
            <label style="color: red">*</label>上传封面：
        </div>
        <div class="whgff-row-input">
            <input type="hidden" id="act_imgurl1" name="culactcover" data-options="required:true" value="${whgCultureAct.culactcover}">
            <div class="whgff-row-input-imgview" id="previewImg1"></div>
            <div class="whgff-row-input-imgfile" id="imgUrl_pic">
                <i><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="imgUploadBtn1">选择图片</a></i>
                <i>图片格式为jpg、png、gif，建议图片尺寸 750*500，大小为2MB以内</i>
            </div>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">
            <label style="color: red">*</label>活动内容：
        </div>
        <div class="whgff-row-input">
            <script id="remark" name="remark" id="remark" type="text/plain" style="width: 800px; height: 600px;">${whgCultureAct.culactcontent}</script>
        </div>
    </div>

</form>
<div id="whgwin-edit-btn" class="whgff-but" style="width: 400px; margin:20px 0px 50px 350px">
    <c:if test="${editType != 'view'}">
        <a href="javascript:void(0)" class="easyui-linkbutton whgff-but-submit" iconCls="icon-ok" id="whgwin-edit-btn-save">提交</a>
    </c:if>
    <a href="javascript:void(0)" class="easyui-linkbutton whgff-but-clear" iconCls="icon-undo" onclick="WhgComm.editDialogClose()">返 回</a>
</div>
</body>
<script type="text/javascript">
    var whgImg = WhgUploadImg.init({
        basePath: '${basePath}',
        uploadBtnId: 'imgUploadBtn1',
        hiddenFieldId: 'act_imgurl1',
        previewImgId: 'previewImg1',
        needCut:true
    });

    var ueConfig = {
        scaleEnabled: false,
        autoFloatEnabled: false
    };

    var remark = UE.getEditor('remark', ueConfig);

    $('#whgff').form({
        novalidate: true,
        url : getFullUrl('/admin/cultureact/doEdit/${editType}'),
        onSubmit : function(param) {
            var _valid = $(this).form('enableValidation').form('validate');
            if(_valid){
                var act_imgurl1 = $("#act_imgurl1").val();
                if(null == act_imgurl1 || "" == act_imgurl1){
                    $.messager.alert("错误", '封面图片不能为空！', 'error');
                    _valid = false;
                    $('#whgwin-edit-btn-save').off('click').one('click',saveFun);
                    return _valid;
                }
                if (!remark.hasContents()){
                    $.messager.alert("错误", '活动内容不能为空', 'error');
                    _valid = false;
                    $('#whgwin-edit-btn-save').off('click').one('click',saveFun);
                    return _valid;
                }
                $.messager.progress();
            }else{
                //失败时再注册提交事件
                $('#whgwin-edit-btn-save').off('click').one('click',saveFun);
            }
            return _valid;
        },
        success : function(data) {
            $.messager.progress('close');
            var Json = eval('('+data+')');
            if(Json && Json.success == '1'){
                if($('#whgff input[name="onlyshow"]').val() != "1") {
                    window.parent.$('#whgdg').datagrid('reload');
                }
                WhgComm.editDialogClose();
            } else {
                $.messager.alert('提示', '操作失败:'+Json.errormsg+'!', 'error');
                $('#whgwin-add-btn-save').off('click').one('click', saveFun);
            }
        }
    });

    function saveFun() {
        $('#whgff').submit();
    }

    $('#whgwin-edit-btn-save').off('click').one('click', saveFun);
</script>
</html>
