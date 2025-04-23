package lk.ijse.gdse71.serenitytherapycenter.dto.tm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

/**
 * --------------------------------------------
 * Author: Zeenathul Ilma
 * GitHub: https://github.com/Seenathul-Ilma
 * Website: https://ilma.live
 * --------------------------------------------
 * Created: 4/22/2025 9:45 AM
 * Project: MobileZone
 * --------------------------------------------
 **/


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TherapySessionTM {
    private String sessionId;
    private String registrationId;
    private String programId;
    private String therapistId;
    private String patientId;
    private Date sessionDate;
    private String sessionStatus;
}

