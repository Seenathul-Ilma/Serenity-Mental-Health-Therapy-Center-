package lk.ijse.gdse71.serenitytherapycenter.dto.tm;

import lombok.*;

/**
 * --------------------------------------------
 * Author: Zeenathul Ilma
 * GitHub: https://github.com/Seenathul-Ilma
 * Website: https://ilma.live
 * --------------------------------------------
 * Created: 3/21/2025 11:00 AM
 * Project: MobileZone
 * --------------------------------------------
 **/

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TherapistTM {
    private String therapistId;
    private String name;
    private String speciality;
    private String nic;
    private String phone;
    private String email;
    private String availability;
}
