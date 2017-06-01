<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<%@include file="/pages/comm/agdhead.jsp"%>
<title>台州文化云-数字展馆-${exh.exhtitle }</title>
<link href="${basePath }/static/assets/css/science/scienceList.css" rel="stylesheet">
<script src="${basePath }/static/assets/js/science/scienceList.js"></script>
<script type="text/javascript"> var _exhid = "${exh.exhid }"; </script>
<script type="text/javascript" src="${basePath }/pages/home/agdszzg/zglist.js"></script>
</head>
<body>
<!-- 头部开始 -->
<%@include file="/pages/comm/agdtopsmall.jsp"%>
<!-- 头部结束-END -->

<!--公共主头部--> 
<div id="header-fix">
	<div class="header-nav-bg">
		<div class="header-nav">
			<div class="logo-small">
				<a href="${basePath }/home"><img src="${basePath }/static/assets/img/public/logoSmall.png"></a>
			</div>
			<ul>
				<li class="active last"><a href="${basePath }/agdszzg/index">数字展厅</a></li>
				<!-- <li class="last"><a href="#">普通展厅</a></li> -->
			</ul>
		</div>
	</div>
</div>
<!--公共主头部结束--> 

<!--主体开始-->
<div class="main-info-bg main-info-bgColorW clearfix">
	<div class="main-info-container">
	    <div class="categoryChange">
			<div class="row clearfix">
			
				<!--分类-->
				<div class="title">分类</div>
				<div class="adrList adrList1">
					<span class="item active"><a href="javascript:void(0)" arttyp="">全部</a></span>
						<c:forEach items="${artList }" var="art" varStatus="s">
							<span class="item"><a href="javascript:void(0)" arttyp="${art.typid }">${art.typname }</a></span>
						</c:forEach>
				</div>
				<!-- 分类-end -->
				
				<!-- 搜索 -->
				<div class="searchCont">
					<input placeholder="搜点什么..." id="select">
					<button type="submit" id="selectBtn"></button>
				</div>
				<!-- 搜索-end -->
			</div>
		</div>
	    
	    <!--返回-->
	    <h2 class="scienceList-title">${exh.exhtitle }<em><a href="${basePath }/agdszzg/index"> &lt;返回</a></em></h2> 
	    <!--返回-end-->
	      
       	<!--空页面  -->
    	<div class="public-no-message"></div>
    	<!--空页面-END-->
    	
	    <!-- 资讯内容 -->
	    <div id="wrap" class="clearfix">
	    <div class = "box none"></div>
	    </div>
		<!--资讯内容-end-->
		
		<!-- 分页-->
	    <div class="green-black" id="artPagging"></div>
	    <!-- 分页-end -->
	    
	</div>
</div>
<!--主体结束--> 

<!--公共主底部开始-->
<%@include file="/pages/comm/agdfooter.jsp"%>
<!--公共主底部结束-END-->
</body>
</html>