package peaksoft.dto.response;

import lombok.Builder;
import peaksoft.enums.RestType;

@Builder
public record RestaurantResponse(
        String name,
        String Location,
        RestType restType,
        int numberOfEmployees,
        int service
) {
}
