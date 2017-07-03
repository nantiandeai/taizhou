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
    <title>编辑重点文物</title>
    <link rel="stylesheet" href="${basePath}/static/admin/css/bootstrap.css"/>
    <link rel="stylesheet" href="${basePath}/static/admin/Font-Awesome/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${basePath}/static/admin/css/build.css"/>

    <script type="text/javascript" charset="utf-8" src="${basePath}/static/ueditor/ueditor.config.js"></script>
    <script type="text/javascript" charset="utf-8" src="${basePath}/static/ueditor/ueditor.all.min.js"></script>
    <script type="text/javascript" charset="utf-8" src="${basePath}/static/ueditor/lang/zh-cn/zh-cn.js"></script>
    <%@include file="/pages/comm/admin/header.jsp"%>
    <script type="text/javascript" src="${basePath }/static/common/js/whg.sys.base.data.js"></script>

    <!-- 图片上传相关 -->
    <script type="text/javascript" src="${basePath}/static/plupload/lib/plupload-2.1.2/js/plupload.full.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/plupload/upload-img.js"></script>
    <script type="text/javascript" src="${basePath}/static/plupload/upload-moreimg.js"></script>
    <script type="text/javascript" src="${basePath}/static/common/js/whg.maps.js"></script>
    <!-- 图片上传相关-END -->
</head>
<body>

<form id="whgff" class="whgff" method="post" action="${basePath}/admin/yunwei/whpp/add">
    <c:choose>
        <c:when test="${not empty targetShow}">
            <h2>查看重点文物</h2>
        </c:when>
        <c:otherwise>
            <h2>编辑重点文物</h2>
        </c:otherwise>
    </c:choose>
    <input type="hidden" name="id" id="id" value="${id}"/>
    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>名称：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="name" value="${cult.name}" style="width:500px; height:32px" data-options="required:true, validType:'length[1,30]'"></div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>重点文物类型：</div>
        <div class="whgff-row-input"><input class="easyui-combobox" name="type" value="${cult.type}" style="height:32px;width: 500px"
                                            data-options="editable:false,required:true, valueField:'id',textField:'text',prompt:'请选择重点文物类型', data:WhgSysData.getTypeData('17')"/></div>
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
        <div class="whgff-row-label"><i>*</i>简介：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="summary" value="${cult.summary}"
                   style="width: 500px; height: 100px" data-options="required:true,multiline:true,validType:['length[1,512]']"/>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">
            <label style="color: red">*</label>地址：
        </div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="address" id="address" value="${cult.address}" style="width: 500px; height: 32px" data-options="required:true,validType:['length[0,60]']">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">
            <label style="color: red">*</label>坐标：
        </div>
        <div class="whgff-row-input">
            <input class="easyui-numberbox" name="actlon" id="actlon" value="${cult.actlon}" style="width: 150px; height: 32px" data-options="required:true, precision:6,readonly:true,prompt:'X轴'">
            <input class="easyui-numberbox" name="actlat" id="actlat" value="${cult.actlat}" style="width: 150px; height: 32px" data-options="required:true, precision:6,readonly:true,prompt:'Y轴'">
            <a class="easyui-linkbutton whg-maps" map-addr="#address" vm-x="actlon" vm-y="actlat" id="getXYPointBtn" text="选择坐标"></a>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>上传封面：</div>
        <div class="whgff-row-input">
            <input type="hidden" id="cult_picture1" name="picture" value="${cult.picture}">
            <div class="whgff-row-input-imgview" id="previewImg1"></div>
            <div class="whgff-row-input-imgfile">
                <i><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="imgUploadBtn1">选择图片</a></i>
                <i>图片格式为jpg、png、gif，建议图片尺寸 750*500，大小为2MB以内</i>
            </div>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>上传展示图片：</div>
        <div class="whgff-row-input">
            <input type="hidden" id="whg_img_upload" name="showpicture" value="${cult.showpicture}">
            <div class="whgff-row-input-fileview" id="whg_img_pload_view"></div>
            <div class="whgff-row-input-filefile">
                <i><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="fileUploadBtn2">选择图片</a></i>
                <i style="color: #999;font-size: 12px;font-style: normal;">图片格式为jpg、png、gif，大小为2MB以内,最多可上传五张图片</i>
            </div>
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>重点文物描述：</div>
        <div class="whgff-row-input">
            <script id="catalog" type="text/plain" style="width:700px; height:250px;"></script>
            </div>
    </div>
</form>

<div id="whgwin-add-btn" class="whgff-but" style="width: 400px; margin:20px 0px 50px 350px">
    <div style="display: inline-block; margin: 0 auto">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="whgwin-add-btn-save">保 存</a>
<a href="javascript:void(0)" class="easyui-linkbutton whgff-but-clear" iconCls="icon-undo" onclick="WhgComm.editDialogClose()">返 回</a>
</div>
</div>

<!-- script -->
<script type="text/javascript">
    var ueConfig = {
        scaleEnabled: false,
        autoFloatEnabled: false,
        readonly: '${targetShow}'? true: false//富文本编辑器设为只读
    };
    var ue_catalog = UE.getEditor('catalog', ueConfig);

    ue_catalog.ready(function () {
        ue_catalog.setContent('${cult.introduction}')
    });
    $(function () {
        //根据地址取坐标
        WhgMap.init({basePath:'${basePath}', addrFieldId:'address', xpointFieldId:'actlon', ypointFieldId:'actlat', getPointBtnId:'getXYPointBtn'});

        WhgUploadImg.init({basePath: '${basePath}', uploadBtnId: 'imgUploadBtn1', hiddenFieldId: 'cult_picture1', previewImgId: 'previewImg1'});

        WhgUploadMoreImg.init({basePath: '${basePath}', uploadBtnId: 'fileUploadBtn2', hiddenFieldId: 'whg_img_upload',previewImgId:'whg_img_pload_view',needCut:false});

        $('#whgff').form({
            novalidate: true,
            url: "${basePath}/admin/historical/edit",
            onSubmit : function(param) {
                var _valid = $(this).form('enableValidation').form('validate')
                if(_valid) {
                    //图片必填
                    if($('#cult_picture1').val() == ""){
                        _valid = false;
                        $.messager.alert('提示', '请选择封面图片');
                    }else if($('#whg_img_upload').val() == ""){
                        _valid = false;
                        $.messager.alert('提示', '请上传展示图片图片');
                    }else if(!isUEvalid) {
                        var isUEvalid = validateUE();
                        if (isUEvalid) {
                            param.introduction = ue_catalog.getContent();
                            $.messager.progress();
                        } else {
                            _valid = false;
                        }
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
                    $.messager.alert('提示', '操作失败:'+Json.errormsg+'!', 'error');
                    $('#whgwin-add-btn-save').off('click').one('click', submitFun);
                }
            }
        });
        //注册提交事件
        $('#whgwin-add-btn-save').off('click').one('click', function () { $('#whgff').submit(); });
    });

    function validateUE(){
        if (!ue_catalog.hasContents()) {
            $.messager.alert("错误", '重点文物描述不能为空', 'error');
            return false;
        }
        return true;
    }

    //查看时的处理
    $(function () {
        setBranch();
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

    function setBranch() {
        $.getJSON("${basePath}/admin/branch/branchListUser",function (data) {

            if("1" != data.success){
                $.messager.alert("错误", data.errormsg, 'error');
                return;
            }
            var rows = data.rows;
            $("#branch").combobox("loadData",rows);
//                debugger;
            var branchId = "${whBranchRel.branchid}";
            if(0 < rows.length){
                branchId = branchId != ""?branchId:rows[0].id;
                $("#branch").combobox("setValue",branchId);
            }
        });
    }

    </script>
<!-- script END -->
</body>
</html>
