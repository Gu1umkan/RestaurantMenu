package peaksoft.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record FindCategoryResponse(
        Long id,
        String categoryName,
        List<SubCategoryResponse> subCategories
) {
}
