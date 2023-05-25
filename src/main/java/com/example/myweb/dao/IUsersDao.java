package com.example.myweb.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import com.example.myweb.entity.UserInfo;

@Mapper
public interface IUsersDao {
    @Insert("insert into userinfo (userid,username,password,email) values (SEQ_USER.nextval ,#{username},#{password},#{email})")
    int insert(UserInfo user);

    @Select("select * from userinfo where username=#{username} and password=#{password}")
    UserInfo login(UserInfo user);

    @Select("select * from userinfo")
    List<UserInfo> getAllUsers();

    @Delete("delete from userinfo where userid=#{userid}")
    int removeUserByUserid(int userid);

    @Update("update userinfo set password=#{password} where userid=#{userid}")
    int modifyUser_passByUserid(UserInfo user);

}