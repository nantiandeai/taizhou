<%@page import="java.util.Date" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<% request.setAttribute("now", new Date()); %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <%@include file="/pages/comm/agdhead.jsp" %>
    <title>${actdetail.name}</title>
    <link href="${basePath }/static/assets/css/activity/activityDetaiil.css" rel="stylesheet">
    <link href="${basePath }/static/assets/js/plugins/oiplayer-master/css/oiplayer.css" rel="stylesheet">

    <script src="${basePath }/static/assets/js/plugins/sidebar/stickySidebar.js"></script>
    <script src="${basePath }/static/assets/js/activity/activityDetail.js"></script>
    <script src="${basePath }/static/assets/js/plugins/oiplayer-master/plugins/flowplayer-3.2.6.min.js"></script>
    <script src="${basePath }/static/assets/js/plugins/oiplayer-master/js/jquery.oiplayer.js"></script>

    <script type="text/javascript">
        $(function () {
            $(".source li:eq(0)").addClass("active");
            var _html = $(".source li:eq(0)").html();
            $(".sourceinfo").children("div:eq(0)").addClass("on");

            if ($(".nowlist:eq(0)").size() == 0) {
                $(".actvitm:eq(0)").click();
            } else {
                $(".nowlist:eq(0)").click();
            }

            if ($(".source li").size() == 0) {
                $(".source").remove();
            }

            $(".js-fmt-text").each(function () {
                var v = $(this).attr("js-val");
                var fn = $(this).attr('js-fn');
                var text = WhgComm[fn].call(WhgComm, v);
                $(this).text(text);
            })
            
            var sellticket = "${actdetail.sellticket }";
	        if (sellticket == 1) {
	            $("#goNext").css("display", "none");
	        }
	        
	        var endTime = "${actdetail.endtime }";
	        var nowDate = new Date();
	        endTime = dateFMT(endTime);
	        var nowTime = dateFMT(nowDate);
	        if(endTime < nowTime ){
	        	 $("#goNext").html('<a >活动已结束</a>').addClass("no-editable");
	        } 
        })

	    function checkLogin() {
	        rongDialog({type: false, title: "登录之后才能预定！", time: 3 * 1000});
	        $('#toLogin').attr('href', '${basePath }/login');
	    }

	    function checkActState() {
	        var actId = '${actdetail.id}';
	        $.ajax({
	            url: '${basePath }/agdwhhd/checkActPublish',
	            data: {actId: actId,seatStr:null,eventid:null,seats:0},
	            dataType: "json",
	            success: function (data) {
	                if (data.success == 1) {
	                    window.location.href = '${basePath }/agdwhhd/actBaoMing?actvid=' + actId;
	                } else {
	                    rongDialog({type: false, title: data.errormsg, time: 3 * 1000});
	                }
	            }
	        });
	    }

    </script>

</head>
<body class="oiplayer-example">
<!-- 公共头部开始 -->
<%@include file="/pages/comm/agdtop.jsp" %>
<!-- 公共头部结束-END -->

<!--主体开始-->
<div class="special-bg">
    <div class="activity-main">
        <div class="public-crumbs">
            <span><a href="${basePath }/agdwhhd/index">首页</a></span><span></span><span><a
                href="${basePath }/agdwhhd/index">文化活动</a></span><span></span><span><a
                href="${basePath }/agdwhhd/activitylist">活动预约</a></span><span></span><span>${actdetail.name}</span>
        </div>
        <div class="special-content clearfix">
            <div class="con-left clearfix">
                <div class="color-bg"></div>
                <div class="public-info-step">
                    <div class="main-info-cont clearfix">
                        <h2>${actdetail.name}</h2>
                        <div class="special-head-left">
                            <img alt="" src="${imgServerAddr}${whg:getImg750_500(actdetail.imgurl) }" style="width:380px;height: 240px">
                        </div>
                        <div class="detail">
                            <div class="time1 clearfix"><i class="public-s-ico s-ico-9"></i><span>活动时间：</span><fmt:formatDate value="${actdetail.starttime}" pattern="yyyy-MM-dd "/>~
                                <fmt:formatDate value="${actdetail.endtime}" pattern="yyyy-MM-dd "/>
                            </div>
                            <div class="tel"><i class="public-s-ico s-ico-17"></i>活动电话：<span>${actdetail.telphone}</span></div>
                            <div class="tel"><i class="public-s-ico s-ico-10"></i>活动地址：<span>${actdetail.address}</span>
                            </div>
                            <div class="time">
                                <i class="public-s-ico s-ico-19"></i>活动标签：
                                <span>
									<c:forEach var="item" items="${actdetail.etag }">
                                        <span class="label"><a href="javascript:void (0)"><em class="js-fmt-text" js-fn="FMTActivityTag" js-val="${item }"></em></a></span>
                                    </c:forEach>
						    	</span>
                            </div>
                             <c:if test = "${ not empty sessionUser }">
                             	<div class="goNext" id="goNext">
                                    <a href="#" onclick="checkActState()" >立即报名</a>
                                </div>
                            </c:if>
                            <c:if test = "${empty sessionUser }">
                                <div class="goNext" id="goNext">
                                    <a href="#" id="toLogin" onclick="checkLogin()">立即报名</a>
                                </div>
                            </c:if>
                        </div>
                    </div>
                    <h3><span>活动详情</span></h3>
                    <div class="info">${actdetail.remark}</div>
                    <!--分享 -->
                    <div class="public-share">
                        <span class="btn qq"><a href="javascript:void(0);" class="fxqq"></a></span>
                        <span class="btn weixin"><a a href="javascript:void(0);" class="fxweix"></a></span>
                        <span class="btn weibo"><a href="javascript:void(0)" class="fxweibo" target="_blank"></a></span>
                         <span class="btn dianzan">
                             <em>0</em>
                             <a href="javascript:void(0)" reftyp="${enumtypeAct}" refid="${actdetail.id }" id="good" title="点赞"></a>
                         </span>
                        <span class="btn shoucang">
                            <em>0</em>
                            <a href="javascript:void(0)" reftyp="${enumtypeAct}" refid="${actdetail.id }" id="collection"  title="收藏"></a>
                        </span>
                    </div>
                </div>
                <%--<div class="site clearfix sourceinfo">
                   <ul class="tab clearfix source">
                       <li class="active">活动图片</li>
                       <li>活动视频</li>
                       <li>活动音频</li>
                   </ul>
                   <c:if test="${not empty tsource}">
                       <div class="list1">
                           <div class="demo-list list-video clearfix">
                           <c:forEach items="${tsource}" var="item" varStatus="s">
                               <a ${s.count%3 == 0?'class="last"':'' } href="javascript:void(0)" onClick="show_img(this,{url:'${imgServerAddr}${item.enturl}'})">
                                   <div class="img1">
                                       <img src="${imgServerAddr}${item.enturl}"  width="252" height="170"/>
                                       <span>${item.entname }</span>
                                   </div>
                               </a>
                            </c:forEach>
                           </div>
                       </div>
                   </c:if>
                   <c:if test="${empty tsource}">
                        <div class="list1">
                        </div>
                   </c:if>
                   <c:if test="${not empty ssource}">
                       <div class="list1">
                           <div class="demo-list list-video clearfix">
                            <c:forEach items="${ssource}" var="item" varStatus="s">
                               <a ${s.count%3 == 0?'class="last"':'' } href="javascript:void(0)" onClick="show_vedio(this,{url:'${item.enturl}'})">
                                   <div class="mask"></div>
                                   <div class="video1">
                                       <img src="${imgServerAddr}${item.deourl}" width="252" height="170"  width="252" height="170"/>
                                       <span>${item.entname }</span>
                                   </div>
                               </a>
                              </c:forEach>
                           </div>
                       </div>
                   </c:if>
                   <c:if test="${empty ssource}">
                        <div class="list1">
                        </div>
                   </c:if>
                   <c:if test="${not empty ysource}">
                       <div class="list1">
                           <div class="demo-list list-mp3 clearfix">
                            <c:forEach items="${ysource}" var="item" varStatus="s">
                               <a ${s.count%3 == 0?'class="last"':'' } href="javascript:void(0)" onClick="show_vedio(this,{url:'${item.enturl}'})">
                                     <div class="mask"></div>
                                   <div class="mp31">
                                       <span>${item.entname }</span>
                                   </div>
                               </a>
                            </c:forEach>
                           </div>
                       </div>
                   </c:if>
                   <c:if test="${empty ysource}">
                        <div class="list1">
                        </div>
                   </c:if>
               </div>--%>

                <!-- 动态包含评论 -->
                <jsp:include page="/pages/comm/agdcomment.jsp" flush="true">
                    <jsp:param name="reftype" value="1"/>
                    <jsp:param name="refid" value="${actdetail.id }"/>
                </jsp:include>
                <!-- 动态包含评论-END -->

            </div>
            <div class="public-right-main">
                <div class="public-other-notice">
                    <h2>推荐活动</h2>
                    <c:choose>
                        <c:when test="${not empty acttj}">
                            <c:forEach items="${acttj}" var="item">
                                <div class="item clearfix">
                                    <div class="right-img">
                                        <a href="${basePath }/agdwhhd/activityinfo?actvid=${item.id}"><img
                                                src="${imgServerAddr}${ item.imgurl}" width="130" height="90"
                                                onerror="showDefaultIMG(this, '${basePath }/static/assets/img/img_demo/1.jpg')"></a>
                                    </div>
                                    <div class="right-detail">
                                        <a href="${basePath }/agdwhhd/activityinfo?actvid=${item.id}">
                                            <h3>${item.name}</h3></a>
                                        <p class="time"><fmt:formatDate value="${item.starttime}"
                                                                        pattern="yyyy-MM-dd"/></p>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <div class='public-no-message'></div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
</div>
<!--主体结束-->
<!--公共主底部开始-->
<%@include file="/pages/comm/agdfooter.jsp" %>
<!--公共主底部结束-END-->
</body>
</html>