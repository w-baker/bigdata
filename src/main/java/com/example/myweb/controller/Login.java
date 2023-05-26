package com.example.myweb.controller;

import com.example.myweb.entity.HDFSObject;
import com.example.myweb.service.IHDFSService;
import com.example.myweb.service.IUserService;
import org.apache.hadoop.fs.FileStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.myweb.entity.UserInfo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@CrossOrigin
public class Login {
    @Resource
    private IUserService userService;

    @Resource
    private IHDFSService hdfssService;

    @RequestMapping("/")
    public String login() {
        return "login";
    }

    @RequestMapping("/userlogin")
    public String userlogin(UserInfo u, Model model, HttpServletRequest request) {
        String res = "login";
        UserInfo dbuser = userService.login(u);
        if (dbuser != null) {
            res = "index";
            //登录成功以后，直接访问hdfs的根目录：hdfs://10.38.17.37:9000/rt/
            model.addAttribute("user", dbuser);
            request.getSession().setAttribute("username", dbuser.getUsername());
            //跳转到主页时，加载HDFS的目录文件：
            List<HDFSObject> hdfsobjectlist = hdfssService.ReadHDFSObject();
            model.addAttribute("hdfsobjectlist", hdfsobjectlist);
        }
        return res;
    }
}
