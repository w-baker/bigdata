package com.example.myweb.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.myweb.dao.IUsersDao;
import com.example.myweb.entity.UserInfo;
import com.example.myweb.service.IUserService;


@Service("userService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUsersDao userDao;

    @Override
    public UserInfo login(UserInfo user) {
        user = userDao.login(user);
        return user;
    }

    @Override
    public List<UserInfo> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Override
    public int insert(UserInfo user) {
        return userDao.insert(user);
    }

    @Override
    public int removeUserByUserid(int userid) {
        return userDao.removeUserByUserid(userid);
    }

    @Override
    public int modifyUser_passByUserid(UserInfo user) {
        return userDao.modifyUser_passByUserid(user);
    }

    @Override
    public Integer register(UserInfo userInfo) {
        UserInfo userInfo1 = userDao.findByUsername(userInfo.getUsername());
        if (userInfo1 != null){
            return 3;
        }
        return userDao.addUser(userInfo);
    }
}
