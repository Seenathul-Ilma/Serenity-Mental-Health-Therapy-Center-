package lk.ijse.gdse71.serenitytherapycenter.dao.custom;

import lk.ijse.gdse71.serenitytherapycenter.dao.CrudDAO;
import lk.ijse.gdse71.serenitytherapycenter.entity.Enrollment;
import org.hibernate.Session;

import java.util.List;

/**
 * --------------------------------------------
 * Author: Zeenathul Ilma
 * GitHub: https://github.com/Seenathul-Ilma
 * Website: https://ilma.live
 * --------------------------------------------
 * Created: 3/26/2025 10:03 PM
 * Project: MobileZone
 * --------------------------------------------
 **/

public interface EnrollmentDAO extends CrudDAO<Enrollment, String> {
    boolean save(Session session, Enrollment enrollment);

    List<String> getPendingIds();

    boolean updateEnrollmentStatus(String registrationId, String newStatus);
}
