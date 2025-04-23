package lk.ijse.gdse71.serenitytherapycenter.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

/**
 * --------------------------------------------
 * Author: Zeenathul Ilma
 * GitHub: https://github.com/Seenathul-Ilma
 * Website: https://ilma.live
 * --------------------------------------------
 * Created: 3/16/2025 10:08 PM
 * Project: MobileZone
 * --------------------------------------------
 **/

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Patient implements SuperEntity{
    @Id
    private String patientId;

    //@Embedded
    //private FullName name;
    private String name;
    private String nic;

    private String gender;
    private Date dateOfBirth;
    private String phone;

    //@OneToMany(mappedBy = "patient")
    //private List<PatientProgram> patientPrograms;       // uni directional


    // Therapy History
    //@OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    //private List<TherapySession> therapySessions;

    //@OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    //private List<Appointment> appointments;
}
