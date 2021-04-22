package com.zero.static_resources_manager.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zero.static_resources_manager.dataobject.FolderDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author Zero
 * @Date 2021/4/19 21:17
 * @Since 1.8
 **/

public interface FolderService extends IService<FolderDao> {
    List<FolderDao> show(Integer folderId);
}
