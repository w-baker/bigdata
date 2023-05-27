package com.example.myweb.controller;

import com.example.myweb.entity.HDFSObject;
import com.example.myweb.service.IELKService;
import com.example.myweb.service.IHDFSService;
import com.example.myweb.util.Result;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("page")
@CrossOrigin
public class FileRestController {

    @Resource
    private IHDFSService hdfssService;

    @Resource
    private IELKService elkService;

    Result<Object> result = new Result<>();

    @GetMapping("/readdir")//接收用户提交登录页面。
    public Result ReadDir(String url) {
        if (url == null || url.length() == 0){
            url = "hdfs://192.168.200.101:8020/rt";
        }
        try {
            List<HDFSObject> hdfsobjectlist = hdfssService.ReadHDFSObject(url);
            result.setCode(200);
            result.setData(hdfsobjectlist);
            String currenturl = url.substring(url.indexOf("rt") + 2);
        } catch (Exception ex) {
            result.setCode(400);
            result.setMsg(ex.getMessage());
        }
        return result;
    }

    private String changeFileName(String filename) {
        String fullFileNmae = "";
        fullFileNmae = filename.substring(0, filename.indexOf(".docx"));
        fullFileNmae += ("_" + (int) (Math.random() * 100000000) + ".docx");

        return fullFileNmae;
    }

    private String getFullFileName(String filename) {
        String fullFileNmae = "";
        String localPath = "E:/BigData/Project/word2/";
        fullFileNmae = localPath + filename;
        return fullFileNmae;
    }

    @PostMapping("upload")
    public Result upload(MultipartFile file, String url, String username) {
        Result<Object> result = new Result<>();
        //接收上传文件，读取内容写入ES成功后，再上传到HDFS中。
        if (!file.isEmpty() && file.getSize() > 0) {
            try {
                String realPath = "E:/doc/upload";
                String filepath=realPath + "/" + file.getOriginalFilename();
                String filename=file.getOriginalFilename();
                 file.transferTo(new File(filepath)); //Michael
                boolean res=hdfssService.UploadHDFSFile(filepath,url);
                if(res){//如果成功上传到hdfs中，就读出文件内容，写入ES中,filename为已经上传到本地的文件路径和名称，url为用户传到hdfs的路径
                    long filelen=file.getSize();
                    elkService.ReadDocAndInsertES(filepath,filename,url,username,String.valueOf(filelen));
                }
                result.setCode(200);
                result.setMsg("上传成功");
            } catch (IOException e) {
                result.setCode(400);
                result.setMsg(e.getMessage());
            }
        }
        return result;
    }

    @GetMapping("getstatus")
    public Result getstatus(){
        Result<Object> result = new Result<>();
        Map<String, Long> hdfsstatus = null;
        try {
            hdfsstatus = hdfssService.GetStatus();
        }catch (Exception e){
            result.setCode(400);
            result.setMsg(e.getMessage());
        }
        result.setCode(200);
        result.setData(hdfsstatus);
        return result;
    }

    @GetMapping("delete")
    public Result delete(String filePath) {
        // 从ES中删除
        elkService.deleteInfo(filePath);

        // 从hdfs中删除文件
        hdfssService.DeleteFile(filePath);
        result.setCode(200);
        result.setMsg("删除成功");
        return result;
    }
    @GetMapping("preview")
    public Result preview(String filepath){
        Result<Object> result = new Result<>();
        String res =null;
        try{
            res = elkService.review(filepath);
            result.setCode(200);
            result.setData(res);
        }catch (Exception e){
            result.setCode(400);
            result.setMsg(e.getMessage());
        }
        if (res==null){
            result.setCode(200);
            result.setMsg("文件没有数据");
        }
        return result;
    }

    @GetMapping("download")
    public void download(@RequestParam String fileName, HttpServletResponse response) {
        response.setContentType("application/force-download");
        response.setHeader("content-type", "application/octet-stream");
        response.setContentType("application/octet-stream");
        try {
            String tmp = URLDecoder.decode(fileName, "utf-8");
            tmp = tmp.substring(fileName.lastIndexOf("/") + 1);
            tmp = String.format("attachment; filename=\"%s\"", URLEncoder.encode(tmp, "utf-8"));
            response.addHeader("Content-Disposition", tmp);
        } catch (Exception e) {
        }
        byte[] buff = new byte[1024];
        InputStream bis = null;
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            bis = hdfssService.getFileInputStreamForPath(fileName);
            int i = bis.read(buff);
            while (i != -1) {
                os.write(buff, 0, i);
                os.flush();
                i = bis.read(buff);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
