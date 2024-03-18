package peaksoft.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import peaksoft.dto.response.StopListResponse;
import peaksoft.entity.MenuItem;
import peaksoft.entity.StopList;
import peaksoft.exceptions.NotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StopListRepository extends JpaRepository<StopList, Long> {
    @Query("select new peaksoft.dto.response.StopListResponse(s.id,s.menuItem.name,s.reason,s.date) " +
            " from StopList s where s.menuItem.restaurant.id =:resId")
    List<StopListResponse> findStopListsByRestaurant(Long resId);

    default Page<StopListResponse> findStopListsByResId(Long resId, Pageable pageable){
        List<StopListResponse> lists = findStopListsByRestaurant(resId);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), lists.size());
        return new PageImpl<>(lists.subList(start, end), pageable, lists.size());
    }

    @Query("select s from StopList s where s.id =:stopId")
    Optional<StopList> findStopListById(Long stopId);

    default StopList getStopListById(Long stopId){
        return findStopListById(stopId).orElseThrow(() ->
                new NotFoundException("Stop list with id: "+stopId+" not found"));
    }


    boolean existsByMenuItemAndDate(MenuItem menuItem, LocalDate date);
    @Query("select new peaksoft.dto.response.StopListResponse(s.id,s.menuItem.name,s.reason,s.date) " +
            " from StopList s where s.menuItem.restaurant.id =:resId and s.isActive = true ")
    List<StopListResponse> findActiveStopListsByRestaurant(Long resId);


   default Page<StopListResponse> findStopListsActiveByRestId(Long resId, Pageable pageable){
       List<StopListResponse> lists = findActiveStopListsByRestaurant(resId);
       int start = (int) pageable.getOffset();
       int end = Math.min((start + pageable.getPageSize()), lists.size());
       return new PageImpl<>(lists.subList(start, end), pageable, lists.size());
   }
}