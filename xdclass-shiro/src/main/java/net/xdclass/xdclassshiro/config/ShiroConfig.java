package net.xdclass.xdclassshiro.config;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {


    /**
     * @param securityManager
     * @return
     * @Bean是一个方法级别上的注解，主要用在@Configuration注解的类里，也可以用在@Component注解的类里。添加的bean的id为方法名 等同于xml配置中的<bean id = " shiroFilter " class = " org.apache.shiro.spring.web.ShiroFilterFactoryBean " />
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        System.out.println("ShiroFilterFactoryBean.shiroFilter");
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 设置securityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // 如果访问配置的这个接口时用户还未登录，则调用该接口登录
        shiroFilterFactoryBean.setLoginUrl("/pub/need_login");
        // 登录成功后重定向url 前后端分离的，没有这个调用
        shiroFilterFactoryBean.setSuccessUrl("/");
        // 没有页面访问权限（登录了，未授权），调用该接口
        shiroFilterFactoryBean.setUnauthorizedUrl("/pub/not_permit");
        // 注意，这里必须使用有序的LinkedHashMap，过滤器链是从上往下顺序执行，一般将/**放在最后，否则部分路径无法拦截
        Map<String,String> filterChainDefinitionMap = new LinkedHashMap<>();
        // 登出过滤器
        filterChainDefinitionMap.put("/logout", "logout");
        // 匿名访问过滤器(游客访问)
        filterChainDefinitionMap.put("/pub/**", "anon");
        // 登陆用户访问
        filterChainDefinitionMap.put("/authc/**", "authc");
        // 管理员角色才能访问
        filterChainDefinitionMap.put("/admin/**", "roles[admin]");
        // 有编辑权限才可以访问 /video/update是数据库配置的权限路径
        filterChainDefinitionMap.put("/video/update", "perms[video_update]");
        // authc： 定义的url必须通过认证才可以访问 anon: url可以匿名访问
        filterChainDefinitionMap.put("/**", "authc");
        return shiroFilterFactoryBean;


    }

    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 错误写法：securityManager.setRealm(new CustomRealm());
        // 必须通过spring实例化CustomRealm的实例之后，这里再通过customRealm()进行注入
        securityManager.setRealm(customRealm());
        securityManager.setSessionManager(sessionManager());
        return securityManager;
    }

    @Bean
    public CustomRealm customRealm(){
        CustomRealm customRealm = new CustomRealm();
        customRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return customRealm;
    }

    @Bean
    public SessionManager sessionManager(){
        CustomSessionManager sessionManager = new CustomSessionManager();
        return sessionManager;
    }

    /**
     *
     * @return
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher(){
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        // 设置散列算法,进行加解密
        credentialsMatcher.setHashAlgorithmName("md5");
        // 设置散列迭代次数，2 表示 hash之后再次hash
        credentialsMatcher.setHashIterations(2);
        return credentialsMatcher;
    }
}
