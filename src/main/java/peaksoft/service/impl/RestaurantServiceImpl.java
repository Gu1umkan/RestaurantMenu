package peaksoft.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import peaksoft.dto.request.RestaurantRequest;
import peaksoft.dto.response.RestaurantResponse;
import peaksoft.dto.response.SimpleResponse;
import peaksoft.entity.Restaurant;
import peaksoft.entity.User;
import peaksoft.enums.Role;
import peaksoft.exceptions.AlreadyExistsException;
import peaksoft.exceptions.ForbiddenException;
import peaksoft.repository.CategoryRepository;
import peaksoft.repository.MenuItemRepository;
import peaksoft.repository.RestaurantRepository;
import peaksoft.repository.UserRepository;
import peaksoft.service.RestaurantService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final MenuItemRepository menuItemRepository;
    private final CategoryRepository categoryRepository;
    private final PasswordEncoder passwordEncoder;

    @Override @Transactional
    public SimpleResponse save(RestaurantRequest restaurantRequest) {
        if (restaurantRepository.existsByName(restaurantRequest.name()))
            throw new AlreadyExistsException("Restaurant with name:" + restaurantRequest.name() + " already exists!");

        if (userRepository.existsByEmail(restaurantRequest.emailAdmin())) {
            throw new AlreadyExistsException(String.format
                    ("User with email %s already exists", restaurantRequest.emailAdmin()));
        }
            Restaurant restaurant = restaurantRepository.save(Restaurant.builder()
                    .name(restaurantRequest.name())
                    .location(restaurantRequest.location())
                    .service(restaurantRequest.service())
                    .restType(restaurantRequest.restType())
                    .build());

        User user = userRepository.save(User.builder()
                .restaurant(restaurant)
                .role(Role.ADMIN)
                .email(restaurantRequest.emailAdmin())
                .password(passwordEncoder.encode(restaurantRequest.password()))
                .build());

        restaurant.addUser(user);
        restaurant.setNumberOfEmployees(restaurant.getUsers().size());
            return SimpleResponse.builder()
                    .httpStatus(HttpStatus.OK)
                    .message("Successfully saved!").build();
        }

        @Override @Transactional
        public SimpleResponse update (Long restId, RestaurantRequest restaurantRequest){
            Restaurant restaurant = restaurantRepository.getResById(restId);
            User user = getCurrentuser(restId);

            if (restaurantRepository.existsByName(restaurantRequest.name()) && !restaurant.getName().equals(restaurantRequest.name()))
                throw new AlreadyExistsException("Restaurant with name:" + restaurantRequest.name() + " already exists!");

            if (userRepository.existsByEmail(restaurantRequest.emailAdmin())) {
                throw new AlreadyExistsException(String.format
                        ("User with email %s already exists", restaurantRequest.emailAdmin()));
            }

            restaurant.setName(restaurantRequest.name());
            restaurant.setLocation(restaurantRequest.location());
            restaurant.setService(restaurantRequest.service());
            restaurant.setRestType(restaurantRequest.restType());

            user.setEmail(restaurantRequest.emailAdmin());
            user.setPassword(passwordEncoder.encode(restaurantRequest.password()));

            return SimpleResponse.builder()
                    .httpStatus(HttpStatus.OK)
                    .message("Successfully updated!")
                    .build();
        }

        @Override
        public RestaurantResponse findById (Long restId){
            Restaurant restaurant = restaurantRepository.getResById(restId);

            return RestaurantResponse.builder()
                    .name(restaurant.getName())
                    .Location(restaurant.getLocation())
                    .numberOfEmployees(restaurant.getUsers().size())
                    .restType(restaurant.getRestType())
                    .service(restaurant.getService())
                    .build();
        }

        @Override
        public List<RestaurantResponse> findAll () {
            List<Restaurant> allRest = restaurantRepository.findAll();
            List<RestaurantResponse> allRestaurant = new ArrayList<>();

            for (Restaurant restaurant : allRest) {
                allRestaurant.add(findById(restaurant.getId()));
            }
            return allRestaurant;
        }

        @Override
        public SimpleResponse delete (Long restId){
            Restaurant restaurant = restaurantRepository.getResById(restId);

//            restaurant.getMenuItems()
            categoryRepository.deleteAll(restaurant.getCategories());
            restaurantRepository.delete(restaurant);

            return SimpleResponse.builder()
                    .httpStatus(HttpStatus.OK)
                    .message("Successfully deleted!")
                    .build();
        }

    private User getCurrentuser (Long restId){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentuser = userRepository.getByEmail(email);
        if (currentuser.getRole().equals(Role.ADMIN) && currentuser.getRestaurant().getId().equals(restId))
            return currentuser;
        else throw new ForbiddenException("Forbidden 403");
    }
    }
