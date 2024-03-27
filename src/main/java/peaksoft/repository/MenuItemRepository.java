package peaksoft.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import peaksoft.dto.response.MenuItemResponse;
import peaksoft.dto.response.MenuResponse;
import peaksoft.entity.MenuItem;
import peaksoft.exceptions.NotFoundException;

import java.util.List;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    boolean existsByName(String name);
    default MenuItem getMenuById(Long id) {
        return findById(id).orElseThrow(()->
                new NotFoundException("MenuItem with id:"+id+" not found"));
    }

    @Query("select new peaksoft.dto.response.MenuItemResponse(m.id,m.name,m.image,m.price,m.description,m.isVegetarian) from MenuItem  m where m.restaurant.id =:restId and m.stopList is  null ")
    List<MenuItemResponse> findAllMenuItemByRestaurantId(Long restId);


    default Page<MenuItemResponse> findAllMenuByRestaurantId(Long restId, Pageable pageable){
        List<MenuItemResponse> menu = findAllMenuItemByRestaurantId(restId);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), menu.size());
        return new PageImpl<>(menu.subList(start, end), pageable, menu.size());
    }

    @Query("""
            select new peaksoft.dto.response.MenuItemResponse(m.id,m.name,m.image,m.price,m.description,m.isVegetarian) 
            from MenuItem  m where m.restaurant.id =:restId and m.stopList is null 
             order by 
             case when :ascOrDesc = 'asc' then m.price end asc,
             case when :ascOrDesc = 'desc' then m.price end desc""")
    List<MenuItemResponse> sortMenuItemByPrice(Long restId, String ascOrDesc);


    default Page<MenuItemResponse> sortMenuItemByPrice(Long restId,String ascOrDesc, Pageable pageable){
        List<MenuItemResponse> menu = sortMenuItemByPrice(restId,ascOrDesc);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), menu.size());
        return new PageImpl<>(menu.subList(start, end), pageable, menu.size());
    }

    @Query("""
               select new peaksoft.dto.response.MenuItemResponse
               (m.id,m.name,m.image,m.price,m.description,m.isVegetarian) 
               from MenuItem  m where m.restaurant.id =:restId and m.isVegetarian = :isVegan
               and m.stopList is null  """)
    List<MenuItemResponse> filterVegetarian(Long restId, Boolean isVegan);

    default Page<MenuItemResponse> filterByVegetarian(Long restId, Boolean isVegan, Pageable pageable){
        List<MenuItemResponse> menu = filterVegetarian(restId,isVegan);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), menu.size());
        return new PageImpl<>(menu.subList(start, end), pageable, menu.size());
    }



    @Query("""
               select new peaksoft.dto.response.MenuItemResponse
               (m.id,m.name,m.image,m.price,m.description,m.isVegetarian) 
               from MenuItem  m where m.restaurant.id =:restId and  
                (m.name ilike (:keyword)
                  or m.subCategory.name ilike (:keyword)
                  or m.subCategory.category.name ilike (:keyword))
                       """)
    List<MenuItemResponse> searchMenuItemBy(Long restId, String keyword);


   default Page<MenuItemResponse> searchMenuItem(Long restId, String keyword, Pageable pageable){
        List<MenuItemResponse> menu = searchMenuItemBy(restId,keyword);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), menu.size());
        return new PageImpl<>(menu.subList(start, end), pageable, menu.size());
    }

    @Query("select new peaksoft.dto.response.MenuItemResponse(m.id,m.name,m.image,m.price,m.description,m.isVegetarian) from MenuItem  m where m.subCategory.id =:subCatId")
    List<MenuItemResponse> findAllBySubCat(Long subCatId);

   default Page<MenuItemResponse> findAllMenuBySubCatId(Long subCatId,Pageable pageable){
       List<MenuItemResponse> menu = findAllBySubCat(subCatId);
       int start = (int) pageable.getOffset();
       int end = Math.min((start + pageable.getPageSize()), menu.size());
       return new PageImpl<>(menu.subList(start, end), pageable, menu.size());
   }

   @Query("select m from MenuItem  m where m.id in (:menu) and m.stopList is null ")
    List<MenuItem> getAllMenuId(List<Long> menu);
   @Query("select new peaksoft.dto.response.MenuResponse(m.name,m.price) from Cheque  c join  c.menuItems m where c.id =:checkId")
    List<MenuResponse> menuCheckById(Long checkId);


}