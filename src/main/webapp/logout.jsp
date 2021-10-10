<%@ page contentType="text/html;charset=UTF-8" language="java" import="java.util.*" %>
<% session=request.getSession(false);
  String id = session.getId();
  session.invalidate();
%>
<html>

<head>
    <title>
    </title>
</head>

<body>
<div align="center">
    <table border="1">
        <tr>
            <td>
                Session Logout Successfully : <% response.getWriter().println(id); %>
            </td>
        </tr>

    </table>
</div>
</body>

</html>
