package com.zero.static_resources_manager.dataobject;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author Zero
 * @Date 2021/4/19 19:13
 * @Since 1.8
 **/
@Data
@TableName("user_password")
@NoArgsConstructor
@AllArgsConstructor
public class PasswordDao {
    private int id;
    private int userId;
    private String encrptPassword;
    
}
