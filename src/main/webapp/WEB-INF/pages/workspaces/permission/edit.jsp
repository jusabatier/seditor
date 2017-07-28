<%@ page language="java" contentType="text/html; charset=UTF-8" isELIgnored="false" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ include file="../../header.jsp" %>

<div class="row">
	<div class="col-lg-2">
		
	</div>
	<div class="col-lg-10">
		<form:form method="post" modelAttribute="editworkspacepermission" class="form-horizontal">
			<form:hidden path="id" value="${id}" />
			<form:hidden path="workspace" value="${workspace}" />
			<fieldset>
				<legend><spring:message code="permissions.edit.title"/></legend>
				<form:errors path="roles" cssClass="alert alert-danger" element="div" cssStyle="text-align: center;"/>
				<div class="form-group">
					<form:label path="roles" class="col-md-4 control-label"><spring:message code="permissions.field.roles"/></form:label>
					<div class="col-md-4">
						<form:select multiple="true" path="roles" class="form-control">
							<form:option value="ALL"><spring:message code="permissions.field.roles.anyone"/></form:option>
							<form:option value="*"><spring:message code="permissions.field.roles.anyrole"/></form:option>
							<form:options items="${listRoles}" itemValue="name" itemLabel="name" />
						</form:select>
						<span class="help-block"><spring:message code="permissions.field.roles.help"/></span> 
					</div>
				</div>
				<form:errors path="access" cssClass="alert alert-danger" element="div" cssStyle="text-align: center;"/>
				<div class="form-group">
					<form:label path="access" class="col-md-4 control-label"><spring:message code="permissions.field.access"/></form:label>
					<div class="col-md-4">
						<form:select path="access" class="form-control">
							<form:option value="1"><spring:message code="permissions.field.access.1"/></form:option>
							<form:option value="2"><spring:message code="permissions.field.access.2"/></form:option>
							<form:option value="3"><spring:message code="permissions.field.access.3"/></form:option>
						</form:select>
						<span class="help-block"><spring:message code="permissions.field.roles.help"/></span>  
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