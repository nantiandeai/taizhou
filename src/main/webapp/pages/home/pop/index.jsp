<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <%@include file="/pages/comm/agdhead.jsp"%>
    <title>台州文化云-热门资源</title>
    <link href="${basePath }/static/assets/css/field/fieldList.css" rel="stylesheet">
    <script src="${basePath }/static/assets/js/field/fieldList.js"></script>

    <script src="${basePath }/static/common/js/whg.maps.js"></script>

    <script type="text/javascript">

        var tools = (function(){
            //查询条件层选择器
            var queryDiv = "div.categoryChange";
            //列表项模板HTML
            var jsItemDomeLi = '';
            //列表项父UL
            var targetUL = '';

            function init(){
                //数据项模板初始处理
                var jsDome = $(".js-item-dome-li");
                jsItemDomeLi = jsDome.prop("outerHTML");
                targetUL = jsDome.parents("ul");
                //targetUL = $("ul[id='connet']");
                //处理点击类型的查询条件
                $(queryDiv).find("a[pname]").off("click.pname").on("click.pname", onPnameClick);

                //查询点击
                $("#btn_sub").on('click', function(e){
                    e.preventDefault();
                    loadData();
                });

                //回车事件
                $("body").keydown(function() {
                    var isFocus = $("#title").is(":focus");
                    if (event.keyCode == "13" && isFocus) {
                        loadData();
                    }
                });
            }

            function onPnameClick(){
                var _li = $(this).parents("li");
                if (_li.length){
                    //显隐动画之后再查数据
                    setTimeout(function(){loadData()}, 500);
                }else{
                    $(this).parents("span.item").addClass("active").siblings().removeClass("active");
                    loadData();
                }
            }

            function getQueryParams(){
                var params = {};
                //选中项
                $(queryDiv).find(".active:visible").each(function(){
                    var paramA = $(this).find('a');
                    var name = paramA.attr('pname');
                    var value = paramA.attr('pvalue');
                    if (name && value){
                        params[name] = value;
                    }
                });
                //输入项
                var title = $(queryDiv).find("#title").val();
                if (title){
                    params.title = title;
                }

                return params;
            }

            function loadData(page, rows){
                var params = getQueryParams();
                params.page = page||1;
                params.rows = rows||20;
                $.ajax({
                    url: '${basePath}/pop/list',
                    type: "POST",
                    data: params,
                    success : function(data){
                        //showData(data);
                        if("0" != data.code){
                            return;
                        }
                        var myData = data.data;
                        showDataForTz(myData);
                        genPagging('paging', params.page, params.rows, myData.totalPage||0, loadData);
                        if(myData.totalPage == 0){
                            $(".public-no-message").show();
                        }else{
                            $(".public-no-message").hide();
                        }
                    }
                });

            }

            function showDataForTz(data) {
                $("ul[id='connet']").children("li").remove();
                $.each(data.data,function (index,value) {
                    var $li = $("<li>");
                    var $a = $('<a href="${popSourceDoc}'+value.id+'" target="_blank" />');
                    var $img = $('<div class="img">');
                    $img.append('<img src="'+value.imageUrl+'" width="280" height="210">');
                    $img.append('<div class="mask"></div>');
                    $img.appendTo($a);
                    $a.appendTo($li);
                    var $detail = $('<div class="detail">');
                    $detail.append('<h2>'+value.name+'</h2>');
                    $detail.appendTo($li);
                    $li.appendTo("ul[id='connet']");
                });

            }

            function showData(data){
                //显示数据项
                targetUL.children("li").remove();
                for (var i in data.rows){
                    var row = data.rows[i];
                    var item = $(jsItemDomeLi);
                    targetUL.append(item);
                    item.removeClass("js-item-dome-li").show();

                    item.find(".img a, .info h2 a").attr("href", "${basePath}/agdcgfw/venueinfo?venid="+row.id);
                    item.find(".img img").attr("src", WhgComm.getImg750_500('${imgServerAddr}'+row.imgurl) );
                    item.find(".info h2 a").text(row.title);
                    item.find(".info p.adr span.desc-text").text(row.address);
                    item.find(".info p.cate span.desc-text").text( WhgComm.FMTVenueType(row.etype) );
                    item.find(".info p.desc span.desc-text").text(row.summary);

                    //item.find(".info p.type").html('<span class="d">比赛</span>');
                    var etagP = item.find(".info p.type");
                    var etags = row.etag || '';
                    etags = etags.split(/\s*,\s*/);
                    for(var i in etags){
                        if (!etags[i]) continue;
                        etagP.append('<span class="d">'+WhgComm.FMTVenueTag(etags[i])+'</span>')
                    }
                    var mapParams = {address:row.address,longitude:row.longitude, latitude:row.latitude };
                    item.find(".info p.adr a").off("click.maps").on("click.maps",mapParams,function(evt){
                        WhgMap.openMap(evt.data.address, evt.data.longitude, evt.data.latitude);
                    })
                }
                //处理分页

            }

            return {
                init : init,
                loadData: loadData
            }
        })();

        $(function () {
            tools.init();
            tools.loadData();
        })
    </script>
</head>
<body>

<!-- 公共头部开始 -->
<%@include file="/pages/comm/agdtop.jsp"%>
<!-- 公共头部结束-END -->

<!--主体开始-->
<div class="venue-main" >
    <div class="categoryChange" style="display: none">
        <div class="row clearfix adrCont">
            <div class="title">区域</div>
            <div class="adrList">
                <span class="item active"><a href="javascript:void(0)" pname="area" pvalue="">全部</a></span>
                <c:forEach items="${areas }" var="item">
                    <span class="item"><a href="javascript:void(0)" pname="area" pvalue="${item.id}">${item.name}</a></span>
                </c:forEach>
            </div>
        </div>

        <div class="row clearfix" style="display: none">
            <div class="title">类型</div>
            <div class="adrList">
                <span class="item active"><a href="javascript:void(0)" pname="etype" pvalue="">全部</a></span>
                <c:forEach items="${types }" var="item">
                    <span class="item"><a href="javascript:void(0)" pname="etype" pvalue="${item.id}">${item.name}</a></span>
                </c:forEach>
            </div>
        </div>

    </div>
</div>
<div class="main clearfix">
    <div class="venue-main-left">
        <div class="public-no-message" style="display: none"></div>
        <ul class="group clearfix" id="connet">

        </ul>

        <div class="green-black" id="paging">
        </div>

    </div>
</div>
<!--主体结束-->

<!--公共主底部开始-->
<%@include file="/pages/comm/agdfooter.jsp"%>
<!--公共主底部结束-END-->
<script type="text/javascript">
    $(function () {
        tools.loadData(1,20);
    })
</script>
</body>
</html>