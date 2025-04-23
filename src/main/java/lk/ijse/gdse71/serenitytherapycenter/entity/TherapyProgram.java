package lk.ijse.gdse71.serenitytherapycenter.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

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
public class TherapyProgram implements SuperEntity {
    @Id
    private String programId;

    private String programName;

    @Lob
    private String programDescription;

    private String duration;

    @Column(precision = 10, scale = 2)
    private BigDecimal cost;

    //@OneToMany(mappedBy = "therapyProgram")
    //private List<Enrollment> enrollments;

    //@OneToMany(mappedBy = "therapyProgram")
    //private List<TherapySession> therapySessions;
}
