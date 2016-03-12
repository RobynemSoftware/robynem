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

    var READ_NOTIFICATION_TIMOUT = null;
    var READ_NOTIFICATION_TIMEOUT_INTERVAL = 500;

    $(function() {
        loadOwnedBands();

        loadNotifications(1);

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

    function loadNotifications(currentPage) {
        $.ajax({
            url : "${contextPath}/private/dashboard/viewNotifications",
            data : {
                currentPage : currentPage
            },
            type : "get",
            dataType : "html",
            async : true,
            cache : false,
            success : function(data) {
                $("#showNextNotifications").remove();
                $("#notificationsContainer").append(data);
            }
        });
    }

    function initNotificationReadTimeout() {
        $(".notificationContent").mouseover(function() {
            var notificationId = $(this).prop("id");
            //console.log("notificationId: " + notificationId);
            if (notificationId != null) {
                var $this = this;
                READ_NOTIFICATION_TIMOUT = setTimeout(function() {setNotificationRead(notificationId, $this)}, READ_NOTIFICATION_TIMEOUT_INTERVAL);
            }
        });

        $(".notificationContent").mouseout(function() {
            if (READ_NOTIFICATION_TIMOUT != null) {
                clearTimeout(READ_NOTIFICATION_TIMOUT);
                READ_NOTIFICATION_TIMOUT = null;
            }
        });
    }

    function setNotificationRead(notificationId, notificationContentElement) {
        //console.log("notificationId read: " + notificationId);

        if (notificationId != null) {
            execInSession(function() {
                $.ajax({
                    url : "${contextPath}/private/dashboard/setNotificationRead",
                    data : {
                        notificationId : notificationId
                    },
                    async : true,
                    cache : false,
                    dataType : "json",
                    global: false,     // this makes sure ajaxStart is not triggered
                    success : function(data) {

                        showApplicationMessages(data);

                        if (data.success == true) {
                            $(notificationContentElement).unbind("mouseover").unbind("mouseout");
                            $(".row_id_" + notificationId).removeClass("unreadNotification").addClass("readNotification");
                            //Updates notifications icon
                            updateNotificationsIcon();
                        }
                    }
                })
            });
        }

    }

</script>

