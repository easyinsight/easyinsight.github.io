package com.easyinsight.email;

import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.export.AttachmentInfo;
import com.easyinsight.logging.LogClass;
import org.jetbrains.annotations.Nullable;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;
import java.util.List;
import java.util.Properties;

/**
 * User: jamesboe
 * Date: Apr 22, 2010
 * Time: 10:10:43 AM
 */
public class SendGridEmail {

    private static final String SMTP_HOST_NAME = "smtp.sendgrid.net";
    private static final String SMTP_AUTH_USER = "jboe@easy-insight.com";
    private static final String SMTP_AUTH_PWD  = "CrestoneisRedR0ck";

    public static final String PERSONAL_EMAIL_WELCOME = "Hi {0},<br><br>" +
                "Welcome to Easy Insight! I&#39;m available as your point of contact for any issues, questions, or other comments you may have in using the service. If you want to chat for a few minutes on the phone or drop me an email around what you're looking to do in Easy Insight, I&#39;d love to see how we can help you out.<br><br>" +
                "Here are a couple of introductory screencasts you can start with:<br><br>" +                
                "<a href=\"http://www.youtube.com/watch?v=GrUqJpWl8c4\">Building Your Own Reports</a><br><br>" +
                "Thanks for your interest in Easy Insight. We look forward to your business!<br><br>" +
                "James Boe<br>" +
                "CEO, Easy Insight<br>" +
                "http://www.easy-insight.com/<br>" +
                "(720)-220-8085 ";
    
    public static final String BASIC_OR_PRO_EMAIL_WELCOME = "Hi {0},<br>" +
            "<br>" +
            "Welcome to Easy Insight! I'm available as your point of contact for any issues, questions, or other comments you may have in using the service. If you want to chat for a few minutes on the phone or drop me an email around what you're looking to do, I'd love to see how we can help you out. We take great pride in our ability to quickly meet customer feature requests and get you past any stumbling points.<br>" +
            "<br>" +
            "Introductory screencasts are available at http://www.youtube.com/user/easyinsight. In particular, you might find the following screencasts helpful as starting points:<br>" +
            " <br>" +
            "Connecting Easy Insight to Basecamp<br>" +
            "Building Your Own Reports<br>" +
            "Uploading Flat Files to Easy Insight<br>" +
            "Creating KPIs in Easy Insight<br>" +
            "<br>" +
            "Documentation is available at http://www.easy-insight.com/documentation/toc.html.<br>" +
            "<br>" +
            "Thanks for your interest in Easy Insight. Feel free to reach out to me!<br>" +
            "<br>" +
            "James Boe<br>" +
            "CEO, Easy Insight<br>" +
            "http://www.easy-insight.com/<br>" +
            "(720)-220-8085" +
            "Twitter: @easyinsight";
    
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
            sendEmail(userID, emailAddress, category, header, body, conn);
        } catch (Exception e) {
            LogClass.error(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void sendEmail(long userID, String emailAddress, String category, String header, String body, EIConnection conn) throws SQLException, MessagingException, UnsupportedEncodingException {

        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO EMAIL_AUDIT (USER_ID, EMAIL_CATEGORY) VALUES (?, ?)",
                Statement.RETURN_GENERATED_KEYS);
        insertStmt.setLong(1, userID);
        insertStmt.setString(2, category);
        insertStmt.execute();
        long id = Database.instance().getAutoGenKey(insertStmt);
        sendEmail(emailAddress, header, body, id);

    }

    private void decorateWithAudit(MimeMessage message, String emailType, long auditID) throws MessagingException {
        if (emailType != null) {
            message.addHeaderLine("X-SMTPAPI: {\"unique_args\": {\"emailType\": \""+emailType+"\", \"auditID\":\""+auditID+"\"}}");
        }
    }

    public void sendNoAttachmentEmail(String emailAddress, String subject, String htmlBody, boolean htmlEmail, String fromAddress, String fromName)
            throws MessagingException, UnsupportedEncodingException {
        sendNoAttachmentEmail(emailAddress, subject, htmlBody, htmlEmail, fromAddress, fromName, null, 0);
    }

    public void sendNoAttachmentEmail(String emailAddress, String subject, String htmlBody, boolean htmlEmail, String fromAddress, String fromName, @Nullable String emailType, long auditID)
            throws MessagingException, UnsupportedEncodingException {


        // Then add to your message:
        //messageContent.addBodyPart(bodyPart);
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", SMTP_HOST_NAME);
        props.put("mail.smtp.auth", "true");
        props.put("vendor", "SendGrid");
        props.put("mail.smtp.timeout", 30000);
        props.put("mail.smtp.connectiontimeout", 30000);

        Authenticator auth = new SMTPAuthenticator();
        Session mailSession = Session.getInstance(props, auth);
        // uncomment for debugging infos to stdout
        // mailSession.setDebug(true);
        Transport transport = mailSession.getTransport();

        MimeMessage message = new MimeMessage(mailSession);



        Multipart multipart = new MimeMultipart();

        message.setSubject(subject, "UTF-8");

        if (htmlEmail) {
            htmlBody = "<html><head>\n" +
                    "    <style type=\"text/css\" media=\"screen\">\n" +
                    "        tr {\n" +
                    "\n" +
                    "        }\n" +
                    "\n" +
                    "        td {\n" +
                    "            border-style: solid;\n" +
                    "            border-width: 1px;\n" +
                    "        }\n" +
                    "    </style>\n" +
                    "</head><body>" + htmlBody + "</body></html>";
            BodyPart part2 = new MimeBodyPart();
            part2.setContent(htmlBody, "text/html;charset=UTF-8");
            multipart.addBodyPart(part2);
        } else {
            BodyPart part1 = new MimeBodyPart();
            part1.setContent(htmlBody,"text/plain;charset=utf-8");
            multipart.addBodyPart(part1);
        }

        message.setContent(multipart);
        decorateWithAudit(message, emailType, auditID);

        if (fromAddress == null) {
            fromAddress = "reports@easy-insight.com";
        }
        if (fromName == null) {
            fromName = "Easy Insight Reports";
        }
        message.setFrom(new InternetAddress(fromAddress, fromName));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailAddress));

        transport.connect();
        transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
        transport.close();
    }

    public void sendAttachmentEmail(String emailAddress, String subject, String htmlBody, byte[] file, String attachmentName, boolean htmlEmail, String fromAddress, String fromName,
                                    String mimeEncoding)
            throws MessagingException, UnsupportedEncodingException {
        sendAttachmentEmail(emailAddress, subject, htmlBody, file, attachmentName, htmlEmail, fromAddress, fromName, mimeEncoding, null, 0);

    }

    public void sendAttachmentEmail(String emailAddress, String subject, String htmlBody, byte[] file, String attachmentName, boolean htmlEmail, String fromAddress, String fromName,
                                    String mimeEncoding, @Nullable String emailType, long auditID)
            throws MessagingException, UnsupportedEncodingException {


        // Then add to your message:
        //messageContent.addBodyPart(bodyPart);
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", SMTP_HOST_NAME);
        props.put("mail.smtp.auth", "true");
        props.put("vendor", "SendGrid");

        Authenticator auth = new SMTPAuthenticator();
        Session mailSession = Session.getInstance(props, auth);
        // uncomment for debugging infos to stdout
        // mailSession.setDebug(true);
        Transport transport = mailSession.getTransport();

        MimeMessage message = new MimeMessage(mailSession);



        Multipart multipart = new MimeMultipart();

        message.setSubject(subject, "UTF-8");

        if (htmlEmail) {
            BodyPart part2 = new MimeBodyPart();
            part2.setContent(htmlBody, "text/html;charset=UTF-8");
            multipart.addBodyPart(part2);
        } else {
            BodyPart part1 = new MimeBodyPart();
            part1.setContent(htmlBody,"text/plain;charset=utf-8");
            multipart.addBodyPart(part1);
        }

        decorateWithAudit(message, emailType, auditID);

        BodyPart bodyPart = new MimeBodyPart();
        DataSource source = new ByteArrayDataSource(file, mimeEncoding);
        bodyPart.setDataHandler(new DataHandler(source));
        bodyPart.setFileName(attachmentName);
        bodyPart.setDisposition(Part.ATTACHMENT);

        multipart.addBodyPart(bodyPart);

        message.setContent(multipart);

        if (fromAddress == null) {
            fromAddress = "reports@easy-insight.com";
        }
        if (fromName == null) {
            fromName = "Easy Insight Reports";
        }
        message.setFrom(new InternetAddress(fromAddress, fromName));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailAddress));

        transport.connect();
        transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
        transport.close();
    }

    public void sendMultipleAttachmentsEmail(String emailAddress, String subject, String htmlBody, boolean htmlEmail, String fromAddress, String fromName,
                                             List<AttachmentInfo> attachments)
            throws MessagingException, UnsupportedEncodingException {
        sendMultipleAttachmentsEmail(emailAddress, subject, htmlBody, htmlEmail, fromAddress, fromName, attachments, null, 0);

    }

    public void sendMultipleAttachmentsEmail(String emailAddress, String subject, String htmlBody, boolean htmlEmail, String fromAddress, String fromName,
                                             List<AttachmentInfo> attachments, @Nullable String emailType, long auditID)
            throws MessagingException, UnsupportedEncodingException {


        // Then add to your message:
        //messageContent.addBodyPart(bodyPart);
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", SMTP_HOST_NAME);
        props.put("mail.smtp.auth", "true");
        props.put("vendor", "SendGrid");

        Authenticator auth = new SMTPAuthenticator();
        Session mailSession = Session.getInstance(props, auth);
        // uncomment for debugging infos to stdout
        // mailSession.setDebug(true);
        Transport transport = mailSession.getTransport();

        MimeMessage message = new MimeMessage(mailSession);

        boolean cidNeeded = false;
        for (AttachmentInfo attachmentInfo : attachments) {
            if (attachmentInfo.getInlineCID() != null) {
                cidNeeded = true;
                break;
            }
        }

        Multipart multipart;
        if (cidNeeded) {
            multipart = new MimeMultipart("related");
        } else {
            multipart = new MimeMultipart();
        }

        message.setSubject(subject, "UTF-8");

        if (htmlEmail) {
            BodyPart part2 = new MimeBodyPart();
            part2.setContent(htmlBody, "text/html;charset=UTF-8");
            multipart.addBodyPart(part2);
        } else {
            BodyPart part1 = new MimeBodyPart();
            part1.setContent(htmlBody,"text/plain;charset=utf-8");
            multipart.addBodyPart(part1);
        }

        for (AttachmentInfo attachmentInfo : attachments) {
            if (attachmentInfo.getInlineCID() != null) {
                MimeBodyPart imagePart = new MimeBodyPart();

                DataSource iSource = new ByteArrayDataSource(attachmentInfo.getBody(), "image/png");
                imagePart.setDataHandler(new DataHandler(iSource));
                imagePart.setFileName(attachmentInfo.getName() + ".png");
                imagePart.setContentID("<"+attachmentInfo.getInlineCID()+">");
                imagePart.setDisposition(MimeBodyPart.INLINE);
                multipart.addBodyPart(imagePart);
            }
            BodyPart bodyPart = new MimeBodyPart();
            DataSource source = new ByteArrayDataSource(attachmentInfo.getBody(), attachmentInfo.getEncoding());
            bodyPart.setDataHandler(new DataHandler(source));
            bodyPart.setFileName(attachmentInfo.getName());
            bodyPart.setDisposition(Part.ATTACHMENT);

            multipart.addBodyPart(bodyPart);
        }

        decorateWithAudit(message, emailType, auditID);

        message.setContent(multipart);

        if (fromAddress == null) {
            fromAddress = "reports@easy-insight.com";
        }
        if (fromName == null) {
            fromName = "Easy Insight Reports";
        }
        message.setFrom(new InternetAddress(fromAddress, fromName));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailAddress));

        transport.connect();
        transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
        transport.close();
    }

    private void sendEmail(String emailAddress, String subject, String htmlBody, long auditID) throws MessagingException, UnsupportedEncodingException {
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", SMTP_HOST_NAME);
        props.put("mail.smtp.auth", "true");
        props.put("vendor", "SendGrid");

        Authenticator auth = new SMTPAuthenticator();
        Session mailSession = Session.getInstance(props, auth);
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

        message.setFrom(new InternetAddress("sales@easy-insight.com", "Easy Insight"));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailAddress));

        transport.connect();
        transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
        transport.close();
    }

    public void sendEmail(String emailAddress, String subject, String body, String fromAddress, boolean htmlEmail, String fromName) throws MessagingException, UnsupportedEncodingException {
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", SMTP_HOST_NAME);
        props.put("mail.smtp.auth", "true");
        props.put("vendor", "SendGrid");

        Authenticator auth = new SMTPAuthenticator();
        Session mailSession = Session.getInstance(props, auth);
        // uncomment for debugging infos to stdout
        // mailSession.setDebug(true);
        Transport transport = mailSession.getTransport();

        MimeMessage message = new MimeMessage(mailSession);

        Multipart multipart = new MimeMultipart();

        message.setSubject(subject);

        if (htmlEmail) {
            BodyPart part2 = new MimeBodyPart();
            part2.setContent(body, "text/html;charset=UTF-8");
            multipart.addBodyPart(part2);
        } else {
            BodyPart part1 = new MimeBodyPart();
            part1.setContent(body,"text/plain;charset=utf-8");
            multipart.addBodyPart(part1);
        }

        message.setContent(multipart);

        message.setFrom(new InternetAddress(fromAddress, fromName));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailAddress));

        transport.connect();
        transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
        transport.close();
    }

    private static class SMTPAuthenticator extends javax.mail.Authenticator {
        public PasswordAuthentication getPasswordAuthentication() {
           String username = SMTP_AUTH_USER;
           String password = SMTP_AUTH_PWD;
           return new PasswordAuthentication(username, password);
        }
    }
}
