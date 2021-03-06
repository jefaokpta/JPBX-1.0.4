<%-- 
    Document   : listCallGrps
    Created on : 19/12/2013, 16:47:47
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
            out.print(head.header("JPBX->CallGrps"));
            
            if(session.getAttribute("user")==null||!session.getAttribute("lvl").equals("Administrador")){
                response.sendRedirect("logout.jsp?error=Desconectado por inatividade.");
                return;
            }
        %>
    </head>
    <body>
        <%out.print(head.navigator(session.getAttribute("lvl").toString()));%>
    
    <div id="container" class="container table-bordered">
        <h3>Cadastro de Grupos de Chamada</h3>
        <div class="span1">
            <a rel="tooltip" data-placement="right" title="Criar novo grupo de chamada" href="createCallGrp.jsp">
                <img src="css/bootstrap/img/icon_new.png"></a>
        </div>
            <div class="clearfix"></div>
        <%
            JpbxDB db=new JpbxDB();
            out.print(db.listCallGrps());
        %>
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
            function deleteCallGrp(grp,qtde,id){
                if(qtde===0)
                    $('#alert').html('<div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" \n\
                        aria-hidden="true">\n\
                    <div class="modal-header">\n\
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>\n\
                    <h3 id="myModalLabel">JPBX Informa</h3> </div>\n\
                    <div class="modal-body">\n\
                    <p>Deseja realmente apagar o grupo de chamada <b>'+grp+'</b>?</p></div>\n\
                    <div class="modal-footer">\n\
                    <button class="btn disabled" data-dismiss="modal" aria-hidden="true">Cancelar</button>\n\
                    <a href="DeleteCallGrp?grp='+id+'" class="btn btn-info">Apagar</a>\n\
                    </div></div>');
                else
                    $('#alert').html('<div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" \n\
                        aria-hidden="true">\n\
                    <div class="modal-header">\n\
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>\n\
                    <h3 id="myModalLabel">JPBX Informa</h3> </div>\n\
                    <div class="modal-body">\n\
                    <p class="text-error">O grupo de chamada <b>'+grp+'</b> contém integrantes e não pode ser apagado.</p></div>\n\
                    <div class="modal-footer">\n\
                    <button class="btn disabled" data-dismiss="modal" aria-hidden="true">Cancelar</button>\n\
                    </div></div>');
                $('#myModal').modal('show');
            }
            $(function(){
                $('a').tooltip('hide'); 
                $('a.btn').popover('hide');
                startCountDown();
            });
        </script>
        <div id="alert"></div>
    </div>
    </body>
</html>
