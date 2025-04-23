package lk.ijse.gdse71.serenitytherapycenter.dto.tm;

import lombok.*;

import java.sql.Date;

/**
 * --------------------------------------------
 * Author: Zeenathul Ilma
 * GitHub: https://github.com/Seenathul-Ilma
 * Website: https://ilma.live
 * --------------------------------------------
 * Created: 3/20/2025 2:15 PM
 * Project: MobileZone
 * --------------------------------------------
 **/

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PatientTM {
    private String patientId;
    //private String firstName;
    //private String lastName;
    private String name;
    private Date dateOfBirth;
    private String gender;
    private String nic;
    private String phone;
}
