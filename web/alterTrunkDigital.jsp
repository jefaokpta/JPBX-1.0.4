<%-- 
    Document   : alterTrunkDigital
    Created on : 06/08/2013, 16:23:17
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
        String data[]=db.editTrunkDigital(request.getParameter("trk"));
        out.print(head.header("JPBX->Digital->Edit"));
        %>
    </head>
    <body>
        <%=head.navigator(session.getAttribute("lvl").toString())%>
        <div id="container" class="container table-bordered">
          <form id="frm" action="AlterTrunks?chanTech=Digital" method="POST" name="form1">
            <fieldset>
                <legend><h2>Editar Tronco Digital</h2></legend>
                <h4><u>Placa</u></h4>
                <input type="text" name="board" value="<%=data[2] %>" readonly="" />
                <div class="span11">
                    <div style="float: left">
                        <h4><u>Nº Placa</u></h4>
                        <input id="numBoard" class="span1" type="number" name="numboard" value="<%=data[3] %>" />
                    </div>
                    <div class="offset2" style="float: left">
                        <h4><u>Link</u></h4>
                        <select class="span1" name="link">
                            <option <%=data[4].equals("0")?"selected=\"\"":"" %>>0</option>
                            <option <%=data[4].equals("1")?"selected=\"\"":"" %>>1</option>
                            <option <%=data[4].equals("2")?"selected=\"\"":"" %>>2</option>
                            <option <%=data[4].equals("3")?"selected=\"\"":"" %>>3</option>
                        </select>
                    </div>
                </div>
                <div class="span11">
                    <div style="float: left"><h4><u>Nome do Tronco</u></h4>
                            <input id="nameTrunk" type="text" name="trunk_name" readonly="" value="<%=data[0] %>" /></div>
                </div>               
                <h4><u>Idioma dos Audios</u></h4>
                <label class="radio inline">
                    <input type="radio" name="lang" id="lang1" value="pt_BR" <%=data[1].equals("pt_BR")?"checked":"" %>>
                    Português
                </label>
                <label class="radio inline">
                    <input type="radio" name="lang" id="lang2" value="en" <%=data[1].equals("en")?"checked":"" %>>
                    Inglês
                </label>
                <label class="radio inline">
                    <input type="radio" name="lang" id="lang3" value="es" <%=data[1].equals("es")?"checked":"" %>>
                    Espanhol
                </label>
                <label class="radio inline">
                    <input type="radio" name="lang" id="lang4" value="fr" <%=data[1].equals("fr")?"checked":"" %>>
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
