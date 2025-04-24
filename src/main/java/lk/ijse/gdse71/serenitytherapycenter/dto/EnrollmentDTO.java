package lk.ijse.gdse71.serenitytherapycenter.dto;

import lombok.*;

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
@ToString
public class EnrollmentDTO {
    private String registrationId;

    private String programId;

    private String patientId;

    //private String patientId;

    private Date enrollmentDate;

    private String enrollmentStatus;  // 'Active', 'Completed', 'Cancelled'

    //private Double paidPayment;   // Settled Amount (Upfront)

    //private String paymentStatus;  // 'Paid', 'Upfront paid', 'Pending', 'Overdue'

    //private String payment_id;           // Upfront

    private Double registrationFee;
}
