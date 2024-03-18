package peaksoft.dto.response;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import peaksoft.enums.Role;
import peaksoft.validations.ExperienceValidation;
import peaksoft.validations.PasswordValidation;
import peaksoft.validations.PhoneNumberValidation;

import java.time.LocalDate;

@Builder
public record UserResponse(
        Long id,
        String lastName,
        String firstName,

        LocalDate dateOfBirth,
        String email,
        String phoneNumber,
        Role role,

        int experience
) {}
