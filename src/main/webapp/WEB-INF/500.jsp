<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<% String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath(); request.setAttribute("basePath", basePath); %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv=”X-UA-Compatible” content=”IE=edge,chrome=1″ />
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="keywords" content="">
<meta name="author" content="">
<title>台州市文化馆-500</title>
<link href="${basePath }/static/assets/css/public/reset.css" rel="stylesheet">
<!--[if lt IE 9] >
<script src="${basePath }/static/assets/js/plugins/ie/IE9.js"></script> 
<! [endif]]-->
</head>
<body class="err-body">
	<div class="public-err err-500">
    	<h1>服务器遇到错误，无法完成请求</h1>
        <p><a href="${basePath }/">返回首页</a></p>
    </div>
</body>
</html>
