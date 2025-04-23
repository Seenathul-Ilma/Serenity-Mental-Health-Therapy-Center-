package lk.ijse.gdse71.serenitytherapycenter.dto;

import lombok.*;

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
public class TherapistDTO {
    private String therapistId;
    private String name;
    private String speciality;
    private String nic;
    private String phone;
    private String email;
    private String availability;
}
