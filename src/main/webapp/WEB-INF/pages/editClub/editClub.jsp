<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>



<html>
<head>
  <jsp:include page="../common/head-include.jsp"></jsp:include>

  <link rel="stylesheet" href="${contextPath}/resources/css/editClub.css?v=${softwareVersion}">
  <title>${siteTitle}</title>
</head>

<body>

<!-- MESSAGES START -->
<jsp:include page="../common/messagesDisplayer.jsp"></jsp:include>
<!-- MESSAGES END -->

<div id="container" class="container mainContainer pageContent">


  <!-- MENU START -->
  <jsp:include page="../common/menu.jsp">
    <jsp:param name="activeItem" value="menuCreateClub"></jsp:param>
  </jsp:include>
  <!-- MENU END -->


  <div class="row">
    <div class="col-md-3 col-xs-3 profileMenuContainer">
      <jsp:include page="../common/myMitMenu.jsp"></jsp:include>
    </div>
    <div class="col-md-9 col-xs-9 editClubContainer">
      <jsp:include page="editClubContent.jsp"></jsp:include>
    </div>
  </div>

</div>

</body>

</html>
