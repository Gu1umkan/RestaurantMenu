package peaksoft.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import peaksoft.dto.request.ClaimRequest;
import peaksoft.dto.request.SignInRequest;
import peaksoft.dto.response.SignInResponse;
import peaksoft.dto.response.SimpleResponse;
import peaksoft.service.UserService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class Auth {

    private final UserService userService;

    @PostMapping("/claim/{resId}")
    public SimpleResponse application(@RequestBody @Valid ClaimRequest claimRequest,
                                      @PathVariable Long resId){
        return userService.addApplication(resId, claimRequest);
    }

    @GetMapping("/sign-in")
    public SignInResponse signIn(@RequestBody @Valid SignInRequest signInRequest){
        return userService.signIn(signInRequest);
    }


}
