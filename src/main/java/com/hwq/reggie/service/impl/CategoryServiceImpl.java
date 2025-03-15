package com.hwq.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hwq.reggie.common.CustomException;
import com.hwq.reggie.entity.Category;
import com.hwq.reggie.entity.Dish;
import com.hwq.reggie.entity.Setmeal;
import com.hwq.reggie.mapper.CatecoryMapper;
import com.hwq.reggie.service.CategoryService;
import com.hwq.reggie.service.DishService;
import com.hwq.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Category Service Implementation
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CatecoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    @Override
    public void remove(Long id) {
        // Create a query wrapper to check if the category is associated with any dishes
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getId, id);
        int count1 = dishService.count(dishLambdaQueryWrapper);

        // Check if the current category is associated with any dishes, if so, throw a business exception
        if (count1 > 0) {
            // Category is associated with dishes, throw a business exception
            throw new CustomException("The category is associated with dishes and cannot be deleted.");
        }

        // Check if the current category is associated with any set meals, if so, throw a business exception
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // Add query condition to filter by category ID
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        int count2 = setmealService.count();
        if (count2 > 0) {
            // Category is associated with set meals, throw a business exception
            throw new CustomException("The category is associated with set meals and cannot be deleted.");
        }

        // Proceed with category deletion
        super.removeById(id);
    }
}