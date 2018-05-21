/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jpbx;

import java.io.DataInputStream;
import java.io.IOException;
import java.sql.SQLException;

/**
 *
 * @author jefaokpta
 */
public class ScriptBackups {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JpbxDBConnect db=new JpbxDBConnect();
        Process ls_proc;
        String ls_str = "opa";
        try {
            ls_proc=Runtime.getRuntime().exec("ls /var/log/asterisk/");
            DataInputStream ls_in = new DataInputStream(ls_proc.getInputStream());
                    while ((ls_str = ls_in.readLine()) != null) {
                        if(ls_str.contains("full."))
                           Runtime.getRuntime().exec("mv /var/log/asterisk/"+ls_str+" /tmp/"); 
                    }
            
            
            String date_old = null,date_now=null;
            int wait=0;
            db.resultado=db.Comando.executeQuery("SELECT date from backups order by idbackups desc limit 1");
            if(db.resultado.next())
                date_old=db.resultado.getString(1).substring(0, 10);
            int sql=db.Comando.executeUpdate("insert into backups (archive,period) "
                    + "values (concat(substr(now(),1,10),'.rar'),concat('"+date_old+" a ',substr(now(),1,10)))");
            db.resultado=db.Comando.executeQuery("select now()");
            if(db.resultado.next())
                date_now=db.resultado.getString(1).substring(0, 10);
            db.resultado=db.Comando.executeQuery("SELECT record FROM transf_history where time "
                    + "between '"+date_old+"' and now() and record!=''");
            Runtime.getRuntime().exec("mkdir /tmp/"+date_now);
            while(db.resultado.next()){
                wait++;
                System.out.println(db.resultado.getString(1));
                //Runtime.getRuntime().exec("cp /etc/asterisk/jpbx/records/"+db.resultado.getString(1)+".wav "
                  //      + "/tmp/"+date_now);
                ls_proc=Runtime.getRuntime().exec("lame -V0 -h -b 160 --vbr-new /etc/asterisk/jpbx/records/"+db.resultado.getString(1)+".wav "
                        + "/tmp/"+date_now+"/"+db.resultado.getString(1)+".mp3");
                ls_in = new DataInputStream(ls_proc.getInputStream());            
		while ((ls_str = ls_in.readLine()) != null) {
                    System.out.println(ls_str);
		}
            }      
            db.resultado=db.Comando.executeQuery("SELECT record FROM rel_conferences where datetime "
                    + "between '"+date_old+"' and now() and record!=''");
            while(db.resultado.next()){
                wait++;
                System.out.println(db.resultado.getString(1));
                //Runtime.getRuntime().exec("cp /etc/asterisk/jpbx/records/"+db.resultado.getString(1)+" "
                  //      + "/tmp/"+date_now);
                ls_proc=Runtime.getRuntime().exec("lame -V0 -h -b 160 --vbr-new /etc/asterisk/jpbx/records/"+db.resultado.getString(1)+" "
                        + "/tmp/"+date_now+"/"+db.resultado.getString(1).replaceAll("wav", "mp3")+"");
                ls_in = new DataInputStream(ls_proc.getInputStream());            
		while ((ls_str = ls_in.readLine()) != null) {
                    System.out.println(ls_str);
		}
            }
            ls_proc=Runtime.getRuntime().exec("rar a /etc/asterisk/jpbx/backups/"+date_now+".rar /tmp/"+date_now);
            ls_in = new DataInputStream(ls_proc.getInputStream());            
		while ((ls_str = ls_in.readLine()) != null) {
                    System.out.println(ls_str);
		}
            System.out.println(wait);
            Thread.sleep(wait*1000);
            Runtime.getRuntime().exec("rm -r /tmp/"+date_now);
            Runtime.getRuntime().exec("chmod -R 775 /etc/asterisk/jpbx/");
            Runtime.getRuntime().exec("chown -R tomcat6.tomcat6 /etc/asterisk/jpbx/");
            sql=db.Comando.executeUpdate("update backups set active=1 where archive like '"+date_now+"%'");
            db.disconnectDB(); //fim script
        } catch (IOException ex) {
            System.out.print(ex);
        } catch (SQLException ex) {
            System.out.print(ex);
        } catch (InterruptedException ex) {
            System.out.print(ex);
        }
        //System.out.print(ls_str);
    }
    
}
