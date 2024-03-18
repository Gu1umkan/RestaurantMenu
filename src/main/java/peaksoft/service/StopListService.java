package peaksoft.service;

import peaksoft.dto.request.StopListRequest;
import peaksoft.dto.request.StopRequest;
import peaksoft.dto.response.SimpleResponse;
import peaksoft.dto.response.StopListPagination;
import peaksoft.dto.response.StopListResponse;

public interface StopListService {
    StopListPagination findAll(int page, int size);

     StopListResponse findById(Long stopId);

    SimpleResponse save(Long menuId, StopRequest stopListRequest);

    SimpleResponse update(Long stopId, StopListRequest stopListRequest);

    SimpleResponse removeById(Long stopId);
    StopListPagination findAllActive(int page, int size);
}
