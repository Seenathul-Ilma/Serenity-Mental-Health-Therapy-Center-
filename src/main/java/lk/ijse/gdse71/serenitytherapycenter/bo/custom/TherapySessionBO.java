package lk.ijse.gdse71.serenitytherapycenter.bo.custom;

import lk.ijse.gdse71.serenitytherapycenter.bo.SuperBO;
import lk.ijse.gdse71.serenitytherapycenter.dto.TherapySessionDTO;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface TherapySessionBO extends SuperBO {

    Optional<String> getNextSessionId() throws SQLException, ClassNotFoundException;

    List<String> getAllSessionIds() throws SQLException, ClassNotFoundException;

    List<TherapySessionDTO> getAllSessions() throws SQLException, ClassNotFoundException;

    boolean deleteSession(String sessionId) throws SQLException, ClassNotFoundException;

    boolean saveSession(TherapySessionDTO therapySessionDTO) throws SQLException, ClassNotFoundException;

    boolean updateSession(TherapySessionDTO therapySessionDTO) throws SQLException, ClassNotFoundException;

    TherapySessionDTO findBySessionId(String selectedSessionId) throws SQLException, ClassNotFoundException;

    List<String> checkBookedTherapistsByDate(Date selectedDate);
}
