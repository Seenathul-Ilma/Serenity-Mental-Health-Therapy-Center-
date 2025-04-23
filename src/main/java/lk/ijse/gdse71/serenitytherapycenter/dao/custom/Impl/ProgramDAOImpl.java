package lk.ijse.gdse71.serenitytherapycenter.dao.custom.Impl;

import lk.ijse.gdse71.serenitytherapycenter.config.FactoryConfiguration;
import lk.ijse.gdse71.serenitytherapycenter.dao.custom.ProgramDAO;
import lk.ijse.gdse71.serenitytherapycenter.entity.TherapyProgram;
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
 * Created: 3/24/2025 9:26 PM
 * Project: MobileZone
 * --------------------------------------------
 **/

public class ProgramDAOImpl implements ProgramDAO {
    private final FactoryConfiguration factoryConfiguration = FactoryConfiguration.getInstance();

    @Override
    public List<TherapyProgram> getAll() throws SQLException, ClassNotFoundException {
        Session session = factoryConfiguration.getSession();
        Query<TherapyProgram> query = session.createQuery("from TherapyProgram ", TherapyProgram.class);
        List<TherapyProgram> programList = query.list();
        return programList;
    }

    @Override
    public Optional<String> getNextId() throws SQLException, ClassNotFoundException {
        Session session = factoryConfiguration.getSession();

        try {
            // Fetch the last userId in descending order
            String lastPk = session.createQuery(
                    "SELECT tp.programId FROM TherapyProgram tp ORDER BY tp.programId DESC", String.class
            ).setMaxResults(1).uniqueResult();

            if (lastPk != null && lastPk.matches("^TP\\d+$")) {
                int nextId = Integer.parseInt(lastPk.substring(2)) + 1;
                return Optional.of(String.format("TP%03d", nextId)); // Format as TP001, TP002...
            } else {
                return Optional.of("TP001"); //
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean save(TherapyProgram programEntity) throws SQLException, ClassNotFoundException {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();
        try {

            TherapyProgram existProgram = session.get(TherapyProgram.class, programEntity.getProgramId());
            if (existProgram != null){
                return false;
            }

            session.persist(programEntity);
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
    public boolean delete(String selectedProId) throws SQLException, ClassNotFoundException {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            TherapyProgram programEntity = session.get(TherapyProgram.class, selectedProId);
            if (programEntity == null) {
                return false;
            }
            session.remove(programEntity);
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
    public boolean update(TherapyProgram programEntity) throws SQLException, ClassNotFoundException {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.merge(programEntity);
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
        Query<String> query = session.createQuery("select tp.programId from TherapyProgram tp order by tp.programId ASC ", String.class);
        List<String> idList = query.list();
        return idList;
    }

    @Override
    public Optional<TherapyProgram> findById(String selectedProId) throws SQLException, ClassNotFoundException {
        Session session = factoryConfiguration.getSession();
        TherapyProgram programEntity = session.get(TherapyProgram.class, selectedProId);
        session.close();
        if (programEntity == null) {
            return Optional.empty();
        }
        return Optional.of(programEntity);
    }

    @Override
    public String getNameById(String programId) {
        Session session = factoryConfiguration.getSession();
        try {
            Query<String> nameQuery = session.createQuery("SELECT tp.programName FROM TherapyProgram tp WHERE tp.programId = :programId", String.class);
            nameQuery.setParameter("programId", programId);

            String nameResult = nameQuery.uniqueResult(); // Get the single result

            return (nameResult != null) ? nameResult : "N/A";
        } finally {
            session.close(); // Ensure the session is closed to prevent leaks
        }
    }

   /* @Override
    public Double getTotalById(String programId) {
        Session session = factoryConfiguration.getSession();
        try {
            Query<Double> costQuery = session.createQuery("SELECT tp.cost FROM TherapyProgram tp WHERE tp.programId = :programId", Double.class);
            costQuery.setParameter("programId", programId);

            Double costResult = costQuery.uniqueResult(); // Get the single result

            return (costResult != null) ? costResult : 0;
        } finally {
            session.close(); // Ensure the session is closed to prevent leaks
        }
    }*/
}
