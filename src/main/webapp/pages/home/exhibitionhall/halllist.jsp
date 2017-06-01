<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="author" content="">
    <%@include file="/pages/comm/agdhead.jsp"%>
    <title>展馆列表</title>
    <link href="${basePath }/static/assets/css/public/reset.css" rel="stylesheet">
    <link href="${basePath }/static/assets/css/digital/digital.css" rel="stylesheet">
    <script src="${basePath }/static/assets/js/public/jquery-1.11.0.min.js" type="text/javascript"></script>
    <script src="${basePath }/static/assets/js/public/jquery-migrate-1.0.0.js" type="text/javascript"></script>
    <script src="${basePath }/static/assets/js/plugins/roll/jquery.sly.min.js" type="text/javascript"></script>
    <script src="${basePath }/static/assets/js/public/public.js"></script>
    <script src="${basePath }/static/assets/js/digital/digital.js" type="text/javascript"></script>
</head>
<body>
        <!--公共主头部开始-->
        <%@include file="/pages/comm/agdtop.jsp"%>
        <!--公共主头部结束-->

        <!--主体开始-->
        <div class="main-info-bg">
            <div class="container">
                <div class="public-no-message none"></div>
                <ul class="list clearfix" id="hallContent">

                </ul>
            </div>
        </div>

        <!--分页开始 -->
        <div class="green-black" id="artPagging" style="margin-bottom: 40px"></div>
        <!--分页结束-->
        <!--主体结束-->


        <!--公共主底部开始-->
        <%@include file="/pages/comm/agdfooter.jsp"%>
        <!--公共主底部结束-END-->
<script type="text/javascript">

    function setData(data) {
        $("#hallContent").children("li").remove();
        $.each(data.rows,function(index,value){
            var $li = $("<li>");
            var $img = $('<div class="img">');
            $img.append('<img src="${basePath}/whgstatic'+value.hallcover+'" width="280" height="236">');
            $img.append('<span class="title">'+value.hallname+'</span>');
            var $detail = $('<div class="detail">');
            $detail.append('<h2>'+value.hallname+'</h2>');
            $detail.append('<p>'+value.hallsummary+'</p>');
            $detail.append('<i>');
            $detail.appendTo($img);
            $img.append('<a href="${basePath}/exhibitionhall/hallPage?id='+value.id+'" target="_blank"></a>');
            $img.appendTo($li);
            $li.appendTo("#hallContent");
        });
    }

    function loadData(page,rows) {
        page = page || 1;
        rows = rows || 12;
        $.getJSON("${basePath}/exhibitionhall/getHallList?page=" + page + "&rows=" + rows,function(data){
            setData(data);
            renderMyList();
            genPagging('artPagging', page, rows, data.total||0, loadData);
            if(data.total == 0){
                $(".public-no-message").show();
            }else{
                $(".public-no-message").hide();
            }
        });
    }

    $(function(){
        loadData();
    });
</script>
</body>
</html>
