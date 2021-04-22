package com.zero.static_resources_manager.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zero.static_resources_manager.dataobject.FileDao;
import com.zero.static_resources_manager.dataobject.FolderDao;
import com.zero.static_resources_manager.error.BusinessException;
import com.zero.static_resources_manager.error.EmBustinessError;
import com.zero.static_resources_manager.response.CommonReturnType;
import com.zero.static_resources_manager.service.FileService;
import com.zero.static_resources_manager.service.FolderService;
import com.zero.static_resources_manager.util.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;
import sun.nio.ch.IOUtil;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author Zero
 * @Date 2021/4/21 11:38
 * @Since 1.8
 **/
@Slf4j
@RestController
@RequestMapping("/file")
public class FileController extends BaseController {
    @Autowired
    private FileService fileService;
    @Value("${os.name}")
    private String osName;
    @Autowired
    private FolderService folderService;
    /**
     * 文件上传
     * @param folderId
     * @param files
     * @return
     */
    @PostMapping("/upload")
    public CommonReturnType upload(@RequestParam(value = "folderId" , required = false, defaultValue = "0")Integer folderId,
                                   @RequestParam(value = "file" , required = false)MultipartFile[] files,
                                   HttpServletRequest request) throws BusinessException, IOException {
        if(files == null || files.length < 0) {
            throw new BusinessException(EmBustinessError.PARAMETER_VALIDATION_ERROR,"上传的文件为空");
        }
        for (MultipartFile f :
                files) {
            if(!f.isEmpty()) {
                String filename = f.getOriginalFilename();//获取上传文件名
                long size = f.getSize();
                String type = filename.substring(filename.lastIndexOf('.') + 1);
                String token = request.getHeader("token");
                DecodedJWT verify = JWTUtils.verifyToken(token);
                String upload_auth = verify.getClaim("userName").asString();
                if(osName.equals("Windows 10")) {
                    f.transferTo(new File("C:\\Users\\Administrator\\Desktop\\" + filename));
                } else {
                    f.transferTo(new File("/file/" + filename));
                }
                FileDao fileDao = new FileDao();
                fileDao.setFileName(filename);
                fileDao.setDownloadCount(0);
                fileDao.setFileSize(size);
                fileDao.setFolderId(folderId);
                fileDao.setFileType(type);
                fileDao.setUploadTime(new Date().getTime());
                fileDao.setUploadAuth(upload_auth);
                //将信息存入数据库
                try {
                    fileService.save(fileDao);
                } catch (DuplicateKeyException e) {
                    throw new BusinessException(EmBustinessError.PARAMETER_VALIDATION_ERROR, "已存在文件名为" + filename + "的文件");

                }
            }
        }
        return CommonReturnType.create(null);
    }
    /**
     * 文件下载
     */
    @GetMapping("/download")
    public CommonReturnType download(@RequestParam("fileId")Integer fileId, HttpServletRequest request, HttpServletResponse response) throws BusinessException, IOException {
        //通过id拿到要下载的文件信息
        FileDao file = fileService.getFile(fileId);
        //下载
        fileService.downloadFile(file, request,response);
        file.setDownloadCount(file.getDownloadCount()+1);
        //更新数据库中文件下载次数
//        fileService.updateDownloadCount(file);
        fileService.saveOrUpdate(file);
        return CommonReturnType.create(null);
    }
    /**
     * 文件展示
     */
    @GetMapping("/show/file")
    public CommonReturnType show(@RequestParam(value = "folderId" ,required = false,defaultValue = "0")Integer folderId) throws BusinessException {
        List<FileDao> list = fileService.show(folderId);
        return CommonReturnType.create(list);
    }
    @GetMapping("/show/folder")
    public CommonReturnType showFiles(@RequestParam(value = "folderId",required = false,defaultValue = "0") Integer foderId){
        List<FolderDao> list = folderService.show(foderId);
        return CommonReturnType.create(list);
    }
    /**
     * 文件详情
     */
    @GetMapping("/detail")
    public CommonReturnType detail(@RequestParam(value = "fileId",required = false)Integer fileId) throws BusinessException {
        if(fileId == null) {
            throw new BusinessException(EmBustinessError.PARAMETER_VALIDATION_ERROR);
        }
        FileDao file = fileService.getFile(fileId);
        return CommonReturnType.create(file);
    }

}
