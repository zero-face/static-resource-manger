package com.zero.static_resources_manager.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sun.xml.internal.rngom.parse.host.Base;
import com.zero.static_resources_manager.dataobject.FolderDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author Zero
 * @Date 2021/4/19 21:18
 * @Since 1.8
 **/
@Mapper
@Repository
public interface FolderMapper extends BaseMapper<FolderDao> {
    @Select("select id ,folder_name , create_time , folder_id from folder where folder_id = #{folderId} and id != 1")
    List<FolderDao> getFolderByFolderId(@Param("folderId") Integer folderId);
}
