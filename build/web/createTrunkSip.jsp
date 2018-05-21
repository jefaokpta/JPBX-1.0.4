<%-- 
    Document   : createTrunkSip
    Created on : 25/02/2013, 17:06:21
    Author     : jefaokpta
--%>

<%@page import="com.jpbx.Controller"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        
        <%
            Controller head=new Controller();
            out.print(head.header("JPBX->Trunks->SIP"));
            if(session.getAttribute("user")==null){
            response.sendRedirect("logout.jsp?error=Desconectado por inatividade.");
            return;
            }
            if(request.getParameter("selectTech")!=null){
            out.print("<script>"+
    	       "$(function(){"+
                    "$('#myModal').modal('show')"+
                "});"+
                "</script>");
            out.print(" <div id=\"alert\">");
            out.print("<div id=\"myModal\" class=\"modal hide fade\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"myModalLabel\" aria-hidden=\"true\">");
            out.print("<div class=\"modal-header\">");
            out.print("<button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-hidden=\"true\">×</button>");
            out.print("<h3 id=\"myModalLabel\">Selecione a Tecnologia</h3> </div>");
            out.print("<div class=\"modal-body\">");
            out.print("<p><a href=\"#\"><h4>SIP</h4></a><a href=\"#\"><h4>IAX</h4></a>"
                        + "<a href=\"#\"><h4>KHOMP</h4></a><a href=\"#\"><h4>Virtual</h4></a></p></div>");
            out.print("<div class=\"modal-footer\">");
            out.print("<button class=\"btn disabled\" data-dismiss=\"modal\" aria-hidden=\"true\">Cancelar</button>");
           // out.print("<button class=\"btn btn-primary\">Apagar</button>");
           // out.print("<a href=\"DeletePeers?peer="+request.getParameter("delete")+"\" class=\"btn btn-info\">Apagar</a>");
            out.print("</div></div></div>");
            request.removeAttribute("selectTech");
        }
        %>
    </head>
    <body>
        <%out.print(head.navigator(session.getAttribute("lvl").toString()));%>
        <div id="container" class="container table-bordered">
        <form id="frm" name="form1" action="CreateTrunks?chanTech=SIP" method="POST">
            <fieldset>
                <legend><h2>Novo Tronco SIP</h2></legend>
                <div>
                    <div style="float: left"><h4 style="float: left"><u>Nome do Tronco </u></h4> <a style="float: left" href="#" rel="tooltip" 
                                                  data-placement="right" title="Evite caracteres especiais como espaço ou hífen.">
                        <input id="nameTrunk" type="text" name="trunk_name" 
                               onblur=loadInternal("WebServ?trunk_name="+this.value,'validation'); /></a>
                    </div>
                    <div style="float: left" id="validation"></div>  
                </div>
                <div class="clearfix"></div>
                <div>
                <h4 style="float: left"><u>Usuário &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    </u> </h4><a style="float: left" href="#" rel="tooltip" data-placement="right" title="Username (fornecido pela operadora)">
                        <input id="username" type="text" name="username" /></a>
                </div>
                <div class="clearfix"></div>
                <div>
                <h4 style="float: left"><u>Senha &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    </u> </h4><input id="pass" type="password" name="secret" />
                </div>
                <div>
                        <h4 style="float: left"><u>Servidor Remoto </u></h4> <a href="#info" rel="tooltip" 
                                                   data-placement="right" title="Host">
                        <input style="float: left" id="host" type="text" onblur="loadInternal('WebServ?testHost='+this.value,'respHost');"
                               name="host"/></a><div style="float: left" id="respHost"></div>
                    </div>
                    <div class="clearfix"></div>
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
                <input type="hidden" name="lang" id="lang1" value="pt_BR">
   
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
                
                <h4><u>Ativa NAT</u></h4>
                <label class="radio inline">
                    <input type="radio" name="nat" id="nat1" value="never" checked>
                    Não
                </label>
                <label class="radio inline">
                    <input type="radio" name="nat" id="nat2" value="yes">
                    Sim
                </label>
                
                <h4><u>Ativa Qualidade</u></h4>
                <label class="radio inline">
                    <input type="radio" name="qualify" id="q1" value="no" checked>
                    Não
                </label>
                <label class="radio inline">
                    <input type="radio" name="qualify" id="q2" value="yes">
                    Sim
                </label>
                </br></br>
                <input class="btn btn-warning" type="button" id="disp" value="Dados Avançados"  />
                <div id="bla">
                    <div style="float: left">
                        <h5>Reinvite:</h5>
                        <select name="reinvite" class="span1">
                            <option></option>
                            <option>yes</option>
                            <option>no</option>
                        </select>
                    </div>
                    <div style="float: left;padding-left: 1%">
                        <h5>Canreinvite:</h5>
                        <select name="canreinvite" class="span1">
                            <option></option>
                            <option>yes</option>
                            <option>no</option>
                        </select>
                    </div>
                    <div style="float: left;padding-left: 1%">
                        <h5>Limite de ligações:</h5>
                        <a rel="tooltip" data-placement="right" title="Você pode limitar a quantidade de ligações simultâneas neste tronco.">
                            <input class="span1" type="number" name="limit" placeholder="Ilimitado" /></a>
                    </div>
                    <div class="clearfix"></div>
                    <h5>Marcando abaixo o sistema tentará registrar esta conta para receber ligações.</h5>
                    <input style="float: left" id="reg" name="reg" type="checkbox" >
                    <p style="float: left"> Registrar </p>

                    <a rel="tooltip" data-placement="right" 
                       title="Identificação caso a operadora não envie. (Pode ficar em branco)">
                        <input class="offset2" id="exten" type="text" name="exten" placeholder="EXTEN"/>
                    </a>
                </div><div id="register"></div>
                <div class="clearfix"></div>
                </br>
                <input type="button" onclick="verifyFrm();" value="Salvar" class="btn btn-info" />
                <img id="load" src="css/bootstrap/img/loading.gif"/>
                <a class="btn btn-info offset1" href="listTrunks.jsp">Voltar</a>
            </fieldset>
        </form>
        
    </div>
    <script>
        $('#bla').hide();
        $('#load').hide();       
        $('#reg').attr('checked',false);
        $('#disp').click(function(){
            $('#bla').toggle('slow');
            $('#reg').attr('checked',false);
            $('#exten').hide();
        });
        $('#reg').click(function(){
            $('#exten').toggle('slow');
            $('#exten').val(null);
        });
        function verifyFrm(){
            if($('#pass').val()==''){
                 informer('<p style="color: #ff0000">Preencha o campo SENHA</p>');
                // $('#pass').focus();
             }
             else if($('#nameTrunk').val()==''){
                 informer('<p style="color: #ff0000">Preencha o campo NOME DO TRONCO</p>');
                 //$('#nameTrunk').focus();
             }
             else if($('#username').val()==''){
                 informer('<p style="color: #ff0000">Preencha o campo USUÁRIO</p>');
                 //$('#nameTrunk').focus();
             }
             else if($('#host').val()==''){
                 informer('<p style="color: #ff0000">Preencha o campo HOST</p>');
                 //$('#host').focus();
             }else{
                 if($('#reg').is(':checked')){
                     $('#register').html("<input type=\"hidden\" name=\"reception\" value=\"yes\" />\n\
                            <input type=\"hidden\" name=\"register\" value=\""+
                             $('#username').val()+":"+$('#pass').val()+"@"+$('#host').val() +"\"/>");
                 }
                 else
                     $('#register').html("<input type=\"hidden\" name=\"reception\" value=\"no\" />");
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
                $('#nameTrunk').text('');
                $('#pass').text('');
            });
        </script>
        <div id="alert"></div>
    </body>
</html>
