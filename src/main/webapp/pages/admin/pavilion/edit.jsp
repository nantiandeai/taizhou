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
        <c:when test="${type eq 'edit'}">
            <c:set var="pageTitle" value="展馆编辑"></c:set>
        </c:when>
        <c:when test="${type eq 'add'}">
            <c:set var="pageTitle" value="展馆添加"></c:set>
        </c:when>
        <c:when test="${type eq 'view'}">
            <c:set var="pageTitle" value="展馆查看"></c:set>
        </c:when>
        <c:otherwise>
            <c:set var="pageTitle" value="无标题"></c:set>
        </c:otherwise>
    </c:choose>
    <title>${pageTitle}</title>
    <link rel="stylesheet" href="${basePath}/static/admin/css/bootstrap.css"/>
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
        <input id="type" type="hidden" name="type" value="${type}">
        <input id="editType" type="hidden" name="editType">
        <h2>${pageTitle}</h2>
        <input id="id" name="id" type="hidden" value="${id}">
        <div class="whgff-row">
            <div class="whgff-row-label"><i>*</i>展馆名称：</div>
            <div class="whgff-row-input">
                <input class="easyui-textbox" name="hallname"
                       style="width: 500px; height: 32px" data-options="required:true,validType:['length[1,60]']" value="${hallname}"/>
            </div>
        </div>
        <div class="whgff-row">
            <div class="whgff-row-label"><i>*</i>展馆简介：</div>
            <div class="whgff-row-input">
                <input class="easyui-textbox" name="hallsummary"
                       style="width: 500px; height: 80px" data-options="multiline:true,required:true,validType:['length[1,512]']" value="${hallsummary}"/>
            </div>
        </div>
        <div class="whgff-row">
            <div class="whgff-row-label"><label style="color: red">*</label>所属单位：</div>
            <div class="whgff-row-input">
                <input class="easyui-combobox" name="branch" id="branch" panelHeight="auto" limitToList="true" style="width:500px; height:32px"
                       data-options="required:false, editable:false,multiple:false, mode:'remote',
                   valueField:'id', textField:'name'
                   "/>
            </div>
        </div>
        <div class="whgff-row">
            <div class="whgff-row-label">
                <label style="color: red">*</label>上传封面：
            </div>
            <div class="whgff-row-input">
                <input type="hidden" id="act_imgurl1" name="hallcover" data-options="required:true" value="${hallcover}">
                <div class="whgff-row-input-imgview" id="previewImg1"></div>
                <div class="whgff-row-input-imgfile" id="imgUrl_pic1">
                    <i><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="imgUploadBtn1">选择图片</a></i>
                    <i>图片格式为jpg、png、gif，建议图片尺寸 750*500，大小为2MB以内</i>
                </div>
                <label style="color: red;display: none" id="act_imgurl1_alert">封面不能为空</label>
            </div>
        </div>

        <div class="whgff-row">
            <div class="whgff-row-label">
                <label style="color: red">*</label>上传内景照片：
            </div>
            <div class="whgff-row-input">
                <input type="hidden" id="act_imgurl3" name="hallInterior360" data-options="required:true" value="${hallInterior360}">
                <div class="whgff-row-input-imgview" id="previewImg3"></div>
                <div class="whgff-row-input-imgfile" id="imgUrl_pic3">
                    <i><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="imgUploadBtn3">选择图片</a></i>
                    <i>图片格式为jpg、png、gif，建议图片尺寸 1280*500，大小为2MB以内</i>
                </div>
                <label style="color: red;display: none" id="act_imgurl3_alert">内景照片不能为空</label>
            </div>
        </div>

        <div class="whgff-row">
            <div class="whgff-row-label">展馆地址：</div>
            <div class="whgff-row-input">
                <input class="easyui-textbox" name="halladdress"
                       style="width: 500px; height: 32px" data-options="validType:['length[1,512]']" value="${halladdress}"/>
            </div>
        </div>
        <div class="whgff-row">
            <div class="whgff-row-label">展馆联系电话：</div>
            <div class="whgff-row-input">
                <input class="easyui-textbox" name="hallphone"
                       style="width: 500px; height: 32px" data-options="validType:['length[1,60]']" value="${hallphone}"/>
            </div>
        </div>
    </form>
    <div id="whgwin-edit-btn" class="whgff-but" style="width: 400px; margin:20px 0px 50px 350px">
        <c:if test="${type == 'add'}">
            <a href="javascript:void(0)" class="easyui-linkbutton whgff-but-submit" iconCls="icon-ok" id="whgwin-edit-btn-save">保存编辑</a>
            <a href="javascript:void(0)" class="easyui-linkbutton whgff-but-submit" iconCls="icon-ok" id="whgwin-edit-btn-submit">提交并送审</a>
        </c:if>
        <c:if test="${type == 'edit'}">
            <a href="javascript:void(0)" class="easyui-linkbutton whgff-but-submit" iconCls="icon-ok" id="whgwin-edit-btn-save">保存编辑</a>
            <a href="javascript:void(0)" class="easyui-linkbutton whgff-but-submit" iconCls="icon-ok" id="whgwin-edit-btn-submit">提交并送审</a>
        </c:if>
        <a href="javascript:void(0)" class="easyui-linkbutton whgff-but-clear" iconCls="icon-undo" onclick="WhgComm.editDialogClose()">返 回</a>
    </div>
<script type="text/javascript">
    $(function () {

        var whgImg1 = WhgUploadImg.init({basePath: '${basePath}', uploadBtnId: 'imgUploadBtn1', hiddenFieldId: 'act_imgurl1', previewImgId: 'previewImg1',needCut:true});

        var whgImg2 = WhgUploadImg.init({basePath: '${basePath}', uploadBtnId: 'imgUploadBtn2', hiddenFieldId: 'act_imgurl2', previewImgId: 'previewImg2',needCut:false});

        var whgImg3 = WhgUploadImg.init({basePath: '${basePath}', uploadBtnId: 'imgUploadBtn3', hiddenFieldId: 'act_imgurl3', previewImgId: 'previewImg3',needCut:false});

        $('#whgff').form({
            novalidate: true,
            url : getFullUrl('/admin/pavilion/do/edit'),
            onSubmit : function(param) {
                var _valid = $(this).form('enableValidation').form('validate');
                if(_valid){
                    //图片不能为空act_imgurl3
                    var picture1 = $("#act_imgurl1").val();

                    if (!picture1){
                        $.messager.alert("错误", '封面图片不能为空！', 'error');
                        $("#whgwin-edit-btn-submit").off('click').one("click",submitFun);
                        $('#whgwin-edit-btn-save').off('click').one('click',saveFun);
                        _valid = false;
                        return _valid;
                    }
                    var act_imgurl3 = $("#act_imgurl3").val();
                    if (!act_imgurl3){
                        $.messager.alert("错误", '内景图片不能为空！', 'error');
                        $("#whgwin-edit-btn-submit").off('click').one("click",submitFun);
                        $('#whgwin-edit-btn-save').off('click').one('click',saveFun);
                        _valid = false;
                        return _valid;
                    }
                }
                if(_valid){
                    $.messager.progress();
                }else{
                    //失败时再注册提交事件
                    $("#whgwin-edit-btn-submit").off('click').one("click",submitFun);
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
                    $("#whgwin-edit-btn-submit").off('click').one("click",submitFun);
                    $('#whgwin-add-btn-save').off('click').one('click', saveFun);
                }
            }
        });

        setBranch();

        function checkImg() {
            $("#act_imgurl1_alert").hide();
            $("#act_imgurl3_alert").hide();
            var act_imgurl1 = $("#act_imgurl1").val();
            var act_imgurl3 = $("#act_imgurl3").val();
            if(null == act_imgurl1 || "" == act_imgurl1){
                $("#act_imgurl1_alert").show();
                return false;
            }
            if(null == act_imgurl3 || "" == act_imgurl3){
                $("#act_imgurl3_alert").show();
                return false;
            }
            return true;
        }

        //提交事件
        function submitFun() {
            $("#editType").val("submit");
            $('#whgff').submit();
        }

        //保存事件
        function saveFun() {

            $("#editType").val("save");
            $('#whgff').submit();
        }

        $("#whgwin-edit-btn-submit").off('click').one("click",submitFun);

        $('#whgwin-edit-btn-save').off('click').one('click', saveFun);
    });

    function setBranch() {
        $.getJSON("${basePath}/admin/branch/branchListUser",function (data) {

            if("1" != data.success){
                $.messager.alert("错误", data.errormsg, 'error');
                return;
            }
            var rows = data.rows;
            $("#branch").combobox("loadData",rows);
            debugger;
            var branchId = "${whBranchRel.branchid}";
            if(0 < rows.length){
                branchId = branchId != ""?branchId:rows[0].id;
                $("#branch").combobox("setValue",branchId);
            }
        });
    }
</script>
</body>
</html>
