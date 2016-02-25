<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<div class="row">
    <div class="col-md-12">

        <form id="signupForm" role="form" action="${contextPath}/authentication/signup" method="post">

            <div class="form-group">
                <label for="firstName"><spring:message code="login.signup.first-name"></spring:message>:</label>
                <input type="text" class="form-control" id="firstName" name="firstName"/>
            </div>

            <div class="form-group">
                <label for="lastName"><spring:message code="login.signup.last-name"></spring:message>:</label>
                <input type="text" class="form-control" id="lastName" name="lastName"/>
            </div>

            <div class="form-group">
                <label for="emailAddress"><spring:message code="login.signup.email-address"></spring:message>:</label>
                <input type="email" class="form-control" id="emailAddress" name="emailAddress"/>
                <div id="emailLoadingImage" class="img-responsive" style="display:none;">
                    <img src="${contextPath}/resources/images/ajax-loader.gif">
                </div>
                <div id="emailAddressValidationMessage" style="display:none;">

                </div>
            </div>

            <div class="form-group">
                <label for="password"><spring:message code="login.signup.password"></spring:message>:</label>
                <input type="password" class="form-control" id="password" name="password"/>
            </div>

            <div class="form-group">
                <label for="confirmPassword"><spring:message code="login.signup.confirm-password"></spring:message>:</label>
                <input type="password" class="form-control" id="confirmPassword" name="confirmPassword"/>
            </div>

            <div class="form-group">
                <div class="row">
                    <div class="col-md-12">
                        <button id="sendRegistrationButton"><spring:message code="login.signup.send"></spring:message></button>
                        &nbsp;
                        <button id="signupCancelButton"><spring:message code="global.cancel"></spring:message></button>
                    </div>
                </div>
            </div>
        </form>

    </div>
</div>

<!-- MESSAGE DIALOG -->
<div id="signupMessageDialog" style="display: none;">


        <div class="row">

            <div class="col-md-12">


                    <div class="row">

                        <div class="col-md-4">

                            <img src="${contextPath}/resources/images/Warning_32x32.png" class="img-responsive" />

                        </div>

                        <div class="col-md-8">
                            <span id="signupMessage" class="fatal">
                                ${signupMessage}
                            </span>
                        </div>
                    </div>
            </div>
        </div>
</div>



<script type="text/javascript">
    var EMAIL_ALREADY_EXISTS = "<spring:message code="login.signup.email-already-exists"></spring:message>";
    var EMAIL_NOT_VALID = "<spring:message code="login.signup.email-invalid"></spring:message>";
    var FIST_NAME_MANDATORY = "<spring:message code="login.signup.first-name-mandatory"></spring:message>";
    var LAST_NAME_MANDATORY = "<spring:message code="login.signup.last-name-mandatory"></spring:message>";
    var EMAIL_MANDATORY = "<spring:message code="login.signup.email-mandatory"></spring:message>";
    var PASSWORD_MANDATORY = "<spring:message code="login.signup.password-mandatory"></spring:message>";
    var PASSWORDS_DO_NOT_MATCH = "<spring:message code="login.signup.password-does-not-match"></spring:message>";
    var PASSWORDS_LENGHT_INVALID = "<spring:message code="login.signup.password-length-invalid"></spring:message>";


    $(function() {
        /*$("#emailAddress").blur(function() {
            validateEmail()
        });*/

        $("#emailAddress").change(function() {
            validateEmail()
        });

        $("#sendRegistrationButton").click(function() {
            $(this).attr("disabled", true);

            var firstName = $.trim($("#firstName").val());
            var lastName = $.trim($("#lastName").val());
            var email = $.trim($("#emailAddress").val());
            var password = $.trim($("#password").val());
            var confirmPassword = $.trim($("#confirmPassword").val());

            if (firstName == '') {
                showSignupMessage(FIST_NAME_MANDATORY);
                $(this).attr("disabled", false);
                return false;
            }

            if (lastName == '') {
                showSignupMessage(LAST_NAME_MANDATORY);
                $(this).attr("disabled", false);
                return false;
            }

            if (email == '') {
                showSignupMessage(EMAIL_MANDATORY);
                $(this).attr("disabled", false);
                return false;
            }

            if (password == '') {
                showSignupMessage(PASSWORD_MANDATORY);
                $(this).attr("disabled", false);
                return false;
            }

            if (password.length < 5) {
                showSignupMessage(PASSWORDS_LENGHT_INVALID);
                $(this).attr("disabled", false);
                return false;
            }

            if (password != confirmPassword) {
                showSignupMessage(PASSWORDS_DO_NOT_MATCH);
                $(this).attr("disabled", false);
                return false;
            }

            if (!validateEmail()) {
                showSignupMessage(EMAIL_NOT_VALID);
                $(this).attr("disabled", false);
                return false;
            }


            if (emailExists(email)) {
                showSignupMessage(EMAIL_ALREADY_EXISTS);
                $(this).attr("disabled", false);
                return false;
            }

            $("#signupForm").submit();
        });

        $("#signupCancelButton").click(function() {
            $("#loginDialog").dialog("close");
            return false;
        });
    });

    function emailExists(email) {
        var exists = false;
        $("#emailAddressValidationMessage").hide();

        $.ajax({
            type: "GET",
            url: "${contextPath}/authentication/checkEmail",
            dataType: 'json',
            data : "email=" + email,
            cache : false,
            async : false,
            beforeSend: function () {
                $("#emailLoadingImage").show();
            },
            complete: function () {
                $("#emailLoadingImage").hide();
            },
            success: function (data) {
                console.log("data: "+ data.exists);
                exists = data.exists;

                if (exists) {
                    valid = false;

                    console.log("email exists!");

                    $("#emailAddressValidationMessage").html($("<span class=\"fatal\"></span>").html(EMAIL_ALREADY_EXISTS));
                    $("#emailAddressValidationMessage").show();
                }
            }
        });

        return exists;
    }

    function validateEmail() {
        var valid = true;

        $("#emailAddressValidationMessage").hide();
        var email = $.trim($("#emailAddress").val());

        console.log("email: " + email);
        console.log("is email: " + isEmail(email));

        if (email != "") {
            if(!isEmail(email)) {
                valid = false;

                console.log("is mobile: " + isMobile());
                $("#emailAddressValidationMessage").html($("<span class=\"fatal\"></span>").html(EMAIL_NOT_VALID));
                $("#emailAddressValidationMessage").show();

            } else  {
                emailExists(email); // checks if email exists. Logic inside function.
            }
        }

        return valid;
    }

    function showSignupMessage(message) {
        $("#signupMessage").html(message);

        $("#signupMessageDialog").dialog({
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
</script>









