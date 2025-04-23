package lk.ijse.gdse71.serenitytherapycenter.dao.custom;

import lk.ijse.gdse71.serenitytherapycenter.dao.CrudDAO;
import lk.ijse.gdse71.serenitytherapycenter.entity.Patient;

public interface PatientDAO extends CrudDAO<Patient, String> {

    String getNameById(String patientId);
}
