<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>

<head>
    <title>StackOverflow search</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link rel="stylesheet" type="text/css" href="/search/styles.css">
</head>

<body>
    <h1>StackOverflow search</h1>
    <form action="" method="post">
        <input type="text" name="title" />
        <input type="hidden" name="page" value=1 />
        <input type="submit" value="Search" />
    </form>
    <c:if test="${!empty error}">
        <c:out value="${error}" />
    </c:if>
    <c:if test="${!empty questions}">
        <p>Found ${total} questions by title "${title}"</p>
        <table>
            <thead>
                <tr>
                    <th>#</th>
                    <th>Date</th>
                    <th>Title</th>
                    <th>Author</th>
                    <th>Answers</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${questions}" var="q" varStatus="loop">
                    <tr <c:if test="${q.is_answered}">class="answered"</c:if>>
                        <td>${(page - 1) * 10 + loop.index + 1}</td>
                        <td>${q.date}</td>
                        <td><a href="${q.link}">${q.title}</a></td>
                        <td><a href="${q.owner.link}"><img src="${q.owner.profile_image}" width="30"></a>${q.owner.display_name}</td>
                        <td>${q.answer_count}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <c:if test="${total > 10}">
            <form action="" method="post" style="display: inline-block;">
                <input type="hidden" name="title" value=${title} />
                <input type="hidden" name="page" value=${page - 1} />
                <input type="submit" value="Previous 10" <c:if test="${page <= 1}">disabled</c:if> />
            </form>

            <form action="" method="post" style="display: inline-block;">
                <input type="hidden" name="title" value=${title} />
                <input type="hidden" name="page" value=${page + 1} />
                <input type="submit" value="Next 10" <c:if test="${page * 10 > total}">disabled</c:if> />
            </form>
        </c:if>
    </c:if>
</body>

</html>