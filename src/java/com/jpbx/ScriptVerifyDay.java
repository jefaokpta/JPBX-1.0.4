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
public class ScriptVerifyDay {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        String day=null,res="0";  
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select weekday(now())");
            if(db.resultado.next())
                day=db.resultado.getString(1);
            db.disconnectDB();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        for(int i=0;i<args.length;i++){
            if(day.equals(args[i])){
                res="1";
                break;
            }
        }
        System.out.print(res);
    }
}
