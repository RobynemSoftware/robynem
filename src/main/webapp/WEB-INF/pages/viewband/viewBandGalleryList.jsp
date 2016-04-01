<%@ page import="com.robynem.mit.web.util.ImageSize" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<jsp:include page="../common/messagesDisplayerAsync.jsp"></jsp:include>

<c:choose>
    <c:when test="${not empty galleryModel.images}">
        <c:forEach var="image" items="${galleryModel.images}">
            <div id="image_${image.id}" class="col-md-4 videoColumn">

                <img src="${contextPath}/media/getImage?imageId=${image.id}&size=<%=ImageSize.MEDIUM.toString()%>" class="img-responsive">

            </div>


        </c:forEach>
    </c:when>

    <c:otherwise>
        <div class="col-md-12 noVideos">
            <spring:message code="viewband.gallery.empty"></spring:message>
        </div>

    </c:otherwise>
</c:choose>

<%-- PAGINATION --%>
<c:if test="${galleryModel.nextRows gt 0}">

    <div id="showNextGallery" class="col-md-12">
        <a  href="javascript:loadGallery(${galleryModel.currentPage + 1})"><span><spring:message code="viewband.gallery.show-next"></spring:message></span></a>
    </div>


</c:if>