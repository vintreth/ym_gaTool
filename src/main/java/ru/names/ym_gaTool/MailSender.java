package ru.names.ym_gaTool;

import org.apache.log4j.Logger;
import ru.names.ym_gaTool.configuration.MailSenderConfig;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Mail sender
 *
 * @author kbogdanov 24.06.16
 */
class MailSender {

    private MailSenderConfig config;

    private Context context;

    private static Logger logger = Logger.getLogger("MailSender");

    public MailSender(MailSenderConfig config, Context context) {
        this.config = config;
        this.context = context;
    }

    /**
     * Sends mail to current addresses
     *
     * @throws MailSenderException
     */
    public void send() throws MailSenderException {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(config.getUserName(), config.getPassword());
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(context.from));

            StringBuilder builder = new StringBuilder();
            for (String recipient : context.recipients) {
                builder.append(",");
                builder.append(recipient);
            }
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(builder.toString()));
            message.setSubject(context.subject);
            message.setText(context.text);

            Transport.send(message);

        } catch (MessagingException e) {
            logger.error(e.getMessage(), e);
            throw new MailSenderException(e.getMessage(), e);
        }
    }

    public static class Context {
        /**
         * Sender mail
         */
        private final String from;
        /**
         * Array of recepients' mail
         */
        private final String[] recipients;
        /**
         * Subject of letter
         */
        private final String subject;
        /**
         * Main text of letter
         */
        private final String text;

        public Context(String from, String[] recipients, String subject, String text) {
            this.from = from;
            this.recipients = recipients;
            this.subject = subject;
            this.text = text;
        }
    }
}
