package com.zero.static_resources_manager.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author Zero
 * @Date 2021/4/19 19:00
 * @Since 1.8
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {
    private int userId;
    @NotNull(message = "用户名不能为空")
    private String userName;
    @NotNull(message = "电话不能不填")
    private String telephone;
    @NotNull(message = "密码不能不填")
    private String encrptPassword;
    private String picture;
}
