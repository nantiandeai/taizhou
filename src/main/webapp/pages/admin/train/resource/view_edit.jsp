<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<% request.setAttribute("basePath", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath());%>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>编辑资源</title>
    <%@include file="/pages/comm/admin/header.jsp"%>
    <link rel="stylesheet" href="${basePath}/static/admin/css/bootstrap.css"/>
    <link rel="stylesheet" href="${basePath}/static/admin/Font-Awesome/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${basePath}/static/admin/css/build.css"/>
    <!-- 图片上传相关 -->
    <script type="text/javascript" src="${basePath}/static/plupload/lib/plupload-2.1.2/js/plupload.full.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/plupload/upload-img.js"></script>
    <!-- 图片上传相关-END -->
<body>

<form id="whgff" class="whgff" method="post">

    <h2>编辑资源</h2>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>资源类型：</div>
        <select id="cc" class="easyui-combobox" name="enttype" style="width:300px; height:32px">
            <%--<option value=""></option>--%>
            <option value="1">图片</option>
            <option value="2">视频</option>
            <option value="3">音频</option>
        </select>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>资源名称：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="entname" value="${wcr.entname}" style="width:600px; height:32px" data-options="required:true, validType:'length[1,60]'"></div>
    </div>

    <div class="whgff-row video_wrap">
        <div class="whgff-row-label"><i>*</i>资源地址：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" name="enturl" value="${wcr.enturl}" id="video_enturl" style="height:35px;width:600px"
                   data-options="editable:false,required:true, valueField:'addr', textField:'key', url:'${basePath}/admin/video/srchPagging'"/>
        </div>
    </div>

    <div class="whgff-row video_wrap">
        <div class="whgff-row-label"><i></i>视频/音频时长：</div>
        <div class="whgff-row-input"><input class="easyui-timespinner" name="enttimes" value="${wcr.enttimes}" style="width:300px; height:32px" data-options="showSeconds:true"></div>
    </div>


    <div class="whgff-row picture_warp" style="display:none">
        <div class="whgff-row-label"><i>*</i>资源图片：</div>
        <div class="whgff-row-input">
            <input type="hidden" id="cult_picture2" name="penturl" value="${wcr.enturl}">
            <div class="whgff-row-input-imgview" id="previewImg2"></div>
            <div class="whgff-row-input-imgfile">
                <i><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="imgUploadBtn2">选择图片</a></i>
                <i>图片格式为jpg、png、gif，大小为2MB以内</i>
            </div>
        </div>
    </div>

    <div class="whgff-row  video_wrap" id="spfm">
        <div class="whgff-row-label"><i></i>视频封面图片：</div>
        <div class="whgff-row-input">
            <input type="hidden" id="cult_picture1" name="deourl" value="${wcr.deourl}">
            <div class="whgff-row-input-imgview" id="previewImg1"></div>
            <div class="whgff-row-input-imgfile">
                <i><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="imgUploadBtn1">选择图片</a></i>
                <i>图片格式为jpg、png、gif，建议图片尺寸 750*500，大小为2MB以内</i>
            </div>
        </div>
    </div>

</form>

<div id="whgwin-add-btn" class="whgff-but" style="width: 400px; margin:20px 0px 50px 350px">
    <a href="javascript:void(0)" class="easyui-linkbutton whgff-but-submit" iconCls="icon-ok" id="whgwin-add-btn-save">提 交</a>
    <a href="javascript:void(0)" class="easyui-linkbutton whgff-but-clear" iconCls="icon-undo" onclick="WhgComm.editDialogClose()">返 回</a>
</div>

<!-- script -->
<script type="text/javascript">
    $(function () {
        //图片上传
        WhgUploadImg.init({basePath: '${basePath}', uploadBtnId: 'imgUploadBtn1', hiddenFieldId: 'cult_picture1', previewImgId: 'previewImg1',needCut:true});
        WhgUploadImg.init({basePath: '${basePath}', uploadBtnId: 'imgUploadBtn2', hiddenFieldId: 'cult_picture2', previewImgId: 'previewImg2',needCut:false});

        $('#whgff').form({
            novalidate: true,
            url : getFullUrl('/admin/train/resource/edit?entid=${entid}'),
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
                    window.parent.$('#whgdg').datagrid('reload');
                    WhgComm.editDialogClose();
                } else {
                    $.messager.alert('提示', '操作失败！', 'error');
                    $('#whgwin-add-btn-save').off('click').one('click', function () { $('#whgff').submit(); });
                }
            }
        });
        //注册提交事件
        $('#whgwin-add-btn-save').off('click').one('click', function () { $('#whgff').submit(); });

        selectLoad("${wcr.enttype}");

        $('#cc').combobox({
            onLoadSuccess: function () {
                $("#cc").combobox("select", "${wcr.enttype}");
            },
            onChange: function (newv, oldv) {
                selectLoad(newv);
            }
        });

        function selectLoad(newv) {
            console.log(newv);
            if (newv == 1) {
                $(".picture_warp").show();
                $(".video_wrap").hide();
            } else if (newv == 2) {
                $(".video_wrap").show();
                $(".picture_warp").hide();
                <%--$("[name=deourl]").val("${wcr.deourl}");--%>
            } else if (newv == 3) {
                $(".video_wrap").show();
                $("#spfm").hide();
                $(".picture_warp").hide();
//                $("[name=deourl]").val("");
            }
        }

    });
</script>
<!-- script END -->
</body>
</html>