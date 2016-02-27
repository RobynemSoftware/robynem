<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript" defer>
    $(function () {
        <c:if test="${not empty messages}">
        var data = new Object();
        var messageList = new Array();

        <c:forEach var="message" items="${messages}">
        messageList.push({
            severity: "${message.severity}",
            message: "${message.message}",
            <c:choose>
                <c:when test="${not empty message.link}">
                    link: "${message.link}"
                </c:when>
                <c:otherwise>
                    link: null
                </c:otherwise>
            </c:choose>
        });

        </c:forEach>

        data.messages = messageList;
        data.redirectUrl = "${redirectUrl}";

        showApplicationMessages(data);
        </c:if>
    });
</script>


