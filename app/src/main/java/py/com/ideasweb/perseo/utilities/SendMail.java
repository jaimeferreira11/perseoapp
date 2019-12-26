package py.com.ideasweb.perseo.utilities;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import py.com.ideasweb.perseo.restApi.ConstantesRestApi;


/**
 * Created by jaime on 23/11/16.
 */

public class SendMail extends AsyncTask<Void, Void, Void> {

    //Declaring Variables
    private Context context;
    private Session session;
    private View view;

    //Information to send email
    private String email;
    private String subject;
    private String message;
    private String cc_email;
    private ArrayList<File> adjuntos;
    private String cc_ayuda;

    //Progressdialog to show while sending email
   // private ProgressDialog progressDialog;

    //Class Constructor


    public SendMail(String subject, String message, String email) {
        this.email = email;
        this.subject = subject;
        this.message = message;
    }

    public SendMail(String subject, String message, String email, String cc_email){
        //Initializing variables
        //    this.context = context;
        this.email = email;
        this.subject = subject;
        this.message = message;
        this.cc_email = cc_email;
        //  this.view = view;
    }
    public SendMail(String subject, String message, String email, String cc_email, String cc_ayuda){
        //Initializing variables
        //    this.context = context;
        this.email = email;
        this.subject = subject;
        this.message = message;
        this.cc_email =  cc_email;
        this.cc_ayuda = cc_ayuda;
        //  this.view = view;
    }

    public SendMail(String subject, String message, String email, ArrayList<File> adjuntos, String cc_ayuda) {
        this.email = email;
        this.subject = subject;
        this.message = message;
        this.adjuntos = adjuntos;
        this.cc_ayuda = cc_ayuda;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        //  progressDialog = ProgressDialog.show(context,"Enviando mensaje","Aguarde...",false,false);

    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //Dismissing the progress dialog
        // progressDialog.dismiss();
        // Snackbar.make(view, "Enviado! En breve nos comunicaremos con Ud.", Snackbar.LENGTH_LONG).show();

    }

    @Override
    protected Void doInBackground(Void... params) {
        //Creating properties
        Properties props = new Properties();

        //Configuring properties for gmail
        //If you are not using gmail you may need to change the values
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", "mail.drclick.com.py");
        //props.put("mail.smtp.username", "smtp.gmail.com");
       // props.put("mail.smtp.password", "smtp.gmail.com");
        //props.put("mail.smtp.socketFactory.port", "465");
        //props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "587");
        //props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.trust", "mail.drclick.com.py");


        //Creating a new session
        session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    //Authenticating the password
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(ConstantesRestApi.ADMIN_EMAIL, ConstantesRestApi.ADMIN_EMAIL_PASSWORD);
                    }
                });




            if(adjuntos == null) {

                try {
                    //Creating MimeMessage object
                    MimeMessage mm = new MimeMessage(session);

                    //Setting sender address
                    mm.setFrom(new InternetAddress(ConstantesRestApi.ADMIN_EMAIL));

                    //Adding receiver
                    mm.addRecipient(Message.RecipientType.TO, new InternetAddress(email));

                    if (cc_email != null)
                        mm.addRecipient(Message.RecipientType.CC, new InternetAddress(cc_email));

                    // copiar al admin
                    mm.addRecipient(Message.RecipientType.CC , new InternetAddress(ConstantesRestApi.ADMIN_EMAIL));

                    //Adding subject
                    mm.setSubject(subject);


                    //Adding message
                    //mm.setText(message);

                    // Create a multipar message
                    Multipart m = new MimeMultipart();
                    // Create the message part
                    BodyPart mbp = new MimeBodyPart();
                    //Set key values
                    Map<String, String> input = new HashMap<String, String>();
                    input.put("Titulo", subject);
                    input.put("Contenido", message);
                    input.put("CorreoInfo", ConstantesRestApi.ADMIN_EMAIL);
                    //HTML mail content
                    String htmlText = readEmailFromHtml(input);
                    mbp.setContent(htmlText, "text/html; charset=UTF-8");
                    // Set text message part
                    m.addBodyPart(mbp);
                    // Send the complete message parts
                    mm.setContent(m);
                    //Sending email
                    Transport.send(mm);

                    System.out.println("Enviando mensaje " + email);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {

                //enviar mensaje con adjuntos (files)
                System.out.println("Enviando mensaje con adjunto.... ");
                try {
                    //Creating MimeMessage object
                    MimeMessage mm = new MimeMessage(session);
                    //Setting sender address
                    mm.setFrom(new InternetAddress(ConstantesRestApi.ADMIN_EMAIL));
                    //Adding receiver
                    mm.addRecipient(Message.RecipientType.TO, new InternetAddress(email));

                    if (cc_email != null)
                        mm.addRecipient(Message.RecipientType.CC, new InternetAddress(cc_email));

                    // copiar al admin
                    mm.addRecipient(Message.RecipientType.CC , new InternetAddress(ConstantesRestApi.ADMIN_EMAIL));
                    //Adding subject
                    mm.setSubject(subject);

                    // Create a multipar message
                    Multipart m = new MimeMultipart();
                    // Create the message part
                    BodyPart mbp = new MimeBodyPart();
                    //mbp.setText(message);

                    //Set key values
                    Map<String, String> input = new HashMap<String, String>();
                    input.put("Titulo", subject);
                    input.put("Contenido", message);
                    input.put("CorreoInfo", ConstantesRestApi.ADMIN_EMAIL);
                    //HTML mail content
                    String htmlText = readEmailFromHtml(input);
                    mbp.setContent(htmlText, "text/html; charset=UTF-8");

                    // Set text message part
                    m.addBodyPart(mbp);
                    // Part two is attachment
                    for (File file: adjuntos ) {
                        mbp = new MimeBodyPart();
                        String filename = file.getAbsolutePath();
                        DataSource source = new FileDataSource(filename);
                        mbp.setDataHandler(new DataHandler(source));
                        mbp.setFileName(filename);
                        m.addBodyPart(mbp);
                    }
                    // Send the complete message parts
                    mm.setContent(m);
                    //Sending email
                    Transport.send(mm);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }



        return null;
    }

    protected String readEmailFromHtml(Map<String, String> input){
        String msg = Utilities.getEmailTemplate();
        try{
            Set<Map.Entry<String, String>> entries = input.entrySet();
            for(Map.Entry<String, String> entry : entries) {
                msg = msg.replace(entry.getKey().trim(), entry.getValue().trim());
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return msg;
    }
}