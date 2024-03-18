package peaksoft.dto.response;

import jakarta.persistence.CascadeType;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import peaksoft.entity.User;

import java.math.BigDecimal;
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
