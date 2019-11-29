package net.xdclass.xdclassshiro.config;

import net.xdclass.xdclassshiro.domain.Permission;
import net.xdclass.xdclassshiro.domain.Role;
import net.xdclass.xdclassshiro.domain.User;
import net.xdclass.xdclassshiro.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 自定义realm,继承AuthorizingRealm，重写认证，授权的方法
 */
public class CustomRealm extends AuthorizingRealm {
    @Autowired
    private UserService userService;

    /**
     * 进行授权校验
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String username = (String) principalCollection.getPrimaryPrincipal();
        User user = userService.findAllUserInfoByUsername(username);
        List<String> roleList = Collections.emptyList();
        List<String> permissionList = Collections.emptyList();
        List<Role> userRoleList = user.getRoleList();
        // 把用户的角色，权限放到对应的list中
        userRoleList.forEach(role ->{
            roleList.add(role.getName());
            for (Permission p: role.getPermissionList()){
                if(null != p.getName()){
                  permissionList.add(p.getName());
                }
            }
        });
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addRoles(roleList);
        simpleAuthorizationInfo.addStringPermissions(permissionList);
        // 返回simpleAuthorizationInfo对象，交给shiro框架去做权限校验
        return simpleAuthorizationInfo;
    }

    /**
     * 自定义shiro认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //从token中获取用户信息，token代表用户输入
        String username = (String) authenticationToken.getPrincipal();
        User user = userService.findAllUserInfoByUsername(username);

        String pwd = user.getPassword();
        if(StringUtils.isBlank(pwd)){
            return null;
        }
//org.apache.shiro.authc.SimpleAuthenticationInfo.SimpleAuthenticationInfo(java.lang.Object, java.lang.Object, java.lang.String)
        return new SimpleAuthenticationInfo(username,pwd,this.getClass().getName());
    }
}
