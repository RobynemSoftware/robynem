<%@ page import="com.robynem.mit.web.util.ImageSize" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<jsp:include page="../common/messagesDisplayerAsync.jsp"></jsp:include>

<c:choose>
    <c:when test="${not empty galleryModel.images}">
        <c:forEach var="image" items="${galleryModel.images}">
            <div id="image_${image.id}" class="col-md-3 videoColumn">

                <a href="#photoViewer" onclick="javarscript:return showOriginalPhoto(${image.id});" data-toggle="modal">
                    <img src="${contextPath}/media/getImage?imageId=${image.id}&size=<%=ImageSize.MEDIUM.toString()%>" class="img-responsive">
                </a>


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


<%-- PHOTO VIEWER --%>
<div class="modal fade" id="photoViewer" role="dialog" aria-labelledby="photoViewerTitle" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <!--
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    ×
                </button>

                <h4 class="modal-title" id="photoViewerTitle">
                    Modal title
                </h4>
                -->
            </div>
            <div class="modal-body">
                <img id="photoViewerImg" src="" class="img-responsive">
            </div>
            <div class="modal-footer">

                <button type="button"  data-dismiss="modal">
                    <spring:message code="global.close"></spring:message>
                </button>
            </div>
        </div>

    </div>

</div>

<script type="text/javascript">
    function showOriginalPhoto(imageId) {
        $("#photoViewerImg").attr("src", "${contextPath}/media/getImage?imageId=" + imageId + "&size=<%=ImageSize.ORIGINAL%>");

        return true;
    }
</script>