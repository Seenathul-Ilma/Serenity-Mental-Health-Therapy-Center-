package lk.ijse.gdse71.serenitytherapycenter.bo.custom.Impl;

import lk.ijse.gdse71.serenitytherapycenter.bo.custom.PaymentBO;
import lk.ijse.gdse71.serenitytherapycenter.dao.DAOFactory;
import lk.ijse.gdse71.serenitytherapycenter.dao.custom.PaymentDAO;
import lk.ijse.gdse71.serenitytherapycenter.dto.PaymentDTO;
import lk.ijse.gdse71.serenitytherapycenter.entity.*;

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
        /*ArrayList<PaymentDTO> paymentDTOS = new ArrayList<>();

        List<Payment> payments = paymentDAO.getAll();

        if (payments == null || payments.isEmpty()) {
            System.out.println("No registrations found!");
            return paymentDTOS; // Return empty list if no patients exist
        }

        for(Payment payment : payments){
            paymentDTOS.add(new PaymentDTO(
                    payment.getPaymentId(),
                    payment.getPaymentDate(),
                    payment.getPaymentAmount().doubleValue(),
                    payment.getPaymentType(),
                    payment.getPatientId(),
                    payment.getProgramId(),
                    payment.getEnrollment().getRegistrationId(),
                    payment.getTherapySession().getSessionId()
            ));
        }
        return paymentDTOS;*/

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
        /*TherapySession therapySession = new TherapySession();
        therapySession.setSessionId(paymentDTO.getSessionId());

        Enrollment enrollment = new Enrollment();
        enrollment.setRegistrationId(paymentDTO.getEnrollmentId());

        Payment payment = new Payment();
        payment.setPaymentId(paymentDTO.getPaymentId());
        payment.setPaymentDate(paymentDTO.getPaymentDate());
        payment.setPaymentAmount(BigDecimal.valueOf(paymentDTO.getPaymentAmount()));
        payment.setPaymentType(paymentDTO.getPaymentType());
        payment.setPatientId(paymentDTO.getPatientId());
        payment.setProgramId(paymentDTO.getProgramId());
        //payment.setEnrollment(enrollment);
        payment.setTherapySession(therapySession);

        return paymentDAO.save(payment);*/

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
    public boolean deletePayment(String s) throws SQLException, ClassNotFoundException {
        return false;
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
}
