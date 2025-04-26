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
import lk.ijse.gdse71.serenitytherapycenter.bo.custom.PaymentBO;
import lk.ijse.gdse71.serenitytherapycenter.bo.custom.ProgramBO;
import lk.ijse.gdse71.serenitytherapycenter.bo.custom.RegistrationBO;
import lk.ijse.gdse71.serenitytherapycenter.bo.custom.TherapySessionBO;
import lk.ijse.gdse71.serenitytherapycenter.dto.EnrollmentDTO;
import lk.ijse.gdse71.serenitytherapycenter.dto.PaymentDTO;
import lk.ijse.gdse71.serenitytherapycenter.dto.ProgramDTO;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private Label lblPaymentStatus;

    @FXML
    private Label lblSessionPayment;

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

    @FXML
    private ComboBox<String> cmbRegId;


    TherapySessionBO therapySessionBO = (TherapySessionBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.THERAPY_SESSION);
    RegistrationBO registrationBO = (RegistrationBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.ENROLLMENT);
    ProgramBO programBO = (ProgramBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.THERAPY_PROGRAM);
    PaymentBO paymentBO = (PaymentBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.PAYMENT);

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
        loadRegistrationIds();

        btnSave.setDisable(false);

        lblDate.setText(LocalDate.now().toString());

        lblSessionPayment.setText("");
        lblPaymentStatus.setText("");
        lblPatId.setText("");
        lblProId.setText("");
        lblTherapistId.setText("");

        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
        btnReset.setDisable(false);
        txtSearch.setText("");
        rBtnComplete.setSelected(false);
        rBtnIncomplete.setSelected(false);

    }

    private void loadRegistrationIds() throws SQLException, ClassNotFoundException {
        List<String> regIds = registrationBO.getPendingRegistrationIds();

        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(regIds);
        cmbRegId.setItems(observableList);

        if (!observableList.isEmpty()) {
            cmbRegId.getSelectionModel().selectFirst();
        }
        System.out.println("ComboBox loaded with: " + cmbRegId.getItems());
    }

    @FXML
    void cmbRegOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String selectedRegId = cmbRegId.getSelectionModel().getSelectedItem();

        try {
            if (selectedRegId != null) {
                EnrollmentDTO enrollmentDTO = registrationBO.findByRegistrationId(selectedRegId);

                if (enrollmentDTO != null) {
                    lblPatId.setText(enrollmentDTO.getPatientId());
                    lblProId.setText(enrollmentDTO.getProgramId());
                    Double upfront = enrollmentDTO.getRegistrationFee();

                    ProgramDTO programDTO = programBO.findByProgramId(enrollmentDTO.getProgramId());

                    if (programDTO != null) {
                        Double programFee = programDTO.getCost();
                        String duration = programDTO.getDuration();

                        double sessionFee = paymentBO.calculateSessionFee(upfront, programFee, duration);
                        lblSessionPayment.setText(String.format("%.2f", sessionFee));
                        lblPaymentStatus.setText("Pending");
                    } else {
                        new Alert(Alert.AlertType.WARNING, "Program details not found!").show();
                    }
                } else {
                    new Alert(Alert.AlertType.WARNING, "Enrollment details not found!").show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something went wrong while loading registration details.").show();
        }
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

            String bookedTherapistId = scheduleTherapistController.getTherapistId();
            LocalDate bookedSessionDate = scheduleTherapistController.getSessionDate();

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

    @FXML
    void btnDeleteOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String sessionId = lblSessionId.getText();

        Optional<Object> existPaymentDTO = paymentBO.findBySessionId(sessionId);
        if (existPaymentDTO.isPresent()) {
            new Alert(Alert.AlertType.ERROR, "Cannot delete completed session..!").show();
            return;
        }

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
    void btnSaveOnAction(ActionEvent event) {
        String sessionId = lblSessionId.getText();
        String registrationId = cmbRegId.getSelectionModel().getSelectedItem();
        String programId = lblProId.getText();
        String therapistId = lblTherapistId.getText();
        String patientId = lblPatId.getText();
        String status = rBtnComplete.isSelected() ? "Completed" : "Ongoing";
        Double sessionPayment = Double.valueOf(lblSessionPayment.getText());

        if (!rBtnComplete.isSelected() && !rBtnIncomplete.isSelected()) {
            new Alert(Alert.AlertType.ERROR, "Please select session status before saving..!").show();
            return;
        }

        Date dateOfSession = Date.valueOf(txtDateOfSession.getValue());

        TherapySessionDTO sessionDTO = new TherapySessionDTO(
                sessionId, registrationId, programId, therapistId, patientId, dateOfSession, status
        );

        try {
            if (status.equals("Completed")) {
                double sessionFee = Double.parseDouble(lblSessionPayment.getText());

                Optional<String> nextPaymentIdOptional = paymentBO.getNextPaymentId();
                String nextPaymentId = nextPaymentIdOptional.orElse("PAY001");

                PaymentDTO paymentDTO = new PaymentDTO(
                        nextPaymentId,
                        dateOfSession,
                        sessionPayment,
                        "Session Payment",
                        patientId,
                        programId,
                        registrationId,
                        sessionId
                );

                boolean success = therapySessionBO.saveSessionWithPayment(sessionDTO, paymentDTO);

                if (success) {

                    double totalPaid = paymentBO.getTotalPaidAmountForEnrollment(registrationId);

                    ProgramDTO programDTO = programBO.findByProgramId(programId);

                    if (programDTO == null) {
                        new Alert(Alert.AlertType.ERROR, "Program not found.").show();
                        return;
                    }
                    //lblProFee.setText(programDTO.getCost().toString());
                    double programCost = programDTO.getCost();

                    System.out.println("Total Paid: " + totalPaid + ", Program Cost: " + programCost);

                    if (totalPaid >= programCost) {
                        boolean isStatusUpdated = registrationBO.updateEnrollmentStatus(registrationId, "Completed");
                        if (isStatusUpdated) {
                            System.out.println("Enrollment marked as completed.");
                        }
                    }

                    lblPaymentStatus.setText("Completed");
                    new Alert(Alert.AlertType.INFORMATION, "Session and Payment saved successfully!").show();
                    refreshPage();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to save Session and Payment transaction!").show();
                }

            } else {
                // Just save session
                boolean isSaved = therapySessionBO.saveSession(sessionDTO);
                if (isSaved) {
                    lblPaymentStatus.setText("Pending");
                    new Alert(Alert.AlertType.INFORMATION, "Session saved successfully!").show();
                    refreshPage();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to save Session!").show();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error while saving data!").show();
        }
    }



    @FXML
    void btnSearchOnAction(ActionEvent event) {

    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String sessionId = lblSessionId.getText();
        String registrationId = cmbRegId.getSelectionModel().getSelectedItem();
        String programId = lblProId.getText();
        String therapistId = lblTherapistId.getText();
        String patientId = lblPatId.getText();
        String status = rBtnComplete.isSelected() ? "Completed" : "Ongoing";
        Double sessionPayment = Double.valueOf(lblSessionPayment.getText());


        if (registrationId == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a Registration ID!").show();
            return;
        }

        if (!rBtnComplete.isSelected() && !rBtnIncomplete.isSelected()) {
            new Alert(Alert.AlertType.ERROR, "Please select session status before saving..!").show();
            return;
        }

        Date dateOfSession = Date.valueOf(txtDateOfSession.getValue());

        TherapySessionDTO sessionDTO = new TherapySessionDTO(
                sessionId, registrationId, programId, therapistId, patientId, dateOfSession, status
        );
        System.out.println("Reg 1:"+registrationId);

        try {


            if (status.equals("Completed")) {

                Optional<Object> existPaymentDTO = paymentBO.findBySessionId(sessionId);
                if (existPaymentDTO.isPresent()) {
                    new Alert(Alert.AlertType.ERROR, "Cannot update completed session..!").show();
                    return;
                }

                // double sessionFee = Double.parseDouble(lblSessionPayment.getText());
                lblPaymentStatus.setText("Completed");

                Optional<String> nextPaymentIdOptional = paymentBO.getNextPaymentId();
                String nextPaymentId = nextPaymentIdOptional.orElse("PAY001");

                PaymentDTO paymentDTO = new PaymentDTO(
                        nextPaymentId,
                        dateOfSession,
                        sessionPayment,
                        "Session Payment",
                        patientId,
                        programId,
                        registrationId,
                        sessionId
                );

                boolean success = therapySessionBO.saveSessionWithPayment(sessionDTO, paymentDTO);

                if (success) {
                    double totalPaid = paymentBO.getTotalPaidAmountForEnrollment(registrationId);

                    ProgramDTO programDTO = programBO.findByProgramId(programId);

                    if (programDTO == null) {
                        new Alert(Alert.AlertType.ERROR, "Program not found.").show();
                        return;
                    }
                    //lblProFee.setText(programDTO.getCost().toString());
                    double programCost = programDTO.getCost();

                    System.out.println("Total Paid: " + totalPaid + ", Program Cost: " + programCost);

                    if (totalPaid >= programCost) {
                        boolean isStatusUpdated = registrationBO.updateEnrollmentStatus(registrationId, "Completed");
                        if (isStatusUpdated) {
                            System.out.println("Enrollment marked as completed.");
                        }
                    }

                    lblPaymentStatus.setText("Completed");
                    new Alert(Alert.AlertType.INFORMATION, "Session and Payment updated successfully!").show();
                    refreshPage();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to update Session and Payment transaction!").show();
                }

            } else {

                Optional<Object> existPaymentDTO = paymentBO.findBySessionId(sessionId);
                if (existPaymentDTO.isPresent()) {
                    new Alert(Alert.AlertType.ERROR, "Cannot update completed sessions again..!").show();
                    return;
                }
                // Just save session
                lblPaymentStatus.setText("Pending");
                boolean isUpdated = therapySessionBO.updateSession(sessionDTO);
                if (isUpdated) {
                    lblPaymentStatus.setText("Pending");
                    new Alert(Alert.AlertType.INFORMATION, "Session updated successfully!").show();
                    refreshPage();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to update Session!").show();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error while saving data!").show();
        }
    }

    @FXML
    void getSessionStatusOnAction(ActionEvent event) {

    }

    @FXML
    void onClickTable(MouseEvent event) {
        TherapySessionTM therapySessionTM = tblSession.getSelectionModel().getSelectedItem();
        if (therapySessionTM != null) {
            lblSessionId.setText(therapySessionTM.getSessionId());
            cmbRegId.setValue(therapySessionTM.getRegistrationId());
            lblProId.setText(therapySessionTM.getProgramId());
            lblPatId.setText(therapySessionTM.getPatientId());
            lblTherapistId.setText(therapySessionTM.getTherapistId());
            txtDateOfSession.setValue(therapySessionTM.getSessionDate().toLocalDate());

            if ("Ongoing".equalsIgnoreCase(therapySessionTM.getSessionStatus())) {
                lblPaymentStatus.setText("Pending");
                rBtnIncomplete.setSelected(true);
            } else if ("Completed".equalsIgnoreCase(therapySessionTM.getSessionStatus())) {
                lblPaymentStatus.setText("Completed");
                rBtnComplete.setSelected(true);
            } else {
                // Handle cases where gender is not specified or is different
                sessionStatus.selectToggle(null); // Deselect both radio buttons
            }

            try {
                EnrollmentDTO enrollmentDTO = registrationBO.findByRegistrationId(therapySessionTM.getRegistrationId());

                if (enrollmentDTO != null) {
                    ProgramDTO programDTO = programBO.findByProgramId(enrollmentDTO.getProgramId());

                    if (programDTO != null) {
                        Double upfront = enrollmentDTO.getRegistrationFee();
                        Double programFee = programDTO.getCost();
                        String duration = programDTO.getDuration();

                        // Calculate session fee
                        double sessionFee = paymentBO.calculateSessionFee(upfront, programFee, duration);

                        // Update labels
                        lblSessionPayment.setText(String.format("%.2f", sessionFee));
                        lblPaymentStatus.setText("Pending");
                    } else {
                        new Alert(Alert.AlertType.WARNING, "Program details not found!").show();
                    }
                } else {
                    new Alert(Alert.AlertType.WARNING, "Enrollment details not found!").show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Something went wrong while loading payment details.").show();
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
