package org.example.Database.CursachN1;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Random;

public class methodsWithConnectionToInternet {

    public Boolean isSentMessage(String messageText, String mail, String messageTheme) {

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.mail.ru");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        Session session = Session.getDefaultInstance(props,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("sankevich2003@mail.ru", "ajjWRRCKXHnqGETMWBDf");
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("sankevich2003@mail.ru"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mail));
            message.setSubject(messageTheme);
            message.setText(messageText);

            Transport.send(message);

            return true;

        } catch (MessagingException e) {
           return false;
        }
    }

    public String getVerificationCode() {
        Random rnd = new Random(System.currentTimeMillis());
        int randCode = 1000 + rnd.nextInt(9999 - 1000 + 1);

        return String.valueOf(randCode);
    }
}