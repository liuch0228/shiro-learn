package net.xdclass.xdclassshiro.config;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *  ShiroFilterFactoryBean配置，要加@Configuration注解
 *
 */
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

        // 设置自定义Filter
        Map<String, Filter> filterMap = new LinkedHashMap<>();
        filterMap.put("roleOrFilter", new CustomRolesAuthorizationFilter());
        // 绑定
        shiroFilterFactoryBean.setFilters(filterMap);

        // 注意，这里必须使用有序的LinkedHashMap，过滤器链是从上往下顺序执行，一般将/**放在最后，否则部分路径无法拦截
        Map<String,String> filterChainDefinitionMap = new LinkedHashMap<>();
        // 登出过滤器
        filterChainDefinitionMap.put("/logout", "logout");
        // 匿名访问过滤器(游客访问)
        filterChainDefinitionMap.put("/pub/**", "anon");
        // 登陆用户访问
        filterChainDefinitionMap.put("/authc/**", "authc");
        // 设置自定义Filter,只要是管理员角色或者root角色就能访问
        filterChainDefinitionMap.put("/admin/**", "roleOrFilter[admin,root]");
        // 管理员角色才能访问
//        filterChainDefinitionMap.put("/admin/**", "roles[admin]");

        // 有编辑权限才可以访问 /video/update是数据库配置的权限路径
        filterChainDefinitionMap.put("/video/update", "perms[video_update]");
        // authc： 定义的url必须通过认证才可以访问 anon: url可以匿名访问
        filterChainDefinitionMap.put("/**", "authc");
         // 这里没设置，过滤器不生效！！！
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;


    }

    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 错误写法：securityManager.setRealm(new CustomRealm());
        // 不是前后端分离项目，这里可以不用设置sessionmanager
        securityManager.setSessionManager(sessionManager());

        // 使用自定义的cacheManager
        securityManager.setCacheManager(cacheManager());

        // 必须通过spring实例化CustomRealm的实例之后，这里再通过customRealm()进行注入
        securityManager.setRealm(customRealm());
        return securityManager;
    }

    /**
     * 注入realm
     * @return
     */
    @Bean
    public CustomRealm customRealm(){
        CustomRealm customRealm = new CustomRealm();
        // 设置密码验证器  使用明文密码进行登录测试时，需要将这里注释掉，或者在数据库存储new SimpleHash("md5",pwd,null,2)加密过的密码
        //数据库修改二当家小D的密码为4280d89a5a03f812751f504cc10ee8a5,这里密码验证注释放开，使用明文密码访问http://localhost:8080/pub/login,能够登录成功
        customRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return customRealm;
    }

    /**
     * 注入自定义sessionManager
     * @return
     */
    @Bean
    public SessionManager sessionManager(){
        CustomSessionManager sessionManager = new CustomSessionManager();
        // 设置session过期时间,不设置默认是30分钟,单位ms
        sessionManager.setGlobalSessionTimeout(200000);
        // 设置session持久化到redis中,这样服务器重启后，用户还可以通过之前的session进行操作
        sessionManager.setSessionDAO(redisSessionDAO());
        return sessionManager;
    }

    /**
     * 密码加解密规则
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


    /**
     * 配置redisManager
     * @return
     */
    public RedisManager getRedisManager(){
        RedisManager redisManager = new RedisManager();
//        redisManager.setHost("192.168.5.112");
        redisManager.setHost("192.168.0.114");
        redisManager.setPort(8007);
        return redisManager;
    }

    /**
     * 配置缓存管理器具体实习类，然后添加到securityManager里面
     * @return
     */
    public RedisCacheManager cacheManager(){
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(getRedisManager());
        // 设置缓存过期时间，单位秒
        redisCacheManager.setExpire(1800);
        return redisCacheManager;
    }

    /**
     * 1.通过shiro redis插件自定义session持久化
     * 2.在shiro的sessionManager中配置session持久化
     * @return
     */
    public RedisSessionDAO redisSessionDAO(){
        RedisSessionDAO redisSessionDao = new RedisSessionDAO();
        redisSessionDao.setRedisManager(getRedisManager());
        // session持久化的过期时间，单位s,如果不设置，默认使用session的过期时间，如果设置了，则使用这里设置的过期时间
        redisSessionDao.setExpire(1800);
        return redisSessionDao;
    }
}
