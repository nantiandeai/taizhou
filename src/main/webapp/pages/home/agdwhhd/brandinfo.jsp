<%@ page import="com.creatoo.hn.model.WhgYwiWhpp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8">
	<%@include file="/pages/comm/agdhead.jsp"%>
	<title>台州文化云-文化品牌</title>
	<link href="${basePath }/static/assets/css/activity/specialDetail.css" rel="stylesheet">
	<script src="${basePath }/static/assets/js/activity/specialDetail.js"></script>
</head>
<body class="oiplayer-example">

<!--公共主头部开始-->
<div id="header">
	<div class="main clearfix">
		<div class="gatalogMain">
			<div class="return">
				<a href="${basePath}/agdwhhd/brandlist" class="btn">
					<i class="kx-arrow kx-arrow-right">
						<em></em>
						<span></span>
					</i>
					返回文化品牌列表
				</a>
			</div>
		</div>
	</div>
</div>
<!--公共主头部结束-->

<!--主体开始-->
<c:choose>
	<c:when test="${brand.norepeat !=null && brand.norepeat == 0}">
		<c:set var="whgpp_norepeat" value="no-repeat" />
	</c:when>
	<c:otherwise>
		<c:set var="whgpp_norepeat" value="repeat" />
	</c:otherwise>
</c:choose>
<c:choose>
	<c:when test="${brand.bgpicture !=null && brand.bgcolour != null}">
		<div class="special-bg" style="background:url(${imgServerAddr}${brand.bgpicture}) center top ${whgpp_norepeat} ${brand.bgcolour};">
	</c:when>
	<c:when test="${brand.bgpicture !=null}">
		<div class="special-bg" style="background:url(${imgServerAddr}${brand.bgpicture}) center top ${whgpp_norepeat};">
	</c:when>
	<c:otherwise>
		<div class="special-bg" style="background:url(${basePath }/static/assets/img/activity/special-bg.jpg) center top ${whgpp_norepeat};">
	</c:otherwise>
</c:choose>
	<div class="special-main">
		<div class="banner">
			<h1>${brand.shortname}</h1>
			<div>${brand.introduction}</div>
		</div>
		<div class="groups">
			<ul class="clearfix">
				<c:forEach items="${actList}" var="row" varStatus="s">
				<li>
					<a href="${basePath}/agdwhhd/activityinfo?actvid=${row.id}">
						<img width="255" height="148" src="${imgServerAddr}${row.imgurl}">
						<h2>${row.name}</h2>
						<p>时间：<span><fmt:formatDate value="${row.starttime}" pattern="yyyy-MM-dd"/>至<fmt:formatDate value="${row.endtime}" pattern="yyyy-MM-dd"/></span></p>
						<p>地址：<span>${row.address}</span></p>
					</a>
				</li>
				</c:forEach>
			</ul>
		</div>



		<!-- 图片视频资源 -->
		<div class="site special_file clearfix">
			<ul class="tab clearfix">
				<li class="active">活动图片</li>
				<li class="">活动视频</li>
				<li class="">活动音频</li>
			</ul>
			<div class="list1 on">
				<div class="demo-list list-video clearfix">
					<c:choose>
						<c:when test="${imgList !=null && fn:length(imgList) > 0}">
							<c:forEach items="${imgList}" var="row" varStatus="s">
								<a href="javascript:void(0)" onclick="show_img(this,{url:'${basePath }/static/assets/img/img_demo/works-1.jpg'})">
									<div class="img1">
										<img src="${imgServerAddr }${row.enturl }">
										<span>${row.entname}</span>
									</div>
								</a>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<div class="special_msg">暂无更新活动图片</div>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
			<div class="list1">
				<div class="demo-list list-video clearfix">
					<c:choose>
						<c:when test="${vedioList !=null && fn:length(vedioList) > 0}">
							<c:forEach items="${vedioList}" var="row" varStatus="s">
								<a href="javascript:void(0)" onclick="show_vedio(this,{url:'${basePath }/static/assets/img/img_demo/xl.mp4'})">
									<div class="mask"></div>
									<div class="video1">
										<img src="${imgServerAddr }${row.enturl }">
										<span>${row.entname}</span>
									</div>
								</a>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<div class="special_msg">暂无更新活动视频</div>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
			<div class="list1">
				<div class="demo-list list-mp3 clearfix">
					<c:choose>
						<c:when test="${audioList !=null && fn:length(audioList) > 0}">
							<c:forEach items="${audioList}" var="row" varStatus="s">
								<a href="javascript:void(0)" onclick="show_vedio(this,{url:'${imgServerAddr }${row.enturl }'})">
									<div class="mask"></div>
									<div class="mp31">
										<span>${row.entname}</span>
									</div>
								</a>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<div class="special_msg">暂无更新活动音频</div>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</div>
		<!-- 图片视频资源-END -->

		<div class="backrun"><a href="${basePath}/agdwhhd/brandlist">返回文化品牌列表</a></div>
	</div>
</div>
<!--主体结束-->

<!--公共主底部开始-->
<%@include file="/pages/comm/agdfooter.jsp"%>
<!--公共主底部结束-END-->
</body>
</html>