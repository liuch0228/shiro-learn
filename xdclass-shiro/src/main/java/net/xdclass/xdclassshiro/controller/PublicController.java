package net.xdclass.xdclassshiro.controller;

import net.xdclass.xdclassshiro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pub")
public class PublicController {

    @Autowired
    private UserService userService;
//http://localhost:8080/pub/find_user_info?username=jack
    @GetMapping("/find_user_info")
    public Object queryUserInfo(@RequestParam("username")String username){
        return  userService.findAllUserInfoByUsername(username);
    }
}
