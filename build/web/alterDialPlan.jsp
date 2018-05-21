<%-- 
    Document   : alterDialPlan
    Created on : 05/07/2013, 09:45:52
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
            out.print(head.header("JPBX->Dialplan->Edit"));
            
            if(session.getAttribute("user")==null){
                response.sendRedirect("logout.jsp?error=Desconectado por inatividade.");
                return;
            }
            %>
            <script src="css/jquery-ui-1.10.3/ui/jquery-ui.js"></script>
    </head>
    <body>
        <%out.print(head.navigator(session.getAttribute("lvl").toString()));
            JpbxDB db=new JpbxDB();
            String rule[]=db.dialPlanRules(Integer.parseInt(request.getParameter("rule")));
            String actions[][]=db.dialplanActions(Integer.parseInt(request.getParameter("rule")));
        %>
    
        <div id="container" class="container table-bordered">
        <form id="frm" name="frm1" action="AlterDialPlan" method="POST">
            <fieldset>
                <legend><h2>Editar Regra</h2></legend>
                <div class="span11">                    
                    <h4><u>Nome da Regra<u></h4>
                    <div style="float: left">
                        <input id="rulename" type="text" name="rulename" rel="tooltip" data-placement="right" title="Evite caracteres especiais" 
                               value="<%=rule[13] %>" onblur=loadInternal('WebServ?dialplan_name='+this.value,'checked'); />
                        <input type="hidden" name="ruleid" value="<%=request.getParameter("rule") %>" />
                    </div>
                    <div style="float: left" id="checked"></div>
                </div>
                <div class="span5">
                    <h4><u>Origem</u></h4>                  
                        <%                    
                        String listPeers=db.dialplanPeers(rule[2]);
                        String listTrunks=db.dialplanTrunks(rule[2]);
                        String deptos=db.listDeptos(rule[2]);
                        out.print(db.srcOptions(rule[1]));
                        %>
                </div>
                <div id="ori" class="span5">
                    <h4><u>Descrição da Origem</u></h4>
                </div>
               <div class="span11">
                    <h4><u>Destino</u></h4>
                    <%
                        if(rule[14].equals("1"))
                            out.print("<div id=\"aliases\">"
                                    + "<select name=\"alias\">"
                                    + ""+db.aliasOptions(rule[15])+""
                                    + "</select>"
                                    + "</div>"
                                    + "<input type=\"hidden\" name=\"exten\" value=\""+rule[0]+"\" />"
                                    + "<input type=\"hidden\" name=\"checkAlias\" value=\"on\" />");
                        else
                            out.print("<div style=\"float: left\">"
                                    + "<input id=\"exten\" type=\"text\" name=\"exten\" placeholder=\"Expressão\" "
                                    + "readonly=\"\" value=\""+rule[0]+"\" />"
                                    + "</div>");
                    %>
                    
                    <div style="float: left" id="extenChecked"></div>
                    <div class="clearfix"></div>
                    <h5><u>Período de Funcionamento</u></h5>
                    <h6>24hs <input id="checkTime" class="checkbox" type="checkbox" name="fulltime" <%=(rule[3].equals("1")?"checked":"") %> /></h6>
                    <div id="divTime">
                        <label class="checkbox inline">
                            <input type="checkbox" name="seg" value="0" <%=(rule[6].equals("1")?"checked":"") %>/>
                            Seg</label>
                        <label class="checkbox inline">
                            <input type="checkbox" name="ter" value="1" <%=(rule[7].equals("1")?"checked":"") %>/>
                            Ter</label>
                        <label class="checkbox inline">
                            <input type="checkbox" name="qua" value="2" <%=(rule[8].equals("1")?"checked":"") %>/>
                            Qua</label>
                        <label class="checkbox inline">
                            <input type="checkbox" name="qui" value="3" <%=(rule[9].equals("1")?"checked":"") %>/>
                            Qui</label>
                        <label class="checkbox inline">
                            <input type="checkbox" name="sex" value="4" <%=(rule[10].equals("1")?"checked":"") %>/>
                            Sex</label>
                        <label class="checkbox inline">
                            <input type="checkbox" name="sab" value="5" <%=(rule[11].equals("1")?"checked":"") %>/>
                            Sáb</label>
                        <label class="checkbox inline">
                            <input type="checkbox" name="dom" value="6" <%=(rule[12].equals("1")?"checked":"") %>/>
                            Dom</label>
                        <p>Horário Início: <input class="span1" id="hstart" type="text" name="hstart" value="<%=rule[4] %>" onblur="formatTime('#hstart');" />
                            Fim: <input class="span1" id="hend" type="text" name="hend" value="<%=rule[5] %>" onblur="formatTime('#hend');" /></p>
                    </div>
                    <div class="clearfix"></div>
                    <h4><u>Ações</u></h4>
                    <%=db.actionsOptions() %>
                <input id="add" type="button" class="btn btn-info" value="Adiciona"/>
                </div>
                <div class="tabbable tabs-left span9 table-bordered">
                    <ul style="background-color: #cccccc" class="nav nav-tabs" id="liapps">
                    <!--<li ><a href="#lA" data-toggle="tab">Seção 1</a></li>-->
                    <%
                        for(int i=1;i<=actions.length;i++){
                            out.print("<li id=\"liapp"+i+"\">"
                                    + "<a data-toggle=\"tab\" href=\"#li"+i+"\">"
                                    + db.translateApps(actions[(i-1)][0])
                                    + " <img src=\"css/bootstrap/img/deletarsmall.gif\" onclick=\"delli("+i+");\"></img>"
                                    + "</a>"
                                    + "</li>");
                        }
                    %>
                </ul>
                    <div class="tab-content" id="appparams">
                <!--  <div class="tab-pane active" id="lA"></div>-->
                  <%
                        for(int i=1;i<=actions.length;i++){
                            out.print(db.appsParams(actions[(i-1)][0],actions[(i-1)][1] ,
                                    actions[(i-1)][2], actions[(i-1)][3],String.valueOf(i),actions[(i-1)][4] ));
                        }
                  %>
                
              </div>
            </div> <!-- /tabbable -->
            <div id="post"></div>
            <div id="verifySrcDst"></div>
            <div class="clearfix"></div>
            <div class="span5">
                <input type="button" onclick="verifyFrm();" value="Salvar" class="btn btn-info offset1" />
                <img id="load" src="css/bootstrap/img/loading.gif"/>
                <a class="btn btn-info offset1" href="listDialPlan.jsp">Voltar</a>
            </div>
            </fieldset>
        </form>
    </div> 
        <script>
            function formatTime(id){
                if($(id).val()!==''){
                    time=$(id).val();
                    time=time.replace(':','');               
                    time=time.substring(0,4);
                    $(id).val(time.substring(0,2)+':'+time.substring(2));
                }
            }
            if($('#checkTime').is(':checked'))
                $('#divTime').hide();
            $('#checkTime').click(function(){
                $('#divTime').toggle('slow');
            });
            posli=<%=actions.length %>
            $('#add').click(function(){
                posli++;
                active="";
                if(posli===1)
                    active="active";
                $('#liapps').append('<li id="liapp'+posli+'" class="'+active+'" ><a href="#li'+posli+'" data-toggle="tab">'+
                        $('#selAct option:selected').text()+'\n\
                    <img onclick=delli('+posli+'); src="css/bootstrap/img/deletarsmall.gif"/></a></li>');
               // $('#appparams').html($('#appparams').html()+'<div class="tab-pane" id="li'+posli+'"><p>Estou na Seção '+posli+'</p></div>');
               //loadInternal("webserv.jsp?peer="+this.value,'validation');
                $('#appparams').append('<div class="tab-pane '+active+'" id="li'+posli+'"></div>');
                loadInternal('WebServ?dialplan='+posli+'&app='+$('#selAct option:selected').text()+'','li'+posli+'');
            });
            function delli(posli){
                $('#liapp'+posli).remove();
                $('#li'+posli).remove();
            };
            $('#load').hide();
            qtdeapps=0;
            function verifyFrm(){             
                if($('#rulename').val()==='')
                    informer('<p style="color: #ff0000">Preencha o NOME DA REGRA</p>');
                else if($('#exten').val()==='')
                    informer('<p style="color: #ff0000">Preencha o DESTINO</p>');
                else{
                    $('#load').show();   
                    if($('#selOri option:selected').text()==='Expressão Regular'||
                        $('#selOri option:selected').text()==='Qualquer')
                        src=$('#src').val();
                    else
                        src=$('#src option:selected').text();
                    $('#post').html('<input type="hidden" name="selsrc" value="'+$('#selOri option:selected').val()+'" />');
                    $("#liapps > li:not(:has(ul))").each(function(){ 
                        div=$(this).attr('id').replace('app','');
                        apppos=$(this).attr('id').replace('liapp','');
                        qtdeapps++;
                        $('#post').html($('#post').html()+'<input type="hidden" name="appname'+qtdeapps+'" value="'+$('#app'+apppos).attr('value')+'" />\n\
                            <input type="hidden" name="param'+qtdeapps+'" value="'+$('#appp'+apppos).attr('value')+'" />\n\
                            <input type="hidden" name="paramaux'+qtdeapps+'" value="'+$('#apppp'+apppos).attr('value')+'" />');
                        //alert('app '+$('#app'+apppos).attr('value')+'\n param '+$('#appp'+apppos).attr('value'));
                    });
                    $('#post').html($('#post').html()+'<input type="hidden" name="qtdeapps" value="'+$('#liapps').children().length+'" />');
                    //alert($('#post').html());
                   // loadInternal('WebServ?verifySrcDst='+$('#exten').val()+'&src='+src,'verifySrcDst');
                    if($('#liapps').children().length===0){
                        informer('<p style="color: #ff0000">SEM AÇÕES DEFINIDAS!</p>');
                        $('#load').hide();
                    }
                    else
                        setTimeout("beforeSubmit();",1000);   
                }   
            };
            function beforeSubmit(){
              /*  if($('#returnVerifySrcDst').val()!=='0'){
                        $('#load').hide();
                        informer('<p style="color: #ff0000">ORIGEM E DESTINO DUPLICADOS!</br> Esta regra não será criada </p>\n\
                            Aguarde <img src="css/bootstrap/img/loading.gif"/>'+$('#returnVerifySrcDst').val());
                        $('#post').html('');
                        setTimeout('location.reload();',5000);
                    }
                    else*/
                        $('#frm').submit();
            }
            $('#sec2').click(function(){
                alert('posição: '+($('#sec2').index()+1));
            });
            $('#sec3').click(function(){
                alert('posição: '+($('#sec3').index()+1));
            });
              // alert('total de '+$('ol').children().length);
              // alert('posicão '+($('#'+id+'').index()+1));
            $('#selOri').change(function(){
                selectSrc();
            });
            function selectSrc(){
                switch($('#selOri option:selected').text()){
                    case 'Expressão Regular':
                        $('#ori').html('<h4><u>Descrição da Origem</u></h4>\n\
                            <input id="src" type="text" name="src" placeholder="<%="${CALLERID(num)} == 2000" %>"\n\
                                 value="<%=(rule[1].equals("Expr")?rule[2]:"") %>" />');
                        break;
                    case 'Ramal':
                        $('#ori').html('<h4><u>Descrição da Origem</u></h4><%=listPeers %>');
                        break;
                    case 'Qualquer':
                        $('#ori').html('<input id="src" type="hidden" name="src" value="Qualquer" />');
                        break;
                    case 'Fax Virtual':
                        $('#ori').html('<input id="src" type="hidden" name="src" value="Fax" />');
                        break;
                    case 'Tronco':
                        $('#ori').html('<h4><u>Descrição da Origem</u></h4><%=listTrunks %>');
                        break; 
                    case 'Departamento':
                        $('#ori').html('<h4><u>Descrição da Origem</u></h4><%=deptos %>');
                        break;
                    default:
                        informer('<p>Não Implementado</p>');
                }
            }
            $('#cria').click(function(){
                $('#ori').html($('#ori').html()+'</br> otra coisa');
            });
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
            $(function(){
               // $('a').tooltip('hide');
                startCountDown();              
                $('#liapps').sortable();
                $('#liapps').disableSelection();
                selectSrc();          
            });           
    </script>
    <div id=alert></div>
    </body>
</html>
