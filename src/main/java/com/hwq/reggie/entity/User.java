package com.hwq.reggie.entity;

import lombok.Data;
import java.io.Serializable;

/**
 * User Entity
 */
@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    // Name
    private String name;

    // Phone Number
    private String phone;

    // Gender: 0 - Female, 1 - Male
    private String sex;

    // ID Number
    private String idNumber;

    // Avatar URL
    private String avatar;

    // Status: 0 - Disabled, 1 - Active
    private Integer status;
}