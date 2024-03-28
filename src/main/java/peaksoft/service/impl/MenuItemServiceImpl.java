package peaksoft.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import peaksoft.dto.request.MenuItemRequest;
import peaksoft.dto.response.*;
import peaksoft.entity.MenuItem;
import peaksoft.entity.Restaurant;
import peaksoft.entity.SubCategory;
import peaksoft.entity.User;
import peaksoft.enums.Role;
import peaksoft.exceptions.AlreadyExistsException;
import peaksoft.exceptions.ForbiddenException;
import peaksoft.exceptions.NotFoundException;
import peaksoft.repository.MenuItemRepository;
import peaksoft.repository.SubCategoryRepository;
import peaksoft.repository.UserRepository;
import peaksoft.service.MenuItemService;

@Service
@RequiredArgsConstructor
public class MenuItemServiceImpl implements MenuItemService {
    private final SubCategoryRepository subCategoryRepo;
    private final MenuItemRepository menuItemRepo;
    private final UserRepository userRepository;


    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User current = userRepository.getByEmail(email);
        if (current.getRole().equals(Role.ADMIN) || current.getRole().equals(Role.CHEF))
            return current;
        else throw new ForbiddenException("Forbidden 403");
    }

    @Override @Transactional
    public SimpleResponse save(Long subId, MenuItemRequest menuItemRequest) {
        boolean b = menuItemRepo.existsByName(menuItemRequest.name());
        if (b) throw new AlreadyExistsException("MenuItem with name: " + menuItemRequest.name() + "already exists!");

        User user = getCurrentUser();
        Restaurant restaurant = user.getRestaurant();
        SubCategory subCategory = subCategoryRepo.getSubCatById(subId);

        MenuItem menuItem = new MenuItem();
        menuItem.setImage(menuItemRequest.image());
        menuItem.setName(menuItemRequest.name());
        menuItem.setDescription(menuItemRequest.description());
        menuItem.setPrice(menuItemRequest.price());
        menuItem.setVegetarian(menuItemRequest.isVegetarian());
        menuItem.setRestaurant(restaurant);
        menuItem.setSubCategory(subCategory);

        menuItemRepo.save(menuItem);
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.ACCEPTED)
                .message("MenuItem with name:" + menuItem.getName() + "  successfully saved!")
                .build();
    }

    @Override @Transactional
    public SimpleResponse deleteMenuById(Long menuId) {
        getCurrentUser();
        MenuItem menuItem = menuItemRepo.findById(menuId).orElseThrow(() -> new NotFoundException("Not found menu"));
        menuItemRepo.delete(menuItem);
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Menu successfully deleted")
                .build();
    }

    @Override @Transactional
    public SimpleResponse update(Long menuId, MenuItemRequest menuItemRequest) {

        boolean b = menuItemRepo.existsByName(menuItemRequest.name());
        if (b) throw new AlreadyExistsException("MenuItem with name: " + menuItemRequest.name() + "  already exists!");

        getCurrentUser();
        MenuItem menuItem = menuItemRepo.findById(menuId).orElseThrow(() -> new NotFoundException("MenuItem not found"));
        menuItem.setName(menuItemRequest.name());
        menuItem.setDescription(menuItemRequest.description());
        menuItem.setPrice(menuItemRequest.price());
        menuItem.setVegetarian(menuItemRequest.isVegetarian());
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Successfully updated!")
                .build();
    }

    @Override
    public PaginationMenuItem findAllMenu(int page, int size) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User current = userRepository.getByEmail(email);

        Long restId = current.getRestaurant().getId();

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<MenuItemResponse> menu = menuItemRepo.findAllMenuByRestaurantId(restId, pageable);

        return PaginationMenuItem.builder()
                .page(menu.getNumber() + 1)
                .size(menu.getTotalPages())
                .allMenuItems(menu.getContent())
                .build();
    }

    @Override
    public PaginationMenuItem sortMenuByPrice(int page, int size, String ascOrDesc) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User current = userRepository.getByEmail(email);

        Long restId = current.getRestaurant().getId();

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<MenuItemResponse> menu = menuItemRepo.sortMenuItemByPrice(restId,ascOrDesc,pageable);

        return PaginationMenuItem.builder()
                .page(menu.getNumber() + 1)
                .size(menu.getTotalPages())
                .allMenuItems(menu.getContent())
                .build();
    }

    @Override
    public PaginationMenuItem filterByVegetarian(int page, int size, Boolean isVegan) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User current = userRepository.getByEmail(email);

        Long restId = current.getRestaurant().getId();

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<MenuItemResponse> menu = menuItemRepo.filterByVegetarian(restId,isVegan,pageable);

        return PaginationMenuItem.builder()
                .page(menu.getNumber() + 1)
                .size(menu.getTotalPages())
                .allMenuItems(menu.getContent())
                .build();
    }

    @Override
    public PaginationMenuItem globalSearch(int page, int size, String keyWord) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User current = userRepository.getByEmail(email);

        Long restId = current.getRestaurant().getId();

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<MenuItemResponse> menu = menuItemRepo.searchMenuItem(restId,"%"+keyWord+"%",pageable);

        return PaginationMenuItem.builder()
                .page(menu.getNumber() + 1)
                .size(menu.getTotalPages())
                .allMenuItems(menu.getContent())
                .build();
    }

    @Override
    public MenuItemResponse findMenuById(Long menuItemId) {
        MenuItem menuItem = menuItemRepo.getMenuById(menuItemId);
        return MenuItemResponse.builder()
                .id(menuItem.getId())
                .name(menuItem.getName())
                .image(menuItem.getImage())
                .price(menuItem.getPrice())
                .isVegetarian(menuItem.isVegetarian())
                .build();
    }


}

