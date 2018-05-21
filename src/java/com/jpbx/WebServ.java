/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jpbx;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class WebServ extends HttpServlet {

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
        
        if(request.getParameter("trunk_name")!=null){
            JpbxDB db=new JpbxDB();
            String name=request.getParameter("trunk_name");
            name=db.replaceNames(name);
            int res=Integer.parseInt(db.seekTrunkName(name));
            if(res>0){
                out.print("<script>"+
                        "document.form1.nameTrunk.value='';"+
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
                out.print("<p>O nome <b>"+request.getParameter("trunk_name") +"</b> está sendo usado por outro tronco.</p>"
                        + "<p>Por favor escolha outro nome.</p></div>");
                //out.print("<div class=\"modal-footer\">");
                //out.print("<button class=\"btn disabled\" data-dismiss=\"modal\" aria-hidden=\"true\">Cancelar</button>");
               // out.print("<button class=\"btn btn-primary\">Apagar</button>");
               // out.print("<a href=\"DeletePeers?peer="+request.getParameter("delete")+"\" class=\"btn btn-info\">Apagar</a>");
                out.print("</div></div>");
                }
               // out.print("<script>"
                 //   + "alert(\""+request.getParameter("trunk_name")+"\");"
                   // + "document.getElementById('nameTrunk').focus();"
                   // + "</script>");
            else
                out.print("<img src=\"css/bootstrap/img/checked.png\"/>"
                        + "<script>"
                        + "document.form1.nameTrunk.value='"+name+"';"
                        + "</script>");
        }
        if(request.getParameter("dialplan_name")!=null&&request.getParameter("dialplan_name")!=""){
            JpbxDB db=new JpbxDB();
            String name=db.replaceNamesDialPlan(request.getParameter("dialplan_name"));
            int res=Integer.parseInt(db.seekDialPlanName(name));
            if(res>0){
                out.print("<script>"+
                        "$('#rulename').val('');"+
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
                out.print("<p>O nome <b>"+name +"</b> está sendo usado por outra regra.</p>"
                        + "<p>Por favor escolha outro nome.</p></div>");
                //out.print("<div class=\"modal-footer\">");
                //out.print("<button class=\"btn disabled\" data-dismiss=\"modal\" aria-hidden=\"true\">Cancelar</button>");
               // out.print("<button class=\"btn btn-primary\">Apagar</button>");
               // out.print("<a href=\"DeletePeers?peer="+request.getParameter("delete")+"\" class=\"btn btn-info\">Apagar</a>");
                out.print("</div></div>");
                }
            else
                out.print("<img src=\"css/bootstrap/img/checked.png\"/>"
                        + "<script>"
                        + "$('#rulename').val('"+name+"');"
                        + "</script>");
        }
        if(request.getParameter("pickupGrp")!=null&&!request.getParameter("pickupGrp").equals("")){
             JpbxDB db=new JpbxDB();
            String name=db.replaceNamesDialPlan(request.getParameter("pickupGrp"));
            int res=Integer.parseInt(db.seekPickupGrpName(name));
            if(res>0){
                out.print("<p class=\"text-error\">O nome de grupo <b>"+name+"</b> já existe!</p>"+
                         "<script>"+
                        "$('#grpname').val('');"+                  
                "</script>");
                }
            else
                out.print("<img src=\"css/bootstrap/img/checked.png\"/>"
                        + "<script>"
                        + "$('#grpname').val('"+name+"');"
                        + "</script>");
        }
        if(request.getParameter("callGrp")!=null&&!request.getParameter("callGrp").equals("")){
             JpbxDB db=new JpbxDB();
            String name=db.replaceNamesDialPlan(request.getParameter("callGrp"));
            int res=Integer.parseInt(db.seekCallGrpName(name));
            if(res>0){
                out.print("<p class=\"text-error\">O nome de grupo <b>"+name+"</b> já existe!</p>"+
                         "<script>"+
                        "$('#grpname').val('');"+                  
                "</script>");
                }
            else
                out.print("<img src=\"css/bootstrap/img/checked.png\"/>"
                        + "<script>"
                        + "$('#grpname').val('"+name+"');"
                        + "</script>");
        }
        if(request.getParameter("cosName")!=null&&!request.getParameter("cosName").equals("")){
             JpbxDB db=new JpbxDB();
            String name=request.getParameter("cosName");
            int res=Integer.parseInt(db.seekCosName(name));
            if(res>0){
                out.print("<p class=\"text-error\">A Classe de Serviço <b>"+name+"</b> já existe!</p>"+
                         "<script>"+
                        "$('#cosName').val('');"+                  
                "</script>");
                }
            else
                out.print("<img src=\"css/bootstrap/img/checked.png\"/>"
                        + "<script>"
                        + "$('#cosName').val('"+name+"');"
                        + "</script>");
        }
        if(request.getParameter("usr")!=null&&!request.getParameter("usr").equals("")){
             JpbxDB db=new JpbxDB();
            String name=db.replaceNamesDialPlan(request.getParameter("usr"));
            int res=Integer.parseInt(db.seekUserName(name));
            if(res>0){
                out.print("<p class=\"text-error\">O usuário <b>"+name+"</b> já existe!</p>"+
                         "<script>"+
                        "$('#user').val('');"+                  
                "</script>");
                }
            else
                out.print("<img src=\"css/bootstrap/img/checked.png\"/>"
                        + "<script>"
                        + "$('#user').val('"+name+"');"
                        + "</script>");
        }
        if(request.getParameter("company")!=null&&!request.getParameter("company").equals("")){
             JpbxDB db=new JpbxDB();
            String name=db.replaceNamesDialPlan(request.getParameter("company"));
            int res=Integer.parseInt(db.seekCompanyName(name));
            if(res>0){
                out.print("<p class=\"text-error\">A Empresa <b>"+name+"</b> já existe!</p>"+
                         "<script>"+
                        "$('#name').val('');"+                  
                "</script>");
                }
            else
                out.print("<img src=\"css/bootstrap/img/checked.png\"/>"
                        + "<script>"
                        + "$('#name').val('"+name+"');"
                        + "</script>");
        }
        if(request.getParameter("alias")!=null&&!request.getParameter("alias").equals("")){
             JpbxDB db=new JpbxDB();
            String name=request.getParameter("alias");
            int res=Integer.parseInt(db.seekAliasName(name));
            if(res>0){
                out.print("<p class=\"text-error\">O Alias <b>"+name+"</b> já existe!</p>"+
                         "<script>"+
                        "$('#name').val('');"+                  
                "</script>");
                }
            else
                out.print("<img src=\"css/bootstrap/img/checked.png\"/>"
                        + "<script>"
                        + "$('#name').val('"+name+"');"
                        + "</script>");
        }
        if(request.getParameter("dialplan_exten")!=null&&request.getParameter("dialplan_exten")!=""){
            JpbxDB db=new JpbxDB();
            String fdb=db.seekDialPlanExten(request.getParameter("dialplan_exten"));
            if(fdb.equals("ok"))
                out.print("<img src=\"css/bootstrap/img/checked.png\"/>");
            else
                out.print("<p class=\"text-info\">Esta expressão está sendo usada na <b>"+fdb+"</b>.</br>"
                        + "Fique atento! Procure não criar <b>Origens, Destinos e Períodos</b> iguais.</p>");
        }
        if(request.getParameter("peer")!=null){
            try{
                int peer=Integer.parseInt(request.getParameter("peer").trim());
                JpbxDB db=new JpbxDB();
                if(peer<10||peer>9999){
                    out.print("<script>"+
                        "document.frm1.peer.value='';"+
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
                out.print("<p>Ramal deve ter no mínimo 2 e no máximo 4 digitos!</p>"
                        + "<p>Por favor escolha outro.</p></div>");
                //out.print("<div class=\"modal-footer\">");
                //out.print("<button class=\"btn disabled\" data-dismiss=\"modal\" aria-hidden=\"true\">Cancelar</button>");
               // out.print("<button class=\"btn btn-primary\">Apagar</button>");
               // out.print("<a href=\"DeletePeers?peer="+request.getParameter("delete")+"\" class=\"btn btn-info\">Apagar</a>");
                out.print("</div></div>");
                }
                else if(db.testDuplicate(request.getParameter("peer"))){
                    out.print("<img src=\"css/bootstrap/img/checked.png\"/>");
                }
                else{
                    out.print("<script>"+
                        "document.frm1.peer.value='';"+
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
                out.print("<p>Ramal <b>"+request.getParameter("peer") +"</b> já existe.</p>"
                        + "<p>Por favor escolha outro.</p></div>");
                //out.print("<div class=\"modal-footer\">");
                //out.print("<button class=\"btn disabled\" data-dismiss=\"modal\" aria-hidden=\"true\">Cancelar</button>");
               // out.print("<button class=\"btn btn-primary\">Apagar</button>");
               // out.print("<a href=\"DeletePeers?peer="+request.getParameter("delete")+"\" class=\"btn btn-info\">Apagar</a>");
                out.print("</div></div>");
                }
           }catch(NumberFormatException ex){
                out.print("<script>"+
                        "document.frm1.peer.value='';"+
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
                out.print("<p>No campo <b>Ramal</b> insira somente numerais.</p>"
                        + "<p><b>"+request.getParameter("peer") +"</b> Não parece ser numeral.</p></div>");
                //out.print("<div class=\"modal-footer\">");
                //out.print("<button class=\"btn disabled\" data-dismiss=\"modal\" aria-hidden=\"true\">Cancelar</button>");
               // out.print("<button class=\"btn btn-primary\">Apagar</button>");
               // out.print("<a href=\"DeletePeers?peer="+request.getParameter("delete")+"\" class=\"btn btn-info\">Apagar</a>");
                out.print("</div></div>");
             }
            
        }
        if(request.getParameter("passwordPeer")!=null){
            String pass=request.getParameter("passwordPeer"),res="";
            if(pass.length()>5){
                int test=0,letter=0,number=0;
                for(int i=1;i<=pass.length();i++){
                    if(pass.substring((i-1),(i-1)).equals("0")||
                            pass.substring((i-1),i).equals("1")||
                            pass.substring((i-1),i).equals("2")||
                            pass.substring((i-1),i).equals("3")||
                            pass.substring((i-1),i).equals("4")||
                            pass.substring((i-1),i).equals("5")||
                            pass.substring((i-1),i).equals("6")||
                            pass.substring((i-1),i).equals("7")||
                            pass.substring((i-1),i).equals("8")||
                            pass.substring((i-1),i).equals("9"))
                        number++;
                    else
                        letter++;
                    res+=pass.substring((i-1), i)+" ";
                }
                if(letter<2||number<2)
                    out.print("<script>"
                        + "$('#pass').val('');"
                        + "</script>"
                        + "<p class=\"text-error\">Por segurança a senha deve conter no mínimo 2 letras e 2 números.</p>");
            }
            else{
                out.print("<script>"
                        + "$('#pass').val('');"
                        + "</script>"
                        + "<p class=\"text-error\">A Senha de Registro deve ter ao menos 6 caracteres.</p>");
            }
        }
        if(request.getParameter("cpuInfo")!=null){
           String ls_str;
            float load=0;
            try {
                Process ls_proc=Runtime.getRuntime().exec("ps aux");
                DataInputStream ls_in = new DataInputStream(
                                              ls_proc.getInputStream());

                    // get its output (your input) stream
                    ls_in = new DataInputStream(ls_proc.getInputStream());
                    int i=0;

                    while ((ls_str = ls_in.readLine()) != null) {
                       // list.add(ls_str);
                        if(i>0)
                            load+=Float.parseFloat(ls_str.substring(14,19).replaceAll(" ", ""));
                        i++;
                    }
                    load=load/Integer.parseInt(request.getParameter("cpuInfo"));
            } catch (IOException ex) {
                        Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
            out.print("<script>cpu="+String.valueOf(Math.round(load))+"</script>");
            // out.print("<input type=\"text\" id=\"cpuInfo\" value=\""+String.valueOf(Math.round(load))+"\" />");
        }
        if(request.getParameter("ramInfo")!=null){
            String ls_str;
            List<String> list=new ArrayList<String>();
                Process ls_proc=Runtime.getRuntime().exec("free -m");
                DataInputStream ls_in = new DataInputStream(
                                              ls_proc.getInputStream());
                while ((ls_str=ls_in.readLine()) != null) {
                   list.add(ls_str);
		}
                String ram=(String)list.get(1),cache=(String)list.get(2),swap=(String)list.get(3);
                ram=ram.substring(ram.indexOf(":")+1).trim();
                cache=cache.substring(cache.indexOf(":")+1).trim();
                cache=cache.substring(0, 8).trim();
                float swapVal,ramVal=(Float.parseFloat(ram.substring(8,16))-Float.parseFloat(cache))*100;
                ramVal=ramVal/Float.parseFloat(ram.substring(0, 7));
                swap=swap.substring(swap.indexOf(":")+1).trim();
                swapVal=Float.parseFloat(swap.substring(8,16))*100;
                swapVal=swapVal/Float.parseFloat(swap.substring(0, 7));
                
                out.print("<script>ram="+String.valueOf(Math.round(ramVal))+";"
                        + "swap="+String.valueOf(Math.round(swapVal))+";</script>");
        }
        if(request.getParameter("verifySrcDst")!=null){
            JpbxDB db=new JpbxDB();
            if(request.getParameter("src").equals("Qualquer"))
                out.print("<input id=\"returnVerifySrcDst\" type=\"hidden\" "
                    + "value=\""+db.seekSrcDst(request.getParameter("verifySrcDst"), "Ramal --> Todos")+"\" />");
            else
                out.print("<input id=\"returnVerifySrcDst\" type=\"hidden\" "
                    + "value=\""+db.seekSrcDst(request.getParameter("verifySrcDst"), request.getParameter("src"))+"\" />");
        }
        if(request.getParameter("dialplan")!=null){
            if(request.getParameter("app").equals("Toca Audio")){
                String dialplan=request.getParameter("dialplan");
                JpbxDB db=new JpbxDB();
                out.print("<h5>Audio para Reprodução</h5>"
                        + "<input type = \"hidden\" id = \"app"+dialplan+"\" value = \"PlayBacks\" />"
                        + "<select id=\"appp"+dialplan+"\">"
                        + db.listMohsDialPlan()
                        + "</select>");                       
                        //+ "<input type=\"text\" id=\"appp"+dialplan+"\" />");
            }
            else if(request.getParameter("app").equals("Atende Canal")){
                String dialplan=request.getParameter("dialplan");
                out.print("<h5>Envia sinal de atendimento a ligação em andamento</h5>"
                        + "<h6>(Sem parâmetros)</h6>"
                        + "<input type = \"hidden\" id = \"app"+dialplan+"\" value = \"Answer\" />"                       
                        + "<input type=\"hidden\" id=\"appp"+dialplan+"\" value=\"\" />");
            }
            else if(request.getParameter("app").equals("Desliga Canal")){
                String dialplan=request.getParameter("dialplan");
                out.print("<h5>Envia sinal de desligamento a ligação em andamento</h5>"
                        + "<h6>(Sem parâmetros)</h6>"
                        + "<input type = \"hidden\" id = \"app"+dialplan+"\" value = \"Hangup\" />"                       
                        + "<input type=\"hidden\" id=\"appp"+dialplan+"\" value=\"\" />");
            }
            else if(request.getParameter("app").equals("Disca Ramal")){
                String dialplan=request.getParameter("dialplan");
                out.print("<h5>Envia a ligação para o ramal definido ou discado</h5>"
                        + "<h6>(Deixe em branco para chamar o ramal discado)</h6>"
                        + "<input type = \"hidden\" id = \"app"+dialplan+"\" value = \"DialPeer\" />"                       
                        + "<input class=\"span2\" type=\"number\" id=\"appp"+dialplan+"\" placeholder=\"Ramal Discado\" value=\"\" />"
                        + "<h5>Tempo limite para atendimento (segundos)</h5>"
                        + "<input style=\"float: left\" class=\"span1\" id=\"apppp"+dialplan+"\" type=\"number\" value=\"30\" />"
                        + "<p style=\"float: left\" class=\"muted offset1\">Habilitar Rechamada</p>"
                        + "<input class=\"checkbox\" name=\"recall"+dialplan+"\" type=\"checkbox\" />");
            }
            else if(request.getParameter("app").equals("Disca Tronco")){
                String numDialplan=request.getParameter("dialplan");
                JpbxDB db=new JpbxDB();
                out.print("<h5>Selecione o Tronco</h5>"
                        + "<input type = \"hidden\" id = \"app"+numDialplan+"\" value = \"DialTrunk\" />"
                        + ""+db.listActionTrunk(numDialplan)+""
                        + "<h5>Tempo limite para atendimento (segundos)</h5>"
                        + "<input class=\"span1\" id=\"apppp"+numDialplan+"\" type=\"number\" value=\"30\" />");
            }
            else if(request.getParameter("app").equals("Chamar Grupo")){
                String numDialplan=request.getParameter("dialplan");
                JpbxDB db=new JpbxDB();
                out.print("<h5>Selecione o Grupo de Chamada</h5>"
                        + "<input type = \"hidden\" id = \"app"+numDialplan+"\" value = \"CallGrp\" />"
                        + ""+db.listActionCallGrp(numDialplan)+""
                        + "<h5>Tempo limite para atendimento (segundos)</h5>"
                        + "<input class=\"span1\" id=\"apppp"+numDialplan+"\" type=\"number\" value=\"120\" />");
            }
            else if(request.getParameter("app").equals("Edita Destino")){
                String numDialplan=request.getParameter("dialplan");
                out.print("<h5>Os números serão adicionados no inicio do Destino</h5>\n" +
                        "<input type=\"hidden\" id=\"app"+numDialplan+"\" value=\"SetDst\" />"+
"                   <input type=\"number\" id=\"appp"+numDialplan+"\" placeholder=\"Número a ser add\" />\n" +
"                   <input class=\"checkbox\" id=\"check"+numDialplan+"\" type=\"checkbox\" name=\"checkedit"+numDialplan+"\" /> Substituir todo o Destino</input>\n"
                        + "<div id=\"numcut"+numDialplan+"\">"+                        
"                   <h5>Quantidade de números a serem cortados</h5>\n" +
"                   <input id=\"apppp"+numDialplan+"\" class=\"span1\" type=\"number\" value=\"0\" />"
                        + "<input class=\"checkbox\" id=\"pipe"+numDialplan+"\" class=\"span1\" type=\"checkbox\" name=\"pipe"+numDialplan+"\" />"
                        + "Cortar no PIPE (apenas usando alias)"
                        + "</div>"
                        + "<script>"
                        + " $('#check"+numDialplan+"').click(function(){\n" +
"                               $('#numcut"+numDialplan+"').toggle('slow');\n" +
"                           });"
                        + "</script>");
            }
            else if(request.getParameter("app").equals("Comando Livre")){
                String dialplan=request.getParameter("dialplan");
                out.print("<div style=\"float: left\">"
                        + "<h5>Aplicação</h5>"
                        + "<input type=\"text\" id=\"app"+dialplan+"\" />"
                        + "</div>"
                        + "<div style=\"float: left;padding-left: 10px\">"
                        + "<h5>Parâmetro</h5>"
                        + "<input type=\"text\" id=\"appp"+dialplan+"\" />"
                        + "</div>"
                        + "<a href=\"cli.jsp?listapp=1\" target=\"_blank\"> Aplicações Disponíveis</a>");
            }
            else if(request.getParameter("app").equals("Define Custo")){
                JpbxDB db=new JpbxDB();
                String dialplan=request.getParameter("dialplan");
                out.print("<h5>Escolha o Centro de Custo</h5>"
                        + "<input type=\"hidden\" id=\"app"+dialplan+"\" value=\"SetCost\" />"
                        + "<select id=\"appp"+dialplan+"\">"
                        + ""+db.selectCosts()+""
                        + "</select>");
            }
            else if(request.getParameter("app").equals("Recebe Fax")){
                JpbxDB db=new JpbxDB();
                String dialplan=request.getParameter("dialplan");
                out.print("<h5>Escolha a Empresa Destino do Fax Virtual</h5>"
                        + "<input type=\"hidden\" id=\"app"+dialplan+"\" value=\"receiveFax\" />"
                        + "<select id=\"appp"+dialplan+"\">"
                        + ""+db.selectCompanys()+""
                        + "</select>");
            }
            else if(request.getParameter("app").equals("Envia Fax")){
                JpbxDB db=new JpbxDB();
                String dialplan=request.getParameter("dialplan");
                out.print("<h5>Ajusta Parâmetros para envio de Fax Virtual</h5>"
                        + "<input type=\"hidden\" id=\"app"+dialplan+"\" value=\"sendFax\" />"
                        + "<h6>(Sem parâmetros)</h6>");
            }
            else
                out.print("<h5>Não Implementado</h5>");
        }
        if(request.getParameter("cli")!=null){
            Asterisk ast=new Asterisk();
            try {
                ast.AstConnect();
                List list=ast.getInfos(request.getParameter("cli"));
                for(int i=0;i<list.size();i++){
                    out.print(list.get(i)+"</br>");
                }
                ast.AstDisconnect();
                out.print("<script>"
                        + "$('#load').hide();"
                        + "</script>");
            } catch (AuthenticationFailedException ex) {
               out.print("Falha:</br>"+ex.getMessage());
            } catch (TimeoutException ex) {
                out.print("Falha:</br>"+ex.getMessage());
            }
        }
        if(request.getParameter("costID")!=null){
            int costId = 0,fraction,shortage,cicle;
            float fare;
            try{
                costId=Integer.parseInt(request.getParameter("costID"));
                fraction=Integer.parseInt(request.getParameter("fraction"));
                shortage=Integer.parseInt(request.getParameter("shortage"));
                cicle=Integer.parseInt(request.getParameter("cicle"));
                fare=Float.parseFloat(request.getParameter("fare").trim());
                JpbxDB db=new JpbxDB();
                String res=db.alterCost(costId, fare, cicle, fraction, shortage);
                if(!res.equals("ok")){
                    res=res.replaceAll("'", "");
                    out.print("<script>"
                        + "$('#img"+costId+"').hide();"
                        + "informer('<p class=\"text-error\">"+res+"</p>');"
                        + "</script>");
                }
                else
                    out.print("<script>"
                            + "$('#img"+costId+"').show();"
                            + "</script>");
            }catch(NumberFormatException ex){
                out.print("<script>"
                        + "$('#img"+costId+"').hide();"
                        + "informer('<p class=\"text-error\">Os dados informados devem ser exclusivamente Números.</p>');"
                        + "</script>");
            }
        }
        if(request.getParameter("delCost")!=null){
            JpbxDB db=new JpbxDB();
            String res=db.verifyCostsInRules(request.getParameter("desc"));
            if(res.equals("")){
                res=db.deleteCost(request.getParameter("delCost"));
                if(!res.equals("ok")){
                    res=res.replaceAll("'", "");
                        out.print("<script>"
                            + "informer('<p class=\"text-error\">"+res+"</p>');"
                            + "</script>");
                }
                else{
                    out.print("<script>"
                            + "$('#title'+"+request.getParameter("delCost")+").remove();"
                            + "$('#div'+"+request.getParameter("delCost")+").remove();"
                            + "</script>");
                }           
            }
            else
                out.print("<script>"
                        + "informer('<p class=\"text-error\">As regras "+res+" utilizam este custo, "
                        + "portanto não pode ser apagado no momento.</p>');"
                        + "</script>");
        }
        if(request.getParameter("testHost")!=null&&!request.getParameter("testHost").equals("")){
            String ls_str,time="";
            Process ls_proc=Runtime.getRuntime().exec("ping -w1 -c1 "+request.getParameter("testHost"));
	    // get its output (your input) stream
	    DataInputStream ls_in = new DataInputStream(ls_proc.getInputStream());
            while ((ls_str = ls_in.readLine()) != null) {
                    if(ls_str.contains("icmp_req=1")){
                        time=ls_str.substring(ls_str.indexOf("time=")+5);
                        break;
                    }
		}
            if(time.equals(""))
                out.print("<p class=\"text-error\">Host inalcançável no momento.</br>Verifique se o Host está ativo.</p>");
            else
                out.print("<p class=\"text-success\">Host respondeu em "+time+".</p>");
        }
        if(request.getParameter("relDialog")!=null){
            JpbxDB db=new JpbxDB();
            out.print(db.relDetails(request.getParameter("relDialog")));
        }
        if(request.getParameter("relPag")!=null){
            JpbxDB db=new JpbxDB();
            int pag=(Integer.parseInt(request.getParameter("relPag"))-1);
            pag=pag*Integer.parseInt(request.getParameter("limit"));
            if(request.getSession().getAttribute("lvl").equals("Administrador"))
                out.print(db.relCalls(request.getParameter("dateini"),request.getParameter("dateend"),request.getParameter("order"),
                        request.getParameter("src"),request.getParameter("dstfinal"),request.getParameter("ccost"),
                        request.getParameter("status"),pag,Integer.parseInt(request.getParameter("limit")),
                        request.getParameter("srcseek"),request.getParameter("dstseek"))+
                    "<script>"
                    + "$('td').css('font-size', 14);"
                    + "</script>");
            else
                out.print(db.relCalls(request.getParameter("dateini"),request.getParameter("dateend"),request.getParameter("order"),
                        request.getParameter("src"),request.getParameter("dstfinal"),request.getParameter("ccost"),
                        request.getParameter("status"),pag,Integer.parseInt(request.getParameter("limit")),
                        request.getParameter("srcseek"),request.getParameter("dstseek"),
                        db.getUserCompany(request.getSession().getAttribute("user").toString()))+
                    "<script>"
                    + "$('td').css('font-size', 14);"
                    + "</script>");
        }
        if(request.getParameter("relPagConf")!=null){
            JpbxDB db=new JpbxDB();
            int pag=(Integer.parseInt(request.getParameter("relPagConf"))-1);
            pag=pag*Integer.parseInt(request.getParameter("limit"));
            if(request.getSession().getAttribute("lvl").equals("Administrador"))
            out.print(db.relConferences(request.getParameter("dateini"), request.getParameter("dateend"), 
                    request.getParameter("rooms"), pag, Integer.parseInt(request.getParameter("limit")))+
                    "<script>"
                    + "$('td').css('font-size', 14);"
                    + "</script>");
            else
                out.print(db.relConferences(request.getParameter("dateini"), request.getParameter("dateend"), 
                    request.getParameter("rooms"), pag, Integer.parseInt(request.getParameter("limit")),
                            db.getUserCompany(request.getSession().getAttribute("user").toString()))+
                    "<script>"
                    + "$('td').css('font-size', 14);"
                    + "</script>");
        }
        if(request.getParameter("delCall")!=null){
            JpbxDB db=new JpbxDB();
            String res=db.deleteCall(request.getParameter("delCall"));
            if(res.equals("ok"))
                out.print("<script>"
                        + "window.location.reload();"
                        + "informer('<p class=\"text-success\">A Janela deve ser recarregada.</p>');"
                        + "</script>");
            else
                out.print("<script>"
                        + "informer('"+res+"');"
                        + "</script>");
        }
        if(request.getParameter("delConf")!=null){
            JpbxDB db=new JpbxDB();
            String res=db.deleteConference(request.getParameter("delConf"),request.getParameter("rec"));
            if(res.equals("ok"))
                out.print("<script>"
                        + "window.location.reload();"
                        + "informer('<p class=\"text-success\">A Janela deve ser recarregada.</p>');"
                        + "</script>");
            else
                out.print("<script>"
                        + "informer('"+res+"');"
                        + "</script>");
        }
        if(request.getParameter("console")!=null){
            String ls_str;
            Process ls_proc = Runtime.getRuntime().exec("tail -n1000 /var/log/asterisk/full");
            DataInputStream ls_in = new DataInputStream(ls_proc.getInputStream());
            if(!request.getParameter("console").equals("1")){
                while ((ls_str=ls_in.readLine()) != null) {
                    try{
                        if(ls_str.contains(request.getParameter("console")))
                            out.println(ls_str.substring(ls_str.indexOf("--")));
                    }catch(StringIndexOutOfBoundsException ex){
                        out.println(ls_str);
                    }
                }
            }
            else{
                while ((ls_str=ls_in.readLine()) != null) {
                    try{
                        out.println(ls_str.substring(ls_str.indexOf("--")));
                    }catch(StringIndexOutOfBoundsException ex){
                        out.println(ls_str);
                    }
                }
            }
        }
        if(request.getParameter("arqMoh")!=null){
            out.print("<script>"
                    + "alert('recebido "+request.getParameter("arqMoh")+"');"
                    + "</script>");
        }
        if(request.getParameter("mohName")!=null&&!request.getParameter("mohName").equals("")){
             JpbxDB db=new JpbxDB();
            String name=db.replaceNames(request.getParameter("mohName"));
            int res=Integer.parseInt(db.seekMohName(name));
            if(res>0){
                out.print("<p class=\"text-error\">O nome de música <b>"+name+"</b> já existe!</p>"+
                         "<script>"+
                        "$('#mohname').val('');"+                  
                "</script>");
                }
            else
                out.print("<img src=\"css/bootstrap/img/checked.png\"/>"
                        + "<script>"
                        + "$('#mohname').val('"+name+"');"
                        + "</script>");
        }
        if(request.getParameter("execBkp")!=null&&request.getParameter("execBkp").equals("bkp")){
            Asterisk ast=new Asterisk();
            try {
                ast.execBkp();
            } catch (AuthenticationFailedException ex) {
                out.print("<script>"
                        + "alert('"+ex+"');"
                        + "</script>");
            } catch (TimeoutException ex) {
                out.print("<script>"
                        + "alert('"+ex+"');"
                        + "</script>");
            } catch (InterruptedException ex) {
                out.print("<script>"
                        + "alert('"+ex+"');"
                        + "</script>");
            }
        }
        if(request.getParameter("activeRule")!=null&&!request.getParameter("activeRule").equals("")){
            JpbxDB db=new JpbxDB();
            String ret=db.activateRule(request.getParameter("activeRule"), request.getParameter("act"));
            if(ret.equals("ok"))
                out.print("<script>"
                        + "location.reload();"
                        + "</script>");
            else
                out.print("<script>"
                        + "informer('Opa algo deu errado\n"+ret+"');"
                        + "</script>");
        }
        if(request.getParameter("peerDetails")!=null&&!request.getParameter("peerDetails").equals("")){
            Asterisk ast=new Asterisk();
            String res="";
            List ret = null;
            try {
                ast.AstConnect();
                ret=ast.getInfos("sip show peer "+request.getParameter("peerDetails"));
                ast.AstDisconnect();
            } catch (AuthenticationFailedException ex) {
                res=ex.getMessage();
            } catch (TimeoutException ex) {
                res=ex.getMessage();
            }
            for(int i=0;i<ret.size();i++){
                if(ret.get(i).toString().contains("Useragent"))
                    res+="<b>Dispositivo </b>"+ret.get(i).toString().substring(ret.get(i).toString().indexOf(":"))+"<br>";
                else if(ret.get(i).toString().contains("Addr->IP"))
                    res+="<b>IP de Registro </b>"+ret.get(i).toString().substring(ret.get(i).toString().indexOf(":"),
                            ret.get(i).toString().lastIndexOf(":"))+"<br>";
                else if(ret.get(i).toString().contains("Status"))
                    res+="<b>Tempo de Resposta </b>"+ret.get(i).toString().substring(ret.get(i).toString().indexOf(":"))+"<br>";
            }
            res+="<h5>Facilidades</h5>";
            JpbxDB db=new JpbxDB();
            res+=db.verifyStatusPeer(request.getParameter("peerDetails"));
            out.print(res);
        }
        if(request.getParameter("sendFax")!=null&&!request.getParameter("sendFax").equals("")){
            Asterisk ast=new Asterisk();
            JpbxDB db=new JpbxDB();
            String ret;
            try {
                if(request.getParameter("mode").equals("peer"))
                    ret=ast.sendFax(request.getParameter("sendFax"), request.getParameter("file"),
                        request.getParameter("stationId"), db.getUserCompany(request.getSession().getAttribute("user").toString()));
                else
                    ret=ast.sendFax(request.getParameter("sendFax"), request.getParameter("file"), "trunk",
                        request.getParameter("stationId"), db.getUserCompany(request.getSession().getAttribute("user").toString()));
                if(ret.equals("Success"))
                    out.print("<h5 class=\"text-success\">O Fax Remoto Atendeu!</h5> <b>Em instantes voçê pode acessar Relatórios -> Faxes para mais informações.</b>");
                else
                    out.print("<h5 class=\"text-error\">O Fax Remoto Não Atendeu!</h5>");
            } catch (AuthenticationFailedException ex) {
                out.print(ex.getMessage());
            } catch (TimeoutException ex) {
                out.print(ex.getMessage());
            }
        }
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
