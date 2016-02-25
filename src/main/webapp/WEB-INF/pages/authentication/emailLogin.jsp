<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<div class="row">
    <div class="col-md-12">

        <form id="emailLoginForm" role="form">
            <div class="form-group">
                <label for="emailLoginForm_userName"><spring:message code="login.email-address"></spring:message>:</label>
                <input type="email" class="form-control" id="emailLoginForm_userName" name="emailAddress"/>
            </div>

            <div class="form-group">
                <label for="emailLoginForm_password"><spring:message code="login.password"></spring:message>:</label>
                <input type="password" class="form-control" id="emailLoginForm_password" name="password" maxlength="50"/>
            </div>

            <div class="form-group">
                <div class="row">
                    <div class="col-md-12">
                        <button id="emailLoginButton"><spring:message code="login.signin"></spring:message></button>
                        &nbsp;
                        <button id="emailLoginCancelButton"><spring:message code="global.cancel"></spring:message></button>
                    </div>
                </div>
            </div>
        </form>

        <form id="postLoginForm" action="${contextPath}/authentication/redirect" method="get">
            <input type="hidden" name="redirectView" value="${redirectView}">
        </form>

    </div>

    <div class="col-md-12">
        <div class="row">
            <div class="col-md-6">
                <a href="javascript:showSignUp();">
                    <spring:message code="login.signup"></spring:message>
                </a>
            </div>

            <div class="col-md-6">
                <a href="javascript:showResetPasswordDialog();">
                    <spring:message code="login.forgot-password"></spring:message>
                </a>
            </div>
        </div>
    </div>

</div>

<!-- MESSAGE DIALOG -->
<div id="emailLoginMessageDialog" style="display: none;">


    <div class="row">

        <div class="col-md-12">


            <div class="row">

                <div class="col-md-4">

                    <img src="${contextPath}/resources/images/Warning_32x32.png" class="img-responsive" />

                </div>

                <div class="col-md-8">
                    <span id="emailLoginMessage" class="fatal">

                    </span>
                </div>
            </div>
        </div>
    </div>
</div>


<!-- RESET PASSWORD DIALOG -->
<div id="resetPasswordDialog" style="display: none;">
    <div class="row">
        <div class="col-md-12">

            <form id="resetPasswordForm" action="${contextPath}/authentication/resetPasswordCompleted" role="form">
                <div class="form-group">
                    <label for="resetPasswordForm_email"><spring:message code="login.reset-password.email"></spring:message>:</label>
                    <input type="email" class="form-control" id="resetPasswordForm_email" name="emailAddress"/>
                    <div id="resetPasswordValidationMessage" style="display:none;">

                    </div>
                </div>

                <div class="form-group">
                    <div class="row">
                        <div class="col-md-12">
                            <button id="resetPasswordForm_SubmitButton" class="resetPasswordButton"><spring:message code="login.reset-password.reset"></spring:message></button>
                            &nbsp;
                            <button id="resetPasswordForm_CancelButton" class="resetPasswordButton"><spring:message code="global.cancel"></spring:message></button>
                        </div>
                    </div>
                </div>

                <input type="hidden" name="redirectView" value="${redirectView}">
            </form>
        </div>
    </div>
</div>

<script type="text/javascript">
    var EMAIL_MANDATORY = "<spring:message code="login.signup.email-mandatory"></spring:message>";
    var PASSWORD_MANDATORY = "<spring:message code="login.signup.password-mandatory"></spring:message>";
    var EMAIL_NOT_VALID = "<spring:message code="login.signup.email-invalid"></spring:message>";

    $(function() {

        $("#emailLoginButton").click(function () {
           if (validateEmailLoginFieldsFields()) {
               doEmailLogin();
           }

           return false;
       });

        $("#resetPasswordForm_SubmitButton").click(function() {
            return  resetPassword();
        });

        $("#emailLoginCancelButton").click(function() {
            $("#loginDialog").dialog("close");
            return false;
        });

        $("#resetPasswordForm_CancelButton").click(function() {
            $("#resetPasswordDialog").dialog("close");
            return false;
        });
    });

    function validateEmailLoginFieldsFields() {
        var valid = true;

        var emailAddress = $.trim($("#emailLoginForm_userName").val());
        var password = $.trim($("#emailLoginForm_password").val());

        if (emailAddress == "") {
            showEmailLoginMessage(EMAIL_MANDATORY);
            valid = false;
        } else if (password == "") {
            showEmailLoginMessage(PASSWORD_MANDATORY);
            valid = false;
        }

        return valid;
    }

    function doEmailLogin() {
        $.ajax({
            type: "POST",
            url: "${contextPath}/authentication/emailLogin",
            dataType: 'json',
            data : $("#emailLoginForm").serialize(),
            cache : false,
            async : true,
            beforeSend: function () {

            },
            complete: function () {

            },
            success: function (data) {

                if (!showApplicationMessages(data)) {
                    if (data.success == true) {
                        $("#postLoginForm").submit();
                    } else {
                        if (data.loginMessage != null) {
                            showEmailLoginMessage(data.loginMessage);
                        }
                    }
                }


            }
        });
    }

    function showEmailLoginMessage(message) {
        $("#emailLoginMessage").html(message);

        $("#emailLoginMessageDialog").dialog({
            modal : true,
            height : 'auto',
            width : 'auto',
            autoOpen : true,
            position : 'center',
            closeText : 'X',
            buttons: [
                {
                    text: "Ok",
                    /*icons: {
                     primary: "ui-icon-heart"
                     },*/
                    click: function() {
                        $( this ).dialog( "close" );
                    }

                    // Uncommenting the following line would hide the text,
                    // resulting in the label being used as a tooltip
                    //showText: false
                }
            ]
        });
    }

    function showResetPasswordDialog() {

        $("#resetPasswordForm_email").val("");
        $("#resetPasswordValidationMessage").hide();

        $("#resetPasswordDialog").dialog({
            modal : true,
            height : 'auto',
            width : 'auto',
            autoOpen : true,
            position : 'center',
            closeText : 'X'
        });
    }

    function resetPassword() {

        $("#resetPasswordValidationMessage").hide();
        var email = $.trim($("#resetPasswordForm_email").val());

        console.log("reset password email: " + email);

        if (email == "") {
            $("#resetPasswordValidationMessage").html($("<span class=\"fatal\"></span>").html(EMAIL_MANDATORY));
            $("#resetPasswordValidationMessage").show();
            return false;
        } else if (!isEmail(email)) {
            $("#resetPasswordValidationMessage").html($("<span class=\"fatal\"></span>").html(EMAIL_NOT_VALID));
            $("#resetPasswordValidationMessage").show();
            return false;
        } else {
            doResetPassword();
        }
    }

    function doResetPassword() {
        $.ajax({
            type: "POST",
            url: "${contextPath}/authentication/resetPassword",
            dataType: 'json',
            data : $("#resetPasswordForm").serialize(),
            cache : false,
            async : true,
            beforeSend: function () {
                $(".resetPasswordButton").attr("disabled", true);
            },
            complete: function () {
                $(".resetPasswordButton").attr("disabled", false);
            },
            success: function (data) {

                if (!showApplicationMessages(data)) {
                    if (data.success == true) {
                        $("#resetPasswordForm").submit();
                    } else {
                        if (data.resetMessage != null) {
                            showEmailLoginMessage(data.resetMessage);
                        }
                    }
                }
            }
        });
    }
</script>








