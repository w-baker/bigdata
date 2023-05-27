package com.example.myweb.service;

import com.example.myweb.entity.HDFSFile;
import com.example.myweb.entity.HDFSDir;
import com.example.myweb.entity.HDFSObject;
import org.apache.hadoop.fs.FileStatus;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface IHDFSService {
    public FileStatus[] ReadHDFS();

    public FileStatus[] ReadHDFS(String path);

    public List<HDFSObject> ReadHDFSObject();

    public List<HDFSObject> ReadHDFSObject(String path);

    public boolean CreateDir();

    public boolean DeleteFile(String path);

    public boolean UploadHDFSFile(String local, String desturl);

    public InputStream getFileInputStreamForPath(String strpath) throws IOException;

    Map<String,Long> GetStatus();

}
