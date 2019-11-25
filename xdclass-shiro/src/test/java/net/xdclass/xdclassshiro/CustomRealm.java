package net.xdclass.xdclassshiro;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 自定义realm:继承AuthorizingRealm，重写2个抽象方法
 */
public class CustomRealm extends AuthorizingRealm {

    private final Map<String, String> userInfoMap = new HashMap<>();
    {
        userInfoMap.put("jack", "123");
        userInfoMap.put("xdclass", "456");
    }

    //role->permission 模拟数据库角色与权限的关系
    private final Map<String, Set<String>> permissonMap = new HashMap<>();
    {
        Set<String> set1 = new HashSet<>();
        Set<String> set2 = new HashSet<>();
        set1.add("video:find");
        set1.add("video:buy");
        set2.add("video:add");
        set2.add("video:delete");
        permissonMap.put("jack", set1);
        permissonMap.put("xdclass", set2);
    }

    //user->role 模拟数据库角色与权限的关系
    private final Map<String, Set<String>> roleMap = new HashMap<>();
    {
        Set<String> set1 = new HashSet<>();
        Set<String> set2 = new HashSet<>();
        set1.add("role1");
        set1.add("role2");
        set2.add("root");
        roleMap.put("jack", set1);
        roleMap.put("xdclass", set2);
    }


    //校验权限时会调用这个方法
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("权限：doGetAuthorizationInfo");
        //获取用户主账户名
        String name = (String) principalCollection.getPrimaryPrincipal();
        //通过名称查找权限，简化操作（正常是通过名称找角色，通过角色查询对应的权限）
        Set<String> permissions = getPermissonsByNameFromDB(name);
        Set<String> roles = getRolesByNameFromDB(name);

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setRoles(roles);
        simpleAuthorizationInfo.setStringPermissions(permissions);
        return simpleAuthorizationInfo;
    }

    private Set<String> getRolesByNameFromDB(String name) {
        return roleMap.get(name);
    }

    private Set<String> getPermissonsByNameFromDB(String name) {
        return permissonMap.get(name);
    }

    //用户登录的时候会调用该方法
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("认证： doGetAuthenticationInfo");
        //从token中获取身份信息，token代表用户输入的信息
        String name = (String) authenticationToken.getPrincipal();
        //模拟从数据库读密码
        String pwd = getPwdByUserNameFromDB(name);
        if (StringUtils.isBlank(pwd)) {
            return null; //匹配失败
        }
        /*useNmaePasswordToken中有用户的Principal和Credential
        SimpleAuthorizationInfo代表用户的橘色权限信息
        SimpleAuthenticationInfo 代表该用户的认证信息*/
        // this.getName()是获取CachingRealm的名字
        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(name, pwd, this.getName());

        return simpleAuthenticationInfo;
    }

    private String getPwdByUserNameFromDB(String name) {
        return userInfoMap.get(name);
    }
}
