<%-- 
    Document   : createCallGrp
    Created on : 19/12/2013, 16:48:27
    Author     : jefaokpta
--%>

<%@page import="com.jpbx.JpbxDB"%>
<%@page import="com.jpbx.Controller"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <%
            Controller head = new Controller();
            out.print(head.header("JPBX->CallGrps->New"));

            if (session.getAttribute("user") == null) {
                response.sendRedirect("logout.jsp?error=Desconectado por inatividade.");
                return;
            }
            JpbxDB db=new JpbxDB();
        %>
        <script src="css/jquery-ui-1.10.3/ui/jquery-ui.js"></script>
         <style>
            #feedback { font-size: 1.4em; }
            #peersAval .ui-selecting { background: #FECA40; }
            #peersAval .ui-selected { background: #F39814; color: white; }
            #peersAval { list-style-type: none; margin: 0; padding: 0; width: 90%; }
            #peersAval li { padding: 0.4em; font-size: 15px; height: 18px; }
        </style>
    </head>
    <body>
        <%out.print(head.navigator(session.getAttribute("lvl").toString()));%>

        <div id="container" class="container table-bordered">
            <form id="frm" name="frm1" action="CreateCallGrp" method="POST">
                <fieldset>
                    <legend><h3>Novo Grupo de Chamada</h3></legend>
                    <h4>Nome do Grupo</h4>
                    <div style="float: left">
                        <input id="grpname" type="text" name="grpName" onblur=loadInternal("WebServ?callGrp="+this.value,'return'); />
                    </div>
                    <div style="float: left" id="return"></div>
                    <div class="clearfix"></div>
                    <h4>Empresa</h4>
                    <%=db.getCompanyOptions() %>
                    <h4>Música de Espera</h4>
                    <select name="moh" id="moh">
                            <%=db.optionsMoh() %>
                    </select>
                    <div>
                        <h4>Estratégia de Toque</h4>
                        <select name="strategy" id="strat">
                            <option value="ringall">Todos</option>
                            <option value="random">Aleatório</option>
                            <option value="fewestcalls">Oscioso</option>
                        </select>
                        <div id="info"></div>
                    </div>
                    <div class="clearfix"></div>
                    <h4>Tempo de Chamada dos Integrantes</h4>
                    <input name="timeout" type="number" value="15" />
                    <h4>Escolha Ramais Integrantes</h4>
                    <div class="table-bordered span3" style="height: 190px;overflow: scroll;float: left">
                        <ol id="peersAval">
                            <%
                              out.print(db.listPeersCallGrp());
                            %>
                    </ol>
                    </div>
                    <div style="float: left;padding-left: 2%">
                        <h5>Selecionados: </h5>
                    </div>
                    <div class="table-bordered span3" style="height: 190px;overflow: scroll;float: left;padding-left: 2%">
                        <ul id="peersSel"></ul>
                    </div>
                    <p class="muted">Organize a ordem dos ramais<br/> na caixa selecionados arrastando-os.</p>
                    <div class="clearfix"></div>
                    <br/>
                    <input type="button" onclick="verifyFrm();" value="Salvar" class="btn btn-info" />
                    <a class="btn btn-info offset1" href="listCallGrps.jsp">Voltar</a>
                    <div id="post"></div>
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
                        if ($('#grpname').val()==='')
                            informer('<p class=\"text-error\">PREENCHA O NOME DO GRUPO!</p>');
                        else{
                            i=0;
                            $("#peersSel > li:not(:has(ul))").each(function(){
                                i++;
                                $('#post').append('<input type="hidden" name="peer'+i+'" value="'+$(this).text().substring(0,4)+'" />');
                            });
                            $('#post').append('<input type="hidden" name="qtde" value="'+i+'" />');
                            $('#frm').submit();
                        }
                    }   
                    $('#strat').change(function(){
                        switch($('#strat option:selected').text()){
                            case 'Todos':
                                $('#info').html('<p class="muted">Toca todos os ramais do grupo simultâneamente.</p>');
                                break;
                            case 'Aleatório':
                                $('#info').html('<p class="muted">Toca os ramais do grupo aleatóriamente.</p>');
                                break;
                            case 'Oscioso':
                                $('#info').html('<p class="muted">Busca o ramal que menos atendeu ligações.</p>');
                                break;
                        }
                    });
                    $(function(){
                        startCountDown();
                         $( "#peersAval" ).selectable({
                            stop: function() {
                            $('#peersSel').html('');
                            $('#post').html('');
                            var result = $( "#select-result" ).empty();
                            $( ".ui-selected", this ).each(function() {
                            var index = $( "#peersAval li" ).index( this );
                            result.append( " #" + ( index + 1 ));
                            $('#peersSel').append('<li>'+$( '#peersAval li:nth-child('+(index+1)+')' ).text()+'</li>');
                            });
                            }
                         });
                         $('#peersSel').sortable();
                         $('#grpname').focus();
                    });
        </script>
        <div id="alert"></div>
    </body>
</html>
