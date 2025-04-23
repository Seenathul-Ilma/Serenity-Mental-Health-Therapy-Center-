package lk.ijse.gdse71.serenitytherapycenter.dao.custom.Impl;

import lk.ijse.gdse71.serenitytherapycenter.config.FactoryConfiguration;
import lk.ijse.gdse71.serenitytherapycenter.dao.custom.LogInCredentialsDAO;
import lk.ijse.gdse71.serenitytherapycenter.entity.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class LogInCredentialsDAOImpl implements LogInCredentialsDAO {
    private final FactoryConfiguration factoryConfiguration = FactoryConfiguration.getInstance();

    @Override
    //public ArrayList<LogInCredentialsDTO> getAll() throws SQLException, ClassNotFoundException {
    public List<User> getAll() throws SQLException, ClassNotFoundException {
        Session session = factoryConfiguration.getSession();
        Query<User> query = session.createQuery("from User", User.class);
        List<User> userList = query.list();
        return userList;
    }

    @Override
    //public boolean save(LogInCredentialsDTO credentialDTO) throws SQLException, ClassNotFoundException {
    public boolean save(User userEntity) throws SQLException, ClassNotFoundException {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();
        try {

            User existUser = session.get(User.class, userEntity.getUserId());
            if (existUser != null){
                return false;
            }

            session.persist(userEntity);
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
    public Optional<String> getNextId() throws SQLException, ClassNotFoundException {
        /*Session session = factoryConfiguration.getSession();
        String lastPk = session.createQuery("SELECT u.userId FROM User u ORDER BY u.userId DESC", String.class).setMaxResults(1).uniqueResult();
        session.close(); // Close the session after query execution

        if (lastPk != null) {
            // Extract the numeric part and increment
            int nextId = Integer.parseInt(lastPk.substring(1)) + 1;
            return Optional.of(String.format("US%03d", nextId)); // Format as C001, C002, etc.
        } else {
            return Optional.of("US001"); // If no customers exist, start from C001
        }*/

        Session session = factoryConfiguration.getSession();

        try {
            // Fetch the last userId in descending order
            String lastPk = session.createQuery(
                    "SELECT u.userId FROM User u ORDER BY u.userId DESC", String.class
            ).setMaxResults(1).uniqueResult();

            if (lastPk != null && lastPk.matches("^US\\d+$")) { // Ensure correct format
                int nextId = Integer.parseInt(lastPk.substring(2)) + 1; // Extract numeric part
                return Optional.of(String.format("US%03d", nextId)); // Format as US001, US002...
            } else {
                return Optional.of("US001"); // Default if no data found
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(String userId) throws SQLException, ClassNotFoundException {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            User userEntity = session.get(User.class, userId);
            if (userEntity == null) {
                return false;
            }
            session.remove(userEntity);
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
    //public boolean update(LogInCredentialsDTO credentialDTO) throws SQLException, ClassNotFoundException {
    public boolean update(User userEntity) throws SQLException, ClassNotFoundException {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.merge(userEntity);
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
        Query<String> query = session.createQuery("select u.userId from User u order by u.userId ASC ", String.class);
        List<String> idList = query.list();
        return idList;
    }

    @Override
    //public LogInCredentialsDTO findById(String id) throws SQLException, ClassNotFoundException {
    public Optional<User> findById(String id) throws SQLException, ClassNotFoundException {
        Session session = factoryConfiguration.getSession();
        User userEntity = session.get(User.class, id);
        session.close();
        if (userEntity == null) {
            return Optional.empty();
        }
        return Optional.of(userEntity);
    }

    @Override
    public String getAdminPassword() throws SQLException, ClassNotFoundException {
        /*String role = "Admin";
        ResultSet rst = SQLUtil.execute("select password from logInCredentials where role=?", role);

        if (rst.next()) {
            return rst.getString("password");
        }
        return "";*/
        return "";
    }

    @Override
    public String isExistUsernameAndPassword(String username, String password) throws SQLException, ClassNotFoundException {
        Session session = factoryConfiguration.getSession();
        try {
            Query<String> query = session.createQuery(
                    "SELECT u.userId FROM User u WHERE u.username = :username AND u.password = :password",
                    String.class
            );
            query.setParameter("username", username);
            query.setParameter("password", password);

            //return query.uniqueResult(); // Returns userId if found, else null
            String userId = query.uniqueResult();

            if (userId != null) {
                return userId;
            } else {
                return "";
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String isExistUsername(String username) throws SQLException {
        /*ResultSet rst = SQLUtil.execute("select userId from logInCredentials where userName=?", username);

        if (rst.next()) {
            return rst.getString("userId");
        }
        return "";*/

        Session session = factoryConfiguration.getSession();
        try {
            Query<String> query = session.createQuery("SELECT u.userId FROM User u WHERE u.username = :username", String.class);
            query.setParameter("username", username);

            //return query.uniqueResult(); // Returns userId if found, else null
            String userId = query.uniqueResult();

            if (userId != null) {
                return userId;
            } else {
                return "";
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getPasswordByUserId(String userId) {
        Session session = factoryConfiguration.getSession();
        try {
            Query<String> query = session.createQuery("SELECT u.password FROM User u WHERE u.userId = :userId", String.class);
            query.setParameter("userId", userId);

            //return query.uniqueResult(); // Returns userId if found, else null
            String userPassword = query.uniqueResult();

            if (userPassword != null) {
                return userPassword;
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /*public boolean isExist(String userId) throws SQLException {
        return SQLUtil.execute("select userId from logInCredentials where userId=?", userId);
    }*/
}
