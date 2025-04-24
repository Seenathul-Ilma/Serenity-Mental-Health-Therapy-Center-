package lk.ijse.gdse71.serenitytherapycenter.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

/**
 * --------------------------------------------
 * Author: Zeenathul Ilma
 * GitHub: https://github.com/Seenathul-Ilma
 * Website: https://ilma.live
 * --------------------------------------------
 * Created: 3/19/2025 10:03 PM
 * Project: MobileZone
 * --------------------------------------------
 **/

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Enrollment implements SuperEntity {
    @Id
    private String registrationId;

    @Column(nullable = false)
    private BigDecimal registrationFee;

    /*@ManyToOne(fetch = FetchType.LAZY)
    //@MapsId("programId")
    @JoinColumn(name = "program_id")
    private TherapyProgram therapyProgram;

    @ManyToOne(fetch = FetchType.LAZY)
    //@MapsId("patientId")
    @JoinColumn(name = "patientId")
    private Patient patient;*/

    @ManyToOne(fetch = FetchType.LAZY)
    //@MapsId("programId")
    @JoinColumn(name = "program_id")
    private TherapyProgram therapyProgram;

    @ManyToOne(fetch = FetchType.LAZY)
    //@MapsId("patientId")
    @JoinColumn(name = "patientId")
    private Patient patient;

    private Date enrollmentDate;

    private String enrollmentStatus;

    @OneToMany(mappedBy = "enrollment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Payment> payment;

}
