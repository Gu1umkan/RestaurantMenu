package peaksoft.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import peaksoft.dto.request.SubCategoryRequest;
import peaksoft.dto.response.FindSubCategoryResponse;
import peaksoft.dto.response.MenuItemResponse;
import peaksoft.dto.response.SimpleResponse;
import peaksoft.dto.response.SubCategoryResponse;
import peaksoft.entity.Category;
import peaksoft.entity.SubCategory;
import peaksoft.entity.User;
import peaksoft.enums.Role;
import peaksoft.exceptions.AlreadyExistsException;
import peaksoft.exceptions.BedRequestException;
import peaksoft.exceptions.ForbiddenException;
import peaksoft.exceptions.NotFoundException;
import peaksoft.repository.CategoryRepository;
import peaksoft.repository.MenuItemRepository;
import peaksoft.repository.SubCategoryRepository;
import peaksoft.repository.UserRepository;
import peaksoft.service.SubCategoryService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubCategoryServiceImpl implements SubCategoryService {
    private final UserRepository userRepo;
    private final CategoryRepository categoryRepo;
    private final SubCategoryRepository subCategoryRepo;
    private final MenuItemRepository menuItemRepo;

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User current = userRepo.getByEmail(email);
        if (current.getRole().equals(Role.ADMIN) || current.getRole().equals(Role.CHEF))
            return current;
        else throw new ForbiddenException("Forbidden 403");
    }

    private SubCategory getSubCategory(Long subId) {
        return subCategoryRepo.findSubIdAndRestId(subId).orElseThrow(() ->
                new NotFoundException("SubCategory not found!"));
    }

    @Override
    @Transactional
    public SimpleResponse saveSubCat(Long catId, SubCategoryRequest subCategoryRequest) {
        boolean b = subCategoryRepo.existsByName(subCategoryRequest.name());
        if(b) throw  new AlreadyExistsException("SubCategory with name: "+subCategoryRequest.name() +" already exists!");

        Category foundCategory = categoryRepo.getCategoryById(catId);
        getCurrentUser();
        subCategoryRepo.save(SubCategory.builder()
                .category(foundCategory)
                .name(subCategoryRequest.name())
                .build());

        return SimpleResponse.builder()
                .httpStatus(HttpStatus.CREATED)
                .message("subcategory with name: " + subCategoryRequest.name() + " successfully saved")
                .build();
    }

    @Override
    public List<SubCategoryResponse> findAllByCategoryId(Long catId) {
        categoryRepo.getCategoryById(catId);
        User currentUser = getCurrentUser();
        return subCategoryRepo.findAllByCategoryId(currentUser.getRestaurant().getId(), catId);
    }

    @Override
    public SimpleResponse updateSubCat(Long subCatId, SubCategoryRequest subCategoryRequest) {
        User currentUser = getCurrentUser();
        SubCategory subCategory = getSubCategory(subCatId);
        subCategory.setName(subCategoryRequest.name());
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("SubCategory with name " + subCategoryRequest.name() + " successfully updated!")
                .build();
    }

    @Override
    public SimpleResponse deleteSubCategory(Long subCatId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepo.getByEmail(email);
        SubCategory subCategory = getSubCategory(subCatId);
        subCategoryRepo.delete(subCategory);
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("SubCategory with name " + subCategory.getName() + " successfully deleted!")
                .build();
    }

    @Override
    public FindSubCategoryResponse findSubCatById(int page, int size, Long subCatId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepo.getByEmail(email);

        Pageable pageable = PageRequest.of(page - 1, size);
        SubCategory subCatById = subCategoryRepo.getSubCatById(subCatId);
        Page<MenuItemResponse> allMenuBySubCat = menuItemRepo.findAllMenuBySubCatId(subCatId, pageable);

        return FindSubCategoryResponse.builder()
                .id(subCatById.getId())
                .name(subCatById.getName())
                .menuItems(allMenuBySubCat)
                .build();
    }

}
