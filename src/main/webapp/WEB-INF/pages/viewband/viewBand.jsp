<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <jsp:include page="../common/head-include.jsp"></jsp:include>

    <%--<link rel="stylesheet" href="${contextPath}/resources/css/viewBand.css?v=${softwareVersion}">--%>
    <title>${siteTitle}</title>
</head>

<body>
    <!-- MESSAGES START -->
    <jsp:include page="../common/messagesDisplayer.jsp"></jsp:include>
    <!-- MESSAGES END -->

    <div id="container" class="container mainContainer pageContent">

        <!-- MENU START -->
        <jsp:include page="../common/menu.jsp">
            <jsp:param name="activeItem" value="menuCreateBand"></jsp:param>
        </jsp:include>
        <!-- MENU END -->

        <div id="viewBandContainer" class="row">

        </div>

    </div>
</body>

<script type="text/javascript">
    $(function () {
        $.ajax({
            url : "${contextPath}/viewBand/viewContent",
            data : {
                bandId : ${bandId}
            },
            dataType : "html",
            cache : false,
            success : function (data) {
                $("#viewBandContainer").html(data);
            }
        });
    });
</script>