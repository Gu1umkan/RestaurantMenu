package peaksoft.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import peaksoft.dto.request.CategoryRequest;
import peaksoft.dto.response.CategoryResponse;
import peaksoft.dto.response.FindCategoryResponse;
import peaksoft.dto.response.SimpleResponse;
import peaksoft.dto.response.SubCategoryResponse;
import peaksoft.entity.Category;
import peaksoft.entity.Restaurant;
import peaksoft.entity.User;
import peaksoft.enums.Role;
import peaksoft.exceptions.AlreadyExistsException;
import peaksoft.exceptions.ForbiddenException;
import peaksoft.repository.CategoryRepository;
import peaksoft.repository.SubCategoryRepository;
import peaksoft.repository.UserRepository;
import peaksoft.service.CategoryService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepo;
    private final UserRepository userRepository;
    private final SubCategoryRepository subCategoryRepo;
    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User current = userRepository.getByEmail(email);
        if (current.getRole().equals(Role.ADMIN) || current.getRole().equals(Role.CHEF))
             return current;
        else throw new ForbiddenException("Forbidden 403");
    }
    @Override
    @Transactional
    public SimpleResponse save(CategoryRequest categoryRequest) {

        boolean b = categoryRepo.existsByName(categoryRequest.name());
        if(b) throw  new AlreadyExistsException("Category with name: "+categoryRequest.name() +" already exists!");
        Restaurant restaurant = getCurrentUser().getRestaurant();
        categoryRepo.save(Category.builder()
                       .restaurant(restaurant)
                       .name(categoryRequest.name())
                       .build());
       return SimpleResponse.builder()
               .httpStatus(HttpStatus.ACCEPTED)
               .message("Category with name "+categoryRequest.name()+" successfully saved")
               .build();
    }

    @Override @Transactional
    public SimpleResponse update(Long catId, CategoryRequest categoryRequest) {
        Category category = categoryRepo.getCategoryById(catId);
        if(categoryRequest.name() != null) {
          getCurrentUser();
        category.setName(categoryRequest.name());
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message(("Category with name "+categoryRequest.name()+" successfully updated"))
                .build();}
        else throw  new RuntimeException("category request is null");
    }

    @Override @Transactional
    public SimpleResponse delete(Long catId) {
        Category category = categoryRepo.getCategoryById(catId);
        getCurrentUser();
        categoryRepo.delete(category);
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message(("Category with name "+category.getName()+" successfully deleted"))
                .build();
    }

    @Override
    public List<CategoryResponse> findAll() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.getByEmail(email);
        Restaurant restaurant = currentUser.getRestaurant();
        return categoryRepo.findAllByRestaurantId(restaurant.getId());
    }

    @Override
    public FindCategoryResponse findCategoryById(Long catId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.getByEmail(email);

        Category foundCategory = categoryRepo.getCategoryById(catId);
        List<SubCategoryResponse> allSubCatByCategoryId = subCategoryRepo.findAllByCategoryId(currentUser.getRestaurant().getId(), catId);

        return FindCategoryResponse.builder()
                .id(foundCategory.getId())
                .categoryName(foundCategory.getName())
                .subCategories(allSubCatByCategoryId)
                .build();
    }

}
