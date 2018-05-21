<%-- 
    Document   : alterPeer
    Created on : 13/02/2013, 16:32:20
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
            out.print(head.header("JPBX->Peers->EditPeer"));
            if(session.getAttribute("user")==null){
                response.sendRedirect("logout.jsp?error=Desconectado por inatividade.");
                return;
            }
            JpbxDB db=new JpbxDB();
        %>
    </head>
    <body>
        <%out.print(head.navigator(session.getAttribute("lvl").toString()));%>
        
    <div id="container" class="container">
        <form id="frm" action="AlterPeer" method="POST">
            <fieldset>
                <legend><h2>Editar Ramal</h2></legend>
        <%
            String data[]=db.editPeer(request.getParameter("peer"));
        %>
        <div class="span11 table-bordered">
            <div style="float: left">
            <h4><u>Ramal</u> <input id="peer" type="text" name="peer" value="<%=data[0] %>" readonly="readonly" />
            </h4></div>
            <div style="float: left" class="offset1">
                <h4><u>Senha de Registro</u> <input id="pass" placeholder="Senha Criptografada" 
                         onblur=loadInternal("WebServ?passwordPeer="+this.value,'passpeer'); 
                         type="password" /></h4>
                                                       
            <div id="passpeer"></div>
            <input type="hidden" id="passport" name="secret"/>
            </div>
            
        </div>
        <%   
            out.print("<h4><u>Nome do Ramal</u> <input id=\"name\" type=\"text\" name=\"name\" value=\""+data[2]+"\" /></h4>");
        %>
                <h4><u>Senha do Ramal</u> <input id="featpass" onblur=ajustFeatPass(); type="number" name="featpass" placeholder="4 Digitos" value="<%=data[10] %>" /></h4>
                <h4><u>Idioma dos Audios</u></h4>
                <label class="radio inline">
                    <% if(data[3].equals("pt_BR")){
                        out.print("<input type=\"radio\" name=\"lang\" id=\"lang1\" value=\"pt_BR\" checked>");
                        } else{out.print("<input type=\"radio\" name=\"lang\" id=\"lang1\" value=\"pt_BR\">");}
                    %>
                    Português
                </label>
                <label class="radio inline">
                    <% if(data[3].equals("en")){
                            out.print("<input type=\"radio\" name=\"lang\" id=\"lang2\" value=\"en\" checked>");
                        } else{out.print("<input type=\"radio\" name=\"lang\" id=\"lang2\" value=\"en\">");}
                    %>
                    Inglês
                </label>  
                <label class="radio inline">
                    <%
                        if(data[3].equals("es")){
                            out.print("<input type=\"radio\" name=\"lang\" id=\"lang3\" value=\"es\" checked>");
                        } else{out.print("<input type=\"radio\" name=\"lang\" id=\"lang3\" value=\"es\">");}
                    %>
                    Espanhol
                </label>
                <label class="radio inline">
                    <%
                        if(data[3].equals("fr")){
                            out.print("<input type=\"radio\" name=\"lang\" id=\"lang4\" value=\"fr\" checked>");
                        } else{out.print("<input type=\"radio\" name=\"lang\" id=\"lang4\" value=\"fr\">");}
                    %>
                    Francês
                </label>
                <h4><u>Tipo de Discagem</u></h4>
                <label class="radio inline">
                    <%
                        if(data[4].equals("rfc2833")){
                            out.print("<input type=\"radio\" name=\"tone\" id=\"tone1\" value=\"rfc2833\" checked>");
                        } else{out.print("<input type=\"radio\" name=\"tone\" id=\"tone1\" value=\"rfc2833\">");}
                    %>
                    rfc2833
                </label>
                <label class="radio inline">
                    <%
                        if(data[4].equals("info")){
                            out.print("<input type=\"radio\" name=\"tone\" id=\"tone2\" value=\"info\" checked>");
                        } else{out.print("<input type=\"radio\" name=\"tone\" id=\"tone2\" value=\"info\">");} 
                    %>
                    Info
                </label>
                <label class="radio inline">
                    <%
                        if(data[4].equals("inband")){
                            out.print("<input type=\"radio\" name=\"tone\" id=\"tone3\" value=\"inband\" checked>");
                        } else{out.print("<input type=\"radio\" name=\"tone\" id=\"tone3\" value=\"inband\">");}
                    %>
                    Inband
                </label>
                <h4><u>Ramal Externo</u></h4>
                <label class="radio inline">
                    <%
                        if(data[5].equals("never")){
                            out.print("<input type=\"radio\" name=\"nat\" id=\"nat1\" value=\"never\" checked>");
                        } else{out.print("<input type=\"radio\" name=\"nat\" id=\"nat1\" value=\"never\">");}
                    %>
                    Não
                </label>
                <label class="radio inline">
                    <%
                        if(data[5].equals("yes")){
                            out.print("<input type=\"radio\" name=\"nat\" id=\"nat2\" value=\"yes\" checked>");
                        } else{out.print("<input type=\"radio\" name=\"nat\" id=\"nat2\" value=\"yes\">");}
                    %>
                    Sim
                </label>
                <h4><u>Ativa Qualidade</u></h4>
                <label class="radio inline">
                    <%
                        if(data[6].equals("no")){
                            out.print("<input type=\"radio\" name=\"qualify\" id=\"q1\" value=\"no\" checked>");
                        } else{out.print("<input type=\"radio\" name=\"qualify\" id=\"q1\" value=\"no\">");}
                    %>
                    Não
                </label>
                <label class="radio inline">
                    <%
                        if(data[6].equals("yes")){
                            out.print("<input type=\"radio\" name=\"qualify\" id=\"q2\" value=\"yes\" checked>");
                        } else{out.print("<input type=\"radio\" name=\"qualify\" id=\"q2\" value=\"yes\">");}
                    %>
                    Sim
                </label>
                <h4><u>Ativa Gravação</u></h4>
                <label class="radio inline">
                    <%
                        if(data[7].equals("0")){
                            out.print("<input type=\"radio\" name=\"record\" id=\"rec1\" value=\"0\" checked>");
                        } else{out.print("<input type=\"radio\" name=\"record\" id=\"rec1\" value=\"0\">");}
                    %>
                    Não
                </label>
                <label class="radio inline">
                    <%
                        if(data[7].equals("1")){
                            out.print("<input type=\"radio\" name=\"record\" id=\"rec2\" value=\"1\" checked>");
                        } else{out.print("<input type=\"radio\" name=\"record\" id=\"rec2\" value=\"1\">");}
                    %>
                    Sim
                </label>
                <h4><u>Ordem de Codecs</u></h4>
                <%String cod1,cod2,cod3,mod;
                mod=data[11].replaceFirst(",", ";");
                cod1=mod.substring(0, mod.indexOf(";"));
                cod2=mod.substring(mod.indexOf(";")+1, mod.indexOf(","));
                cod3=mod.substring(mod.indexOf(",")+1);
                //out.print(cod1+cod2+cod3);
                %>
                <select name="cod1" class="span1">
                    <option <%=(cod1.equals("alaw")?"selected":"") %>>alaw</option>
                    <option <%=(cod1.equals("ulaw")?"selected":"") %>>ulaw</option>
                    <option <%=(cod1.equals("g729")?"selected":"") %>>g729</option>
                    <option <%=(cod1.equals("gsm")?"selected":"") %>>gsm</option>
                    <option <%=(cod1.equals("---")?"selected":"") %>>---</option>
                </select>
                <select name="cod2" class="span1">
                    <option <%=(cod2.equals("alaw")?"selected":"") %>>alaw</option>
                    <option <%=(cod2.equals("ulaw")?"selected":"") %>>ulaw</option>
                    <option <%=(cod2.equals("g729")?"selected":"") %>>g729</option>
                    <option <%=(cod2.equals("gsm")?"selected":"") %>>gsm</option>
                    <option <%=(cod2.equals("---")?"selected":"") %>>---</option>
                </select>
                <select name="cod3" class="span1">
                    <option <%=(cod3.equals("alaw")?"selected":"") %>>alaw</option>
                    <option <%=(cod3.equals("ulaw")?"selected":"") %>>ulaw</option>
                    <option <%=(cod3.equals("g729")?"selected":"") %>>g729</option>
                    <option <%=(cod3.equals("gsm")?"selected":"") %>>gsm</option>
                    <option <%=(cod3.equals("---")?"selected":"") %>>---</option>
                </select>
                <h4><u>Limite de Chamadas Simultâneas</u> </h4>
                <input class="span1" type="text" name="call-limit" placeholder="ilimitado" <% out.print("value="+data[8]); %> >
                <p class="muted">Por segurança limite a uma quantidade baixa</p>
                <h4>Empresa</h4>
                <%=db.getCompanyOptions(data[14]) %>
                <h4><u>Departamento</u> </h4>
                <select name="pickupgroup">
                    <%
                        out.print(db.listPickupGroups(data[9]));
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
                    <input type="number" value="<%=data[12] %>" name="mailboxSecret" id="mailboxSecret" />
                    <h5>Email de Contato</h5>
                    <input type="email" name="email" value="<%=data[13] %>" id="email" onblur="validEmail()" />
                </div>
                </br>
                <input type="button" onclick=verifyFrm(); value="Salvar" class="btn btn-info" />
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
             else if($('#name').val()===''){
                 informer('<p style="color: #ff0000">Preencha o campo NOME DO RAMAL!</p>');
               //  $('#name').focus();
             }
             else{
                 $('#load').show();
                 if($('#pass').val()!==''){
                        $('#passport').val(hashMD5($('#peer').val()+':asterisk:'+$('#pass').val()));
                        $('#pass').val('');
                 }
                 setTimeout("submit();",500);
             }
        }
        $('#voicemail').click(function(){
            $('#mailbox').toggle('slow');
            $('#mailboxSecret').val('');
            $('#email').val('');
        });
        function submit(){
            $('#frm').submit();
        }
            time=<%= session.getMaxInactiveInterval() %>;
            user="<%= session.getAttribute("user") %>";
            function startCountDown(){
                if(time>0){
                    $("#cron").html("Olá "+user+" </br>"+countDown(time));
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
                startCountDown();
                <% 
                    if(data[12].equals(""))
                        out.print("$('#mailbox').hide();");
                    else
                        out.print("$('#voicemail').prop('checked',true);");
                %>
            });
        </script>
        <div id="alert"></div>
    </body>
</html>
