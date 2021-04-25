package com.zero.static_resources_manager.config;
import com.zero.static_resources_manager.interceptor.JWTInterceptor;
import com.zero.static_resources_manager.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author Zero
 * @Date 2021/4/10 21:40
 * @Since 1.8
 **/
@Configuration
public class AdminWebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //registry.addInterceptor(new LoginInterceptor())
        //.addPathPatterns("/**")
        //.excludePathPatterns("/user/**");
        registry.addInterceptor(new JWTInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/user/**");
    }
}
