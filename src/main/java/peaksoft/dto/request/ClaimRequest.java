package peaksoft.dto.request;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import peaksoft.enums.Role;
import peaksoft.validations.AgeValidation;
import peaksoft.validations.ExperienceValidation;
import peaksoft.validations.PasswordValidation;
import peaksoft.validations.PhoneNumberValidation;

import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClaimRequest {
    @NotNull
    @NotBlank
    private String lastName;
    @NotNull @NotBlank
    private String firstName;
    @AgeValidation
    private LocalDate dateOfBirth;
    @Email
    private String email;
    @PasswordValidation
    private String password;
    @PhoneNumberValidation
    private String phoneNumber;
    private Role role;
    @ExperienceValidation
    private int experience;
}
