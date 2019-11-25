package net.xdclass.xdclassshiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Before;
import org.junit.Test;

/**
 * 自定义Realm操作
 */
public class QuicksStratTest5_4 {

    private CustomRealm customRealm = new CustomRealm();
    private DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
    @Before
    public void inint(){
        //构建环境
        defaultSecurityManager.setRealm(customRealm);
        SecurityUtils.setSecurityManager(defaultSecurityManager);
    }

    @Test
    public void testAuthentication() {
        //获取当前操作的主体
        Subject subject = SecurityUtils.getSubject();
        //模拟用户输入账号密码
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken("jack", "123");

        subject.login(usernamePasswordToken);
        System.out.println("认证 结果：" + subject.isAuthenticated());
        System.out.println("getPrincipal()=" + subject.getPrincipal());

        System.out.println("调用权限校验:subject.hasRole方法会调用自定义realm重写的doGetAuthorizationInfo方法");
        System.out.println("用户jack是否有root角色:" + subject.hasRole("root"));  //false
        System.out.println("用户jack是否有role1角色:" + subject.hasRole("role1")); //true
        System.out.println("用户jack是否有video:find权限:" + subject.isPermitted("video:find")); //true
    }


}
