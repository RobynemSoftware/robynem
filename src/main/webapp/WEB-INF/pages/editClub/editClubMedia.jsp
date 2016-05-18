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
        <div id="editClubMedia_title" class="row">
            <div  class="col-md-12">
                <span>
                    <spring:message code="club.media.title"></spring:message>
                </span>
            </div>
        </div>

        <br>

        <div class="accordion" id="editClubMediaAccordion">

            <!-- IMAGES -->
            <div class="accordion-group">
                <div class="accordion-heading">
                    <a class="accordion-toggle" data-toggle="collapse" data-parent="#editClubMediaAccordion" href="#collapseImages">
                        <span><spring:message code="band.media.images"></spring:message> </span>
                    </a>
                </div>
                <div id="collapseImages" class="accordion-body collapse in">
                    <div class="accordion-inner">

                        <form:form id="galleryForm" method="post"
                                   action="${contextPath}/private/editClub/addGalleryImage"
                                   modelAttribute="clubModel" enctype="multipart/form-data">
                            <div class="row">

                                <div class="col-md-3">
                                    <label for="addImageFile"><spring:message
                                            code="band.media.images.add-image"></spring:message> </label>
                                </div>

                                <div class="col-md-6">

                                    <input id="addImageFile" type="file" name="image" class="form-control"/>


                                </div>

                                <div class="col-md-3">
                                    <button id="addImageButton" class="button"><spring:message
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

        </div>




    </div>

</div>

<script type="text/javascript">

    $(function() {

        // inits gallery form
        initGalleryForm();

    });


    function initGalleryForm() {
        <c:forEach var="picId" items="${clubModel.mediaModel.imageIds}">
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

                    showClubStatus();
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
                   url : "${contextPath}/private/editClub/removeGalleryImage",
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

                            showClubStatus();
                        }
                    }
                });

            });
        });
    }

</script>