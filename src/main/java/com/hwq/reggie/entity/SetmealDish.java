package com.hwq.reggie.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Setmeal-Dish Relationship Entity
 */
@Data
public class SetmealDish implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    // Setmeal ID
    private Long setmealId;

    // Dish ID
    private Long dishId;

    // Dish Name (Redundant Field)
    private String name;

    // Dish Original Price
    private BigDecimal price;

    // Quantity
    private Integer copies;

    // Sorting Order
    private Integer sort;

    // Creation Time
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    // Update Time
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    // Created By
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    // Updated By
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    // Is Deleted (Soft Delete Flag)
    private Integer isDeleted;
}