package lk.ijse.gdse71.serenitytherapycenter.bo.custom.Impl;

import lk.ijse.gdse71.serenitytherapycenter.bo.custom.RegistrationBO;
import lk.ijse.gdse71.serenitytherapycenter.dao.DAOFactory;
import lk.ijse.gdse71.serenitytherapycenter.dao.custom.EnrollmentDAO;
import lk.ijse.gdse71.serenitytherapycenter.dto.EnrollmentDTO;
import lk.ijse.gdse71.serenitytherapycenter.entity.Enrollment;
import lk.ijse.gdse71.serenitytherapycenter.entity.Patient;
import lk.ijse.gdse71.serenitytherapycenter.entity.TherapyProgram;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * --------------------------------------------
 * Author: Zeenathul Ilma
 * GitHub: https://github.com/Seenathul-Ilma
 * Website: https://ilma.live
 * --------------------------------------------
 * Created: 4/20/2025 11:34 AM
 * Project: MobileZone
 * --------------------------------------------
 **/

public class RegistrationBOImpl implements RegistrationBO {

    EnrollmentDAO enrollmentDAO = (EnrollmentDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOType.ENROLLMENT);

    @Override
    public List<EnrollmentDTO> getAllRegistrations() throws SQLException, ClassNotFoundException {
        ArrayList<EnrollmentDTO> enrollmentDTOS = new ArrayList<>();

        List<Enrollment> enrollments = enrollmentDAO.getAll();

        if (enrollments == null || enrollments.isEmpty()) {
            System.out.println("No registrations found!");
            return enrollmentDTOS; // Return empty list if no patients exist
        }

        for(Enrollment enrollment : enrollments){
            enrollmentDTOS.add(new EnrollmentDTO(
                    enrollment.getRegistrationId(),
                    enrollment.getTherapyProgram().getProgramId(),
                    enrollment.getPatient().getPatientId(),
                    enrollment.getEnrollmentDate(),
                    enrollment.getEnrollmentStatus()
            ));
        }
        return enrollmentDTOS;
    }

    @Override
    public Optional<String> getNextRegisterId() throws SQLException, ClassNotFoundException {
        return enrollmentDAO.getNextId();
    }

    @Override
    public boolean saveRegistration(EnrollmentDTO enrollmentDTO) throws SQLException, ClassNotFoundException {
        // Just set patient and program objects with IDs only (no fetching from DB)
        Patient patient = new Patient();
        patient.setPatientId(enrollmentDTO.getPatientId());

        TherapyProgram program = new TherapyProgram();
        program.setProgramId(enrollmentDTO.getProgramId());

        Enrollment enrollment = new Enrollment();
        enrollment.setRegistrationId(enrollmentDTO.getRegistrationId());
        enrollment.setEnrollmentDate(enrollmentDTO.getEnrollmentDate());
        enrollment.setEnrollmentStatus(enrollmentDTO.getEnrollmentStatus());
        enrollment.setPatient(patient);
        enrollment.setTherapyProgram(program);

        return enrollmentDAO.save(enrollment);
    }


    @Override
    public boolean deleteRegistration(String registerId) throws SQLException, ClassNotFoundException {
        return enrollmentDAO.delete(registerId);
    }

    @Override
    public boolean updateRegistration(EnrollmentDTO enrollmentDTO) throws SQLException, ClassNotFoundException {
        // Just set patient and program objects with IDs only (no fetching from DB)
        Patient patient = new Patient();
        patient.setPatientId(enrollmentDTO.getPatientId());

        TherapyProgram program = new TherapyProgram();
        program.setProgramId(enrollmentDTO.getProgramId());

        Enrollment enrollment = new Enrollment();
        enrollment.setRegistrationId(enrollmentDTO.getRegistrationId());
        enrollment.setEnrollmentDate(enrollmentDTO.getEnrollmentDate());
        enrollment.setEnrollmentStatus(enrollmentDTO.getEnrollmentStatus());
        enrollment.setPatient(patient);
        enrollment.setTherapyProgram(program);

        return enrollmentDAO.update(enrollment);
    }

    @Override
    public List<String> getAllRegistrationIds() throws SQLException, ClassNotFoundException {
        return List.of();
    }

    @Override
    public EnrollmentDTO findByRegistrationId(String selectedRegisterId) throws SQLException, ClassNotFoundException {
        Optional<Enrollment> enrollmentOptional = enrollmentDAO.findById(selectedRegisterId);

        if(enrollmentOptional.isPresent()){
            Enrollment enrollment = enrollmentOptional.get();
            return new EnrollmentDTO(
                    enrollment.getRegistrationId(),
                    enrollment.getTherapyProgram().getProgramId(),
                    enrollment.getPatient().getPatientId(),
                    enrollment.getEnrollmentDate(),
                    enrollment.getEnrollmentStatus()
            );
        } else {
            return null;
        }

    }
}
