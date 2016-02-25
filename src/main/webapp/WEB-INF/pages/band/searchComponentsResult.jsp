<%@ page import="com.robynem.mit.web.util.ImageSize" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:choose>

    <c:when test="${empty searchResult or fn:length(searchResult) eq 0}">

        <div class="row fatal">
            <div id="searchResultEmpty" class="col-xs-12">
                <span><spring:message code="band.component.search.result-empty"></spring:message> </span>
            </div>
        </div>

    </c:when>

    <c:otherwise>


        <c:set var="counter" value="0"></c:set>
            <c:forEach var="item" items="${searchResult}">
                <c:set var="counter" value="${counter + 1}"></c:set>

                <c:if test="${counter eq 1}">
                    <div class="row line">
                </c:if>

                        <div class="col-md-4 col-xs-12">

                            <div class="row">

                                <div class="col-md-12">
                                    <c:choose>
                                        <c:when test="${not empty item.profileImage}">
                                            <img src="${contextPath}/media/getImage?imageId=${item.profileImage.id}&size=<%=ImageSize.MEDIUM.toString()%>" class="img-responsive"/>
                                        </c:when>

                                        <c:otherwise>
                                            <img src="${contextPath}/resources/images/profile_avatar.png" class="img-responsive" />
                                        </c:otherwise>
                                    </c:choose>
                                </div>

                                <!-- Name -->
                                <div class="col-md-12">
                                    <span class="searchResultName">
                                        ${item.firstName} &nbsp; ${item.lastName}
                                    </span>
                                </div>

                                <!-- Actions -->
                                <div class="col-md-12">

                                    <div class="row">

                                        <div class="col-md-6 col-xs-6 showDetails">
                                            <a href="javascript:showMusicianDetails(${item.id});">
                                                <img src="${contextPath}/resources/images/show_details_32x32.png" title='<spring:message code="band.component.search-component.show-details"></spring:message>'>
                                            </a>
                                        </div>

                                        <div class="col-md-6 col-xs-6 sendMessage">
                                            <a href="#">
                                                <img src="${contextPath}/resources/images/message_32x32.png" title='<spring:message code="band.component.search-component.send-message"></spring:message>'>
                                            </a>
                                        </div>

                                        <div style="clear: both"></div>

                                    </div>

                                </div>
                            </div>

                        </div>

                <c:if test="${counter eq 3}">
                    </div>
                    <c:set var="counter" value="0"></c:set>
                </c:if>
            </c:forEach>

    </c:otherwise>
</c:choose>

<!-- Musician Info Dialog -->
<div id="musicianInfoDialog">

</div>

<script type="text/javascript">
    $(function() {

        $("#musicianInfoDialog").dialog({
            modal: true,
            height: 'auto',
            width: 'auto',
            autoOpen: false,
            position: {my: "center top", at: "center top"},
            closeText: 'X',
            buttons: [
                {
                    text: "<spring:message code="global.close"></spring:message>",
                    /*icons: {
                     primary: "ui-icon-heart"
                     },*/
                    click: function () {
                        $(this).dialog("close");
                    }

                    // Uncommenting the following line would hide the text,
                    // resulting in the label being used as a tooltip
                    //showText: false
                }
            ]
        });

    });

    function showMusicianDetails(userId) {

        execInSession(function() {
            $.ajax({
                url : "${contextPath}/private/editBand/showMusicianDetails",
                type : "get",
                dataType : "html",
                data : {
                    userId : userId
                },
                success : function (data) {
                    if (!showApplicationMessages(data)) {
                        $("#musicianInfoDialog").html(data);
                        $("#musicianInfoDialog").dialog("open");
                    }
                }
            });
        })

    }
</script>