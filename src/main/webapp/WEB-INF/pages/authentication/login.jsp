<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="fbLoginURL" value="${contextPath}/authentication/fbLogin"/>
<c:set var="logoutURL" value="${contextPath}/authentication/logout"/>

<script type="text/javascript">
    window.fbAsyncInit = function () {
        FB.init({
            appId: '959166177463363',
            cookie: true,  // enable cookies to allow the server to access
                           // the session
            xfbml: true,  // parse social plugins on this page
            version: 'v2.5'
        });

    };

    function statusChangeCallback(response) {
        console.log('statusChangeCallback');
        console.log(response);
        // The response object is returned with a status field that lets the
        // app know the current login status of the person.
        // Full docs on the response object can be found in the documentation
        // for FB.getLoginStatus().
        if (response.status === 'connected') {
            //do login
            console.log("Connected");
            console.log(response);

            console.log(response.authResponse.accessToken);


            $("#accessToken").val(response.authResponse.accessToken);
            $("#fbUserId").val(response.authResponse.userID);
            $("#fbLoginForm").submit();
            //fbLogin();

            console.log("access token val: " + $("#accessToken").val());
        }
    }

    function onFbLogin() {
        FB.getLoginStatus(function (response) {
            statusChangeCallback(response);
        });
    }

    function fbLogin() {
        var url = '${fbLoginURL}';

        $.ajax({
            type: "POST",
            url: url,
            dataType: 'json',
            data: $("#loginForm").serialize(),
            beforeSend: function () {
                //$.blockUI();
            },
            complete: function () {
                //jQuery.unblockUI();
            },
            success: function (response) {

                console.log(response);

                var loginResult = response.loginResult;
                var login = response.login;
                var password = response.password;


            }
        });
    }


</script>

<div class="loginContainer row">
    <c:choose>
        <c:when test="${empty portalUser}">
            <div class="col-md-6">
                    <%--<div id="loginButton" scope="public_profile,email" onlogin="javascript:onFbLogin();" class="fb-login-button" data-auto-logout-link="false" data-max-rows="1" data-show-faces="false" data-size="small">&nbsp;</div>--%>
                <fb:login-button scope="public_profile,email" onlogin="javascript:onFbLogin();">
                    <spring:message code="login.facebook-login"></spring:message>
                </fb:login-button>
            </div>

            <div class="col-md-6">
                <button id="showSignInButton"><spring:message code="login.signin-signup"></spring:message></button>
            </div>

        </c:when>
        <c:otherwise>
            <div class="col-md-12">
                <div class="row">
                    <span style="background-color: white;">
                        <spring:message code="login.welcome"></spring:message>
                        &nbsp;
                        <span id="portalUserFirstName">
                            ${portalUser.firstName}
                        </span>

                    </span>
                    &nbsp;
                    (<a title="Sign Out" href="${logoutURL}"><spring:message code="login.logout"></spring:message></a>)
                </div>


                <div class="row">

                    <%-- Notification icon --%>
                    <div class="col-md-12">
                        <div id="noti_Container" title="<spring:message code="login.go-to-notifications"></spring:message>">
                            <img src="${contextPath}/resources/images/notification_icon.png" alt="notifications" class="img-responsive" />
                            <div class="noti_bubble"></div>
                        </div>
                    </div>

                </div>
            </div>

            <script type="text/javascript">
                var NOTIFICATION_ICON_INTERVAL = 60000;

                $(function () {
                    // Call notifications icon update on page load
                    updateNotificationsIcon();

                    // sets notification icon update timeout
                    setInterval(function() {updateNotificationsIcon();}, NOTIFICATION_ICON_INTERVAL);

                    // sets notification icon link
                    $("#noti_Container").click(function () {
                        window.location.href = "${contextPath}/private/dashboard/";
                    })
                })

                function updateNotificationsIcon() {
                    $.ajax({
                        url : "${contextPath}/private/dashboard/getUnreadNotificationsCount",
                        dataType : "json",
                        async : true,
                        cache : false,
                        global: false,     // this makes sure ajaxStart is not triggered
                        success : function (data) {
                            //console.log("data.count: " + data.count);
                            if (data.count != null) {
                                $(".noti_bubble").html(data.count);
                            } else {
                                $(".noti_bubble").html("");
                            }
                        }
                    });
                }
            </script>

        </c:otherwise>
    </c:choose>

    <form id="fbLoginForm" method="post" action="${contextPath}/authentication/fbLogin">
        <input type="hidden" id="accessToken" name="accessToken" value="">
        <input type="hidden" id="redirectView" name="redirectView" value="${redirectView}">
    </form>

    <div id="loginDialog" class="loginDialog" style="display: none;">

    </div>


</div>


<script type="text/javascript" defer>
    $(function () {
        $("#showSignInButton").click(function () {

            // Load email login page
            $.ajax({
                type: "GET",
                url: "${contextPath}/authentication/showEmailLogin",
                dataType: 'html',
                beforeSend: function () {
                    //$.blockUI();
                },
                complete: function () {
                    //jQuery.unblockUI();
                },
                success: function (response) {

                    $("#loginDialog").html(response);
                    $("#loginDialog").dialog({
                        modal: true,
                        height: 'auto',
                        width:  isMobile() ? 'auto' : '40%',
                        autoOpen: true,
                        closeText : 'X',
                        position : ['top', 'center']
                    });
                }
            });
        });


    });

    function showSignUp() {
        $("#loginDialog").dialog("close");

        // Load signup page
        $.ajax({
            type: "GET",
            url: "${contextPath}/authentication/showSignUp",
            dataType: 'html',
            beforeSend: function () {
                //$.blockUI();
            },
            complete: function () {
                //jQuery.unblockUI();
            },
            success: function (response) {

                $("#loginDialog").html(response);
                $("#loginDialog").dialog({
                    modal: true,
                    height: 'auto',
                    width: isMobile() ? 'auto' : '40%',
                    autoOpen: true,
                    closeText : 'X',
                    position : ['top', 'center'],
                    title : "<spring:message code="login.signup"></spring:message>"
                });
            }
        });
    }
</script>