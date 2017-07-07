<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<% request.setAttribute("basePath", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath());%>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<% String path = request.getContextPath();%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>编辑特色资源</title>
    <link rel="stylesheet" href="${basePath}/static/admin/css/bootstrap.css"/>
    <link rel="stylesheet" href="${basePath}/static/admin/Font-Awesome/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${basePath}/static/admin/css/build.css"/>

    <%@include file="/pages/comm/admin/header.jsp"%>
    <script type="text/javascript" src="${basePath }/static/common/js/whg.sys.base.data.js"></script>
    <script src="${basePath}/static/admin/js/whgtoolmodule.js"></script>
    <!-- 图片上传相关 -->
    <script type="text/javascript" src="${basePath}/static/plupload/lib/plupload-2.1.2/js/plupload.full.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/plupload/upload-img.js"></script>
    <script type="text/javascript" src="${basePath}/static/plupload/upload-moreimg.js"></script>
    <script type="text/javascript" src="${basePath}/static/plupload/upload-morefile.js"></script>
    <!-- 图片上传相关-END -->
</head>
<body>

<form id="whgff" class="whgff" method="post">
    <c:choose>
        <c:when test="${not empty targetShow}">
            <h2>查看市民上传</h2>
        </c:when>
        <c:otherwise>
            <h2>审核市民上传</h2>
        </c:otherwise>
    </c:choose>
    <input type="hidden" name="id" id="id" value="${id}"/>
    <input type="hidden" name="intype" value="2">
    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>标题：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="name" value="${source.name}" style="width:500px; height:32px" data-options="required:true, readonly:true, validType:'length[1,30]'"></div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>来源：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="income" value="${source.income}" style="width:500px; height:32px" data-options="required:true, readonly:true, validType:'length[1,30]'"></div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>区域：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" name="area" style="width:300px; height:32px"  data-options="prompt:'请选择所属区域', value:'${source.areaid}',required:true, readonly:true,valueField:'id', textField:'text', data:WhgComm.getAreaType()">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">
            <label style="color: red">*</label>资源类型：
        </div>
        <div class="whgff-row-input radio-disabled">
            <div class="radio radio-primary whg-js-data" name="type" id="type" value="${source.type}"
                 js-data='[{"id":"1","text":"图片"},{"id":"2","text":"视频"},{"id":"3","text":"文档"},{"id":"4","text":"音频"}]'>
            </div>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>资源：</div>
        <div class="whgff-row-input">
            <input type="hidden" id="whg_file_upload" name="address" value="${source.address}">
            <div class="whgff-row-input-fileview" id="whg_file_pload_view"></div>
            <div class="whgff-row-input-filefile" >
                <%--<i><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="fileUploadBtn2">上传</a></i>--%>
                <%--<i style="color: #999;font-size: 13px;font-style: normal;">图片格式为jpg、png、gif,大小为2MB以内;视频/音频格式为mp3,mp4,flv,avi,大小为100MB以内;文档格式为doc,docx,xls,zip,xlsx,pdf,大小为10MB以内;</i>--%>
            </div>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>资源描述：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="introduction" id="introduction" value="${source.introduction}" style="width:550px; height:100px" data-options="required:true, readonly:true, multiline:true, validType:['length[1,500]', 'isText']">
        </div>
    </div>

    <c:choose>
        <c:when test="${not empty targetShow}">

        </c:when>
        <c:otherwise>
            <div class="whgff-row">
                <div class="whgff-row-label">审核意见：</div>
                <div class="whgff-row-input">
                    <div class="radio radio-primary whg-js-data" value="6" name="state"
                         js-data='[{"id":"6","text":"通过"},{"id":"4","text":"不通过"}]'>
                    </div>
                </div>
            </div>
        </c:otherwise>
    </c:choose>

</form>

<div id="whgwin-add-btn" class="whgff-but" style="width: 400px; margin:20px 0px 50px 350px">
    <div style="display: inline-block; margin: 0 auto">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="whgwin-add-btn-save">保 存</a>
        <a href="javascript:void(0)" class="easyui-linkbutton whgff-but-clear" iconCls="icon-undo" onclick="WhgComm.editDialogClose()">返 回</a>
    </div>
</div>

<!-- script -->
<script type="text/javascript">
    $(function () {

        WhgUploadMoreFile.init({basePath: '${basePath}', uploadBtnId: 'fileUploadBtn2', hiddenFieldId: 'whg_file_upload',previewFileId:'whg_file_pload_view'});

        $('#whgff').form({
            novalidate: true,
            url: "${basePath}/admin/specialResource/edit",
            onSubmit : function(param) {
                var _valid = $(this).form('enableValidation').form('validate')
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
                    $.messager.alert('提示', '操作失败:'+Json.errormsg+'!', 'error');
                    $('#whgwin-add-btn-save').off('click').one('click', function () { $('#whgff').submit(); });
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

//            $("#whgff").find("input[type='radio']").on('click', function(){return false});
            $(".radio-disabled input[type=radio]").attr("disabled",true);
            //不显示提交 button
            $('#whgwin-add-btn-save').hide();
            return;
        }
        $(".radio-disabled input[type=radio]").attr("disabled",true);
    });
</script>
<!-- script END -->
</body>
</html>
