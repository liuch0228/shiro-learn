package net.xdclass.xdclassshiro.config;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.Set;

/**
 * 1.自定义filter
 * 2. 把自定义filter加入到ShiroConfig中去
 */
public class CustomRolesAuthorizationFilter extends AuthorizationFilter {
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        Subject subject = this.getSubject(request, response);
        String[] rolesArray = (String[]) ((String[]) mappedValue);
        if (rolesArray != null && rolesArray.length != 0) {
            Set<String> roles = CollectionUtils.asSet(rolesArray);
            /*RolesAuthorizationFilter 中是return subject.hasAllRoles(roles)，同时具备所有角色才能够访问
            这里改为遍历角色 ，当前subject是roles中的任意一个，则有权限访问
            * */
            for (String role : roles) {
                if (subject.hasRole(role)) {
                    return true;
                }
            }
            // return subject.hasAllRoles(roles);
        }
        // 没有角色限制，可以直接访问
        return true;
    }
}
