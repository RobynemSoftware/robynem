<%@ page import="com.robynem.mit.web.util.Constants" %>
<%@ page import="com.robynem.mit.web.util.NotificationType" %>
<%@ page import="com.robynem.mit.web.util.EntityStatus" %>
<%@ page import="com.robynem.mit.web.util.ImageSize" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="../common/messagesDisplayerAsync.jsp"></jsp:include>

<c:set var="BAND_INVITATION_TYPE">
    <%=NotificationType.BAND_INVITATION%>
</c:set>
<c:set var="ENTITY_STATUS_NOT_PUBLISHED">
    <%=EntityStatus.NOT_PUBLISHED%>
</c:set>
<c:set var="ENTITY_STATUS_PUBLISHED">
    <%=EntityStatus.PUBLISHED%>
</c:set>

 <%--Loops over notifications --%>
<c:forEach var="notification" items="${notificationModel.notifications}">

    <c:set var="isUnread" value="${empty notification.readDate ? true : false}"></c:set>
    <c:set var="rowClass" value="${isUnread eq true ? 'unreadNotification' : 'readNotification'}"></c:set>

    <div class="row notificationRow ${rowClass} row_id_${notification.id}">

        <%--<!-- Type logo -->--%>
        <div class="col-md-2">

            <c:choose>
                <%--<!-- BAND INVITATION -->--%>
                <c:when test="${notification.type.code eq BAND_INVITATION_TYPE}">
                    <img src="${contextPath}/resources/images/notification_band_invitation.jpg" class="img-responsive" />
                </c:when>
            </c:choose>

        </div>

        <%--<!-- Description -->--%>
        <div class="col-md-8 notificationDescription">

            <div class="row notificationContent" id="${notification.id}">
                <c:choose>
                    <%--<!-- BAND INVITATION -->--%>
                    <c:when test="${notification.type.code eq BAND_INVITATION_TYPE}">

                        <%-- Sender name --%>
                        <c:set var="senderName" value="${notification.senderUser.firstName} ${notification.senderUser.lastName}"></c:set>

                        <%-- Band name --%>
                        <c:choose>
                            <c:when test="${notification.band.status.code eq ENTITY_STATUS_NOT_PUBLISHED}">
                                <%-- Takes stage at last position (actually we have just one) --%>
                                <c:set var="bandName" value="${notification.band.stageVersions[fn:length(notification.band.stageVersions) - 1].name}"></c:set>
                            </c:when>
                            <c:otherwise>
                                <c:set var="bandName" value="${notification.band.name}"></c:set>
                            </c:otherwise>
                        </c:choose>


                        <spring:message code="dashbord.notifications.band-invitation.description"
                                        arguments="${senderName};${bandName}"
                                        argumentSeparator=";"
                                ></spring:message>
                    </c:when>
                </c:choose>
            </div>

            <%-- Notification Options --%>
            <div class="row notificationDescription">

                <div class="col-md-6">
                    <fmt:formatDate value="${notification.created}" type="both"  ></fmt:formatDate>
                </div>

            </div>

        </div>

        <%--<!-- Subject logo -->--%>
        <div class="col-md-2">
            <c:choose>
                <%--<!-- BAND INVITATION -->--%>
                <c:when test="${notification.type.code eq BAND_INVITATION_TYPE}">
                    <%-- If it's not published, gets stage loge, otherwise gets published one --%>
                    <c:choose>
                        <c:when test="${notification.band.status.code eq ENTITY_STATUS_NOT_PUBLISHED}">
                            <%-- Takes stage at last position (actually we have just one) --%>
                            <c:set var="imageId" value="${notification.band.stageVersions[fn:length(notification.band.stageVersions) - 1].bandLogo.id}"></c:set>
                        </c:when>
                        <c:otherwise>
                            <c:set var="imageId" value="${notification.band.bandLogo.id}"></c:set>
                        </c:otherwise>
                    </c:choose>
                </c:when>
            </c:choose>

            <%--<!-- Evaluate imageId -->--%>
            <c:choose>
                <c:when test="${not empty imageId}">
                    <img src="${contextPath}/media/getImage?imageId=${imageId}&size=<%=ImageSize.SMALL.toString()%>" class="img-responsive" />
                </c:when>
                <c:otherwise>
                    <img src="${contextPath}/resources/images/profile_avatar_50x50.png" class="img-responsive" />
                </c:otherwise>
            </c:choose>
        </div>

    </div>
</c:forEach>

<%-- PAGINATION --%>
<c:if test="${notificationModel.nextRows gt 0}">

    <a id="showNextNotifications" href="javascript:loadNotifications(${notificationModel.currentPage + 1})"><span><spring:message code="dashbord.notifications.pagination-show-next"></spring:message></span></a>

</c:if>

<script type="text/javascript">
    $(function () {
        initNotificationReadTimeout();
    });
</script>