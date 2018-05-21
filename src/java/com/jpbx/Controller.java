/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jpbx;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.TimeoutException;

/**
 *
 * @author jefaokpta
 */
public class Controller extends HttpServlet {
    
    

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        JpbxDB db=new JpbxDB();
                 /* TODO output your page here. You may use following sample code. */
        if(request.getParameter("modifyOrder")!=null){
            String rule[]=db.getRuleName(request.getParameter("modifyOrder"));
            String exten=rule[2];
            int rulePos=Integer.parseInt(rule[1]),qtdRule=Integer.parseInt(rule[3]);
            String res=db.setPriorityDialplans(Integer.parseInt(request.getParameter("modifyOrder")),exten, rulePos, qtdRule);
            if(res.equals("ok"))
                response.sendRedirect("listDialPlan.jsp");
            else
                out.print(res);
            return;
        }
        if(request.getParameter("user")!=null&&request.getSession().getAttribute("token")!=null){
            if(request.getSession().getAttribute("token").equals(getToken())){
                String pass=db.login(request.getParameter("user"));
                if(!pass.equals("")){
                    if(request.getParameter("pass").equals(pass)){
                        String ls_str,res="";
                        Process ls_proc=Runtime.getRuntime().exec("ps aux");
                        // get its output (your input) stream
                        DataInputStream ls_in = new DataInputStream(ls_proc.getInputStream());
                        while ((ls_str = ls_in.readLine()) != null) {
                                res+=ls_str;
                        }
                        //if(true){
                        if(res.contains("/usr/sbin/asterisk")){
                            request.getSession().setMaxInactiveInterval(db.getSessionTimeout(request.getParameter("user"))*60);
                            request.getSession().setAttribute("user", request.getParameter("user"));
                            request.getSession().setAttribute("lvl", db.getLvl(request.getParameter("user")));
                            if(!db.verifyBackup().equals("ok"))
                                request.getSession().setAttribute("bkp", "start");
                            response.sendRedirect("home.jsp");
                        }
                        else{
                            response.sendRedirect("logout.jsp?error=Identificado falha com Asterisk.<br>Verifique seu funcionamento.");
                            return;
                        }
                    }
                    else{
                        response.sendRedirect("logout.jsp?error=Usuario ou Senha Invalidos.");
                        return;
                    }
                }
                else{
                    response.sendRedirect("logout.jsp?error=Usuario ou Senha Invalidos.");
                    return;
                }
            }
            else{
                response.sendRedirect("logout.jsp?error=Envio de dados fora da validade.<br>Por favor tente novamente.");
                return;
            }
        }
        else{
            response.sendRedirect("logout.jsp?error=Identificado Usuario mal intencionado!");
            return;
        }
        
    }
    public String peersRegistered(){
        String res = "ok";
        File makefile=new File("/tmp/registered");
        try {   
            Asterisk ast=new Asterisk();
            FileWriter fwrite=new FileWriter(makefile);
            ast.AstConnect();
            List peers=ast.getInfos("sip show peers");
            ast.AstDisconnect();
            for(int i=0;i<peers.size();i++){
                fwrite.write(peers.get(i).toString()+"\n");
            }
            fwrite.flush();
            fwrite.close();
        }catch(IOException ex){
            res=ex.getMessage();
        } catch (AuthenticationFailedException ex) {
           res=ex.getMessage();
        } catch (TimeoutException ex) {
            res=ex.getMessage();
        }
        return res;
    }
    public String header(String head){
        String res="<meta charset=\"utf-8\">\n" +
"    <link rel=\"icon\" href=\"favicon.ico\" type=\"image/x-icon\" />"+
"    <link rel=\"shortcut icon\" href=\"favicon.ico\" type=\"image/x-icon\" />"+
"    <title>"+head+"</title>\n" +
"    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
"    <meta name=\"description\" content=\"\">\n" +
"    <meta name=\"author\" content=\"\">\n" +
"\n" +
"    <!-- Le styles -->\n" +
"    <link href=\"css/bootstrap/css/bootstrap.css\" rel=\"stylesheet\" type=\"text/css\">\n" +
"    <link href=\"css/bootstrap/css/bootstrap-responsive.css\" rel=\"stylesheet\" type=\"text/css\">\n"+
"    <link rel=\"stylesheet\" type=\"text/css\" href=\"css/cssJefao.css\"/>\n" +
"    <script type=\"text/javascript\" src=\"css/jquery.js\"></script>\n" +
"    <script src=\"css/functionsJefao.js\"></script>\n" +
"    <script type=\"text/javascript\" src=\"css/bootstrap/js/bootstrap.js\"></script>\n" +
"<script src=\"css/webserv.js\"></script>";
        return res;
    }
    public String navigator(String lvl){
        String res;
        if(lvl.equals("Administrador"))
            res="\n" +
"\n" +
"      <div class=\"navbar navbar-inverse navbar-fixed-top\" role=\"navigation\">\n" +
"        <div class=\"navbar-inner\">\n" +
"          <!-- Responsive Navbar Part 1: Button for triggering responsive navbar (not covered in tutorial). Include responsive CSS to utilize. -->\n" +
"          <a class=\"btn btn-navbar\" >\n" +
"           <select class=\"span1\" id=\"selNavBar\">"
                    + "<option value=\"#\">Cadastros</option>"
                    + "<option value=\"listPeers.jsp\">- Ramais</option>"
                    + "<option value=\"listTrunks.jsp\">- Troncos</option>"
                    + "<option value=\"listPickupGrps.jsp\">- Captura</option>"
                    + "<option value=\"listCallGrps.jsp\">- Chamada</option>"
                    + "<option value=\"listUsers.jsp\">- Usuários</option>"
                    + "<option value=\"listCompanys.jsp\">- Empresas</option>"
                    + "<option value=\"#\">Relatórios</option>"
                    + "<option value=\"relCalls.jsp\">- Ligações</option>"
                    + "<option value=\"relConferences.jsp\">- Conferências</option>"
                    + "<option value=\"relFaxes.jsp\">- Faxes</option>"
                    + "<option value=\"#\">Controles</option>"
                    + "<option value=\"listDialPlan.jsp\">- Regras</option>"
                    + "<option value=\"listCosts.jsp\">- Custos</option>"
                    + "<option value=\"cos.jsp\">- Classes</option>"
                    + "<option value=\"listAlias.jsp\">- Alias</option>"
                    + "<option value=\"#\">Ferramentas</option>"
                    + "<option value=\"cli.jsp\">- Console</option>"
                    + "<option value=\"listBackups.jsp\">- Backups</option>"
                    + "<option value=\"sendFax.jsp\">- Fax</option>"
                    + "<option value=\"#\">Configurações</option>"
                    + "<option value=\"configs.jsp\">- Sistema</option>"
                    + "<option value=\"moh.jsp\">- Espera</option>"
                    + "<option value=\"configEmail.jsp\">- Email</option>"
                    + "<option value=\"dependencies.jsp\">- Dependências</option>"
                    + "<option value=\"logout.jsp?error=logout\">Sair</option>"
                    + "</select>\n" +
"          </a>\n" +
"          <a href=\"home.jsp\" class=\"brand\"><strong>JPBX</strong></a>\n" +
"          <div class=\"nav-collapse collapse\">\n" +
"            <ul class=\"nav navbar-nav navbar-right\">\n" +
"              <li class=\"dropdown\">\n" +
"                <a href=\"#\" class=\"dropdown-toggle\" data-toggle=\"dropdown\">Cadastros <b class=\"caret\"></b></a>\n" +
"                <ul class=\"dropdown-menu\">\n" +
"                  <li><a href=\"listPeers.jsp\">Ramais</a></li>\n" +
"                  <li><a href=\"listTrunks.jsp\">Troncos</a></li>\n" +
"                  <li><a href=\"listPickupGrps.jsp\">Grupos de Captura</a></li>\n"+
"                   <li><a href=\"listCallGrps.jsp\">Grupos de Chamada</a></li>\n" +
"                  <li><a href=\"listUsers.jsp\">Usuários</a></li>"
                    + "<li><a href=\"listCompanys.jsp\">Empresas</a></li>\n" +
"                </ul>\n" +
"              </li>\n" +
"              <li class=\"dropdown\">\n" +
"                   <a href=\"#\" class=\"dropdown-toggle\" data-toggle=\"dropdown\">Relatórios <b class=\"caret\"></b></a>\n" +
"                   <ul class=\"dropdown-menu\">\n" +
"                       <li><a href=\"relCalls.jsp\">Ligações</a></li>\n" +
"                       <li><a href=\"relConferences.jsp\">Conferências</a></li>\n"
                    + " <li><a href=\"relFaxes.jsp\">Faxes</a></li>" +                    
"                   </ul>\n"+
"             </li>\n"+
"              <li class=\"dropdown\">\n" +
"                   <a href=\"#\" class=\"dropdown-toggle\" data-toggle=\"dropdown\">Controles <b class=\"caret\"></b></a>\n" +
"                   <ul class=\"dropdown-menu\">\n" +
"                       <li><a href=\"listDialPlan.jsp\">Regras de Discagem</a></li>\n" +
"                       <li><a href=\"listCosts.jsp\">Centro de Custos</a></li>\n"
                    + "<li><a href=\"cos.jsp\">Classes de Serviço</a></li>\n"
                    + "<li><a href=\"listAlias.jsp\">Alias de Expressão</a></li>\n" +                
"                   </ul>\n"+
"             </li>\n"
                + "<li class=\"dropdown\">"+
"                   <a href=\"#\" class=\"dropdown-toggle\" data-toggle=\"dropdown\">Ferramentas <b class=\"caret\"></b></a>\n"
                + "<ul class=\"dropdown-menu\">\n" +
"                       <li><a href=\"cli.jsp\">Console Asterisk</a></li>\n" +
"                       <li><a href=\"listBackups.jsp\">Backups</a></li>\n"
                    + " <li><a href=\"sendFax.jsp\">Enviar Fax</a></li>\n" +
"                   </ul>"
                + "</li>" +
                "<li class=\"dropdown\">"+
"                   <a href=\"#\" class=\"dropdown-toggle\" data-toggle=\"dropdown\">Configurações <b class=\"caret\"></b></a>\n"
                + "<ul class=\"dropdown-menu\">\n" +
"                       <li><a href=\"configs.jsp\">Configurações do Sistema</a></li>\n" +
"                       <li><a href=\"moh.jsp\">Musicas de Espera</a></li>\n"
                    + " <li><a href=\"configEmail.jsp\">Servidor de Email</a></li>\n"
                    + " <li><a href=\"dependencies.jsp\">Dependências do Sistema</a></li>" +
"                   </ul>"
                + "</li>" +
"              <li><a href=\"logout.jsp?error=logout\"><i class=\"icon-off icon-white\"></i></a></li>\n" +
"               </ul>  \n" +
"              <ul class=\"nav pull-right\">\n" +
"                  <li class=\"span2\">\n" +
"                      <div id=\"cron\"></div>    \n" +
"                  </li>\n" +
"                  <li>\n" +
"                      <div>Developed by <br><a style=\"color: gray\" target=\"blank\" href=\"http://www.linkedin.com/pub/jefferson-alves-reis/61/73/0\">Jefaokpta</a></div>\n" +
"                  </li>\n" +
"              </ul>\n" +
"          </div><!--/.nav-collapse -->\n" +
"        </div><!-- /.navbar-inner-->\n" +
"      </div><!-- /.navbar -->\n" +
"    <!-- /.wrapper -->"
                    + "<script>"
                    + "$('#selNavBar').change(function(){\n" +
                        "location.href=$('#selNavBar option:selected').val();" +
                        "});"
                    + "</script>";
        else
            res="<div class=\"navbar-wrapper\">\n" +
"\n" +
"      <div class=\"navbar navbar-inverse navbar-fixed-top\">\n" +
"        <div class=\"navbar-inner\">\n" +
"          <!-- Responsive Navbar Part 1: Button for triggering responsive navbar (not covered in tutorial). Include responsive CSS to utilize. -->\n" +
"          <a class=\"btn btn-navbar\">\n" +
"            <select class=\"span1\" id=\"selNavBar\">"
                    + "<option value=\"#\">Relatórios</option>"
                    + "<option value=\"relCalls.jsp\">- Ligações</option>"
                    + "<option value=\"relConferences.jsp\">- Conferências</option>"
                    + "<option value=\"relFaxes.jsp\">- Faxes</option>"
                    + "<option value=\"#\">Ferramentas</option>"
                    + "<option value=\"sendFax.jsp\">- Envia Fax</option>"
                    + "<option value=\"logout.jsp?error=logout\">Sair</option>"
                    + "</select>" +
"          </a>\n" +
"          <a href=\"home.jsp\" class=\"brand\"><strong>JPBX</strong></a>\n" +
"          <div class=\"nav-collapse collapse\">\n" +
"            <ul class=\"nav\">\n" +
"              <li class=\"dropdown\">\n" +
"                   <a href=\"#\" class=\"dropdown-toggle\" data-toggle=\"dropdown\">Relatórios</a>\n" +
"                   <ul class=\"dropdown-menu\">\n" +
"                       <li><a href=\"relCalls.jsp\">Ligações</a></li>\n" + 
"                       <li><a href=\"relConferences.jsp\">Conferências</a></li>\n"
                    + " <li><a href=\"relFaxes.jsp\">Faxes</a></li>" +                    
"                   </ul>\n"+
"             </li>\n"
                    + "<li class=\"dropdown\">"+
"                   <a href=\"#\" class=\"dropdown-toggle\" data-toggle=\"dropdown\">Ferramentas <b class=\"caret\"></b></a>\n"
                + "<ul class=\"dropdown-menu\">\n"
                    + " <li><a href=\"sendFax.jsp\">Enviar Fax</a></li>\n" +
"                   </ul>"
                + "</li>"+
"              <li><a href=\"logout.jsp?error=logout\"><i class=\"icon-off icon-white\"></i></a></li>\n" +
"               </ul>  \n" +
"              <ul class=\"nav pull-right\">\n" +
"                  <li class=\"span2\">\n" +
"                      <div id=\"cron\"></div>    \n" +
"                  </li>\n" +
"                  <li>\n" +
"                      <div>Developed by <br><a style=\"color: gray\" target=\"blank\" href=\"http://www.linkedin.com/pub/jefferson-alves-reis/61/73/0\">Jefaokpta</a></div>\n" +
"                  </li>\n" +
"              </ul>\n" +
"          </div><!--/.nav-collapse -->\n" +
"        </div><!-- /.navbar-inner -->\n" +
"      </div><!-- /.navbar -->\n" +
"    </div><!-- /.container -->"
                    + "<script>"
                    + "$('#selNavBar').change(function(){\n" +
                        "location.href=$('#selNavBar option:selected').val();" +
                        "});"
                    + "</script>";
        return res;
    }
    public String md5(String senha){  
        String sen = "";  
        MessageDigest md = null;  
        try {  
            md = MessageDigest.getInstance("MD5");  
        } catch (NoSuchAlgorithmException ex) {  
            sen=ex.getMessage();
        }  
        BigInteger hash = new BigInteger(1, md.digest(senha.getBytes()));  
        sen = hash.toString(16);              
        String zeros="";
        if(sen.length()!=32){ //ajustar os 0 (zeros) nos MD5 q iniciam com 0
            int c=32-sen.length();         
            for(int i=0;i<c;i++)
                zeros+="0";
        }
        return zeros+sen; 
    }
    public String getToken(){
        GregorianCalendar now=new GregorianCalendar();
        now.setTime(new Date());
        SimpleDateFormat sdf=new SimpleDateFormat("MM-dd_HH:mm");
        return md5(sdf.format(now.getTime()));
    }
    public String dependencies(String app){
        String res="ok";
        String[] cmd = {
            "/bin/bash",
            "-c",
            "echo $?"
        };
        try {
                Runtime.getRuntime().exec(app);
                Process ls_proc=Runtime.getRuntime().exec(cmd);
                DataInputStream ls_in = new DataInputStream(
                                              ls_proc.getInputStream());

                    // get its output (your input) stream
                ls_in = new DataInputStream(ls_proc.getInputStream());
                String ls_str;

                if ((ls_str = ls_in.readLine()) != null) {
                     res=ls_str;
                }
            } catch (IOException ex) {
                res=ex.getMessage();
            }
        return res;
    }
    public String depCommand(String cmd){
        String res="ok";
        try {
                Process ls_proc=Runtime.getRuntime().exec(cmd);
                DataInputStream ls_in = new DataInputStream(
                                              ls_proc.getInputStream());

                    // get its output (your input) stream
                ls_in = new DataInputStream(ls_proc.getInputStream());
                String ls_str;

                if ((ls_str = ls_in.readLine()) != null) {
                     res=ls_str;
                }
            } catch (IOException ex) {
                res=ex.getMessage();
            }
        return res;
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
