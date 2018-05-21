<%-- 
    Document   : createUser
    Created on : 26/09/2013, 10:05:27
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
            JpbxDB db=new JpbxDB();
            out.print(head.header("JPBX->User->New"));
            
            if(session.getAttribute("user")==null){
                response.sendRedirect("logout.jsp?error=Desconectado por inatividade.");
                return;
            }
        %>
    </head>
    <body>
        <%out.print(head.navigator(session.getAttribute("lvl").toString()));%>
    
    <div id="container" class="container table-bordered">
        <form id="frm" name="frm1" action="CreateUser" method="POST">
            <fieldset>
                <legend><h2>Novo Usuário do Sistema</h2></legend>
                <h4>Nome do Usuário</h4>
                <div style="float: left">
                    <input id="user" type="text" name="user" onblur=loadInternal("WebServ?usr="+this.value,'return'); />
                </div>
                <div style="float: left" id="return"></div>
                <div class="clearfix"></div>
                <h4>Senha</h4>
                <input type="password" id="pass"/>
                <h4>Empresa</h4>
                <%=db.getCompanyOptions() %>
                <h4>Nível do usuário</h4>
                <select name="nivel">
                    <option>Supervisor</option>
                    <option>Administrador</option>
                    <option>Telefonista</option>
                </select>
                <h4>Tempo limite para desconexão (minutos)</h4>
                <select name="timeout">
                    <option>5</option>
                    <option>10</option>
                    <option>30</option>
                    <option>60</option>
                </select>
                <div class="clearfix"></div>
                <input type="button" onclick="verifyFrm();" value="Salvar" class="btn btn-info" />
                <a class="btn btn-info offset1" href="listUsers.jsp">Voltar</a>
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
                if($('#user').val()==='')
                    informer('<p class=\"text-error\">PREENCHA O NOME DO USUÁRIO</p>');
                else if($('#pass').val()==='')
                    informer('<p class=\"text-error\">PREENCHA A SENHA DO USUÁRIO</p>');
                else{
                    $('#passport').val(hashMD5($('#user').val()+':'+$('#pass').val()));
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
