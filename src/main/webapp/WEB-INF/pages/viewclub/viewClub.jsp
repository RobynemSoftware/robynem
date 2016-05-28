<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<tiles:insertDefinition name="defaultTemplate">
    <tiles:putAttribute name="contextVariables">
        <c:set var="redirectView" value="forward:/viewClub?clubId=${clubId}" scope="session" />
        &nbsp;
    </tiles:putAttribute>

    <tiles:putAttribute name="headDeclarations">

        <script type="text/javascript">
            $(function () {
                $.ajax({
                    url : "${contextPath}/viewClub/viewContent",
                    data : {
                        bandId : ${clubId}
                    },
                    dataType : "html",
                    cache : false,
                    success : function (data) {
                        $("#viewBandContainer").html(data);
                    }
                });
            });


        </script>

    </tiles:putAttribute>


    <tiles:putAttribute name="body">
        <div id="viewBandContainer" class="row">

        </div>
    </tiles:putAttribute>
</tiles:insertDefinition>