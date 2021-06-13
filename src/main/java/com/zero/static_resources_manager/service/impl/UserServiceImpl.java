package com.zero.static_resources_manager.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zero.static_resources_manager.dao.PasswordMapper;
import com.zero.static_resources_manager.dao.UserMapper;
import com.zero.static_resources_manager.dataobject.PasswordDao;
import com.zero.static_resources_manager.dataobject.UserDao;
import com.zero.static_resources_manager.error.BusinessException;
import com.zero.static_resources_manager.error.EmBustinessError;
import com.zero.static_resources_manager.service.UserService;
import com.zero.static_resources_manager.service.model.UserModel;
import com.zero.static_resources_manager.validator.ValidationResult;
import com.zero.static_resources_manager.validator.ValidatorImpl;
import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.Console;

/**
 * @Author Zero
 * @Date 2021/4/19 18:59
 * @Since 1.8
 **/
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDao> implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordMapper passwordMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ValidatorImpl validator;

    @Override
    public UserModel loginValidator(String username, String encrptPassword) throws BusinessException {
        //通过用户名拿到用户信息

        UserDao userDao = userMapper.selectByUserName(username);
        //如果没拿到，说明用户名不对，抛出错误
        if(userDao == null) {
            throw new BusinessException(EmBustinessError.PARAMETER_VALIDATION_ERROR,"用户名或者密码不正确");
        }
        //获取一个用户密码信息
        PasswordDao passwordDao = passwordMapper.selectByUserId(userDao.getUserId());
        //将用户信息和用户密码信息转换为一个核心领域用户模型
        UserModel userModel = convertFromdataobject(userDao, passwordDao);

        //比对通过用户名拿出的这个核心领域模型中的密码和传入的密码是否匹配
        if(!StringUtils.equals(encrptPassword, userModel.getEncrptPassword())){
            throw new BusinessException(EmBustinessError.PARAMETER_VALIDATION_ERROR,"用户名或者密码错误");
        }
        return userModel;
    }

    //手机登录验证
    @Override
    public UserModel logincode(String telephone, String code) throws BusinessException {

        //通过手机拿到用户信息
        UserDao userDao = userMapper.selectByTelephone(telephone);
        if(userDao == null) {
            throw new BusinessException(EmBustinessError.PARAMETER_VALIDATION_ERROR,"手机号或者验证码错误");
        }
        //通过用户id拿到用户密码
        PasswordDao passwordDao = passwordMapper.selectByUserId(userDao.getUserId());
        //转化为核心领域模型
        UserModel userModel = convertFromdataobject(userDao, passwordDao);
        //验证验证码
        validatorCode(telephone, code);
        return userModel;
    }
    //验证码验证
    @Override
    public void validatorCode(String telephone, String code) throws BusinessException {
        String o = (String)redisTemplate.opsForValue().get(telephone);
        if(!StringUtils.equals(o, code)) {
            throw new BusinessException(EmBustinessError.PARAMETER_VALIDATION_ERROR,"手机号或者验证码错误");
        }
    }
    //注册
    @Override
    public void register(UserModel userModel) throws BusinessException {
        if(userModel == null) {
            throw new BusinessException(EmBustinessError.PARAMETER_VALIDATION_ERROR);
        }
        ValidationResult validationResult = validator.validate(userModel);
        if(validationResult.isHasErrs()) {
            throw new BusinessException(EmBustinessError.PARAMETER_VALIDATION_ERROR,validationResult.getErrMsg());
        }
        UserDao userDao = convertFromModel(userModel);
        try{
            userMapper.insert(userDao);
        } catch (DuplicateKeyException e) {
            throw new BusinessException(EmBustinessError.PARAMETER_VALIDATION_ERROR,"用户已存在");
        }
        userModel.setUserId(userDao.getUserId());
        PasswordDao passwordDao = convertPasswordFromModel(userModel);
        passwordMapper.insert(passwordDao);
    }

    private PasswordDao convertPasswordFromModel(UserModel userModel) {
        if(userModel == null) {
            return null;
        }
        PasswordDao passwordDao = new PasswordDao();
        BeanUtils.copyProperties(userModel, passwordDao);
        return passwordDao;
    }

    private UserDao convertFromModel(UserModel userModel) {
        if(userModel == null) {
            return null;
        }
        UserDao userDao = new UserDao();
        BeanUtils.copyProperties(userModel, userDao);
        return userDao;
    }


    private UserModel convertFromdataobject(UserDao userDao, PasswordDao passwordDao) {
        if(userDao == null) {
            return null;
        }
        UserModel userModel = new UserModel();
        //使用beanUtil中的拷贝方法
        BeanUtils.copyProperties(userDao, userModel);
        if(passwordDao != null) {
            userModel.setEncrptPassword(passwordDao.getEncrptPassword());
        }
        return userModel;
    }
}
