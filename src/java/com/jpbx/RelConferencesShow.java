/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jpbx;

import java.io.File;
import java.io.FileWriter;
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
public class RelConferencesShow extends HttpServlet {

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
        Controller head=new Controller();
        JpbxDB db=new JpbxDB();
        PrintWriter out = response.getWriter();
        out.print("<html>"
                + "<head>");     
        if(request.getSession().getAttribute("user")==null){
            response.sendRedirect("logout.jsp?error=Desconectado por inatividade.");
            return;
        }
        int pagSeq=0;
        int numCalls=Integer.parseInt(db.countRelConferences(request.getParameter("dateini"),request.getParameter("dateend"),
                    request.getParameter("rooms")));
        if(!request.getSession().getAttribute("lvl").equals("Administrador"))
            numCalls=Integer.parseInt(db.countRelConferences(request.getParameter("dateini"),request.getParameter("dateend"),
                    request.getParameter("rooms"),db.getUserCompany(request.getSession().getAttribute("user").toString())));
        int data[]=db.getLimitRelCalls();
        int limitSeq=data[0];
        out.print(head.header("JPBX->Conferences->Show")+""
                + "<link rel=\"stylesheet\" href=\"css/jquery-ui-1.10.3/themes/base/jquery-ui.css\"/>\n" +
                "<script src=\"css/jquery-ui-1.10.3/ui/jquery-ui.js\"></script>");
            
        out.print("</head>"
                + "<body>");
        out.print(head.navigator(request.getSession().getAttribute("lvl").toString()));//conta os registros
        if(request.getSession().getAttribute("lvl").equals("Administrador"))
            out.print("<div id=\"container\" class=\"container table-bordered\">"
                + "<h3>Conferencias de "+request.getParameter("dateini")+" até "+request.getParameter("dateend")+"</h3>"
                +"<div id=\"rel\">"+ db.relConferences(request.getParameter("dateini"),request.getParameter("dateend"),
                    request.getParameter("rooms"),pagSeq,limitSeq));// escreve registros em tabela e fecha
        else
            out.print("<div id=\"container\" class=\"container table-bordered\">"
                + "<h3>Conferencias de "+request.getParameter("dateini")+" até "+request.getParameter("dateend")+"</h3>"
                +"<div id=\"rel\">"+ db.relConferences(request.getParameter("dateini"),request.getParameter("dateend"),
                    request.getParameter("rooms"),pagSeq,limitSeq,
                    db.getUserCompany(request.getSession().getAttribute("user").toString())));// escreve registros em tabela e fecha
        out.print("</div>" //fecha div rel
                + "<div class=\"clearfix\"></div>");
        if(numCalls==0)
            out.print("<script>"
                        + "alert('Nenhum Dado Encontrado.');"
                        + "window.history.back(-1);</script>");
        //começa paginação
        out.print("<div class=\"pagination pagination-right pagination-small\" style=\"padding-right: 5%\">"
                    +"<ul>");
        if(numCalls>limitSeq){
            int pages=(numCalls/limitSeq);
            if((numCalls%limitSeq)!=0)
                pages++;
            for(int i=1;i<=pages;i++){
                            out.print("<li id=\"pag"+i+"\" "+(i==1?"class=\"active\"":"")+"><a onclick=\"pages(this.text);\" href=\"#\">"+i+"</a></li>");
            }
        }
        else
            out.print("<li class=\"active\"><a href=\"#\">1</a></li>");
        out.print("</ul>"
                + "</div>");
        //fim paginação
        //out.print("<a href=\"#\" onclick=\"startCountDown();\">crika</a>");
        out.print("</div>"//fecha div container
                + "<div id=\"alert\"></div>"
                + "<div id=\"divdialogs\"></div>"
                + "<script>"
                + "function delConf(id,rec){"
                + " loadInternal('WebServ?delConf='+id+'&rec='+rec,'divdialogs');"
                + "}"
                +"time="+request.getSession().getMaxInactiveInterval()+";"
                +"user='"+request.getSession().getAttribute("user")+"';"
                +"function startCountDown(){" 
                +"  if(time>0){" 
                +"      $('#cron').html('Olá '+user+'</br>'+countDown(time));" 
                +"      time-=1;" 
                +"      setTimeout('startCountDown();', 1000);" 
                +"  }else{" 
                +"      $('#cron').html('Sessão Expirada!');" 
                +"  }" 
                +"}"
                + "currentPage=1;"
                + "function pages(numPage){"
                + " if(currentPage!==numPage){"
                + "     loadInternal('WebServ?dateini="+request.getParameter("dateini")+"&"
                +           "dateend="+request.getParameter("dateend")+"&rooms="+request.getParameter("rooms")
                +"&limit="+limitSeq+"&relPagConf='+numPage,'rel');"
                + "     $('.pagination > ul li').removeClass();"
                + "     $('#pag'+numPage).attr('class','active');"                
                + "     currentPage=numPage;"
                + " }"
                + "}" 
                +"$(function(){"
                + "startCountDown();"
                + "});"
                + "</script>"
                + "</body>"
                + "</html>");
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
