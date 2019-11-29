package net.xdclass.xdclassshiro.service;

import net.xdclass.xdclassshiro.domain.User;
import org.apache.ibatis.annotations.Select;

public interface UserService {
    /**
     * 获取用户全部信息，包含角色权限
     * @param username
     * @return
     */
    User findAllUserInfoByUsername(String username);

    /**
     * 获取用户基本信息
     * @param userId
     * @return
     */
    User findSimpleUserInfoById(int userId);

    /**
     * 根据用户名查找用户
     * @param username
     * @return
     */
    User findSimpleUserInfoByUsername(String username);



}
