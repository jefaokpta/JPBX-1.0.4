<%-- 
    Document   : listPeers
    Created on : 01/02/2013, 17:04:17
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
            out.print(head.header("JPBX->Peers"));
            
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
            out.print("<p>Falha na criação do Ramal.<br>"+session.getAttribute("error")+"</p>");
            //out.print("<div class=\"modal-footer\">");
            //out.print("<button class=\"btn disabled\" data-dismiss=\"modal\" aria-hidden=\"true\">Cancelar</button>");
           // out.print("<button class=\"btn btn-primary\">Apagar</button>");
           // out.print("<a href=\"DeletePeers?peer="+request.getParameter("delete")+"\" class=\"btn btn-info\">Apagar</a>");
            out.print("</div></div>");
            session.removeAttribute("error");  
            }
            String fileTmp=head.peersRegistered();
        %>
    </head>
    <body>
        <%out.print(head.navigator(session.getAttribute("lvl").toString()));%>
    
    <div id="container" class="container table-bordered">
        <h3>Cadastro de Ramais</h3>
        <div class="span1">
            <a rel="tooltip" data-placement="right" title="Criar novo ramal" href="createPeers.jsp"><img src="css/bootstrap/img/icon_new.png"></a>
        </div>
        <form id="frmSearch" name="frm2" action="listPeers.jsp" method="POST">
            <div class="offset5 span5">
                <select class="span2" name="field">
                    <option value="name">Ramal</option>
                    <option value="callerid">Nome</option>
                    <option value="depto">Departamento</option>
                </select>
                <input type="text" placeholder="Digite a Busca" name="seek" id="seek" />
                <button style="float: right" class="btn btn-info" id="btnSearch">Buscar</button>
            </div>
        </form>
            <div class="clearfix"></div>
        <%
            JpbxDB db=new JpbxDB();
            if(request.getParameter("seek")!=null)
                out.print(db.listPeers(request.getParameter("field"),request.getParameter("seek")));
            else
                out.print(db.listPeers());
            request.removeAttribute("seek");
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
            $('#btnSearch').click(function(){
                if($('#seek').val()==='')
                    informer('Falta Dados para Busca.');
                else
                    $('#frmSearch').submit();
            });
            function deletePeer(peer){
                $('#alert').html('<div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" \n\
                    aria-hidden="true">\n\
                    <div class="modal-header">\n\
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>\n\
                    <h3 id="myModalLabel">JPBX Informa</h3> </div>\n\
                    <div class="modal-body">\n\
                    <p>Deseja realmente apagar o ramal <b>'+peer+'</b>?</p></div>\n\
                    <div class="modal-footer">\n\
                    <button class="btn disabled" data-dismiss="modal" aria-hidden="true">Cancelar</button>\n\
                    <a href="DeletePeers?peer='+peer+'" class="btn btn-info">Apagar</a>\n\
                    </div></div>');
            $('#myModal').modal('show');
            }
            function peerDetails(peer){
                $('#alert').html('<div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" \n\
                    aria-hidden="true">\n\
                    <div class="modal-header">\n\
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>\n\
                    <h3 id="myModalLabel">Detalhes do Ramal '+peer+'</h3> </div>\n\
                    <div class="modal-body" id=\"details\">\n\
                    <p>Detalhes aqui</p></div>\n\
                    <div class="modal-footer">\n\
                    <button class="btn disabled" data-dismiss="modal" aria-hidden="true">Fechar</button>\n\
                    </div></div>');
                loadInternal('WebServ?peerDetails='+peer+'',"details");
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
