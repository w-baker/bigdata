package com.example.myweb.controller;

import com.example.myweb.entity.HDFSObject;
import com.example.myweb.service.IELKService;
import com.example.myweb.service.IHDFSService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class Index {
    @Resource
    private IHDFSService hdfssService;

    @Resource
    private IELKService elkService;

    @RequestMapping("/index")
    public String index(Model model) {
        String res = "index";
        try {
            List<HDFSObject> hdfsobjectlist = hdfssService.ReadHDFSObject();
            model.addAttribute("hdfsobjectlist", hdfsobjectlist);
            model.addAttribute("currenturl", "/");
        } catch (Exception ex) {

        }
        return res;
    }

    @RequestMapping("/queryinfo")
    public String queryinfo(String querykeywords, Model model) {
        String res = "index";
        List<HDFSObject> hdfsobjectlist = elkService.queryinfo(querykeywords);
        model.addAttribute("hdfsobjectlist", hdfsobjectlist);
        return res;
    }

}
