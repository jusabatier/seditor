<%@ page language="java" contentType="text/html; charset=UTF-8" isELIgnored="false" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ include file="../header.jsp" %>
<div class="row">
	<div class="col-lg-2">
		<div class="row">
			<div class="col-lg-offset-1 col-lg-10">
				<a href="<c:url value="/workspaces/add"/>" class="btn btn-default btn-block"><span class="glyphicon glyphicon-plus"></span> <spring:message code="workspaces.list.new"/></a>
			</div>
		</div>
	</div>
	<div class="col-lg-10">
		<table class="table table-striped table-condensed">
			<caption>
				<h4><spring:message code="workspaces.list.title"/></h4>
			</caption>
			<thead>
				<tr>
					<th><spring:message code="workspaces.fields.health"/></th>
					<th>Id</th>
					<th><spring:message code="workspaces.fields.key"/></th>
					<th><spring:message code="workspaces.fields.tableName"/></th>
					<th><spring:message code="workspaces.fields.featureType"/></th>
					<th><spring:message code="tablelist.columns.actions"/></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${listeWorkspaces}" var="workspace">
					<tr>
						<td></td>
						<td><c:out value="${workspace.id}"/></td>
						<td><c:out value="${workspace.key}"/></td>
						<td><c:out value="${workspace.tableName}"/></td>
						<td><c:out value="${workspace.featureType}"/></td>
						<td>
							<c:url value="/workspaces/edit" var="editLink">
								<c:param name="id" value="${workspace.id}"/>
							</c:url>
							<a href="${editLink}" class="btn btn-info btn-sm">
								<span class="glyphicon glyphicon-pencil"></span> <spring:message code="tablelist.buttons.edit"/>
							</a>
							<c:url value="/workspaces/delete" var="deleteLink">
								<c:param name="id" value="${workspace.id}"/>
							</c:url>
							<a href="#" onclick='confirmDeleteWorkspace("<spring:message code="workspaces.list.confirmdelete" arguments="${workspace.key}"/>","${deleteLink}");' class="btn btn-danger btn-sm">
								<span class="glyphicon glyphicon-remove"></span> <spring:message code="tablelist.buttons.delete"/>
							</a>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</div>

<%@ include file="../footer.jsp" %>