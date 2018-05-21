<%-- 
    Document   : createPeers
    Created on : 05/02/2013, 17:36:35
    Author     : jefaokpta
--%>

<%@page import="com.jpbx.Controller"%>
<%@page import="com.jpbx.JpbxDB"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        
        <%
            Controller head=new Controller();
            out.print(head.header("JPBX->Peers->New"));
            
            if(session.getAttribute("user")==null){
                response.sendRedirect("logout.jsp?error=Desconectado por inatividade.");
                return;
            }
            if(request.getParameter("duplicated")!=null){
            out.print("<script>"+
    	       "$(function(){"+
                    "$('#myModal').modal('show')"+
                "});"+
                "</script>");
            out.print(" <div id=\"alert\">");
            out.print("<div id=\"myModal\" class=\"modal hide fade\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"myModalLabel\" aria-hidden=\"true\">");
            out.print("<div class=\"modal-header\">");
            out.print("<button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-hidden=\"true\">×</button>");
            out.print("<h3 id=\"myModalLabel\">JPBX Informa</h3> </div>");
            out.print("<div class=\"modal-body\">");
            out.print("<p>Ramal "+request.getParameter("duplicated") +" já existe.</p></div>");
            //out.print("<div class=\"modal-footer\">");
            //out.print("<button class=\"btn disabled\" data-dismiss=\"modal\" aria-hidden=\"true\">Cancelar</button>");
           // out.print("<button class=\"btn btn-primary\">Apagar</button>");
           // out.print("<a href=\"DeletePeers?peer="+request.getParameter("delete")+"\" class=\"btn btn-info\">Apagar</a>");
            out.print("</div></div>");
            //session.removeAttribute("return");
            }
            if(request.getParameter("invalid")!=null){
            out.print("<script>"+
    	       "$(function(){"+
                    "$('#myModal').modal('show')"+
                "});"+
                "</script>");
            out.print(" <div id=\"alert\">");
            out.print("<div id=\"myModal\" class=\"modal hide fade\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"myModalLabel\" aria-hidden=\"true\">");
            out.print("<div class=\"modal-header\">");
            out.print("<button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-hidden=\"true\">×</button>");
            out.print("<h3 id=\"myModalLabel\">JPBX Informa</h3> </div>");
            out.print("<div class=\"modal-body\">");
            out.print("<p>Descrição "+request.getParameter("invalid") +" não pode ser usado como ramal.</p></div>");
            //out.print("<div class=\"modal-footer\">");
            //out.print("<button class=\"btn disabled\" data-dismiss=\"modal\" aria-hidden=\"true\">Cancelar</button>");
           // out.print("<button class=\"btn btn-primary\">Apagar</button>");
           // out.print("<a href=\"DeletePeers?peer="+request.getParameter("delete")+"\" class=\"btn btn-info\">Apagar</a>");
            out.print("</div></div>");
            //session.removeAttribute("return");
            }
            JpbxDB db=new JpbxDB();
        %>
    </head>
    <body>
    <%out.print(head.navigator(session.getAttribute("lvl").toString()));%>    
    <div id="container"class="container">
        <form id="frm" name="frm1" action="CreatePeers" method="POST">
            <fieldset>
                <legend><h2>Novo Ramal</h2></legend>
                <div class="span11 table-bordered">
                <div style="float: left"><h4><u>Ramal</u> <a rel="tooltip" data-placement="top" 
                                        title="Range de ramais: 1000 a 9999. Próximo ramal: 
                                        <%= db.peerSuggest() %>">
                        <input id="peer" onblur=loadInternal("WebServ?peer="+this.value,'validation');
                               type="number" name="peer" /></a></h4>
                </div><div style="float: left" id="validation"></div>
                <div style="float: left" class="offset1" ><h4><u>Senha de Registro</u> 
                        <a rel="tooltip" data-placement="top" title="Senha para registrar o dispositivo telefônico">
                            <input id="pass" onblur=loadInternal("WebServ?passwordPeer="+this.value,'passpeer'); 
                                   type="password"/></a></h4>
                    <div id="passpeer"></div>
                    <input type="hidden" id="passport" name="secret"/>
                </div>
                </div>
                <h4><u>Nome do Ramal</u> <input id="name" type="text" name="name" /></h4>
                <div><h4><u>Senha do Ramal</u> <a rel="tooltip" data-placement="right" title="Senha para acessar facilidades do deste ramal">
                            <input id="featpass" onblur=ajustFeatPass(); type="number" name="featsecret" placeholder="4 Digitos" /></a></h4></div>
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
                </label>
                
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
                
                <h4><u>Ramal Externo</u></h4>
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
                
                <h4><u>Ativa Gravação</u></h4>
                <label class="radio inline">
                    <input type="radio" name="record" id="rec1" value="0" checked>
                    Não
                </label>
                <label class="radio inline">
                    <input type="radio" name="record" id="rec2" value="1">
                    Sim
                </label>
                
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
                
                <h4><u>Limite de Chamadas Simultâneas</u> </h4>
                <div>
                <input class="span1" type="text" name="call-limit" placeholder="ilimitado" value="1" />
                <p class="muted">Por segurança limite a uma quantidade baixa</p>
                </div>
                <h4>Empresa</h4>
                <%=db.getCompanyOptions() %>
                <h4><u>Departamento</u> </h4>
                <select name="pickupgroup">
                    <% 
                        out.print(db.listPickupGroups(null));
                    %>
                </select>
                <h4><u>Voicemail</u> </h4>
                <label class="checkbox span2">
                   <h5> <input type="checkbox" id="voicemail">
                    Ativar Voicemail</h5>
                </label>
                <div class="clearfix"></div>
                <div id="mailbox">
                    <h5>Senha de Voicemail <i class="muted">(apenas números)</i></h5>                 
                    <input type="number" name="mailboxSecret" id="mailboxSecret" />
                    <h5>Email de Contato</h5>
                    <input type="email" name="email" id="email" onblur="validEmail()" />
                </div>
                </br>
                <input type="button" onclick="verifyFrm();" value="Salvar" class="btn btn-info" />
                <img id="load" src="css/bootstrap/img/loading.gif"/>
                <a class="btn btn-info offset1" href="listPeers.jsp">Voltar</a>
            </fieldset>
        </form>
        
    </div>
     <script>
         $('#load').hide();
         function verifyFrm(){
             if($('#peer').val()===''){
                 informer('<p style="color: #ff0000">Preencha o campo RAMAL!</p>');
                // $('#peer').focus();
             }
             else if($('#pass').val()===''){
                 informer('<p style="color: #ff0000">Preencha o campo SENHA!</p>');
                 $('#pass').focus();
             }
             else if($('#name').val()===''){
                 informer('<p style="color: #ff0000">Preencha o campo NOME DO RAMAL!</p>');
                 $('#name').focus();
             }
             else{
                 $('#load').show();
                 $('#passport').val(hashMD5($('#peer').val()+':asterisk:'+$('#pass').val()));
                 $('#pass').val('');
                 setTimeout("submit();",500);
             }
        }
        $('#voicemail').click(function(){
            $('#mailbox').toggle('slow');
        });
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
            function ajustFeatPass(){
                if($('#featpass').val().length>4)
                    $('#featpass').val($('#featpass').val().substring(0,4));
            }
            $(function(){
                $('a').tooltip('hide');
                startCountDown();
                $('#pass').val('');
                $('#peer').val('');
                $('#mailbox').hide();
            });
        </script>
        <div id="alert"></div>
    </body>
</html>
