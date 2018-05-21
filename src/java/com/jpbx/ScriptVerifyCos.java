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
public class ScriptVerifyCos {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JpbxDB db=new JpbxDB();
        System.out.print(db.verifyCosCall(args[0], args[1]));
    }
    
}
