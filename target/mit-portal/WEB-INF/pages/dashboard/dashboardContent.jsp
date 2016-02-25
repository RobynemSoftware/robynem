<%@ page import="com.robynem.mit.web.util.ImageSize" %>
<%@ page import="com.robynem.mit.web.util.PortalHelper" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>




<div class="row mainContent">

    <div class="col-md-12">

        <!-- OWNED BANDS -->

        <div class="row ">

            <div class="col-md-12 sectionTitle">
                <span ><spring:message code="dashbord.owned-bands.title"></spring:message> </span>
            </div>

            <c:choose>
                <c:when test="${not empty ownedBands}">

                        <c:set var="counter" value="0"></c:set>
                        <c:forEach var="band" items="${ownedBands}" varStatus="status">

                            <c:set var="counter" value="${counter + 1}"></c:set>

                            <c:if test="${counter eq 1}">
                                <div class="row line">
                            </c:if>

                            <c:set var="bandHref" value="${contextPath}/private/editBand/edit?bandId=${band.id}"></c:set>

                            <div class="col-md-3 col-xs-12 bandItem">

                                <!-- Image -->
                                <div class="row">

                                    <div class="col-md-12">

                                        <c:choose>
                                            <c:when test="${not empty band.bandLogo}">
                                                <c:set var="ownedBandLogoUrl">
                                                    ${contextPath}/media/getImage?imageId=${band.bandLogo.id}&size=<%=ImageSize.MEDIUM.toString()%>
                                                </c:set>
                                            </c:when>
                                            <c:otherwise>
                                                <c:set var="ownedBandLogoUrl">
                                                    ${contextPath}/resources/images/profile_avatar.png
                                                </c:set>
                                            </c:otherwise>
                                        </c:choose>

                                        <a href="${bandHref}"><img src="${ownedBandLogoUrl}" class="img-responsive" /></a>

                                    </div>

                                </div>

                                <!-- Band Name -->
                                <div class="row">

                                    <div class="col-md-12">

                                            <%--<s:eval expression="T(com.robynem.mit.web.util.PortalHelper).replaceSpecialCharactersForHtml(band.name)" var="bandName" />--%>
                                        <a href="${bandHref}"> ${fn:replace(band.name, 'è','&egrave;' )
                                                .replace('é', '&egrave;')
                                                .replace('ò', '&ograve;')
                                                .replace('à', '&agrave;')
                                                .replace('ù', '&ugrave;')
                                                } </a>

                                    </div>

                                </div>

                            </div>

                            <c:if test="${counter eq 4}">
                                </div>
                                <c:set var="counter" value="0"></c:set>
                            </c:if>

                        </c:forEach>

                        <div style="clear: both"></div>



                </c:when>
            </c:choose>


        </div>

        <hr class="lineSeparator" />
    </div>

</div>

