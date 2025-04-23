package lk.ijse.gdse71.serenitytherapycenter.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * --------------------------------------------
 * Author: Zeenathul Ilma
 * GitHub: https://github.com/Seenathul-Ilma
 * Website: https://ilma.live
 * --------------------------------------------
 * Created: 3/16/2025 10:48 PM
 * Project: MobileZone
 * --------------------------------------------
 **/

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Payment implements SuperEntity {
    @Id
    private String paymentId;

    private String patientId;    // Just storing ID, no relation here

    private String programId;    // Just storing ID, no relation here

    private Date paymentDate;

    private String paymentType;

    private BigDecimal paymentAmount;
    //private String therapySessionId;

    @OneToOne(fetch = FetchType.LAZY)
    //@MapsId("registrationId")
    @JoinColumn(name = "registration_id")
    private Enrollment enrollment;

    /*@OneToOne(fetch = FetchType.LAZY)
    //@MapsId("sessionId")   ////// just waiting to watch
    @JoinColumn(name = "session_id")*/
    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "session_id", referencedColumnName = "sessionId", nullable = true, unique = true)
    private TherapySession therapySession;

}
