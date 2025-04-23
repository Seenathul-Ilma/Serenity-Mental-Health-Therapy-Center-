package lk.ijse.gdse71.serenitytherapycenter.bo.custom;

import lk.ijse.gdse71.serenitytherapycenter.bo.SuperBO;
import lk.ijse.gdse71.serenitytherapycenter.dto.ProgramDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ProgramBO extends SuperBO {
    Optional<String> getNextProgramId() throws SQLException, ClassNotFoundException;
    List<ProgramDTO> getAllPrograms() throws SQLException, ClassNotFoundException;
    List<String> getAllProgramIds() throws SQLException, ClassNotFoundException;
    boolean deleteProgram(String programId) throws SQLException, ClassNotFoundException;
    boolean saveProgram(ProgramDTO programDTO) throws SQLException, ClassNotFoundException;
    ProgramDTO findByProgramId(String searchId) throws SQLException, ClassNotFoundException;
    boolean updateProgram(ProgramDTO programDTO) throws SQLException, ClassNotFoundException;

    String getProgramNameById(String programId);

}
