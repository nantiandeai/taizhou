<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="keywords" content="">
    <meta name="author" content="">
    <%@include file="/pages/comm/agdhead.jsp"%>
    <link href="${basePath }/static/assets/css/public/reset.css" rel="stylesheet">
    <link href="${basePath }/static/assets/css/digital/digital.css" rel="stylesheet">
    <link href="${basePath }/static/viewer/viewer.min.css" rel="stylesheet">
    <script src="${basePath }/static/assets/js/public/jquery-1.11.0.min.js" type="text/javascript"></script>
    <script src="${basePath }/static/assets/js/public/jquery-migrate-1.0.0.js" type="text/javascript"></script>
    <script src="${basePath }/static/assets/js/plugins/roll/jquery.sly.min.js" type="text/javascript"></script>
    <script src="${basePath }/static/assets/js/plugins/zoomin/zoomin.js"></script>
    <script src="${basePath }/static/assets/js/public/rong-dialog.js" type="text/javascript"></script>
    <script src="${basePath }/static/assets/js/public/public.js"></script>
    <script src="${basePath }/static/assets/js/digital/digital.js" type="text/javascript"></script>
    <script src="${basePath }/static/viewer/viewer.min.js" type="text/javascript"></script>
    <title>${myHall.hallname}</title>
</head>
<body>
    <!--公共主头部开始-->
    <%@include file="/pages/comm/agdtop.jsp"%>
    <!--公共主头部结束-->

    <!--主体开始-->
    <div class="main-info-bg">
        <div class="container">
            <div class="banner">
                <img src="${basePath}/whgstatic${myHall.hallInterior360}" width="1200" height="460">
                <span>${myHall.hallname}</span>
            </div>
            <div class="public-no-message none"></div>
                <ul class="detail clearfix" id="cpContent">
                    <li>
                        <a href="#" onclick="show_img(this,{url:'${basePath }/static/assets/img/img_demo/demo.jpg',zoomin:'ture',title:'我是标题03'})"></a>
                        <div class="img">
                            <img src="${basePath }/static/assets/img/img_demo/feiyiminglu1.jpg" width="280" height="236">
                            <div class="detail" >
                                <h2>台州文化馆台州文化馆</h2>
                                <p>简介：<span>台州文化馆台州文化馆台州文化馆台州文化馆台州文化馆台州文化馆台州文化馆台州文化馆</span></p>
                                <i></i>
                            </div>
                            <a href="#"></a>
                        </div>
                    </li>
                    <li>
                        <a href="#" onclick="show_img(this,{url:'${basePath }/static/assets/img/img_demo/demo2.jpg',zoomin:'ture',title:'我是标题03'})"></a>
                        <div class="img">
                            <img src="${basePath }/static/assets/img/img_demo/feiyiminglu1.jpg" width="280" height="236">
                            <div class="detail">
                                <h2>台州文化馆台州文化馆</h2>
                                <p>简介：<span>台州文化馆台州文化馆台州文化馆台州文化馆台州文化馆台州文化馆台州文化馆台州文化馆</span></p>
                                <i></i>
                                <div class="mask"></div>
                            </div>

                        </div>
                    </li>
                    <li>
                        <a href="#" onclick="show_img(this,{url:'${basePath }/static/assets/img/img_demo/demo3.jpg',zoomin:'ture',title:'我是标题03'})"></a>
                        <div class="img">
                            <img src="${basePath }/static/assets/img/img_demo/feiyiminglu1.jpg" width="280" height="236">
                            <div class="detail">
                                <h2>台州文化馆台州文化馆</h2>
                                <p>简介：<span>台州文化馆台州文化馆台州文化馆台州文化馆台州文化馆台州文化馆台州文化馆台州文化馆</span></p>
                                <i></i>
                                <div class="mask"></div>
                            </div>
                        </div>
                    </li>
                    <li>
                        <a href="#" onclick="show_img(this,{url:'${basePath }/static/assets/img/img_demo/demo4.jpg',zoomin:'ture',title:'我是标题03'})"></a>
                        <div class="img">
                            <img src="${basePath }/static/assets/img/img_demo/feiyiminglu1.jpg" width="280" height="236">
                            <div class="detail">
                                <h2>台州文化馆台州文化馆</h2>
                                <p>简介：<span>台州文化馆台州文化馆台州文化馆台州文化馆台州文化馆台州文化馆台州文化馆台州文化馆</span></p>
                                <i></i>
                                <div class="mask"></div>
                            </div>
                        </div>
                    </li>
                </ul>
        </div>

        <!--分页开始 -->
        <div class="green-black" id="artPagging" style="margin-bottom: 40px"></div>
        <!--分页结束-->

    </div>
    <!--主体结束-->


    <!--公共主底部开始-->
    <%@include file="/pages/comm/agdfooter.jsp"%>
    <!--公共主底部结束-END-->
    <script type="text/javascript">
        function loadData(page,rows) {
            page = page || 1;
            rows = rows || 12;
            $.getJSON("${basePath}/exhibitionhall/getExhibitList?id=${myHall.id}&page="+page+"&rows="+rows,function (data) {
                setData(data);
                renderMyList();
                setViewer();
                genPagging('artPagging', page, rows, data.total||0, loadData);
                if(data.total == 0){
                    $(".public-no-message").show();
                }else{
                    $(".public-no-message").hide();
                }
            });
        }

        function setViewer() {
            $('#cpContent').viewer();
        }

        function setData(data) {
            $("#cpContent").children("li").remove();
            $.each(data.rows,function (index,value) {
                var $li = $("<li>");
                //$li.append('<a href="javascript:void(0);" onclick=""></a>');
                var $img = $('<div class="img">');
                $img.append('<img src="${basePath }/whgstatic'+value.exhphoto+'" width="280" height="236">');
                $img.append('<div class="detail" ><h2>'+value.exhname+'</h2><p>简介：<span>'+value.exhsummary+'</span></p><i></i></div>');
                //$img.append('<a href="javascript:void(0);"></a>');
                $img.appendTo($li);
                $li.appendTo("#cpContent");
            });
        }

        $(function(){
            loadData();
        });

    </script>
</body>
</html>
