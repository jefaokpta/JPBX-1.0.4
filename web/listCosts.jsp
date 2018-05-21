<%-- 
    Document   : listCusts
    Created on : 26/09/2013, 14:32:16
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
            out.print(head.header("JPBX->Costs"));
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
            out.print("<p>Falha na criação do Custo.<br>"+session.getAttribute("error")+"</p>");
            //out.print("<div class=\"modal-footer\">");
            //out.print("<button class=\"btn disabled\" data-dismiss=\"modal\" aria-hidden=\"true\">Cancelar</button>");
           // out.print("<button class=\"btn btn-primary\">Apagar</button>");
           // out.print("<a href=\"DeletePeers?peer="+request.getParameter("delete")+"\" class=\"btn btn-info\">Apagar</a>");
            out.print("</div></div>");
            session.removeAttribute("error");
            }
        %>
        <script src="css/jquery-ui-1.10.3/ui/jquery-ui.js"></script>
    </head>
    <body>
       <%out.print(head.navigator(session.getAttribute("lvl").toString()));
         JpbxDB db=new JpbxDB(); %> 
       
         <div id="container" class="container table-bordered">
        <h3>Centro de Custos</h3>
        <div id="accordion" class="span10">
         <%=db.listCosts() %>
                   
        </div>
        <a rel="tooltip" data-placement="botton" title="Criar novo Custo" href="#" onclick="newCost();"><img src="css/bootstrap/img/icon_new.png"></a>
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
            function verifyEdit(id){
                if($('#fare'+id).val()!==''&&$('#cicle'+id).val()!==''&&$('#fraction'+id).val()!==''&&$('#shortage'+id).val()!=='')
                    loadInternal('WebServ?costID='+id+'&fare='+$('#fare'+id).val()+'&cicle='+$('#cicle'+id).val()+'&fraction='+$('#fraction'+id).val()+'&shortage='+$('#shortage'+id).val()+'','alert');
                else
                    informer('<p class="text-error">TODOS OS CAMPOS DEVEM SER PREENCHIDOS PARA ALTERAÇÂO!</p>');
            }
            function delCost(id){   
               loadInternal('WebServ?delCost='+id+'&desc='+$('#title'+id).text(),'alert');
            }
            $(function(){               
                $('#accordion').accordion();
                startCountDown();
                $('h5 img').hide();
            <%=(request.getParameter("error")!=null?"informer('<p class=\"text-error\">"+request.getParameter("error")+"</p>');":"") %>
            });
        </script>
        <div id="alert"></div>
    </body>
</html>
