package com.zero.static_resources_manager.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
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
@AllArgsConstructor
@NoArgsConstructor
@TableName("folder")
public class FolderDao {
    @TableId(type = IdType.AUTO)
    private int id;
    private String folderName;
    private BigInteger createTime;
    private int folderId;
    @TableLogic
    private Boolean deleted;
}
