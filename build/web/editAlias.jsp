<%-- 
    Document   : editAlias
    Created on : 22/09/2014, 18:35:49
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
            out.print(head.header("JPBX->Alias->Edit"));
            
            if(session.getAttribute("user")==null){
                response.sendRedirect("logout.jsp?error=Desconectado por inatividade.");
                return;
            }
            JpbxDB db=new JpbxDB();
            String data[]=db.getAliasDescs(request.getParameter("id"));
        %>
    </head>
    <body>
        <%out.print(head.navigator(session.getAttribute("lvl").toString()));%>
    
    <div id="container" class="container table-bordered">
        <form id="frm" name="frm1" action="AlterAlias" method="POST">
            <fieldset>
                <legend><h2>Editar Alias de Expressão</h2></legend>
                <h4>Nome</h4>
                <input type="hidden" name="aliasID" value="<%=request.getParameter("id") %>" />
                <div style="float: left">
                    <input id="name" type="text" name="name" readonly="" value="<%=db.getAliasName(request.getParameter("id")) %>"/>
                </div>
                <div style="float: left" id="return"></div>
                <div class="clearfix"></div>
                <input type="hidden" id="qtdeDesc" name="qtdeDesc" />
                    <h4>Descrições</h4>
                    <div id="descs">
                        <%=data[0] %>
                    </div>
                <div class="clearfix"></div>
                <input type="button" onclick="verifyFrm();" value="Salvar" class="btn btn-info" />
                <a class="btn btn-info offset1" href="listAlias.jsp">Voltar</a>
            </fieldset>
        </form>
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
                if($('#name').val()==='')
                    informer('<p class=\"text-error\">PREENCHA O NOME</p>');
                else{
                    $('#qtdeDesc').val(pos);
                    $('#frm').submit();
                }
            }
            var pos=<%=data[1] %>;
            function addDesc(){
                pos++;
                $('#descs').append('<div id=\"desc'+pos+'\">\n\
                <input type="text" name="desc'+pos+'" />\n\
                <a class="icon-plus-sign" href="#" onclick="addDesc();"></a>\n\
                <a class="icon-minus-sign" href="#" onclick="delDesc(\'desc'+pos+'\');"></a>\n\
                </div>');
                $('#desc'+pos+' input').focus();
            }
            function delDesc(id){
                if(id!=='desc1')
                    $('#'+id).remove();
            }
            $(function(){
                $('a').tooltip('hide');
                startCountDown();
            });
        </script>
        <div id="alert"></div>
</html>
