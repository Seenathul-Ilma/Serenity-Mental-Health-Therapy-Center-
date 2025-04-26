package lk.ijse.gdse71.serenitytherapycenter.dao.custom;

import lk.ijse.gdse71.serenitytherapycenter.dao.CrudDAO;
import lk.ijse.gdse71.serenitytherapycenter.entity.Therapist;

import java.time.DayOfWeek;
import java.util.List;

public interface TherapistDAO extends CrudDAO<Therapist, String> {
    List<Therapist> getAvailableTherapists(DayOfWeek selectedDay, String dayType);
    //List<Therapist> getAvailableTherapists(DayOfWeek selectedDay);
}
