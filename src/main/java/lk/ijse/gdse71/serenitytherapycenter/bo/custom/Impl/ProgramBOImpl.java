package lk.ijse.gdse71.serenitytherapycenter.bo.custom.Impl;

import lk.ijse.gdse71.serenitytherapycenter.bo.custom.ProgramBO;
import lk.ijse.gdse71.serenitytherapycenter.dao.DAOFactory;
import lk.ijse.gdse71.serenitytherapycenter.dao.custom.ProgramDAO;
import lk.ijse.gdse71.serenitytherapycenter.dto.ProgramDTO;
import lk.ijse.gdse71.serenitytherapycenter.entity.TherapyProgram;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * --------------------------------------------
 * Author: Zeenathul Ilma
 * GitHub: https://github.com/Seenathul-Ilma
 * Website: https://ilma.live
 * --------------------------------------------
 * Created: 3/24/2025 9:22 PM
 * Project: MobileZone
 * --------------------------------------------
 **/

public class ProgramBOImpl implements ProgramBO {
    ProgramDAO programDAO = (ProgramDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOType.THERAPY_PROGRAM);

    @Override
    public Optional<String> getNextProgramId() throws SQLException, ClassNotFoundException {
        return programDAO.getNextId();
    }

    @Override
    public List<ProgramDTO> getAllPrograms() throws SQLException, ClassNotFoundException {
        ArrayList<ProgramDTO> programDTOS = new ArrayList<>();

        List<TherapyProgram> programs = programDAO.getAll();

        if (programs == null || programs.isEmpty()) {
            System.out.println("No therapy program found!");
            return programDTOS; // Return empty list if no programs exist
        }

        for(TherapyProgram program : programs){
            programDTOS.add(new ProgramDTO(
                    program.getProgramId(),
                    program.getProgramName(),
                    program.getProgramDescription(),
                    program.getDuration(),
                    program.getCost().doubleValue()
            ));
        }
        return programDTOS;
    }

    @Override
    public List<String> getAllProgramIds() throws SQLException, ClassNotFoundException {
        List<String> ids = programDAO.getAllIds();
        List<String> programIds = new ArrayList<>();

        for(String programId : ids){
            programIds.add(programId);
        }
        return programIds;
    }

    @Override
    public boolean deleteProgram(String programId) throws SQLException, ClassNotFoundException {
        return programDAO.delete(programId);
    }

    @Override
    public boolean saveProgram(ProgramDTO programDTO) throws SQLException, ClassNotFoundException {
        double cost = programDTO.getCost();
        return programDAO.save(new TherapyProgram(
                programDTO.getProgramId(),
                programDTO.getProgramName(),
                programDTO.getProgramDescription(),
                programDTO.getDuration(),
                BigDecimal.valueOf(cost)
        ));
    }

    @Override
    public ProgramDTO findByProgramId(String searchId) throws SQLException, ClassNotFoundException {
        Optional<TherapyProgram> programOptional = programDAO.findById(searchId);

        if(programOptional.isPresent()){
            TherapyProgram program = programOptional.get();
            // Convert BigDecimal price to double
            double amount = program.getCost().doubleValue();
            return new ProgramDTO(
                    program.getProgramId(),
                    program.getProgramName(),
                    program.getProgramDescription(),
                    program.getDuration(),
                    amount
            );
        } else {
            return null;
        }
    }

    @Override
    public boolean updateProgram(ProgramDTO programDTO) throws SQLException, ClassNotFoundException {
        double cost = programDTO.getCost();
        return programDAO.update(new TherapyProgram(
                programDTO.getProgramId(),
                programDTO.getProgramName(),
                programDTO.getProgramDescription(),
                programDTO.getDuration(),
                BigDecimal.valueOf(cost)
        ));
    }

    @Override
    public String getProgramNameById(String programId) {
        return programDAO.getNameById(programId);
    }
}
