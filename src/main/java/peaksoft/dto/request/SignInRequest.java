package peaksoft.dto.request;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;
import peaksoft.validations.PasswordValidation;

@Getter
@Setter
public class SignInRequest {
    @Email
    private String email;
    @PasswordValidation
    private String password;
}
