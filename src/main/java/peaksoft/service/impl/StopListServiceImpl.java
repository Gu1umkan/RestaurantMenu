package peaksoft.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import peaksoft.dto.request.StopListRequest;
import peaksoft.dto.request.StopRequest;
import peaksoft.dto.response.SimpleResponse;
import peaksoft.dto.response.StopListPagination;
import peaksoft.dto.response.StopListResponse;
import peaksoft.entity.MenuItem;
import peaksoft.entity.Restaurant;
import peaksoft.entity.StopList;
import peaksoft.entity.User;
import peaksoft.enums.Role;
import peaksoft.exceptions.ForbiddenException;
import peaksoft.exceptions.NotFoundException;
import peaksoft.repository.MenuItemRepository;
import peaksoft.repository.StopListRepository;
import peaksoft.repository.UserRepository;
import peaksoft.service.StopListService;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class StopListServiceImpl implements StopListService {
    private final UserRepository userRepo;
    private final StopListRepository stopListRepo;
    private final MenuItemRepository menuItemRepo;
    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User current = userRepo.getByEmail(email);
        if (current.getRole().equals(Role.ADMIN) || current.getRole().equals(Role.CHEF))
            return current;
        else throw new ForbiddenException("Forbidden 403");
    }

    @Override @Transactional
    public SimpleResponse save(Long menuId, StopRequest stopListRequest) {

        MenuItem menuItem = menuItemRepo.getMenuById(menuId);

        if (menuItem.getStopList() != null) {
            return SimpleResponse.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message("MenuItem on stopList already exists!")
                    .build();
        }

        StopList stopList = new StopList();
        stopList.setReason(stopListRequest.message());

        System.out.println("stopListRequest.message() = " + stopListRequest.message());

        stopList.setIsActive(true);

        stopListRepo.save(stopList);
        stopList.setMenuItem(menuItem);
        menuItem.setStopList(stopList);

        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Successfully saved")
                .build();
    }

    @Override
    public SimpleResponse update(Long stopId, StopListRequest stopListRequest) {
        StopList stopListById = stopListRepo.getStopListById(stopId);
        stopListById.setReason(stopListRequest.reason());
        stopListRepo.save(stopListById);

        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Successfully updated!")
                .build();
    }

    @Override @Transactional
    public SimpleResponse removeById(Long stopId) {
        StopList stopListById = stopListRepo.getStopListById(stopId);
        MenuItem menuItem = stopListById.getMenuItem();
        menuItem.setStopList(null);
        stopListById.setIsActive(false);
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("MenuItem with name "+menuItem.getName()+" from stopList successfully deleted! ")
                .build();
    }

    @Override
    public StopListPagination findAllActive(int page, int size) {
        User user = getCurrentUser();
        Long resId = user.getRestaurant().getId();
        Pageable pageable = PageRequest.of(page - 1, size);

        Page<StopListResponse> stopLists = stopListRepo.findStopListsActiveByRestId(resId, pageable);

        if (stopLists.isEmpty()) throw new NotFoundException("Stop Lists not found");

        return StopListPagination.builder()
                .page(stopLists.getNumber() + 1)
                .size(stopLists.getTotalPages())
                .responses(stopLists.getContent())
                .build();
    }

    @Override
    public StopListPagination findAll(int page, int size) {
        User user = getCurrentUser();
        Long resId = user.getRestaurant().getId();
        Pageable pageable = PageRequest.of(page - 1, size);

        Page<StopListResponse> stopLists = stopListRepo.findStopListsByResId(resId, pageable);

        if (stopLists.isEmpty()) throw new NotFoundException("Stop Lists not found");

        return StopListPagination.builder()
                .page(stopLists.getNumber() + 1)
                .size(stopLists.getTotalPages())
                .responses(stopLists.getContent())
                .build();
    }

    @Override
    public StopListResponse findById(Long stopId) {
        User user = getCurrentUser();
        StopList list = stopListRepo.getStopListById(stopId);
        return StopListResponse.builder()
                .name(list.getMenuItem().getName())
                .reason(list.getReason())
                .date(list.getDate())
                .build();
    }



}
