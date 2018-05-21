<%-- 
    Document   : alterTrunkIax
    Created on : 25/06/2013, 14:43:33
    Author     : jefaokpta
--%>

<%@page import="com.jpbx.JpbxDB"%>
<%@page import="com.jpbx.Controller"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <%  Controller head=new Controller();
            out.print(head.header("JPBX->IAX2->Edit"));
            
            if(session.getAttribute("user")==null){
                response.sendRedirect("logout.jsp?error=Desconectado por inatividade.");
                return;
            }         
        %>
    </head>
    <body>
      <%out.print(head.navigator(session.getAttribute("lvl").toString()));
        JpbxDB db=new JpbxDB();
        String data[]=db.editTrunks(request.getParameter("trk"));
      %>
      <div id="container"class="container table-bordered">
          <form id="frm" action="AlterTrunks?chanTech=IAX2" method="POST" name="form1">
            <fieldset>
                <legend><h2>Alterar Tronco IAX2</h2></legend>
                
                <h4><u>Nome do Tronco</u></h4>
                <input id="nameTrunk" type="text" name="trunk_name" readonly="" value="<%=data[0]%>" />
                <h4><u>Senha</u></h4> <input id="pass" type="password" name="secret" value="<%=data[1]%>" />
                <a rel="tooltip" data-placement="top" title="Revela Senha" ><img onclick="informer('<p>Senha:</br>'+$('#pass').val()+'</p>');" src="css/bootstrap/img/lupa.png"></a>
                <h4><u>Host</u></h4>
                <div>
                    <input style="float: left" id="host" onblur="loadInternal('WebServ?testHost='+this.value,'respHost');" value="<%=data[2]%>" type="text" name="host"/>
                    <div style="float: left" id="respHost"></div>
                </div>
                <div class="clearfix"></div>
                <h4><u>Tipo de Discagem</u></h4>
                <label class="radio inline">
                    <input type="radio" name="tone" id="tone1" value="rfc2833" <%
                    if(data[3].equals("rfc2833")) out.print("checked"); %>>
                    rfc2833
                </label>
                <label class="radio inline">
                    <input type="radio" name="tone" id="tone2" value="info" <%
                    if(data[3].equals("info")) out.print("checked"); %>>
                    Info
                </label>
                <label class="radio inline">
                    <input type="radio" name="tone" id="tone3" value="inband" <%
                    if(data[3].equals("inband")) out.print("checked"); %>>
                    Inband
                </label>
                
                <h4><u>Ativa NAT</u></h4>
                <label class="radio inline">
                    <input type="radio" name="nat" id="nat1" value="never" <%
                    if(data[4].equals("never")) out.print("checked"); %>>
                    Não
                </label>
                <label class="radio inline">
                    <input type="radio" name="nat" id="nat2" value="yes" <%
                    if(data[4].equals("yes")) out.print("checked"); %>>
                    Sim
                </label>
                
                <h4><u>Ativa Qualidade</u></h4>
                <label class="radio inline">
                    <input type="radio" name="qualify" id="q1" value="no" <%
                    if(data[5].equals("no")) out.print("checked"); %>>
                    Não
                </label>
                <label class="radio inline">
                    <input type="radio" name="qualify" id="q2" value="yes" <%
                    if(data[5].equals("yes")) out.print("checked"); %>>
                    Sim
                </label>
                <input type="hidden" name="lang" id="lang1" value="pt_BR">
             <!--   <h4><u>Idioma dos Audios</u></h4>
                <label class="radio inline">
                    <input type="radio" name="lang" id="lang1" value="pt_BR" <%
                    //if(data[6].equals("pt_BR")) out.print("checked"); %>>
                    Português
                </label>
                <label class="radio inline">
                    <input type="radio" name="lang" id="lang2" value="en" <%
                    //if(data[6].equals("en")) out.print("checked"); %>>
                    Inglês
                </label>
                <label class="radio inline">
                    <input type="radio" name="lang" id="lang3" value="es" <%
                    //if(data[6].equals("es")) out.print("checked"); %>>
                    Espanhol
                </label>
                <label class="radio inline">
                    <input type="radio" name="lang" id="lang4" value="fr" <%
                    //if(data[6].equals("es")) out.print("checked"); %>>
                    Francês
                </label> -->
                <h4><u>Ordem de Codecs</u></h4>
                <%String cod1,cod2,cod3,mod;
                mod=data[7].replaceFirst(",", ";");
                cod1=mod.substring(0, mod.indexOf(";"));
                cod2=mod.substring(mod.indexOf(";")+1, mod.indexOf(","));
                cod3=mod.substring(mod.indexOf(",")+1);
                //out.print(cod1+cod2+cod3);
                %>
                <select name="cod1" class="span1">
                    <option <%if(cod1.equals("alaw")) out.print("selected=\"\""); %>>alaw</option>
                    <option <%if(cod1.equals("ulaw")) out.print("selected=\"\""); %>>ulaw</option>
                    <option <%if(cod1.equals("g729")) out.print("selected=\"\""); %>>g729</option>
                    <option <%if(cod1.equals("gsm")) out.print("selected=\"\""); %>>gsm</option>
                    <option <%if(cod1.equals("---")) out.print("selected=\"\""); %>>---</option>
                </select>
                <select name="cod2" class="span1">
                    <option <%if(cod2.equals("alaw")) out.print("selected=\"\""); %>>alaw</option>
                    <option <%if(cod2.equals("ulaw")) out.print("selected=\"\""); %>>ulaw</option>
                    <option <%if(cod2.equals("g729")) out.print("selected=\"\""); %>>g729</option>
                    <option <%if(cod2.equals("gsm")) out.print("selected=\"\""); %>>gsm</option>
                    <option <%if(cod2.equals("---")) out.print("selected=\"\""); %>>---</option>
                </select>
                <select name="cod3" class="span1">
                    <option <%if(cod3.equals("alaw")) out.print("selected=\"\""); %>>alaw</option>
                    <option <%if(cod3.equals("ulaw")) out.print("selected=\"\""); %>>ulaw</option>
                    <option <%if(cod3.equals("g729")) out.print("selected=\"\""); %>>g729</option>
                    <option <%if(cod3.equals("gsm")) out.print("selected=\"\""); %>>gsm</option>
                    <option <%if(cod3.equals("---")) out.print("selected=\"\""); %>>---</option>
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
         function displayPass(){
             informer('<p>Senha Atual: \"'+$('#pass').val()+'\"</p>');
         }
         function verifyFrm(){
             if($('#pass').val()==''){
                 informer('<p style="color: #ff0000">Preencha o campo Senha</p>');
                 //$('#pass').focus();
             }
             else if($('#nameTrunk').val()==''){
                 informer('<p style="color: #ff0000">Preencha o campo Nome</p>');
                 //$('#nameTrunk').focus();
             }
             else if($('#host').val()==''){
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
