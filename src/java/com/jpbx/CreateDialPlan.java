/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jpbx;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jefaokpta
 */
public class CreateDialPlan extends HttpServlet {

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
       // PrintWriter out = response.getWriter();
        JpbxDB db=new JpbxDB();
        String alias=null,aliasCheck="0",exten=request.getParameter("exten");
        int gotoIfFalse=Integer.parseInt(db.verifyGotoIf(exten));
        int aliasID=0;
        if(request.getParameter("checkAlias")!=null){
            aliasID=Integer.parseInt(request.getParameter("alias"));
            alias=db.getAliasName(String.valueOf(aliasID));
            aliasCheck="1";
            gotoIfFalse=Integer.parseInt(db.verifyGotoIfAlias());
        }
        int prio=1;
        int qtdeApps=Integer.parseInt(request.getParameter("qtdeapps"));
        
        
        String fdb,
                seg=(request.getParameter("seg")!=null?"1":"0"),ter=(request.getParameter("ter")!=null?"1":"0"),qua=(request.getParameter("qua")!=null?"1":"0"),
                qui=(request.getParameter("qui")!=null?"1":"0"),sex=(request.getParameter("sex")!=null?"1":"0"),sab=(request.getParameter("sab")!=null?"1":"0"),
                dom=(request.getParameter("dom")!=null?"1":"0");
        // popula dialplan_rules
        if(request.getParameter("selsrc").equals("Ramal")&&request.getParameter("src").equals("Qualquer")) 
            if(request.getParameter("fulltime")!=null)
                fdb=db.createDialPlanRule(request.getParameter("rulename"),"Ramal --> Todos",exten,1,(gotoIfFalse+1),request.getParameter("selsrc"),request.getParameter("src"),aliasCheck,alias);
            else
                fdb=db.createDialPlanRule(request.getParameter("rulename"),"Ramal --> Todos",exten,1,(gotoIfFalse+1),request.getParameter("selsrc"),request.getParameter("src"),
                        "0",request.getParameter("hstart"),request.getParameter("hend"),
                        seg,ter,qua,qui,sex,sab,dom,aliasCheck,alias);
        else if(request.getParameter("selsrc").equals("Qualquer"))
            if(request.getParameter("fulltime")!=null)
                fdb=db.createDialPlanRule(request.getParameter("rulename"),"Qualquer",exten,1,(gotoIfFalse+1),request.getParameter("selsrc"),request.getParameter("src"),aliasCheck,alias);
            else
                fdb=db.createDialPlanRule(request.getParameter("rulename"),"Qualquer",exten,1,(gotoIfFalse+1),request.getParameter("selsrc"),request.getParameter("src"),
                        "0",request.getParameter("hstart"),request.getParameter("hend"),
                        seg,ter,qua,qui,sex,sab,dom,aliasCheck,alias);
        else if(request.getParameter("selsrc").equals("Fax"))
            if(request.getParameter("fulltime")!=null)
                fdb=db.createDialPlanRule(request.getParameter("rulename"),"Fax Virtual",exten,1,(gotoIfFalse+1),request.getParameter("selsrc"),request.getParameter("src"),aliasCheck,alias);
            else
                fdb=db.createDialPlanRule(request.getParameter("rulename"),"Fax Virtual",exten,1,(gotoIfFalse+1),request.getParameter("selsrc"),request.getParameter("src"),
                        "0",request.getParameter("hstart"),request.getParameter("hend"),
                        seg,ter,qua,qui,sex,sab,dom,aliasCheck,alias);
        else if(request.getParameter("selsrc").equals("Tronco"))
            if(request.getParameter("fulltime")!=null)
                fdb=db.createDialPlanRule(request.getParameter("rulename"),"Tronco --> "+request.getParameter("src"),exten,1,(gotoIfFalse+1),request.getParameter("selsrc"),request.getParameter("src"),aliasCheck,alias);
            else
                fdb=db.createDialPlanRule(request.getParameter("rulename"),"Tronco --> "+request.getParameter("src"),exten,1,(gotoIfFalse+1),request.getParameter("selsrc"),request.getParameter("src"),
                        "0",request.getParameter("hstart"),request.getParameter("hend"),
                        seg,ter,qua,qui,sex,sab,dom,aliasCheck,alias);
        else if(request.getParameter("selsrc").equals("Departamento"))
            if(request.getParameter("fulltime")!=null)
                fdb=db.createDialPlanRule(request.getParameter("rulename"),"Departamento --> "+request.getParameter("src"),exten,1,(gotoIfFalse+1),request.getParameter("selsrc"),request.getParameter("src"),aliasCheck,alias);
            else
                fdb=db.createDialPlanRule(request.getParameter("rulename"),"Departamento --> "+request.getParameter("src"),exten,1,(gotoIfFalse+1),request.getParameter("selsrc"),request.getParameter("src"),
                        "0",request.getParameter("hstart"),request.getParameter("hend"),
                        seg,ter,qua,qui,sex,sab,dom,aliasCheck,alias);
        else
            if(request.getParameter("fulltime")!=null)
                fdb=db.createDialPlanRule(request.getParameter("rulename"),request.getParameter("src"),exten,1,(gotoIfFalse+1),request.getParameter("selsrc"),request.getParameter("src"),aliasCheck,alias);
            else
                fdb=db.createDialPlanRule(request.getParameter("rulename"),request.getParameter("src"),exten,1,(gotoIfFalse+1),request.getParameter("selsrc"),request.getParameter("src"),
                        "0",request.getParameter("hstart"),request.getParameter("hend"),
                        seg,ter,qua,qui,sex,sab,dom,aliasCheck,alias);       
        if(!fdb.equals("ok")){
            response.sendRedirect("listDialPlan.jsp?fail="+fdb);
            return;
        }
        if(gotoIfFalse>0) //trata exten em caso de duplicação
            prio=gotoIfFalse*100+1;
        
        String days;  //verifica validade da regra
        int ruleId=Integer.parseInt(db.getRuleId(request.getParameter("rulename")));
        if(request.getParameter("fulltime")!=null)
            days="";
        else
            days="&&1==${SHELL(/java -cp /scriptsJPBX com.jpbx.ScriptVerifyDay " //ajustando tempo de função da regra
                    + ""+request.getParameter("seg")+" "+request.getParameter("ter")+" "+request.getParameter("qua")+" "+
                request.getParameter("qui")+" "+request.getParameter("sex")+" "+request.getParameter("sab")+" "+
                request.getParameter("dom")+")}&&${IFTIME("+request.getParameter("hstart")+"-"+request.getParameter("hend")+",*,*,*?1:0)}";       
        
        String dialplanAlias=""; //verifica se usara Alias
        if(aliasID!=0)
            dialplanAlias="&&1==${SHELL(/java -cp /scriptsJPBX com.jpbx.ScriptAlias "
                    + ""+aliasID+" ${DST} ${CHANNEL})}";
        //1º prioridade da regra origens
        if(request.getParameter("selsrc").equals("Ramal")){ // origem ramal
            if(request.getParameter("src").equals("Qualquer"))
                fdb=db.createDialPlan(exten,prio,"GotoIf",
                    "$[${ODBC_RULE_ACTIVE("+ruleId+")}==1&&"
                            + "1==${SHELL(/java -cp /scriptsJPBX com.jpbx.ScriptExtensions ${ORG})}"+days+""
                        +dialplanAlias+ "]?:auxDialPlan,"+(prio+100)+",1",ruleId);
            else
                fdb=db.createDialPlan(exten,prio,"GotoIf",
                    "$[${ODBC_RULE_ACTIVE("+ruleId+")}==1&&"
                            + "${ORG}=="+request.getParameter("src")+""+days+dialplanAlias+"]?:auxDialPlan,"+(prio+100)+",1",ruleId);
        }
        else if(request.getParameter("selsrc").equals("Qualquer")){  //origem qlqr
            fdb=db.createDialPlan(exten, prio, "GotoIf", 
                    "$[${ODBC_RULE_ACTIVE("+ruleId+")}==1&&"
                            + "1==1"+days+dialplanAlias+"]?:auxDialPlan,"+(prio+100)+",1",ruleId);
        }
        else if(request.getParameter("selsrc").equals("Fax")){  //origem Fax
            fdb=db.createDialPlan(exten, prio, "GotoIf", 
                    "$[${ODBC_RULE_ACTIVE("+ruleId+")}==1&&"
                            + "${CALLERID(num)}==9999"+days+dialplanAlias+"]?:auxDialPlan,"+(prio+100)+",1",ruleId);
        }
        else if(request.getParameter("selsrc").equals("Expr")){  //origem livre    
            fdb=db.createDialPlan(exten, prio, "GotoIf",
                    "$[${ODBC_RULE_ACTIVE("+ruleId+")}==1&&"
                            + ""+request.getParameter("src")+""+days+dialplanAlias+"]?:auxDialPlan,"+(prio+100)+",1",ruleId);
        }else if(request.getParameter("selsrc").equals("Departamento")){ //origem departamento
            fdb=db.createDialPlan(exten, prio, "GotoIf",
                    "$[${ODBC_RULE_ACTIVE("+ruleId+")}==1&&"
                            + "1==${SHELL(/java -cp /scriptsJPBX com.jpbx.ScriptDeptos "
                    +request.getParameter("src")+" ${ORG})}"+days+dialplanAlias+"]?:auxDialPlan,"+(prio+100)+",1",ruleId);
        }else if(request.getParameter("selsrc").equals("Tronco")){ //origem tronco
           // if(db.getTrunkTecnology(request.getParameter("src")).equals("Digital"))
            if(request.getParameter("src").equals("Qualquer"))
                fdb=db.createDialPlan(exten, prio, "GotoIf",
                        "$[${ODBC_RULE_ACTIVE("+ruleId+")}==1&&"
                                + "\"${CDR(userfield)}\"==\"EXTERNAL\""+days+dialplanAlias+"]?:auxDialPlan,"+(prio+100)+",1",ruleId);
            else
                fdb=db.createDialPlan(exten, prio, "GotoIf",
                        "$[${ODBC_RULE_ACTIVE("+ruleId+")}==1&&"
                                + "\"${TRUNKNAME}\"==\""+request.getParameter("src")+"\""+days+dialplanAlias+"]?:auxDialPlan,"+(prio+100)+",1",ruleId);
        }
       
        //2ª identificando regra
        prio++;
        db.createDialPlan(exten, prio, "AelSub", "Transfer_Ajust,${ORG},${DST},${UNIQUEID},"+request.getParameter("rulename")+","+ruleId,ruleId);
        
        // Ações
        prio++;
        for(int i=1;i<=qtdeApps;i++){
            if(request.getParameter("appname"+i).equals("DialPeer")){ //Manipulando app Dial Ramal
             //   String param,dial=request.getParameter("appname"+i).substring(0,4);
                String param,dial="AelSub";
                if(request.getParameter("param"+i).equals(""))
                    param="dialPeer,${DST},"+request.getParameter("paramaux"+i)+","+(request.getParameter("recall"+i)!=null?"1":"0");
                    //param="${ODBC_RAMAL(${DST})},"+request.getParameter("paramaux"+i)+",tkM(answerer,${ORG},${TRANSFERERNAME})";
                else
                    param="dialPeer,"+request.getParameter("param"+i)+","+request.getParameter("paramaux"+i)+
                            ","+(request.getParameter("recall"+i)!=null?"1":"0");
                    //param="${ODBC_RAMAL("+request.getParameter("param"+i)+")},"+request.getParameter("paramaux"+i)+",tkM(answerer,${ORG},${TRANSFERERNAME})";
                fdb=db.createDialPlan(exten, prio, dial,param, ruleId);
                db.dialplanActions(ruleId, request.getParameter("appname"+i),request.getParameter("param"+i) ,
                        request.getParameter("paramaux"+i),(request.getParameter("recall"+i)!=null?"1":"0"),"");
            }
            else if(request.getParameter("appname"+i).equals("DialTrunk")){  //Manipulando app Dial tronco
                //String param,dial=request.getParameter("appname"+i).substring(0,4);
                String param,dial="AelSub";
                //param=db.getTrunkCanal(request.getParameter("param"+i))+"/${DST},"+request.getParameter("paramaux"+i)+
                       // ",TKM(answerer-trunks,${CHANNEL},${ORG},${DST})";
                param="dialTrunk,"+db.getTrunkCanal(request.getParameter("param"+i))+","+request.getParameter("paramaux"+i)+"";
                fdb=db.createDialPlan(exten, prio, dial,param,ruleId);
                db.dialplanActions(ruleId, request.getParameter("appname"+i), request.getParameter("param"+i),
                        request.getParameter("paramaux"+i),"","");
            }
            else if(request.getParameter("appname"+i).equals("CallGrp")){  //Manipulando app queue p grupo de chamada
                String param,queue="AelSub";
                param="callGrp,"+"callGrp&"+request.getParameter("param"+i)+","+request.getParameter("paramaux"+i);
                fdb=db.createDialPlan(exten, prio, queue,param,ruleId);
                db.dialplanActions(ruleId, request.getParameter("appname"+i), request.getParameter("param"+i),
                        request.getParameter("paramaux"+i),"","");
            }
            else if(request.getParameter("appname"+i).equals("SetDst")){ //edita destino
                if(request.getParameter("checkedit"+i)!=null){
                    fdb=db.createDialPlan(exten, prio, "Set", "DST="+request.getParameter("param"+i)+"", ruleId);
                    db.dialplanActions(ruleId, request.getParameter("appname"+i), 
                            request.getParameter("param"+i), request.getParameter("paramaux"+i), "y","n");
                }
                else{
                    if(request.getParameter("pipe"+i)!=null){
                        fdb=db.createDialPlan(exten, prio, "Set", "DST="+request.getParameter("param"+i)+"${DST:${ALIASCUT}}",
                            ruleId);
                        db.dialplanActions(ruleId, request.getParameter("appname"+i), 
                            request.getParameter("param"+i), request.getParameter("paramaux"+i), "n","y");
                    }
                    else{
                        fdb=db.createDialPlan(exten, prio, "Set", "DST="+request.getParameter("param"+i)+"${DST:"+request.getParameter("paramaux"+i)+"}",
                            ruleId);   
                        db.dialplanActions(ruleId, request.getParameter("appname"+i), 
                            request.getParameter("param"+i), request.getParameter("paramaux"+i), "n","n");
                    }
                }
            }
            else if(request.getParameter("appname"+i).equals("PlayBacks")){ //toca audio
                fdb=db.createDialPlan(exten, prio, "PlayBack", 
                    "/etc/asterisk/jpbx/moh/"+request.getParameter("param"+i), ruleId);
                db.dialplanActions(ruleId, request.getParameter("appname"+i),request.getParameter("param"+i) , "","","");
            }
            else if(request.getParameter("appname"+i).equals("SetCost")){
                fdb=db.createDialPlan(exten, prio, "Set",
                        "CDR(accountcode)="+request.getParameter("param"+i).substring(0,request.getParameter("param"+i).indexOf(" ")),
                        ruleId);
                db.dialplanActions(ruleId, request.getParameter("appname"+i),request.getParameter("param"+i) , "","","");
            }
            else if(request.getParameter("appname"+i).equals("receiveFax")){
                fdb=db.createDialPlan(exten, prio, "Goto",
                        "receiveFax,"+request.getParameter("param"+i)+",1",
                        ruleId);
                db.dialplanActions(ruleId, request.getParameter("appname"+i),request.getParameter("param"+i) , "","","");
            }
            else if(request.getParameter("appname"+i).equals("sendFax")){
                fdb=db.createDialPlan(exten, prio, "Set",
                        "DB(FAX/${CUT(UNIQUEID,.,1)})=${CDR(accountcode)}",
                        ruleId);
                db.dialplanActions(ruleId, request.getParameter("appname"+i),"" , "","","");
            }
            else{
                fdb=db.createDialPlan(exten, prio, request.getParameter("appname"+i), 
                    request.getParameter("param"+i), ruleId);
                db.dialplanActions(ruleId, request.getParameter("appname"+i),request.getParameter("param"+i) , "","","");
            }
            prio++;
            //out.print("App: "+request.getParameter("appname"+i)+"param: "+request.getParameter("param"+i)+"</br>");
        }
        if(fdb.equals("ok"))
            response.sendRedirect("listDialPlan.jsp");
        else
            response.sendRedirect("listDialPlan.jsp?fail="+fdb);
        
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
