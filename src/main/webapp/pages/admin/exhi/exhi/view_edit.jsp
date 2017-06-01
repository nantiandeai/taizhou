<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<% request.setAttribute("basePath", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath());%>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>编辑展览</title>
    <link rel="stylesheet" href="${basePath}/static/admin/css/bootstrap.css"/>
    <link rel="stylesheet" href="${basePath}/static/admin/Font-Awesome/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${basePath}/static/admin/css/build.css"/>
    <%@include file="/pages/comm/admin/header.jsp"%>
    <!-- 图片上传相关 -->
    <script type="text/javascript" src="${basePath}/static/plupload/lib/plupload-2.1.2/js/plupload.full.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/plupload/upload-img.js"></script>
    <!-- 图片上传相关-END -->
</head>
<body>



<form id="whgff" class="whgff" method="post">
    <c:choose>
        <c:when test="${targetShow == '1'}">
            <h2>数字展览管理——查看展览</h2>
        </c:when>
        <c:otherwise>
            <h2>数字展览管理——编辑展览</h2>
        </c:otherwise>
    </c:choose>
    <input type="hidden" name="exhid" value="${exhid}">
    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>标题：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="exhtitle" value="${exhi.exhtitle}" style="width:600px; height:32px" data-options="required:true, validType:'length[1,30]'"></div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>艺术分类：</div>
        <div class="whgff-row-input"><input class="easyui-combobox " name="exharttyp" value="${exhi.exharttyp}"
                                            style="height:32px;width: 600px;"
                                            data-options="editable:false, valueField:'id',textField:'text',prompt:'艺术分类',data:WhgSysData.getTypeData('1'),required:true"/></div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red"></label>文化展类型：</div>
        <div class="whgff-row-input"><input class="easyui-combobox " name="exhtype" value="${exhi.exhtype}"
                                            style="height:32px;width: 600px;"
                                            data-options="editable:false, valueField:'id',textField:'text',prompt:'文化展类型',data:WhgSysData.getTypeData('15'),required:false"/></div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>展览地址：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="exhaddress" value="${exhi.exhaddress}" style="width:600px; height:32px" data-options="required:true, validType:'length[1,30]'"></div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>开始时间：</div>
        <div class="whgff-row-input">
            <input type="text" class="easyui-datetimebox" style="width:300px;height: 32px;" name="startTime" value="<fmt:formatDate value='${exhi.exhstime}' pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate>" data-options="required:true,editable:false,prompt:'请选择'"/>
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>结束时间：</div>
        <div class="whgff-row-input">
            <input type="text" class="easyui-datetimebox" style="width:300px;height: 32px;" name="endTime" value="<fmt:formatDate value='${exhi.exhetime}' pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate>" data-options="required:true,editable:false,prompt:'请选择'"/>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>封面图片图片：</div>
        <div class="whgff-row-input">
            <input type="hidden" id="cult_picture1" name="exhpic" value="${exhi.exhpic}">
            <div class="whgff-row-input-imgview" id="previewImg1"></div>
            <div class="whgff-row-input-imgfile">
                <i><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="imgUploadBtn1">选择图片</a></i>
                <i>图片格式为jpg、png、gif，建议图片尺寸 750*500，大小为2MB以内</i>
            </div>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>展览简介：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="exhdesc"  value="${exhi.exhdesc}" style="width:550px; height:250px" data-options="required:true, multiline:true, validType:['length[1,500]', 'isText']">
        </div>
    </div>
</form>

<div id="whgwin-add-btn" class="whgff-but" style="width: 400px; margin:20px 0px 50px 350px">
    <div style="display: inline-block; margin: 0 auto">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="whgwin-add-btn-save">保 存</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-undo" onclick="WhgComm.editDialogClose()">返  回</a>
    </div>
</div>

<!-- script -->
<script type="text/javascript">
    $(function () {
        WhgUploadImg.init({basePath: '${basePath}', uploadBtnId: 'imgUploadBtn1', hiddenFieldId: 'cult_picture1', previewImgId: 'previewImg1'});
        $('#whgff').form({
            novalidate: true,
            url: "${basePath}/admin/exhi/exhi/edit",
            onSubmit : function(param) {
                var _valid = $(this).form('enableValidation').form('validate')
                if(_valid) {
                    //图片必填
                    if($('#cult_picture1').val() == ""){
                        _valid = false;
                        $.messager.alert('提示', '封面图片');
                    }
                }
                if (!_valid){
                    $.messager.progress('close');
                    $('#whgwin-add-btn-save').off('click').one('click', function () { $('#whgff').submit(); });
                }
                return _valid;
            },
            success : function(data) {
                $.messager.progress('close');
                var Json = eval('('+data+')');
                if(Json && Json.success == '1'){
                    window.parent.$('#whgdg').datagrid('reload');
                    WhgComm.editDialogClose();
                } else {
                    $.messager.alert('提示', '操作失败！', 'error');
                }
            }
        });
        //注册提交事件
        $('#whgwin-add-btn-save').off('click').one('click', function () { $('#whgff').submit(); });
    });

    //查看时的处理
    $(function () {
        var targetShow = '${targetShow}';

        if (targetShow){
            //取消表单验证
            $("#whgff").form("disableValidation");

            $('.easyui-textbox').textbox('readonly');
            $('.easyui-combobox').combobox('readonly');

            //不显示提交 button
            $('#whgwin-add-btn-save').hide();
            return;
        }
    });
</script>
<!-- script END -->
</body>
</html>
