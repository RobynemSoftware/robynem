<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<jsp:include page="../common/messagesDisplayerAsync.jsp"></jsp:include>



<c:choose>
    <c:when test="${not empty audioModel.audios}">

        <c:forEach var="audio" items="${audioModel.audios}">
            <div id="audio_${audio.id}" class="col-md-6 col-xs-12 videoColumn">
            </div>

            <c:set var="audioContainer">
                $("#audio_${audio.id}")
            </c:set>
            <script type="text/javascript">
                SC.oEmbed('${audio.soundCloudUrl}', { auto_play: false, maxheight:80}).then(function(oEmbed) {
                    ${audioContainer}.html(oEmbed.html);
                });
            </script>
        </c:forEach>

    </c:when>

    <c:otherwise>
        <div class="col-md-12 noVideos">
            <spring:message code="viewband.audios.empty"></spring:message>
        </div>

    </c:otherwise>
</c:choose>



<%-- PAGINATION --%>
<c:if test="${audioModel.nextRows gt 0}">

    <div id="showNextAudios" class="col-md-12">
        <a  href="javascript:loadAudios(${audioModel.currentPage + 1})"><span><spring:message code="viewband.audios.show-next"></spring:message></span></a>
    </div>


</c:if>