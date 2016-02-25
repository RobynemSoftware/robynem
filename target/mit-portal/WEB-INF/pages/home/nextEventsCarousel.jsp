<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>



<div class="carousel slide" id="nextEventsCarousel" data-interval="false">
  <%--
  <ol class="carousel-indicators">
    <li data-slide-to="0" data-target="#nextEventsCarousel" class="active">
    </li>
    <li data-slide-to="1" data-target="#nextEventsCarousel">
    </li>
    <li data-slide-to="2" data-target="#nextEventsCarousel">
    </li>
  </ol>
  --%>
  <div class="carousel-inner">
    <c:forEach var="event" items="${nextEvents}" varStatus="loop">


      <c:set var="active" value="" />

      <c:if test="${loop.index eq 0}">
        <c:set var="active" value="active" />
      </c:if>

      <c:if test="${loop.index eq 0 or loop.index eq 4 or loop.index eq 8}">
        <div class="item ${active}" >
          <div class="row">
      </c:if>

          <div class="col-md-3 col-sm-6">
            <div class="nextEventData">
              <img  src="${event.imgUrl}" class="img-event"/>
            </div>
            <div class="nextEventData">
              <span clas="bandName" style="font-weight: bold;">
                  ${event.artistName}
              </span>
            </div>
            <div class="nextEventData">
              <span clas="bandDescription">
                  ${event.artistDescription}
              </span>
            </div>
            <div class="nextEventData">
              <span clas="clubName" style="font-weight: bold;">
                  ${event.eventPlaceName}
              </span>
            </div>
            <div  class="nextEventData">
              <span clas="clubType">
                  ${event.eventPlaceType}
              </span>
            </div>
            <div  class="nextEventData">
              <span clas="eventInfo" style="font-weight: bold;">
                  ${event.eventDetails}
              </span>
            </div>
            <div class="nextEventData">
              <c:if test="${event.phone eq true}">
                  <img src="${contextPath}/resources/images/icon-phone.png"  width="20px;" height="20px;" title="Necessaria prenotazione">
                  &nbsp;
              </c:if>

              <c:if test="${event.money eq true}">
                <img src="${contextPath}/resources/images/icon-money.png" width="20px;" height="20px;" title="Prezzo d'ingresso previsto">
                &nbsp;
              </c:if>

              <c:if test="${event.specialOffer eq true}">
                <img src="${contextPath}/resources/images/icon-special-offer.png" width="20px;" height="20px;" title="Prevista offerta speciale">
                &nbsp;
              </c:if>
            </div>
          </div>


      <c:if test="${loop.index eq 3 or loop.index eq 7 or loop.index eq 11 or fn:length(nextEvents) - 1 eq loop.index}">
        </div>
       </div>
      </c:if>

    </c:forEach>

  </div>
  <c:if test="${fn:length(nextEvents) gt 4}"  >
    <a class="left carousel-control nextEventCarouselArrow" href="#nextEventsCarousel" data-slide="prev"><span class="glyphicon glyphicon-chevron-left"></span></a> <a class="right carousel-control nextEventCarouselArrow" href="#nextEventsCarousel" data-slide="next"><span class="glyphicon glyphicon-chevron-right"></span></a>
  </c:if>

</div>


