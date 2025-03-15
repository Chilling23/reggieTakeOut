package com.hwq.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hwq.reggie.common.R;
import com.hwq.reggie.entity.User;
import com.hwq.reggie.service.UserService;
import com.hwq.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * User Management Controller
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Send Mobile SMS Verification Code
     * @param user User object containing phone number
     * @return Response indicating success or failure
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        // Get phone number
        String phone = user.getPhone();
        log.info("Phone number: {}", phone);

        if(StringUtils.isNotEmpty(phone)){
            // Generate a random 4-digit verification code
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("Generated verification code: {}", code);

            // Call Alibaba Cloud SMS API to send the message
            // SMSUtils.sendMessage("Reggie Takeaway", "", phone, code);

            // Save the generated verification code in the session
            session.setAttribute(phone, code);

            return R.success("SMS verification code sent successfully");
        }

        return R.error("Failed to send SMS");
    }

    /**
     * User Login via Mobile
     * @param map Request body containing phone number and verification code
     * @param session HttpSession for storing user session data
     * @return Response with user information if login is successful
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map<String, String> map, HttpSession session){
        log.info("Login request data: {}", map);

        String phone = map.get("phone");
        String code = map.get("code");

        Object codeInSession = session.getAttribute(phone);
        log.info("User entered code: {}, Stored code: {}", code, codeInSession);

        if (codeInSession != null && codeInSession.equals(code)) {
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, phone);

            User user = userService.getOne(queryWrapper);
            if (user == null) {
                log.info("User not found, creating a new user for phone: {}", phone);
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }

            log.info("User ID stored in session: {}", user.getId());
            session.setAttribute("user", user.getId());
            return R.success(user);
        }

        log.warn("Login failed: incorrect verification code.");
        return R.error("Login failed");
    }

    /**
     * User Logout
     * @param session HttpSession for invalidating the session
     * @return Redirect URL to login page
     */
    @PostMapping("/loginout")
    public String logout(HttpSession session) {
        session.invalidate();
        log.info("User has logged out");

        // Redirect to login page
        return "redirect:/front/page/login.html";
    }
}