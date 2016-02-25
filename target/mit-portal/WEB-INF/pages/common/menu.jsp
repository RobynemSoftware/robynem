<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="row">
    <div class="col-md-12">
        <nav class="navbar navbar-default" role="navigation">
            <div class="navbar-header">

                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#mainMenu">
                    <span class="sr-only">Toggle navigation</span><span class="icon-bar"></span><span
                        class="icon-bar"></span><span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="${contextPath}">Brand</a>
            </div>

            <div class="collapse navbar-collapse" id="mainMenu">
                <ul class="nav navbar-nav">
                    <li  class="menuIndex">
                        <a href="${contextPath}/index"><spring:message code="menu.home"></spring:message> </a>
                    </li>

                    <c:if test="${not empty portalUser}">

                        <li  class="dropdown menuEditProfile">
                            <a href="#" class="dropdown-toggle" data-toggle="dropdown"><spring:message code="menu.profile"></spring:message><strong class="caret"></strong></a>
                            <ul class="dropdown-menu">
                                <li class="menuDashboard">
                                    <a href="${contextPath}/private/dashboard"><spring:message code="menu.dashboard"></spring:message></a>
                                </li>
                                <li class="menuEditProfile">
                                    <a href="${contextPath}/private/profile/editProfile"><spring:message code="menu.edit-profile"></spring:message></a>
                                </li>
                                <li class="menuChangePassword">
                                    <a href="${contextPath}/private/profile/changePassword" ><spring:message code="menu.change-password"></spring:message></a>
                                </li>
                                <li class="menuCreateArtist">
                                    <a href="${contextPath}/private/profile/createArtist" ><spring:message code="menu.create-artist-page"></spring:message></a>
                                </li>
                                <li class="menuCreateBand">
                                    <a href="${contextPath}/private/editBand/edit?create=true" ><spring:message code="menu.create-band-page"></spring:message></a>
                                </li>
                                <li class="menuCreateClub">
                                    <a href="${contextPath}/private/profile/createClub" ><spring:message code="menu.create-club"></spring:message></a>
                                </li>
                                <li class="menuCreateEvent">
                                    <a href="${contextPath}/private/profile/createEvent" ><spring:message code="menu.create-event"></spring:message></a>
                                </li>
                            </ul>
                        </li>
                    </c:if>


                </ul>

                <div class="nav navbar-nav navbar-right">
                    <jsp:include page="../authentication/login.jsp"></jsp:include>
                </div>
            </div>

        </nav>


    </div>
</div>

<script type="text/javascript">
    $(function() {
        $(".${param.activeItem}").addClass("active");
    });
</script>
