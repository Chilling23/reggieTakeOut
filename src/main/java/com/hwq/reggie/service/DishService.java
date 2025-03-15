package com.hwq.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hwq.reggie.dto.DishDto;
import com.hwq.reggie.entity.Dish;

import java.util.List;


public interface DishService extends IService<Dish> {

    /**
     * Add a new dish and insert the corresponding flavor data.
     * This operation involves two tables: dish and dish_flavor.
     * @param dishDto Data transfer object for Dish
     */
    void saveWithFlavor(DishDto dishDto);

    /**
     * Retrieve dish information along with its corresponding flavor details by ID.
     * @param id Dish ID
     * @return DishDto containing dish and flavor information
     */
    DishDto getByIdWithFlavor(Long id);

    /**
     * Update dish information and simultaneously update its corresponding flavor details.
     * @param dishDto Data transfer object for Dish
     */
    void updateWithFlavor(DishDto dishDto);

    /**
     * Remove dishes along with their flavor data.
     * @param ids List of dish IDs to be deleted
     */
    void removeWithFlavor(List<Long> ids);
}
