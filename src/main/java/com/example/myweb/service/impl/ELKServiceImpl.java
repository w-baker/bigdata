package com.example.myweb.service.impl;

import com.example.myweb.dao.ESDao;
import com.example.myweb.entity.HDFSObject;
import com.example.myweb.service.IELKService;
import com.example.myweb.util.WordUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class ELKServiceImpl implements IELKService {

    @Resource
    private ESDao esDao;

    public boolean ReadDocAndInsertES(String filepath, String filename, String hdfsurl, String username, String filelength) {
        //写入es中的名为：rt的index中，rt字段如下：filename，hdfsurl，username，fileinfo，createdate
        boolean res = false;
        String info = WordUtil.readWord(filepath);
        String fileinfo = info;

        //把不相关的多余字符替换掉，如替换掉换行符和空格
//        fileinfo = fileinfo.replace("\t", "");
//        fileinfo = fileinfo.replaceAll("(\\r\\n|\\n|\\n\\r)", "");
//        fileinfo = fileinfo.replace(" ", "");
//        fileinfo = fileinfo.replace("\"", "");

        fileinfo = fileinfo.replace("\t", " ");
        fileinfo = fileinfo.replaceAll("(\\r\\n|\\n|\\n\\r)", " ");
        fileinfo = fileinfo.replace("\"", "");

        Date d = new Date();
        String createdate = d.toLocaleString();
        if (info.length() > 1) {
            String indexscript = "{\r\n" +
                    "    \"filename\": \"" + filename + "\",\r\n" +
                    "    \"hdfsurl\": \"" + hdfsurl + "\",\r\n" +
                    "    \"username\": \"" + username + "\",\r\n" +
                    "    \"filelength\": \"" + filelength + "\",\r\n" +
                    "    \"fileinfo\": \"" + fileinfo + "\",\r\n" +
                    "    \"createdate\": \"" + createdate + "\"\r\n" +
                    "}";
            esDao.addnew(indexscript);
        }
        return res;
    }

    @Override
    public List<HDFSObject> queryinfo(String querykeywords) {
        List<HDFSObject> filelist = esDao.query(querykeywords);
        return filelist;
    }
}
