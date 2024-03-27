package peaksoft.dto.response;

import peaksoft.enums.Role;

public record AllUsersResponse (Long id,
                                String lastName,
                                String firstName,
                                String email,
                                Role role
){}
