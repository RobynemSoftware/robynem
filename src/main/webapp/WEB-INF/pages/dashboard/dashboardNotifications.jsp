<%@ page import="com.robynem.mit.web.util.Constants" %>
<%@ page import="com.robynem.mit.web.util.NotificationType" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../common/messagesDisplayerAsync.jsp"></jsp:include>

<div class="row">

    <div class="col-md-12">

        <div class="accordion" id="dashboardNotificationAccordion">

            <!-- UNREAD -->
            <div class="accordion-group">

                <div class="accordion-heading">
                    <a class="accordion-toggle" data-toggle="collapse" data-parent="#dashboardNotificationAccordion" href="#collapseUnreadNotifications">
                        <span><spring:message code="dashbord.notifications.unread.title" arguments="${fn:length(notifications.unreadNotifications)}"></spring:message></span>
                    </a>
                </div>

                <div id="collapseUnreadNotifications" class="accordion-body collapse in">
                    <div id="notificationsContainer" class="accordion-inner">
                        <c:set var="BAND_INVITATION_TYPE">
                            <%=NotificationType.BAND_INVITATION.toString()%>
                        </c:set>

                        <!-- Loops over unread notifications -->
                        <c:forEach var="notification" items="${notifications.unreadNotifications}">

                            <div class="row">

                                <c:choose>

                                    <!-- BAND INVITATION -->
                                    <c:when test="${notification.type.code eq BAND_INVITATION_TYPE}">

                                        <!-- Type logo -->
                                        <div class="col-md-3">

                                        </div>

                                    </c:when>

                                </c:choose>

                            </div>








                        </c:forEach>


                    </div>

                </div>
            </div>



        </div>

    </div>

</div>