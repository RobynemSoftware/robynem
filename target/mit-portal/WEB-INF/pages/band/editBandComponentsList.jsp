<%@ page import="com.robynem.mit.web.util.ImageSize" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:forEach var="component" items="${bandModel.components}">
    <div class="row componentRow">
        <!-- IMAGE  COLUMN-->
        <div class="col-md-2 col-xs-12">
            <c:choose>
                <c:when test="${not empty component.profileImageId}">
                    <c:set var="imageUrl">
                        ${contextPath}/media/getImage?imageId=${component.profileImageId}&size=<%=ImageSize.SMALL.toString()%>
                    </c:set>
                </c:when>
                <c:otherwise>
                    <c:set var="imageUrl">
                        ${contextPath}/resources/images/profile_avatar_50x50.png
                    </c:set>
                </c:otherwise>
            </c:choose>

            <img src="${imageUrl}" class="img-responsive" />

        </div>

        <!-- NAME COLUMN -->
        <div class="col-md-4 col-xs-12">
            <span id="componentName">${component.name}</span>
        </div>

        <!-- INSTUMENTS COLUMN-->
        <div class="col-md-3 col-xs-12">

            <!-- Singer and Dj -->
            <div class="checkbox">

                <!-- Singer -->
                <c:if test="${component.singer eq true}">
                    <label class="checkbox-inline"><input type="checkbox"
                    <c:if test="${component.singerSelected}">
                                                          checked="checked"
                    </c:if>

                    <c:if test="${component.confirmed eq false}">
                                                          disabled="disabled"
                    </c:if>

                                        data="${component.id}"   onclick="javascript:saveComponentSinger(this);"
                            ><spring:message code="profile.musician-form.singer"></spring:message></label>

                    <br/>
                </c:if>

                <!-- Dj -->
                <c:if test="${component.discJockey eq true}">
                    <label class="checkbox-inline"><input type="checkbox"
                    <c:if test="${component.discJockeySelected}">
                                                          checked="checked"
                    </c:if>

                    <c:if test="${component.confirmed eq false}">
                                                          disabled="disabled"
                    </c:if>

                                           data="${component.id}"  onclick="javascript:saveComponentDiscJockey(this);"
                            ><spring:message code="profile.musician-form.dj"></spring:message></label>

                    <br/>
                </c:if>

                <c:forEach var="instrument" items="${component.instruments}">

                        <label class="checkbox-inline"><input type="checkbox" value="${instrument.id}"
                        <c:if test="${instrument.selected}">
                            checked="checked"
                        </c:if>

                        <c:if test="${component.confirmed eq false}">
                            disabled="disabled"
                        </c:if>

                        onclick="javascript:saveComponentInstrument(${component.userId}, ${instrument.id}, this);"
                                >${instrument.name}</label>

                        <br/>

                </c:forEach>
            </div>
        </div>

        <!-- CONFIRMATION -->
        <div class="col-md-3 col-xs-6">
            <c:if test="${component.confirmed eq false}">
                <span id="confirmationPanding"><spring:message code="band.component.confirmation-pending"></spring:message> </span>
            </c:if>
        </div>

        <!-- REMOVE -->
        <div class="col-md-3 col-xs-6">
            <a href="javascript:removeComponent(${component.userId})" class="removeBandComponent"><spring:message code="band.component.remove-component"></spring:message></a>
        </div>



    </div>

    <hr class="bandComponentSeparator"/>

 </c:forEach>

<script type="text/javascript">
    function saveComponentInstrument(userId, instrumentId, checkboxObject) {
        var chk = $(checkboxObject);

        var checked = chk.is(":checked");

        execInSession(function() {
            $.ajax({
                url: "${contextPath}/private/editBand/saveComponentInstrument",
                type: "post",
                dataType: "html",
                data: {
                    userId : userId,
                    instrumentId: instrumentId,
                    selected: checked
                },
                success: function(data) {
                    showApplicationMessages(data);

                    $("#editBandComponentsList").html(data);

                    showBandStatus();
                },
                error: function() {
                    chk.attr("checked", !checked);
                }
            });
        });
    }

    function saveComponentSinger(checkboxObject) {
        var chk = $(checkboxObject);
        var bandComponentId = chk.attr("data");

        var checked = chk.is(":checked");

        execInSession(function() {
            $.ajax({
                url: "${contextPath}/private/editBand/saveComponentSinger",
                type: "post",
                dataType: "html",
                data: {
                    bandComponentId : bandComponentId,
                    selected: checked
                },
                success: function(data) {
                    showApplicationMessages(data);

                    $("#editBandComponentsList").html(data);

                    showBandStatus();
                },
                error: function() {
                    chk.attr("checked", !checked);
                }
            });
        });
    }

    function saveComponentDiscJockey(checkboxObject) {
        var chk = $(checkboxObject);
        var bandComponentId = chk.attr("data");

        var checked = chk.is(":checked");

        execInSession(function() {
            $.ajax({
                url: "${contextPath}/private/editBand/saveComponentDiscJockey",
                type: "post",
                dataType: "html",
                data: {
                    bandComponentId : bandComponentId,
                    selected: checked
                },
                success: function(data) {
                    showApplicationMessages(data);

                    $("#editBandComponentsList").html(data);

                    showBandStatus();
                },
                error: function() {
                    chk.attr("checked", !checked);
                }
            });
        });
    }

    function removeComponent(userId) {
        if (confirm("<spring:message code="band.component.confirm-component-removal"></spring:message>")) {
            execInSession(function() {
                $.ajax({
                    url: "${contextPath}/private/editBand/removeComponent",
                    type: "post",
                    dataType: "html",
                    data: {
                        userId : userId
                    },
                    success: function(data) {
                        $("#editBandComponentsList").html(data);

                        showBandStatus();
                    }
                });
            });
        }
    }
</script>