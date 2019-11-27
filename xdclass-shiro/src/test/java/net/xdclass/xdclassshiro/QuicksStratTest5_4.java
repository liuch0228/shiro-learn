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
/**实际调用的是org.apache.shiro.subject.support.DelegatingSubject.login方法
 * ==》Subject subject = this.securityManager.login(this, token);
 * ==》 上面这个方法又调用了DefaultSecurityManager的login方法
 * org.apache.shiro.mgt.DefaultSecurityManager#login(org.apache.shiro.subject.Subject, org.apache.shiro.authc.AuthenticationToken)
 * ==》 info = this.authenticate(token);
 * ==》 实际上调用的是
 * org.apache.shiro.authc.AbstractAuthenticator#authenticate(org.apache.shiro.authc.AuthenticationToken)
 * 方法里面： info = this.doAuthenticate(token);
 * ==> org.apache.shiro.authc.pam.ModularRealmAuthenticator#doAuthenticate(org.apache.shiro.authc.AuthenticationToken)
 *  this.assertRealmsConfigured();
 *         Collection<Realm> realms = this.getRealms();
 *         return realms.size() == 1 ? this.doSingleRealmAuthentication((Realm)realms.iterator().next(), authenticationToken) : this.doMultiRealmAuthentication(realms, authenticationToken);
 * 一般只配置一个realm,因此看doSingleRealmAuthentication这个方法：
 *    protected AuthenticationInfo doSingleRealmAuthentication(Realm realm, AuthenticationToken token) {
 *         if (!realm.supports(token)) {
 *             String msg = "Realm [" + realm + "] does not support authentication token [" + token + "].  Please ensure that the appropriate Realm implementation is " + "configured correctly or that the realm accepts AuthenticationTokens of this type.";
 *             throw new UnsupportedTokenException(msg);
 *         } else {
 *         // 这里实际上调用的就是自定义 realm重写了的getAuthenticationInfo方法，如果返回null,抛出UnknownAccountException，认证不通过
 *             AuthenticationInfo info = realm.getAuthenticationInfo(token);
 *             if (info == null) {
 *                 String msg = "Realm [" + realm + "] was unable to find account data for the " + "submitted AuthenticationToken [" + token + "].";
 *                 throw new UnknownAccountException(msg);
 *             } else {
 *                 return info;
 *             }
 *         }
 *     }
 *      AuthenticationInfo info = realm.getAuthenticationInfo(token); 调用了：
 *  org.apache.shiro.realm.AuthenticatingRealm#getAuthenticationInfo(org.apache.shiro.authc.AuthenticationToken)
 *  这个方法里面，调用了一行：  info = this.doGetAuthenticationInfo(token);
 *  查找其实现类，就是我们自定义realm重写的的doGetAuthenticationInfo方法
 * **/

        subject.login(usernamePasswordToken);
        System.out.println("认证 结果：" + subject.isAuthenticated());
        System.out.println("getPrincipal()=" + subject.getPrincipal());

        System.out.println("调用权限校验:subject.hasRole方法会调用自定义realm重写的doGetAuthorizationInfo方法");
        System.out.println("用户jack是否有root角色:" + subject.hasRole("root"));  //false
        // 权限校验
        subject.checkRole("role1");
        System.out.println("用户jack是否有role1角色:" + subject.hasRole("role1")); //true
        System.out.println("用户jack是否有video:find权限:" + subject.isPermitted("video:find")); //true
    }


}
