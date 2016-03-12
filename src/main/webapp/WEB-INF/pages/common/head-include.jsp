<%@ page import="com.robynem.mit.web.util.Constants" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.robynem.mit.web.util.PortalHelper" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%-- Properties --%>
<spring:eval var="googleApyKey" expression="@commonProperties['google.apy-key']" scope="request"/>
<spring:eval var="facebookAppId" expression="@commonProperties['facebook.app-id']"/>
<spring:eval var="softwareVersion" expression="@commonProperties['software.version']" scope="session"/>

<!-- JSTL VARIABLES -->
<c:set var="contextPath" value="<%=request.getContextPath()%>" scope="application"></c:set>

<c:set var="logoutURL" value="/authentication/logout"></c:set>

<c:set var="messagesKey" >
    <%=Constants.APPLICATION_MESSAGES_KEY%>
</c:set>

<c:set var="siteTitle" scope="application">
    <spring:message code="global.site-title"></spring:message>
</c:set>

<%--
Creates the localized date format
--%>

<%
    String shortDateFormat = PortalHelper.getDateFormat(request.getLocale(), DateFormat.SHORT);
    //String defaultDateFormat = PortalHelper.getDateFormat(request.getLocale(), DateFormat.FULL);
%>

<c:set var="dateFormat" scope="request">
    <%=shortDateFormat.toLowerCase()%>
</c:set>

<%--<c:set var="defaultDateFormat" scope="session">
    <%=defaultDateFormat%>
</c:set>--%>

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html;charset=ISO-8859-1">
<%--<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />--%>



<!-- JQUERY -->
<link rel="stylesheet" href="${contextPath}/resources/css/jquery/jquery-ui.css?v=${softwareVersion}" />
<link rel="stylesheet" href="${contextPath}/resources/css/jquery/jquery-ui.structure.css?v=${softwareVersion}" />
<link rel="stylesheet" href="${contextPath}/resources/css/jquery/jquery-ui.theme.css?v=${softwareVersion}" />

<!-- BOOTSTRAP -->
<link rel="stylesheet" href="${contextPath}/resources/bootstrap/css/bootstrap.min.css?v=${softwareVersion}" />
<link rel="stylesheet" href="${contextPath}/resources/bootstrap/css/bootstrap-theme.min.css?v=${softwareVersion}" />

<!-- SITE -->
<link rel="stylesheet" href="${contextPath}/resources/css/main.css?v=${softwareVersion}" />

<!-- JQUERY -->
<script src="${contextPath}/resources/js/jquery/jquery.js?v=${softwareVersion}"></script>
<script src="${contextPath}/resources/js/jquery/jquery.i18n.properties-min-1.0.9.js?v=${softwareVersion}"></script>
<script src="${contextPath}/resources/js/jquery/jquery.form.min.js?v=${softwareVersion}"></script>

<!-- BOOTSTRAP -->
<script src="${contextPath}/resources/bootstrap/js/bootstrap.min.js?v=${softwareVersion}"></script>

<!-- JQUERY UI -->
<script src="${contextPath}/resources/js/jquery/jquery-ui.js?v=${softwareVersion}"></script>
<script src="${contextPath}/resources/js/jquery/datepicker-it.js?v=${softwareVersion}"></script>

<!-- BLOCK UI -->
<script src="${contextPath}/resources/js/blockui.js?v=${softwareVersion}"></script>









<script src="https://maps.googleapis.com/maps/api/js?key=${googleApyKey}&signed_in=false&libraries=places"></script>
<script type="text/javascript" src="https://connect.facebook.net/it_IT/sdk.js#xfbml=1&version=v2.5&appId=${facebookAppId}"></script>



<!-- SITE -->
<script type="text/javascript">
    var MESSAGES_KEY = "${messagesKey}";
    var SESSION_EXPIRED_MESSAGE = "<spring:message code="global.session-expired"></spring:message>";
    var CONTEXT_PATH = "${contextPath}";

    $(function() {
        $.blockUI.defaults.message = '<div class="row"><div class="col-md-2"><img src="${contextPath}/resources/images/ajax-loader.gif" class="img-responsive" /></div><div class="col-md-10"><span class="loading"><spring:message code="global.loading"/></span></div></div>';
        $(document).ajaxStart($.blockUI).ajaxStop($.unblockUI);
    });

</script>
<script src="${contextPath}/resources/js/main.js?v=${softwareVersion}"></script>
<script src="${contextPath}/resources/js/TagViewer.js?v=${softwareVersion}"></script>

<!-- FAVICON -->
<link rel="shortcut icon" href="${contextPath}/favicon.ico" />