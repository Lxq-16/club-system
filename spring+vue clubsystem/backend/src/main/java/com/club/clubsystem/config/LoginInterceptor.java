package com.club.clubsystem.config;
import com.club.clubsystem.controller.AdminController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 跨域预检请求直接放行
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        String token = request.getHeader("Authorization");
        if (token != null && AdminController.TOKEN_POOL.containsKey(token)) {
            return true;
        }
        response.setContentType("application/json;charset=UTF-8");
        Map<String, Object> res = new HashMap<>();
        res.put("code", 401);
        res.put("msg", "未登录或登录已过期");
        response.getWriter().write(new ObjectMapper().writeValueAsString(res));
        return false;
    }
}
