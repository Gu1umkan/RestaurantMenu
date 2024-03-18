package peaksoft.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record PaginationMenuItem(int page,
                                 int size,
                                 List<MenuItemResponse> allMenuItems) {
}
