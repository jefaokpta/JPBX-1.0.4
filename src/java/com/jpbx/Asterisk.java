/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jpbx;

import java.io.IOException;
import java.util.List;
import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionFactory;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.CommandAction;
import org.asteriskjava.manager.action.OriginateAction;
import org.asteriskjava.manager.action.SetVarAction;
import org.asteriskjava.manager.response.CommandResponse;
import org.asteriskjava.manager.response.ManagerResponse;

/**
 *
 * @author jefaokpta
 */
public class Asterisk {
    private ManagerConnection managerConnection;
    
    public Asterisk() throws IOException{
        ManagerConnectionFactory factory=new
                        ManagerConnectionFactory("localhost", "jpbx", "jpbxadmin");
        this.managerConnection=factory.createManagerConnection();
    }
    
    public String originateCall() throws IOException,AuthenticationFailedException,
            TimeoutException{
        OriginateAction originateAction;
        ManagerResponse originateResponse;
        
        originateAction=new OriginateAction();
        originateAction.setChannel("SIP/1000");
        originateAction.setCallerId("Jpbx");
        originateAction.setContext("jpbxRoute");
        originateAction.setExten("*10");
        originateAction.setPriority(1);
        originateAction.setTimeout(60000);
        
        //Conectando e logando ao Ast*
        managerConnection.login();
        //Envie a action e espere por 30 seg a resposta do *
        originateResponse=managerConnection.sendAction(originateAction,60000);
        //Informação de Success ou Fail
        String res=originateResponse.getResponse();
        //Finaliza e desconecta
        managerConnection.logoff();
        return res;
    }
    String sendFax(String dst,String file,String trunk,String stationId,String company) throws IOException,AuthenticationFailedException,
            TimeoutException{
        OriginateAction originateAction;
        ManagerResponse originateResponse;
        
        originateAction=new OriginateAction();
        originateAction.setChannel("Local/"+dst+"@sendFaxCall");
        originateAction.setCallerId("JPBX FAX - "+stationId+" <9999>");
        originateAction.setContext("sendFax");
        originateAction.setExten(company+"#"+dst);
        originateAction.setPriority(1);
        originateAction.setTimeout(60000);
        originateAction.setVariable("FAX_FILE", file);
        
        //Conectando e logando ao Ast*
        managerConnection.login();
        //Envie a action e espere por 30 seg a resposta do *
        originateResponse=managerConnection.sendAction(originateAction,60000);
        //Informação de sucess ou fail
        String res=originateResponse.getResponse();
        //Finaliza e desconecta
        managerConnection.logoff();
        return res;
    }
    String sendFax(String dst,String file,String stationId,String company) throws IOException,AuthenticationFailedException,
            TimeoutException{
        OriginateAction originateAction;
        ManagerResponse originateResponse;
        
        originateAction=new OriginateAction();
        originateAction.setChannel("SIP/"+dst);
        originateAction.setCallerId("JPBX FAX - "+stationId+" <9999>");
        originateAction.setContext("sendFax");
        originateAction.setExten(company+"#"+dst);
        originateAction.setPriority(1);
        originateAction.setTimeout(30000);
        originateAction.setVariable("FAX_FILE", file);
        
        //Conectando e logando ao Ast*
        managerConnection.login();
        //Envie a action e espere por 30 seg a resposta do *
        originateResponse=managerConnection.sendAction(originateAction,30000);
        //Informação de sucess ou fail
        String res=originateResponse.getResponse();
        //Finaliza e desconecta
        managerConnection.logoff();
        return res;
    }
    public String removeCall(String record) throws IOException,AuthenticationFailedException,
            TimeoutException{
        OriginateAction originateAction;
        ManagerResponse originateResponse;
        
        originateAction=new OriginateAction();
        originateAction.setChannel("Console/dsp");
        originateAction.setCallerId("JPBX WEB");
        originateAction.setApplication("System");
        originateAction.setData("rm /etc/asterisk/jpbx/records/"+record+".wav");
        
        //Conectando e logando ao Ast*
        managerConnection.login();
        //Envie a action e espere por 30 seg a resposta do *
        originateResponse=managerConnection.sendAction(originateAction,30000);
        //Informação de sucess ou fail
        String res=originateResponse.getResponse();
        //Finaliza e desconecta
        managerConnection.logoff();
        return res;
    }
    public void execBkp() throws IOException,AuthenticationFailedException,
            TimeoutException, InterruptedException{
        OriginateAction originateAction;
        OriginateAction originate1Action;
        ManagerResponse originateResponse;
        
        originateAction=new OriginateAction();
        originateAction.setChannel("Console/dsp");
        originateAction.setCallerId("JPBX WEB");
        originateAction.setApplication("System");
        originateAction.setData("/java -cp /scriptsJPBX com.jpbx.ScriptBackups");
        
        originate1Action=new OriginateAction();
        originate1Action.setChannel("Console/dsp");
        originate1Action.setCallerId("JPBX WEB");
        originate1Action.setApplication("System");
        originate1Action.setData("/java -cp /scriptsJPBX com.jpbx.ScriptTestConnection");
        
        //Conectando e logando ao Ast*
        managerConnection.login();
        //Envie a action e espere por 30 seg a resposta do *
        managerConnection.sendAction(originate1Action,30000);
        Thread.sleep(25000);
        originateResponse=managerConnection.sendAction(originateAction,30000);
        //Informação de sucess ou fail
        String res=originateResponse.getResponse();
        //Finaliza e desconecta
        managerConnection.logoff();
    }
    public String removeConference(String record) throws IOException,AuthenticationFailedException,
            TimeoutException{
        OriginateAction originateAction;
        ManagerResponse originateResponse;
        
        originateAction=new OriginateAction();
        originateAction.setChannel("Console/dsp");
        originateAction.setCallerId("JPBX WEB");
        originateAction.setApplication("System");
        originateAction.setData("rm /etc/asterisk/jpbx/records/"+record);
        
        //Conectando e logando ao Ast*
        managerConnection.login();
        //Envie a action e espere por 30 seg a resposta do *
        originateResponse=managerConnection.sendAction(originateAction,30000);
        //Informação de sucess ou fail
        String res=originateResponse.getResponse();
        //Finaliza e desconecta
        managerConnection.logoff();
        return res;
    }
    public void setCutAlias(String channel,String pos) throws IOException,AuthenticationFailedException,
            TimeoutException{

        //CommandResponse commandResponse;
        managerConnection.login();
        SetVarAction setVarAction = new SetVarAction(channel, "ALIASCUT", pos);
        managerConnection.sendAction(setVarAction);

        managerConnection.logoff();
    }
    public List<String> getInfos(String command) throws IOException,AuthenticationFailedException,
            TimeoutException{
    
        CommandAction commandAction;
        CommandResponse commandResponse;
        
        commandAction=new CommandAction(command);
        
        commandResponse=(CommandResponse) managerConnection.sendAction(commandAction,3000);
        List<String> res=commandResponse.getResult();
        return res;
    }
    public void AstConnect() throws IOException,AuthenticationFailedException,
            TimeoutException{
        managerConnection.login();
    }
    public void AstDisconnect() throws IOException,AuthenticationFailedException,
            TimeoutException{
        managerConnection.logoff();
    }
}
