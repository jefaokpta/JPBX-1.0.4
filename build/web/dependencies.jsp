<%-- 
    Document   : dependencies
    Created on : 29/10/2014, 12:49:19
    Author     : jefaokpta
--%>

<%@page import="com.jpbx.Controller"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        
        <%
            Controller head=new Controller();
            out.print(head.header("JPBX->Dependencies"));
            if(session.getAttribute("user")==null||!session.getAttribute("lvl").equals("Administrador")){
            response.sendRedirect("logout.jsp?error=Desconectado por inatividade.");
            return;
            }
        
        %>
    </head>
    <body>
       <%out.print(head.navigator(session.getAttribute("lvl").toString()));%> 
       
       <div id="container" class="container table-bordered">
        <h3>Lista de Dependências do Sistema</h3>
        <table class="span11 table-striped table-condensed table-hover table-bordered">
            <tr><th>Programa</th><th>Status</th><th>Descrição</th></tr>          
        <%
            out.print("<tr><td><b>JAVA</b></td><td>"+(head.dependencies("/java -h").equals("0")?""
                    + "<b class=\"text-success\">Instalado<b>":"<b class=\"text-error\">Não instalado</b>")+
                    "</td><td class=\"text-info\">Executa scripts externos ao sistema.</td></tr>");
            out.print("<tr><td><b>LINK JPBX</b></td><td>"+(head.depCommand("ls /scriptsJPBX").equals("com")?""
                    + "<b class=\"text-success\">Instalado<b>":"<b class=\"text-error\">Não instalado</b>")+
                    "</td><td class=\"text-info\">Caminho para repositório de scripts JPBX.</td></tr>");
            out.print("<tr><td><b>RAR</b></td><td>"+(head.dependencies("rar --help").equals("0")?""
                    + "<b class=\"text-success\">Instalado<b>":"<b class=\"text-error\">Não instalado</b>")+
                    "</td><td class=\"text-info\">Comprime dados de Relatórios e Backups.</td></tr>");
            out.print("<tr><td><b>LAME</b></td><td>"+(head.dependencies("lame --help").equals("0")?""
                    + "<b class=\"text-success\">Instalado<b>":"<b class=\"text-error\">Não instalado</b>")+
                    "</td><td class=\"text-info\">Converte formatos de Audios.</td></tr>");
            out.print("<tr><td><b>CONVERT</b></td><td>"+(head.dependencies("convert -h").equals("0")?""
                    + "<b class=\"text-success\">Instalado<b>":"<b class=\"text-error\">Não instalado</b>")+
                    "</td><td class=\"text-info\">Converte formatos de Imagens (ImageMagick).</td></tr>");
            out.print("<tr><td><b>CURL</b></td><td>"+(head.dependencies("curl -h").equals("0")?""
                    + "<b class=\"text-success\">Instalado<b>":"<b class=\"text-error\">Não instalado</b>")+
                    "</td><td class=\"text-info\">Navegador WEB via Terminal.</td></tr>");
            out.print("<tr><td><b>GHOSTSCRIPT</b></td><td>"+(head.dependencies("ghostscript -h").equals("0")?""
                    + "<b class=\"text-success\">Instalado<b>":"<b class=\"text-error\">Não instalado</b>")+
                    "</td><td class=\"text-info\">Formato de imagem para FAX.</td></tr>");
        %>
        </table>
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
            $(function(){
                $('a').tooltip('hide');
                startCountDown();
            });
        </script>
        <div id="alert"></div>
    </body>
</html>
