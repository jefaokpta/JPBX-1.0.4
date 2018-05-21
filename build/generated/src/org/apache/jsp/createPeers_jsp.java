package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.jpbx.Controller;
import com.jpbx.JpbxDB;

public final class createPeers_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List<String> _jspx_dependants;

  private org.glassfish.jsp.api.ResourceInjector _jspx_resourceInjector;

  public java.util.List<String> getDependants() {
    return _jspx_dependants;
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;

    try {
      response.setContentType("text/html;charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;
      _jspx_resourceInjector = (org.glassfish.jsp.api.ResourceInjector) application.getAttribute("com.sun.appserv.jsp.resource.injector");

      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("<!DOCTYPE html>\n");
      out.write("<html>\n");
      out.write("    <head>\n");
      out.write("        \n");
      out.write("        ");

            Controller head=new Controller();
            out.print(head.header("JPBX->Peers->New"));
            
            if(session.getAttribute("user")==null){
                response.sendRedirect("logout.jsp?error=Desconectado por inatividade.");
                return;
            }
            if(request.getParameter("duplicated")!=null){
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
            out.print("<p>Ramal "+request.getParameter("duplicated") +" já existe.</p></div>");
            //out.print("<div class=\"modal-footer\">");
            //out.print("<button class=\"btn disabled\" data-dismiss=\"modal\" aria-hidden=\"true\">Cancelar</button>");
           // out.print("<button class=\"btn btn-primary\">Apagar</button>");
           // out.print("<a href=\"DeletePeers?peer="+request.getParameter("delete")+"\" class=\"btn btn-info\">Apagar</a>");
            out.print("</div></div>");
            //session.removeAttribute("return");
            }
            if(request.getParameter("invalid")!=null){
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
            out.print("<p>Descrição "+request.getParameter("invalid") +" não pode ser usado como ramal.</p></div>");
            //out.print("<div class=\"modal-footer\">");
            //out.print("<button class=\"btn disabled\" data-dismiss=\"modal\" aria-hidden=\"true\">Cancelar</button>");
           // out.print("<button class=\"btn btn-primary\">Apagar</button>");
           // out.print("<a href=\"DeletePeers?peer="+request.getParameter("delete")+"\" class=\"btn btn-info\">Apagar</a>");
            out.print("</div></div>");
            //session.removeAttribute("return");
            }
            JpbxDB db=new JpbxDB();
        
      out.write("\n");
      out.write("    </head>\n");
      out.write("    <body>\n");
      out.write("    ");
out.print(head.navigator(session.getAttribute("lvl").toString()));
      out.write("    \n");
      out.write("    <div id=\"container\"class=\"container\">\n");
      out.write("        <form id=\"frm\" name=\"frm1\" action=\"CreatePeers\" method=\"POST\">\n");
      out.write("            <fieldset>\n");
      out.write("                <legend><h2>Novo Ramal</h2></legend>\n");
      out.write("                <div class=\"span11 table-bordered\">\n");
      out.write("                <div style=\"float: left\"><h4><u>Ramal</u> <a rel=\"tooltip\" data-placement=\"top\" \n");
      out.write("                                        title=\"Range de ramais: 1000 a 9999. Próximo ramal: \n");
      out.write("                                        ");
      out.print( db.peerSuggest() );
      out.write("\">\n");
      out.write("                        <input id=\"peer\" onblur=loadInternal(\"WebServ?peer=\"+this.value,'validation');\n");
      out.write("                               type=\"number\" name=\"peer\" /></a></h4>\n");
      out.write("                </div><div style=\"float: left\" id=\"validation\"></div>\n");
      out.write("                <div style=\"float: left\" class=\"offset1\" ><h4><u>Senha de Registro</u> \n");
      out.write("                        <a rel=\"tooltip\" data-placement=\"top\" title=\"Senha para registrar o dispositivo telefônico\">\n");
      out.write("                            <input id=\"pass\" onblur=loadInternal(\"WebServ?passwordPeer=\"+this.value,'passpeer'); \n");
      out.write("                                   type=\"password\"/></a></h4>\n");
      out.write("                    <div id=\"passpeer\"></div>\n");
      out.write("                    <input type=\"hidden\" id=\"passport\" name=\"secret\"/>\n");
      out.write("                </div>\n");
      out.write("                </div>\n");
      out.write("                <h4><u>Nome do Ramal</u> <input id=\"name\" type=\"text\" name=\"name\" /></h4>\n");
      out.write("                <div><h4><u>Senha do Ramal</u> <a rel=\"tooltip\" data-placement=\"right\" title=\"Senha para acessar facilidades do deste ramal\">\n");
      out.write("                            <input id=\"featpass\" onblur=ajustFeatPass(); type=\"number\" name=\"featsecret\" placeholder=\"4 Digitos\" /></a></h4></div>\n");
      out.write("                <h4><u>Idioma dos Audios</u></h4>\n");
      out.write("                <label class=\"radio inline\">\n");
      out.write("                    <input type=\"radio\" name=\"lang\" id=\"lang1\" value=\"pt_BR\" checked>\n");
      out.write("                    Português\n");
      out.write("                </label>\n");
      out.write("                <label class=\"radio inline\">\n");
      out.write("                    <input type=\"radio\" name=\"lang\" id=\"lang2\" value=\"en\">\n");
      out.write("                    Inglês\n");
      out.write("                </label>\n");
      out.write("                <label class=\"radio inline\">\n");
      out.write("                    <input type=\"radio\" name=\"lang\" id=\"lang3\" value=\"es\">\n");
      out.write("                    Espanhol\n");
      out.write("                </label>\n");
      out.write("                <label class=\"radio inline\">\n");
      out.write("                    <input type=\"radio\" name=\"lang\" id=\"lang4\" value=\"fr\">\n");
      out.write("                    Francês\n");
      out.write("                </label>\n");
      out.write("                \n");
      out.write("                <h4><u>Tipo de Discagem</u></h4>\n");
      out.write("                <label class=\"radio inline\">\n");
      out.write("                    <input type=\"radio\" name=\"tone\" id=\"tone1\" value=\"rfc2833\" checked>\n");
      out.write("                    rfc2833\n");
      out.write("                </label>\n");
      out.write("                <label class=\"radio inline\">\n");
      out.write("                    <input type=\"radio\" name=\"tone\" id=\"tone2\" value=\"info\">\n");
      out.write("                    Info\n");
      out.write("                </label>\n");
      out.write("                <label class=\"radio inline\">\n");
      out.write("                    <input type=\"radio\" name=\"tone\" id=\"tone3\" value=\"inband\">\n");
      out.write("                    Inband\n");
      out.write("                </label>\n");
      out.write("                \n");
      out.write("                <h4><u>Ramal Externo</u></h4>\n");
      out.write("                <label class=\"radio inline\">\n");
      out.write("                    <input type=\"radio\" name=\"nat\" id=\"nat1\" value=\"never\" checked>\n");
      out.write("                    Não\n");
      out.write("                </label>\n");
      out.write("                <label class=\"radio inline\">\n");
      out.write("                    <input type=\"radio\" name=\"nat\" id=\"nat2\" value=\"yes\">\n");
      out.write("                    Sim\n");
      out.write("                </label>\n");
      out.write("                \n");
      out.write("                <h4><u>Ativa Qualidade</u></h4>\n");
      out.write("                <label class=\"radio inline\">\n");
      out.write("                    <input type=\"radio\" name=\"qualify\" id=\"q1\" value=\"no\" checked>\n");
      out.write("                    Não\n");
      out.write("                </label>\n");
      out.write("                <label class=\"radio inline\">\n");
      out.write("                    <input type=\"radio\" name=\"qualify\" id=\"q2\" value=\"yes\">\n");
      out.write("                    Sim\n");
      out.write("                </label>\n");
      out.write("                \n");
      out.write("                <h4><u>Ativa Gravação</u></h4>\n");
      out.write("                <label class=\"radio inline\">\n");
      out.write("                    <input type=\"radio\" name=\"record\" id=\"rec1\" value=\"0\" checked>\n");
      out.write("                    Não\n");
      out.write("                </label>\n");
      out.write("                <label class=\"radio inline\">\n");
      out.write("                    <input type=\"radio\" name=\"record\" id=\"rec2\" value=\"1\">\n");
      out.write("                    Sim\n");
      out.write("                </label>\n");
      out.write("                \n");
      out.write("                <h4><u>Ordem de Codecs</u></h4>\n");
      out.write("                <select name=\"cod1\" class=\"span1\">\n");
      out.write("                    <option>alaw</option>\n");
      out.write("                    <option>ulaw</option>\n");
      out.write("                    <option>g729</option>\n");
      out.write("                    <option>gsm</option>\n");
      out.write("                </select>\n");
      out.write("                <select name=\"cod2\" class=\"span1\">\n");
      out.write("                    <option>alaw</option>\n");
      out.write("                    <option selected=\"\">ulaw</option>\n");
      out.write("                    <option>g729</option>\n");
      out.write("                    <option>gsm</option>\n");
      out.write("                </select>\n");
      out.write("                <select name=\"cod3\" class=\"span1\">\n");
      out.write("                    <option>alaw</option>\n");
      out.write("                    <option>ulaw</option>\n");
      out.write("                    <option>g729</option>\n");
      out.write("                    <option selected=\"\">gsm</option>\n");
      out.write("                </select>\n");
      out.write("                \n");
      out.write("                <h4><u>Limite de Chamadas Simultâneas</u> </h4>\n");
      out.write("                <div>\n");
      out.write("                <input class=\"span1\" type=\"text\" name=\"call-limit\" placeholder=\"ilimitado\" value=\"1\" />\n");
      out.write("                <p class=\"muted\">Por segurança limite a uma quantidade baixa</p>\n");
      out.write("                </div>\n");
      out.write("                <h4>Empresa</h4>\n");
      out.write("                ");
      out.print(db.getCompanyOptions() );
      out.write("\n");
      out.write("                <h4><u>Departamento</u> </h4>\n");
      out.write("                <select name=\"pickupgroup\">\n");
      out.write("                    ");
 
                        out.print(db.listPickupGroups(null));
                    
      out.write("\n");
      out.write("                </select>\n");
      out.write("                <h4><u>Voicemail</u> </h4>\n");
      out.write("                <label class=\"checkbox span2\">\n");
      out.write("                   <h5> <input type=\"checkbox\" id=\"voicemail\">\n");
      out.write("                    Ativar Voicemail</h5>\n");
      out.write("                </label>\n");
      out.write("                <div class=\"clearfix\"></div>\n");
      out.write("                <div id=\"mailbox\">\n");
      out.write("                    <h5>Senha de Voicemail <i class=\"muted\">(apenas números)</i></h5>                 \n");
      out.write("                    <input type=\"number\" name=\"mailboxSecret\" id=\"mailboxSecret\" />\n");
      out.write("                    <h5>Email de Contato</h5>\n");
      out.write("                    <input type=\"email\" name=\"email\" id=\"email\" onblur=\"validEmail()\" />\n");
      out.write("                </div>\n");
      out.write("                </br>\n");
      out.write("                <input type=\"button\" onclick=\"verifyFrm();\" value=\"Salvar\" class=\"btn btn-info\" />\n");
      out.write("                <img id=\"load\" src=\"css/bootstrap/img/loading.gif\"/>\n");
      out.write("                <a class=\"btn btn-info offset1\" href=\"listPeers.jsp\">Voltar</a>\n");
      out.write("            </fieldset>\n");
      out.write("        </form>\n");
      out.write("        \n");
      out.write("    </div>\n");
      out.write("     <script>\n");
      out.write("         $('#load').hide();\n");
      out.write("         function verifyFrm(){\n");
      out.write("             if($('#peer').val()===''){\n");
      out.write("                 informer('<p style=\"color: #ff0000\">Preencha o campo RAMAL!</p>');\n");
      out.write("                // $('#peer').focus();\n");
      out.write("             }\n");
      out.write("             else if($('#pass').val()===''){\n");
      out.write("                 informer('<p style=\"color: #ff0000\">Preencha o campo SENHA!</p>');\n");
      out.write("                 $('#pass').focus();\n");
      out.write("             }\n");
      out.write("             else if($('#name').val()===''){\n");
      out.write("                 informer('<p style=\"color: #ff0000\">Preencha o campo NOME DO RAMAL!</p>');\n");
      out.write("                 $('#name').focus();\n");
      out.write("             }\n");
      out.write("             else{\n");
      out.write("                 $('#load').show();\n");
      out.write("                 $('#passport').val(hashMD5($('#peer').val()+':asterisk:'+$('#pass').val()));\n");
      out.write("                 $('#pass').val('');\n");
      out.write("                 setTimeout(\"submit();\",500);\n");
      out.write("             }\n");
      out.write("        }\n");
      out.write("        $('#voicemail').click(function(){\n");
      out.write("            $('#mailbox').toggle('slow');\n");
      out.write("        });\n");
      out.write("        function submit(){\n");
      out.write("            $('#frm').submit();\n");
      out.write("        }\n");
      out.write("            time=");
      out.print( session.getMaxInactiveInterval() );
      out.write(";\n");
      out.write("            user=\"");
      out.print( session.getAttribute("user") );
      out.write("\";\n");
      out.write("            function startCountDown(){\n");
      out.write("                if(time>0){\n");
      out.write("                    $(\"#cron\").html(\"Olá \"+user+\"</br>\"+countDown(time));\n");
      out.write("                    time-=1;\n");
      out.write("                    setTimeout(\"startCountDown();\", 1000);\n");
      out.write("                }else{\n");
      out.write("                    $(\"#cron\").html(\"Sessão Expirada!\");\n");
      out.write("                }\n");
      out.write("            }\n");
      out.write("            function ajustFeatPass(){\n");
      out.write("                if($('#featpass').val().length>4)\n");
      out.write("                    $('#featpass').val($('#featpass').val().substring(0,4));\n");
      out.write("            }\n");
      out.write("            $(function(){\n");
      out.write("                $('a').tooltip('hide');\n");
      out.write("                startCountDown();\n");
      out.write("                $('#pass').val('');\n");
      out.write("                $('#peer').val('');\n");
      out.write("                $('#mailbox').hide();\n");
      out.write("            });\n");
      out.write("        </script>\n");
      out.write("        <div id=\"alert\"></div>\n");
      out.write("    </body>\n");
      out.write("</html>\n");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          out.clearBuffer();
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
