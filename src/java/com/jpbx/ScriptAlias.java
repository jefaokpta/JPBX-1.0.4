/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jpbx;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.TimeoutException;

/**
 *
 * @author jefaokpta
 */
public class ScriptAlias {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JpbxDB db=new JpbxDB();
        Pattern patt;
        Matcher matt;
        String alias[]=db.aliasExpressions(args[0]);
        //System.out.println(alias.length);
        for(int i=0;i<alias.length;i++){
            patt=Pattern.compile(db.translateER(alias[i]));
            matt=patt.matcher(args[1]);
            if(matt.matches()){
                int pos;
                try{
                    pos=alias[i].indexOf("|");
                    if(pos<0)
                        pos=0;
                }catch(NumberFormatException ex){
                    pos=0;
                }
                Asterisk ast;
                try {
                    if(pos!=0){
                        ast = new Asterisk();
                        ast.setCutAlias(args[2], String.valueOf(pos));
                    }
                } catch (IOException ex) {
                    System.out.print(ex.getMessage());
                } catch (AuthenticationFailedException ex) {
                    System.out.print(ex.getMessage());
                } catch (TimeoutException ex) {
                    System.out.print(ex.getMessage());
                }            
                System.out.print("1");
                System.exit(0);
            }
        }
        System.out.print("0");
        
        
        // TODO code application logic here
    }
    
}
