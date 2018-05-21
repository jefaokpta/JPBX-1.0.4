<%-- 
    Document   : alterTrunkManual
    Created on : 08/10/2014, 18:04:13
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
        if(session.getAttribute("user")==null){
                response.sendRedirect("logout.jsp?error=Desconectado por inatividade.");
                return;
        }
        out.print(head.header("JPBX->Manual->Edit"));
        %>
    </head>
    <body>
        <%=head.navigator(session.getAttribute("lvl").toString())%>
        <div id="container" class="container table-bordered">
          <form id="frm" action="AlterTrunks?chanTech=Manual" method="POST" name="form1">
            <fieldset>
                <legend><h2>Editar Tronco Manual</h2></legend>
                
                <h4><u>Nome do Tronco</u></h4><a rel="tooltip" data-placement="right" 
                                                  title="Caracteres ' ' (espaço) e '-' (traço) serão retirados automaticamente pelo sistema">
                    <input style="float: left" id="nameTrunk" type="text" name="trunk_name" 
                           value="<%=request.getParameter("trk") %>" readonly="" /></a>
                <div style="float: left" id="validation"></div>
                <!--função ajax que consulta outra pagina p validações escreve a div validation-->
                <div class="clearfix"></div>
                <h4><u>Decrição do Canal</u></h4>
                <input type="text" value="<%=db.editTrunkManual(request.getParameter("trk")) %>" placeholder="Technology/Username" name="channel" id="channel" />
                <br>
               
                <input type="button" onclick="verifyFrm();" value="Salvar" class="btn btn-info" />
                <img id="load" src="css/bootstrap/img/loading.gif"/>
                <a class="btn btn-info offset1" href="listTrunks.jsp">Voltar</a>
            </fieldset>
        </form>
        
    </div>
        <script>
         $('#load').hide();
         function verifyFrm(){
             if($('#nameTrunk').val()===''){
                 informer('<p style="color: #ff0000">Preencha o campo Nome</p>');
                 //$('#nameTrunk').focus();
             }
             else if($('#channel').val()===''){
                 informer('<p style="color: #ff0000">Preencha o campo Descrição do Canal</p>');
                 //$('#host').focus();
             }else{
                 $('#load').show();
                 setTimeout("submit();",500);
             }
        }
        function submit(){
            $('#frm').submit();
        }
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
            $(function(){
                $('a').tooltip('hide');
                startCountDown();
            });
        </script>
        <div id="alert"></div>
    </body>
</html>
