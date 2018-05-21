<%-- 
    Document   : alterPickupGrp
    Created on : 17/09/2013, 14:02:12
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
            out.print(head.header("JPBX->PickupGrps->Edit"));
            
            if(session.getAttribute("user")==null){
                response.sendRedirect("logout.jsp?error=Desconectado por inatividade.");
                return;
            }
            JpbxDB db=new JpbxDB();
        %>
    </head>
    <body>
        <%out.print(head.navigator(session.getAttribute("lvl").toString()));%>
    
    <div id="container" class="container table-bordered">
        <form id="frm" name="frm1" action="AlterPickupGrp" method="POST">
            <fieldset>
                <legend><h2>Editar Grupo de Captura</h2></legend>
                <h4>Nome do Grupo</h4>
                <div style="float: left">
                    <input id="grpname" type="text" name="grpname" onblur=loadInternal("WebServ?pickupGrp="+this.value,'return'); 
                           value="<%=request.getParameter("grp") %>"/>
                </div>
                <div style="float: left" id="return"></div>
                <input type="hidden" name="pickupid" value="<%=db.getPickupId(request.getParameter("grp")) %>" />
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
