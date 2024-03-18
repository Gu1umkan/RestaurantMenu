package peaksoft.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import peaksoft.enums.Role;
import peaksoft.validations.ExperienceValidation;
import peaksoft.validations.PasswordValidation;
import peaksoft.validations.PhoneNumberValidation;

import java.time.LocalDate;

public class UserRequest {
    @NotNull @NotBlank
    private String lastName;
    @NotNull @NotBlank
    private String firstName;
    @NotNull @NotBlank
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
