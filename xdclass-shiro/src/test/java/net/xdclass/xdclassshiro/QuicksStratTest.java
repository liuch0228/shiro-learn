package net.xdclass.xdclassshiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;

/**
 * shiro认证过程：
 * 1.构造SecurityManager环境
 * 2.调用Subject.login()执行认证
 * 3.SecurityManager进行认证
 * 4.Authenticator执行 认证
 * 5.根据realm进行验证
 */
public class QuicksStratTest {

    /**
     * accountRealm 相当于数据库的作用
     */
    private SimpleAccountRealm accountRealm = new SimpleAccountRealm();

    private DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();

    @Before
    public void init() {
        //初始化数据源
        accountRealm.addAccount("lch", "123");
        accountRealm.addAccount("jack", "345");
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


    }
}
