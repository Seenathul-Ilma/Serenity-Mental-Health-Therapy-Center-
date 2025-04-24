package lk.ijse.gdse71.serenitytherapycenter.dao.custom.Impl;

import lk.ijse.gdse71.serenitytherapycenter.config.FactoryConfiguration;
import lk.ijse.gdse71.serenitytherapycenter.dao.custom.EnrollmentDAO;
import lk.ijse.gdse71.serenitytherapycenter.entity.Enrollment;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * --------------------------------------------
 * Author: Zeenathul Ilma
 * GitHub: https://github.com/Seenathul-Ilma
 * Website: https://ilma.live
 * --------------------------------------------
 * Created: 4/20/2025 11:36 AM
 * Project: MobileZone
 * --------------------------------------------
 **/
 
public class EnrollmentDAOImpl implements EnrollmentDAO {
    private final FactoryConfiguration factoryConfiguration = FactoryConfiguration.getInstance();

    @Override
    public List<Enrollment> getAll() throws SQLException, ClassNotFoundException {
        Session session = factoryConfiguration.getSession();
        Query<Enrollment> query = session.createQuery("from Enrollment ", Enrollment.class);
        List<Enrollment> enrollmentList = query.list();
        return enrollmentList;
    }

    @Override
    public Optional<String> getNextId() throws SQLException, ClassNotFoundException {
        Session session = factoryConfiguration.getSession();

        try {
            String lastPk = session.createQuery(
                    "SELECT e.registrationId FROM Enrollment e ORDER BY e.registrationId DESC", String.class
            ).setMaxResults(1).uniqueResult();

            if (lastPk != null && lastPk.matches("^REG\\d+$")) {
                int nextId = Integer.parseInt(lastPk.substring(3)) + 1;
                return Optional.of(String.format("REG%03d", nextId));
            } else {
                return Optional.of("REG001");
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean save(Enrollment enrollmentEntity) throws SQLException, ClassNotFoundException {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();
        try {

            Enrollment existEnroll = session.get(Enrollment.class, enrollmentEntity.getRegistrationId());
            if (existEnroll != null){
                return false;
            }

            session.persist(enrollmentEntity);
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
    public boolean delete(String registrationId) throws SQLException, ClassNotFoundException {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            Enrollment enrollEntity = session.get(Enrollment.class, registrationId);
            if (enrollEntity == null){
                return false;
            }
            session.remove(enrollEntity);
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
    public boolean update(Enrollment enrollmentEntity) throws SQLException, ClassNotFoundException {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();
        try {

            session.merge(enrollmentEntity);
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
    public List<String> getAllIds() throws SQLException, ClassNotFoundException {
        return List.of();
    }

    @Override
    public Optional<Enrollment> findById(String selectedEnrollId) throws SQLException, ClassNotFoundException {
        Session session = factoryConfiguration.getSession();
        Enrollment enrollEntity = session.get(Enrollment.class, selectedEnrollId);
        session.close();
        if (enrollEntity == null) {
            return Optional.empty();
        }
        return Optional.of(enrollEntity);
    }

    @Override
    public boolean save(Session session, Enrollment enrollment) {
        try {
            //if data is already existed ? update : else, save data
            session.merge(enrollment);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<String> getPendingIds() {
        Session session = factoryConfiguration.getSession();
        Query<String> query = session.createQuery(
                "select e.registrationId from Enrollment e where e.enrollmentStatus = :status order by e.registrationId ASC",
                String.class
        );
        query.setParameter("status", "Ongoing");
        List<String> idList = query.list();
        return idList;
    }
}
