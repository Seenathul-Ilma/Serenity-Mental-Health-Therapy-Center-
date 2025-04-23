package lk.ijse.gdse71.serenitytherapycenter.bo.custom;

import lk.ijse.gdse71.serenitytherapycenter.bo.SuperBO;
import lk.ijse.gdse71.serenitytherapycenter.dto.PatientDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * --------------------------------------------
 * Author: Zeenathul Ilma
 * GitHub: https://github.com/Seenathul-Ilma
 * Website: https://ilma.live
 * --------------------------------------------
 * Created: 3/20/2025 2:39 PM
 * Project: MobileZone
 * --------------------------------------------
 **/

public interface PatientBO extends SuperBO {

    List<String> getAllPatientIds() throws SQLException, ClassNotFoundException;

    boolean savePatient(PatientDTO patientDTO) throws SQLException, ClassNotFoundException;

    boolean updatePatient(PatientDTO patientDTO) throws SQLException, ClassNotFoundException;

    boolean deletePatient(String patientId) throws SQLException, ClassNotFoundException;

    List<PatientDTO> getAllPatients() throws SQLException, ClassNotFoundException;

    Optional<String> getNextPatientId() throws SQLException, ClassNotFoundException;

    //Optional<PatientDTO> findByPatientId(String selectedPatientId) throws SQLException, ClassNotFoundException;
    PatientDTO findByPatientId(String selectedPatientId) throws SQLException, ClassNotFoundException;

    String getPatientNameById(String patientId);
}
