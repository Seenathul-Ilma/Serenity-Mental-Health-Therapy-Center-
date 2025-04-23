package lk.ijse.gdse71.serenitytherapycenter.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import lk.ijse.gdse71.serenitytherapycenter.config.FactoryConfiguration;
import lk.ijse.gdse71.serenitytherapycenter.entity.User;
import lk.ijse.gdse71.serenitytherapycenter.util.UserSession;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * --------------------------------------------
 * Author: Zeenathul Ilma
 * GitHub: https://github.com/Seenathul-Ilma
 * Website: https://zeenathulilma.vercel.app/
 * --------------------------------------------
 * Created: 4/23/2025 11:07 AM
 * Project: SerenityTherapyCenter
 * --------------------------------------------
 **/

public class LogInPageController {

    @FXML
    private AnchorPane imageAnchorPane;

    @FXML
    private AnchorPane logInPage;

    @FXML
    private Button btnLogIn;

    @FXML
    private Button btnLogInOTP;

    @FXML
    private VBox pageOne2;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtUserName;

    @FXML
    private PasswordField txtOTP;
    public String userRole;

    private final FactoryConfiguration factoryConfiguration = FactoryConfiguration.getInstance();

    //UserSession userSession = new UserSession();

    @FXML
    void forgetPasswordOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/forgotPassword.fxml"));
            Parent load = loader.load();

            ForgotUsernameController forgotUsernameController = loader.getController();

            Stage stage = new Stage();
            stage.setScene(new Scene(load));
            stage.setTitle("Forgot Password");
            stage.setResizable(false);

            stage.initModality(Modality.APPLICATION_MODAL);
            Window underWindow = btnLogIn.getScene().getWindow();
            stage.initOwner(underWindow);

            stage.showAndWait();

            String ReceivedOTPCode = forgotUsernameController.otp;


            btnLogIn.setDisable(true);
            txtPassword.setDisable(true);

            txtOTP.setDisable(false);
            btnLogInOTP.setDisable(false);

            txtUserName.setText(forgotUsernameController.inputUserName);
            //txtOTP.setText(ReceivedOTPCode);
            userRole = forgotUsernameController.role;

        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Fail to load UI..!").show();
            e.printStackTrace();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load forgot password ui..!").show();
            e.printStackTrace();
        }

    }

    @FXML
    void logInOnAction(ActionEvent event) {
        String inputUserName = txtUserName.getText();
        String inputPassword = txtPassword.getText();

        Session session = factoryConfiguration.getSession();
        try {
            session.beginTransaction();

            // Fetch full user object to get the hashed password and role
            Query<User> query = session.createQuery(
                    "FROM User u WHERE u.username = :username", User.class);
            query.setParameter("username", inputUserName);
            User user = query.uniqueResult();

            if (user != null && BCrypt.checkpw(inputPassword, user.getPassword())) {
                String role = user.getRole();

                UserSession.setRole(role);

                showAlert("Login Successful", "Welcome, " + inputUserName + "!", Alert.AlertType.INFORMATION);

                String fxmlPath;
                if ("Admin".equalsIgnoreCase(role)) {
                    fxmlPath = "/view/MainLayout.fxml";
                } else if ("Receptionist".equalsIgnoreCase(role)) {
                    fxmlPath = "/view/MainLayout.fxml";
                } else {
                    showAlert("Access Denied", "Unauthorized role: " + role, Alert.AlertType.ERROR);
                    return;
                }

                // Load the new layout
                logInPage.getChildren().clear();
                AnchorPane load = FXMLLoader.load(getClass().getResource(fxmlPath));
                load.prefWidthProperty().bind(logInPage.widthProperty());
                load.prefHeightProperty().bind(logInPage.heightProperty());
                logInPage.getChildren().add(load);

            } else {
                showAlert("Login Failed", "Invalid username or password.", Alert.AlertType.ERROR);
            }

            session.getTransaction().commit();

        } catch (Exception e) {
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
            showAlert("Error", "An error occurred while logging in.", Alert.AlertType.ERROR);
        } finally {
            session.close();
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void logInByOTPOnAction(ActionEvent actionEvent) throws IOException {
        String fxmlPath;
        if ("Admin".equalsIgnoreCase(userRole)) {
            fxmlPath = "/view/MainLayout.fxml";
        } else if ("Receptionist".equalsIgnoreCase(userRole)) {
            fxmlPath = "/view/MainLayout.fxml";
        } else {
            showAlert("Access Denied", "Unauthorized role! ", Alert.AlertType.ERROR);
            return;
        }

        UserSession.setRole(userRole);

        // Load the new layout
        logInPage.getChildren().clear();
        AnchorPane load = FXMLLoader.load(getClass().getResource(fxmlPath));
        load.prefWidthProperty().bind(logInPage.widthProperty());
        load.prefHeightProperty().bind(logInPage.heightProperty());
        logInPage.getChildren().add(load);
    }
}
