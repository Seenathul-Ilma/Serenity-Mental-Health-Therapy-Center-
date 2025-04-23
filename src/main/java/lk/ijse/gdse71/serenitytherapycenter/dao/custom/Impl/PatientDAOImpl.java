package lk.ijse.gdse71.serenitytherapycenter.dao.custom.Impl;

import lk.ijse.gdse71.serenitytherapycenter.config.FactoryConfiguration;
import lk.ijse.gdse71.serenitytherapycenter.dao.custom.PatientDAO;
import lk.ijse.gdse71.serenitytherapycenter.entity.Patient;
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
 * Created: 3/20/2025 2:43 PM
 * Project: MobileZone
 * --------------------------------------------
 **/

public class PatientDAOImpl implements PatientDAO {
    private final FactoryConfiguration factoryConfiguration = FactoryConfiguration.getInstance();

    @Override
    public List<Patient> getAll() throws SQLException, ClassNotFoundException {
        Session session = factoryConfiguration.getSession();
        Query<Patient> query = session.createQuery("from Patient ", Patient.class);
        List<Patient> patientList = query.list();
        return patientList;
    }

    @Override
    public Optional<String> getNextId() throws SQLException, ClassNotFoundException {
        Session session = factoryConfiguration.getSession();

        try {
            // Fetch the last userId in descending order
            String lastPk = session.createQuery(
                    "SELECT p.patientId FROM Patient p ORDER BY p.patientId DESC", String.class
            ).setMaxResults(1).uniqueResult();

            if (lastPk != null && lastPk.matches("^P\\d+$")) { // Ensure correct format
                int nextId = Integer.parseInt(lastPk.substring(1)) + 1; // Extract numeric part
                return Optional.of(String.format("P%03d", nextId)); // Format as P001, P002...
            } else {
                return Optional.of("P001"); // Default if no data found
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean save(Patient patientEntity) throws SQLException, ClassNotFoundException {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();
        try {

            Patient existPatient = session.get(Patient.class, patientEntity.getPatientId());
            if (existPatient != null){
                return false;
            }

            session.persist(patientEntity);
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
    public boolean delete(String patientId) throws SQLException, ClassNotFoundException {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            Patient patientEntity = session.get(Patient.class, patientId);
            if (patientEntity == null) {
                return false;
            }
            session.remove(patientEntity);
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
    public boolean update(Patient patientEntity) throws SQLException, ClassNotFoundException {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.merge(patientEntity);
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
        Query<String> query = session.createQuery("select p.patientId from Patient p order by p.patientId ASC ", String.class);
        List<String> idList = query.list();
        return idList;
    }

    @Override
    public Optional<Patient> findById(String selectedPatientId) throws SQLException, ClassNotFoundException {
        Session session = factoryConfiguration.getSession();
        Patient patientEntity = session.get(Patient.class, selectedPatientId);
        session.close();
        if (patientEntity == null) {
            return Optional.empty();
        }
        return Optional.of(patientEntity);
    }

    @Override
    public String getNameById(String patientId) {
        Session session = factoryConfiguration.getSession();
        try {
            Query<String> nameQuery = session.createQuery("SELECT p.name FROM Patient p WHERE p.patientId = :patientId", String.class);
            nameQuery.setParameter("patientId", patientId);

            String nameResult = nameQuery.uniqueResult(); // Get the single result

            return (nameResult != null) ? nameResult : "N/A";
        } finally {
            session.close(); // Ensure the session is closed to prevent leaks
        }
    }
}
