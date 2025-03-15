package com.hwq.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hwq.reggie.common.BaseContext;
import com.hwq.reggie.common.R;
import com.hwq.reggie.entity.ShoppingCart;
import com.hwq.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Shopping Cart Controller
 */
@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * Add to Shopping Cart
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        log.info("Shopping cart data: {}", shoppingCart);

        // Set user ID to specify which user's cart data this belongs to
        // User is logged in, so get the user ID from BaseContext
        Long currentId = BaseContext.getId();
        shoppingCart.setUserId(currentId);

        Long dishId = shoppingCart.getDishId();

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, currentId);

        if (dishId != null) {
            // Adding a dish to the shopping cart
            queryWrapper.eq(ShoppingCart::getDishId, dishId);
        } else {
            // Adding a set meal to the shopping cart
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }

        // Check if the dish or set meal is already in the shopping cart
        // SQL: select * from shopping_cart where user_id = ? and dish_id/setmeal_id = ?
        ShoppingCart cartServiceOne = shoppingCartService.getOne(queryWrapper);

        if (cartServiceOne != null) {
            // If it already exists, increase the quantity by one
            Integer number = cartServiceOne.getNumber();
            cartServiceOne.setNumber(number + 1);
            shoppingCartService.updateById(cartServiceOne);
        } else {
            // If it does not exist, add it to the shopping cart with a default quantity of one
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            cartServiceOne = shoppingCart;
        }

        return R.success(cartServiceOne);
    }

    /**
     * View Shopping Cart
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        log.info("Viewing shopping cart...");

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getId());
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);

        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);

        return R.success(list);
    }

    /**
     * Clear Shopping Cart
     * @return
     */
    @DeleteMapping("/clean")
    public R<String> clean(){
        // SQL: delete from shopping_cart where user_id = ?

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getId());

        shoppingCartService.remove(queryWrapper);

        return R.success("Shopping cart cleared successfully");
    }

    /**
     * Remove Dish or Set Meal from Shopping Cart
     * @param shoppingCart
     * @return
     */
    @PostMapping("/sub")
    public R<List<ShoppingCart>> sub(@RequestBody ShoppingCart shoppingCart) {
        log.info("Removing item from shopping cart: {}", shoppingCart);

        // Get current user ID
        Long currentId = BaseContext.getId();
        shoppingCart.setUserId(currentId);

        Long dishId = shoppingCart.getDishId();

        // Query conditions
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, currentId);

        if (dishId != null) {
            // Removing a dish
            queryWrapper.eq(ShoppingCart::getDishId, dishId);
        } else {
            // Removing a set meal
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }

        // Check if the dish or set meal exists in the shopping cart
        ShoppingCart cartServiceOne = shoppingCartService.getOne(queryWrapper);

        if (cartServiceOne != null) {
            Integer number = cartServiceOne.getNumber();
            if (number > 1) {
                // If quantity is greater than 1, decrease the quantity
                cartServiceOne.setNumber(number - 1);
                shoppingCartService.updateById(cartServiceOne);
            } else {
                // If quantity is 1, remove the item from the shopping cart
                shoppingCartService.removeById(cartServiceOne.getId());
            }
        } else {
            return R.error("Item not found in shopping cart");
        }

        // Retrieve updated shopping cart list
        LambdaQueryWrapper<ShoppingCart> newQueryWrapper = new LambdaQueryWrapper<>();
        newQueryWrapper.eq(ShoppingCart::getUserId, currentId);
        newQueryWrapper.orderByAsc(ShoppingCart::getCreateTime);

        List<ShoppingCart> updatedCartList = shoppingCartService.list(newQueryWrapper);

        return R.success(updatedCartList);
    }
}