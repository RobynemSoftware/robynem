<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="row">
    <div class="col-md-12">

      <div id="messagesDialog" style="display: none;">

        <c:if test="${not empty messages}">
          <div class="row">

            <div class="col-md-12">
              <c:forEach var="mess" items="${messages}">

                <div class="row">

                    <div class="col-md-4">
                      <c:choose>
                        <c:when test="${mess.severity eq 'INFO'}">
                          <img src="${contextPath}/resources/images/Info_32x32.png" class="img-responsive" />
                          <c:set var="applicationMessageClass" value="info"></c:set>
                        </c:when>
                        <c:when test="${mess.severity eq 'WARNING'}">
                          <img src="${contextPath}/resources/images/Warning_32x32.png" class="img-responsive" />
                          <c:set var="applicationMessageClass" value="warning"></c:set>
                        </c:when>
                        <c:when test="${mess.severity eq 'FATAL'}">
                          <img src="${contextPath}/resources/images/Fatal_32x32.png" class="img-responsive" />
                          <c:set var="applicationMessageClass" value="fatal"></c:set>
                        </c:when>
                      </c:choose>
                    </div>

                    <div class="col-md-8">

                      <c:choose>
                        <c:when test="${not empty mess.link}">
                          <a class="${applicationMessageClass}" href="${mess.link}">${mess.message}</a>
                        </c:when>
                        <c:otherwise>
                          <span class="${applicationMessageClass}">
                              ${mess.message}
                          </span>
                        </c:otherwise>
                      </c:choose>


                    </div>
                 </div>
              </c:forEach>


            </div>
          </div>

        </c:if>

      </div>

    </div>
</div>



<script type="text/javascript" defer>
  $(function() {
    <c:if test="${not empty messages}">
      openApplicationMessagesDialog("${redirectUrl}");
    </c:if>
  });
</script>
