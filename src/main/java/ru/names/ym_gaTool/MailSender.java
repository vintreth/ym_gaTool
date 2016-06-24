package ru.names.ym_gaTool;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * @author kbogdanov 24.06.16
 */
class MailSender {

    private String from;
    private String[] recipients;
    private String subject;
    private String text;

    public MailSender(String from, String[] recipients, String subject, String text) {
        this.from = from;
        this.recipients = recipients;
        this.subject = subject;
        this.text = text;
    }

    public void send() {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("", "");
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));

            StringBuilder builder = new StringBuilder();
            for (String recipient : recipients) {
                builder.append(recipient);
            }
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(builder.toString()));


        } catch (MessagingException e) {

        }


    }
}
