package com.zero.static_resources_manager.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zero.static_resources_manager.dataobject.FileDao;
import com.zero.static_resources_manager.error.BusinessException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Author Zero
 * @Date 2021/4/19 21:14
 * @Since 1.8
 **/
public interface FileService extends IService<FileDao> {
    //展示文件列表
    List<FileDao> show(Integer folderId) throws BusinessException;
    //根据文件id文件详情
    FileDao getFile(Integer fileId) throws BusinessException;

    //下载文件
    void  downloadFile(FileDao file, HttpServletRequest request, HttpServletResponse response) throws IOException, BusinessException;
    //删除文件
    void deleteFile(Integer fileId) throws BusinessException, IOException;
}
