<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<div class="row mainContent">

    <div class="col-md-12">


        <div class="accordion" id="dashboardAccordion">

            <!-- NOTIFICATIONS -->
            <div class="accordion-group">

                <div class="accordion-heading">
                    <a class="accordion-toggle" data-toggle="collapse" data-parent="#dashboardAccordion" href="#collapseNotifications">
                        <span><spring:message code="dashbord.notifications.title"></spring:message></span>
                    </a>
                </div>

                <div id="collapseNotifications" class="accordion-body collapse in">
                    <div id="notificationsContainer" class="accordion-inner">

                    </div>

                </div>
            </div>

            <!-- OWNED BANDS -->
            <div class="accordion-group">

                <div class="accordion-heading">
                    <a class="accordion-toggle" data-toggle="collapse" data-parent="#dashboardAccordion" href="#collapseOwnedBands">
                        <span><spring:message code="dashbord.owned-bands.title"></spring:message></span>
                    </a>
                </div>

                <div id="collapseOwnedBands" class="accordion-body collapse in">
                    <div id="ownedBandsContainer" class="accordion-inner">

                    </div>

                </div>
            </div>

        </div>


        <hr class="lineSeparator" />
    </div>

</div>

<script type="text/javascript">

    $(function() {
        loadOwnedBands();

        loadNotifications();
    });

    function loadOwnedBands() {
        $.ajax({
            url : "${contextPath}/private/dashboard/viewOwnedBands",
            type : "get",
            dataType : "html",
            async : true,
            cache : false,
            success : function(data) {
                $("#ownedBandsContainer").html(data);
            }
        });
    }

    function loadNotifications() {
        $.ajax({
            url : "${contextPath}/private/dashboard/viewNotifications",
            type : "get",
            dataType : "html",
            async : true,
            cache : false,
            success : function(data) {
                $("#notificationsContainer").html(data);
            }
        });
    }

</script>

