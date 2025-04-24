package lk.ijse.gdse71.serenitytherapycenter.dao.custom;

import lk.ijse.gdse71.serenitytherapycenter.dao.CrudDAO;
import lk.ijse.gdse71.serenitytherapycenter.entity.TherapySession;
import org.hibernate.Session;

import java.sql.Date;
import java.util.List;

public interface TherapySessionDAO extends CrudDAO<TherapySession, String> {
    List<String> findByTherapistByDate(Date selectedDate);

    boolean save(Session session, TherapySession therapySession);
}
