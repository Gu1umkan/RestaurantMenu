package peaksoft.dto.response;

import lombok.Builder;

@Builder
public record SubCategoryResponse(
        Long id,
        String name) {}
