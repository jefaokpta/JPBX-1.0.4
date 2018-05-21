<%-- 
    Document   : logout
    Created on : 22/01/2013, 15:54:45
    Author     : jefaokpta
--%>

<%@page import="com.jpbx.JpbxDB"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%
            JpbxDB db=new JpbxDB();
            if(request.getParameter("error").equals("logout")){
                session.invalidate();
                response.sendRedirect("index.jsp");
            }
            else{
                session.setAttribute("alert", request.getParameter("error"));
                response.sendRedirect("index.jsp");
            }
        %>
    </head>

</html>
