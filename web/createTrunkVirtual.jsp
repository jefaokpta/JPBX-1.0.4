<%-- 
    Document   : createTrunkVirtual
    Created on : 25/02/2013, 17:07:45
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
        out.print(head.header("JPBX->Virtual->New"));
        %>
    </head>
    <body>
        <%=head.navigator(session.getAttribute("lvl").toString())%>
        <div id="container" class="container table-bordered">
          <form id="frm" action="CreateTrunks?chanTech=Virtual" method="POST" name="form1">
            <fieldset>
                <legend><h2>Novo Tronco Virtual</h2></legend>
                
                <h4><u>Nome do Tronco</u></h4><a rel="tooltip" data-placement="right" 
                                                  title="Caracteres ' ' (espaço) e '-' (traço) serão retirados automaticamente pelo sistema">
                    <input id="nameTrunk" type="text" name="trunk_name" 
                           onblur=loadInternal("WebServ?trunk_name="+this.value,'validation'); /></a>
                <div id="validation"></div>
                <!--função ajax que consulta outra pagina p validações escreve a div validation-->
                <h4><u>Senha</u></h4> <input id="pass" type="password" name="secret" />
                <h4><u>Host</u></h4>
                <div>
                    <input style="float: left" onblur="loadInternal('WebServ?testHost='+this.value,'respHost');" id="host" type="text" name="host"/>
                    <div style="float: left" id="respHost"></div>
                </div>
                <div class="clearfix"></div>
                <h4><u>Tipo de Discagem</u></h4>
                <label class="radio inline">
                    <input type="radio" name="tone" id="tone1" value="rfc2833" checked>
                    rfc2833
                </label>
                <label class="radio inline">
                    <input type="radio" name="tone" id="tone2" value="info">
                    Info
                </label>
                <label class="radio inline">
                    <input type="radio" name="tone" id="tone3" value="inband">
                    Inband
                </label>
                <h4><u>Gateway Externo</u></h4>
                <label class="radio inline">
                    <input type="radio" name="nat" id="nat1" value="never" checked>
                    Não
                </label>
                <label class="radio inline">
                    <input type="radio" name="nat" id="nat2" value="yes">
                    Sim
                </label>
                <input type="hidden" name="qualify" id="q1" value="no">
                <input type="hidden" name="lang" id="lang1" value="pt_BR">
              <!--  <h4><u>Idioma dos Audios</u></h4>
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
                </label>  -->
                <h4><u>Ordem de Codecs</u></h4>
                <select name="cod1" class="span1">
                    <option>alaw</option>
                    <option>ulaw</option>
                    <option>g729</option>
                    <option>gsm</option>
                    <option>---</option>
                </select>
                <select name="cod2" class="span1">
                    <option>alaw</option>
                    <option selected="">ulaw</option>
                    <option>g729</option>
                    <option>gsm</option>
                    <option>---</option>
                </select>
                <select name="cod3" class="span1">
                    <option>alaw</option>
                    <option>ulaw</option>
                    <option>g729</option>
                    <option selected="">gsm</option>
                    <option>---</option>
                </select>
                </br></br>
               
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
             else if($('#host').val()===''){
                 informer('<p style="color: #ff0000">Preencha o campo Host</p>');
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
