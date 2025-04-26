package lk.ijse.gdse71.serenitytherapycenter.bo.custom.Impl;

import lk.ijse.gdse71.serenitytherapycenter.bo.custom.RegistrationBO;
import lk.ijse.gdse71.serenitytherapycenter.config.FactoryConfiguration;
import lk.ijse.gdse71.serenitytherapycenter.dao.DAOFactory;
import lk.ijse.gdse71.serenitytherapycenter.dao.custom.EnrollmentDAO;
import lk.ijse.gdse71.serenitytherapycenter.dao.custom.PaymentDAO;
import lk.ijse.gdse71.serenitytherapycenter.dto.EnrollmentDTO;
import lk.ijse.gdse71.serenitytherapycenter.dto.PaymentDTO;
import lk.ijse.gdse71.serenitytherapycenter.entity.Enrollment;
import lk.ijse.gdse71.serenitytherapycenter.entity.Patient;
import lk.ijse.gdse71.serenitytherapycenter.entity.Payment;
import lk.ijse.gdse71.serenitytherapycenter.entity.TherapyProgram;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.math.BigDecimal;
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
    PaymentDAO paymentDAO = (PaymentDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOType.PAYMENT);
    private final FactoryConfiguration factoryConfiguration = FactoryConfiguration.getInstance();

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
                    enrollment.getEnrollmentStatus(),
                    enrollment.getRegistrationFee().doubleValue()
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
        enrollment.setRegistrationFee(BigDecimal.valueOf(enrollmentDTO.getRegistrationFee()));
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
        enrollment.setRegistrationFee(BigDecimal.valueOf(enrollmentDTO.getRegistrationFee()));
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
            double registrationFee = enrollment.getRegistrationFee().doubleValue();
            return new EnrollmentDTO(
                    enrollment.getRegistrationId(),
                    enrollment.getTherapyProgram().getProgramId(),
                    enrollment.getPatient().getPatientId(),
                    enrollment.getEnrollmentDate(),
                    enrollment.getEnrollmentStatus(),
                    registrationFee
            );
        } else {
            return null;
        }

    }

    @Override
    public boolean saveRegistrationWithPayment(EnrollmentDTO enrollmentDTO, PaymentDTO paymentDTO) throws SQLException, ClassNotFoundException {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();

        try {
            // Check if registration already exists
            Optional<Enrollment> existingReg = enrollmentDAO.findById(enrollmentDTO.getRegistrationId());
            if (existingReg.isPresent()) {
                transaction.rollback();
                return false;
            }

            Patient patient = session.get(Patient.class, enrollmentDTO.getPatientId());
            TherapyProgram program = session.get(TherapyProgram.class, enrollmentDTO.getProgramId());

            // If patient or program doesn't exist, throw exception or return false
            if (patient == null || program == null) {
                throw new RuntimeException("Patient or TherapyProgram not found");
            }

            // Create and set Enrollment entity
            Enrollment enrollment = new Enrollment();
            enrollment.setRegistrationId(enrollmentDTO.getRegistrationId());
            enrollment.setEnrollmentDate(enrollmentDTO.getEnrollmentDate());
            enrollment.setEnrollmentStatus(enrollmentDTO.getEnrollmentStatus());
            enrollment.setRegistrationFee(BigDecimal.valueOf(enrollmentDTO.getRegistrationFee()));
            enrollment.setPatient(patient);
            enrollment.setTherapyProgram(program);

            // Save Enrollment
            boolean isEnrollmentSaved = enrollmentDAO.save(session, enrollment);
            if (!isEnrollmentSaved) {
                System.err.println("Failed to save enrollment!");
                transaction.rollback();
                return false;
            }

            // Create Payment if upfront payment exists
            if (enrollmentDTO.getRegistrationFee() > 0) {
                System.out.println("Saving payment...");
                Payment payment = new Payment(
                        paymentDTO.getPaymentId(),
                        enrollment.getPatient().getPatientId(),
                        enrollment.getTherapyProgram().getProgramId(),
                        enrollment.getEnrollmentDate(),
                        "Upfront Payment",
                        BigDecimal.valueOf(enrollmentDTO.getRegistrationFee()),
                        enrollment,
                        null // sessionId is null
                );

                boolean isPaymentSaved = paymentDAO.save(session, payment);
                if (!isPaymentSaved) {
                    System.err.println(" Failed to save payment!");
                    transaction.rollback();
                    return false;
                }
            }

            transaction.commit();
            return true;
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public List<String> getPendingRegistrationIds() {
        List<String> ids = enrollmentDAO.getPendingIds();
        List<String> enrollIds = new ArrayList<>();

        for(String enrollId : ids){
            enrollIds.add(enrollId);
        }
        return enrollIds;
    }

    @Override
    public boolean updateEnrollmentStatus(String registrationId, String newStatus) {
        return enrollmentDAO.updateEnrollmentStatus(registrationId, newStatus);
    }

}
