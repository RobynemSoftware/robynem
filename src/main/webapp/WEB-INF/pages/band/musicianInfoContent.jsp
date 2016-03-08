<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="row">

    <!-- TOWN -->
    <div class="col-md-6">
        <label><spring:message code="band.component.search-component.musician-town"></spring:message> </label>
    </div>

    <div class="col-md-6 musicianInfoText">
        <span class="musicianInfoTown">${town}</span>
    </div>
</div>

<div class="row">
    <!-- INSTRUMENTS -->
    <div class="col-md-3">
        <label><spring:message code="band.component.search-component.instruments"></spring:message> </label>
    </div>

    <div class="col-md-9 musicianInfoText">
        <c:forEach var="instr" items="${playedInstruments}" varStatus="status">
            <span>${instr.name}</span>

            <c:if test="${not status.last}">
                |
            </c:if>

        </c:forEach>
    </div>

</div>

<div class="row">

    <div class="col-md-12">
        <label><spring:message code="band.component.search-component.musician-bio"></spring:message> </label>
    </div>

</div>

<div class="row">
    <div class="col-md-12">
        <textarea readonly="readonly" rows="20" class="text-area musicianInfoBio">${biography}</textarea>
    </div>

</div>