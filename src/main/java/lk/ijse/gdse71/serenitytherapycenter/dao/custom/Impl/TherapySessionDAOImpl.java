package lk.ijse.gdse71.serenitytherapycenter.dao.custom.Impl;

import lk.ijse.gdse71.serenitytherapycenter.config.FactoryConfiguration;
import lk.ijse.gdse71.serenitytherapycenter.dao.custom.TherapySessionDAO;
import lk.ijse.gdse71.serenitytherapycenter.entity.TherapySession;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * --------------------------------------------
 * Author: Zeenathul Ilma
 * GitHub: https://github.com/Seenathul-Ilma
 * Website: https://ilma.live
 * --------------------------------------------
 * Created: 4/19/2025 11:48 AM
 * Project: MobileZone
 * --------------------------------------------
 **/

public class TherapySessionDAOImpl implements TherapySessionDAO {
    private final FactoryConfiguration factoryConfiguration = FactoryConfiguration.getInstance();

    @Override
    public List<TherapySession> getAll() throws SQLException, ClassNotFoundException {
        Session session = factoryConfiguration.getSession();
        Query<TherapySession> query = session.createQuery("from TherapySession ", TherapySession.class);
        List<TherapySession> therapySessionList = query.list();
        return therapySessionList;
    }

    @Override
    public Optional<String> getNextId() throws SQLException, ClassNotFoundException {
        Session session = factoryConfiguration.getSession();

        try {
            String lastPk = session.createQuery(
                    "SELECT ts.sessionId FROM TherapySession ts ORDER BY ts.sessionId DESC", String.class
            ).setMaxResults(1).uniqueResult();

            if (lastPk != null && lastPk.matches("^TSE\\d+$")) {
                int nextId = Integer.parseInt(lastPk.substring(3)) + 1;
                return Optional.of(String.format("TSE%03d", nextId));
            } else {
                return Optional.of("TSE001");
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean save(TherapySession entity) throws SQLException, ClassNotFoundException {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();
        try {

            TherapySession existSession = session.get(TherapySession.class, entity.getSessionId());
            if (existSession != null){
                return false;
            }

            session.persist(entity);
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
    public boolean delete(String selectedId) throws SQLException, ClassNotFoundException {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            TherapySession sessionEntity = session.get(TherapySession.class, selectedId);
            if (sessionEntity == null){
                return false;
            }
            session.remove(sessionEntity);
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
    public boolean update(TherapySession entity) throws SQLException, ClassNotFoundException {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();
        try {

            session.merge(entity);
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
    public Optional<TherapySession> findById(String selectedId) throws SQLException, ClassNotFoundException {
        Session session = factoryConfiguration.getSession();
        TherapySession therapySessionEntity = session.get(TherapySession.class, selectedId);
        session.close();
        if (therapySessionEntity == null) {
            return Optional.empty();
        }
        return Optional.of(therapySessionEntity);
    }

    @Override
    public List<String> findByTherapistByDate(Date selectedDate) {
        Session session = factoryConfiguration.getSession();

        try {
            String hql = "SELECT ts.therapist FROM TherapySession ts WHERE sessionDate = :selectedDate";
            Query<String> therapistIdQuery = session.createQuery(hql, String.class);

            therapistIdQuery.setParameter("selectedDate", selectedDate);

            return therapistIdQuery.list();
        } finally {
            session.close(); // Always close Hibernate sessions
        }
    }
}
