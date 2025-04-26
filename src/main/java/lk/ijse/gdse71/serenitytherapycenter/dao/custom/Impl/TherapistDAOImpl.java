package lk.ijse.gdse71.serenitytherapycenter.dao.custom.Impl;

import lk.ijse.gdse71.serenitytherapycenter.config.FactoryConfiguration;
import lk.ijse.gdse71.serenitytherapycenter.dao.custom.TherapistDAO;
import lk.ijse.gdse71.serenitytherapycenter.entity.Therapist;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.sql.SQLException;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

/**
 * --------------------------------------------
 * Author: Zeenathul Ilma
 * GitHub: https://github.com/Seenathul-Ilma
 * Website: https://ilma.live
 * --------------------------------------------
 * Created: 3/21/2025 11:10 AM
 * Project: MobileZone
 * --------------------------------------------
 **/

public class TherapistDAOImpl implements TherapistDAO {
    private final FactoryConfiguration factoryConfiguration = FactoryConfiguration.getInstance();

    @Override
    public List<Therapist> getAll() throws SQLException, ClassNotFoundException {
        Session session = factoryConfiguration.getSession();
        Query<Therapist> query = session.createQuery("from Therapist ", Therapist.class);
        List<Therapist> therapistList = query.list();
        return therapistList;
    }

    @Override
    public Optional<String> getNextId() throws SQLException, ClassNotFoundException {
        Session session = factoryConfiguration.getSession();

        try {
            // Fetch the last userId in descending order
            String lastPk = session.createQuery(
                    "SELECT t.therapistId FROM Therapist t ORDER BY t.therapistId DESC", String.class
            ).setMaxResults(1).uniqueResult();

            if (lastPk != null && lastPk.matches("^T\\d+$")) { // Ensure correct format
                int nextId = Integer.parseInt(lastPk.substring(1)) + 1; // Extract numeric part
                return Optional.of(String.format("T%03d", nextId)); // Format as P001, P002...
            } else {
                return Optional.of("T001"); // Default if no data found
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean save(Therapist therapistEntity) throws SQLException, ClassNotFoundException {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();
        try {

            Therapist existTherapist = session.get(Therapist.class, therapistEntity.getTherapistId());
            if (existTherapist != null){
                return false;
            }

            session.persist(therapistEntity);
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
    public boolean delete(String selectedTherapistId) throws SQLException, ClassNotFoundException {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            Therapist therapistEntity = session.get(Therapist.class, selectedTherapistId);
            if (therapistEntity == null) {
                return false;
            }
            session.remove(therapistEntity);
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
    public boolean update(Therapist therapistEntity) throws SQLException, ClassNotFoundException {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.merge(therapistEntity);
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
    public List<String> getAllIds() throws SQLException, ClassNotFoundException {
        Session session = factoryConfiguration.getSession();
        Query<String> query = session.createQuery("select t.therapistId from Therapist t order by t.therapistId ASC ", String.class);
        List<String> idList = query.list();
        return idList;
    }

    @Override
    public Optional<Therapist> findById(String selectedTherapistId) throws SQLException, ClassNotFoundException {
        Session session = factoryConfiguration.getSession();
        Therapist therapistEntity = session.get(Therapist.class, selectedTherapistId);
        session.close();
        if (therapistEntity == null) {
            return Optional.empty();
        }
        return Optional.of(therapistEntity);
    }

    @Override
    public List<Therapist> getAvailableTherapists(DayOfWeek selectedDay, String dayType) {
    //public List<Therapist> getAvailableTherapists(DayOfWeek selectedDay) {
        Session session = factoryConfiguration.getSession();

        try {
            String selectedDayName = capitalizeFirst(selectedDay.toString().toLowerCase()); // "Monday"

            //String hql = "FROM Therapist WHERE availability = :selectedDay OR availability = :dayType";
            String hql = "FROM Therapist t WHERE t.availability LIKE CONCAT('%', :dayName, '%') or t.availability = :dayType";
            Query<Therapist> therapistQuery = session.createQuery(hql, Therapist.class);

            //therapistQuery.setParameter("selectedDay", dayName);
            //therapistQuery.setParameter("dayType", dayType);
            therapistQuery.setParameter("dayName", selectedDayName);
            therapistQuery.setParameter("dayType", dayType);

            return therapistQuery.list();
        } finally {
            session.close(); // Always close Hibernate sessions
        }

    }

    private String capitalizeFirst(String day) {
        //return day.substring(0, 1).toUpperCase() + day.substring(1).toLowerCase();
        if (day == null || day.isEmpty()) {
            return day;
        }
        return day.substring(0, 1).toUpperCase() + day.substring(1);
    }
}
