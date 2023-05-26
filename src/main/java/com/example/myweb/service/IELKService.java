package com.example.myweb.service;

import com.example.myweb.entity.HDFSFile;
import com.example.myweb.entity.HDFSObject;

import java.util.List;

public interface IELKService {
    //读取word文档并把内容添加到ES集群中
    public boolean ReadDocAndInsertES(String filepath, String filename, String hdfsurl, String username, String filelength);

    //从ES集群中查找文档内容
    public List<HDFSObject> queryinfo(String querykeywords);

    void deleteInfo(String filePath);
}
