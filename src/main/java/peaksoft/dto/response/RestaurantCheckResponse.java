package peaksoft.dto.response;

import lombok.Builder;

@Builder
public record RestaurantCheckResponse(
        String totalSumma,
        String priceTotal,
        String serviceTotal
) {
}
