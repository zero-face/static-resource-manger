package com.zero.static_resources_manager.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zero.static_resources_manager.dao.FileMapper;
import com.zero.static_resources_manager.dao.FolderMapper;
import com.zero.static_resources_manager.dataobject.FileDao;
import com.zero.static_resources_manager.dataobject.FolderDao;
import com.zero.static_resources_manager.error.BusinessException;
import com.zero.static_resources_manager.error.EmBustinessError;
import com.zero.static_resources_manager.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;

/**
 * @Author Zero
 * @Date 2021/4/19 21:14
 * @Since 1.8
 **/
@Service
@Slf4j
public class FileServiceImpl extends ServiceImpl<FileMapper, FileDao> implements FileService {
    @Autowired
    private FileMapper fileMapper;
    @Value("${os.name}")
    private String osName;
    @Autowired
    private FolderMapper folderMapper;

    @Override
    public List<FileDao> show(Integer folderId) throws BusinessException {
        List<FileDao> list = fileMapper.getListByFolderId(folderId);
        return list;
    }

    @Override
    public FileDao getFile(Integer fileId) throws BusinessException {
        FileDao file = fileMapper.selectById(fileId);
        if(file == null) {
            throw new BusinessException(EmBustinessError.PARAMETER_VALIDATION_ERROR, "文件不存在");
        }
        return file;
    }

    @Override
    public void downloadFile(FileDao file, HttpServletRequest request, HttpServletResponse response) throws IOException, BusinessException {
        File f;
        if (osName.equals("Windows 10")) {
            f = new File("C:\\Users\\Administrator\\Desktop\\" + file.getFileName());
        } else {
            f = new File("/file/" + file.getFileName());
        }
        if (f.exists()) {
            FileInputStream fileInputStream = new FileInputStream(f);
            ServletContext servletContext = request.getServletContext(); //获取上下文路径
            servletContext.getMimeType(file.getFileName());
            if (request.getHeader("User-Agent").contains("Firefox")) {
                response.setHeader("Content-Disposition", "attachment;filename==?UTF-8?B?" + new BASE64Encoder().encode(file.getFileName().getBytes("utf-8")) + "?=");
            } else {
                response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(file.getFileName(), "utf-8"));
            }
            OutputStream outputStream = response.getOutputStream();
            IOUtils.copy(fileInputStream, outputStream);
            log.info("下载完成");
        } else {
        throw new BusinessException(EmBustinessError.PARAMETER_VALIDATION_ERROR, "文件信息不存在");
        }
    }

    @Override
    public void deleteFile(Integer fileId) throws BusinessException, IOException {
        FileDao fileDao = getFile(fileId);
        if(fileDao == null ) {
            throw new BusinessException(EmBustinessError.PARAMETER_VALIDATION_ERROR, "文件不存在");
        }
        String filename = fileDao.getFileName();
        fileMapper.deleteFileById(fileId);
        deleteLocalFile(filename);
    }

    private void deleteLocalFile(String name) throws BusinessException, IOException {
        File f , copyFile;
        if(osName.equals("Windows 10")) {
            f = new File("C:\\Users\\Administrator\\Desktop\\" + name);
            copyFile= new File("C:\\Users\\Administrator\\Desktop\\deleteFile\\" + name);
        } else {
            f = new File("/file/" + name);
            copyFile = new File("/file/deleteFile/" + name);
        }
        if(!f.exists()){
            throw new BusinessException(EmBustinessError.PARAMETER_VALIDATION_ERROR, "文件源不存在");
        }
        FileInputStream fileInputStream = new FileInputStream(f);

        FileOutputStream fileOutputStream = new FileOutputStream(copyFile);
        byte[] bytes = new byte[1024];
        int len = 0;
        while((len = fileInputStream.read(bytes)) != -1) {
            fileOutputStream.write(bytes,0,len);
        }
        fileInputStream.close();
        fileOutputStream.flush();
        fileOutputStream.close();
        f.delete();
    }
}
