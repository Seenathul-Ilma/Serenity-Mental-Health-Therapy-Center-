package lk.ijse.gdse71.serenitytherapycenter.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import lk.ijse.gdse71.serenitytherapycenter.bo.BOFactory;
import lk.ijse.gdse71.serenitytherapycenter.bo.custom.TherapySessionBO;
import lk.ijse.gdse71.serenitytherapycenter.dto.TherapySessionDTO;
import lk.ijse.gdse71.serenitytherapycenter.dto.tm.TherapySessionTM;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * --------------------------------------------
 * Author: Zeenathul Ilma
 * GitHub: https://github.com/Seenathul-Ilma
 * Website: https://ilma.live
 * --------------------------------------------
 * Created: 4/22/2025 8:38 AM
 * Project: MobileZone
 * --------------------------------------------
 **/

public class TherapySessionController implements Initializable {

    @FXML
    private AnchorPane sessionContent;

    @FXML
    private Button btnAssignTherapist;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnReset;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnSearch;

    @FXML
    private Button btnUpdate;

    @FXML
    private TableColumn<TherapySessionTM, String> colSessionStatus;

    @FXML
    private TableColumn<TherapySessionTM, String> colPatId;

    @FXML
    private TableColumn<TherapySessionTM, String> colProId;

    @FXML
    private TableColumn<TherapySessionTM, String> colRegId;

    @FXML
    private TableColumn<TherapySessionTM, Date> colSessionDate;

    @FXML
    private TableColumn<TherapySessionTM, String> colSessionId;

    @FXML
    private TableColumn<TherapySessionTM, String> colTherapistId;

    @FXML
    private VBox insertVBox;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblPatId;

    @FXML
    private Label lblProId;

    @FXML
    private Label lblRegistrationId;

    @FXML
    private Label lblSessionId;

    @FXML
    private Label lblTherapistId;

    @FXML
    private RadioButton rBtnComplete;

    @FXML
    private RadioButton rBtnIncomplete;

    @FXML
    private ToggleGroup sessionStatus;

    @FXML
    private TableView<TherapySessionTM> tblSession;

    @FXML
    private DatePicker txtDateOfSession;

    @FXML
    private TextField txtSearch;

    TherapySessionBO therapySessionBO = (TherapySessionBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.THERAPY_SESSION);


    public void initData(String registrationId, String programId, String patientId, boolean isNavigateSaveBtn) {
        lblRegistrationId.setText(registrationId);
        lblProId.setText(programId);
        lblPatId.setText(patientId);
        btnSave.setDisable(isNavigateSaveBtn);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colSessionId.setCellValueFactory(new PropertyValueFactory<>("sessionId"));
        colRegId.setCellValueFactory(new PropertyValueFactory<>("registrationId"));
        colProId.setCellValueFactory(new PropertyValueFactory<>("programId"));
        colTherapistId.setCellValueFactory(new PropertyValueFactory<>("therapistId"));
        colPatId.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        colSessionDate.setCellValueFactory(new PropertyValueFactory<>("sessionDate"));
        colSessionStatus.setCellValueFactory(new PropertyValueFactory<>("sessionStatus"));

        try {
            refreshPage();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load session id").show();
        }

    }

    private void refreshPage() throws SQLException, ClassNotFoundException {
        loadNextSessionId();
        loadTableData();

        btnSave.setDisable(false);

        lblDate.setText(LocalDate.now().toString());

        btnSave.setDisable(true);
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
        btnReset.setDisable(false);
        txtSearch.setText("");
        rBtnComplete.setSelected(false);
        rBtnIncomplete.setSelected(false);

    }

    private void loadTableData() throws SQLException, ClassNotFoundException {
        List<TherapySessionDTO> therapySessionDTOS = therapySessionBO.getAllSessions();

        ObservableList<TherapySessionTM> sessionTMS = FXCollections.observableArrayList();

        for(TherapySessionDTO therapySessionDTO : therapySessionDTOS){
            TherapySessionTM therapySessionTM = new TherapySessionTM(
                    therapySessionDTO.getSessionId(),
                    therapySessionDTO.getRegistrationId(),
                    therapySessionDTO.getProgramId(),
                    therapySessionDTO.getTherapistId(),
                    therapySessionDTO.getPatientId(),
                    therapySessionDTO.getSessionDate(),
                    therapySessionDTO.getSessionStatus()
            );
            sessionTMS.add(therapySessionTM);
        }
        tblSession.setItems(sessionTMS);

    }

    private void loadNextSessionId() throws SQLException, ClassNotFoundException {
        Optional<String> nextSessionIdOptional = therapySessionBO.getNextSessionId();
        String nextSessionId = nextSessionIdOptional.orElse("TSE001");
        lblSessionId.setText(nextSessionId);
    }

    @FXML
    void btnAssignTherapistOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ScheduleTherapist.fxml"));
            Parent load = loader.load();

            ScheduleTherapistController scheduleTherapistController = loader.getController();

            Stage stage = new Stage();
            stage.setScene(new Scene(load));
            stage.setTitle("Booking");
            stage.setResizable(false);

            stage.initModality(Modality.APPLICATION_MODAL);
            Window underWindow = btnDelete.getScene().getWindow();
            stage.initOwner(underWindow);

            stage.showAndWait();

            // Now that the modal is closed, retrieve the therapist ID and session date
            String bookedTherapistId = scheduleTherapistController.getTherapistId();
            LocalDate bookedSessionDate = scheduleTherapistController.getSessionDate();

            // Set the data to your UI components or handle the booking logic
            lblTherapistId.setText(bookedTherapistId);
            txtDateOfSession.setValue(bookedSessionDate);

        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Fail to load UI..!").show();
            e.printStackTrace();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load invoice data..!").show();
            e.printStackTrace();
        }
    }

    public void onTherapistBooked(String therapistId, Date sessionDate) {
        // Do something with the therapistId and sessionDate
        System.out.println("Therapist booked: " + therapistId + " on " + sessionDate);
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String sessionId = lblSessionId.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> optionalButtonType = alert.showAndWait();

        if (optionalButtonType.isPresent() && optionalButtonType.get() == ButtonType.YES) {

            boolean isDeleted = therapySessionBO.deleteSession(sessionId);

            if (isDeleted) {
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION, "Therapy Session record deleted successfully...!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to delete session record...!").show();
            }
        }
    }

    @FXML
    void btnResetOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        refreshPage();
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String sessionId = lblSessionId.getText();
        String registrationId = lblRegistrationId.getText();
        String programId = lblProId.getText();
        String therapistId = lblTherapistId.getText();
        String patientId = lblPatId.getText();
        String status = rBtnComplete.isSelected() ? "Completed" : "Incomplete";

        Date dateOfSession = Date.valueOf(txtDateOfSession.getValue());

        boolean therapySessionStatus = false;
        if (rBtnComplete.isSelected()) {
            therapySessionStatus = true;
        }

        if (!rBtnComplete.isSelected() && !rBtnIncomplete.isSelected()) {
            new Alert(Alert.AlertType.ERROR, "Please select session status before saving..!").show();
            return;
        } else {

            TherapySessionDTO therapySessionDTO = new TherapySessionDTO(
                    sessionId,
                    registrationId,
                    programId,
                    therapistId,
                    patientId,
                    dateOfSession,
                    status
            );

            System.out.println("programId: "+programId);
            System.out.println("therapistId: "+therapistId);
            System.out.println("patientId: "+patientId);
            System.out.println("dateOfSession: "+dateOfSession);
            System.out.println("status: "+status);
            System.out.println("registrationId: "+registrationId);
            System.out.println("sessionId: "+sessionId);

            boolean isSaved = therapySessionBO.saveSession(therapySessionDTO);

            if (isSaved) {
                Alert saveAlert = new Alert(Alert.AlertType.INFORMATION, "Therapy Session schedule saved successfully...!");
                saveAlert.showAndWait(); // Wait until the user clicks "OK"

                if(therapySessionStatus){
                    // After the user clicks OK, show another message asking them to collect the payment
                    Alert paymentAlert = new Alert(Alert.AlertType.INFORMATION, "Please collect the payment from the patient.");
                    paymentAlert.showAndWait();

                    navigateToPaymentPage();
                }

                refreshPage();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to save session...!").show();
            }
        }
    }

    private void navigateToPaymentPage() {
        try {
            sessionContent.getChildren().clear();
            URL fxmlUrl = getClass().getResource("/view/PaymentTest.fxml");

            if (fxmlUrl == null) {
                throw new RuntimeException("FXML file not found!");
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/PaymentTest.fxml"));
            AnchorPane load = loader.load(); // Loads the FXML file and binds the controller

            System.out.println("ses: "+lblSessionId.getText());
            System.out.println("reg: "+lblRegistrationId.getText());

            PaymentController paymentController = loader.getController();
            paymentController.initData(
                    lblPatId.getText(), lblProId.getText(),
                    lblRegistrationId.getText(), lblSessionId.getText() , false// "Upfront Payment"
            );
            load.prefWidthProperty().bind(sessionContent.widthProperty());
            load.prefHeightProperty().bind(sessionContent.heightProperty());

            sessionContent.getChildren().add(load);

        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load page!").show();
        } catch (RuntimeException re) {
            re.printStackTrace();
            new Alert(Alert.AlertType.ERROR, re.getMessage()).show();
        }
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {

    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String sessionId = lblSessionId.getText();
        String registrationId = lblRegistrationId.getText();
        String programId = lblProId.getText();
        String therapistId = lblTherapistId.getText();
        String patientId = lblPatId.getText();
        String status = rBtnComplete.isSelected() ? "Completed" : "Incomplete";

        Date dateOfSession = Date.valueOf(txtDateOfSession.getValue());

        boolean therapySessionStatus = false;
        if (rBtnComplete.isSelected()) {
            therapySessionStatus = true;
        }

        if (!rBtnComplete.isSelected() && !rBtnIncomplete.isSelected()) {
            new Alert(Alert.AlertType.ERROR, "Please select session status before saving..!").show();
            return;
        } else {

            // 👇 Get current status from the database or table (you can also fetch this from `therapySessionBO.getSessionById(sessionId)`)
            TherapySessionTM selectedSession = tblSession.getSelectionModel().getSelectedItem();
            if (selectedSession != null && "Completed".equalsIgnoreCase(selectedSession.getSessionStatus()) && "Incomplete".equalsIgnoreCase(status)) {
                new Alert(Alert.AlertType.WARNING, "This session has already been marked as Completed. It cannot be changed to Incomplete.").show();
                rBtnComplete.setSelected(true);
                return;
            }

            TherapySessionDTO therapySessionDTO = new TherapySessionDTO(
                    sessionId,
                    registrationId,
                    programId,
                    therapistId,
                    patientId,
                    dateOfSession,
                    status
            );

            System.out.println("programId: "+programId);
            System.out.println("therapistId: "+therapistId);
            System.out.println("patientId: "+patientId);
            System.out.println("dateOfSession: "+dateOfSession);
            System.out.println("status: "+status);
            System.out.println("registrationId: "+registrationId);
            System.out.println("sessionId: "+sessionId);

            boolean isUpdated = therapySessionBO.updateSession(therapySessionDTO);

            if (isUpdated) {
                Alert saveAlert = new Alert(Alert.AlertType.INFORMATION, "Therapy Session schedule updated successfully...!");
                saveAlert.showAndWait(); // Wait until the user clicks "OK"

                if(therapySessionStatus){
                    // After the user clicks OK, show another message asking them to collect the payment
                    Alert paymentAlert = new Alert(Alert.AlertType.INFORMATION, "Please collect the payment from the patient.");
                    paymentAlert.showAndWait();

                    navigateToPaymentPage();
                }

                refreshPage();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to update session...!").show();
            }
        }
    }

    @FXML
    void getSessionStatusOnAction(ActionEvent event) {

    }

    @FXML
    void onClickTable(MouseEvent event) {
        TherapySessionTM therapySessionTM = tblSession.getSelectionModel().getSelectedItem();
        if(therapySessionTM != null){
            lblSessionId.setText(therapySessionTM.getSessionId());
            lblRegistrationId.setText(therapySessionTM.getRegistrationId());
            lblProId.setText(therapySessionTM.getProgramId());
            lblPatId.setText(therapySessionTM.getPatientId());
            lblTherapistId.setText(therapySessionTM.getTherapistId());
            txtDateOfSession.setValue(therapySessionTM.getSessionDate().toLocalDate());

            if ("Incomplete".equalsIgnoreCase(therapySessionTM.getSessionStatus())) {
                rBtnIncomplete.setSelected(true);
            } else if ("Completed".equalsIgnoreCase(therapySessionTM.getSessionStatus())) {
                rBtnComplete.setSelected(true);
            } else {
                // Handle cases where gender is not specified or is different
                sessionStatus.selectToggle(null); // Deselect both radio buttons
            }

            //txtDateOfSession.setDisable(true);
            btnSave.setDisable(true);
            btnDelete.setDisable(false);
            btnUpdate.setDisable(false);
            // btnCollectRegFee.setDisable(false);
        }
    }

    @FXML
    void sessionDateOnAction(ActionEvent event) {

    }

}
