package peaksoft.dto.request;

import java.util.List;

public record ChequeRequest(
        List<Long> menuItemIds) {
}
