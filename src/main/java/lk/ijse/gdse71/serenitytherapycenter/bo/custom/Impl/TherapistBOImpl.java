package lk.ijse.gdse71.serenitytherapycenter.bo.custom.Impl;

import lk.ijse.gdse71.serenitytherapycenter.bo.custom.TherapistBO;
import lk.ijse.gdse71.serenitytherapycenter.dao.DAOFactory;
import lk.ijse.gdse71.serenitytherapycenter.dao.custom.TherapistDAO;
import lk.ijse.gdse71.serenitytherapycenter.dto.TherapistDTO;
import lk.ijse.gdse71.serenitytherapycenter.entity.Therapist;

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
 * Created: 3/21/2025 11:08 AM
 * Project: MobileZone
 * --------------------------------------------
 **/

public class TherapistBOImpl implements TherapistBO {
    TherapistDAO therapistDAO = (TherapistDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOType.THERAPIST);

    @Override
    public Optional<String> getNextTherapistId() throws SQLException, ClassNotFoundException {
        return therapistDAO.getNextId();
    }

    @Override
    public List<String> getAllTherapistIds() throws SQLException, ClassNotFoundException {
        List<String> ids = therapistDAO.getAllIds();
        List<String> therapistIds = new ArrayList<>();

        for(String therapistId : ids){
            therapistIds.add(therapistId);
        }
        return therapistIds;
    }

    @Override
    public List<TherapistDTO> getAllTherapist() throws SQLException, ClassNotFoundException {
        ArrayList<TherapistDTO> therapistDTOS = new ArrayList<>();

        List<Therapist> therapists = therapistDAO.getAll();

        if (therapists == null || therapists.isEmpty()) {
            System.out.println("No therapists found!");
            return therapistDTOS; // Return empty list if no patients exist
        }

        for(Therapist therapist : therapists){
            therapistDTOS.add(new TherapistDTO(
                    therapist.getTherapistId(),
                    therapist.getName(),
                    therapist.getSpeciality(),
                    therapist.getNic(),
                    therapist.getPhone(),
                    therapist.getEmail(),
                    therapist.getAvailability()
            ));
        }
        return therapistDTOS;
    }

    @Override
    public boolean deleteTherapist(String therapistId) throws SQLException, ClassNotFoundException {
        return therapistDAO.delete(therapistId);
    }

    @Override
    public boolean saveTherapist(TherapistDTO therapistDTO) throws SQLException, ClassNotFoundException {
        //FullName fullName = new FullName(therapistDTO.getFirstName(), therapistDTO.getLastName());
        return therapistDAO.save(new Therapist(
                therapistDTO.getTherapistId(),
                //fullName,
                therapistDTO.getName(),
                therapistDTO.getSpeciality(),
                therapistDTO.getNic(),
                therapistDTO.getPhone(),
                therapistDTO.getEmail(),
                therapistDTO.getAvailability()
        ));
    }

    @Override
    public boolean updateTherapist(TherapistDTO therapistDTO) throws SQLException, ClassNotFoundException {
        //FullName fullName = new FullName(therapistDTO.getFirstName(), therapistDTO.getLastName());
        return therapistDAO.update(new Therapist(
                therapistDTO.getTherapistId(),
                //fullName,
                therapistDTO.getName(),
                therapistDTO.getSpeciality(),
                therapistDTO.getNic(),
                therapistDTO.getPhone(),
                therapistDTO.getEmail(),
                therapistDTO.getAvailability()
        ));
    }

    @Override
    public TherapistDTO findByTherapistId(String selectedTherapistId) throws SQLException, ClassNotFoundException {

        Optional<Therapist> therapistOptional = therapistDAO.findById(selectedTherapistId);

        if(therapistOptional.isPresent()){
            Therapist therapist = therapistOptional.get();
            return new TherapistDTO(
                    therapist.getTherapistId(),
                    therapist.getName(),
                    therapist.getSpeciality(),
                    therapist.getNic(),
                    therapist.getPhone(),
                    therapist.getEmail(),
                    therapist.getAvailability()
            );
        } else {
            return null;
        }
    }

    @Override
    public ArrayList<TherapistDTO> checkAvailableTherapistsByDay(DayOfWeek selectedDay, String dayType) {
    //public ArrayList<TherapistDTO> checkAvailableTherapistsByDay(DayOfWeek selectedDay) {
        ArrayList<TherapistDTO> therapistDTOS = new ArrayList<>();

        List<Therapist> therapists = therapistDAO.getAvailableTherapists(selectedDay, dayType);
        //List<Therapist> therapists = therapistDAO.getAvailableTherapists(selectedDay);

        if (therapists == null || therapists.isEmpty()) {
            System.out.println("No available therapists found!");
            return therapistDTOS; // Return empty list if no patients exist
        }

        for(Therapist therapist : therapists){
            therapistDTOS.add(new TherapistDTO(
                    therapist.getTherapistId(),
                    therapist.getName(),
                    therapist.getSpeciality(),
                    therapist.getNic(),
                    therapist.getPhone(),
                    therapist.getEmail(),
                    therapist.getAvailability()
            ));
        }
        return therapistDTOS;
    }
}
