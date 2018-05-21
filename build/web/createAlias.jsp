<%-- 
    Document   : createAlias
    Created on : 20/09/2014, 17:20:05
    Author     : jefaokpta
--%>

<%@page import="com.jpbx.Controller"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        
        <%
            Controller head=new Controller();
            out.print(head.header("JPBX->Alias->New"));
            
            if(session.getAttribute("user")==null){
                response.sendRedirect("logout.jsp?error=Desconectado por inatividade.");
                return;
            }
        %>
    </head>
    <body>
        <%out.print(head.navigator(session.getAttribute("lvl").toString()));%>
    
    <div id="container" class="container table-bordered">
        <form id="frm" name="frm1" action="CreateAlias" method="POST">
            <fieldset>
                <legend><h2>Novo Alias de Expressão</h2></legend>
                <h4>Nome</h4>
                <div style="float: left">
                    <input id="name" type="text" name="name" onblur=loadInternal("WebServ?alias="+this.value,'return'); />
                </div>
                <div style="float: left" id="return"></div>
                <div class="clearfix"></div>
                <input type="hidden" id="qtdeDesc" name="qtdeDesc" />
                    <h4>Descrições</h4>
                    <div id="descs">
                        <div id="desc1">
                            <input type="text" name="desc1" />
                            <a class="icon-plus-sign" href="#" onclick="addDesc();"></a>
                            <a class="icon-minus-sign" href="#" onclick="delDesc('desc1');"></a>
                        </div>
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
            var pos=1;
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
                $('#name').focus();
            });
        </script>
        <div id="alert"></div>
</html>
