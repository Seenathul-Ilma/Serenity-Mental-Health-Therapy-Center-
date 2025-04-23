package lk.ijse.gdse71.serenitytherapycenter.bo.custom.Impl;

import lk.ijse.gdse71.serenitytherapycenter.bo.custom.LogInCredentialsBO;
import lk.ijse.gdse71.serenitytherapycenter.dao.DAOFactory;
import lk.ijse.gdse71.serenitytherapycenter.dao.custom.LogInCredentialsDAO;
import lk.ijse.gdse71.serenitytherapycenter.dto.LogInCredentialsDTO;
import lk.ijse.gdse71.serenitytherapycenter.entity.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LogInCredentialsBOImpl implements LogInCredentialsBO {
    //LogInCredentialsDAO logInCredentialsDAO = new LogInCredentialsDAOImpl();
    LogInCredentialsDAO logInCredentialsDAO = (LogInCredentialsDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOType.USER);

    @Override
    public List<LogInCredentialsDTO> getAllCredentials() throws SQLException, ClassNotFoundException {
        List<LogInCredentialsDTO> logInCredentialsDTOS = new ArrayList<>();

        //ArrayList<LogInCredentialsDTO> logInCredentials = logInCredentialsDAO.getAll();
        List<User> logInCredentials = logInCredentialsDAO.getAll();

        //for (LogInCredentialsDTO logInCredentialsDTO : logInCredentials) {
        for (User logInCredential : logInCredentials) {
            logInCredentialsDTOS.add(new LogInCredentialsDTO(
                    //logInCredentialsDTO.getUserId(),
                    logInCredential.getUserId(),
                    logInCredential.getRole(),
                    logInCredential.getEmail(),
                    logInCredential.getUsername(),
                    logInCredential.getPassword()
            ));
        }
        return logInCredentialsDTOS;
    }

    @Override
    public boolean saveCredential(LogInCredentialsDTO credentialDTO) throws SQLException, ClassNotFoundException {
        //return logInCredentialsDAO.save(new LogInCredentialsDTO(
        return logInCredentialsDAO.save(new User(
                credentialDTO.getUserId(),
                credentialDTO.getRole(),
                credentialDTO.getEmail(),
                credentialDTO.getUsername(),
                credentialDTO.getPassword()
        ));
    }

    @Override
    public Optional<String> getNextUserId() throws SQLException, ClassNotFoundException {
        return logInCredentialsDAO.getNextId();
    }

    @Override
    public boolean deleteCredential(String userId) throws SQLException, ClassNotFoundException {
        return logInCredentialsDAO.delete(userId);
    }

    @Override
    public boolean updateCredential(LogInCredentialsDTO credentialDTO) throws SQLException, ClassNotFoundException {
        //return logInCredentialsDAO.update(new LogInCredentialsDTO(
        return logInCredentialsDAO.update(new User(
                credentialDTO.getUserId(),
                credentialDTO.getRole(),
                credentialDTO.getEmail(),
                credentialDTO.getUsername(),
                credentialDTO.getPassword()
        ));
    }

    @Override
    public String getPasswordByUserId(String userId) {
        return logInCredentialsDAO.getPasswordByUserId(userId);
    }

    @Override
    public String getAdminPassword() throws SQLException, ClassNotFoundException {
        return logInCredentialsDAO.getAdminPassword();
    }

    @Override
    public List<String> getAllUserIds() throws SQLException, ClassNotFoundException {
        List<String> users =  logInCredentialsDAO.getAllIds();
        List<String> userIds = new ArrayList<>();

        for (String userId : users) {
            userIds.add(userId);
        }
        return userIds;
    }

    @Override
    public String isExistUsername(String username) throws SQLException {
        return logInCredentialsDAO.isExistUsername(username);
    }
}
