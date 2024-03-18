package peaksoft.api;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import peaksoft.dto.request.ChequeRequest;
import peaksoft.dto.response.GetCheckResponse;
import peaksoft.dto.response.SimpleResponse;
import peaksoft.service.ChequeService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/cheque")
@RequiredArgsConstructor
public class ChequeApi {
    private final ChequeService chequeService;
    @Secured({"ADMIN", "WAITER"})
    @PostMapping("/saveCheque")
    public SimpleResponse saveCheque(@RequestBody ChequeRequest chequeRequest){
        return chequeService.saveCheque(chequeRequest);
    }


    @Secured({"ADMIN"})
    @PutMapping("/updateCheque/{chequeId}")
    public SimpleResponse update(@PathVariable Long chequeId,@RequestBody ChequeRequest chequeRequest){
        return chequeService.update(chequeId,chequeRequest);
    }

    @Secured({"ADMIN"})
    @DeleteMapping("/delete/{chequeId}")
    public SimpleResponse delete (@PathVariable Long chequeId){
        return chequeService.remove(chequeId);
    }

    @Secured("ADMIN")
    @PostMapping ("/deleteMenuOfCheck/{checkId}")
    public SimpleResponse deleteMenuOfCheck(@PathVariable Long checkId,@RequestBody ChequeRequest chequeRequest){
        return chequeService.deleteMenuOfCheck(checkId,chequeRequest);
    }

    @Secured({"ADMIN", "WAITER"})
    @GetMapping("/findAll")
    public List<GetCheckResponse> findAll(){
        return chequeService.findAllCheque();
    }

    @Secured({"ADMIN", "WAITER"})
    @GetMapping("/findCheque/{chequeId}")
    public GetCheckResponse find(@PathVariable Long chequeId){
        return chequeService.findChequeById(chequeId);
    }

    @Secured({"ADMIN", "WAITER"})
    @GetMapping("/findAllChequeOfUser/{userId}")
    public List<GetCheckResponse> getChequeByUser(@PathVariable Long userId){
        return chequeService.getChequeByUserId(userId);
    }

    @Secured({"ADMIN", "WAITER"})
    @GetMapping("/findAllChequeOfUserAndDate/{userId}")
    public String getChequeByUserAnDate(@PathVariable Long userId, @RequestParam LocalDate localDate){
        return chequeService.getChequeByUserIdAndDate(userId,localDate);
    }

    @Secured("ADMIN")
    @GetMapping("/avgRest")
    public String avgRest(){
        return chequeService.avgRest();
    }

}
