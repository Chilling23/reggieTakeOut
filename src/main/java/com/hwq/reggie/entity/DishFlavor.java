package com.hwq.reggie.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Dish Flavor Entity
 */
@Data
public class DishFlavor implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    // Dish ID
    private Long dishId;

    // Flavor Name
    private String name;

    // Flavor Data List (JSON or CSV format)
    private String value;

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

    // Is Deleted (Soft delete flag)
    private Integer isDeleted;
}