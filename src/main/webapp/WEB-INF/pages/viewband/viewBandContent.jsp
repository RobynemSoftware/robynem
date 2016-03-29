<%@ page import="com.robynem.mit.web.util.ImageSize" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" href="${contextPath}/resources/css/viewBand.css?v=${softwareVersion}">

<jsp:include page="../common/messagesDisplayerAsync.jsp"></jsp:include>

<div class="viewBand col-md-12">





        <div class="row header" >

                <%-- LOGO --%>
                <div class="col-md-3 col-xs-12">

                    <c:choose>

                        <c:when test="${not empty bandModel.logoImageId}">
                            <img src="${contextPath}/media/getImage?imageId=${bandModel.logoImageId}&size=<%=ImageSize.LARGE%>" class="img-responsive" />
                        </c:when>
                        <c:otherwise>
                            <img src="${contextPath}/resources/images/profile_avatar.png" class="img-responsive"/>
                        </c:otherwise>

                    </c:choose>

                </div>

                <%-- HEADER --%>
                <div class="col-md-6 col-xs-12 headerDescription">

                    <div class="row headerRow">
                        <span class="headerText">${bandModel.name}</span>
                    </div>

                    <div class="row headerRow">
                    <span class="headerText musicGenre">

                        <c:forEach var="musicGenre" items="${bandModel.genres}" varStatus="status">

                            ${musicGenre}

                            <c:if test="${not status.last}">
                                &nbsp;|&nbsp;
                            </c:if>

                        </c:forEach>

                    </span>
                    </div>

                    <div class="row headerRow">
                        <span class="headerText town">${bandModel.town}</span>
                    </div>

                </div>





        </div>



</div>