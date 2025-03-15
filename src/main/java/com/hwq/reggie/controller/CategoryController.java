package com.hwq.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hwq.reggie.common.R;
import com.hwq.reggie.entity.Category;
import com.hwq.reggie.entity.Employee;
import com.hwq.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * Add New Category
     */
    @PostMapping
    public R<String> saveCategory(@RequestBody Category category) {
        log.info(category.toString());
        categoryService.save(category);
        return R.success("Category added successfully");
    }

    /**
     * Category Pagination Query
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize) {
        log.info("Page: {} PageSize: {}", page, pageSize);

        Page<Category> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();

        // Sort by creation time in descending order
        queryWrapper.orderByDesc(Category::getCreateTime);

        IPage<Category> pages = categoryService.page(pageInfo, queryWrapper);

        return R.success(pageInfo);
    }

    /**
     * Delete Category
     */
    @DeleteMapping
    public R<String> deleteCategory(Long ids) {
        log.info("IDs: {}", ids);
        categoryService.remove(ids);
        return R.success("Category deleted successfully");
    }

    /**
     * List Categories (Data Echo)
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();

        // Filter by type if provided
        queryWrapper.eq(category.getType() != null, Category::getType, category.getType());

        // Sort by sort order and update time in descending order
        queryWrapper.orderByDesc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> list = categoryService.list(queryWrapper);

        return R.success(list);
    }
}