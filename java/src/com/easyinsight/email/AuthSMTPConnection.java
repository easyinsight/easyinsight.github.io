package com.easyinsight.email;

/**
 * User: James Boe
 * Date: Aug 17, 2008
 * Time: 9:00:16 PM
 */

import java.util.Properties;
import java.io.UnsupportedEncodingException;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class AuthSMTPConnection {
    private static final String emailFromAddress = "accounts@easy-insight.com";
    private static final String USERNAME = "ac41045";
    private static final String PASSWORD = "kcrs2gtun";
    private static final String HOSTNAME = "mail.authsmtp.com";


    public void sendSSLMessage(String recipient, String subject,
                               String message, String from) throws MessagingException, UnsupportedEncodingException {
        boolean debug = true;

        Properties props = new Properties();
        props.put("mail.smtp.host", HOSTNAME);
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props,new javax.mail.Authenticator() {

                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(USERNAME, PASSWORD);
                    }
                });

        session.setDebug(debug);

        Message msg = new MimeMessage(session);
        InternetAddress addressFrom = new InternetAddress(emailFromAddress, from);
        msg.setFrom(addressFrom);

        InternetAddress addressTo = new InternetAddress(recipient);
        msg.setRecipient(Message.RecipientType.TO, addressTo);

        // Setting the Subject and Content Type
        msg.setSubject(subject);
        msg.setContent(message, "text/plain");
        Transport.send(msg);
    }

    public static void main(String[] args) throws MessagingException, UnsupportedEncodingException {
        new AuthSMTPConnection().sendSSLMessage("jboe99@gmail.com", "Testing...", "Blah", "Easy Insight");
    }
}