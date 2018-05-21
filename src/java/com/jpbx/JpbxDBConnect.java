/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jpbx;

import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author jefaokpta
 */
public class JpbxDBConnect {
    private String enderecoDB;
    private java.sql.Connection Conexao;
    java.sql.Statement Comando;
    java.sql.ResultSet resultado;
    
    public JpbxDBConnect() {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            enderecoDB="jdbc:mysql://localhost/jpbx";
            Conexao=DriverManager.getConnection(
                    enderecoDB,"jpbx","jpbxadmin");
            Comando=Conexao.createStatement();
           // java.sql.ResultSet resultado=Comando.executeQuery();
                        
         //   Comando.close();
         //   Conexao.close();

        }
        catch (Exception Excecao){
            System.out.println(Excecao);
            /**HttpServletResponse rp = null;
            try {
                rp.sendRedirect("index.jsp?error=Falha na conexão com banco:</br>"+
                        Excecao.getMessage());
            } catch (IOException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }**/
        }
    }
    public void disconnectDB(){
        try {
            Comando.close();
            Conexao.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        
    }
}
