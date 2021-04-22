package com.zero.static_resources_manager.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author Zero
 * @Date 2021/4/19 19:13
 * @Since 1.8
 **/
@TableName("user_info")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDao {
    @TableId(type= IdType.AUTO)
    private Integer userId;
    private String userName;
    private String telephone;
    private String picture;
}
