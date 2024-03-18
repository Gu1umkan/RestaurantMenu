package peaksoft.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import peaksoft.dto.response.ChequeResponse;
import peaksoft.entity.Cheque;
import peaksoft.entity.MenuItem;
import peaksoft.exceptions.NotFoundException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ChequeRepository extends JpaRepository<Cheque, Long> {

   default Cheque getChequeById(Long chequeId){
      return findById(chequeId).orElseThrow(()-> new NotFoundException("Cheque with id:"+ chequeId+" not found!"));
   }

   @Query("select c from Cheque c where c.user.restaurant.id = :restId")
   List<Cheque> findAllCheque(Long restId);

   @Query("select c from Cheque c where c.user.id = :userId")
   List<Cheque> findAllChequeByUserId(Long userId);

   @Query("select c from Cheque c where c.user.id = :userId and c.createdAt = :localDate")
   List<Cheque> findAllChequeByUserIdAndDate(Long userId, LocalDate localDate);


}