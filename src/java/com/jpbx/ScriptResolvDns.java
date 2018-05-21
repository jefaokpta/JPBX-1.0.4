/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jpbx;

import java.io.DataInputStream;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jefaokpta
 */
public class ScriptResolvDns {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JpbxDB db=new JpbxDB();
        String ret[][]=db.listDnsScript();
        String ls_str,ip = null,res;
        int reload=0;
        DataInputStream ls_in;
        Process ls_proc;
        for(int i=0;i<ret.length;i++){  
            ip="";
            try {
                ls_proc = Runtime.getRuntime().exec("ping -w1 -c1 "+ret[i][0]);
                // get its output (your input) stream
                ls_in = new DataInputStream(ls_proc.getInputStream());
                while ((ls_str = ls_in.readLine()) != null) {
                    if(ls_str.contains(ret[i][0]+" (")){
                        ip=ls_str.substring(ls_str.indexOf("(")+1, ls_str.indexOf(")")).trim();
                        break;
                    }
		}
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }    
            System.out.println(i+" "+ret[i][0]+" b"+ret[i][1]+" p"+ip);
            if(!ip.equals(ret[i][1])){
                res=db.updateDns(ret[i][0], ip);
                reload=1;
                if(!res.equals("ok"))
                    System.out.println(res);
            }
        }
        if(reload==1){
            res=db.reloadDdns();
            if(!res.equals("ok"))
                System.out.println(res);
        }
        // TODO code application logic here
    }
    
}
