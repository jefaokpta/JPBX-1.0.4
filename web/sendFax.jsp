<%-- 
    Document   : sendFax
    Created on : 31/10/2014, 16:49:39
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
            out.print(head.header("JPBX->Send Fax"));
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
            out.print("<p>Falha na criação do Ramal.<br>"+session.getAttribute("error")+"</p>");
            //out.print("<div class=\"modal-footer\">");
            //out.print("<button class=\"btn disabled\" data-dismiss=\"modal\" aria-hidden=\"true\">Cancelar</button>");
           // out.print("<button class=\"btn btn-primary\">Apagar</button>");
           // out.print("<a href=\"DeletePeers?peer="+request.getParameter("delete")+"\" class=\"btn btn-info\">Apagar</a>");
            out.print("</div></div>");
            session.removeAttribute("error");  
            }
            JpbxDB db=new JpbxDB();
        %>
    </head>
    <body>
       <%out.print(head.navigator(session.getAttribute("lvl").toString()));%> 
       
       <div id="container" class="container table-bordered">
        <form id="frm" name="frm1" action="SendFax" method="POST">
            <fieldset>
                <legend><h2>Enviar Fax</h2></legend>
                <h4>Número Destino</h4>
                <input type="number" id="dst" />
                <label class="radio inline">
                    <input type="radio" id="trunk" name="mode" checked="" value="trunk">
                    <b>Destino Externo</b>
                </label>
                <label class="radio inline">
                    <input type="radio" id="peer" name="mode" value="peer">
                    <b>Destino Interno </b><i class="muted">(Ramal)</i>
                </label>
                <h4>Número Retorno</h4>
                <a rel="tooltip" data-placement="right" title="Número que o destinatário do FAX pode retornar" href="#">
                    <input type="number" id="stationId" />
                </a>
                <h4>Arquivo</h4>
                <%
                    if(session.getAttribute("lvl").equals("Administrador"))
                        out.print(db.sendFaxOptions()); 
                    else
                        out.print(db.sendFaxOptions(db.getUserCompany(session.getAttribute("user").toString())));
                %>               
                <a id="preview" rel="tooltip" data-placement="right" title="Visualizar Arquivo" href="#"><img src="css/bootstrap/img/printer.png" /></a>
                <div class="clearfix"></div>
                <input type="button" onclick="sendFax($('#dst').val(),$('#file').val(),$('#stationId').val());" value="Enviar" class="btn btn-info" />
            </fieldset>
        </form>
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
            $('#preview').click(function(){
               window.open('asterisk/faxes/'+$('#file').val()+'.pdf','_blank');
            });
            function sendFax(dst,file,stationId){
                if(dst==='')
                    informer('<b class="text-error">PREENCHA O DESTINO!</b>');
                else if(stationId==='')
                    informer('<b class="text-error">PREENCHA O RETORNO!</b>');
                else{
                    $('#alert').html('<div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" \n\
                        aria-hidden="true">\n\
                        <div class="modal-header">\n\
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>\n\
                        <h3 id="myModalLabel">Enviando Fax para '+dst+'.</h3> </div>\n\
                        <div class="modal-body" id=\"details\">\n\
                        <p>Detalhes aqui</p></div>\n\
                        <div class="modal-footer">\n\
                        <button class="btn disabled" data-dismiss="modal" aria-hidden="true">Fechar</button>\n\
                        </div></div>');
                    loadInternal('WebServ?sendFax='+dst+'&file='+file+'&stationId='+stationId+'&mode='+$('input[type=radio]:checked').val(),"details");
                    $('#myModal').modal('show'); 
                    //alert($('input[type=radio]:checked').val());
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
