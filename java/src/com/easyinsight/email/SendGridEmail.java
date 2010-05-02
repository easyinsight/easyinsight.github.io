package com.easyinsight.email;

import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.logging.LogClass;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.UnsupportedEncodingException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.Properties;

/**
 * User: jamesboe
 * Date: Apr 22, 2010
 * Time: 10:10:43 AM
 */
public class SendGridEmail {

    private static final String SMTP_HOST_NAME = "smtp.sendgrid.net";
    private static final String SMTP_AUTH_USER = "jboe@easy-insight.com";
    private static final String SMTP_AUTH_PWD  = "e@symone$";

    public static final String PERSONAL_EMAIL_WELCOME = "Hi {0},<br><br>" +
                "Welcome to Easy Insight! I&#39;m available as your point of contact for any issues, questions, or other comments you may have in using the service. If you want to chat for a few minutes on the phone or drop me an email around what you're looking to do in Easy Insight, I&#39;d love to see how we can help you out.<br><br>" +
                "Here are a couple of introductory screencasts you can start with:<br><br>" +                
                "<a href=\"http://www.youtube.com/watch?v=GrUqJpWl8c4\">Building Your Own Reports</a><br><br>" +
                "Thanks for your interest in Easy Insight. We look forward to your business!<br><br>" +
                "James Boe<br>" +
                "CEO, Easy Insight<br>" +
                "http://www.easy-insight.com/<br>" +
                "(720)-220-8085 ";
    
    public static final String BASIC_OR_PRO_EMAIL_WELCOME = "Hi {0},<br><br>" +
                "Welcome to Easy Insight! I&#39;m available as your point of contact for any issues, questions, or other comments you may have in using the service. If you want to chat for a few minutes on the phone or drop me an email around what you're looking to do in Easy Insight, I&#39;d love to see how we can help you out.<br><br>" +
                "Here are a couple of introductory screencasts you can start with:<br><br><br>" +
                "<a href=\"http://www.youtube.com/watch?v=6Xd5sfnEwBQ\">Connecting Easy Insight to Basecamp</a><br>"+
                "<a href=\"http://www.youtube.com/watch?v=GrUqJpWl8c4\">Building Your Own Reports</a><br><br>" +
                "Thanks for your interest in Easy Insight. We are looking forward to your business!<br><br><br>" +
                "James Boe<br>" +
                "CEO, Easy Insight<br>" +
                "http://www.easy-insight.com/<br>" +
                "(720)-220-8085";
    
    public static final String BASIC_OR_PRO_EMAIL_WELCOME_TEXT = "Hi {0}," +
                "Welcome to Easy Insight! I&#39;m available as your point of contact for any issues, questions, or other comments you may have in using the service. If you want to chat for a few minutes on the phone or drop me an email around what you're looking to do in Easy Insight, I&#39;d love to see how we can help you out." +
                "Here are a couple of introductory screencasts you can start with:" +
                "<a href=\"http://www.youtube.com/watch?v=6Xd5sfnEwBQ\">Connecting Easy Insight to Basecamp</a>"+
                "<a href=\"http://www.youtube.com/watch?v=GrUqJpWl8c4\">Building Your Own Reports</a>" +
                "Thanks for your interest in Easy Insight. We look forward to your business!" +
                "James Boe" +
                "CEO, Easy Insight" +
                "http://www.easy-insight.com/" +
                "(720)-220-8085 ";

    public static final String TWO_WEEKS_EMAIL = "Hi {0},<br><br>" +
                "I hope you've been successful with your use of Easy Insight. " +
                "Here are a couple of introductory screencasts you can start with:<br><br>" +
                "<a href=\"http://www.youtube.com/watch?v=6Xd5sfnEwBQ\">Connecting Easy Insight to Basecamp</a><br>"+
                "<a href=\"http://www.youtube.com/watch?v=GrUqJpWl8c4\">Building Your Own Reports</a><br><br>" +
                "Thanks for your interest in Easy Insight. We look forward to your business!<br><br>" +
                "James Boe<br>" +
                "CEO, Easy Insight<br>" +
                "http://www.easy-insight.com/<br>" +
                "(720)-220-8085 ";
    
    public static final String TRIAL_ALMOST_UP_EMAIL = "Hi {0},<br><br>" +
            "Your 30 day trial is about to expire.";

    public static final String SPECIAL_OFFER = "Hi {0},<br><br>" +
            "";

    public void sendEmail(long userID, String emailAddress, String category, String header, String body) {
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO EMAIL_AUDIT (USER_ID, EMAIL_CATEGORY) VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            insertStmt.setLong(1, userID);
            insertStmt.setString(2, category);
            insertStmt.execute();
            long id = Database.instance().getAutoGenKey(insertStmt);
            sendEmail(emailAddress, header, body, id);
        } catch (Exception e) {
            LogClass.error(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    private void sendEmail(String emailAddress, String subject, String htmlBody, long auditID) throws MessagingException, UnsupportedEncodingException {
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", SMTP_HOST_NAME);
        props.put("mail.smtp.auth", "true");

        Authenticator auth = new SMTPAuthenticator();
        Session mailSession = Session.getDefaultInstance(props, auth);
        // uncomment for debugging infos to stdout
        // mailSession.setDebug(true);
        Transport transport = mailSession.getTransport();

        MimeMessage message = new MimeMessage(mailSession);

        Multipart multipart = new MimeMultipart();

        message.setSubject(subject);

        message.addHeaderLine("X-SMTPAPI: {\"category\": \"" + auditID + "\"}");

        /*BodyPart part1 = new MimeBodyPart();
        part1.setText(body);*/

        BodyPart part2 = new MimeBodyPart();
        part2.setContent(htmlBody, "text/html");
        
        //multipart.addBodyPart(part1);
        multipart.addBodyPart(part2);

        message.setContent(multipart);

        message.setFrom(new InternetAddress("jboe@easy-insight.com", "James Boe"));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailAddress));

        transport.connect();
        transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
        transport.close();
    }

    public static void main(String[] args) throws MessagingException, UnsupportedEncodingException {
        new SendGridEmail().sendEmail("jboe99@gmail.com", "Welcome to Easy Insight!",
                MessageFormat.format(SendGridEmail.BASIC_OR_PRO_EMAIL_WELCOME, "James"), 1);
    }

    private class SMTPAuthenticator extends javax.mail.Authenticator {
        public PasswordAuthentication getPasswordAuthentication() {
           String username = SMTP_AUTH_USER;
           String password = SMTP_AUTH_PWD;
           return new PasswordAuthentication(username, password);
        }
    }
}
