<%-- 
    Document   : moh
    Created on : 08/06/2014, 21:14:45
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
            out.print(head.header("JPBX->Mohs"));
            
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
            out.print("<p>"+session.getAttribute("error") +"</p></div>");
            //out.print("<div class=\"modal-footer\">");
            //out.print("<button class=\"btn\" data-dismiss=\"modal\" aria-hidden=\"true\">Fechar</button>");
            //out.print("<button class=\"btn btn-primary\">Salvar mudanças</button>");
            out.print("</div></div>");
            session.removeAttribute("error");
            }
        %>
    </head>
    <body>
        <%out.print(head.navigator(session.getAttribute("lvl").toString()));%>
    
    <div id="container" class="container table-bordered">
        <h3>Lista de Músicas de Espera</h3>
        <div class="span1">
            <a rel="tooltip" data-placement="right" title="Os arquivos de audio devem estar no formato wav, Mono, 8K, 16bits.">
                <img onclick="uploadMoh();" src="css/bootstrap/img/icon_new.png"></a>
        </div>
            <div class="clearfix"></div>
        <%
            JpbxDB db=new JpbxDB();
            out.print(db.listMohsJsp());
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
        function deleteMoh(mohId,mohName){
            if(mohName==='JPBX')
                $('#alert').html('<div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" \n\
                aria-hidden="true">\n\
                <div class="modal-header">\n\
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>\n\
                <h3 id="myModalLabel">JPBX Informa</h3> </div>\n\
                <div class="modal-body">\n\
                <p class="text-error">A Espera <b>'+mohName+'</b> não pode ser apagada.</p></div>\n\
                <div class="modal-footer">\n\
                <button class="btn disabled" data-dismiss="modal" aria-hidden="true">Cancelar</button>\n\
                </div></div>');
            else
                $('#alert').html('<div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" \n\
                    aria-hidden="true">\n\
                    <div class="modal-header">\n\
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>\n\
                    <h3 id="myModalLabel">JPBX Informa</h3> </div>\n\
                    <div class="modal-body">\n\
                    <p>Deseja realmente apagar a Espera <b>'+mohName+'</b>?</p></div>\n\
                    <div class="modal-footer">\n\
                    <button class="btn disabled" data-dismiss="modal" aria-hidden="true">Cancelar</button>\n\
                    <a href="DeleteMoh?moh='+mohId+'&name='+mohName+'" class="btn btn-info">Apagar</a>\n\
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
