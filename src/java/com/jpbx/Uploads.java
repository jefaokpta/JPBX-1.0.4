package com.jpbx;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
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
public class Uploads extends HttpServlet {

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
        String saveFile = "";
        String contentType = request.getContentType();
        if ((contentType != null) && contentType.indexOf("multipart/form-data")>=0){
            DataInputStream in = new DataInputStream(request.getInputStream());
            int formDataLength = request.getContentLength();
            byte dataBytes[]= new byte[formDataLength];
            int byteRead = 0;
            int totalBytesRead = 0;
            while ( totalBytesRead < formDataLength){
                byteRead = in.read(dataBytes , totalBytesRead, formDataLength);
                totalBytesRead += byteRead;
            }
            String file = new String(dataBytes);
            saveFile = file.substring(file.indexOf("filename=\"") + 10);
            saveFile = saveFile.substring(0, saveFile.indexOf("\n"));
            saveFile = saveFile.substring(saveFile.lastIndexOf("\\") + 1,saveFile.indexOf("\"") );
            int lastIndex = contentType.lastIndexOf("=");
            String boundary ="\n------"+ contentType.substring(lastIndex + 1, contentType.length());
            int pos,convert=0;
            pos = file.indexOf("filename=\"");
            pos = file.indexOf("\n" , pos)+ 1;
            pos = file.indexOf("\n" , pos)+ 1;
            pos = file.indexOf("\n" , pos)+ 1;

            int startPos = ((file.substring(0,pos)).getBytes()).length;
            int endPos=(dataBytes.length-startPos)-boundary.getBytes().length;
            String folder = "/tmp/";
            FileOutputStream fileOut = new FileOutputStream(folder + saveFile.replace(" ", "_") );
            fileOut.write(dataBytes, startPos , endPos);
            fileOut.flush();
            fileOut.close(); 
                        
            Asterisk ast=new Asterisk();
            JpbxDB db=new JpbxDB();
            saveFile=saveFile.replace(" ", "_");
            try {
                ast.AstConnect();
                List<String> converter=ast.getInfos("file convert /tmp/"+saveFile+" /tmp/"+saveFile.replace(".wav", ".sln")+"");
                ast.AstDisconnect();
                for(int i=0;i<converter.size();i++){
                    if(converter.get(i).contains("Converted")){
                        convert++;
                        break;
                    }
                }
            } catch (AuthenticationFailedException ex) {
                request.getSession().setAttribute("error", ex.getMessage());
            } catch (TimeoutException ex) {
                request.getSession().setAttribute("error",ex.getMessage());
            }
            if(convert>0){
                request.getSession().setAttribute("file", saveFile);
                response.sendRedirect("createMoh.jsp");
                return;
                //Runtime.getRuntime().exec("mv /tmp/"+saveFile.replace(" ", "_")+" /etc/asterisk/jpbx/moh");      
                //db.addMoh(saveFile);
            }
            else
                request.getSession().setAttribute("error", "Erro! Audio em formato inadequado.</br>"
                        + "Formato certo: Canal: Monoaural, Freq: 8 Kilohertz, Bitrate: 16 Bits ");
            response.sendRedirect("moh.jsp");
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
