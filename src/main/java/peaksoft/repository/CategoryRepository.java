package peaksoft.repository;

import org.apache.coyote.BadRequestException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import peaksoft.dto.response.CategoryResponse;
import peaksoft.entity.Category;
import peaksoft.exceptions.BedRequestException;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name);

    default Category getCategoryById(Long catId) {
         return findById(catId).orElseThrow(()->
                 new BedRequestException("Category with id:"+catId+" not found"));
    }
    @Query("select new peaksoft.dto.response.CategoryResponse(c.id,c.name) from Category  c  where c.restaurant.id = :id" )
    List<CategoryResponse> findAllByRestaurantId(Long id);
}