/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jpbx;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.TimeoutException;

/**
 *
 * @author jefaokpta
 */
public class ScriptClearConfRoom {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        String res="0";
        JpbxDBConnect db=new JpbxDBConnect();
        try {
            db.resultado=db.Comando.executeQuery("select channel from conference_aux where room="+args[0]);
            Asterisk ast=new Asterisk();
            ast.AstConnect();
            while(db.resultado.next()){
                List<String> foo=ast.getInfos("hangup request "+db.resultado.getString(1));
            }
            ast.AstDisconnect();
            int sql=db.Comando.executeUpdate("delete from conference_aux where room="+args[0]);
            db.disconnectDB();
        } catch (SQLException ex) {
            System.out.print(ex);
        } catch (IOException ex) {
            System.out.print(ex);
        } catch (AuthenticationFailedException ex) {
            System.out.print(ex);
        } catch (TimeoutException ex) {
            System.out.print(ex);
        } 
        System.out.print(res);
    }
    
}
