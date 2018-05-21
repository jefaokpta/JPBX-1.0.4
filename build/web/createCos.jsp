<%-- 
    Document   : createCos
    Created on : 25/08/2014, 17:25:26
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
            out.print(head.header("JPBX->COS->New"));

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
            
            #costsAval .ui-selecting { background: #FECA40; }
            #costsAval .ui-selected { background: #F39814; color: white; }
            #costsAval { list-style-type: none; margin: 0; padding: 0; width: 90%; }
            #costsAval li { padding: 0.4em; font-size: 15px; height: 18px; }
        </style>
    </head>
    <body>
        <%out.print(head.navigator(session.getAttribute("lvl").toString()));%>

        <div id="container" class="container table-bordered">
            <form id="frm" name="frm1" action="CreateCos" method="POST">
                <fieldset>
                    <legend><h3>Nova Classe de Serviço</h3></legend>
                    <h4>Nome da Classe de Serviço</h4>
                    <div style="float: left">
                        <input id="cosName" type="text" name="cosName" onblur=loadInternal("WebServ?cosName="+this.value,'return'); />
                    </div>
                    <div style="float: left" id="return"></div>
                    <div class="clearfix"></div>
                    <h4>Centros de Custos</h4>
                    <div class="table-bordered span3" style="height: 190px;overflow: scroll;float: left">
                        <ol id="costsAval">
                            <%
                              out.print(db.listCostsCos());
                            %>
                        </ol>
                    </div>
                    <div style="float: left;padding-left: 2%">
                        <h5>Selecionados: </h5>
                    </div>
                    <div class="table-bordered span3" style="height: 190px;overflow: scroll;float: left;padding-left: 2%">
                        <ul id="costsSel"></ul>
                    </div>
                    
                    <div class="clearfix"></div>
                    <h4>Ramais</h4>
                    <div class="table-bordered span3" style="height: 190px;overflow: scroll;float: left">
                        <ol id="peersAval">
                            <%
                              out.print(db.listPeersCos());
                            %>
                        </ol>
                    </div>
                    <div style="float: left;padding-left: 2%">
                        <h5>Selecionados: </h5>
                    </div>
                    <div class="table-bordered span3" style="height: 190px;overflow: scroll;float: left;padding-left: 2%">
                        <ul id="peersSel"></ul>
                    </div>
                    
                    <div class="clearfix"></div>
                    <br/>
                    <input type="button" onclick="verifyFrm();" value="Salvar" class="btn btn-info" />
                    <a class="btn btn-info offset1" href="cos.jsp">Voltar</a>
                    <div id="post"></div>
                    <div id="post2"></div>
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
                        if ($('#cosName').val()==='')
                            informer('<p class=\"text-error\">PREENCHA O NOME DA CLASSE DE SERVIÇO!</p>');
                        else{
                            i=0;
                            $("#peersSel > li:not(:has(ul))").each(function(){
                                i++;
                                $('#post').append('<input type="hidden" name="peer'+i+'" value="'+$(this).text().substring(0,4)+'" />');
                            });
                            $('#post').append('<input type="hidden" name="qtde" value="'+i+'" />');
                            i=0;
                            $("#costsSel > li:not(:has(ul))").each(function(){
                                i++;
                                $('#post2').append('<input type="hidden" name="cost'+i+'" value="'+$(this).text()+'" />');
                            });
                            $('#post2').append('<input type="hidden" name="qtde2" value="'+i+'" />');
                            $('#frm').submit();
                            //alert($('#frm').html());
                        }
                    }   
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
                         $( "#costsAval" ).selectable({
                            stop: function() {
                            $('#costsSel').html('');
                            $('#post2').html('');
                            var result2 = $( "#select-result2" ).empty();
                            $( ".ui-selected", this ).each(function() {
                            var index2 = $( "#costsAval li" ).index( this );
                            result2.append( " #" + ( index2 + 1 ));
                            $('#costsSel').append('<li>'+$( '#costsAval li:nth-child('+(index2+1)+')' ).text()+'</li>');
                            });
                            }
                         });
                        // $('#peersSel').sortable();
                         $('#cosName').focus();
                    });
        </script>
        <div id="alert"></div>
    </body>
</html>
