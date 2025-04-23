package lk.ijse.gdse71.serenitytherapycenter.dto.tm;

import lombok.*;

/**
 * --------------------------------------------
 * Author: Zeenathul Ilma
 * GitHub: https://github.com/Seenathul-Ilma
 * Website: https://ilma.live
 * --------------------------------------------
 * Created: 3/24/2025 9:07 PM
 * Project: MobileZone
 * --------------------------------------------
 **/

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ProgramTM {
    private String programId;

    private String programName;

    private String programDescription;

    private String duration;

    private Double cost;
}
