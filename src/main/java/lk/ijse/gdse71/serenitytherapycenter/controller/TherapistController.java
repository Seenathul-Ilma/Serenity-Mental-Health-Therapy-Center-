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
import lk.ijse.gdse71.serenitytherapycenter.bo.custom.TherapistBO;
import lk.ijse.gdse71.serenitytherapycenter.dto.TherapistDTO;
import lk.ijse.gdse71.serenitytherapycenter.dto.tm.TherapistTM;
import org.controlsfx.control.CheckComboBox;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * --------------------------------------------
 * Author: Zeenathul Ilma
 * GitHub: https://github.com/Seenathul-Ilma
 * Website: https://ilma.live
 * --------------------------------------------
 * Created: 3/21/2025 10:58 AM
 * Project: MobileZone
 * --------------------------------------------
 **/

public class TherapistController implements Initializable {

    @FXML
    private CheckComboBox<String> availableListCombo;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnReset;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private TableColumn<TherapistTM, String> colAvailability;

    @FXML
    private TableColumn<TherapistTM, String> colEmail;

    @FXML
    private TableColumn<TherapistTM, String> colName;

    @FXML
    private TableColumn<TherapistTM, String> colNic;

    @FXML
    private TableColumn<TherapistTM, String> colPhone;

    @FXML
    private TableColumn<TherapistTM, String> colSpeciality;

    @FXML
    private TableColumn<TherapistTM, String> colTherapistId;

    @FXML
    private Label lblTherapistId;

    @FXML
    private TableView<TherapistTM> tblTherapist;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtNic;

    @FXML
    private TextField txtPhone;

    @FXML
    private TextField txtSpecial;

    TherapistBO therapistBO = (TherapistBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.THERAPIST);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colTherapistId.setCellValueFactory(new PropertyValueFactory<>("therapistId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colSpeciality.setCellValueFactory(new PropertyValueFactory<>("speciality"));
        colNic.setCellValueFactory(new PropertyValueFactory<>("nic"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colAvailability.setCellValueFactory(new PropertyValueFactory<>("availability"));

        try {
            refreshPage();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load therapist id").show();
        }
    }

    private void refreshPage() throws SQLException, ClassNotFoundException {
        loadNextTherapistId();
        loadTableData();
        loadAvailability();

        btnSave.setDisable(false);

        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
        btnReset.setDisable(false);
        txtName.setText("");
        txtSpecial.setText("");
        txtNic.setText("");
        txtEmail.setText("");
        txtPhone.setText("");
        availableListCombo.getCheckModel().clearChecks();
    }

    private void loadNextTherapistId() throws SQLException, ClassNotFoundException {
        Optional<String> nextTherapistIdOptional = therapistBO.getNextTherapistId();  // Fetch Optional<String> from business logic
        // Unwrap Optional value before setting in UI (text field)
        String nextTherapistId = nextTherapistIdOptional.orElse("T001");  // If empty, use default "P001"
        lblTherapistId.setText(nextTherapistId); // Set the actual patient ID in the text field
    }

    private void loadTableData() throws SQLException, ClassNotFoundException {
        List<TherapistDTO> therapistDTOS = therapistBO.getAllTherapist();

        ObservableList<TherapistTM> therapistTMS = FXCollections.observableArrayList();

        for(TherapistDTO therapistDTO : therapistDTOS){
            TherapistTM therapistTM = new TherapistTM(
                    therapistDTO.getTherapistId(),
                    therapistDTO.getName(),
                    therapistDTO.getSpeciality(),
                    therapistDTO.getNic(),
                    therapistDTO.getEmail(),
                    therapistDTO.getPhone(),
                    therapistDTO.getAvailability()
            );
            therapistTMS.add(therapistTM);
        }
        tblTherapist.setItems(therapistTMS);
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String therapistId = lblTherapistId.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> optionalButtonType = alert.showAndWait();

        if (optionalButtonType.isPresent() && optionalButtonType.get() == ButtonType.YES) {

            boolean isDeleted = therapistBO.deleteTherapist(therapistId);

            if (isDeleted) {
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION, "Therapy record deleted...!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to delete therapist...!").show();
            }
        }
    }

    @FXML
    void btnResetOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        refreshPage();
    }

    private void resetStyles() {
        // Reset input field styles
        txtName.setStyle(txtName.getStyle() + ";-fx-border-color: #7367F0;");
        txtEmail.setStyle(txtEmail.getStyle() + ";-fx-border-color: #7367F0;");
        txtNic.setStyle(txtNic.getStyle() + ";-fx-border-color: #7367F0;");
        txtPhone.setStyle(txtPhone.getStyle() + ";-fx-border-color: #7367F0;");
    }

    private void loadAvailability() {
        ObservableList<String> availableList = FXCollections.observableArrayList(
                "Weekdays", "Weekends", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
        );
        availableListCombo.getItems().setAll(availableList);
    }

    private String getSelectedDays() {
        return String.join(", ", availableListCombo.getCheckModel().getCheckedItems());
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String therapistId = lblTherapistId.getText();
        String name = txtName.getText();
        String nic = txtNic.getText();
        String phone = txtPhone.getText();
        String email = txtEmail.getText();
        String speciality = txtSpecial.getText();
        String availabilities = getSelectedDays();

        //resetStyles();

        String namePattern = "^[A-Za-z ]+$";
        String nicPattern = "^[0-9]{9}[vVxX]||[0-9]{12}$";
        String emailPattern = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        //String phonePattern = "^(\\d+)||((\\d+\\.)(\\d){2})$";
        String phoneNumberPattern = "^\\+(\\d{1,3})\\s?\\d{7,15}$";

        boolean isValidName = name.matches(namePattern);
        boolean isValidEmail = email.matches(emailPattern);
        boolean isValidNic = nic.matches(nicPattern);
        boolean isValidPhone = phone.matches(phoneNumberPattern);

        if (!isValidName) {
           // txtName.setStyle(txtName.getStyle() + ";-fx-border-color: red;");
            System.out.println("Invalid name.............");
            return;
        }

        if (!isValidNic) {
            //txtNic.setStyle(txtNic.getStyle() + ";-fx-border-color: red;");
            return;
        }

        if (!isValidPhone) {
            //txtPhone.setStyle(txtPhone.getStyle() + ";-fx-border-color: red;");
            return;
        }

        if (!isValidEmail) {
            //txtEmail.setStyle(txtEmail.getStyle() + ";-fx-border-color: red;");
            return;
        }

        if(name.equals("") || speciality.equals("") || nic.equals("") || phone.equals("") || email.equals("") || availabilities.equals("")) {
            new Alert(Alert.AlertType.ERROR, "Above fields cannot be empty! Fill those empty fields.. ").show();
            return;
        }

        if (isValidName && isValidNic && isValidPhone && isValidEmail) {
            TherapistDTO therapistDTO = new TherapistDTO(
                    therapistId,
                    //firstName,
                    //lastName,
                    name,
                    speciality,
                    nic,
                    email,
                    phone,
                    availabilities
            );

            boolean isSaved = therapistBO.saveTherapist(therapistDTO);

            if (isSaved) {
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION, "Therapist saved...!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to save therapist...!").show();
            }
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String therapistId = lblTherapistId.getText();
        String name = txtName.getText();
        String nic = txtNic.getText();
        String phone = txtPhone.getText();
        String email = txtEmail.getText();
        String speciality = txtSpecial.getText();
        String availabilities = getSelectedDays();

        //resetStyles();

        String namePattern = "^[A-Za-z ]+$";
        String nicPattern = "^[0-9]{9}[vVxX]||[0-9]{12}$";
        String emailPattern = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        //String phonePattern = "^(\\d+)||((\\d+\\.)(\\d){2})$";
        String phoneNumberPattern = "^\\+(\\d{1,3})\\s?\\d{7,15}$";

        boolean isValidName = name.matches(namePattern);
        boolean isValidEmail = email.matches(emailPattern);
        boolean isValidNic = nic.matches(nicPattern);
        boolean isValidPhone = phone.matches(phoneNumberPattern);

        if (!isValidName) {
            //txtName.setStyle(txtName.getStyle() + ";-fx-border-color: red;");
            System.out.println("Invalid name.............");
            return;
        }

        if (!isValidNic) {
            //txtNic.setStyle(txtNic.getStyle() + ";-fx-border-color: red;");
            return;
        }

        if (!isValidPhone) {
            //txtPhone.setStyle(txtPhone.getStyle() + ";-fx-border-color: red;");
            return;
        }

        if (!isValidEmail) {
            //txtEmail.setStyle(txtEmail.getStyle() + ";-fx-border-color: red;");
            return;
        }

        if(name.equals("") || speciality.equals("") || nic.equals("") || phone.equals("") || email.equals("") || availabilities.equals("")) {
            new Alert(Alert.AlertType.ERROR, "Above fields cannot be empty! Fill those empty fields.. ").show();
            return;
        }

        if (isValidName && isValidNic && isValidPhone && isValidEmail) {
            TherapistDTO therapistDTO = new TherapistDTO(
                    therapistId,
                    //firstName,
                    //lastName,
                    name,
                    speciality,
                    nic,
                    email,
                    phone,
                    availabilities
            );


            boolean isUpdated = therapistBO.updateTherapist(therapistDTO);

            if (isUpdated) {
                new Alert(Alert.AlertType.INFORMATION, "Therapist updated...!").show();
                refreshPage();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to update therapist...!").show();
            }
        }
    }

    @FXML
    void generateAllTherapistReportOnAction(ActionEvent event) {

    }

    @FXML
    void onClickTable(MouseEvent event) {
        TherapistTM therapistTM = tblTherapist.getSelectionModel().getSelectedItem();
        if(therapistTM != null){
            lblTherapistId.setText(therapistTM.getTherapistId());
            //txtFName.setText(therapistTM.getFirstName());
            //txtLName.setText(therapistTM.getLastName());
            txtName.setText(therapistTM.getName());
            txtNic.setText(therapistTM.getNic());
            txtPhone.setText(therapistTM.getPhone());
            txtEmail.setText(therapistTM.getEmail());
            txtSpecial.setText(therapistTM.getSpeciality());

            // Clear previous selections
            availableListCombo.getCheckModel().clearChecks();

            // Set the availability checkboxes
            String availability = therapistTM.getAvailability(); // Get stored availability string
            if (availability != null && !availability.isEmpty()) {
                String[] selectedDays = availability.split(", "); // Split stored days
                for (String day : selectedDays) {
                    if (availableListCombo.getItems().contains(day)) {
                        availableListCombo.getCheckModel().check(day);
                    }
                }
            }


            btnSave.setDisable(true);
            btnDelete.setDisable(false);
            btnUpdate.setDisable(false);
            //btnSendMail.setDisable(false);
        }
    }

    @FXML
    void sendMailOnAction(ActionEvent event) {

    }

    public void onClickAvailableListCombo(MouseEvent mouseEvent) {
        availableListCombo.getCheckModel().getCheckedItems();
    }
}
