<%@ page import="com.robynem.mit.web.util.Constants" %>
<%@ page import="com.robynem.mit.web.util.ImageSize" %>
<%@ page import="com.robynem.mit.web.util.MessageSeverity" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:include page="../common/messagesDisplayerAsync.jsp"></jsp:include>

<div class="row">

    <div class="col-md-12">

        <!-- DESCRIPTION -->
        <div id="editBandMedia_title" class="row">
            <div  class="col-md-12">
                <span>
                    <spring:message code="band.media.title"></spring:message>
                </span>
            </div>
        </div>

        <br>

        <div class="accordion" id="editBandMediaAccordion">
            <!-- VIDEOS -->
            <div class="accordion-group">
                <div class="accordion-heading">
                    <a class="accordion-toggle" data-toggle="collapse" data-parent="#editBandMediaAccordion" href="#collapseVideos">
                        <span><spring:message code="band.media.videos"></spring:message> </span>
                    </a>
                </div>
                <div id="collapseVideos" class="accordion-body collapse in">
                    <div class="accordion-inner">

                        <div class="row">

                            <div class="col-md-3">
                                <label for="addVideoText"><spring:message code="band.media.videos.add-link"></spring:message> </label>
                            </div>

                            <div class="col-md-6">
                                <input id="addVideoText" type="text" class="form-control" />
                            </div>

                            <div class="col-md-3">
                                <button id="addVideoButton" class="button btn-default"><spring:message code="global.add"></spring:message> </button>
                            </div>
                        </div>

                        <%--<div class="row">

                            <div class="col-md-12 saveBandLink">
                                <span class="saveBandLink">
                                    <spring:message code="band.media.videos.save-alert"></spring:message>

                                    <a id="saveVideosLink" href="#"><spring:message code="global.here"></spring:message> </a>
                                </span>
                            </div>

                        </div>--%>

                        <%--<form:form id="videosForm" method="post" action="${contextPath}/private/editBand/saveVideos" modelAttribute="bandModel" >--%>
                            <input type="hidden" name="currentTabIndex" value="2">

                            <div id="videoContainerRow" class="row">

                            </div>
                        <%--</form:form>--%>


                    </div>
                </div>
            </div>

            <br />

            <!-- IMAGES -->
            <div class="accordion-group">
                <div class="accordion-heading">
                    <a class="accordion-toggle" data-toggle="collapse" data-parent="#editBandMediaAccordion" href="#collapseImages">
                        <span><spring:message code="band.media.images"></spring:message> </span>
                    </a>
                </div>
                <div id="collapseImages" class="accordion-body collapse in">
                    <div class="accordion-inner">

                        <form:form id="galleryForm" method="post"
                                   action="${contextPath}/private/editBand/addGalleryImage"
                                   modelAttribute="bandModel" enctype="multipart/form-data">
                            <div class="row">

                                <div class="col-md-3">
                                    <label for="addImageFile"><spring:message
                                            code="band.media.images.add-image"></spring:message> </label>
                                </div>

                                <div class="col-md-6">

                                    <input id="addImageFile" type="file" name="image" class="form-control"/>


                                </div>

                                <div class="col-md-3">
                                    <button id="addImageButton" class="button btn-default"><spring:message
                                            code="global.add"></spring:message></button>
                                </div>
                            </div>

                            <!-- Progress bar -->
                            <div class="row">
                                <div class="col-md-12">
                                    <div  class="progress progress-striped">
                                        <div id="galleryProgress" class="progress-bar progress-success" role="progressbar" aria-valuenow="0"
                                             aria-valuemin="0" aria-valuemax="100" style="width:100%">
                                            <span class="sr-only"></span>
                                            <span ></span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </form:form>

                        <div id="imageContainerRow" class="row">

                        </div>

                    </div>
                </div>
            </div>

            <br>

            <!-- AUDIO -->
            <div class="accordion-group">
                <div class="accordion-heading">
                    <a class="accordion-toggle" data-toggle="collapse" data-parent="#editBandMediaAccordion" href="#collapseAudio">
                        <span><spring:message code="band.media.audio"></spring:message> </span>
                    </a>
                </div>
                <div id="collapseAudio" class="accordion-body collapse in">
                    <div class="accordion-inner">

                        <form:form id="audioForm" method="post"
                                   action="${contextPath}/private/editBand/addAudio"
                                   modelAttribute="bandModel" enctype="multipart/form-data">
                            <div class="row">

                                <div class="col-md-6">
                                    <label for="addAudioFile"><spring:message
                                            code="band.media.audio.add-audio"></spring:message> </label>
                                </div>

                                <div class="col-md-6">

                                    <input id="addAudioFile" type="file" name="audio" class="form-control"/>
                                </div>

                            </div>

                            <div class="row">

                                <div class="col-md-3">

                                    <label for="addAudioName"><spring:message code="band.media.audio.name"></spring:message> </label>
                                </div>

                                <div class="col-md-6">

                                    <input id="addAudioName" type="text" name="name" class="form-control"/>
                                </div>

                                <div class="col-md-3" style="text-align: right;">
                                    <button id="addAudioButton" class="button btn-default"><spring:message
                                            code="global.add"></spring:message></button>
                                </div>
                            </div>

                            <!-- Progress bar -->
                            <div class="row">
                                <div class="col-md-12">
                                    <div  class="progress progress-striped">
                                        <div id="audioProgress" class="progress-bar progress-success" role="progressbar" aria-valuenow="0"
                                             aria-valuemin="0" aria-valuemax="100" style="width:100%">
                                            <span class="sr-only"></span>
                                            <span ></span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </form:form>

                        <div id="audioContainerRow" class="row">

                        </div>

                    </div>
                </div>
            </div>

        </div>




    </div>

</div>

<script type="text/javascript">

    $(function() {

        $("#addVideoButton").click(function(e) {
            //e.preventDefault();

            var url = $.trim($("#addVideoText").val());

            if (url != "") {

                var videoAdded = addYoutubeVideo(url, $("#videoContainerRow"),
                        {
                            name : "youtubeUrl",

                            saveCallback : function() {
                                var newVideoId = null;

                                if (execInSession(null)) {
                                    $.ajax({
                                        url : "${contextPath}/private/editBand/saveVideo",
                                        data : {
                                            youtubeUrl : url
                                        },
                                        type : "post",
                                        dataType : "json",
                                        async : false,
                                        cache : false,
                                        beforeSend : function (jqXHR, settings ) {
                                            $.blockUI();
                                        },
                                        success : function(data) {
                                            showApplicationMessages(data);

                                            if (data.success == true) {
                                                newVideoId = data.newVideoId;
                                                //console.log("newVideoId: " + newVideoId);
                                                showBandStatus();
                                            }
                                        },
                                        complete : function() {
                                            $.unblockUI();
                                        }
                                    });
                                }

                                // Returns new video id to addYoutubeVideo function.
                                return newVideoId;
                            },

                            deleteCallback : deleteVideo
                        });

                if (videoAdded) {
                    $("#addVideoText").val("");
                }
            }

            return false;
        });

        <%-- Adds model videos dinamically --%>
        <c:if test="${ not empty bandModel.mediaModel.videos}">
            <c:forEach var="video" items="${bandModel.mediaModel.videos}">
                addYoutubeVideo("${video.youtubeUrl}", $("#videoContainerRow"), {
                    name : "youtubeUrl",
                    deleteCallback : deleteVideo,
                    videoId : ${video.id}
                });
            </c:forEach>
        </c:if>

        /*$("#saveVideosLink").click(function() {
            $("#videosForm").submit();
        });*/

        // Sets video form async
        //initVideoForm();

        // inits gallery form
        initGalleryForm();

        // inits audio form
        initAudioForm();

        initAudioControls();

    });

    function initVideoForm() {
        var options = {
            dataType : "json",
            beforeSubmit: function() {
                return execInSession(null);
            },
            beforeSend: function()
            {

            },
            uploadProgress: function(event, position, total, percentComplete)
            {


            },
            success: function(data, textStatus, jqXHR)
            {
                showApplicationMessages(data);
            },
            complete: function(response)
            {

            },
            error: function()
            {

            }

        };

        $("#videosForm").ajaxForm(options);
    }



    function initGalleryForm() {
        <c:forEach var="picId" items="${bandModel.mediaModel.imageIds}">
        addGalleryImage(${picId});
        </c:forEach>

        $("#galleryProgress").parent().hide();

        var options = {
            beforeSubmit: function() {

                if ($.trim($("#addImageFile").val()) == "" ) {
                    return false;
                }

                return execInSession(null);
            },
            beforeSend: function()
            {
                $("#galleryProgress").parent().show();

                $("#galleryProgress").addClass("active");
                $("#galleryProgress").attr("aria-valuenow", "0");
                $("#galleryProgress").children("span").html("0%");
            },
            uploadProgress: function(event, position, total, percentComplete)
            {
                $("#galleryProgress").attr("aria-valuenow", percentComplete);
                $("#galleryProgress").children("span").html(percentComplete + "%");

            },
            success: function(data)
            {

                showApplicationMessages(data);

                $("#galleryProgress").removeClass("active");
                $("#galleryProgress").attr("aria-valuenow", "100");
                $("#galleryProgress").children("span").html("100");

                //console.log("uploadedImageId: " + data.uploadedImageId);
                if (data.uploadedImageId != null) {
                    addGalleryImage(data.uploadedImageId);
                }
            },
            complete: function(response)
            {
                $("#addImageFile").val("");
            },
            error: function()
            {

            }

        };

        $("#galleryForm").ajaxForm(options);
    }

    function initAudioForm() {
        <c:forEach var="audio" items="${bandModel.mediaModel.audios}">
            addAudio(${audio.id}, "${audio.name}");
        </c:forEach>

        $("#audioProgress").parent().hide();

        var options = {
            beforeSubmit: function() {

                if ($.trim($("#addAudioFile").val()) == "" ) {
                    return false;
                }

                if ($.trim($("#addAudioName").val()) == "") {
                    showApplicationMessages({
                        "<%=Constants.APPLICATION_MESSAGES_KEY%>" : [
                            {
                                message : "<spring:message code="band.validation.insert-audio-name"></spring:message>",
                                severity: "<%=MessageSeverity.FATAL%>",
                                link : null
                            }
                        ]
                    });

                    return false;
                }

                return execInSession(null);
            },
            beforeSend: function()
            {
                $("#audioProgress").parent().show();

                $("#audioProgress").addClass("active");
                $("#audioProgress").attr("aria-valuenow", "0");
                $("#audioProgress").children("span").html("0%");
            },
            uploadProgress: function(event, position, total, percentComplete)
            {
                $("#audioProgress").attr("aria-valuenow", percentComplete);
                $("#audioProgress").children("span").html(percentComplete + "%");

            },
            success: function(data)
            {

                showApplicationMessages(data);

                $("#audioProgress").removeClass("active");
                $("#audioProgress").attr("aria-valuenow", "100");
                $("#audioProgress").children("span").html("100");

                //console.log("uploadedImageId: " + data.uploadedImageId);
                if (data.success == true && data.uploadedAudioId != null && data.uploadedAudioName != null) {
                    addAudio(data.uploadedAudioId, data.uploadedAudioName);

                    showBandStatus();
                }
            },
            complete: function(response)
            {
                $("#addAudioFile").val("");
                $("#addAudioName").val("");
            },
            error: function()
            {

            }

        };

        $("#audioForm").ajaxForm(options);
    }

    function addGalleryImage(imageId) {



        var url = CONTEXT_PATH + "/media/getImage?imageId=" + imageId + "&size=<%=ImageSize.MEDIUM%>";

        var img = $("<iframe  src='" + url + "' frameborder='0' ></iframe>");
        img = img.css("max-width", "140px");
        img = img.css("max-height", "140px");

        var row = $("#imageContainerRow");
        var col = $("<div id='" + imageId + "' class='col-md-4 galleryImageCol'></div>");

        col = col.append(img);

        col = col.appendTo(row);

        var del = $("<img src='" + CONTEXT_PATH + "/resources/images/delete_16x16.png' />");
        del = del.css("cursor", "pointer");

        del.appendTo(col);

        del = del.position({
            my: "right top",
            at: "right top",
            of: img
        });

        del.click(function() {
            execInSession(function() {

                $.ajax({
                   url : "${contextPath}/private/editBand/removeGalleryImage",
                    data : {
                        imageId : imageId
                    },
                    async : true,
                    type : "post",
                    dataType : "json",
                    success : function(data) {
                        showApplicationMessages(data);

                        if (data.success == true) {
                            col.remove();
                        }
                    }
                });

            });
        });
    }

    function addAudio(audioId, name) {


        var url = CONTEXT_PATH + "/media/getAudio?audioId=" + audioId;

        var notSupportedMessage = "<spring:message code="global.html5.audio-not-supported"></spring:message>";

        var audio = $("<audio controls class='audio' preload='none'><source src='" + url + "' type='audio/mpeg'>" + notSupportedMessage + "</audio>");
        var audioName = $("<span>" + name + "</span>");
        var audioDelete = $("<img src='" + CONTEXT_PATH + "/resources/images/delete_32x32.png' class='img-responsive' />");
        audioDelete.css("cursor", "pointer");

        var row = $("<div class='row'></div>");
        var colAudio = $("<div class='col-md-6'></div>");
        var colName = $("<div class='col-md-4'></div>");
        var colDelete = $("<div class='col-md-2'></div>");

        colAudio.append(audio);
        colName.append(audioName);
        colDelete.append(audioDelete);

        row.append(colAudio).append(colName).append(colDelete);

        var container = $("#audioContainerRow");

        container.append(row);



        audioDelete.click(function() {
            if (confirm("<spring:message code="band.media.audio.confirm-delete"></spring:message>")) {
                execInSession(function() {

                    $.ajax({
                        url : "${contextPath}/private/editBand/removeAudio",
                        data : {
                            audioId : audioId
                        },
                        async : true,
                        type : "post",
                        dataType : "json",
                        success : function(data) {
                            showApplicationMessages(data);

                            if (data.success == true) {
                                row.remove();
                            }
                        }
                    });

                });
            }
        });
    }

    function initAudioControls() {
        $(".audio").bind("onerror", function() {
            $(this).load();
        });
    }

    function deleteVideo(videoId) {
        if (videoId != null && execInSession(null)) {
            var success = false;

            $.ajax({
                url : "${contextPath}/private/editBand/removeVideo",
                data : {
                    videoId : videoId
                },
                type : "post",
                dataType : "json",
                async : false,
                cache : false,
                beforeSend : function (jqXHR, settings ) {
                    $.blockUI();
                },
                success : function(data) {
                    showApplicationMessages(data);

                    success = data.success;

                    showBandStatus();
                },
                complete : function() {
                    $.unblockUI();
                }
            });
        }

        return success;
    }

</script>