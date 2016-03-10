<%@ page import="com.robynem.mit.web.util.Constants" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%--
  Created by IntelliJ IDEA.
  User: roberto
  Date: 06/12/2015
  Time: 19.46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<c:set var="redirectView" value="index" scope="session"></c:set>
<html>
<head>
    <jsp:include page="common/head-include.jsp"   ></jsp:include>

    <link rel="stylesheet" href="${contextPath}/resources/css/index.css?v=${softwareVersion}">
    <script type="text/javascript" src="${contextPath}/resources/js/smartsearch.js?v=${softwareVersion}"></script>
    <title>${siteTitle}</title>
</head>
<body>

    <!-- MESSAGES START -->
    <jsp:include page="common/messagesDisplayer.jsp"></jsp:include>
    <!-- MESSAGES END -->

  <div id="container" class="container mainContainer">


      <!-- MENU START -->
     <jsp:include page="common/menu.jsp">
         <jsp:param name="activeItem" value="menuIndex"></jsp:param>
     </jsp:include>
      <!-- MENU END -->

      <div class="row">
          <div id="firstGrouping" class="col-md-12">
              <!-- SLIDER / SEARCH - START -->

              <div class="row">
                  <!--SLIDER-->
                  <div class="col-md-8">
                      <div class="carousel slide" id="slidesCarousel" data-ride="carousel">
                          <ol class="carousel-indicators">
                              <li data-slide-to="0" data-target="#slidesCarousel">
                              </li>
                              <li data-slide-to="1" data-target="#slidesCarousel">
                              </li>
                              <li data-slide-to="2" data-target="#slidesCarousel" class="active">
                              </li>
                          </ol>
                          <div class="carousel-inner">
                              <div class="item">
                                  <img alt="Carousel Bootstrap First" src="${contextPath}/resources/images/home_slides_620x390.jpg" class="img-responsive"/>
                                  <div class="carousel-caption">
                                      <h4>
                                          First Thumbnail label
                                      </h4>
                                      <p>
                                          Cras justo odio, dapibus ac facilisis in, egestas eget quam. Donec id elit non mi porta gravida at eget metus. Nullam id dolor id nibh ultricies vehicula ut id elit.
                                      </p>
                                  </div>
                              </div>
                              <div class="item">
                                  <img alt="Carousel Bootstrap Second" src="${contextPath}/resources/images/home_slides_620x390.jpg" class="img-responsive"/>
                                  <div class="carousel-caption">
                                      <h4>
                                          Second Thumbnail label
                                      </h4>
                                      <p>
                                          Cras justo odio, dapibus ac facilisis in, egestas eget quam. Donec id elit non mi porta gravida at eget metus. Nullam id dolor id nibh ultricies vehicula ut id elit.
                                      </p>
                                  </div>
                              </div>
                              <div class="item active">
                                  <img alt="Carousel Bootstrap Third" src="${contextPath}/resources/images/home_slides_620x390.jpg" class="img-responsive"/>
                                  <div class="carousel-caption">
                                      <h4>
                                          Third Thumbnail label
                                      </h4>
                                      <p>
                                          Cras justo odio, dapibus ac facilisis in, egestas eget quam. Donec id elit non mi porta gravida at eget metus. Nullam id dolor id nibh ultricies vehicula ut id elit.
                                      </p>
                                  </div>
                              </div>
                          </div> <a class="left carousel-control" href="#slidesCarousel" data-slide="prev"><span class="glyphicon glyphicon-chevron-left"></span></a> <a class="right carousel-control" href="#slidesCarousel" data-slide="next"><span class="glyphicon glyphicon-chevron-right"></span></a>
                      </div>
                  </div>



                  <!--SMART SEARCH-->
                  <div class="col-md-4">

                      <form role="form">
                          <div class="form-group">

                              <label for="searchLocation">
                                  <spring:message code="smart-search.location"></spring:message>
                              </label>
                              <input id="searchLocation" type="text" class="form-control" value=""   placeholder="<spring:message code="smart-search.insert.location"></spring:message>"/>
                          </div>
                          <div class="form-group">

                              <label for="searchType">
                                  <spring:message code="smart-search.what-you-look-for"></spring:message>
                              </label>
                              <select id="searchType" class="form-control">
                                  <option><spring:message code="smart-search.artist"></spring:message></option>
                                  <option><spring:message code="smart-search.band"></spring:message></option>
                                  <option><spring:message code="smart-search.club"></spring:message></option>
                              </select>
                          </div>
                          <div class="form-group">

                              <label for="searchFilter">
                                  <spring:message code="smart-search.filter"></spring:message>
                              </label>
                              <input id="searchFilter" type="text" class="form-control" value=""/>
                          </div>

                          <button id="btnSearch" type="submit" class="btn btn-default">
                              <spring:message code="smart-search.search"></spring:message>
                          </button>
                      </form>

                  </div>

                  <script type="text/javascript">
                      var CHOOSE_A_LOCALITY_MESSAGE = '<spring:message code="smart-search.choose-a-location"></spring:message>!';


                      $(function() {
                          var smartSearch = new SmartSearch({
                              namespace : '',
                              chooseLocalityError : {"<%=Constants.APPLICATION_MESSAGES_KEY%>" : [
                                  {
                                      severity : "WARNING",
                                      link : null,
                                      message : CHOOSE_A_LOCALITY_MESSAGE
                                  }
                              ]}
                          });

                          smartSearch._init();
                      });
                  </script>

              </div>

              <!-- SLIDER / SEARCH - END -->

              <!-- ADVICES START -->
              <div id="advices" class="row">
                  <div class="col-md-4 advice">
                      <img src="${contextPath}/resources/images/box-01-cos-e-mit.gif" class="img-responsive"  />
                  </div>

                  <div class="col-md-4 advice">
                      <img src="${contextPath}/resources/images/box-02-i-vantaggi-dell-app.gif"  class="img-responsive" />
                  </div>

                  <div class="col-md-4 advice">
                      <img src="${contextPath}/resources/images/box-03-scarica-l-app.gif" class="img-responsive" />
                  </div>
              </div>
              <!-- ADVICES END -->
           </div>


      </div>

       <br/>

       <!-- EVENTS LIST START -->
       <div class="row">
            <div  class="col-md-12">
                <form id="nextEventsForm" role="form" class="form-inline">
                    <div class="row">
                        <div class="col-md-4" >
                            <label for="nextEventsLocation">
                                <spring:message code="next-events.next-in-your-town"></spring:message>
                            </label>
                        </div>

                        <div class="col-md-8" >
                            <input type="text" class="form-control" id="nextEventsLocation" placeholder="<spring:message code="next-events.digit-your-town"></spring:message>" style="width: 100%;"/>
                        </div>
                    </div>
                </form>
            </div>
       </div>

      <div class="row">
          <div id="nextEventsContainer"  class="col-md-12">

          </div>
      </div>

      <script type="text/javascript">
          $(function() {
              $.ajax({
                  url : '${contextPath}/home/loadNextEvents',
                  method : 'get',
                  cache : false,
                  async : true,
                  success : function(data) {
                      $("#nextEventsContainer").html(data);
                  }
              });
          });
      </script>
       <!-- EVENTS LIST START -->

      <!-- ARTIST LIST START -->
      <div class="row newArtistsRow">
          <div  class="col-md-12">
              <label >
                  <spring:message code="new-artists.new-artists-and-bands"></spring:message>
              </label>
          </div>
      </div>

      <div class="row">
          <div id="newArtistsContainer"  class="col-md-12">

          </div>
      </div>

      <script type="text/javascript">
          $(function() {
              $.ajax({
                  url : '${contextPath}/home/loadNewArtists',
                  method : 'get',
                  cache : false,
                  async : true,
                  success : function(data) {
                      $("#newArtistsContainer").html(data);
                  }
              });
          });
      </script>
      <!-- ARTIST LIST END -->

      <!-- CLUBS LIST START -->
      <div class="row newClubsRow">
          <div  class="col-md-12">
              <label >
                  <spring:message code="new-clubs.new-clubs"></spring:message>
              </label>
          </div>
      </div>

      <div class="row">
          <div id="newClubsContainer"  class="col-md-12">

          </div>
      </div>

      <script type="text/javascript">
          $(function() {
              $.ajax({
                  url : '${contextPath}/home/loadNewClubs',
                  method : 'get',
                  cache : false,
                  async : true,
                  success : function(data) {
                      $("#newClubsContainer").html(data);
                  }
              });
          });
      </script>
      <!-- CLUBS LIST END -->

  </div>
  <!-- CONTAINER END -->


</body>
</html>
