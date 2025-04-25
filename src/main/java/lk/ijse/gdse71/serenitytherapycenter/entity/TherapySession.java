package lk.ijse.gdse71.serenitytherapycenter.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@Table(name = "Therapy_Session")
public class TherapySession implements SuperEntity {
    @Id
    private String sessionId;

    @Column(nullable = false)
    private String registrationId;  // Just storing ID, no relation here

    @ManyToOne(fetch = FetchType.LAZY)
    //@MapsId("programId")
    @JoinColumn(name = "program_id")
    private TherapyProgram therapyProgram;

    @ManyToOne(fetch = FetchType.LAZY)
    //@MapsId("therapistId")
    @JoinColumn(name = "therapist_id")
    private Therapist therapist;

    @ManyToOne(fetch = FetchType.LAZY)
    //@MapsId("patientId")
    @JoinColumn(name = "patient_id")
    private Patient patient;

    private Date sessionDate;

    private String sessionStatus;     //track attendance

    @OneToOne(mappedBy = "therapySession", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Payment payment;


    //@Lob
    //private String sessionNotes;
}
