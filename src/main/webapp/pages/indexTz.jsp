<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="keywords" content="">
    <meta name="author" content="">
    <link href="${basePath }/static/assets/css/public/reset.css" rel="stylesheet">
    <link href="${basePath }/static/assets/css/index/index.css" rel="stylesheet">
    <style type="text/css">
        #header {
            overflow: hidden;
            height: 199px;
            position: absolute;
            left: 0px;
            top: 0px;
            width: 100%;
        }
    </style>
    <script src="${basePath }/static/assets/js/public/jquery-1.11.0.min.js" type="text/javascript"></script>
    <script src="${basePath }/static/assets/js/public/jquery-migrate-1.0.0.js" type="text/javascript"></script>
    <script src="${basePath }/static/assets/js/plugins/roll/jquery.sly.min.js" type="text/javascript"></script>
    <script src="${basePath }/static/assets/js/public/public.js"></script>
    <script src="${basePath }/static/assets/js/index/index.js" type="text/javascript"></script>
    <meta charset="utf-8">
    <%@include file="/pages/comm/agdhead.jsp"%>
    <title>台州文化云</title>
</head>
<body>
    <!--公共主头部开始-->
    <%@include file="/pages/comm/agdtop.jsp"%>
    <!--轮播广告开始-->
    <div class="banner-position">
        <div class="big-banner">
            <div class="banner-cont">
                <ul class="forceCentered">
                    <c:choose>
                        <c:when test="${lbtList != null && fn:length(lbtList) > 0}">
                            <c:forEach items="${lbtList}" var="row" varStatus="s">
                                <li style="background:url(${basePath}/whgstatic${row.picture})">
                                    <a href="${row.url != null && fn:startsWith(row.url, 'http') ? row.url : 'javascript:void(0)'}" title="${row.name}"></a>
                                </li>
                            </c:forEach>
                        </c:when>
                    </c:choose>
                </ul>
            </div>
        </div>
        <div class="page">
            <ul class="pages">
            </ul>
        </div>
    </div>
    <!--轮播广告结束-->
    <!--文化活动开始-->
    <div class="public-index-bg margin-bo0px row-1-bg">
        <div class="public-index-cont">
            <h2>
                <img src="${basePath}/static/assets/img/index/wenhuahuodongbiaoti.png">
            </h2>
            <a href="${basePath}/agdwhhd/index">更多&nbsp;></a>
        </div>
        <div class="public-index-cont">
            <div class="activity-list">
                <ul class="clearfix" id="activityContent">

                </ul>
            </div>
        </div>
    </div>
    <!--文化活动结束-->
    <!--文化场馆开始-->
    <div class="public-index-bg margin-bo0px">
        <div class="public-index-cont">
            <h2>
                <img src="${basePath}/static/assets/img/index/wenhuachangguan.png">
            </h2>
            <a href="${basePath}/agdcgfw/index">更多&nbsp;></a>
        </div>
        <div class="venue-content">
            <div class="venue-list">
                <ul class="clearfix" id="venContent">
                    <li>
                        <div class="content">
                            <div class="img">
                                <img src="${basePath}/static/assets/img/img_demo/seBig8.jpg">
                            </div>
                            <div class="desc">
                                <h2>台州文化馆</h2>
                                <p>台州文化馆详情台州文化馆详情台州文化馆详情台州文化馆详情台州文化馆详情台州文化馆详情台州文化馆详情台州文化馆详情台州文化馆详情</p>
                            </div>
                        </div>
                        <a href="javascript:void(0)"></a>
                    </li>
                    <li class="active">
                        <div class="content">
                            <div class="img">
                                <img src="${basePath}/static/assets/img/img_demo/seBig8.jpg">
                            </div>
                            <div class="desc">
                                <h2>台州文化馆</h2>
                                <p>台州文化馆详情台州文化馆详情台州文化馆详情台州文化馆详情台州文化馆详情台州文化馆详情台州文化馆详情台州文化馆详情台州文化馆详情</p>
                            </div>
                        </div>
                        <a href="javascript:void(0)"></a>
                    </li>
                    <li>
                        <div class="content">
                            <div class="img">
                                <img src="${basePath}/static/assets/img/img_demo/seBig8.jpg">
                            </div>
                            <div class="desc">
                                <h2>台州文化馆</h2>
                                <p>台州文化馆详情台州文化馆详情台州文化馆详情台州文化馆详情台州文化馆详情台州文化馆详情台州文化馆详情台州文化馆详情台州文化馆详情</p>
                            </div>
                        </div>
                        <a href="javascript:void(0)"></a>
                    </li>
                </ul>
            </div>
        </div>
    </div>
    <!--文化场馆结束-->
    <!--热门资源开始-->
    <div class="public-index-bg margin-bo0px">
        <div class="public-index-cont">
            <h2>
                <img src="${basePath}/static/assets/img/index/remenziyuan.png">
            </h2>
            <a href="${basePath}/pop/index">更多&nbsp;></a>
        </div>
        <div class="public-index-cont clearfix" id="popContent">
            <div class="left-wrapper">
                <div class="img">
                    <img src="${basePath}/static/assets/img/img_demo/seBig7.jpg" width="500" height="420">
                </div>
                <div class="title">这是一个美女</div>
                <a href="#"></a>
            </div>
            <div class="right-wrapper">
                <ul class="clearfix">
                    <li>
                        <div class="img">
                            <img src="${basePath}/static/assets/img/img_demo/seBig7.jpg" width="316" height="193">
                        </div>
                        <div class="title">这是一个美女</div>
                        <a href="#"></a>
                    </li>
                    <li>
                        <div class="img">
                            <img src="${basePath}/static/assets/img/img_demo/seBig7.jpg" width="316" height="193">
                        </div>
                        <div class="title">这是一个美女</div>
                        <a href="#"></a>
                    </li>
                    <li>
                        <div class="img">
                            <img src="${basePath}/static/assets/img/img_demo/seBig7.jpg" width="316" height="193">
                        </div>
                        <div class="title">这是一个美女</div>
                        <a href="#"></a>
                    </li>
                    <li>
                        <div class="img">
                            <img src="${basePath}/static/assets/img/img_demo/seBig7.jpg" width="316" height="193">
                        </div>
                        <div class="title">这是一个美女</div>
                        <a href="#"></a>
                    </li>
                </ul>
            </div>
        </div>
    </div>
    <!--热门资源结束-->
    <!--文化新闻开始-->
    <div class="public-index-bg">
        <div class="public-index-cont" style="display: none">
            <h2>
                <img src="${basePath}/static/assets/img/index/wenhuaxinwen.png">
            </h2>
           <!--  <a href="#">更多&nbsp;></a> -->
        </div>
        <div class="public-index-cont" style="display: none">
            <div class="culture-news clearfix">
                <div class="calendars">
                    <span class="years">2017</span>
                    <span class="months">04/11</span>
                </div>
                <div class="calendats-news">
                    <h2><i></i><a href="#">这是一个标题</a></h2>
                    <p>这是一段详情详情详情详情详情详情详情详情详情详情详情详情详情详情详情详情详情详情详情详情详情详情详情详情详情详情详情详情详情详情详情详情详情详情详情详情详情详情详情详情</p>
                </div>
                <div class="calendats-list">
                    <ul>
                        <li class="clearfix"><i></i><a href="#">这是一个标题这是一个标题这是一个标题这是一个标题这是一个标题这是一个标题</a><span class="time">2017/04/11</span></li>
                        <li class="clearfix"><i></i><a href="#">这是一个标题这是一个标题这是一个标题这是一个标题这是一个标题这是一个标题</a><span class="time">2017/04/11</span></li>
                        <li class="clearfix"><i></i><a href="#">这是一个标题这是一个标题这是一个标题这是一个标题这是一个标题这是一个标题</a><span class="time">2017/04/11</span></li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
    <!--文化新闻结束-->
    <!--底部开始-->
    <div class="footer-container">
        <div class="public-footer clearfix">
            <div class="download clearfix">
                <div class="ios-android float-left">
                    <div class="img">
                        <img src="${basePath}/static/assets/img/public/whg.jpg" width="117" height="117">
                        <span>IOS</span>
                    </div>
                    <span class="line"></span>
                </div>
                <div class="ios-android float-right">
                    <div class="img">
                        <img src="${basePath}/static/assets/img/public/whg.jpg" width="117" height="117">
                        <span>android</span>
                    </div>
                </div>
            </div>
            <div class="message">
                <p><span>联系电话：xxxx-xxxxxxxx</span><span>传真：xxxx-xxxxxxxx</span></p>
                <p>台州文化云&nbsp;&nbsp;&nbsp;&nbsp;版权所有&nbsp;&nbsp;&nbsp;&nbsp;ICP备案号：xxxxxxxx&nbsp;&nbsp;&nbsp;&nbsp;服务邮箱：xxxxxxxxxxxxxxxxxxxxx&nbsp;&nbsp;&nbsp;&nbsp;技术支持：<a href="#">上海创图</a></p>
            </div>
        </div>
    </div>
    <!--底部结束-->
    <a class="to-top"></a>
    <script type="text/javascript">
        function setActivity(actList) {
            $("#activityContent").children("li").remove();
            $.each(actList,function (index,value) {

                var $li = $("<li>");
                if(2 == index || 5 == index){
                    $li.addClass("last");
                }
                var $div = $('<div class="img">');
                $div.append('<a href="${basePath }/agdwhhd/activityinfo?actvid='+value.id+'"><img src="${basePath}/whgstatic' + value.imgurl + '" width="326" height="240"></a>');
                $div.appendTo($li);
                var $detail = $('<div class="detail">');
                $detail.append('<h4><a href="${basePath }/agdwhhd/activityinfo?actvid='+value.id+'">'+value.name+'</a></h4>');
                $detail.append('<p>时间：<span>'+WhgComm.FMTDate(value.starttime)+'</span></p>');
                $detail.append('<p>地点：<span>'+value.address+'</span></p>');
                $detail.appendTo($li);
                $li.append('<a href="${basePath }/agdwhhd/activityinfo?actvid='+value.id+'" class="goDetail"></a>');
                $("#activityContent").append($li);
            });
        }
        
        function setVenue(venList) {
            $("#venContent").children("li").remove();
            $.each(venList,function (index,value) {
                if(3 <= index){
                    return;
                }
                var $li = $("<li>");
                if(1 == index){
                    $li.addClass("active");
                }
                var $div = $('<div class="content">');
                $div.append('<a href="${basePath}/agdcgfw/venueinfo?venid='+value.id+'"><div class="img"><img src="${basePath}/whgstatic'+value.imgurl+'"></a></div>');
                $div.append('<div class="desc"><h2>'+value.title+'</h2><p>'+value.summary+'</p></div>');
                $div.appendTo($li);
                $li.append('<a href="${basePath }/agdcgfw/venueinfo?venid='+value.id+'"></a>');
                $li.on("hover",function () {
                    $("#venContent").children("li").removeClass("active");
                    $(this).addClass("active");
                })
                $("#venContent").append($li);
            })
        }

        function setPopResource(popResource,popSourceDoc) {
            if(null == popResource || "" == popResource){
                return;
            }
            if(null == popSourceDoc || "" == popSourceDoc){
                return;
            }
            $("#popContent").children("div").remove();
            $.getJSON("${basePath}/home/popSource",function (data) {
                if("0" != data.code){
                    return;
                }
                var myList = data.data.data;
                if(null == myList || 0 == myList.length){
                    return;
                }
                var $popLeft = $('<div class="left-wrapper" id="popLeft">');
                var $popRight = $('<div class="right-wrapper">');
                var $ulRight = $('<ul class="clearfix" id="popRight">');
                $.each(myList,function (index,value) {
                    var docUrl = popSourceDoc + value.id;
                    if(0 == index){
                        var $divImg = $('<div class="img">');
                        $divImg.append('<img src="'+value.imageUrl+'" width="500" height="420">');
                        $divImg.appendTo($popLeft);
                        $popLeft.append('<div class="title">'+value.name+'</div>');
                        $popLeft.append('<a href="'+docUrl+'" target="_blank"></a>');
                    }else{
                        var $li = $('<li>');
                        $li.append('<div class="img"><img src="'+value.imageUrl+'" width="316" height="193"></div>');
                        $li.append('<div class="title">'+value.name+'</div>');
                        $li.append('<a href="'+docUrl+'" target="_blank"></a>');
                        $ulRight.append($li);
                    }
                });
                $popLeft.appendTo("#popContent");
                $ulRight.appendTo($popRight);
                $popRight.appendTo("#popContent");
            })
        }

        $(function () {
            var actList = eval(${actList});
            var venList = eval(${venList});
            var popResource = '${popResource}';
            var popSourceDoc = '${popSourceDoc}';
            setActivity(actList);
            setVenue(venList);
            setPopResource(popResource,popSourceDoc);
        });

    </script>
</body>
</html>
