package email.notification;


import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class SendEmailNotification {

    private static final String HOST = "192.168.67.231";
    private static final String PORT = "25";
    private static final String FROM_USER_NAME = "user@gmail.com";  // GMail user name (just the part before "@gmail.com")
    private static final String[] RECIPIENTS = {"fguerra@gmail.com"};

    public static void main(String[] args) {
        String serverType = args[1];
        String subject = "Alerta de monitoreo redmine: servidor no responde!!!";
        String body = "Buen(a) dia/noche,\n\nUno de los servidores de redmine: \"" + serverType
                + "\" no ha respondido al monitoreo. Favor conectarse y revisar.\n\n";
        sendNotification(subject, body);
    }

    private static void sendNotification(String subject, String body) {
        Properties props = System.getProperties();
        props.put("mail.smtp.starttls.enable", "false");
        props.put("mail.smtp.host", HOST);
        props.put("mail.smtp.user", FROM_USER_NAME);
        props.put("mail.smtp.port", PORT);
        props.put("mail.smtp.auth", "false");

        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(SendEmailNotification.FROM_USER_NAME));
            InternetAddress[] toAddress = new InternetAddress[SendEmailNotification.RECIPIENTS.length];

            // Generate array of addresses
            for (int i = 0; i < SendEmailNotification.RECIPIENTS.length; i++) {
                toAddress[i] = new InternetAddress(SendEmailNotification.RECIPIENTS[i]);
            }
            for (InternetAddress address : toAddress) {
                message.addRecipient(Message.RecipientType.TO, address);
            }
            message.setHeader("Content-Type", "text/plain; charset=UTF-8");
            message.setSubject(subject, "UTF-8");
            message.setText(body, "UTF-8");
            Transport transport = session.getTransport("smtp");
            transport.connect(HOST, FROM_USER_NAME, "");
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (MessagingException ae) {
            ae.printStackTrace();
        }
    }
}