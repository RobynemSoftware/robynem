<%@ page import="com.robynem.mit.web.util.Constants" %>
<%@ page import="com.robynem.mit.web.util.MessageSeverity" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="../common/messagesDisplayerAsync.jsp"></jsp:include>

    <input type="hidden" name="currentTabIndex" value="0">

    <div class="row">

        <!-- DESCRIPTION -->
        <div id="editBandComponentsList_title" class="row">
            <div  class="col-md-12">
                <span>
                    <spring:message code="band.component.title"></spring:message>
                </span>
            </div>
        </div>

        <br>

        <!-- COMPONENTS LIST -->
        <div  class="row">
            <div id="editBandComponentsList" class="col-md-12">
                <jsp:include page="editBandComponentsList.jsp"></jsp:include>
            </div>
        </div>

        <br>

        <!-- ACCORDIONS -->
        <div class="accordion" id="editBandComponentsAccordion">
            <!-- ADD EXISTING COMPONENTS -->
            <div class="accordion-group">
                <div class="accordion-heading">
                    <a class="accordion-toggle" data-toggle="collapse" data-parent="#editBandComponentsAccordion" href="#collapseFilterRegisteredComponents">
                        <span><spring:message code="band.component.select-component"></spring:message> </span>
                    </a>
                </div>
                <div id="collapseFilterRegisteredComponents" class="accordion-body collapse in">
                    <div class="accordion-inner">

                        <!-- Add component -->
                        <div class="row">
                            <div class="col-md-6 col-xs-12">
                                <label><spring:message code="band.component.find-by-name"></spring:message> </label>
                            </div>

                            <div class="col-md-6 col-xs-12">
                                <input type="text" id="editBandComponentsNameAutocomplete" class="form-control">
                            </div>
                        </div>

                        <!-- Invite external -->
                        <div class="row">
                            <div class="col-md-6 col-xs-12">
                                <label><spring:message code="band.component.invite-external-by-email"></spring:message> </label>
                            </div>

                            <div class="col-md-4 col-xs-12">
                                <input type="email" id="inviteExternalEmail" class="form-control" placeholder="<spring:message code="global.insert-email-address"></spring:message>">
                            </div>

                            <div class="col-md-2 col-xs-12">
                                <button id="sendEmailInvitation" class="btn btn-default">
                                    <spring:message code="band.component.send-invitation"></spring:message>
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <br />

            <!-- SEARCH COMPONENTS -->
            <div class="accordion-group">
                <div class="accordion-heading">
                    <a class="accordion-toggle" data-toggle="collapse" data-parent="#editBandComponentsAccordion" href="#collapseSearchComponents">
                        <span><spring:message code="band.component.search-component"></spring:message> </span>
                    </a>
                </div>
                <div id="collapseSearchComponents" class="accordion-body collapse">
                    <div class="accordion-inner">

                        <!-- Filters -->
                        <div class="row">
                            <div class="col-md-12">

                                <form id="searchComponentsForm" role="form">

                                    <div class="row">
                                        <div class="col-md-6 col-xs-12">

                                            <!-- Instrument -->
                                            <div class="form-group">
                                                <label for="searchComponentInstrument">
                                                    <spring:message code="band.component.search-component.filter.instrument"></spring:message>
                                                </label>
                                                <select id="searchComponentInstrument" class="form-control" name="selectedInstrument">
                                                    <option></option>
                                                    <c:forEach var="item" items="${instrumentsList}">
                                                        <option value="${item.id}">${item.name}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </div>

                                        <div class="col-md-6 col-xs-12">

                                            <!-- Genre -->
                                            <div class="form-group">
                                                <label for="searchComponentGenre">
                                                    <spring:message code="band.component.search-component.filter.genre"></spring:message>
                                                </label>
                                                <select id="searchComponentGenre" class="form-control" name="selectedGenre">
                                                    <option></option>
                                                    <c:forEach var="item" items="${musicGenresList}">
                                                        <option value="${item.id}">${item.name}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>

                                        </div>
                                    </div>

                                    <div class="row">

                                        <div class="col-md-6 col-xs-12">
                                            <!-- Location -->
                                            <div class="form-group">

                                                <label for="searchComponentLocation">
                                                    <spring:message code="band.component.search-component.filter.location"></spring:message>
                                                </label>
                                                <input id="searchComponentLocation" type="text" class="form-control" value=""   placeholder="<spring:message code="smart-search.insert.location"></spring:message>"/>
                                                <input id="searchComponentPlaceId" name="placeId" type="hidden"/>
                                            </div>
                                        </div>

                                        <div class="col-md-6 col-xs-12">
                                            <!-- Keyword -->
                                            <div class="form-group">

                                                <label for="searchComponentKeyword">
                                                    <spring:message code="band.component.search-component.filter.keyword"></spring:message>
                                                </label>
                                                <input id="searchComponentKeyword" type="text" class="form-control" name="keyword" value=""   placeholder="<spring:message code="band.component.search-component.filter.keyword.placeholder"></spring:message>"/>
                                            </div>
                                        </div>

                                     </div>

                                    <div class="row">

                                        <!-- Singer -->
                                        <div class="col-md-6 col-xs-12">
                                            <input id="searchComponentSinger" type="checkbox" name="singer" class="checkbox-inline" value="true" />

                                            <label for="searchComponentSinger" ><spring:message code="profile.musician-form.singer"></spring:message></label>
                                        </div>

                                        <!-- Dj -->
                                        <div class="col-md-6 col-xs-12">
                                            <input id="searchComponentDj" type="checkbox" name="dj" class="checkbox-inline" value="true" />

                                            <label for="searchComponentDj" ><spring:message code="profile.musician-form.dj"></spring:message></label>
                                        </div>

                                    </div>

                                    <!-- Buttons -->
                                    <button id="searchComponentBtnSearch" type="submit" class="btn btn-default">
                                        <spring:message code="band.component.search-component.filter.btn.search"></spring:message>
                                    </button>

                                </form>

                            </div>
                        </div>

                        <br/>

                        <div class="row">
                            <div id="searchComponentResultContent" class="col-md-12">

                            </div>
                        </div>

                    </div>
                </div>
            </div>
        </div>


    </div>
<%--</form:form>--%>


<script type="text/javascript">
    $(function () {
        initNameAutocomplete();

        initSearchComponentForm();

        initSearchLocationAutocomplete();

        initInviteFields();
    });

    function initNameAutocomplete() {
        $("#editBandComponentsNameAutocomplete").autocomplete({
            minLength : 4,
            source : function(request, response) {

                execInSession(function() {
                    $.ajax({
                        url: "${contextPath}/private/editBand/filterComponentName",
                        type: "post",
                        dataType: "json",
                        data: {
                            term : request.term
                        },
                        success: function(data) {
                            if (!showApplicationMessages(data)) {
                                response($.map(data.items, function(item) {
                                    return {
                                        label: item.label,
                                        value: item.value,
                                        imageUrl: item.imageUrl,
                                        name: item.name,
                                        emailAddress: item.emailAddress
                                    }
                                }));
                            }
                        }
                    });
                });
            },

            select: function( event, ui ) {
                execInSession(function() {
                    $.ajax({
                        url: "${contextPath}/private/editBand/addComponentAutocomplete",
                        type: "post",
                        dataType: "html",
                        data: {
                            userId : ui.item.value
                        },
                        success: function(data) {
                            $("#editBandComponentsNameAutocomplete").val("");
                            $("#editBandComponentsList").html(data);

                            showBandStatus();
                        }
                    });
                });
            }
        }).data("ui-autocomplete")._renderItem = function(ul, item) {

            var imageUrl = item.imageUrl != null ? item.imageUrl : "${contextPath}/resources/images/profile_avatar_50x50.png";

            var mainRow = $("<div class='row'></div>");
            var image = $("<div class='col-md-3'><img src=\"" + imageUrl + "\"  /></div>").appendTo(mainRow);

            var contentCol = $("<div class='col-md-9'></div>");
            var nameContent = $("<div class='row'><span>" + item.name + "</span></div>").appendTo(contentCol);
            var emailContent = $("<div class='row'><span>(" + item.emailAddress + ")</span></div>").appendTo(contentCol);

            mainRow.append(contentCol);

            return $("<li></li>").data("item.autocomplete", item)
                    .append(mainRow)
                    .appendTo(ul);
        };
    }

    function initSearchComponentForm() {
        $("#searchComponentBtnSearch").click(function() {
            searchComponents();

            return false;
        });

        $("#searchComponentLocation").keydown(function(event){
            if(event.keyCode == 13) {
                event.preventDefault();
                return false;
            } else {
                $("#searchComponentPlaceId").val("");
            }
        });
    }

    function addComponent(component) {
        //editBandComponentsList

        var row = $("<div></div>").addClass("row");

        var imgCol = $("<div></div>").addClass("col-md-4");
    }

    function searchComponents() {



        // Validation
        var validationError = null;
        if ($("#searchComponentInstrument").val() == "" && $("#searchComponentGenre").val() == "" && $("#searchComponentPlaceId").val() == ""
                    && $.trim($("#searchComponentKeyword").val()) == "" && $.trim($("#searchComponentLocation").val()) == ""
                    && !$("#searchComponentSinger").is(":checked") && !$("#searchComponentDj").is(":checked")) {
            validationError = "<spring:message code="band.validation.select-a-filter"></spring:message>";
        } else if ($("#searchComponentPlaceId").val() == "" && $.trim($("#searchComponentLocation").val()) != "") {
            validationError = "<spring:message code="band.validation.select-a-location"></spring:message>";
        }

        if (validationError != null) {
            showApplicationMessages({
                "<%=Constants.APPLICATION_MESSAGES_KEY%>": [
                    {
                        severity: "<%=MessageSeverity.FATAL.toString()%>",
                        link: null,
                        message: validationError
                    }
                ]
            });
        } else {
            execInSession(function() {
                $.ajax({
                    url: "${contextPath}/private/editBand/searchComponents",
                    type: "get",
                    dataType: "html",
                    data: $("#searchComponentsForm").serialize(),
                    success: function(data) {
                        if (!showApplicationMessages(data)) {
                            $("#searchComponentResultContent").html(data);
                        }
                    }
                });
            });
        }


    }

    function initSearchLocationAutocomplete() {
        var accepted_google_types = ["locality", "administrative_area_level_2", "administrative_area_level_3"];
        var CHOOSE_A_LOCALITY_MESSAGE = '<spring:message code="smart-search.choose-a-location"></spring:message>!';

        var placeId;
        var placeDescription;

        var placeIdFieldId = "searchComponentPlaceId";
        var locationFieldId = "searchComponentLocation";

        var locationField = document.getElementById(locationFieldId);


        var options = {
            types: ['(cities)']
        };

        var autocomplete = new google.maps.places.Autocomplete(locationField, options);

        autocomplete.addListener('place_changed', function () {
            var place = autocomplete.getPlace();


            if (place.place_id != undefined && place.types != undefined) {

                var match = false;

                var types = place.types;
                for (var i = 0; i < accepted_google_types.length; i++) {

                    for (var j = 0; j < types.length; j++) {

                        if (types[j] == accepted_google_types[i]) {
                            match = true;
                            break;
                        }
                    }

                }

                if (!match) {

                    $("#" + placeIdFieldId).val("");
                    $("#" + locationFieldId).val("");

                    showApplicationMessages({
                        "<%=Constants.APPLICATION_MESSAGES_KEY%>": [
                            {
                                severity: "<%=MessageSeverity.WARNING.toString()%>",
                                link: null,
                                message: CHOOSE_A_LOCALITY_MESSAGE
                            }
                        ]
                    });
                } else {
                    placeId = place.place_id;

                    $("#" + placeIdFieldId).val(placeId);
                }
            } else {
                showApplicationMessages({
                    "<%=Constants.APPLICATION_MESSAGES_KEY%>": [
                        {
                            severity: "<%=MessageSeverity.WARNING.toString()%>",
                            link: null,
                            message: CHOOSE_A_LOCALITY_MESSAGE
                        }
                    ]
                });
            }


        });
    }

    function initInviteFields() {


        $("#sendEmailInvitation").click(function () {

            var emailAddress = $.trim($("#inviteExternalEmail").val());

            if (emailAddress != "") {
                if (!isEmail(emailAddress)) {
                    showApplicationMessages({
                        "<%=Constants.APPLICATION_MESSAGES_KEY%>" : [
                            {
                                message : "<spring:message code="global.invalid-email"></spring:message>",
                                severity : "<%=MessageSeverity.FATAL%>",
                                link : null
                            }
                        ]
                    });
                } else {
                    execInSession(function () {

                        $.ajax({
                            url : "${contextPath}/private/editBand/inviteNonRegisteredUser",
                            data : {
                                emailAddress : emailAddress
                            },
                            type : "post",
                            dataType : "json",
                            cache : false,
                            async : true,
                            success : function (data) {
                                showApplicationMessages(data);

                                if (data.success == true) {
                                    $("#inviteExternalEmail").val("");

                                    showApplicationMessages({
                                        "<%=Constants.APPLICATION_MESSAGES_KEY%>" : [
                                            {
                                                message : "<spring:message code="band.component.invitation.sent"></spring:message>",
                                                severity : "<%=MessageSeverity.INFO%>",
                                                link : null
                                            }
                                        ]
                                    });
                                }

                                if (data.emailExists == true) {
                                    showApplicationMessages({
                                        "<%=Constants.APPLICATION_MESSAGES_KEY%>" : [
                                            {
                                                message : "<spring:message code="band.component.email-already-exists"></spring:message>",
                                                severity : "<%=MessageSeverity.FATAL%>",
                                                link : null
                                            }
                                        ]
                                    });
                                }

                                if (data.invitationExists == true) {
                                    showApplicationMessages({
                                        "<%=Constants.APPLICATION_MESSAGES_KEY%>" : [
                                            {
                                                message : "<spring:message code="band.component.invitation.exists"></spring:message>",
                                                severity : "<%=MessageSeverity.WARNING%>",
                                                link : null
                                            }
                                        ]
                                    });
                                }
                            }
                        });

                    });
                }
            }
            return false;
        });
    }


</script>