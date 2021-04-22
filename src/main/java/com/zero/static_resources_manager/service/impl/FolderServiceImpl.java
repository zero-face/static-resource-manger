package com.zero.static_resources_manager.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zero.static_resources_manager.dao.FolderMapper;
import com.zero.static_resources_manager.dataobject.FolderDao;
import com.zero.static_resources_manager.service.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author Zero
 * @Date 2021/4/19 21:18
 * @Since 1.8
 **/
@Service
public class FolderServiceImpl extends ServiceImpl<FolderMapper, FolderDao> implements FolderService {
    @Autowired
    private FolderMapper folderMapper;


    @Override
    public List<FolderDao> show(Integer folderId) {
        List<FolderDao> listFolder = folderMapper.getFolderByFolderId(folderId);
        return listFolder;
    }

}
