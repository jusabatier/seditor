<%@ page language="java" contentType="text/json; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
{
	"workspace": "${wsKey}",
	"user": "${username}",
	"accessLevel": ${accessLevel}, 
	"projection": {
		"code": "EPSG:${SRS}",
		"def": "${SRSdef}"
	},
	"center": {
		"x": ${CenterX},
		"y": ${CenterY},
		"zoom": ${CenterZ}
	},
	"extent": {
		"xmin": ${MExtentXmin},
		"ymin": ${MExtentYmin},
		"xmax": ${MExtentXmax},
		"ymax": ${MExtentYmax}
	},
	"featureType": "${featureType}",
	"layers": [
		<c:forEach items="${layers}" var="layer" varStatus="status">
		{
			"snappable": <c:if test="${layer.snappable}">true</c:if><c:if test="${not layer.snappable}">false</c:if>,
			"url": "<c:out value="${layer.url}"/>",
			"typename": "<c:out value="${layer.typeName}"/>",
			"visible": <c:out value="${layer.visible}" />
		}<c:if test="${not status.last}">,</c:if>
		</c:forEach>
	],
	"attributes": [
		<c:forEach items="${attributes}" var="attribute" varStatus="status">
		{
			<c:if test="${not empty attribute.values}">"datasource": ${attribute.values},</c:if>
			"name": "<c:out value="${attribute.name}"/>",
			"type": "<c:out value="${attribute.type}"/>",
			"label": "<c:out value="${attribute.label}"/>",
			"required": "<c:if test="${attribute.required}">true</c:if><c:if test="${not attribute.required}">false</c:if>"
		}<c:if test="${not status.last}">,</c:if>
		</c:forEach>
	]
}
