package com.hwq.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hwq.reggie.common.R;
import com.hwq.reggie.entity.Orders;
import com.hwq.reggie.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Order Management Controller
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * User Place Order
     * @param orders Order details
     * @return Success response
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        log.info("Order data: {}", orders);
        orderService.submit(orders);
        return R.success("Order placed successfully");
    }

    /**
     * User Order Pagination Query
     * @param page Current page number
     * @param pageSize Number of records per page
     * @return Paginated order data
     */
    @GetMapping("/userPage")
    public R<IPage<Orders>> userPage(@RequestParam Integer page, @RequestParam Integer pageSize) {
        log.info("Paginated order query: page={}, pageSize={}", page, pageSize);

        // Create pagination object
        Page<Orders> pageInfo = new Page<>(page, pageSize);

        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Orders::getOrderTime);

        // Call OrderService for query
        IPage<Orders> orderPage = orderService.page(pageInfo, queryWrapper);

        return R.success(orderPage);
    }
}