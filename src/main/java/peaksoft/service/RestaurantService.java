package peaksoft.service;

import peaksoft.dto.request.RestaurantRequest;
import peaksoft.dto.response.RestaurantResponse;
import peaksoft.dto.response.SimpleResponse;

import java.util.List;

public interface RestaurantService {
    SimpleResponse save(RestaurantRequest restaurantRequest);

    SimpleResponse update(Long restId, RestaurantRequest restaurantRequest);

    RestaurantResponse findById(Long restId);

    List<RestaurantResponse> findAll();

    SimpleResponse delete(Long restId);
}
