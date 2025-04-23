package lk.ijse.gdse71.serenitytherapycenter.dao.custom;

import lk.ijse.gdse71.serenitytherapycenter.dao.CrudDAO;
import lk.ijse.gdse71.serenitytherapycenter.entity.User;

import java.sql.SQLException;

//public interface LogInCredentialsDAO extends CrudDAO<LogInCredentialsDTO> {
public interface LogInCredentialsDAO extends CrudDAO<User, String> {
    String getAdminPassword() throws SQLException, ClassNotFoundException;
    String isExistUsernameAndPassword(String username, String password) throws SQLException, ClassNotFoundException;
    String isExistUsername(String username) throws SQLException;
    String getPasswordByUserId(String userId);
}
