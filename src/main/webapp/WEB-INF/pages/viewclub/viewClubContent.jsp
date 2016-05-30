<%@ page import="com.robynem.mit.web.util.ImageSize" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

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

    <%-- ACTIONS --%>
    <div class="row actions">

    </div>

    <%-- INFO --%>
    <div class="row info">

        <%-- COMPONENTS --%>
        <div class="col-md-3 col-xs-12">
            <div class="span12 infoTitle">
                <spring:message code="viewclub.info.title"></spring:message>
            </div>

            <div class="span12 infoList">


                <%-- CONTACTS --%>
                <c:if test="${not empty clubModel.emailContacts or not empty clubModel.phoneNumberContacts}">
                    <div class="span12 contactsList">

                        <span class="span12 contactsTitle">
                            <spring:message code="viewband.band-contacts"></spring:message>
                        </span>
                        <br>

                        <c:forEach var="contact" items="${clubModel.emailContacts}">
                            <span class="span12 contactItem">
                                    ${contact.value}
                            </span>
                            <br>
                        </c:forEach>

                        <c:forEach var="contact" items="${clubModel.phoneNumberContacts}">
                            <span class="span12 contactItem">
                                    ${contact.value}
                            </span>
                            <br>
                        </c:forEach>
                    </div>
                </c:if>

                <br>

                <%-- WEB SITE --%>
                <c:if test="${not empty clubModel.webSite}">
                    <div class="span12 contactsList">

                        <span class="span12 contactsTitle">
                            <spring:message code="viewclub.website.title"></spring:message>
                        </span>
                        <br>

                        <c:if test="${not fn:startsWith(fn:toLowerCase(clubModel.webSite), 'http://') and not fn:startsWith(fn:toLowerCase(clubModel.webSite), 'https://')}">
                            <c:set var="urlPrefix" value="http://"></c:set>
                        </c:if>

                        <a href="${urlPrefix}${clubModel.webSite}" class="webSite" target="_blank">${clubModel.webSite}</a>
                    </div>
                </c:if>

            </div>


        </div>

        <%-- BIOGRAPHY --%>
        <div class="col-md-9 col-xs-12">
            <div class="span12 descriptionTitle">
                <spring:message code="viewband.biography.title"></spring:message>
            </div>

            <div class="span12 descriptionContent">

                <pre class="descriptionContentPre">${clubModel.description}</pre>

            </div>
        </div>

    </div>

</div>

