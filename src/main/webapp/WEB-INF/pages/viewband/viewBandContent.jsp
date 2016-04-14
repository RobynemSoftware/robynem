<%@ page import="com.robynem.mit.web.util.ImageSize" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<link rel="stylesheet" href="${contextPath}/resources/css/viewBand.css?v=${softwareVersion}">

<jsp:include page="../common/messagesDisplayerAsync.jsp"></jsp:include>

<div class="viewBand col-md-12">


    <%-- HEADER --%>
    <div class="row header">

        <%-- LOGO --%>
        <div class="col-md-3 col-xs-12">

            <c:choose>

                <c:when test="${not empty bandModel.logoImageId}">
                    <img src="${contextPath}/media/getImage?imageId=${bandModel.logoImageId}&size=<%=ImageSize.LARGE%>"
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
            <div class="span12 componentsTitle">
                <spring:message code="viewband.components.title"></spring:message>
            </div>

            <div class="span12 componentsList">
                <c:forEach var="component" items="${bandModel.components}">

                    <span class="componentName">${component.name}</span>

                    &nbsp;

                        <span class="componentInstrument">

                            <c:if test="${component.singerSelected eq true}">
                                <spring:message code="profile.musician-form.singer"></spring:message>
                                <c:if test="${component.discJockeySelected eq true or not empty component.instruments}">
                                    ,&nbsp;
                                </c:if>
                            </c:if>

                        <c:if test="${component.discJockeySelected eq true}">
                            <spring:message code="profile.musician-form.dj"></spring:message>
                            <c:if test="${not empty component.instruments}">
                                ,&nbsp;
                            </c:if>
                        </c:if>

                        <c:forEach var="instr" items="${component.instruments}" varStatus="status">
                            ${instr.name}
                            <c:if test="${not status.last}">
                                ,&nbsp;
                            </c:if>
                        </c:forEach>

                        </span>

                    <br>
                </c:forEach>

                <%-- CONTACTS --%>
                <c:if test="${not empty bandModel.emailContacts or not empty bandModel.phoneNumberContacts}">
                    <div class="span12 contactsList">

                            <span class="span12 contactsTitle">
                                <spring:message code="viewband.band-contacts"></spring:message>
                            </span>
                        <br>

                        <c:forEach var="contact" items="${bandModel.emailContacts}">
                                <span class="span12 contactItem">
                                        ${contact.value}
                                </span>
                            <br>
                        </c:forEach>

                        <c:forEach var="contact" items="${bandModel.phoneNumberContacts}">
                                <span class="span12 contactItem">
                                        ${contact.value}
                                </span>
                            <br>
                        </c:forEach>
                    </div>
                </c:if>

            </div>


        </div>

        <%-- BIOGRAPHY --%>
        <div class="col-md-9 col-xs-12">
            <div class="span12 biographyTitle">
                <spring:message code="viewband.biography.title"></spring:message>
            </div>

            <div class="span12 biographyContent">

                <pre class="biographyContentPre">${bandModel.biography}</pre>

            </div>
        </div>

    </div>

    <%-- VIDEOS --%>
    <div class="row videos">

        <div class="col-md-12 videosTitle">
            <span class="span12">
                <spring:message code="band.media.videos"></spring:message>
            </span>
        </div>

        <div class="col-md-12">

            <div id="videosContainer" class="row">

            </div>

        </div>

    </div>

    <%-- GALLERY --%>
    <div class="row gallery">

        <div class="col-md-12 galleryTitle">
            <span class="span12">
                <spring:message code="band.media.images"></spring:message>
            </span>
        </div>

        <div class="col-md-12">

            <div id="galleryContainer" class="row">

            </div>

        </div>

    </div>

        <%-- AUDIO --%>
        <div class="row audio">

            <div class="col-md-12 audioTitle">
            <span class="span12">
                <spring:message code="band.media.audio"></spring:message>
            </span>
            </div>

            <div class="col-md-12">

                <div id="audioContainer" class="row">

                </div>

            </div>

        </div>


</div>

<script type="text/javascript">

    $(function () {
        loadVideos(1);

        loadGallery(1);

        loadAudios(1);

        loadActions();
    })

    function loadVideos(currentPage) {
        $.ajax({
            url: "${contextPath}/viewBand/getVideos",
            data: {
                currentPage: currentPage
            },
            type: "get",
            dataType: "html",
            async: true,
            cache: false,
            success: function (data) {
                $("#showNextVideos").remove();
                $("#videosContainer").append(data);
            }
        });
    }

    function loadGallery(currentPage) {
        $.ajax({
            url: "${contextPath}/viewBand/getGallery",
            data: {
                currentPage: currentPage
            },
            type: "get",
            dataType: "html",
            async: true,
            cache: false,
            success: function (data) {
                $("#showNextGallery").remove();
                $("#galleryContainer").append(data);
            }
        });
    }

    function loadAudios(currentPage) {
        $.ajax({
            url: "${contextPath}/viewBand/getAudios",
            data: {
                currentPage: currentPage
            },
            type: "get",
            dataType: "html",
            async: true,
            cache: false,
            success: function (data) {
                $("#showNextAudios").remove();
                $("#audioContainer").append(data);
            }
        });
    }

    function loadActions() {
        $.ajax({
            url: "${contextPath}/viewBand/getActions",
            type: "get",
            dataType: "html",
            async: true,
            cache: false,
            success: function (data) {
                $(".viewBand .actions").html(data);
            }
        });
    }

    function acceptDeclineComponentRequest(accept) {
        $.ajax({
            url: "${contextPath}/viewBand/acceptDeclineComponentRequest",
            type: "get",
            data : {
                accept : accept
            },
            dataType: "html",
            async: true,
            cache: false,
            success: function (data) {
                $("#viewBandContainer").html(data);
            }
        });
    }
</script>