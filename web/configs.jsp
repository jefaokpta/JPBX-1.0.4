<%-- 
    Document   : configs
    Created on : 21/10/2013, 16:43:55
    Author     : jefaokpta
--%>

<%@page import="java.io.DataInputStream"%>
<%@page import="com.jpbx.JpbxDB"%>
<%@page import="com.jpbx.Controller"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>       
        <%
            Controller head=new Controller();
            out.print(head.header("JPBX->Configs"));
            if(session.getAttribute("user")==null||!session.getAttribute("lvl").equals("Administrador")){
                response.sendRedirect("logout.jsp?error=Desconectado por inatividade.");
                return;
            }
            if(session.getAttribute("error")!=null){
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
            out.print("<p>"+session.getAttribute("error") +"</p></div>");
            //out.print("<div class=\"modal-footer\">");
            //out.print("<button class=\"btn\" data-dismiss=\"modal\" aria-hidden=\"true\">Fechar</button>");
            //out.print("<button class=\"btn btn-primary\">Salvar mudanças</button>");
            out.print("</div></div>");
            session.removeAttribute("error");
            }
        %>
    </head>
    <body>
       <%out.print(head.navigator(session.getAttribute("lvl").toString()));
         JpbxDB db=new JpbxDB(); 
         int data[]=db.getLimitRelCalls();
         int limit=data[0];
       %> 
         <div id="container" class="container table-bordered">
        <form id="frm" name="frm1" action="Configs" method="POST">
            <fieldset>
                <legend><h2>Configurações do Sistema</h2></legend>
                <h5><u>Limite de Linhas nos Relatórios</u></h5>
                <select name="limit" class="span1">
                    <option <%=(limit==15?"selected":"")%>>15</option>
                    <option <%=(limit==30?"selected":"")%>>30</option>
                    <option <%=(limit==60?"selected":"")%>>60</option>
                    <option <%=(limit==90?"selected":"")%>>90</option>
                </select>
                <div class="clearfix"></div>
                <h5><u>Música em Espera</u></h5>
                <div>
                    <select style="float: left" name="moh" id="moh">
                        <%=db.listMohs() %>
                    </select>
                    <div id="audio"></div>
                    
                </div>
               <!-- <a rel="tooltip" data-placement="right" title="Os arquivos de audio devem estar no formato wav, Mono, 8K, 16bits.">
                        <input type="button" value="Adicionar Espera" onclick="uploadMoh();"></a> -->
                <div class="clearfix"></div>
                <h5 style="float: left" class="span4"><u>Ativa Lista de IPs permitidos para Registrar Ramais</u></h5>
                <input id="checkIps" style="float: left" type="checkbox" name="checkIps" <%=(data[1]==1?"checked":"") %> />
                <a rel="tooltip" data-placement="right" title="ATENÇÂO! Seus ramais devem estar dentro dos ranges cadastrados nesta lista.">
                    <textarea id="ips" class="span3"  rows="5" cols="50" placeholder="172.16.0.0/255.255.0.0" name="ips"><%
                    if(data[1]==1){
                        String ls_str;
                        Process ls_proc=Runtime.getRuntime().exec("cat /etc/asterisk/jpbx/sipContacts.conf");
                        DataInputStream ls_in = new DataInputStream(ls_proc.getInputStream());
                        while ((ls_str = ls_in.readLine()) != null) {
                            if(ls_str.contains("permit"))
                                out.println(ls_str.substring((ls_str.indexOf("=")+1)));
                        }
                    }
                    %></textarea>
                </a>
                <div class="clearfix"></div>
                <input type="hidden" id="qtdeDns" name="qtdeDns" />
                <div id="dnsView">
                    <h4 style="float: left">DNS's Dinâmicos</h4>
                    <div style="float: left" class="offset2" id="respHost"></div>
                    <div class="clearfix"></div>
                    <div id="dnss">
                        <%
                        String dns[][]=db.listDnsScript();
                        if(dns.length==0)
                            out.print("<div id=\"dns1\">"
                            + "<input type=\"text\" name=\"dns1\" onblur=\"loadInternal('WebServ?testHost='+this.value,'respHost');\" />"
                                    + "<a class=\"icon-plus-sign\" href=\"#\" onclick=\"addDesc();\"></a>"
                                    + "<a class=\"icon-minus-sign\" href=\"#\" onclick=\"delDesc('dns1');\"></a>"
                                    + "</div>");
                        else
                            for(int i=1;i<=dns.length;i++)
                                out.print("<div id=\"dns"+i+"\">"
                                + "<input type=\"text\" name=\"dns"+i+"\" value=\""+dns[i-1][0]+"\" onblur=\"loadInternal('WebServ?testHost='+this.value,'respHost');\" />"
                                    + "<a class=\"icon-plus-sign\" href=\"#\" onclick=\"addDesc();\"></a>"
                                    + "<a class=\"icon-minus-sign\" href=\"#\" onclick=\"delDesc('dns"+i+"');\"></a>"
                                    + "</div>");
                        %>          
                    </div>
                </div>
                <input class="btn btn-warning" type="button" value="Lista de Facilidades" onclick="listFeatures();" />               
                </br></br>
                <div class="clearfix"></div>
                <input type="button" onclick="verifyFrm();" value="Salvar" class="btn btn-info" />
                <img id="load" src="css/bootstrap/img/loading.gif" />
            </fieldset>
        </form>
        </div>
                    <div id="alert"></div>
                <script>
                    $('#load').hide();
                    var pos=<%=(dns.length==0?"1":dns.length) %>;
                    function addDesc(){
                        pos++;
                        $('#dnss').append('<div id=\"dns'+pos+'\">\n\
                        <input type="text" name="dns'+pos+'" onblur="loadInternal(\'WebServ?testHost=\'+this.value,\'respHost\');" />\n\
                        <a class="icon-plus-sign" href="#" onclick="addDesc();"></a>\n\
                        <a class="icon-minus-sign" href="#" onclick="delDesc(\'dns'+pos+'\');"></a>\n\
                        </div>');
                        $('#dns'+pos+' input').focus();
                    }
                    function delDesc(id){
                        if(id!=='dns1')
                            $('#'+id).remove();
                    }
                    function verifyFrm(){
                        if($('#checkIps:checked').val())
                            if($('#ips').val()!==''){
                                $('#load').show();
                                $('#qtdeDns').val(pos);
                                $('#frm').submit();
                            }
                            else
                                informer('<p class="text-error">Preencha corretamente a lista de IPs.</p>');
                        else{
                            $('#load').show();
                            $('#qtdeDns').val(pos);
                            $('#frm').submit();
                        }
                    }
                    time=<%= session.getMaxInactiveInterval() %>;
                    user="<%= session.getAttribute("user") %>";
                    function startCountDown(){
                        if(time>0){
                            $("#cron").html("Olá "+user+"</br>"+countDown(time));
                            time--;
                            setTimeout("startCountDown();", 1000);
                        }else{
                        $("#cron").html("Sessão Expirada!");
                        }
                    }
                    $('#moh').change(function(){
                        $('#audio').html('<audio controls><source src="asterisk/moh/'+$('#moh').val()+'/'+$('#moh option:selected').attr('name')+'" type="audio/wav"></audio>');
                    });
                    $('#checkIps').click(function(){
                        if($('#checkIps:checked').val()){
                            $('#ips').removeAttr('readonly');
                            $('#dnsView').toggle('slow');
                        }
                        else{
                            $('#ips').attr('readonly',true);
                            $('#dnsView').toggle('slow');
                        }
                    });
                    $(function(){               
                        startCountDown();
                        $('#audio').html('<audio controls><source src="asterisk/moh/'+$('#moh').val()+'/'+$('#moh option:selected').attr('name')+'" type="audio/wav"></audio>');
                        //alert($('#moh option:selected').attr('name'));
                        $('a').tooltip();
                        if(!$('#checkIps:checked').val()){
                            $('#ips').attr('readonly',true);
                            $('#dnsView').hide();
                        }
                    });
                </script>
    </body>
</html>
