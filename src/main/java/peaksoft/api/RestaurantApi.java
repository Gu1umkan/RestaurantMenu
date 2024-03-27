package peaksoft.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import peaksoft.dto.request.RestaurantRequest;
import peaksoft.dto.response.RestaurantResponse;
import peaksoft.dto.response.SimpleResponse;
import peaksoft.service.RestaurantService;

import java.util.List;

@RestController
@RequestMapping("/api/restaurant")
@RequiredArgsConstructor
public class RestaurantApi {
    private final RestaurantService restaurantService;

    @Secured("PROGRAMMER")
    @PostMapping("/save")
    public SimpleResponse save(@RequestBody @Valid RestaurantRequest restaurantRequest){
        return restaurantService.save(restaurantRequest);
    }

    @Secured({"PROGRAMMER","ADMIN"})
    @PutMapping("/update/{restId}")
    public SimpleResponse update(@PathVariable Long restId,@RequestBody @Valid RestaurantRequest restaurantRequest){
        return restaurantService.update(restId,restaurantRequest);
    }

    @Secured({"PROGRAMMER","ADMIN","WAITER","CHEF"})
    @GetMapping("/findById/{restId}")
    public RestaurantResponse find(@PathVariable Long restId){
        return restaurantService.findById(restId);
    }

    @Secured({"PROGRAMMER","ADMIN"})
    @GetMapping("/findAll")
    public List<RestaurantResponse> findAll(){
        return restaurantService.findAll();
    }

    @Secured({"PROGRAMMER","ADMIN"})
    @DeleteMapping("/delete/{restId}")
    public SimpleResponse delete(@PathVariable Long restId){
        return restaurantService.delete(restId);
    }
}
