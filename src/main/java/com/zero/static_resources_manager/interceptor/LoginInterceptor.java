package com.zero.static_resources_manager.interceptor;


import com.zero.static_resources_manager.error.BusinessException;
import com.zero.static_resources_manager.error.EmBustinessError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author Zero
 * @Date 2021/4/10 21:35
 * @Since 1.8
 **/
@Slf4j
@Deprecated

public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        log.info("拦截的请求：" + requestURI);
        String is_login =(String) redisTemplate.opsForValue().get("IS_LOGIN");
        if(is_login != null) {
            System.out.println((String)redisTemplate.opsForValue().get("LOGIN_USER"));
            return true;
        }
        throw new BusinessException(EmBustinessError.USER_NOT_LOGIN);
    }
}
