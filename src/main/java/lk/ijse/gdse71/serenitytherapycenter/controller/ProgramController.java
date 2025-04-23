package lk.ijse.gdse71.serenitytherapycenter.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import lk.ijse.gdse71.serenitytherapycenter.bo.BOFactory;
import lk.ijse.gdse71.serenitytherapycenter.bo.custom.ProgramBO;
import lk.ijse.gdse71.serenitytherapycenter.dto.ProgramDTO;
import lk.ijse.gdse71.serenitytherapycenter.dto.tm.ProgramTM;

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
 * Created: 3/24/2025 9:04 PM
 * Project: MobileZone
 * --------------------------------------------
 **/

public class ProgramController implements Initializable {
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
    private TableColumn<ProgramTM, Double> colAmount;

    @FXML
    private TableColumn<ProgramTM, String> colDesc;

    @FXML
    private TableColumn<ProgramTM, String> colDuration;

    @FXML
    private TableColumn<ProgramTM, String> colProId;

    @FXML
    private TableColumn<ProgramTM, String> colProName;

    @FXML
    private ComboBox<String> combDuration;

    @FXML
    private VBox insertVBox;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblProgramId;

    @FXML
    private TableView<ProgramTM> tblProgram;

    @FXML
    private TextField txtAmount;

    @FXML
    private TextField txtDuration;

    @FXML
    private TextField txtProDesc;

    @FXML
    private TextField txtProName;

    @FXML
    private TextField txtSearch;

    ProgramBO programBO = (ProgramBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.THERAPY_PROGRAM);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colProId.setCellValueFactory(new PropertyValueFactory<>("programId"));
        colProName.setCellValueFactory(new PropertyValueFactory<>("programName"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("programDescription"));
        colDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("cost"));

        try {
            refreshPage();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load program id").show();
        }
    }

    private void refreshPage() throws SQLException, ClassNotFoundException {
        loadTableData();
        loadDuration();
        loadNextProgramId();
        insertVBox.setDisable(false);

        btnSave.setDisable(false);
        btnSearch.setDisable(false);

        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
        btnReset.setDisable(false);
        txtProName.setText("");
        txtSearch.setText("");
        txtProDesc.setText("");
        txtDuration.setText("");
        txtAmount.setText("");
        combDuration.getSelectionModel().clearSelection();
    }

    private void loadNextProgramId() throws SQLException, ClassNotFoundException {
        Optional<String> nextProgramIdOptional = programBO.getNextProgramId();
        String nextProgramId = nextProgramIdOptional.orElse("TP001");
        lblProgramId.setText(nextProgramId);
    }

    private void loadTableData() throws SQLException, ClassNotFoundException {
        List<ProgramDTO> programDTOS = programBO.getAllPrograms();
        ObservableList<ProgramTM> programTMS = FXCollections.observableArrayList();
        for (ProgramDTO programDTO : programDTOS) {
            ProgramTM programTM = new ProgramTM(
                    programDTO.getProgramId(),
                    programDTO.getProgramName(),
                    programDTO.getProgramDescription(),
                    programDTO.getDuration(),
                    programDTO.getCost()
            );
            programTMS.add(programTM);
        }
        tblProgram.setItems(programTMS);
    }

    private void loadDuration() {
        ObservableList<String> durations = FXCollections.observableArrayList(
                "Days",
                "Weeks",
                "Months",
                "Years"
        );
        System.out.println("load duration");
        combDuration.setItems(durations);
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String programId = lblProgramId.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> optionalButtonType = alert.showAndWait();

        if (optionalButtonType.isPresent() && optionalButtonType.get() == ButtonType.YES) {

            boolean isDeleted = programBO.deleteProgram(programId);

            if (isDeleted) {
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION, "Program record deleted...!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to delete program...!").show();
            }
        }
    }

    private void resetStyles() {
        // Reset input field styles
        txtProName.setStyle(txtProName.getStyle() + ";-fx-border-color: #7367F0;");
        txtSearch.setStyle(txtSearch.getStyle() + "#7367F0");
        txtDuration.setStyle(txtDuration.getStyle() + ";-fx-border-color: #7367F0;");
        txtAmount.setStyle(txtAmount.getStyle() + ";-fx-border-color: #7367F0;");
    }

    @FXML
    void btnResetOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        refreshPage();
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String programId = lblProgramId.getText();
        String proName = txtProName.getText();
        String proDesc = txtProDesc.getText();
        String duration = txtDuration.getText();
        String durationType = combDuration.getSelectionModel().getSelectedItem();
        String cost = txtAmount.getText();

        resetStyles();

        String namePattern = "^[A-Za-z ]+$";
        String durationPattern = "^\\d{1,2}$";
        String costPattern = "^([1-9]\\d{0,5}|0)(\\.\\d{1,2})?$";

        //boolean isValidName = firstName.matches(namePattern) && lastName.matches(namePattern);
        boolean isValidName = proName.matches(namePattern);
        boolean isValidDuration = duration.matches(durationPattern);
        boolean isValidAmount = cost.matches(costPattern);

        double amount = Math.abs(Double.valueOf(cost));
        String finalDuration = duration+" "+durationType;

        if (!isValidName) {
            txtProName.setStyle(txtProName.getStyle() + ";-fx-border-color: red;");
            System.out.println("Invalid name.............");
            return;
        }

        if (!isValidDuration) {
            txtDuration.setStyle(txtDuration.getStyle() + ";-fx-border-color: red;");
            return;
        }

        if (!isValidAmount) {
            txtAmount.setStyle(txtAmount.getStyle() + ";-fx-border-color: red;");
            return;
        }

        if(combDuration.getSelectionModel().isEmpty()){
            new Alert(Alert.AlertType.ERROR, "You should select the duration type..!").show();
            return;
        }

        if(proName.equals("") || proDesc.equals("") || duration.equals("") || cost.equals("") || combDuration.getSelectionModel().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Above fields cannot be empty! Fill those empty fields.. ").show();
            return;
        }

        if (isValidName && isValidDuration && isValidAmount) {
            ProgramDTO programDTO = new ProgramDTO(
                    programId,
                    proName,
                    proDesc,
                    finalDuration,
                    amount
            );

            boolean isSaved = programBO.saveProgram(programDTO);

            if (isSaved) {
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION, "Therapy Program saved...!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to save therapy program...!").show();
            }
        }
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        insertVBox.setDisable(true);
        String searchPattern = "^TP\\d{3}$";
        String programId = txtSearch.getText();
        boolean isValidId = programId.matches(searchPattern);
        if (!isValidId) {
            txtSearch.setStyle(txtSearch.getStyle() + ";-fx-border-color: red;");
            return;
        }

        // Return the dto of searched program
        ProgramDTO programDTO = programBO.findByProgramId(programId);
        if (programDTO == null) {
            new Alert(Alert.AlertType.ERROR, "Therapy Program not found...!").show();
            refreshPage();
            return;
        }

        // Insert found DTO into table model
        ObservableList<ProgramTM> programTMS = FXCollections.observableArrayList();
        programTMS.add(new ProgramTM(
                programDTO.getProgramId(),
                programDTO.getProgramName(),
                programDTO.getProgramDescription(),
                programDTO.getDuration(),
                programDTO.getCost()
        ));

        // Set the table data
        tblProgram.setItems(programTMS);
        // Ensure the table updates
        tblProgram.refresh();
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String programId = lblProgramId.getText();
        String proName = txtProName.getText();
        String proDesc = txtProDesc.getText();
        String duration = txtDuration.getText();
        String durationType = combDuration.getSelectionModel().getSelectedItem();
        String cost = txtAmount.getText();

        resetStyles();

        String namePattern = "^[A-Za-z ]+$";
        String durationPattern = "^\\d{1,2}$";
        String costPattern = "^([1-9]\\d{0,5}|0)(\\.\\d{1,2})?$";

        //boolean isValidName = firstName.matches(namePattern) && lastName.matches(namePattern);
        boolean isValidName = proName.matches(namePattern);
        boolean isValidDuration = duration.matches(durationPattern);
        boolean isValidAmount = cost.matches(costPattern);

        double amount = Math.abs(Double.valueOf(cost));
        String finalDuration = duration+" "+durationType;

        if (!isValidName) {
            txtProName.setStyle(txtProName.getStyle() + ";-fx-border-color: red;");
            System.out.println("Invalid name.............");
            return;
        }

        if (!isValidDuration) {
            txtDuration.setStyle(txtDuration.getStyle() + ";-fx-border-color: red;");
            return;
        }

        if (!isValidAmount) {
            txtAmount.setStyle(txtAmount.getStyle() + ";-fx-border-color: red;");
            return;
        }

        if(combDuration.getSelectionModel().isEmpty()){
            new Alert(Alert.AlertType.ERROR, "You should select the duration type..!").show();
            return;
        }

        if(proName.equals("") || proDesc.equals("") || duration.equals("") || cost.equals("") || combDuration.getSelectionModel().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Above fields cannot be empty! Fill those empty fields.. ").show();
            return;
        }

        if (isValidName && isValidDuration && isValidAmount) {
            ProgramDTO programDTO = new ProgramDTO(
                    programId,
                    proName,
                    proDesc,
                    finalDuration,
                    amount
            );

            boolean isUpdated = programBO.updateProgram(programDTO);

            if (isUpdated) {
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION, "Therapy Program updated...!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to update therapy program...!").show();
            }
        }
    }

    @FXML
    void combDurationOnAction(ActionEvent event) {
        String selectedDuration = combDuration.getSelectionModel().getSelectedItem();
    }

    @FXML
    void onClickTable(MouseEvent event) {
        ProgramTM programTM = tblProgram.getSelectionModel().getSelectedItem();
        if (programTM != null) {
            lblProgramId.setText(programTM.getProgramId());
            txtProName.setText(programTM.getProgramName());
            txtProDesc.setText(programTM.getProgramDescription());

            // Extract duration (number and type)
            String[] durationParts = programTM.getDuration().split(" ", 2);
            if (durationParts.length == 2) {
                txtDuration.setText(durationParts[0]); // Number part
                combDuration.getSelectionModel().select(durationParts[1]); // Type part
            } else {
                txtDuration.clear();
                combDuration.getSelectionModel().clearSelection();
            }

            txtAmount.setText(String.valueOf(programTM.getCost()));

            // Enable/disable buttons accordingly
            btnSave.setDisable(true);
            btnDelete.setDisable(false);
            btnUpdate.setDisable(false);
            btnSearch.setDisable(true);
        }
    }

    /*void onClickTable(MouseEvent event) {
        ProgramTM programTM = tblProgram.getSelectionModel().getSelectedItem();
        if(programTM != null){
            lblProgramId.setText(programTM.getProgramId());
            txtProName.setText(programTM.getProgramName());
            txtProDesc.setText(programTM.getProgramDescription());
            txtDuration.setText(programTM.getDuration().substring(0,1));
            txtAmount.setText(String.valueOf(programTM.getCost()));

            // Clear previous selections
            combDuration.getSelectionModel().clearSelection();

            *//*String durationType = programTM.getDuration().substring(3);
            if (durationType != null && !durationType.isEmpty()) {
                 if (combDuration.getItems().contains(durationType)) {
                     combDuration.getSelectionModel().select(durationType);
                 }
            }*//*

            // Extract duration (number and type)
            String[] durationParts = programTM.getDuration().split(" ", 2);
            if (durationParts.length == 2) {
                txtDuration.setText(durationParts[0]); // Number part
                combDuration.getSelectionModel().select(durationParts[1]); // Type part
            } else {
                txtDuration.clear();
                combDuration.getSelectionModel().clearSelection();
            }


            btnSave.setDisable(true);
            btnDelete.setDisable(false);
            btnUpdate.setDisable(false);
            btnSearch.setDisable(true);
            //btnSendMail.setDisable(false);
        }
    }*/
}
