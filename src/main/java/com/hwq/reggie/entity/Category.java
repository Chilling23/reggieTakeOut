package com.hwq.reggie.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Category Entity
 */
@Data
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    // Type: 1 - Dish Category, 2 - Setmeal Category
    private Integer type;

    // Category Name
    private String name;

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