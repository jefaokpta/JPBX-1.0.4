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
public class ScriptVerifyTrunk {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JpbxDB db=new JpbxDB();
        if(args[0].substring(0, 3).equals("SIP")||args[0].substring(0, 4).equals("IAX2"))
            System.out.print(db.getTrunkName(args[0]));
    }
    
}
