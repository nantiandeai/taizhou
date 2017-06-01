<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<%@include file="/pages/comm/agdhead.jsp"%>
<title>台州文化云-志愿服务-先进个人-${geren.zyfcgrtitle}</title>
<link href="${basePath }/static/assets/css/volunteer/huodongxiangqing.css" rel="stylesheet">
<link href="${basePath }/static/assets/js/plugins/oiplayer-master/css/oiplayer.css" rel="stylesheet">
<script src="${basePath }/static/assets/js/plugins/oiplayer-master/plugins/flowplayer-3.2.6.min.js"></script>
<script src="${basePath }/static/assets/js/plugins/oiplayer-master/js/jquery.oiplayer.js"></script>
<script src="${basePath }/static/assets/js/volunteer/xiangmushifan.js"></script>
</head>
<body>
<!-- 公共头部开始 -->
<%@include file="/pages/comm/agdtopsmall.jsp"%>
<!-- 公共头部结束-END -->

<!--公共主头部开始-->
<div id="header-fix">
    <div class="header-nav-bg">
        <div class="header-nav">
            <div class="logo-small"> <a href="${basePath }/home"><img src="${basePath }/static/assets/img/public/logoSmall.png"></a> </div>
            <ul>
                <li><a href="${basePath }/agdzyfw/index">志愿服务</a></li>
		        <li><a href="${basePath }/agdzyfw/news">志愿资讯</a></li>
		        <li><a href="${basePath }/agdzyfw/huodong">志愿活动</a></li>
		        <li><a href="${basePath }/agdzyfw/peixun">志愿培训</a></li>
		        <li class="active"><a href="${basePath }/agdzyfw/xiangmu">风采展示</a></li>
		        <li><a href="${basePath }/agdzyfw/tashan">他山之石</a></li>
		        <li class="last"><a href="${basePath }/agdzyfw/zhengce">政策法规</a></li>
            </ul>
        </div>
    </div>
</div>
<!--公共主头部结束-END-->

<!--面包屑开始-->
<div class="public-crumbs">
    <span><a href="${basePath }/dgszwhg/index">首页</a></span><span>></span><span><a href="${basePath }/agdzyfw/index">志愿服务</a></span><span>></span><span><a href="${basePath }/agdzyfw/geren">先进个人</a></span><span>></span><span>${geren.zyfcgrtitle}</span>
</div>
<!--面包屑结束-->
<!--主体开始-->
<div class="special-bg">
    <div class="special-main">
        <div class="special-head">
            <div class="special-head-left" style="background:url(${imgServerAddr }/${not empty geren.zyfcgrbigpic?geren.zyfcgrbigpic:'static/assets/img/img_demo/special-head.jpg'}) no-repeat;"></div>
            <div class="special-head-right clearfix">
                <div class="head-father">
                    <div class="head-con on">
                        <h1 style="margin: 15px 0 10px 0;">${geren.zyfcgrtitle}</h1>
                        <div class="detail">
                            <div class="time"><i class="public-s-ico s-ico-5"></i>文艺专长：<span>${geren.zyfcgrzc}</span></div>
                            <div class="time1"><i class="public-s-ico s-ico-6"></i>工作单位：<span>${geren.zyfcgrworkaddr}</span></div>
                            <div class="time1"><i class="public-s-ico s-ico-7"></i>参与活动：<span>${geren.zyfcgrjoinact}</span></div>
                            <div><i class="public-s-ico s-ico-8"></i>加入时间：<span><fmt:formatDate value="${geren.zyfcgrjrtime}" pattern="yyyy-MM-dd "/></span></div>
                        </div>
                        <div class="time2"><i class="public-s-ico s-ico-10"></i>服务地区：<span>${geren.zyfcgrscope}</span></div>
                        <div class="time2"><i class="public-s-ico s-ico-9"></i>服务时长：<span>${geren.zyfwhen}小时</span></div>
                    </div>
                </div>
            </div>
        </div>
        <div class="special-content clearfix">
            <div class="con-left clearfix">
                <div class="site">
                    <div class="site-head"><span>事迹材料</span></div>
                    <div class="detail">
                        <p>${geren.zyfcgrcontent}</p>
                    </div>
                </div>
                
                <div class="public-share">
	                <span class="btn qq"><a href="javascript:void(0);" class="fxqq"></a></span>
	                <span class="btn weixin"><a href="javascript:void(0)" class="fxweix"></a></span>
					<span class="btn weibo"><a href="javascript:void(0)" class="fxweibo" target="_blank"></a></span>
	                <span class="btn dianzan">
	                	<em>0</em>
	                	<a href="javascript:void(0)" class="dianzan" reftyp="2016120900000003" refid="${geren.zyfcgrid}" id="good">
	                    </a>
	                </span>
            	</div> 
            	
                <div class="site clearfix">
                  <ul class="tab clearfix">
                  <c:if test="${not empty pic}">
                      <li class="active">项目图片</li>
                  </c:if>
                  <c:if test="${not empty vido}">
                      <li>项目视频</li>
                  </c:if>
                  <c:if test="${not empty musci}">
                      <li>项目音频</li>
                  </c:if>
                  </ul>
                  
                 <c:if test="${not empty pic}">
                  <div class="list1 on">
                      <div class="demo-list list-video clearfix">
	                      <c:forEach items="${pic}" var="pics" varStatus="s">
	                          <a ${s.count%3 == 0?'class="last"':'' } href="javascript:void(0)" onClick="show_img(this,{url:'${basePath }/${not empty pics.enturl ? pics.enturl : 'static/assets/img/img_demo/works-1.jpg'}' })">
	                              <div class="img1">
	                                  <img src="${imgServerAddr}/${not empty pics.enturl ? pics.enturl : 'static/assets/img/img_demo/works-1.jpg'}">
	                                  <span>${pics.entname}</span>
	                              </div>
	                          </a>
	                      </c:forEach>
                      </div>
                  </div>
                 </c:if> 
                 
                 <c:if test="${not empty vido}">
                 <div class="list1">
                      <div class="demo-list list-video clearfix">
                     
                      	<c:forEach items="${vido}" var="vidos" varStatus="s">
                          <a ${s.count%3 == 0?'class="last"':'' } href="javascript:void(0)" onClick="show_vedio(this,{url:'${basePath }/${vidos.enturl}',basePath:'${basePath }'})">
                              <div class="mask"></div>
                              <div class="video1">
                                  <img src="${imgServerAddr}/${not empty vidos.deourl ? vidos.deourl : 'static/assets/img/public/vedioBg.jpg'}">
                                  <span>${vidos.entname}</span>
                              </div>
                          </a>
                          </c:forEach>
                         
                      </div>
                  </div>
                 </c:if>
                 
                  <c:if test="${not empty musci}">
                  <div class="list1">
                    <div class="demo-list list-mp3 clearfix">
                    <c:forEach items="${musci}" var="muscis" varStatus="s">
                        <a ${s.count%3 == 0?'class="last"':'' } href="javascript:void(0)" onClick="show_vedio(this,{url:'${basePath }/${muscis.enturl}',basePath:'${basePath }'})">
                        	  <div class="mask"></div>
                            <div class="mp31">
                                <span>${muscis.entname}</span>
                            </div>
                        </a>
                     </c:forEach> 
                    </div>
                </div>
               </c:if> 
              </div>
            </div>
            <div class="public-right-main">
                <div class="public-other-notice">
                    <h2>其它先进个人</h2>
                    <c:choose>
					   <c:when test="${not empty whGrList }">  
						   <c:forEach items="${whGrList}" var="whGrLists" varStatus="s">
			                    <div class="item clearfix">
			                        <div class="right-img">
			                            <a href="${basePath }/agdzyfw/gereninfo?zyfcgrid=${whGrLists.zyfcgrid}"><img src="${imgServerAddr}/${whGrLists.zyfcgrpic}" width="130" height="90"></a>
			                        </div>
			                        <div class="right-detail">
			                            <a href="${basePath }/agdzyfw/gereninfo?zyfcgrid=${whGrLists.zyfcgrid}"><h3>${whGrLists.zyfcgrshorttitle}</h3></a>
			                            <p class="time"><fmt:formatDate value="${whGrLists.zyfcgropttime}" pattern="yyyy-MM-dd"/></p>
			                        </div>
			                    </div>
		                    </c:forEach>      
					   </c:when>
					   <c:otherwise> 
					   		<div class="public-no-message "></div>
					   </c:otherwise>
					</c:choose>
                    
                    <%-- //
                    <c:if test="${not empty whGrList}">
                    <c:forEach items="${whGrList}" var="whGrLists" varStatus="s">
                    <div class="item clearfix">
                        <div class="right-img">
                            <a href="${basePath }/agdzyfw/gereninfo?zyfcgrid=${whGrLists.zyfcgrid}"><img src="${basePath }/${whGrLists.zyfcgrpic}" width="130" height="90"></a>
                        </div>
                        <div class="right-detail">
                            <a href="${basePath }/agdzyfw/gereninfo?zyfcgrid=${whGrLists.zyfcgrid}"><h3>${whGrLists.zyfcgrshorttitle}</h3></a>
                            <p class="time"><fmt:formatDate value="${whGrLists.zyfcgropttime}" pattern="yyyy-MM-dd"/></p>
                        </div>
                    </div>
                    </c:forEach>
                    </c:if> --%>
                </div>
            </div>
            
        </div>
    </div>
</div>
<!--主体结束-->

<!--公共主底部开始-->
<%@include file="/pages/comm/agdfooter.jsp"%>
<!--公共主底部结束-END-->
</body>
</html>