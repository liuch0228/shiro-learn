package net.xdclass.xdclassshiro.dao;

import net.xdclass.xdclassshiro.domain.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import javax.websocket.server.ServerEndpoint;

public interface UserMapper {

    @Select("select * from user where username= #{username}")
    User findByUserName(@Param("username") String username);

    @Select("select * from user where id = #{userId}")
    User findById(@Param("userId") int id);

    @Select("select * from user where username=#{username} and password=#{pwd}")
    User findByUsernameAndPwd(@Param("username") String userName,@Param("pwd")String pwd);


}
