<%@ page import="com.robynem.mit.web.util.UserGender" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="com.robynem.mit.web.util.Constants" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%--
  Created by IntelliJ IDEA.
  User: robyn_000
  Date: 26/12/2015
  Time: 20:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="row">
    <!-- PICTURE -->
    <div class="col-md-4 col-xs-12">

        <div class="row">

            <div class="col-md-12">
                <c:choose>
                    <c:when test="${not empty profileModel.profileImageId}">
                        <img src="${contextPath}/media/getUserImage?imageId=${profileModel.profileImageId}&size=MEDIUM"
                             class="img-responsive">
                    </c:when>
                    <c:otherwise>
                        <img src="${contextPath}/resources/images/profile_avatar.png" class="img-responsive">
                    </c:otherwise>
                </c:choose>

            </div>

        </div>

        <div class="row">

            <div class="col-md-12">
                <a id="editProfileOpenImageUpload" href="#"><spring:message
                        code="profile.choose-profile-image"></spring:message> </a>
            </div>

        </div>

    </div>


    <!-- FORM FIELDS -->
    <div class="col-md-8 col-xs-12">

        <!-- FIRST NAME -->
        <div class="row">
            <div class="col-md-3">
                <label for="editProfileFirstName"><spring:message code="profile.first-name"></spring:message> </label>
            </div>

            <div class="col-md-7">
                <div id="editProfileFirstNameView">
                    <span>${profileModel.firstName}</span>
                </div>

                <div id="editProfileFirstNameEdit" style="display: none;">
                    <input type="text" class="form-control" id="editProfileFirstName"
                           value="${profileModel.firstName}"/>
                </div>

            </div>


            <div class="col-md-2">
                <div id="editProfileFirstNameButtonEdit">
                    <a href="#"><spring:message code="profile.button.edit"></spring:message></a>
                </div>
                <div id="editProfileFirstNameButtonSave" style="display: none;">
                    <a href="#" class="save"><spring:message code="profile.button.save"></spring:message></a>
                    &nbsp;
                    <a href="#" class="cancel"><spring:message code="global.cancel"></spring:message></a>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="col-md-12">
                <hr/>
            </div>
        </div>


        <!-- LAST NAME -->
        <div class="row">
            <div class="col-md-3">
                <label for="editProfileLastName"><spring:message code="profile.last-name"></spring:message> </label>
            </div>

            <div class="col-md-7">
                <div id="editProfileLastNameView">
                    <span>${profileModel.lastName}</span>
                </div>

                <div id="editProfileLastNameEdit" style="display: none;">
                    <input type="text" class="form-control" id="editProfileLastName" value="${profileModel.lastName}"/>
                </div>

            </div>


            <div class="col-md-2">
                <div id="editProfileLastNameButtonEdit">
                    <a href="#"><spring:message code="profile.button.edit"></spring:message></a>
                </div>
                <div id="editProfileLastNameButtonSave" style="display: none;">
                    <a href="#" class="save"><spring:message code="profile.button.save"></spring:message></a>
                    &nbsp;
                    <a href="#" class="cancel"><spring:message code="global.cancel"></spring:message></a>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="col-md-12">
                <hr/>
            </div>
        </div>


        <!-- GENDER -->
        <div class="row">
            <div class="col-md-3">
                <label for="editProfileGender"><spring:message code="profile.gender"></spring:message> </label>
            </div>

            <div class="col-md-7">
                <div id="editProfileGenderView">
                    <span>
                        <c:if test="${not empty profileModel.gender}">
                            <spring:message code="global.gender.${profileModel.gender}"></spring:message>
                        </c:if>
                    </span>
                </div>

                <div id="editProfileGenderEdit" style="display: none;">
                    <select id="editProfileGender" class="form-control">
                        <option></option>
                        <option value="<%=UserGender.MALE%>"><spring:message
                                code="global.gender.MALE"></spring:message></option>
                        <option value="<%=UserGender.FEMALE%>"><spring:message
                                code="global.gender.FEMALE"></spring:message></option>
                    </select>
                </div>

            </div>


            <div class="col-md-2">
                <div id="editProfileGenderButtonEdit">
                    <a href="#"><spring:message code="profile.button.edit"></spring:message></a>
                </div>
                <div id="editProfileGenderButtonSave" style="display: none;">
                    <a href="#" class="save"><spring:message code="profile.button.save"></spring:message></a>
                    &nbsp;
                    <a href="#" class="cancel"><spring:message code="global.cancel"></spring:message></a>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="col-md-12">
                <hr/>
            </div>
        </div>


        <!-- EMAIL -->
        <div class="row">
            <div class="col-md-3">
                <label><spring:message code="profile.email"></spring:message> </label>
            </div>

            <div class="col-md-8">
                <div id="editProfileEmailView">
                    <span>${profileModel.emailAddress}</span>
                </div>

            </div>

        </div>

        <div class="row">
            <div class="col-md-12">
                <hr/>
            </div>
        </div>


        <!-- BIRTH DATE -->
        <div class="row">
            <div class="col-md-3">
                <label for="editProfileBirthDate"><spring:message code="profile.birth-date"></spring:message> </label>
            </div>

            <div class="col-md-7">
                <div id="editProfileBirthDateView">
                    <span>${profileModel.birthDate}</span>
                </div>

                <div id="editProfileBirthDateEdit" style="display: none;">
                    <input type="text" class="form-control" id="editProfileBirthDate" value="${profileModel.birthDate}"
                           readonly="readonly"/>
                </div>

            </div>


            <div class="col-md-2">
                <div id="editProfileBirthDateButtonEdit">
                    <a href="#"><spring:message code="profile.button.edit"></spring:message></a>
                </div>
                <div id="editProfileBirthDateButtonSave" style="display: none;">
                    <a href="#" class="save"><spring:message code="profile.button.save"></spring:message></a>
                    &nbsp;
                    <a href="#" class="cancel"><spring:message code="global.cancel"></spring:message></a>
                </div>
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
                <label for="editProfileTown"><spring:message code="profile.town"></spring:message> </label>
            </div>

            <div class="col-md-7">
                <div id="editProfileTownView">
                    <span>${profileModel.town}</span>
                </div>

                <div id="editProfileTownEdit" style="display: none;">
                    <input type="text" class="form-control" id="editProfileTown" value="${profileModel.town}"
                           placeholder="<spring:message code="smart-search.insert.location"></spring:message>"/>
                    <input type="hidden" id="editProfilePlaceId" value="${profileModel.placeId}">
                </div>

            </div>


            <div class="col-md-2">
                <div id="editProfileTownButtonEdit">
                    <a href="#"><spring:message code="profile.button.edit"></spring:message></a>
                </div>
                <div id="editProfileTownButtonSave" style="display: none;">
                    <a href="#" class="save"><spring:message code="profile.button.save"></spring:message></a>
                    &nbsp;
                    <a href="#" class="cancel"><spring:message code="global.cancel"></spring:message></a>
                </div>
            </div>

        </div>

        <div class="row">
            <div class="col-md-12">
                <hr/>
            </div>
        </div>

        <!-- MUSICIAN FORM -->
        <br/>
        <br/>
        <form:form id="musicianForm" action="${contextPath}/private/profile/saveMusicianForm" method="post"
                   modelAttribute="musicianModel" onsubmit="javascript:$.blockUI();">

            <div class="row">
                <div class="col-md-12">


                    <div class="row">
                        <div class="col-md-1 col-xs-12">
                            <input type="checkbox" id="editProfileMusician" name="musician" value="true"/>
                        </div>

                        <div class="col-md-11 col-xs-12">
                            <label for="editprofilemusician">
                                <spring:message code="profile.musician-form.title"></spring:message>
                            </label>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-12">
                            <a name="goToMusicianForm" ></a>
                            <fieldset style="display: none;">

                                <!-- SINGER -->
                                <div class="row">
                                    <div class="col-md-12">
                                        <input type="checkbox" id="editProfileSinger" name="singer" value="true"/>
                                        &nbsp;
                                        <label for="editProfileSinger"><spring:message
                                                code="profile.musician-form.singer"></spring:message> </label>
                                    </div>
                                </div>

                                <div class="row line">
                                    <div class="col-md-12">
                                        <hr/>
                                    </div>
                                </div>

                                <!-- DJ -->
                                <div class="row">
                                    <div class="col-md-12">
                                        <input type="checkbox" id="editProfileDiscJockey" name="discJockey" value="true"/>
                                        &nbsp;
                                        <label for="editProfileDiscJockey"><spring:message
                                                code="profile.musician-form.dj"></spring:message> </label>
                                    </div>
                                </div>

                                <div class="row line">
                                    <div class="col-md-12">
                                        <hr/>
                                    </div>
                                </div>


                                <!-- PLAYED INSTRUMENTS -->
                                <div class="row">
                                    <div class="col-md-12">
                                        <label><spring:message
                                                code="profile.musician-form.played-instruments"></spring:message> </label>
                                    </div>

                                    <div id="playedInstrumentsList" class="col-md-12">

                                    </div>

                                    <div class="col-md-12">
                                        <select id="playedInstrumentsSelect" class="formSelect">
                                            <option><spring:message
                                                    code="profile.musician-form.played-instruments-placeholder"></spring:message></option>
                                            <c:if test="${not empty musicalInstrumentsList}">
                                                <c:forEach var="instr" items="${musicalInstrumentsList}">
                                                    <option value="${instr.id}">${instr.name}</option>
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

                                <!-- PREFERRED MUSIC GENRES -->
                                <div class="row">
                                    <div class="col-md-12">
                                        <label><spring:message
                                                code="profile.musician-form.preferred-music-genres"></spring:message> </label>
                                    </div>

                                    <div id="preferredMusicGenresList" class="col-md-12">

                                    </div>

                                    <div class="col-md-12">
                                        <select id="preferredMusicGenresSelect" class="formSelect">
                                            <option><spring:message
                                                    code="profile.musician-form.preferred.music.genres-placeholder"></spring:message></option>
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

                                <!-- ENGAGEMENT AVAILABLE -->
                                <div class="row">
                                    <div class="col-md-12">
                                        <input type="checkbox" id="engagementAvailable" name="engagementAvailable"
                                               value="true"/>
                                        &nbsp;
                                        <label for="engagementAvailable"><spring:message
                                                code="profile.musician-form.engagement-available"></spring:message> </label>
                                        &nbsp;
                                        <img id="engagementAvailableTooltip" src="${contextPath}/resources/images/Info_16x16.png"
                                             title="<spring:message code="profile.musician-form.engagement-available.tooltip"></spring:message>">
                                    </div>

                                    <!-- For mobile only -->
                                    <div class="col-md-12 forMobile" style="display: none;">
                                        <span><spring:message
                                                code="profile.musician-form.engagement-available.tooltip"></spring:message></span>
                                    </div>
                                </div>

                                <div class="row">
                                    <div class="col-md-12">
                                        <hr/>
                                    </div>
                                </div>

                                <!-- MUSIC MASTER -->
                                <div class="row">
                                    <div class="col-md-12">
                                        <input type="checkbox" id="musicMaster" name="musicMaster" value="true"/>
                                        &nbsp;
                                        <label for="musicMaster"><spring:message
                                                code="profile.musician-form.music-master"></spring:message> </label>
                                        &nbsp;
                                        <img id="musicMasterTooltip" src="${contextPath}/resources/images/Info_16x16.png"
                                             title="<spring:message code="profile.musician-form.music-master.tooltip"></spring:message>">
                                    </div>

                                    <!-- For mobile only -->
                                    <div class="col-md-12 forMobile" style="display: none;">
                                        <span><spring:message
                                                code="profile.musician-form.music-master.tooltip"></spring:message></span>
                                    </div>
                                </div>

                                <!-- Music master Instruments -->
                                <div class="row">
                                    <div class="col-md-12">
                                        <label><spring:message
                                                code="profile.musician-form.music-master.teached-instruments"></spring:message> </label>
                                    </div>

                                    <div id="musicMasterInstrumentsList" class="col-md-12">

                                    </div>

                                    <div class="col-md-12">
                                        <select id="musicMasterInstrumentsSelect" class="formSelect">
                                            <option><spring:message
                                                    code="profile.musician-form.played-instruments-placeholder"></spring:message></option>
                                            <c:if test="${not empty musicalInstrumentsList}">
                                                <c:forEach var="instr" items="${musicalInstrumentsList}">
                                                    <option value="${instr.id}">${instr.name}</option>
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

                                <!-- BIOGRAPHY -->
                                <div class="row">
                                    <div class="col-md-4">
                                        <label for="editProfileBiography"><spring:message
                                                code="profile.musician-form.biography"></spring:message> </label>
                                    </div>
                                    <div class="col-md-6">
                                        <textarea id="editProfileBiography" name="biography" class="form-control"
                                                  rows="10"
                                                  maxlength="<%=Constants.BIOGRAPHY_MAX_LENGHT%>">${musicianModel.biography}</textarea>
                                    </div>
                                    <div class="col-md-2">
                                        <img id="biographyTooltip" src="${contextPath}/resources/images/Info_16x16.png"
                                             title="<spring:message code="profile.musician-form.biography.tooltip"></spring:message>">
                                    </div>
                                    <!-- For mobile only -->
                                    <div class="col-md-12 forMobile" style="display: none;">
                                        <span><spring:message
                                                code="profile.musician-form.biography.tooltip"></spring:message></span>
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
                                        <input type="text" id="biographyCharsLeft" class="charsLeft" value="1000"
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
                            </fieldset>

                            <!-- BUTTONS -->
                            <div class="row musicianFormButtons">
                                <div class="col-md-12">
                                    <button id="editProfileMusicianSaveButton" class="btn btn-default">
                                        <spring:message code="profile.musician-form.save-button"></spring:message>
                                    </button>
                                </div>
                            </div>


                        </div>
                    </div>


                </div>
            </div>

        </form:form>

    </div>
</div>

<div id="editProfileUploadProfileImageDialog" style="display: none;">
    <form action="${contextPath}/private/profile/uploadProfileImage" method="post" enctype="multipart/form-data"
          onsubmit="javascript: $.blockUI();">
        <div class="row">
            <div class="col-md-12">
                <span><spring:message code="profile.choose-profile-image.title"></spring:message> </span>
            </div>
        </div>

        <div class="row">
            <div class="col-md-8 col-xs-12">
                <input type="file" id="editProfilePictureFile" name="pictureFile" class="form-control"/>
            </div>

            <div class="col-md-4 col-xs-12">
                <button id="editProfileUploadImageButton" class="btn btn-default">
                    <spring:message code="profile.choose-profile-image.load"></spring:message>
                </button>
            </div>
        </div>
    </form>
</div>


<script type="text/javascript">

    var BASE_URL = "${contextPath}/private/profile";
    var MALE = "<spring:message code="global.gender.MALE" ></spring:message>";
    var FEMALE = "<spring:message code="global.gender.FEMALE" ></spring:message>";

    var GENDER_MAPPING = {};
    GENDER_MAPPING["MALE"] = MALE;
    GENDER_MAPPING["FEMALE"] = FEMALE;


    $(function () {
        initEditSaveControls();

        initFields();

        initMusicianForm();


    });

    function initFields() {
        $("#editProfileBirthDate").datepicker({
            dateFormat: "${dateFormat}",
            buttonImageOnly: true,
            changeYear: true,
            changeMonth: true,
            maxDate: 0,
            yearRange: "1900:<%=Calendar.getInstance().get(Calendar.YEAR)%>"
        });

        $("#editProfileBirthDate").val("${profileModel.birthDate}");
        $("#editProfileBirthDate").datepicker("setDate", new Date(${profileModel.birthDateMillis}));

        $("#editProfileGender").val("${profileModel.gender}");

        // PROFILE PICTURE UPLOAD
        $("#editProfileUploadProfileImageDialog").dialog({
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

        $("#editProfileOpenImageUpload").click(function () {
            $("#editProfileUploadProfileImageDialog").dialog("open");
        });

        $("#editProfileUploadImageButton").click(function () {
            var file = $.trim($("#editProfilePictureFile").val());

            if (file != "") {

                if (window.FileReader) {
                    var fileObj = document.getElementById("editProfilePictureFile");
                    //alert(fileObj.size);
                    console.log("File size: " + fileObj.size);
                }

                return true;
            } else {
                return false;
            }
        });

        /*Google location autocomplete*/
        initLocationAutocomplete();
    }

    function initLocationAutocomplete() {
        var accepted_google_types = ["locality", "administrative_area_level_2"];
        var CHOOSE_A_LOCALITY_MESSAGE = '<spring:message code="smart-search.choose-a-location"></spring:message>!';
        var saveLink = $("#editProfileTownButtonSave").children("a");

        var placeId;
        var placeDescription;

        var locationField = document.getElementById("editProfileTown");

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

                    $("#editProfilePlaceId").val("");
                    $("#editProfileTown").val("");

                    saveLink.attr("disabled", true);
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
                    saveLink.attr("disabled", false);

                    $("#editProfilePlaceId").val(placeId);
                }
            } else {
                saveLink.attr("disabled", true);
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

    function initEditSaveControls() {
        // Validation messages
        var FIRST_NAME_INVALID = "<spring:message code="profile.validation.first-name-invalid"></spring:message>";
        var LAST_NAME_INVALID = "<spring:message code="profile.validation.last-name-invalid"></spring:message>";
        var GENDER_INVALID = "<spring:message code="profile.validation.gender-invalid"></spring:message>";
        var BIRTH_DATE_INVALID = "<spring:message code="profile.validation.birth-date-invalid"></spring:message>";
        var TOWN_INVALID = "<spring:message code="profile.validation.town-invalid"></spring:message>";

        var firstNameOriginal = "${profileModel.firstName}";
        var lastNameOriginal = "${profileModel.lastName}";
        var genderOriginal = "${profileModel.gender}";
        var birthDateOriginal = "${profileModel.birthDate}";
        var birthDateObjectOriginal = new Date(${profileModel.birthDateMillis});
        var placeIdDateOriginal = "${profileModel.placeId}";
        var townOriginal = "${profileModel.town}";

        // First Name
        $("#editProfileFirstNameButtonEdit").children("a").click(function () {
            setEditMode("FirstName");
        });

        $("#editProfileFirstNameButtonSave").children(".cancel").click(function () {
            $("#editProfileFirstName").val(firstNameOriginal);
            setViewMode("FirstName");
        });

        $("#editProfileFirstNameButtonSave").children(".save").click(function () {

            var value = $.trim($("#editProfileFirstName").val());

            if (value == "") {
                showApplicationMessages({
                    "<%=Constants.APPLICATION_MESSAGES_KEY%>": [
                        {
                            severity: "FATAL",
                            link: null,
                            message: FIRST_NAME_INVALID
                        }
                    ]
                });
            }

            execInSession(function () {

                $.ajax({
                    url: BASE_URL + "/updateFirstName",
                    data: "value=" + value,
                    async: true,
                    cache: false,
                    dataType: "json",
                    type: "post",
                    success: function (data) {
                        if (!showApplicationMessages(data)) {
                            firstNameOriginal = value;
                            $("#editProfileFirstName").val(value); // updates input field
                            $("#editProfileFirstNameView").children("span").html(value); // updates view field
                            $("#portalUserFirstName").html(value); // Updates login section First Name

                            setViewMode("FirstName");
                        }
                    }
                });
            });
        });

        $("#editProfileLastNameButtonSave").children(".cancel").click(function () {
            setViewMode("LastName");
            $("#editProfileFirstName").val(lastNameOriginal);
        });

        // Last Name
        $("#editProfileLastNameButtonEdit").children("a").click(function () {
            setEditMode("LastName");
        });

        $("#editProfileLastNameButtonSave").children(".save").click(function () {

            var value = $.trim($("#editProfileLastName").val());

            if (value == "") {
                showApplicationMessages({
                    "<%=Constants.APPLICATION_MESSAGES_KEY%>": [
                        {
                            severity: "FATAL",
                            link: null,
                            message: LAST_NAME_INVALID
                        }
                    ]
                });
            }

            execInSession(function () {

                $.ajax({
                    url: BASE_URL + "/updateLastName",
                    data: "value=" + value,
                    async: true,
                    cache: false,
                    dataType: "json",
                    type: "post",
                    success: function (data) {
                        if (!showApplicationMessages(data)) {
                            lastNameOriginal = value;
                            $("#editProfileLastName").val(value); // updates input field
                            $("#editProfileLastNameView").children("span").html(value); // updates view field

                            setViewMode("LastName");
                        }
                    }
                });
            });
        });

        // Gender
        $("#editProfileGenderButtonEdit").children("a").click(function () {
            setEditMode("Gender");
        });

        $("#editProfileGenderButtonSave").children(".cancel").click(function () {
            setViewMode("Gender");
            $("#editProfileGender").val(genderOriginal);
        });

        $("#editProfileGenderButtonSave").children(".save").click(function () {

            var value = $.trim($("#editProfileGender").val());

            if (value == "") {
                showApplicationMessages({
                    "<%=Constants.APPLICATION_MESSAGES_KEY%>": [
                        {
                            severity: "FATAL",
                            link: null,
                            message: GENDER_INVALID
                        }
                    ]
                });
            }

            execInSession(function () {

                $.ajax({
                    url: BASE_URL + "/updateGender",
                    data: "value=" + value,
                    async: true,
                    cache: false,
                    dataType: "json",
                    type: "post",
                    success: function (data) {
                        if (!showApplicationMessages(data)) {
                            genderOriginal = value;
                            $("#editProfileGender").val(value); // updates input field
                            $("#editProfileGenderView").children("span").html(GENDER_MAPPING[value]); // updates view field

                            setViewMode("Gender");
                        }
                    }
                });
            });
        });


        // Birth Date
        $("#editProfileBirthDateButtonEdit").children("a").click(function () {
            setEditMode("BirthDate");
        });

        $("#editProfileBirthDateButtonSave").children(".cancel").click(function () {
            setViewMode("BirthDate");
            $("#editProfileBirthDate").val(birthDateOriginal);
            $("#editProfileBirthDate").datepicker("setDate", birthDateObjectOriginal);
        });

        $("#editProfileBirthDateButtonSave").children(".save").click(function () {

            var value = $.trim($("#editProfileBirthDate").val());

            if (value == "") {
                showApplicationMessages({
                    "<%=Constants.APPLICATION_MESSAGES_KEY%>": [
                        {
                            severity: "FATAL",
                            link: null,
                            message: GENDER_INVALID
                        }
                    ]
                });
            }

            execInSession(function () {

                $.ajax({
                    url: BASE_URL + "/updateBirthDate",
                    data: "value=" + value,
                    async: true,
                    cache: false,
                    dataType: "json",
                    type: "post",
                    success: function (data) {
                        if (!showApplicationMessages(data)) {
                            birthDateOriginal = value;

                            $("#editProfileBirthDate").val(value); // updates input field
                            $("#editProfileBirthDateView").children("span").html(value); // updates view field
                            $("#editProfileBirthDate").datepicker("setDate", value);

                            birthDateObjectOriginal = $("#editProfileBirthDate").datepicker("getDate");

                            setViewMode("BirthDate");
                        }
                    }
                });
            });
        });

        // Town
        $("#editProfileTownButtonEdit").children("a").click(function () {
            setEditMode("Town");
        });

        $("#editProfileTownButtonSave").children(".cancel").click(function () {
            setViewMode("Town");
            $("#editProfileTown").val(townOriginal);
        });

        $("#editProfileTownButtonSave").children(".save").click(function () {

            var town = $.trim($("#editProfileTown").val());
            var placeId = $.trim($("#editProfilePlaceId").val());

            if (town == "") {
                showApplicationMessages({
                    "<%=Constants.APPLICATION_MESSAGES_KEY%>": [
                        {
                            severity: "FATAL",
                            link: null,
                            message: GENDER_INVALID
                        }
                    ]
                });
            }

            execInSession(function () {

                $.ajax({
                    url: BASE_URL + "/updateTown",
                    data: {
                        placeId: placeId,
                        town: town
                    },
                    async: true,
                    cache: false,
                    dataType: "json",
                    type: "post",
                    success: function (data) {
                        if (!showApplicationMessages(data)) {
                            placeIdDateOriginal = placeId;
                            townOriginal = town;
                            $("#editProfileTown").val(town); // updates input field
                            $("#editProfilePlaceId").val(town);
                            $("#editProfileTownView").children("span").html(town); // updates view field

                            setViewMode("Town");
                        }
                    }
                });
            });
        });
    }

    function initMusicianForm() {
        var playedInstrumentsTagViewer = new TagViewer({
            selectId: "playedInstrumentsSelect",
            tagContainerId: "playedInstrumentsList",
            paramName: "playedInstruments",
            deleteTooltip: "<spring:message code="profile.musician-form.played-instruments.delete-tooltip"></spring:message>"
        });

        var preferredGenresTagViewer = new TagViewer({
            selectId: "preferredMusicGenresSelect",
            tagContainerId: "preferredMusicGenresList",
            paramName: "preferredGenres",
            deleteTooltip: "<spring:message code="profile.musician-form.preferred-genres.delete-tooltip"></spring:message>"
        });

        var musicMasterInstrumentsTagViewer = new TagViewer({
            selectId: "musicMasterInstrumentsSelect",
            tagContainerId: "musicMasterInstrumentsList",
            paramName: "teachedInstruments",
            deleteTooltip: "<spring:message code="profile.musician-form.played-instruments.delete-tooltip"></spring:message>"
        });

        <c:if test="${musicianModel.musician eq true}">
        $("#editProfileMusician").prop("checked", true);
        </c:if>

        <c:if test="${musicianModel.singer eq true}">
        $("#editProfileSinger").prop("checked", true);
        </c:if>

        <c:if test="${musicianModel.discJockey eq true}">
        $("#editProfileDiscJockey").prop("checked", true);
        </c:if>

        <c:if test="${not empty musicianModel.playedInstruments}">
        <c:forEach var="instr" items="${musicianModel.playedInstruments}">
        playedInstrumentsTagViewer.addTagById("${instr}");
        </c:forEach>
        </c:if>

        <c:if test="${not empty musicianModel.preferredGenres}">
        <c:forEach var="genre" items="${musicianModel.preferredGenres}">
        preferredGenresTagViewer.addTagById("${genre}");
        </c:forEach>
        </c:if>

        <c:if test="${musicianModel.engagementAvailable eq true}">
        $("#engagementAvailable").prop("checked", true);
        </c:if>

        <c:if test="${musicianModel.musicMaster eq true}">
        $("#musicMaster").prop("checked", true);
        </c:if>

        <c:if test="${not empty musicianModel.teachedInstruments}">
        <c:forEach var="instr" items="${musicianModel.teachedInstruments}">
        musicMasterInstrumentsTagViewer.addTagById("${instr}");
        </c:forEach>
        </c:if>

        <c:set var="biographyMaxLenght" >
        <%=Constants.BIOGRAPHY_MAX_LENGHT%>
        </c:set>


        countBioCharsLeft();

        $("#editProfileBiography").keyup(function () {
            countBioCharsLeft();
        });

        checkMusicianFlag();

        $("#editProfileMusician").change(function () {
            checkMusicianFlag();
            setTimeout(function() {window.location.hash = "#goToMusicianForm";}, 600);

        });
    }

    function countBioCharsLeft() {
        var contentValue = $("#editProfileBiography").val();
        var numOfReturn = (contentValue.match(/\n/g) || []).length;

        // Since the length of the content doesn't consider the carriage in carriage return, I add those manually
        var charsLeft = ${biographyMaxLenght} -(contentValue.length + numOfReturn);


        if (charsLeft < 0) {
            $("#biographyCharsLeft").val("0");
        } else {
            $("#biographyCharsLeft").val(charsLeft);
        }
    }

    function setEditMode(section) {
        $("#editProfile" + section + "View").hide();
        $("#editProfile" + section + "Edit").show();

        $("#editProfile" + section + "ButtonEdit").hide();
        $("#editProfile" + section + "ButtonSave").show();
    }

    function setViewMode(section) {
        $("#editProfile" + section + "View").show();
        $("#editProfile" + section + "Edit").hide();

        $("#editProfile" + section + "ButtonEdit").show();
        $("#editProfile" + section + "ButtonSave").hide();
    }

    function checkMusicianFlag() {
        var checked = $("#editProfileMusician").prop("checked");

        if (checked) {
            $("#musicianForm fieldset").show(500);
        } else {
            $("#musicianForm fieldset").hide(500);
        }

    }

</script>
