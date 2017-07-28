<%@ page language="java" contentType="text/html; charset=UTF-8" isELIgnored="false" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ include file="../../header.jsp" %>

<div class="row">
	<div class="col-lg-2">
		
	</div>
	<div class="col-lg-10">
		<form:form method="post" modelAttribute="editworkspacelayer" class="form-horizontal">
			<form:hidden path="id" value="${id}" />
			<form:hidden path="workspace" value="${workspace}" />
			<fieldset>
				<legend><spring:message code="layers.edit.title" arguments="${typeName}"/></legend>
				<form:errors path="snappable" cssClass="alert alert-danger" element="div" cssStyle="text-align: center;"/>
				<div class="form-group">
					<form:label path="snappable" class="col-md-4 control-label"><spring:message code="layers.field.snappable"/></form:label>
					<div class="col-md-4">
						<form:checkbox path="snappable" value="true" class="form-control" />
					</div>
				</div>
				<form:errors path="url" cssClass="alert alert-danger" element="div" cssStyle="text-align: center;"/>
				<div class="form-group">
					<form:label path="url" class="col-md-4 control-label"><spring:message code="layers.field.url"/></form:label>
					<div class="col-md-4">
						<form:input path="url" class="form-control input-md"/>
						<span class="help-block"><spring:message code="layers.field.url.help"/></span>  
					</div>
				</div>
				<form:errors path="typeName" cssClass="alert alert-danger" element="div" cssStyle="text-align: center;"/>
				<div class="form-group">
					<form:label path="typeName" class="col-md-4 control-label"><spring:message code="layers.field.typename"/></form:label>
					<div class="col-md-4">
						<form:textarea path="typeName" class="form-control" />
						<span class="help-block"><spring:message code="layers.field.typename.help"/></span>  
					</div>
				</div>
				<form:errors path="visible" cssClass="alert alert-danger" element="div" cssStyle="text-align: center;"/>
				<div class="form-group">
					<form:label path="visible" class="col-md-4 control-label"><spring:message code="layers.field.visible"/></form:label>
					<div class="col-md-4">
						<form:checkbox path="visible" value="true" class="form-control" />
					</div>
				</div>
			</fieldset>
			<div class="form-group">
				<div class="col-md-4">
					<c:url value="/workspaces/edit" var="backLink">
						<c:param name="id" value="${workspace}"/>
					</c:url>
					<a href="${backLink}" class="btn btn-danger pull-right">
						<span class="glyphicon glyphicon-chevron-left"></span> <spring:message code="buttons.back"/>
					</a>
				</div>
				<div class="col-md-4">
					<button id="sendform" name="save" class="btn btn-primary"><spring:message code="forms.buttons.send"/></button>
				</div>
			</div>
		</form:form>
	</div>
</div>

<%@ include file="../../footer.jsp" %>