package net.xdclass.xdclassshiro.controller;

import net.xdclass.xdclassshiro.domain.JsonData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 *  登录才能访问的接口，使用@RestController，返回json数据到前端
 */
@RestController
@RequestMapping("authc")
public class OrderController {

    /**
     *  视频播放列表接口
     * @return
     */
    @PostMapping("/video/play_record")
    public JsonData findPlayRecord(){
        Map<String ,String> recordMap = new HashMap<>();
        recordMap.put("SpringBoot入门到高级实战","第8章第1集");
        recordMap.put("Cloud微服务入门到高级实战","第4章第10集");
        recordMap.put("分布式缓存Redis","第10章第3集");
        return JsonData.buildSuccess(recordMap);
    }

}
