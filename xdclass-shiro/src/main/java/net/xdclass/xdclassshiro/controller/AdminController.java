package net.xdclass.xdclassshiro.controller;

import net.xdclass.xdclassshiro.domain.JsonData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 *  只有admin管理员角色才能访问的接口
 */
@RestController
@RequestMapping("admin")
public class AdminController {

    /**
     *  视频播放列表接口
     * @return
     */
    @PostMapping("/video/order")
    public JsonData findPlayRecord(){
        Map<String ,String> recordMap = new HashMap<>();
        recordMap.put("SpringBoot入门到高级实战","300元");
        recordMap.put("Cloud微服务入门到高级实战","100元");
        recordMap.put("分布式缓存Redis","90元");
        return JsonData.buildSuccess(recordMap);
    }

}
