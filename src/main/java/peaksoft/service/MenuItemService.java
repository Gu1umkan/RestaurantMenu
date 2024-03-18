package peaksoft.service;

import peaksoft.dto.request.MenuItemRequest;
import peaksoft.dto.response.MenuItemResponse;
import peaksoft.dto.response.PaginationMenuItem;
import peaksoft.dto.response.SimpleResponse;

public interface MenuItemService {
    SimpleResponse save(Long subId,MenuItemRequest menuItemRequest);

    SimpleResponse deleteMenuById(Long menuId);

    SimpleResponse update(Long menuId, MenuItemRequest menuItemRequest);


    PaginationMenuItem findAllMenu(int page, int size);

    PaginationMenuItem sortMenuByPrice(int page, int size, String ascOrDesc);

    PaginationMenuItem filterByVegetarian(int page, int size, Boolean isVegan);

    PaginationMenuItem globalSearch(int page, int size, String keyWord);

    MenuItemResponse findMenuById(Long menuItemId);
}
