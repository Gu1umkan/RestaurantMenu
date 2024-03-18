package peaksoft.dto.response;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import peaksoft.enums.Role;

import java.time.LocalDate;

public record AllUsersResponse (Long id,
                                String lastName,
                                String firstName,
                                String email,
                                Role role
){}
