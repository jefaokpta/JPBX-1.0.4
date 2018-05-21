/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jpbx;

/**
 *
 * @author jefaokpta
 */
public class ScriptVoicemailPasswd {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //args context mailbox pass
        JpbxDB db=new JpbxDB();
        String ret;
        try{
            int pass=Integer.parseInt(args[2]);
            ret=db.alterVoicemailPasswd(args[1], String.valueOf(pass));
        }catch(NumberFormatException ex){
            System.out.print("INVALID");
        }
    }
    
}
