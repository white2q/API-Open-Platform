package com.ppf.apiinterface.controller;

import com.ppf.apiclientsdk.model.User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author panpengfei
 * @date 2023/8/15
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/get")
    public String getNameByGET(String name) {
        System.out.println("你的名字是：" + name);
        return name;
    }

    @PostMapping("/post")
    public String getNameByPOST(@RequestParam String name) {
        System.out.println("你的名字是：" + name);
        return name;
    }

    @PostMapping("/user")
    public String getUserNameByPOST(@RequestBody User user, HttpServletRequest request) {
        String result = "您的名字是：" + user.getName();
        return result;
    }
}
