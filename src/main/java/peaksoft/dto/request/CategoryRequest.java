package peaksoft.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.security.access.annotation.Secured;

@Builder
public record CategoryRequest (
        @NotNull @NotBlank
        String name){
}
