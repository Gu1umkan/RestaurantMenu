package peaksoft.service;

import org.springframework.data.domain.Pageable;
import peaksoft.dto.request.SubCategoryRequest;
import peaksoft.dto.response.FindCategoryResponse;
import peaksoft.dto.response.FindSubCategoryResponse;
import peaksoft.dto.response.SimpleResponse;
import peaksoft.dto.response.SubCategoryResponse;

import java.util.List;

public interface SubCategoryService {
    SimpleResponse saveSubCat(Long catId, SubCategoryRequest subCategoryRequest);

    List<SubCategoryResponse> findAllByCategoryId(Long catId);

    SimpleResponse updateSubCat(Long subCat, SubCategoryRequest subCategoryRequest);

    SimpleResponse deleteSubCategory(Long subCatId);

    FindSubCategoryResponse findSubCatById(int page,int size,Long subCatId);
}
