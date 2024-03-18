package peaksoft.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import peaksoft.dto.request.CategoryRequest;
import peaksoft.dto.response.CategoryResponse;
import peaksoft.dto.response.FindCategoryResponse;
import peaksoft.dto.response.SimpleResponse;
import peaksoft.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryApi {
    private final CategoryService categoryService;

    @Secured({"ADMIN","CHEF"})
    @PostMapping("/saveCat")
    public SimpleResponse save(@RequestBody @Valid CategoryRequest categoryRequest){
        return categoryService.save(categoryRequest);
    }

    @Secured({"ADMIN","CHEF"})
    @PutMapping("/updateCat/{catId}")
    public SimpleResponse update(@PathVariable Long catId,@RequestBody @Valid CategoryRequest categoryRequest){
        return categoryService.update(catId, categoryRequest);
    }

    @Secured({"ADMIN","CHEF"})
    @DeleteMapping("/deleteCat/{catId}")
    public SimpleResponse remove(@PathVariable Long catId){
        return categoryService.delete(catId);
    }


    @Secured({"ADMIN","CHEF","WAITER"})
    @GetMapping("/findAllCat")
    public List<CategoryResponse> findAll(){
        return categoryService.findAll();
    }


    @Secured({"ADMIN","CHEF","WAITER"})
    @GetMapping("/findCategoryById/{catId}")
    public FindCategoryResponse find(@PathVariable Long catId){
        return categoryService.findCategoryById(catId);
    }

}
