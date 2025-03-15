package com.hwq.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hwq.reggie.common.R;
import com.hwq.reggie.dto.SetmealDto;
import com.hwq.reggie.entity.Category;
import com.hwq.reggie.entity.Setmeal;
import com.hwq.reggie.entity.SetmealDish;
import com.hwq.reggie.service.CategoryService;
import com.hwq.reggie.service.SetmealDishService;
import com.hwq.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Setmeal Management Controller
 */
@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private CategoryService categoryService;

    /**
     * Add New Setmeal
     * @param setmealDto Setmeal data transfer object
     * @return Response indicating success or failure
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        log.info("Setmeal information: {}", setmealDto);

        setmealService.saveWithDish(setmealDto);

        return R.success("Setmeal added successfully");
    }

    /**
     * Setmeal Pagination Query
     * @param page Current page number
     * @param pageSize Number of records per page
     * @param name Setmeal name for filtering
     * @return Paginated setmeal data
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){

        // Create pagination object
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        Page<SetmealDto> setmealDtoPage = new Page<>();

        // Query condition builder
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        // Add filtering condition
        queryWrapper.like(name != null, Setmeal::getName, name);
        // Add sorting condition
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        // Execute pagination query
        setmealService.page(pageInfo, queryWrapper);

        // Copy properties
        BeanUtils.copyProperties(pageInfo, setmealDtoPage, "records");

        List<Setmeal> records = pageInfo.getRecords();

        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();

            BeanUtils.copyProperties(item, setmealDto);

            Long categoryId = item.getCategoryId(); // Category ID
            // Query category by ID
            Category category = categoryService.getById(categoryId);

            if (category != null) {
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());

        setmealDtoPage.setRecords(list);

        return R.success(setmealDtoPage);
    }

    /**
     * Delete Setmeals
     * @param ids List of setmeal IDs to delete
     * @return Response indicating success or failure
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        log.info("Setmeal IDs to delete: {}", ids);
        setmealService.removeWithDish(ids);
        return R.success("Setmeals deleted successfully");
    }

    /**
     * Update Setmeal Status
     * @param status New status (e.g., enable/disable)
     * @param ids List of setmeal IDs to update
     * @return Response indicating success or failure
     */
    @PostMapping("/status/{status}")
    public R<String> updateStatus(@PathVariable Integer status, @RequestParam List<Long> ids) {
        log.info("Status: {}, Setmeal IDs to update: {}", status, ids);

        // Create update wrapper
        LambdaUpdateWrapper<Setmeal> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(Setmeal::getId, ids) // Match IDs
                .set(Setmeal::getStatus, status); // Set new status

        // Execute update operation
        boolean updateResult = setmealService.update(updateWrapper);

        if (updateResult) {
            return R.success("Status updated successfully");
        } else {
            return R.error("Status update failed");
        }
    }

    /**
     * Get List of Setmeals Based on Conditions
     * @param setmeal Setmeal filter conditions
     * @return List of setmeals matching the conditions
     */
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId() != null, Setmeal::getCategoryId, setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus() != null, Setmeal::getStatus, setmeal.getStatus());
        List<Setmeal> setmeals = setmealService.list(queryWrapper);
        return R.success(setmeals);
    }

    /**
     * Get Setmeal Details by ID
     * @param id Setmeal ID
     * @return Setmeal details including associated dishes
     */
    @GetMapping("/{id}")
    public R<SetmealDto> getById(@PathVariable Long id) {
        // Query basic setmeal information
        Setmeal setmeal = setmealService.getById(id);
        if (setmeal == null) {
            return R.error("Setmeal not found");
        }

        // Create DTO and copy basic information
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal, setmealDto);

        // Query category name
        Category category = categoryService.getById(setmeal.getCategoryId());
        if (category != null) {
            setmealDto.setCategoryName(category.getName());
        }

        // Query associated dishes for the setmeal
        LambdaQueryWrapper<SetmealDish> dishQuery = new LambdaQueryWrapper<>();
        dishQuery.eq(SetmealDish::getSetmealId, id);
        List<SetmealDish> setmealDishes = setmealDishService.list(dishQuery);

        setmealDto.setSetmealDishes(setmealDishes);

        return R.success(setmealDto);
    }
}