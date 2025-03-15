package com.hwq.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hwq.reggie.common.CustomException;
import com.hwq.reggie.dto.SetmealDto;
import com.hwq.reggie.entity.Setmeal;
import com.hwq.reggie.entity.SetmealDish;
import com.hwq.reggie.mapper.SetmealMapper;
import com.hwq.reggie.service.SetmealDishService;
import com.hwq.reggie.service.SetmealService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Setmeal Service Implementation
 */
@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * Add a new setmeal and save the association between the setmeal and dishes
     * @param setmealDto Data transfer object for Setmeal
     */
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        // Save basic setmeal information by inserting into the setmeal table
        this.save(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        // Save the association between setmeal and dishes by inserting into the setmeal_dish table
        setmealDishService.saveBatch(setmealDishes);
    }

    /**
     * Delete setmeals and remove their associated dish data
     * @param ids List of setmeal IDs to be deleted
     */
    @Transactional
    public void removeWithDish(List<Long> ids) {
        // Check the setmeal status to determine if deletion is allowed
        // SQL: SELECT COUNT(*) FROM setmeal WHERE id IN (1,2,3) AND status = 1
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId, ids);
        queryWrapper.eq(Setmeal::getStatus, 1);

        int count = this.count(queryWrapper);
        if (count > 0) {
            // If deletion is not allowed, throw a business exception
            throw new CustomException("The setmeal is currently being sold and cannot be deleted.");
        }

        // If allowed, first delete data from the setmeal table
        this.removeByIds(ids);

        // Delete associated data from the setmeal_dish table
        // SQL: DELETE FROM setmeal_dish WHERE setmeal_id IN (1,2,3)
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealDish::getSetmealId, ids);
        setmealDishService.remove(lambdaQueryWrapper);
    }
}