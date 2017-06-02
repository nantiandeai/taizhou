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
            <c:set var="pageTitle" value="藏品编辑"></c:set>
        </c:when>
        <c:when test="${type eq 'add'}">
            <c:set var="pageTitle" value="藏品添加"></c:set>
        </c:when>
        <c:when test="${type eq 'view'}">
            <c:set var="pageTitle" value="藏品查看"></c:set>
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
            <div class="whgff-row-label"><i>*</i>藏品名称：</div>
            <div class="whgff-row-input">
                <input class="easyui-textbox" name="exhname"
                       style="width: 500px; height: 32px" data-options="required:true,validType:['length[1,60]']" value="${exhname}"/>
            </div>
        </div>
        <div class="whgff-row">
            <div class="whgff-row-label"><i>*</i>藏品简介：</div>
            <div class="whgff-row-input">
                <input class="easyui-textbox" name="exhsummary"
                       style="width: 500px; height: 80px" data-options="multiline:true,required:true,validType:['length[1,512]']" value="${exhsummary}"/>
            </div>
        </div>

        <div class="whgff-row">
            <div class="whgff-row-label">
                <label style="color: red"><i>*</i></label>藏品类别：
            </div>
            <div class="whgff-row-input">
                <div class="radio radio-primary whg-js-data"  name="exhtype" value="${exhtype}"  js-data="getExhType"></div>
            </div>

        </div>

        <div class="whgff-row">
            <div class="whgff-row-label">
                <label style="color: red">*</label>上传藏品照片：
            </div>
            <div class="whgff-row-input">
                <input type="hidden" id="act_imgurl1" name="exhphoto" data-options="required:true" value="${exhphoto}">
                <div class="whgff-row-input-imgview" id="previewImg1"></div>
                <div class="whgff-row-input-imgfile" id="imgUrl_pic1">
                    <i><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="imgUploadBtn1">选择图片</a></i>
                    <i>图片格式为jpg、png、gif，建议图片尺寸 750*500，大小为2MB以内</i>
                </div>
            </div>
        </div>

        <div class="whgff-row">
            <div class="whgff-row-label"><label style="color: red">*</label>所属展馆：</div>
            <div class="whgff-row-input">
                <input class="easyui-textbox" id="hallname" name="hallname"
                       style="width: 500px; height: 32px" data-options="required:true,validType:['length[1,512]']" value="${hallname}" readonly="true"/>
                <input type="hidden" id="hallid" name="hallid" value="${hallid}">
                <a href="javascript:void(0);" class="easyui-linkbutton" id="selectHall">选择展馆</a>
            </div>
        </div>

    </form>
    <div id="whgwin-edit-btn" class="whgff-but" style="width: 400px; margin:20px 0px 50px 350px">
        <c:if test="${type  != 'view'}">
            <a href="javascript:void(0)" class="easyui-linkbutton whgff-but-submit" iconCls="icon-ok" id="whgwin-edit-btn-save">保存编辑</a>
            <a href="javascript:void(0)" class="easyui-linkbutton whgff-but-submit" iconCls="icon-ok" id="whgwin-edit-btn-submit">提交并送审</a>
        </c:if>
        <a href="javascript:void(0)" class="easyui-linkbutton whgff-but-clear" iconCls="icon-undo" onclick="WhgComm.editDialogClose()">返 回</a>
    </div>

    <div id="dd" style="display: none">
        <table id="whgdg" class="easyui-datagrid" title="${pageTitle}" style=""
               toolbar="#tb" pagination=true pageSize=30 pageList="[10,20,30,50,100,200]" loadMsg="数据加载中，请稍候。。。"
               data-options="fit:true, striped:true, rownumbers:true, fitColumns:true, singleSelect:false, checkOnSelect:true, selectOnCheck:true,
           url:'${basePath}/admin/antiques/getPavilionList'">
            <thead>
            <tr>
                <th data-options="width: 40, field:'selectId',formatter:getRadio">选择</th>
                <th data-options="width:150, sortable:false, field:'hallname' ">展馆名称</th>
                <th data-options="width:150, sortable: true, field:'halladdress'">展馆地址</th>
                <th data-options="width:80, sortable: true, field:'hallcover',formatter:showImg">展馆照片</th>
            </tr>
            </thead>
        </table>
    </div>

<script type="text/javascript">

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

    var whgImg1 = WhgUploadImg.init({basePath: '${basePath}', uploadBtnId: 'imgUploadBtn1', hiddenFieldId: 'act_imgurl1', previewImgId: 'previewImg1',needCut:false});

    function getRadio(value, rowData, rowIndex) {
        return '<input type="radio" name="selectRadio" id="selectRadio"' + rowIndex + '    value="' + rowData.id + '"  selectName="'+rowData.hallname+'"/>';
    }

    function showImg(v, r, i){
        var img = r.hallcover || "";
        if (!img) return "";
        return imgFMT(img, r, i);
    }

    $("#selectHall").on("click",function () {
        $("#dd").dialog(
            {
                title:'选择展馆',
                width:1000,
                height:800,
                close:false,
                modal:true,
                buttons:[
                    {
                        text:'确认',
                        handler:function () {
                            var selectId = $('input:radio[id="selectRadio"]:checked').val();
                            var selectName = $('input:radio[id="selectRadio"]:checked').attr("selectName");
                            $("#hallname").textbox("setValue",selectName);
                            $("#hallid").val(selectId);
                            $("#dd").dialog('close');
                        }
                    },
                    {
                        text:'取消',
                        handler:function () {
                            $("#dd").dialog('close');
                        }
                    }
                ]
            }
        );
    });

    function getExhType() {
//        debugger;
        var whgYwiKeyList = eval('${whgYwiKeyList}');
        var data = [];
        if(null != whgYwiKeyList){
            $.each(whgYwiKeyList,function (index,value) {
                var item = {};
                item.id = value.id;
                item.text = value.name;
                data.push(item);
            });
            return data;
        }
        return null;
    }


    $(function () {

        $('#whgff').form({
            novalidate: true,
            url : getFullUrl('/admin/antiques/do/edit'),
            onSubmit : function(param) {
                var _valid = $(this).form('enableValidation').form('validate');
                if(_valid){
                    //图片不能为空act_imgurl3
                    var picture1 = $("#act_imgurl1").val();

                    if (!picture1){
                        $.messager.alert("错误", '藏品照片不能为空！', 'error');
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




        $("#whgwin-edit-btn-submit").off('click').one("click",submitFun);

        $('#whgwin-edit-btn-save').off('click').one('click', saveFun);
    });
</script>
</body>
</html>
