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
public class AlterTrunks extends HttpServlet {

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
       if(request.getParameter("chanTech")!=null){
            JpbxDB db=new JpbxDB();
                      
            if(request.getParameter("chanTech").equals("IAX2")){
                    String bck=db.alterTrunkIax(request.getParameter("trunk_name"), request.getParameter("secret"), 
                            request.getParameter("host"), request.getParameter("lang"), request.getParameter("tone"), 
                            request.getParameter("nat"), request.getParameter("qualify"), 
                            request.getParameter("cod1")+","+request.getParameter("cod2")+","+request.getParameter("cod3"));
                    if(bck.equals("ok"))
                        response.sendRedirect("listTrunks.jsp");
                    else
                        out.print("<script>"
                            + "alert(\"Segue falha:\\n"+bck+"\");"
                            + "window.location=\"listTrunks.jsp\";"
                            + "</script>");
              
            }
            if(request.getParameter("chanTech").equals("Virtual")){
                    String bck=db.alterTrunkVirtual(request.getParameter("trunk_name"), request.getParameter("secret"), 
                            request.getParameter("host"), request.getParameter("lang"), request.getParameter("tone"), 
                            request.getParameter("nat"), request.getParameter("qualify"), 
                            request.getParameter("cod1")+","+request.getParameter("cod2")+","+request.getParameter("cod3"));
                    if(bck.equals("ok"))
                        response.sendRedirect("listTrunks.jsp");
                    else
                        out.print("<script>"
                            + "alert(\"Segue falha:\\n"+bck+"\");"
                            + "window.location=\"listTrunks.jsp\";"
                            + "</script>");
              
            }
            if(request.getParameter("chanTech").equals("SIP")){
                    String bck=db.alterTrunkSip(request.getParameter("trunk_name"), request.getParameter("secret"), 
                            request.getParameter("host"), request.getParameter("lang"), request.getParameter("tone"), 
                            request.getParameter("nat"), request.getParameter("qualify"), 
                            request.getParameter("cod1")+","+request.getParameter("cod2")+","+request.getParameter("cod3"),
                            request.getParameter("reception"), request.getParameter("register"), request.getParameter("exten"),
                            request.getParameter("username"),request.getParameter("reinvite"),request.getParameter("canreinvite"),
                            request.getParameter("limit"));
                    if(bck.equals("ok"))
                        response.sendRedirect("listTrunks.jsp");
                    else
                        out.print("<script>"
                            + "alert(\"Segue falha:\\n"+bck+"\");"
                            + "window.location=\"listTrunks.jsp\";"
                            + "</script>");
              
            }
            if(request.getParameter("chanTech").equals("Digital")){
                    String bck=db.alterTrunkDigital(request.getParameter("trunk_name"), 
                            request.getParameter("numboard"), request.getParameter("link"), request.getParameter("lang"));
                    if(bck.equals("ok"))
                        response.sendRedirect("listTrunks.jsp");
                    else
                        out.print("<script>"
                            + "alert(\"Segue falha:\\n"+bck+"\");"
                            + "window.location=\"listTrunks.jsp\";"
                            + "</script>");
              
            }
            if(request.getParameter("chanTech").equals("Manual")){
                String ret=db.alterTrunkManual(request.getParameter("trunk_name"), 
                        request.getParameter("channel"));
                if(!ret.equals("ok"))
                    request.getSession().setAttribute("error", ret);
                response.sendRedirect("listTrunks.jsp");
            }
       }
       else
            out.print("<script>"
                    + "alert(\"Tecnologia Inválida\");"
                    + "window.location=\"listTrunks.jsp\";"
                    + "</script>");
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
