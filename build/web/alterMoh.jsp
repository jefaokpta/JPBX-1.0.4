<%-- 
    Document   : alterMoh
    Created on : 19/06/2014, 17:14:37
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
            out.print(head.header("JPBX->Mohs->Edit"));
            
            if(session.getAttribute("user")==null){
                response.sendRedirect("logout.jsp?error=Desconectado por inatividade.");
                return;
            }
            JpbxDB db=new JpbxDB();
            String data[]=db.editMoh(request.getParameter("moh"));
        %>
    </head>
    <body>
        <%out.print(head.navigator(session.getAttribute("lvl").toString()));%>
    
    <div id="container" class="container table-bordered">
        <form id="frm" name="frm1" action="AlterMoh" method="POST">
            <fieldset>
                <legend><h3>Editar Música de Espera</h3></legend>
                <input type="hidden" id="idmoh" name="idmoh" value="<%=request.getParameter("moh") %>" />
                <h4>Nome da Música</h4>
                <div style="float: left">
                    <input id="mohname" type="text" name="mohname" value="<%=data[0] %>" readonly="" />
                </div>
                <div style="float: left" id="return"></div>
                <div class="clearfix"></div>
                <h4>Arquivo Recebido</h4>
                <input id="file" type="text" name="file" value="<%=data[1] %>" readonly="" />
                <div class="clearfix"></div>
                <h4>Descrição</h4>
                <textarea id="ta" name="ta" rows="5" cols="30"><%=data[2] %></textarea>
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
