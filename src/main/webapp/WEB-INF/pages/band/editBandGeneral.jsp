<%@ page import="com.robynem.mit.web.util.Constants" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:include page="../common/messagesDisplayerAsync.jsp"></jsp:include>

<div class="row">

    <div class="col-md-12 saveBandLink">
        <span class="saveBandLink">
            <spring:message code="band.general.save-alert"></spring:message>

            <a id="saveGeneralInfoLink" href="#"><spring:message code="global.here"></spring:message> </a>
        </span>
    </div>

</div>

<div class="row">

    <!-- LOGO -->
    <div class="col-md-4 col-xs-12">

        <div class="row">

            <div class="col-md-12">
                <c:choose>
                    <c:when test="${not empty bandModel.logoImageId}">
                        <img src="${contextPath}/media/getBandImage?imageId=${bandModel.logoImageId}&size=MEDIUM"
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
                <a id="editBandOpenImageUpload" href="#"><spring:message
                        code="band.load-logo"></spring:message> </a>
            </div>

        </div>

    </div>

    <!-- FORM FIELDS -->
    <form:form id="editBandGeneralForm" cssClass="formFields" action="${contextPath}/private/editBand/save" method="post" modelAttribute="bandModel" >
        <input type="hidden" name="currentTabIndex" value="0">

        <div class="col-md-8 col-xs-12">

            <!-- NAME -->
            <div class="row">

                <div class="col-md-3">
                    <label for="editBandName"><spring:message code="band.name"></spring:message></label>
                </div>
                <div class="col-md-9">
                    <input type="text" id="editBandName" name="name" class="form-control formField"/>
                </div>

            </div>

            <div class="row">
                <div class="col-md-12">
                    <hr/>
                </div>
            </div>

            <!-- BAND GENRES -->
            <div class="row">
                <div class="col-md-12">
                    <label><spring:message code="band.genres"></spring:message> </label>
                </div>

                <div id="bandGenresList" class="col-md-12">

                </div>

                <div class="col-md-12">
                    <select id="bandGenresSelect" class="formSelect formField">
                        <option><spring:message  code="band.genres.placeholder"></spring:message></option>
                        <c:if test="${not empty musicGenresList}">
                            <c:forEach var="genre" items="${musicGenresList}">
                                <option value="${genre.id}">${genre.name}</option>
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
                    <label for="editBandTown"><spring:message code="profile.town"></spring:message> </label>
                </div>

                <div class="col-md-9">
                    <input type="text" class="form-control formField" id="editBandTown" name="town"
                           placeholder="<spring:message code="smart-search.insert.location"></spring:message>"/>
                    <input type="hidden" id="editBandPlaceId" name="placeId">
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
                    <label for="editBandWebSite"><spring:message code="band.web-site"></spring:message></label>
                </div>
                <div class="col-md-9">
                    <input type="text" id="editBandWebSite" name="webSite" class="form-control formField"/>
                </div>

            </div>

            <div class="row">
                <div class="col-md-12">
                    <hr/>
                </div>
            </div>

            <!-- BIOGRAPHY -->
            <div class="row">
                <div class="col-md-4">
                    <label for="editBandBiography"><spring:message
                            code="profile.musician-form.biography"></spring:message> </label>
                </div>
                <div class="col-md-6">
                                        <textarea id="editBandBiography" name="biography" class="form-control formField"
                                                  rows="10"
                                                  maxlength="<%=Constants.BIOGRAPHY_MAX_LENGHT%>">${bandModel.biography}</textarea>
                </div>
                <div class="col-md-2">
                    <img id="biographyTooltip" src="${contextPath}/resources/images/Info_16x16.png"
                         title="<spring:message code="band.biography.tooltip"></spring:message>">
                </div>
                <!-- For mobile only -->
                <div class="col-md-12 forMobile" style="display: none;">
                                        <span><spring:message
                                                code="band.biography.tooltip"></spring:message></span>
                </div>
            </div>

            <div class="row">
                <div class="col-md-4">
                    &nbsp;
                </div>
                <div class="col-md-6">
                                        <span class="charsLeft"><spring:message
                                                code="profile.musician-form.biography.characters-left"></spring:message> </span>
                    &nbsp;
                    <input type="text" id="biographyCharsLeft" class="charsLeft" value="1000" style="max-width: 20%; font-size: 10px;"
                           readonly="readonly">
                </div>
                <div class="col-md-2">
                    &nbsp;
                </div>
            </div>

            <div class="row">
                <div class="col-md-12">
                    <hr/>
                </div>
            </div>

            <!-- CONTACTS -->
            <div class="accordion" id="editBandContactsAccordion">

                <div class="accordion-group">
                    <div class="accordion-heading">
                        <a class="accordion-toggle" data-toggle="collapse" data-parent="#editBandContactsAccordion" href="#collapseContacts">
                            <span><spring:message code="band.contacts.title"></spring:message> </span>
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
                                    <c:forEach var="contact" items="${bandModel.emailContacts}" varStatus="status">
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
                                    <c:forEach var="contact" items="${bandModel.phoneNumberContacts}" varStatus="status">
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

<div id="editBandUploadLogoImageDialog" style="display: none;">
    <form id="editBandUploadImageForm" action="${contextPath}/private/editBand/uploadLogoImage" method="post" enctype="multipart/form-data">
        <div class="row">
            <div class="col-md-12">
                <span><spring:message code="profile.choose-profile-image.title"></spring:message> </span>
            </div>
        </div>

        <div class="row">
            <div class="col-md-8 col-xs-12">
                <input type="file" id="editBandPictureFile" name="pictureFile" class="form-control"/>
            </div>

            <div class="col-md-4 col-xs-12">
                <button id="editBandUploadImageButton" class="btn btn-default">
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
    $(function() {
        initGeneralFormFields();

        initLocationAutocomplete();

        initLogoLoad();
    });

    function initLogoLoad() {
        $("#editBandUploadLogoImageDialog").dialog({
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

        $("#editBandOpenImageUpload").click(function () {
            openLogoLoadDialog()
        });

        $("#editBandUploadImageButton").click(function() {

            var file = $.trim($("#editBandPictureFile").val());

            if (file != "") {
                execInSession(function() {
                    $("#editBandUploadImageForm").submit();
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
                    $(".logoImage").attr("src", "${contextPath}/media/getBandImage?imageId=" + data.uploadedImageId + "&size=MEDIUM&" + new Date().getTime());
                    console.log("Img src attr: " + $(".logoImage").attr("src"));
                    setTimeout(function() {$("#editBandUploadLogoImageDialog").dialog("close", 600);}, 600);

                    GENERAL_TAB_MODIFIED = true;

                    showBandStatus();
                }
            },
            complete: function(response)
            {
                $("#editBandPictureFile").val("");
            },
            error: function()
            {

            }

        };

        $("#editBandUploadImageForm").ajaxForm(options);

        $("#editBandUploadLogoImageDialog").dialog("open");
    }

    function initGeneralFormFields() {

        initGeneralForm();

        $("#saveGeneralInfoLink").click(function() {
            $("#editBandGeneralForm").submit();

        });

        var genresTagViewer = new TagViewer({
            selectId: "bandGenresSelect",
            tagContainerId: "bandGenresList",
            paramName: "genres",
            deleteTooltip: "<spring:message code="band.genres.delete-tooltip"></spring:message>",
            deleteHandler : function (tag) {
                GENERAL_TAB_MODIFIED = true;

                console.log("formField modified!");

                return true;
            }
        });

        $("#editBandName").val("${bandModel.name}");

        <c:if test="${not empty bandModel.genres}">
            <c:forEach var="genre" items="${bandModel.genres}">
                genresTagViewer.addTagById("${genre}");
            </c:forEach>
        </c:if>

        $("#editBandTown").val("${bandModel.town}");
        $("#editBandPlaceId").val("${bandModel.placeId}");

        $("#editBandWebSite").val("${bandModel.webSite}");

        countBioCharsLeft();

        $("#editBandBiography").keyup(function () {
            countBioCharsLeft();
        });

        // Form validation
        $("#editBandGeneralForm").on("submit", function(e) {

            var validated = validateGeneralForm();

            if (validated) {
                $.blockUI();
            } else {
                e.preventDefault();
            }

        });

        // Set general form modified on formField change
        $("#editBandGeneralForm .formField").change(function () {
            GENERAL_TAB_MODIFIED = true;

            console.log("formField modified!");
        }).keydown(function () {
            GENERAL_TAB_MODIFIED = true;

            console.log("formField modified!");
        });
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
                    $("#publishBandForm").submit();
                }

                showBandStatus();
            },
            complete: function(response)
            {

            },
            error: function()
            {

            }

        };

        $("#editBandGeneralForm").ajaxForm(options);
    }

    <c:set var="biographyMaxLenght" >
        <%=Constants.BIOGRAPHY_MAX_LENGHT%>
    </c:set>

    function countBioCharsLeft() {
        var contentValue = $("#editBandBiography").val();
        var numOfReturn = (contentValue.match(/\n/g) || []).length;

        // Since the length of the content doesn't consider the carriage in carriage return, I add those manually
        var charsLeft = ${biographyMaxLenght} -(contentValue.length + numOfReturn);


        if (charsLeft < 0) {
            $("#biographyCharsLeft").val("0");
        } else {
            $("#biographyCharsLeft").val(charsLeft);
        }
    }

    function initLocationAutocomplete() {
        var accepted_google_types = ["locality", "administrative_area_level_2"];
        var CHOOSE_A_LOCALITY_MESSAGE = '<spring:message code="smart-search.choose-a-location"></spring:message>!';

        var placeId;
        var placeDescription;

        var locationField = document.getElementById("editBandTown");

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
                    $("#editBandPlaceId").val("");
                    $("#editBandTown").val("");

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

                    $("#editBandPlaceId").val(placeId);
                }
            } else {
                $("#editBandPlaceId").val("");
                $("#editBandTown").val("");

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


</script>