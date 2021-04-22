package com.zero.static_resources_manager.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zero.static_resources_manager.dataobject.FileDao;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author Zero
 * @Date 2021/4/19 21:13
 * @Since 1.8
 **/
@Mapper
@Repository
public interface FileMapper extends BaseMapper<FileDao> {
    //根据文件夹id获取文件所有文件
    @Select("select id , file_name , folder_id , file_size , upload_time , download_count , file_type ,  upload_auth from static_resource where folder_id = #{folderId}")
    List<FileDao> getListByFolderId(@Param("folderId") Integer folderId);


}
