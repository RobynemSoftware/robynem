<%@ page import="com.robynem.mit.web.util.Constants" %>
<%@ page import="com.robynem.mit.web.util.EntityStatus" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<div class="row">
    <div class="col-md-12">

        <!-- SAVE AND PUBLISH BUTTONS -->
        <div class="row">

            <div class="col-md-8">
                <span class="bandStatus"></span>
            </div>



            <%--<div class="col-md-2 saveButtons">
                <button id="editBandGoToPreview" onclick="$('#goToPreviewBandForm').submit();" ><spring:message code="band.go-to-preview"></spring:message></button>
            </div>--%>
            <div class="col-md-4 saveButtons">
                <button id="editBandGoToPreview" onclick="$('#goToPreviewBandForm').submit();" ><spring:message code="global.preview"></spring:message></button>
                <button id="editBandPublish" ><spring:message code="global.publish"></spring:message></button>
            </div>

            <form:form id="publishBandForm" action="${contextPath}/private/editBand/publishBand" onsubmit="javascript:$.blockUI()"></form:form>
            <form:form id="goToPreviewBandForm" action="${contextPath}/private/editBand/goToPreview" target="_blank"></form:form>
        </div>
        <!-- TABS -->
        <div class="row">
            <div class="col-md-12">

                <div id="tabs">
                    <ul>
                        <li><a href="#tabs-general"><spring:message code="band.tabs.general"></spring:message></a></li>
                        <li><a href="#tabs-components"><spring:message
                                code="band.tabs.components"></spring:message> </a></li>
                        <li><a href="#tabs-media"><spring:message code="band.tabs.media"></spring:message> </a></li>
                    </ul>
                    <div id="tabs-general">
                        <%--<jsp:include page="editBandGeneral.jsp"></jsp:include>--%>
                    </div>
                    <div id="tabs-components">
                        <%--<jsp:include page="editBandComponents.jsp"></jsp:include>--%>
                    </div>
                    <div id="tabs-media">
                        <%--<jsp:include page="editBandMedia.jsp"></jsp:include>--%>
                    </div>
                </div>

            </div>
        </div>
    </div>
</div>

<div id="editBandSetNameDialog" style="display: none;">
    <form:form id="editBandSetNameForm" action="${contextPath}/private/editBand/saveName" method="post"
               onsubmit="javascript:$.blockUI();">
        <div class="row">
            <div class="col-md-12">
                <span><spring:message code="band.save-name.title"></spring:message> </span>
            </div>
        </div>

        <div class="row">
            <div class="col-md-4 col-xs-12">
                <label><spring:message code="band.name"></spring:message> </label>
            </div>

            <div class="col-md-8 col-xs-12">
                <input id="editBandNameInDialog" type="text" name="bandName" class="form-control"/>
            </div>
        </div>

        <div class="row">
            <div class="col-md-12">
                <button class="save">
                    <spring:message code="global.save"></spring:message>
                </button>
                &nbsp;
                <button class="cancel">
                    <spring:message code="global.cancel"></spring:message>
                </button>
            </div>
        </div>

    </form:form>
</div>

<script type="text/javascript">
    var GENERAL_TAB_MODIFIED = false;
    var DO_PUBLISH = false;  // Raises publish event
    var SWITCH_TAB_TO = null; // Raises tab switch event. Must be populated with tab index

    $(function () {

        init();
    });

    function init() {
        initTabs();

        initSetNameDialog();

        initPublishButton();
    }

    function initTabs() {
        $("#tabs").tabs({

            beforeActivate: function (event, ui) {
                SWITCH_TAB_TO = null; // Resets event

                var id = ui.newPanel.attr('id');
                var oldId = ui.oldPanel.attr('id');
                //console.log("new tab id: " + id);

                /*If we're leaving general form, we save it before.*/
                if (oldId == "tabs-general" && GENERAL_TAB_MODIFIED == true) {

                    GENERAL_TAB_MODIFIED = false;

                    if (validateGeneralForm()) {
                        // Tells to form submit handler to switch tab
                        SWITCH_TAB_TO = ui.newTab.index();
                        $("#editBandGeneralForm").submit();
                        return false;
                    } else {
                        return false;
                    }
                }

                switch(id) {
                    case "tabs-general":
                        loadGeneralTab();
                        break;

                    case "tabs-components":
                        loadComponentsTab();
                        break;

                    case "tabs-media":
                        loadMediaTab();
                        break;
                }
            }
        });

        <c:if test="${not empty currentTabIndex}">
        $("#tabs").tabs("option", "active", "${currentTabIndex}");
        </c:if>

        <c:choose>
            <c:when test="${not empty bandId}">
                loadGeneralTab();
            </c:when>
            <c:otherwise>
               <c:set var="bandId" value="0"></c:set>
            </c:otherwise>
        </c:choose>


    }



    function initSetNameDialog() {
        $("#editBandSetNameDialog").dialog({
            modal: true,
            height: 'auto',
            width: 'auto',
            autoOpen: false,
            position: {my: "center bottom", at: "center top"},
            closeText: 'X',
            close: function () {
                window.location.href = "${contextPath}/index";
                $.blockUI();
            }
        });

        <c:if test="${setName eq true}">
        $("#editBandSetNameDialog").dialog("open");
        </c:if>

        $("#editBandSetNameForm button.save").click(function () {
            var name = $.trim($("#editBandNameInDialog").val());

            if (name != "") {
                return true;
            } else {
                return false;
            }
        });

        $("#editBandSetNameForm button.cancel").click(function () {
            $("#editBandSetNameDialog").dialog("close");

            return false;
        });
    }

    function loadGeneralTab() {
        execInSession(function() {
            $.ajax({
                url : "${contextPath}/private/editBand/showGeneralInfo",
                data : {
                    bandId : ${bandId}
                },
                dataType : "html",
                success : function(data) {
                    $("#tabs-general").html(data);

                    GENERAL_TAB_MODIFIED = false;

                    showBandStatus();
                }
            });
        });
    }

    function loadComponentsTab() {
        execInSession(function() {
            $.ajax({
                url : "${contextPath}/private/editBand/showComponents",
                data : {
                    bandId : ${bandId}
                },
                dataType : "html",
                success : function(data) {
                    $("#tabs-components").html(data);

                    showBandStatus();
                }
            });
        });
    }

    function loadMediaTab() {
        execInSession(function() {
            $.ajax({
                url : "${contextPath}/private/editBand/showMedia",
                data : {
                    bandId : ${bandId}
                },
                dataType : "html",
                success : function(data) {
                    $("#tabs-media").html(data);

                    showBandStatus();
                }
            });
        });
    }

    function showBandStatus() {

        setTimeout(function () {
            $.ajax({
                url : "${contextPath}/private/editBand/getBandStatus",
                data : {
                    bandId : ${bandId != null ? bandId : 0}
                },
                dataType : "json",
                cache : false,
                global : false,
                success : function (data) {
                    $(".bandStatus").val("");

                    if (data.bandStatus != null) {
                        var alertMessage = "";
                        if (data.bandStatus == '<%=EntityStatus.NOT_PUBLISHED.toString()%>') {
                            alertMessage = "<spring:message code="band.status.not-published.alert"></spring:message>";
                        } else if (data.bandStatus == '<%=EntityStatus.STAGE.toString()%>') {
                            alertMessage = "<spring:message code="band.status.stage.alert"></spring:message>";
                        }

                        $(".bandStatus").html(alertMessage);
                    }
                }
            });
        }, 1000);


    }

    function initPublishButton() {
        $("#editBandPublish").click(function () {

            /*
            * If general form has been modified and not saved, we indicate that we request to publish and we submit general form.
            * This form, after saving, reads DO_PUBLISH equals true and raises publish event.
            * */
            //console.log("Publish: GENERAL_TAB_MODIFIED: " + GENERAL_TAB_MODIFIED);
            if (GENERAL_TAB_MODIFIED == true) {
                DO_PUBLISH = true;
                $("#editBandGeneralForm").submit();
            } else {
                $("#publishBandForm").submit();
            }


        });
    }

    function validateGeneralForm() {
        var validated = validateGeneralFormMandatoryFields() && validateEmailContacts() && validatePhoneNumberContacts();

        return validated;
    }

    function validateEmailContacts() {
        var valid = true;
        var message = "<spring:message code="band.validation.invalid-email-contact"></spring:message>"
        $(".emailText").each(function() {
            var value = $.trim($(this).val());

            if (value != "" && !isEmail(value)) {
                showApplicationMessages({
                    "<%=Constants.APPLICATION_MESSAGES_KEY%>": [
                        {
                            severity: "FATAL",
                            link: null,
                            message: message.replace("{0}", value)
                        }
                    ]
                });

                valid = false;
                return;
            }
        });

        return valid;
    }

    function validatePhoneNumberContacts() {
        var valid = true;
        var message = "<spring:message code="band.validation.invalid-phone-contact"></spring:message>"
        $(".phoneText").each(function() {
            var value = $.trim($(this).val());

            if (value != "" && !isPhoneNumber(value)) {
                showApplicationMessages({
                    "<%=Constants.APPLICATION_MESSAGES_KEY%>": [
                        {
                            severity: "FATAL",
                            link: null,
                            message: message.replace("{0}", value)
                        }
                    ]
                });

                valid = false;
                return;
            }
        });

        return valid;
    }

    //
    function validateGeneralFormMandatoryFields() {
        //editBandPlaceId
        var valid = true;

        var messages = new Array();

        if ($.trim($("#editBandName").val()) == "") {
            messages.push({
                severity: "FATAL",
                link: null,
                message: "<spring:message code="band.validation.band-name-mandatory"></spring:message>"
            });
            valid = false;
        }



        if (messages.length > 0) {
            showApplicationMessages({
                "<%=Constants.APPLICATION_MESSAGES_KEY%>": messages
            });
        }

        return valid;

    }


</script>