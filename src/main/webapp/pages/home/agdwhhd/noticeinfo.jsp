<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<%@include file="/pages/comm/agdhead.jsp"%>
<title>台州文化云-文化活动-活动公告-${wh_zx_colinfo.clnftltle }</title>
<link href="${basePath }/static/assets/css/train/noticeInfo.css" rel="stylesheet">
</head>
<body>
<!-- 公共头部 -->
<%@include file="/pages/comm/agdtopsmall.jsp"%>
<!-- 公共头部-END -->

<!--公共主头部-->
<div id="header-fix">
	<div class="header-nav-bg">
		<div class="header-nav">
			<div class="logo-small">
				<a href="${basePath }/home"><img src="${basePath }/static/assets/img/public/logoSmall.png"></a>
			</div>
			<ul>
				<%--<li><a href="${basePath }/agdwhhd/index">文化活动</a></li>--%>
				<li ><a href="${basePath }/agdwhhd/activitylist">活动预约</a></li>
				 <li class="active"><a href="${basePath }/agdwhhd/notice">活动公告</a></li>
				<li><a href="${basePath }/agdwhhd/news">活动资讯</a></li>
				<li class="last"><a href="${basePath }/agdwhhd/brandlist">品牌活动</a></li>
			</ul>
		</div>
	</div>
</div>
<!--公共主头部-END-->

<!-- 面包屑 -->
<div class="public-crumbs">
    <span><a href="${basePath }/home">首页</a></span><span>></span>
    <span><a href="${basePath }/agdwhhd/index">文化活动</a></span><span>></span>
    <span><a href="${basePath }/agdwhhd/notice">活动公告</a></span><span>></span>
    <span>${wh_zx_colinfo.clnftltle }</span>
</div>
<!-- 面包屑-END -->

<!--主体开始-->
<div class="main-info-bg main-info-no-padding main-info-bgColorW">
	<div class="main-info-container clearfix">
		
		<!-- 公告详情 -->
		<div class="public-left-main">
			<div class="noticeInfo-main">
				<h1>${wh_zx_colinfo.clnftltle }</h1>
				<div class="otherInfo">
					<span class="adr">来源 :<span>${wh_zx_colinfo.clnfsource }</span></span>
					<span class="key">关键字 :<span>${wh_zx_colinfo.clnfkey}</span></span> 
					<span class="time">时间 :<span><fmt:formatDate value="${wh_zx_colinfo.clnfcrttime}" pattern="yyyy.MM.dd" /></span></span>
				</div>
				<div class="info-main">${wh_zx_colinfo.clnfdetail }</div>
				<!-- 视频 -->
				<c:forEach items="${loadent }" var="loadents" varStatus="s">
					<div class="video">
						<div id="clientcaps"></div>
						<c:if test="${not empty loadents.deourl}">
						<video width="800" height="525" poster="${imgServerAddr }/${loadents.deourl}" controls  oncontextmenu="return false">
							</c:if>
							<c:if test="${empty loadents.deourl}">
							<video width="800" height="525" poster="${basePath }/static/assets/img/public/vedioBg.jpg" controls  oncontextmenu="return false">
								</c:if>
								<c:set var="video_url" value="${loadents.enturl}"></c:set>
								<c:if test="${fn:startsWith(loadents.enturl, 'upload')}">
									<c:set var="video_url" value="${basePath }/${loadents.enturl}"></c:set>
								</c:if>
								<source type="video/mp4" src="${video_url}" alt="szwhg" />
									<%-- <source type="video/mp4" src="${basePath }/${loadents.enturl}" /> --%>
							</video>
							<p>${loadents.entname }</p>
					</div>
				</c:forEach>

				<!-- 音频 -->
				<c:forEach items="${loadclazz}" var="loadclazzs" varStatus="s">
					<div class="video">
						<div id="clientcaps2"></div>
						<video width="800" height="525" poster="${basePath }/static/assets/img/public/vedioBg.jpg" controls  oncontextmenu="return false">
							<source type="video/mp4" src="${basePath }/${loadclazzs.enturl}" />
						</video>
						<p>${loadclazzs.entname }</p>
					</div>
				</c:forEach>

				<!-- 图片 -->
				<c:forEach items="${loadwhe }" var="loadwhes" varStatus="s">
					<div class="img-upload-list">
						<img src="${imgServerAddr }/${loadwhes.enturl}">
						<p>${loadwhes.entname }</p>
					</div>
				</c:forEach>

				<div class="next-notice">
					<a href="${basePath }/agdwhhd/noticeinfo?id=${wh_zx_colinfo_next.clnfid}">下一篇<span>${wh_zx_colinfo_next.clnftltle}</span></a>
				</div>
			</div>
		</div>
		<!-- 公告详情-END -->
		
		<!-- 相关推荐 -->
		<div class="public-right-main">
			<div class="public-other-notice">
				<h2>相关推荐</h2>
				<ul>
					<c:if test="${wh_zx_colinfo_ref != null && fn:length(wh_zx_colinfo_ref) > 0}">
						<c:forEach items="${wh_zx_colinfo_ref }" var="row" varStatus="s">
							<li><a href="${basePath }/agdwhhd/noticeinfo?id=${row.clnfid}">${row.clnftltle }</a><p class="time"><fmt:formatDate value="${row.clnfcrttime}" pattern="yyyy-MM-dd" /></p></li>
						</c:forEach>
					</c:if>
				</ul>
			</div>
		</div>
		<!-- 相关推荐-END -->
		
	</div>
</div>
<!--主体结束-->

<!--公共主底部开始-->
<%@include file="/pages/comm/agdfooter.jsp"%>
<!--公共主底部结束-END-->
</body>
</html>