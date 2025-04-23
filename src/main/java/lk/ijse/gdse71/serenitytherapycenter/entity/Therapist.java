package lk.ijse.gdse71.serenitytherapycenter.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * --------------------------------------------
 * Author: Zeenathul Ilma
 * GitHub: https://github.com/Seenathul-Ilma
 * Website: https://ilma.live
 * --------------------------------------------
 * Created: 3/16/2025 10:48 PM
 * Project: MobileZone
 * --------------------------------------------
 **/

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Therapist implements SuperEntity {
    @Id
    private String therapistId;

    //@Embedded
    //private FullName name;
    private String name;

    private String speciality;

    private String nic;

    private String phone;

    private String email;

    private String availability;

    //@OneToMany(mappedBy = "therapist")
    //private List<Appointment> appointments;

    //@OneToMany(mappedBy = "therapist")
    //private List<TherapySession> therapySessions;
}
