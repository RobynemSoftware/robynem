<%@ page import="com.robynem.mit.web.util.Constants" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.robynem.mit.web.util.PortalHelper" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

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
    String dp = PortalHelper.getDateFormat(request.getLocale(), DateFormat.SHORT);
%>

<c:set var="dateFormat" scope="request">
    <%=dp.toLowerCase()%>
</c:set>

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html;charset=ISO-8859-1">
<%--<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />--%>



<!-- JQUERY -->
<link rel="stylesheet" href="${contextPath}/resources/css/jquery/jquery-ui.css/" />
<link rel="stylesheet" href="${contextPath}/resources/css/jquery/jquery-ui.structure.css" />
<link rel="stylesheet" href="${contextPath}/resources/css/jquery/jquery-ui.theme.css" />

<!-- BOOTSTRAP -->
<link rel="stylesheet" href="${contextPath}/resources/bootstrap/css/bootstrap.min.css" />
<link rel="stylesheet" href="${contextPath}/resources/bootstrap/css/bootstrap-theme.min.css" />

<!-- SITE -->
<link rel="stylesheet" href="${contextPath}/resources/css/main.css" />

<!-- JQUERY -->
<script src="${contextPath}/resources/js/jquery/jquery.js"></script>
<script src="${contextPath}/resources/js/jquery/jquery.i18n.properties-min-1.0.9.js"></script>
<script src="${contextPath}/resources/js/jquery/jquery.form.min.js"></script>

<!-- BOOTSTRAP -->
<script src="${contextPath}/resources/bootstrap/js/bootstrap.min.js"></script>

<!-- JQUERY UI -->
<script src="${contextPath}/resources/js/jquery/jquery-ui.js"></script>
<script src="${contextPath}/resources/js/jquery/datepicker-it.js"></script>

<!-- BLOCK UI -->
<script src="${contextPath}/resources/js/blockui.js"></script>





<spring:eval var="googleApyKey" expression="@commonProperties['google.apy-key']" scope="request"/>
<spring:eval var="facebookAppId" expression="@commonProperties['facebook.app-id']"/>



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
<script src="${contextPath}/resources/js/main.js"></script>
<script src="${contextPath}/resources/js/TagViewer.js"></script>