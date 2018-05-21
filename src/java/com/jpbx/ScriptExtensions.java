/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jpbx;

import java.sql.SQLException;

/**
 *
 * @author jefaokpta
 */
public class ScriptExtensions {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String res="0";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select name from peers");
            while(db.resultado.next()){
                if(args[0].equals(db.resultado.getString(1))){
                    res="1";
                    break;
                }
            }
            db.disconnectDB();
        } catch (SQLException ex) {
            System.out.print(ex);
        }
        System.out.print(res);
    }
    
}
