/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jpbx;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.TimeoutException;

/**
 *
 * @author jefaokpta
 */
public class ControllerAsterisk extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        Asterisk ast=new Asterisk();
        if(request.getParameter("action").equals("liga")){
            try {
                response.sendRedirect("return.jsp?return="+ast.originateCall()+"&dest=home.jsp");
            } catch (AuthenticationFailedException ex) {
                response.sendRedirect("return.jsp?return="+ex.getMessage()+"&dest=home.jsp");
            } catch (TimeoutException ex) {
                response.sendRedirect("return.jsp?return="+ex.getMessage()+"&dest=home.jsp");
            }
        }
        if(request.getParameter("action").equals("version")){
                    try {
                        //out.print("não implementado");
                        String ver=ast.getInfos("core show version").get(0);
                        out.print(ver.substring(ver.indexOf("A"), ver.indexOf("b")));
                    } catch (AuthenticationFailedException ex) {
                        Logger.getLogger(ControllerAsterisk.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (TimeoutException ex) {
                        Logger.getLogger(ControllerAsterisk.class.getName()).log(Level.SEVERE, null, ex);
                    }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
