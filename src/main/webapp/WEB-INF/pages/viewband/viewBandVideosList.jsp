<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<jsp:include page="../common/messagesDisplayerAsync.jsp"></jsp:include>



<c:choose>
    <c:when test="${not empty videosModel.videos}">

        <c:forEach var="video" items="${videosModel.videos}">
            <div id="video_${video.id}" class="col-md-4 videoColumn">
            </div>

            <c:set var="videoContainer">
                $("#video_${video.id}")
            </c:set>
            <script type="text/javascript">
                addYoutubeVideo("${video.youtubeUrl}", ${videoContainer}, {
                    videoId : "${video.id}",
                    readOnly : true
                });
            </script>
        </c:forEach>

    </c:when>

    <c:otherwise>
        <div class="col-md-12 noVideos">
            <spring:message code="viewband.videos.empty"></spring:message>
        </div>

    </c:otherwise>
</c:choose>



<%-- PAGINATION --%>
<c:if test="${videosModel.nextRows gt 0}">

    <div id="showNextVideos" class="col-md-12">
        <a  href="javascript:loadVideos(${videosModel.currentPage + 1})"><span><spring:message code="viewband.videos.show-next"></spring:message></span></a>
    </div>


</c:if>