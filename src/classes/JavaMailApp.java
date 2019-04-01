package classes;

import java.util.Properties;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public abstract class JavaMailApp {

    private static final long serialVersionUID = 123L;

    static int op;

    public static int enviaEmailRecuperacao(String nome, String destinatario, String senha) {
        Properties props = new Properties();
        /**
         * Parâmetros de conexão com servidor Gmail
         */
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("suporte.ifound@gmail.com", "ifound123");
            }
        });
        /**
         * Ativa Debug para sessão
         */
        session.setDebug(true);
        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("suporte.ifound@gmail.com")); //Remetente

            Address[] toUser = InternetAddress //Destinatário(s)
                    .parse(destinatario);
            message.setRecipients(Message.RecipientType.TO, toUser);

            message.setSubject("é o java do iFound - Recuperação de senha");//Assunto
            message.setText("A César o que é de César. \nSua senha é: '" + senha + "");

            /*message.setSubject("é o java do iFound - Alguém disse que encontrou seu objeto.");
                message.setText("Alguém respondeu em sua postagem '"+titulo+"' que encontrou seu objeto. Abra seu aplicativo para mais informações."); */
            /**
             * Método para enviar a mensagem criada
             */
            Transport.send(message);
            System.out.println("Email enviado");

            op = 1;
            return op;
        } catch (MessagingException e) {
            op = 0;
            return op;
            //throw new RuntimeException(e);

        }
    }

    public static int enviaEmailNotificacao(String destinatario, String titulo) {
        System.out.println ("Entrou na funcao de email");
        Properties props = new Properties();
        /**
         * Parâmetros de conexão com servidor Gmail
         */
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("suporte.ifound@gmail.com", "ifound123");
            }
        });
        /**
         * Ativa Debug para sessão
         */
        session.setDebug(true);
        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("suporte.ifound@gmail.com")); //Remetente

            Address[] toUser = InternetAddress //Destinatário(s)
                    .parse(destinatario);
            message.setRecipients(Message.RecipientType.TO, toUser);

            message.setSubject("é o java do iFound - Parece que alguém encontrou seu objeto.");
            message.setText("Alguém respondeu em sua postagem '" + titulo + "' que encontrou seu objeto. Abra seu aplicativo para mais informações.");

            /**
             * Método para enviar a mensagem criada
             */
            Transport.send(message);
            System.out.println("Email enviado");

            op = 1;
            return op;
        } catch (MessagingException e) {
            op = 0;
            return op;
            //throw new RuntimeException(e);

        }
    }

}
