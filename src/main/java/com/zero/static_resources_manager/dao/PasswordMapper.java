package com.zero.static_resources_manager.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zero.static_resources_manager.dataobject.PasswordDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @Author Zero
 * @Date 2021/4/19 21:26
 * @Since 1.8
 **/
@Mapper
@Repository
public interface PasswordMapper extends BaseMapper<PasswordDao> {
    @Select("select id,user_id,encrpt_password from user_password where user_id = #{userid}")
    PasswordDao selectByUserId(@Param("userid")Integer userid);
}
