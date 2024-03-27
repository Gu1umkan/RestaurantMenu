package peaksoft.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import peaksoft.dto.request.ChequeRequest;
import peaksoft.dto.response.*;
import peaksoft.entity.Cheque;
import peaksoft.entity.MenuItem;
import peaksoft.entity.User;
import peaksoft.enums.Role;
import peaksoft.exceptions.ForbiddenException;
import peaksoft.repository.ChequeRepository;
import peaksoft.repository.MenuItemRepository;
import peaksoft.repository.UserRepository;
import peaksoft.service.ChequeService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChequeServiceImpl implements ChequeService {
    private final ChequeRepository chequeRepo;
    private final UserRepository userRepo;
    private final MenuItemRepository menuItemRepo;

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User current = userRepo.getByEmail(email);
        if (current.getRole().equals(Role.ADMIN) || current.getRole().equals(Role.WAITER)) return current;
        else throw new ForbiddenException("Forbidden 403");
    }

    @Override
    @Transactional
    public SimpleResponse saveCheque(ChequeRequest chequeRequest) {
        User currentUser = getCurrentUser();

        List<MenuItem> allMenu = new ArrayList<>();

        for (Long menuItemId : chequeRequest.menuItemIds()) {
            MenuItem menuById = menuItemRepo.getMenuById(menuItemId);
            if (menuById.getStopList() == null) allMenu.add(menuById);
        }

        log.error(String.valueOf(allMenu.size()));

        BigDecimal priceTotal = totalPrice(allMenu);

        Cheque cheque = new Cheque();

        BigDecimal service = priceTotal.multiply(BigDecimal.valueOf(cheque.getPercent() / 100.0));

        cheque.setMenuItems(allMenu);
        cheque.setTotalPrice(priceTotal);
        cheque.setService(service);
        cheque.setAllSumma(service.add(priceTotal));
        cheque.setUser(currentUser);
        currentUser.addCheque(cheque);

        chequeRepo.save(cheque);

        return SimpleResponse.builder().httpStatus(HttpStatus.OK).message(" Successfully saved!").build();

    }

    @Override
    @Transactional
    public SimpleResponse update(Long chequeId, ChequeRequest chequeRequest) {

        Long currentUserId = getCurrentUser().getId();
        Cheque updateCheque = chequeRepo.getChequeById(chequeId);
        if (!updateCheque.getUser().getId().equals(currentUserId)) throw new ForbiddenException("Forbidden");

        log.error(String.valueOf(chequeRequest.menuItemIds().size()));

        for (Long menuItemId : chequeRequest.menuItemIds()) {
            MenuItem menuById = menuItemRepo.getMenuById(menuItemId);
            if (menuById.getStopList() == null) updateCheque.getMenuItems().add(menuById);
        }

        log.error(String.valueOf(updateCheque.getMenuItems().size()));


        BigDecimal priceTotal = totalPrice(updateCheque.getMenuItems());

        Cheque cheque = chequeRepo.getChequeById(chequeId);

        BigDecimal service = priceTotal.multiply(BigDecimal.valueOf(cheque.getPercent() / 100.0));

        cheque.setTotalPrice(priceTotal);
        cheque.setService(service);
        cheque.setAllSumma(service.add(priceTotal));

        return SimpleResponse.builder().httpStatus(HttpStatus.OK).message(" Successfully updated!").build();


    }

    @Override
    @Transactional
    public SimpleResponse remove(Long chequeId) {
        Long currentUserId = getCurrentUser().getId();
        Cheque chequeById = chequeRepo.getChequeById(chequeId);
        if (chequeById.getUser().getId().equals(currentUserId)) {
            chequeRepo.delete(chequeById);
            return SimpleResponse.builder().httpStatus(HttpStatus.OK).message("Successfully deleted!").build();
        } else throw new ForbiddenException("Forbidden");

    }

    @Override
    public List<GetCheckResponse> findAllCheque() {
        List<GetCheckResponse> getAll = new ArrayList<>();
        Long restId = getCurrentUser().getRestaurant().getId();

        List<Cheque> getCheckResponses = chequeRepo.findAllCheque(restId);
        for (Cheque getCheckResponse : getCheckResponses) {
            getAll.add(findChequeById(getCheckResponse.getId()));
        }
        return getAll;
    }

    @Override
    public GetCheckResponse findChequeById(Long checkId) {
        Cheque find = chequeRepo.getChequeById(checkId);

        List<MenuResponse> menuItemResponse = menuItemRepo.menuCheckById(checkId);

        String fullName = find.getUser().getLastName() + " " + find.getUser().getFirstName();
        ChequeResponse chequeResponse = ChequeResponse.builder().service(find.getService() + " som").percent(find.getPercent() + "%").allSumma(find.getAllSumma() + " som").totalPrice(find.getTotalPrice() + " som").createdAt(find.getCreatedAt()).waiterName(fullName).build();


        return GetCheckResponse.builder().chequeResponse(chequeResponse).menuItems(menuItemResponse).build();
    }

    @Override
    public List<GetCheckResponse> getChequeByUserId(Long userId) {
        List<GetCheckResponse> getAll = new ArrayList<>();

        List<Cheque> getCheckResponses = chequeRepo.findAllChequeByUserId(userId);
        for (Cheque getCheckResponse : getCheckResponses) {
            getAll.add(findChequeById(getCheckResponse.getId()));
        }
        return getAll;
    }

    @Override
    public String getChequeByUserIdAndDate(Long userId, LocalDate localDate) {
        List<Cheque> getCheques = chequeRepo.findAllChequeByUserIdAndDate(userId, localDate);
        BigDecimal reduce = getCheques.stream().map(Cheque::getAllSumma).reduce(BigDecimal.ZERO, BigDecimal::add);
        return "date:" + localDate + "  " + reduce + " som ";

    }

    @Override
    public RestaurantCheckResponse avgRest() {
        Long restId = getCurrentUser().getRestaurant().getId();

        List<Cheque> getCheques = chequeRepo.findAllCheque(restId);
        if (!getCheques.isEmpty()) {
            BigDecimal totalSum = getCheques.stream().map(Cheque::getAllSumma).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal totalPrice = getCheques.stream().map(Cheque::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal totalService = getCheques.stream().map(Cheque::getService).reduce(BigDecimal.ZERO, BigDecimal::add);
            return RestaurantCheckResponse.builder()
                    .priceTotal(totalPrice + " som")
                    .serviceTotal(totalService + " som")
                    .totalSumma(totalSum + " som")
                    .build();
        } else throw new RuntimeException(" Restaurant's check not found! ");
    }

    @Override
    @Transactional
    public SimpleResponse deleteMenuOfCheck(Long checkId, ChequeRequest chequeRequest) {
        List<Long> menu = chequeRequest.menuItemIds();
        List<MenuItem> allMenu = menuItemRepo.getAllMenuId(menu);
        Cheque chequeById = chequeRepo.getChequeById(checkId);
        chequeById.getMenuItems().removeAll(allMenu);

        return SimpleResponse.builder().httpStatus(HttpStatus.OK).message("Success").build();
    }


    private BigDecimal totalPrice(List<MenuItem> menuItems) {
        return menuItems.stream().map(MenuItem::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}