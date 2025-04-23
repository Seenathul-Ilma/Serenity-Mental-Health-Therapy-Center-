package lk.ijse.gdse71.serenitytherapycenter.dto.tm;

import lombok.*;

import java.sql.Date;

/**
 * --------------------------------------------
 * Author: Zeenathul Ilma
 * GitHub: https://github.com/Seenathul-Ilma
 * Website: https://ilma.live
 * --------------------------------------------
 * Created: 4/20/2025 11:04 AM
 * Project: MobileZone
 * --------------------------------------------
 **/

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaymentTM {
    private String paymentId;
    private String registrationId;      // or sessionId if you're paying for a session
    private String paymentType;       // "Upfront", "Session", etc.
    private String patientId;
    private String programId;
    private String sessionId;
    private Date paymentDate;
    private Double paymentAmount;

    public String getReferenceId() {
        if ("Upfront Payment".equalsIgnoreCase(paymentType)) {
            return registrationId != null ? "Reg: " + registrationId : "N/A";
        } else if ("Session Payment".equalsIgnoreCase(paymentType)) {
            return sessionId != null ? "Ses: " + sessionId : "N/A";
        } else {
            return "N/A";
        }
    }
}
