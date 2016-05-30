<%@ page import="com.robynem.mit.web.util.ImageSize" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<link rel="stylesheet" href="${contextPath}/resources/css/viewClub.css?v=${softwareVersion}">

<jsp:include page="../common/messagesDisplayerAsync.jsp"></jsp:include>

<div class="viewClub col-md-12">
    <%-- HEADER --%>
    <div class="row header">

        <%-- LOGO --%>
        <div class="col-md-3 col-xs-12">

            <c:choose>

                <c:when test="${not empty clubModel.logoImageId}">
                    <img src="${contextPath}/media/getImage?imageId=${clubModel.logoImageId}&size=<%=ImageSize.LARGE%>"
                         class="img-responsive"/>
                </c:when>
                <c:otherwise>
                    <img src="${contextPath}/resources/images/profile_avatar.png" class="img-responsive"/>
                </c:otherwise>

            </c:choose>

        </div>

        <%-- NAME --%>
        <div class="col-md-6 col-xs-10 headerDescription">

            <div class="row headerRow">
                <span class="headerText">${clubModel.name}</span>
            </div>

            <div class="row headerRow">
                    <span class="headerText clubGenre">

                        <c:forEach var="clubGenre" items="${clubModel.genres}" varStatus="status">

                            ${clubGenre}

                            <c:if test="${not status.last}">
                                &nbsp;|&nbsp;
                            </c:if>

                        </c:forEach>

                    </span>
            </div>

            <div class="row headerRow">
                <span class="headerText address">${clubModel.address}</span>
            </div>

            <c:if test="${clubModel.owner eq true or clubModel.admin eq true}">
                <div class="row headerRow">
                    <a class="edit" href="${contextPath}/private/editClub/edit?clubId=${clubModel.id}"><spring:message code="viewband.edit"></spring:message></a>
                </div>
            </c:if>


        </div>

        <div class="col-md-2 col-xs-2 forDesktop">
            <img src="${contextPath}/resources/images/mit-logo_160x160.png" class="img-thumbnail"/>
        </div>

    </div>
</div>

