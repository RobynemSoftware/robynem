<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="row">

    <div class="col-md-6">
        <label><spring:message code="band.component.search-component.musician-town"></spring:message> </label>
    </div>

    <div class="col-md-6">
        <span class="musicianInfoTown">${town}</span>
    </div>

    <div class="col-md-12">
        <label><spring:message code="band.component.search-component.musician-bio"></spring:message> </label>
    </div>

    <div class="col-md-12">
        <textarea readonly="readonly" rows="20" class="text-area musicianInfoBio">${biography}</textarea>
    </div>

</div>