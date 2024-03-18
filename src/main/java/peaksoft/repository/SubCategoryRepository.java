package peaksoft.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import peaksoft.dto.response.SubCategoryResponse;
import peaksoft.entity.SubCategory;
import peaksoft.exceptions.NotFoundException;

import java.util.List;
import java.util.Optional;

public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {

    @Query("select s from SubCategory  s   where s.id = :subId ")
    Optional<SubCategory> findSubIdAndRestId(Long subId);


    @Query("select new peaksoft.dto.response.SubCategoryResponse(s.id,s.name) from SubCategory  s " +
            " where s.category.restaurant.id = :restId and s.category.id = :catId order by s.name asc ")
    List<SubCategoryResponse> findAllByCategoryId(Long restId, Long catId);

    default SubCategory getSubCatById(Long subId){
        return findSubIdAndRestId(subId).orElseThrow(()-> new NotFoundException("Subcategory not found!"));
    }


    boolean existsByName(String name);
}