package peaksoft.dto.response;

import lombok.Builder;
import peaksoft.enums.Role;

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
