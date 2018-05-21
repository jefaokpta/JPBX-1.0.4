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
public class CreatePeers extends HttpServlet {

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
        try{
            int test=Integer.parseInt(request.getParameter("peer"));
            JpbxDB db=new JpbxDB();
            if(db.testDuplicate(request.getParameter("peer"))){
                String res=db.createPeer(request.getParameter("peer"), request.getParameter("secret"), request.getParameter("name"), 
                        request.getParameter("lang"), request.getParameter("tone"), request.getParameter("nat"), 
                        request.getParameter("qualify"), request.getParameter("record"), 
                        request.getParameter("call-limit"), db.parsePickupGroup(request.getParameter("pickupgroup")),
                        request.getParameter("cod1")+","+request.getParameter("cod2")+","+request.getParameter("cod3"),
                        (request.getParameter("featsecret").equals("")?"1234":request.getParameter("featsecret")),
                        request.getParameter("mailboxSecret"),request.getParameter("email"),request.getParameter("company"));
                if(res.equals("ok"))
                    response.sendRedirect("listPeers.jsp");
                else{
                    request.getSession().setAttribute("error", res);
                    response.sendRedirect("listPeers.jsp");
                }
            }
            else{
               response.sendRedirect("createPeers.jsp?duplicated="+request.getParameter("peer"));
            }
        }
        catch(NumberFormatException ex){
            response.sendRedirect("createPeers.jsp?invalid="+request.getParameter("peer"));
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
