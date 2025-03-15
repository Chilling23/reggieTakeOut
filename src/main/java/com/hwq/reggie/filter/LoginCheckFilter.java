package com.hwq.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.hwq.reggie.common.R;
import com.hwq.reggie.common.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter to check if the user is logged in
 */
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {

    // Path matcher that supports wildcards
    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    // Define paths that do not require authentication
    private static final String[] EXCLUDED_PATHS = {
            "/employee/login",
            "/employee/logout",
            "/backend/**",
            "/front/**",
            "/common/**",
            "/user/sendMsg",
            "/user/login"
    };

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String requestURI = request.getRequestURI();
        log.info("Intercepted request: {}", requestURI);

        // 1. Check if the request should be excluded from authentication
        if (isExcluded(requestURI)) {
            log.info("Request {} does not require authentication", requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Check if the employee is logged in
        if (isUserLoggedIn(request, "employee")) {
            Long empId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setId(empId);
            log.info("Employee logged in, ID: {}", empId);
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Check if the customer (user) is logged in
        if (isUserLoggedIn(request, "user")) {
            Long userId = (Long) request.getSession().getAttribute("user");
            BaseContext.setId(userId);
            log.info("User logged in, ID: {}", userId);
            filterChain.doFilter(request, response);
            return;
        }

        // 4. If not logged in, return an error response
        log.info("User not logged in");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
    }

    /**
     * Check if the requested URI matches any excluded paths.
     *
     * @param requestURI The URI of the incoming request.
     * @return true if the request should be excluded from authentication.
     */
    private boolean isExcluded(String requestURI) {
        for (String url : EXCLUDED_PATHS) {
            if (PATH_MATCHER.match(url, requestURI)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if the user is logged in based on session attributes.
     *
     * @param request The HTTP request.
     * @param userType The type of user ("employee" or "user").
     * @return true if the user is logged in.
     */
    private boolean isUserLoggedIn(HttpServletRequest request, String userType) {
        return request.getSession().getAttribute(userType) != null;
    }
}