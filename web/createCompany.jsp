<%-- 
    Document   : createCompany
    Created on : 10/10/2014, 16:48:58
    Author     : jefaokpta
--%>

<%@page import="com.jpbx.Controller"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        
        <%
            Controller head=new Controller();
            out.print(head.header("JPBX->Company->New"));
            
            if(session.getAttribute("user")==null){
                response.sendRedirect("logout.jsp?error=Desconectado por inatividade.");
                return;
            }
        %>
    </head>
    <body>
        <%out.print(head.navigator(session.getAttribute("lvl").toString()));%>
    
    <div id="container" class="container table-bordered">
        <form id="frm" name="frm1" action="CreateCompany" method="POST">
            <fieldset>
                <legend><h2>Nova Empresa</h2></legend>
                <h4>Nome da Empresa</h4>
                <div style="float: left">
                    <input id="name" type="text" name="name" onblur=loadInternal("WebServ?company="+this.value,'return'); />
                </div>
                <div style="float: left" id="return"></div>
                <div class="clearfix"></div>
                <h4>Observações</h4>
                <textarea name="obs"></textarea>
                <div class="clearfix"></div>
                <input type="button" onclick="verifyFrm();" value="Salvar" class="btn btn-info" />
                <a class="btn btn-info offset1" href="listCompanys.jsp">Voltar</a>
                <input type="hidden" id="passport" name="pass"/>
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
                if($('#name').val()==='')
                    informer('<p class=\"text-error\">PREENCHA O NOME DA EMPRESA</p>');
                else{
                    $('#frm').submit();
                }
            }
            $(function(){
                $('a').tooltip('hide');
                startCountDown();
                $('#user').focus();
            });
        </script>
        <div id="alert"></div>
</html>
