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
            <c:set var="pageTitle" value="资讯编辑"></c:set>
        </c:when>
        <c:when test="${type eq 'add'}">
            <c:set var="pageTitle" value="资讯添加"></c:set>
        </c:when>
        <c:when test="${type eq 'view'}">
            <c:set var="pageTitle" value="资讯查看"></c:set>
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
        <input id="type" type="hidden" name="type" value="${type}">
        <input id="submitType" type="hidden" name="submitType">
        <h2>${pageTitle}</h2>
        <input id="id" name="id" type="hidden" value="${id}">
        <div class="whgff-row">
            <div class="whgff-row-label"><i>*</i>资讯标题：</div>
            <div class="whgff-row-input">
                <input class="easyui-textbox" name="infotitle"
                       style="width: 500px; height: 32px" data-options="required:true,validType:['length[1,60]']" value="${infotitle}"/>
            </div>
        </div>
        <div class="whgff-row">
            <div class="whgff-row-label"><i>*</i>资讯简介：</div>
            <div class="whgff-row-input">
                <input class="easyui-textbox" name="infosummary"
                       style="width: 500px; height: 100px" data-options="required:true,multiline:true,validType:['length[1,512]']" value="${infosummary}"/>
            </div>
        </div>
        <div class="whgff-row">
            <div class="whgff-row-label">
                <label style="color: red">*</label>上传封面：
            </div>
            <div class="whgff-row-input">
                <input type="hidden" id="act_imgurl1" name="infocover" data-options="required:true" value="${infocover}">
                <div class="whgff-row-input-imgview" id="previewImg1"></div>
                <div class="whgff-row-input-imgfile" id="imgUrl_pic">
                    <i><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="imgUploadBtn1">选择图片</a></i>
                    <i>图片格式为jpg、png、gif，建议图片尺寸 750*500，大小为2MB以内</i>
                </div>
            </div>
        </div>
        <div class="whgff-row">
            <div class="whgff-row-label">作者：</div>
            <div class="whgff-row-input">
                <input class="easyui-textbox" name="author"
                       style="width: 500px; height: 32px" data-options="required:false,validType:['length[1,60]']" value="${author}"/>
            </div>
        </div>
        <div class="whgff-row">
            <div class="whgff-row-label">资讯来源：</div>
            <div class="whgff-row-input">
                <input class="easyui-textbox" name="infosource"
                       style="width: 500px; height: 32px" data-options="required:false,validType:['length[1,60]']" value="${infosource}"/>
            </div>
        </div>
        <div class="whgff-row">
            <div class="whgff-row-label"><i>*</i>资讯链接：</div>
            <div class="whgff-row-input" id="divInfolink">
                <input class="easyui-textbox" id="infolink" name="infolink"
                       style="width: 500px; height: 32px" data-options="required:true,validType:['length[1,1000]','url'],prompt:'请输入连接地址,如：http://yoursite.com/'" value="${infolink}"/>
                <a href="javascript:void(0);" class="easyui-linkbutton" id="selectAct">选择活动</a>
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

    </form>
    <div id="whgwin-edit-btn" class="whgff-but" style="width: 400px; margin:20px 0px 50px 350px">
        <c:if test="${type != 'view'}">
            <a href="javascript:void(0)" class="easyui-linkbutton whgff-but-submit" iconCls="icon-ok" id="whgwin-edit-btn-save">保存编辑</a>
            <a href="javascript:void(0)" class="easyui-linkbutton whgff-but-submit" iconCls="icon-ok" id="whgwin-edit-btn-submit">提交并送审</a>
        </c:if>
        <a href="javascript:void(0)" class="easyui-linkbutton whgff-but-clear" iconCls="icon-undo" onclick="WhgComm.editDialogClose()">返 回</a>
    </div>

    <div id="dd" style="display: none">
        <table id="whgdg" class="easyui-datagrid" title="${pageTitle}" style=""
               toolbar="#tb" pagination=true pageSize=30 pageList="[10,20,30,50,100,200]" loadMsg="数据加载中，请稍候。。。"
               data-options="fit:true, striped:true, rownumbers:true, fitColumns:true, singleSelect:false, checkOnSelect:true, selectOnCheck:true,
           url:'${basePath}/admin/reading/getActList'">
            <thead>
            <tr>
                <th data-options="width: 20, field:'selectId',formatter:getRadio">选择</th>
                <th data-options="width:80, sortable:false, field:'name' ">名称</th>
                <th data-options="width:80, sortable: true, field:'starttime', formatter:WhgComm.FMTDate ">开始时间</th>
                <th data-options="width:80, sortable: true, field:'endtime', formatter:WhgComm.FMTDate">结束时间</th>
            </tr>
            </thead>
        </table>
    </div>
<script type="text/javascript">

    function getRadio(value, rowData, rowIndex) {
        return '<input type="radio" name="selectRadio" id="selectRadio"' + rowIndex + '    value="' + rowData.id + '" />';
    }
    
    $("#selectAct").on("click",function () {
        $("#dd").dialog(
            {
                title:'选择活动链接',
                width:600,
                height:600,
                close:false,
                modal:true,
                buttons:[
                    {
                        text:'确认',
                        handler:function () {
                            var selectId = $('input:radio[id="selectRadio"]:checked').val();
                            var url = "${basePath}/agdwhhd/activityinfo?actvid=" + selectId;
                            $("#infolink").textbox("setValue",url);
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

    function setBranch() {
        $.getJSON("${basePath}/admin/branch/branchListUser",function (data) {

            if("1" != data.success){
                $.messager.alert("错误", data.errormsg, 'error');
                return;
            }
            var rows = data.rows;
            $("#branch").combobox("loadData",rows);
//            debugger;
            var branchId = "${whBranchRel.branchid}";
            if(0 < rows.length){
                branchId = branchId != ""?branchId:rows[0].id;
                $("#branch").combobox("setValue",branchId);
            }
        });
    }

    $(function () {
        var whgImg = WhgUploadImg.init({
            basePath: '${basePath}',
            uploadBtnId: 'imgUploadBtn1',
            hiddenFieldId: 'act_imgurl1',
            previewImgId: 'previewImg1',
            needCut:true
        });

        $('#whgff').form({
            novalidate: true,
            url : getFullUrl('/admin/reading/do/edit'),
            onSubmit : function(param) {
                var _valid = $(this).form('enableValidation').form('validate');
                if(_valid){
                    var act_imgurl1 = $("#act_imgurl1").val();
                    if(null == act_imgurl1 || "" == act_imgurl1){
                        $.messager.alert("错误", '封面图片不能为空！', 'error');
                        _valid = false;
                        $('#whgwin-edit-btn-save').off('click').one('click',submitFun);
                        return _valid;
                    }
                    $.messager.progress();
                }else{
                    //失败时再注册提交事件
                    $('#whgwin-edit-btn-save').off('click').one('click',submitFun);
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
                    $('#whgwin-add-btn-save').off('click').one('click', submitFun);
                }
            }
        });

        setBranch();

        //提交事件
        function saveFun() {
            $("#submitType").val(1);
            $('#whgff').submit();
        }

        function submitFun() {
            $("#submitType").val(2);
            $('#whgff').submit();
        }
        
        $('#whgwin-edit-btn-save').on('click', saveFun);
        $('#whgwin-edit-btn-submit').on('click',submitFun);
    });
</script>
</body>
</html>
