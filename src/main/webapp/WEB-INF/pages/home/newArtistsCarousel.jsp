<%@ page import="com.robynem.mit.web.util.ArtistType" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<form id="newArtistsForm" action="${contextPath}/viewBand">
    <input type="hidden" id="newArtistsForm_bandId" name="bandId">
</form>

<script type="text/javascript">
    function goToBand(bandId) {
        $("#newArtistsForm_bandId").val(bandId);
        $("#newArtistsForm").submit();
    }
</script>

<div class="carousel slide" id="newArtistsCarousel" data-interval="false">
    <%--
    <ol class="carousel-indicators">
        <li data-slide-to="0" data-target="#newArtistsCarousel" class="active">
        </li>
        <li data-slide-to="1" data-target="#newArtistsCarousel">
        </li>
        <li data-slide-to="2" data-target="#newArtistsCarousel">
        </li>
    </ol>
    --%>
    <div class="carousel-inner">
        <c:forEach var="artist" items="${newArtists}" varStatus="loop">


            <c:set var="active" value="" />

            <c:if test="${loop.index eq 0}">
                <c:set var="active" value="active" />
            </c:if>

            <c:if test="${loop.index eq 0 or loop.index eq 6 or loop.index eq 12}">
                <div class="item ${active}" >
                <div class="row">
            </c:if>

            <div class="col-md-2">
                <div class="nextEventData">
                    <img  src="${artist.imgUrl}" class="img-artist"/>
                </div>
                <div class="nextEventData">
                  <span class="bandName" style="font-weight: bold;">
                      <c:set var="ARTIST_TYPE_BAND">
                          <%=ArtistType.BAND%>
                      </c:set>

                      <c:choose>
                          <c:when test="${ARTIST_TYPE_BAND eq artist.artistType}">
                              <c:set var="newArtistsJs">
                                  javascript:goToBand(${artist.id});
                              </c:set>
                          </c:when>
                          <c:otherwise>
                              <c:set var="newArtistsJs" value="#"/>
                          </c:otherwise>
                      </c:choose>
                          <a href="${newArtistsJs}">${artist.artistName}</a>
                  </span>
                </div>
                <div class="nextEventData">
                  <span class="bandDescription">
                          ${artist.artistDescription}
                  </span>
                </div>
            </div>


            <c:if test="${loop.index eq 5 or loop.index eq 11 or loop.index eq 17 or fn:length(newArtists) - 1 eq loop.index}">
                </div>
                </div>
            </c:if>

        </c:forEach>

    </div>
    <c:if test="${fn:length(newArtists) gt 6}"  >
        <a class="left carousel-control nextEventCarouselArrow" href="#newArtistsCarousel" data-slide="prev"><span class="glyphicon glyphicon-chevron-left"></span></a> <a class="right carousel-control nextEventCarouselArrow" href="#newArtistsCarousel" data-slide="next"><span class="glyphicon glyphicon-chevron-right"></span></a>
    </c:if>

</div>



