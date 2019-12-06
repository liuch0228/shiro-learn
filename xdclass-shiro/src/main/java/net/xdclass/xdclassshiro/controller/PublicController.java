package net.xdclass.xdclassshiro.controller;

import net.xdclass.xdclassshiro.domain.JsonData;
import net.xdclass.xdclassshiro.domain.UserQuery;
import net.xdclass.xdclassshiro.service.UserService;
import org.apache.commons.collections.MapUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.ToLongBiFunction;

@RestController
@RequestMapping("/pub")
public class PublicController {

    @Autowired
    private UserService userService;

    //http://localhost:8080/pub/find_user_info?username=jack
    @GetMapping("/find_user_info")
    public Object queryUserInfo(@RequestParam("username") String username) {
        return userService.findAllUserInfoByUsername(username);
    }

    @RequestMapping("/need_login")
    public JsonData needLogin(){
        return JsonData.buildError("请登录",-2);
    }

// shiroconfig配置类里面配置的路径
    @RequestMapping("/not_permit")
    public JsonData noPermit(){
        return JsonData.buildError("没有权限访问",-3);
    }

    //首页,任何角色的都可以访问
    @RequestMapping("/index")
    public JsonData index(){
        List<String> videoList = new ArrayList<>();
        videoList.add("Mysql零基础入门到实战 数据库教程");
        videoList.add("Redis高并发高可用集群百万级秒杀实战");
        videoList.add("Zookeeper+Dubbo视频教程 微服务教程分布式教程");
        videoList.add("2019年新版本RocketMQ4.X教程消息队列教程");
        videoList.add("微服务SpringCloud+Docker入门到高级实战");
        return JsonData.buildSuccess(videoList);
    }


    /**
     * 登录接口
     * @param userQuery
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/login")
    public JsonData login(@RequestBody UserQuery userQuery, HttpServletRequest request,
                          HttpServletResponse response){
        Map<String,Object> info = new HashMap<>();
        try {
            Subject subject = SecurityUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(userQuery.getName(),userQuery.getPwd());
            subject.login(token);

            info.put("msg", "登录成功");
            info.put("session_id",subject.getSession().getId());
            return JsonData.buildSuccess(info);
        } catch (Exception e){
            e.printStackTrace();
            return JsonData.buildError("登录失败");
        }




    }



}
