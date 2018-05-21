/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jpbx;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jefaokpta
 */
public class ScriptPeersLot {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int begin=Integer.parseInt(args[0]);
        int end=Integer.parseInt(args[1]);
        if(begin<10){
            System.out.println("Parametro "+begin+" invalido");
            System.exit(0);
        }
        if(end>9999){
            System.out.println("Parametro "+end+" invalido");
            System.exit(0);
        }
        int sum=0;
        Scanner scan = new Scanner (System.in);    
        System.out.println ("Digite uma senha para os ramais: ");    
        String pass = scan.nextLine();
        scan = new Scanner (System.in);    
        System.out.println ("Digite um nome para os ramais: ");    
        String name = scan.nextLine();
        String passMD5;
        JpbxDB db=new JpbxDB();
        for(int i=begin;i<=end;i++){
            System.out.println("Criando ramal "+i);
            passMD5 = i+":asterisk:"+pass;
            //System.out.println(db.md5(passMD5));
            db.createPeer(String.valueOf(i), db.md5(passMD5), name+i, "pt_BR", "rfc2833", "yes", "yes", "0",
                    "1", 5, "alaw,ulaw,gsm", "1234","","","");
            sum++;
        }          
        System.out.println("Criados "+sum+" ramais");
        // TODO code application logic here
    }
    
}
