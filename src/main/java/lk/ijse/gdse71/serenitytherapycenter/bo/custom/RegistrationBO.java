package lk.ijse.gdse71.serenitytherapycenter.bo.custom;

import lk.ijse.gdse71.serenitytherapycenter.bo.SuperBO;
import lk.ijse.gdse71.serenitytherapycenter.dto.EnrollmentDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface RegistrationBO extends SuperBO {

    List<EnrollmentDTO> getAllRegistrations() throws SQLException, ClassNotFoundException;

    Optional<String> getNextRegisterId() throws SQLException, ClassNotFoundException;

    boolean saveRegistration(EnrollmentDTO dto) throws SQLException, ClassNotFoundException;

    boolean deleteRegistration(String s) throws SQLException, ClassNotFoundException;

    boolean updateRegistration(EnrollmentDTO dto) throws SQLException, ClassNotFoundException;

    List<String> getAllRegistrationIds() throws SQLException, ClassNotFoundException;

    EnrollmentDTO findByRegistrationId(String id) throws SQLException, ClassNotFoundException;

}
