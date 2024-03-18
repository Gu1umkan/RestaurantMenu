package peaksoft.api;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import peaksoft.dto.request.StopListRequest;
import peaksoft.dto.request.StopRequest;
import peaksoft.dto.response.SimpleResponse;
import peaksoft.dto.response.StopListPagination;
import peaksoft.dto.response.StopListResponse;
import peaksoft.service.StopListService;

@RestController
@RequestMapping("/api/stopList")
@RequiredArgsConstructor
public class StopListApi {
    private final StopListService stopListService;


    @Secured({"ADMIN","CHEF"})
    @PostMapping("/saveStopList/{menuId}")
    public SimpleResponse save(@PathVariable Long menuId, @RequestBody StopRequest stopRequest){
        return stopListService.save(menuId,stopRequest);
    }

    @Secured({"ADMIN","CHEF"})
    @PutMapping("/update/{stopId}")
    public SimpleResponse update (@PathVariable Long stopId, @RequestBody  StopListRequest stopListRequest){
        return stopListService.update(stopId,stopListRequest);
    }

    @Secured({"ADMIN","CHEF"})
    @DeleteMapping("/removeStop/{stopId}")
    public SimpleResponse remove(@PathVariable Long stopId){
        return stopListService.removeById(stopId);
    }

    @Secured({"ADMIN","CHEF"})
    @GetMapping("getAllStop")
    public StopListPagination getAll(@RequestParam int page,@RequestParam int size){
        return stopListService.findAll(page,size);
    }


    @Secured({"ADMIN","CHEF","WAITER"})
    @GetMapping("getAllStopActive")
    public StopListPagination getAllActive(@RequestParam int page,@RequestParam int size){
        return stopListService.findAllActive(page,size);
    }
    @Secured({"ADMIN","CHEF","WAITER"})
    @GetMapping("/findStopList/{stopId}")
    public StopListResponse findStopList(@PathVariable Long stopId){
        return stopListService.findById(stopId);
    }
}
