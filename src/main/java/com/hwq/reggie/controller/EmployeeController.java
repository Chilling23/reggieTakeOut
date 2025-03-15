package com.hwq.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hwq.reggie.common.R;
import com.hwq.reggie.entity.Employee;
import com.hwq.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * Employee Login
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {

        // 1. Encrypt the submitted password using MD5
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        // 2. Query the database by username
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        // 3. If no record is found, return login failure
        if (emp == null) {
            return R.error("Login failed");
        }

        // 4. Compare passwords, return login failure if they do not match
        if (!emp.getPassword().equals(password)) {
            return R.error("Login failed");
        }

        // 5. Check employee status, return "Account disabled" if the account is disabled
        if (emp.getStatus() == 0) {
            return R.error("Account disabled");
        }

        // 6. Login successful, save employee ID in session and return success response
        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);
    }

    /**
     * Employee Logout
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        // Clear the current logged-in employee ID from session
        request.getSession().removeAttribute("employee");
        return R.success("Logout successful");
    }

    /**
     * Add New Employee
     * @param request
     * @param employee
     * @return
     */
    @PostMapping
    public R<Employee> save(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("Adding new employee, employee info: {}", employee.toString());

        // Set default password as "123456" and encrypt it using MD5
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

        // Get the currently logged-in user ID
        Object obj = request.getSession().getAttribute("employee");
        log.info("Session attribute type: " + obj.getClass().getName());

        employeeService.save(employee);
        return R.success(employee);
    }

    /**
     * Employee Pagination Query
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("page")
    public R<Page> page(int page, int pageSize, String name) {
        log.info("page: {} pageSize: {} name: {}", page, pageSize, name);

        Page<Employee> pageInfo = new Page<>(page, pageSize);

        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(name), Employee::getUsername, name);
        queryWrapper.orderByDesc(Employee::getCreateTime);

        IPage<Employee> pages = employeeService.page(pageInfo, queryWrapper);

        return R.success(pageInfo);
    }

    /**
     * Update Employee Information
     * @param request
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee) {
        log.info(employee.toString());

        employeeService.updateById(employee);

        return R.success("Employee information updated successfully");
    }

    /**
     * Get Employee by ID
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> get(@PathVariable Long id) {
        log.info("id: {}", id);
        Employee employee = employeeService.getById(id);
        if (employee != null) {
            return R.success(employee);
        }
        return R.error("User not found");
    }
}