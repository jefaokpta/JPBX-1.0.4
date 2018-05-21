<%-- 
    Document   : createMoh
    Created on : 08/06/2014, 22:14:21
    Author     : jefaokpta
--%>

<%@page import="com.jpbx.Controller"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        
        <%
            Controller head=new Controller();
            out.print(head.header("JPBX->Mohs->New"));
            
            if(session.getAttribute("user")==null){
                response.sendRedirect("logout.jsp?error=Desconectado por inatividade.");
                return;
            }
        %>
    </head>
    <body>
        <%out.print(head.navigator(session.getAttribute("lvl").toString()));%>
    
    <div id="container" class="container table-bordered">
        <form id="frm" name="frm1" action="CreateMoh" method="POST">
            <fieldset>
                <legend><h3>Nova Música de Espera</h3></legend>
                <h4>Nome da Música</h4>
                <div style="float: left">
                    <input id="mohname" type="text" name="mohname" onblur=loadInternal("WebServ?mohName="+this.value,'return'); />
                </div>
                <div style="float: left" id="return"></div>
                <div class="clearfix"></div>
                <h4>Arquivo Recebido</h4>
                <input id="file" type="text" name="file" value="<%=session.getAttribute("file") %>" readonly="" />
                <div class="clearfix"></div>
                <h4>Descrição</h4>
                <textarea id="ta" name="ta" rows="5" cols="30"></textarea>
                <div class="clearfix"></div>
                <input type="button" onclick="verifyFrm();" value="Salvar" class="btn btn-info" />
                <a class="btn btn-info offset1" href="moh.jsp">Voltar</a>
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
                if($('#mohname').val()==='')
                    informer('<p class=\"text-error\">PREENCHA O NOME DA MÚSICA</p>');
                else
                    $('#frm').submit();
            }
            $(function(){
                $('a').tooltip('hide');
                startCountDown();
            });
        </script>
        <div id="alert"></div>
    </body>
</html>
