<%-- 
    Document   : alterCallGrp
    Created on : 19/12/2013, 16:58:37
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
            String data[]=db.editCallGrp(request.getParameter("grp"));
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
            <form id="frm" name="frm1" action="AlterCallGrp" method="POST">
                <fieldset>
                    <legend><h3>Editar Grupo de Chamada</h3></legend>
                    <h4>Nome do Grupo</h4>
                    <div style="float: left">
                        <input id="grpname" readonly="" type="text" name="grpName" value="<%=data[0].substring(8) %>"/>
                    </div>
                    <div style="float: left" id="return"></div>
                    <div class="clearfix"></div>
                    <h4>Empresa</h4>
                    <%=db.getCompanyOptions(data[4]) %>
                    <h4>Música de Espera</h4>
                    <select name="moh" id="moh">
                        <%=db.optionMohSelected(data[3])%>
                    </select>
                    <div>
                        <h4>Estratégia de Toque</h4>
                        <select name="strategy" id="strat" style="float: left">
                            <option value="ringall" <%=(data[1].equals("ringall")?"selected":"") %>>Todos</option>
                            <option value="random" <%=(data[1].equals("random")?"selected":"") %>>Aleatório</option>
                            <option value="fewestcalls" <%=(data[1].equals("fewestcalls")?"selected":"") %>>Oscioso</option>
                        </select>
                        <div id="info"></div>
                    </div>
                    <div class="clearfix"></div>
                    <h4>Tempo de Chamada dos Integrantes</h4>
                    <input name="timeout" type="number" value="<%=data[2] %>" />
                    <h4>Escolha Ramais Integrantes</h4>
                    <div class="table-bordered span3" style="height: 190px;overflow: scroll;float: left">
                        <ol id="peersAval">
                            <%
                              out.print(db.listPeersCallGrp(data[0]));
                            %>
                    </ol>
                    </div>
                    <div style="float: left;padding-left: 2%">
                        <h5>Selecionados: </h5>
                    </div>
                    <div class="table-bordered span3" style="height: 190px;overflow: scroll;float: left;padding-left: 2%">
                        <ul id="peersSel"><%=db.listCallPeersSelected(data[0]) %></ul>
                    </div>
                    <p class="muted">Organize a ordem dos ramais<br/> na caixa selecionados arrastando-os.</p>
                    <a style="padding-left: 2%" href="#" onclick="clean();">Limpar</a>
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
                    function clean(){
                        $('#peersSel li').remove();
                    }
                    function verifyFrm(){
                        i=0;
                        $("#peersSel > li:not(:has(ul))").each(function(){
                            i++;
                            $('#post').append('<input type="hidden" name="peer'+i+'" value="'+$(this).text().substring(0,4)+'" />');
                        });
                        $('#post').append('<input type="hidden" name="qtde" value="'+i+'" />');
                        $('#frm').submit();
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
