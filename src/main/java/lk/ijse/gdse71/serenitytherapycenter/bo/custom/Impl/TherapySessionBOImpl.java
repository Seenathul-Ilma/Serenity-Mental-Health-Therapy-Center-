package lk.ijse.gdse71.serenitytherapycenter.bo.custom.Impl;

import lk.ijse.gdse71.serenitytherapycenter.bo.custom.TherapySessionBO;
import lk.ijse.gdse71.serenitytherapycenter.config.FactoryConfiguration;
import lk.ijse.gdse71.serenitytherapycenter.dao.DAOFactory;
import lk.ijse.gdse71.serenitytherapycenter.dao.custom.PaymentDAO;
import lk.ijse.gdse71.serenitytherapycenter.dao.custom.TherapySessionDAO;
import lk.ijse.gdse71.serenitytherapycenter.dto.PaymentDTO;
import lk.ijse.gdse71.serenitytherapycenter.dto.TherapySessionDTO;
import lk.ijse.gdse71.serenitytherapycenter.entity.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * --------------------------------------------
 * Author: Zeenathul Ilma
 * GitHub: https://github.com/Seenathul-Ilma
 * Website: https://zeenathulilma.vercel.app/
 * --------------------------------------------
 * Created: 4/22/2025 12:13 PM
 * Project: MobileZone
 * --------------------------------------------
 **/

public class TherapySessionBOImpl implements TherapySessionBO {
    TherapySessionDAO therapySessionDAO = (TherapySessionDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOType.THERAPY_SESSION);
    PaymentDAO paymentDAO = (PaymentDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOType.PAYMENT);
    private final FactoryConfiguration factoryConfiguration = FactoryConfiguration.getInstance();

    @Override
    public Optional<String> getNextSessionId() throws SQLException, ClassNotFoundException {
        return therapySessionDAO.getNextId();
    }

    @Override
    public List<String> getAllSessionIds() throws SQLException, ClassNotFoundException {
        return List.of();
    }

    @Override
    public List<TherapySessionDTO> getAllSessions() throws SQLException, ClassNotFoundException {
        ArrayList<TherapySessionDTO> therapySessionDTOS = new ArrayList<>();

        List<TherapySession> therapySessions = therapySessionDAO.getAll();

        if (therapySessions == null || therapySessions.isEmpty()) {
            System.out.println("No sessions found!");
            return therapySessionDTOS; // Return empty list if no sessions exist
        }

        for(TherapySession therapySession : therapySessions){
            therapySessionDTOS.add(new TherapySessionDTO(
                    therapySession.getSessionId(),
                    therapySession.getRegistrationId(),
                    therapySession.getTherapyProgram().getProgramId(),
                    therapySession.getTherapist().getTherapistId(),
                    therapySession.getPatient().getPatientId(),
                    therapySession.getSessionDate(),
                    therapySession.getSessionStatus()
            ));
        }
        return therapySessionDTOS;
    }

    @Override
    public boolean deleteSession(String sessionId) throws SQLException, ClassNotFoundException {
        return therapySessionDAO.delete(sessionId);
    }

    @Override
    public boolean saveSession(TherapySessionDTO therapySessionDTO) throws SQLException, ClassNotFoundException {
        // Just set patient and program objects with IDs only
        Patient patient = new Patient();
        patient.setPatientId(therapySessionDTO.getPatientId());

        TherapyProgram program = new TherapyProgram();
        program.setProgramId(therapySessionDTO.getProgramId());

        Therapist therapist = new Therapist();
        therapist.setTherapistId(therapySessionDTO.getTherapistId());

        TherapySession therapySession = new TherapySession();
        therapySession.setSessionId(therapySessionDTO.getSessionId());
        therapySession.setRegistrationId(therapySessionDTO.getRegistrationId());
        therapySession.setTherapyProgram(program);
        therapySession.setTherapist(therapist);
        therapySession.setPatient(patient);
        therapySession.setSessionDate(therapySessionDTO.getSessionDate());
        therapySession.setSessionStatus(therapySessionDTO.getSessionStatus());

        return therapySessionDAO.save(therapySession);
    }

    @Override
    public boolean updateSession(TherapySessionDTO therapySessionDTO) throws SQLException, ClassNotFoundException {
        // Just set patient and program objects with IDs only (no fetching from DB)
        Patient patient = new Patient();
        patient.setPatientId(therapySessionDTO.getPatientId());

        TherapyProgram program = new TherapyProgram();
        program.setProgramId(therapySessionDTO.getProgramId());

        Therapist therapist = new Therapist();
        therapist.setTherapistId(therapySessionDTO.getTherapistId());

        TherapySession therapySession = new TherapySession();
        therapySession.setSessionId(therapySessionDTO.getSessionId());
        therapySession.setRegistrationId(therapySessionDTO.getRegistrationId());
        therapySession.setTherapyProgram(program);
        therapySession.setTherapist(therapist);
        therapySession.setPatient(patient);
        therapySession.setSessionDate(therapySessionDTO.getSessionDate());
        therapySession.setSessionStatus(therapySessionDTO.getSessionStatus());

        return therapySessionDAO.update(therapySession);
    }

    @Override
    public TherapySessionDTO findBySessionId(String selectedSessionId) throws SQLException, ClassNotFoundException {
        Optional<TherapySession> therapySessionOptional = therapySessionDAO.findById(selectedSessionId);

        if(therapySessionOptional.isPresent()){
            TherapySession therapySession = therapySessionOptional.get();
            return new TherapySessionDTO(
                    therapySession.getSessionId(),
                    therapySession.getRegistrationId(),
                    therapySession.getTherapyProgram().getProgramId(),
                    therapySession.getTherapist().getTherapistId(),
                    therapySession.getPatient().getPatientId(),
                    therapySession.getSessionDate(),
                    therapySession.getSessionStatus()
            );
        } else {
            return null;
        }
    }

    @Override
    public List<String> checkBookedTherapistsByDate(Date selectedDate) {
        return therapySessionDAO.findByTherapistByDate(selectedDate);
    }

    @Override
    public boolean saveSessionWithPayment(TherapySessionDTO therapySessionDTO, PaymentDTO paymentDTO) {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();

        try {
            // Fetch the session, patient, therapist, and program details from the session
            Patient patient = session.get(Patient.class, therapySessionDTO.getPatientId());
            Therapist therapist = session.get(Therapist.class, therapySessionDTO.getTherapistId());
            TherapyProgram program = session.get(TherapyProgram.class, therapySessionDTO.getProgramId());

            // Validate existence of entities
            if (patient == null || therapist == null || program == null) {
                throw new RuntimeException("Required entities not found: Patient, Therapist, or Program");
            }

            // Create and set TherapySession entity
            TherapySession therapySession = new TherapySession();
            therapySession.setSessionId(therapySessionDTO.getSessionId());
            therapySession.setPatient(patient);
            therapySession.setTherapist(therapist);
            therapySession.setTherapyProgram(program);
            therapySession.setSessionDate(therapySessionDTO.getSessionDate());
            therapySession.setSessionStatus(therapySessionDTO.getSessionStatus());

            // Save TherapySession
            boolean isSessionSaved = therapySessionDAO.save(session, therapySession);
            if (!isSessionSaved) {
                System.err.println("Failed to save therapy session!");
                transaction.rollback();
                return false;
            }

            Enrollment enrollment = session.get(Enrollment.class, paymentDTO.getEnrollmentId());

            // If a session-wise payment exists, save the payment
            if (paymentDTO.getPaymentAmount() > 0) {
                Payment payment = new Payment(
                        paymentDTO.getPaymentId(),
                        paymentDTO.getPatientId(),
                        paymentDTO.getProgramId(),
                        therapySession.getSessionDate(),  // Set session date for payment
                        "Session Payment",
                        BigDecimal.valueOf(paymentDTO.getPaymentAmount()),
                        enrollment,
                        therapySession// Set sessionId for the payment
                );

                // Save Payment
                boolean isPaymentSaved = paymentDAO.save(session, payment);
                if (!isPaymentSaved) {
                    System.err.println("Failed to save payment!");
                    transaction.rollback();
                    return false;
                }
            }

            // Commit transaction after successful operations
            transaction.commit();
            return true;

        } catch (Exception e) {
            // Rollback transaction in case of an error
            transaction.rollback();
            e.printStackTrace();
            return false;
        } finally {
            // Close session to release resources
            session.close();
        }

    }
}
