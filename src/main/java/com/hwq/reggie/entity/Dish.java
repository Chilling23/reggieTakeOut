package com.hwq.reggie.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Dish Entity
 */
@Data
public class Dish implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    // Dish Name
    private String name;

    // Dish Category ID
    private Long categoryId;

    // Dish Price
    private BigDecimal price;

    // Product Code
    private String code;

    // Image URL
    private String image;

    // Description
    private String description;

    // Status: 0 - Unavailable, 1 - Available
    private Integer status;

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
}