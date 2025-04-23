package lk.ijse.gdse71.serenitytherapycenter.bo.custom.Impl;

import lk.ijse.gdse71.serenitytherapycenter.bo.custom.PatientBO;
import lk.ijse.gdse71.serenitytherapycenter.dao.DAOFactory;
import lk.ijse.gdse71.serenitytherapycenter.dao.custom.PatientDAO;
import lk.ijse.gdse71.serenitytherapycenter.dto.PatientDTO;
import lk.ijse.gdse71.serenitytherapycenter.entity.Patient;

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
 * Created: 3/20/2025 2:39 PM
 * Project: MobileZone
 * --------------------------------------------
 **/

public class PatientBOImpl implements PatientBO {
    PatientDAO patientDAO = (PatientDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOType.PATIENT);

    @Override
    public List<String> getAllPatientIds() throws SQLException, ClassNotFoundException {
        List<String> ids = patientDAO.getAllIds();
        List<String> patientIds = new ArrayList<>();

        for(String patientId : ids){
            patientIds.add(patientId);
        }
        return patientIds;
    }

    @Override
    public boolean savePatient(PatientDTO patientDTO) throws SQLException, ClassNotFoundException {
        //FullName fullName = new FullName(patientDTO.getFirstName(), patientDTO.getLastName());
        return patientDAO.save(new Patient(
                patientDTO.getPatientId(),
                //fullName,
                patientDTO.getName(),
                patientDTO.getNic(),
                patientDTO.getGender(),
                patientDTO.getDateOfBirth(),
                patientDTO.getPhone()
        ));
    }

    @Override
    public boolean updatePatient(PatientDTO patientDTO) throws SQLException, ClassNotFoundException {
        //FullName fullName = new FullName(patientDTO.getFirstName(), patientDTO.getLastName());
        return patientDAO.update(new Patient(
                patientDTO.getPatientId(),
                //fullName,
                patientDTO.getName(),
                patientDTO.getNic(),
                patientDTO.getGender(),
                patientDTO.getDateOfBirth(),
                patientDTO.getPhone()
        ));
    }

    @Override
    public boolean deletePatient(String patientId) throws SQLException, ClassNotFoundException {
        return patientDAO.delete(patientId);
    }

    @Override
    public List<PatientDTO> getAllPatients() throws SQLException, ClassNotFoundException {
        ArrayList<PatientDTO> patientDTOS = new ArrayList<>();

        List<Patient> patients = patientDAO.getAll();

        if (patients == null || patients.isEmpty()) {
            System.out.println("No patients found!");
            return patientDTOS; // Return empty list if no patients exist
        }

        for(Patient patient : patients){
            patientDTOS.add(new PatientDTO(
                    patient.getPatientId(),
                    patient.getName(),
                    patient.getDateOfBirth(),
                    patient.getGender(),
                    patient.getNic(),
                    patient.getPhone()
            ));
        }
        return patientDTOS;
    }

    @Override
    public Optional<String> getNextPatientId() throws SQLException, ClassNotFoundException {
        return patientDAO.getNextId();
    }

    @Override
    //public Optional<PatientDTO> findByPatientId(String selectedPatientId) throws SQLException, ClassNotFoundException {
    public PatientDTO findByPatientId(String selectedPatientId) throws SQLException, ClassNotFoundException {
        /*PatientDTO patientDTO = new PatientDTO();

        return Optional.ofNullable(patientDAO.findById(selectedPatientId)
                .map(patient -> new PatientDTO(
                        patient.getPatientId(),
                        patient.getName(),
                        patient.getGender(),
                        patient.getDateOfBirth(),
                        patient.getNic(),
                        patient.getPhone()
                ))
                .orElse(null));*/

        //PatientDTO patientDTO = new PatientDTO();
        //FullName fullName = new FullName(patientDTO.getFirstName(), patientDTO.getLastName());
        Optional<Patient> patientOptional = patientDAO.findById(selectedPatientId);

        if(patientOptional.isPresent()){
            Patient patient = patientOptional.get();
            return new PatientDTO(
                    patient.getPatientId(),
                    patient.getName(),
                    patient.getDateOfBirth(),
                    patient.getGender(),
                    patient.getNic(),
                    patient.getPhone()
            );
        } else {
            return null;
        }


        /* Optional<Patient> patientOptional = patientDAO.findById(selectedPatientId);

        if(patientOptional.isPresent()){
            Patient patient = patientOptional.get();
            return new PatientDTO(
                    patient.getPatientId(),
                    patient.getName(),
                    patient.getGender(),
                    patient.getDateOfBirth(),
                    patient.getNic(),
                    patient.getPhone()
            );
        } else {
            return null;
        }*/
    }

    @Override
    public String getPatientNameById(String patientId) {
        return patientDAO.getNameById(patientId);
    }
}
