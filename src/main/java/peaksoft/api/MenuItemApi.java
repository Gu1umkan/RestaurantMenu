package peaksoft.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import peaksoft.dto.request.MenuItemRequest;
import peaksoft.dto.response.MenuItemResponse;
import peaksoft.dto.response.PaginationMenuItem;
import peaksoft.dto.response.SimpleResponse;
import peaksoft.service.MenuItemService;

@RestController
@RequestMapping("/api/menu")
@RequiredArgsConstructor
public class MenuItemApi {

    private final MenuItemService menuItemService;

    @Secured({"ADMIN","CHEF"})
    @PostMapping("/saveMenu/{subId}")
    public SimpleResponse saveMenu(@PathVariable Long subId, @RequestBody @Valid MenuItemRequest menuItemRequest) {
        return menuItemService.save(subId, menuItemRequest);
    }

    @Secured({"ADMIN","CHEF"})
    @DeleteMapping("/deleteMenu/{menuId}")
    public SimpleResponse delete(@PathVariable Long menuId) {
        return menuItemService.deleteMenuById(menuId);
    }

    @Secured({"ADMIN","CHEF"})
    @PutMapping("/updateMenu/{menuId}")
    public SimpleResponse update(@PathVariable Long menuId, @RequestBody @Valid MenuItemRequest menuItemRequest) {
      return menuItemService.update(menuId,menuItemRequest);
    }


    @Secured({"ADMIN","CHEF","WAITER"})
    @GetMapping("/getAllMenu")
    public PaginationMenuItem findAll(@RequestParam int page,
                                      @RequestParam int size){
        return menuItemService.findAllMenu(page,size);
    }

    @Secured({"ADMIN","CHEF","WAITER"})
    @GetMapping("/sortMenu")
    public PaginationMenuItem sortMenuByPrice(@RequestParam  int page,
                                      @RequestParam int size,
                                      @RequestParam String ascOrDesc){
        return menuItemService.sortMenuByPrice(page,size,ascOrDesc);
    }

    @Secured({"ADMIN","CHEF","WAITER"})
    @GetMapping("/isVegan")
    public PaginationMenuItem filterByVegetarian(@RequestParam int page,
                                              @RequestParam int size,
                                              @RequestParam Boolean isVegan){
        return menuItemService.filterByVegetarian(page,size,isVegan);
    }

    @Secured({"ADMIN","CHEF","WAITER"})
    @GetMapping("/globalSearch")
    public PaginationMenuItem globalSearch(@RequestParam int page,
                                                 @RequestParam int size,
                                                 @RequestParam String keyWord){
        return menuItemService.globalSearch(page,size,keyWord);
    }
    @Secured({"ADMIN","CHEF","WAITER"})
    @GetMapping("/findMenuItem/{menuItemId}")
    public MenuItemResponse findMenuItem(@PathVariable Long menuItemId){
        return menuItemService.findMenuById(menuItemId);
    }


}
