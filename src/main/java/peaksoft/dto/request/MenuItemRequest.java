package peaksoft.dto.request;

import jakarta.validation.constraints.NotBlank;
import peaksoft.validations.PriceValidation;

import java.math.BigDecimal;

public record MenuItemRequest(
        @NotBlank
        String name,
        @NotBlank
        String image,
        @PriceValidation
        BigDecimal price,
        @NotBlank
        String description,
        Boolean isVegetarian) {
}
