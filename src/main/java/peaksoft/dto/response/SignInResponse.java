package peaksoft.dto.response;

import lombok.Builder;
import peaksoft.enums.Role;

@Builder
public record SignInResponse(String token,Long id, String email, Role role) {}
