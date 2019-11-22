package net.xdclass.xdclassshiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;

/**
 * shiro授权过程及常用API：
 * 1.构造SecurityManager环境
 * 2.Subject执行授权
 * 3.SecurityManager进行认证授权
 * 4.Authenticator执行授权
 * 5.根据realm进行授权验证
 */
public class QuicksStratTest2 {

    /**
     * accountRealm 相当于数据库的作用
     */
    private SimpleAccountRealm accountRealm = new SimpleAccountRealm();

    private DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();

    @Before
    public void init() {
        //初始化数据源,入参加入用户角色
        accountRealm.addAccount("lch", "123","root","admin");
        accountRealm.addAccount("jack", "345","user");
        //构建环境
        defaultSecurityManager.setRealm(accountRealm);

    }

    @Test
    public void testAuthentication() {
        //设置当前上下文
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        //设置当前subject(application应用的user)
        Subject subject = SecurityUtils.getSubject();
        //模拟用户输入
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken("lch", "123");
        // 实际是调用securityManager的login方法 Subject subject = this.securityManager.login(this, token);
        subject.login(usernamePasswordToken);

        System.out.println("认证结果(是否已授权)：" + subject.isAuthenticated());
        //最终调用的是org.apache.shiro.authz.ModularRealmAuthorizer.hasRole方法
        System.out.println("是否有对应的角色：" + subject.hasRole("root"));
        //获取登录 账号
        System.out.println("getPrincipal():" + subject.getPrincipal());
        subject.logout();
        System.out.println("logout后认证结果：" + subject.isAuthenticated());


    }
}
