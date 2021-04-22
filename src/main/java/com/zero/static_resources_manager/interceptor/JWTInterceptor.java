package com.zero.static_resources_manager.interceptor;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.zero.static_resources_manager.error.BusinessException;
import com.zero.static_resources_manager.error.EmBustinessError;
import com.zero.static_resources_manager.util.JWTUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.SignatureException;

/**
 * @Author Zero
 * @Date 2021/4/21 0:10
 * @Since 1.8
 **/
public class JWTInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws BusinessException {
        String token = request.getHeader("token"); //获取请求头令牌
        try {
            JWTUtils.verifyToken(token);
        } catch (SignatureVerificationException e) {
            throw new BusinessException(EmBustinessError.PARAMETER_VALIDATION_ERROR, "无效签名");
        } catch (TokenExpiredException e) {
            throw new BusinessException(EmBustinessError.PARAMETER_VALIDATION_ERROR, "token过期");
        } catch (AlgorithmMismatchException e) {
            throw new BusinessException(EmBustinessError.PARAMETER_VALIDATION_ERROR, "算法不一致");
        } catch (Exception e) {
            throw new BusinessException(EmBustinessError.PARAMETER_VALIDATION_ERROR, "token无效");
        }
        return true;
    }
}
