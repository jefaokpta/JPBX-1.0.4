/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jpbx;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.TimeoutException;

/**
 *
 * @author jefaokpta
 */
public class ScriptTestConnection {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            Asterisk ast=new Asterisk();
            ast.AstConnect();
            ast.getInfos("logger rotate");
            ast.AstDisconnect();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } catch (AuthenticationFailedException ex) {
            System.out.println(ex.getMessage());
        } catch (TimeoutException ex) {
            System.out.println(ex.getMessage());
        }
        
        // TODO code application logic here
    }
    
}
