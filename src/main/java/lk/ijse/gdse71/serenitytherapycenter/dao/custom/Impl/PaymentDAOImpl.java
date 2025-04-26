package lk.ijse.gdse71.serenitytherapycenter.dao.custom.Impl;

import lk.ijse.gdse71.serenitytherapycenter.config.FactoryConfiguration;
import lk.ijse.gdse71.serenitytherapycenter.dao.custom.PaymentDAO;
import lk.ijse.gdse71.serenitytherapycenter.entity.Payment;
import lk.ijse.gdse71.serenitytherapycenter.entity.TherapySession;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * --------------------------------------------
 * Author: Zeenathul Ilma
 * GitHub: https://github.com/Seenathul-Ilma
 * Website: https://ilma.live
 * --------------------------------------------
 * Created: 4/20/2025 11:37 AM
 * Project: MobileZone
 * --------------------------------------------
 **/
 
public class PaymentDAOImpl implements PaymentDAO {
    private final FactoryConfiguration factoryConfiguration = FactoryConfiguration.getInstance();

    @Override
    public List<Payment> getAll() throws SQLException, ClassNotFoundException {
        Session session = factoryConfiguration.getSession();
        Query<Payment> query = session.createQuery("from Payment ", Payment.class);
        List<Payment> paymentList = query.list();
        return paymentList;
    }

    @Override
    public Optional<String> getNextId() throws SQLException, ClassNotFoundException {
        Session session = factoryConfiguration.getSession();

        try {
            String lastPk = session.createQuery(
                    "SELECT p.paymentId FROM Payment p ORDER BY p.paymentId DESC", String.class
            ).setMaxResults(1).uniqueResult();

            if (lastPk != null && lastPk.matches("^PAY\\d+$")) {
                int nextId = Integer.parseInt(lastPk.substring(3)) + 1;
                return Optional.of(String.format("PAY%03d", nextId));
            } else {
                return Optional.of("PAY001");
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean save(Payment paymentEntity) throws SQLException, ClassNotFoundException {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();
        try {

            Payment existPayment = session.get(Payment.class, paymentEntity.getPaymentId());
            if (existPayment != null){
                return false;
            }

            session.persist(paymentEntity);
            transaction.commit();
            return true;
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
            return false;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public boolean delete(String paymentId) throws SQLException, ClassNotFoundException {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            Payment paymentEntity = session.get(Payment.class, paymentId);
            if (paymentEntity == null){
                return false;
            }
            session.remove(paymentEntity);
            transaction.commit();
            return true;
        } catch (Exception e) {
            transaction.rollback();
            return false;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public boolean update(Payment dto) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public List<String> getAllIds() throws SQLException, ClassNotFoundException {
        return List.of();
    }

    @Override
    public Optional<Payment> findById(String id) throws SQLException, ClassNotFoundException {
        return Optional.empty();
    }

    @Override
    public boolean save(Session session, Payment payment) {
        try {
            //if data is already existed ? update : else, save data
            session.merge(payment);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Optional<Object> findBySessionId(String sessionId) {
        Session session = factoryConfiguration.getSession();
        try {
            Query query = session.createQuery("FROM Payment p WHERE p.therapySession.sessionId = :sessionId");
            query.setParameter("sessionId", sessionId);
            List<Payment> list = query.list();

            if (list.isEmpty()) {
                return Optional.empty();
            } else {
                return Optional.of(list.get(0));
            }
        } finally {
            session.close();
        }
    }

    @Override
    public double getTotalPaidAmountByEnrollmentId(String registrationId) {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            Query<BigDecimal> query = session.createQuery(
                    "SELECT COALESCE(SUM(p.paymentAmount), 0.0) FROM Payment p WHERE p.enrollment.id = :id", BigDecimal.class);
            query.setParameter("id", registrationId);

            BigDecimal totalPaid = query.uniqueResult();

            // If the result is null, return 0.0; otherwise, return the BigDecimal value as double
            return totalPaid != null ? totalPaid.doubleValue() : 0.0;
        } finally {
            session.close();
        }
    }
}
