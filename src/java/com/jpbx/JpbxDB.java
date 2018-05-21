/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jpbx;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.TimeoutException;

/**
 *
 * @author jefaokpta
 */
public class JpbxDB {
    
    public JpbxDB() {}
    
    public String login(String user){
        JpbxDBConnect db=new JpbxDBConnect();
        String res="fail";
        try {   
            db.resultado=db.Comando.executeQuery("select password "
                            + "from users where name='"+user+"'");
            if(db.resultado.next()){
                res=db.resultado.getString(1);
            }
        } catch (SQLException ex) {
            res+=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String listPeers(){
        JpbxDBConnect db=new JpbxDBConnect();
        JpbxDBConnect dbGroups=new JpbxDBConnect();
        String depto = null,nat,res="<table class=\"span11 table-striped table-condensed table-hover table-bordered\">"
                + "<tr><th>Código</th><th>Ramal</th><th>Nome</th><th>Empresa</th><th>Departamento</th><th>Externo</th><th>Ações</th><th></th></tr>";
        try {
            db.resultado=db.Comando.executeQuery("select id,name,callerid,pickupgroup,nat,company"
                    + " from peers where name!='admin' order by name");
            while(db.resultado.next()){
                dbGroups.resultado=dbGroups.Comando.executeQuery("select name from grppickup where pickup_id="+db.resultado.getString(4)+"");
                if(dbGroups.resultado.next())
                    depto=dbGroups.resultado.getString(1);
                nat=db.resultado.getString(5).equals("never")?"Não":"Sim";
                res+="<tr><td>"+db.resultado.getString(1)+"</td>"
                        + "<td>"+verifyRegistered(db.resultado.getString(2))+" "+db.resultado.getString(2)+"</td>"
                        + "<td>"+db.resultado.getString(3)+"</td>"
                        + "<td>"+getCompanyName(db.resultado.getString(6))+"</td>"
                        + "<td>"+depto+"</td>"
                        + "<td>"+nat+"</td>"
                        + "<td><a rel=\"tooltip\" data-placement=\"left\" title=\"Editar ramal\" href=\"alterPeer.jsp?peer="+db.resultado.getString(2)+"\" ><img src=\"css/bootstrap/img/icone-editar.gif\"></a></td> "
                        + "<td><a rel=\"tooltip\" data-placement=\"right\" title=\"Apagar ramal\" href=\"#\" onclick=\"deletePeer("+db.resultado.getString(2)+");\" ><img src=\"css/bootstrap/img/deletar.gif\"></a></td></tr>";
            }
            res+="</table>";

        } catch (SQLException ex) {
            res="Falha na lista: "+ex.getMessage();
        }
        dbGroups.disconnectDB();
        db.disconnectDB();
        return res;
    }
    public String listPeers(String method,String seek){
        JpbxDBConnect db=new JpbxDBConnect();
        JpbxDBConnect dbGroups=new JpbxDBConnect();
        String where;
        if(method.equals("depto"))
            where=" where pickupgroup=(select pickup_id from grppickup where name='"+seek+"') ";
        else
            where=" where "+method+"='"+seek+"' ";
        String depto = null,nat,res="<table class=\"span11 table-striped table-condensed table-hover table-bordered\">"
                + "<tr><th>Código</th><th>Ramal</th><th>Nome</th><th>Departamento</th><th>Externo</th><th>Ações</th><th></th></tr>";
        try {
            db.resultado=db.Comando.executeQuery("select id,name,callerid,pickupgroup,nat,company"
                    + " from peers "+where+" order by name");
            while(db.resultado.next()){
                dbGroups.resultado=dbGroups.Comando.executeQuery("select name from grppickup where pickup_id="+db.resultado.getString(4)+"");
                if(dbGroups.resultado.next())
                    depto=dbGroups.resultado.getString(1);
                nat=db.resultado.getString(5).equals("never")?"Não":"Sim";
                res+="<tr><td>"+db.resultado.getString(1)+"</td>"
                        + "<td>"+verifyRegistered(db.resultado.getString(2))+" "+db.resultado.getString(2)+"</td>"
                        + "<td>"+db.resultado.getString(3)+"</td>"
                        + "<td>"+getCompanyName(db.resultado.getString(6))+"</td>"
                        + "<td>"+depto+"</td>"
                        + "<td>"+nat+"</td>"
                        + "<td><a rel=\"tooltip\" data-placement=\"left\" title=\"Editar ramal\" href=\"alterPeer.jsp?peer="+db.resultado.getString(2)+"\" ><img src=\"css/bootstrap/img/icone-editar.gif\"></a></td> "
                        + "<td><a rel=\"tooltip\" data-placement=\"right\" title=\"Apagar ramal\" href=\"#\" onclick=\"deletePeer("+db.resultado.getString(2)+");\" ><img src=\"css/bootstrap/img/deletar.gif\"></a></td></tr>";
            }
            res+="</table>";

        } catch (SQLException ex) {
            res="Falha na lista: "+ex.getMessage();
        }
        dbGroups.disconnectDB();
        db.disconnectDB();
        return res;
    }
    String verifyRegistered(String peer){
        String res = "<img src=\"css/bootstrap/img/red-signals.png\">";//"(Unspecified)";
        String ls_str;
        Process ls_proc;
        try {
            ls_proc = Runtime.getRuntime().exec("cat /tmp/registered");
            DataInputStream ls_in = new DataInputStream(
                                              ls_proc.getInputStream());
            // get its output (your input) stream
            ls_in = new DataInputStream(ls_proc.getInputStream());
            while ((ls_str = ls_in.readLine()) != null) {
                if(ls_str.substring(0, peer.length()+1).equals(peer+" ")||ls_str.substring(0, peer.length()+1).equals(peer+"/"))
                    if(!ls_str.substring(27, 42).trim().contains("Unspecified"))
                        res="<a href=\"#\" onclick=\"peerDetails("+peer+");\"><img src=\"css/bootstrap/img/green-signals.png\"></a>";
            }
        } catch (IOException ex) {
            res=ex.getMessage();
        }
                
        return res;
    }
    public String listPeersCallGrp(){
        String res="";
        JpbxDBConnect db=new JpbxDBConnect();
        JpbxDBConnect dbQueueMember=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select name from peers order by name");
            while(db.resultado.next()){
                dbQueueMember.resultado=dbQueueMember.Comando.executeQuery("select queue_name from queue_members where membername="+db.resultado.getString(1)+"");
                if(dbQueueMember.resultado.next())
                    res+="<li class=\"ui-widget-content\">"+db.resultado.getString(1)+" ("+dbQueueMember.resultado.getString(1).substring(8)+")</li>";
                else
                    res+="<li class=\"ui-widget-content\">"+db.resultado.getString(1)+"</li>";
            }
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        dbQueueMember.disconnectDB();
        return res;
    }
    public String listPeersCos(){
        String res="";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select name from peers order by name");
            while(db.resultado.next()){
                res+="<li class=\"ui-widget-content\">"+db.resultado.getString(1)+"</li>";
            }
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String listPeersCos(String idCos){
        String res="";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select name from peers order by name");
            while(db.resultado.next()){
                res+="<li class=\"ui-widget-content "+markPeerCos(idCos, db.resultado.getString(1))
                        + "\">"+db.resultado.getString(1)+"</li>";
            }
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    String markPeerCos(String idCos,String peer){
        String res="";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select peer from cos_peers where idcos="+idCos);
            while(db.resultado.next())
                if(db.resultado.getString(1).equals(peer)){
                    res="ui-selected";
                    break;
                }
        } catch (SQLException ex) {
            Logger.getLogger(JpbxDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        db.disconnectDB();
        return res;
    }
    public String listCostsCos(){
        String res="";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("SELECT ccost_name,ccost FROM jpbx.ccosts where ccost regexp '2|3|4|5|6|7'");
            while(db.resultado.next()){
                res+="<li class=\"ui-widget-content\">"+db.resultado.getString(2)+"-"+db.resultado.getString(1)+"</li>";
            }
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String listCostsCos(String idCos){
        String res="";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("SELECT ccost_name,ccost FROM jpbx.ccosts where ccost regexp '2|3|4|5|6|7'");
            while(db.resultado.next()){
                res+="<li class=\"ui-widget-content "+markCostCos(idCos, db.resultado.getString(2))
                        + "\">"+db.resultado.getString(2)+"-"+db.resultado.getString(1)+"</li>";
            }
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    String markCostCos(String idCos,String ccost){
        String res="";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select costype from cos_types where idcos="+idCos);
            while(db.resultado.next())
                if(db.resultado.getString(1).equals(ccost)){
                    res="ui-selected";
                    break;
                }
        } catch (SQLException ex) {
            Logger.getLogger(JpbxDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        db.disconnectDB();
        return res;
    }
    public String listPeersCallGrp(String callGrp){
        String res="";
        JpbxDBConnect db=new JpbxDBConnect();
        JpbxDBConnect dbQueueMember=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select name from peers order by name");
            while(db.resultado.next()){
                dbQueueMember.resultado=dbQueueMember.Comando.executeQuery("select queue_name from queue_members where membername="+db.resultado.getString(1)+"");
                if(dbQueueMember.resultado.next())
                    res+="<li class=\"ui-widget-content "+(callGrp.equals(dbQueueMember.resultado.getString(1))?"ui-selected":"")+"\">"+
                            db.resultado.getString(1)+" ("+dbQueueMember.resultado.getString(1).substring(8)+")</li>";
                else
                    res+="<li class=\"ui-widget-content\">"+db.resultado.getString(1)+"</li>";
            }
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        dbQueueMember.disconnectDB();
        return res;
    }
    public String listCallPeersSelected(String callGrp){
        String res="";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select membername from queue_members where queue_name='"+callGrp+"' order by uniqueid");
            while(db.resultado.next()){
                res+="<li>"+db.resultado.getString(1)+"</li>";
            }
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String listPickupGrps(){
        JpbxDBConnect db=new JpbxDBConnect();
        String qtde,res="<table class=\"span11 table-striped table-condensed table-hover table-bordered\">"
                + "<tr><th>Código</th><th>Grupo de Captura</th><th>Integrantes</th><th>Ações</th><th></th></tr>";
        try {
            db.resultado=db.Comando.executeQuery("select pickup_id,name "
                    + " from grppickup order by name");
            while(db.resultado.next()){
                qtde=getPickupPeers(db.resultado.getString(1));
                res+="<tr><td>"+db.resultado.getString(1)+"</td>"
                        + "<td>"+db.resultado.getString(2)+"</td>"
                        + "<td><a class=\"btn\" data-content=\""+getPickupPeersName(db.resultado.getString(1))+"\" data-placement=\"left\" "
                        + "rel=\"popover\" href=\"#\" data-original-title=\""+db.resultado.getString(2)+"\">"+qtde+"</a></td>"
                        + "<td><a rel=\"tooltip\" data-placement=\"left\" title=\"Editar grupo\" href=\"alterPickupGrp.jsp?grp="+db.resultado.getString(2)+"\" ><img src=\"css/bootstrap/img/icone-editar.gif\"></a></td> "
                        + "<td><a rel=\"tooltip\" data-placement=\"right\" title=\"Apagar grupo\" href=\"#\" onclick=\"deleteGrp('"+db.resultado.getString(2)+"',"+qtde+");\" ><img src=\"css/bootstrap/img/deletar.gif\"></a></td>"
                        + "</tr>";
            }
            res+="</table>";//"+getPickupPeers(db.resultado.getString(1))+"

        } catch (SQLException ex) {
            res="Falha na lista: "+ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String listCos(){
        JpbxDBConnect db=new JpbxDBConnect();
        String qtde,qtde2,res="<table class=\"span11 table-striped table-condensed table-hover table-bordered\">"
                + "<tr><th>Código</th><th>Classe de serviço</th><th>Bloqueios</th><th>Integrantes</th><th>Ações</th><th></th></tr>";
        try {
            db.resultado=db.Comando.executeQuery("select idcos,name "
                    + " from cos order by name");
            while(db.resultado.next()){
                qtde=getCosPeers(db.resultado.getString(1));
                qtde2=getCosTypes(db.resultado.getString(1));
                res+="<tr><td>"+db.resultado.getString(1)+"</td>"
                        + "<td>"+db.resultado.getString(2)+"</td>"
                        + ""
                        + "<td><a class=\"btn\" data-content=\""+getCosTypesName(db.resultado.getString(1))+"\" data-placement=\"left\" "
                        + "rel=\"popover\" href=\"#\" data-original-title=\""+db.resultado.getString(2)+"\">"+qtde2+"</a></td>"
                        + "<td><a class=\"btn\" data-content=\""+getCosPeersName(db.resultado.getString(1))+"\" data-placement=\"right\" "
                        + "rel=\"popover\" href=\"#\" data-original-title=\""+db.resultado.getString(2)+"\">"+qtde+"</a></td>"
                        + "<td><a rel=\"tooltip\" data-placement=\"left\" title=\"Editar classe\" href=\"alterCos.jsp?grp="+db.resultado.getString(1)+"\" ><img src=\"css/bootstrap/img/icone-editar.gif\"></a></td> "
                        + "<td><a rel=\"tooltip\" data-placement=\"right\" title=\"Apagar classe\" href=\"#\" onclick=\"deleteCos('"+db.resultado.getString(2)+"',"+db.resultado.getString(1)+");\" ><img src=\"css/bootstrap/img/deletar.gif\"></a></td>"
                        + "</tr>";
            }
            res+="</table>";//"+getPickupPeers(db.resultado.getString(1))+"

        } catch (SQLException ex) {
            res="Falha na lista: "+ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String getCosTypesName(String id){
        String res = "";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select costype from cos_types where idcos="+id);
            while(db.resultado.next()){
                if(res.equals(""))
                    res+=getCosTypesTranslate(db.resultado.getString(1));
                else
                    res+=", "+getCosTypesTranslate(db.resultado.getString(1));
            }
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String getCosTypesTranslate(String type){
        String res = null;
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select ccost_name from ccosts where ccost="+type);
            if(db.resultado.next())
                res=db.resultado.getString(1);
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String getCosPeersName(String id){
        String res = "";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select peer from cos_peers where idcos="+id+" "
                    + "order by peer");
            while(db.resultado.next()){
                if(res.equals(""))
                    res+=db.resultado.getString(1);
                else
                    res+=", "+db.resultado.getString(1);
            }
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String getCosTypes(String id){
        String res = null;
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select count(costype) from cos_types where idcos="+id);
            if(db.resultado.next())
                res=db.resultado.getString(1);
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String getCosPeers(String id){
        String res = null;
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select count(peer) from cos_peers where idcos="+id);
            if(db.resultado.next())
                res=db.resultado.getString(1);
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String listCallGrps(){
        JpbxDBConnect db=new JpbxDBConnect();
        String qtde,res="<table class=\"span11 table-striped table-condensed table-hover table-bordered\">"
                + "<tr><th>Código</th><th>Grupo de Chamada</th><th>Estratégia de Toque</th><th>Integrantes</th><th>Ações</th><th></th></tr>";
        try {
            db.resultado=db.Comando.executeQuery("select queues_id,name,strategy"
                    + " from queues where type='C' order by name");
            while(db.resultado.next()){
                qtde=getCallPeers(db.resultado.getString(2));
                res+="<tr><td>"+db.resultado.getString(1)+"</td>"
                        + "<td>"+db.resultado.getString(2).substring(8)+"</td>"
                        + "<td>"+translateCallGrpsStrategys(db.resultado.getString(3))+"</td>"
                        + "<td><a class=\"btn\" data-content=\""+getCallPeersName(db.resultado.getString(2))+"\" data-placement=\"left\" "
                        + "rel=\"popover\" href=\"#\" data-original-title=\""+db.resultado.getString(2).substring(8)+"\">"+qtde+"</a></td>"
                        + "<td><a rel=\"tooltip\" data-placement=\"left\" title=\"Editar grupo\" href=\"alterCallGrp.jsp?grp="+db.resultado.getString(1)+"\" ><img src=\"css/bootstrap/img/icone-editar.gif\"></a></td> "
                        + "<td><a rel=\"tooltip\" data-placement=\"right\" title=\"Apagar grupo\" href=\"#\" onclick=\"deleteCallGrp('"+db.resultado.getString(2).substring(8)+"',"+qtde+","+db.resultado.getString(1)+");\" ><img src=\"css/bootstrap/img/deletar.gif\"></a></td>"
                        + "</tr>";
            }
            res+="</table>";//"+getPickupPeers(db.resultado.getString(1))+"

        } catch (SQLException ex) {
            res="Falha na lista: "+ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String translateCallGrpsStrategys(String strategy){
        String res="Todos";
        if(strategy.equals("random"))
            res="Aleatório";
        else if(strategy.equals("fewestcalls"))
            res="Oscioso";
        return res;
    }
    public String callGrpRemoveMember(String memberName){
        String res="ok";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            int sql=db.Comando.executeUpdate("delete from queue_members where membername='"+memberName+"'");
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String callGrpAddMember(String memberName,String callGrp){
        String res="ok";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            int sql=db.Comando.executeUpdate("insert into queue_members (membername,queue_name,interface) "
                    + "values ('"+memberName+"','callGrp&"+callGrp+"','SIP/"+memberName+"')");
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String createCallGrp(String grpName,String timeout,String strategy,String moh,String comp){
        String res="ok";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            int sql=db.Comando.executeUpdate("insert into queues (name,timeout,type,strategy,joinempty,leavewhenempty,musiconhold,company) "
                    + "values ('callGrp&"+grpName+"','"+timeout+"','C','"+strategy+"','no','yes','"+moh+"',"+comp+")");
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String alterCallGrp(String grpName,String timeout,String strategy,String comp){
        String res="ok";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            int sql=db.Comando.executeUpdate("update queues set timeout="+timeout+",strategy='"+strategy+"',company="+comp+""
                    + " where name='callGrp&"+grpName+"'");
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String deleteCallGrpMembers(String grpName){
        String res="ok";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            int sql=db.Comando.executeUpdate("delete from queue_members where queue_name='"+grpName+"'");
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String deleteCallGrp(String idGrp){
        String res="ok";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            int sql=db.Comando.executeUpdate("delete from queues where queues_id="+idGrp+"");
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String listUsers(){
        JpbxDBConnect db=new JpbxDBConnect();
        String qtde,res="<table class=\"span11 table-striped table-condensed table-hover table-bordered\">"
                + "<tr><th>Código</th><th>Usuário</th><th>Empresa</th><th>Nível</th><th>Ações</th><th></th></tr>";
        try {
            db.resultado=db.Comando.executeQuery("select idusers,name,company,nivel "
                    + " from users order by name");
            while(db.resultado.next()){
                res+="<tr><td>"+db.resultado.getString(1)+"</td>"
                        + "<td>"+db.resultado.getString(2)+"</td>"
                        + "<td>"+getCompanyName(db.resultado.getString(3))+"</td>"
                        + "<td>"+db.resultado.getString(4)+"</td>"
  
                        + "<td><a rel=\"tooltip\" data-placement=\"left\" title=\"Editar usuário\" href=\"alterUser.jsp?usr="+db.resultado.getString(1)+"\" ><img src=\"css/bootstrap/img/icone-editar.gif\"></a></td> "
                        + "<td><a rel=\"tooltip\" data-placement=\"right\" title=\"Apagar usuário\" href=\"#\" onclick=\"deleteUser('"+db.resultado.getString(2)+"',"+db.resultado.getString(1)+");\" ><img src=\"css/bootstrap/img/deletar.gif\"></a></td>"
                        + "</tr>";
            }
            res+="</table>";//"+getPickupPeers(db.resultado.getString(1))+"

        } catch (SQLException ex) {
            res="Falha na lista: "+ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    String getCompanyName(String idComp){
        String res="ok";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select name from companys where idcompanys="+idComp);
            if(db.resultado.next())
                res=db.resultado.getString(1);
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String getCompanyOptions(){
        String res="<select name=\"company\">";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select idcompanys,name from companys order by idcompanys");
            while(db.resultado.next())
                res+="<option value=\""+db.resultado.getString(1)+"\">"+db.resultado.getString(2)+"</option>";
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        res+="</select>";
        db.disconnectDB();
        return res;
    }
    public String getCompanyOptions(String id){
        String res="<select name=\"company\">";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select idcompanys,name from companys order by idcompanys");
            while(db.resultado.next())
                res+="<option "+(db.resultado.getString(1).equals(id)?"selected":"")+" "
                        + "value=\""+db.resultado.getString(1)+"\">"+db.resultado.getString(2)+"</option>";
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        res+="</select>";
        db.disconnectDB();
        return res;
    }
    public String listCosts(){
        String res="";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select * from ccosts order by ccost");
            while(db.resultado.next()){
                res+="<h4 style=\"background-color: #e1e1e1;padding-left: 10%;color: blue\" id=\"title"+db.resultado.getString(1)+"\" class=\"table-bordered\">"+db.resultado.getString(7)+" - "+db.resultado.getString(2)+"</h4>\n" +
"            <div class=\"table-bordered\" id=\"div"+db.resultado.getString(1)+"\">\n" +
"                <h5 style=\"float: left;padding-left: 1%\"> Tarifa: </h5>\n" +
"                <a href=\"#\" rel=\"tooltip\" data-placement=\"botton\" title=\"Tarifa a ser cobrada.\">\n" +
"                    <input style=\"float: left\" class=\"span1\" type=\"number\" id=\"fare"+db.resultado.getString(1)+"\" value=\""+db.resultado.getString(3)+"\" placeholder=\"2.50\"/></a>\n" +
"                    <h5 style=\"float: left\">(R$)</h5>\n" +
"                <h5 style=\"float: left;padding-left: 1%\"> Ciclo: </h5>\n" +
"                <a href=\"#\" rel=\"tooltip\" data-placement=\"botton\" title=\"Frequência que se cobra a tarifa inteira.\">\n" +
"                    <input style=\"float: left\" class=\"span1\" type=\"number\" value=\""+db.resultado.getString(4)+"\" id=\"cicle"+db.resultado.getString(1)+"\"/></a>\n" +
"                    <h5 style=\"float: left\">(s)</h5>\n" +
"                <h5 style=\"float: left;padding-left: 1%\"> Fração: </h5>\n" +
"                <a href=\"#\" rel=\"tooltip\" data-placement=\"botton\" title=\"Fracionamento do ciclo.\">\n" +
"                    <input style=\"float: left\" class=\"span1\" type=\"number\" value=\""+db.resultado.getString(5)+"\" id=\"fraction"+db.resultado.getString(1)+"\"/></a>\n" +
"                    <h5 style=\"float: left\">(s)</h5>\n" +
"                <h5 style=\"float: left;padding-left: 1%\"> Carência: </h5>\n" +
"                <a href=\"#\" rel=\"tooltip\" data-placement=\"botton\" title=\"Carência antes de iniciar a tarifação (após atendimento).\">\n" +
"                    <input style=\"float: left\" class=\"span1\" type=\"number\" value=\""+db.resultado.getString(6)+"\" id=\"shortage"+db.resultado.getString(1)+"\"/></a>\n" +
"                    <h5 style=\"float: left\">(s)<img id=\"img"+db.resultado.getString(1)+"\" src=\"css/bootstrap/img/checked.png\"/></h5>\n" +
"                    <input style=\"float: left\" type=\"button\" class=\"button offset1\" onclick=\"verifyEdit("+db.resultado.getString(1)+");\" value=\"Salvar\"/>\n" +
                    (db.resultado.getString(7).substring(2).equals("00")?"":""
                        + "<a style=\"float: left;padding-left: 2%\" rel=\"tooltip\" data-placement=\"botton\" title=\"Apagar Custo\" href=\"#\" onclick=\"delCost("+db.resultado.getString(1)+");\"><img src=\"css/bootstrap/img/deletar.gif\"></a>\n") +
"            </div>";
            }
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String getPickupPeersName(String id){
        String res = "";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select name from peers where pickupgroup="+id+" "
                    + "order by name");
            while(db.resultado.next()){
                if(res.equals(""))
                    res+=db.resultado.getString(1);
                else
                    res+=", "+db.resultado.getString(1);
            }
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String getCallPeersName(String grp){
        String res = "";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select membername from queue_members where queue_name='"+grp+"' "
                    + "order by uniqueid");
            while(db.resultado.next()){
                if(res.equals(""))
                    res+=db.resultado.getString(1);
                else
                    res+=", "+db.resultado.getString(1);
            }
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String getPickupPeers(String id){
        String res = null;
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select count(name) from peers where pickupgroup="+id+"");
            if(db.resultado.next())
                res=db.resultado.getString(1);
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String getCallPeers(String grp){
        String res = null;
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select count(membername) from queue_members where queue_name='"+grp+"'");
            if(db.resultado.next())
                res=db.resultado.getString(1);
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String getPickupId(String grp){
        String res = null;
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select pickup_id from grppickup where name='"+grp+"'");
            if(db.resultado.next())
                res=db.resultado.getString(1);
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String createPeer(String peer,String secret,String name,String lang,String tone,String nat,
            String qualify,String record,String callLimit,int pickupGroup,String codecs,String featPass,String mailbox,String email,String comp){
        String res;
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            int ins=db.Comando.executeUpdate("insert into peers (name,callerid,context,dtmfmode,host,language,nat,pickupgroup,"
                    + "qualify,secret,type,allow,canal,call_limit,disallow,record,password,dnd,mailbox,email,company) values ('"
                    + peer+"','"+name+"','jpbxRoute','"+tone+"','dynamic','"+lang+"','"+nat+"',"+pickupGroup+",'"
                    + qualify+"','"+secret+"','friend','"+codecs+"','SIP/"+peer+"','"+callLimit+"','all','"+record+"',"+featPass+",0,"
                    + "'"+mailbox+"','"+email+"',"+comp+")");
            
            //=============================================================================================
            res=writePeers();
            res=writeVoicemail();
        } catch (SQLException ex) {
            res="Falha no banco\n"+ex.getMessage();
        }db.disconnectDB();
        return res;
    }
    public String listPickupGroups(String peer){
        String res="";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select name from grppickup");
            while(db.resultado.next()){
                if(db.resultado.getString(1).equals(peer)){
                    res+="<option selected=\"selected\">"+db.resultado.getString(1)+"</option>";
                }
                else{
                    res+="<option>"+db.resultado.getString(1)+"</option>";
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(JpbxDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        db.disconnectDB();
        return res;
    }
    public String listMohs(){
        String res="";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select name,active,moh from mohs");
            while(db.resultado.next()){
                if(db.resultado.getString(2).equals("1")){
                    res+="<option name=\""+db.resultado.getString(3)+"\" selected=\"selected\">"+db.resultado.getString(1)+"</option>";
                }
                else{
                    res+="<option name=\""+db.resultado.getString(3)+"\">"+db.resultado.getString(1)+"</option>";
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(JpbxDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        db.disconnectDB();
        return res;
    }
    public String listMohsDialPlan(){
        String res="";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select moh,name from mohs order by name");
            while(db.resultado.next()){
                res+="<option value=\""+db.resultado.getString(2)+"/"
                        + ""+db.resultado.getString(1).substring(0, db.resultado.getString(1).indexOf(".wav"))+
                        "\">"+db.resultado.getString(2)+"</option>";
            }
        } catch (SQLException ex) {
            Logger.getLogger(JpbxDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        db.disconnectDB();
        return res;
    }
    public String listMohsDialPlan(String song){
        String res="";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select moh,name from mohs order by name");
            while(db.resultado.next()){
                if(db.resultado.getString(2).equals(song)){
                    res+="<option selected value=\""+db.resultado.getString(2)+"/"
                        + ""+db.resultado.getString(1).substring(0, db.resultado.getString(1).indexOf(".wav"))+
                        "\">"+db.resultado.getString(2)+"</option>";
                }
                else{
                    res+="<option value=\""+db.resultado.getString(2)+"/"
                        + ""+db.resultado.getString(1).substring(0, db.resultado.getString(1).indexOf(".wav"))+
                        "\">"+db.resultado.getString(2)+"</option>";
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(JpbxDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        db.disconnectDB();
        return res;
    }
    public String alterMoh(String moh){
        String res="ok",selectedMoh = moh;
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select name from mohs where active='1'");
            if(db.resultado.next())
                selectedMoh=db.resultado.getString(1);
            if(!moh.equals(selectedMoh)){
                int sql=db.Comando.executeUpdate("update mohs set active='0'");
                sql=db.Comando.executeUpdate("update mohs set active='1' where name='"+moh+"'");
                res=astFileConvert(moh);
            }
        } catch (SQLException ex) {
                res=ex.getMessage();
        }
        return res;
    }
    public void addMoh(String moh){
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            int sql=db.Comando.executeUpdate("insert into mohs (moh) values ('"+moh+"')");
        } catch (SQLException ex) {
            Logger.getLogger(JpbxDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public String astFileConvert(String moh){
        String res="ok";
        File makefile=new File("webapps/jpbx/asterisk/mohDefault.conf");
        FileWriter fwrite;
        try {
            fwrite = new FileWriter(makefile);
            fwrite.write(";;Arquivo escrito automaticamente pelo sistema JPBX\n"
                    + ";;Portanto inutil sua edicao manual\n\n"); 
            fwrite.write("[default]\n");
            fwrite.write("mode=files\n");
            fwrite.write("directory=moh/"+moh);
            fwrite.flush();
            fwrite.close();
        } catch (IOException ex) {
            res=ex.getMessage();
        }
        if(res.equals("ok")){
            try{
                Asterisk ast=new Asterisk();
                ast.AstConnect();
                List<String> foo=ast.getInfos("moh reload");
                ast.AstDisconnect();           
            } catch (TimeoutException ex) {
                res=ex.getMessage();
            } catch (AuthenticationFailedException ex) {
                res=ex.getMessage();
            } catch (IOException ex){
                res=ex.getMessage();
            }
        }
        return res;
    }
    public int parsePickupGroup(String pickupGroup){
        int res=1;
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select pickup_id from grppickup where name='"+pickupGroup+"'");
            if(db.resultado.next()) {
                res=Integer.parseInt(db.resultado.getString(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(JpbxDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        db.disconnectDB();
        return res;
    }
    public boolean testDuplicate(String peer){
        boolean res=false;
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select count(name) from peers where name="+peer);
            if(db.resultado.next()){
                if(Integer.parseInt(db.resultado.getString(1))==0){
                    res=true;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(JpbxDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        db.disconnectDB();
        return res;
    }
    public void deletePeer(String peer){
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            int res=db.Comando.executeUpdate("delete from peers where name="+peer);
            writePeers();
        } catch (SQLException ex) {
            Logger.getLogger(JpbxDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        db.disconnectDB();
    }
    public String deleteUser(String userId){
        String res="ok";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            int sql=db.Comando.executeUpdate("delete from users where idusers="+userId);
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String writePeers(){
        String res="ok";
        File makefile=new File("webapps/jpbx/asterisk/sip.conf");
        JpbxDBConnect db=new JpbxDBConnect();
        try {   
            FileWriter fwrite=new FileWriter(makefile);
            db.resultado=db.Comando.executeQuery("select name,type,host,secret,language,dtmfmode,nat,"
                    + "qualify,disallow,allow,call_limit from peers where name != 'admin'");
            fwrite.write(";Arquivo escrito automaticamente pelo sistema JPBX\n"
                    + ";Portanto inutil sua edicao manual\n\n");
        
            while(db.resultado.next()){
                fwrite.write("["+db.resultado.getString("name") +"](security)\n");
                fwrite.write("type="+db.resultado.getString("type")+"\n");
                fwrite.write("context=Jpbx-Peers\n");
                fwrite.write("host="+db.resultado.getString("host")+"\n");
                fwrite.write("md5secret="+db.resultado.getString("secret")+"\n");
                fwrite.write("language="+db.resultado.getString("language")+"\n");
                fwrite.write("canreinvite=no\n");
                fwrite.write("dtmfmode="+db.resultado.getString("dtmfmode")+"\n");
                fwrite.write("nat="+db.resultado.getString("nat")+"\n");
                fwrite.write("qualify="+db.resultado.getString("qualify")+"\n");
                fwrite.write("disallow="+db.resultado.getString("disallow")+"\n");
                fwrite.write("allow="+db.resultado.getString("allow").replaceAll(",---", "")+"\n");
                fwrite.write("call-limit="+db.resultado.getString("call_limit")+"\n");
                fwrite.write("\n");
            }
            fwrite.flush();
            fwrite.close();
       
            Asterisk ast=new Asterisk();
            db.disconnectDB();
            try {
                ast.AstConnect();
                List<String> foo=ast.getInfos("sip reload");
                ast.AstDisconnect();
            } catch (AuthenticationFailedException ex) {
                res="Falha de login Asterisk\n"+ex.getMessage();
            } catch (TimeoutException ex) {
                res="Tempo de retorno Asterisk demorou\n"+ex.getMessage();
            }
            } catch (SQLException ex) {
                res="Falha no banco\n"+ex.getMessage();
            } catch(IOException io){
                res="Falha no banco\n"+io.getMessage();
            }
        return res;
    }
    public String writeTrunkSIP(){
        String res="ok";
        File makefile=new File("webapps/jpbx/asterisk/sip-trunk.conf");
        File makefile2=new File("webapps/jpbx/asterisk/sip-trunksR.conf");
        JpbxDBConnect db=new JpbxDBConnect();
        try {   
            FileWriter fwrite=new FileWriter(makefile);
            FileWriter fwrite2=new FileWriter(makefile2);
            db.resultado=db.Comando.executeQuery("select trunk_name,type,host,secret,language,dtmfmode,nat,"
                    + "qualify,disallow,allow,context,reinvite,canreinvite,call_limit,insecure,username from trunks where tecnology in ('SIP','Virtual')");
            fwrite.write(";Arquivo escrito automaticamente pelo sistema JPBX\n"
                    + ";Portanto inutil sua edicao manual\n\n");
        
            while(db.resultado.next()){
                fwrite.write("["+db.resultado.getString("username") +"]\n");
                fwrite.write("username="+db.resultado.getString("username")+"\n");
                fwrite.write("type="+db.resultado.getString("type")+"\n");
                fwrite.write("context="+db.resultado.getString("context")+"\n");
                fwrite.write("host="+db.resultado.getString("host")+"\n");
                fwrite.write("secret="+db.resultado.getString("secret")+"\n");
                fwrite.write("language="+db.resultado.getString("language")+"\n");
                fwrite.write("dtmfmode="+db.resultado.getString("dtmfmode")+"\n");
                fwrite.write("nat="+db.resultado.getString("nat")+"\n");
                fwrite.write("qualify="+db.resultado.getString("qualify")+"\n");
                fwrite.write("disallow="+db.resultado.getString("disallow")+"\n");
                fwrite.write("allow="+db.resultado.getString("allow").replaceAll(",---", "")+"\n");
                fwrite.write("defaultuser="+db.resultado.getString("username")+"\n");
                fwrite.write("fromuser="+db.resultado.getString("username")+"\n");
                fwrite.write("insecure="+db.resultado.getString("insecure")+"\n");
                fwrite.write((!db.resultado.getString("call_limit").equals("0")?"call-limit="+db.resultado.getString("call_limit")+"\n":""));
                fwrite.write((!db.resultado.getString("reinvite").equals("")?"reinvite="+db.resultado.getString("reinvite")+"\n":""));
                fwrite.write((!db.resultado.getString("canreinvite").equals("")?"canreinvite="+db.resultado.getString("canreinvite")+"\n":""));
                fwrite.write("\n");
            }
            fwrite.flush();
            fwrite.close();
            
            db.resultado=db.Comando.executeQuery("select reception,register,num_recebimento from trunks"
                    + " where tecnology='SIP'");
            fwrite2.write(";Arquivo escrito automaticamente pelo sistema JPBX\n"
                    + ";Portanto inutil sua edicao manual\n\n");
            
            while(db.resultado.next()){
                if(db.resultado.getString("reception").equals("yes")){
                    String exten=db.resultado.getString("num_recebimento")!=null
                            ?"/"+db.resultado.getString("num_recebimento"):"";
                    fwrite2.write("register => "+db.resultado.getString("register")+exten+"\n");
                }
            }
            fwrite2.flush();
            fwrite2.close();
       
            Asterisk ast=new Asterisk();
            db.disconnectDB();
            try {
                ast.AstConnect();
                List<String> foo=ast.getInfos("sip reload");
                ast.AstDisconnect();
            } catch (AuthenticationFailedException ex) {
                res="Falha de login Asterisk\n"+ex.getMessage();
            } catch (TimeoutException ex) {
                res="Tempo de retorno Asterisk demorou\n"+ex.getMessage();
            }
            } catch (SQLException ex) {
                res="Falha no banco\n"+ex.getMessage();
            } catch(IOException io){
                res="Falha no banco\n"+io.getMessage();
            }
        return res;
    }
    public String writeTrunkIax(){
        String res="ok";
        File makefile=new File("webapps/jpbx/asterisk/iax2-trunk.conf");
        JpbxDBConnect db=new JpbxDBConnect();
        try {   
            FileWriter fwrite=new FileWriter(makefile);
            db.resultado=db.Comando.executeQuery("select trunk_name,type,secret,dtmfmode,host,qualify,nat,"
                    + "allow,disallow from trunks where tecnology='IAX2'");
            
            fwrite.write(";Arquivo escrito automaticamente pelo sistema JPBX\n"
                    + ";Portanto inutil sua edicao manual\n\n");
        
            while(db.resultado.next()){
                fwrite.write("["+db.resultado.getString("trunk_name") +"]\n");
                fwrite.write("username="+db.resultado.getString("trunk_name")+"\n");
                fwrite.write("type="+db.resultado.getString("type")+"\n");
                fwrite.write("context=Jpbx-Trunks\n");
                fwrite.write("host="+db.resultado.getString("host")+"\n");
                fwrite.write("secret="+db.resultado.getString("secret")+"\n");
                //fwrite.write("language="+db.resultado.getString("language")+"\n");
                fwrite.write("canreinvite=no\n");
                fwrite.write("dtmfmode="+db.resultado.getString("dtmfmode")+"\n");
                fwrite.write("nat="+db.resultado.getString("nat")+"\n");
                fwrite.write("qualify="+db.resultado.getString("qualify")+"\n");
                fwrite.write("disallow="+db.resultado.getString("disallow")+"\n");
                fwrite.write("allow="+db.resultado.getString("allow").replaceAll(",---", "")+"\n");
                //fwrite.write("call-limit="+db.resultado.getString("call_limit")+"\n");
                fwrite.write("\n");
            }
            fwrite.flush();
            fwrite.close();
       
            Asterisk ast=new Asterisk();
            db.disconnectDB();
            try {
                ast.AstConnect();
                List<String> foo=ast.getInfos("iax2 reload");
                ast.AstDisconnect();
            } catch (AuthenticationFailedException ex) {
                res="Falha de login Asterisk\n"+ex.getMessage();
            } catch (TimeoutException ex) {
                res="Tempo de retorno Asterisk demorou\n"+ex.getMessage();
            }
            } catch (SQLException ex) {
                res="Falha no banco\n"+ex.getMessage();
            } catch(IOException io){
                res="Falha no banco\n"+io.getMessage();
            }
        return res;
    }
    public String[] editPeer(String peer){
        JpbxDBConnect db=new JpbxDBConnect();
        String res[]=new String[15];
        try {
            db.resultado=db.Comando.executeQuery("select name,secret,callerid,language,dtmfmode,nat,"
                    + "qualify,record,call_limit,(select name from grppickup where pickup_id=pickupgroup),password,allow,"
                    + "mailbox,email,company"
                    + " from peers where name="+peer);
            
            while(db.resultado.next()){
                res[0]=db.resultado.getString(1);
                res[1]=db.resultado.getString(2);
                res[2]=db.resultado.getString(3);
                res[3]=db.resultado.getString(4);
                res[4]=db.resultado.getString(5);
                res[5]=db.resultado.getString(6);
                res[6]=db.resultado.getString(7);
                res[7]=db.resultado.getString(8);
                res[8]=db.resultado.getString(9);
                res[9]=db.resultado.getString(10);
                res[10]=db.resultado.getString(11);
                res[11]=db.resultado.getString(12);
                res[12]=db.resultado.getString(13);
                res[13]=db.resultado.getString(14);
                res[14]=db.resultado.getString(15);
            }
        } catch (SQLException ex) {
            Logger.getLogger(JpbxDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        db.disconnectDB();
        return res;
    }
    public String[] editCallGrp(String idGrp){
        JpbxDBConnect db=new JpbxDBConnect();
        String res[]=new String[5];
        try {
            db.resultado=db.Comando.executeQuery("select name,strategy,timeout,musiconhold,company from queues where queues_id="+idGrp+"");
            
            while(db.resultado.next()){
                res[0]=db.resultado.getString(1);
                res[1]=db.resultado.getString(2);
                res[2]=db.resultado.getString(3);
                res[3]=db.resultado.getString(4);
                res[4]=db.resultado.getString(5);
            }
        } catch (SQLException ex) {
            res[0]=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String[] editUser(String user){
        JpbxDBConnect db=new JpbxDBConnect();
        String res[]=new String[6];
        try {
            db.resultado=db.Comando.executeQuery("select idusers,name,password,timeout,nivel,company"
                    + " from users where idusers="+user);
            
            if(db.resultado.next()){
                res[0]=db.resultado.getString(1);
                res[1]=db.resultado.getString(2);
                res[2]=db.resultado.getString(3);
                res[3]=db.resultado.getString(4);
                res[4]=db.resultado.getString(5);
                res[5]=db.resultado.getString(6);
            }
        } catch (SQLException ex) {
            Logger.getLogger(JpbxDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        db.disconnectDB();
        return res;
    }
    public String[] editCompany(String comp){
        JpbxDBConnect db=new JpbxDBConnect();
        String res[]=new String[2];
        try {
            db.resultado=db.Comando.executeQuery("select name,obs"
                    + " from companys where idcompanys="+comp);
            
            if(db.resultado.next()){
                res[0]=db.resultado.getString(1);
                res[1]=db.resultado.getString(2);
            }
        } catch (SQLException ex) {
            Logger.getLogger(JpbxDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        db.disconnectDB();
        return res;
    }
    public String alterPeer(String peer,String secret,String name,String lang,String tone,String nat,
            String qualify,String record,String callLimit,int pickupGroup,String featPass,String codec,String mailbox,String email,String comp){
        String res;
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            int up=db.Comando.executeUpdate("update peers set name='"+peer+"',callerid='"+name+"',context='jpbxRoute',"
                    + "dtmfmode='"+tone+"',host='dynamic',language='"+lang+"',nat='"+nat+"',pickupgroup='"+pickupGroup+"',"
                    + "qualify='"+qualify+"',secret='"+secret+"',type='friend',allow='"+codec+"',canal='SIP/"+peer+"',"
                    + "call_limit='"+callLimit+"',disallow='all',record='"+record+"',password='"+featPass+"',dnd='0',"
                    + "mailbox='"+mailbox+"',email='"+email+"',company="+comp+" "
                    + "where name='"+peer+"'");
            //=============================================================================================
        } catch (SQLException ex) {
            res="Falha no banco\n"+ex.getMessage();
        }
        db.disconnectDB();
        res=writePeers();
        return res;
    }
    public String alterPeer(String peer,String name,String lang,String tone,String nat,
            String qualify,String record,String callLimit,int pickupGroup,String featPass,String codec,String mailbox,String email,String comp){
        String res;
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            int up=db.Comando.executeUpdate("update peers set name='"+peer+"',callerid='"+name+"',context='jpbxRoute',"
                    + "dtmfmode='"+tone+"',host='dynamic',language='"+lang+"',nat='"+nat+"',pickupgroup='"+pickupGroup+"',"
                    + "qualify='"+qualify+"',type='friend',allow='"+codec+"',canal='SIP/"+peer+"',"
                    + "call_limit='"+callLimit+"',disallow='all',record='"+record+"',password='"+featPass+"',dnd='0',"
                    + "mailbox='"+mailbox+"',email='"+email+"',company="+comp+" "
                    + "where name='"+peer+"'");
            //=============================================================================================
        } catch (SQLException ex) {
            res="Falha no banco\n"+ex.getMessage();
        }
        db.disconnectDB();
        res=writePeers();
        res=writeVoicemail();
        return res;
    }
    public String listTrunks(){
        JpbxDBConnect db=new JpbxDBConnect();
        String edit = null,link,res="<table class=\"span11 table-striped table-condensed table-hover table-bordered\">"
                + "<tr><th>Código</th><th>Nome</th><th>Tecnologia</th><th>Status do Link</th><th>Ações</th><th></th></tr>";
        try {
            db.resultado=db.Comando.executeQuery("select trunk_id,trunk_name,tecnology,host,language"
                    + " from trunks");            
            while(db.resultado.next()){
                link="";
                if(db.resultado.getString(3).equals("SIP")){
                    edit="alterTrunkSip.jsp?trk="+db.resultado.getString(2);
                    link=statusLinkTrunk(db.resultado.getString(4));
                }
                else if(db.resultado.getString(3).equals("IAX2")){
                    edit="alterTrunkIax.jsp?trk="+db.resultado.getString(2);
                    link=statusLinkTrunk(db.resultado.getString(4));
                }
                else if(db.resultado.getString(3).equals("Virtual")){
                    edit="alterTrunkVirtual.jsp?trk="+db.resultado.getString(2);
                    link=statusLinkTrunk(db.resultado.getString(4));
                }
                else if(db.resultado.getString(3).equals("Digital"))
                    edit="alterTrunkDigital.jsp?trk="+db.resultado.getString(2);
                else if(db.resultado.getString(3).equals("Manual"))
                    edit="alterTrunkManual.jsp?trk="+db.resultado.getString(2);
                else
                    edit="#";
                res+="<tr><td>"+db.resultado.getString(1)+"</td>"
                        + "<td>"+db.resultado.getString(2)+"</td>"
                        + "<td>"+db.resultado.getString(3)+"</td>"
                        + "<td>"+link+"</td>"
                        //+ "<td>"+db.resultado.getString(5)+"</td>"
                        + "<td><a rel=\"tooltip\" data-placement=\"top\" title=\"Editar tronco\" href=\""+edit+"\" ><img src=\"css/bootstrap/img/icone-editar.gif\"></a></td> "
                        + "<td><a rel=\"tooltip\" data-placement=\"right\" title=\"Apagar tronco\" href=\"#\" onclick=\"deleteTrunk('"+
                            db.resultado.getString(2)+"');\"><img src=\"css/bootstrap/img/deletar.gif\"></a></td></tr>";
            }
            res+="</table>";

        } catch (SQLException ex) {
            res="Falha na lista: "+ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String formatDate(String date){
        String res=null;
        date=date.replaceFirst("/", "s");
        res=date.substring(date.indexOf("/")+1)+"-";
        res+=date.substring(date.indexOf("s")+1, date.indexOf("/"))+"-";
        res+=date.substring(0,date.indexOf("s"));
        return res;
    }
    public String[] countRelCalls(String dateini,String dateend,String order,String src,String dst,String cost,String state,
            String srcseek,String dstseek){
        JpbxDBConnect db=new JpbxDBConnect();
        int answer=0,noanswer=0, all=0,others=0,busy=0;
        double val=0;
        String srcSeek,dstSeek;
        if(srcseek.equals("equals"))
            srcSeek=" and src in ('"+src.replaceAll(",", "','")+"')";
        else if(srcseek.equals("begin"))
            srcSeek=" and src like '"+src+"%'";
        else
            srcSeek=" and src like '%"+src+"%'";
        if(dstseek.equals("equals"))
            dstSeek=" and dstfinal in ('"+dst.replaceAll(",", "','")+"')";
        else if(dstseek.equals("begin"))
            dstSeek=" and dstfinal like '"+dst+"%'";
        else
            dstSeek=" and dstfinal like '%"+dst+"%'";
        String filterSrc=(!src.equals("")?srcSeek:"");
        String filterDst=(!dst.equals("")?dstSeek:"");
        String filterCost=cost,filterStatus=state;
        String res[]=new String[6];
        if(filterCost.equals("Todos"))
            filterCost="";
        else if(filterCost.equals("Indefinido"))
            filterCost=" and accountcode='' ";
        else
            filterCost=" and accountcode="+cost.substring(0,cost.indexOf(" "));
        if(filterStatus.equals("all"))
            filterStatus="";
        else
            filterStatus=" and disposition='"+state+"'";
        try{
            db.resultado=db.Comando.executeQuery("select disposition,round(tarifador(accountcode,billsec),2) "
                    + "from relcalls where calldate between '"+formatDate(dateini)+" 00:00:00' and '"+formatDate(dateend)+" 23:59:59' "
                    + filterSrc
                    + filterDst
                    + filterCost
                    + filterStatus);
            while(db.resultado.next()){
                if(db.resultado.getString(1).equals("ANSWERED")){
                    answer++;
                    all++;
                }
                else if(db.resultado.getString(1).equals("NO ANSWER")){
                    noanswer++;
                    all++;
                }
                else if(db.resultado.getString(1).equals("BUSY")){
                    busy++;
                    all++;
                }
                else{
                    others++;
                    all++;
                }
                val+=Double.parseDouble(db.resultado.getString(2));
            }
            String frac,strVal=String.valueOf(val);
            frac=strVal.substring(strVal.indexOf("."));
            if(frac.length()<3)
                strVal=strVal+"0";
            else if(frac.length()>3){
                int pos=strVal.indexOf(".");
                strVal=strVal.substring(0, (pos+3));
            }
            res[0]="<h3>Ligações de "+dateini+" até "+dateend+"</h3>"
                + "<h5 style=\"padding-left: 10%;color: green;float: left\">Atendidas: "+answer+"</h5>"
                    + "<h5 style=\"padding-left: 5%;color: brown;float: left\">Não Atendidas: "+noanswer+"</h5>"
                    + "<h5 style=\"padding-left: 5%;color: orange;float: left\">Ocupadas: "+busy+"</h5>"
                    + "<h5 style=\"padding-left: 5%;color: red;float: left\">Outras: "+others+"</h5>"
                    + "<h5 style=\"padding-left: 5%;color: blue;float: left\">Ligações: "+all+"</h5>"
                    + "<h5 style=\"padding-left: 5%;float: left\">Total: R$ "+strVal+"</h5>";
            res[1]=String.valueOf(answer);
            res[2]=String.valueOf(noanswer);
            res[3]=String.valueOf(busy);
            res[4]=String.valueOf(others);
            res[5]=String.valueOf(all);
        }catch(SQLException ex){
            res[0]=ex.getMessage();
        }
        return res;
    }
    public String relCalls(String dateini,String dateend,String order,String src,String dst,String cost,String state,
            int startCall,int numCalls,String srcseek,String dstseek){
        JpbxDBConnect db=new JpbxDBConnect();
        int seq=startCall;
        String srcSeek,dstSeek;
        if(srcseek.equals("equals"))
            srcSeek=" and src in ('"+src.replaceAll(",", "','")+"')";
        else if(srcseek.equals("begin"))
            srcSeek=" and src like '"+src+"%'";
        else
            srcSeek=" and src like '%"+src+"%'";
        if(dstseek.equals("equals"))
            dstSeek=" and dstfinal in ('"+dst.replaceAll(",", "','")+"')";
        else if(dstseek.equals("begin"))
            dstSeek=" and dstfinal like '"+dst+"%'";
        else
            dstSeek=" and dstfinal like '%"+dst+"%'";
        String filterSrc=(!src.equals("")?srcSeek:"");
        String filterDst=(!dst.equals("")?dstSeek:"");
        String filterCost=cost,filterStatus=state;
        if(filterCost.equals("Todos"))
            filterCost="";
        else if(filterCost.equals("Indefinido"))
            filterCost=" and accountcode='' ";
        else
            filterCost=" and accountcode="+cost.substring(0,cost.indexOf(" "));
        if(filterStatus.equals("all"))
            filterStatus="";
        else
            filterStatus=" and disposition='"+state+"'";
        String status = null,
                res="<table class=\"span11 table-striped table-hover table-bordered\">"
                + "<tr><th>Seq</th><th>Data Hora</th><th>Origem</th><th>Destino</th><th>Estado Final</th><th>Duração</th>"
                + "<th>Conversação</th><th>Custo</th><th>Valor</th><th>Ações</th></tr>";
        
        try {
            db.resultado=db.Comando.executeQuery("select date_format(calldate,'%d/%m/%Y %H:%i:%s'),src,dstfinal,disposition,formatSecs(duration),formatSecs(billsec),"
                    + "(select ccost_name from ccosts where ccost=accountcode),"
                    + "round(tarifador(accountcode,billsec),2),uniqueid "
                    + "from relcalls where calldate between '"+formatDate(dateini)+" 00:00:00' and '"+formatDate(dateend)+" 23:59:59' "
                    + filterSrc
                    + filterDst
                    + filterCost
                    + filterStatus
                    + " order by "+order.substring(5)+" limit "+startCall+","+numCalls+"");         
            while(db.resultado.next()){ 
                seq++;
                if(db.resultado.getString(4).equals("ANSWERED")){
                    status="Atendida";
                }
                else if(db.resultado.getString(4).equals("NO ANSWER")){
                    status="Não Atendida";
                }
                else if(db.resultado.getString(4).equals("BUSY")){
                    status="Ocupada";
                }
                else if(db.resultado.getString(4).equals("FAILED")){
                    status="Falhada";
                }
                else{
                    status="Desconhecido";
                }
                res+="<tr>"
                        + "<td>"+seq+"</td>"
                        + "<td>"+db.resultado.getString(1)+"</td>"
                        + "<td>"+db.resultado.getString(2)+"</td>"
                        + "<td>"+db.resultado.getString(3)+"</td>"
                        + "<td>"+status+"</td>"
                        + "<td>"+db.resultado.getString(5)+"</td>"
                        + "<td>"+db.resultado.getString(6)+"</td> "
                        + "<td>"+(db.resultado.getString(7)!=null?db.resultado.getString(7):"Indefinido")+"</td>"
                        + "<td>R$ "+db.resultado.getString(8)+"</td>"
                        + "<td><img onclick=\"dialogs('"+db.resultado.getString(9)+"');\" src=\"css/bootstrap/img/lupasmall.png\"> "
                        + "         <img onclick=\"deleteCall('"+db.resultado.getString(9)+"');\" src=\"css/bootstrap/img/deletar.gif\"></td>"
                        + "</tr>";             
            }
            res+="</table>";    
        } catch (SQLException ex) {
            res="Falha no relatório:</br>"+ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String relConferences(String dateini,String dateend,String rooms,int startCall,int numCalls){
        JpbxDBConnect db=new JpbxDBConnect();
        int seq=startCall;
        String filterRooms="";
        if(!rooms.equals(""))
            filterRooms=" and room in ('"+rooms.replaceAll(",", "','")+"') ";
        String res="<table class=\"span11 table-striped table-hover table-bordered\">"
                + "<tr><th>Seq</th><th>Data Hora</th><th>Sala</th><th>Responsável</th><th>Gravação</th>"
                + "<th>Ações</th></tr>";
        try {
            db.resultado=db.Comando.executeQuery("select date_format(datetime,'%d/%m/%Y %H:%i:%s'),room,responsable,record,id_conference "
                    + "from rel_conferences where datetime between '"+formatDate(dateini)+" 00:00:00' and '"+formatDate(dateend)+" 23:59:59' "
                    + filterRooms
                    + " limit "+startCall+","+numCalls+"");         
            while(db.resultado.next()){ 
                seq++;
                res+="<tr>"
                        + "<td>"+seq+"</td>"
                        + "<td>"+db.resultado.getString(1)+"</td>"
                        + "<td>"+db.resultado.getString(2)+"</td>"
                        + "<td>"+db.resultado.getString(3)+"</td>"
                        + "<td><audio controls><source src=\"asterisk/records/"+db.resultado.getString(4)+"\" type=\"audio/wav\"></audio></td>"
                        + "<td><img onclick=\"deleteConference("+db.resultado.getString(5)+",'"+db.resultado.getString(4)+"');\" src=\"css/bootstrap/img/deletar.gif\"></td>"
                        + "</tr>";             
            }
            res+="</table>";    
        } catch (SQLException ex) {
            res="Falha no relatório:</br>"+ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String relConferences(String dateini,String dateend,String rooms,int startCall,int numCalls,String comp){
        JpbxDBConnect db=new JpbxDBConnect();
        int seq=startCall;
        String filterRooms="";
        if(!rooms.equals(""))
            filterRooms=" and room in ('"+rooms.replaceAll(",", "','")+"') ";
        String res="<table class=\"span11 table-striped table-hover table-bordered\">"
                + "<tr><th>Seq</th><th>Data Hora</th><th>Sala</th><th>Responsável</th><th>Gravação</th>"
                + "<th>Ações</th></tr>";
        try {
            db.resultado=db.Comando.executeQuery("select date_format(datetime,'%d/%m/%Y %H:%i:%s'),room,responsable,record,id_conference,company "
                    + "from rel_conferences where datetime between '"+formatDate(dateini)+" 00:00:00' and '"+formatDate(dateend)+" 23:59:59' "
                    + filterRooms+" having company="+comp
                    + " limit "+startCall+","+numCalls);         
            while(db.resultado.next()){ 
                seq++;
                res+="<tr>"
                        + "<td>"+seq+"</td>"
                        + "<td>"+db.resultado.getString(1)+"</td>"
                        + "<td>"+db.resultado.getString(2)+"</td>"
                        + "<td>"+db.resultado.getString(3)+"</td>"
                        + "<td><audio controls><source src=\"asterisk/records/"+db.resultado.getString(4)+"\" type=\"audio/wav\"></audio></td>"
                        + "<td><img onclick=\"deleteConference("+db.resultado.getString(5)+",'"+db.resultado.getString(4)+"');\" src=\"css/bootstrap/img/deletar.gif\"></td>"
                        + "</tr>";             
            }
            res+="</table>";    
        } catch (SQLException ex) {
            res="Falha no relatório:</br>"+ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String countRelConferences(String dateini,String dateend,String rooms){
        JpbxDBConnect db=new JpbxDBConnect();
        String filterRooms="";
        if(!rooms.equals(""))
            filterRooms=" and room in ('"+rooms.replaceAll(",", "','")+"') ";
        String res="0";
        try {
            db.resultado=db.Comando.executeQuery("select count(room) "
                    + "from rel_conferences where datetime between '"+formatDate(dateini)+" 00:00:00' and '"+formatDate(dateend)+" 23:59:59' "
                    + filterRooms);         
            if(db.resultado.next()){ 
                res=db.resultado.getString(1);
            } 
        } catch (SQLException ex) {
            res="Falha no relatório:</br>"+ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String countRelConferences(String dateini,String dateend,String rooms,String comp){
        JpbxDBConnect db=new JpbxDBConnect();
        String filterRooms="";
        if(!rooms.equals(""))
            filterRooms=" and room in ('"+rooms.replaceAll(",", "','")+"') ";
        String res="0";
        try {
            db.resultado=db.Comando.executeQuery("select room,company "
                    + "from rel_conferences where datetime between '"+formatDate(dateini)+" 00:00:00' and '"+formatDate(dateend)+" 23:59:59' "
                    + filterRooms+ "having company="+comp);  
            int count=0;
            while(db.resultado.next()){ 
                count++;
                res=String.valueOf(count);
            } 
        } catch (SQLException ex) {
            res="Falha no relatório:</br>"+ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String listDialplan(){
        String edit,imgUp="",active,res ="<table class=\"span11 table-striped table-condensed table-hover table-bordered\">"
                + "<tr><th>Código</th><th>Execução</th><th>Regra</th><th>Origem</th><th>Destino</th><th>Ações</th></tr>";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select rule_order,rule_name,src,dst,active,rule_id,alias,alias_name "
                    + "from dialplan_rules "
                    + "order by dst,rule_order");
            while(db.resultado.next()){
                if(!db.resultado.getString(1).equals("1"))
                    imgUp="<img src=\"css/bootstrap/img/up.png\" onclick=dialPlanRuleOrder("+db.resultado.getString(6)+"); />";
                    edit="<a rel=\"tooltip\" data-placement=\"left\" title=\"Editar Regra\" href=\"alterDialPlan.jsp?rule="
                        +db.resultado.getString(6)+"\" ><img src=\"css/bootstrap/img/icone-editar.gif\"></a>";
                    if(db.resultado.getString(5).equals("0"))
                        active="<a rel=\"tooltip\" data-placement=\"top\" title=\"Ativar regra\" href=\"#\" >"
                        + "<img src=\"css/bootstrap/img/red-signals.png\" onclick=\"activeRule("+
                            db.resultado.getString(6)+",'"+db.resultado.getString(2)+"',1);\"></a>";
                    else
                        active="<a rel=\"tooltip\" data-placement=\"top\" title=\"Desativar Regra\" href=\"#\" >"
                        + "<img src=\"css/bootstrap/img/green-signals.png\" onclick=\"activeRule("+
                            db.resultado.getString(6)+",'"+db.resultado.getString(2)+"',0);\"></a>";
                res+="<tr class=\""+(db.resultado.getString(1).equals("1")?"":"text-info")+"\">"
                        + "<td>"+db.resultado.getString(6)+"</td>"
                        + "<td>"+db.resultado.getString(1)+"</td>"
                        + "<td>"+db.resultado.getString(2)+"</td>"
                        + "<td>"+db.resultado.getString(3)+"</td>"
                        + "<td>"+(db.resultado.getString(7).equals("1")?db.resultado.getString(8):db.resultado.getString(4))+"</td>"
                        + "<td>"+edit+"&nbsp;&nbsp;&nbsp;<a rel=\"tooltip\" data-placement=\"top\" title=\"Apagar Regra\" href=\"#\" >"
                        + "<img src=\"css/bootstrap/img/deletar.gif\" onclick=\"deleteDialPlan("+
                            db.resultado.getString(6)+",'"+db.resultado.getString(2)+"');\"></a>&nbsp;&nbsp;&nbsp;"+active+"&nbsp;&nbsp;&nbsp;"+imgUp+"</td>"
                        + "</tr>";
                imgUp="";
            }
            res+="</table>";
        } catch (SQLException ex) {
            res="Falha na lista:</br>"+ex.getMessage();
        }
        return res;
    }
    public String peerSuggest(){
        String res=null;
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select max(name)+1 from peers");
            if(db.resultado.next()){
                res=db.resultado.getString(1);
            }
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String createTrunk(String trunk_name,String lang,String dtmf,String host,String qualify,
            String nat,String username,String secret,String codecs,String tech,String reception,
            String register,String num_rece,String reinvite,String canreinvite,String limit){
        String res="ok";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            int sql;
            sql = db.Comando.executeUpdate("insert into trunks (trunk_name,type,context,"
                  + "language,canreinvite,dtmfmode,host,qualify,nat,disallow,allow,username,"
                  + "secret,tecnology,reception,register,num_recebimento,canal,reinvite,call_limit,insecure) values("
                  + "'"+trunk_name+"','friend','Jpbx-Trunks','"+lang+"','"+canreinvite+"','"+dtmf+"','"+host+"',"
                  + "'"+qualify+"','"+nat+"','all','"+codecs+"','"+username+"','"+secret+"',"
                  + "'"+tech+"','"+reception+"','"+register+"','"+num_rece+"','"+tech+"/"+username+"',"
                  + "'"+reinvite+"',"+(limit.equals("")?"0":limit)+",'invite,port')");            
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        res=writeTrunkSIP();
        return res;
    }
    public String createTrunkIax(String trunk_name,String secret,String host,String dtmfmode,
            String nat,String qualify,String allow,String lang){
        String res="ok";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            int sql=db.Comando.executeUpdate("insert into trunks (trunk_name,type,secret,language,"
                    + "context,dtmfmode,host,qualify,nat,allow,username,tecnology,canal) "
                    + "values('"+trunk_name+"','friend','"+secret+"','"+lang+"','Jpbx-Trunks',"
                    + "'"+dtmfmode+"','"+host+"','"+qualify+"','"+nat+"','"+allow+"','"+trunk_name+"',"
                    + "'IAX2','IAX2/"+trunk_name+"')");
        } catch (SQLException ex) {
            res="Falha no banco\n"+ex.getMessage();
        }
        db.disconnectDB();
        res=writeTrunkIax();
        return res;
    }
    public String createTrunkVirtual(String trunk_name,String secret,String host,String dtmfmode,
            String nat,String qualify,String allow,String lang){
        String res="ok";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            int sql=db.Comando.executeUpdate("insert into trunks (trunk_name,type,secret,language,"
                    + "context,dtmfmode,host,qualify,nat,allow,username,tecnology,canal) "
                    + "values('"+trunk_name+"','friend','"+secret+"','"+lang+"','Jpbx-Trunks',"
                    + "'"+dtmfmode+"','"+host+"','"+qualify+"','"+nat+"','"+allow+"','"+trunk_name+"',"
                    + "'Virtual','SIP/"+trunk_name+"')");
        } catch (SQLException ex) {
            res="Falha no banco\n"+ex.getMessage();
        }
        db.disconnectDB();
        res=writeTrunkSIP();
        return res;
    }
    public String createTrunkDigital(String board,String trunk_name,String numboard,String link,String lang){
        String res="ok";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            int sql=db.Comando.executeUpdate("insert into trunks (trunk_name,number_board,number_link,language,canal,tecnology,desc_channel) "
                    + "values ('"+trunk_name+"',"+numboard+","+link+",'"+lang+"','"+board+"/B"+numboard+"L"+link+"','Digital','"+board+"')");
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String createPickupGrp(String grp){
        String res="ok";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            int sql=db.Comando.executeUpdate("insert into grppickup (name) values ('"+grp+"')");
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String createCost(String cost,String type){
        String res="ok";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select max(ccost)+0.01 from ccosts "
                    + "where ccost_name='"+type+"'");
            if(db.resultado.next()){
                int sql=db.Comando.executeUpdate("insert into ccosts (ccost_name,ccost) values ('"+cost+"',"+db.resultado.getString(1)+")");
            }
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String deleteCost(String cost){
        String res="ok";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            int sql=db.Comando.executeUpdate("delete from ccosts where idccosts="+cost+"");
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String alterCost(int costId,float fare,int cicle,int fraction,int shortage){
        String res="ok";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            int sql=db.Comando.executeUpdate("update ccosts set fare="+fare+",cicle="+cicle+","
                    + "fraction="+fraction+",shortage="+shortage+" where idccosts="+costId+"");
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String createUser(String user,String pass,String nivel,String timeout,String comp){
        String res="ok";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            int sql=db.Comando.executeUpdate("insert into users (name,password,timeout,nivel,company) "
                    + "values ('"+user+"','"+pass+"',"+timeout+",'"+nivel+"',"+comp+")");
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String alterUser(String id,String user,String pass,String nivel,String timeout,String comp){
        String res="ok";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            int sql=db.Comando.executeUpdate("update users set name='"+user+"',password='"+pass+"',"
                    + "timeout="+timeout+",nivel='"+nivel+"',company="+comp+" where idusers="+id+"");
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String alterUser(String id,String user,String nivel,String timeout,String comp){
        String res="ok";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            int sql=db.Comando.executeUpdate("update users set name='"+user+"',"
                    + "timeout="+timeout+",nivel='"+nivel+"',company="+comp+" where idusers="+id+"");
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String alterPickupGrp(String grp,String id){
        String res="ok";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            int sql=db.Comando.executeUpdate("update grppickup set name='"+grp+"' where pickup_id="+id+"");
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String deleteTrunks(String trunk){
        String res = "ok",tech = null;
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select tecnology from trunks where trunk_name='"+trunk+"'");
            if(db.resultado.next())
                tech=db.resultado.getString(1);
            int sql=db.Comando.executeUpdate("delete from trunks where trunk_name='"+trunk+"'");
            if(sql==0)
                res="Tronco "+trunk+" não existe!";
        } catch (SQLException ex) {
            res="Falha no banco\n"+ex.getMessage();
        }
        db.disconnectDB();
        if(tech.equals("IAX2"))
            res=writeTrunkIax();
        else if(tech.equals("SIP")||tech.equals("Virtual"))
            res=writeTrunkSIP();
        return res;
    }
    public String seekTrunkName(String trunkName){
        String res=null;
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select count(trunk_name) from trunks "
                    + "where trunk_name='"+trunkName+"'");
            if(db.resultado.next())
                res=db.resultado.getString(1);
        } catch (SQLException ex) {
            Logger.getLogger(JpbxDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        db.disconnectDB();
        return res;
    }
    public String seekDialPlanName(String dialplanName){
        String res=null;
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select count(rule_name) from dialplan_rules "
                    + "where rule_name='"+dialplanName+"'");
            if(db.resultado.next())
                res=db.resultado.getString(1);
        } catch (SQLException ex) {
            Logger.getLogger(JpbxDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        db.disconnectDB();
        return res;
    }
    public String seekPickupGrpName(String pickupName){
        String res=null;
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select count(name) from grppickup "
                    + "where name='"+pickupName+"'");
            if(db.resultado.next())
                res=db.resultado.getString(1);
        } catch (SQLException ex) {
            Logger.getLogger(JpbxDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        db.disconnectDB();
        return res;
    }
    public String seekCallGrpName(String grpName){
        String res=null;
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select count(name) from queues "
                    + "where name='callGrp&"+grpName+"'");
            if(db.resultado.next())
                res=db.resultado.getString(1);
        } catch (SQLException ex) {
            Logger.getLogger(JpbxDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        db.disconnectDB();
        return res;
    }
    public String seekCosName(String cosName){
        String res=null;
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select count(name) from cos "
                    + "where name='"+cosName+"'");
            if(db.resultado.next())
                res=db.resultado.getString(1);
        } catch (SQLException ex) {
            Logger.getLogger(JpbxDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        db.disconnectDB();
        return res;
    }
    public String seekUserName(String user){
        String res=null;
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select count(name) from users "
                    + "where name='"+user+"'");
            if(db.resultado.next())
                res=db.resultado.getString(1);
        } catch (SQLException ex) {
            Logger.getLogger(JpbxDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        db.disconnectDB();
        return res;
    }
    String seekCompanyName(String comp){
        String res=null;
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select count(name) from companys "
                    + "where name='"+comp+"'");
            if(db.resultado.next())
                res=db.resultado.getString(1);
        } catch (SQLException ex) {
            Logger.getLogger(JpbxDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        db.disconnectDB();
        return res;
    }
    public String seekAliasName(String alias){
        String res=null;
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select count(name) from alias "
                    + "where name='"+alias+"'");
            if(db.resultado.next())
                res=db.resultado.getString(1);
        } catch (SQLException ex) {
            Logger.getLogger(JpbxDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        db.disconnectDB();
        return res;
    }
    public String seekSrcDst(String dst,String src){
        String res=null;
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select count(rule_name) from dialplan_rules "
                    + "where src='"+src+"' and dst='"+dst+"'");
            if(db.resultado.next())
                res=db.resultado.getString(1);
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String seekDialPlanExten(String dialplanExten){
        String res="ok";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select rule_name,src from dialplan_rules "
                    + "where dst='"+dialplanExten+"'");
            if(db.resultado.next())
                res="Regra: "+db.resultado.getString(1);
        } catch (SQLException ex) {
            Logger.getLogger(JpbxDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        db.disconnectDB();
        return res;
    }
    public String[] editTrunks(String trk){
        JpbxDBConnect db=new JpbxDBConnect();
        String res[]=new String[15];
        try {
            db.resultado=db.Comando.executeQuery("select trunk_name,secret,host,dtmfmode,"
                    + "nat,qualify,language,allow,reception,register,num_recebimento,username,reinvite,canreinvite,call_limit "
                    + "from trunks where trunk_name='"+trk+"'");
            
            if(db.resultado.next()){
                res[0]=db.resultado.getString(1);
                res[1]=db.resultado.getString(2);
                res[2]=db.resultado.getString(3);
                res[3]=db.resultado.getString(4);
                res[4]=db.resultado.getString(5);
                res[5]=db.resultado.getString(6);
                res[6]=db.resultado.getString(7);
                res[7]=db.resultado.getString(8);
                res[8]=db.resultado.getString(9);
                res[9]=db.resultado.getString(10);
                res[10]=db.resultado.getString(11);
                res[11]=db.resultado.getString(12);
                res[12]=db.resultado.getString(13);
                res[13]=db.resultado.getString(14);
                res[14]=db.resultado.getString(15);
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(JpbxDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        db.disconnectDB();
        return res;
    }
    public String[] editTrunkDigital(String trk){
        JpbxDBConnect db=new JpbxDBConnect();
        String res[]=new String[5];
        try {
            db.resultado=db.Comando.executeQuery("select trunk_name,language,desc_channel,number_board,number_link "
                    + "from trunks where trunk_name='"+trk+"'");
            
            if(db.resultado.next()){
                res[0]=db.resultado.getString(1);
                res[1]=db.resultado.getString(2);
                res[2]=db.resultado.getString(3);
                res[3]=db.resultado.getString(4);
                res[4]=db.resultado.getString(5);
            }
        } catch (SQLException ex) {
            Logger.getLogger(JpbxDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        db.disconnectDB();
        return res;
    }
    public String alterTrunkIax(String trunk_name,String secret,String host,String lang,String tone,String nat,
            String qualify,String codec){
        String res = "ok";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            int up=db.Comando.executeUpdate("update trunks set secret='"+secret+"',host='"+host+"',"
                    + "language='"+lang+"',dtmfmode='"+tone+"',nat='"+nat+"',qualify='"+qualify+"',"
                    + "allow='"+codec+"' where trunk_name='"+trunk_name+"'");
            //=============================================================================================
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        res=writeTrunkIax();
        return res;
    }
    public String alterTrunkVirtual(String trunk_name,String secret,String host,String lang,String tone,String nat,
            String qualify,String codec){
        String res = "ok";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            int up=db.Comando.executeUpdate("update trunks set secret='"+secret+"',host='"+host+"',"
                    + "language='"+lang+"',dtmfmode='"+tone+"',nat='"+nat+"',qualify='"+qualify+"',"
                    + "allow='"+codec+"' where trunk_name='"+trunk_name+"'");
            //=============================================================================================
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        res=writeTrunkSIP();
        return res;
    }
    public String alterTrunkSip(String trunk_name,String secret,String host,String lang,String tone,String nat,
            String qualify,String codec,String reception,String register,String exten,String username,String reinvite,
            String canreinvite,String limit){
        String res = "ok";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            int up=db.Comando.executeUpdate("update trunks set secret='"+secret+"',host='"+host+"',"
                    + "language='"+lang+"',dtmfmode='"+tone+"',nat='"+nat+"',qualify='"+qualify+"',"
                    + "allow='"+codec+"',reception='"+reception+"',register='"+register+"',"
                    + "num_recebimento='"+exten+"',username='"+username+"',reinvite='"+reinvite+"',"
                    + "canreinvite='"+canreinvite+"',call_limit='"+(limit.equals("")?"0":limit)+"'"
                    + " where trunk_name='"+trunk_name+"'");
            //=============================================================================================
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        res=writeTrunkSIP();
        return res;
    }
    public String alterTrunkDigital(String trunkName,String numBoard,String numLink,String lang){
        String res = "ok",range;
        JpbxDBConnect db=new JpbxDBConnect();
        switch(Integer.parseInt(numLink)){
            case 0: range="[0-29]";break;
            case 1: range="[30-59]";break;
            case 2: range="[60-89]";break;
            case 3: range="[90-119]";break;
            default: range="";
        }
        String canal="Khomp/B"+numBoard+"C"+range+"";
        try {
            int sql=db.Comando.executeUpdate("update trunks set language='"+lang+"',number_board="+numBoard+",number_link="+numLink+", "
                    + "canal='"+canal+"' where trunk_name='"+trunkName+"'");
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String dialplanPeers(){
        String res = "<select id=\"src\" name=\"src\">"
                + "<option>Qualquer</option>";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select name from peers order by name");
            while(db.resultado.next()){
                res+="<option>"+db.resultado.getString(1)+"</option>";
            }
            res+="</select>";
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String dialplanPeers(String option){
        String res = "<select id=\"src\" name=\"src\">"
                + "<option "+(option.equals("Qualquer")?"selected=\"\"":"")+">Qualquer</option>";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select name from peers order by name");
            while(db.resultado.next()){
                res+="<option "+(option.equals(db.resultado.getString(1))?"selected=\"\"":"")+">"+db.resultado.getString(1)+"</option>";
            }
            res+="</select>";
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String dialplanTrunks(){
        String res = "<select id=\"src\" name=\"src\">"
                + "<option>Qualquer</option>";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select trunk_name from trunks order by trunk_name");
            while(db.resultado.next()){
                res+="<option>"+db.resultado.getString(1)+"</option>";
            }
            res+="</select>";
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String dialplanTrunks(String option){
        String res = "<select id=\"src\" name=\"src\">"
                + "<option "+(option.equals("Qualquer")?"selected=\"\"":"")+">Qualquer</option>";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select trunk_name from trunks order by trunk_name");
            while(db.resultado.next()){
                res+="<option "+(option.equals(db.resultado.getString(1))?"selected=\"\"":"")+">"+db.resultado.getString(1)+"</option>";
            }
            res+="</select>";
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String createDialPlanRule(String rule_name,String src,String dst,int active,int order,String src_sup,String src_desc,
            String fullTime,String hstart,String hend,String seg,String ter,String qua,String qui,String sex,String sab,String dom,String alias,String aliasName){
        String res="ok";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            int sql=db.Comando.executeUpdate("insert into dialplan_rules (rule_name,src,dst,active,rule_order,src_sup,src_desc,"
                    + "fulltime,hstart,hend,seg,ter,qua,qui,sex,sab,dom,alias,alias_name)"
                    + " values ('"+rule_name+"','"+src+"','"+dst+"',"+active+","+order+",'"+src_sup+"','"+src_desc+"',"
                    + ""+fullTime+",'"+hstart+"','"+hend+"',"+seg+","+ter+","+qua+","+qui+","+sex+","+sab+","+dom+","+alias+",'"+aliasName+"')");
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String createDialPlanRule(int ruleId,String rule_name,String src,String dst,int active,int order,String src_sup,String src_desc,
            String fullTime,String hstart,String hend,String seg,String ter,String qua,String qui,String sex,String sab,String dom,String alias,String aliasName){
        String res="ok";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            int sql=db.Comando.executeUpdate("insert into dialplan_rules (rule_id,rule_name,src,dst,active,rule_order,src_sup,src_desc,"
                    + "fulltime,hstart,hend,seg,ter,qua,qui,sex,sab,dom,alias,alias_name)"
                    + " values ("+ruleId+",'"+rule_name+"','"+src+"','"+dst+"',"+active+","+order+",'"+src_sup+"','"+src_desc+"',"
                    + ""+fullTime+",'"+hstart+"','"+hend+"',"+seg+","+ter+","+qua+","+qui+","+sex+","+sab+","+dom+","+alias+",'"+aliasName+"')");
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String createDialPlanRule(String rule_name,String src,String dst,int active,int order,String src_sup,String src_desc,String alias,String aliasName){
        String res="ok";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            int sql=db.Comando.executeUpdate("insert into dialplan_rules (rule_name,src,dst,active,rule_order,src_sup,src_desc,alias,alias_name)"
                    + " values ('"+rule_name+"','"+src+"','"+dst+"',"+active+","+order+",'"+src_sup+"','"+src_desc+"',"+alias+",'"+aliasName+"')");
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String createDialPlanRule(int ruleId,String rule_name,String src,String dst,int active,int order,String src_sup,String src_desc,String alias,String aliasName){
        String res="ok";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            int sql=db.Comando.executeUpdate("insert into dialplan_rules (rule_id,rule_name,src,dst,active,rule_order,src_sup,src_desc,alias,alias_name)"
                    + " values ("+ruleId+",'"+rule_name+"','"+src+"','"+dst+"',"+active+","+order+",'"+src_sup+"','"+src_desc+"',"+alias+",'"+aliasName+"')");
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public void dialplanActions(int ruleId,String action,String arg1,String arg2,String arg3,String arg4){
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            int sql=db.Comando.executeUpdate("insert into dialplan_actions (rule_id,action,arg1,arg2,arg3,arg4) "
                    + "values ('"+ruleId+"','"+action+"','"+arg1+"','"+arg2+"','"+arg3+"','"+arg4+"')");
        } catch (SQLException ex) {
            Logger.getLogger(JpbxDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public String deleteDialPlan(int ruleId){
        String ruleIdent,exten = null,res="ok";
        int prio=1,loop=0;
        JpbxDBConnect db=new JpbxDBConnect();
        JpbxDBConnect db2=new JpbxDBConnect();
        JpbxDBConnect db3=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select dst from dialplan_rules "
                    + "where rule_id="+ruleId+"");
            if(db.resultado.next())
                exten=db.resultado.getString(1);
            int sql=db.Comando.executeUpdate("delete from dialplan_rules where rule_id="+ruleId+"");
            sql=db.Comando.executeUpdate("delete from dialplan where rule_id="+ruleId+"");
            db.resultado=db.Comando.executeQuery("select rule_id from dialplan_rules "
                    + "where dst='"+exten+"' order by rule_order");
            while(db.resultado.next()){
                loop++;
                ruleIdent=db.resultado.getString(1);
                sql=db2.Comando.executeUpdate("update dialplan_rules set rule_order="+loop+" "
                        + "where rule_id="+ruleIdent+"");
                if(loop>1)
                    prio=(loop-1)*100+1;
                db2.resultado=db2.Comando.executeQuery("select id from dialplan "
                        + "where rule_id="+ruleIdent+" order by priority");
                while(db2.resultado.next()){
                    sql=db3.Comando.executeUpdate("update dialplan set priority="+prio+" "
                            + "where id="+db2.resultado.getString(1)+"");
                    prio++;
                }
            }
            sql=db.Comando.executeUpdate("update dialplan set appdata=concat(substr(appdata,1,(instr(appdata,'?:auxDialPlan')+13)),(priority+100),',1')"
                    + "where exten='_"+exten+"' and app='GotoIf'");
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        db2.disconnectDB();
        db3.disconnectDB();
        return res;
    }
    public String alterDialPlan(int ruleId){
        String res="ok";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            int sql=db.Comando.executeUpdate("delete from dialplan_rules where rule_id='"+ruleId+"'");
            sql=db.Comando.executeUpdate("delete from dialplan where rule_id='"+ruleId+"'");
            sql=db.Comando.executeUpdate("delete from dialplan_actions where rule_id='"+ruleId+"'");
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public void deleteDialplanActions(int ruleId){
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            int sql=db.Comando.executeUpdate("delete from dialplan_actions where rule_id='"+ruleId+"'");
        } catch (SQLException ex) {
            Logger.getLogger(JpbxDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        db.disconnectDB();
    }
    public String createDialPlan(String exten,int priority,String app,String appdata,int rule_id){
        String res="ok";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            int sql=db.Comando.executeUpdate("insert into dialplan (context,exten,priority,app,appdata,rule_id)"
                    + " values ('jpbxRoute','_"+exten+"',"+priority+",'"+app+"','"+appdata+"','"+rule_id+"')");
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String verifyGotoIf(String exten){
        String res=null;
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select count(dst) from dialplan_rules "
                    + "where dst='"+exten+"'");
            if(db.resultado.next())
            res=db.resultado.getString(1);
        } catch (SQLException ex) {
            Logger.getLogger(JpbxDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }
    public String verifyGotoIfAlias(){
        String res=null;
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select count(alias) from dialplan_rules "
                    + "where alias=1");
            if(db.resultado.next())
            res=db.resultado.getString(1);
        } catch (SQLException ex) {
            Logger.getLogger(JpbxDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }
    public String peersForDialPlan(){
        String res="(";
        int c=0;
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select name from peers");
            while(db.resultado.next()){
                if(c>0)
                    res+="|"+db.resultado.getString(1);
                else
                    res+=db.resultado.getString(1);
                c++;
            }
            res+=")";
        } catch (SQLException ex) {
            res="fail";
        }
        db.disconnectDB();
        return res;
    }
    public String peersForDialPlanPickup(String pickup){
        String res="(";
        int c=0;
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select name from peers where "
                    + "pickupgroup=(select pickup_id from grppickup where name='"+pickup+"')");
            while(db.resultado.next()){
                if(c>0)
                    res+="|"+db.resultado.getString(1);
                else
                    res+=db.resultado.getString(1);
                c++;
            }
            res+=")";
        } catch (SQLException ex) {
            res="fail";
        }
        db.disconnectDB();
        return res;
    }
    public String srcOptions(){
        String res="<select id=\"selOri\" class=\"span2\" name=\"selOri\">"
                +   "<option value=\"Ramal\">Ramal</option>" +
"                   <option value=\"Departamento\">Departamento</option>" +
"                   <option value=\"Tronco\">Tronco</option>" +
"                   <option value=\"Expr\">Expressão Regular</option>" +
"                   <option value=\"Qualquer\">Qualquer</option>"
                + " <option value=\"Fax\">Fax Virtual</option>"
                + "</select>";
        return res;
    }
    public String srcOptions(String option){
        String res="<select id=\"selOri\" class=\"span2\" name=\"selOri\">"
                +   "<option value=\"Ramal\" "+(option.equals("Ramal")?"selected":"")+">Ramal</option>" +
"                   <option value=\"Departamento\" "+(option.equals("Departamento")?"selected":"")+">Departamento</option>" +
"                   <option value=\"Tronco\" "+(option.equals("Tronco")?"selected":"")+">Tronco</option>" +
"                   <option value=\"Expr\" "+(option.equals("Expr")?"selected":"")+">Expressão Regular</option>" +
"                   <option value=\"Qualquer\" "+(option.equals("Qualquer")?"selected":"")+">Qualquer</option>"
                + " <option value=\"Fax\" "+(option.equals("Fax")?"selected":"")+">Fax Virtual</option>"
                + "</select>";
        return res;
    }
    public String actionsOptions(){
        String res="<select id=\"selAct\">\n" +
"                    <option>Toca Audio</option>\n" +
"                    <option>Atende Canal</option>\n" +
"                    <option>Desliga Canal</option>\n" +
"                    <option>Disca Ramal</option>\n" +
"                    <option>Disca Tronco</option>\n" + 
"                    <option>Edita Destino</option>\n" +
"                    <option>Comando Livre</option>\n" +
"                    <option>Define Custo</option>"+
"                    <option>Chamar Grupo</option>"
                + "  <option>Recebe Fax</option>"
                + "  <option>Envia Fax</option>" +                
"                </select>";
        return res;
    }
    public String listPickupGroups(){
        JpbxDBConnect db=new JpbxDBConnect();
        String res = "<select id=\"src\" name=\"src\">";
        try {
            db.resultado=db.Comando.executeQuery("select name from grppickup order by name");
            while(db.resultado.next()){
                res+="<option>"+db.resultado.getString(1)+"</option>";
            }
            res+="</select>";
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String selectCosts(){
        JpbxDBConnect db=new JpbxDBConnect();
        String res = "";
        try {
            db.resultado=db.Comando.executeQuery("select ccost,ccost_name from ccosts order by ccost");
            while(db.resultado.next()){
                res+="<option>"+db.resultado.getString(1)+" - "+db.resultado.getString(2)+"</option>";
            }
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String verifyCostsInRules(String desc){
        JpbxDBConnect db=new JpbxDBConnect();
        String res ="";
        try {
            db.resultado=db.Comando.executeQuery("select rule_id from dialplan_actions "
                    + "where action='SetCost' and arg1='"+desc+"'");
            while(db.resultado.next()){
                res+=db.resultado.getString(1)+" ";
            }
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String selectCosts(String cost){
        JpbxDBConnect db=new JpbxDBConnect();
        String res = "",selected="";
        try {
            db.resultado=db.Comando.executeQuery("select ccost,ccost_name from ccosts order by ccost");
            while(db.resultado.next()){
                if(cost.subSequence(0, cost.indexOf(" ")).equals(db.resultado.getString(1)))
                    selected="selected";
                res+="<option "+selected+">"+db.resultado.getString(1)+" - "+db.resultado.getString(2)+"</option>";
                selected="";
            }
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String listDeptos(String option){
        JpbxDBConnect db=new JpbxDBConnect();
        String res = "<select id=\"src\" name=\"src\">";
        try {
            db.resultado=db.Comando.executeQuery("select name from grppickup order by name");
            while(db.resultado.next()){
                res+="<option "+(option.equals(db.resultado.getString(1))?"selected=\"\"":"")+">"+db.resultado.getString(1)+"</option>";
            }
            res+="</select>";
        } catch (SQLException ex) {
            Logger.getLogger(JpbxDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        db.disconnectDB();
        return res;
    }
    public String getTrunkCanal(String trunk){
        JpbxDBConnect db=new JpbxDBConnect();
        String res = "";
        try {
            db.resultado=db.Comando.executeQuery("select canal from trunks where trunk_name='"+trunk+"'");
            if(db.resultado.next()){
                res=db.resultado.getString(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(JpbxDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        db.disconnectDB();
        return res;
    }
    public String getTrunkTecnology(String trunk){
        JpbxDBConnect db=new JpbxDBConnect();
        String res = "";
        try {
            db.resultado=db.Comando.executeQuery("select tecnology from trunks where trunk_name='"+trunk+"'");
            if(db.resultado.next()){
                res=db.resultado.getString(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(JpbxDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        db.disconnectDB();
        return res;
    }
    public String[] getRuleName(String rule){
        JpbxDBConnect db=new JpbxDBConnect();
        String res[] =new String[4];
        try {
            db.resultado=db.Comando.executeQuery("select rule_name,rule_order,dst from dialplan_rules where rule_id="+rule+"");
            if(db.resultado.next()){
                res[0]=db.resultado.getString(1);
                res[1]=db.resultado.getString(2);
                res[2]=db.resultado.getString(3);
            }
            db.resultado=db.Comando.executeQuery("select max(rule_order) from dialplan_rules "
                    + "where dst=(select dst from dialplan_rules where rule_id="+rule+")");
            if(db.resultado.next())
                res[3]=db.resultado.getString(1);
        } catch (SQLException ex) {
            Logger.getLogger(JpbxDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        db.disconnectDB();
        return res;
    }
    public String setPriorityDialplans(int ruleId,String exten,int rulePos,int qtdeRules){
        JpbxDBConnect db=new JpbxDBConnect();
        String res="ok",otherRuleId = null;
        try {
            db.resultado=db.Comando.executeQuery("select rule_id from dialplan_rules "
                    + "where dst='"+exten+"' and rule_order="+(rulePos-1)+"");
            if(db.resultado.next())
                otherRuleId=db.resultado.getString(1);
            int sql=db.Comando.executeUpdate("update dialplan set priority=(priority+1000) \n" +
                "where rule_id="+otherRuleId+"");
            sql=db.Comando.executeUpdate("update dialplan set priority=(priority-100) "
                    + "where rule_id="+ruleId+"");
            sql=db.Comando.executeUpdate("update dialplan set priority=(priority-900) \n" +
                "where rule_id="+otherRuleId+"");
            sql=db.Comando.executeUpdate("update dialplan set appdata=concat(substr(appdata,1,(instr(appdata,'?:auxDialPlan')+13)),(priority+100),',1') "
                    + "where exten='_"+exten+"' and app='GotoIf'");
            sql=db.Comando.executeUpdate("update dialplan_rules set rule_order=(rule_order-1) "
                    + "where rule_id="+ruleId+"");
            sql=db.Comando.executeUpdate("update dialplan_rules set rule_order=(rule_order+1) "
                    + "where rule_id="+otherRuleId+"");
            
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String listActionTrunk(String numDialplan){
        String res = "<select id=\"appp"+numDialplan+"\">";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select trunk_name from trunks order by trunk_name");
            while(db.resultado.next()){
                res+="<option>"+db.resultado.getString(1)+"</option>";
            }
            res+="</select>";
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String listActionCallGrp(String numDialplan){
        String res = "<select id=\"appp"+numDialplan+"\">";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select name from queues where type='C'");
            while(db.resultado.next()){
                res+="<option>"+db.resultado.getString(1).substring(8)+"</option>";
            }
            res+="</select>";
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String listActionCallGrp(String numDialplan,String grp){
        String res = "<select id=\"appp"+numDialplan+"\">";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select name from queues where type='C'");
            while(db.resultado.next()){
                res+="<option "+(grp.equals(db.resultado.getString(1).substring(8))?"selected=\"\"":"")+">"+db.resultado.getString(1).substring(8)+"</option>";
            }
            res+="</select>";
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String listActionTrunk(String numDialplan,String trk){
        String res = "<select id=\"appp"+numDialplan+"\">";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select trunk_name from trunks order by trunk_name");
            while(db.resultado.next()){
                res+="<option "+(trk.equals(db.resultado.getString(1))?"selected=\"\"":"")+">"+db.resultado.getString(1)+"</option>";
            }
            res+="</select>";
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String replaceNames(String name){
        String res=name.replaceAll(" ", "");
            res = res.replaceAll("[ÂÀÁÄÃ]","A");  
            res = res.replaceAll("[âãàáä]","a");  
            res = res.replaceAll("[ÊÈÉË]","E");  
            res = res.replaceAll("[êèéë]","e");  
            res = res.replaceAll("ÎÍÌÏ","I");  
            res = res.replaceAll("îíìï","i");  
            res = res.replaceAll("í","i");
            res = res.replaceAll("[ÔÕÒÓÖ]","O");  
            res = res.replaceAll("[ôõòóö]","o");  
            res = res.replaceAll("[ÛÙÚÜ]","U");  
            res = res.replaceAll("[ûúùü]","u");  
            res = res.replaceAll("Ç","C");  
            res = res.replaceAll("ç","c");   
            res = res.replaceAll("[ýÿ]","y");  
            res = res.replaceAll("Ý","Y");  
            res = res.replaceAll("ñ","n");  
            res = res.replaceAll("Ñ","N");  
            //res = res.replaceAll("['\"-<>\\|/]","");
            return res;
    }
    public String replaceNamesDialPlan(String name){
        String res=name.replaceAll("[ÂÀÁÄÃ]","A");  
            res = res.replaceAll("[âãàáä]","a");  
            res = res.replaceAll("[ÊÈÉË]","E");  
            res = res.replaceAll("[êèéë]","e");  
            res = res.replaceAll("ÎÍÌÏ","I");  
            res = res.replaceAll("îíìï","i");  
            res = res.replaceAll("í","i");
            res = res.replaceAll("[ÔÕÒÓÖ]","O");  
            res = res.replaceAll("[ôõòóö]","o");  
            res = res.replaceAll("[ÛÙÚÜ]","U");  
            res = res.replaceAll("[ûúùü]","u");  
            res = res.replaceAll("Ç","C");  
            res = res.replaceAll("ç","c");   
            res = res.replaceAll("[ýÿ]","y");  
            res = res.replaceAll("Ý","Y");  
            res = res.replaceAll("ñ","n");  
            res = res.replaceAll("Ñ","N");  
           // res = res.replaceAll("['\"-<>\\|/]","");
            return res;
    }
    public String[] dialPlanRules(int rule){
        String res[]=new String[16];
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select dst,src_sup,src_desc,"
                    + "fulltime,hstart,hend,seg,ter,qua,qui,sex,sab,dom,rule_name,alias,alias_name"
                    + " from dialplan_rules where rule_id='"+rule+"'");
            if(db.resultado.next()){
                res[0]=db.resultado.getString(1);
                res[1]=db.resultado.getString(2);
                res[2]=db.resultado.getString(3);
                res[3]=db.resultado.getString(4);
                res[4]=db.resultado.getString(5);
                res[5]=db.resultado.getString(6);
                res[6]=db.resultado.getString(7);
                res[7]=db.resultado.getString(8);
                res[8]=db.resultado.getString(9);
                res[9]=db.resultado.getString(10);
                res[10]=db.resultado.getString(11);
                res[11]=db.resultado.getString(12);
                res[12]=db.resultado.getString(13);
                res[13]=db.resultado.getString(14);
                res[14]=db.resultado.getString(15);
                res[15]=db.resultado.getString(16);
            }
        } catch (SQLException ex) {
            res[0]=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String[][] dialplanActions(int rule){
        JpbxDBConnect db=new JpbxDBConnect();
        String res[][] = null;
        try {
            db.resultado=db.Comando.executeQuery("select action,arg1,arg2,arg3,arg4 from dialplan_actions "
                    + "where rule_id='"+rule+"' order by action_id");
            db.resultado.last();
            res=new String[db.resultado.getRow()][5];
            db.resultado.beforeFirst();
            for(int i=0;db.resultado.next();i++){
                res[i][0]=db.resultado.getString(1);
                res[i][1]=db.resultado.getString(2);
                res[i][2]=db.resultado.getString(3);
                res[i][3]=db.resultado.getString(4);
                res[i][4]=db.resultado.getString(5);
            }
        } catch (SQLException ex) {
            Logger.getLogger(JpbxDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        db.disconnectDB();
        return res;
    }
    public String translateApps(String app){
        String res=app;
        if(app.equals("Answer"))
            res="Atende Canal";
        else if(app.equals("PlayBacks"))
            res="Toca Audio";
        else if(app.equals("DialPeer"))
            res="Disca Ramal";
        else if(app.equals("Hangup"))
            res="Desliga Canal";
        else if(app.equals("DialTrunk"))
            res="Disca Tronco";
        else if(app.equals("SetDst"))
            res="Edita Destino";
        else if(app.equals("SetCost"))
            res="Define Custo";
        else if(app.equals("CallGrp"))
            res="Chamar Grupo";
        else if(app.equals("receiveFax"))
            res="Recebe Fax";
        else if(app.equals("sendFax"))
            res="Envia Fax";
        else
            res="Comando Livre";
        return res;
    }
    public String appsParams(String app,String arg1,String arg2,String arg3,String dialplan,String arg4){
        String res="<div id=\"li"+dialplan+"\" class=\"tab-pane\">";        
        if(app.equals("PlayBacks"))
            res+="<h5>Audio para Reprodução</h5>"
                    + "<input type = \"hidden\" id = \"app"+dialplan+"\" value = \"PlayBacks\" />"
                    + "<select id=\"appp"+dialplan+"\">"
                        + listMohsDialPlan(arg1.substring(0, arg1.indexOf("/")))
                        + "</select>";                       
                    //+ "<input type=\"text\" id=\"appp"+dialplan+"\" value=\""+arg1+"\" />";
        else if(app.equals("Answer"))
            res+="<h5>Envia sinal de atendimento a ligação em andamento</h5>"
                    + "<h6>(Sem parâmetros)</h6>"
                    + "<input type = \"hidden\" id = \"app"+dialplan+"\" value = \"Answer\" />"                       
                    + "<input type=\"hidden\" id=\"appp"+dialplan+"\" value=\"\" />";
        else if(app.equals("Hangup"))
            res+="<h5>Envia sinal de desligamento a ligação em andamento</h5>"
                    + "<h6>(Sem parâmetros)</h6>"
                    + "<input type = \"hidden\" id = \"app"+dialplan+"\" value = \"Hangup\" />"                       
                    + "<input type=\"hidden\" id=\"appp"+dialplan+"\" value=\"\" />";
        else if(app.equals("DialPeer"))
            res+="<h5>Envia a ligação para o ramal definido ou discado</h5>"
                    + "<h6>(Deixe em branco para chamar o ramal discado)</h6>"
                    + "<input type = \"hidden\" id = \"app"+dialplan+"\" value = \"DialPeer\" />"                       
                    + "<input class=\"span2\" type=\"number\" id=\"appp"+dialplan+"\" placeholder=\"Ramal Discado\" value=\""+arg1+"\" />"
                    + "<h5>Tempo limite para atendimento (segundos)</h5>"
                    + "<input style=\"float: left\" class=\"span1\" id=\"apppp"+dialplan+"\" type=\"number\" value=\""+arg2+"\" />"
                    + "<p style=\"float: left\" class=\"muted offset1\">Habilitar Rechamada</p>"
                    + "<input class=\"checkbox\" name=\"recall"+dialplan+"\" type=\"checkbox\" "
                    + ""+(arg3.equals("1")?"checked":"")+" />";
        else if(app.equals("DialTrunk"))
            res+="<h5>Selecione o Tronco</h5>"
                        + "<input type = \"hidden\" id = \"app"+dialplan+"\" value = \"DialTrunk\" />"
                        + ""+listActionTrunk(dialplan,arg1)+""
                        + "<h5>Tempo limite para atendimento (segundos)</h5>"
                        + "<input class=\"span1\" id=\"apppp"+dialplan+"\" type=\"number\" value=\""+arg2+"\" />";
        else if(app.equals("CallGrp"))
            res+="<h5>Selecione o Grupo de Chamada</h5>"
                        + "<input type = \"hidden\" id = \"app"+dialplan+"\" value = \"CallGrp\" />"
                        + ""+listActionCallGrp(dialplan,arg1)+""
                        + "<h5>Tempo limite para atendimento (segundos)</h5>"
                        + "<input class=\"span1\" id=\"apppp"+dialplan+"\" type=\"number\" value=\""+arg2+"\" />";
        else if(app.equals("SetDst"))
            res+="<h5>Os números serão adicionados no inicio do Destino</h5>\n" +
                        "<input type=\"hidden\" id=\"app"+dialplan+"\" value=\"SetDst\" />"+
"                   <input type=\"number\" id=\"appp"+dialplan+"\" placeholder=\"Número a ser add\" value=\""+arg1+"\" />\n" +
"                   <input class=\"checkbox\" id=\"check"+dialplan+"\" type=\"checkbox\" name=\"checkedit"+dialplan+"\" "+(arg3.equals("y")?"checked":"")+" /> Substituir todo o Destino</input>\n"
                        + "<div id=\"numcut"+dialplan+"\">"+                        
"                   <h5>Quantidade de números a serem cortados</h5>\n" +
"                   <input id=\"apppp"+dialplan+"\" class=\"span1\" type=\"number\" value=\""+arg2+"\" />"
                    + "<input class=\"checkbox\" id=\"pipe"+dialplan+"\" class=\"span1\" type=\"checkbox\" name=\"pipe"+dialplan+"\" "
                    +(arg4.equals("y")?"checked":"")+" />"
                    + "Cortar no PIPE (apenas usando alias)"
                        + "</div>"
                        + "<script>"
                    + "if($('#check"+dialplan+"').is(':checked')) \n"
                    + " $('#numcut"+dialplan+"').toggle('slow');"
                        + " $('#check"+dialplan+"').click(function(){\n" +
"                               $('#numcut"+dialplan+"').toggle('slow');\n" +
"                           });"
                        + "</script>";
        else if(app.equals("SetCost"))
            res+="<h5>Escolha o Centro de Custo</h5>"
                        + "<input type=\"hidden\" id=\"app"+dialplan+"\" value=\"SetCost\" />"
                        + "<select id=\"appp"+dialplan+"\">"
                        + ""+selectCosts(arg1)+""
                        + "</select>";
        else if(app.equals("receiveFax"))
            res+="<h5>Escolha a Empresa Destino do Fax Virtual</h5>"
                        + "<input type=\"hidden\" id=\"app"+dialplan+"\" value=\"receiveFax\" />"
                        + "<select id=\"appp"+dialplan+"\">"
                        + ""+selectCompanys(arg1)+""
                        + "</select>";
        else if(app.equals("sendFax"))
            res+="<h5>Ajusta Parâmetros para envio de Fax Virtual</h5>"
                        + "<input type=\"hidden\" id=\"app"+dialplan+"\" value=\"sendFax\" />"
                    + " <h6>(Sem parâmetros)</h6>";
        else
            res+="<div style=\"float: left\">"
                        + "<h5>Aplicação</h5>"
                        + "<input type=\"text\" id=\"app"+dialplan+"\" value=\""+app+"\" />"
                        + "</div>"
                        + "<div style=\"float: left;padding-left: 10px\">"
                        + "<h5>Parâmetro</h5>"
                        + "<input type=\"text\" id=\"appp"+dialplan+"\" value=\""+arg1+"\" />"
                        + "</div>"
                        + "<a href=\"cli.jsp?listapp=1\" target=\"_blank\"> Aplicações Disponíveis</a>";
        res+="</div>";
        return res;
    }
    public String getLessDialPlanPriority(int ruleId){
        JpbxDBConnect db=new JpbxDBConnect();
        String res="0";
        try {
            db.resultado=db.Comando.executeQuery("SELECT min(priority) from dialplan "
                    + "where rule_id='"+ruleId+"'");
            if(db.resultado.next())
                res=db.resultado.getString(1);
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String getRuleOrder(int ruleId){
        JpbxDBConnect db=new JpbxDBConnect();
        String res="0";
        try {
            db.resultado=db.Comando.executeQuery("SELECT rule_order from dialplan_rules "
                    + "where rule_id='"+ruleId+"'");
            if(db.resultado.next())
                res=db.resultado.getString(1);
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String getRuleId(String ruleName){
        JpbxDBConnect db=new JpbxDBConnect();
        String res="0";
        try {
            db.resultado=db.Comando.executeQuery("SELECT rule_id from dialplan_rules "
                    + "where rule_name='"+ruleName+"'");
            if(db.resultado.next())
                res=db.resultado.getString(1);
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public int getSessionTimeout(String user){
        JpbxDBConnect db=new JpbxDBConnect();
        int res = 0;
        try {
            db.resultado=db.Comando.executeQuery("SELECT timeout from users "
                    + "where name='"+user+"'");
            if(db.resultado.next())
                res=Integer.parseInt(db.resultado.getString(1));
        } catch (SQLException ex) {
            res=0;
        }
        db.disconnectDB();
        return res;
    }
    public int[] getLimitRelCalls(){
        JpbxDBConnect db=new JpbxDBConnect();
        int res[] =new int[2];
        try {
            db.resultado=db.Comando.executeQuery("SELECT limit_relcalls,custom_ips from configs");
            if(db.resultado.next()){
                res[0]=Integer.parseInt(db.resultado.getString(1));
                res[1]=Integer.parseInt(db.resultado.getString(2));
            }
        } catch (SQLException ex) {
            res[0]=0;
        }
        db.disconnectDB();
        return res;
    }
    public String alterSisConfigs(String limit){
        JpbxDBConnect db=new JpbxDBConnect();
        String res = "ok";
        try {
            int sql=db.Comando.executeUpdate("update configs set limit_relcalls="+limit);
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String getLvl(String user){
        JpbxDBConnect db=new JpbxDBConnect();
        String res = null;
        try {
            db.resultado=db.Comando.executeQuery("SELECT nivel from users "
                    + "where name='"+user+"'");
            if(db.resultado.next())
                res=db.resultado.getString(1);
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String relDetails(String uniqueid){
        JpbxDBConnect db=new JpbxDBConnect();
        String res="<div style=\"display: none\" id=\"dialog\" title=\"Detalhes da Ligação\">";
        try {
            db.resultado=db.Comando.executeQuery("SELECT origem,value,date_format(time,'%H:%i:%s'),record from transf_history "
                    + "where id_ligacao='"+uniqueid+"' order by time");
            while(db.resultado.next()){
                res+="<p class=\"text-info\" style=\"padding-top: 1%\"><u>"+db.resultado.getString(1)+" --> "+db.resultado.getString(2)+""
                        + " as "+db.resultado.getString(3)+"</u></p>"
                        + (!db.resultado.getString(4).equals("")?"<audio controls style=\"width: 90%\"><source src=\"asterisk/records/"+db.resultado.getString(4)+".wav\" "
                        + "type=\"audio/wav\"></audio>":"<p class=\"text-error\">Sem Gravação.</p>");
            }
            res+="</div><script>"
                        + "$('#dialog').dialog();"
                        + "</script>";
        } catch (SQLException ex) {
            res=ex.getMessage();
        }//webapps/jpbx/asterisk
        db.disconnectDB();
        return res;
    }
    public String deleteCall(String uniqueid){
        String res="ok";
        int sql = 0;
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            Asterisk ast=new Asterisk();
            try {
                db.resultado=db.Comando.executeQuery("select record from transf_history where id_ligacao='"+uniqueid+"'");
                //ast.AstConnect();
                while(db.resultado.next()){
                    res=ast.removeCall(db.resultado.getString(1));
                }
                //ast.AstDisconnect();
            } catch (SQLException ex) {
                res=ex.getMessage();
            }      
        } catch (IOException ex) {
            res=ex.getMessage();
        } catch(AuthenticationFailedException ex){
            res=ex.getMessage();
        } catch(TimeoutException ex){
            res=ex.getMessage();
        }
        if(!res.equals("Fail")){
            try {
                sql=db.Comando.executeUpdate("delete from relcalls where uniqueid='"+uniqueid+"'");
                sql=db.Comando.executeUpdate("delete from transf_history where id_ligacao='"+uniqueid+"'");
                sql=db.Comando.executeUpdate("delete from cdr where uniqueid='"+uniqueid+"'");
                if(sql>0)
                    res="ok";
            } catch (SQLException ex) {
                res=ex.getMessage();
            }
        }
        db.disconnectDB();
        return res;
    }
    public String deleteConference(String id,String rec){
        String res="ok";
        int sql = 0;
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            Asterisk ast=new Asterisk();
            ast.removeConference(rec);
        } catch(AuthenticationFailedException ex){
            res=ex.getMessage();
        } catch(TimeoutException ex){
            res=ex.getMessage();
        } catch (IOException ex){
            res=ex.getMessage();
        }
        if(!res.equals("Fail")){
            try {
                sql=db.Comando.executeUpdate("delete from rel_conferences where id_conference="+id+"");
                if(sql>0)
                    res="ok";
            } catch (SQLException ex) {
                res=ex.getMessage();
            }
        }
        db.disconnectDB();
        return res;
    }
    public String statusLinkTrunk(String host){
        String res="",ls_str;
        Process ls_proc;
        try {
            ls_proc = Runtime.getRuntime().exec("ping -w1 -c1 "+host);
            DataInputStream ls_in = new DataInputStream(ls_proc.getInputStream());
            while ((ls_str = ls_in.readLine()) != null) {
                if(ls_str.contains("icmp_req=1")){
                    res=ls_str.substring(ls_str.indexOf("time=")+5);
                    break;
                }
            }
            if(!res.equals(""))
                res="<p class=\"text-success\">Ativo. Resposta: "+res+"</p>";
            else
                res="<p class=\"text-error\">Inativo ou Inalcançável</p>";
        } catch (IOException ex) {
            res=ex.getMessage();
        }
        return res;
    }
    public String buildCSV(String dateini,String dateend,String order,String src,String dst,String cost,String state,
            String srcseek,String dstseek){
        JpbxDBConnect db=new JpbxDBConnect();
        int seq=0;
        String srcSeek,dstSeek,res="ok";
        if(srcseek.equals("equals"))
            srcSeek=" and src in ('"+src+"')";
        else if(srcseek.equals("begin"))
            srcSeek=" and src like '"+src+"%'";
        else
            srcSeek=" and src like '%"+src+"%'";
        if(dstseek.equals("equals"))
            dstSeek=" and dstfinal in ('"+dst+"')";
        else if(dstseek.equals("begin"))
            dstSeek=" and dstfinal like '"+dst+"%'";
        else
            dstSeek=" and dstfinal like '%"+dst+"%'";
        String filterSrc=(!src.equals("")?srcSeek:"");
        String filterDst=(!dst.equals("")?dstSeek:"");
        String filterCost=cost,filterStatus=state;
        if(filterCost.equals("Todos"))
            filterCost="";
        else if(filterCost.equals("Indefinido"))
            filterCost=" and accountcode='' ";
        else
            filterCost=" and accountcode="+cost.substring(0,cost.indexOf(" "));
        if(filterStatus.equals("all"))
            filterStatus="";
        else
            filterStatus=" and disposition='"+state+"'";
        String status = null;
        
        try {
            db.resultado=db.Comando.executeQuery("select calldate,src,dstfinal,disposition,duration,billsec,"
                    + "(select ccost_name from ccosts where idccosts=accountcode),"
                    + "round(tarifador(accountcode,billsec),2),uniqueid "
                    + "from relcalls where calldate between '"+formatDate(dateini)+" 00:00:00' and '"+formatDate(dateend)+" 23:59:59' "
                    + filterSrc
                    + filterDst
                    + filterCost
                    + filterStatus
                    + " order by "+order.substring(5));  
            String ls_str,file;
            GregorianCalendar now=new GregorianCalendar();
            now.setTime(new Date());
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd_HH:mm");
            file="Relatorio_"+sdf.format(now.getTime());
            File makefile=new File("/tmp/"+file+".csv");
            FileWriter fwrite=new FileWriter(makefile);
            fwrite.write("Seq;Data Hora;Origem;Destino;Estado Final;Duracao;Conversacao;Custo;Valor\n");
            while(db.resultado.next()){
                seq++;
                fwrite.write(seq+";"+
                        db.resultado.getString(1)+";"+
                        db.resultado.getString(2)+";"+
                        db.resultado.getString(3)+";"+
                        db.resultado.getString(4)+";"+
                        db.resultado.getString(5)+";"+
                        db.resultado.getString(6)+";"+
                        db.resultado.getString(7)+";"+
                        db.resultado.getString(8)+"\n");
            }
            fwrite.flush();
            fwrite.close();
            Process ls_proc;
            ls_proc = Runtime.getRuntime().exec("rar a /tmp/Relatorio.rar /tmp/"+file+".csv");
            DataInputStream ls_in = new DataInputStream(ls_proc.getInputStream());
            while ((ls_str = ls_in.readLine()) != null) {
                if(ls_str.contains("Done")){
                    res=file;
                    break;
                }
            }
            makefile.delete();
            ls_proc=Runtime.getRuntime().exec("mv /tmp/Relatorio.rar /etc/asterisk/jpbx/reports");
        } catch (SQLException ex) {
            res="Falha no relatório:</br>"+ex.getMessage();
        } catch(IOException ex){
            res="Falha no CSV:</br>"+ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String buildCSV(String dateini,String dateend,String order,String src,String dst,String cost,String state,
            String srcseek,String dstseek,String comp){
        JpbxDBConnect db=new JpbxDBConnect();
        int seq=0;
        String srcSeek,dstSeek,res="ok";
        if(srcseek.equals("equals"))
            srcSeek=" and src in ('"+src+"')";
        else if(srcseek.equals("begin"))
            srcSeek=" and src like '"+src+"%'";
        else
            srcSeek=" and src like '%"+src+"%'";
        if(dstseek.equals("equals"))
            dstSeek=" and dstfinal in ('"+dst+"')";
        else if(dstseek.equals("begin"))
            dstSeek=" and dstfinal like '"+dst+"%'";
        else
            dstSeek=" and dstfinal like '%"+dst+"%'";
        String filterSrc=(!src.equals("")?srcSeek:"");
        String filterDst=(!dst.equals("")?dstSeek:"");
        String filterCost=cost,filterStatus=state;
        if(filterCost.equals("Todos"))
            filterCost="";
        else if(filterCost.equals("Indefinido"))
            filterCost=" and accountcode='' ";
        else
            filterCost=" and accountcode="+cost.substring(0,cost.indexOf(" "));
        if(filterStatus.equals("all"))
            filterStatus="";
        else
            filterStatus=" and disposition='"+state+"'";
        String status = null;
        
        try {
            db.resultado=db.Comando.executeQuery("select calldate,src,dstfinal,disposition,duration,billsec,"
                    + "(select ccost_name from ccosts where idccosts=accountcode),"
                    + "round(tarifador(accountcode,billsec),2),uniqueid,company "
                    + "from relcalls where calldate between '"+formatDate(dateini)+" 00:00:00' and '"+formatDate(dateend)+" 23:59:59' "
                    + filterSrc
                    + filterDst
                    + filterCost
                    + filterStatus
                    + " having company="+comp+" order by "+order.substring(5));  
            String ls_str,file;
            GregorianCalendar now=new GregorianCalendar();
            now.setTime(new Date());
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd_HH:mm");
            file="Relatorio_"+sdf.format(now.getTime());
            File makefile=new File("/tmp/"+file+".csv");
            FileWriter fwrite=new FileWriter(makefile);
            fwrite.write("Seq;Data Hora;Origem;Destino;Estado Final;Duracao;Conversacao;Custo;Valor\n");
            while(db.resultado.next()){
                seq++;
                fwrite.write(seq+";"+
                        db.resultado.getString(1)+";"+
                        db.resultado.getString(2)+";"+
                        db.resultado.getString(3)+";"+
                        db.resultado.getString(4)+";"+
                        db.resultado.getString(5)+";"+
                        db.resultado.getString(6)+";"+
                        db.resultado.getString(7)+";"+
                        db.resultado.getString(8)+"\n");
            }
            fwrite.flush();
            fwrite.close();
            Process ls_proc;
            ls_proc = Runtime.getRuntime().exec("rar a /tmp/Relatorio.rar /tmp/"+file+".csv");
            DataInputStream ls_in = new DataInputStream(ls_proc.getInputStream());
            while ((ls_str = ls_in.readLine()) != null) {
                if(ls_str.contains("Done")){
                    res=file;
                    break;
                }
            }
            makefile.delete();
            ls_proc=Runtime.getRuntime().exec("mv /tmp/Relatorio.rar /etc/asterisk/jpbx/reports");
        } catch (SQLException ex) {
            res="Falha no relatório:</br>"+ex.getMessage();
        } catch(IOException ex){
            res="Falha no CSV:</br>"+ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String writeSipContacts(String contacts,int action){
        String res="ok";
        JpbxDBConnect db=new JpbxDBConnect();
        File makefile=new File("webapps/jpbx/asterisk/sipContacts.conf");
        try {   
            FileWriter fwrite=new FileWriter(makefile);
            int sql=db.Comando.executeUpdate("update configs set custom_ips="+action+"");
            switch(action){
                case 1:
                    fwrite.write("permit=");
                    fwrite.write(contacts.replaceAll("\n", "\npermit="));
                    fwrite.write("\n");
                    fwrite.flush();
                    fwrite.close();
                    String ls_str;
                    List list=new ArrayList();
                    Process ls_proc=Runtime.getRuntime().exec("cat /etc/asterisk/jpbx/sipContacts.conf");
                    DataInputStream ls_in = new DataInputStream(ls_proc.getInputStream());
                    while ((ls_str = ls_in.readLine()) != null) {
                        if(!ls_str.equals("permit="))
                            list.add(ls_str);
                    }
                    makefile=new File("webapps/jpbx/asterisk/sipContacts.conf");
                    fwrite=new FileWriter(makefile);
                    fwrite.write("deny=0.0.0.0/0.0.0.0\n");
                    for(int i=0;i<list.size();i++){
                        fwrite.write(list.get(i).toString()+"\n");
                    }
                    fwrite.write("#include jpbx/sipContactsDDNS.conf");
                    fwrite.flush();
                    fwrite.close();
                    break;
                case 0:
                    fwrite.write("");
                    fwrite.flush();
                    fwrite.close();
                    break;
            }
       
            Asterisk ast=new Asterisk();
            try {
                ast.AstConnect();
                List<String> foo=ast.getInfos("sip reload");
                ast.AstDisconnect();
            } catch (AuthenticationFailedException ex) {
                res="Falha de login Asterisk\n"+ex.getMessage();
            } catch (TimeoutException ex) {
                res="Tempo de retorno Asterisk demorou\n"+ex.getMessage();
            }
            } catch(IOException io){
                res="Falha no banco\n"+io.getMessage();
            } catch(SQLException ex){
                res=ex.getMessage();
            }
        return res;
    }
    public String listMohsJsp(){
        JpbxDBConnect db=new JpbxDBConnect();
        String qtde,res="<table class=\"span11 table-striped table-condensed table-hover table-bordered\">"
                + "<tr><th>Código</th><th>Nome</th><th>Música</th><th>Ações</th><th></th></tr>";
        try {
            db.resultado=db.Comando.executeQuery("select moh_id,name,moh "
                    + " from mohs");
            while(db.resultado.next()){
                //qtde=getPickupPeers(db.resultado.getString(1));
                res+="<tr><td>"+db.resultado.getString(1)+"</td>"
                        + "<td>"+db.resultado.getString(2)+"</td>"
                        + "<td>"+db.resultado.getString(3)+"</td>"
                        + "<td><a rel=\"tooltip\" data-placement=\"left\" title=\"Editar Espera\" href=\"alterMoh.jsp?moh="+db.resultado.getString(1)+"\" ><img src=\"css/bootstrap/img/icone-editar.gif\"></a></td> "
                        + "<td><a rel=\"tooltip\" data-placement=\"right\" title=\"Apagar Espera\" href=\"#\" onclick=\"deleteMoh("+db.resultado.getString(1)+",'"+db.resultado.getString(2)+"');\" ><img src=\"css/bootstrap/img/deletar.gif\"></a></td>"
                        + "</tr>";
            }
            res+="</table>";//"+getPickupPeers(db.resultado.getString(1))+"

        } catch (SQLException ex) {
            res="Falha na lista: "+ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String seekMohName(String mohName){
        String res=null;
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select count(name) from mohs "
                    + "where name='"+mohName+"'");
            if(db.resultado.next())
                res=db.resultado.getString(1);
        } catch (SQLException ex) {
            Logger.getLogger(JpbxDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        db.disconnectDB();
        return res;
    }
    public String createMoh(String name,String moh,String description){
        JpbxDBConnect db=new JpbxDBConnect();
        String res = "ok";
        try {
            int sql=db.Comando.executeUpdate("insert into mohs (name,moh,description) "
                    + "values ('"+name+"','"+moh+"','"+description+"')");
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        if(res.equals("ok"))
            res=writeMohs();
        return res;
    }
    public String deleteMoh(String id){
        JpbxDBConnect db=new JpbxDBConnect();
        String res = "ok";
        try {
            int sql=db.Comando.executeUpdate("delete from mohs where moh_id="+id);
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        res=writeMohs();
        return res;
    }
    public String[] editMoh(String moh){
        JpbxDBConnect db=new JpbxDBConnect();
        String res[]=new String[3];
        try {
            db.resultado=db.Comando.executeQuery("select name,moh,description from mohs where moh_id="+moh);
            
            while(db.resultado.next()){
                res[0]=db.resultado.getString(1);
                res[1]=db.resultado.getString(2);
                res[2]=db.resultado.getString(3);
            }
        } catch (SQLException ex) {
            Logger.getLogger(JpbxDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        db.disconnectDB();
        return res;
    }
    public String alterMohDescription(String id,String description){
        JpbxDBConnect db=new JpbxDBConnect();
        String res = "ok";
        try {
            int sql=db.Comando.executeUpdate("update mohs set description='"+description+"' where moh_id="+id);
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String verifyBackup(){
        String res="ok",wStore = null;
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("SELECT week(date) from backups order by idbackups desc limit 1");
            if(db.resultado.next())
                wStore=db.resultado.getString(1);
            db.resultado=db.Comando.executeQuery("SELECT week(now())");
            if(db.resultado.next())
                if(!wStore.equals(db.resultado.getString(1)))
                    res=wStore;
            db.disconnectDB();
        } catch (SQLException ex) {
            Logger.getLogger(JpbxDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }
    public String listBackups(){
        JpbxDBConnect db=new JpbxDBConnect();
        String qtde,res="<table class=\"span11 table-striped table-condensed table-hover table-bordered\">"
                + "<tr><th>Código</th><th>Data Hora</th><th>Período</th><th>Status</th><th>Ações</th><th></th></tr>";
        try {
            db.resultado=db.Comando.executeQuery("select idbackups,date_format(date,'%d/%m/%Y %H:%i:%s'),period,active,archive "
                    + "from backups");
            while(db.resultado.next()){
                res+="<tr><td>"+db.resultado.getString(1)+"</td>"
                        + "<td>"+db.resultado.getString(2)+"</td>"
                        + "<td>"+db.resultado.getString(3)+"</td>"
                        + "<td>"+(db.resultado.getString(4).equals("1")?"<img id=\"load\" src=\"css/bootstrap/img/checked.png\"/>"
                            :"<img src=\"css/bootstrap/img/loading.gif\"/>")+"</td>"
  
                        + "<td><a rel=\"tooltip\" data-placement=\"right\" title=\"Apagar\" href=\"#\" onclick=\"deleteBkp("+db.resultado.getString(1)+");\" ><img src=\"css/bootstrap/img/deletar.gif\"></a></td>"
                        + "<td><a rel=\"tooltip\" data-placement=\"right\" title=\"Baixar\" href=\"asterisk/backups/"+db.resultado.getString(5)+"\" >"
                        + "<img src=\"css/bootstrap/img/download.png\"></a> "+lenBackup(db.resultado.getString(5))+"</td>"
                        + "</tr>";
            }
            res+="</table>";

        } catch (SQLException ex) {
            res="Falha na lista: "+ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    private String lenBackup(String bkp){
        String res = null,ls_str;
            Process ls_proc;
        try {
                ls_proc = Runtime.getRuntime().exec("du -h /etc/asterisk/jpbx/backups/"+bkp);
                DataInputStream ls_in = new DataInputStream(
                                              ls_proc.getInputStream());
                if ((ls_str = ls_in.readLine()) != null){
                    res=ls_str.substring(0, ls_str.indexOf("/")-1);
                }
            } catch (IOException ex) {
                res=ex.getMessage();
            }
        return res;
    }
    public String activateRule(String id,String act){
        String res="ok";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            int sql=db.Comando.executeUpdate("update dialplan_rules set active="+act+" "
                    + "where rule_id="+id);
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String writeMohs(){
        String res="ok";
        File makefile=new File("webapps/jpbx/asterisk/mohClasses.conf");
        FileWriter fwrite;
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select name from mohs");
            fwrite = new FileWriter(makefile);
            fwrite.write(";;Arquivo escrito automaticamente pelo sistema JPBX\n"
                    + ";;Portanto inutil sua edicao manual\n\n");
            while(db.resultado.next()){
                fwrite.write("["+db.resultado.getString(1)+"]\n");
                fwrite.write("mode=files\n");
                fwrite.write("directory=moh/"+db.resultado.getString(1)+"\n");
            }
            fwrite.flush();
            fwrite.close();
        } catch (IOException ex) {
            res=ex.getMessage();
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        if(res.equals("ok")){
            try{
                Asterisk ast=new Asterisk();
                ast.AstConnect();
                List<String> foo=ast.getInfos("moh reload");
                ast.AstDisconnect();           
            } catch (TimeoutException ex) {
                res=ex.getMessage();
            } catch (AuthenticationFailedException ex) {
                res=ex.getMessage();
            } catch (IOException ex){
                res=ex.getMessage();
            }
        }
        return res;
    }
    public String optionsMoh(){
        String res="";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select name from mohs");
            while(db.resultado.next()){
                res+="<option>"+db.resultado.getString(1)+"</option>";
            }
            db.disconnectDB();
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        return res;
    }
    public String optionMohSelected(String select){
        String res="";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select name from mohs");
            while(db.resultado.next()){
                res+="<option "+(db.resultado.getString(1).equals(select)?"selected":"")+">"+db.resultado.getString(1)+"</option>";
            }
            db.disconnectDB();
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        return res;
    }
    public String createCos(String name){
        JpbxDBConnect db=new JpbxDBConnect();
        String res = "ok";
        try {
            int sql=db.Comando.executeUpdate("insert into cos (name) values ('"+name+"')");
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String seekCos(String name){
        JpbxDBConnect db=new JpbxDBConnect();
        String res = "ok";
        try {
            db.resultado=db.Comando.executeQuery("select idcos from cos where name='"+name+"'");
            if(db.resultado.next())
                res=db.resultado.getString(1);
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String createCosPeer(String id,String peer){
        JpbxDBConnect db=new JpbxDBConnect();
        String res = "ok";
        try {
            int sql=db.Comando.executeUpdate("insert into cos_peers (idcos,peer) values ("+id+","+peer+")");
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String createCosType(String id,String type){
        JpbxDBConnect db=new JpbxDBConnect();
        String res = "ok";
        try {
            int sql=db.Comando.executeUpdate("insert into cos_types (idcos,costype) values ("+id+","+type+")");
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String deleteCos(String id){
        JpbxDBConnect db=new JpbxDBConnect();
        String res = "ok";
        try {
            int sql=db.Comando.executeUpdate("delete from cos_types where idcos="+id);
            sql=db.Comando.executeUpdate("delete from cos_peers where idcos="+id);
            sql=db.Comando.executeUpdate("delete from cos where idcos="+id);
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String editCos(String id){
        JpbxDBConnect db=new JpbxDBConnect();
        String res = "ok";
        try {
            int sql=db.Comando.executeUpdate("delete from cos_types where idcos="+id);
            sql=db.Comando.executeUpdate("delete from cos_peers where idcos="+id);
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String verifyCosCall(String peer,String ccost){
        JpbxDBConnect db=new JpbxDBConnect();
        String res = "0";
        try {
            db.resultado=db.Comando.executeQuery("select idcos from cos_peers where peer="+peer);
            while(db.resultado.next())
                if(compareCos(db.resultado.getString(1), ccost)>0){
                    res="1";
                    break;
            }
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public int compareCos(String idCos,String ccost){
        JpbxDBConnect db=new JpbxDBConnect();
        int res =0;
        try {
            db.resultado=db.Comando.executeQuery("select costype from cos_types where idcos="+idCos);
            while(db.resultado.next())
                if(db.resultado.getString(1).equals(ccost)){
                    res=1;
                    break;
                }
        } catch (SQLException ex) {
            res=ex.getErrorCode();
        }
        db.disconnectDB();
        return res;
    }
    public String cosName(String idCos){
        JpbxDBConnect db=new JpbxDBConnect();
        String res = null;
        try {
            db.resultado=db.Comando.executeQuery("select name from cos where idcos="+idCos);
            if(db.resultado.next())
                res=db.resultado.getString(1);
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String listCosPeersSelected(String idcos){
        String res="";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select peer from cos_peers where idcos="+idcos);
            while(db.resultado.next()){
                res+="<li>"+db.resultado.getString(1)+"</li>";
            }
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String listCosTypesSelected(String idcos){
        String res="";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select costype from cos_types where idcos="+idcos);
            while(db.resultado.next()){
                res+="<li>"+db.resultado.getString(1)+"-"+completeCosTypesSelected(db.resultado.getString(1))+"</li>";
            }
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String completeCosTypesSelected(String ccost){
        String res="";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select ccost_name from ccosts where ccost="+ccost);
            while(db.resultado.next()){
                res=db.resultado.getString(1);
            }
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String editEmail(String email,String pass,String smtp,String port,String tls,String ssl){
        JpbxDBConnect db=new JpbxDBConnect();
        String res = "ok";
        try {
            int sql=db.Comando.executeUpdate("update serv_email set email='"+email+"',password='"+pass+"',"
                    + "smtp='"+smtp+"',port="+port+",sec_tls="+tls+",sec_ssl="+ssl+" where idserv_email=1");
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String[] showEmail(){
        String res[]=new String[6];
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select email,password,smtp,port,sec_tls,sec_ssl from serv_email limit 1");
            if(db.resultado.next()){
              res[0]=db.resultado.getString(1);
              res[1]=db.resultado.getString(2);
              res[2]=db.resultado.getString(3);
              res[3]=db.resultado.getString(4);
              res[4]=db.resultado.getString(5);
              res[5]=db.resultado.getString(6);
            }
        } catch (SQLException ex) {
            res[0]=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String[] notifyVoicemail(String peer){
        String res[]=new String[2];
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select callerid,email from peers where name="+peer);
            if(db.resultado.next()){
              res[0]=db.resultado.getString(1);
              res[1]=db.resultado.getString(2);
            }
        } catch (SQLException ex) {
            res[0]=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    String getTrunkName(String canal){
        String res="0";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select trunk_name from trunks where canal='"+canal+"'");
            if(db.resultado.next()){
              res=db.resultado.getString(1);
            }
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    String translateER(String er){
        String res=er;
        res=res.toUpperCase();
        res=res.replaceAll("_", "");
        res=res.replaceAll("\\|", "");
        res=res.replaceAll("X", "[0-9]");
        res=res.replaceAll("Z", "[1-9]");
        res=res.replaceAll("N", "[2-9]");
        res=res.replaceAll("\\.", "[0-9].*");
        res=res.replaceAll("!", ".*");
        return res;
    }
    public String[] aliasExpressions(String id){
        String res[] = null;
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select expression from alias_expressions where idalias="+id);
            db.resultado.last();
            res=new String[db.resultado.getRow()];
            db.resultado.beforeFirst();
            int loop=0;
            while(db.resultado.next()){
              res[loop]=db.resultado.getString(1);
              loop++;
            }
        } catch (SQLException ex) {
            res[0]=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String listAlias(){
        JpbxDBConnect db=new JpbxDBConnect();
        String qtde,res="<table class=\"span11 table-striped table-condensed table-hover table-bordered\">"
                + "<tr><th>Código</th><th>Nome</th><th>Ações</th><th></th></tr>";
        try {
            db.resultado=db.Comando.executeQuery("select idalias,name from alias");
            while(db.resultado.next()){
                res+="<tr><td>"+db.resultado.getString(1)+"</td>"
                        + "<td>"+db.resultado.getString(2)+"</td>"
                        + "<td><a rel=\"tooltip\" data-placement=\"right\" title=\"Editar\" href=\"editAlias.jsp?id="+db.resultado.getString(1)+"\" ><img src=\"css/bootstrap/img/icone-editar.gif\"></a></td>"
                        + "<td><a rel=\"tooltip\" data-placement=\"right\" title=\"Apagar\" href=\"#\" onclick=\"deleteAlias("+db.resultado.getString(1)+",'"+db.resultado.getString(2)+"');\" ><img src=\"css/bootstrap/img/deletar.gif\"></a></td>"
                        + "</tr>";
            }
            res+="</table>";

        } catch (SQLException ex) {
            res="Falha na lista: "+ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    String createAlias(String aliasName){
        String res="ok";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            int sql=db.Comando.executeUpdate("insert into alias (name) values ('"+aliasName+"')");
            db.resultado=db.Comando.executeQuery("select idalias from alias where name='"+aliasName+"'");
            if(db.resultado.next())
                res=db.resultado.getString(1);
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    String createAliasDesc(int idAlias,String desc){
        String res="ok";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            int sql=db.Comando.executeUpdate("insert into alias_expressions (idalias,expression) "
                    + "values ("+idAlias+",'"+desc+"')");
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String getAliasName(String id){
        String res="ok";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select name from alias where idalias="+id);
            if(db.resultado.next())
                res=db.resultado.getString(1);
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String[] getAliasDescs(String id){
        String res[]={"","1"};
        int qtde=0;
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select expression from alias_expressions"
                    + " where idalias="+id);
            while(db.resultado.next()){
                res[0]+="<div id=\"desc"+db.resultado.getRow()+"\">\n" +
"                            <input type=\"text\" value=\""+db.resultado.getString(1)+"\" name=\"desc"+db.resultado.getRow()+"\" />\n" +
"                            <a class=\"icon-plus-sign\" href=\"#\" onclick=\"addDesc();\"></a>\n" +
"                            <a class=\"icon-minus-sign\" href=\"#\" onclick=\"delDesc('desc"+db.resultado.getRow()+"');\"></a>\n" +
"                        </div>";
                qtde++;
            }
        } catch (SQLException ex) {
            res[0]=ex.getMessage();
        }
        db.disconnectDB();
        res[1]=String.valueOf(qtde);
        return res;
    }
    String deleteAliasDesc(String id){
        String res="ok";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            int sql=db.Comando.executeUpdate("delete from alias_expressions where idalias="+id);
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    String deleteAlias(String id){
        String res="ok";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            int sql=db.Comando.executeUpdate("delete from alias where idalias="+id);
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String aliasOptions(){
        String res="";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select name,idalias from alias");
            while(db.resultado.next()){
                res+="<option value=\""+db.resultado.getString(2)+"\">"+db.resultado.getString(1)+"</option>";
            }
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String aliasOptions(String aliasName){
        String res="";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select name,idalias from alias");
            while(db.resultado.next()){
                if(db.resultado.getString(1).equals(aliasName))
                    res+="<option selected=\"\" value=\""+db.resultado.getString(2)+"\">"+db.resultado.getString(1)+"</option>";
                else
                    res+="<option value=\""+db.resultado.getString(2)+"\">"+db.resultado.getString(1)+"</option>";
            }
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
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
    String deleteBackup(String id){
        String res="ok";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            int sql=db.Comando.executeUpdate("delete from backups where idbackups="+id);
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    String testConnect(){
        String res="ok";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select count(date) from backups");
            if(db.resultado.next())
                res=db.resultado.getString(1);
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String writeVoicemail(){
        String res="ok";
        File makefile=new File("webapps/jpbx/asterisk/voicemail.conf");
        JpbxDBConnect db=new JpbxDBConnect();
        try {   
            FileWriter fwrite=new FileWriter(makefile);
            db.resultado=db.Comando.executeQuery("select name,mailbox,callerid,email from peers where mailbox != ''");
            fwrite.write(";Arquivo escrito automaticamente pelo sistema JPBX\n"
                    + ";Portanto inutil sua edicao manual\n\n");
        
            while(db.resultado.next()){
                fwrite.write(db.resultado.getString(1)+" => "+db.resultado.getString(2)+","+
                        db.resultado.getString(3)+","+db.resultado.getString(4));
                fwrite.write("\n");
            }
            fwrite.flush();
            fwrite.close();
       
            Asterisk ast=new Asterisk();
            db.disconnectDB();
            try {
                ast.AstConnect();
                List<String> foo=ast.getInfos("voicemail reload");
                ast.AstDisconnect();
            } catch (AuthenticationFailedException ex) {
                res="Falha de login Asterisk\n"+ex.getMessage();
            } catch (TimeoutException ex) {
                res="Tempo de retorno Asterisk demorou\n"+ex.getMessage();
            }
            } catch (SQLException ex) {
                res="Falha no banco\n"+ex.getMessage();
            } catch(IOException io){
                res="Falha no banco\n"+io.getMessage();
            }
        return res;
    }
    String alterVoicemailPasswd(String mailbox,String pass){
        String res="ok";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            int sql=db.Comando.executeUpdate("update peers set mailbox='"+pass+"' "
                    + "where name="+mailbox);
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    String createTrunkManual(String name,String canal){
        String res="ok";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            int sql=db.Comando.executeUpdate("insert into trunks (trunk_name,username,canal,tecnology) "
                    + "values ('"+name+"','"+name+"','"+canal+"','Manual')");
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    String alterTrunkManual(String name,String canal){
        String res="ok";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            int sql=db.Comando.executeUpdate("update trunks set canal='"+canal+"' "
                    + "where trunk_name='"+name+"'");
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String editTrunkManual(String trunkName){
        String res="ok";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select canal from trunks "
                    + "where trunk_name='"+trunkName+"'");
            if(db.resultado.next())
                res=db.resultado.getString(1);
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String getUserCompany(String user){
        String res="ok";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select company from users where name='"+user+"'");
            if(db.resultado.next())
                res=db.resultado.getString(1);
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String[] countRelCalls(String dateini,String dateend,String order,String src,String dst,String cost,String state,
            String srcseek,String dstseek,String company){
        JpbxDBConnect db=new JpbxDBConnect();
        int answer=0,noanswer=0, all=0,others=0,busy=0;
        double val=0;
        String srcSeek,dstSeek;
        if(srcseek.equals("equals"))
            srcSeek=" and src in ('"+src.replaceAll(",", "','")+"')";
        else if(srcseek.equals("begin"))
            srcSeek=" and src like '"+src+"%'";
        else
            srcSeek=" and src like '%"+src+"%'";
        if(dstseek.equals("equals"))
            dstSeek=" and dstfinal in ('"+dst.replaceAll(",", "','")+"')";
        else if(dstseek.equals("begin"))
            dstSeek=" and dstfinal like '"+dst+"%'";
        else
            dstSeek=" and dstfinal like '%"+dst+"%'";
        String filterSrc=(!src.equals("")?srcSeek:"");
        String filterDst=(!dst.equals("")?dstSeek:"");
        String filterCost=cost,filterStatus=state;
        String res[]=new String[6];
        if(filterCost.equals("Todos"))
            filterCost="";
        else if(filterCost.equals("Indefinido"))
            filterCost=" and accountcode='' ";
        else
            filterCost=" and accountcode="+cost.substring(0,cost.indexOf(" "));
        if(filterStatus.equals("all"))
            filterStatus="";
        else
            filterStatus=" and disposition='"+state+"'";
        try{
            db.resultado=db.Comando.executeQuery("select disposition,round(tarifador(accountcode,billsec),2),company "
                    + "from relcalls where calldate between '"+formatDate(dateini)+" 00:00:00' and '"+formatDate(dateend)+" 23:59:59' "
                    + filterSrc
                    + filterDst
                    + filterCost
                    + filterStatus+ " having company="+company);
            while(db.resultado.next()){
                if(db.resultado.getString(1).equals("ANSWERED")){
                    answer++;
                    all++;
                }
                else if(db.resultado.getString(1).equals("NO ANSWER")){
                    noanswer++;
                    all++;
                }
                else if(db.resultado.getString(1).equals("BUSY")){
                    busy++;
                    all++;
                }
                else{
                    others++;
                    all++;
                }
                val+=Double.parseDouble(db.resultado.getString(2));
            }
            String frac,strVal=String.valueOf(val);
            frac=strVal.substring(strVal.indexOf("."));
            if(frac.length()<3)
                strVal=strVal+"0";
            else if(frac.length()>3){
                int pos=strVal.indexOf(".");
                strVal=strVal.substring(0, (pos+3));
            }
            res[0]="<h3>Ligações de "+dateini+" até "+dateend+"</h3>"
                + "<h5 style=\"padding-left: 10%;color: green;float: left\">Atendidas: "+answer+"</h5>"
                    + "<h5 style=\"padding-left: 5%;color: brown;float: left\">Não Atendidas: "+noanswer+"</h5>"
                    + "<h5 style=\"padding-left: 5%;color: orange;float: left\">Ocupadas: "+busy+"</h5>"
                    + "<h5 style=\"padding-left: 5%;color: red;float: left\">Outras: "+others+"</h5>"
                    + "<h5 style=\"padding-left: 5%;color: blue;float: left\">Ligações: "+all+"</h5>"
                    + "<h5 style=\"padding-left: 5%;float: left\">Total: R$ "+strVal+"</h5>";
            res[1]=String.valueOf(answer);
            res[2]=String.valueOf(noanswer);
            res[3]=String.valueOf(busy);
            res[4]=String.valueOf(others);
            res[5]=String.valueOf(all);
        }catch(SQLException ex){
            res[0]=ex.getMessage();
        }
        return res;
    }
    public String relCalls(String dateini,String dateend,String order,String src,String dst,String cost,String state,
            int startCall,int numCalls,String srcseek,String dstseek,String company){
        JpbxDBConnect db=new JpbxDBConnect();
        int seq=startCall;
        String srcSeek,dstSeek;
        if(srcseek.equals("equals"))
            srcSeek=" and src in ('"+src.replaceAll(",", "','")+"')";
        else if(srcseek.equals("begin"))
            srcSeek=" and src like '"+src+"%'";
        else
            srcSeek=" and src like '%"+src+"%'";
        if(dstseek.equals("equals"))
            dstSeek=" and dstfinal in ('"+dst.replaceAll(",", "','")+"')";
        else if(dstseek.equals("begin"))
            dstSeek=" and dstfinal like '"+dst+"%'";
        else
            dstSeek=" and dstfinal like '%"+dst+"%'";
        String filterSrc=(!src.equals("")?srcSeek:"");
        String filterDst=(!dst.equals("")?dstSeek:"");
        String filterCost=cost,filterStatus=state;
        if(filterCost.equals("Todos"))
            filterCost="";
        else if(filterCost.equals("Indefinido"))
            filterCost=" and accountcode='' ";
        else
            filterCost=" and accountcode="+cost.substring(0,cost.indexOf(" "));
        if(filterStatus.equals("all"))
            filterStatus="";
        else
            filterStatus=" and disposition='"+state+"'";
        String status = null,
                res="<table class=\"span11 table-striped table-hover table-bordered\">"
                + "<tr><th>Seq</th><th>Data Hora</th><th>Origem</th><th>Destino</th><th>Estado Final</th><th>Duração</th>"
                + "<th>Conversação</th><th>Custo</th><th>Valor</th><th>Ações</th></tr>";
        
        try {
            db.resultado=db.Comando.executeQuery("select date_format(calldate,'%d/%m/%Y %H:%i:%s'),src,dstfinal,disposition,formatSecs(duration),formatSecs(billsec),"
                    + "(select ccost_name from ccosts where ccost=accountcode),"
                    + "round(tarifador(accountcode,billsec),2),uniqueid,company "
                    + "from relcalls where calldate between '"+formatDate(dateini)+" 00:00:00' and '"+formatDate(dateend)+" 23:59:59' "
                    + filterSrc
                    + filterDst
                    + filterCost
                    + filterStatus+ " having company="+company
                    + " order by "+order.substring(5)+" limit "+startCall+","+numCalls+"");         
            while(db.resultado.next()){ 
                seq++;
                if(db.resultado.getString(4).equals("ANSWERED")){
                    status="Atendida";
                }
                else if(db.resultado.getString(4).equals("NO ANSWER")){
                    status="Não Atendida";
                }
                else if(db.resultado.getString(4).equals("BUSY")){
                    status="Ocupada";
                }
                else if(db.resultado.getString(4).equals("FAILED")){
                    status="Falhada";
                }
                else{
                    status="Desconhecido";
                }
                res+="<tr>"
                        + "<td>"+seq+"</td>"
                        + "<td>"+db.resultado.getString(1)+"</td>"
                        + "<td>"+db.resultado.getString(2)+"</td>"
                        + "<td>"+db.resultado.getString(3)+"</td>"
                        + "<td>"+status+"</td>"
                        + "<td>"+db.resultado.getString(5)+"</td>"
                        + "<td>"+db.resultado.getString(6)+"</td> "
                        + "<td>"+(db.resultado.getString(7)!=null?db.resultado.getString(7):"Indefinido")+"</td>"
                        + "<td>R$ "+db.resultado.getString(8)+"</td>"
                        + "<td><img onclick=\"dialogs('"+db.resultado.getString(9)+"');\" src=\"css/bootstrap/img/lupasmall.png\"> "
                        + "         <img onclick=\"deleteCall('"+db.resultado.getString(9)+"');\" src=\"css/bootstrap/img/deletar.gif\"></td>"
                        + "</tr>";             
            }
            res+="</table>";    
        } catch (SQLException ex) {
            res="Falha no relatório:</br>"+ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String listCompanys(){
        JpbxDBConnect db=new JpbxDBConnect();
        String qtde,res="<table class=\"span11 table-striped table-condensed table-hover table-bordered\">"
                + "<tr><th>Código</th><th>Empresa</th><th>Ações</th><th></th></tr>";
        try {
            db.resultado=db.Comando.executeQuery("select idcompanys,name "
                    + " from companys order by name");
            while(db.resultado.next()){
                res+="<tr><td>"+db.resultado.getString(1)+"</td>"
                        + "<td>"+db.resultado.getString(2)+"</td>"
  
                        + "<td><a rel=\"tooltip\" data-placement=\"left\" title=\"Editar usuário\" href=\"alterCompany.jsp?id="+db.resultado.getString(1)+"\" ><img src=\"css/bootstrap/img/icone-editar.gif\"></a></td> "
                        + "<td><a rel=\"tooltip\" data-placement=\"right\" title=\"Apagar usuário\" href=\"#\" onclick=\"deleteCompany('"+db.resultado.getString(2)+"',"+db.resultado.getString(1)+");\" ><img src=\"css/bootstrap/img/deletar.gif\"></a></td>"
                        + "</tr>";
            }
            res+="</table>";//"+getPickupPeers(db.resultado.getString(1))+"

        } catch (SQLException ex) {
            res="Falha na lista: "+ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    String createCompany(String name,String obs){
        String res="ok";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            int sql=db.Comando.executeUpdate("insert into companys (name,obs) "
                    + "values ('"+name+"','"+obs+"')");
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    String selectCompanys(){
        String res="";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select * from companys");
            while(db.resultado.next()){
                res+="<option value=\""+db.resultado.getString(1)+"\">"+db.resultado.getString(2)+"</option>";
            }
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    String selectCompanys(String comp){
        String res="";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select * from companys");
            while(db.resultado.next()){
                res+="<option value=\""+db.resultado.getString(1)+"\" "+(db.resultado.getString(1).equals(comp)?"selected":"")+">"
                        + ""+db.resultado.getString(2)+"</option>";
            }
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    String alterCompany(String id,String name,String obs){
        String res="ok";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            int sql=db.Comando.executeUpdate("update companys set name='"+name+"',obs='"+obs+"'"
                    + " where idcompanys="+id);
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    String deleteCompany(String id){
        String res="ok";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            int sql=db.Comando.executeUpdate("delete from companys"
                    + " where idcompanys="+id);
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    String verifyStatusPeer(String peer){
        String res="ok";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select dnd,fwd,`lock` from peers "
                    + "where name="+peer);
            if(db.resultado.next())
                res="<b>Não Perturbe: </b>"+(db.resultado.getString(1).equals("0")?"Desativado":"Ativado")
                        + "<br><b>Cadeado: </b>"+(db.resultado.getString(3).equals("0")?"Desativado":"Ativado")
                        + "<br><b>Desvio: </b>"+(db.resultado.getString(2).equals("0")?"Desativado":db.resultado.getString(2));
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String[][] listDnsScript(){
        String res[][] = null;
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select dns,ip from ddns");
            db.resultado.last();
            res=new String[db.resultado.getRow()][2];
            db.resultado.beforeFirst();
            while(db.resultado.next()){
                res[db.resultado.getRow()-1][0]=db.resultado.getString(1);
                res[db.resultado.getRow()-1][1]=db.resultado.getString(2);
            }
        } catch (SQLException ex) {
            res[0][0]=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    String updateDns(String dns,String ip){
        String res="ok";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            int sql=db.Comando.executeUpdate("update ddns set ip='"+ip+"' "
                    + "where dns='"+dns+"'");
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    String createDns(String dns){
        String res="ok";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            int sql=db.Comando.executeUpdate("insert into ddns (dns) values ('"+dns+"')");
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    String createFax(String fax,String company){
        String res="ok";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            int sql=db.Comando.executeUpdate("insert into faxes (fax,src,status,type,company) "
                    + "values ('"+fax.replace(".pdf", "")+"','JPBX WEB','LOADED','Carregado',"+company+")");
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    String deleteAllDns(){
        String res="ok";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            int sql=db.Comando.executeUpdate("delete from ddns");
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    String deleteFax(String id){
        String res="ok";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            int sql=db.Comando.executeUpdate("delete from faxes where idfaxes="+id);
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    String reloadDdns(){
        String res="ok";
        File makefile=new File("/etc/asterisk/jpbx/sipContactsDDNS.conf");
        JpbxDBConnect db=new JpbxDBConnect();       
        try {
            db.resultado=db.Comando.executeQuery("select ip from ddns");
            FileWriter fwrite=new FileWriter(makefile);
            while(db.resultado.next()){
                if(!db.resultado.getString(1).equals("")){
                    fwrite.write("permit="+db.resultado.getString(1));
                    fwrite.write("/255.255.255.255\n");
                }
            }
            fwrite.flush();
            fwrite.close();
            Asterisk ast=new Asterisk();
            ast.AstConnect();
            List<String> foo=ast.getInfos("sip reload");
            ast.AstDisconnect();
        } catch (IOException ex) {
            res = ex.getMessage();
        } catch (AuthenticationFailedException ex) {
            res = ex.getMessage();
        } catch (TimeoutException ex) {
            res = ex.getMessage();
        } catch (SQLException ex) {
            Logger.getLogger(JpbxDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }
    public String listFaxes(String type){
        JpbxDBConnect db=new JpbxDBConnect();
        String qtde,res="<table class=\"span11 table-striped table-condensed table-hover table-bordered\">"
                + "<tr><th>Código</th><th>Data</th><th>Origem/Destino</th><th>Empresa</th><th>Status</th>"
                + "<th>Identificação</th><th>Arquivo</th><th>Ações</th><th></th></tr>";
        try {
            db.resultado=db.Comando.executeQuery("select idfaxes,date_format(datetime,'%d/%m/%Y %H:%i:%s'),"
                    + "company,fax,src,status,identify,type from faxes "
                    + "where type='"+type+"' order by datetime");
            String stat;
            while(db.resultado.next()){
                if(db.resultado.getString(6).equals("FAILED"))
                    stat="<td class=\"text-error\">"+db.resultado.getString(8)+" com Falha</td>";
                else if(db.resultado.getString(6).equals("SUCCESS"))
                    stat="<td class=\"text-success\">"+db.resultado.getString(8)+" OK</td>";
                else
                    stat="<td class=\"text-info\">"+db.resultado.getString(8)+"</td>";
                res+="<tr><td>"+db.resultado.getString(1)+"</td>"
                        + "<td>"+db.resultado.getString(2)+"</td>"
                        + "<td>"+db.resultado.getString(5)+"</td>"
                        + "<td>"+getCompanyName(db.resultado.getString(3))+"</td>"
                        + stat
                        + "<td>"+db.resultado.getString(7)+"</td>"
                        + "<td><a href=\"asterisk/faxes/"+db.resultado.getString(4)+".pdf\" target=\"_blank\">"
                        +   "<img src=\"css/bootstrap/img/printer.png\"></a></td>"
  
                        + "<td><a rel=\"tooltip\" data-placement=\"right\" title=\"Apagar usuário\" href=\"#\" onclick=\"deleteFax("+db.resultado.getString(1)+");\" ><img src=\"css/bootstrap/img/deletar.gif\"></a></td>"
                        + "</tr>";
            }
            res+="</table>";//"+getPickupPeers(db.resultado.getString(1))+"

        } catch (SQLException ex) {
            res="Falha na lista: "+ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String listFaxes(String type,String comp){
        JpbxDBConnect db=new JpbxDBConnect();
        String qtde,res="<table class=\"span11 table-striped table-condensed table-hover table-bordered\">"
                + "<tr><th>Código</th><th>Data</th><th>Origem/Destino</th><th>Empresa</th><th>Status</th>"
                + "<th>Identificação</th><th>Arquivo</th><th>Ações</th><th></th></tr>";
        try {
            db.resultado=db.Comando.executeQuery("select idfaxes,date_format(datetime,'%d/%m/%Y %H:%i:%s'),"
                    + "company,fax,src,status,identify,type from faxes "
                    + "where type='"+type+"' having company="+comp+" order by datetime");
            String stat;
            while(db.resultado.next()){
                if(db.resultado.getString(6).equals("FAILED"))
                    stat="<td class=\"text-error\">"+db.resultado.getString(8)+" com Falha</td>";
                else if(db.resultado.getString(6).equals("SUCCESS"))
                    stat="<td class=\"text-success\">"+db.resultado.getString(8)+" OK</td>";
                else
                    stat="<td class=\"text-info\">"+db.resultado.getString(8)+"</td>";
                res+="<tr><td>"+db.resultado.getString(1)+"</td>"
                        + "<td>"+db.resultado.getString(2)+"</td>"
                        + "<td>"+db.resultado.getString(5)+"</td>"
                        + "<td>"+getCompanyName(db.resultado.getString(3))+"</td>"
                        + stat
                        + "<td>"+db.resultado.getString(7)+"</td>"
                        + "<td><a href=\"asterisk/faxes/"+db.resultado.getString(4)+".pdf\" target=\"_blank\">"
                        +   "<img src=\"css/bootstrap/img/printer.png\"></a></td>"
  
                        + "<td><a rel=\"tooltip\" data-placement=\"right\" title=\"Apagar usuário\" href=\"#\" onclick=\"deleteFax("+db.resultado.getString(1)+");\" ><img src=\"css/bootstrap/img/deletar.gif\"></a></td>"
                        + "</tr>";
            }
            res+="</table>";//"+getPickupPeers(db.resultado.getString(1))+"

        } catch (SQLException ex) {
            res="Falha na lista: "+ex.getMessage();
        }
        db.disconnectDB();
        return res;
    }
    public String sendFaxOptions(){
        String res="<select id=\"file\">";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select idfaxes,fax,src,type from faxes "
                    + "order by datetime");
            while(db.resultado.next())
                res+="<option value=\""+db.resultado.getString(2)+"\">"+db.resultado.getString(1)+" - "
                        + ""+db.resultado.getString(3)+" - "+db.resultado.getString(4)+"</option>";
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        res+="</select>";
        db.disconnectDB();
        return res;
    }
    public String sendFaxOptions(String comp){
        String res="<select id=\"file\">";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select idfaxes,fax,src,type from faxes "
                    + "where company="+comp+" order by datetime");
            while(db.resultado.next())
                res+="<option value=\""+db.resultado.getString(2)+"\">"+db.resultado.getString(1)+" - "
                        + ""+db.resultado.getString(3)+" - "+db.resultado.getString(4)+"</option>";
        } catch (SQLException ex) {
            res=ex.getMessage();
        }
        res+="</select>";
        db.disconnectDB();
        return res;
    }
}
