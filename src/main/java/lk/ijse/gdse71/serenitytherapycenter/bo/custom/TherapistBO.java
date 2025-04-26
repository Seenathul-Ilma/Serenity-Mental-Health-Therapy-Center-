package lk.ijse.gdse71.serenitytherapycenter.bo.custom;

import lk.ijse.gdse71.serenitytherapycenter.bo.SuperBO;
import lk.ijse.gdse71.serenitytherapycenter.dto.TherapistDTO;

import java.sql.SQLException;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * --------------------------------------------
 * Author: Zeenathul Ilma
 * GitHub: https://github.com/Seenathul-Ilma
 * Website: https://ilma.live
 * --------------------------------------------
 * Created: 3/21/2025 11:09 AM
 * Project: MobileZone
 * --------------------------------------------
 **/

public interface TherapistBO extends SuperBO {
    Optional<String> getNextTherapistId() throws SQLException, ClassNotFoundException;

    List<String> getAllTherapistIds() throws SQLException, ClassNotFoundException;

    List<TherapistDTO> getAllTherapist() throws SQLException, ClassNotFoundException;

    boolean deleteTherapist(String therapistId) throws SQLException, ClassNotFoundException;

    boolean saveTherapist(TherapistDTO therapistDTO) throws SQLException, ClassNotFoundException;

    boolean updateTherapist(TherapistDTO therapistDTO) throws SQLException, ClassNotFoundException;

    TherapistDTO findByTherapistId(String selectedTherapistId) throws SQLException, ClassNotFoundException;

    ArrayList<TherapistDTO> checkAvailableTherapistsByDay(DayOfWeek selectedDay, String dayType);
    //ArrayList<TherapistDTO> checkAvailableTherapistsByDay(DayOfWeek selectedDay);
}
