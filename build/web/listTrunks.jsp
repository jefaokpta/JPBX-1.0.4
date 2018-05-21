<%-- 
    Document   : listTrunks
    Created on : 25/02/2013, 15:24:32
    Author     : jefaokpta
--%>

<%@page import="com.jpbx.Controller"%>
<%@page import="com.jpbx.JpbxDB"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        
        <%
            Controller head=new Controller();
            out.print(head.header("JPBX->Trunks"));
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
            out.print("<p>Falha na criação do Tronco. <br>"+session.getAttribute("error")+"</p>");
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
        <h3>Cadastro de Troncos</h3>
        <a class="span1" rel="tooltip" data-placement="right" title="Criar novo tronco" href="#" onclick="selectTech();"><img src="css/bootstrap/img/icon_new.png"></a>
        <%
            JpbxDB db=new JpbxDB();
            out.print(db.listTrunks());
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
            function deleteTrunk(trunk){
                $('#alert').html('<div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" \n\
                    aria-hidden="true">\n\
                <div class="modal-header">\n\
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>\n\
                <h3 id="myModalLabel">JPBX Informa</h3> </div>\n\
                <div class="modal-body">\n\
                <p>Deseja realmente apagar o tronco <b>'+trunk+'</b>?</p></div>\n\
                <div class="modal-footer">\n\
                <button class="btn disabled" data-dismiss="modal" aria-hidden="true">Cancelar</button>\n\
                <a href="DeleteTrunks?trk='+trunk+'" class="btn btn-info">Apagar</a>\n\
                </div></div>');
                $('#myModal').modal('show');
            }
            function selectTech(){
                $('#alert').html('<div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" \n\
                    aria-hidden="true">\n\
                <div class="modal-header">\n\
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>\n\
                <h3 id="myModalLabel">Escolha a Tecnologia do Tronco</h3> </div>\n\
                <div class="modal-body">\n\
                <p><a href="createTrunkSip.jsp"><h4>SIP</h4></a><a href="createTrunkIax.jsp"><h4>IAX2</h4></a>\n\
                        <a href="createTrunkDigital.jsp"><h4>Digital</h4></a><a href="createTrunkVirtual.jsp"><h4>Virtual</h4></a>\n\
                        <a href="createTrunkManual.jsp"><h4>Manual</h4></a></p></div>\n\
                <div class="modal-footer">\n\
                <button class="btn disabled" data-dismiss="modal" aria-hidden="true">Cancelar</button>\n\
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
