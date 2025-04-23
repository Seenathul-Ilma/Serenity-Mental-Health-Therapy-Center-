package lk.ijse.gdse71.serenitytherapycenter.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import lk.ijse.gdse71.serenitytherapycenter.util.UserSession;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


/**
 * --------------------------------------------
 * Author: Zeenathul Ilma
 * GitHub: https://github.com/Seenathul-Ilma
 * Website: https://ilma.live
 * --------------------------------------------
 * Created: 3/16/2025 9:24 PM
 * Project: MobileZone
 * --------------------------------------------
 **/

public class MainPageController implements Initializable {

    @FXML
    private Button btnLogOut;

    @FXML
    private Button btnPatients;

    @FXML
    private Button btnPayments;

    @FXML
    private Button btnRegistrations;

    @FXML
    private Button btnTherapists;

    @FXML
    private Button btnTherapyPrograms;

    @FXML
    private Button btnTherapySessions;

    @FXML
    private Button btnUsers;

    @FXML
    private AnchorPane firstAnchorPane;

    @FXML
    private AnchorPane secondAnchorPane;

    @FXML
    void navigateToLogInCredentialPage(ActionEvent event) {
        navigateTo("/view/LogInCredentials.fxml");
    }

    @FXML
    void navigateToPatientPage(ActionEvent event) {
        navigateTo("/view/Patient.fxml");
    }

    @FXML
    void navigateToTherapistPage(ActionEvent event) {
        navigateTo("/view/Therapist.fxml");
    }

    @FXML
    void navigateToTherapyProgramPage(ActionEvent event) {
        navigateTo("/view/ProgramView.fxml");
    }

    @FXML
    void navigateToRegistrationPage(ActionEvent event) {
        navigateTo("/view/Registration.fxml");
    }

    @FXML
    void navigateToPaymentPage(ActionEvent event) {
        navigateTo("/view/PaymentTest.fxml");
    }

    @FXML
    void navigateToTherapySessionPage(ActionEvent event) {
        navigateTo("/view/Session.fxml");
    }

    private void navigateTo(String fxmlPath) {
        try {
            secondAnchorPane.getChildren().clear();
            AnchorPane load = FXMLLoader.load(getClass().getResource(fxmlPath));

            load.prefWidthProperty().bind(secondAnchorPane.widthProperty());
            load.prefHeightProperty().bind(secondAnchorPane.heightProperty());

            secondAnchorPane.getChildren().add(load);
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load page!").show();
        }
    }

    @FXML
    public void navigateToLogout(ActionEvent event){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to logout?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> optionalButtonType = alert.showAndWait();

        if (optionalButtonType.isPresent() && optionalButtonType.get() == ButtonType.YES) {
            try {
                firstAnchorPane.getChildren().clear();
                AnchorPane load = FXMLLoader.load(getClass().getResource("/view/LogInPage.fxml"));

                load.prefWidthProperty().bind(firstAnchorPane.widthProperty());
                load.prefHeightProperty().bind(firstAnchorPane.heightProperty());

                firstAnchorPane.getChildren().add(load);
            } catch (IOException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Fail to load page!").show();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String role = UserSession.getRole();

        if ("Receptionist".equalsIgnoreCase(role)) {
            btnUsers.setVisible(false);
            btnTherapists.setVisible(false);
            btnTherapyPrograms.setVisible(false);
        }
    }
}
