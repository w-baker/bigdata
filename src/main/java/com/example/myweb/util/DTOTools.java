package com.example.myweb.util;

import com.example.myweb.entity.HDFSObject;
import org.apache.hadoop.fs.FileStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DTOTools {
    //用于把HDFS返回的FileStatus数组转化为HDFSObject的集合类型
    public static List<HDFSObject> FileStatusConvertHDFSObjectList(FileStatus[] fileStatuses) {
        List<HDFSObject> filelist = new ArrayList<HDFSObject>();
        HDFSObject hdfsobject = null;
        try {
            for (FileStatus fs : fileStatuses) {
                hdfsobject = new HDFSObject();
                hdfsobject.setLen(fs.getLen());
                hdfsobject.setName(fs.getPath().getName());
                String d = new Date(fs.getAccessTime()).toLocaleString();
                hdfsobject.setAccessTime(d);
                hdfsobject.setPath(fs.getPath().toString());
                hdfsobject.setIsdirectory(fs.isDirectory());
                filelist.add(hdfsobject);
            }
        } catch (Exception ex) {

        }
        return filelist;
    }
}
