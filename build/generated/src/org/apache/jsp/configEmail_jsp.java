package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.jpbx.Controller;

public final class configEmail_jsp extends org.apache.jasper.runtime.HttpJspBase
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
      out.write("<!DOCTYPE html>\n");
      out.write("<html>\n");
      out.write("    <head>\n");
      out.write("        \n");
      out.write("        ");

            Controller head=new Controller();
            out.print(head.header("JPBX->Configs->Email"));
            
            if(session.getAttribute("user")==null){
                response.sendRedirect("logout.jsp?error=Desconectado por inatividade.");
                return;
            }
        
      out.write("\n");
      out.write("    </head>\n");
      out.write("    <body>\n");
      out.write("        ");
out.print(head.navigator(session.getAttribute("lvl").toString()));
      out.write("\n");
      out.write("    \n");
      out.write("    <div id=\"container\" class=\"container table-bordered\">\n");
      out.write("        <form id=\"frm\" name=\"frm1\" action=\"CreateEmail\" method=\"POST\">\n");
      out.write("            <fieldset>\n");
      out.write("                <legend><h3>Servidor de Envio de Email</h3></legend>\n");
      out.write("                <h4>Email</h4>\n");
      out.write("                <div style=\"float: left\">\n");
      out.write("                    <input id=\"email\" type=\"text\" name=\"email\" onblur=\"validEmail()\"/>\n");
      out.write("                </div>\n");
      out.write("                <div style=\"float: left\" id=\"return\"></div>\n");
      out.write("                <div class=\"clearfix\"></div>\n");
      out.write("                <h4>Senha</h4>\n");
      out.write("                <input id=\"pass\" type=\"password\" name=\"pass\"/>\n");
      out.write("                <div class=\"clearfix\"></div>\n");
      out.write("                <h4>Servidor SMTP</h4>\n");
      out.write("                <input id=\"smtp\" type=\"text\" name=\"smtp\"/>\n");
      out.write("                <div class=\"clearfix\"></div>\n");
      out.write("                <h4>Porta SMTP</h4>\n");
      out.write("                <input id=\"port\" type=\"number\" name=\"port\"/>\n");
      out.write("                <div class=\"clearfix\"></div>\n");
      out.write("                <h4>Seguranças</h4> \n");
      out.write("                \n");
      out.write("                <div class=\"clearfix\"></div>\n");
      out.write("                <input type=\"button\" onclick=\"verifyFrm();\" value=\"Salvar\" class=\"btn btn-info\" />\n");
      out.write("            </fieldset>\n");
      out.write("        </form>\n");
      out.write("    </div>\n");
      out.write("    <script>\n");
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
      out.write("            function verifyFrm(){\n");
      out.write("                \n");
      out.write("            }\n");
      out.write("            $(function(){\n");
      out.write("                startCountDown();\n");
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
