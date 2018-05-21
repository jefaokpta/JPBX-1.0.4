<%-- 
    Document   : createPickupGrp
    Created on : 17/09/2013, 11:01:46
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
            out.print(head.header("JPBX->PickupGrps->New"));
            
            if(session.getAttribute("user")==null){
                response.sendRedirect("logout.jsp?error=Desconectado por inatividade.");
                return;
            }
        %>
    </head>
    <body>
        <%out.print(head.navigator(session.getAttribute("lvl").toString()));%>
    
    <div id="container" class="container table-bordered">
        <form id="frm" name="frm1" action="CreatePickupGrp" method="POST">
            <fieldset>
                <legend><h3>Novo Grupo de Captura</h3></legend>
                <h4>Nome do Grupo</h4>
                <div style="float: left">
                    <input id="grpname" type="text" name="grpname" onblur=loadInternal("WebServ?pickupGrp="+this.value,'return'); />
                </div>
                <div style="float: left" id="return"></div>
                <div class="clearfix"></div>
                <input type="button" onclick="verifyFrm();" value="Salvar" class="btn btn-info" />
                <a class="btn btn-info offset1" href="listPickupGrps.jsp">Voltar</a>
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
                if($('#grpname').val()==='')
                    informer('<p class=\"text-error\">PREENCHA O NOME DO GRUPO</p>');
                else
                    $('#frm').submit();
            }
            $(function(){
                $('a').tooltip('hide');
                startCountDown();
                $('#grpname').focus();
            });
        </script>
        <div id="alert"></div>
    </body>
</html>
