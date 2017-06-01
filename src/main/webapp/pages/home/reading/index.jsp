<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <%@include file="/pages/comm/agdhead.jsp"%>
    <title>数字阅读</title>
    <link href="${basePath }/static/assets/css/index/index.css" rel="stylesheet">
    <link href="${basePath }/static/assets/css/public/reset.css" rel="stylesheet">
    <link href="${basePath }/static/assets/css/read/readList.css" rel="stylesheet">
    <script src="${basePath }/static/assets/js/public/jquery-1.11.0.min.js" type="text/javascript"></script>
    <script src="${basePath }/static/assets/js/public/jquery-migrate-1.0.0.js" type="text/javascript"></script>
    <script src="${basePath }/static/assets/js/plugins/roll/jquery.sly.min.js" type="text/javascript"></script>
    <script src="${basePath }/static/assets/js/public/public.js"></script>
    <script src="${basePath }/static/assets/js/index/index.js" type="text/javascript"></script>
</head>
<body>
    <!--公共主头部开始-->
    <%@include file="/pages/comm/agdtop.jsp"%>
    <!--公共主头部结束-->

    <!--页面主体开始-->
    <div class="public-tz-main">
        <div class="search-cont">
            <ul class="tab-list clearfix" id="mySelection">
                <li class="active" section="all">全部</li>
                <li section="tushu">图书</li>
                <li section="qikan">报刊</li>
                <li section="tupian">图片</li>
                <li section="shijuan">考试</li>
                <li section="shipin">视频</li>
                <li section="zonghe">综合</li>
                <li section="shiyong">实体文献</li>
            </ul>
            <script type="text/javascript">
                $(".tab-list li").on("click",function(){
                    $(this).addClass("active").siblings().removeClass("active");
                })
            </script>
            <div class="bg">
                <input type="text" class="search-input" placeholder="数据库统一检索">
                <button class="search-btn"></button>
            </div>
            <script type="text/javascript">
                $(".search-btn").on("click",function () {
                    var serchWord = $(".search-input").val();
                    var section = $("#mySelection").find("li.active").attr("section");
                    if(null == serchWord || "" == serchWord){
                        return;
                    }
                    if(null == section || "" == section){
                        section = "all";
                    }
                    var urlStr = "http://www.tzlib.info/listData!getList.action?keywords=" + serchWord + "&chkval=title&section=" + section + "&ordertype=default";
                    window.open(encodeURI(urlStr));
                })
            </script>
        </div>
        <div class="list-3-type-1">
            <ul class="u-b clearfix" id="zxContent">
                <li>
                    <div class="img">
                        <a href="#"><img src="${basePath }/static/assets/img/img_demo/tz2.jpg"> </a>
                    </div>
                    <h2>书式生活</h2>
                    <p>以“书海泛舟 修身启智——弘扬中华优秀...”</p>
                </li>
                <li>
                    <div class="img">
                        <a href="#"><img src="${basePath }/static/assets/img/img_demo/tz2.jpg"> </a>
                    </div>
                    <h2>书式生活</h2>
                    <p>以“书海泛舟 修身启智——弘扬中华优秀...”</p>
                </li>
                <li>
                    <div class="img">
                        <a href="#"><img src="${basePath }/static/assets/img/img_demo/tz2.jpg"> </a>
                    </div>
                    <h2>书式生活</h2>
                    <p>以“书海泛舟 修身启智——弘扬中华优秀...”</p>
                </li>
            </ul>
        </div>
        <div class="ad-1" id="carouselContent">
            <a href="#"><img src="${basePath }/static/assets/img/img_demo/tz1.jpg"> </a>
        </div>
        <div class="list-3-type-1">
            <ul class="u-r clearfix" id="actContent">
                <li>
                    <div class="img">
                        <a href="#"><img src="${basePath }/static/assets/img/img_demo/tz2.jpg"> </a>
                    </div>
                    <h2>书式生活</h2>
                    <p>以“书海泛舟 修身启智——弘扬中华优秀...”</p>
                </li>
                <li>
                    <div class="img">
                        <a href="#"><img src="${basePath }/static/assets/img/img_demo/tz2.jpg"> </a>
                    </div>
                    <h2>书式生活</h2>
                    <p>以“书海泛舟 修身启智——弘扬中华优秀...”</p>
                </li>
                <li>
                    <div class="img">
                        <a href="#"><img src="${basePath }/static/assets/img/img_demo/tz2.jpg"> </a>
                    </div>
                    <h2>书式生活</h2>
                    <p>以“书海泛舟 修身启智——弘扬中华优秀...”</p>
                </li>
            </ul>
        </div>
    </div>
    <!--页面主体结束-->



    <!--公共主底部开始-->
    <%@include file="/pages/comm/agdfooter.jsp"%>
    <!--公共主底部结束-END-->
<script type="text/javascript">
    function setZxContent(zxList) {
        if(null == zxList || "" == zxList){
            return;
        }
        var list = JSON.parse(zxList);
        if(0 == list.length){
            return;
        }
        $("#zxContent").children("li").remove();
        $("#actContent").children("li").remove();
        $.each(list,function(index,value){
            if(3 > index){
                var $li = $("<li>");
                $li.append('<div class="img"><a href="' + value.infolink + '" target="_blank"><img src="${basePath}/whgstatic' + value.infocover + '" width="300" height="190"> </a></div>');
                $li.append('<h2>'+value.infotitle+'</h2>');
                $li.append('<p>' + value.infosummary + '</p>');
                $("#zxContent").append($li);
            }else{
                var $li = $("<li>");
                $li.append('<div class="img"><a href="'+value.infolink+'" target="_blank"><img src="${basePath }/whgstatic'+value.infocover+'" width="300" height="190"> </a></div>');
                $li.append('<h2>'+value.infotitle+'</h2><p>'+value.infosummary+'</p>');
                $li.appendTo("#actContent");
            }
        });
    }

    function setCarousel(carousel) {
        if(null == carousel || "" == carousel){
            return;
        }
        var list = JSON.parse(carousel);
        if(0 == list.length){
            return;
        }
        $("#carouselContent").children("a").remove();
        $.each(list,function (index,value) {
            var $a = $("<a href='"+value.url+"' target='_blank'>");
            $a.append("<img src='${basePath}/whgstatic"+value.picture+"' width='1200' height='400'>");
            $("#carouselContent").append($a);
        });
    }

    $(function () {
        var infoList = '${infoList}';
        var carousel = '${carousel}';
        setZxContent(infoList);
        setCarousel(carousel);
    });
</script>
</body>
</html>
