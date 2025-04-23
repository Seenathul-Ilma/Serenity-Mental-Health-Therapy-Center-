package lk.ijse.gdse71.serenitytherapycenter.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import lk.ijse.gdse71.serenitytherapycenter.bo.BOFactory;
import lk.ijse.gdse71.serenitytherapycenter.bo.custom.LogInCredentialsBO;
import lk.ijse.gdse71.serenitytherapycenter.dto.LogInCredentialsDTO;
import lk.ijse.gdse71.serenitytherapycenter.dto.tm.LogInCredentialTM;
import org.mindrot.jbcrypt.BCrypt;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;

public class LogInCredentialController implements Initializable {
    static String generatedVerificationCode;
    @FXML
    private AnchorPane pageLogInCred;

    @FXML
    private ComboBox<String> cmbUserRole;

    @FXML
    private VBox VboxCredentialDetails;


    @FXML
    private Button btnDelete;

    @FXML
    private Button btnReset;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private TableColumn<LogInCredentialTM, String> colEmail;

    @FXML
    private TableColumn<LogInCredentialTM, String> colRole;

    @FXML
    private TableColumn<LogInCredentialTM, String> colUserId;

    @FXML
    private TableColumn<LogInCredentialTM, String> colUsername;

    @FXML
    private Label lblUserId;

    @FXML
    private TableView<LogInCredentialTM> tblCredential;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtUsername;

    //LogInCredentialModel logInCredentialsModel = new LogInCredentialModel();
    //LogInCredentialsDAOImpl logInCredentialsDAO = new LogInCredentialsDAOImpl();
    //LogInCredentialsDAO logInCredentialsDAO = new LogInCredentialsDAOImpl();
    //private final LogInCredentialsBOImpl logInCredentialsBO = new LogInCredentialsBOImpl();
    //private final LogInCredentialsBO logInCredentialsBO = new LogInCredentialsBOImpl();
    LogInCredentialsBO logInCredentialsBO = (LogInCredentialsBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.USER);


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colUserId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        //colPassword.setCellValueFactory(new PropertyValueFactory<>("password"));

        try {
            if(VboxCredentialDetails.isVisible()){
                VboxCredentialDetails.setVisible(true);
            }
            if(tblCredential.isVisible()){
                tblCredential.setVisible(true);
            }
            refreshPage();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load user Id").show();
        }
    }

    @FXML
    void btnDeleteCredentialsOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String userId = lblUserId.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to delete this user credential?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> optionalButtonType = alert.showAndWait();

        if(optionalButtonType.isPresent() && optionalButtonType.get() == ButtonType.YES){

            //boolean isDeleted = logInCredentialsModel.deleteCredentials(userId);
            //boolean isDeleted = logInCredentialsDAO.delete(userId);
            boolean isDeleted = logInCredentialsBO.deleteCredential(userId);

            if(isDeleted){
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION, "User credential deleted successfully!").show();
            } else{
                new Alert(Alert.AlertType.ERROR, "Failed to delete credential!").show();
            }
        }
    }

    @FXML
    void btnResetOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        refreshPage();
    }

    @FXML
    void btnSaveCredentialsOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String userId = lblUserId.getText();
        String email = txtEmail.getText();
        //String role = txtUserRole.getText();
        String role = cmbUserRole.getSelectionModel().getSelectedItem();
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        resetStyles();

        String userNamePattern = "^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){3,18}[a-zA-Z0-9]$";
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        String emailPattern = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

        boolean isValidUserName = username.matches(userNamePattern);
        boolean isValidPassword = password.matches(passwordPattern);
        boolean isValidEmail = email.matches(emailPattern);

        if (!isValidUserName) {
            txtUsername.setStyle(txtUsername.getStyle() + ";-fx-border-color: red;");
            System.out.println("Invalid Username!");
            return;
        }

        if (!isValidPassword) {
            txtPassword.setStyle(txtPassword.getStyle() + ";-fx-border-color: red;");
            System.out.println("Invalid Password!");
            return;
        }

        if (!isValidEmail) {
            txtEmail.setStyle(txtEmail.getStyle() + ";-fx-border-color: red;");
            System.out.println("Invalid Email!");
            return;
        }

        //String existUsernamePassword = logInCredentialsModel.isExistUsernameAndPassword(username, password);
        //String existUsernamePassword = logInCredentialsDAO.isExistUsernameAndPassword(username, password);
        String existUsername = logInCredentialsBO.isExistUsername(username);

        if(!existUsername.equals("")){
            new Alert(Alert.AlertType.ERROR, "Username already exists!\nTry another username!").show();
            return;
        }

        if (isValidPassword && isValidUserName && isValidEmail) {
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            LogInCredentialsDTO credentialDTO = new LogInCredentialsDTO(
                    userId,
                    role,
                    email,
                    username,
                    //password
                    hashedPassword
            );

            System.out.println("DTO UserId: "+userId+" "+
                    "Role: "+role+" "+
                    "Email: "+email+" "+
                    "Username: "+username);

            System.out.println("Hashed Password: " + hashedPassword);

            //boolean isSaved = logInCredentialsModel.saveCredentials(credentialDTO);
            //boolean isSaved = logInCredentialsDAO.save(credentialDTO);
            boolean isSaved = logInCredentialsBO.saveCredential(credentialDTO);

            if (isSaved) {
                refreshPage();
                resetStyles();
                new Alert(Alert.AlertType.INFORMATION, "Credentials saved successfully!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to save credentials...!").show();
            }
        }
    }

    private void refreshPage() throws SQLException, ClassNotFoundException {

        loadNextUserId();
        loadTableData();
        loadRoles();

        btnSave.setDisable(false);

        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
        btnReset.setDisable(false);

        cmbUserRole.getSelectionModel().clearSelection();
        txtUsername.setText("");
        txtPassword.setDisable(false);
        txtPassword.setText("");
        txtEmail.setText("");
    }

    private void loadTableData() throws SQLException, ClassNotFoundException {
        //ArrayList<LogInCredentialsDTO> logInCredentialsDTOS = logInCredentialsModel.getAllCredentials();
        //ArrayList<LogInCredentialsDTO> logInCredentialsDTOS = logInCredentialsDAO.getAll();
        List<LogInCredentialsDTO> logInCredentialsDTOS = logInCredentialsBO.getAllCredentials();

        ObservableList<LogInCredentialTM> logInCredentialTMS = FXCollections.observableArrayList();

        for(LogInCredentialsDTO logInCredentialsDTO : logInCredentialsDTOS){
            LogInCredentialTM logInCredentialTM = new LogInCredentialTM(
                    logInCredentialsDTO.getUserId(),
                    logInCredentialsDTO.getRole(),
                    logInCredentialsDTO.getEmail(),
                    logInCredentialsDTO.getUsername()
                    //logInCredentialsDTO.getPassword()
            );
            System.out.println("TM UserId: "+logInCredentialsDTO.getUserId()+" "+
                    "Role: "+logInCredentialsDTO.getRole()+" "+
                    "Email: "+logInCredentialsDTO.getEmail()+" "+
                    "Username: "+logInCredentialsDTO.getUsername());
            logInCredentialTMS.add(logInCredentialTM);
        }
        tblCredential.setItems(logInCredentialTMS);
    }

    private void loadNextUserId() throws SQLException, ClassNotFoundException {
        //String nextUserId = logInCredentialsModel.getNextUserId();
        //String nextUserId = logInCredentialsDAO.getNextId();

        Optional<String> nextUserIdOptional = logInCredentialsBO.getNextUserId();  // Fetch Optional<String> from business logic
        // Unwrap Optional value before setting in UI (text field)
        String nextUserId = nextUserIdOptional.orElse("US001");  // If empty, use default "US001"
        lblUserId.setText(nextUserId); // Set the actual customer ID in the text field
    }

    @FXML
    void btnUpdateCredentialsOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        txtPassword.setDisable(true);
        String userId = lblUserId.getText();
        String email = txtEmail.getText();
        String role = cmbUserRole.getSelectionModel().getSelectedItem();
        String username = txtUsername.getText();
        //String password = txtPassword.getText();

        resetStyles();
        //txtPassword.setStyle(txtPassword.getStyle() + ";-fx-border-color: #7367F0;");

        String userNamePattern = "^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){3,18}[a-zA-Z0-9]$";
        String emailPattern = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        //String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";

        boolean isValidUserName = username.matches(userNamePattern);
        boolean isValidEmail = email.matches(emailPattern);
        //boolean isValidPassword = password.matches(passwordPattern);

        if (!isValidUserName) {
            txtUsername.setStyle(txtUsername.getStyle() + ";-fx-border-color: red;");
            System.out.println("Invalid Username!");
            return;
        }

        if (!isValidEmail) {
            txtEmail.setStyle(txtEmail.getStyle() + ";-fx-border-color: red;");
            System.out.println("Invalid Email!");
            return;
        }

        /*if (!isValidPassword) {
            txtPassword.setStyle(txtPassword.getStyle() + ";-fx-border-color: red;");
            System.out.println("Invalid Password!");
            return;
        }*/

        // Check if the username already exists in the database
        /*String existingUserId = logInCredentialsBO.isExistUsername(username);
        System.out.println("Existing User ID: " + existingUserId);
        if (!existingUserId.equals(userId)) {
            new Alert(Alert.AlertType.ERROR, "Username already exists! Please try another username.").show();
            return;
        }*/

        //String existUsernamePassword = logInCredentialsBO.isExistUsernameAndPassword(username, password);

        /*if(!existUsernamePassword.equals(userId)) {
            new Alert(Alert.AlertType.ERROR, "Already existed username and password...!\nTry another username and password!").show();
            return;
        }*/

        String storedHashedPassword = logInCredentialsBO.getPasswordByUserId(userId);

        if (storedHashedPassword == null) {
            new Alert(Alert.AlertType.ERROR, "User not found!").show();
            return;
        }

        //if (isValidPassword && isValidUserName) {
        if (isValidEmail && isValidUserName) {
            // Save updated credentials
            LogInCredentialsDTO credentialDTO = new LogInCredentialsDTO(
                    userId,
                    role,
                    email,
                    username,
                    storedHashedPassword
            );

            boolean isUpdated = logInCredentialsBO.updateCredential(credentialDTO);

            if (isUpdated) {
                refreshPage();
                resetStyles();
                new Alert(Alert.AlertType.INFORMATION, "Credentials updated successfully!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to update credentials...!").show();
            }
        }
    }

    private void resetStyles() {
        txtUsername.setStyle(txtUsername.getStyle() + ";-fx-border-color: #7367F0;");
        txtEmail.setStyle(txtEmail.getStyle() + ";-fx-border-color: #7367F0;");
        txtPassword.setStyle(txtPassword.getStyle() + ";-fx-border-color: #7367F0;");
    }


    @FXML
    void onClickTable(MouseEvent event) {
        LogInCredentialTM logInCredentialTM = tblCredential.getSelectionModel().getSelectedItem();
        if(logInCredentialTM != null){
            lblUserId.setText(logInCredentialTM.getUserId());
            cmbUserRole.setValue(logInCredentialTM.getRole());
            txtUsername.setText(logInCredentialTM.getUsername());
            txtEmail.setText(logInCredentialTM.getEmail());
            txtPassword.setDisable(true);
            //txtPassword.setText(logInCredentialTM.getPassword());

            btnSave.setDisable(true);
            btnDelete.setDisable(false);
            btnUpdate.setDisable(false);
        }
    }

    @FXML
    void ReceiveEmailOnAction(ActionEvent event) {
    }

    private void sendEmailWithGmail(String from, String recipient, String subject, String body) {
        String PASSWORD = "mtrm qcsm gery orqu"; // Enter ur app password here

        Properties props = new Properties();

        props.put("mail.smtp.auth", "true");

        props.put("mail.smtp.starttls.enable", "true");

        props.put("mail.smtp.host", "smtp.gmail.com");

        props.put("mail.smtp.port", "587");

        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session session = Session.getInstance(props, new Authenticator() {
            // Replace with your email and app password
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(from));

            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));

            message.setSubject(subject);

            message.setText(body);

            Transport.send(message);

            new Alert(Alert.AlertType.INFORMATION, "Verification code sent successfully!").show();
        } catch (MessagingException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to send verification code..").show();
        }
    }

    public void cmbUserRoleOnAction(ActionEvent actionEvent) {
        String selectedRole = cmbUserRole.getSelectionModel().getSelectedItem();
    }

    private void loadRoles(){
        ObservableList<String> roles = FXCollections.observableArrayList(
                "Admin",
                "Receptionist"
        );
        System.out.println("load roles");
        cmbUserRole.setItems(roles);
    }

}



