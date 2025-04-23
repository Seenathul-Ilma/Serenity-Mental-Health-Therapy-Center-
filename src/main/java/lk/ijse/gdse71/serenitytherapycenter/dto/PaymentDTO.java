package lk.ijse.gdse71.serenitytherapycenter.dto;

import lombok.*;

import java.sql.Date;

/**
 * --------------------------------------------
 * Author: Zeenathul Ilma
 * GitHub: https://github.com/Seenathul-Ilma
 * Website: https://ilma.live
 * --------------------------------------------
 * Created: 3/26/2025 11:27 PM
 * Project: MobileZone
 * --------------------------------------------
 **/

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PaymentDTO {
    private String paymentId;

    private Date paymentDate;

    private Double paymentAmount;

    private String paymentType;

    private String patientId;       // Optional – for display or filtering

    private String programId;       // Optional – if using it for dashboard/stats

    private String enrollmentId;    // For upfront payment

    private String sessionId;       // For session-wise payment
}
