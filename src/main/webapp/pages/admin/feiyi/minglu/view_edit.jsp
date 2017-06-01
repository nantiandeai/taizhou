<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<% request.setAttribute("basePath", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath());%>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>名录项目管理-编辑</title>
    <link rel="stylesheet" href="${basePath}/static/admin/css/bootstrap.css"/>
    <link rel="stylesheet" href="${basePath}/static/admin/Font-Awesome/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${basePath}/static/admin/css/build.css"/>
    <!-- 富文本相关 -->
    <script type="text/javascript" charset="utf-8" src="${basePath}/static/ueditor/ueditor.config.js"></script>
    <script type="text/javascript" charset="utf-8" src="${basePath}/static/ueditor/ueditor.all.min.js"></script>
    <script type="text/javascript" charset="utf-8" src="${basePath}/static/ueditor/lang/zh-cn/zh-cn.js"></script>
    <!-- 富文本相关-END -->
    <%@include file="/pages/comm/admin/header.jsp"%>
    <script type="text/javascript" src="${basePath }/static/admin/js/common_img.js"></script>
    <!-- 图片上传相关 -->
    <script type="text/javascript" src="${basePath}/static/plupload/lib/plupload-2.1.2/js/plupload.full.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/plupload/upload-img.js"></script>
    <!-- 图片上传相关-END -->
</head>
<body>
<h2>编辑名录</h2>
<form id="whgff" class="whgff" method="post" enctype="multipart/form-data" >
    <input type="hidden" name="mlproid" value="${mlproid}">
    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>名录项目类型：</div>
        <div class="whgff-row-input"><input class="easyui-combobox " name="mlprotype" value="${ml.mlprotype}"
                                            style="height:35px;width: 600px;"
                                            data-options="editable:false, valueField:'id',textField:'text',prompt:'名录项目类型',data:WhgSysData.getTypeData('8'),required:true"/></div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>名录项目批次：</div>
        <div class="whgff-row-input"><input class="easyui-combobox" name="mlproitem" style="height:35px;width: 600px" value="${ml.mlproitem}"
                                            data-options="editable:false,required:true, valueField:'id',textField:'text',prompt:'名录项目批次', data:WhgSysData.getTypeData('9')"/></div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>名录项目级别：</div>
        <div class="whgff-row-input"><input class="easyui-combobox" name="mlprolevel" style="height:35px;width: 600px" value="${ml.mlprolevel}"
                                            data-options="editable:false,required:true, valueField:'id',textField:'text', prompt:'名录项目级别',data:WhgSysData.getTypeData('10')"/></div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>名录项目区域：</div>
        <div class="whgff-row-input"><input class="easyui-combobox" name="mlproqy" style="height:35px;width: 600px" value="${ml.mlproqy}"
                                            data-options="editable:false,required:true, valueField:'id',textField:'text',prompt:'名录项目区域', data:WhgSysData.getTypeData('6')"/></div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>名录项目列表标题：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="mlproshortitel" value="${ml.mlproshortitel}" style="width:600px; height:32px" data-options="required:true, validType:'length[0,60]'"></div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>名录项目详情标题：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="mlprotailtitle" value="${ml.mlprotailtitle}" style="width:600px; height:32px" data-options="required:true, validType:'length[0,60]'"></div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>申报区域：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="mlprosbaddr" value="${ml.mlprosbaddr}" style="width:600px; height:32px" data-options="required:true, validType:'length[0,200]'"></div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>资源来源：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="mlprosource" value="${ml.mlprosource}" style="width:600px; height:32px" data-options="required:true, validType:'length[0,200]'"></div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red"></label>关键字：</div>
        <div class="whgff-row-input"><input class="easyui-combobox" name="mlprokey" value="${ml.mlprokey}" multiple="true" style="width:600px;height:32px;" data-options="required:true,missingMessage:'请用英文逗号分隔', panelHeight:'auto',editable:true,valueField:'text',textField:'text',data: WhgComm.getZxKey(), multiple:true"/>
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>名录列表图：</div>
        <div class="whgff-row-input">
            <input type="hidden" id="cult_picture1" name="mlprosmpic" value="${ml.mlprosmpic}">
            <div class="whgff-row-input-imgview" id="previewImg1"></div>
            <div class="whgff-row-input-imgfile">
                <i><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="imgUploadBtn1">选择图片</a></i>
                <i>图片格式为jpg、png、gif，建议图片尺寸 750*500，大小为2MB以内</i>
            </div>
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><i></i>名录详情图：</div>
        <div class="whgff-row-input">
            <input type="hidden" id="cult_picture2" name="mlprobigpic" value="${ml.mlprobigpic}">
            <div class="whgff-row-input-imgview" id="previewImg2"></div>
            <div class="whgff-row-input-imgfile">
                <i><a href="#" class="easyui-linkbutton" iconCls="icon-save" id="imgUploadBtn2">选择图片</a></i>
                <i>图片格式为jpg、png、gif，建议图片尺寸 1920*500，大小为2MB以内</i>
            </div>
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>名录项目详情：</div>
        <div class="whgff-row-input">
            <script id="catalog" type="text/plain" style="width:600px; height:200px;"></script>
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
    /**
     * 处理富文本框
     */
    var ueConfig = {
        scaleEnabled: false,
        autoFloatEnabled: false,
        readonly: '${targetShow}'? true: false//富文本编辑器设为只读
    };
    var ue_catalog = UE.getEditor('catalog', ueConfig);
    ue_catalog.ready(function () {
        ue_catalog.setContent('${ml.mlprodetail}')
    });

    //查看时的处理
    $(function () {
        var targetShow = '${targetShow}';
        if (targetShow){
            //取消表单验证
            $("#whgff").form("disableValidation");
            //组件设为只读
            $('.easyui-textbox').textbox('readonly');
            $('.easyui-combobox').combobox('readonly');
            //不显示提交 button
            $('#whgwin-add-btn-save').hide();
        }
    });
    /**
     * 名录添加表单js
     */
    $(function () {
        WhgUploadImg.init({basePath: '${basePath}', uploadBtnId: 'imgUploadBtn1', hiddenFieldId: 'cult_picture1', previewImgId: 'previewImg1', needCut:true,cutWidth:380, cutHeight:240 });
        WhgUploadImg.init({basePath: '${basePath}', uploadBtnId: 'imgUploadBtn2', hiddenFieldId: 'cult_picture2', previewImgId: 'previewImg2', needCut:false});

        $('#whgff').form({
            novalidate: true,
            url: "${basePath}/admin/feiyi/addOrEditminglu",
            onSubmit : function(param) {
                var _valid = $(this).form('enableValidation').form('validate')
                if(_valid) {
                    var isUEvalid = validateUE();
                    if (isUEvalid) {
                        param.mlprodetail = ue_catalog.getContent();
                        $.messager.progress();
                    } else {
                        _valid = false;
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

    //富文本验证
    function validateUE(){
        if (!ue_catalog.hasContents()) {
            $.messager.alert("错误", '名录项目详情不能为空', 'error');
            return false;
        }
        return true;
    }

</script>
<!-- script END -->
</body>
</html>
