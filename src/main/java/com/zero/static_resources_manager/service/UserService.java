package com.zero.static_resources_manager.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zero.static_resources_manager.dataobject.UserDao;
import com.zero.static_resources_manager.error.BusinessException;
import com.zero.static_resources_manager.service.model.UserModel;

/**
 * @Author Zero
 * @Date 2021/4/19 18:59
 * @Since 1.8
 **/
public interface UserService extends IService<UserDao> {
    //验证用户名登录
    UserModel loginValidator(String username, String encrptPassword) throws BusinessException;
    //验证手机验证码登录
    UserModel logincode(String telephone,String code) throws BusinessException;
    //验证验证码是否正确
    void validatorCode(String telephone,String code) throws BusinessException;
    //注册用户
    void register(UserModel userModel) throws BusinessException;
}
