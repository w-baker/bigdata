package com.example.myweb.controller;

import com.example.myweb.entity.HDFSObject;
import com.example.myweb.service.IELKService;
import com.example.myweb.service.IHDFSService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;


@Controller
public class FileController {

    @Resource
    private IHDFSService hdfssService;

    @Resource
    private IELKService elkService;

    @RequestMapping("/readdir")//接收用户提交登录页面。
    public String ReadDir(String url, Model model) {
        String res = "index";
        if (url != null) {
            try {
                List<HDFSObject> hdfsobjectlist = hdfssService.ReadHDFSObject(url);
                model.addAttribute("hdfsobjectlist", hdfsobjectlist);
                String currenturl = url.substring(url.indexOf("rt") + 2);
                model.addAttribute("currenturl", currenturl);
                model.addAttribute("fullurl", url);
            } catch (Exception ex) {

            }
        }
        return res;
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

    @RequestMapping("upload")
    public String upload(MultipartFile file, HttpSession session, String url, String username) {
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
            } catch (IOException e) {
                System.out.println("error");
                System.out.println(e.getMessage());
            }
        }
        try {
            url = URLEncoder.encode(url, "utf8");
        } catch (Exception ex) {

        }

        System.out.println("上传成功");
        return "redirect:/readdir?url=" + url;
    }
    @RequestMapping("delete")
    public String delete(String filePath) {
        String[] paths = filePath.split("/");
        String pageUrl = filePath.replace("/" + paths[paths.length-1],"");
        // 从hdfs中删除文件
        hdfssService.DeleteFile(filePath);
        // 从ES中删除

        return "redirect:/readdir?url=" + pageUrl;
    }

    @RequestMapping("download")
    public void download1(HttpSession session, @RequestParam String fileName, HttpServletResponse response) {
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
            //bis = new BufferedInputStream(new FileInputStream(new File("C:\\Users\\sss\\Pictures\\Saved Pictures\\"+ fileName)));
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
