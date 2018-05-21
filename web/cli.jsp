<%-- 
    Document   : cli
    Created on : 12/09/2013, 13:54:17
    Author     : jefaokpta
--%>

<%@page import="java.io.DataInputStream"%>
<%@page import="com.jpbx.Controller"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <%
        Controller head=new Controller();
            out.print(head.header("JPBX->Console"));
            
            if(session.getAttribute("user")==null||!session.getAttribute("lvl").equals("Administrador")){
                response.sendRedirect("logout.jsp?error=Desconectado por inatividade.");
                return;
            }
        %>
    </head>
    <body>
        <%out.print(head.navigator(session.getAttribute("lvl").toString()));%>
    
        <div id="container" class="container table-bordered">
            <h3>Central de Comandos do Asterisk</h3>
            <textarea id="textarea" class="span11" style="margin-left: 5%" rows="10" cols="50" readonly="readonly">
                
                </textarea>
            <div class="offset1">
                <h5 style="float: left">Filtro: </h5>
                <a style="float: left" rel="tooltip" data-placement="right" title="Busca no Console apenas a palavra desejada.">
                    <input type="text" id="filter"/>
                </a>
                <input style="float: left" class="btn-warning offset2" type="button" onclick="reloadCli($('#filter').val());" value="Atualiza Console" />
            </div>
            <div class="clearfix"></div>
            <div class="offset1">               
                <div style="float: left">
                    <h5>CLI > <input type="text" id="comm"  /></h5> 
                </div>
                <div  style="float: left;padding: 1%">
                    <input id="btn" class="btn btn-info" type="button" value="Envia" />
                    <img src="css/bootstrap/img/loading.gif" id="load"/>
                </div>
            </div>
            <div class="clearfix"></div>
            <div id="response" class="table-bordered span10" style="padding-left: 10%"></div>
        </div>
        <script>
            $('#btn').click(function(){
                if($('#comm').val()!=''){
                    $('#load').show();
                    loadInternal('WebServ?cli='+$('#comm').val(),'response');
                }
            });
            $('#load').hide();
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
            function reloadCli(filter){
                if(filter!=='')
                    loadInternal("WebServ?console="+filter,'textarea');
                else
                    loadInternal("WebServ?console=1",'textarea');
            }
            $(function(){
                startCountDown();
                reloadCli('');
                $('a').tooltip();
            });
        </script>
       <%
       if(request.getParameter("listapp")!=null)
                out.print("<script>"
                        + "$('#load').show();"
                        + "loadInternal('WebServ?cli=core show applications','response');"
                        + "</script>");
       %>
    </body>
</html>