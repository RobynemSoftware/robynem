<%@ page import="com.robynem.mit.web.model.viewband.BandRequestModel" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<jsp:include page="../common/messagesDisplayerAsync.jsp"></jsp:include>

<%-- VOTE --%>
<div class="col-md-3 col-xs-12">

    <span class="vote">
        <spring:message code="viewband.vote"></spring:message>
    </span>

&nbsp;

    <span>
        <img id="vote_1" class="voteLink" src="${contextPath}/resources/images/star-empty.png">
        &nbsp;
        <img id="vote_2" class="voteLink" src="${contextPath}/resources/images/star-empty.png">
        &nbsp;
        <img id="vote_3" class="voteLink" src="${contextPath}/resources/images/star-empty.png">
        &nbsp;
        <img id="vote_4" class="voteLink" src="${contextPath}/resources/images/star-empty.png">
        &nbsp;
        <img id="vote_5" class="voteLink" src="${contextPath}/resources/images/star-empty.png">
    </span>


</div>


<div class="col-md-9 col-xs-12">
    <%-- FAVOURITES --%>
    <button><spring:message code="viewband.add-favorite"></spring:message></button>

    &nbsp;

    <%-- CONTACT BAND --%>
    <button><spring:message code="viewband.contact-band"></spring:message></button>

    <%-- COMPONENT INVITATION --%>
    <c:if test="${not empty bandRequests}">
        <div class="dropdown" id="requestDDContent" >
            <button class="btn btn-primary dropdown-toggle" type="button" data-toggle="dropdown" id="ddRequests"><spring:message code="viewband.requests"></spring:message>
                <span class="caret"></span></button>
            <ul class="dropdown-menu dropdown-menu-right" role="menu" aria-labelledby="ddRequests">

                <c:set var="COMPONENT_INVITATION_TYPE">
                    <%=BandRequestModel.COMPONENT_INVITATION%>
                </c:set>

                <c:forEach var="bandRequest" items="${bandRequests}">
                    <c:choose>
                        <c:when test="${bandRequest.type eq COMPONENT_INVITATION_TYPE}">
                            <li class="dropdown-header" role="presentation"><spring:message code="viewband.requests.component"></spring:message> </li>
                            <li role="presentation"><a href="javascript:acceptDeclineComponentRequest(true);" role="menuitem"><spring:message code="viewband.requests.accept"></spring:message> </a></li>
                            <li role="presentation"><a href="javascript:acceptDeclineComponentRequest(false);" role="menuitem"><spring:message code="viewband.requests.decline"></spring:message></a></li>
                        </c:when>
                    </c:choose>
                </c:forEach>


            </ul>
        </div>

        <div style="clear: both;"></div>
    </c:if>
</div>