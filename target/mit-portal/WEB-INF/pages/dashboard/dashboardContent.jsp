<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<div class="row mainContent">

    <div class="col-md-12">

        <!-- OWNED BANDS -->
        <div class="accordion" id="dashboardAccordion">

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

</script>

