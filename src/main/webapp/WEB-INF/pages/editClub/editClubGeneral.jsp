<%@ page import="com.robynem.mit.web.util.Constants" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<jsp:include page="../common/messagesDisplayerAsync.jsp"></jsp:include>

<div class="row">

    <div class="col-md-12 saveClubLink">
        <span class="saveClubLink">
            <spring:message code="band.general.save-alert"></spring:message>

            <a id="saveGeneralInfoLink" href="#"><spring:message code="global.here"></spring:message> </a>
        </span>
    </div>

</div>

<div class="row">

    <!-- LOGO -->
    <div class="col-md-12 col-xs-12">

        <div class="row">

            <div class="col-md-12">
                <c:choose>
                    <c:when test="${not empty clubModel.logoImageId}">
                        <img src="${contextPath}/media/getImage?imageId=${clubModel.logoImageId}&size=MEDIUM"
                             class="img-responsive logoImage">
                    </c:when>
                    <c:otherwise>
                        <img src="${contextPath}/resources/images/profile_avatar.png" class="img-responsive logoImage">
                    </c:otherwise>
                </c:choose>

            </div>

        </div>

        <div class="row">

            <div class="col-md-12">
                <a id="editClubOpenImageUpload" href="#"><spring:message
                        code="club.load-logo"></spring:message> </a>
            </div>

        </div>

    </div>

</div>

<div class="row">
    <div class="col-md-12">
        <hr/>
    </div>
</div>

<div class="row">

    <!-- FORM FIELDS -->
    <form:form id="editClubGeneralForm" cssClass="formFields" action="${contextPath}/private/editClub/saveGeneralInfo" method="post" modelAttribute="clubModel" >
        <input type="hidden" name="currentTabIndex" value="0">

        <div class="col-md-12 col-xs-12">

            <!-- NAME -->
            <div class="row">

                <div class="col-md-3">
                    <label for="editClubName"><spring:message code="band.name"></spring:message></label>
                </div>
                <div class="col-md-9">
                    <input type="text" id="editClubName" name="name" class="form-control formField"/>
                </div>

            </div>

            <div class="row">
                <div class="col-md-12">
                    <hr/>
                </div>
            </div>

            <!-- CLUB GENRES -->
            <div class="row">
                <div class="col-md-12">
                    <label><spring:message code="band.genres"></spring:message> </label>
                </div>

                <div id="clubGenresList" class="col-md-12">

                </div>

                <div class="col-md-12">
                    <select id="clubGenresSelect" class="formSelect formField">
                        <option><spring:message  code="club.genres.placeholder"></spring:message></option>
                        <c:if test="${not empty clubGenresList}">
                            <c:forEach var="genre" items="${clubGenresList}">
                                <option value="${genre.id}"><spring:message code="${genre.resourceBundleCode}"></spring:message></option>
                            </c:forEach>
                        </c:if>
                    </select>
                </div>
            </div>

            <div class="row">
                <div class="col-md-12">
                    <hr/>
                </div>
            </div>

            <!-- TOWN -->
            <div class="row">
                <div class="col-md-3">
                    <label for="editClubTown"><spring:message code="profile.town"></spring:message> </label>
                </div>

                <div class="col-md-9">
                    <input type="text" class="form-control formField" id="editClubTown" name="town"
                           placeholder="<spring:message code="smart-search.insert.location"></spring:message>"/>
                    <input type="hidden" id="editClubPlaceId" name="placeId">
                </div>
            </div>

            <div class="row">
                <div class="col-md-12">
                    <hr/>
                </div>
            </div>

            <!-- WEB SITE -->
            <div class="row">

                <div class="col-md-3">
                    <label for="editClubWebSite"><spring:message code="band.web-site"></spring:message></label>
                </div>
                <div class="col-md-9">
                    <input type="text" id="editClubWebSite" name="webSite" class="form-control formField"/>
                </div>

            </div>

            <div class="row">
                <div class="col-md-12">
                    <hr/>
                </div>
            </div>

            <!-- DESCRIPTION -->
            <div class="row">
                <div class="col-md-10">
                    <label for="editClubDescription"><spring:message
                            code="club.description"></spring:message> </label>
                </div>

                <div class="col-md-2">
                    <img id="descriptionTooltip" src="${contextPath}/resources/images/Info_16x16.png"
                         title="<spring:message code="club.description.tooltip"></spring:message>">
                </div>
            </div>

            <div class="row">
                <div class="col-md-12">
                        <textarea id="editClubDescription" name="description" class="form-control formField"
                                  rows="10"
                                  maxlength="<%=Constants.BIOGRAPHY_MAX_LENGHT%>">${clubModel.description}</textarea>
                </div>
            </div>



            <div class="row">
                <!-- For mobile only -->
                <div class="col-md-12 forMobile" style="display: none;">
                                        <span><spring:message
                                                code="club.description.tooltip"></spring:message></span>
                </div>
            </div>

            <div class="row">
                <div class="col-md-12">
                    <span class="charsLeft"><spring:message
                            code="profile.musician-form.biography.characters-left"></spring:message> </span>
                    &nbsp;
                    <input type="text" id="descriptionCharsLeft" class="charsLeft" value="1000" style="max-width: 20%; font-size: 10px;"
                           readonly="readonly">
                </div>
            </div>

            <div class="row">
                <div class="col-md-12">
                    <hr/>
                </div>
            </div>

            <!-- OPENING INFO -->
            <div class="accordion" id="editClubOpeningInfoAccordion">
                <div class="accordion-group">
                    <div class="accordion-heading">
                        <a class="accordion-toggle" data-toggle="collapse" data-parent="#editClubOpeningInfoAccordion" href="#collapseOpeningInfo">
                            <span><spring:message code="club.opening-info.title"></spring:message> </span>
                        </a>
                    </div>
                    <div id="collapseOpeningInfo" class="accordion-body collapse in">
                        <div class="accordion-inner">

                            <!-- Opening info -->
                            <div class="row openingInfo">
                                <jsp:include page="editClubOpeningInfo.jsp"></jsp:include>
                            </div>


                        </div>
                    </div>
                </div>
            </div>


            <div class="row">
                <div class="col-md-12">
                    <hr/>
                </div>
            </div>

            <!-- CONTACTS -->
            <div class="accordion" id="editClubContactsAccordion">

                <div class="accordion-group">
                    <div class="accordion-heading">
                        <a class="accordion-toggle" data-toggle="collapse" data-parent="#editClubContactsAccordion" href="#collapseContacts">
                            <span><spring:message code="club.contacts.title"></spring:message> </span>
                        </a>
                    </div>
                    <div id="collapseContacts" class="accordion-body collapse in">
                        <div class="accordion-inner">

                            <!-- Email -->
                            <div class="row emailContacts">

                                <div class="col-md-12">
                                    <span><spring:message code="band.contacts.email-contacts"></spring:message> </span>
                                </div>

                                <!-- Contacts list -->
                                <div id="emailList" class="col-md-10">
                                    <c:forEach var="contact" items="${clubModel.emailContacts}" varStatus="status">
                                        <div id="email_${status.index}" class="row">
                                            <div class="col-md-10">
                                                <input type="email" name="emailContact" class="form-control emailText formField" value="${contact.value}" />
                                            </div>
                                            <div class="col-md-2">
                                                <img src="${contextPath}/resources/images/delete_32x32.png" class="img-responsive clickable" onclick="javascript:removeContactField('email_${status.index}')">
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>

                                <div id="addEmailContactLink" class="col-md-2">
                                    <img  src="${contextPath}/resources/images/add_32x32.png"
                                          class="img-responsive clickable"
                                          title="<spring:message code="band.contacts.email.add-new.tooltip"></spring:message>"
                                          onclick="javascript:addEmailContactField();">
                                </div>

                            </div>

                            <br />

                            <!-- Phone  -->
                            <div class="row phoneContacts">

                                <div class="col-md-12">
                                    <span><spring:message code="band.contacts.phone-contacts"></spring:message> </span>
                                </div>

                                <!-- Contacts list -->
                                <div id="phoneList" class="col-md-10">
                                    <c:forEach var="contact" items="${clubModel.phoneNumberContacts}" varStatus="status">
                                        <div id="phone_${status.index}" class="row">
                                            <div class="col-md-10">
                                                <input type="tel" name="phoneContact" class="form-control phoneText formField" value="${contact.value}"/>
                                            </div>
                                            <div class="col-md-2">
                                                <img src="${contextPath}/resources/images/delete_32x32.png" class="img-responsive clickable" onclick="javascript:removeContactField('phone_${status.index}')">
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>

                                <div id="addPhoneContactLink" class="col-md-2">
                                    <img  src="${contextPath}/resources/images/add_32x32.png"
                                          class="img-responsive clickable"
                                          title="<spring:message code="band.contacts.phone.add-new.tooltip"></spring:message>"
                                          onclick="javascript:addPhoneContactField();">
                                </div>

                            </div>
                        </div>
                    </div>
                </div>


            </div>

        </div>
    </form:form>

</div>

<div id="editClubUploadLogoImageDialog" style="display: none;">
    <form id="editClubUploadImageForm" action="${contextPath}/private/editClub/uploadLogoImage" method="post" enctype="multipart/form-data">
        <div class="row">
            <div class="col-md-12">
                <span><spring:message code="profile.choose-profile-image.title"></spring:message> </span>
            </div>
        </div>

        <div class="row">
            <div class="col-md-8 col-xs-12">
                <input type="file" id="editClubPictureFile" name="pictureFile" class="form-control"/>
            </div>

            <div class="col-md-4 col-xs-12">
                <button id="editClubUploadImageButton" class="btn btn-default">
                    <spring:message code="profile.choose-profile-image.load"></spring:message>
                </button>
            </div>
        </div>

        <div class="row">
            <div class="col-md-12">
                <div  class="progress progress-striped">
                    <div id="progress" class="progress-bar progress-success" role="progressbar" aria-valuenow="0"
                         aria-valuemin="0" aria-valuemax="100" style="width:100%">
                        <span class="sr-only"></span>
                        <span ></span>
                    </div>
                </div>
            </div>
        </div>

    </form>
</div>

<script type="text/javascript">
    var OI_APPEND_EXISTING = true; // If add new OpeningInfo needs to append stored ones.

    $(function() {
        initGeneralFormFields();

        initLocationAutocomplete();

        initLogoLoad();
    });

    function initLogoLoad() {
        $("#editClubUploadLogoImageDialog").dialog({
            modal: true,
            height: 'auto',
            width: 'auto',
            autoOpen: false,
            position: {my: "center bottom", at: "center top"},
            closeText: 'X',
            buttons: [
                {
                    text: "Cancel",
                    click: function () {
                        $(this).dialog("close");
                    }

                    // Uncommenting the following line would hide the text,
                    // resulting in the label being used as a tooltip
                    //showText: false
                }
            ]
        });

        $("#editClubOpenImageUpload").click(function () {
            openLogoLoadDialog()
        });

        $("#editClubUploadImageButton").click(function() {

            var file = $.trim($("#editClubPictureFile").val());

            if (file != "") {
                execInSession(function() {
                    $("#editClubUploadImageForm").submit();
                });
            }

            return false;
        });
    }

    function openLogoLoadDialog() {

        $("#progress").parent().hide();

        var options = {
            beforeSubmit: function() {
                return execInSession(null);
            },
            beforeSend: function()
            {
                $("#progress").parent().show();

                $("#progress").addClass("active");
                $("#progress").attr("aria-valuenow", "0");
                $("#progress").children("span").html("0%");
            },
            uploadProgress: function(event, position, total, percentComplete)
            {
                $("#progress").attr("aria-valuenow", percentComplete);
                $("#progress").children("span").html(percentComplete + "%");

            },
            success: function(data)
            {

                showApplicationMessages(data);

                $("#progress").removeClass("active");
                $("#progress").attr("aria-valuenow", "100");
                $("#progress").children("span").html("100");

                //console.log("uploadedImageId: " + data.uploadedImageId);
                if (data.uploadedImageId != null) {
                    $(".logoImage").attr("src", "${contextPath}/media/getImage?imageId=" + data.uploadedImageId + "&size=MEDIUM&" + new Date().getTime());
                    console.log("Img src attr: " + $(".logoImage").attr("src"));
                    setTimeout(function() {$("#editClubUploadLogoImageDialog").dialog("close", 600);}, 600);

                    GENERAL_TAB_MODIFIED = true;

                    showClubStatus();
                }
            },
            complete: function(response)
            {
                $("#editClubPictureFile").val("");
            },
            error: function()
            {

            }

        };

        $("#editClubUploadImageForm").ajaxForm(options);

        $("#editClubUploadLogoImageDialog").dialog("open");
    }

    function initGeneralFormFields() {

        initGeneralForm();

        $("#saveGeneralInfoLink").click(function() {
            $("#editClubGeneralForm").submit();

        });

        var genresTagViewer = new TagViewer({
            selectId: "clubGenresSelect",
            tagContainerId: "clubGenresList",
            paramName: "genres",
            deleteTooltip: "<spring:message code="band.genres.delete-tooltip"></spring:message>",
            deleteHandler : function (tag) {
                GENERAL_TAB_MODIFIED = true;

                return true;
            }
        });

        $("#editClubName").val("${clubModel.name}");

        <c:if test="${not empty clubModel.genres}">
            <c:forEach var="genre" items="${clubModel.genres}">
                genresTagViewer.addTagById("${genre}");
            </c:forEach>
        </c:if>

        $("#editClubTown").val("${clubModel.town}");
        $("#editClubPlaceId").val("${clubModel.placeId}");

        $("#editClubWebSite").val("${clubModel.webSite}");

        countBioCharsLeft();

        $("#editClubDescription").keyup(function () {
            countBioCharsLeft();
        });

        // Form validation
        $("#editClubGeneralForm").on("submit", function(e) {

            var validated = validateGeneralForm();

            if (validated) {
                $.blockUI();
            } else {
                e.preventDefault();
            }

        });

        // Set general form modified on formField change
        $("#editClubGeneralForm .formField").change(function () {
            GENERAL_TAB_MODIFIED = true;
        }).keydown(function () {
            GENERAL_TAB_MODIFIED = true;
        });

        initOpeningInfo();
    }

    function initGeneralForm() {
        var options = {
            beforeSubmit: function() {
                var doSubmit = execInSession(null);

                if (doSubmit) {
                    doSubmit = validateGeneralForm();
                }

                return doSubmit
            },
            beforeSend: function()
            {

            },
            uploadProgress: function(event, position, total, percentComplete)
            {


            },
            success: function(data)
            {
                $("#tabs-general").html(data);

                // If publish command is requested.
                //console.log("General form aved: DO_PUBLISH: " + DO_PUBLISH);
                if (DO_PUBLISH == true) {
                    $("#publishClubForm").submit();
                }

                // If tab switch is requested.
                if (SWITCH_TAB_TO != null) {
                    var index = SWITCH_TAB_TO;
                    SWITCH_TAB_TO = null;
                    $("#tabs").tabs("option", "active", index);
                }

                showClubStatus();
            },
            complete: function(response)
            {

            },
            error: function()
            {

            }

        };

        $("#editClubGeneralForm").ajaxForm(options);
    }

    <c:set var="descriptionMaxLenght" >
        <%=Constants.BIOGRAPHY_MAX_LENGHT%>
    </c:set>

    function countBioCharsLeft() {
        var contentValue = $("#editClubDescription").val();
        var numOfReturn = (contentValue.match(/\n/g) || []).length;

        // Since the length of the content doesn't consider the carriage in carriage return, I add those manually
        var charsLeft = ${descriptionMaxLenght} -(contentValue.length + numOfReturn);


        if (charsLeft < 0) {
            $("#descriptionCharsLeft").val("0");
        } else {
            $("#descriptionCharsLeft").val(charsLeft);
        }
    }

    function initLocationAutocomplete() {
        var accepted_google_types = ["locality", "administrative_area_level_2"];
        var CHOOSE_A_LOCALITY_MESSAGE = '<spring:message code="smart-search.choose-a-location"></spring:message>!';

        var placeId;
        var placeDescription;

        var locationField = document.getElementById("editClubTown");

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
                    $("#editClubPlaceId").val("");
                    $("#editClubTown").val("");

                    showApplicationMessages({
                        "<%=Constants.APPLICATION_MESSAGES_KEY%>": [
                            {
                                severity: "WARNING",
                                link: null,
                                message: CHOOSE_A_LOCALITY_MESSAGE
                            }
                        ]
                    });
                } else {
                    placeId = place.place_id;

                    $("#editClubPlaceId").val(placeId);
                }
            } else {
                $("#editClubPlaceId").val("");
                $("#editClubTown").val("");

                showApplicationMessages({
                    "<%=Constants.APPLICATION_MESSAGES_KEY%>": [
                        {
                            severity: "WARNING",
                            link: null,
                            message: CHOOSE_A_LOCALITY_MESSAGE
                        }
                    ]
                });
            }


        });
    }

    function initOpeningInfo() {
        $(".timeText").timepicker({ 'timeFormat': 'H:i' });
    }

    function addEmailContactField() {

        var id = new Date().getMilliseconds();
        id = "email_" + id;

        var row = $("<div class=\"row\"></row>").attr("id", id);
        var col1 = $("<div class=\"col-md-10\"></div>");
        var col2 = $("<div class=\"col-md-2\"></div>");

        var input = $("<input type=\"email\" name=\"emailContact\" class=\"form-control emailText formField\" />");
        var img = $("<img  class='img-responsive clickable' />")
                .attr("src", "${contextPath}" + "/resources/images/delete_32x32.png")
                .bind("click", null, function() {
                    removeContactField(id);
                });

        col1 = col1.append(input);
        col2 = col2.append(img)

        row = row.append(col1).append(col2);

        row.appendTo($("#emailList"));

        GENERAL_TAB_MODIFIED = true;

    }

    function addPhoneContactField() {
        var id = new Date().getMilliseconds();
        id = "phone_" + id;

        var row = $("<div class=\"row\"></row>").attr("id", id);;
        var col1 = $("<div class=\"col-md-10\"></div>");
        var col2 = $("<div class=\"col-md-2\"></div>");

        var input = $("<input type=\"tel\" name=\"phoneContact\" class=\"form-control phoneText formField\" />");
        var img = $("<img  class='img-responsive clickable' />")
                .attr("src", "${contextPath}" + "/resources/images/delete_32x32.png")
                .bind("click", null, function() {
                    removeContactField(id);
                });;

        col1 = col1.append(input);
        col2 = col2.append(img)

        row = row.append(col1).append(col2);

        row.appendTo($("#phoneList"));

        GENERAL_TAB_MODIFIED = true;
    }

    function removeContactField(id) {
        $("#" + id).remove();
        GENERAL_TAB_MODIFIED = true;
    }

    function removeOpeningInfo(id) {
        $("#" + id).remove();
        GENERAL_TAB_MODIFIED = true;
    }

    function addOpeningInfo() {

        execInSession(function () {

            //console.log("Param: " + $.param(getOpeningInfoData(), true));

            $.ajax({
                url : "${contextPath}/private/editClub/addEmptyOpeningInfo",
                data : getOpeningInfoData(),
                dataType : "html",
                type : "post",
                async : true,
                cache : false,
                global : false,
                success : function (data) {
                    $(".openingInfo").html(data);
                }
            });
        });

        GENERAL_TAB_MODIFIED = true;
    }

    function getOpeningInfoData_() {
        var data = new Array();

        $(".OI_dataRow").each(function () {
            var model = new Object();

            /*console.log("start day: " + $(this).find("[name=OI_startDay] option:selected").val());
            console.log("start hour: " + $(this).find("[name=OI_startHour]").val());*/

            model.startDay = $(this).find("[name=OI_startDay] option:selected").val();
            model.endDay = $(this).find("[name=OI_endDay] option:selected").val();
            model.startHour = $(this).find("[name=OI_startHour]").val();
            model.endHour = $(this).find("[name=OI_endHour]").val();
            model.opened = $(this).find("[name=OI_opened]").attr("checked");

            data.push(model);
        });



        return data;
    }

    function getOpeningInfoData() {
        var data = new FormData();

        $(".OI_dataRow").each(function () {
            var model = new Object();

            /*console.log("start day: " + $(this).find("[name=OI_startDay] option:selected").val());
             console.log("start hour: " + $(this).find("[name=OI_startHour]").val());*/

            data.append("startDay", $(this).find("[name=OI_startDay] option:selected").val());
            data.append("endDay", $(this).find("[name=OI_endDay] option:selected").val());
            data.append("startHour", $(this).find("[name=OI_startHour]").val());
            data.append("endHour", $(this).find("[name=OI_endHour]").val());
            data.append("opened", $(this).find("[name=OI_opened]").attr("checked"));

        });



        return data;
    }


</script>