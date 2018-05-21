/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jpbx;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jefaokpta
 */
public class CreateCallGrp extends HttpServlet {

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
        JpbxDB db=new JpbxDB();
        String fdb = null;

        for(int i=1;i<=Integer.parseInt(request.getParameter("qtde"));i++){
           fdb=db.callGrpRemoveMember(request.getParameter("peer"+i));
           if(!fdb.equals("ok"))
               break;
        }
        if(!fdb.equals("ok")){
            out.print(fdb);
            return;
        }
        fdb=db.createCallGrp(request.getParameter("grpName"), request.getParameter("timeout"), request.getParameter("strategy"),
                request.getParameter("moh"),request.getParameter("company"));
        for(int i=1;i<=Integer.parseInt(request.getParameter("qtde"));i++){
           fdb=db.callGrpAddMember(request.getParameter("peer"+i),request.getParameter("grpName"));
           if(!fdb.equals("ok"))
               break;
        }
        if(!fdb.equals("ok")){
            out.print(fdb);
            return;
        }
        
        response.sendRedirect("listCallGrps.jsp");
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
