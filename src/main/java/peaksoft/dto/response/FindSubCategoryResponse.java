package peaksoft.dto.response;

import lombok.Builder;
import org.springframework.data.domain.Page;

@Builder
public record FindSubCategoryResponse(
        Long id,
        String name,
        Page<MenuItemResponse> menuItems
        ) {}
