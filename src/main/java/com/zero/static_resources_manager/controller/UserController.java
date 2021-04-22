package com.zero.static_resources_manager.controller;

import com.zero.static_resources_manager.error.BusinessException;
import com.zero.static_resources_manager.error.EmBustinessError;
import com.zero.static_resources_manager.response.CommonReturnType;
import com.zero.static_resources_manager.service.UserService;
import com.zero.static_resources_manager.service.model.UserModel;
import com.zero.static_resources_manager.util.JWTUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @Author Zero
 * @Date 2021/4/19 18:46
 * @Since 1.8
 **/
@RestController
@RequestMapping("/user")
public class UserController extends BaseController{
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(UserController.class);
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private UserService userService;
    //用户注册接口
    @PostMapping("/register")
    public CommonReturnType register(@RequestParam("username")String username,
                                     @RequestParam("password")String password,
                                     @RequestParam("telephone")String telephone,
                                     @RequestParam("code")String code) throws UnsupportedEncodingException, NoSuchAlgorithmException, BusinessException {
        if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password) || StringUtils.isEmpty(telephone) || StringUtils.isEmpty(code)){
            throw new BusinessException(EmBustinessError.PARAMETER_VALIDATION_ERROR);
        }
        userService.validatorCode(telephone,code);
        UserModel userModel = new UserModel();
        userModel.setUserName(username);
        userModel.setEncrptPassword(EncodeByMd5(password));
        userModel.setTelephone(telephone);
        userService.register(userModel);
        return CommonReturnType.create(null);
    }
    //验证码获取接口
    @PostMapping("/getotp")
    public CommonReturnType getOtp(@RequestParam("telephone")String telephone) {
        //按照一定的规则生成验证码
        Random random = new Random();
        int code = random.nextInt(9998);
        if(code < 1000) {
            code += 1001;
        } else {
            code +=1;
        }
        String otpcode = String.valueOf(code);
        //将验证码与手机绑定，放入redis
        redisTemplate.opsForValue().set(telephone,otpcode);
        redisTemplate.expire(telephone, 300, TimeUnit.SECONDS);
        return CommonReturnType.create(otpcode+"，该验证码将于5分钟后失效");
    }
    //手机登录接口
    @PostMapping("/login/phone")
    public CommonReturnType loginByphone(@RequestParam("telephone")String telephone,
                                         @RequestParam("code")String code) throws BusinessException {
        //首先验证码是否正确
        if(StringUtils.isEmpty(telephone) || StringUtils.isEmpty(code)) {
            throw new BusinessException(EmBustinessError.PARAMETER_VALIDATION_ERROR);
        }
        //验证登录手机和验证码正确性
        UserModel userModel = userService.logincode(telephone, code);
        if(userModel == null) {
            throw new BusinessException(EmBustinessError.PARAMETER_VALIDATION_ERROR,"手机号或者验证码错误");
        }
        //保存登录状态信息保存7天
        Map<String,String> map = new HashMap<>();
        map.put("userName", userModel.getUserName());
        map.put("userId", String.valueOf(userModel.getUserId()));
        String token = JWTUtils.getToken(map);
        Map<String,String> map1 = new HashMap<>();
        map1.put("token",token);
        return CommonReturnType.create(map1);
    }

    //用户名登陆接口
    @PostMapping("/login/name")
    public CommonReturnType loginByName(@RequestParam("username")String username,
                                        @RequestParam("password")String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //判断传入参数是否为空
        if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            throw new BusinessException(EmBustinessError.PARAMETER_VALIDATION_ERROR);
        }
        //验证登录用户名，密码的正确性并且返回一个用户核心领域模型
        UserModel userModel = userService.loginValidator(username, EncodeByMd5(password));
        //保存登录状态信息保存1小时
        Map<String,String> map = new HashMap<>();
        map.put("userName", username);
        String token = JWTUtils.getToken(map);
        Map<String,String> map1 = new HashMap<>();
        map1.put("token",token);
        return CommonReturnType.create(map1);
    }

    private String EncodeByMd5(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //确定计算方法
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64Encoder = new BASE64Encoder();
        //加密字符串
        String encode = base64Encoder.encode(md5.digest(password.getBytes("utf-8")));
        return encode;
    }
}
