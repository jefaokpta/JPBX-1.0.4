/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jpbx;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.mail.EmailException;

/**
 *
 * @author jefaokpta
 */
public class ScriptVoicemail {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //args context mailbox qtdeMens
        String msg="msg00",caller="";
        if(Integer.parseInt(args[2])<=10)
            msg+="0"+(Integer.parseInt(args[2])-1);
        else
            msg+=(Integer.parseInt(args[2])-1);
        String voicePath="var/spool/asterisk/voicemail/"+args[0]+"/"+args[1]+"/INBOX/"+msg;
        String ls_str;
        Process ls_proc;
        try {
            ls_proc = Runtime.getRuntime().exec("cat /"+voicePath+".txt");
            DataInputStream ls_in = new DataInputStream(ls_proc.getInputStream());
            while ((ls_str = ls_in.readLine()) != null) {
                if(ls_str.contains("callerid")){
                    caller=ls_str.substring(ls_str.indexOf("<")+1,ls_str.indexOf(">"));
                }
            }
        }catch(IOException ex){
            System.out.print(ex.getMessage());
        }
        //System.out.print(voicePath+" -- "+caller);
        Email email=new Email();
        try {
            email.enviaEmailVoicemail(args[1], voicePath+".wav",caller);
        } catch (EmailException ex) {
            System.out.print(ex.getMessage());
        } catch (MalformedURLException ex) {
            System.out.print(ex.getMessage());
        }
    }
    
}
