package com.hwq.reggie.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Address Book Entity
 */
@Data
public class AddressBook implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    // User ID
    private Long userId;

    // Consignee (Recipient)
    private String consignee;

    // Phone Number
    private String phone;

    // Gender: 0 - Female, 1 - Male
    private String sex;

    // Province Code
    private String provinceCode;

    // Province Name
    private String provinceName;

    // City Code
    private String cityCode;

    // City Name
    private String cityName;

    // District Code
    private String districtCode;

    // District Name
    private String districtName;

    // Detailed Address
    private String detail;

    // Label
    private String label;

    // Is Default: 0 - No, 1 - Yes
    private Integer isDefault;

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