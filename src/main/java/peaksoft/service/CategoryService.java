package peaksoft.service;

import peaksoft.dto.request.CategoryRequest;
import peaksoft.dto.response.CategoryResponse;
import peaksoft.dto.response.FindCategoryResponse;
import peaksoft.dto.response.SimpleResponse;

import java.util.List;

public interface CategoryService {

    SimpleResponse save(CategoryRequest categoryRequest);

    SimpleResponse update(Long catId,CategoryRequest categoryRequest);
    SimpleResponse delete(Long catId);

    List<CategoryResponse> findAll();

    FindCategoryResponse findCategoryById(Long catId);
}
