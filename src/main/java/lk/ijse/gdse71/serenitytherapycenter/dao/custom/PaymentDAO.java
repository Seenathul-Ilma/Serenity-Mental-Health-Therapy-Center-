package lk.ijse.gdse71.serenitytherapycenter.dao.custom;

import lk.ijse.gdse71.serenitytherapycenter.dao.CrudDAO;
import lk.ijse.gdse71.serenitytherapycenter.entity.Payment;
import org.hibernate.Session;

import java.util.Optional;

public interface PaymentDAO extends CrudDAO<Payment, String> {
    boolean save(Session session, Payment payment);
    Optional<Object> findBySessionId(String sessionId);

    double getTotalPaidAmountByEnrollmentId(String registrationId);
}
