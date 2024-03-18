package peaksoft.service;

import peaksoft.dto.request.ClaimRequest;
import peaksoft.dto.request.SignInRequest;
import peaksoft.dto.response.PaginationUser;
import peaksoft.dto.response.SignInResponse;
import peaksoft.dto.response.SimpleResponse;
import peaksoft.dto.response.UserResponse;

import java.security.Principal;

public interface UserService {
    SimpleResponse addApplication(Long resId, ClaimRequest application);

    SignInResponse signIn(SignInRequest signInRequest);

    SimpleResponse assign(Long claimId, Principal principal);

    PaginationUser findALlUsers(int page, int size, Principal principal);
    PaginationUser findALlClaims(int page, int size, Principal principal);

    SimpleResponse deleteClaim(Long claimId, Principal principal);

    SimpleResponse removeUser(Long userId);

    UserResponse findUser(Long userId);

    UserResponse findClaimById(Long userId);
}
