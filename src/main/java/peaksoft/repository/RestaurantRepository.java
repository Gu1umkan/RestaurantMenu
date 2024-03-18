package peaksoft.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import peaksoft.entity.Restaurant;
import peaksoft.exceptions.NotFoundException;

import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    @Query("select r from Restaurant r where r.id =:resId")
    Optional<Restaurant> findRestaurantByById(Long resId);
    default Restaurant getResById(Long resId){
        return findRestaurantByById(resId).orElseThrow(() ->
                new NotFoundException("restaurant with id: "+resId+" not found"));
    }
}