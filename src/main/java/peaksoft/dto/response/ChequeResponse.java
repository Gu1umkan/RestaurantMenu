package peaksoft.dto.response;


import lombok.Builder;
import java.time.LocalDate;
@Builder
public record ChequeResponse(
         String percent ,
         String service,
         LocalDate createdAt,
         String allSumma,
         String waiterName,
         String totalPrice) {
}
