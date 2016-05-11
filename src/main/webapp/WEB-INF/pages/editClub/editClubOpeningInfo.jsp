<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="../common/messagesDisplayerAsync.jsp"></jsp:include>

<div id="openingInfoList" class="col-md-12">

  <div class="row">
    <div class="col-md-12">

      <div class="row headerRow">
        <div class="col-md-2">
          <span><spring:message code="club.opening-info.start-day"></spring:message> </span>
        </div>

        <div class="col-md-2">
          <span><spring:message code="club.opening-info.end-day"></spring:message> </span>
        </div>

        <div class="col-md-2">
          <span><spring:message code="club.opening-info.opening-hour"></spring:message> </span>
        </div>

        <div class="col-md-2">
          <span><spring:message code="club.opening-info.closing-hour"></spring:message> </span>
        </div>

        <div class="col-md-2">
          <span><spring:message code="club.opening-info.opened"></spring:message> </span>
        </div>

        <div id="addOpeningInfoLink" class="col-md-2">
          <img  src="${contextPath}/resources/images/add_32x32.png"
                class="img-responsive clickable"
                title="<spring:message code="club.opening-info.add-new.tooltip"></spring:message>"
                onclick="javascript:addOpeningInfo();">
        </div>
      </div>

      <c:forEach var="info" items="${clubModel.openingInfos}" varStatus="status">
        <div class="row OI_dataRow" id="info_${status.index}">
            <%-- start day --%>
          <div class="col-md-2">
            <select class="form-control openingInfoControl" name="OI_startDay">
              <option></option>
              <c:forEach var="day" items="${daysOfWeek}">
                <option value="${day.value}"
                        <c:if test="${day.value eq info.startDay}">selected="selected"</c:if>
                        >${day.text}</option>
              </c:forEach>
            </select>
          </div>

            <%-- end day --%>
          <div class="col-md-2">
            <select class="form-control openingInfoControl" name="OI_endDay">
              <option></option>
              <c:forEach var="day" items="${daysOfWeek}">
                <option value="${day.value}"
                        <c:if test="${day.value eq info.endDay}">selected="selected"</c:if>
                        >${day.text}</option>
              </c:forEach>
            </select>
          </div>

            <%-- start hour --%>
          <div class="col-md-2">
            <input type="text" class="form-control timeText openingInfoControl" value="${info.startHour}" name="OI_startHour" />

          </div>

            <%-- end hour --%>
          <div class="col-md-2">

            <input type="text" class="form-control timeText openingInfoControl" value="${info.endHour}" name="OI_startEnd"/>

          </div>

          <div class="col-md-2">
            <input type="checkbox" class="openingInfoControl" <c:if test="${info.opened eq true}">checked="checked"</c:if> name="OI_opened" />
          </div>

          <div class="col-md-2">
            <img src="${contextPath}/resources/images/delete_32x32.png" class="img-responsive clickable" onclick="javascript:removeOpeningInfo('info_${status.index}')">
          </div>

        </div>
      </c:forEach>

    </div>
  </div>

</div>