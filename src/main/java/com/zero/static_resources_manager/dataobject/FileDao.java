package com.zero.static_resources_manager.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

/**
 * @Author Zero
 * @Date 2021/4/19 19:13
 * @Since 1.8
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("static_resource")
public class FileDao {
    @TableId(type = IdType.AUTO)
    private int id;
    private String fileName;
    private int folderId;
    private double fileSize;
    private String fileType;
    private Long uploadTime;
    private int downloadCount;
    private String uploadAuth;
}
