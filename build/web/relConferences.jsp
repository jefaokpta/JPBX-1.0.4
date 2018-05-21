<%-- 
    Document   : securityLogin
    Created on : 01/02/2013, 16:12:13
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
            out.print(head.header("JPBX->Conferences"));
            
            if(session.getAttribute("user")==null){
                response.sendRedirect("logout.jsp?error=Desconectado por inatividade.");
                return;
            }
        %>
        <link rel="stylesheet" href="css/jquery-ui-1.10.3/themes/base/jquery-ui.css"/>
        <script src="css/jquery-ui-1.10.3/ui/jquery-ui.js"></script>
        <script src="css/jquery-ui-1.10.3/ui/ui.datepicker-pt_BR.js"></script>
    </head>
    <body>
        <%out.print(head.navigator(session.getAttribute("lvl").toString()));%>
    
    <div id="container" class="container table-bordered">
        <form id="frm" name="frm1" action="RelConferencesShow" method="POST">
            <fieldset>
                <legend><h3><u>Relatório de Conferências</u></h3></legend>
                <h4><u>Periodo: </u></h4>
                    <h5 style="float: left">Data de Início: </h5>
                    <input style="float: left" id="dateini" type="text" name="dateini"/>
                    <h5 class="offset1" style="float: left">Até: </h5>
                    <input id="dateend" type="text" name="dateend"/>
                <div class="clearfix"></div>
                
                <h4><u>Filtros </u></h4>
                <div id="filters" class="table-bordered span10">
                    <h5 style="float: left">Sala(s): </h5>
                    <input id="src" style="float: left" placeholder="901,902,903" type="text" name="rooms" 
                           rel="tooltip" data-placement="right" title="Permite várias salas separadas por ',' (vírgula)."/>
                    
                    </label>              
                </div>
                <div class="clearfix"></div>
                </br>
                <input type="button" onclick="verifyFrm();" value="Exibir" class="btn btn-info" />
                <img id="load" src="css/bootstrap/img/loading.gif"/>
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
            function verifyFrm(){
                $('#load').show();
                setTimeout('doPost();',1000);
            }
            function doPost(){
                $('#frm').submit();
                $('#load').hide();
            }
            $(function(){        
                day=new Date();
                if((day.getDate())<10)
                    d='0'+(day.getDate());
                else
                    d=(day.getDate());
                if((day.getMonth()+1)<10)
                    m='0'+(day.getMonth()+1);
                else
                    m=(day.getMonth()+1);
                $('#dateend').val(d+'/'+m+'/'+day.getFullYear());
                $('#dateini').val('01/'+m+'/'+day.getFullYear());
                startCountDown();
                $( "#dateini" ).datepicker();
                $( "#dateend" ).datepicker();
                $('#method').val('');
                $('#load').hide();
            });
        </script>
        <div id="alert"></div>
    </body>
</html>
