<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<% 
String __nav = request.getParameter("nav"); request.setAttribute("__nav", __nav);
String sys_mode = request.getRequestURI(); 
sys_mode = sys_mode.substring(sys_mode.lastIndexOf("pages/")+6);
if("index.jsp".equals(sys_mode)){
	sys_mode = "";
}else if(sys_mode.endsWith("update.jsp")){
	sys_mode = sys_mode.substring(17);
	sys_mode = sys_mode.substring(0, sys_mode.lastIndexOf("/"));
}else if(sys_mode != null && sys_mode.startsWith("home")){
	sys_mode = sys_mode.substring(sys_mode.lastIndexOf("home/")+5);
	if(sys_mode.lastIndexOf("/") > -1){
		sys_mode = sys_mode.substring(0, sys_mode.lastIndexOf("/"));
	}
}
request.setAttribute("sys_mode", sys_mode); 
%>
<script>var __nav = '${sys_mode}'; var basePath = '${basePath}';</script>
<script type="text/javascript">
	//查询
/*	$.ajax({
		type : "POST",
		url : '$ {basePath}/center/alertHeader',
		dataType: 'json',
		success : function(data){
			if(data.success == "0"){
				if (data.msgCount && data.msgCount>0){
					$(".comm_alert").html(data.msgCount);
					$(".comm_alert").addClass("userMsg");
				}else{
					$(".comm_alert").html("");
					$(".comm_alert").removeClass("userMsg");
				}
			}
		}
	});*/

	$(function(){
	    //全局搜索
		$("#header").on("click", ".search-login .icon-search", function(event){
			var _input = $(this).siblings('input:first');
			if (_input && _input.val()){
				window.location = "${basePath}/search?srchkey="+encodeURIComponent(_input.val());
			}
		})
		$("#header").on("keydown", ".search input:first", function(event){
			if (event.keyCode==13){
				$(this).siblings(".icon-search").click();
			}
		});

		//登录状态处理
        $.ajax({
            type : "GET",
            url : '${basePath}/home/validLogin',
            dataType: 'json',
			cache: false,
            success : function(res){
                var data = res.data;
                if(res.success == "1"){
                    if(data.login == "true"){
                        $('#header .login').hide();
                        $('#header .logon').show();
                        $('#header .logon .userName a').text(data.username);
					}else{
						$('#header .login').show();
						$('#header .logon').hide();
					}
                }
            }
        });
	})
</script>
<script src="${basePath }/pages/comm/agdtop.js"></script>
<div id="header">
	<div class="main-header clearfix">
		<div class="logo">
			<div class="img">
				<img src="${basePath}/static/assets/img/index/LOGO.png" width="100%">
				<a href="${basePath}/"></a>
			</div>
		</div>
		<div class="header-nav clearfix">
			<div class="header-nav-content">
				<ul id="whgnav">
					<li><a href="${basePath}/">首页</a></li>
					<li><a href="${basePath}/agdwhhd/index">活动</a></li>
					<li><a href="${basePath}/agdcgfw/index">场馆</a></li>
					<li><a href="javascript:void(0);" style="color: grey">志愿者</a></li>
					<li><a href="javascript:void(0);" style="color: grey">团体</a></li>
					<li><a href="javascript:void(0);" style="color: grey">云直播</a></li>
					<li><a href="javascript:void(0);" style="color: grey">数字非遗</a></li>
					<li><a href="${basePath}/reading/index">数字阅读</a></li>
					<li><a href="${basePath}/exhibitionhall/index">数字展馆</a></li>
					<li><a href="javascript:void(0);" style="color: grey">秀我风采</a></li>
				</ul>
			</div>
		</div>
		<div class="login">
            <div class="login-in">
                <a href="/login"></a>
            </div>
            <div class="register">
                <a href="${basePath}/toregist"></a>
            </div>
            <div class="search">
                <a href="${basePath}/search"></a>
            </div>
        </div>
		<div class="logon">
			<div class="logon-on">
			 <!--
			 <div class="user-nav">
                <ul>
                 <li><a href="/center/userInfo">用户中心</a></li>
                 <li><a href="${basePath }/sessionExit">账户退出</a></li>
                </ul>
             </div>
             -->
				<a href="/center/userInfo"></a>
			</div>
			<div class="search">
				<a href="${basePath}/search"></a>
			</div>
			<div class="email">
				<a href="${basePath }/sessionExit"></a>
			</div>

		</div>
	</div>
</div>