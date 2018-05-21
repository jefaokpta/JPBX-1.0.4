<%-- 
    Document   : configEmail
    Created on : 07/09/2014, 13:48:16
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
            out.print(head.header("JPBX->Configs->Email"));
            
            if(session.getAttribute("user")==null||!session.getAttribute("lvl").equals("Administrador")){
                response.sendRedirect("logout.jsp?error=Desconectado por inatividade.");
                return;
            }
            if(session.getAttribute("error")!=null){
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
            out.print("<p>"+session.getAttribute("error")+"</p>");
            //out.print("<div class=\"modal-footer\">");
            //out.print("<button class=\"btn disabled\" data-dismiss=\"modal\" aria-hidden=\"true\">Cancelar</button>");
           // out.print("<button class=\"btn btn-primary\">Apagar</button>");
           // out.print("<a href=\"DeletePeers?peer="+request.getParameter("delete")+"\" class=\"btn btn-info\">Apagar</a>");
            out.print("</div></div>");
            session.removeAttribute("error");
            }
            JpbxDB db=new JpbxDB();
            String data[]=db.showEmail();
        %>
    </head>
    <body>
        <%out.print(head.navigator(session.getAttribute("lvl").toString()));%>
    
    <div id="container" class="container table-bordered">
        <form id="frm" name="frm1" action="AlterEmail" method="POST">
            <fieldset>
                <legend><h3>Servidor de Envio de Email</h3></legend>
                <h4>Email</h4>
                <div style="float: left">
                    <input id="email" type="text" name="email" value="<%=data[0] %>" onblur="validEmail()"/>
                </div>
                <div style="float: left" id="return"></div>
                <div class="clearfix"></div>
                <h4>Senha</h4>
                <input id="pass" type="password" value="<%=data[1] %>" name="pass"/>
                <div class="clearfix"></div>
                <h4>Servidor SMTP</h4>
                <input id="smtp" type="text" name="smtp" value="<%=data[2] %>" placeholder="smtp.gmail.com"/>
                <div class="clearfix"></div>
                <h4>Porta SMTP</h4>
                <input id="port" type="number" name="port" value="<%=data[3] %>" placeholder="465"/>
                <div class="clearfix"></div>
                <h4>Seguranças</h4> 
                <label class="checkbox span1">
                    <input name="tls" type="checkbox" <%=(data[4].equals("1")?"checked=\"true\"":"") %>>TLS
                </label>
                <label class="checkbox span1">
                    <input name="ssl" type="checkbox" <%=(data[5].equals("1")?"checked=\"true\"":"") %>>SSL
                </label>
                <div class="clearfix"></div>
                <input type="button" onclick="verifyFrm();" value="Salvar" class="btn btn-info" />
            </fieldset>
        </form>
        <div id="alert"></div>
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
                if($('#email').val()===''||$('#pass').val()===''||$('#smtp').val()===''||$('#port').val()==='')    
                    informer('PREENCHA OS CAMPO EM BRANCO');
                else
                    $('#frm').submit();
            }
            $(function(){
                startCountDown();
            });
        </script>
        <div id="alert"></div>
    </body>
</html>
