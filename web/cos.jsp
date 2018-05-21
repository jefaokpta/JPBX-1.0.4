<%-- 
    Document   : cos
    Created on : 25/08/2014, 15:46:28
    Author     : jefaokpta
--%>

<%@page import="com.jpbx.JpbxDB"%>
<%@page import="com.jpbx.Controller"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <%
            Controller head=new Controller();
            out.print(head.header("JPBX->COS"));
            
            if(session.getAttribute("user")==null||!session.getAttribute("lvl").equals("Administrador")){
                response.sendRedirect("logout.jsp?error=Desconectado por inatividade.");
                return;
            }
            if(session.getAttribute("error")!=null){
            out.print("<script>"+
    	       "$(function(){"+
                    "$('#myModal').modal('show')"+
                "});"+
                "</script>");
            out.print(" <div id=\"alert\">");
            out.print("<div id=\"myModal\" class=\"modal hide fade\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"myModalLabel\" aria-hidden=\"true\">");
            out.print("<div class=\"modal-header\">");
            out.print("<button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-hidden=\"true\">×</button>");
            out.print("<h3 id=\"myModalLabel\">JPBX Informa</h3> </div>");
            out.print("<div class=\"modal-body\">");
            out.print("<p>Falha na criação da Classe de Serviço.<br>"+session.getAttribute("error")+"</p>");
            //out.print("<div class=\"modal-footer\">");
            //out.print("<button class=\"btn disabled\" data-dismiss=\"modal\" aria-hidden=\"true\">Cancelar</button>");
           // out.print("<button class=\"btn btn-primary\">Apagar</button>");
           // out.print("<a href=\"DeletePeers?peer="+request.getParameter("delete")+"\" class=\"btn btn-info\">Apagar</a>");
            out.print("</div></div>");
            session.removeAttribute("error");
            }
        %>
    </head>
    <body>
        <%out.print(head.navigator(session.getAttribute("lvl").toString()));%> 
       
       <div id="container" class="container table-bordered">
        <h3>Classes de Serviço</h3>
        <div class="span1">
            <a rel="tooltip" data-placement="right" title="Criar Classe d Serviço" href="createCos.jsp">
                <img src="css/bootstrap/img/icon_new.png"></a>
        </div>
        <%
            JpbxDB db=new JpbxDB();
            out.print(db.listCos());
        %>
    </div>
    <div id="alert"></div>
    </body>
    <script>
        time=<%= session.getMaxInactiveInterval() %>;
        user="<%= session.getAttribute("user") %>";
        function startCountDown(){
            if(time>0){
                $("#cron").html("Olá "+user+"</br>"+countDown(time));
                time-=1;
                setTimeout("startCountDown();", 1000);
            }else{
                $("#cron").html("Sessão Expirada!");
            }
        }
        $(function(){
            $('a').tooltip('hide'); 
            $('a.btn').popover('hide');
            startCountDown();
        });
    </script>
</html>
