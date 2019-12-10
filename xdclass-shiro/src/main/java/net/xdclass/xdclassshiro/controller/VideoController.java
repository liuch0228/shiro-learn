package net.xdclass.xdclassshiro.controller;

import net.xdclass.xdclassshiro.domain.JsonData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 *  只有具有video/update权限才能访问的接口
 *  select u.username,u.password,ur.remarks,r.name,r.description,rp.role_id,p.name from user u
 * left join user_role ur on u.id = ur.user_id
 * left join role r on r.id = ur.role_id
 * left join role_permission rp on rp.role_id = r.id
 * left join permission p on p.id = rp.permission_id
 * where  p.id = 1
 * 查询出来的用户才能访问该接口
 */
@RestController
@RequestMapping("video")
public class VideoController {
    @PostMapping("/update")
    public JsonData findPlayRecord(){
        return JsonData.buildSuccess("video更新成功");
    }
}
