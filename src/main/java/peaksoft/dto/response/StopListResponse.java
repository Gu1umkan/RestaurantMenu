package peaksoft.dto.response;

import lombok.Builder;
import peaksoft.entity.MenuItem;

import java.time.LocalDate;
@Builder
public record StopListResponse( Long id,
                                String name,
                                String reason,
                                LocalDate date) {}
