package com.example.myweb.controller;

import com.example.myweb.dao.IUsersDao;
import com.example.myweb.entity.UserInfo;
import com.example.myweb.service.IUserService;
import com.example.myweb.util.Result;
import com.example.myweb.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Classname UserController
 * @Date 2023/5/26 10:31
 * @Author by Rain
 */
@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {
    @Autowired
    private IUserService userService;

    private Result<Object> result = new Result<>();
    @PostMapping("/login")
    public Result login(@RequestBody UserInfo userInfo){
        UserInfo login = userService.login(userInfo);
        if (login == null){
            result.setCode(400);
            result.setMsg("登录失败");
            return result;
        }
        result.setCode(200);
        result.setMsg("成功");
        return result;
    }
    @PostMapping("/register")
    public Result register(@RequestBody UserInfo userInfo){
        Integer register = userService.register(userInfo);
        result.setCode(200);
        result.setMsg("注册成功");

        if (register==0){
            result.setCode(400);
            result.setMsg("注册失败");
        }
        if (register == 3){
            result.setCode(400);
            result.setMsg("用户已存在");
        }
        return result;
    }
}
