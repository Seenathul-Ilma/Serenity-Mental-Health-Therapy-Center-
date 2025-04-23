package lk.ijse.gdse71.serenitytherapycenter.dto;

import lombok.*;

import java.sql.Date;

/**
 * --------------------------------------------
 * Author: Zeenathul Ilma
 * GitHub: https://github.com/Seenathul-Ilma
 * Website: https://ilma.live
 * --------------------------------------------
 * Created: 3/19/2025 1:20 PM
 * Project: MobileZone
 * --------------------------------------------
 **/

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PatientDTO {
    private String patientId;
    //private String firstName;
    //private String lastName;
    private String name;
    private Date dateOfBirth;
    private String gender;
    private String nic;
    private String phone;

    /*public PatientDTO(String patientId, FullName name, String gender, Date dateOfBirth, String nic, String phone) {
        this.patientId = patientId;
        this.firstName = name.getFirstName();
        this.lastName = name.getLastName();
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.nic = nic;
        this.phone = phone;
    }*/
}
