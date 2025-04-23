package lk.ijse.gdse71.serenitytherapycenter.controller;

/**
 * --------------------------------------------
 * Author: Zeenathul Ilma
 * GitHub: https://github.com/Seenathul-Ilma
 * Website: https://ilma.live
 * --------------------------------------------
 * Created: 4/20/2025 10:14 AM
 * Project: MobileZone
 * --------------------------------------------
 **/

import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import lk.ijse.gdse71.serenitytherapycenter.bo.BOFactory;
import lk.ijse.gdse71.serenitytherapycenter.bo.custom.PatientBO;
import lk.ijse.gdse71.serenitytherapycenter.bo.custom.PaymentBO;
import lk.ijse.gdse71.serenitytherapycenter.bo.custom.ProgramBO;
import lk.ijse.gdse71.serenitytherapycenter.dto.PaymentDTO;
import lk.ijse.gdse71.serenitytherapycenter.dto.tm.PaymentTM;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class PaymentController implements Initializable {

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnPrint;

    @FXML
    private Button btnReset;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnSearch;

    @FXML
    private TableColumn<PaymentTM, String> colPatId;

    @FXML
    private TableColumn<PaymentTM, Double> colPayAmount;

    @FXML
    private TableColumn<PaymentTM, Date> colPayDate;

    @FXML
    private TableColumn<PaymentTM, String> colPayId;

    @FXML
    private TableColumn<PaymentTM, String> colPayType;

    @FXML
    private TableColumn<PaymentTM, String> colProId;

    /*@FXML
    private TableColumn<PaymentTM, String> colRegId;

    @FXML
    private TableColumn<PaymentTM, String> colSessionId;*/

    @FXML
    private TableColumn<PaymentTM, String> colRefId;

    @FXML
    private VBox insertVBox;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblPatId;

    @FXML
    private Label lblPayId;

    @FXML
    private Label lblProId;

    @FXML
    private Label lblRegistrationId;

    @FXML
    private Label lblSessionId;

    @FXML
    private TableView<PaymentTM> tblPayment;

    @FXML
    private TextField txtPayAmount;

    @FXML
    private TextField txtSearch;
    /*@FXML
    private Label lblPayType;*/

    //public static String paymentType;

    PaymentBO paymentBO = (PaymentBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.PAYMENT);
    PatientBO patientBO = (PatientBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.PATIENT);
    ProgramBO programBO = (ProgramBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.THERAPY_PROGRAM);

    //public void initData(String patientId, String programId, String referenceId, String payType) {
    public void initData(String patientId, String programId, String registrationId, String sessionId, boolean isNavigateSaveBtn) {
        lblPatId.setText(patientId);
        lblProId.setText(programId);
        lblRegistrationId.setText(registrationId);
        lblSessionId.setText(sessionId);
        btnSave.setDisable(isNavigateSaveBtn);

       // paymentType = payType;

        // You can now use them in initialize method or later
        System.out.println("Received: " + patientId + ", " + programId + ", " + registrationId);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colPayId.setCellValueFactory(new PropertyValueFactory<>("paymentId"));
        colPatId.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        colPayAmount.setCellValueFactory(new PropertyValueFactory<>("paymentAmount"));
        colPayDate.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));
        colProId.setCellValueFactory(new PropertyValueFactory<>("programId"));
        //colRegId.setCellValueFactory(new PropertyValueFactory<>("registration_id"));
        //colSessionId.setCellValueFactory(new PropertyValueFactory<>("session_id"));
        colPayType.setCellValueFactory(new PropertyValueFactory<>("paymentType"));

        // Smart binding for session or registration based on payment type
        colRefId.setCellValueFactory(cellData -> {
            PaymentTM payment = cellData.getValue();
            String refId = payment.getPaymentType().equalsIgnoreCase("Upfront Payment")
                    ? payment.getRegistrationId()
                    : payment.getSessionId();
            return new SimpleStringProperty(refId != null ? refId : "N/A");
        });

        try {
            refreshPage();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load payment id").show();
        }
    }

    private void refreshPage() throws SQLException, ClassNotFoundException {
        loadNextPaymentId();
        loadTableData();

        btnSave.setDisable(true);

        lblDate.setText(LocalDate.now().toString());

        btnDelete.setDisable(true);
        btnPrint.setDisable(true);
        btnReset.setDisable(false);
        txtSearch.setText("");
        lblRegistrationId.setText("");
        lblSessionId.setText("");
        lblPatId.setText("");
        lblProId.setText("");
        txtPayAmount.setText("");
    }

    private void loadNextPaymentId() throws SQLException, ClassNotFoundException {
        Optional<String> nextPaymentIdOptional = paymentBO.getNextPaymentId();
        String nextPaymentId = nextPaymentIdOptional.orElse("PAY001");
        lblPayId.setText(nextPaymentId);
    }

    private void loadTableData() throws SQLException, ClassNotFoundException {
        List<PaymentDTO> paymentDTOS = paymentBO.getAllPayments();

        ObservableList<PaymentTM> paymentTMS = FXCollections.observableArrayList();

        for(PaymentDTO paymentDTO : paymentDTOS){

            PaymentTM paymentTM = new PaymentTM(
                    paymentDTO.getPaymentId(),
                    paymentDTO.getEnrollmentId(),
                    paymentDTO.getPaymentType(),
                    paymentDTO.getPatientId(),
                    paymentDTO.getProgramId(),
                    //paymentDTO.getSessionId(),
                    paymentDTO.getSessionId() == null ? "N/A" : paymentDTO.getSessionId(),
                    paymentDTO.getPaymentDate(),
                    paymentDTO.getPaymentAmount()
            );

            System.out.println("Loaded Payment: " + paymentDTO.getPaymentId() + " | Type: " + paymentDTO.getPaymentType() + " | Ref ID: " + (paymentDTO.getPaymentType().equalsIgnoreCase("Upfront Payment") ? paymentDTO.getEnrollmentId() : paymentDTO.getSessionId()));

            paymentTMS.add(paymentTM);
        }
        tblPayment.setItems(paymentTMS);

    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {

    }

    @FXML
    void btnPrintInvoiceOnAction(ActionEvent event) {

    }

    @FXML
    void btnResetOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        refreshPage();
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String paymentId = lblPayId.getText();
        String patientId = lblPatId.getText();
        String programId = lblProId.getText();
        String registrationId = lblRegistrationId.getText();
        String sessionId = lblSessionId.getText();
        String paymentAmount = txtPayAmount.getText();
        Date dateOfPayment = Date.valueOf(lblDate.getText());
        //String paymentType = "Session Payment";


        if (txtPayAmount.getText() == null || txtPayAmount.getText().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please insert the amount of payment..!").show();
            return;
        } else {

           /* if(sessionId.matches("N/A")) {
                paymentType = "Upfront Payment";
            }*/

            String paymentType;
            if (sessionId == null || sessionId.equalsIgnoreCase("N/A")) {
                paymentType = "Upfront Payment";
                sessionId = null; // Important: don't send "N/A" to DB
            } else {
                paymentType = "Session Payment";
            }

            Double payAmount = Double.valueOf(paymentAmount);

            PaymentDTO paymentDTO = new PaymentDTO(
                    paymentId,
                    dateOfPayment,
                    payAmount,
                    paymentType,
                    patientId,
                    programId,
                    registrationId,
                    sessionId
            );

            boolean isSaved = paymentBO.savePayment(paymentDTO);

            if (isSaved) {
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION, "Payment saved successfully...!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to save payment...!").show();
            }
        }

    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {

    }

    @FXML
    void onClickTable(MouseEvent event) {
        btnPrint.setDisable(false);
        btnDelete.setDisable(false);
    }

    @FXML
    void btnPrintOnAction(ActionEvent actionEvent) {
        PaymentTM selectedItem = tblPayment.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a payment..!");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/InvoiceView.fxml"));
            Parent load = loader.load();

            InvoiceController invoiceController = loader.getController();

            // Get required values from PaymentTM
            String paymentId = selectedItem.getPaymentId();
            String patientId = selectedItem.getPatientId();
            String programId = selectedItem.getProgramId();
            String paymentType = selectedItem.getPaymentType();
            String date = selectedItem.getPaymentDate().toString();
            String amount = String.format("%.2f", selectedItem.getPaymentAmount());

            // Fetch names from BO layer
            String patientName = patientBO.getPatientNameById(patientId);
            String programName = programBO.getProgramNameById(programId);

            // Set data to invoice controller
            invoiceController.setInvoiceData(date, paymentId, paymentType, patientName, programName, amount);

            Stage stage = new Stage();
            stage.setScene(new Scene(load));
            stage.setTitle("Invoice");
            stage.setResizable(false);

            stage.initModality(Modality.APPLICATION_MODAL);
            Window underWindow = btnReset.getScene().getWindow();
            stage.initOwner(underWindow);

            stage.showAndWait();
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Fail to load UI..!").show();
            e.printStackTrace();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load invoice data..!").show();
            e.printStackTrace();
        }

    }
}

