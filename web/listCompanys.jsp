<%-- 
    Document   : listCompanys
    Created on : 10/10/2014, 16:36:23
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
            out.print(head.header("JPBX->Companys"));
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
            out.print("<p>Falha na criação da Empresa.<br>"+session.getAttribute("error")+"</p>");
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
        <h3>Cadastro de Empresas</h3>
        <a class="span1" rel="tooltip" data-placement="right" title="Criar nova Empresa" href="createCompany.jsp"><img src="css/bootstrap/img/icon_new.png"></a>
        <%
            JpbxDB db=new JpbxDB();
            out.print(db.listCompanys());
        %>
    </div>
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
                startCountDown();
            });
        </script>
        <div id="alert"></div>
    </body>
</html>
