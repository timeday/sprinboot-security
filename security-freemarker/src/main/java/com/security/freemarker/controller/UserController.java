package com.security.freemarker.controller;

import com.security.freemarker.dto.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {


    @GetMapping
    public List<User> query() {
        List<User> users = new ArrayList<User>();
        users.add(new User("hdd","123456","1",new Date()));
        return users;
    }

    @PostMapping("/regist")
    public void regist(User user, HttpServletRequest request) {
        //不管是注册用户还是绑定用户，都会拿到一个用户唯一标示。
        String userId = user.getUsername();

    }

}
