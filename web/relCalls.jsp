<%-- 
    Document   : relCalls
    Created on : 30/09/2013, 10:15:07
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
            out.print(head.header("JPBX->Report"));
            
            if(session.getAttribute("user")==null){
                response.sendRedirect("logout.jsp?error=Desconectado por inatividade.");
                return;
            }
        %>
        <link rel="stylesheet" href="css/jquery-ui-1.10.3/themes/base/jquery-ui.css"/>
        <script src="css/jquery-ui-1.10.3/ui/jquery-ui.js"></script>
        <script src="css/jquery-ui-1.10.3/ui/ui.datepicker-pt_BR.js"></script>
    </head>
    <body>
        <%out.print(head.navigator(session.getAttribute("lvl").toString()));%>
    
    <div id="container" class="container table-bordered">
        <form id="frm" name="frm1" action="RelCalls" method="POST">
            <fieldset>
                <legend><h3><u>Relatório de Ligações</u></h3></legend>
                <h4><u>Periodo: </u></h4>
                    <h5 style="float: left">Data de Início: </h5>
                    <input style="float: left" id="dateini" type="text" name="dateini"/>
                    <h5 class="offset1" style="float: left">Até: </h5>
                    <input id="dateend" type="text" name="dateend"/>
                <div class="clearfix"></div>
                <h4 style="float: left"><u>Ordenar: </u></h4>
                <label class="radio inline" style="padding-left: 5%">
                    <input type="radio" name="order" value="ordercalldate" checked>
                    <h5>Data</h5>
                </label>
                <label class="radio inline">
                    <input type="radio" name="order" value="ordersrc">
                    <h5>Origem</h5>
                </label>
                <label class="radio inline">
                    <input type="radio" name="order" value="orderdstfinal">
                    <h5>Destino</h5>
                </label>
                <label class="radio inline">
                    <input type="radio" name="order" value="orderbillsec">
                    <h5>Conversação</h5>
                </label>
                <h4><u>Filtros </u></h4>
                <div id="filters" class="table-bordered span10">
                    <h5 style="float: left">Origem(s): </h5>
                    <input id="src" style="float: left" placeholder="1000,1001,1002" type="text" name="src" 
                           rel="tooltip" data-placement="right" title="Permite várias origens separadas por ',' (vírgula)."/>
                    <label class="radio inline offset1">
                        <input type="radio" name="srcseek" value="equals" checked>
                        <h5>Igual a:</h5>
                    </label>
                    <label class="radio inline">
                        <input type="radio" name="srcseek" value="begin">
                        <h5>Começa com:</h5>
                    </label>
                    <label class="radio inline">
                        <input type="radio" name="srcseek" value="contains">
                        <h5>Contém:</h5>
                    </label>
                    <div class="clearfix"></div>
                    <h5 style="float: left">Destino(s): </h5>
                    <input id="dst" style="float: left" placeholder="1000,1001,1002" type="text" name="dstfinal" 
                           rel="tooltip" data-placement="right" title="Permite vários destinos separadas por ',' (vírgula)."/>
                    <label class="radio inline offset1">
                        <input type="radio" name="dstseek" value="equals" checked>
                        <h5>Igual a:</h5>
                    </label>
                    <label class="radio inline">
                        <input type="radio" name="dstseek" value="begin">
                        <h5>Começa com:</h5>
                    </label>
                    <label class="radio inline">
                        <input type="radio" name="dstseek" value="contains">
                        <h5>Contém:</h5>
                    </label>
                    <div class="clearfix"></div>
                    <h5 style="float: left">Centro de Custo: </h5>
                    <select style="float: left" name="ccost">
                        <option>Todos</option>
                        <option>Indefinido</option>
                        <%
                        JpbxDB db=new JpbxDB();
                        out.print(db.selectCosts());
                        %>
                    </select>
                    <div class="clearfix"></div>
                    <h5 style="float: left"><u>Estado da Ligação: </u></h5>
                <label class="radio inline" style="padding-left: 5%">
                    <input type="radio" name="status" value="all" checked>
                    <h5>Todas</h5>
                </label>
                <label class="radio inline">
                    <input type="radio" name="status" value="answered">
                    <h5>Atendidas</h5>
                </label>
                <label class="radio inline">
                    <input type="radio" name="status"value="no answer">
                    <h5>Não Atendidas</h5>
                </label>
                <label class="radio inline">
                    <input type="radio" name="status" value="busy">
                    <h5>Ocupadas</h5>
                </label>
                </div>
                <input type="hidden" id="method" name="method"/>
                <div class="clearfix"></div>
                </br>
                <input type="button" onclick="verifyFrm();" value="Exibir" class="btn btn-info" />
                <img id="load" src="css/bootstrap/img/loading.gif"/>
                <input type="button" onclick="csv();" value="Extrair CSV" class="btn btn-info offset1" />
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
                $('#load').show();
                $('#method').val('');
                setTimeout('doPost();',1000);
            }
            function doPost(){
                $('#frm').submit();
                $('#load').hide();
            }
            function csv(){
                $('#load').show();
                $('#method').val('csv');
                doPost();
            }
            $(function(){        
                day=new Date();
                if((day.getDate())<10)
                    d='0'+(day.getDate());
                else
                    d=(day.getDate());
                if((day.getMonth()+1)<10)
                    m='0'+(day.getMonth()+1);
                else
                    m=(day.getMonth()+1);
                $('#dateend').val(d+'/'+m+'/'+day.getFullYear());
                $('#dateini').val('01/'+m+'/'+day.getFullYear());
                startCountDown();
                $( "#dateini" ).datepicker();
                $( "#dateend" ).datepicker();
                $('#method').val('');
                $('#load').hide();
            });
        </script>
        <div id="alert"></div>
    </body>
</html>
