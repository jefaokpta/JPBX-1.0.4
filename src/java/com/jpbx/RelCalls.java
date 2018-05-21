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
public class RelCalls extends HttpServlet {

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
        out.print("<html>"
                + "<head>");
        Controller head=new Controller();       
        if(request.getSession().getAttribute("user")==null){
            response.sendRedirect("logout.jsp?error=Desconectado por inatividade.");
            return;
        }
        JpbxDB db=new JpbxDB();
        //CSV
        if(request.getParameter("method")!=null&&request.getParameter("method").equals("csv")){
            String csv;
            if(request.getSession().getAttribute("lvl").equals("Administrador"))
                csv=db.buildCSV(request.getParameter("dateini"),request.getParameter("dateend"),request.getParameter("order"),
                        request.getParameter("src"),request.getParameter("dstfinal"),request.getParameter("ccost"),
                        request.getParameter("status"),request.getParameter("srcseek"),request.getParameter("dstseek"));
            else
                csv=db.buildCSV(request.getParameter("dateini"),request.getParameter("dateend"),request.getParameter("order"),
                        request.getParameter("src"),request.getParameter("dstfinal"),request.getParameter("ccost"),
                        request.getParameter("status"),request.getParameter("srcseek"),request.getParameter("dstseek"),
                        db.getUserCompany(request.getSession().getAttribute("user").toString()));
            if(csv.equals("ok")){
                out.print("<script>"
                        + "alert('Falha na Compressão. Verifique se o programa rar está instalado.');"
                        + "window.history.back(-1);"
                        + "</script>");
                return;
            }
            else if(csv.substring(0, 9).equals("Relatorio")){               
                response.sendRedirect("/jpbx/asterisk/reports/Relatorio.rar");    
                return;
            }
            else{
                csv=csv.replaceAll("'", "");
                out.print("<script>"
                        + "alert('Falhou</br>"+csv+"');"
                        + "window.history.back(-1);"
                        + "</script>");
                return;
            }
        }
        //CSVFIM
        int pagSeq=0,numCalls;
        int data[]=db.getLimitRelCalls();
        int limitSeq=data[0];
        out.print(head.header("JPBX->Report->Show")+""
                + "<link rel=\"stylesheet\" href=\"css/jquery-ui-1.10.3/themes/base/jquery-ui.css\"/>\n" +
                "<script src=\"css/jquery-ui-1.10.3/ui/jquery-ui.js\"></script>");
            
        out.print("</head>"
                + "<body>");
        String[] numCount;
        if(request.getSession().getAttribute("lvl").equals("Administrador"))
            numCount=db.countRelCalls(request.getParameter("dateini"),request.getParameter("dateend"),request.getParameter("order"),
                        request.getParameter("src"),request.getParameter("dstfinal"),request.getParameter("ccost"),
                        request.getParameter("status"),request.getParameter("srcseek"),request.getParameter("dstseek"));
        else
            numCount=db.countRelCalls(request.getParameter("dateini"),request.getParameter("dateend"),request.getParameter("order"),
                        request.getParameter("src"),request.getParameter("dstfinal"),request.getParameter("ccost"),
                        request.getParameter("status"),request.getParameter("srcseek"),request.getParameter("dstseek"),
                        db.getUserCompany(request.getSession().getAttribute("user").toString()));
        out.print(head.navigator(request.getSession().getAttribute("lvl").toString()));//conta os registros
        numCalls=Integer.parseInt(numCount[5]);
        if(numCalls==0)
            out.print("<script>"
                        + "alert('Nenhum Dado Encontrado.');"
                        + "window.history.back(-1);</script>");
        if(request.getSession().getAttribute("lvl").equals("Administrador"))
            out.print("<div id=\"container\" class=\"container table-bordered\">"+numCount[0]
                +"<div id=\"rel\">"+ db.relCalls(request.getParameter("dateini"),request.getParameter("dateend"),request.getParameter("order"),
                        request.getParameter("src"),request.getParameter("dstfinal"),request.getParameter("ccost"),
                        request.getParameter("status"),pagSeq,limitSeq,request.getParameter("srcseek"),request.getParameter("dstseek")) //limit
                );// escreve registros em tabela e fecha
        else
            out.print("<div id=\"container\" class=\"container table-bordered\">"+numCount[0]
                +"<div id=\"rel\">"+ db.relCalls(request.getParameter("dateini"),request.getParameter("dateend"),request.getParameter("order"),
                        request.getParameter("src"),request.getParameter("dstfinal"),request.getParameter("ccost"),
                        request.getParameter("status"),pagSeq,limitSeq,request.getParameter("srcseek"),request.getParameter("dstseek"),
                        db.getUserCompany(request.getSession().getAttribute("user").toString())) //limit
                );// escreve registros em tabela e fecha
        out.print("</div>" //fecha div rel
                + "<div class=\"clearfix\"></div>");
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
        out.print("</div>");//fecha div container
        out.print("<div id=\"divdialogs\"></div>");
        out.print("<script>"
                + "$(function(){"
                + " $('td').css('font-size', 14);"
                + " startCountDown();"
                + "});"
                + "function delCall(uniqueid){"
                +   "loadInternal('WebServ?delCall='+uniqueid,'alert');"
                + "}"
                + "function dialogs(uniqueid){"
                +   "loadInternal('WebServ?relDialog='+uniqueid,'divdialogs');"
                + "}"
                + "currentPage=1;"
                + "function pages(numPage){"
                + " if(currentPage!==numPage){"
                + "     loadInternal('WebServ?dateini="+request.getParameter("dateini")+"&"
                +           "dateend="+request.getParameter("dateend")+"&order="+request.getParameter("order")+"&"
                +           "src="+request.getParameter("src")+"&dstfinal="+request.getParameter("dstfinal")+"&"
                +           "ccost="+request.getParameter("ccost")+"&status="+request.getParameter("status")+"&"
                +           "limit="+limitSeq+"&srcseek="+request.getParameter("srcseek")+"&"
                +           "dstseek="+request.getParameter("dstseek")+"&relPag='+numPage,'rel');"
                + "     $('.pagination > ul li').removeClass();"
                + "     $('#pag'+numPage).attr('class','active');"                
                + "     currentPage=numPage;"
                + " }"
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
                + "</script>"
                + "<div id=\"alert\"></div>"
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
