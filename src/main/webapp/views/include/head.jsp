<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="SHORTCUT ICON" href="/static/photo/favicon.ico">
<!-- Bootstrap core CSS -->
<link href="/static/bootstrap/bootstrap.min.css" rel="stylesheet">
<!-- Bootstrap theme -->
<link href="/static/bootstrap/bootstrap-theme.min.css" rel="stylesheet">
<!-- Custom styles for this template -->
<link href="/static/bootstrap/theme.css" rel="stylesheet">
<title>用户系统...</title>
</head>
<body role="document">
	<!-- Fixed navbar -->
	<nav class="navbar navbar-default navbar-fixed-top">
	<div class="container">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed"
				data-toggle="collapse" data-target="#navbar" aria-expanded="false"
				aria-controls="navbar">
				<span class="sr-only">Toggle navigation</span> <span
					class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
			<span class="navbar-brand"></span>
		</div>
		<div id="navbar" class="navbar-collapse collapse">
			<ul class="nav navbar-nav">
				<li><a href="/jeadmin/">首页</a></li>
			</ul>
			<div class="navbar-form navbar-right">
				<c:choose>
					<c:when test="${sessionScope.pritk != null}">
						<a href="#" class="dropdown-toggle btn btn-success"
							data-toggle="dropdown" role="button" aria-haspopup="true"
							aria-expanded="false">你好，${sessionScope.username}</a>
						<ul class="dropdown-menu">
							<li><a href="/jeadmin/space">个人信息</a></li>
							<li><a href="/jeadmin/logout">注销</a></li>
						</ul>
					</c:when>
					<c:otherwise>
						<a class="btn btn-success" href="/jeadmin/login">登录</a>
					</c:otherwise>
				</c:choose>
				<a class="btn btn-default" href="/jeadmin/register">注册</a>
			</div>
		</div>
		<!--/.nav-collapse -->
	</div>
	</nav>