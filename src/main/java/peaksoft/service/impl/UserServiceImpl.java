package peaksoft.service.impl;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import peaksoft.config.jwt.JwtService;
import peaksoft.dto.request.ClaimRequest;
import peaksoft.dto.request.SignInRequest;
import peaksoft.dto.response.*;
import peaksoft.entity.Claim;
import peaksoft.entity.Restaurant;
import peaksoft.entity.User;
import peaksoft.enums.RestType;
import peaksoft.enums.Role;
import peaksoft.exceptions.AlreadyExistsException;
import peaksoft.exceptions.BedRequestException;
import peaksoft.exceptions.ForbiddenException;
import peaksoft.exceptions.NotFoundException;
import peaksoft.repository.ClaimRepository;
import peaksoft.repository.RestaurantRepository;
import peaksoft.repository.UserRepository;
import peaksoft.service.UserService;

import java.security.Principal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final RestaurantRepository restaurantRepo;
    private final UserRepository userRepository;
    private final ClaimRepository claimRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @PostConstruct
    private void saveProgrammer() {
        userRepository.save(
                User.builder()
                        .email("programmer@gmail.com")
                        .password(passwordEncoder.encode("programmer"))
                        .role(Role.PROGRAMMER)
                        .build()
        );

        Restaurant restaurant = restaurantRepo.save(
                Restaurant.builder()
                        .name("Java-12")
                        .location("Bishkek Jal-29")
                        .restType(RestType.NATIONAL)
                        .build()
        );
        User user = userRepository.save(
                User.builder()
                        .role(Role.ADMIN)
                        .email("admin@gmail.com")
                        .password(passwordEncoder.encode("admin"))
                        .restaurant(restaurant)
                        .build()

        );
        restaurant.addUser(user);
    }

    private User getCurrentUser(Principal principal) {
        String email = principal.getName();
        return userRepository.getByEmail(email);
    }

    @Override
    @Transactional
    public SimpleResponse addApplication(Long resId, ClaimRequest application) {
        Restaurant restaurant = restaurantRepo.getResById(resId);
        if (userRepository.existsByEmail(application.getEmail())) {
            throw new AlreadyExistsException(String.format("User with email %s already exists", application.getEmail()));
        } else {
            claimRepo.save(Claim.builder()
                    .firstName(application.getFirstName())
                    .lastName(application.getLastName())
                    .email(application.getEmail())
                    .password(passwordEncoder.encode(application.getPassword()))
                    .role(application.getRole())
                    .phoneNumber(application.getPhoneNumber())
                    .experience(application.getExperience())
                    .dateOfBirth(application.getDateOfBirth())
                    .restaurant(restaurant)
                    .build());

            return SimpleResponse.builder()
                    .httpStatus(HttpStatus.OK)
                    .message("Successfully saved your application!")
                    .build();
        }
    }

    @Override
    public SignInResponse signIn(SignInRequest signInRequest) {
        User user = userRepository.getByEmail(signInRequest.getEmail());
        String encodePassword = user.getPassword();
        String password = signInRequest.getPassword();

        boolean matches = passwordEncoder.matches(password, encodePassword);

        if (!matches) throw new RuntimeException("Invalid password");
        return SignInResponse.builder()
                .token(jwtService.createToken(user))
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    private void checkApplication(Claim claim) {

        if (claim.getRole().equals(Role.CHEF)) {

            int year = claim.getDateOfBirth().getYear();
            int currentYear = LocalDate.now().getYear();
            int age = currentYear - year;
            if (age >= 45 || age < 25) {
                throw new BedRequestException("The age limit should be from 25 to 45");
            }
            if (claim.getExperience() < 2) {
                throw new BedRequestException("experience of at least 2 years");
            }
        } else if (claim.getRole().equals(Role.WAITER)) {
            int year = claim.getDateOfBirth().getYear();
            int currentYear = LocalDate.now().getYear();
            int age = currentYear - year;
            if (age >= 30 || age < 18) {
                throw new BedRequestException("The age limit should be from 18 to 30");
            }
            if (claim.getExperience() < 1) {
                throw new BedRequestException("experience of at least 1 years");
            }
        }
    }

    @Override
    @Transactional
    public SimpleResponse assign(Long claimId, Principal principal) {

        Claim claim = claimRepo.getClaimById(claimId);
        User currentUser = getCurrentUser(principal);
        checkApplication(claim);
        Restaurant restaurant = claim.getRestaurant();

        if (currentUser.getRole().equals(Role.ADMIN)
                && restaurant.getUsers().contains(currentUser)
                && restaurant.getUsers().size() < 15) {
            User user = new User();
            user.setEmail(claim.getEmail());
            user.setPassword(claim.getPassword());
            user.setDateOfBirth(claim.getDateOfBirth());
            user.setExperience(claim.getExperience());
            user.setPhoneNumber(claim.getPhoneNumber());
            user.setFirstName(claim.getFirstName());
            user.setLastName(claim.getLastName());
            user.setRole(claim.getRole());
            user.setRestaurant(claim.getRestaurant());
            userRepository.save(user);
            claimRepo.delete(claim);
            return SimpleResponse.builder()
                    .httpStatus(HttpStatus.ACCEPTED)
                    .message("with email : " + user.getEmail() + " successfully assign to restaurant")
                    .build();
        } else throw new RuntimeException("Restaurant is no vacancy");
    }

    @Override
    public PaginationUser findALlUsers(int page, int size, Principal principal) {

        User user = getCurrentUser(principal);

        if (user.getRole().equals(Role.ADMIN)) {
            Pageable pageable = PageRequest.of(page - 1, size);
            Page<AllUsersResponse> users = userRepository.findAllByRestaurantId(user.getRestaurant().getId(), pageable);
            return PaginationUser.builder()
                    .page(users.getNumber() + 1)
                    .size(users.getTotalPages())
                    .allUsersResponses(users.getContent())
                    .build();
        } else throw new ForbiddenException("Forbidden 403");
    }

    @Override
    public PaginationUser findALlClaims(int page, int size, Principal principal) {

        User user = getCurrentUser(principal);

        if (user.getRole().equals(Role.ADMIN)) {
            Pageable pageable = PageRequest.of(page - 1, size);
            Page<AllUsersResponse> claims = claimRepo.findAllClaimsByRestaurantId(user.getRestaurant().getId(), pageable);

            return PaginationUser.builder()
                    .page(claims.getNumber() + 1)
                    .size(claims.getTotalPages())
                    .allUsersResponses(claims.getContent())
                    .build();
        } else throw new ForbiddenException("Forbidden 403");
    }

    @Override
    @Transactional
    public SimpleResponse deleteClaim(Long claimId, Principal principal) {
        User user = getCurrentUser(principal);
        Claim claim = claimRepo.findById(claimId).orElseThrow(() -> new NotFoundException("Claim with id: " + claimId + " not found!"));

        if (user.getRole().equals(Role.ADMIN)
                && claim.getRestaurant().getId().equals(user.getRestaurant().getId())) {
            claimRepo.delete(claim);
            return SimpleResponse.builder()
                    .httpStatus(HttpStatus.ACCEPTED)
                    .message("Successfully deleted!")
                    .build();
        } else throw new RuntimeException("Invalid user");

    }

    @Override
    public SimpleResponse removeUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("User with id:" + userId + " not found"));
        userRepository.deleteById(user.getId());
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("User with email:" + user.getEmail() + " successfully deleted!")
                .build();
    }

    @Override
    public UserResponse findUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("User with id:" + userId + "not found"));
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .dateOfBirth(user.getDateOfBirth())
                .experience(user.getExperience())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .build();
    }

    @Override
    public UserResponse findClaimById(Long claimId) {
        Claim claim = claimRepo.findById(claimId).orElseThrow(() -> new NotFoundException("Claim with id: " + claimId + " not found!"));
        return UserResponse.builder()
                .id(claim.getId())
                .lastName(claim.getLastName())
                .firstName(claim.getFirstName())
                .email(claim.getEmail())
                .phoneNumber(claim.getPhoneNumber())
                .role(claim.getRole())
                .experience(claim.getExperience())
                .dateOfBirth(claim.getDateOfBirth())
                .build();
    }

}
