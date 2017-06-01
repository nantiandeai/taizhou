<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<%@include file="/pages/comm/agdhead.jsp"%>
<title>台州文化云-活动报名</title>
<link href="${basePath }/static/assets/css/activity/activityDetaiil.css" rel="stylesheet">
<link href="${basePath }/static/assets/js/plugins/oiplayer-master/css/oiplayer.css" rel="stylesheet">
<link href="${basePath }/static/assets/css/activity/baoming.css" rel="stylesheet">

<script src="${basePath }/static/assets/js/plugins/sidebar/stickySidebar.js"></script>
<script src="${basePath }/static/assets/js/activity/activityDetail.js"></script>
<script src="${basePath }/static/assets/js/plugins/oiplayer-master/plugins/flowplayer-3.2.6.min.js"></script>
<script src="${basePath }/static/assets/js/plugins/oiplayer-master/js/jquery.oiplayer.js"></script>
<script src="${basePath }/static/assets/js/activity/seatOrder.js"></script>
<script src="${basePath }/static/assets/js/plugins/laydate.dev.min.js"></script>
</head>
<body class="oiplayer-example">
<!-- 公共头部开始 -->
<%@include file="/pages/comm/agdtop.jsp"%>
<!-- 公共头部结束-END -->


<!--主体开始-->
<div class="main-info-bg bg-color">
   <ul class="crumbs crumbs-5 clearfix">
        <li class="step-1">1. 填写基本信息<em class="arrow"></em></li>
        <li class="step-2">2. 选择座位<em class="arrow"></em></li>
        <li class="step-3">3. 填写取票信息<em class="arrow"></em></li>
        <li class="step-4">4. 确认订单信息<em class="arrow"></em></li>
        <li class="step-5 last">5. 完成报名<em class="arrow"></em></li>
    </ul>
    <input type="hidden" value="${order.actId }" name="actId" id="actId">
    <div class="container-wrapper">
        <div class="order-msg">
            <span class="msg-title">完成报名</span>
            <a href="${basePath}/center/activity" class="return">查看活动预定</a>
        </div>
        <div class="order-content clearfix">
            <div class="complete-order">
                <img src="${basePath}/static/assets/img/activity/complete-order.png" width="61" height="61">
            </div>
            <div class="compltet-order-msg">
                <p>恭喜您，${order.name }</p>
                <span>已报名成功 ！</span>
            </div>
        </div>
        <a href="${basePath}/agdwhhd/activityinfo?actvid=${order.actId}" class="submit">返回活动详情</a>
    </div>
</div>
<!--主体结束-->

<!--公共主底部开始-->
<%@include file="/pages/comm/agdfooter.jsp"%>
<!--公共主底部结束-END-->
</body>
</html>