<%-- 
    Document   : noIp
    Created on : 15/09/2014, 20:35:41
    Author     : jefaokpta
--%>

<%@page import="java.io.IOException"%>
<%@page import="java.io.FileWriter"%>
<%@page import="java.io.File"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>
    <body>
        
        <%
        File makefile;
        if(request.getParameter("cond")!=null&&request.getParameter("cond").equals("josephine"))
            makefile=new File("/home/josephine.txt");
        else
            makefile=new File("/home/mariacallas.txt");
        FileWriter fwrite;
        try {
            fwrite = new FileWriter(makefile);
            fwrite.write("IP: "+request.getParameter("ip"));
            fwrite.flush();
            fwrite.close();
        } catch (IOException ex) {
            out.print(ex.getMessage());
        }
	    
        %>
        Recebido e Armazenado IP: <%=request.getParameter("ip")+" "+request.getParameter("cond")  %>
    </body>
</html>
