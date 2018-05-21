<%-- 
    Document   : alterTrunkSip
    Created on : 27/06/2013, 17:58:44
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
            out.print(head.header("JPBX->Trunks->New"));
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
         <div id="container" class="container table-bordered">
        <form id="frm" name="form1" action="AlterTrunks?chanTech=SIP" method="POST">
            <fieldset>
                <legend><h2>Alterar Tronco SIP</h2></legend>
                <h4><u>Nome do Tronco </u>
                    <input id="nameTrunk" type="text" name="trunk_name" readonly="" value="<%=data[0]%>"</h4>
                    <h4><u>Usuário&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</u>
                    <a href="#" rel="tooltip" data-placement="right" title="Username (fornecido pela operadora)">
                        <input id="username" type="text" name="username" value="<%=data[11]%>"></a></h4>
                <h4><u>Senha &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    </u> <input id="pass" type="password" name="secret" value="<%=data[1] %>" />
                    <a rel="tooltip" data-placement="top" title="Revela Senha" ><img onclick="informer('<p class=\'text-info\'>Senha:</br>'+$('#pass').val()+'</p>');" src="css/bootstrap/img/lupa.png"></a></h4>
                    <div>
                        <h4 style="float: left"><u>Servidor Remoto </u></h4> <a href="#info" rel="tooltip" 
                                                   data-placement="right" title="Host">
                        <input style="float: left" id="host" type="text" onblur="loadInternal('WebServ?testHost='+this.value,'respHost');"
                               name="host" value="<%=data[2] %>"/></a><div style="float: left" id="respHost"></div>
                    </div>
                    <div class="clearfix"></div>
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
                <input type="hidden" name="lang" id="lang1" value="pt_BR">
     
                
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

                
                </br></br>
                <input type="button" id="disp" value="Dados Avançados" class="btn btn-warning" />
                <div id="bla">
                    <div style="float: left">
                        <h5>Reinvite:</h5>
                        <select name="reinvite" class="span1">
                            <option <%=(data[12].equals("")?"selected":"") %>></option>
                            <option <%=(data[12].equals("yes")?"selected":"") %>>yes</option>
                            <option <%=(data[12].equals("no")?"selected":"") %>>no</option>
                        </select>
                    </div>
                    <div style="float: left;padding-left: 1%">
                        <h5>Canreinvite:</h5>
                        <select name="canreinvite" class="span1">
                            <option <%=(data[13].equals("")?"selected":"") %>></option>
                            <option <%=(data[13].equals("yes")?"selected":"") %>>yes</option>
                            <option <%=(data[13].equals("no")?"selected":"") %>>no</option>
                        </select>
                    </div>
                    <div style="float: left;padding-left: 1%">
                        <h5>Limite de ligações:</h5>
                        <a rel="tooltip" data-placement="right" title="Você pode limitar a quantidade de ligações simultâneas neste tronco.">
                            <input class="span1" type="number" name="limit" value="<%=(data[14].equals("0")?"":data[14]) %>" placeholder="Ilimitado" /></a>
                    </div>
                    <div class="clearfix"></div>
                    <h5>Marcando abaixo o sistema tentará registrar esta conta para receber ligações.</h5>
                    <input id="reg" name="reg" type="checkbox"
                           <%=data[8].equals("yes")?"checked=\"true\"":"" %>>

                    <a rel="tooltip" data-placement="right" 
                       title="Identificação caso a operadora não envie. (Pode ficar em branco)">
                        <input class="offset2" id="exten" type="text" name="exten" placeholder="EXTEN"
                               value="<%=data[10] %>"/>
                    </a>
                </div><div id="register"></div>
                </br>
                <input type="button" onclick="verifyFrm();" value="Salvar" class="btn btn-info" />
                <img id="load" src="css/bootstrap/img/loading.gif"/>
                <a class="btn btn-info offset1" href="listTrunks.jsp">Voltar</a>
            </fieldset>
        </form>
    </div>
    <script>
        $('#load').hide();
        if(!$('#reg').is(':checked'))
            $('#bla').hide();    
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
                 //$('#pass').focus();
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
            });
        </script>
        <div id="alert"></div>
    </body>
</html>
