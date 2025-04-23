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
 * Created: 3/26/2025 9:17 PM
 * Project: MobileZone
 * --------------------------------------------
 **/

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EnrollTM {
        private String registrationId;
        private String patientId;
        private String programId;
        private Date enrollDate;
       // private Double totalCost;
        //private Double pendingPayment;
        private String enrollmentStatus;
}
