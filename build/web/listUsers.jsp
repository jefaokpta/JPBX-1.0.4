<%-- 
    Document   : listUsers
    Created on : 25/09/2013, 15:44:53
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
            out.print(head.header("JPBX->Users"));
            if(session.getAttribute("user")==null||!session.getAttribute("lvl").equals("Administrador")){
            response.sendRedirect("logout.jsp?error=Desconectado por inatividade.");
            return;
            }
        
        %>
    </head>
    <body>
       <%out.print(head.navigator(session.getAttribute("lvl").toString()));%> 
       
       <div id="container" class="container table-bordered">
        <h3>Cadastro de Usuários</h3>
        <a class="span1" rel="tooltip" data-placement="right" title="Criar novo usuário" href="createUser.jsp"><img src="css/bootstrap/img/icon_new.png"></a>
        <%
            JpbxDB db=new JpbxDB();
            out.print(db.listUsers());
        %>
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
            function deleteUser(user,id){
                $('#alert').html('<div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" \n\
                    aria-hidden="true">\n\
                <div class="modal-header">\n\
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>\n\
                <h3 id="myModalLabel">JPBX Informa</h3> </div>\n\
                <div class="modal-body">\n\
                <p>Deseja realmente apagar o usuário <b>'+user+'</b>?</p></div>\n\
                <div class="modal-footer">\n\
                <button class="btn disabled" data-dismiss="modal" aria-hidden="true">Cancelar</button>\n\
                <a href="DeleteUser?usr='+id+'" class="btn btn-info">Apagar</a>\n\
                </div></div>');
                $('#myModal').modal('show');
            }
            $(function(){
                $('a').tooltip('hide');
                startCountDown();
            });
        </script>
        <div id="alert"></div>
    </body>
</html>
