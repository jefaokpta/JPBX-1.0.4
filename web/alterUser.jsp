<%-- 
    Document   : alterUser
    Created on : 26/09/2013, 10:49:05
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
            out.print(head.header("JPBX->User->Edit"));
            
            if(session.getAttribute("user")==null){
                response.sendRedirect("logout.jsp?error=Desconectado por inatividade.");
                return;
            }
            JpbxDB db=new JpbxDB();
            String data[]=db.editUser(request.getParameter("usr"));
        %>
    </head>
    <body>
        <%out.print(head.navigator(session.getAttribute("lvl").toString()));%>
    
    <div id="container" class="container table-bordered">
        <form id="frm" name="frm1" action="AlterUser" method="POST">
            <fieldset>
                <legend><h2>Editar Usuário do Sistema</h2></legend>
                <h4>Nome do Usuário</h4>
                <div style="float: left">
                    <input id="user" type="text" name="user" value="<%=data[1] %>" onblur=loadInternal("WebServ?usr="+this.value,'return'); />
                </div>
                <div style="float: left" id="return"></div>
                <div class="clearfix"></div>
                <input type="hidden" name="userid" value="<%=request.getParameter("usr") %>"/>
                <h4>Senha</h4>
                <input type="password" id="pass" placeholder="Senha Criptografada"/>
               <!-- <a rel="tooltip" data-placement="top" title="Revela Senha" >
                    <img onclick="informer('<p>Senha:</br>'+$('#pass').val()+'</p>');" src="css/bootstrap/img/lupa.png"></a>
               -->
               <h4>Empresa</h4>
                <%=db.getCompanyOptions(data[5]) %>
                <h4>Nível do usuário</h4>
                <select name="nivel">
                    <option <%=(data[4].equals("Supervisor")?"selected":"") %>>Supervisor</option>
                    <option <%=(data[4].equals("Administrador")?"selected":"") %>>Administrador</option>
                    <option <%=(data[4].equals("Telefonista")?"selected":"") %>>Telefonista</option>
                </select>
                <h4>Tempo limite para desconexão (minutos)</h4>
                <select name="timeout">
                    <option <%=(data[3].equals("5")?"selected":"") %>>5</option>
                    <option <%=(data[3].equals("10")?"selected":"") %>>10</option>
                    <option <%=(data[3].equals("30")?"selected":"") %>>30</option>
                    <option <%=(data[3].equals("60")?"selected":"") %>>60</option>
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
                else{
                    if($('#pass').val()!=='')
                        $('#passport').val((hashMD5($('#user').val()+':'+$('#pass').val())));
                    $('#frm').submit();
                }
            }
            $(function(){
                $('a').tooltip('hide');
                startCountDown();
            });
        </script>
        <div id="alert"></div>
</html>
