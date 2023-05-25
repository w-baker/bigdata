package com.example.myweb.service;

import java.util.List;

import com.example.myweb.entity.UserInfo;


public interface IUserService {

    int insert(UserInfo user);

    UserInfo login(UserInfo user);

    List<UserInfo> getAllUsers();

    int removeUserByUserid(int userid);

    int modifyUser_passByUserid(UserInfo user);
}
