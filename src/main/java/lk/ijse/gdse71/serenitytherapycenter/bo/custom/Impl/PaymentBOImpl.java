package lk.ijse.gdse71.serenitytherapycenter.bo.custom.Impl;

import lk.ijse.gdse71.serenitytherapycenter.bo.custom.PaymentBO;
import lk.ijse.gdse71.serenitytherapycenter.dao.DAOFactory;
import lk.ijse.gdse71.serenitytherapycenter.dao.custom.PaymentDAO;
import lk.ijse.gdse71.serenitytherapycenter.dto.PaymentDTO;
import lk.ijse.gdse71.serenitytherapycenter.entity.Enrollment;
import lk.ijse.gdse71.serenitytherapycenter.entity.Payment;
import lk.ijse.gdse71.serenitytherapycenter.entity.TherapySession;

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
 * Created: 4/20/2025 11:42 AM
 * Project: MobileZone
 * --------------------------------------------
 **/

public class PaymentBOImpl implements PaymentBO {
    PaymentDAO paymentDAO = (PaymentDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOType.PAYMENT);

    @Override
    public List<PaymentDTO> getAllPayments() throws SQLException, ClassNotFoundException {

        ArrayList<PaymentDTO> paymentDTOS = new ArrayList<>();

        List<Payment> payments = paymentDAO.getAll();

        if (payments == null || payments.isEmpty()) {
            System.out.println("No payments found!");
            return paymentDTOS; // Return empty list if no payments exist
        }

        for (Payment payment : payments) {
            String sessionId = null;
            if (payment.getTherapySession() != null) {
                sessionId = payment.getTherapySession().getSessionId();
            }

            paymentDTOS.add(new PaymentDTO(
                    payment.getPaymentId(),
                    payment.getPaymentDate(),
                    payment.getPaymentAmount().doubleValue(),
                    payment.getPaymentType(),
                    payment.getPatientId(),
                    payment.getProgramId(),
                    payment.getEnrollment().getRegistrationId(),
                    sessionId
            ));
        }
        return paymentDTOS;

    }

    @Override
    public Optional<String> getNextPaymentId() throws SQLException, ClassNotFoundException {
        return paymentDAO.getNextId();
    }

    @Override
    public boolean savePayment(PaymentDTO paymentDTO) throws SQLException, ClassNotFoundException {

        Payment payment = new Payment();

        payment.setPaymentId(paymentDTO.getPaymentId());
        payment.setPaymentDate(paymentDTO.getPaymentDate());
        payment.setPaymentAmount(BigDecimal.valueOf(paymentDTO.getPaymentAmount()));
        payment.setPaymentType(paymentDTO.getPaymentType());
        payment.setPatientId(paymentDTO.getPatientId());
        payment.setProgramId(paymentDTO.getProgramId());

        // Set enrollment (required)
        Enrollment enrollment = new Enrollment();
        enrollment.setRegistrationId(paymentDTO.getEnrollmentId());
        payment.setEnrollment(enrollment);

        // Only set session if it's not null
        if (paymentDTO.getSessionId() != null) {
            TherapySession therapySession = new TherapySession();
            therapySession.setSessionId(paymentDTO.getSessionId());
            payment.setTherapySession(therapySession);
        }

        return paymentDAO.save(payment);

    }

    @Override
    public boolean deletePayment(String paymentId) throws SQLException, ClassNotFoundException {
        return paymentDAO.delete(paymentId);
    }

    @Override
    public boolean updatePayment(PaymentDTO dto) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public List<String> getAllPaymentIds() throws SQLException, ClassNotFoundException {
        return List.of();
    }

    @Override
    public PaymentDTO findByPaymentId(String id) throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public double calculateSessionFee(Double upfront, Double programFee, String duration) {
        int sessionCount = 0;
        duration = duration.toLowerCase().trim();

        if (duration.contains("day")) {
            sessionCount = extractNumber(duration) / 7; // 1 session per week
        } else if (duration.contains("week")) {
            sessionCount = extractNumber(duration);
        } else if (duration.contains("month")) {
            sessionCount = extractNumber(duration) * 4;
        } else if (duration.contains("year")) {
            sessionCount = extractNumber(duration) * 12 * 4;
        }

        if (sessionCount > 0) {
            double remainingFee = programFee - upfront;
            return remainingFee / sessionCount;
        } else {
            throw new IllegalArgumentException("Invalid duration format!");
        }
    }

    @Override
    public Optional<Object> findBySessionId(String sessionId) {
        return paymentDAO.findBySessionId(sessionId);
    }

    @Override
    public double getTotalPaidAmountForEnrollment(String registrationId) {
        return paymentDAO.getTotalPaidAmountByEnrollmentId(registrationId);
    }

    private int extractNumber(String duration) {
        return Integer.parseInt(duration.replaceAll("\\D+", ""));
    }
}
