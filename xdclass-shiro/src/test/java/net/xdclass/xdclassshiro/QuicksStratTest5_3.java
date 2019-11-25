package net.xdclass.xdclassshiro;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Test;

/**
 * jdbcRealm操作
 */
public class QuicksStratTest5_3 {

    @Test
    public void testAuthentication() {
        //通过配置文件创建SecurityManager工厂
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:jdbcrealm.ini");
       // 获取SecurityManager实例
        SecurityManager securityManager = factory.getInstance();
        //设置当前上下文
        SecurityUtils.setSecurityManager(securityManager);

        //获取当前subject(application应用的user)
        Subject subject = SecurityUtils.getSubject();
        // 模拟用户输入
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken("jack","123");
        //
        subject.login(usernamePasswordToken);
        System.out.println("认证结果(是否已授权)：" + subject.isAuthenticated());
        //最终调用的是org.apache.shiro.authz.ModularRealmAuthorizer.hasRole方法
        System.out.println("是否有role1角色：" + subject.hasRole("role1"));
        System.out.println("是否有role2角色：" + subject.hasRole("role2"));
        System.out.println("是否有root角色：" + subject.hasRole("root"));
        //获取登录 账号
        System.out.println("getPrincipal():" + subject.getPrincipal());
        //校验角色，没有返回值，校验不通过，直接跑出异常
        subject.checkRole("role1");
        System.out.println("=======subject.checkRole(\"role1\") passed=====" );
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

       /* org.apache.shiro.realm.jdbc.JdbcRealm源码
       * 1. class JdbcRealm extends AuthorizingRealm
       * 2. 预置了默认的查询语句,因此创建数据库时字段名字要与这里定义的一致！！！
       *   protected static final String DEFAULT_AUTHENTICATION_QUERY = "select password from users where username = ?";
    protected static final String DEFAULT_SALTED_AUTHENTICATION_QUERY = "select password, password_salt from users where username = ?";
    #根据用户名称查角色
    protected static final String DEFAULT_USER_ROLES_QUERY = "select role_name from user_roles where username = ?";
    *  #根据用户名称查权限
    protected static final String DEFAULT_PERMISSIONS_QUERY = "select permission from roles_permissions where role_name = ?";
    protected String authenticationQuery = "select password from users where username = ?";
    protected String userRolesQuery = "select role_name from user_roles where username = ?";
    * #根据角色查询权限
    protected String permissionsQuery = "select permission from roles_permissions where role_name = ?";
    *
    * 3. protected boolean permissionsLookupEnabled = false;  这个开关默认是关闭的，需要手动打开
    * 4.
    * protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        if (principals == null) {
            throw new AuthorizationException("PrincipalCollection method argument cannot be null.");
        } else {
            String username = (String)this.getAvailablePrincipal(principals);
            Connection conn = null;
            Set<String> roleNames = null;
            Set permissions = null;

            try {
                conn = this.dataSource.getConnection();
                roleNames = this.getRoleNamesForUser(conn, username);
                if (this.permissionsLookupEnabled) {
                    permissions = this.getPermissions(conn, username, roleNames);
                }
            } catch (SQLException var11) {
                String message = "There was a SQL error while authorizing user [" + username + "]";
                if (log.isErrorEnabled()) {
                    log.error(message, var11);
                }

                throw new AuthorizationException(message, var11);
            } finally {
                JdbcUtils.closeConnection(conn);
            }

            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roleNames);
            info.setStringPermissions(permissions);
            return info;
        }
        * */
    }

    @Test
    public void test2(){
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        DruidDataSource ds = new DruidDataSource();
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setUrl("jdbc:mysql://localhost:3306/xdclass_shiro?characterEncoding=UTF-8&serverTimezone=UTC&useSSL=false");
        ds.setUsername("root");
        ds.setPassword("lchadmin");
        JdbcRealm jdbcRealm  = new JdbcRealm();
        jdbcRealm.setPermissionsLookupEnabled(true);
        jdbcRealm.setDataSource(ds);
        securityManager.setRealm(jdbcRealm);
        // 将securityManager设置到当前运行环境中
        SecurityUtils.setSecurityManager(securityManager);
        //获取当前subject(application应用的user)
        Subject subject = SecurityUtils.getSubject();
        // 模拟用户输入
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken("jack","123");
        //
        subject.login(usernamePasswordToken);
        System.out.println("认证结果(是否已授权)：" + subject.isAuthenticated());
        //最终调用的是org.apache.shiro.authz.ModularRealmAuthorizer.hasRole方法
        System.out.println("是否有role1角色：" + subject.hasRole("role1"));
        System.out.println("是否有role2角色：" + subject.hasRole("role2"));
        System.out.println("是否有root角色：" + subject.hasRole("root"));
        //获取登录 账号
        System.out.println("getPrincipal():" + subject.getPrincipal());
        //校验角色，没有返回值，校验不通过，直接跑出异常
        subject.checkRole("role1");
        System.out.println("=======subject.checkRole(\"role1\") passed=====" );
        // user jack有video的find权限，执行通过
        subject.checkPermission("video:find");
        // 是否有video:find权限：true
        System.out.println("是否有video:find权限：" + subject.isPermitted("video:find"));
        //   是否有video:delete权限：false
        System.out.println("是否有video:delete权限：" + subject.isPermitted("video:delete"));
        //user jack没有video的删除权限，执行会报错：org.apache.shiro.authz.UnauthorizedException: Subject does not have permission [video:delete]
        subject.checkPermission("video:delete");


    }


}
