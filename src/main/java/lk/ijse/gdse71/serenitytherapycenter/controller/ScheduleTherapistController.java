package lk.ijse.gdse71.serenitytherapycenter.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lk.ijse.gdse71.serenitytherapycenter.bo.BOFactory;
import lk.ijse.gdse71.serenitytherapycenter.bo.custom.TherapistBO;
import lk.ijse.gdse71.serenitytherapycenter.bo.custom.TherapySessionBO;
import lk.ijse.gdse71.serenitytherapycenter.dto.TherapistDTO;
import lk.ijse.gdse71.serenitytherapycenter.dto.tm.TherapistTM;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * --------------------------------------------
 * Author: Zeenathul Ilma
 * GitHub: https://github.com/Seenathul-Ilma
 * Website: https://zeenathulilma.vercel.app/
 * --------------------------------------------
 * Created: 4/22/2025 7:30 PM
 * Project: MobileZone
 * --------------------------------------------
 **/

public class ScheduleTherapistController implements Initializable {

    @FXML
    private Button btnBookNow;

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
    private TableView<TherapistTM> tblTherapist;

    @FXML
    private DatePicker txtDatePicker;

    public String bookedTherapistId;
    public LocalDate bookedSessionDate;

    TherapistBO therapistBO = (TherapistBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.THERAPIST);
    TherapySessionBO therapySessionBO = (TherapySessionBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.THERAPY_SESSION);

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
        btnBookNow.setDisable(true);
        loadTableData();
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
    void bookNowOnAction(ActionEvent event) {
        // Close the modal window
        Stage stage = (Stage) btnBookNow.getScene().getWindow();
        stage.close();
    }

    @FXML
    void datePickOnAction(ActionEvent event) {
        LocalDate selectedDate = txtDatePicker.getValue();
        if (selectedDate == null) {
            new Alert(Alert.AlertType.WARNING, "Please pick a date!").show();
            return;
        }

        if (selectedDate.isBefore(LocalDate.now())) {
            new Alert(Alert.AlertType.WARNING, "Please pick an upcoming date!").show();
            return;
        }

        // Get therapists that are already booked for the selected date
        List<String> bookedTherapists = getBookedTherapistsForDate(Date.valueOf(selectedDate));

        // Get all available therapists for the selected day
        DayOfWeek selectedDay = selectedDate.getDayOfWeek();

        String dayType = getDayType(selectedDay);

        ArrayList<TherapistDTO> allTherapists = therapistBO.checkAvailableTherapistsByDay(selectedDay, dayType);

        // Remove the booked therapists from the available list
        ArrayList<TherapistDTO> availableTherapists = new ArrayList<>();
        for (TherapistDTO therapistDTO : allTherapists) {
            if (!bookedTherapists.contains(therapistDTO.getTherapistId())) {
                availableTherapists.add(therapistDTO);
            }
        }

        // Show available therapists
        ObservableList<TherapistTM> therapistTMS = FXCollections.observableArrayList();
        for (TherapistDTO therapistDTO : availableTherapists) {
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

        // Step 5: Show message if no therapists are available
        if (therapistTMS.isEmpty()) {
            tblTherapist.getItems().clear();
            new Alert(Alert.AlertType.INFORMATION, "No available therapists on selected date.").show();
        } else {
            tblTherapist.setItems(therapistTMS);
        }
    }

    private String getDayType(DayOfWeek selectedDay) {
        if(selectedDay == DayOfWeek.SATURDAY || selectedDay == DayOfWeek.SUNDAY){
            return "Weekends";
        }
        return "Weekdays";
    }

    List<String> getBookedTherapistsForDate(Date selectedDate) {
        // Fetch the list of booked therapists for the selected date
        return therapySessionBO.checkBookedTherapistsByDate(selectedDate);
    }

    @FXML
    void onClickTableScheduleBook(MouseEvent event) {
        TherapistTM selectedTherapist = tblTherapist.getSelectionModel().getSelectedItem();

        btnBookNow.setDisable(false);

        if(selectedTherapist != null){
            bookedTherapistId = selectedTherapist.getTherapistId();
            bookedSessionDate = Date.valueOf(txtDatePicker.getValue()).toLocalDate();
        }
    }

    public String getTherapistId() {
        return bookedTherapistId;
    }

    public LocalDate getSessionDate() {
        return bookedSessionDate;
    }
}
