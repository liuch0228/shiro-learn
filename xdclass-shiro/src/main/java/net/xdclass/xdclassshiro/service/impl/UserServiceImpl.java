package net.xdclass.xdclassshiro.service.impl;

import net.xdclass.xdclassshiro.dao.RoleMapper;
import net.xdclass.xdclassshiro.dao.UserMapper;
import net.xdclass.xdclassshiro.domain.Role;
import net.xdclass.xdclassshiro.domain.User;
import net.xdclass.xdclassshiro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public User findAllUserInfoByUsername(String username) {
        User user = userMapper.findByUserName(username);
        List<Role> roleList = roleMapper.findRoleListByUserId(user.getId());
        user.setRoleList(roleList);
        return user;
    }

    @Override
    public User findSimpleUserInfoById(int userId) {

        return userMapper.findById(userId);
    }

    @Override
    public User findSimpleUserInfoByUsername(String username) {
        return userMapper.findByUserName(username);
    }
}
