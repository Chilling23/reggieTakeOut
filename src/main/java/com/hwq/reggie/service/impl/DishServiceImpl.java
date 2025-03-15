package com.hwq.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hwq.reggie.common.CustomException;
import com.hwq.reggie.dto.DishDto;
import com.hwq.reggie.entity.Dish;
import com.hwq.reggie.entity.DishFlavor;
import com.hwq.reggie.mapper.DishMapper;
import com.hwq.reggie.service.DishFlavorService;
import com.hwq.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Dish Service Implementation
 */
@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * Remove dishes along with their flavor data
     * @param ids List of dish IDs to be deleted
     */
    @Override
    @Transactional
    public void removeWithFlavor(List<Long> ids) {
        // First check if the dishes are currently being sold (status=1). If so, they cannot be deleted.
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Dish::getId, ids).eq(Dish::getStatus, 1);
        int count = this.count(queryWrapper);

        if (count > 0) {
            throw new CustomException("Dishes are currently being sold and cannot be deleted!");
        }

        // Delete data from the dish table
        this.removeByIds(ids);

        // Delete the corresponding flavor data from the dish_flavor table
        LambdaQueryWrapper<DishFlavor> flavorQueryWrapper = new LambdaQueryWrapper<>();
        flavorQueryWrapper.in(DishFlavor::getDishId, ids);
        dishFlavorService.remove(flavorQueryWrapper);
    }

    /**
     * Add a new dish and save its corresponding flavor data
     * @param dishDto Data transfer object for Dish
     */
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        // Save basic dish information to the dish table
        this.save(dishDto);

        Long dishId = dishDto.getId(); // Dish ID

        // Retrieve dish flavors and associate them with the dish ID
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());

        // Save dish flavor data to the dish_flavor table
        dishFlavorService.saveBatch(flavors);
    }

    /**
     * Retrieve dish information along with its flavor details by ID
     * @param id Dish ID
     * @return DishDto containing dish and flavor information
     */
    public DishDto getByIdWithFlavor(Long id) {
        // Retrieve basic dish information from the dish table
        Dish dish = this.getById(id);

        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);

        // Retrieve corresponding flavor information from the dish_flavor table
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getId() != null, DishFlavor::getDishId, dish.getId());
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(flavors);

        return dishDto;
    }

    /**
     * Update dish information along with its flavors
     * @param dishDto Data transfer object for Dish
     */
    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        // Update basic dish information in the dish table
        this.updateById(dishDto);

        // Remove existing flavor data for the dish from the dish_flavor table
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        dishFlavorService.remove(queryWrapper);

        // Add new flavor data provided in the request to the dish_flavor table
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);
    }
}