package lk.ijse.gdse71.serenitytherapycenter.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import lk.ijse.gdse71.serenitytherapycenter.config.FactoryConfiguration;
import lk.ijse.gdse71.serenitytherapycenter.entity.User;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.mindrot.jbcrypt.BCrypt;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * --------------------------------------------
 * Author: Zeenathul Ilma
 * GitHub: https://github.com/Seenathul-Ilma
 * Website: https://zeenathulilma.vercel.app/
 * --------------------------------------------
 * Created: 4/23/2025 3:40 PM
 * Project: SerenityTherapyCenter
 * --------------------------------------------
 **/

public class ForgotUsernameController {

    static String generatedOTPCode;

    @FXML
    private TextField txtUsername;

    private final FactoryConfiguration factoryConfiguration = FactoryConfiguration.getInstance();

    public String inputUserName;
    public String role;
    public String otp;

    @FXML
    void sendUsernameOnAction(ActionEvent event) {
        inputUserName = txtUsername.getText();

        Session session = factoryConfiguration.getSession();

        try {
            session.beginTransaction();

            // Fetch full user object to get the hashed password and role
            Query<User> query = session.createQuery(
                    "FROM User u WHERE u.username = :username", User.class);
            query.setParameter("username", inputUserName);
            User user = query.uniqueResult();

            if (user != null) {
                role = user.getRole();
                //String email = user.getEmail();

                final String FROM = "seenathulilma121243@gmail.com"; // Enter Sender's Email Address
                String recipient = user.getEmail();   // Enter Recipient's Email Address
                String subject = "OTP has been received!";
                generatedOTPCode = OTPGenerator.generatePassword(12);
                String body = "Your OTP is :- "+ generatedOTPCode;
                System.out.println(generatedOTPCode);
                otp = generatedOTPCode;
                sendEmailWithGmail(FROM, recipient, subject, body);
            } else {
                new Alert(Alert.AlertType.ERROR, "Invalid username..").show();
                return;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            session.getTransaction().commit();
        }
    }

    private void sendEmailWithGmail(String from, String recipient, String subject, String body) {
        String PASSWORD = "mtrm qcsm gery orqu"; // Enter ur app password here

        Properties props = new Properties();

        props.put("mail.smtp.auth", "true");

        props.put("mail.smtp.starttls.enable", "true");

        props.put("mail.smtp.host", "smtp.gmail.com");

        props.put("mail.smtp.port", "587");

        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        javax.mail.Session session = javax.mail.Session.getInstance(props, new Authenticator() {
            // Replace with your email and app password
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(from));

            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));

            message.setSubject(subject);

            message.setText(body);

            Transport.send(message);

            new Alert(Alert.AlertType.INFORMATION, "Verification code sent successfully!").show();
        } catch (MessagingException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to send verification code..").show();
        }
    }

}
