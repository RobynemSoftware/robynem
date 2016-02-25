<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<ul class="list-unstyled profileMenu">
    <li class="menuDashboard">
        <a href="${contextPath}/private/dashboard"><spring:message code="menu.dashboard"></spring:message></a>
    </li>
    <li class="menuEditProfile">
        <a href="${contextPath}/private/profile/editProfile"><spring:message code="menu.edit-profile"></spring:message></a>
    </li>
    <li class="menuChangePassword">
        <a href="${contextPath}/private/profile/changePassword"><spring:message code="menu.change-password"></spring:message></a>
    </li>
    <li class="menuCreateArtist">
        <a href="${contextPath}/private/profile/createArtist"><spring:message code="menu.create-artist-page"></spring:message></a>
    </li>
    <li class="menuCreateBand">
        <a href="${contextPath}/private/editBand/edit?create=true"><spring:message code="menu.create-band-page"></spring:message></a>
    </li>
    <li class="menuCreateClub">
        <a href="${contextPath}/private/profile/createClub"><spring:message code="menu.create-club"></spring:message></a>
    </li>
    <li class="menuCreateEvent">
        <a href="${contextPath}/private/profile/createEvent"><spring:message code="menu.create-event"></spring:message></a>
    </li>
</ul>