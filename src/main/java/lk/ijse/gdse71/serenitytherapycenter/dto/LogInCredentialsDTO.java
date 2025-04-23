package lk.ijse.gdse71.serenitytherapycenter.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LogInCredentialsDTO {
    private String userId;
    private String role;
    private String email;
    private String username;
    private String password;
}
