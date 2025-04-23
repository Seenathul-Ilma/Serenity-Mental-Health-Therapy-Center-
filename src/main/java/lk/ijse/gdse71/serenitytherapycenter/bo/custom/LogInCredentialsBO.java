package lk.ijse.gdse71.serenitytherapycenter.bo.custom;

import lk.ijse.gdse71.serenitytherapycenter.bo.SuperBO;
import lk.ijse.gdse71.serenitytherapycenter.dto.LogInCredentialsDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface LogInCredentialsBO extends SuperBO {
    List<String> getAllUserIds() throws SQLException, ClassNotFoundException;

    String getAdminPassword() throws SQLException, ClassNotFoundException;

    boolean deleteCredential(String userId) throws SQLException, ClassNotFoundException;

    String isExistUsername(String username) throws SQLException, ClassNotFoundException;

    boolean saveCredential(LogInCredentialsDTO credentialDTO) throws SQLException, ClassNotFoundException;

    List<LogInCredentialsDTO> getAllCredentials() throws SQLException, ClassNotFoundException;

    Optional<String> getNextUserId() throws SQLException, ClassNotFoundException;

    boolean updateCredential(LogInCredentialsDTO credentialDTO) throws SQLException, ClassNotFoundException;

    String getPasswordByUserId(String userId);
}
