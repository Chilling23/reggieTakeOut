package com.hwq.reggie.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hwq.reggie.entity.Orders;


public interface OrderService extends IService<Orders> {


    public void submit(Orders orders);


}
