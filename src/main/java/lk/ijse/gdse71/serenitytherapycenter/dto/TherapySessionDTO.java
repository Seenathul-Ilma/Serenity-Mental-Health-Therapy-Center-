package lk.ijse.gdse71.serenitytherapycenter.dto;

import lombok.*;

import java.sql.Date;

/**
 * --------------------------------------------
 * Author: Zeenathul Ilma
 * GitHub: https://github.com/Seenathul-Ilma
 * Website: https://ilma.live
 * --------------------------------------------
 * Created: 4/22/2025 9:38 AM
 * Project: MobileZone
 * --------------------------------------------
 **/


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TherapySessionDTO {
    private String sessionId;
    private String registrationId;
    private String programId;
    private String therapistId;
    private String patientId;
    private Date sessionDate;
    private String sessionStatus;

}
