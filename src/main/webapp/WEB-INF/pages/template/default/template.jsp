<%--
  Created by IntelliJ IDEA.
  User: rrennaloiacono
  Date: 25/05/2016
  Time: 14:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- Declare all variables useful into page context. (such as  redirectView, activeItem, ...) --%>
<tiles:insertAttribute name="contextVariables"></tiles:insertAttribute>

<html>
<head>
  <jsp:include page="../../common/head-include.jsp"></jsp:include>

    <tiles:insertAttribute name="headDeclarations"></tiles:insertAttribute>
  <title>${siteTitle}</title>
</head>

<header>
    <tiles:insertAttribute name="header"></tiles:insertAttribute>
</header>

<body>
<!-- MESSAGES START -->
<jsp:include page="../../common/messagesDisplayer.jsp"></jsp:include>
<!-- MESSAGES END -->

<div id="container" class="container mainContainer pageContent">



    <tiles:insertAttribute name="menu"></tiles:insertAttribute>

    <tiles:insertAttribute name="body"></tiles:insertAttribute>

</div>
</body>

<footer>
    <tiles:insertAttribute name="footer"></tiles:insertAttribute>
</footer>

