package com.hwq.reggie.entity;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Order Entity
 */
@Data
public class Orders implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    // Order Number
    private String number;

    // Order Status: 1 - Pending Payment, 2 - Pending Delivery, 3 - Delivered, 4 - Completed, 5 - Canceled
    private Integer status;

    // User ID (Who placed the order)
    private Long userId;

    // Address Book ID
    private Long addressBookId;

    // Order Time
    private LocalDateTime orderTime;

    // Checkout Time
    private LocalDateTime checkoutTime;

    // Payment Method: 1 - WeChat, 2 - Alipay
    private Integer payMethod;

    // Actual Amount Paid
    private BigDecimal amount;

    // Remarks
    private String remark;

    // User Name
    private String userName;

    // Phone Number
    private String phone;

    // Address
    private String address;

    // Consignee (Recipient)
    private String consignee;
}