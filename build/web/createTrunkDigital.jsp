<%-- 
    Document   : createTrunkDigital
    Created on : 25/02/2013, 17:07:16
    Author     : jefaokpta
--%>

<%@page import="com.jpbx.Controller"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <%
        Controller head=new Controller();
        if(session.getAttribute("user")==null){
                response.sendRedirect("logout.jsp?error=Desconectado por inatividade.");
                return;
        }
        out.print(head.header("JPBX->Digital->New"));
        %>
    </head>
    <body>
        <%=head.navigator(session.getAttribute("lvl").toString())%>
        <div id="container" class="container table-bordered">
          <form id="frm" action="CreateTrunks?chanTech=Digital" method="POST" name="form1">
            <fieldset>
                <legend><h2>Novo Tronco Digital</h2></legend>
                <h4><u>Placa</u></h4>
                    <select name="board">
                        <option>Khomp</option>
                    </select>
                <div class="span11">
                    <div style="float: left">
                        <h4><u>Nº Placa</u></h4>
                        <input id="numBoard" class="span1" type="number" name="numboard" placeholder="0" />
                    </div>
                    <div class="offset2" style="float: left">
                        <h4><u>Link</u></h4>
                        <select class="span1" name="link">
                            <option>0</option>
                            <option>1</option>
                            <option>2</option>
                            <option>3</option>
                        </select>
                    </div>
                </div>
                <div class="span11">
                    <div style="float: left"><h4><u>Nome do Tronco</u></h4><a rel="tooltip" data-placement="right" 
                                                      title="Caracteres ' ' (espaço) e '-' (traço) serão retirados automaticamente pelo sistema">
                        <input id="nameTrunk" type="text" name="trunk_name" 
                               onblur=loadInternal("WebServ?trunk_name="+this.value,'validation'); /></a></div>
                    <div id="validation" style="float: left"></div>
                    <!--função ajax que consulta outra pagina p validações escreve a div validation-->
                </div>               
                <h4><u>Idioma dos Audios</u></h4>
                <label class="radio inline">
                    <input type="radio" name="lang" id="lang1" value="pt_BR" checked>
                    Português
                </label>
                <label class="radio inline">
                    <input type="radio" name="lang" id="lang2" value="en">
                    Inglês
                </label>
                <label class="radio inline">
                    <input type="radio" name="lang" id="lang3" value="es">
                    Espanhol
                </label>
                <label class="radio inline">
                    <input type="radio" name="lang" id="lang4" value="fr">
                    Francês
                </label></br></br>
                               
                <input type="button" onclick="verifyFrm();" value="Salvar" class="btn btn-info" />
                <img id="load" src="css/bootstrap/img/loading.gif"/>
                <a class="btn btn-info offset1" href="listTrunks.jsp">Voltar</a>
            </fieldset>
        </form>
            <script>
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
                $(function(){
                $('a').tooltip('hide');
                startCountDown();
            });
            function verifyFrm(){
                if($('#nameTrunk').val()==''){
                     informer('<p style="color: #ff0000">Preencha o campo NOME DO TRONCO</p>');
                     //$('#pass').focus();
                 }
                 else if($('#numBoard').val()==''){
                     informer('<p style="color: #ff0000">Preencha o campo Nº DA PLACA</p>');
                     //$('#nameTrunk').focus();
                 }
                 else{
                    $('#load').show();
                    setTimeout("submit();",500);
                 }
            }
            function submit(){
                $('#frm').submit();
            }
            </script>
            <div id="alert"></div>
    </body>
</html>
