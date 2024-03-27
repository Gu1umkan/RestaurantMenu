package peaksoft.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import peaksoft.enums.RestType;
import peaksoft.validations.PasswordValidation;

public record RestaurantRequest(
        @NotNull@NotBlank
        String name,
        String location,
        RestType restType,
        int service,
        @Email@NotBlank
        String emailAdmin,
        @PasswordValidation
        String password
) {
}
