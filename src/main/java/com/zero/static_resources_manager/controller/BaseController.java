package com.zero.static_resources_manager.controller;


import com.zero.static_resources_manager.error.BusinessException;
import com.zero.static_resources_manager.error.EmBustinessError;
import com.zero.static_resources_manager.response.CommonReturnType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: Zero
 * @Date: 2021/4/2 - 21:39
 * @since: jdk 1.8
 */
public class BaseController {

    public static final String CONTENT_TYPE_FORMED="application/x-www-form-urlencoded";

    //定义exceptionhandler解决未被controller层吸收的exception
    @ExceptionHandler(Exception.class) //异常处理器（处理异常的范围）
    @ResponseStatus(value = HttpStatus.OK) //会返回一个状态码
    @ResponseBody
    public Object handlerException(HttpServletRequest request, Exception exception){
        Map<String,Object> responseData = new HashMap<>();
        if(exception instanceof BusinessException) {
            BusinessException businessException = (BusinessException) exception;
            //CommonReturnType commonReturnType = new CommonReturnType();
            //commonReturnType.setStatus("fail");
            responseData.put("errCode",businessException.getErrorCode());
            responseData.put("errMsg",businessException.getErrMsg());
        } else {
            responseData.put("errCode", EmBustinessError.UNKNOWN_ERROR.getErrorCode());
            responseData.put("errMsg",EmBustinessError.UNKNOWN_ERROR.getErrMsg());
        }

        return CommonReturnType.create(responseData,"fail");
        //commonReturnType.setData(responseData);
        //return commonReturnType;

    }
}
