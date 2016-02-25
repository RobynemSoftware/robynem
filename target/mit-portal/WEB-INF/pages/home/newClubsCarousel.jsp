<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>



<div class="carousel slide" id="newClubsCarousel" data-interval="false">
    <%--
    <ol class="carousel-indicators">
        <li data-slide-to="0" data-target="#newClubsCarousel" class="active">
        </li>
        <li data-slide-to="1" data-target="#newClubsCarousel">
        </li>
        <li data-slide-to="2" data-target="#newClubsCarousel">
        </li>
    </ol>
    --%>
    <div class="carousel-inner">
        <c:forEach var="club" items="${newClubs}" varStatus="loop">


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
                    <img  src="${club.imgUrl}" class="img-artist"/>
                </div>
                <div class="nextEventData">
                  <span clas="bandName" style="font-weight: bold;">
                          ${club.clubName}
                  </span>
                </div>
                <div class="nextEventData">
                  <span clas="bandDescription">
                          ${club.clubType}
                  </span>
                </div>
            </div>


            <c:if test="${loop.index eq 5 or loop.index eq 11 or loop.index eq 17 or fn:length(newClubs) - 1 eq loop.index}">
                </div>
                </div>
            </c:if>

        </c:forEach>

    </div>
    <c:if test="${fn:length(newClubs) gt 6}"  >
        <a class="left carousel-control nextEventCarouselArrow" href="#newClubsCarousel" data-slide="prev"><span class="glyphicon glyphicon-chevron-left"></span></a> <a class="right carousel-control nextEventCarouselArrow" href="#newClubsCarousel" data-slide="next"><span class="glyphicon glyphicon-chevron-right"></span></a>
    </c:if>

</div>



