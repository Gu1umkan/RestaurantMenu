package peaksoft.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import peaksoft.dto.request.CategoryRequest;
import peaksoft.dto.request.SubCategoryRequest;
import peaksoft.dto.response.*;
import peaksoft.service.SubCategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/subCategory")
@RequiredArgsConstructor
public class SubCategoryApi {
    private final SubCategoryService subCategoryService;

    @Secured({"ADMIN","CHEF"})
    @PostMapping("/saveSubCat/{catId}")
    public SimpleResponse save(@PathVariable Long catId,@RequestBody SubCategoryRequest subCategoryRequest){
        return subCategoryService.saveSubCat(catId,subCategoryRequest);
    }


    @Secured({"ADMIN","CHEF"})
    @PutMapping("/updateSubCat/{subCatId}")
    public SimpleResponse update(@PathVariable Long subCatId, @RequestBody SubCategoryRequest subCategoryRequest){
        return subCategoryService.updateSubCat(subCatId, subCategoryRequest);
    }

    @Secured({"ADMIN","CHEF"})
    @DeleteMapping("/deleteSubCat/{subCatId}")
    public SimpleResponse remove(@PathVariable Long subCatId){
        return subCategoryService.deleteSubCategory(subCatId);
    }


    @Secured({"ADMIN","CHEF","WAITER"})
    @GetMapping("/findAllSubCat/{catId}")
    public List<SubCategoryResponse> findAll(@PathVariable Long catId){
        return subCategoryService.findAllByCategoryId(catId);
    }


    @Secured({"ADMIN","CHEF","WAITER"})
    @GetMapping("/findSubCategoryById/{subCatId}")
    public FindSubCategoryResponse find(@PathVariable Long subCatId,@RequestParam int page, @RequestParam int size){
        return subCategoryService.findSubCatById(page,size,subCatId);
    }
}
