<%-- 
    Document   : listDialPlan
    Created on : 05/07/2013, 09:42:25
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
            out.print(head.header("JPBX->Dialplan"));
            
            if(session.getAttribute("user")==null||!session.getAttribute("lvl").equals("Administrador")){
                response.sendRedirect("logout.jsp?error=Desconectado por inatividade.");
                return;
            }
            if(request.getParameter("delete")!=null){
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
            out.print("<p>Deseja realmente apagar a regra <b>"+request.getParameter("delete") +"</b>?</p></div>");
            out.print("<div class=\"modal-footer\">");
            out.print("<button class=\"btn disabled\" data-dismiss=\"modal\" aria-hidden=\"true\">Cancelar</button>");
           // out.print("<button class=\"btn btn-primary\">Apagar</button>");
            out.print("<a href=\"DeleteDialPlan?rule="+request.getParameter("delete")+"\" class=\"btn btn-info\">Apagar</a>");
            out.print("</div></div></div>");
            //session.removeAttribute("return");
            }
            
            if(request.getParameter("fail")!=null){
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
            out.print("<p>Possivel falha no Banco de Dados:</br> "+request.getParameter("fail") +"</p></div>");
            out.print("<div class=\"modal-footer\">");
            out.print("<button class=\"btn disabled\" data-dismiss=\"modal\" aria-hidden=\"true\">Cancelar</button>");
           // out.print("<button class=\"btn btn-primary\">Apagar</button>");
           //out.print("<a href=\"DeletePeers?peer="+request.getParameter("delete")+"\" class=\"btn btn-info\">Apagar</a>");
            out.print("</div></div></div>");
            //session.removeAttribute("return");
        }
        %>
    </head>
    <body>
        <%out.print(head.navigator(session.getAttribute("lvl").toString()));%>
    
        <div id="container" class="container table-bordered">
        <h3>Regras de Discagem</h3>
        <a class="span1" rel="tooltip" data-placement="right" title="Criar nova Regra" href="createDialPlan.jsp">
            <img src="css/bootstrap/img/icon_new.png"></a>
        <%
        JpbxDB db=new JpbxDB();
        out.print(db.listDialplan());
        %>
    </div>
    <div id="alert"></div>
    </body>
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
            function activeRule(id,name,act){
                loadInternal('WebServ?activeRule='+id+'&act='+act+'','alert');
            }
            function deleteDialPlan(ruleId,name){
                $('#alert').html('<div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" \n\
                    aria-hidden="true">\n\
                <div class="modal-header">\n\
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>\n\
                <h3 id="myModalLabel">JPBX Informa</h3> </div>\n\
                <div class="modal-body">\n\
                <p>Deseja realmente apagar a Regra <b>'+ruleId+' - '+name+'</b>?</p></div>\n\
                <div class="modal-footer">\n\
                <button class="btn disabled" data-dismiss="modal" aria-hidden="true">Cancelar</button>\n\
                <a href="DeleteDialPlan?rule='+ruleId+'" class="btn btn-info">Apagar</a>\n\
                </div></div>');
                $('#myModal').modal('show');
            }
            $(function(){
                $('a').tooltip('hide');
                startCountDown();
            });
    </script>
</html>
