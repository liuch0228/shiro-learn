package net.xdclass.xdclassshiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public class QuicksStratTest5_2 {

    @Test
    public void testAuthentication() {
        //通过配置文件创建SecurityManager工厂
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
       // 获取SecurityManager实例
        SecurityManager securityManager = factory.getInstance();
        //设置当前上下文
        SecurityUtils.setSecurityManager(securityManager);

        //获取当前subject(application应用的user)
        Subject subject = SecurityUtils.getSubject();
        // 模拟用户输入
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken("jack","456");
        //
        subject.login(usernamePasswordToken);
        System.out.println("认证结果(是否已授权)：" + subject.isAuthenticated());
        //最终调用的是org.apache.shiro.authz.ModularRealmAuthorizer.hasRole方法
        System.out.println("是否有对应的角色：" + subject.hasRole("root"));
        //获取登录 账号
        System.out.println("getPrincipal():" + subject.getPrincipal());
        //校验角色，没有返回值，校验不通过，直接跑出异常
        subject.checkRole("user");
        // user jack有video的find权限，执行通过
        subject.checkPermission("video:find");
        // 是否有video:find权限：true
        System.out.println("是否有video:find权限：" + subject.isPermitted("video:find"));
        //   是否有video:delete权限：false
        System.out.println("是否有video:delete权限：" + subject.isPermitted("video:delete"));
        //user jack没有video的删除权限，执行会报错：org.apache.shiro.authz.UnauthorizedException: Subject does not have permission [video:delete]
        subject.checkPermission("video:delete");
        subject.logout();
        System.out.println("logout后认证结果：" + subject.isAuthenticated());
    }

    @Test
    public void testAuthentication2() {
        //通过配置文件创建SecurityManager工厂
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        // 获取SecurityManager实例
        SecurityManager securityManager = factory.getInstance();
        //设置当前上下文
        SecurityUtils.setSecurityManager(securityManager);

        //获取当前subject(application应用的user)
        Subject subject = SecurityUtils.getSubject();
        // 模拟用户输入
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken("xdclass","123");
        //
        subject.login(usernamePasswordToken);
        System.out.println("认证结果(是否已授权)：" + subject.isAuthenticated());
        //最终调用的是org.apache.shiro.authz.ModularRealmAuthorizer.hasRole方法
        System.out.println("是否有admin角色：" + subject.hasRole("admin"));
        System.out.println("是否有root角色：" + subject.hasRole("root"));
        //获取登录 账号
        System.out.println("getPrincipal():" + subject.getPrincipal());
        // admin角色具有所有权限
        subject.checkPermission("video:find");
        // 是否有video:find权限：true
        System.out.println("是否有video:find权限：" + subject.isPermitted("video:find"));
        //   是否有video:delete权限：true
        System.out.println("是否有video:delete权限：" + subject.isPermitted("video:delete"));
        // 结果为true
        subject.checkPermission("video:delete");


    }
}
