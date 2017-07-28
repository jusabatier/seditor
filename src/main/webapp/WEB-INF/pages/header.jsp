<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta charset="utf-8">
    	<title>sEditor - A simple editor for your maps !</title>
    	<meta name="description" content="sEditor - A simple editor for your maps !">
    	<meta name="author" content="Julien Sabatier">
    	<link rel="stylesheet" href="<c:url value="/resources/css/bootstrap/bootstrap.min.css"/>">
    	<link rel="stylesheet" href="<c:url value="/resources/css/bootstrap/bootstrap-theme.min.css"/>">
    	<script type="text/javascript" src="<c:url value="/resources/scripts/lib/jquery/jquery.min.js"/>"></script>
    	<script type="text/javascript" src="<c:url value="/resources/scripts/lib/bootstrap/bootstrap.min.js"/>"></script>
    	<script type="text/javascript" src="<c:url value="/resources/scripts/lib/bootstrap/bootbox.min.js"/>"></script>
    	<script type="text/javascript" src="<c:url value="/resources/scripts/script-admin.js"/>"></script>
	</head>
	<body>
		<div class="container-fluid">
			<div class="page-header" style="margin-top: 0;">
        		<iframe src="/header/?active=seditor" style="width:100%;height:80px;border:none;overflow:hidden;" scrolling="no" frameborder="0"></iframe>
				<h1>sEditor</h1>
			</div>