package lk.ijse.gdse71.serenitytherapycenter.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import lk.ijse.gdse71.serenitytherapycenter.bo.BOFactory;
import lk.ijse.gdse71.serenitytherapycenter.bo.custom.PatientBO;
import lk.ijse.gdse71.serenitytherapycenter.bo.custom.ProgramBO;
import lk.ijse.gdse71.serenitytherapycenter.bo.custom.RegistrationBO;
import lk.ijse.gdse71.serenitytherapycenter.dto.EnrollmentDTO;
import lk.ijse.gdse71.serenitytherapycenter.dto.ProgramDTO;
import lk.ijse.gdse71.serenitytherapycenter.dto.tm.EnrollTM;

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
 * Created: 4/20/2025 10:08 AM
 * Project: MobileZone
 * --------------------------------------------
 **/

public class RegistrationController implements Initializable {

    @FXML
    private Button btnCollectRegFee;

    @FXML
    private Button btnAddSession;

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
    private ComboBox<String> cmbPatientId;

    @FXML
    private ComboBox<String> cmbStatus;

    @FXML
    private ComboBox<String> cmbProId;

    @FXML
    private TableColumn<EnrollTM, String> colPatientId;

    @FXML
    private TableColumn<EnrollTM, String> colProId;

    @FXML
    private TableColumn<EnrollTM, Date> colRegDate;

    @FXML
    private TableColumn<EnrollTM, String> colRegId;

    @FXML
    private TableColumn<EnrollTM, String> colStatus;

    @FXML
    private VBox insertVBox;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblProFee;
    
    @FXML
    private Label lblRegisterId;

    @FXML
    private TableView<EnrollTM> tblRegister;

    @FXML
    private TextField txtSearch;

    @FXML
    private AnchorPane content;

    RegistrationBO registrationBO = (RegistrationBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.ENROLLMENT);
    ProgramBO programBO = (ProgramBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.THERAPY_PROGRAM);
    PatientBO patientBO = (PatientBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.PATIENT);


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colRegId.setCellValueFactory(new PropertyValueFactory<>("registrationId"));
        colRegDate.setCellValueFactory(new PropertyValueFactory<>("enrollDate"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("enrollmentStatus"));
        colPatientId.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        colProId.setCellValueFactory(new PropertyValueFactory<>("programId"));

        try {
            refreshPage();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load patient id").show();
        }
    }

    private void refreshPage() throws SQLException, ClassNotFoundException {
        loadNextRegistrationId();
        loadTableData();
        loadPatientIds();
        loadProgramIds();
        loadEnrollStatus();

        btnSave.setDisable(false);

        lblDate.setText(LocalDate.now().toString());

        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
        btnAddSession.setDisable(true);
        btnReset.setDisable(false);
        txtSearch.setText("");
        lblProFee.setText("");
        cmbStatus.getSelectionModel().clearSelection();
        cmbPatientId.getSelectionModel().clearSelection();
        cmbProId.getSelectionModel().clearSelection();
    }

    private void loadEnrollStatus() {
        ObservableList<String> status = FXCollections.observableArrayList(
                "Active", "Completed", "Cancelled"
        );
        System.out.println("load enroll status");
        cmbStatus.setItems(status);
    }

    private void loadProgramIds() throws SQLException, ClassNotFoundException {
        List<String> programIds = programBO.getAllProgramIds();

        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(programIds);
        cmbProId.setItems(observableList);
    }

    private void loadPatientIds() throws SQLException, ClassNotFoundException {
        List<String> patientIds = patientBO.getAllPatientIds();

        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(patientIds);
        cmbPatientId.setItems(observableList);
    }

    private void loadTableData() throws SQLException, ClassNotFoundException {
        List<EnrollmentDTO> enrollmentDTOS = registrationBO.getAllRegistrations();

        ObservableList<EnrollTM> enrollTMS = FXCollections.observableArrayList();

        for(EnrollmentDTO enrollmentDTO : enrollmentDTOS){
            EnrollTM enrollTM = new EnrollTM(
                    enrollmentDTO.getRegistrationId(),
                    enrollmentDTO.getPatientId(),
                    enrollmentDTO.getProgramId(),
                    enrollmentDTO.getEnrollmentDate(),
                    enrollmentDTO.getEnrollmentStatus()
            );
            enrollTMS.add(enrollTM);
        }
        tblRegister.setItems(enrollTMS);
    }

    private void loadNextRegistrationId() throws SQLException, ClassNotFoundException {
        Optional<String> nextRegistrationIdOptional = registrationBO.getNextRegisterId();
        String nextRegistrationId = nextRegistrationIdOptional.orElse("REG001");
        lblRegisterId.setText(nextRegistrationId);
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String enrollId = lblRegisterId.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> optionalButtonType = alert.showAndWait();

        if (optionalButtonType.isPresent() && optionalButtonType.get() == ButtonType.YES) {

            boolean isDeleted = registrationBO.deleteRegistration(enrollId);

            if (isDeleted) {
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION, "Enrollment record deleted...!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to delete enrollment record...!").show();
            }
        }
    }

    @FXML
    void btnResetOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        refreshPage();
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String registrationId = lblRegisterId.getText();
        String patientId = cmbPatientId.getSelectionModel().getSelectedItem();
        String programId = cmbProId.getSelectionModel().getSelectedItem();
        String enrollmentStatus = cmbStatus.getSelectionModel().getSelectedItem();
        Date dateOfEnroll = Date.valueOf(lblDate.getText());


        if (cmbPatientId.getSelectionModel().getSelectedItem() == null || cmbProId.getSelectionModel().getSelectedItem() == null || cmbStatus.getSelectionModel().getSelectedItem() == null) {
            new Alert(Alert.AlertType.ERROR, "Please select patient id, program id and status before saving..!").show();
            return;
        } else {

            EnrollmentDTO enrollmentDTO = new EnrollmentDTO(
                    registrationId,
                    programId,
                    patientId,
                    dateOfEnroll,
                    enrollmentStatus
            );

            boolean isSaved = registrationBO.saveRegistration(enrollmentDTO);

            if (isSaved) {
                Alert saveAlert = new Alert(Alert.AlertType.INFORMATION, "Registration saved successfully...!");
                saveAlert.showAndWait(); // Wait until the user clicks "OK"

                // After the user clicks OK, show another message asking them to collect the payment
                Alert paymentAlert = new Alert(Alert.AlertType.INFORMATION, "Please collect the payment from the patient.");
                paymentAlert.showAndWait();

                navigateToPaymentPage();
                refreshPage();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to save registration...!").show();
            }
        }

    }

    @FXML
    void btnNavigateToPaymentAction(ActionEvent event) {
    }

    private void navigateToPaymentPage() throws SQLException, ClassNotFoundException {
        try {
            content.getChildren().clear();
            URL fxmlUrl = getClass().getResource("/view/PaymentTest.fxml");

            if (fxmlUrl == null) {
                throw new RuntimeException("FXML file not found!");
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/PaymentTest.fxml"));
            AnchorPane load = loader.load(); // Loads the FXML file and binds the controller

            System.out.println("reg: "+lblRegisterId.getText());

            PaymentController paymentController = loader.getController();
            paymentController.initData(
                    cmbPatientId.getSelectionModel().getSelectedItem(),
                    cmbProId.getSelectionModel().getSelectedItem(),
                    lblRegisterId.getText(), "N/A" , false// "Upfront Payment"
            );
            load.prefWidthProperty().bind(content.widthProperty());
            load.prefHeightProperty().bind(content.heightProperty());

            content.getChildren().add(load);

        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load page!").show();
        } catch (RuntimeException re) {
            re.printStackTrace();
            new Alert(Alert.AlertType.ERROR, re.getMessage()).show();
        }
    }

    private void resetStyles() {
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {

    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String registrationId = lblRegisterId.getText();
        String patientId = cmbPatientId.getSelectionModel().getSelectedItem();
        String programId = cmbProId.getSelectionModel().getSelectedItem();
        String enrollmentStatus = cmbStatus.getSelectionModel().getSelectedItem();
        Date dateOfEnroll = Date.valueOf(lblDate.getText());

        if (cmbPatientId.getSelectionModel().getSelectedItem() == null || cmbProId.getSelectionModel().getSelectedItem() == null || cmbStatus.getSelectionModel().getSelectedItem() == null) {
            new Alert(Alert.AlertType.ERROR, "Please select patient id, program id and status before updating..!").show();
            return;
        } else {

            EnrollmentDTO enrollmentDTO = new EnrollmentDTO(
                    registrationId,
                    programId,
                    patientId,
                    dateOfEnroll,
                    enrollmentStatus
            );

            boolean isUpdated = registrationBO.updateRegistration(enrollmentDTO);

            if (isUpdated) {
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION, "Patient successfully updated...!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to update patient...!").show();
            }
        }
    }

    @FXML
    void cmbPatientIdOnAction(ActionEvent event) {

    }

    @FXML
    void cmbProgramIdOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String selectedProgramId = cmbProId.getSelectionModel().getSelectedItem();
        
        ProgramDTO programDTO = programBO.findByProgramId(selectedProgramId);

        if (programDTO != null) {
            lblProFee.setText(programDTO.getCost().toString());
        }
    }

    @FXML
    void onClickTable(MouseEvent event) {
        EnrollTM enrollTM = tblRegister.getSelectionModel().getSelectedItem();
        if(enrollTM != null){
            lblRegisterId.setText(enrollTM.getRegistrationId());
            cmbStatus.setValue(enrollTM.getEnrollmentStatus());
            cmbPatientId.setValue(enrollTM.getPatientId());
            cmbProId.setValue(enrollTM.getProgramId());

            btnSave.setDisable(true);
            btnDelete.setDisable(false);
            btnUpdate.setDisable(false);
            btnAddSession.setDisable(false);
           // btnCollectRegFee.setDisable(false);
        }
    }

    public void cmbStatusOnAction(ActionEvent actionEvent) {
    }

    public void btnAddSessionOnAction(ActionEvent actionEvent) {
        try {
            content.getChildren().clear();
            URL fxmlUrl = getClass().getResource("/view/Session.fxml");

            if (fxmlUrl == null) {
                throw new RuntimeException("FXML file not found!");
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Session.fxml"));
            AnchorPane load = loader.load(); // Loads the FXML file and binds the controller

            System.out.println("reg: "+lblRegisterId.getText());

            TherapySessionController therapySessionController = loader.getController();
            therapySessionController.initData(
                    lblRegisterId.getText(),
                    cmbProId.getSelectionModel().getSelectedItem(),
                    cmbPatientId.getSelectionModel().getSelectedItem(), false// "Upfront Payment"
            );
            load.prefWidthProperty().bind(content.widthProperty());
            load.prefHeightProperty().bind(content.heightProperty());

            content.getChildren().add(load);

        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load page!").show();
        } catch (RuntimeException re) {
            re.printStackTrace();
            new Alert(Alert.AlertType.ERROR, re.getMessage()).show();
        }
    }
}

