package net.xdclass.xdclassshiro.config;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;

/**
 * 自定义sessionManager
 * 继承DefaultWebSessionManager并重写该类的getSessionId(javax.servlet.ServletRequest, javax.servlet.ServletResponse)方法
 * 意义：
 * 前后端分离时，用户访问接口时，接口生成一个token保存到map中，
 * 下次用户访问接口时带上这个token，后端进行校验
 */
public class CustomSessionManager extends DefaultWebSessionManager {

    /**
     * 请求头中token的key名
     */
    private static  final String AUTHORIZATION = "token";

    public CustomSessionManager() {
        super();
    }

    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        String sessionId = WebUtils.toHttp(request).getHeader(AUTHORIZATION);
        if(StringUtils.isBlank(sessionId)){
            return super.getSessionId(request,response);
        } else {
//org.apache.shiro.web.session.mgt.DefaultWebSessionManager.getReferencedSessionId id不为null时的分支处理
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE,  ShiroHttpServletRequest.COOKIE_SESSION_ID_SOURCE);
            // 校验sessionId是否有效
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, sessionId);
            // automatically mark it valid here,if it is invalid, the onUnknowSesson method
            // below will be invoked and we`ll remove the attribute at that time.
            // 标记当前sessionId有效
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
            return sessionId;
        }


    }
}
