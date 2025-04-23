package lk.ijse.gdse71.serenitytherapycenter.dto.tm;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LogInCredentialTM {
    private String userId;
    private String role;
    private String email;
    private String username;
    //private String password;
}
