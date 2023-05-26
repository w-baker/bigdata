package com.example.myweb.controller;

import com.example.myweb.entity.HDFSObject;
import com.example.myweb.service.IELKService;
import com.example.myweb.service.IHDFSService;
import com.example.myweb.util.Result;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/query")
@CrossOrigin
public class RestIndex {
    @Resource
    private IHDFSService hdfssService;

    @Resource
    private IELKService elkService;

    Result result = new Result();

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

    @RequestMapping("/queryInfo")
    public Result queryInfo(String keyWords) {
        List<HDFSObject> hdfsobjectlist = elkService.queryinfo(keyWords);

        result.setCode(200);
        result.setMsg("查询成功");
        result.setData(hdfsobjectlist);

        return result;
    }
}
