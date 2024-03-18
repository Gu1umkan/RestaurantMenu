package peaksoft.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SubCategoryRequest(
        @NotNull@NotBlank
        String name){}
