<%@ page import="com.robynem.mit.web.util.Constants" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<div class="row">
    <div class="col-md-12">

        <!-- SAVE AND PUBLISH BUTTONS -->
        <div class="row">
            <div class="col-md-12 saveButtons">
                <%--<button id="editBandSave" class="btn-default"><spring:message
                        code="global.save"></spring:message></button>
                &nbsp;--%>
                <button id="editBandPublish" class="btn-default"><spring:message
                        code="global.publish"></spring:message></button>
            </div>
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
                <button class="btn btn-default save">
                    <spring:message code="global.save"></spring:message>
                </button>
                &nbsp;
                <button class="btn cancel">
                    <spring:message code="global.cancel"></spring:message>
                </button>
            </div>
        </div>

    </form:form>
</div>

<script type="text/javascript">
    /*var FORM_TAB_MAPPING = ["editBandGeneralForm"];

    var FORM_TAB_VALIDATION_MAPPING = [function () {
        return validateGeneralForm();
    }];*/


    $(function () {

        init();
    });

    function init() {
        initTabs();

        //setCurrentSaveForm();

        initSetNameDialog();
    }

    function initTabs() {
        $("#tabs").tabs({

            beforeActivate: function (event, ui) {
                var id = ui.newPanel.attr('id');
                console.log("new tab id: " + id);

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
                }
            });
        });
    }


</script>