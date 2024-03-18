package peaksoft.api;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import peaksoft.dto.response.PaginationUser;
import peaksoft.dto.response.SimpleResponse;
import peaksoft.dto.response.UserResponse;
import peaksoft.service.UserService;

import java.security.Principal;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class UserApi {
    private final UserService userService;

    @Secured("ADMIN")
    @GetMapping("/findAllEmployees")
    public PaginationUser findAll(@RequestParam int page,
                                  @RequestParam int size,
                                  Principal principal){
        return userService.findALlUsers(page, size, principal);
    }
    @Secured("ADMIN")
    @GetMapping("/findAllClaims")
    public PaginationUser findAllClaims(@RequestParam int page,
                                        @RequestParam int size,
                                        Principal principal){
        return userService.findALlClaims(page, size, principal);
    }

    @Secured("ADMIN")
    @GetMapping ("/assign/{claimId}")
    public SimpleResponse  assign(@PathVariable Long claimId, Principal principal){
     return userService.assign(claimId, principal);
    }

    @Secured("ADMIN")
    @DeleteMapping("/deleteClaim/{claimId}")
    public SimpleResponse deleteClaim(@PathVariable Long claimId,Principal principal){
        return userService.deleteClaim(claimId,principal);
    }

    @Secured("ADMIN")
    @DeleteMapping("/removeEmployee/{userId}")
    public SimpleResponse remove(@PathVariable Long userId){
        return userService.removeUser(userId);
    }

    @Secured("ADMIN")
    @GetMapping("/findEmployee/{userId}")
    public UserResponse findUser(@PathVariable Long userId){
        return userService.findUser(userId);
    }

    @Secured("ADMIN")
    @GetMapping("/findClaim/{claimId}")
    public UserResponse findClaim(@PathVariable Long claimId){
        return userService.findClaimById(claimId);
    }

}

