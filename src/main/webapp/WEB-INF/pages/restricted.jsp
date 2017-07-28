<%@ page language="java" contentType="text/html; charset=UTF-8" isELIgnored="false" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ include file="header.jsp" %>
<div class="row">
	<div class="col-lg-2">
		
	</div>
	<div class="col-lg-10">
		<h2><spring:message code="restricted.title"/></h2>
		<p><spring:message code="restricted.message"/></p>
	</div>
</div>

<%@ include file="footer.jsp" %>