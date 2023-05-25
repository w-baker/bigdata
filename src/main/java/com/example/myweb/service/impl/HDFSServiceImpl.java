package com.example.myweb.service.impl;

import com.example.myweb.dao.HdfsDao;
import com.example.myweb.entity.HDFSObject;
import com.example.myweb.service.IHDFSService;
import com.example.myweb.util.DTOTools;
import org.apache.hadoop.fs.FileStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class HDFSServiceImpl implements IHDFSService {

    @Resource
    private HdfsDao hdfsDao;

    @Override
    public FileStatus[] ReadHDFS() {
        FileStatus[] filelist = hdfsDao.getDirectoryFromHdfs();
        return filelist;
    }

    @Override
    public FileStatus[] ReadHDFS(String path) {
        FileStatus[] filelist = hdfsDao.getDirectoryFromHdfs(path);
        return filelist;
    }

    @Override
    public List<HDFSObject> ReadHDFSObject() {
        //从HDFS返回的是FileStatus数组，需要转化为HDFSObject的集合类型
        FileStatus[] filelist = hdfsDao.getDirectoryFromHdfs();
        List<HDFSObject> hdfsobjects = DTOTools.FileStatusConvertHDFSObjectList(filelist);
        return hdfsobjects;
    }

    @Override
    public List<HDFSObject> ReadHDFSObject(String path) {
        FileStatus[] filelist = hdfsDao.getDirectoryFromHdfs(path);
        List<HDFSObject> hdfsobjects = DTOTools.FileStatusConvertHDFSObjectList(filelist);
        return hdfsobjects;
    }

    @Override
    public boolean CreateDir() {
        return false;
    }

    @Override
    public boolean DeleteFile(String path) {
        return false;
    }

    @Override
    public boolean UploadHDFSFile(String local, String desturl) {
        boolean res = false;
        try {
            hdfsDao.copyFile(local, desturl);
            res = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return res;
    }

    @Override
    public InputStream getFileInputStreamForPath(String strpath) throws IOException {
        return hdfsDao.getFileInputStreamForPath(strpath);
    }
}
