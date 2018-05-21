<%-- 
    Document   : relFaxes
    Created on : 30/10/2014, 17:56:23
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
            out.print(head.header("JPBX->Faxes"));
            if(session.getAttribute("user")==null){
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
            out.print("<p>Oops, algo deu errado.<br>"+session.getAttribute("error")+"</p>");
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
        <h3>Lista de FAX Enviados/Recebidos</h3>      
        <div class="span1">
            <a rel="tooltip" data-placement="right" title="Novo Arquivo PDF">
                <img onclick="uploadPdf();" src="css/bootstrap/img/icon_new.png"></a>
        </div>
        <form id="frmSearch" name="frm2" action="relFaxes.jsp" method="POST">
            <div class="offset5 span5">
                <select class="span2" name="type">
                    <option value="Recebido">Recebidos</option>
                    <option value="Enviado">Enviados</option>
                    <option value="Carregado">Carregados</option>
                </select>
                <button style="float: left" type="submit" class="btn btn-info">Filtrar</button>
            </div>
        </form>
        <%
            JpbxDB db=new JpbxDB();
            if(session.getAttribute("lvl").equals("Administrador"))
                if(request.getParameter("type")!=null)
                    out.print(db.listFaxes(request.getParameter("type")));
                else
                    out.print(db.listFaxes("Recebido"));
            else if(request.getParameter("type")!=null)
                out.print(db.listFaxes(request.getParameter("type"),db.getUserCompany(session.getAttribute("user").toString())));
            else
                out.print(db.listFaxes("Recebido",db.getUserCompany(session.getAttribute("user").toString())));
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
            function deleteFax(id){
                $('#alert').html('<div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" \n\
                    aria-hidden="true">\n\
                <div class="modal-header">\n\
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>\n\
                <h3 id="myModalLabel">JPBX Informa</h3> </div>\n\
                <div class="modal-body">\n\
                <p>Deseja realmente apagar este Fax?</p></div>\n\
                <div class="modal-footer">\n\
                <button class="btn disabled" data-dismiss="modal" aria-hidden="true">Cancelar</button>\n\
                <a href="DeleteFax?fax='+id+'" class="btn btn-info">Apagar</a>\n\
                </div></div>');
                $('#myModal').modal('show');
            }
            $(function(){
                $('a').tooltip('hide');
                startCountDown();
            });
        </script>
        <div id="alert"></div>
    </body>
</html>
