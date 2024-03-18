package peaksoft.dto.response;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record MenuResponse(String menuName, BigDecimal price) {
}
