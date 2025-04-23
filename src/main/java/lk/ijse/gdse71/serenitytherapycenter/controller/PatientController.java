package lk.ijse.gdse71.serenitytherapycenter.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.gdse71.serenitytherapycenter.bo.BOFactory;
import lk.ijse.gdse71.serenitytherapycenter.bo.custom.PatientBO;
import lk.ijse.gdse71.serenitytherapycenter.dto.PatientDTO;
import lk.ijse.gdse71.serenitytherapycenter.dto.tm.PatientTM;
import net.sf.jasperreports.engine.*;

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
 * Created: 3/20/2025 2:11 PM
 * Project: MobileZone
 * --------------------------------------------
 **/

public class PatientController implements Initializable {

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnReset;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private TableColumn<PatientTM, String> colDob;

    /*@FXML
    private TableColumn<PatientTM, String> colFirstName;

    @FXML
    private TableColumn<PatientTM, String> colLastName;*/

    @FXML
    private TableColumn<PatientTM, String> colName;

    @FXML
    private TableColumn<PatientTM, String> colGender;

    @FXML
    private TableColumn<PatientTM, String> colNic;

    @FXML
    private TableColumn<PatientTM, String> colPatientId;

    @FXML
    private TableColumn<PatientTM, String> colPhone;

    @FXML
    private DatePicker txtDateOfBirth;

    @FXML
    private ToggleGroup gender;

    @FXML
    private Label lblPatientId;

    @FXML
    private RadioButton rBtnFemale;

    @FXML
    private RadioButton rBtnMale;

    @FXML
    private TableView<PatientTM> tblPatient;

    /*@FXML
    private TextField txtFname;

    @FXML
    private TextField txtLname;*/

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtNic;

    @FXML
    private TextField txtPhone;


    PatientBO patientBO = (PatientBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.PATIENT);


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        colPatientId.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        //colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        //colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDob.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colNic.setCellValueFactory(new PropertyValueFactory<>("nic"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));

        try {
            refreshPage();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load patient id").show();
        }

    }

    private void refreshPage() throws SQLException, ClassNotFoundException {
        loadNextPatientId();
        loadTableData();

        btnSave.setDisable(false);

        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
        btnReset.setDisable(false);
        //txtFname.setText("");
        //txtLname.setText("");
        txtName.setText("");
        txtNic.setText("");
        txtPhone.setText("");
        rBtnFemale.setSelected(false);
        rBtnMale.setSelected(false);
        txtDateOfBirth.setValue(null);
    }

    private void loadNextPatientId() throws SQLException, ClassNotFoundException {
        Optional<String> nextPatientIdOptional = patientBO.getNextPatientId();  // Fetch Optional<String> from business logic
        // Unwrap Optional value before setting in UI (text field)
        String nextPatientId = nextPatientIdOptional.orElse("P001");  // If empty, use default "P001"
        lblPatientId.setText(nextPatientId); // Set the actual patient ID in the text field
    }

    private void loadTableData() throws SQLException, ClassNotFoundException {
        List<PatientDTO> patientDTOS = patientBO.getAllPatients();

        ObservableList<PatientTM> patientTMS = FXCollections.observableArrayList();

        for(PatientDTO patientDTO : patientDTOS){
            PatientTM patientTM = new PatientTM(
                    patientDTO.getPatientId(),
                    //patientDTO.getFirstName(),
                    //patientDTO.getLastName(),
                    patientDTO.getName(),
                    patientDTO.getDateOfBirth(),
                    patientDTO.getGender(),
                    patientDTO.getNic(),
                    patientDTO.getPhone()
            );
            patientTMS.add(patientTM);
        }
        tblPatient.setItems(patientTMS);
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String patientId = lblPatientId.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> optionalButtonType = alert.showAndWait();

        if (optionalButtonType.isPresent() && optionalButtonType.get() == ButtonType.YES) {

            boolean isDeleted = patientBO.deletePatient(patientId);

            if (isDeleted) {
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION, "Patient record deleted...!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to delete patient...!").show();
            }
        }
    }

    private void resetStyles() {
        // Reset input field styles
        txtName.setStyle(txtName.getStyle() + ";-fx-border-color: #7367F0;");
        txtDateOfBirth.setStyle(txtDateOfBirth.getStyle() + "#7367F0");
        txtNic.setStyle(txtNic.getStyle() + ";-fx-border-color: #7367F0;");
        txtPhone.setStyle(txtPhone.getStyle() + ";-fx-border-color: #7367F0;");
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String patientId = lblPatientId.getText();
        //String firstName = txtFname.getText();
        //String lastName = txtLname.getText();
        String name = txtName.getText();
        String dateOfBirth = String.valueOf(txtDateOfBirth.getValue());
        LocalDate selectedDate = txtDateOfBirth.getValue();
        Date dob = Date.valueOf(txtDateOfBirth.getValue());
        //Date dateOfBirth = Date.valueOf(txtDateOfBirth.getValue());
        String gender = rBtnFemale.isSelected() ? "Female" : "Male";
        String nic = txtNic.getText();
        String phone = txtPhone.getText();

        //resetStyles();

        String namePattern = "^[A-Za-z ]+$";
        String nicPattern = "^[0-9]{9}[vVxX]||[0-9]{12}$";
        //String emailPattern = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        String phonePattern = "^(\\d+)||((\\d+\\.)(\\d){2})$";
        String phoneNumberPattern = "^\\+(\\d{1,3})\\s?\\d{7,15}$";

        //boolean isValidName = firstName.matches(namePattern) && lastName.matches(namePattern);
        boolean isValidName = name.matches(namePattern);
        boolean isValidNic = nic.matches(nicPattern);
        boolean isValidPhone = phone.matches(phoneNumberPattern);

        try {
            Date date = Date.valueOf(dateOfBirth);
            if(selectedDate == null || dateOfBirth.equals("") || dob == null){
                new Alert(Alert.AlertType.ERROR, "Please fill date field!").show();
                return;
            }
        } catch (IllegalArgumentException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid date format. Please enter a valid date (yyyy-MM-dd).").show();
            return;
        }

        if (!isValidName) {
            //txtFname.setStyle(txtFname.getStyle() + ";-fx-border-color: red;");
            //txtLname.setStyle(txtLname.getStyle() + ";-fx-border-color: red;");
            txtName.setStyle(txtName.getStyle() + ";-fx-border-color: red;");
            System.out.println("Invalid name.............");
            return;
        }

        if (!isValidNic) {
            txtNic.setStyle(txtNic.getStyle() + ";-fx-border-color: red;");
            return;
        }

        if (!isValidPhone) {
            txtPhone.setStyle(txtPhone.getStyle() + ";-fx-border-color: red;");
        }

        if(name.equals("") || txtDateOfBirth.getValue() == null || txtNic.equals("") || txtPhone.equals("") || (!rBtnFemale.isSelected() && !rBtnMale.isSelected())) {
            new Alert(Alert.AlertType.ERROR, "Above fields cannot be empty! Fill those empty fields.. ").show();
            return;
        }

        if (isValidName && isValidNic && isValidPhone) {
            PatientDTO patientDTO = new PatientDTO(
                    patientId,
                    /*firstName,
                    lastName,*/
                    name,
                    dob,
                    gender,
                    nic,
                    phone
            );

            boolean isSaved = patientBO.savePatient(patientDTO);

            if (isSaved) {
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION, "Patient saved...!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to save patient...!").show();
            }
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String patientId = lblPatientId.getText();
        /*String firstName = txtFname.getText();
        String lastName = txtLname.getText();*/
        String name = txtName.getText();
        String dateOfBirth = String.valueOf(txtDateOfBirth.getValue());
        LocalDate selectedDate = txtDateOfBirth.getValue();
        Date dob = Date.valueOf(txtDateOfBirth.getValue());
        //Date dateOfBirth = Date.valueOf(txtDateOfBirth.getValue());
        String gender = rBtnFemale.isSelected() ? "Female" : "Male";
        String nic = txtNic.getText();
        String phone = txtPhone.getText();

        resetStyles();

        String namePattern = "^[A-Za-z ]+$";
        String nicPattern = "^[0-9]{9}[vVxX]||[0-9]{12}$";
        //String emailPattern = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        String phonePattern = "^(\\d+)||((\\d+\\.)(\\d){2})$";
        String phoneNumberPattern = "^\\+(\\d{1,3})\\s?\\d{7,15}$";

        boolean isValidName = name.matches(namePattern);
        boolean isValidNic = nic.matches(nicPattern);
        boolean isValidPhone = phone.matches(phoneNumberPattern);

        try {
            Date date = Date.valueOf(dateOfBirth);
            if(selectedDate == null || dateOfBirth.equals("") || dob == null){
                new Alert(Alert.AlertType.ERROR, "Please fill date field!").show();
                return;
            }
        } catch (IllegalArgumentException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid date format. Please enter a valid date (yyyy-MM-dd).").show();
            return;
        }

        if (!isValidName) {
            txtName.setStyle(txtName.getStyle() + ";-fx-border-color: red;");
            System.out.println("Invalid name.............");
            return;
        }

        if (!isValidNic) {
            txtNic.setStyle(txtNic.getStyle() + ";-fx-border-color: red;");
            return;
        }

        if (!isValidPhone) {
            txtPhone.setStyle(txtPhone.getStyle() + ";-fx-border-color: red;");
        }

        if(name.equals("") || txtDateOfBirth.getValue() == null || txtNic.equals("") || txtPhone.equals("") || (!rBtnFemale.isSelected() && !rBtnMale.isSelected())) {
            new Alert(Alert.AlertType.ERROR, "Above fields cannot be empty! Fill those empty fields.. ").show();
            return;
        }

        if (isValidName && isValidNic && isValidPhone) {
            PatientDTO patientDTO = new PatientDTO(
                    patientId,
                    /*firstName,
                    lastName,*/
                    name,
                    dob,
                    gender,
                    nic,
                    phone
            );

            boolean isUpdated = patientBO.updatePatient(patientDTO);

            if (isUpdated) {
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION, "Patient updated...!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to update patient...!").show();
            }
        }

    }

    @FXML
    void generateAllPatientReportOnAction(ActionEvent event) {
        /*final FactoryConfiguration factoryConfiguration = FactoryConfiguration.getInstance();
        Session session = factoryConfiguration.getSession();
        try  {
            // Fetch patient data using Hibernate
            Query<Patient> query = session.createQuery("FROM Patient", Patient.class);
            List<Patient> patients = query.list();

            // Wrap the patient list in a JRBeanCollectionDataSource
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(patients);

            // Compile the Jasper report from .jrxml file
            JasperReport jasperReport = JasperCompileManager.compileReport(
                    getClass().getResourceAsStream("/report/Blank_A4_4.jrxml"));

            // Set report parameters
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("today", LocalDate.now().toString());
            parameters.put("TODAY", LocalDate.now().toString());

            // Fill the report with data
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            // View the report
            JasperViewer.viewReport(jasperPrint, false);
        } catch (JRException e) {
            new Alert(Alert.AlertType.ERROR, "Fail to load patient report...!").show();
            e.printStackTrace();
        }*/

    }

    @FXML
    void getGenderOnAction(ActionEvent event) {
        RadioButton selectedRadioButton = (RadioButton) gender.getSelectedToggle();
        if(selectedRadioButton != null) {
            String selectedGender = selectedRadioButton.getText();
        } else {
            new Alert(Alert.AlertType.ERROR, "Gender field cannot be empty! Select the gender.. ").show();
        }
    }

    @FXML
    void onClickTable(MouseEvent event) {
        PatientTM patientTM = tblPatient.getSelectionModel().getSelectedItem();
        if(patientTM != null){
            lblPatientId.setText(patientTM.getPatientId());
            txtDateOfBirth.setValue(patientTM.getDateOfBirth().toLocalDate());
            /*txtFname.setText(patientTM.getFirstName());
            txtLname.setText(patientTM.getLastName());*/
            txtName.setText(patientTM.getName());
            txtNic.setText(patientTM.getNic());
            txtPhone.setText(patientTM.getPhone());

            if ("Female".equalsIgnoreCase(patientTM.getGender())) {
                rBtnFemale.setSelected(true);
            } else if ("Male".equalsIgnoreCase(patientTM.getGender())) {
                rBtnMale.setSelected(true);
            } else {
                // Handle cases where gender is not specified or is different
                gender.selectToggle(null); // Deselect both radio buttons
            }


            btnSave.setDisable(true);
            btnDelete.setDisable(false);
            btnUpdate.setDisable(false);
            //btnSendMail.setDisable(false);
        }
    }

    @FXML
    void sessionReportOnAction(ActionEvent event) {

    }

    @FXML
    void resetOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        refreshPage();
    }

    @FXML
    void dateOfBirthOnAction(ActionEvent actionEvent) {
        LocalDate selectedDate = txtDateOfBirth.getValue();
        LocalDate today = LocalDate.now();
        String dateString = String.valueOf(selectedDate);
        if (selectedDate != null) {
            if (selectedDate.isAfter(today)) {
                txtDateOfBirth.setValue(null); // Reset selection
                new Alert(Alert.AlertType.ERROR, "Please input a valid date!").show();
                return;
            }
        }
    }

    @FXML
    public void btnResetOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        refreshPage();
    }
}
