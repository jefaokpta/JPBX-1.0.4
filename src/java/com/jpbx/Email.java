/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jpbx;

import java.net.MalformedURLException;
import java.net.URL;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

/**
 *
 * @author jefaokpta
 */
public class Email {
    JpbxDB db=new JpbxDB();
    public void enviaEmailVoicemail(String peer,String audio,String caller) throws EmailException, MalformedURLException {  
          
        HtmlEmail email = new HtmlEmail();  
        String data[]=db.showEmail();
        String user[]=db.notifyVoicemail(peer);
        /*adiciona uma imagem ao corpo da mensagem e retorna seu id  
        URL url = new URL("http://www.apache.org/images/asf_logo_wide.gif");  
           
         cria o anexo 1.  
        EmailAttachment anexo1 = new EmailAttachment();  
        anexo1.setPath("/tmp/bashShell.pdf"); //caminho do arquivo (RAIZ_PROJETO/teste/teste.txt)  
        anexo1.setDisposition(EmailAttachment.ATTACHMENT);  
        anexo1.setDescription("Exemplo de arquivo anexo");  
        anexo1.setName("bashShell.pdf");
                */
        //insere audio integrado no email
        URL url = new URL("file:///"+audio);
        String cid = email.embed(url, "Audio");
          
        // configura a mensagem para o formato HTML  
        email.setHtmlMsg("<html>Logo do Apache - <img ></html>");  
  
        // configure uma mensagem alternativa caso o servidor não suporte HTML  
        email.setTextMsg("Seu servidor de e-mail não suporta mensagem HTML");  
          
        email.setHostName(data[2]); // o servidor SMTP para envio do e-mail  
        email.addTo(user[1], user[0]); //destinatário  
        email.setFrom(data[0], "JPBX"); // remetente  
        email.setSubject("Nova Mensagem no seu Voicemail "+user[0]); // assunto do e-mail  
        email.setMsg("<html>\n" +
"    <head>\n" +
"        <meta charset=\"UTF-8\">\n" +
"        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
"    </head>\n" +
"    <body style=\"background-color: grey\">\n" +
"        <div style=\"background-color: whitesmoke;width: 70%;margin: 0 auto;text-align: center\">\n" +
"            <h2>JPBX Informa</h2>\n" +
"            <h3>Voce tem uma nova mensagem de voz</h3>\n" +
"            <h4>Mensagem deixada por: <a href=\"tel:"+caller+"\">"+caller+"</a></h4>\n" +
"            <p style=\"color: lightsteelblue\">Mensagem automática</p>\n" +
"            <p style=\"color: lightsteelblue\">Developed by <a href=\"http://www.linkedin.com/pub/jefferson-alves-reis/61/73/0\">Jefaokpta</a> </p>\n" +
"        </div>\n" +
"    </body>\n" +
"</html>"); //conteudo do e-mail  
        email.setAuthentication(data[0], data[1]);  
        email.setSmtpPort(Integer.parseInt(data[3]));
        if(data[4].equals("1"))
            email.setTLS(true);
        if(data[5].equals("1"))
            email.setSSL(true);  
         
        // adiciona arquivo(s) anexo(s)  
        //email.attach(anexo1);
        // envia email  
        email.send();  
    } 
}
