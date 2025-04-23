package lk.ijse.gdse71.serenitytherapycenter.dao.custom;

import lk.ijse.gdse71.serenitytherapycenter.dao.CrudDAO;
import lk.ijse.gdse71.serenitytherapycenter.entity.TherapyProgram;

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

public interface ProgramDAO extends CrudDAO<TherapyProgram, String> {
   // Double getTotalById(String programId);

    String getNameById(String programId);
}
