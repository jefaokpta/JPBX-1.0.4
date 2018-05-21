<%-- 
    Document   : return
    Created on : 28/01/2013, 16:34:49
    Author     : jefaokpta
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%
            session.setAttribute("return", request.getParameter("return"));
            response.sendRedirect(request.getParameter("dest"));
        %>
    </head>
</html>
