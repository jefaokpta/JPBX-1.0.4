<%-- 
    Document   : index
    Created on : 21/12/2012, 10:27:46
    Author     : jefaokpta
--%>


<%@page import="com.jpbx.JpbxDB"%>
<%@page import="java.util.GregorianCalendar"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.jpbx.Controller"%>
<%@page import="java.io.DataInputStream"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
           
      <%
          Controller head=new Controller();
          out.print(head.header("JPBX->Login"));
          
        if(session.getAttribute("alert")!=null){
            out.print("<script>"+
    	       "$(function(){"+
                    "$('#myModal').modal('show')"+
                 //   "window.location=\"index.jsp\";"+
                "});"+
                "</script>");
           
            out.print(" <div id=\"alert\">");
            out.print("<div id=\"myModal\" class=\"modal hide fade\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"myModalLabel\" aria-hidden=\"true\">");
            out.print("<div class=\"modal-header\">");
            out.print("<button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-hidden=\"true\">×</button>");
            out.print("<h3 id=\"myModalLabel\">JPBX Informa</h3> </div>");
            out.print("<div class=\"modal-body\">");
            out.print("<p style=\"color: #f70808\">"+session.getAttribute("alert")+"</p></div>");
          //  out.print("<div class=\"modal-footer\">");
          //  out.print("<button class=\"btn\" data-dismiss=\"modal\" aria-hidden=\"true\">Fechar</button>");
            //out.print("<button class=\"btn btn-primary\">Salvar mudanças</button>");
            out.print("</div></div>");
            
            session.removeAttribute("alert");
        }
        session.setAttribute("token", head.getToken());
    %>
    </head>
    <body>
        <!--######################################################################## -->
        <div id="container" class="container span5 offset4">
               <div class="span3">
               <form id="frm" action="Controller" method="POST">
                   <fieldset>
                    <legend><em>Autenticação JPBX</em></legend>                   
                    <input type="text" id="name" name="user" placeholder="Usuário" />
                    <input id="pass" type="password" placeholder="Senha" /></br>
                    <input type="button" id="go" value="Entrar" class="btn btn-info" />
                    <img id="load" src="css/bootstrap/img/loading.gif"/>
                    <input id="passport" type="hidden" name="pass"/>
                   </fieldset>
                </form>
               </div>
               <div style="margin-top: 50px">
                <img src="css/bootstrap/img/cadeado.png"/>
            </div>
        </div>
        <div id="op"></div>
    </body>
<script>
    $('#load').hide();
    $('#name').focus();
    $('#go').click(function(){
        submit();      
    });
    $('#pass').keypress(function(event){
        if(event.which==13){
            submit();
        }
    });
    function submit(){
        $('#load').show();
        $('#passport').val((hashMD5($('#name').val()+':'+$('#pass').val())));
        $('#pass').val('');
        $('#frm').submit();
    }
</script>
</html>
