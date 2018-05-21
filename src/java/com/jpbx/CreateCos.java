/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
public class CreateCos extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        JpbxDB db=new JpbxDB();
        String ret=db.createCos(request.getParameter("cosName"));
        if(ret.equals("ok")){
            String idCos=db.seekCos(request.getParameter("cosName"));
            for(int i=1;i<=Integer.parseInt(request.getParameter("qtde"));i++){
                ret=db.createCosPeer(idCos, request.getParameter("peer"+i));
                if(!ret.equals("ok")){
                    request.getSession().setAttribute("error", ret);
                    break;
                }
            }
            for(int i=1;i<=Integer.parseInt(request.getParameter("qtde2"));i++){
                ret=db.createCosType(idCos, request.getParameter("cost"+i).substring(0, 
                        request.getParameter("cost"+i).indexOf("-")));
                if(!ret.equals("ok")){
                    request.getSession().setAttribute("error", ret);
                    break;
                }
            }
        }
        else{
            request.getSession().setAttribute("error", ret);           
        }
        response.sendRedirect("cos.jsp");
        /**PrintWriter out = response.getWriter();
        try {
          
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet CreateCos</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println(request.getParameter("cosName"));
            for(int i=1;i<=Integer.parseInt(request.getParameter("qtde"));i++){
                out.print(request.getParameter("peer"+i)+"<br>");
            }
            for(int i=1;i<=Integer.parseInt(request.getParameter("qtde2"));i++){
                out.print(request.getParameter("cost"+i)+"<br>");
            }
            out.println("</body>");
            out.println("</html>");
        } finally {
            out.close();
        }**/
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
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
     * Handles the HTTP <code>POST</code> method.
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
