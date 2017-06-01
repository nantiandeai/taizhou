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
    <link href="${basePath }/static/assets/css/public/reset.css" rel="stylesheet">
    <link href="${basePath }/static/assets/css/digital/digital.css" rel="stylesheet">
    <script src="${basePath }/static/assets/js/public/jquery-1.11.0.min.js" type="text/javascript"></script>
    <script src="${basePath }/static/assets/js/public/jquery-migrate-1.0.0.js" type="text/javascript"></script>
    <script src="${basePath }/static/assets/js/plugins/roll/jquery.sly.min.js" type="text/javascript"></script>
    <script src="${basePath }/static/assets/js/public/public.js"></script>
    <script src="${basePath }/static/assets/js/digital/digital.js" type="text/javascript"></script>
    <title>数字展馆</title>
</head>
<body>
    <!--公共主头部开始-->
    <%@include file="/pages/comm/agdtop.jsp"%>
    <!--公共主头部结束-->

    <!--主体开始-->
    <div class="main-info-bg">
        <div class="container">
            <div class="index-banner-content">
                <ul class="banner">
                    <c:forEach items="${photo360Link}" var="item">
                        <li class="index-banner" style="background:url(${basePath}/whgstatic${item.picture})">
                            <a href="${item.url}" target="_blank"></a>
                        </li>
                    </c:forEach>
                </ul>
                <div class="page">
                    <ul class="pages"></ul>
                </div>
            </div>
            <div class="venue-list clearfix">
                <div class="lists">
                    <ul class="clearfix">
                        <c:forEach items="${exhibitionHallList}" var="item">
                            <li>
                                <div class="img">
                                    <img src="${basePath}/whgstatic${item.hallcover}" width="260" height="220">
                                    <span class="title" style="display: block; opacity: 1;">${item.hallname}</span>
                                    <div class="detail" style="display: none; opacity: 1;">
                                        <h2>${item.hallname}</h2>
                                        <p>${item.hallsummary}</p>
                                        <i></i>
                                    </div>
                                    <a href="${basePath}/exhibitionhall/hallPage?id=${item.id}" target="_blank"></a>
                                </div>
                            </li>
                        </c:forEach>
                    </ul>
                    <div class="arrow">
                        <div class="arrows left-arrow">
                            <i class="iconfont icon-back"></i>
                        </div>
                        <div class="arrows right-arrow">
                            <i class="iconfont icon-more"></i>
                        </div>
                    </div>
                </div>
                <div class="lookmore clearfix">
                    <dit class="title">
                        <h2>CULTURAL<br>
                            VENUES</h2>
                        <p>文化展馆</p>
                    </dit>
                    <a href="${basePath}/exhibitionhall/list">查看更多</a>
                </div>
            </div>
        </div>
    </div>
    <!--主体结束-->


    <!--公共主底部开始-->
    <%@include file="/pages/comm/agdfooter.jsp"%>
    <!--公共主底部结束-END-->
<script type="text/javascript">
    $(function(){
        renderMyList();
    });
</script>
</body>
</html>
