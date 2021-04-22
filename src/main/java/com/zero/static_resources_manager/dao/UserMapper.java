package com.zero.static_resources_manager.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zero.static_resources_manager.dataobject.UserDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @Author Zero
 * @Date 2021/4/19 21:08
 * @Since 1.8
 **/
@Mapper
@Repository
public interface UserMapper extends BaseMapper<UserDao> {
     UserDao selectByUserName(@Param("username")String username); //通过用户名拿到用户信息
     //通过手机拿到用户信息
     @Select("select user_id , user_name , telephone from user_info where telephone = #{telephone}")
     UserDao selectByTelephone(@Param("telephone")String telephone);
}
