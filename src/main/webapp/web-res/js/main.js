/**
 * Created by roberto on 06/12/2015.
 */

function isEmail(email) {
    var regex = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
    return regex.test(email);
}

function isPhoneNumber(phoneNumber) {
    var regex = /^[\s()+-]*([0-9][\s()+-]*){6,20}$/;
    return regex.test(phoneNumber);
}

function isMobile() {
    if (navigator.userAgent.match(/Android/i)
        || navigator.userAgent.match(/webOS/i)
        || navigator.userAgent.match(/iPhone/i)
        || navigator.userAgent.match(/iPad/i)
        || navigator.userAgent.match(/iPod/i)
        || navigator.userAgent.match(/BlackBerry/i)
        || navigator.userAgent.match(/Windows Phone/i)
    ) {
        return true;
    }
    else {
        return false;
    }
}

/**
 * Shows application messages retrieved from ajax response.
 * It uses the same dialog implemented in the messageDisplayer.jsp
 * @param data
 */
function showApplicationMessages(data) {
    var hasMessages = false;
    var messages = data.messages;
    var redirectUrl = data.redirectUrl;

    /*console.log("message key: " + MESSAGES_KEY);
    console.log("data: " + data);*/

    if (messages != null) {
        hasMessages = true;

        var mainRow = $("<div class=\"row\"></div>");
        var mainCol = $("<div class=\"col-md-12\"></div>");

        for (var i = 0; i < messages.length; i++) {
            var mess = messages[i];

            var messageRow = $("<div class=\"row\"></div>");
            var iconCol = $("<div class=\"col-md-2\"></div>");

            var img;

            var messageClass = "";

            //console.log("message severity: " + mess.severity);

            if (mess.severity == "INFO") {
                img = $("<img src=\"" + CONTEXT_PATH + "/resources/images/Info_32x32.png\" class=\"img-responsive\" />");
                messageClass = "info";
            } else if (mess.severity == "WARNING") {
                img = $("<img src=\"" + CONTEXT_PATH + "/resources/images/Warning_32x32.png\" class=\"img-responsive\" />");
                messageClass = "warning";
            } else if (mess.severity == "FATAL") {
                img = $("<img src=\"" + CONTEXT_PATH + "/resources/images/Fatal_32x32.png\" class=\"img-responsive\" />");
                messageClass = "fatal";
            }

            iconCol.append(img);

            var messageCol = $("<div class=\"col-md-10\"></div>");

            var messageContent = "";

            /*console.log("message link: " + mess.link);
            console.log("message text: " + mess.message);*/

            if (mess.link != null) {
                messageContent = $("<a class= " + messageClass + " href=" + mess.link + ">" + mess.message + "</a>");
            } else {
                messageContent = $("<span class=" + messageClass + ">" + mess.message + "</span>");
            }

            messageCol.append(messageContent);

            messageRow = messageRow.append(iconCol);
            messageRow = messageRow.append(messageCol);

            mainCol = mainCol.append(messageRow);
        }

        mainRow = mainRow.append(mainCol);
    }

    //console.log(mainRow);

    if (hasMessages) {
        $("#messagesDialog").html("");
        $("#messagesDialog").append(mainRow);
        openApplicationMessagesDialog(redirectUrl);
    }

    return hasMessages;
}

function openApplicationMessagesDialog(redirectUrl) {
    $("#messagesDialog").dialog({
        modal: true,
        height: 'auto',
        width: '40%',
        autoOpen: true,
        position: {my: "center top", at: "center top"},
        closeText: 'X',
        buttons: [
            {
                text: "Ok",
                /*icons: {
                 primary: "ui-icon-heart"
                 },*/
                click: function () {
                    $(this).dialog("close");
                }

                // Uncommenting the following line would hide the text,
                // resulting in the label being used as a tooltip
                //showText: false
            }
        ],
        close : function(event, ui) {
            if (redirectUrl != null && $.trim(redirectUrl) != "") {
                window.location.href = redirectUrl;
            }
        }
    });
}

function execInSession(callback) {

    var canExec = true;
    /*
    Checks on server if user is authenticated.
    If so, calls the callback method otherwise shows application dialog with error message
     */
    $.ajax({
        url: CONTEXT_PATH + "/authentication/isAuthenticated",
        dataType: "json",
        async : callback != null,
        cache : false,
        success : function(data) {
            if (!showApplicationMessages(data)) {
                if (data.authenticated == true) {
                    if (callback != null) {
                        callback();
                    }
                } else {
                    canExec = false;
                    showApplicationMessages({messages : [
                        {
                            severity : "WARNING",
                            link : null,
                            message : SESSION_EXPIRED_MESSAGE
                        }
                    ],
                    redirectUrl : CONTEXT_PATH + "/index"
                    });
                }
            }
        }
    });

    return canExec;
}

function isYoutubeUrl(url) {
    var pattern = /(?:http?s?:\/\/)?(?:www\.)?(?:youtube\.com|youtu\.be)\/(?:watch\?v=)?(.+)/g;

    if (url != null && pattern.test(url)) {
        return true;
    } else {
        return false;
    }
}

function isSoundCloudUrl(url){
    var regexp = /^https?:\/\/(soundcloud.com|snd.sc)\/(.*)$/;
    return url.match(regexp) && url.match(regexp)[2]
}

/**
 * Checks if url is a youtube url. Then adds the video into container based on submitSettings object.
 * Settings has following attributes:
 *      name: the hidden form parameter name
 *      saveCallback: the function to be called if on viedo add. If it's not null and returs new video id, the video will be added to container
 *      deleteCallback: the function to be called if a video gets deleted. It takes the videoId to delete on server. If it's not null and returns true, the video will be removed from container
 *      videoId: the persistent video id. If this parameter is set, saveCallback should be null.
 *      readOnly: (dafault false) - if true, delete link won't be added
 * @param url youtube url
 * @param container jQuery container div
 * @param submitSettings an object with settings
 * @returns {*}
 */
function addYoutubeVideo(url, container, submitSettings) {
    var success = isYoutubeUrl((url));

    var readonly = false;
    if (submitSettings.readOnly != null && submitSettings.readOnly != undefined) {
        readonly = submitSettings.readOnly;
    }

    /*
    * If it's a youtube video and, if there is a save callback function and it retirns true, viedel gets added.
    * If save callback function is not present video gets added. Otherwise, no.*/
    var videoId = submitSettings.videoId;
    if (success && ((submitSettings.saveCallback != null && (videoId = submitSettings.saveCallback()) != null) || submitSettings.saveCallback == null)) {

        //console.log("addYoutubeVideo - videoId: " + videoId);

        var iFrameCode = '<iframe width="200" height="150" src="http://www.youtube.com/embed/$1" frameborder="0" allowfullscreen></iframe>';

        var video_id = "";

        if (url.indexOf("v=") != -1) {
            video_id = url.split('v=')[1];

            var ampersandPosition = video_id.indexOf('&');
            if(ampersandPosition != -1) {
                video_id = video_id.substring(0, ampersandPosition);
            }
        } else {
            video_id = url.substring(url.lastIndexOf("/") + 1);
        }

        //console.log("video id: " + video_id);

        iFrameCode = iFrameCode.replace("$1", video_id);

        var iFrame = $(iFrameCode);

        iFrame = iFrame.appendTo(container);

        var hdn = $("<input type='hidden' name='" + submitSettings.name + "' value='" + url + "' />");
        hdn.appendTo(container);

        console.log("readonly: " + readonly);

        if (!readonly) {
            var del = $("<img src='" + CONTEXT_PATH + "/resources/images/delete_16x16.png' />");
            del = del.css("cursor", "pointer");

            del = del.appendTo(container);

            del = del.position({
                my: "right top",
                at: "right top",
                of: iFrame
            });

            del.click(function() {

                /*
                 * If delete callback is present and returns true, video gets deleted.
                 * If delete callback is not present, video gets deleted.
                 * Otherwise, no.*/
                if ((submitSettings.deleteCallback != null && submitSettings.deleteCallback(videoId)) || submitSettings.deleteCallback == null) {
                    iFrame.remove();
                    hdn.remove();
                    $(this).remove();
                }
            });
        }

    } else {
        // If url is correctly validated but save callback returns false, this variable needs to be overwritten.
        success = false;
    }

    return success;
}

$(function() {
    $(document).tooltip({
        position : { my: "center bottom", at: "center top" }
    });

    if (isMobile()) {
        $(".forMobile").show();
        $(".forDesktop").hide();
    } else {
        $(".forMobile").hide();
        $(".forDesktop").show();
    }
})

